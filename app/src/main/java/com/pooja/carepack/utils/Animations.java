/*
 * 
 */
package com.pooja.carepack.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.pooja.carepack.R;
import com.pooja.carepack.interfaces.iAnimationEndListener;

// TODO: Auto-generated Javadoc

/**
 * The Class Animations.
 */
public class Animations {
	
	/** The anim to bottom. */
	private Animation animTranslate, animTranslateTop, animToMiddle, animToBottom;
	
	/** The listener. */
	private iAnimationEndListener listener;

	/**
	 * Instantiates a new animations.
	 *
	 * @param mContext the m context
	 * @param l the l
	 */
	public Animations(Context mContext, iAnimationEndListener l) {
		listener = l;
		animTranslate = AnimationUtils.loadAnimation(mContext, R.anim.translate);
		animToMiddle = AnimationUtils.loadAnimation(mContext, R.anim.bottom2middle);
		animToBottom = AnimationUtils.loadAnimation(mContext, R.anim.middle2bottom);
		animTranslateTop = AnimationUtils.loadAnimation(mContext, R.anim.translate_top);
	}

	/**
	 * Gets the translate.
	 *
	 * @return the translate
	 */
	public Animation getTranslate() {
		animTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (listener != null)
					listener.onTranslateListener();
			}
		});
		return animTranslate;
	}

	/**
	 * Gets the translate top.
	 *
	 * @return the translate top
	 */
	public Animation getTranslateTop() {
		animTranslateTop.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (listener != null)
					listener.onTranslateTopListener();
			}
		});
		return animTranslateTop;
	}

	/**
	 * Gets the to middle.
	 *
	 * @return the to middle
	 */
	public Animation getToMiddle() {
		animToMiddle.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (listener != null)
					listener.onToMiddleListener();
			}
		});
		return animToMiddle;
	}

	/**
	 * Gets the to bottom.
	 *
	 * @return the to bottom
	 */
	public Animation getToBottom() {
		animToBottom.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (listener != null)
					listener.onToBottomListener();
			}
		});
		return animToBottom;
	}
}
