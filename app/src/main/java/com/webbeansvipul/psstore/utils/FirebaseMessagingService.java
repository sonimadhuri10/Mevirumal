package com.webbeansvipul.psstore.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.webbeansvipul.psstore.MainActivity;
import com.webbeansvipul.psstore.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Panacea-Soft on 8/11/16.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private String channelId = "PsStoreChannelId1";
    private String title,message,imageUrl="",url,nid;
    Bitmap image;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /* Important : Please don't change this "message" because if you change this, need to update at PHP.  */

        String msg = remoteMessage.getData().get("message");

        if (remoteMessage.getNotification().getImageUrl()!= null) {
            try
            {
                URL urls = new URL(String.valueOf(remoteMessage.getNotification().getImageUrl()));
                image = BitmapFactory.decodeStream(urls.openConnection().getInputStream());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


//        Utils.psLog("****** 2 >>> " + remoteMessage.getData().get("action2"));
        if(msg == null || msg.equals("")) {
            msg = remoteMessage.getNotification().getBody();
        }


        showNotification(msg);

    }


    private void showNotification(String message) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.NOTI_MSG, message);
        editor.putBoolean(Constants.NOTI_EXISTS_TO_SHOW, true);
        editor.apply();

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Constants.NOTI_MSG, message);
        String displayMessage;
        Utils.psLog("Message From Server : " + message);
        if (message == null || message.equals("")) {
            i.putExtra(Constants.NOTI_EXISTS_TO_SHOW, false);
            displayMessage = "Welcome from PSApp";
        } else {
            i.putExtra(Constants.NOTI_EXISTS_TO_SHOW, true);
            displayMessage = message; //"You've received new message.";

        }

        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.ZERO)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.notification__alert))
                .setContentText(displayMessage)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentIntent(pendingIntent);


        if (image!=null) {
            builder.setLargeIcon(image);
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null));
        }


            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            builder.setDefaults(defaults);
            // Set the content for Notification
            builder.setContentText(displayMessage);
            // Set autocancel
            builder.setAutoCancel(true);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = this.channelId;
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        getString(R.string.notification__alert),
                        NotificationManager.IMPORTANCE_DEFAULT);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
                builder.setChannelId(channelId);
            }

            if (manager != null) {
                manager.notify(0, builder.build());
            }
        }


    public Bitmap getBitmapfromUrl (String imageUrl){
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
