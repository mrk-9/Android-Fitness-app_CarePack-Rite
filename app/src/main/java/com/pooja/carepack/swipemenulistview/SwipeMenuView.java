/*
 * 
 */
package com.pooja.carepack.swipemenulistview;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenuView.
 *
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenuView extends LinearLayout implements OnClickListener {

	/** The m list view. */
	private SwipeMenuListView mListView;
	
	/** The m layout. */
	private SwipeMenuLayout mLayout;
	
	/** The m menu. */
	private SwipeMenu mMenu;
	
	/** The on item click listener. */
	private OnSwipeItemClickListener onItemClickListener;
	
	/** The position. */
	private int position;

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
	}

	/**
	 * Instantiates a new swipe menu view.
	 *
	 * @param menu the menu
	 * @param listView the list view
	 */
	public SwipeMenuView(SwipeMenu menu, SwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	/**
	 * Adds the item.
	 *
	 * @param item the item
	 * @param id the id
	 */
	private void addItem(SwipeMenuItem item, int id) {
		LayoutParams params = new LayoutParams(item.getWidth(),
				LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);

		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		if (!TextUtils.isEmpty(item.getTitle())) {
			parent.addView(createTitle(item));
		}

	}

	/**
	 * Creates the icon.
	 *
	 * @param item the item
	 * @return the image view
	 */
	private ImageView createIcon(SwipeMenuItem item) {
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	/**
	 * Creates the title.
	 *
	 * @param item the item
	 * @return the text view
	 */
	private TextView createTitle(SwipeMenuItem item) {
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
	}

	/**
	 * Gets the on swipe item click listener.
	 *
	 * @return the on swipe item click listener
	 */
	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	/**
	 * Sets the on swipe item click listener.
	 *
	 * @param onItemClickListener the new on swipe item click listener
	 */
	public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	/**
	 * Sets the layout.
	 *
	 * @param mLayout the new layout
	 */
	public void setLayout(SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	/**
	 * The listener interface for receiving onSwipeItemClick events.
	 * The class that is interested in processing a onSwipeItemClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnSwipeItemClickListener<code> method. When
	 * the onSwipeItemClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnSwipeItemClickEvent
	 */
	public static interface OnSwipeItemClickListener {
		
		/**
		 * On item click.
		 *
		 * @param view the view
		 * @param menu the menu
		 * @param index the index
		 */
		void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}
}
