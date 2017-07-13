package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.dialogs.CountryDialog;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class SettingProfileFragment extends BaseFragment implements LibPostListner, View.OnClickListener, MainActivity.OnDateDialogListener {

    public int menu_item = 0;
    //    private TextView tvCountry, tvBloodGroup, tvGender, tvBirthDate;
    private View view;
    private EditText etEmail, etFirstName, etLastName, etStreet1, etCity, etZIP, etPhoneHome, etCellPhone, etStreet2, etCountry, etBloodGroup, etGender, etBirthDate;
    private ModelLogin modelLogin;
    private MyPrefs prefs;
    private Menu menu;
    private LinearLayout llCountry, llBloddGroup, llGender, llBirthDate;
    private String selectedCountryID = null, selectedBloodgroup = null, selectedGender = null, selectedBirthDate = null;
    private com.rey.material.app.Dialog.Builder builder = null;
    private Animation shakeAnim;
    private String[] numArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String tempS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_setting_profile, container, false);
        intiUI(view);
        prefs = new MyPrefs(getActivity());
//        if (prefs.getUser("SettingProfileFragment") != null) {
//            modelLogin = prefs.getUser("SettingProfileFragment");
//            setData();
//            onGetDetails();
//        }
        if (prefs.getUser() != null) {
            modelLogin = prefs.getUser();
            setData();
            onGetDetails();
        }
        android.util.Log.d("TAG", "setting profile frg");

//        setData();

        setHasOptionsMenu(true);

        etCity.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        } else {
                            etCity.setText("");
                            etCity.setText(tempS);
                            etCity.setSelection(etCity.getText().length());
                            return tempS;
                        }
                    }
                }
        });

        etCity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempS = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        return view;
    }

    private void intiUI(View view) {
        prefs = new MyPrefs(getActivity());

        etEmail = (EditText) view.findViewById(R.id.frg_general_info_etEmail);
        etEmail.setText(prefs.get(MyPrefs.keys.EMAIL));
        etFirstName = (EditText) view.findViewById(R.id.frg_general_info_etFirstName);
        etLastName = (EditText) view.findViewById(R.id.frg_general_info_etLastName);
        etStreet1 = (EditText) view.findViewById(R.id.frg_general_info_etStreet1);
        etCity = (EditText) view.findViewById(R.id.frg_general_info_etCity);
        etZIP = (EditText) view.findViewById(R.id.frg_general_info_etZIP);
        etPhoneHome = (EditText) view.findViewById(R.id.frg_general_info_etPhoneHome);
        etCellPhone = (EditText) view.findViewById(R.id.frg_general_info_etCellHome);
        etStreet2 = (EditText) view.findViewById(R.id.frg_general_info_etStreet2);

        llCountry = (LinearLayout) view.findViewById(R.id.frg_general_info_llCountry);
        llBloddGroup = (LinearLayout) view.findViewById(R.id.frg_general_info_llbloodGroup);
        llGender = (LinearLayout) view.findViewById(R.id.frg_general_info_llGender);
        llBirthDate = (LinearLayout) view.findViewById(R.id.frg_general_info_llbirthdate);

        etCountry = (EditText) view.findViewById(R.id.frg_general_info_etCountry);
        etGender = (EditText) view.findViewById(R.id.frg_general_info_etGender);
        etBloodGroup = (EditText) view.findViewById(R.id.frg_general_info_etbloodGroup);
        etBirthDate = (EditText) view.findViewById(R.id.frg_general_info_etbirthdate);
        shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        etBirthDate.setOnClickListener(this);

        llCountry.setOnClickListener(this);
        llBloddGroup.setOnClickListener(this);
        llGender.setOnClickListener(this);
        llBirthDate.setOnClickListener(this);
    }

    private void setData() {

        if (modelLogin.general.email != "")
            etEmail.setText(modelLogin.general.email);
        if (modelLogin.general.firstname != "")
            etFirstName.setText(modelLogin.general.firstname);
        if (modelLogin.general.lastname != "")
            etLastName.setText(modelLogin.general.lastname);
        if (modelLogin.general.bloodgroup != "")
            etBloodGroup.setText(modelLogin.general.bloodgroup);
        if (modelLogin.general.street1 != "")
            etStreet1.setText(modelLogin.general.street1);
        if (modelLogin.general.street2 != "")
            etStreet2.setText(modelLogin.general.street2);
        if (modelLogin.general.city != "")
            etCity.setText(modelLogin.general.city);
        if (modelLogin.general.postalcode != "")
            etZIP.setText(modelLogin.general.postalcode);
        if (modelLogin.general.country != "")
            etCountry.setText(modelLogin.general.country);
        if (modelLogin.general.homephone != "")
            etPhoneHome.setText(modelLogin.general.homephone);
        if (modelLogin.general.mobile != "")
            etCellPhone.setText(modelLogin.general.mobile);
        if (modelLogin.general.gender != "")
            etGender.setText(modelLogin.general.gender);
        if (modelLogin.general.birthdate != "")
            etBirthDate.setText(modelLogin.general.birthdate);

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
                if (valid())
                    onSaveDetails();
                break;
            default:
                break;
        }
        return true;
    }


    private boolean valid() {
        if (etFirstName.getText().length() == 0) {
            etFirstName.startAnimation(shakeAnim);
            etFirstName.requestFocus();
            toast(R.string.toast_invalid_, etFirstName.getHint().toString());
            return false;
        } else if (etLastName.getText().length() == 0) {
            etLastName.startAnimation(shakeAnim);
            etLastName.requestFocus();
            toast(R.string.toast_invalid_, etLastName.getHint().toString());
            return false;
        }
//        else if (etStreet1.getText().length() == 0) {
//            etStreet1.startAnimation(shakeAnim);
//            etStreet1.requestFocus();
//            toast(R.string.toast_invalid_,etStreet1.getHint().toString());
//            return false;
//        } else if (etStreet2.getText().length() == 0) {
//            etStreet2.startAnimation(shakeAnim);
//            etStreet2.requestFocus();
//            toast(R.string.toast_invalid_,etStreet2.getHint().toString());
//            return false;
//        } else if (etZIP.getText().length() != 6) {
//            etZIP.startAnimation(shakeAnim);
//            etZIP.requestFocus();
//            toast(R.string.toast_invalid_zip);
//            return false;
//        } else if (etCity.getText().length() == 0) {
//            etCity.startAnimation(shakeAnim);
//            etCity.requestFocus();
//            toast(R.string.toast_invalid_,etCity.getHint().toString());
//            return false;
//        } else if (etCountry.getText().length() == 0) {
//            etCountry.startAnimation(shakeAnim);
//            etCountry.requestFocus();
//            toast(R.string.toast_invalid_,etCountry.getHint().toString());
//            return false;
//        } else if (etPhoneHome.getText().length() < 10) {
//            etPhoneHome.startAnimation(shakeAnim);
//            etPhoneHome.requestFocus();
//            toast(R.string.toast_invalid_phone);
//            return false;
//        }
        else if (etCellPhone.getText().length() < 10) {
            etCellPhone.startAnimation(shakeAnim);
            etCellPhone.requestFocus();
            toast(R.string.toast_invalid_phone);
            return false;
        } else if (etBirthDate.getText().length() == 0) {
            etBirthDate.startAnimation(shakeAnim);
            etBirthDate.requestFocus();
            toast(R.string.toast_invalid_selection, etBirthDate.getHint().toString());
            return false;
        } else if (etBloodGroup.getText().length() == 0) {
            etBloodGroup.startAnimation(shakeAnim);
            etBloodGroup.requestFocus();
            toast(R.string.toast_invalid_selection, etBloodGroup.getHint().toString());
            return false;
        } else if (etGender.getText().length() == 0) {
            etGender.startAnimation(shakeAnim);
            etGender.requestFocus();
            toast(R.string.toast_invalid_selection, etGender.getHint().toString());
            return false;
        }
        return true;
    }

    private void onGetDetails() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(SettingProfileFragment.this, new ModelLogin(), getActivity(), hashMap, WebServicesConst.USER_PROFILE + "/" + prefs.get(MyPrefs.keys.ID) + "/" + prefs.get(MyPrefs.keys.GCMKEY), WebServicesConst.RES.GET_USER_PROFILE, true, true);
    }

    private void onSaveDetails() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        android.util.Log.e("TAG", "Strret1    " + etStreet1.getText().toString());
        if (etEmail.getText().toString().length() > 0)
            hashMap.put("email", etEmail.getText().toString());
        hashMap.put("firstname", etFirstName.getText().toString());
        hashMap.put("lastname", etLastName.getText().toString());
        hashMap.put("action", "submit_user_profile");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("gender", etGender.getText().toString());
        hashMap.put("birthdate", etBirthDate.getText().toString());
        hashMap.put("bloodgroup", etBloodGroup.getText().toString());
//        hashMap.put("countryid", selectedCountryID);
        hashMap.put("street1", etStreet1.getText().toString());
        hashMap.put("street2", etStreet2.getText().toString());
        hashMap.put("city", etCity.getText().toString());
        hashMap.put("postalcode", etZIP.getText().toString());
        hashMap.put("homephone", etPhoneHome.getText().toString());
        hashMap.put("mobile", etCellPhone.getText().toString());
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("device_type", "android");
        if (prefs.get(MyPrefs.keys.COUNTRYID) != null)
            hashMap.put("countryid", prefs.get(MyPrefs.keys.COUNTRYID));// selectedCountryID);
        else
            hashMap.put("countryid", "");


        new PostLibResponse(SettingProfileFragment.this, new ModelLogin(), getActivity(), hashMap, WebServicesConst.UPDATE_PROFILE, WebServicesConst.RES.UPDATE_PROFILE, true, true);
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelLogin = (ModelLogin) clsGson;
        if (requestCode == WebServicesConst.RES.UPDATE_PROFILE) {
            if (modelLogin != null) {
                int Status = modelLogin.status;
                if (Status == 200) {
                    prefs.setUser(modelLogin);
                    prefs.set(MyPrefs.keys.FILLEDPROFILE, "filled");
                    ((MainActivity) getActivity()).setUserData();
                    getActivity().onBackPressed();
                } else if (Status == 404) {
                    toast(modelLogin.message);
                }

            }
        } else if (requestCode == WebServicesConst.RES.GET_USER_PROFILE) {
            int Status = modelLogin.status;
            if (Status == 200) {
                prefs.setUser(modelLogin);
                setData();
            } else if (Status == 404) {
                toast(modelLogin.message);
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_general_info_llCountry:
                DialogFragment dialogFrag = new CountryDialog();
                dialogFrag.setTargetFragment(SettingProfileFragment.this, 100);
                dialogFrag.show(getChildFragmentManager().beginTransaction(), "countryList");
                break;
            case R.id.frg_general_info_llbloodGroup:
                showDialog("BloodGroup");
                break;
            case R.id.frg_general_info_llGender:
                showDialog("Gender");
                break;
            case R.id.frg_general_info_llbirthdate:
                mainActivity.showFutureDateDialog(SettingProfileFragment.this);
                break;
        }
    }

    private void showDialog(String type) {
        final String dialogType = type;
        ArrayAdapter adtItems = null;
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gender_bloodgroup_dialog);

        ListView lvData = (ListView) dialog.findViewById(R.id.gender_bloodgroup_dialog_lv);
        if (dialogType == "BloodGroup") {
            adtItems = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bloodgroup));
        } else if (dialogType == "Gender") {
            adtItems = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        }
        if (adtItems != null)
            lvData.setAdapter(adtItems);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dialogType == "BloodGroup") {
                    etBloodGroup.setText(getResources().getStringArray(R.array.bloodgroup)[position].toString());
                    selectedBloodgroup = getResources().getStringArray(R.array.bloodgroup)[position].toString();
                    dialog.dismiss();
                } else if (dialogType == "Gender") {
                    etGender.setText(getResources().getStringArray(R.array.gender)[position].toString());
                    selectedGender = getResources().getStringArray(R.array.gender)[position].toString();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null && data.getExtras() != null) {
//                Log.d("TAG", "ongenral fragment selected country name   " + data.getExtras().getString("CountryID") + "  " + data.getExtras().getString("CountryName"));
                if (data.getExtras().getString("CountryName") != null && data.getExtras().getString("CountryID") != null) {
                    etCountry.setText(data.getExtras().getString("CountryName"));
                    selectedCountryID = data.getExtras().getString("CountryID");
                    prefs.set(MyPrefs.keys.COUNTRYID, selectedCountryID);
                }
            }
        }

    }


    @Override
    public void onPositiveActionClicked(DatePickerDialog dialog) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = dialog.getFormattedDate(format);
        selectedBirthDate = date.toString();
        etBirthDate.setText(date.toString());
    }
}
