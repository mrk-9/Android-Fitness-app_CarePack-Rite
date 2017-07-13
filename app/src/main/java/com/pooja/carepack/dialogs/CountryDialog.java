package com.pooja.carepack.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelCountryList;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yudiz on 23/12/15.
 */
public class CountryDialog extends DialogFragment implements LibPostListner, AdapterView.OnItemClickListener {

    ArrayAdapter adtCountryList;
    private ListView lvCountry;
    private ModelCountryList modelCountryList;
    private HashMap<String, String> countryMap;
    private MyPrefs prefs;
    private Dialog view;
    private String selectedCountryID, selectedCountryName;
    private EditText etSearch;
    private ArrayList<String> countrySearchList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = new Dialog(getActivity(), R.style.AppTheme);
        view.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view.setContentView(R.layout.country_dialog);
        view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        view.show();

        initUI(view);
        callCountryList();

        return view;
    }

    private void initUI(Dialog view) {
        prefs = new MyPrefs(getActivity());
        lvCountry = (ListView) view.findViewById(R.id.country_dialog_lstCountry);
        etSearch = (EditText) view.findViewById(R.id.dlg_country_search);
        lvCountry.setOnItemClickListener(this);
        countryMap = new HashMap<>();
        countrySearchList = new ArrayList<String>();
        etSearch.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        countrySearchList.clear();

                        for (String name : countryMap.keySet()) {
                            if (name.toLowerCase().contains(s.toString().toLowerCase()))
                                countrySearchList.add(name);
                        }
                        adtCountryList = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, countrySearchList);
                        lvCountry.setAdapter(adtCountryList);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
    }

    public void callCountryList() {
        Log.d("TAG", " countryList ws call");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        new PostLibResponse(CountryDialog.this, new ModelCountryList(), getActivity(), hashMap, WebServicesConst.COUNTRY_LIST, WebServicesConst.RES.COUNTRY_LIST, true, true);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        try {
            modelCountryList = (ModelCountryList) clsGson;
            if (modelCountryList != null) {
                Log.d("TAG", "countrylist response  " + modelCountryList.getCountryInfo().get(0).getCountryname() + "   " + modelCountryList.getMessage() + "  " + modelCountryList.getStatus() + "  " + modelCountryList.getCountryInfo().size());
                if (modelCountryList.getCountryInfo().size() > 0) {
//                    String test = "";
                    for (int pos = 0; pos < modelCountryList.getCountryInfo().size(); pos++) {
//                        test += modelCountryList.getCountryInfo().get(pos).getCountryname() + ", ";
                        countryMap.put("" + modelCountryList.getCountryInfo().get(pos).getCountryname(), "" + modelCountryList.getCountryInfo().get(pos).getId());
                        countrySearchList.add(modelCountryList.getCountryInfo().get(pos).getCountryname());
                    }
//                    Log.d("","" + test);
                    for (int pos = 0; pos < countrySearchList.size(); pos++) {
                        Log.d("TAG", "country name  " + countrySearchList.get(pos));
                    }
                    adtCountryList = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, countrySearchList);
                    lvCountry.setAdapter(adtCountryList);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TAG", "onItemclick on countryList  " + countrySearchList.get(position) + "    " + countryMap.get(countrySearchList.get(position)));
        selectedCountryName = countrySearchList.get(position);
        selectedCountryID = countryMap.get(countrySearchList.get(position));
        CountryDialog.this.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent intent = new Intent();
        if (selectedCountryID != null && selectedCountryName != null)
            intent.putExtra("CountryID", selectedCountryID);
        intent.putExtra("CountryName", selectedCountryName);
        getTargetFragment().onActivityResult(100, 0, intent);
    }
}
