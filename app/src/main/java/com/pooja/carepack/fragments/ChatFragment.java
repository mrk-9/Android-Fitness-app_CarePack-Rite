package com.pooja.carepack.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.adapter.ChatAdapter;
import com.pooja.carepack.async.PostFileAsync;
import com.pooja.carepack.model.ModelConversion;
import com.pooja.carepack.model.ModelSendMessage;
import com.pooja.carepack.model.ModelThread;
import com.pooja.carepack.model.ModelThreadList;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.widget.LinearLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class ChatFragment extends BaseFragment implements PostFileAsync.PostFileListener, View.OnClickListener, LibPostListner, MainActivity.OnImageChooserListener {

    private View view;
    private ListView lv;
    private String friendID;
    private EditText etMessage;
    private String imagePath;
    private ModelConversion modelConversion;
    private LinearLayout llMessageSend;
    private ModelThreadList modelThreadList;
    private ArrayList<ModelThread> modelThreadAryList;
    private ModelSendMessage modelSendMessage;
    private ImageView ivSendImage;
    private android.widget.LinearLayout llImageView;
    private ImageView ivChoosenImage;
    private ImageView ivRemoveImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_chat_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            friendID = bundle.getString("FriendID");
        }
        intiUI();
        getThreadList();
        return view;
    }

    private void intiUI() {
        etMessage = (EditText) view.findViewById(R.id.frg_chat_list_etMessage);
        llMessageSend = (LinearLayout) view.findViewById(R.id.frg_chat_list_llSendMessage);
        ivSendImage = (ImageView) view.findViewById(R.id.frg_chat_list_ivSendImage);
        llImageView=(android.widget.LinearLayout)view.findViewById(R.id.frg_chat_list_llImageView);
        ivChoosenImage=(ImageView)view.findViewById(R.id.frg_chat_list_ivChoosenImage);
        ivRemoveImage=(ImageView)view.findViewById(R.id.frg_chat_list_ivRemoveImage);
        modelThreadAryList = new ArrayList<ModelThread>();
        llMessageSend.setOnClickListener(this);
        ivSendImage.setOnClickListener(this);
        ivRemoveImage.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lv = (ListView) view.findViewById(R.id.frg_chat_list_lvChat);


    }

    private void getThreadList() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("friendid", friendID);

        new PostLibResponse(ChatFragment.this, new ModelThreadList(), getActivity(), hashMap, WebServicesConst.MESSAGE_LIST, WebServicesConst.RES.MESSAGE_LIST, true, true);
    }


    private void sendMessage(String textMessage, String imagePath) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "submit_message");
        hashMap.put("receiver", friendID);
        hashMap.put("message", textMessage);

        HashMap<String, String> fileparam = new HashMap<>();
        if (imagePath != null) {
            String uri = Utility.getStringPathFromURI(getActivity(), Uri.parse(imagePath));
            if (!uri.equalsIgnoreCase("")) {
                fileparam.put("image", uri);
            }
        } else
            hashMap.put("image", "");

//        new PostLibResponse(ConversionNewFragment.this, new ModelConversion(), getActivity(), hashMap, WebServicesConst.MESSAGE_LIST, WebServicesConst.RES.MESSAGE_LIST, true, true);
        new PostFileAsync(getActivity(), new ModelSendMessage(), hashMap, fileparam, 1, ChatFragment.this).execute(WebServicesConst.MESSAGE_LIST);
        this.imagePath=null;
    }


    @Override
    public void onPostFileSuccess(Object clsGson, int requestCode) {
        modelSendMessage = (ModelSendMessage) clsGson;
        if (modelSendMessage != null) {
            int status = modelSendMessage.getStatus();
            if (status == 200) {
                etMessage.setText("");
                modelThreadAryList.add(modelSendMessage.getThread());
                lv.setAdapter(new ChatAdapter(getActivity(), modelThreadAryList, getActivity().getSupportFragmentManager()));
                lv.setSelection(modelThreadAryList.size());
                llImageView.setVisibility(View.GONE);
//                toast(modelSendMessage.getMessage());
            } else {
                toast(modelSendMessage.getMessage());
            }
        }
    }

    @Override
    public void onPostFileError(int requestCode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_chat_list_llSendMessage:
                sendMessage(etMessage.getText().toString(), imagePath);
                break;
            case R.id.frg_chat_list_ivSendImage:
                mainActivity.setPhotoPickerDialog(this, "ChatSendImage");
                break;
            case R.id.frg_chat_list_ivRemoveImage:
                llImageView.setVisibility(View.GONE);
                imagePath=null;
                break;
        }
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelThreadList = (ModelThreadList) clsGson;
        if (modelThreadList != null) {
            int status = modelThreadList.getStatus();
            if (status == 200) {
                toast(modelThreadList.getMessage());
                modelThreadAryList = modelThreadList.getThread();
                lv.setAdapter(new ChatAdapter(getActivity(), modelThreadAryList,getActivity().getSupportFragmentManager()));
                lv.setSelection(modelThreadAryList.size());
            } else {
                toast(modelThreadList.getMessage());
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    @Override
    public void onImageChoose(String path) {
        android.util.Log.d("TAG", "Selected Image for send msg  " + path);
//        sendMessage("", path);
        imagePath=path;
        llImageView.setVisibility(View.VISIBLE);
        Picasso.with(getActivity()).load(new File(path)).into(ivChoosenImage);
        etMessage.setFocusable(true);

    }
}
