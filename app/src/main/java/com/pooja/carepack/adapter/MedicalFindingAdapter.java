package com.pooja.carepack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelMedicalFinding;
import com.squareup.picasso.Picasso;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class MedicalFindingAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private View view;
    private Context context;
    private ModelMedicalFinding modelMedicalFinding;
    private ImageView ivDrawerImage;
    private TextView tvTitle,tvdate;
    private CheckBox cbStatus;

    public MedicalFindingAdapter(Context context, ModelMedicalFinding modelMedicalFinding) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.modelMedicalFinding = modelMedicalFinding;
    }

    @Override
    public int getCount() {
        return modelMedicalFinding.getMedicalFindings().size();
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
        view = mInflater.inflate(R.layout.row_medical_finding, null);

        ivDrawerImage=(ImageView)view.findViewById(R.id.row_medical_finding_ivDrawerImage);
        cbStatus=(CheckBox)view.findViewById(R.id.row_medical_finding_cbDrawerImage);
        tvTitle=(TextView)view.findViewById(R.id.row_medical_finding_tvTitle);
        tvdate=(TextView)view.findViewById(R.id.row_medical_finding_tvDate);

        tvTitle.setText(modelMedicalFinding.getMedicalFindings().get(position).getTitle());
        tvdate.setText(modelMedicalFinding.getMedicalFindings().get(position).getFindingDate());
//        String status=modelMedicalFinding.getMedicalFindings().get(position).getEmailStatus();
        cbStatus.setVisibility(View.GONE);
//        if(status.equals("y"))
//            cbStatus.setChecked(true);
//        else
//            cbStatus.setChecked(false);

        Picasso.with(context).load(modelMedicalFinding.getMedicalFindings().get(position).getImage()).placeholder(R.drawable.image_placehoder).into(ivDrawerImage);
//        Glide.with(context).load(modelMedicalFinding.getMedicalFindings().get(position).getImage()).into(ivDrawerImage);

        return view;
    }

}
