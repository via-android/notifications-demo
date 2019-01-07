package com.morepranit.notificationsdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "sports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
    }

    public void onSend(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Title");
        builder.setContentText("Content Text");
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        // builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.desc)));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.percent);

        builder.setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon(null).bigPicture(bitmap));
        builder.setLargeIcon(bitmap);

        builder.addAction(R.drawable.ic_stat_name, "Action Name", attachActionButtons());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(34, builder.build());
    }



    public void onSendProgress(View view) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Download");
        builder.setContentText("Downloading Content...");
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSound(null);
        final NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                builder.setProgress(100, 0, false);
                builder.setOngoing(true);
                managerCompat.notify(35, builder.build());

                for (int i = 0; i <= 100; i+=5) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    builder.setProgress(100, i, false);
                    managerCompat.notify(35, builder.build());
                }

                builder.setContentText("Download Finished");
                builder.setOngoing(false);
                builder.setProgress(0, 0, false);
                managerCompat.notify(35, builder.build());
            }
        }).start();

    }

    private PendingIntent attachActionButtons() {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.morepranit.notificationsdemo.NOTIFICATION");
        intent.putExtra("key", "value");

        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "sports", importance);
            channel.setDescription("sports channel");

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}

