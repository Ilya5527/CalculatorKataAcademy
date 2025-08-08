package ru.ilya.myProject.config.cache;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ResilientCache implements Cache {
    private final String name;
    private final Cache igniteCache;
    private final Cache localCache;
    private final AtomicBoolean igniteHealthy;

    public ResilientCache(String name, Cache igniteCache, Cache localCache, AtomicBoolean igniteHealthy) {
        this.name = name;
        this.igniteCache = igniteCache;
        this.localCache = localCache;
        this.igniteHealthy = igniteHealthy;
    }

    @Override
    public String getName() { return name; }

    @Override
    public Object getNativeCache() { return igniteHealthy.get() ? igniteCache.getNativeCache() : localCache.getNativeCache(); }

    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        if (igniteHealthy.get()) {
            try { return igniteCache.get(key); } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        return localCache.get(key);
    }

    @Override
    @Nullable
    public <T> T get(Object key, @Nullable Class<T> type) {
        if (igniteHealthy.get()) {
            try { return igniteCache.get(key, type); } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        return localCache.get(key, type);
    }

    @Override
    public <T> T get(Object key, java.util.concurrent.Callable<T> valueLoader) {
        if (igniteHealthy.get()) {
            try { return igniteCache.get(key, valueLoader); } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        return localCache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        if (igniteHealthy.get()) {
            try { igniteCache.put(key, value); return; } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        localCache.put(key, value);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        if (igniteHealthy.get()) {
            try { return igniteCache.putIfAbsent(key, value); } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        return localCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        if (igniteHealthy.get()) {
            try { igniteCache.evict(key); return; } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        localCache.evict(key);
    }

    @Override
    public void clear() {
        if (igniteHealthy.get()) {
            try { igniteCache.clear(); return; } catch (RuntimeException ex) { igniteHealthy.set(false); }
        }
        localCache.clear();
    }
}