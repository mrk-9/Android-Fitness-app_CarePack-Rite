/*
 * 
 */

package com.pooja.carepack.volly;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

// TODO: Auto-generated Javadoc
/**
 * The Class LruBitmapCache.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    
    /**
     * Gets the default lru cache size.
     *
     * @return the default lru cache size
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    /**
     * Instantiates a new lru bitmap cache.
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * Instantiates a new lru bitmap cache.
     *
     * @param sizeInKiloBytes the size in kilo bytes
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    /* (non-Javadoc)
     * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object, java.lang.Object)
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang.String)
     */
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang.String, android.graphics.Bitmap)
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
