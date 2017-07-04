package app.com.bongdadayroi.myapp;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;

import java.io.File;

/**
 * Created by tuan on 02/04/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.setApplicationId(Config.APP_ID);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }
}
