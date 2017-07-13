/*
 * 
 */

package com.pooja.carepack.volly;

// TODO: Auto-generated Javadoc
/**
 * The Interface LibPostListner.
 */
public interface LibPostListner {
    
    /**
     * On post response complete.
     *
     * @param clsGson the cls gson
     * @param requestCode the request code
     */
    public void onPostResponseComplete(Object clsGson, int requestCode);

    /**
     * On post response error.
     *
     * @param errorMessage the error message
     * @param requestCode the request code
     */
    public void onPostResponseError(String errorMessage, int requestCode);
}
