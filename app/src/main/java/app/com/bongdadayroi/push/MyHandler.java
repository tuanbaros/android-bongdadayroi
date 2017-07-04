package app.com.bongdadayroi.push;
/**
 * Created by hnc on 10/27/2015.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

import app.com.bongdadayroi.R;
import app.com.bongdadayroi.activities.MainActivity;

public class MyHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    Context ctx;
    static public MainActivity mainActivity;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");
        Log.i("des", "nhMessage: " + nhMessage);
        Gson gson = new Gson();
        Push push = gson.fromJson(nhMessage, Push.class);
        sendNotification(push);
    }

    private void sendNotification(Push msg) {
        mNotificationManager = (NotificationManager)
            ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(ctx, PushActivity.class);
        resultIntent.putExtra("post_id", msg.getPost_id());
        if (msg.getOption().equals("comment")) {
            resultIntent.putExtra("checkPushComment", 1);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(PushActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
        NotificationCompat.Builder mBuilder;
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(
            "tuannt", Context.MODE_PRIVATE);
        if (msg.getOption().equals("comment")) {
            boolean check = sharedPreferences
                .getBoolean(ctx.getResources().getStringArray(R.array.setting)[1], true);
            if (check) {
                mBuilder = new NotificationCompat.Builder(ctx)
                    .setSmallIcon(getNotificationIcon())
                    .setContentTitle(msg.getTitle())
                    .setContentText(msg.getDes());
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        } else {
            boolean check = sharedPreferences
                .getBoolean(ctx.getResources().getStringArray(R.array.setting)[0], true);
            if (check) {
                mBuilder = new NotificationCompat.Builder(ctx)
                    .setSmallIcon(getNotificationIcon())
                    .setContentTitle(msg.getTitle())
                    .setContentText(msg.getDes());
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon =
            (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}
