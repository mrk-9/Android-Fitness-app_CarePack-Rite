package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.async.PostFileAsync;
import com.pooja.carepack.dialogs.DialogFragmentZoomImage;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelTeeth;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class TeethPager1Fragment extends BaseFragment implements View.OnClickListener, MainActivity.OnImageChooserListener, PostFileAsync.PostFileListener, LibPostListner {

    public int menu_item = 0;
    private View view;
    private ImageView image;
    private LibPostListner libPostListner;
    private ModelTeeth.Teeth user_teeth;
    private TextView tvAdd, tvEdit;
    private TeethDetailAdapter mAdapter;
    private LinearLayout llDetail;
    private Menu menu;
    private String path;
    private ViewPager pager;
    private ImageView ivZoom;
    private String imagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_teeth1, container, false);
        setHasOptionsMenu(true);
        llDetail = (LinearLayout) view.findViewById(R.id.frg_teeth_detail_layout);
        ivZoom=(ImageView)view.findViewById(R.id.frg_teeth1_ivZoom);
        ivZoom.setOnClickListener(this);

        image = (ImageView) view.findViewById(R.id.frg_teeth_image);
        image.getLayoutParams().height = Utility.getScreenPart(getActivity(), 60);
        image.setOnClickListener(this);

        tvAdd = (TextView) view.findViewById(R.id.teeth_detail_tv_add);
        tvAdd.setOnClickListener(this);

        tvEdit = (TextView) view.findViewById(R.id.teeth_detail_tv_edit);
        tvEdit.setOnClickListener(this);
        mAdapter = new TeethDetailAdapter(getActivity(), llDetail);
        mAdapter.add(null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (user_teeth != null)
            setImage(user_teeth);
    }

    private void saveInsuranceCardData(String path) {
        HashMap<String, String> fileparam = new HashMap<>();
        if (path != null && path.length() > 0) {
            String uri = Utility.getStringPathFromURI(getActivity(), Uri.parse(path));
            if (!uri.equalsIgnoreCase("")) {
                fileparam.put("image", uri);
            }
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "submit_user_teeth");
        param.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        param.put("userid", prefs.get(MyPrefs.keys.ID));
        param.put("image_type", "l");
        param.putAll(mAdapter.getList());
        new PostFileAsync(getActivity(), new ModelTeeth(), param, fileparam, 1, TeethPager1Fragment.this).execute(WebServicesConst.TEETH);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frg_teeth_image:
                mainActivity.setPhotoPickerDialog(this, "TeethPager");
                break;
            case R.id.teeth_detail_tv_add:
                if (!tvEdit.getText().toString().equalsIgnoreCase("edit")) {
                    tvEdit.setText("Edit");
                    mAdapter.setEditable(false);
                }
                mAdapter.add(null);
                break;
            case R.id.teeth_detail_tv_edit:
                if (tvEdit.getText().toString().equalsIgnoreCase("edit")) {
                    tvEdit.setText("Done");
                    mAdapter.setEditable(true);
                } else {
                    tvEdit.setText("Edit");
                    mAdapter.setEditable(false);
                }
                break;
            case R.id.frg_teeth1_ivZoom:
                DialogFragment newFragment = new DialogFragmentZoomImage();
                Bundle bundle = new Bundle();
                if (imagePath != null)
                    bundle.putString("ImagePath", imagePath);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "Zoom Image");
                break;
        }

    }

    @Override
    public void onImageChoose(String path) {
        imagePath=path;
        this.path = path;
        Picasso.with(getActivity()).load(new File(path)).into(image);
    }

    @Override
    public void onPostFileSuccess(Object clsGson, int requestCode) {
        if (libPostListner != null)
            libPostListner.onPostResponseComplete(clsGson, requestCode);
            pager.setCurrentItem(1);
    }

    public void setImage(ModelTeeth.Teeth teeth) {
        if (isVisible()) {
            imagePath=teeth.image;
            android.util.Log.d("TAG", "Imagepath in teeth1  ----  " + imagePath);
            Picasso.with(getActivity()).load(teeth.image).into(image);
            if (teeth.details != null && teeth.details.size() > 0) {
                mAdapter.removeViewAll();
                mAdapter.addAll(teeth.details);
            }
        }
    }

    @Override
    public void onPostFileError(int requestCode) {

    }

    public void setResponseListener(LibPostListner libPostListner, ModelTeeth.Teeth user_teeth, ViewPager pager) {
        this.pager=pager;
        this.libPostListner = libPostListner;
        this.user_teeth = user_teeth;
        if (user_teeth != null)
            setImage(user_teeth);

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
                saveInsuranceCardData(path);
                break;
            default:
                break;
        }
        return true;
    }

    private void deleteTeethDetail(String id) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("action", "delete_teeth_details");
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        hashMap.put("id", id);
        new PostLibResponse(TeethPager1Fragment.this, new ModelEditEmailDeletVaccination(), getActivity(), hashMap, WebServicesConst.TEETH, WebServicesConst.RES.DELETE_TEETH_DETAIL, true, true);
    }


    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {
        toast(errorMessage);
    }

    class TeethDetailAdapter implements View.OnClickListener {
        private final LinearLayout layout;
        private final Context context;

        public TeethDetailAdapter(Context context, LinearLayout layout) {
            this.layout = layout;
            this.context = context;
        }

        public void removeView(int position) {
            LinearLayout removeView = ((LinearLayout) layout.getChildAt(position));
            EditText title = ((EditText) removeView.findViewById(R.id.row_teeth_detail_tvTitle));
            EditText desc = ((EditText) removeView.findViewById(R.id.row_teeth_detail_tvDescription));
            TextView id = ((TextView) removeView.findViewById(R.id.row_teeth_detail_tvId));

            if (id.length() > 0) {
                deleteTeethDetail("" + id.getText().toString());
            }
            title.setText("");
            desc.setText("");
            id.setText("");

            removeView.setVisibility(View.GONE);
        }

        public void removeViewAll() {
            layout.removeAllViews();
            tvEdit.setText("Edit");
        }

        @Nullable
        public void addAll(ArrayList<ModelTeeth.TeethDetail> teeths) {
            for (ModelTeeth.TeethDetail teeth : teeths) {
                add(teeth);
            }
        }

        public HashMap<String, String> getList() {
            HashMap<String, String> hashMap = new HashMap<>();
            for (int i = 0; i < layout.getChildCount(); i++) {
                LinearLayout removeView = ((LinearLayout) layout.getChildAt(i));
                TextView id = ((TextView) removeView.findViewById(R.id.row_teeth_detail_tvId));
                EditText title = ((EditText) removeView.findViewById(R.id.row_teeth_detail_tvTitle));
                EditText desc = ((EditText) removeView.findViewById(R.id.row_teeth_detail_tvDescription));

                if (title.getText().length() > 0 && desc.getText().length() > 0) {
                    hashMap.put("details[" + i + "][title]", title.getText().toString());
                    hashMap.put("details[" + i + "][description]", desc.getText().toString());
                    if (id.getText().length() == 0)
                        id.setText("0");
                    hashMap.put("details[" + i + "][id]", id.getText().toString());
                }
            }
            return hashMap;
        }

        public void add(ModelTeeth.TeethDetail teeth) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_teeth_detail, null);
            view.findViewById(R.id.row_teeth_detail_tvDelete).setTag("" + layout.getChildCount());
            view.findViewById(R.id.row_teeth_detail_tvDelete).setOnClickListener(this);
            if (teeth != null) {
                ((EditText) view.findViewById(R.id.row_teeth_detail_tvTitle)).setText("" + teeth.title);
                ((EditText) view.findViewById(R.id.row_teeth_detail_tvDescription)).setText("" + teeth.description);
                ((TextView) view.findViewById(R.id.row_teeth_detail_tvId)).setText("" + teeth.id);
            }
            layout.addView(view);
        }

        public void setEditable(boolean editable) {
            for (int i =1; i < layout.getChildCount(); i++) {
                ((LinearLayout) layout.getChildAt(i)).getChildAt(2).setVisibility(editable ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            synchronized (this) {
                removeView(Integer.parseInt(v.getTag() + ""));

            }
        }
    }
}