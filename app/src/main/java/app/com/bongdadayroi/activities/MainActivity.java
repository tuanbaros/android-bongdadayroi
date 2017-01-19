package app.com.bongdadayroi.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdSize;
import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.api.VmaxSdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.adapters.ListAdapter;
import app.com.bongdadayroi.adapters.MyAdapter;
import app.com.bongdadayroi.adapters.TWVAdapter;
import app.com.bongdadayroi.features.ad.banner.BannerAdPresenter;
import app.com.bongdadayroi.features.ad.banner.BannerAdView;
import app.com.bongdadayroi.fragments.DrawerFragment;
import app.com.bongdadayroi.fragments.HomeFragment;
import app.com.bongdadayroi.models.Category;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.myapp.MyApplication;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.myapp.Share;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.push.MyHandler;
import vn.amobi.util.ads.AmobiAdView;

public class MainActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener,
        BannerAdView {

    DrawerLayout drawerLayout;

    private boolean flag = false;

    private ActionBar actionBar;

    private SearchView searchView;

    private HomeFragment homeFragment = new HomeFragment();

    private DrawerFragment[] drawerFragments;
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;

    private static Boolean isVisible = false;

    boolean checkshare = false;

    RelativeLayout Llayout_customshare;

    private String widgetAmobiId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VmaxSdk.init(this);
        setContentView(R.layout.activity_main);

        mBannerAdPresenter = new BannerAdPresenter(this);
        setUpAmobiAd();
        setUpVmaxAd();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        Category category = Category.getInstance();
        MyData myData = MyData.getInstance();
        drawerFragments = new DrawerFragment[category.getData().length+2];
        for (int i = 0; i < category.getData().length+2; i++){
            drawerFragments[i] = new DrawerFragment();
            if(i < category.getData().length){
                myData.getArrayListData().add(new ArrayList<MyVideo>());
            }
        }

        MyAdapter myAdapter = MyAdapter.getInstance();

        myAdapter.setNewAdapter(new TWVAdapter(this, myData.getArrayNew()));
        myAdapter.setMostAdapter(new TWVAdapter(this, myData.getArrayMost()));

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getWidgetAmobiId();

        setUpListViewDrawer();

        setUpFragment(0);

        setUpPush();

        setUpShare();
    }

    private void getWidgetAmobiId() {
        try {
            ApplicationInfo app = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            widgetAmobiId = bundle.getString("vn.amobi.util.ads.widget_id");
            Log.i("widget", "widget: " + widgetAmobiId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setUpListViewDrawer() {

        TextView textView = (TextView)findViewById(R.id.feature);
        textView.setText(Config.APP_NAME);

        ListView listView = (ListView) findViewById(R.id.lvMenu);

        LayoutInflater inflater = getLayoutInflater();

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.navi_drawer_footer, null);

        listView.addFooterView(view, null, false);

        TextView tvAccount, tvFavorite, tvWebsite, tvQstore, tvGradle;

        LinearLayout llAccount = (LinearLayout)findViewById(R.id.tvAccount);
        tvAccount = (TextView)llAccount.getChildAt(0);
        tvAccount.setText("Tài khoản");
        tvAccount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account, 0, 0, 0);
        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llFavorite = (LinearLayout)findViewById(R.id.tvFavorite);
        tvFavorite = (TextView)llFavorite.getChildAt(0);
        tvFavorite.setText("Yêu thích");
        tvFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        llFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);


            }
        });

        LinearLayout llWebsite = (LinearLayout)findViewById(R.id.tvWebsite);
        tvWebsite = (TextView)llWebsite.getChildAt(0);
        tvWebsite.setText("Website");
        tvWebsite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        llWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://bongdadayroi.com/?utm_source=inapp-contact")));
            }
        });

        LinearLayout llQstore = (LinearLayout)findViewById(R.id.tvQstore);
        tvQstore = (TextView)llQstore.getChildAt(0);
        tvQstore.setText("Ứng dụng hot");
        tvQstore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cart, 0, 0, 0);
        llQstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (widgetAmobiId.equals(Config.MOBIISTAR_WIDGET_AD)){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://amb.qstore.vn/")));
                }else {
                    if (widgetAmobiId.equals(Config.VGROUP_WIDGET_AD)){
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://gamepikachu.mobi/")));
                    }else{
                        Uri uri = Uri.parse("market://apps/developer?id=" + Config.ACCOUNT_PUBLIC);
                        Intent goToMarket1 = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket1);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/developer?id=" + Config.ACCOUNT_PUBLIC)));
                        }
                    }
                }
            }
        });

        LinearLayout llGradle = (LinearLayout)findViewById(R.id.tvGradle);
        tvGradle = (TextView)llGradle.getChildAt(0);
        tvGradle.setText("Đánh giá và xếp hạng");
        tvGradle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like, 0, 0, 0);

        llGradle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        Category category = Category.getInstance();


        listView.setAdapter(new ListAdapter(this, category.getData()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUpFragment(position);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

    }

    private void setUpFragment(int position) {
        if(searchView!=null){
            searchView.onActionViewCollapsed();
        }
        String title = setUpTitle(position);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        Fragment fragmet;
        if(position>0){
            fragmet = drawerFragments[position-1];
            transaction.replace(R.id.fragment, drawerFragments[position-1], title);
        }else{
            fragmet = homeFragment;
            transaction.replace(R.id.fragment, homeFragment, title);
        }

        if (fragmet.getArguments()==null){
            fragmet.setArguments(bundle);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String setUpTitle(int position) {
        Resources resources = getResources();
        String title;
        if (position > 0) {
            if(position<3){
                title = resources.getStringArray(R.array.category)[position];
            }else{
                Category category = Category.getInstance();
                title = category.getData()[position-3].getName();
            }
        } else {
            title = Config.APP_NAME;
        }
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
        TFirebaseAnalytics.setAnalytic(this, title);
        return title;

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(Llayout_customshare.getVisibility()==View.VISIBLE){
                hideShare();
            }else{
                if (homeFragment.isVisible()) {
                    if (!flag) {
                        Toast.makeText(this, "Nhấn Back lần nữa để thoát!", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                flag = true;
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                flag = false;
                            }
                        }).start();
                    } else {
                        if (mVmaxAdView  != null) {
                            mVmaxAdView .onBackPressed();
                        }
                        super.onBackPressed();
                        finish();
                    }

                } else {
                    setUpFragment(0);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        if (item.getItemId() == R.id.action_search) {

        }
        if(item.getItemId() == R.id.action_share){
            if (checkshare) {
                hideShare();
            } else {
                showShare();
            }
        }
        if(item.getItemId() == R.id.action_setting){
            FacebookUser user = FacebookUser.getInstance();
            if(user.getUser_id()!=null) {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void startResultScreen(String query) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
        searchView.onActionViewCollapsed();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query!=""){
            startResultScreen(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onPause() {
        if (mVmaxAdView  != null) {

            mVmaxAdView .onPause();
        }
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        isVisible = false;
    }

    @Override
    public void onDestroy() {
        if (mVmaxAdView  != null) {
            mVmaxAdView .onDestroy();
        }
        super.onDestroy();
        MyApplication.deleteCache(this);
    }

    @Override
    public void onResume(){
        if (mVmaxAdView  != null) {
            mVmaxAdView .onResume();
        }
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    private void setUpPush() {
        MyHandler.mainActivity = this;
        NotificationsManager.handleNotifications(this, Config.SENDER_ID, MyHandler.class);
        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(Config.HUB_NAME, Config.HUB_LISTEN_CONNECTION_STRING, this);
        registerWithNotificationHubs();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String regid = null;
                try {
                    FacebookUser facebookUser = FacebookUser.getInstance();
                    regid = GoogleCloudMessaging.getInstance(getBaseContext()).register(Config.SENDER_ID);
                    hub.register(regid, facebookUser.getUser_id());
                    Log.i("facebookID", "facebookID: " + facebookUser.getUser_id());
                } catch (IOException e) {
                    Log.i("facebookID", "facebookID: ");
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.i("facebookID", "facebookID: " + e);
                    e.printStackTrace();
                }
                Log.i("regID", "regID: " + regid);
            }
        }).start();
    }



    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    gcm.register(Config.SENDER_ID);
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }

    private void setUpShare(){
        RelativeLayout Rlayout_fb;
        RelativeLayout Rlayout_gg;
        RelativeLayout Rlayout_mail;
        RelativeLayout Rlayout_tw;
        RelativeLayout Rlayout_sms;
        RelativeLayout Rlayout_web;
        RelativeLayout Rlayout_link;
        RelativeLayout Rlayout_delete;
        RelativeLayout Rlayout_khac;


        //share
        Llayout_customshare = (RelativeLayout)
                findViewById(R.id.Llayout_customshare);
        Llayout_customshare.setLayoutParams(new RelativeLayout.LayoutParams(ScreenSize.WIDTH, ScreenSize.HEIGHT));
        Llayout_customshare.setVisibility(View.GONE);
        Rlayout_fb = (RelativeLayout)findViewById(R.id.Rlayout_fb);
        Rlayout_tw = (RelativeLayout)
                findViewById(R.id.Rlayout_tw);
        Rlayout_gg = (RelativeLayout)findViewById(R.id.Rlayout_gg);
        Rlayout_mail = (RelativeLayout)findViewById(R.id.Rlayout_mail);
        Rlayout_sms = (RelativeLayout) findViewById(R.id.Rlayout_sms);
        Rlayout_web = (RelativeLayout) findViewById(R.id.Rlayout_web);
        Rlayout_link = (RelativeLayout)findViewById(R.id.Rlayout_link);
        Rlayout_delete = (RelativeLayout)findViewById(R.id.Rlayout_delete);
        Rlayout_khac = (RelativeLayout)findViewById(R.id.Rlayout_khac);


        //share click
        Rlayout_fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareFb(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);
            }
        });

        Rlayout_tw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareTw(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);

            }
        });

        Rlayout_gg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareGg(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);

            }
        });

        Rlayout_mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareMail(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);

            }
        });

        Rlayout_sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareSMS(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);

            }
        });

        Rlayout_web.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Share.shareWeb(MainActivity.this, "http://bongdadayroi.com/");

            }
        });
//
        Rlayout_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(), "Copy link",
                        Toast.LENGTH_SHORT).show();
            }
        });
//
        Rlayout_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideShare();
            }
        });

        Rlayout_khac.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Share.shareKhac(MainActivity.this, "http://bongdadayroi.com/", Config.TITLE_SHARE);
            }
        });
        Llayout_customshare.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                setTouch(event);
                return true;

            }
        });
    }

    public void hideShare() {
        checkshare = false;
        Animation animShareHide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_out_top);
        Llayout_customshare.startAnimation(animShareHide);
        Llayout_customshare.setVisibility(View.GONE);
    }

    public void showShare() {
        checkshare = true;
        Animation animShareShow = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_bottom);
        Llayout_customshare.setVisibility(View.VISIBLE);
        Llayout_customshare.startAnimation(animShareShow);

    }

    public void setTouch(MotionEvent event) {
        final int DISTANCE = 5;

        float startX = 0;
        float startY = 0;
        float dist = 0;
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                dist = event.getY() - startY;
                if ((pxToDp((int) dist) > DISTANCE)) {
                    hideShare();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

    }

    public int pxToDp(int px) {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        return Math.round(px
                / (dm.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }



    @Override
    public void finish() {
        if (mVmaxAdView  != null) {
            mVmaxAdView .finish();
        }
        super.finish();
    }

    private VmaxAdView mVmaxAdView ;

    private AmobiAdView mAmobiAdView;

    private BannerAdPresenter mBannerAdPresenter;

    private void setUpAmobiAd() {
//        TextView adsBannerTextView = (TextView)findViewById(R.id.adsBannerTextView);
//        adsBannerTextView.setLayoutParams(new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 110));

        mAmobiAdView = (AmobiAdView) findViewById(R.id.amobiAdview);
//        mAmobiAdView.setLayoutParams(new RelativeLayout.LayoutParams(320, 50));
        if (mAmobiAdView != null) {
            mAmobiAdView.setEventListener(mBannerAdPresenter);
        }
    }

    private void setUpVmaxAd() {
        mVmaxAdView  = (VmaxAdView ) findViewById(R.id.banner_adview);
//        mVmaxAdView.setLayoutParams(new RelativeLayout.LayoutParams(320, 50));
        HashMap tempAdSettings = new HashMap<>();

        tempAdSettings.put(VmaxAdSettings.AdSettings_sbd, VmaxAdSize.AdSize_320x50);
        //Scale is optional to further scale above mentioned size

        mVmaxAdView .setAdSettings(tempAdSettings);
        mVmaxAdView .setAdListener(mBannerAdPresenter);
//        mVmaxAdView.loadAd();
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView );
    }

    @Override
    public void onHadVmaxBanner() {
        mAmobiAdView.setVisibility(View.GONE);
        mVmaxAdView .setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoVmaxBanner() {
        mVmaxAdView.setVisibility(View.GONE);
        mBannerAdPresenter.loadAmobiAd(mAmobiAdView, AmobiAdView.WidgetSize.SMALL);
    }

    @Override
    public void onHadAmobiBanner() {
        if(mVmaxAdView.getVisibility()==View.GONE){
            mAmobiAdView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoAmobiBanner() {
        mAmobiAdView.setVisibility(View.GONE);
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView);
    }

    @Override
    public void onAdDismiss() {
        mBannerAdPresenter.loadVmaxAd(mVmaxAdView);
    }
}
