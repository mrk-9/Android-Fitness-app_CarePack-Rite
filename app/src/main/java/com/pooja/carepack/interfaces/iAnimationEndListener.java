/*
 * 
 */
package com.pooja.carepack.interfaces;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving iAnimationEnd events.
 * The class that is interested in processing a iAnimationEnd
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addiAnimationEndListener<code> method. When
 * the iAnimationEnd event occurs, that object's appropriate
 * method is invoked.
 *
 * @see iAnimationEndEvent
 */
public interface iAnimationEndListener {
	
	/**
	 * On translate listener.
	 */
	public void onTranslateListener();

	/**
	 * On to middle listener.
	 */
	public void onToMiddleListener();

	/**
	 * On translate top listener.
	 */
	public void onTranslateTopListener();

	/**
	 * On to bottom listener.
	 */
	public void onToBottomListener();
}