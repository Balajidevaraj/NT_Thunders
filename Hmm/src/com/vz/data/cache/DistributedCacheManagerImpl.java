package com.vz.data.cache;

import java.util.List;

public class DistributedCacheManagerImpl implements CacheManager {

	private CacheLoader cacheLoader = null;

	public DistributedCacheManagerImpl() {

		cacheLoader = new CacheLoaderFileSysImpl();
	}

	private List<LexiconCache> caches;

	public LexiconCache getCache(String alpString) {

		LexiconCache lexiconCache = null;

		for (LexiconCache cache : caches) {

			if (cache.getPositiveWordList().contains(alpString.toLowerCase())
					|| cache.getNegativeWordList().contains(
							alpString.toLowerCase())) {

				lexiconCache = cache;

			}

		}

		return lexiconCache;
	}

	public void init() {

		caches = cacheLoader.load();
	}

}
