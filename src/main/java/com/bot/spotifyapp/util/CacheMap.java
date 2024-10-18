package com.bot.spotifyapp.util;

import java.util.concurrent.ConcurrentHashMap;

public class CacheMap {

    private ConcurrentHashMap<String, String> cacheMap;

    private CacheMap() {
        super();
        cacheMap = new ConcurrentHashMap<>();
    }

    private static class InstanceHolder {

        private static final CacheMap instance = new CacheMap();
    }

    public static CacheMap getInstance() {
        return InstanceHolder.instance;
    }

    public void addToCache(String key, String value) {
        cacheMap.put(key, value);
    }

    public String getFromCache(String key) {
        return cacheMap.get(key);
    }

    public String removeFromCache(String key) {
        return cacheMap.remove(key);
    }
}
