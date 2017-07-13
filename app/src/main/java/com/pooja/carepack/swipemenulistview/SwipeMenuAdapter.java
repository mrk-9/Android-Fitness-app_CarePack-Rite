/*
 * 
 */
package com.pooja.carepack.swipemenulistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

// TODO: Auto-generated Javadoc

/**
 * The Class SwipeMenuAdapter.
 *
 * @author baoyz
 * @date 2014-8-24
 */
public class SwipeMenuAdapter implements WrapperListAdapter,
        SwipeMenuView.OnSwipeItemClickListener {

    /**
     * The m adapter.
     */
    private ListAdapter mAdapter;

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The on menu item click listener.
     */
    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;

    /**
     * Instantiates a new swipe menu adapter.
     *
     * @param context the context
     * @param adapter the adapter
     */
    public SwipeMenuAdapter(Context context, ListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout layout = null;
        if (convertView == null) {
            View contentView = mAdapter.getView(position, convertView, parent);
            SwipeMenu menu = new SwipeMenu(mContext);
            menu.setViewType(mAdapter.getItemViewType(position));
            createMenu(menu);
            SwipeMenuView menuView = new SwipeMenuView(menu,
                    (SwipeMenuListView) parent);
            menuView.setOnSwipeItemClickListener(this);
            SwipeMenuListView listView = (SwipeMenuListView) parent;
            layout = new SwipeMenuLayout(contentView, menuView,
                    listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());
            layout.setPosition(position);
        } else {
            layout = (SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            View view = mAdapter.getView(position, layout.getContentView(),
                    parent);
        }
        return layout;
    }

    /**
     * Creates the menu.
     *
     * @param menu the menu
     */
    public void createMenu(SwipeMenu menu) {
        // Test Code
        SwipeMenuItem item = new SwipeMenuItem(mContext);
        item.setTitle("Item 1");
        item.setBackground(new ColorDrawable(Color.GRAY));
        item.setWidth(300);
        menu.addMenuItem(item);

        item = new SwipeMenuItem(mContext);
        item.setTitle("Item 2");
        item.setBackground(new ColorDrawable(Color.RED));
        item.setWidth(300);
        menu.addMenuItem(item);
    }

    /* (non-Javadoc)
     * @see com.carepack.swipemenulistview.SwipeMenuView.OnSwipeItemClickListener#onItemClick(com.carepack.swipemenulistview.SwipeMenuView, com.carepack.swipemenulistview.SwipeMenu, int)
     */
    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
                    index);
        }
    }

    /**
     * Sets the on menu item click listener.
     *
     * @param onMenuItemClickListener the new on menu item click listener
     */
    public void setOnMenuItemClickListener(
            SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#registerDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#unregisterDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    /* (non-Javadoc)
     * @see android.widget.ListAdapter#areAllItemsEnabled()
     */
    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    /* (non-Javadoc)
     * @see android.widget.ListAdapter#isEnabled(int)
     */
    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        if (mAdapter != null)
            return mAdapter.hasStableIds();
        else
            return false;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemViewType(int)
     */
    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getViewTypeCount()
     */
    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    /* (non-Javadoc)
     * @see android.widget.WrapperListAdapter#getWrappedAdapter()
     */
    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

}
