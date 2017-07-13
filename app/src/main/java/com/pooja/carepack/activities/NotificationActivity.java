package com.pooja.carepack.activities;

import android.app.Activity;
import android.content.Intent;

import com.pooja.carepack.utils.MyPrefs;

public class NotificationActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        if (new MyPrefs(getApplicationContext()).get(MyPrefs.keys.USERNAME).length() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("extra", getIntent().getExtras());
            startActivity(intent);
        }
        finish();
    }

}
