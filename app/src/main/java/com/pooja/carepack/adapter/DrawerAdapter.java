package com.pooja.carepack.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class DrawerAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private String[] menus;
    private TypedArray menuItems;
    private View view;
    private int selectedItem;

    public DrawerAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menus = context.getResources().getStringArray(R.array.drawer_item);
        menuItems = context.getResources().obtainTypedArray(R.array.drawer_icon);
    }

    @Override
    public int getCount() {
        return menus.length;
    }

    @Override
    public Object getItem(int position) {
        return menus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_drawer, null);
        if (position == selectedItem)
            view.setBackgroundColor(Color.parseColor("#2c659c"));
        if (position == 0)
            view.setBackgroundColor(position == selectedItem ? Color.parseColor("#99ff0000") : Color.RED);

        ((TextView) view.findViewById(R.id.tvDrawername)).setText(menus[position]);
        ((ImageView) view.findViewById(R.id.ivDrawerImage)).setImageDrawable(menuItems.getDrawable(position));
        return view;
    }

    public void setIndex(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public int getSelected() {
        return selectedItem;
    }
}
