/*
 * 
 */

package com.pooja.carepack.volly;

// TODO: Auto-generated Javadoc
/**
 * The Interface LibCacheListner.
 */
public interface LibCacheListner {
    
    /**
     * On cache response complete.
     *
     * @param status the status
     * @param clsGson the cls gson
     * @param requestCode the request code
     */
    public void onCacheResponseComplete(int status, Object clsGson, int requestCode);

}
