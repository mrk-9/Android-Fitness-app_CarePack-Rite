package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.adapter.SettingsAdapter;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.utils.MyPrefs;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class SettingsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private MyPrefs prefs;
    private View view;
    private ListView lvSetting;
    private SettingsAdapter adtSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_settings, container, false);
        mainActivity.setTitle("Settings");
        prefs = new MyPrefs(getActivity());
        if (prefs.getUser() == null && prefs.get(MyPrefs.keys.FILLEDPROFILE).length() == 0) {
            android.util.Log.d("TAG", "setting frg");
            mainActivity.replaceFragment(new SettingProfileFragment(), "Profile");
        }
        intiUI();
        return view;
    }

    private void intiUI() {
        lvSetting = (ListView) view.findViewById(R.id.frg_setting_lv);
        lvSetting.setOnItemClickListener(this);
        ModelLogin.General general = prefs.getUser("SettingProfileFragment").general;
        String fbid = "";
        if (general != null && general.fbid != null) {
            fbid = general.fbid;
        }
        adtSettings = new SettingsAdapter(getActivity(), getResources().getStringArray(R.array.settings), fbid);
        lvSetting.setAdapter(adtSettings);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = getResources().getStringArray(R.array.settings)[position];

        switch (position) {
            case 0:
                mainActivity.replaceFragment(new SettingProfileFragment(), "Profile");
                break;
            case 1:
                Fragment frg = new SettingBodyFragment();
                Bundle bundle = new Bundle();
                android.util.Log.e("TAG","GENDER  setting   "+prefs.get(MyPrefs.keys.GENDER));
                if (prefs.get(MyPrefs.keys.GENDER).equals("female"))
                    bundle.putInt("gender", 0);
                else
                    bundle.putInt("gender", 1);
                frg.setArguments(bundle);
                mainActivity.replaceFragment(frg, title);
                break;
            case 2:
                mainActivity.replaceFragment(new SettingFamilyDoctorInfoFragment(), title);
                break;
//            case 3:
//                mainActivity.replaceFragment(new SettingEmergencyFragment(), title);
//                break;
            case 3:
                mainActivity.replaceFragment(new SettingKinshipFragment(), title, "kinship", "0");
                break;
            case 4:
                mainActivity.replaceFragment(new SettingKinshipFragment(), title, "kinship", "1");
                break;
            case 5:
                mainActivity.replaceFragment(new SettingWhatsAppInfoFragment(), title);
                break;
            case 6:
//                mainActivity.replaceFragment(new ReadandWriteDataFragment(), title);
//                break;
//            case 8:
                mainActivity.replaceFragment(new SettingPasswordFragment(), title);
                break;

        }
    }
}
