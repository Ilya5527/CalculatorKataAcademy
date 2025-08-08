package ru.ilya.myProject.config.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
@EnableConfigurationProperties(IgniteCacheProperties.class)
public class IgniteCacheConfig {
    private static final Logger log = LoggerFactory.getLogger(IgniteCacheConfig.class);

    @Bean
    @Primary
    public CacheManager cacheManager(IgniteCacheProperties props) {
        Ignite ignite = null;
        if (props.isEnabled()) {
            try {
                IgniteConfiguration cfg = new IgniteConfiguration();
                cfg.setClientMode(true);
                cfg.setIgniteInstanceName(props.getClusterName());
                cfg.setNetworkTimeout(props.getNetworkTimeoutMs());
                cfg.setFailureDetectionTimeout((long) props.getFailureDetectionTimeoutMs());

                TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
                TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
                List<String> addrs = props.getAddresses();
                if (!CollectionUtils.isEmpty(addrs)) {
                    ipFinder.setAddresses(addrs);
                }
                discoverySpi.setIpFinder(ipFinder);
                discoverySpi.setJoinTimeout(props.getDiscoveryJoinTimeoutMs());
                cfg.setDiscoverySpi(discoverySpi);

                List<String> cacheNames = props.getCacheNames();
                if (!CollectionUtils.isEmpty(cacheNames)) {
                    CacheConfiguration<?, ?>[] ccfgs = cacheNames.stream()
                            .map(name -> {
                                CacheConfiguration<Object, Object> c = new CacheConfiguration<>(name);
                                c.setCacheMode(CacheMode.PARTITIONED);
                                c.setBackups(1);
                                return c;
                            })
                            .toArray(CacheConfiguration[]::new);
                    cfg.setCacheConfiguration(ccfgs);
                }

                ignite = Ignition.start(cfg);
                log.info("Ignite client started. Instance: {}", ignite.name());
            } catch (IgniteException ex) {
                log.error("Failed to start Ignite, using NoOp cache. Reason: {}", ex.toString());
            }
        } else {
            log.warn("Ignite is disabled via configuration.");
        }

        if (ignite == null) {
            log.warn("Caching disabled (NoOpCacheManager). Service will always hit endpoints for fresh data.");
            return new NoOpCacheManager();
        }

        org.apache.ignite.cache.spring.IgniteSpringCacheManager igniteMgr = new org.apache.ignite.cache.spring.IgniteSpringCacheManager();
        igniteMgr.setIgniteInstanceName(ignite.name());
        if (!CollectionUtils.isEmpty(props.getCacheNames())) {
            igniteMgr.setCacheNames(props.getCacheNames().toArray(new String[0]));
        }
        return new ResilientCacheManager(igniteMgr, new NoOpCacheManager());
    }
}