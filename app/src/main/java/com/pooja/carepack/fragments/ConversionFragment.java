package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.EmptyAdapter;
import com.pooja.carepack.adapter.MessageConversionAdapter;
import com.pooja.carepack.model.ModelConversion;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class ConversionFragment extends BaseFragment implements LibPostListner, View.OnClickListener {

    public int menu_item = 2;
    private View view;
    private ListView lv;
    private FloatingActionButton newmessage;
    private Menu menu;
    private EditText etSearch;
    private ModelConversion modelConversion;
    private String serachedText;
    private ArrayList<ModelConversion.Thread> convertionList,tempConvertionlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_message, container, false);
        initUI();
        setHasOptionsMenu(true);
        getMessageList();
//        searchonConversion();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serachedText = String.valueOf(s);
                android.util.Log.e("TAG", "text searxh   ***  " + serachedText);

                    for (int pos = 0; pos < convertionList.size(); pos++) {
                        if (convertionList.get(pos).getFullname().toLowerCase().contains(serachedText)) {
                            android.util.Log.e("TAG", "text searxh match  ***  " + serachedText);
                            tempConvertionlist.add(convertionList.get(pos));
                        }
                    }

                    if (serachedText.equals("") || serachedText.equals(null)) {
                        android.util.Log.e("TAG", "text searxh null  ***  " + serachedText + "  " + convertionList.size());
                        lv.setAdapter(new MessageConversionAdapter(getActivity(), convertionList));

                    } else {
                        if(tempConvertionlist.size()>0) {
                            android.util.Log.e("TAG", "tempModelConversion  size  ***  " + tempConvertionlist.size() + "  " + convertionList.size());
                            lv.setAdapter(new MessageConversionAdapter(getActivity(), tempConvertionlist));
                            tempConvertionlist = new ArrayList<ModelConversion.Thread>();
                        }else{
                            lv.setAdapter(new EmptyAdapter(getActivity(),"No Record Found"));
                        }
                    }
            }
        });

        return view;
    }


    private void initUI() {
        convertionList=new ArrayList<ModelConversion.Thread>();
        tempConvertionlist=new ArrayList<ModelConversion.Thread>();
        etSearch = (EditText) view.findViewById(R.id.frg_message_etSearch);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lv = (ListView) view.findViewById(R.id.lv_msg_messagelist);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (modelConversion != null) {
                    Fragment fContainer = new ChatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("FriendID", modelConversion.getThread().get(position).getUserid());
                    fContainer.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragment(fContainer, modelConversion.getThread().get(position).getFullname());
                }
            }
        });

        newmessage = (FloatingActionButton) view.findViewById(R.id.conversion_new_message);

        newmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConversionNewFragment frg = new ConversionNewFragment();
                Bundle args = new Bundle();
                args.putInt("top", (newmessage.getTop() + newmessage.getBottom()) / 2);
                args.putInt("left", (newmessage.getLeft() + newmessage.getRight()) / 2);
                frg.setArguments(args);
                mainActivity.addFragment(frg, "Add New Messages", false);
            }
        });


    }

    private void searchonConversion() {

    }


    private void getMessageList() {

        HashMap<String, String> hashMap = new HashMap<String, String>();


        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));

        new PostLibResponse(ConversionFragment.this, new ModelConversion(), getActivity(), hashMap, WebServicesConst.MESSAGE_LIST, WebServicesConst.RES.MESSAGE_LIST, true, true);
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
            case R.id.main_menu_invite:
                mainActivity.replaceFragment(new InvitationListFragment(), "Invitations");
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelConversion = (ModelConversion) clsGson;
        if (modelConversion != null) {
            int status = Integer.parseInt(String.valueOf(modelConversion.getStatus()));
            android.util.Log.e("TAG", "STATUS INVITATION RES   " + status);
            if (status == 200) {
                convertionList=modelConversion.getThread();
                lv.setAdapter(new MessageConversionAdapter(getActivity(), convertionList));
            } else {
                toast(modelConversion.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
