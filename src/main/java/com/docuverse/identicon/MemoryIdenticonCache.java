package com.docuverse.identicon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

public class MemoryIdenticonCache implements IdenticonCache {
	private static final Log log = LogFactory.getLog(MemoryIdenticonCache.class);

	private Cache cache;

	public MemoryIdenticonCache() {
		try {
			this.cache = CacheManager.getInstance().getCache("identicon");
		} catch (CacheException e) {
			MemoryIdenticonCache.log.error(e);
		}
	}

	@Override
	public byte[] get(String key) {
		if (this.cache != null) {
			return (byte[]) this.cache.retrieve(key);
		} else {
			return null;
		}
	}

	@Override
	public void add(String key, byte[] imageData) {
		if (this.cache != null) {
			this.cache.store(key, imageData);
		}
	}

	@Override
	public void remove(String key) {
		if (this.cache != null) {
			this.cache.remove(key);
		}
	}

	@Override
	public void removeAll() {
		if (this.cache != null) {
			this.cache.clear();
		}
	}

}
