package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelUserTabletList;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TableRegalationAdapter extends BaseAdapter implements LibPostListner {

    private final LayoutInflater mInflater;
    private final List<ModelUserTabletList.UserTablet> modelTableList;
    private final Context context;
    private final MyPrefs prefs;
    private View view;
    private CheckBox cbEmailStatus;
    private TextView tvName, tvDose, tvMg, tvMorning, tvLunch, tvNight;

    public TableRegalationAdapter(Context context, List<ModelUserTabletList.UserTablet> modelTableList) {
        this.context = context;
        prefs = new MyPrefs(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.modelTableList = modelTableList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = mInflater.inflate(R.layout.row_table_regalation, null);

        cbEmailStatus = (CheckBox) view.findViewById(R.id.row_table_cb_status);
        tvName = (TextView) view.findViewById(R.id.row_table_tv_title);
        tvDose = (TextView) view.findViewById(R.id.row_table_tv_dose);
        tvMg = (TextView) view.findViewById(R.id.row_table_tv_mg);
        tvMorning = (TextView) view.findViewById(R.id.row_table_tv_morning);
        tvLunch = (TextView) view.findViewById(R.id.row_table_tv_lunch);
        tvNight = (TextView) view.findViewById(R.id.row_table_tv_night);

        tvName.setText(modelTableList.get(position).title);
        tvDose.setText(modelTableList.get(position).dose);
        tvMg.setText(modelTableList.get(position).mg);
        tvMorning.setText(modelTableList.get(position).morning);
        tvLunch.setText(modelTableList.get(position).lunch);
        tvNight.setText(modelTableList.get(position).night);
        if (modelTableList.get(position).email_status.equals("n"))
            cbEmailStatus.setChecked(false);
        else
            cbEmailStatus.setChecked(true);

        cbEmailStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean Status = buttonView.isChecked();
                String emailStatus;
                if (Status == true)
                    emailStatus = "y";
                else
                    emailStatus = "n";
//                editEmailStatus(position, emailStatus);
            }
        });

        return view;
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    private void editEmailStatus(int position, String status) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "vaccination_email_status");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", modelTableList.get(position).id);
        hashMap.put("email_status", status);

        new PostLibResponse(TableRegalationAdapter.this, new ModelEditEmailDeletVaccination(), context, hashMap, WebServicesConst.USER_TABLE_REGULATION, WebServicesConst.RES.GET_USER_TABLE_REGULATION, true, true);

    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
//        modelEditEmailDeleteVaccination = (ModelEditEmailDeletVaccination) clsGson;
//        if (requestCode == WebServicesConst.RES.EDIT_EMAIL_STAUS) {
//            if (modelEditEmailDeleteVaccination != null) {
//                int status = modelEditEmailDeleteVaccination.getStatus();
//                if (status != 200) {
//                    Toast.makeText(context, modelEditEmailDeleteVaccination.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    private static class ViewHolder {
        private CheckBox cbEmailStatus;
        private TextView tvName, tvDate;

        public ViewHolder(View view) {

            view.setTag(this);
        }
    }
}
