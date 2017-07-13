package com.pooja.carepack.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelUserVaccinationList;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class VaccinationCardAdapter extends BaseAdapter implements LibPostListner//CompoundButton.OnCheckedChangeListener
{

    private final LayoutInflater mInflater;
    private View view;
    private ModelUserVaccinationList modelUserVaccinationList;
    private Context context;
    private CheckBox cbEmailStatus;
    private TextView tvName, tvDate;
    private MyPrefs prefs;
    private String emailStatus;
    private ModelEditEmailDeletVaccination modelEditEmailDeleteVaccination;
//    private int position;

    public VaccinationCardAdapter(Context context, ModelUserVaccinationList modelUserVaccinationList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.modelUserVaccinationList = modelUserVaccinationList;
        prefs = new MyPrefs(context);

        Log.d("TAG", "swipe list lenght  " + modelUserVaccinationList.getUserVaccination().size());
    }

    @Override
    public int getCount() {
        return modelUserVaccinationList.getUserVaccination().size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView1, ViewGroup parent) {

        view = mInflater.inflate(R.layout.row_vaccination_card, null);

        cbEmailStatus = (CheckBox) view.findViewById(R.id.row_vaccination_card_cbEmailStatus);
        tvName = (TextView) view.findViewById(R.id.row_vaccination_card_tvName);
        tvDate = (TextView) view.findViewById(R.id.row_vaccination_card_tvDate);


            tvName.setText(modelUserVaccinationList.getUserVaccination().get(position).getTitle());
            tvDate.setText(modelUserVaccinationList.getUserVaccination().get(position).getReminder());
            if (modelUserVaccinationList.getUserVaccination().get(position).getEmailStatus().equals("n"))
                cbEmailStatus.setChecked(false);
            else
                cbEmailStatus.setChecked(true);

        cbEmailStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean Status = buttonView.isChecked();
                if (Status == true)
                    emailStatus = "y";
                else
                    emailStatus = "n";
//                editEmailStatus(position, emailStatus);
            }
        });

        return view;
    }

    public void refreshAdapter(){
        notifyDataSetChanged();
    }

    private void editEmailStatus(int position, String status) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "vaccination_email_status");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", modelUserVaccinationList.getUserVaccination().get(position).getId());
        hashMap.put("email_status", status);
        new PostLibResponse(VaccinationCardAdapter.this, new ModelEditEmailDeletVaccination(), context, hashMap, WebServicesConst.USER_VACCINATION_LIST, WebServicesConst.RES.EDIT_EMAIL_STAUS, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelEditEmailDeleteVaccination = (ModelEditEmailDeletVaccination) clsGson;
        if (requestCode == WebServicesConst.RES.EDIT_EMAIL_STAUS) {
            if (modelEditEmailDeleteVaccination != null) {
                int status = modelEditEmailDeleteVaccination.getStatus();
                if (status != 200) {
                    Toast.makeText(context, modelEditEmailDeleteVaccination.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

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
