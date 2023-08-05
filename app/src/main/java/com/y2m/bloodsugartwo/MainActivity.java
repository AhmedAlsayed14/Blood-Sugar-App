package com.y2m.bloodsugartwo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private float lastTranslate = 0.0f;
    private ImageButton menu;
    private DrawerLayout drawer;
    private FrameLayout frame;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View view_line;
    private RelativeLayout menu_item1,menu_item2,menu_item3,menu_item4,menu_item5,menu_item6,menu_item7;
    private TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,title,nav_title;
    private ImageButton addButton;
    CallbackManager callbackManager;
    boolean doubleBackToExitPressedOnce = false;
    ListView listView;
    ArrayAdapter adapter;
    private ArrayList<Item> itemArrayList;
    private SugerDBHandler db;
    static SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("user");
        FirebaseInstanceId.getInstance().getToken();
        db = new SugerDBHandler (MainActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppPreferences.getInstance(getApplicationContext()).incrementLaunchCount();
        showRateAppDialogIfNeeded();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Locale locale = new Locale(prefs.getString("language", "en"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        if (!prefs.getBoolean("firstTime", false) ){
            SampleAlarmReceiver alarm1 = new SampleAlarmReceiver();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 16);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            alarm1.setAlarm(this,cal,1);
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    //Toast.makeText(MainActivity.this, "share Success", Toast.LENGTH_SHORT).show();
//                    selectLanguage();
                }
                @Override
                public void onCancel() {
                    //Toast.makeText(MainActivity.this, "share Cancel", Toast.LENGTH_SHORT).show();
//                    selectLanguage();
                }
                @Override
                public void onError(FacebookException error) {
                    //Toast.makeText(MainActivity.this, "share Error", Toast.LENGTH_SHORT).show();
//                    selectLanguage();
             }
            });
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(getResources().getString(R.string.app_title))
                        .setContentUrl(Uri.parse("https://fb.me/1298129213606404"))
                        .setContentDescription("I download this application from google play try it ")
                        .setImageUrl(Uri.parse("http://y2m.esy.es/SugerTraker/icon.png"))
                        .build();
                    shareDialog.show(linkContent);

            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            selectLanguage();
        }

        menu =(ImageButton)findViewById(R.id.menu);
        menu_item1=(RelativeLayout)findViewById(R.id.menu_item1);
        menu_item2=(RelativeLayout)findViewById(R.id.menu_item2);
        menu_item3=(RelativeLayout)findViewById(R.id.menu_item3);
        menu_item4=(RelativeLayout)findViewById(R.id.menu_item4);
        menu_item5=(RelativeLayout)findViewById(R.id.menu_item5);
        menu_item6=(RelativeLayout)findViewById(R.id.menu_item6);
        menu_item7=(RelativeLayout)findViewById(R.id.menu_item7);
        view_line=(View)findViewById(R.id.view_line);
        textView1=(TextView)findViewById(R.id.text1);
        textView2=(TextView)findViewById(R.id.text2);
        textView3=(TextView)findViewById(R.id.text3);
        textView4=(TextView)findViewById(R.id.text4);
        textView5=(TextView)findViewById(R.id.text5);
        textView6=(TextView)findViewById(R.id.text6);
        textView7=(TextView)findViewById(R.id.text7);
        title=(TextView)findViewById(R.id.title);
        nav_title=(TextView)findViewById(R.id.nav_title);
        addButton=(ImageButton) findViewById(R.id.addnewitem);
        String font = "DroidKufi-Regular.ttf";
        Typeface tf = Typeface.createFromAsset(this.getAssets(), font);
        textView1.setTypeface(tf);
        textView2.setTypeface(tf);
        textView3.setTypeface(tf);
        textView4.setTypeface(tf);
        textView5.setTypeface(tf);
        textView6.setTypeface(tf);
        textView7.setTypeface(tf);
        title.setTypeface(tf);
        nav_title.setTypeface(tf);

        menu_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLinkUrl, previewImageUrl;
                appLinkUrl = "https://fb.me/1298129213606404";
                previewImageUrl = "http://y2m.esy.es/SugerTraker/icon.png"; //// TODO: 19-Mar-17 add application icon
                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(MainActivity.this, content);
                }
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(MainActivity.this, AllnotificationsActivity.class);
                startActivity(MainIntent);
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + MainActivity.this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                }
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=Y2M")));
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(MainIntent);
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLanguage();
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu_item7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(MainActivity.this, AverageActivity.class);
                startActivity(MainIntent);
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                else
                {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frame = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                slideOffset=slideOffset*-1;
                float moveFactor = (view_line.getWidth() * slideOffset);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    frame.setTranslationX(moveFactor);
                }
                else
                {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    frame.startAnimation(anim);
                    lastTranslate = moveFactor;
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        AppPreferences.getInstance(getApplicationContext()).incrementLaunchCount();
        showRateAppDialogIfNeeded();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(MainActivity.this, AddNewRowActivity.class);
                startActivity(MainIntent);
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4+
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("badgeCount",0);
        editor.commit();

    }
    private void selectLanguage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.languages);
        // Add the buttons
        builder.setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                dialog.dismiss();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("language", languageToLoad);
                editor.commit();
                adapter.notifyDataSetChanged();
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();

            }
        });
        builder.setNegativeButton(R.string.arabic, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                String languageToLoad = "ar"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                dialog.dismiss();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("language", languageToLoad);
                editor.commit();
                adapter.notifyDataSetChanged();
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();

            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void showRateAppDialogIfNeeded()
    {
        boolean bool = AppPreferences.getInstance(getApplicationContext()).getAppRate();
        int i = AppPreferences.getInstance(getApplicationContext()).getLaunchCount();
        if ((bool) && (i == 5)) {
            createAppRatingDialog(getString(R.string.rate_app_title), getString(R.string.rate_app_message)).show();
        }
    }
    private AlertDialog createAppRatingDialog(String rateAppTitle, String rateAppMessage)
    {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.dialog_app_rate), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                openAppInPlayStore(MainActivity.this);
                AppPreferences.getInstance(MainActivity.this.getApplicationContext()).setAppRate(false);
            }
        }).setNegativeButton(getString(R.string.dialog_ask_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                AppPreferences.getInstance(MainActivity.this.getApplicationContext()).setAppRate(false);
            }
        }).setNeutralButton(getString(R.string.dialog_ask_later), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                AppPreferences.getInstance(MainActivity.this.getApplicationContext()).resetLaunchCount();
            }
        }).setMessage(rateAppMessage).setTitle(rateAppTitle).create();
        return dialog;
    }
    public static void openAppInPlayStore(Context paramContext)
    {
        paramContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=y2m.com.sugartracker")));
    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END))
        {
            drawer.closeDrawer(GravityCompat.END);
        }
        else
        {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "إضغط مره أخري للخروج من التطبيق", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
    @Override
    public void onResume()
    {
        itemArrayList=db.getAllRows();
        Collections.sort(itemArrayList);
        listView = (ListView) this.findViewById(R.id.list_item);
        adapter = new CustomAdapter(this, itemArrayList);
        listView.setAdapter(adapter);
        super.onResume();
    }
}
