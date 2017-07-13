package com.pooja.carepack.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.fragments.VaccinationCardAddFragment;
import com.pooja.carepack.model.ModelVaccinationList;

/**
 * Created by Vinay Rathod on 28/12/15.
 */
public class VaccinationGridViewAdapter extends BaseAdapter
{
    private final LayoutInflater mInflater;
    private View view;
    private ModelVaccinationList modelVaccinationList;
    private Context context;

    public VaccinationGridViewAdapter(Context context, ModelVaccinationList modelVaccinationList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.modelVaccinationList=modelVaccinationList;
    }
    
    @Override
    public int getCount() {
        return modelVaccinationList.getVaccinationList().size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_vaccination_card_gv, null);
        TextView tvName=(TextView)view.findViewById(R.id.row_vaccination_card_gv_tvName);
        LinearLayout llMain=(LinearLayout)view.findViewById(R.id.row_vaccination_card_gv_llMain);

        tvName.setText(modelVaccinationList.getVaccinationList().get(position).getTitle());

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fContainer=new VaccinationCardAddFragment();
                Bundle bundel=new Bundle();
                bundel.putString("VaccinationID",modelVaccinationList.getVaccinationList().get(position).getId());
                fContainer.setArguments(bundel);
                ((MainActivity) context).replaceFragment(fContainer,"Add Vaccination");
            }
        });

        return view;
    }
}
