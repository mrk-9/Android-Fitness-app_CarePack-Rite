/*
 * 
 */
package com.pooja.carepack.swipemenulistview;


import android.content.Context;
import android.graphics.drawable.Drawable;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenuItem.
 *
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenuItem {

	/** The id. */
	private int id;
	
	/** The m context. */
	private Context mContext;
	
	/** The title. */
	private String title;
	
	/** The icon. */
	private Drawable icon;
	
	/** The background. */
	private Drawable background;
	
	/** The title color. */
	private int titleColor;
	
	/** The title size. */
	private int titleSize;
	
	/** The width. */
	private int width;

	/**
	 * Instantiates a new swipe menu item.
	 *
	 * @param context the context
	 */
	public SwipeMenuItem(Context context) {
		mContext = context;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the title color.
	 *
	 * @return the title color
	 */
	public int getTitleColor() {
		return titleColor;
	}

	/**
	 * Gets the title size.
	 *
	 * @return the title size
	 */
	public int getTitleSize() {
		return titleSize;
	}

	/**
	 * Sets the title size.
	 *
	 * @param titleSize the new title size
	 */
	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	/**
	 * Sets the title color.
	 *
	 * @param titleColor the new title color
	 */
	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the title.
	 *
	 * @param resId the new title
	 */
	public void setTitle(int resId) {
		setTitle(mContext.getString(resId));
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public Drawable getIcon() {
		return icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param resId the new icon
	 */
	public void setIcon(int resId) {
		this.icon = mContext.getResources().getDrawable(resId);
	}

	/**
	 * Gets the background.
	 *
	 * @return the background
	 */
	public Drawable getBackground() {
		return background;
	}

	/**
	 * Sets the background.
	 *
	 * @param background the new background
	 */
	public void setBackground(Drawable background) {
		this.background = background;
	}

	/**
	 * Sets the background.
	 *
	 * @param resId the new background
	 */
	public void setBackground(int resId) {
		this.background = mContext.getResources().getDrawable(resId);
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
