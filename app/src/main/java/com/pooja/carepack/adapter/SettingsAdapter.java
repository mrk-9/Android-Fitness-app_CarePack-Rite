package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pooja.carepack.R;

/**
 * Created by Vinay Rathod on 23/12/15.
 */
public class SettingsAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private int length;
    private Context mContex;
    private String[] settingsArray;
    private View view;
    //    private LinearLayout llMain;
    private TextView tvItems;

    public SettingsAdapter(Context mContex, String[] stringArray, String fbId) {
        this.mContex = mContex;
        mInflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.settingsArray = stringArray;

        length = settingsArray.length;
        if (fbId.length() > 0)
            length = length - 1;
    }

    @Override
    public int getCount() {
        return length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_frg_setting, null);
//        llMain=(LinearLayout)view.findViewById(R.id.row_frg_settings_llMain);
        tvItems = (TextView) view.findViewById(R.id.row_frg_setting_tvItems);

        tvItems.setText(settingsArray[position].toString());
//        llMain.setOnClickListener();
        return view;
    }
}
