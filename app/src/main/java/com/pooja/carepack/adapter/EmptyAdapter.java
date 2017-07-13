package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pooja.carepack.R;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class EmptyAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final String title;
    private View view;

    public EmptyAdapter(Context context, String title) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.title = title;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_empty_view, null);
        ((TextView) view.findViewById(R.id.row_empty_tvName)).setText(title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

}
