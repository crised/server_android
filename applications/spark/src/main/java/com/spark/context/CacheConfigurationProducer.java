package com.spark.context;

import javax.enterprise.inject.Produces;

import org.infinispan.cdi.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;

/**
 * Produce cache configurations for different purpose.
 *
 * @author hantsy
 *
 */
public class CacheConfigurationProducer {
	private static final String CACHE_LOC="memeber-cache";

    @ConfigureCache("pending-cache")
    @PendingCache
    @Produces
    public Configuration pendingCacheConfiguration() {
        return new ConfigurationBuilder()
                .eviction()
                    .strategy(EvictionStrategy.LRU)
                    .maxEntries(10)
                .expiration()
                    .lifespan(24L * 60 * 60 * 1000)
                .loaders()
                    .shared(false)
                    .preload(true)
                    .passivation(false)
                .addFileCacheStore()
                     .location(CACHE_LOC)
                     .fetchPersistentState(true)
                     .purgeOnStartup(false)
                     .ignoreModifications(false)
                .build();
    }

  
}
