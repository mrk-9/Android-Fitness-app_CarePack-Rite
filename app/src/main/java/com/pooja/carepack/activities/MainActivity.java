package com.pooja.carepack.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.pooja.carepack.R;
import com.pooja.carepack.async.PostFileAsync;
import com.pooja.carepack.dialogs.OfflineNetworkDialog;
import com.pooja.carepack.dialogs.SignOutDialog;
import com.pooja.carepack.fragments.BMIFragment;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.fragments.ConversionFragment;
import com.pooja.carepack.fragments.ConversionNewFragment;
import com.pooja.carepack.fragments.DashboardFragment;
import com.pooja.carepack.fragments.EmergencyFragment;
import com.pooja.carepack.fragments.FitnessFragment;
import com.pooja.carepack.fragments.HeartRateFragment;
import com.pooja.carepack.fragments.ImageCropFragment;
import com.pooja.carepack.fragments.InsuranceCardFragment;
import com.pooja.carepack.fragments.MedicalFindingFragment;
import com.pooja.carepack.fragments.SettingsFragment;
import com.pooja.carepack.fragments.TabletRegalationFragment;
import com.pooja.carepack.fragments.TeethFragment;
import com.pooja.carepack.fragments.TreatmentPlanFragment;
import com.pooja.carepack.fragments.VaccinationCardFragment;
import com.pooja.carepack.interfaces.iSignoutListener;
import com.pooja.carepack.model.ModelLogin;
import com.pooja.carepack.model.ModelProfileImage;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.utils.Utility;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.app.ToolbarManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class MainActivity extends ActionBarActivity implements ToolbarManager.OnToolbarGroupChangedListener, View.OnClickListener, ImageChooserListener, LibPostListner, PostFileAsync.PostFileListener {

    public static String currentFragment;
    //    public static MyActivityResult myActivityResult;
    private final int MENU_EMERGENCY_EMAIL = 0;
    private final int MENU_DASHBOARD = 1;
    private final int MENU_MESSAGE = 2;
    private final int MENU_FITNESS_STATUS = 3;
    private final int MENU_HEART_RATE = 4;
    private final int MENU_BODY_MASS_INDEX = 5;
    private final int MENU_VACCINATION_CARD = 6;
    private final int MENU_MEDICAL_FINDINGS = 7;
    private final int MENU_TABLET_REGULATION = 8;
    private final int MENU_TREATMENT_PLAN = 9;
    private final int MENU_TEETH_OVERVIEW = 10;
    private final int MENU_INSURANCE_CARD = 11;
    private final int MENU_SETTINGS = 12;
    private final int MENU_LOGOUT = 13;
    public Toolbar tbMain;
    public ToolbarManager tbManager;
    public boolean filledProfile;
    boolean mIsInBackAnimation;
    SignOutDialog signOutDialog;
    OnImageChooserListener onImageChooserListener;
    private MobileDataStateChangedReceiver mobileDataReceiver;
    private DrawerLayout dlMain;
    private ListView lvDrawer;
    private Context context;
    private DrawerAdapter adptDrawer;
    private MainToolbarManager tmMain;
    private int selectedPosition = 0, drawerClick = 0;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private Dialog myDialog;
    private String mediaPath;
    private Uri selectedImageUri = null;
    private ImageView userimage;
    private Stack<String> titles = new Stack<String>();
    private MyPrefs prefs;
    private String takeImageFrgs;
    private boolean doubleBackToExitPressedOnce;
    private String SAMPLE_CROPPED_IMAGE_NAME = "carepack1";
    private Uri mDestinationUri;
    private Target target = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {

        }

        @Override
        public void onBitmapLoaded(Bitmap bmp, Picasso.LoadedFrom arg1) {
            bmp = scaleCenterCrop(bmp);
            ImageView userimage = (ImageView) findViewById(R.id.ivDrawerImage);
            userimage.setImageBitmap(bmp);
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {

        }
    };
    private OfflineNetworkDialog offlineDialog;
    private String choosenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        offlineDialog = new OfflineNetworkDialog(MainActivity.this);
        //////////
        context = MainActivity.this;
        prefs = new MyPrefs(this);

        dlMain = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) findViewById(R.id.lvDrawerMenu);
        tbMain = (Toolbar) findViewById(R.id.toolbar);
        userimage = (ImageView) findViewById(R.id.ivDrawerImage);
        userimage.setOnClickListener(this);


        TypedArray menuItems = context.getResources().obtainTypedArray(R.array.drawer_icon);
        adptDrawer = new DrawerAdapter(context, R.layout.row_drawer, getResources().getStringArray(R.array.drawer_item), menuItems);

        tbManager = new ToolbarManager(getDelegate(), tbMain, 0, R.style.ToolbarRippleStyle, R.anim.abc_fade_in, R.anim.abc_fade_out);
        tmMain = new MainToolbarManager(R.style.NavigationDrawerDrawable, getSupportFragmentManager(), tbMain, dlMain);

        tbManager.setNavigationManager(tmMain);
        tbManager.registerOnToolbarGroupChangedListener(this);

        LinearLayout child_insidenew_layout = new LinearLayout(this);
        AbsListView.LayoutParams child_inside_paramsnew = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100);
        child_insidenew_layout.setLayoutParams(child_inside_paramsnew);
        child_insidenew_layout.setBackgroundColor(getResources().getColor(R.color.btn_pressed));
        lvDrawer.addFooterView(child_insidenew_layout);

        lvDrawer.setAdapter(adptDrawer);

        dlMain.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerStateChanged(int arg0) {

            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            @Override
            public void onDrawerOpened(View arg0) {
                dlMain.setTag(null);
            }

            @Override
            public void onDrawerClosed(View arg0) {
                lvDrawer.setClickable(true);
                if (dlMain.getTag() != null)
                    setDrawerClosed(Integer.parseInt("" + dlMain.getTag()));
            }
        });

        if (savedInstanceState == null) {
            if (prefs.getUser() == null && prefs.get(MyPrefs.keys.FILLEDPROFILE).length() == 0) {
                Log.d("TAG", "MAIN ACTIVITY   setting");
                setDrawerClosed(MENU_SETTINGS);
            } else {
                Log.d("TAG", "MAIN ACTIVITY   dashboard");
                setDrawerClosed(MENU_DASHBOARD);
            }
        }

        setUserData();
//        FrameLayout fl = (FrameLayout) findViewById(R.id.ll_container);
//        fl.getLayoutParams().height = Utility.getScreenSize(this).y - getStatusBarHeight();
    }


    public void setHomeIcon(boolean isHome) {
        dlMain.setDrawerLockMode(isHome ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        tbManager.setCurrentGroup(isHome ? 0 : 1);
    }

    public void setUserData() {
        TextView username = (TextView) findViewById(R.id.tvDrawername);
        username.setText(prefs.get(MyPrefs.keys.USERNAME));
        setProfileImage();
    }

    private void setProfileImage() {
        Log.e("TAG", "PROFILE IMAGE    *****  " + prefs.get(MyPrefs.keys.IMAGE));
        if (prefs.get(MyPrefs.keys.IMAGE).length() > 0)
            Picasso.with(this).load(prefs.get(MyPrefs.keys.IMAGE)).into(target);
    }

    /**
     * Scale center crop.
     *
     * @param srcBmp the src bmp
     * @return the bitmap
     */
    public Bitmap scaleCenterCrop(Bitmap srcBmp) {
        Bitmap dstBmp = null;
        if (srcBmp != null)
            try {
                if (srcBmp.getWidth() >= srcBmp.getHeight()) {
                    dstBmp = Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2, 0, srcBmp.getHeight(), srcBmp.getHeight());
                } else {
                    dstBmp = Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2, srcBmp.getWidth(), srcBmp.getWidth());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        return dstBmp;
    }

    private void setDrawerClosed(int position) {
        if (position == MENU_EMERGENCY_EMAIL) {
            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDarkE));
            tbMain.setBackgroundDrawable(colorDrawable);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryE));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentE));
            } else {
                setTheme(R.style.AppThemeEmergency);
            }
        } else {
            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark));
            tbMain.setBackgroundDrawable(colorDrawable);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
            } else {
                setTheme(R.style.AppTheme);
            }
        }

        if (position < adptDrawer.getCount() - 1) {
            titles.clear();
            setTitle((String) adptDrawer.getItem(position));
            switch (position) {
                case MENU_EMERGENCY_EMAIL:
                    setFragment(new EmergencyFragment());
                    break;
                case MENU_DASHBOARD:
                    setFragment(new DashboardFragment());
                    break;
                case MENU_MESSAGE:
                    setFragment(new ConversionFragment());
                    break;
                case MENU_FITNESS_STATUS:
                    setFragment(new FitnessFragment());
                    break;
                case MENU_HEART_RATE:
                    setFragment(new HeartRateFragment());
                    break;
                case MENU_BODY_MASS_INDEX:
                    setFragment(new BMIFragment());
                    break;
                case MENU_VACCINATION_CARD:
                    setFragment(new VaccinationCardFragment());
                    break;
                case MENU_MEDICAL_FINDINGS:
                    setFragment(new MedicalFindingFragment());
                    break;
                case MENU_TABLET_REGULATION:
                    setFragment(new TabletRegalationFragment());
                    break;
                case MENU_TREATMENT_PLAN:
                    setFragment(new TreatmentPlanFragment());
                    break;
                case MENU_TEETH_OVERVIEW:
                    setFragment(new TeethFragment());
                    break;
                case MENU_INSURANCE_CARD:
                    setFragment(new InsuranceCardFragment());
                    break;
                case MENU_SETTINGS:
                    setFragment(new SettingsFragment());
                    break;
            }
        } else {
            /**
             * @logout user data and start login screen
             */
            signoutDialog();
        }
    }

    private void signoutDialog() {
        signOutDialog = new SignOutDialog(MainActivity.this);
        signOutDialog.showDialog();
        signOutDialog.setOnSignOutListener(new iSignoutListener() {
            @Override
            public void onSignout() {
                userSignOut();
            }
        });
    }

    private void userSignOut() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
        hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        new PostLibResponse(this, new ModelLogin(), this, hashMap, WebServicesConst.LOGOUT, WebServicesConst.RES.LOGOUT, true, true);

    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {

        if (requestCode == WebServicesConst.RES.LOGOUT) {
            ModelLogin login = (ModelLogin) clsGson;
            if (login.status == 200) {
                signOutDialog.dismiss();
                prefs.setUser(null);
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                MainActivity.this.finish();
            } else {
                BaseFragment.toast(this, "" + login.status);
            }
        }
    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    private boolean closeDrawer(int position) {
        dlMain.setTag(position);
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void onDoubleBackPressed() {
        popBackStack();
        popBackStack();
    }

    public void onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else if (isStackEmpty()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            if (prefs.get(MyPrefs.keys.USERNAME).length() > 0)
                dlMain.openDrawer(GravityCompat.START);
            // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public boolean isStackEmpty() {
        hideSoftKeyboard();
//        if (prefs.getUser() == null) {
//            prefs.setUser(null);
//            startActivity(new Intent(MainActivity.this, SplashActivity.class));
//            MainActivity.this.finish();
//            return false;
//        } else
        {
            if (mIsInBackAnimation) return false;
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            //fragments.size() is not correct.
            final int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments != null && fragmentCount > 0) {
                Fragment lastFragment = fragments.get(fragmentCount);
                if (lastFragment != null && lastFragment instanceof ConversionNewFragment) {
                    ((ConversionNewFragment) lastFragment).onBackPressed(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                            mIsInBackAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            popBackStack();
                            mIsInBackAnimation = false;
                        }
                    });
                    return false;
                } else {
                    popBackStack();
                    return false;
                }
            }
        }
        return true;
    }

    private void popBackStack() {
        try {
            titles.pop();
            super.setTitle(titles.lastElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            setHomeIcon(true);
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void
    setFragment(Fragment frg) {
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_container, frg, frg.getClass().getSimpleName()).commit();
    }

//    public void
//    addFragment(Fragment frg) {
//        getSupportFragmentManager().beginTransaction().add(R.id.ll_container, frg, frg.getClass().getSimpleName()).commit();
//    }


    public void addFragment(Fragment frg, String name) {
        addFragment(frg, name, true);
    }

    public void addFragment(Fragment frg, String name, boolean isAnimate) {
        setTitle(name);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("");
        if (isAnimate)
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right);
        setHomeIcon(false);
        ft.add(R.id.ll_container, frg, frg.getClass().getSimpleName()).commit();
    }

    public void replaceFragment(Fragment frg, String name) {
        replaceFragment(frg, name, true);
    }

    public void replaceFragment(Fragment frg, String name, String... args) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < args.length; ) {
            BaseFragment.Log.d("argument : " + args[i] + " " + args[(i + 1)]);
            bundle.putString(args[i++], args[i++]);
        }
        frg.setArguments(bundle);
        replaceFragment(frg, name, true);
    }

    public void replaceFragment(Fragment frg, String name, boolean isAnimate) {
        setTitle(name);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("");
        if (isAnimate)
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_out_right);
        setHomeIcon(false);
        ft.replace(R.id.ll_container, frg, frg.getClass().getSimpleName()).commit();
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onToolbarGroupChanged(int oldGroupId, int groupId) {
        tbManager.notifyNavigationStateChanged();
    }

    public void setTitle(String title) {
        if (title != null) {
            titles.add(title);
            super.setTitle(title);
        }
    }

    public void setHeader(String title) {
        if (title != null) {
            super.setTitle(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDrawerImage:
                setPhotoPickerDialog(null, "profileImage");
                break;
        }
    }

    public void setPhotoPickerDialog(OnImageChooserListener onImageChooserListener, String insauranceCard) {
        takeImageFrgs = insauranceCard;
        this.onImageChooserListener = onImageChooserListener;
        myDialog = new Dialog(MainActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.choose_photo);

        myDialog.show();
        LinearLayout btnPickPhotoCamera = (LinearLayout) myDialog.findViewById(R.id.btnPhotoFromCamera);
        LinearLayout btnPickPhotoGallery = (LinearLayout) myDialog.findViewById(R.id.btnPhotoFromGallery);
        btnPickPhotoCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                takePicture();

            }
        });
        btnPickPhotoGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
                chooseImage();
            }
        });
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            mediaPath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    Log.d("TAG", "onImage choose  " + image.getFilePathOriginal());
                    if (onImageChooserListener != null) {
                        if (takeImageFrgs.equalsIgnoreCase("InsauranceCard")) {
                            Fragment fContainer = new ImageCropFragment(onImageChooserListener);
                            Bundle bundle = new Bundle();
                            bundle.putString("ChooseImage", image.getFilePathOriginal());
                            fContainer.setArguments(bundle);
                            addFragment(fContainer, "Crop Image");
//                            choosenImage=image.getFilePathOriginal();
//                            startCropActivity(Uri.parse(image.getFilePathOriginal()));
                        } else
                            onImageChooserListener.onImageChoose(image.getFilePathOriginal());
                    } else {
                        selectedImageUri = Uri.parse(new File(image.getFileThumbnail()).toString());
                        // profileImage.setVisibility(View.VISIBLE);
//                        userimage.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
//                        userimage.setImageURI(Uri.parse(new File(image.getFileThumbnailSmall()).toString()));
                        saveProfileImage(image.getFilePathOriginal());
                    }
                }
            }
        });
    }

//    private void startCropActivity(@NonNull Uri uri) {
//        mDestinationUri = Uri.fromFile(new File(getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));
//        android.util.Log.e("TAG", "start Crop Activity     uri  " + mDestinationUri + "    " + uri);
//        UCrop uCrop = UCrop.of(uri, mDestinationUri);
//
//        uCrop = uCrop.withAspectRatio(1, 1);//uCrop.useSourceImageAspectRatio();//basisConfig(uCrop);
//
//        uCrop = advancedConfig(uCrop);
//
//        uCrop.start(MainActivity.this);
//    }
//
//    private UCrop advancedConfig(UCrop uCrop) {
//        UCrop.Options options = new UCrop.Options();
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);//;
//        options.setCompressionQuality(100);
//
//        return uCrop.withOptions(options);
//    }
//
//    private void handleCropResult(@NonNull Intent result) {
//
//        final Uri resultUri = UCrop.getOutput(result);
//        Log.d("TAG","handle Crop result  "+resultUri);
//        if (resultUri != null) {
//            if (onImageChooserListener != null)
//                onImageChooserListener.onImageChoose(String.valueOf(resultUri));
////            ResultActivity.startWithUri(SampleActivity.this, resultUri);
//        } else {
//            onImageChooserListener.onImageChoose(choosenImage);
//            BaseFragment.toast(getApplicationContext(), "Cannot retrieve cropped image");
//        }
//    }


    @Override
    public void onError(String s) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "Activity result     " + requestCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (imageChooserManager == null) {
                    imageChooserManager = new ImageChooserManager(this, requestCode, true);
                    imageChooserManager.setImageChooserListener(this);
                    imageChooserManager.reinitialize(mediaPath);
                }
                imageChooserManager.submit(requestCode, data);
            }
        }
//        else if (requestCode == UCrop.REQUEST_CROP) {
//            handleCropResult(data);
//        }
//        else
//            myActivityResult.myOnActivityResult(requestCode, resultCode, data);
    }

    public View getCoordinatorLL() {
        return findViewById(R.id.ll_coordinator);
    }

    public void showFutureDateDialog(final OnDateDialogListener listener) {
        Calendar cal = Calendar.getInstance();
//        showDateDialog(1, 0, 1950, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), listener);
        showDateDialog(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) - 100, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), listener);
    }

    public void showDateDialog(final OnDateDialogListener listener) {
        Calendar cal = Calendar.getInstance();
//        showDateDialog(1, 0, 1950, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), listener);
        showDateDialog(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) - 25, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) + 25, listener);
    }

    public void showDateDialog(int minDay, int minMonth, int minYear, final OnDateDialogListener listener) {
        Calendar cal = Calendar.getInstance();
//        showDateDialog(minDay,  minMonth, minYear, minDay,  minMonth, minYear+25, listener);
        showDateDialog(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) - 25, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) + 25, listener);
    }

    public void showDateDialog(int b, final OnDateDialogListener listener) {
        Calendar cal = Calendar.getInstance();
//        showDateDialog(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), 1, 0, cal.get(Calendar.YEAR) + 25, listener);
        showDateDialog(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) - 25, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR) + 25, listener);
    }
//    public void showDateDialog(int minYear, int maxYear, final OnDateDialogListener listener) {
//        showDateDialog(1, 0, minYear, 1, 0, maxYear, listener);
//    }

    public void showDateDialog(int minDay, int minMonth, int minYear, int maxDay, int maxMonth, int maxYear, final OnDateDialogListener listener) {
        Calendar cal = Calendar.getInstance();
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        int currMonth = cal.get(Calendar.MONTH);
        int currYear = cal.get(Calendar.YEAR);

        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(minDay, minMonth, minYear, maxDay, maxMonth, maxYear, currDay, currMonth, currYear) {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                listener.onPositiveActionClicked(dialog);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }

        };

        builder.positiveAction("OK").negativeAction("CANCEL");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void showTimeDialog(final OnTimeDialogListener listener) {
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder() {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                listener.onTimeClicked(dialog);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }

        };

        builder.positiveAction("OK").negativeAction("CANCEL");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    // =============================================================================================================
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mobileDataReceiver = new MobileDataStateChangedReceiver();
        registerReceiver(mobileDataReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mobileDataReceiver != null) {
                unregisterReceiver(mobileDataReceiver);
            }
        } catch (Exception e) {

        }
    }

    private void saveProfileImage(String path) {
//        path = selectedImageUri;
        HashMap<String, String> fileparam = new HashMap<>();
        String uri = Utility.getStringPathFromURI(this, Uri.parse(path));
        if (!uri.equalsIgnoreCase("")) {
            fileparam.put("profile_image", uri);
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "submit_profile_image");
        param.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
        param.put("userid", prefs.get(MyPrefs.keys.ID));
        new PostFileAsync(this, new ModelProfileImage(), param, fileparam, 1, this).execute(WebServicesConst.UPDATE_PROFILE_IMAGE);
    }

    @Override
    public void onPostFileSuccess(Object clsGson, int requestCode) {
        try {
            ModelProfileImage cls = (ModelProfileImage) clsGson;
            if (cls.status == 200) {
                prefs.set(MyPrefs.keys.IMAGE, cls.profile_image);
                setProfileImage();
            } else
                Toast.makeText(getApplicationContext(), ("Image Failed to Upload"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFileError(int requestCode) {

    }

    public interface OnImageChooserListener {
        void onImageChoose(String path);
    }

    public interface OnDateDialogListener {
        public void onPositiveActionClicked(DatePickerDialog dialog);
    }

    public interface OnTimeDialogListener {
        public void onTimeClicked(TimePickerDialog dialog);
    }

    public static class NetworkUtil {

        static final int TYPE_WIFI = 1;
        static final int TYPE_MOBILE = 2;
        static final int TYPE_NOT_CONNECTED = 0;

        public static int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }
    }

    private class MainToolbarManager extends ToolbarManager.BaseNavigationManager {

        public MainToolbarManager(int styledId, FragmentManager activity, Toolbar toolbar, DrawerLayout drawerLayout) {
            super(styledId, activity, toolbar, drawerLayout);
        }

        @Override
        public void onNavigationClick() {
            if (isStackEmpty()) {
                dlMain.openDrawer(Gravity.LEFT);
            }
            // else
            // onBackPressed();

            notifyStateChanged();
        }

        @Override
        public boolean isBackState() {
            return super.isBackState() || tbManager.getCurrentGroup() != 0;
        }

        @Override
        protected boolean shouldSyncDrawerSlidingProgress() {
            return super.shouldSyncDrawerSlidingProgress()
                    && tbManager.getCurrentGroup() == 0;
        }
    }

    private class DrawerAdapter extends ArrayAdapter<String> implements View.OnClickListener {
        private Context context;
        private int resource;
        private String itemsName[];
        private TypedArray itemsicons;

        private com.rey.material.widget.LinearLayout llDrawer;
        private TextView tvItemName;
        private ImageView ivIcon;
        private LinearLayout lLayout;

        public DrawerAdapter(Context context, int resource, String[] itemsName, TypedArray itemsicons) {
            super(context, resource, itemsName);

            this.context = context;
            this.resource = resource;
            this.itemsName = itemsName;
            this.itemsicons = itemsicons;

        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(resource, parent, false);
            initializeViews(view);
            if (position == selectedPosition)
                llDrawer.setBackgroundColor(Color.parseColor("#2c659c"));
            if (position == 0)
                llDrawer.setBackgroundColor(position == selectedPosition ? Color.parseColor("#99ff0000") : Color.RED);

            llDrawer.setOnClickListener(this);
            tvItemName.setText(itemsName[position]);
            ivIcon.setImageDrawable(itemsicons.getDrawable(position));
            return view;
        }

        private void initializeViews(View view) {
            llDrawer = (com.rey.material.widget.LinearLayout) view.findViewById(R.id.ll_adpt_drawer);
            ivIcon = (ImageView) view.findViewById(R.id.ivDrawerImage);
            tvItemName = (TextView) view.findViewById(R.id.tvDrawername);
        }

        @Override
        public void onClick(View v) {
            lvDrawer.setClickable(false);


            com.rey.material.widget.ListView listview = (com.rey.material.widget.ListView) v.getParent();
            int position = listview.getPositionForView(v);

            BaseFragment.Log.d(position + " " + (adptDrawer.getCount() - 1));
            if (position == adptDrawer.getCount() - 1) {
                signoutDialog();
            } else {
                notifyDataSetChanged();
                selectedPosition = position;

                switch (v.getId()) {
                    case R.id.ll_adpt_drawer:
                        closeDrawer(position);
                }
            }
        }
    }

    private class MobileDataStateChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int state = NetworkUtil.getConnectivityStatus(context);
                if (state == NetworkUtil.TYPE_WIFI || state == NetworkUtil.TYPE_MOBILE) {
//                    offlineDialog.dismiss();
                    prefs.set(MyPrefs.keys.ISONLINE,"1");
                } else if (state == NetworkUtil.TYPE_NOT_CONNECTED) {
//                    offlineDialog.show();
                    prefs.set(MyPrefs.keys.ISONLINE, "0");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =============================================================================================================

}
