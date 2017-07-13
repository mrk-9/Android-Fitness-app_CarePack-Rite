/*
 * 
 */
package com.pooja.carepack.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pooja.carepack.BuildConfig;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc

/**
 * Utility class that define all define which you have to use staticly.
 * <p/>
 * you can use this class as below:<br>
 */
public class Utility {
    /**
     * The Constant month.
     */
    public static final String month[] = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "", "", ""};
    /**
     * that variable use to counter for next displaing toast.
     */
    private static int time;

    public static String getStringPathFromURI(Context context, Uri contentUri) {

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    // public static String replaceAll(String findtxt, String replacetxt, String str, boolean isCaseInsensitive) {
    // if (str == null) {
    // return null;
    // }
    // if (findtxt == null || findtxt.length() == 0) {
    // return str;
    // }
    // if (findtxt.length() > str.length()) {
    // return str;
    // }
    // int counter = 0;
    // String thesubstr = "";
    // while ((counter < str.length()) && (str.substring(counter).length() >= findtxt.length())) {
    // thesubstr = str.substring(counter, counter + findtxt.length());
    // if (isCaseInsensitive) {
    // if (thesubstr.equalsIgnoreCase(findtxt)) {
    // str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
    // // Failing to increment counter by replacetxt.length() leaves you open
    // // to an infinite-replacement loop scenario: Go to replace "a" with "aa" but
    // // increment counter by only 1 and you'll be replacing 'a's forever.
    // counter += replacetxt.length();
    // } else {
    // counter++; // No match so move on to the next character from
    // // which to check for a findtxt string match.
    // }
    // } else {
    // if (thesubstr.equals(findtxt)) {
    // str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
    // counter += replacetxt.length();
    // } else {
    // counter++;
    // }
    // }
    // }
    // return str;
    // }

    /**
     * Dp2px.
     *
     * @param res the res
     * @param dp  the dp
     * @return the int
     */
    public static int dp2px(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    /**
     * Replace all.
     *
     * @param findtxt    the findtxt
     * @param replacetxt the replacetxt
     * @param str        the str
     * @return the string
     */
    public static String replaceAll(String findtxt, String replacetxt, String str) {
        if (str == null) {
            return null;
        }
        if (findtxt == null || findtxt.length() == 0) {
            return str;
        }
        if (findtxt.length() > str.length()) {
            return str;
        }
        int counter = 0;
        String thesubstr = "";
        while ((counter < str.length()) && (str.substring(counter).length() >= findtxt.length())) {
            thesubstr = str.substring(counter, counter + findtxt.length());
            if (thesubstr.equalsIgnoreCase(findtxt)) {
                str = str.substring(0, counter) + ("&lt;font color=\"#5f16df\"&gt;" + thesubstr + "&lt;/font&gt;") + str.substring(counter + findtxt.length());
                counter += replacetxt.length();
            } else {
                counter++;
            }
        }
        return str;
    }

    /**
     * Gets the formated date.
     *
     * @param dateString the date string
     * @param fromFormat the from format
     * @param toFormat   the to format
     * @return the formated date
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDate(String dateString, String fromFormat, String toFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        try {
            Date date = sdf.parse(dateString);
            String formated = new SimpleDateFormat(toFormat).format(date);
            return formated;
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * Gets the formated date.
     *
     * @param dateString the date string
     * @param fromFormat the from format
     * @param toFormat   the to format
     * @return the formated date
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDateToday(String dateString, String fromFormat, String toFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        Calendar cal = Calendar.getInstance();
        String currentdate = new SimpleDateFormat(toFormat).format(cal.getTime());
        try {
            Date date = sdf.parse(dateString);
            String formated = new SimpleDateFormat(toFormat).format(date);

            boolean inTodayYesterdayFormat = true;
            if (inTodayYesterdayFormat) {
                if (currentdate.equals(formated)) {
                    return "Today";
                }
                cal.add(Calendar.DATE, -1);
                currentdate = new SimpleDateFormat(toFormat).format(cal.getTime());
                if (currentdate.equals(formated)) {
                    return "Yesterday";
                }
            }
            return formated;
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * Sets the list view height based on children.
     *
     * @param listView the new list view height based on children
     * @param addview  the addview
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, 0);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int addview) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount() + addview; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Function to display simple Alert Dialog.
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param resId   the res id
     */
    public static void showAlertDialog(Context context, String title, String message, int resId) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (resId != -1)
            // Setting alert dialog icon
            alertDialog.setIcon(resId);

        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Open an asset using ACCESS_STREAMING mode and COPY that data base to you application. This provides access to files that have been bundled with an application as assets -- that is, files placed
     * in to the "assets" directory.
     *
     * @param context The context to use. Usually your {@link android.app.Application} or {@link Activity} object.
     * @param dbName  the db name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copydatabase(Context context, String dbName) throws IOException {
        InputStream myinput = context.getAssets().open(dbName);
        String destPath = context.getFilesDir().getPath();
        String DB_PATH = destPath.substring(0, destPath.lastIndexOf("/")) + "/databases/" + dbName;
        d("" + DB_PATH + " file is copying.. ");
        OutputStream myoutput = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    /**
     * Milli seconds to timer.
     *
     * @param milliseconds the milliseconds
     * @return the string
     */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Gets the progress percentage.
     *
     * @param currentDuration the current duration
     * @param totalDuration   the total duration
     * @return the progress percentage
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Progress to timer.
     *
     * @param progress      the progress
     * @param totalDuration the total duration
     * @return the int
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    /**
     * Indicates whether all type of network connectivity exists and it is possible to establish connections and pass data.
     * <p/>
     * Always call this before attempting to perform data transactions.
     *
     * @param context The context to use. Usually your {@link android.app.Application} or {@link Activity} object.
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager == null)
            return false;
        else {
            NetworkInfo[] nis = cManager.getAllNetworkInfo();
            if (nis != null) {
                for (NetworkInfo ni : nis) {
                    if (ni.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if is email valid.
     *
     * @param email the email
     * @return true, if is email valid
     */
    public static boolean isEmailValid(String email) {
        String EMAILADDRESS_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAILADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Checks if is date valid.
     *
     * @param email the email
     * @return true, if is date valid
     */
    public static boolean isDateValid(String email) {
        String EMAILADDRESS_PATTERN = "(\\d{4})-(\\d{2})-(\\d{2})";
        Pattern pattern = Pattern.compile(EMAILADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Indicates whether WIFI or Mobile data network connectivity exists and it is possible to establish connections and pass data.
     * <p/>
     * Always call this before attempting to perform data transactions.
     *
     * @param context The context to use. Usually your {@link android.app.Application} or {@link Activity} object.
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean hasConnection(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (network != null && network.isConnected()) {
            return true;
        }

        network = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (network != null && network.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Date.
     *
     * @param data the data
     * @return the string
     */
    @SuppressLint("SimpleDateFormat")
    public static String date(String data) {
        String strDate;
        SimpleDateFormat format1 = new SimpleDateFormat("dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy hh:mm a");
        strDate = format1.format(new Date(Long.parseLong(data)));
        int mon = Integer.parseInt(format2.format(new Date(Long.parseLong(data))));
        strDate += "-" + Utility.getMonth(mon) + "-";
        strDate += format3.format(new Date(Long.parseLong(data)));
        return strDate;
    }

    /**
     * Show keyboard.
     *
     * @param context the context
     * @param view    the view
     */
    public static void showKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Hide keyboard.
     *
     * @param context the context
     * @param view    the view
     */
    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the month.
     *
     * @param index the index
     * @return the month
     */
    public static String getMonth(int index) {
        return month[index];
    }

    /**
     * Decode uri.
     *
     * @param context       the context
     * @param selectedImage the selected image
     * @return the bitmap
     * @throws FileNotFoundException the file not found exception
     */
    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    /**
     * Launch a new activity. You will not receive any information about when the activity exits.
     * <p/>
     * <p/>
     * Note that if this method is being called from outside of an {@link Activity} Context, then the Intent must include the {@link Intent#FLAG_ACTIVITY_NEW_TASK} launch flag. This is
     * because, without being started from an existing Activity, there is no existing task in which to place the new activity and thus it needs to be placed in its own separate task.
     *
     * @param packageContext A Context of the application package starting new activity from this class.
     * @param cls            The component class that is to be Launch.
     */
    public static void startAct(Context packageContext, Class<?> cls) {
        startAct(packageContext, cls, null, null);
    }

    /**
     * String to bit map.
     *
     * @param encodedString the encoded string
     * @return the bitmap
     */
    // Convert String To Bitmap
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }

    }

    /**
     * Bit map to string.
     *
     * @param bitmap the bitmap
     * @return the string
     */
    // Convert image Bitmap to String
    public static String BitMapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);

            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * Launch a new activity. You will not receive any information about when the activity exits.
     * <p/>
     * <p/>
     * Note that if this method is being called from outside of an {@link Activity} Context, then the Intent must include the {@link Intent#FLAG_ACTIVITY_NEW_TASK} launch flag. This is
     * because, without being started from an existing Activity, there is no existing task in which to place the new activity and thus it needs to be placed in its own separate task.
     *
     * @param packageContext A Context of the application package starting new activity from this class.
     * @param cls            The component class that is to be Launch.
     * @param name           The name of the extra data, with package prefix.
     * @param value          The String data value.
     */

    public static void startAct(Context packageContext, Class<?> cls, String name, String value) {
        try {
            Intent intent = new Intent(packageContext, cls);
            if (name != null && value != null)
                intent.putExtra(name, value);
            packageContext.startActivity(intent);
            ((Activity) packageContext).finish();
        } catch (ActivityNotFoundException e) {
            Utility.e("have you declared \"" + cls.getSimpleName() + ".java\" activity in your AndroidManifest.xml?");
        }
    }

    /**
     * Start act.
     *
     * @param packageContext the package context
     * @param intent         the intent
     */
    public static void startAct(Context packageContext, Intent intent) {
        try {
            packageContext.startActivity(intent);
            ((Activity) packageContext).finish();
        } catch (ActivityNotFoundException e) {
            Utility.e("have you declared \"" + intent.getClass() + ".java\" activity in your AndroidManifest.xml?");
        }
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context The context to use. Usually your object.
     * @param text    The text to show. Can be formatted text.
     */
    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Make a standard toast that just contains a text view. that not use another toast to next 10 second.
     *
     * @param context The context to use. Usually your object.
     * @param text    The text to show. Can be formatted text.
     */
    public static void toast(Context context, String text) {
        if (time == 0) {
            time = 10;
        }
        if (time == 10) {
            Utility x = new Utility();
            x.new toastTime().execute();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the size of the display, in pixels.
     * <p>
     * Note that this value should <em>not</em> be used for computing layouts, since a device will typically have screen decoration (such as a status bar) along the edges of the display that reduce
     * the amount of application space available from the size returned here. Layouts should instead use the window size.
     * </p>
     * <p>
     * The size is adjusted based on the current rotation of the display.
     * </p>
     * <p>
     * The size returned by this method does not necessarily represent the actual raw size (native resolution) of the display. The returned size may be adjusted to exclude certain system decoration
     * elements that are always visible. It may also be scaled to provide compatibility with older applications that were originally designed for smaller displays.
     * </p>
     *
     * @param activity A Activity of the application package implementing this class.
     * @return the screen size
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static Point getScreenSize(Activity activity) {
        Point size = new Point();
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            d.getSize(size);
        } else {
            size.x = d.getWidth();
            size.y = d.getHeight();
        }
        return size;
    }

    /**
     * Gets the part of the display, in pixels.
     * <p>
     * Note that this value should <em>not</em> be used for computing layouts, since a device will typically have screen decoration (such as a status bar) along the edges of the display that reduce
     * the amount of application space available from the size returned here. Layouts should instead use the window size.
     * </p>
     * <p>
     * The size is adjusted based on the current rotation of the display.
     * </p>
     * <p>
     * The size returned by this method does not necessarily represent the actual raw size (native resolution) of the display. The returned size may be adjusted to exclude certain system decoration
     * elements that are always visible. It may also be scaled to provide compatibility with older applications that were originally designed for smaller displays.
     * </p>
     *
     * @param activity A Activity of the application package implementing this class.
     * @param percent  A percent how part you get.
     * @return the screen part
     */
    public static int getScreenPart(Activity activity, float percent) {
        int i = (int) ((getScreenSize(activity).y * percent) / 100);
        return i;
    }

    /**
     * Send a log message.
     *
     * @param msg The message you would like logged.
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG)
            Log.d("Library", "" + msg);
    }

    /**
     * Send an log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG)
            Log.e("Library", "** " + msg + " **");
    }

    /**
     * Post a notification to be shown in the status bar.
     *
     * @param context The Context in which this PendingIntent should start the activity.
     * @param intent  Intent of the activity to be launched.
     * @param icon    A resource ID in the application's package of the drawable to use.
     * @param image   Add a large icon to the notification (and the ticker on some devices).
     * @param title   Set the first line of text in the platform notification template.
     * @param body    Set the second line of text in the platform notification template.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void Notified(Context context, Intent intent, int icon, Bitmap image, String title, String body) {
        if (Build.VERSION.SDK_INT > 10) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            Notification.Builder builder;
            NotificationManager notifier;
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder = new Notification.Builder(context);
            builder.setSmallIcon(icon).setLargeIcon(image).setWhen(System.currentTimeMillis()).setOngoing(true).setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(uri);
            builder.setContentIntent(pendingIntent);
            notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT <= 15)
                notifier.notify(1, builder.getNotification());
            else if (Build.VERSION.SDK_INT > 15)
                notifier.notify(1, builder.build());
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentText(body).setContentTitle(title).setOngoing(true).setSmallIcon(icon).setLargeIcon(image)
                    .setAutoCancel(true).setSound(uri); // setTicker("my ticker")

            builder.setContentIntent(pendingIntent);
            Notification notif = builder.getNotification();

            NotificationManager mN = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mN.notify(1, notif);
        }
    }

    /**
     * Save iamge.
     *
     * @param finalBitmap   the final bitmap
     * @param name          the name
     * @param directoryName the directory name
     * @return the string
     */
    public static String SaveIamge(Bitmap finalBitmap, String name, String directoryName) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + directoryName);
        myDir.mkdirs();
        String fname = "/thumb.JPEG";
        File file = new File(myDir, fname);

        try {
            file.createNewFile();
            if (file.exists())
                file.delete();
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            out.flush();
            out.close();
            return root + "/" + directoryName + fname;

        } catch (FileNotFoundException e) {
            Log.d("Tag", "Error saving image file: " + e.getMessage());
            return "";
        } catch (IOException e) {
            Log.d("Tag", "Error saving image file: " + e.getMessage());
            return "";
        }
    }

    /**
     * An URI and a imageView.
     *
     * @param context  the context
     * @param imageURI the image uri
     * @return the rotation bitmap
     */
    public static Bitmap getRotationBitmap(Context context, String imageURI) {
        // Get the original bitmap dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageURI, options);
        float rotation = rotationForImage(context, Uri.fromFile(new File(imageURI)));

        if (rotation != 0) {
            // New rotation matrix
            Matrix matrix = new Matrix();
            matrix.preRotate(rotation);
            return (Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true));
        } else {
            // No need to rotate
            return (BitmapFactory.decodeFile(imageURI, options));
        }
    }

    /**
     * Returns how much we have to rotate.
     *
     * @param context the context
     * @param uri     the uri
     * @return the float
     */
    public static float rotationForImage(Context context, Uri uri) {
        try {
            if (uri.getScheme().equals("content")) {
                // From the media gallery
                String[] projection = {Images.ImageColumns.ORIENTATION};
                Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToFirst()) {
                    return c.getInt(0);
                }
            } else if (uri.getScheme().equals("file")) {
                // From a file saved by the camera
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            }
            return 0;

        } catch (IOException e) {
//			Log.d("Error checking exif");
            return 0;
        }
    }

    /**
     * Get rotation in degrees.
     *
     * @param exifOrientation the exif orientation
     * @return the float
     */
    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * The Class toastTime.
     */
    private class toastTime extends AsyncTask<Void, Void, Void> {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {
            time = 0;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (time >= 5)
                    return null;
                try {
                    Thread.sleep(500L);
                    time++;
                } catch (InterruptedException localInterruptedException) {
                    while (true)
                        localInterruptedException.printStackTrace();
                }
            }
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            time = 10;
        }
    }

}
