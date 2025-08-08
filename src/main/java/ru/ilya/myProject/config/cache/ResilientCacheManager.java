package ru.ilya.myProject.config.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResilientCacheManager implements CacheManager {
    private final CacheManager igniteManager;
    private final CacheManager localManager;
    private final AtomicBoolean igniteHealthy = new AtomicBoolean(true);
    private final Map<String, Cache> cacheByName = new ConcurrentHashMap<>();

    public ResilientCacheManager(CacheManager igniteManager, CacheManager localManager) {
        this.igniteManager = igniteManager;
        this.localManager = localManager;
    }

    @Override
    public Cache getCache(String name) {
        return cacheByName.computeIfAbsent(name, n -> {
            Cache igniteCache = null;
            try {
                igniteCache = igniteManager.getCache(n);
            } catch (RuntimeException ex) {
                igniteHealthy.set(false);
            }
            Cache localCache = localManager.getCache(n);
            if (localCache == null) {
                throw new IllegalStateException("Local cache manager did not provide cache: " + n);
            }
            if (igniteCache == null) {
                igniteHealthy.set(false);
                return new ResilientCache(n, localCache, localCache, igniteHealthy);
            }
            return new ResilientCache(n, igniteCache, localCache, igniteHealthy);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        try {
            Collection<String> igniteNames = igniteManager.getCacheNames();
            if (igniteNames != null && !igniteNames.isEmpty()) {
                return igniteNames;
            }
        } catch (RuntimeException ex) {
            igniteHealthy.set(false);
        }
        Collection<String> localNames = localManager.getCacheNames();
        return localNames != null ? localNames : Collections.emptyList();
    }
}