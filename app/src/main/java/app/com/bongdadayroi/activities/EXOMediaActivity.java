package app.com.bongdadayroi.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.astuetz.PagerSlidingTabStrip;
import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.commit451.youtubeextractor.YouTubeExtractor;
import com.devbrackets.android.exomedia.EMVideoView;
import com.devbrackets.android.exomedia.listener.EMVideoViewControlsCallback;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.adapters.DatabaseAdapter;
import app.com.bongdadayroi.adapters.LiveStreamAdapter;
import app.com.bongdadayroi.adapters.ViewPageAdapter;
import app.com.bongdadayroi.interfaces.GetInfoVideo;
import app.com.bongdadayroi.interfaces.MyLogin;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.myapp.Share;
import app.com.bongdadayroi.networks.MyRequest;
import app.com.bongdadayroi.networks.TFirebaseAnalytics;
import app.com.bongdadayroi.utils.NewAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EXOMediaActivity extends YouTubeBaseActivity
    implements GetInfoVideo, MyLogin, YouTubePlayer.OnInitializedListener {
    private MyVideo myVideo;
    private TextView tvNumLike;
    private boolean isFullscreen = false;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView mYouTubePlayerView;
    private ScrollView scrollView;
    //    private ProgressBar progressBar;
//
//    private ListView listView;
//
//    private LinearLayout llTry;
//
//    private Button button;
    private PagerSlidingTabStrip tabsStrip;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    boolean checkshare = false;
    RelativeLayout Llayout_customshare;
    private ImageView ivLike, ivFollow;
    private int infoHeight = 0;
    private RelativeLayout rlOverlay;
    //    private ImageView ivContinue;
    //video
    EMVideoView emVideoView;
    OnPreparedListener first, second;
    OnErrorListener onErrorListener;
    ImageView imageView;
    RelativeLayout relativeLayout, rlMain;
    ArrayList<String> m3u8Array = new ArrayList<>();
    ArrayList<String> youtubeArray = new ArrayList<>();
    ArrayList<String> webArray = new ArrayList<>();
    ArrayList<String> listLinkLiveStream = new ArrayList<>();
    int count = 0;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            MyRequest.requestGetToken(getApplicationContext(), "" + accessToken.getToken(), null);
            Profile profile = Profile.getCurrentProfile();
            FacebookUser facebookUser = FacebookUser.getInstance();
            facebookUser.setInformation(profile);
            ViewPageAdapter.btLogin.setVisibility(View.GONE);
            ViewPageAdapter.rlComment.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };
    private int checkPush;
    private TwoWayView twvLiveLink;
    private boolean isLive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exomedia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPush = getIntent().getIntExtra("push", 0);
        TFirebaseAnalytics.setAnalytic(this);
        TextView textView = (TextView) findViewById(R.id.feature);
        textView.setText(Config.APP_NAME);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        myVideo = (MyVideo) getIntent().getSerializableExtra("video");
        Log.i("post_id123:", "post_id123: " + myVideo.getPost_id());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile != null) {
                    FacebookUser facebookUser = FacebookUser.getInstance();
                    facebookUser.setInformation(newProfile);
                }
            }
        };
        profileTracker.startTracking();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getBaseContext());
        databaseAdapter.open();
        Log.i("count", "count : " + databaseAdapter.getAllRows().getCount());
        databaseAdapter.close();
        setUpYoutube();
        setUpTextViewInfo();
        setUpImageViewControl();
        setUpTabTrip();
        setUpShare();
        setUpTWVLiveStream();
        setUpEMVideo();
    }

    private void setUpTWVLiveStream() {
        twvLiveLink = (TwoWayView) findViewById(R.id.twvLiveLink);
        rlOverlay = (RelativeLayout) findViewById(R.id.rlOverlay);
    }

    private void setUpButtonFullScreen() {
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        imageView = (ImageView) findViewById(R.id.ivFull);
        imageView.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout = (RelativeLayout) findViewById(R.id.topBar);
                relativeLayout.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                emVideoView.setLayoutParams(
                    new RelativeLayout.LayoutParams(ScreenSize.HEIGHT, ScreenSize.WIDTH));
                isFullscreen = true;
                imageView.setVisibility(View.GONE);
            }
        });
    }

    private void setUpEMVideo() {
        emVideoView = (EMVideoView) findViewById(R.id.emVideoView);
        setUpButtonFullScreen();
        emVideoView.setLayoutParams(
            new RelativeLayout.LayoutParams(ScreenSize.WIDTH, ScreenSize.WIDTH * 9 / 16));
        emVideoView.setDefaultControlsEnabled(true);
//        emVideoView.setOnTouchListener(onTouchListener);
        first = new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                emVideoView.start();
                emVideoView.showDefaultControls();
                rlOverlay.setVisibility(View.VISIBLE);
                if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                    imageView.setVisibility(View.VISIBLE);
                }
                if (isLive) {
                    twvLiveLink.setVisibility(View.VISIBLE);
                }
                MyRequest.countView(myVideo.getPost_id());
            }
        };
        second = new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                emVideoView.pause();
                if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        };
        onErrorListener = new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        };
        emVideoView.setVideoViewControlsCallback(new EMVideoViewControlsCallback() {
            @Override
            public boolean onPlayPauseClicked() {
                return false;
            }

            @Override
            public boolean onPreviousClicked() {
                return false;
            }

            @Override
            public boolean onNextClicked() {
                return false;
            }

            @Override
            public boolean onControlsShown() {
                if (!isFullscreen)
                    imageView.setVisibility(View.VISIBLE);
                if (isLive)
                    twvLiveLink.setVisibility(View.VISIBLE);
                rlOverlay.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onControlsHidden() {
                if (!isFullscreen)
                    imageView.setVisibility(View.GONE);
                twvLiveLink.setVisibility(View.GONE);
                rlOverlay.setVisibility(View.GONE);
                return true;
            }
        });
        playEMVideo();
    }

    private void playEMVideo() {
        isLive = false;
        count = 0;
//        comment 1
        if (myVideo.getLink_streamming() == null) {
            isLive = false;
            twvLiveLink.setVisibility(View.GONE);
            if (myVideo.getVideo_url() != null) {
                playYoutubeEMVideo();
            } else {
                playMP4EMVideo();
            }
        } else {
//            twvLiveLink.setVisibility(View.VISIBLE);
            isLive = true;
            getArrayStream();
        }
    }

    private void playM3U8VideoStream() {
        if (m3u8Array.size() > 0) {
            if (count < m3u8Array.size()) {
                emVideoView.setVideoURI(Uri.parse(m3u8Array.get(count)));
                Log.i("livestream", "livestream: " + m3u8Array.get(count));
                count++;
                emVideoView.setOnPreparedListener(first);
                emVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.i("tuan", "error");
                        playM3U8VideoStream();
                        return true;
                    }
                });
            } else {
                count = 0;
                emVideoView.setVisibility(View.GONE);
                mYouTubePlayerView.setVisibility(View.VISIBLE);
                playYoutubeStream();
            }
        } else {
            count = 0;
            emVideoView.setVisibility(View.GONE);
            mYouTubePlayerView.setVisibility(View.VISIBLE);
            playYoutubeStream();
        }
    }

    private void playYoutubeStream() {
        if (youtubeArray.size() > 0) {
            if (count < youtubeArray.size()) {
                if (youtubeArray.get(count) != null) {
                    youTubePlayer.loadVideo(youtubeArray.get(count));
                    Log.i("livestream", "livestream: " + youtubeArray.get(count));
                    count++;
                }
            } else {
//                count = 0;
//                playWebStream();
                //open web
            }
        } else {
            count = 0;
            playWebStream();
        }
    }

    private void playWebStream() {
        Intent intent = new Intent(EXOMediaActivity.this, WebStreamActivity.class);
        intent.putExtra("video", myVideo.getLink());
        startActivity(intent);
    }

    private void getArrayStream() {
        listLinkLiveStream.clear();
        m3u8Array.clear();
        youtubeArray.clear();
        String app = "(APP)";
        String android = "(AD)";
//        m3u8Array.add("http://live.csmtalk.vcdn.vn/hls/12d717e67d52563c7fa074a519181fd5/155d8b5ec5b/cungnghenhac/index.m3u8");
//        m3u8Array.add("http://live.csmtalk.vcdn.vn/hls/285381f67809c6bcdbb9c2c5b6c84dc0/155d8b16cef/thegioionline/index.m3u8");
//        listLinkLiveStream.add("http://live.csmtalk.vcdn.vn/hls/12d717e67d52563c7fa074a519181fd5/155d8b5ec5b/cungnghenhac/index.m3u8");
//        listLinkLiveStream.add("http://live.csmtalk.vcdn.vn/hls/285381f67809c6bcdbb9c2c5b6c84dc0/155d8b16cef/thegioionline/index.m3u8");
//        youtubeArray.add("rkXfOt3vNZg");
        webArray.clear();
//        comment 2
        for (String s : myVideo.getLink_streamming()) {
            if (s.contains(".m3u8") && (s.contains(android) || s.contains(app))) {
                if (s.contains(android)) {
                    listLinkLiveStream.add(s.replace(android, ""));
                    m3u8Array.add(s.replace(android, ""));
                } else {
                    listLinkLiveStream.add(s.replace(app, ""));
                    m3u8Array.add(s.replace(app, ""));
                }
            } else {
                if (s.contains("youtube.com")) {
                    listLinkLiveStream.add(s.substring(31));
                    youtubeArray.add(s.substring(31));
                } else {
                    webArray.add(s);
                }
            }
        }
        Log.i("sizeArr", "sizeArray: " + m3u8Array.size());
        isLive = true;
        twvLiveLink.setAdapter(new LiveStreamAdapter(this, listLinkLiveStream));
        twvLiveLink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String link = listLinkLiveStream.get(i);
                if (link.contains(".m3u8")) {
                    playM3U8StreamLink(link);
                } else {
                    Toast.makeText(getBaseContext(),
                        "Server đang quá tải, xin vui lòng chọn link khác!",
                        Toast.LENGTH_LONG).show();
                }
            }
        });
        playM3U8VideoStream();
    }

    private void playM3U8StreamLink(String url) {
        emVideoView.setVideoURI(Uri.parse(url));
        emVideoView.setOnPreparedListener(first);
        emVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast
                    .makeText(getBaseContext(), "Server đang quá tải, xin vui lòng chọn link khác!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void playYoutubeStreamLink(String url) {
    }

    private void playYoutubeEMVideo() {
        YouTubeExtractor extractor = YouTubeExtractor.create();
        extractor.extract(myVideo.getVideo_url().substring(32))
            .enqueue(new Callback<YouTubeExtractionResult>() {
                @Override
                public void onResponse(Call<YouTubeExtractionResult> call,
                                       Response<YouTubeExtractionResult> response) {
                    final Uri myUri = response.body().getSd360VideoUri();
                    emVideoView.setVideoURI(myUri);
                    emVideoView.setOnPreparedListener(first);
                    emVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            emVideoView.setVideoURI(myUri);
                            emVideoView.setOnPreparedListener(second);
                        }
                    });
                }

                @Override
                public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
                    t.printStackTrace();
                    //Alert your user!
                    Toast.makeText(EXOMediaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void playMP4EMVideo() {
        emVideoView.setVideoURI(Uri.parse(myVideo.getList_video()[0].getLink()));
        emVideoView.setOnPreparedListener(first);
        emVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                emVideoView.setVideoURI(Uri.parse(myVideo.getList_video()[0].getLink()));
                emVideoView.setOnPreparedListener(second);
            }
        });
    }

    private void setUpTabTrip() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPageAdapter(this, this, myVideo.getPost_id(), this);
        viewPager.setAdapter(viewPageAdapter);
        Log.i("checkPushComment",
            "checkPushComment: " + getIntent().getIntExtra("checkPushComment", 0));
        if (getIntent().getIntExtra("checkPushComment", 0) > 0) {
            viewPager.setCurrentItem(1, true);
        } else {
            viewPager.setCurrentItem(0, true);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llHeight);
                    linearLayout.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ScreenSize.RELATE_HEIGHT + tabsStrip.getHeight()));
                    Log.i("page", "page 1");
                    scrollView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llHeight);
                    linearLayout.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ScreenSize.COMMENT_HEIGHT));
                    scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    Log.i("page", "page 2");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(1);
        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    private void setUpTextViewInfo() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(myVideo.getTitle());
        tvNumLike = (TextView) findViewById(R.id.tvNumberLike);
        if (myVideo.getNum_like().equals("0")) {
            Random random = new Random();
            int a = 0;
            if (!myVideo.getNum_view().equals("0")) {
                a = random.nextInt(Integer.parseInt(myVideo.getNum_view()));
            }
            tvNumLike.setText("" + a);
        } else {
            tvNumLike.setText(myVideo.getNum_like());
        }
        TextView tvNumSeen = (TextView) findViewById(R.id.tvNumberSeen);
        tvNumSeen.setText(myVideo.getNum_view());
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottomBar);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                infoHeight = linearLayout.getHeight();
            }
        });
    }

    private void setUpImageViewControl() {
        ImageView ivBack = (ImageView) findViewById(R.id.ivLeft);
        ImageView ivShare = (ImageView) findViewById(R.id.ivShare);
        ivFollow = (ImageView) findViewById(R.id.ivFollow);
        ivFollow.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPush > 0) {
                    Intent intent = new Intent(EXOMediaActivity.this, SplashActivity.class);
                    startActivity(intent);
                }
                EXOMediaActivity.this.finish();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkshare) {
                    hideShare();
                } else {
                    showShare();
                }
            }
        });
        ivLike = (ImageView) findViewById(R.id.ivLike);
        setUpLike();
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ivLike.getTag() == R.drawable.like2) {
                    ivLike.setImageResource(R.drawable.like1);
                    ivLike.setTag(R.drawable.like1);
                    Gson gson = new Gson();
                    DatabaseAdapter db = new DatabaseAdapter(getBaseContext());
                    db.open();
                    db.insertRow(new String[]{myVideo.getPost_id(), gson.toJson(myVideo)});
                    db.close();
                    tvNumLike.setText("" + (Integer.parseInt(tvNumLike.getText().toString()) + 1));
                } else {
                    ivLike.setImageResource(R.drawable.like2);
                    ivLike.setTag(R.drawable.like2);
                    DatabaseAdapter db = new DatabaseAdapter(getBaseContext());
                    db.open();
                    db.deleteRow(myVideo.getPost_id());
                    db.close();
                    tvNumLike.setText("" + (Integer.parseInt(tvNumLike.getText().toString()) - 1));
                }
                postLike(myVideo.getPost_id());
            }
        });
        ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFollow.setVisibility(View.GONE);
            }
        });
    }

    private void setUpLike() {
        DatabaseAdapter db = new DatabaseAdapter(getBaseContext());
        db.open();
        if (db.getAllRows().getCount() > 0) {
            if (db.checkVideoInDB(myVideo.getPost_id())) {
                ivLike.setImageResource(R.drawable.like1);
                ivLike.setTag(R.drawable.like1);
            } else {
                ivLike.setImageResource(R.drawable.like2);
                ivLike.setTag(R.drawable.like2);
            }
        } else {
            ivLike.setImageResource(R.drawable.like2);
            ivLike.setTag(R.drawable.like2);
        }
        db.close();
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener =
        new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onAdStarted() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) {
                playYoutubeStream();
            }

            @Override
            public void onLoaded(String arg0) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onVideoStarted() {
//            twvLiveLink.setVisibility(View.GONE);
                MyRequest.countView(myVideo.getPost_id());
            }
        };

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            if (mYouTubePlayerView.getVisibility() == View.VISIBLE) {
                youTubePlayer.setFullscreen(false);
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                emVideoView.setLayoutParams(
                    new RelativeLayout.LayoutParams(ScreenSize.WIDTH, ScreenSize.WIDTH * 9 / 16));
                if (!emVideoView.isPlaying()) {
                    imageView.setVisibility(View.VISIBLE);
                }
                isFullscreen = false;
            }
        } else {
            if (Llayout_customshare.getVisibility() == View.VISIBLE) {
                hideShare();
            } else {
                if (checkPush > 0) {
                    Intent intent = new Intent(EXOMediaActivity.this, SplashActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }
    }

    @Override
    public void showVideo(final MyVideo myVideo) {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        this.myVideo = myVideo;
        mYouTubePlayerView.setVisibility(View.GONE);
        emVideoView.setVisibility(View.VISIBLE);
        playEMVideo();
        emVideoView.showDefaultControls();
        imageView.setVisibility(View.GONE);
        twvLiveLink.setVisibility(View.GONE);
        setUpTextViewInfo();
        setUpLike();
    }

    @Override
    public void setHeightView(int height) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llHeight);
        if ((height + tabsStrip.getHeight()) > (ScreenSize.RELATE_HEIGHT + tabsStrip.getHeight())) {
            Log.i("set", "has set height");
            ScreenSize.COMMENT_HEIGHT = height + tabsStrip.getHeight();
            if (viewPager.getCurrentItem() == 0) {
                linearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenSize.RELATE_HEIGHT + tabsStrip.getHeight()));
            } else {
                linearLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        height + tabsStrip.getHeight()));
            }
        } else {
            ScreenSize.COMMENT_HEIGHT = ScreenSize.RELATE_HEIGHT + tabsStrip.getHeight();
            linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.RELATE_HEIGHT + tabsStrip.getHeight()));
        }
    }

    @Override
    public void doLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    @Override
    public void pauseYoutube() {
        int y = infoHeight + tabsStrip.getHeight();
        scrollView.smoothScrollTo(0, y);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        profileTracker.startTracking();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (emVideoView.isPlaying()) {
            emVideoView.pause();
        }
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }

    private void setUpShare() {
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
        Llayout_customshare
            .setLayoutParams(new RelativeLayout.LayoutParams(ScreenSize.WIDTH, ScreenSize.HEIGHT));
        Llayout_customshare.setVisibility(View.GONE);
        Rlayout_fb = (RelativeLayout) findViewById(R.id.Rlayout_fb);
        Rlayout_tw = (RelativeLayout)
            findViewById(R.id.Rlayout_tw);
        Rlayout_gg = (RelativeLayout) findViewById(R.id.Rlayout_gg);
        Rlayout_mail = (RelativeLayout) findViewById(R.id.Rlayout_mail);
        Rlayout_sms = (RelativeLayout) findViewById(R.id.Rlayout_sms);
        Rlayout_web = (RelativeLayout) findViewById(R.id.Rlayout_web);
        Rlayout_link = (RelativeLayout) findViewById(R.id.Rlayout_link);
        Rlayout_delete = (RelativeLayout) findViewById(R.id.Rlayout_delete);
        Rlayout_khac = (RelativeLayout) findViewById(R.id.Rlayout_khac);
        //share click
        Rlayout_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("link: ", "linkshare: " + myVideo.getLink());
                Share.shareFb(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
            }
        });
        Rlayout_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareTw(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
            }
        });
        Rlayout_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareGg(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
            }
        });
        Rlayout_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareMail(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
            }
        });
        Rlayout_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Share.shareSMS(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
            }
        });
        Rlayout_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Share.shareKhac(EXOMediaActivity.this, myVideo.getLink(), myVideo.getTitle());
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
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public void showShare() {
        checkshare = true;
        Animation animShareShow = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slide_in_bottom);
        Llayout_customshare.setVisibility(View.VISIBLE);
        Llayout_customshare.startAnimation(animShareShow);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void setTouch(MotionEvent event) {
        final int DISTANCE = 5;
        float startY = 0;
        float dist;
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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

    public void postLike(final String postID) {
        final FacebookUser facebookUser = FacebookUser.getInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put(NewAPI.PARAM_APP_ID, Config.APP_ID);
        params.put("post_id", postID);
        params.put("ads_id", "");
        params.put("screen_size", ScreenSize.WIDTH + "x" + ScreenSize.HEIGHT);
        params.put("device_name", Build.DEVICE);
        params.put("manufacturer", android.os.Build.MANUFACTURER);
        params.put("device_model", Build.MODEL);
        params.put("os", "1");
        params.put("os_ver", "" + Build.VERSION.SDK_INT);
        AndroidNetworking.post("http://content.amobi.vn/api/apiall/like/")
            .addHeaders("Cookie", "user_token=" + facebookUser.getAuth_token())
            .addQueryParameter(params)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean b) {
        youTubePlayer = player;
        youTubePlayer.setOnFullscreenListener(onFullscreenListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
    }

    private YouTubePlayer.OnFullscreenListener onFullscreenListener =
        new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullscreen = b;
            }
        };

    private void setUpYoutube() {
        if (myVideo != null) {
            mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
            mYouTubePlayerView.initialize(Config.YOUTUBE_KEY, this);
//            mYouTubePlayerView.setOnTouchListener(onTouchListener);
        }
    }
//    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if(isLive){
//                twvLiveLink.setVisibility(View.VISIBLE);
//                twvLiveLink.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideAndShow();
//                    }
//                });
//            }
//
//            return false;
//        }
//    };
//    private void hideAndShow() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    twvLiveLink.setVisibility(View.GONE);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
