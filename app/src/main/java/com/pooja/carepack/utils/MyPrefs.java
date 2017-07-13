package com.pooja.carepack.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pooja.carepack.R;
import com.pooja.carepack.model.ModelLogin;

public class MyPrefs {

    //    public static String USERNAME;
    SharedPreferences myPrefs;
    SharedPreferences.Editor prefEditor;
    Context context;

    // ------------------------------------------------------------------costructor
    public MyPrefs(Context context) {
        this.context = context;
        myPrefs = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        prefEditor = myPrefs.edit();
    }

    public void set(String key, String value) {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    // -----------------------------------------------------------------

    public void set(keys key, String value) {
        Log.d("tag", "Adding Prefs : " + key.name() + " > " + value);
        prefEditor.putString(key.name(), value);
        prefEditor.commit();
    }

    public void set(keys key, int value) {
        Log.d("tag", "Adding Prefs : " + key.name() + " > " + value);
        prefEditor.putInt(key.name(), value);
        prefEditor.commit();
    }

    public String get(keys key) {
        return myPrefs.getString(key.name(), "");
    }

    public ModelLogin getUser(String fragmentState) {
        ModelLogin model = new ModelLogin();
        if (fragmentState.equals("SettingProfileFragment")) {
            String user = myPrefs.getString(keys.USER_MODEL.name(), null);
            try {
                model = new Gson().fromJson(user, ModelLogin.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    public ModelLogin getUser() {
        ModelLogin model = new ModelLogin();

        String user = myPrefs.getString(keys.USER_MODEL.name(), null);
        try {
            model = new Gson().fromJson(user, ModelLogin.class);
            if (model.general.birthdate.toString() == "" || model.general.bloodgroup.toString() == "" || model.general.firstname.toString() == "" ||
                    model.general.lastname.toString() == "" || model.general.gender.toString() == "" || model.general.mobile.toString() == "") {
                Log.d("TAG", " MODEL NULL PREF");
                return null;
            }

//            if (model.general.birthdate.toString() == "" || model.general.bloodgroup.toString() == "" || model.general.country.toString() == "" ||
//                    model.general.city.toString() == "" || model.general.countryid.toString() == "" || model.general.firstname.toString() == "" ||
//                    model.general.lastname.toString() == "" || model.general.gender.toString() == "" || model.general.postalcode.toString() == "" ||
//                    model.general.homephone.toString() == "" || model.general.street1.toString() == "" || model.general.street2.toString() == "" ||
//                    model.general.mobile.toString() == "") {
//                return null;
//            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

//    public void setUser(ModelLogin model) {
//        if (model == null) {
//            set(keys.EMAIL, "");
//            set(keys.IMAGE, "");
//            set(keys.USERNAME, "");
//            set(keys.ID, "");
//            set(keys.FILLEDPROFILE, "");
//
//            set(keys.USER_MODEL.name(), null);
//            set(keys.REMEMBER.name(), "");
//        } else {
//            set(keys.EMAIL, model.general.email);
//            set(keys.IMAGE, model.general.profile_image);
//            set(keys.USERNAME, model.general.firstname + " " + model.general.lastname);
//            set(keys.ID, model.general.id);
//
//            String user = new Gson().toJson(model);
//            set(keys.USER_MODEL.name(), user);
//        }
//    }


    public void setUser(ModelLogin model) {
        if (model == null) {
            set(keys.IMAGE, "");
            set(keys.USERNAME, "");
            set(keys.ID, "");
            set(keys.FILLEDPROFILE, "");
            set(keys.USER_MODEL.name(), null);
//            set(keys.REMEMBER.name(), "");
            if (get(keys.REMEMBER).length() == 0) {
                set(MyPrefs.keys.EMAIL, "");
                set(MyPrefs.keys.PASSWORD, "");
            }
        } else {
            set(keys.EMAIL, model.general.email);
            if (model.general.fbid.length() == 0)
                set(keys.IMAGE, model.general.profile_image);
            set(keys.USERNAME, model.general.firstname + " " + model.general.lastname);
            set(keys.ID, model.general.id);
            set(keys.GENDER,model.general.gender);
            String user = new Gson().toJson(model);
            set(keys.USER_MODEL.name(), user);
        }
    }


    public int getInt(keys key) {
        return myPrefs.getInt(key.name(), 0);
    }

    public String get(String key) {
        return myPrefs.getString(key, "");
    }

    public enum keys {
        REMEMBER, GCMVERSION, GCMKEY, ID, USERNAME, EMAIL, IMAGE, USER_MODEL, LAT, LNG, PASSWORD, COUNTRYID, FILLEDPROFILE, HEIGHT, WEIGHT,GENDER,ISONLINE
    }

}
