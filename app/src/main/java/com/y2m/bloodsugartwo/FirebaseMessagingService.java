package com.y2m.bloodsugartwo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Mohamed Antar on 10/18/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
        SharedPreferences sharedPreferences ;
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage)
        {
                String message;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                if (remoteMessage.getData().get("notification")!=null)
                {
                        message = remoteMessage.getData().get("notification");
                        int i = sharedPreferences.getInt("last", 0);
                        i++;
                        sharedPreferences.edit().putString("content"+i,message).apply();
                        sharedPreferences.edit().putInt("last", i).apply();
                        createNotification(getResources().getString(R.string.app_title), message);
                        int badgeCount = sharedPreferences.getInt("badgeCount", 0);
                        badgeCount++;
                        ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4+
                        ShortcutBadger.applyCount(this, badgeCount);
                        sharedPreferences.edit().putInt("badgeCount", badgeCount).apply();
                }
        }
        private void createNotification(String title, String body) {
                Log.d("not","notification Body"+ body);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                // sharedPreferences.edit().putString("last", i).apply();
                Intent intent = new Intent(this, NotificationActivity.class);
                Intent backIntent = new Intent(this, AllnotificationsActivity.class);
                Intent main = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivities(getApplication(), 1, new Intent[]{main, backIntent, intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                Context context = getBaseContext();
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;
                NotificationManager mNotificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(m, mBuilder.build());
        }

}