package com.pooja.carepack.dialogs;

import android.annotation.SuppressLint;
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
import com.pooja.carepack.model.ModelReminder;
import com.pooja.carepack.utils.MyPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinay Rathod on 23/12/15.
 */
@SuppressLint("ValidFragment")
public class ReminderDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private final List<ModelReminder.Reminder> reminder;
    ArrayAdapter adtCountryList;
    private ListView lvCountry;
    private HashMap<String, String> countryMap;
    private MyPrefs prefs;
    private Dialog view;
    private String selectedCountryID, selectedCountryName;
    private EditText etSearch;
    private ArrayList<String> countrySearchList;
    public ReminderDialog(List<ModelReminder.Reminder> reminder) {
        this.reminder = reminder;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = new Dialog(getActivity(), R.style.AppTheme);
        view.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view.setContentView(R.layout.country_dialog);
        view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        view.show();

        initUI(view);

        return view;
    }

    private void initUI(Dialog view) {
        prefs = new MyPrefs(getActivity());
        lvCountry = (ListView) view.findViewById(R.id.country_dialog_lstCountry);
        etSearch = (EditText) view.findViewById(R.id.dlg_country_search);
        etSearch.setVisibility(View.GONE);
        lvCountry.setOnItemClickListener(this);
//        lvCountry.setAdapter(adtCountryList);
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
        try {
            countryMap = new HashMap<>();
            countrySearchList = new ArrayList<String>();
            if (reminder != null) {
                Log.d("TAG", "reminderlist response  " + reminder.get(0).title + "  " + reminder.size());
                if (reminder.size() > 0) {
                    for (int pos = 0; pos < reminder.size(); pos++) {
                        countryMap.put("" + reminder.get(pos).title, "" + reminder.get(pos).minute);
                        countrySearchList.add(reminder.get(pos).title);
                    }
                    for (int pos = 0; pos < countrySearchList.size(); pos++) {
                        Log.d("TAG", "country name  " + countrySearchList.get(pos));
                    }
                    adtCountryList = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, countrySearchList);
                    lvCountry.setAdapter(adtCountryList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TAG", "onItemclick on countryList  " + countrySearchList.get(position) + "    " + countryMap.get(countrySearchList.get(position)));
        selectedCountryName = countrySearchList.get(position);
        selectedCountryID = countryMap.get(countrySearchList.get(position));
        ReminderDialog.this.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent intent = new Intent();
        if (selectedCountryID != null && selectedCountryName != null)
            intent.putExtra("reminderID", selectedCountryID);
        intent.putExtra("reminderName", selectedCountryName);
        getTargetFragment().onActivityResult(100, 0, intent);
    }
}
