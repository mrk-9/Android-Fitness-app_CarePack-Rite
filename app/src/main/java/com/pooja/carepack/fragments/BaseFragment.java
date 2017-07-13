/*
 * 
 */
package com.pooja.carepack.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.utils.MyPrefs;


public abstract class BaseFragment extends Fragment {

    public View view;
    public Menu menu;

    public MainActivity mainActivity;
    public MyPrefs prefs;

    public static void toast(Context context, String msg) {
        try {
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            prefs = new MyPrefs(getActivity());
            mainActivity = ((MainActivity) getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toast(String msg) {
        try {
            Log.d("Toast LOG" + msg);
            Snackbar.make(mainActivity.getCoordinatorLL(), "" + msg, Snackbar.LENGTH_SHORT).show();
        } catch (Resources.NotFoundException e) {
            Log.d("Resource not found");
        } catch (Exception e) {
            Log.d("" + e.getMessage());
        }
    }

    public void toast(int resourceId) {
        try {
            Snackbar.make(mainActivity.getCoordinatorLL(), "" + getString(resourceId), Snackbar.LENGTH_SHORT).show();
        } catch (Resources.NotFoundException e) {
            Log.d("Resource not found");
        } catch (Exception e) {
            Log.d("" + e.getMessage());
        }
    }

    public void toast(int resourceId,String... args) {
        try {
            Snackbar.make(mainActivity.getCoordinatorLL(), "" + getString(resourceId,args), Snackbar.LENGTH_SHORT).show();
        } catch (Resources.NotFoundException e) {
            Log.d("Resource not found");
        } catch (Exception e) {
            Log.d("" + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }
    }


    public static class Log {
        public static void d(int logText) {
            android.util.Log.d("Tag", logText + "");
        }

        public static void d(String logText) {
            android.util.Log.d("Tag", logText);
        }
    }


}