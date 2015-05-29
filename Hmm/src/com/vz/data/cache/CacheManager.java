package com.vz.data.cache;

public interface CacheManager {

	LexiconCache getCache(String alpString);

	void init();

}
