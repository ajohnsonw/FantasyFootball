package com.wj.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Abstract class for caching content of websites.
 *
 * For caching content you can use one of its descendant, for example {@link MemoryWebCache}
 */
public abstract class WebCache
{
    public String getWebContent(URL url) throws IOException
    {
        String webContent = this.getWebContentFromCache(url);
        if (webContent != null) {
            return webContent;
        }

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

    protected abstract void setWebContentToCache(URL url, String webContent);
    protected abstract String getWebContentFromCache(URL url);

}
