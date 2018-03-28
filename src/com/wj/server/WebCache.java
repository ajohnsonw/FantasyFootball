package com.wj.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Abstract class for caching content of websites.
 *
 * For caching content you can use one of its descendant, for example {@link MemoryWebCache}
 */
public abstract class WebCache
{
	protected boolean forceRefresh = false;
	
    protected abstract void setWebContentToCache(URL url, String webContent);
    protected abstract String getWebContentFromCache(URL url);
    /**
     * Gets web content from specified url or from the cache. Default timeout will be 10 seconds.
     *
     * @param url URL to get content from.
     * @return content of the URL.
     * @throws IOException
     */
    public String getWebContent(URL url) throws IOException
    {
        return this.getWebContent(url, 10000);
    }

    /**
     * Gets web content from specified url or from the cache. You should also specify timeout.
     *
     * @param url URL to get content from.
     * @param timeout milliseconds to wait for connection.
     * @return content of the URL.
     * @throws IOException
     */
    public String getWebContent(URL url, int timeout) throws IOException
    {
    	if(!forceRefresh)
    	{
    		String webContent = this.getWebContentFromCache(url);
    		if (webContent != null) {
    			return webContent;
    		}    		
    	}

        //Getting content from web.
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(timeout);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        StringBuilder webBuffer = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            webBuffer.append(inputLine);
        in.close();

        this.setWebContentToCache(url, webBuffer.toString());

        return webBuffer.toString();
    }

    public void setForceRefresh(boolean forceRefresh)
    {
    	this.forceRefresh = forceRefresh;
    }
}
