package com.pooja.carepack.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.SearchUserAdapter;
import com.pooja.carepack.model.ModelInvitation;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;


/**
 * Created by Yudiz on 03/02/16.
 */
public class SearchUserFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,LibPostListner {

    private RadioGroup rGroup;
    private int[] radioButton = {R.id.frg_search_user_rbDoctor, R.id.frg_search_user_rbPatient};
    private android.widget.EditText etSearch;
    private ListView lstSearchUser;
    private String usertype="d";
    private ModelInvitation modelInvitation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_search_user, container, false);
        initUI();
        prefs = new MyPrefs(getActivity());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchedUserList(String.valueOf(s));
            }
        });
        return view;
    }

    private void initUI() {
        etSearch=(android.widget.EditText)view.findViewById(R.id.frg_search_user_etSearch);
        lstSearchUser=(ListView)view.findViewById(R.id.frg_search_user_lstUser);
        rGroup = (RadioGroup) view.findViewById(R.id.frg_search_user_radioGroup);
        rGroup.setOnCheckedChangeListener(this);


    }

    private void getSearchedUserList(String userSearchValue) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("usertype", usertype);
        hashMap.put("chr", userSearchValue);

        new PostLibResponse(SearchUserFragment.this, new ModelInvitation(), getActivity(), hashMap, WebServicesConst.GET_USER, WebServicesConst.RES.GET_USER, false, true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.frg_search_user_rbDoctor:
                    usertype="d";
                break;
            case R.id.frg_search_user_rbPatient:
                usertype="p";
                break;

        }
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelInvitation=(ModelInvitation)clsGson;
        if(modelInvitation!=null){
            int status=Integer.parseInt(String.valueOf(modelInvitation.getStatus()));
            if(status==200){
                 lstSearchUser.setAdapter(new SearchUserAdapter(getActivity(),modelInvitation));
            }
            else{
                toast(modelInvitation.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
