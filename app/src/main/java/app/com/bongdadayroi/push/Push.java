package app.com.bongdadayroi.push;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.annotations.SerializedName;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.io.IOException;

import app.com.bongdadayroi.activities.MainActivity;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.myapp.Config;

/**
 * Created by tuan on 31/12/2015.
 */
public class Push {

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("title")
    private String title;

    @SerializedName("des")
    private String des;

    @SerializedName("data")
    private MyVideo data;

    @SerializedName("option")
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public MyVideo getData() {
        return data;
    }

    public void setData(MyVideo data) {
        this.data = data;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static void registerPush (final Context context){
        MainActivity mainActivity = new MainActivity();
        MyHandler.mainActivity = mainActivity;
        NotificationsManager.handleNotifications(context, Config.SENDER_ID, MyHandler.class);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        final NotificationHub hub = new NotificationHub(Config.HUB_NAME, Config.HUB_LISTEN_CONNECTION_STRING, context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String regid = null;
                try {
                    FacebookUser facebookUser = FacebookUser.getInstance();
                    regid = GoogleCloudMessaging.getInstance(context).register(Config.SENDER_ID);
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
}
