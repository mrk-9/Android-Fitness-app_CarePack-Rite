package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TreatmentInviteeAddFragment extends BaseFragment implements View.OnClickListener {

    public int menu_item = 0;
    private View view;
    private TextView tvAdd, tvEdit;
    private TeethDetailAdapter mAdapter;
    private LinearLayout llDetail;
    private ArrayList<CharSequence> invitees;
    private Menu menu;
    private Animation shakeAnim;
    private int result = Activity.RESULT_CANCELED;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_treatment_invitee_add, container, false);
        setHasOptionsMenu(true);
        intent = new Intent();
        invitees = getArguments().getCharSequenceArrayList("invitees");

        llDetail = (LinearLayout) view.findViewById(R.id.frg_invitee_detail_layout);

        tvAdd = (TextView) view.findViewById(R.id.invitee_detail_tv_add);
        tvAdd.setOnClickListener(this);

        tvEdit = (TextView) view.findViewById(R.id.invitee_detail_tv_edit);
        tvEdit.setOnClickListener(this);
        mAdapter = new TeethDetailAdapter(getActivity(), llDetail);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        if (invitees != null && invitees.size() > 0) {
            mAdapter.addAll(invitees);
        } else
            mAdapter.add(null);
        return view;
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

    @Override
    public void onPause() {
        super.onPause();
        if (menu != null && menu.size() > menu_item)
            menu.getItem(menu_item).setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null && menu.size() > menu_item) {
            menu.getItem(menu_item).setVisible(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_save:
                if (mAdapter.getList() != null) {
                    result = Activity.RESULT_OK;
                    intent.putCharSequenceArrayListExtra("invitees", mAdapter.getList());
                    mainActivity.onBackPressed();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getTargetFragment().onActivityResult(200, result, intent);
    }

    //    private boolean valid() {
//        for (CharSequence email : mAdapter.getList()) {
//            if (!Utility.isEmailValid(email.toString())){
//                toast(R.string.toast_invalid_email);
//                return false;
//            }
//        }
//        return true;
//    }

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
                    if (!Utility.isEmailValid(desc.getText().toString())) {
                        toast(R.string.toast_invalid_email);
                        desc.startAnimation(shakeAnim);
                        desc.requestFocus();
                        return null;
                    }
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