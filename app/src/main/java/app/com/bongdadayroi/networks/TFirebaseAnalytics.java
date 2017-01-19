package app.com.bongdadayroi.networks;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class TFirebaseAnalytics {
    public static void setAnalytic(Activity appCompatActivity){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(appCompatActivity);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //logEnvent
        Bundle bundle = new Bundle();
        bundle.putString(
                FirebaseAnalytics.Param.CONTENT_TYPE, "" + ""+appCompatActivity.getLocalClassName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void setAnalytic(Activity appCompatActivity, String title){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(appCompatActivity);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //logEnvent
        Bundle bundle = new Bundle();
        bundle.putString(
                FirebaseAnalytics.Param.CONTENT_TYPE, "" + ""+appCompatActivity.getLocalClassName() + "/" + title);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }
}
