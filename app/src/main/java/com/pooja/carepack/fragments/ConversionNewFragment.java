package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelSendMessage;
import com.pooja.carepack.reveallayout.RevealLayout;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;

import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class ConversionNewFragment extends BaseFragment implements View.OnClickListener,LibPostListner{

    int top, left;
    private View view;
    private RevealLayout mRevealLayout;
    private Menu menu;
    private TextView tvToFrinedName;
    private String SelectedFriendID;
    private EditText etMessage;
    private ModelSendMessage modelSendMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_message_new, container, false);
        initUI();
        setHasOptionsMenu(true);
        return view;
    }

    private void initUI() {
        etMessage=(EditText)view.findViewById(R.id.frg_message_new_etMessage);
        tvToFrinedName=(TextView)view.findViewById(R.id.frg_message_new_tvToFriendName);
        tvToFrinedName.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        top = getArguments().getInt("top");
        left = getArguments().getInt("left");
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealLayout.setContentShown(false);
//        mRevealLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mRevealLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRevealLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    mRevealLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mRevealLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Point size = Utility.getScreenSize(getActivity());
//                        BaseFragment.Log.d(size.x +" "+size.y +" ");
                        mRevealLayout.show(left, top);
                    }
                }, 50);
            }
        });

    }
    public int menu_item = 3;
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
        menu.getItem(2).setVisible(false);
        this.menu = menu;
        menu.getItem(menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(menu_item).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_send:
                sendMessage();
                break;
            default:
                break;
        }
        return true;
    }


    public void onBackPressed(final Animation.AnimationListener listener) {
        hideSoftKeyBoard();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mRevealLayout.hide(left, top, listener);
            }
        }, 500);

    }

    private void hideSoftKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            Log.d("Hide keyboard");
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frg_message_new_tvToFriendName:
                DialogFragment newFragment = new FriendListDialogFragment();
                newFragment.setTargetFragment(ConversionNewFragment.this, 100);
                newFragment.show(getActivity().getSupportFragmentManager(), "Friend");

//                mainActivity.replaceFragment(new FriendListFragment(),"Friend");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        android.util.Log.d("TAG", "conversion new frag activity result "+requestCode);
        if (requestCode == 100) {
            String SelectedFriendName = null;
            if (data != null && data.getExtras() != null) {
                SelectedFriendName = data.getExtras().getString("FriendName");//
                SelectedFriendID = data.getExtras().getString("FriendID");
                android.util.Log.d("TAG","selected friend name  b  "+SelectedFriendName);
                tvToFrinedName.setText("To : "+SelectedFriendName);
            }
        }
    }

    private void sendMessage() {
//        HashMap<String, String> fileparam = new HashMap<>();
//        fileparam.put("image", "");
//        String uri = Utility.getStringPathFromURI(getActivity(), Uri.parse(path));
//        if (!uri.equalsIgnoreCase("")) {
//            fileparam.put("image", uri);
//        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("action", "submit_message");
        hashMap.put("receiver", SelectedFriendID);
        hashMap.put("message", etMessage.getText().toString());
        hashMap.put("image", "");

        new PostLibResponse(ConversionNewFragment.this, new ModelSendMessage(), getActivity(), hashMap, WebServicesConst.MESSAGE_LIST, WebServicesConst.RES.MESSAGE_LIST, true, true);
//        new PostFileAsync(getActivity(), new ModelInsurance(), hashMap, fileparam, 1, ConversionNewFragment.this).execute(WebServicesConst.MESSAGE_LIST);
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {
        modelSendMsg=(ModelSendMessage)clsGson;
        if(modelSendMsg!=null){
            int status=modelSendMsg.getStatus();
            if(status==200){
                toast(modelSendMsg.getMessage());
                getActivity().onBackPressed();
            }
            else{
                toast(modelSendMsg.getMessage());
            }
        }

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }
}
