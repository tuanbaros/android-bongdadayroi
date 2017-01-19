package app.com.bongdadayroi.networks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import app.com.bongdadayroi.activities.MainActivity;
import app.com.bongdadayroi.interfaces.GetInfoAccount;
import app.com.bongdadayroi.interfaces.SplashError;
import app.com.bongdadayroi.models.Category;
import app.com.bongdadayroi.models.Data;
import app.com.bongdadayroi.models.FacebookUser;
import app.com.bongdadayroi.models.MyAccount;
import app.com.bongdadayroi.models.MyData;
import app.com.bongdadayroi.models.MyVideo;
import app.com.bongdadayroi.models.Test;
import app.com.bongdadayroi.myapp.Config;
import app.com.bongdadayroi.myapp.ScreenSize;
import app.com.bongdadayroi.push.Push;
import app.com.bongdadayroi.utils.API;
import app.com.bongdadayroi.utils.NewAPI;

public class MyRequest {

    public static void requestHomeAPI(final AppCompatActivity context, final SplashError splashError){

        AndroidNetworking.get(API.HOME_URL)
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("response", "response: " + response.toString());

                        Gson gson = new Gson();

                        MyData myData = MyData.getInstance();

                        Data data = gson.fromJson(response.toString(), Data.class);

                        ArrayList<MyVideo> arrayList = new ArrayList<>();

                        Collections.addAll(arrayList, data.getData());

                        myData.setArrayHome(arrayList);

                        requestNewAPI(context, splashError);
                    }

                    @Override
                    public void onError(ANError ANError) {
                        splashError.showButtonTryAgain();
                    }
                });

    }

    public static void requestNewAPI(final AppCompatActivity context, final SplashError splashError){

        AndroidNetworking.get(API.NEW_URL)
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

                        myData.setArrayNew(arrayList);

                        requestMostAPI(context, splashError);
                    }

                    @Override
                    public void onError(ANError ANError) {
                        splashError.showButtonTryAgain();
                    }
                });

    }

    public static void requestMostAPI(final AppCompatActivity context, final SplashError splashError){

        AndroidNetworking.get(API.MOST_URL)
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

                        myData.setArrayMost(arrayList);

                        sendSimpleRequest(context, splashError);
                    }

                    @Override
                    public void onError(ANError ANError) {
                        splashError.showButtonTryAgain();
                    }
                });
    }

    public static synchronized void sendSimpleRequest(final AppCompatActivity context, final SplashError splashError){

        AndroidNetworking.get(API.LIST_CATEGORY_URL)
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();

                        Category category = Category.getInstance();

                        Test test = gson.fromJson(response.toString(), Test.class);

                        category.setData(test.getData());
//                        Toast.makeText(context, "" + category.getData().length, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        context.finish();
                    }

                    @Override
                    public void onError(ANError ANError) {
                        splashError.showButtonTryAgain();
                    }
                });
    }

    public static void requestGetToken(final Context context, final String fa_access_token, final GetInfoAccount getInfoAccount){

        AndroidNetworking.get(API.GET_TOKEN_URL + fa_access_token)
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();

                        MyAccount myData = gson.fromJson(response.toString(), MyAccount.class);

                        FacebookUser facebookUser = FacebookUser.getInstance();
                        if(myData.getSuccess().equals("false")){
                            LoginManager.getInstance().logOut();
                            facebookUser.reset();
                        }else{
                            facebookUser.setAuth_token(myData.getToken());

                            facebookUser.setUser_id(myData.getUser_id());

                            Push.registerPush(context);

                            if(getInfoAccount!=null){
                                getInfoAccount.showInfoUser();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError ANError) {
                        LoginManager.getInstance().logOut();
                        FacebookUser facebookUser = FacebookUser.getInstance();
                        facebookUser.reset();
                    }
                });

    }

    public static void countView(String postID) {
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


        AndroidNetworking.get("http://content.amobi.vn/api/apiall/count-view/")
                .addQueryParameter(params)
                .setPriority(Priority.MEDIUM)
                .doNotCacheResponse()
                .build();
    }


    public static Bitmap getBitmapFromURL(String imageUrl){
        final Bitmap[] bitmap = {null};
        AndroidNetworking.get(imageUrl)
                .setTag("imageRequestTag")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(100)
                .setBitmapMaxWidth(100)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap bmp) {
                        bitmap[0] = bmp;
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
        return bitmap[0];
    }

}
