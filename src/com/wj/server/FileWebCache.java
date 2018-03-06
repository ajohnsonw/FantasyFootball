package com.wj.server;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for caching content of websites.
 *
 * Each website's content is stored in a separate file in the DIR_WEB_CACHE directory.
 */
public class FileWebCache extends WebCache
{
    private final static String DIR_WEB_CACHE = "WebCache";

    private MessageDigest digest;

    FileWebCache()
    {
        try
        {
            this.digest = MessageDigest.getInstance("MD5");
            this.prepareCacheDirectory();
        } catch (NoSuchAlgorithmException | IOException e)
        {
            //todo: log the error.
        }
    }

    /**
     * Creates cache directory if it does not exist.
     *
     * @throws IOException if there are problems with DIR_WEB_CACHE directory.
     */
    synchronized private void prepareCacheDirectory() throws IOException
    {
        File file = new File(FileWebCache.DIR_WEB_CACHE);
        if (file.isFile()) {
            if (!file.delete()) {
                throw new IOException(String.format(
                        "Unable to remove file %s from the app directory. It must be directory.",
                        FileWebCache.DIR_WEB_CACHE
                ));
            }
        }

        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new IOException(String.format(
                        "Unable to create directory %s in the app directory.",
                        FileWebCache.DIR_WEB_CACHE
                ));
            }
        }
    }

    /**
     * Writes website content to file.
     *
     * @param url website URL.
     * @param webContent content of the website.
     */
    @Override
    synchronized protected void setWebContentToCache(URL url, String webContent)
    {
        try
        {
            File file = this.getCacheFile(url);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            writer.write(webContent);
            writer.close();
        } catch (IOException e)
        {
            //todo: log the error.
        }
    }

    /**
     * Get content of the specified URL from the cache.
     *
     * @param url website URL, which content to retrieve from cache.
     * @return website content.
     */
    @Override
    synchronized protected String getWebContentFromCache(URL url)
    {
        String webContent = null;
        try
        {
            File file = this.getCacheFile(url);
            if (file.length() == 0) {
                return null;
            }
            byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
            webContent = new String(encoded, "utf-8");

        } catch (IOException e)
        {
            //todo: log the error.
        }

        return webContent;
    }


    synchronized private File getCacheFile(URL url) throws IOException
    {
        this.digest.update(url.toString().getBytes());
        byte[] digest = this.digest.digest();
        String fileName = DatatypeConverter.printHexBinary(digest).toLowerCase() + ".html";
        Path path = Paths.get(FileWebCache.DIR_WEB_CACHE, fileName);

        File file = new File(path.toString());
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(String.format("Unable to create file %s.", file.getName()));
            }
        }

        return file;
    }

}
