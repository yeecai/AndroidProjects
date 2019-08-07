package com.example.notifcations2;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private static final String KEY_TEXT_REPLY ="" ;
    private int notificationId = 1;
    private NotificationManager notificationManager;
    private Bitmap bitmapChihuahuaLookMe;
    private Object emailObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }

    public void sendNotificationBasic(View view) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
      //  Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //The difference
        NotificationChannel a;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            a = new NotificationChannel("10086", "Notifaction with channelId", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(a);
            mBuilder.setChannelId("10086");
        }
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Let' go")
                .setContentText("Click here")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    /**
     * @param view TODO
     */

   /* public void sendNotificationMessageReply(View view) {

        String replyLabel = getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        PendingIntent.getBroadcast(getApplicationContext(),
                conversation.getConversationId(),
                getMessageReplyIntent(conversation.getConversationId()),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply_black_24dp,
                        getString(R.string.reply_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();
        //The difference
        NotificationChannel a;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            a = new NotificationChannel("10086","Notifaction with channelId", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(a);
            mBuilder.setChannelId("10086");
        }
        mBuilder.setSmallIcon(R.drawable.ic_favorite_border_black_24dp)
                .setContentTitle("Let' go")
                .setContentText("Click here")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAction(action);

        notificationManager.notify(notificationId, mBuilder.build());

    }*/

    public void sendNotificationExpandable(View view) {

        bitmapChihuahuaLookMe = BitmapFactory.decodeResource(getResources(),R.drawable.chihuahua_lookme);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //The difference
        NotificationChannel a;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            a = new NotificationChannel("10086","Notifaction with channelId", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(a);
            mBuilder.setChannelId("10086");
        }
        mBuilder.setSmallIcon(R.drawable.ic_favorite_border_black_24dp)
                .setContentTitle("Look at me")
                .setContentText("Am I adorable?")
                .setLargeIcon(bitmapChihuahuaLookMe)
               /* .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapChihuahuaLookMe)
                        .bigLargeIcon(null))*/
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND);

        notificationManager.notify(notificationId, mBuilder.build());

    }
}