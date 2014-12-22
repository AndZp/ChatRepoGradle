package com.example.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONObject;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString(
                    "com.parse.Data"));

            String name = json.getString("name");
            Toast.makeText(context, action + " " + channel + "  " + name,
                    Toast.LENGTH_LONG).show();

            Intent i = new Intent(context, Login.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);


            Notification n = new NotificationCompat.Builder(context)
                    .setContentTitle("Новое сообщение")
                    .setContentText(name).
                            setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.ic_launcher).build();
            n.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);


        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }


    }


}
