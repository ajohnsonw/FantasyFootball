package com.wj.server;

import java.net.URL;
import java.util.HashMap;

/**
 * Class for caching content of websites. Cache is storing in memory.
 */
public class MemoryWebCache extends WebCache
{
    private HashMap<String, String> webContentMap;

    public MemoryWebCache()
    {
        this.webContentMap = new HashMap<>();
    }

    @Override
    protected synchronized void setWebContentToCache(URL url, String webContent)
    {
        this.webContentMap.put(url.toString(), webContent);
    }

    @Override
    protected synchronized String getWebContentFromCache(URL url)
    {
        return this.webContentMap.get(url.toString());
    }
}
