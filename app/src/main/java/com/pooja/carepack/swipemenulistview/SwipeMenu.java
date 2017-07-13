/*
 * 
 */
package com.pooja.carepack.swipemenulistview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenu.
 *
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenu {

	/** The m context. */
	private Context mContext;
	
	/** The m items. */
	private List<SwipeMenuItem> mItems;
	
	/** The m view type. */
	private int mViewType;

	/**
	 * Instantiates a new swipe menu.
	 *
	 * @param context the context
	 */
	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * Adds the menu item.
	 *
	 * @param item the item
	 */
	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	/**
	 * Removes the menu item.
	 *
	 * @param item the item
	 */
	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	/**
	 * Gets the menu items.
	 *
	 * @return the menu items
	 */
	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	/**
	 * Gets the menu item.
	 *
	 * @param index the index
	 * @return the menu item
	 */
	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	/**
	 * Gets the view type.
	 *
	 * @return the view type
	 */
	public int getViewType() {
		return mViewType;
	}

	/**
	 * Sets the view type.
	 *
	 * @param viewType the new view type
	 */
	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
