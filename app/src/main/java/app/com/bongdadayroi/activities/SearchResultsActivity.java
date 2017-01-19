package app.com.bongdadayroi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdSize;
import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.api.VmaxSdk;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.adapters.ListFavoriteAdapter;
import app.com.bongdadayroi.adapters.MyAdapter;
import app.com.bongdadayroi.adapters.MyListAdapter;
import app.com.bongdadayroi.features.ad.banner.BannerAdPresenter;
import app.com.bongdadayroi.features.ad.banner.BannerAdView;
import app.com.bongdadayroi.models.Data;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.utils.NewAPI;
import vn.amobi.util.ads.AmobiAdView;

public class SearchResultsActivity extends AppCompatActivity implements BannerAdView {

    private android.support.v7.app.ActionBar actionBar;

    private ProgressBar progressBar;

    private ListView listView;

    private LinearLayout llTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VmaxSdk.init(this);
        setContentView(R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBannerAdPresenter = new BannerAdPresenter(this);
        setUpAmobiAd();
        setUpVmaxAd();

        TFirebaseAnalytics.setAnalytic(this);


        if(getSupportActionBar()!=null){
            actionBar = getSupportActionBar();
            setUpActionBar();
        }
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        listView = (ListView)findViewById(R.id.lvResult);

        llTry = (LinearLayout)findViewById(R.id.llTry);

        Button button = (Button) findViewById(R.id.btRetry);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResult();
            }
        });

        loadResult();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResultsActivity.this, AdsActivity.class);
                intent.putExtra("video", (MyVideo)listView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

    }

    public void loadResult() {
        final String keyword = getIntent().getStringExtra("query");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestSearch(keyword);
                    }
                }).start();
            }
        });
    }

    private void setUpActionBar() {
        if(actionBar!=null){
            actionBar.setTitle("Kết quả tìm kiếm");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void requestSearch(String keyword){

        HashMap<String, String> params = new HashMap<>();
        params.put(NewAPI.PARAM_APP_ID, Config.APP_ID);
        params.put(NewAPI.PARAM_TYPE, "search");
        params.put("keyword", keyword);

        AndroidNetworking.get(NewAPI.HOST_VIDEO_API)
                .addQueryParameter(params)
                .setTag("search")
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        MyData myData = MyData.getInstance();

                        Data data = gson.fromJson(response.toString(), Data.class);

                        ArrayList<MyVideo> arrayList = new ArrayList<>();

                        Collections.addAll(arrayList, data.getData());

                        myData.setArrayResult(arrayList);

                        MyAdapter.getInstance().setMyListAdapter(new MyListAdapter(SearchResultsActivity.this, MyData.getInstance().getArrayResult()));
                        listView.setAdapter(MyAdapter.getInstance().getMyListAdapter());

                        progressBar.setVisibility(View.GONE);
                        llTry.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ANError ANError) {
                        progressBar.setVisibility(View.GONE);
                        llTry.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
        AndroidNetworking.cancel("search");
    }

    private VmaxAdView mVmaxAdView ;

    private AmobiAdView mAmobiAdView;

    private BannerAdPresenter mBannerAdPresenter;

    private void setUpAmobiAd() {
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

    @Override
    protected void onPause() {
        if (mVmaxAdView != null) {

            mVmaxAdView.onPause();
        }
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onResume();
        }
        listView.setAdapter(new ListFavoriteAdapter(getBaseContext()));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        if (mVmaxAdView != null) {
            mVmaxAdView.finish();
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (mVmaxAdView != null) {
            mVmaxAdView.onBackPressed();
        }
        super.onBackPressed();
    }

}
