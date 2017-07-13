/*
 * 
 */
package com.pooja.carepack.swipemenulistview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenuLayout.
 *
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenuLayout extends FrameLayout {

	/** The Constant CONTENT_VIEW_ID. */
	private static final int CONTENT_VIEW_ID = 1;
	
	/** The Constant MENU_VIEW_ID. */
	private static final int MENU_VIEW_ID = 2;

	/** The Constant STATE_CLOSE. */
	private static final int STATE_CLOSE = 0;
	
	/** The Constant STATE_OPEN. */
	private static final int STATE_OPEN = 1;

	/** The m swipe direction. */
	private int mSwipeDirection;

	/** The m content view. */
	private View mContentView;
	
	/** The m menu view. */
	private SwipeMenuView mMenuView;
	
	/** The m down x. */
	private int mDownX;
	
	/** The state. */
	private int state = STATE_CLOSE;
	
	/** The m gesture detector. */
	private GestureDetectorCompat mGestureDetector;
	
	/** The m gesture listener. */
	private OnGestureListener mGestureListener;
	
	/** The is fling. */
	private boolean isFling;
	
	/** The min fling. */
	private int MIN_FLING = dp2px(15);
	
	/** The max velocityx. */
	private int MAX_VELOCITYX = -dp2px(500);
	
	/** The m open scroller. */
	private ScrollerCompat mOpenScroller;
	
	/** The m close scroller. */
	private ScrollerCompat mCloseScroller;
	
	/** The m base x. */
	private int mBaseX;
	
	/** The position. */
	private int position;
	
	/** The m close interpolator. */
	private Interpolator mCloseInterpolator;
	
	/** The m open interpolator. */
	private Interpolator mOpenInterpolator;

	/**
	 * Instantiates a new swipe menu layout.
	 *
	 * @param contentView the content view
	 * @param menuView the menu view
	 */
	public SwipeMenuLayout(View contentView, SwipeMenuView menuView) {
		this(contentView, menuView, null, null);
	}

	/**
	 * Instantiates a new swipe menu layout.
	 *
	 * @param contentView the content view
	 * @param menuView the menu view
	 * @param closeInterpolator the close interpolator
	 * @param openInterpolator the open interpolator
	 */
	public SwipeMenuLayout(View contentView, SwipeMenuView menuView,
			Interpolator closeInterpolator, Interpolator openInterpolator) {
		super(contentView.getContext());
		mCloseInterpolator = closeInterpolator;
		mOpenInterpolator = openInterpolator;
		mContentView = contentView;
		mMenuView = menuView;
		mMenuView.setLayout(this);
		init();
	}

	// private SwipeMenuLayout(Context context, AttributeSet attrs, int
	// defStyle) {
	// super(context, attrs, defStyle);
	// }

	/**
	 * Instantiates a new swipe menu layout.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	private SwipeMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Instantiates a new swipe menu layout.
	 *
	 * @param context the context
	 */
	private SwipeMenuLayout(Context context) {
		super(context);
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
		mMenuView.setPosition(position);
	}

	/**
	 * Sets the swipe direction.
	 *
	 * @param swipeDirection the new swipe direction
	 */
	public void setSwipeDirection(int swipeDirection) {
		mSwipeDirection = swipeDirection;
	}

	/**
	 * Inits the.
	 */
	private void init() {
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				isFling = false;
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO
				if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING
						&& velocityX < MAX_VELOCITYX) {
					isFling = true;
				}
				// Log.i("byz", MAX_VELOCITYX + ", velocityX = " + velocityX);
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
		mGestureDetector = new GestureDetectorCompat(getContext(),
				mGestureListener);

		// mScroller = ScrollerCompat.create(getContext(), new
		// BounceInterpolator());
		if (mCloseInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(),
					mCloseInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}
		if (mOpenInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(),
					mOpenInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}

		LayoutParams contentParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContentView.setLayoutParams(contentParams);
		if (mContentView.getId() < 1) {
			mContentView.setId(CONTENT_VIEW_ID);
		}

		mMenuView.setId(MENU_VIEW_ID);
		mMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		addView(mContentView);
		addView(mMenuView);

		// if (mContentView.getBackground() == null) {
		// mContentView.setBackgroundColor(Color.WHITE);
		// }

		// in android 2.x, MenuView height is MATCH_PARENT is not work.
		// getViewTreeObserver().addOnGlobalLayoutListener(
		// new OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// setMenuHeight(mContentView.getHeight());
		// // getViewTreeObserver()
		// // .removeGlobalOnLayoutListener(this);
		// }
		// });

	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * On swipe.
	 *
	 * @param event the event
	 * @return true, if successful
	 */
	public boolean onSwipe(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) event.getX();
			isFling = false;
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.i("byz", "downX = " + mDownX + ", moveX = " + event.getX());
			int dis = (int) (mDownX - event.getX());
			if (state == STATE_OPEN) {
				dis += mMenuView.getWidth()*mSwipeDirection;;
			}
			swipe(dis);
			break;
		case MotionEvent.ACTION_UP:
			if ((isFling || Math.abs(mDownX - event.getX()) > (mMenuView.getWidth() / 2)) &&
					Math.signum(mDownX - event.getX()) == mSwipeDirection) {
				// open
				smoothOpenMenu();
			} else {
				// close
				smoothCloseMenu();
				return false;
			}
			break;
		}
		return true;
	}

	/**
	 * Checks if is open.
	 *
	 * @return true, if is open
	 */
	public boolean isOpen() {
		return state == STATE_OPEN;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * Swipe.
	 *
	 * @param dis the dis
	 */
	private void swipe(int dis) {
		if (Math.signum(dis) != mSwipeDirection) {
			dis = 0;
		} else if (Math.abs(dis) > mMenuView.getWidth()) {
			dis = mMenuView.getWidth()*mSwipeDirection;
		}

		mContentView.layout(-dis, mContentView.getTop(),
				mContentView.getWidth() -dis, getMeasuredHeight());

		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {

			mMenuView.layout(mContentView.getWidth() - dis, mMenuView.getTop(),
					mContentView.getWidth() + mMenuView.getWidth() - dis,
					mMenuView.getBottom());
		} else {
			mMenuView.layout(-mMenuView.getWidth() - dis, mMenuView.getTop(),
					- dis, mMenuView.getBottom());
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (state == STATE_OPEN) {
			if (mOpenScroller.computeScrollOffset()) {
				swipe(mOpenScroller.getCurrX()*mSwipeDirection);
				postInvalidate();
			}
		} else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe((mBaseX - mCloseScroller.getCurrX())*mSwipeDirection);
				postInvalidate();
			}
		}
	}

	/**
	 * Smooth close menu.
	 */
	public void smoothCloseMenu() {
		state = STATE_CLOSE;
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {
			mBaseX = -mContentView.getLeft();
			mCloseScroller.startScroll(0, 0, mMenuView.getWidth(), 0, 350);
		} else {
			mBaseX = mMenuView.getRight();
			mCloseScroller.startScroll(0, 0, mMenuView.getWidth(), 0, 350);
		}
		postInvalidate();
	}

	/**
	 * Smooth open menu.
	 */
	public void smoothOpenMenu() {
		state = STATE_OPEN;
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {
			mOpenScroller.startScroll(-mContentView.getLeft(), 0, mMenuView.getWidth(), 0, 350);
		} else {
			mOpenScroller.startScroll(mContentView.getLeft(), 0, mMenuView.getWidth(), 0, 350);
		}
		postInvalidate();
	}

	/**
	 * Close menu.
	 */
	public void closeMenu() {
		if (mCloseScroller.computeScrollOffset()) {
			mCloseScroller.abortAnimation();
		}
		if (state == STATE_OPEN) {
			state = STATE_CLOSE;
			swipe(0);
		}
	}

	/**
	 * Open menu.
	 */
	public void openMenu() {
		if (state == STATE_CLOSE) {
			state = STATE_OPEN;
			swipe(mMenuView.getWidth()*mSwipeDirection);
		}
	}

	/**
	 * Gets the content view.
	 *
	 * @return the content view
	 */
	public View getContentView() {
		return mContentView;
	}

	/**
	 * Gets the menu view.
	 *
	 * @return the menu view
	 */
	public SwipeMenuView getMenuView() {
		return mMenuView;
	}

	/**
	 * Dp2px.
	 *
	 * @param dp the dp
	 * @return the int
	 */
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mMenuView.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
				getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mContentView.layout(0, 0, getMeasuredWidth(),
				mContentView.getMeasuredHeight());
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {
			mMenuView.layout(getMeasuredWidth(), 0,
					getMeasuredWidth() + mMenuView.getMeasuredWidth(),
					mContentView.getMeasuredHeight());
		} else {
			mMenuView.layout(-mMenuView.getMeasuredWidth(), 0,
					0, mContentView.getMeasuredHeight());
		}
	}

	/**
	 * Sets the menu height.
	 *
	 * @param measuredHeight the new menu height
	 */
	public void setMenuHeight(int measuredHeight) {
		Log.i("byz", "pos = " + position + ", height = " + measuredHeight);
		LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
		if (params.height != measuredHeight) {
			params.height = measuredHeight;
			mMenuView.setLayoutParams(mMenuView.getLayoutParams());
		}
	}
}
