package com.pooja.carepack.gcm;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.NotificationActivity;
import com.pooja.carepack.fragments.BaseFragment;
import com.pooja.carepack.utils.MyPrefs;

import java.util.List;

public class GcmIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    // private SharedPrefs prefs;
    // private JSONArray mPayLoadArray;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (new MyPrefs(getApplicationContext()).get(MyPrefs.keys.USERNAME).length() > 0) {
            BaseFragment.Log.d("One notification received");
            Bundle extras = intent.getExtras();
            String message = "One notification received";
//        if (extras != null && !extras.isEmpty() && extras.containsKey("message")) {
            // GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            // String messageType = gcm.getMessageType(intent);
            // if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            // sendNotification("Send error: ", extras.toString(), null);
            // else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            // sendNotification("Deleted messages on server: ", extras.toString(), null);
            // else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            // sendNotification("Nanwo", message, extras);
            // GcmBroadcastReceiver.completeWakefulIntent(intent);
//			NOTIFICATION_ID = Integer.parseInt(mNotification.eID);
            sendNotification(getString(R.string.app_name), message, extras);
//        }
        }
    }

    private void sendNotification(String title, String msg, Bundle bundle) {
        if (new MyPrefs(getApplicationContext()).get(MyPrefs.keys.USERNAME).length() > 0) {
            // if (!isRunning(this)) {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("extra", bundle);
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setStyle(new
                            NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg).setAutoCancel(true)
                    .setSound(uri);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    // public static String bundle2string(Bundle bundle) {
    // String string = "Bundle{";
    // for (String key : bundle.keySet())
    // string += " " + key + " => " + bundle.get(key) + ";";
    // string += " }Bundle";
    // return string;
    // }

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

}
