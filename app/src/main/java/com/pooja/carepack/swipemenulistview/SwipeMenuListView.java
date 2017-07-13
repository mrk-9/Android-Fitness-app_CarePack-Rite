/*
 * 
 */
package com.pooja.carepack.swipemenulistview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenuListView.
 *
 * @author baoyz
 * @date 2014-8-18
 */
public class SwipeMenuListView extends ListView {

	/** The Constant TOUCH_STATE_NONE. */
	private static final int TOUCH_STATE_NONE = 0;
	
	/** The Constant TOUCH_STATE_X. */
	private static final int TOUCH_STATE_X = 1;
	
	/** The Constant TOUCH_STATE_Y. */
	private static final int TOUCH_STATE_Y = 2;

	/** The Constant DIRECTION_LEFT. */
	public static final int DIRECTION_LEFT = 1;
	
	/** The Constant DIRECTION_RIGHT. */
	public static final int DIRECTION_RIGHT = -1;
	
	/** The m direction. */
	private int mDirection = 1;// swipe from right to left by default

	/** The max y. */
	private int MAX_Y = 5;
	
	/** The max x. */
	private int MAX_X = 3;
	
	/** The m down x. */
	private float mDownX;
	
	/** The m down y. */
	private float mDownY;
	
	/** The m touch state. */
	private int mTouchState;
	
	/** The m touch position. */
	private int mTouchPosition;
	
	/** The m touch view. */
	private SwipeMenuLayout mTouchView;
	
	/** The m on swipe listener. */
	private OnSwipeListener mOnSwipeListener;

	/** The m menu creator. */
	private SwipeMenuCreator mMenuCreator;
	
	/** The m on menu item click listener. */
	private OnMenuItemClickListener mOnMenuItemClickListener;
	
	/** The m close interpolator. */
	private Interpolator mCloseInterpolator;
	
	/** The m open interpolator. */
	private Interpolator mOpenInterpolator;

	/**
	 * Instantiates a new swipe menu list view.
	 *
	 * @param context the context
	 */
	public SwipeMenuListView(Context context) {
		super(context);
		init();
	}

	/**
	 * Instantiates a new swipe menu list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Instantiates a new swipe menu list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public SwipeMenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	/* (non-Javadoc)
	 * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
			@Override
			public void createMenu(SwipeMenu menu) {
				if (mMenuCreator != null) {
					mMenuCreator.create(menu);
				}
			}

			@Override
			public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
				boolean flag = false;
				if (mOnMenuItemClickListener != null) {
					flag = mOnMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
				}
				if (mTouchView != null && !flag) {
					mTouchView.smoothCloseMenu();
				}
			}
		});
	}

	/**
	 * Sets the close interpolator.
	 *
	 * @param interpolator the new close interpolator
	 */
	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	/**
	 * Sets the open interpolator.
	 *
	 * @param interpolator the new open interpolator
	 */
	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	/**
	 * Gets the open interpolator.
	 *
	 * @return the open interpolator
	 */
	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	/**
	 * Gets the close interpolator.
	 *
	 * @return the close interpolator
	 */
	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
			return super.onTouchEvent(ev);
		int action = MotionEventCompat.getActionMasked(ev);
		action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			int oldPos = mTouchPosition;
			mDownX = ev.getX();
			mDownY = ev.getY();
			mTouchState = TOUCH_STATE_NONE;

			mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

			if (mTouchPosition == oldPos && mTouchView != null && mTouchView.isOpen()) {
				mTouchState = TOUCH_STATE_X;
				mTouchView.onSwipe(ev);
				return true;
			}

			View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

			if (mTouchView != null && mTouchView.isOpen()) {
				mTouchView.smoothCloseMenu();
				mTouchView = null;
				// return super.onTouchEvent(ev);
				// try to cancel the touch event
				MotionEvent cancelEvent = MotionEvent.obtain(ev);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
				onTouchEvent(cancelEvent);
				return true;
			}
			if (view instanceof SwipeMenuLayout) {
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.setSwipeDirection(mDirection);
			}
			if (mTouchView != null) {
				mTouchView.onSwipe(ev);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = Math.abs((ev.getY() - mDownY));
			float dx = Math.abs((ev.getX() - mDownX));
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(ev);
				}
				getSelector().setState(new int[] { 0 });
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			} else if (mTouchState == TOUCH_STATE_NONE) {
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				} else if (dx > MAX_X) {
					mTouchState = TOUCH_STATE_X;
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipeStart(mTouchPosition);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(ev);
					if (!mTouchView.isOpen()) {
						mTouchPosition = -1;
						mTouchView = null;
					}
				}
				if (mOnSwipeListener != null) {
					mOnSwipeListener.onSwipeEnd(mTouchPosition);
				}
				ev.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * Smooth open menu.
	 *
	 * @param position the position
	 */
	public void smoothOpenMenu(int position) {
		if (position >= getFirstVisiblePosition() && position <= getLastVisiblePosition()) {
			View view = getChildAt(position - getFirstVisiblePosition());
			if (view instanceof SwipeMenuLayout) {
				mTouchPosition = position;
				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
				}
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.setSwipeDirection(mDirection);
				mTouchView.smoothOpenMenu();
			}
		}
	}

	/**
	 * Dp2px.
	 *
	 * @param dp the dp
	 * @return the int
	 */
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}

	/**
	 * Sets the menu creator.
	 *
	 * @param menuCreator the new menu creator
	 */
	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	/**
	 * Sets the on menu item click listener.
	 *
	 * @param onMenuItemClickListener the new on menu item click listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	/**
	 * Sets the on swipe listener.
	 *
	 * @param onSwipeListener the new on swipe listener
	 */
	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

	/**
	 * The listener interface for receiving onMenuItemClick events.
	 * The class that is interested in processing a onMenuItemClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnMenuItemClickListener<code> method. When
	 * the onMenuItemClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnMenuItemClickEvent
	 */
	public static interface OnMenuItemClickListener {
		
		/**
		 * On menu item click.
		 *
		 * @param position the position
		 * @param menu the menu
		 * @param index the index
		 * @return true, if successful
		 */
		boolean onMenuItemClick(int position, SwipeMenu menu, int index);
	}

	/**
	 * The listener interface for receiving onSwipe events.
	 * The class that is interested in processing a onSwipe
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnSwipeListener<code> method. When
	 * the onSwipe event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnSwipeEvent
	 */
	public static interface OnSwipeListener {
		
		/**
		 * On swipe start.
		 *
		 * @param position the position
		 */
		void onSwipeStart(int position);

		/**
		 * On swipe end.
		 *
		 * @param position the position
		 */
		void onSwipeEnd(int position);
	}

	/**
	 * Sets the swipe direction.
	 *
	 * @param direction the new swipe direction
	 */
	public void setSwipeDirection(int direction) {
		mDirection = direction;
	}

	/**
	 * Close all.
	 */
	public void closeAll() {
		for (int i = 0; i < getChildCount(); i++) {
			try {
				View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
				if (view instanceof SwipeMenuLayout) {
					mTouchView = (SwipeMenuLayout) view;
					if (mTouchView != null && mTouchView.isOpen())
						mTouchView.smoothCloseMenu();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
