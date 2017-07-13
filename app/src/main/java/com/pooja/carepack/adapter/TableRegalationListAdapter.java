package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelTabletList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TableRegalationListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<ModelTabletList.TabletList> modelTableList = new ArrayList<>();
    private List<ModelTabletList.TabletList> modelListAll = new ArrayList<>();
    private View view;

    public TableRegalationListAdapter(Context context, List<ModelTabletList.TabletList> modelTableList) {
        this.modelTableList.addAll(modelTableList);
        this.modelListAll.addAll(modelTableList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return modelTableList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(modelTableList.get(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_table_regalation_list, null);
        TextView tvName = (TextView) view.findViewById(R.id.tvMedicineName);
        tvName.setText("" + modelTableList.get(position).title);
        return view;
    }

    public void search(String s) {
        modelTableList.clear();
        if (s.length() > 0) {
            for (ModelTabletList.TabletList item : modelListAll) {
                if (item.title.toLowerCase().contains(s.toLowerCase())){
                    modelTableList.add(item);
                }
            }
        } else {
            modelTableList.addAll(modelListAll);
        }
        notifyDataSetChanged();
    }
}
