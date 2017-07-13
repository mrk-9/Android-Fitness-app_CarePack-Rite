/*
 * 
 */

package com.pooja.carepack.volly;

// TODO: Auto-generated Javadoc
/**
 * The Interface LibListner.
 */
public interface LibListner {
    
    /**
     * On response complete.
     *
     * @param clsGson the cls gson
     * @param requestCode the request code
     */
    public void onResponseComplete(Object clsGson, int requestCode);

    /**
     * On response error.
     *
     * @param errorMessage the error message
     * @param requestCode the request code
     */
    public void onResponseError(String errorMessage, int requestCode);
}
