/*
 * 
 */
package com.pooja.carepack.interfaces;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving iForgot events.
 * The class that is interested in processing a iForgot
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addiForgotListener<code> method. When
 * the iForgot event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface iForgotListener {
	
	/**
	 * On forgot password.
	 *
	 * @param email the email
	 */
	public void onForgotPassword(String email);
}