/*
 * 
 */
package com.pooja.carepack.interfaces;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving iSignout events.
 * The class that is interested in processing a iSignout
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addiSignoutListener<code> method. When
 * the iSignout event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface iSignoutListener {
	
	/**
	 * On signout.
	 */
	public void onSignout();
}