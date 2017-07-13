package com.pooja.carepack.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.async.PostFileAsync;
import com.pooja.carepack.dialogs.DialogFragmentZoomImage;
import com.pooja.carepack.model.ModelInsurance;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Vinay Rathod on 24/11/15.
 */
public class InsuranceCardPager2Fragment extends BaseFragment implements View.OnClickListener, MainActivity.OnImageChooserListener, PostFileAsync.PostFileListener {

    private View view;
    private ImageView image;
    private Button btnSendMail;
    private String imagePath;
    private ProgressView pvloadingView;
    private ImageView ivZoom;
    //    private ProgressDialog pDilog;
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//            pDilog.dismiss();
//            Log.d("Progress dialog active2");
//            if (pvloadingView.isActivated()) {
                Log.d("Progress dialog active2 in");
                pvloadingView.stop();
//            }
            image.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_insurance_card2, container, false);
//        pDilog=new ProgressDialog(getActivity());
        image = (ImageView) view.findViewById(R.id.frg_insurance_back_image);
        pvloadingView = (ProgressView) view.findViewById(R.id.frg_insaurance_card2_pvLoading);
        btnSendMail = (Button) view.findViewById(R.id.frg_insaurance_card2_btnSendMail);
        btnSendMail.setOnClickListener(this);
        image.setOnClickListener(this);
        ivZoom = (ImageView) view.findViewById(R.id.frg_insaurance_card2_ivZoom);
        ivZoom.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_insurance_back_image:
                mainActivity.setPhotoPickerDialog(this, "InsauranceCard");
                break;
            case R.id.frg_insaurance_card2_btnSendMail:
                sendMail();
                break;
            case R.id.frg_insaurance_card2_ivZoom:
                DialogFragment newFragment = new DialogFragmentZoomImage();
                Bundle bundle = new Bundle();
                if (imagePath != null)
                    bundle.putString("ImagePath", imagePath);
                newFragment.setArguments(bundle);
                newFragment.show(getActivity().getSupportFragmentManager(), "Zoom Image");
                break;
        }

    }

    private void sendMail() {
        if (imagePath != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
            emailIntent.setType("image/png");
            final PackageManager pm = getActivity().getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            else
                toast("Their is no Gmail Application");
            startActivity(emailIntent);


//        Intent i = new Intent(Intent.ACTION_SEND);
//        i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
//        i.putExtra(Intent.EXTRA_SUBJECT,"");
//        //Log.d("URI@!@#!#!@##!", Uri.fromFile(pic).toString() + "   " + pic.exists());
//        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
//
//        i.setType("image/png");
//        startActivity(Intent.createChooser(i,""));
        } else
            toast("Please select image.");
    }

    private void saveInsuranceCardData(String path) {
        HashMap<String, String> fileparam = new HashMap<>();
        String uri = Utility.getStringPathFromURI(getActivity(), Uri.parse(path));
        if (!uri.equalsIgnoreCase("")) {
            fileparam.put("image", uri);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "submit_insurance_card");
        param.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        param.put("userid", prefs.get(MyPrefs.keys.ID));
        param.put("card_type", "b");
        new PostFileAsync(getActivity(), new ModelInsurance(), param, fileparam, 2, InsuranceCardPager2Fragment.this).execute(WebServicesConst.INSURANCE_CARD);
    }


    @Override
    public void onImageChoose(String path) {
        imagePath = path;
        android.util.Log.e("TAG", "PATH cam 2  +++++++++  " + path);
        Picasso.with(getActivity()).load(new File(path)).into(target);
//        pDilog.show();
        pvloadingView.start();
        saveInsuranceCardData(path);
    }

    public void setImage(String path) {
//        imagePath=path;
        android.util.Log.e("TAG", "PATH 2  +++++++++  " + path);
        imagePath = convertURLtoPath(path);
        android.util.Log.e("TAG", "IMage PATH 2  +++++++++  " + imagePath);
        Picasso.with(getActivity()).load(path).into(target);
        pvloadingView.start();
//        pDilog.show();
    }

    private String convertURLtoPath(String imagePath) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            url = new URL(imagePath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap immutableBpm = BitmapFactory.decodeStream(input);
        Bitmap mutableBitmap = immutableBpm.copy(Bitmap.Config.ARGB_8888, true);
        View view = new View(getActivity());
        view.draw(new Canvas(mutableBitmap));

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void onPostFileSuccess(Object clsGson, int requestCode) {

    }

    @Override
    public void onPostFileError(int requestCode) {

    }
}
