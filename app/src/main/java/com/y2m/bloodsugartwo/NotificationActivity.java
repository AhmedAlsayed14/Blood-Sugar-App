package com.y2m.bloodsugartwo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textview = (TextView) findViewById(R.id.content);
        textview.setClickable(true);
        textview.setMovementMethod(LinkMovementMethod.getInstance());

        if(getIntent().hasExtra("num")) {
            int num = getIntent().getExtras().getInt("num");
            Log.d("not1", num + "");

            textview.setText(sharedPreferences.getString("content" + num, "content"));
        }
        else {
            Log.d("not1", sharedPreferences.getInt("last", 0) + "");
            Log.d("not1", sharedPreferences.getString("content" + sharedPreferences.getInt("last", 0), "content"));
            textview.setText(sharedPreferences.getString("content" + sharedPreferences.getInt("last", 0), "content"));
        }
    }




}