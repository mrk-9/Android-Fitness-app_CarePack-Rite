package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelTeeth;

import java.util.ArrayList;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TeethDetailAdapter extends BaseAdapter implements View.OnClickListener {

    private final LayoutInflater mInflater;
    private View view;
    private Context context;
    private ArrayList<ModelTeeth.TeethDetail> modelTeethDetail;
    private boolean editable;
    private int count;

    public TeethDetailAdapter(Context context, ArrayList<ModelTeeth.TeethDetail> modelTeethDetail) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.modelTeethDetail = modelTeethDetail;

        if (modelTeethDetail == null)
            modelTeethDetail = new ArrayList<>();
        if (modelTeethDetail.size() == 0)
            modelTeethDetail.add(new ModelTeeth().new TeethDetail());
        count = (modelTeethDetail != null && modelTeethDetail.size() > 0) ? modelTeethDetail.size() : 1;
    }

    @Override
    public int getCount() {
        count = (modelTeethDetail != null && modelTeethDetail.size() > 0) ? modelTeethDetail.size() : 1;
        return count;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.row_teeth_detail, null);

            holder.tvTitle = (TextView) view.findViewById(R.id.row_teeth_detail_tvTitle);
            holder.tvdesc = (TextView) view.findViewById(R.id.row_teeth_detail_tvDescription);
            holder.ivdelete = (ImageView) view.findViewById(R.id.row_teeth_detail_tvDelete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.ivdelete.setTag("" + position);
        holder.ivdelete.setOnClickListener(this);

        holder.ivdelete.setVisibility(editable ? View.VISIBLE : View.GONE);
        if (modelTeethDetail != null && modelTeethDetail.size() > count) {
            if (modelTeethDetail.get(position).title != null)
                holder. tvTitle.setText(modelTeethDetail.get(position).title);
            if (modelTeethDetail.get(position).description != null)
                holder. tvdesc.setText(modelTeethDetail.get(position).description);
        }
        return view;
    }

    public void deleteRow(int position) {
        modelTeethDetail.remove(position);
        notifyDataSetChanged();
    }

    public void addRow() {
        modelTeethDetail.add(new ModelTeeth().new TeethDetail());
        notifyDataSetChanged();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        deleteRow(Integer.parseInt(v.getTag() + ""));
    }

    class ViewHolder {
        TextView tvTitle, tvdesc;
        ImageView ivdelete;
    }
}
