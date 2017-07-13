package com.pooja.carepack.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Yudiz on 23/12/15.
 */
public class TreatmentInviteeEmailDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvAdd, tvEdit;
    private TeethDetailAdapter mAdapter;
    private LinearLayout llDetail;
    private ArrayList<CharSequence> invitees;


    private Dialog view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = new Dialog(getActivity(), R.style.AppTheme);
        view.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view.setContentView(R.layout.frg_treatment_invitee_add);
        view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        view.show();
        init();
        return view;
    }

    private void init() {
        invitees = getArguments().getCharSequenceArrayList("invitees");

        llDetail = (LinearLayout) view.findViewById(R.id.frg_invitee_detail_layout);

        tvAdd = (TextView) view.findViewById(R.id.invitee_detail_tv_add);
        tvAdd.setOnClickListener(this);

        tvEdit = (TextView) view.findViewById(R.id.invitee_detail_tv_edit);
        tvEdit.setOnClickListener(this);
        mAdapter = new TeethDetailAdapter(getActivity(), llDetail);
        if (invitees != null && invitees.size() > 0) {
            mAdapter.addAll(invitees);
        } else
            mAdapter.add(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitee_detail_tv_add:
                if (!tvEdit.getText().toString().equalsIgnoreCase("edit")) {
                    tvEdit.setText("Edit");
                    mAdapter.setEditable(false);
                }
                mAdapter.add(null);
                break;
            case R.id.invitee_detail_tv_edit:
                if (llDetail.getChildCount() > 1) {
                    if (tvEdit.getText().toString().equalsIgnoreCase("edit")) {
                        tvEdit.setText("Done");
                        mAdapter.setEditable(true);
                    } else {
                        tvEdit.setText("Edit");
                        mAdapter.setEditable(false);
                    }
                }
                break;
        }
    }

    private boolean valid() {
        for (CharSequence email : mAdapter.getList()) {
            if (Utility.isEmailValid(email.toString()))
                return false;
        }
        return true;
    }

    class TeethDetailAdapter implements View.OnClickListener {
        private final LinearLayout layout;
        private final Context context;

        public TeethDetailAdapter(Context context, LinearLayout layout) {
            this.layout = layout;
            this.context = context;
        }

        public void removeView(int position) {
            LinearLayout removeView = ((LinearLayout) layout.getChildAt(position));
            EditText desc = ((EditText) removeView.findViewById(R.id.row_invitee_detail_tvEmail));

            desc.setText("");
            removeView.setVisibility(View.GONE);
        }


        @Nullable
        public void addAll(ArrayList<CharSequence> invitees) {
            for (CharSequence name : invitees) {
                add(name.toString());
            }
        }

        public ArrayList<CharSequence> getList() {
            ArrayList<CharSequence> charSequences = new ArrayList<>();
            for (int i = 0; i < layout.getChildCount(); i++) {
                LinearLayout removeView = ((LinearLayout) layout.getChildAt(i));
                EditText desc = ((EditText) removeView.findViewById(R.id.row_invitee_detail_tvEmail));

                if (desc.getText().length() > 0) {
                    charSequences.add(desc.getText().toString());
                }
            }
            return charSequences;
        }

        public void add(String name) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_treatment_invitee_detail, null);
            view.findViewById(R.id.row_invitee_detail_tvDelete).setTag("" + layout.getChildCount());
            view.findViewById(R.id.row_invitee_detail_tvDelete).setOnClickListener(this);
            if (name != null) {
                ((EditText) view.findViewById(R.id.row_invitee_detail_tvEmail)).setText("" + name);
            }
            layout.addView(view);
        }

        public void setEditable(boolean editable) {
            for (int i = 1; i < layout.getChildCount(); i++) {
                ((LinearLayout) layout.getChildAt(i)).getChildAt(1).setVisibility(editable ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (layout.getChildCount() > 1)
                synchronized (this) {
                    removeView(Integer.parseInt(v.getTag() + ""));
                }
        }
    }

}
