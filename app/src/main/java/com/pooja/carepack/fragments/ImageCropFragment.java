package com.pooja.carepack.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.MainActivity;
import com.pooja.carepack.utils.RotateBitmap;
import com.rey.material.widget.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by Yudiz on 01/02/16.
 */
@SuppressLint("ValidFragment")
public class ImageCropFragment extends BaseFragment implements MainActivity.OnImageChooserListener, Slider.OnPositionChangeListener {

    private com.theartofdev.edmodo.cropper.CropImageView cpivImage;
    private String imagePath;
    private int menu_item = 9;
    private MainActivity.OnImageChooserListener onImageChoosen;
    private Slider sldRotateImage;
    private Bitmap bmp, bmpfullImage;


    public ImageCropFragment(MainActivity.OnImageChooserListener onImageChooserListener) {
        onImageChoosen = onImageChooserListener;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_image_crop, container, false);
        intiUI();
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imagePath = bundle.getString("ChooseImage");
            android.util.Log.e("TAG", "Image path   " + imagePath);
        }

//        if (imagePath != null) {
//            cpivImage.setImageURI(Uri.parse(imagePath));
//            cpivImage.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
//        }
        if (imagePath != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bmpfullImage = BitmapFactory.decodeFile(imagePath, options);
            cpivImage.setImageBitmap(bmpfullImage);
            android.util.Log.e("TAG", "Image bmp   " + bmpfullImage);
//            cpivImage.setImageUri(Uri.parse(imagePath));
//            cpivImage.setAspectRatio(1,1);
            cpivImage.setFixedAspectRatio(false);
            cpivImage.setGuidelines(1);
            cpivImage.setCropShape(com.theartofdev.edmodo.cropper.CropImageView.CropShape.RECTANGLE);
            cpivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }


//        cpivImage.get
        // selected_photo.setImageBitmap(bitmap);

//        bmpfullImage = cpivImage.getRectBitmap();

        sldRotateImage.setOnPositionChangeListener(this);
        return view;
    }

    private void intiUI() {
        cpivImage = (com.theartofdev.edmodo.cropper.CropImageView) view.findViewById(R.id.frg_image_crop_cropImageView);
        sldRotateImage = (Slider) view.findViewById(R.id.frg_image_crop_sdRotateImage);
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
            case R.id.main_menu_done:
                android.util.Log.e("TAG", "BITMAP   " + bmp);
                if (saveBitmap(bmp) != null) {
                    bmp = cpivImage.getCroppedImage();
                    onImageChoosen.onImageChoose(saveBitmap(bmp));
                }
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    private String saveBitmap(Bitmap bmp) {
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
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void onImageChoose(String path) {

    }

    @Override
    public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
//        android.util.Log.e("TAG","oldpos    "+oldPos+"   newpos  "+newPos+"   oldvalue   "+oldValue+"   newvalue   "+newValue);
//         bmp=cpivImage.getCroppedBitmap();
        try {
            RotateBitmap rbmp = new RotateBitmap(bmpfullImage, newValue);
            cpivImage.invalidate();
            cpivImage.setImageBitmap(rbmp.getRotateMatrix());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cpivImage.rotateImage(new CropImageView.RotateDegrees(newValue));
//        try {
//            cpivImage.rotateImage(newValue);
//            cpivImage.invalidate();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
}
