package edu.dongnao.study.cache.architecture.j2cache;

import java.io.IOException;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import net.oschina.j2cache.J2Cache;

/**
 * Java2LevelCache
 * 
 */
public class Java2LevelCache {
	
	public static void main(String[] args) throws IOException {
		CacheChannel cache = J2Cache.getChannel();
		// cache1表示region，region表示定义了缓存大小和过期时间的分组
		cache.set("myRegion","key","cacheValue");
		CacheObject cacheObj = cache.get("myRegion","key");
		System.out.println(cacheObj.getValue());
		cache.get("myRegion","key");
	}
}

