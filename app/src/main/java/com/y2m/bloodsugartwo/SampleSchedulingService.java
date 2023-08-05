package com.y2m.bloodsugartwo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int missedDay=prefs.getInt("missedDay",0);
        Log.d("missedDay","missedDay "+missedDay);
        if (missedDay>=2)
        {
            Log.d("createNotification","createNotification "+missedDay);
            int badgeCount = prefs.getInt("badgeCount", 0);
            badgeCount++;
            ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4+
            ShortcutBadger.applyCount(this, badgeCount);
            prefs.edit().putInt("badgeCount", badgeCount).apply();
            sendNotification(getResources().getString(R.string.app_title),getResources().getString(R.string.notification));
        }
        missedDay=missedDay+1;
        if (missedDay>=3)
        {
            Log.d("missedDay","reset  "+missedDay);
            missedDay=0;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("missedDay", missedDay);
        editor.commit();
        SampleAlarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String title, String body) {
        Context context = getBaseContext();
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AddNewRowActivity.class);
        Intent main = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivities(getApplication(), 1, new Intent[]{main, intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(body);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(2017, mBuilder.build());

    }

}