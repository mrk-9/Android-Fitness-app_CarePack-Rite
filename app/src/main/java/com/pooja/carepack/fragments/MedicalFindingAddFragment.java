package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.GalleryImageAdapter;
import com.pooja.carepack.async.PostFileAsync;
import com.pooja.carepack.model.ModelMedicalFinding;
import com.pooja.carepack.model.ModelMedicalFindingInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class MedicalFindingAddFragment extends BaseFragment implements LibPostListner, View.OnClickListener, MainActivity.OnDateDialogListener, GalleryImageAdapter.OnAddImageClickListener, MainActivity.OnImageChooserListener, PostFileAsync.PostFileListener {

    public int menu_item = 0;
    private View view;
    private RecyclerView lvImageList;
    private Menu menu;
    private MyPrefs prefs;
    private EditText etDesc, etDesign, etDoctor, etHospital, etDoctorPhone, etDoctorEmail, etDateFinding;
    private ModelMedicalFindingInfo modelMedicalFindingInfo;
    private String medicalFindingID;
    private ModelMedicalFinding modelMedicalFinding;
    private Animation shakeAnim;
    private LinearLayout llDateFinding;
    private GalleryImageAdapter adtGallery;
    private ArrayList<ModelMedicalFindingInfo.Images> images;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_medical_finding_add, container, false);
        intiUI();
        Bundle bundle = getArguments();
        if (bundle != null) {
            medicalFindingID = bundle.getString("MedicalFindingID");
            Log.d("MedicalFindingID   " + medicalFindingID);
        }
        if (medicalFindingID != null)
            editMedicalFindingInfo(medicalFindingID);
        else {
            adtGallery = new GalleryImageAdapter(getActivity(), images, MedicalFindingAddFragment.this);
            lvImageList.setAdapter(adtGallery);
        }

        setHasOptionsMenu(true);

        return view;
    }

    private void intiUI() {
        images = new ArrayList<>();
        prefs = new MyPrefs(getActivity());
        etDesc = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDesc);
        etDesign = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDesign);
        etDoctor = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDoctor);
        etDoctorEmail = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDoctorEmail);
        etDoctorPhone = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDoctorPhone);
        etHospital = (EditText) view.findViewById(R.id.frg_medical_finding_add_etHospital);
        etDateFinding = (EditText) view.findViewById(R.id.frg_medical_finding_add_etDateFinding);
        llDateFinding = (LinearLayout) view.findViewById(R.id.frg_medical_finding_add_llDateFinding);
        llDateFinding.setOnClickListener(this);
        lvImageList = (RecyclerView) view.findViewById(R.id.frg_medical_finding_add_rvGallery);
        lvImageList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }

    private void editMedicalFindingInfo(String id) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(MedicalFindingAddFragment.this, new ModelMedicalFindingInfo(), getActivity(), hashMap, WebServicesConst.MEDICAL_FINDING + "/" + prefs.get(MyPrefs.keys.ID) + "/" + id, WebServicesConst.RES.EDIT_MEDICALFINDING_INFO, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == WebServicesConst.RES.ADD_MEDICALFINDING_INFO) {
            modelMedicalFinding = (ModelMedicalFinding) clsGson;
            if (modelMedicalFinding != null) {
                int status = modelMedicalFinding.getStatus();
                if (status == 200) {
                    Log.d(modelMedicalFinding.getMessage().toString());
                    getActivity().onBackPressed();
                } else {
                    toast(modelMedicalFinding.getMessage().toString());
                    Log.d(modelMedicalFinding.getMessage().toString());
                }

            }
        }
        if (requestCode == WebServicesConst.RES.EDIT_MEDICALFINDING_INFO) {
            modelMedicalFindingInfo = (ModelMedicalFindingInfo) clsGson;
            if (modelMedicalFindingInfo != null) {
                int status = modelMedicalFindingInfo.status;
                if (status == 200) {
                    Log.d(modelMedicalFindingInfo.message.toString());
                    loadData();
                } else {
                    toast(modelMedicalFindingInfo.message.toString());
                    Log.d(modelMedicalFindingInfo.message.toString());

                }
            }
        }
    }

    private void loadData() {
        etDesign.setText(modelMedicalFindingInfo.info.title);
        etDesc.setText(modelMedicalFindingInfo.info.description);
        etDoctor.setText(modelMedicalFindingInfo.info.doctor);
        etDoctorPhone.setText(modelMedicalFindingInfo.info.doctor_phone);
        etHospital.setText(modelMedicalFindingInfo.info.hospital);
        etDoctorEmail.setText(modelMedicalFindingInfo.info.doctor_email);
        etDateFinding.setText(modelMedicalFindingInfo.info.finding_date);

        lvImageList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        images.addAll(modelMedicalFindingInfo.images);
        adtGallery = new GalleryImageAdapter(getActivity(), images, MedicalFindingAddFragment.this);
        lvImageList.setAdapter(adtGallery);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_medical_finding_add_llDateFinding:
                mainActivity.showDateDialog(MedicalFindingAddFragment.this);
                break;
        }
    }

    @Override
    public void onPositiveActionClicked(DatePickerDialog dialog) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = dialog.getFormattedDate(format);
        etDateFinding.setText(selectedDate);
    }

    @Override
    public void onAddImageClick(View v) {
        if (v.getId() == R.id.row_imagemedical_ivGallery) {
            int position = Integer.parseInt("" + v.getTag());
            Log.d(position + " ==" + adtGallery.getItemCount());
            if (position == adtGallery.getItemCount() - 1)
                mainActivity.setPhotoPickerDialog(this, "MedicalFinding");
            else {
                adtGallery.removeData(position);
            }
        }
    }

    @Override
    public void onImageChoose(String path) {
        try {
            adtGallery.addData(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFileSuccess(Object clsGson, int requestCode) {
        getActivity().onBackPressed();
    }

    @Override
    public void onPostFileError(int requestCode) {
        toast("Please try again");
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
                if (valid()) {
                    saveMedicalFindingData();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void saveMedicalFindingData() {
        HashMap<String, String> fileparam = new HashMap<>();
        ArrayList<String> imageUriList = adtGallery.getImageUri();
        if (imageUriList != null && imageUriList.size() > 0) {
            for (String path : imageUriList) {
                String uri = Utility.getStringPathFromURI(getActivity(), Uri.parse(path));
                if (!uri.equalsIgnoreCase("")) {
                    fileparam.put("image[]", uri);
                }
            }
        }
        HashMap<String, String> param = new HashMap<>();
        if (medicalFindingID != null)
            param.put("id", medicalFindingID);
        param.put("action", "submit_medical_finding");
        param.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        param.put("userid", prefs.get(MyPrefs.keys.ID));
        param.put("title", etDesign.getText().toString());
        param.put("doctor", etDoctor.getText().toString());
        param.put("finding_date", etDateFinding.getText().toString());
        param.put("hospital", etHospital.getText().toString());
        param.put("doctor_phone", etDoctorPhone.getText().toString());
        param.put("doctor_email", etDoctorEmail.getText().toString());
        param.put("description", etDesc.getText().toString());
        new PostFileAsync(getActivity(), new ModelMedicalFinding(), param, fileparam, 1, MedicalFindingAddFragment.this).execute(WebServicesConst.MEDICAL_FINDING);
    }

    private boolean valid() {
        if (etDesc.getText().length() == 0) {
            etDesc.startAnimation(shakeAnim);
            etDesc.requestFocus();
            toast(R.string.toast_invalid_, etDesc.getHint().toString());
            return false;
        } else if (etDesign.getText().length() == 0) {
            etDesign.startAnimation(shakeAnim);
            etDesign.requestFocus();
            toast(R.string.toast_invalid_, etDesign.getHint().toString());
            return false;
        } else if (etHospital.getText().length() == 0) {
            etHospital.startAnimation(shakeAnim);
            etHospital.requestFocus();
            toast(R.string.toast_invalid_, etHospital.getHint().toString());
            return false;
        } else if (etDoctor.getText().length() == 0) {
            etDoctor.startAnimation(shakeAnim);
            etDoctor.requestFocus();
            toast(R.string.toast_invalid_, etDoctor.getHint().toString());
            return false;
        } else if (etDoctorPhone.getText().length()<10) {
            etDoctorPhone.startAnimation(shakeAnim);
            etDoctorPhone.requestFocus();
            toast(R.string.toast_invalid_phone);
            return false;
        } else if (etDoctorEmail.getText().length() == 0 || !Utility.isEmailValid(etDoctorEmail.getText().toString())) {
            etDoctorEmail.startAnimation(shakeAnim);
            etDoctorEmail.requestFocus();
            toast(R.string.toast_invalid_email);
            return false;
        } else if (etDateFinding.getText().length() == 0) {
            etDateFinding.startAnimation(shakeAnim);
            etDateFinding.requestFocus();
            toast(R.string.toast_invalid_, etDateFinding.getHint().toString());
            return false;
        }
        return true;
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

}
