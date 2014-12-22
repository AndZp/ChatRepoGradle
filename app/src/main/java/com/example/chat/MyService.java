package com.example.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by User on 25.11.2014.
 */
public class MyService extends Service implements Runnable {
    private Vector<Message> messageVector;
    private int timeUpdate = 5000;


    public void setmActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }


    public void setMessageVector(Vector<Message> messageVector) {
        this.messageVector = messageVector;
    }

    private MainActivity mActivity;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void run() {
        while (true) {
            if (mActivity != null) {
                mActivity = getmActivity();

                try {
                    Thread.sleep(5000);

                } catch (Exception e) {
                    System.out.println("connect/online: " + e);
                }
                if (Singleton.getInstance().getUser() != null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("CHAT");
                    query.whereEqualTo("TO", Singleton.getInstance().getUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> scoreList, ParseException e) {
                            if (e == null) {

                                for (int i = 0; i < scoreList.size(); i++) {

                                    ParseObject parseObject = scoreList.get(i);
                                    if (parseObject.getBoolean("READ") == false) {
                                        Message message = new Message(
                                                parseObject.getString("FROM"),
                                                parseObject.getString("TO"),
                                                parseObject.getString("MESSAGE"),
                                                true,
                                                new Date()
                                        );
                                        Singleton.getInstance().getMessageVector().add(message);


                                        parseObject.put("READ", true);
                                        parseObject.saveInBackground();
                                        mActivity.sound();
                                    }

                                }

                                mActivity.refresh();

                            }


                        }

                    });
                }
            }


        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Thread myThread = new Thread(this);
        myThread.start();
        return new MyBinding(this);

    }


    public MainActivity getmActivity() {
        return mActivity;
    }

    public int getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(int timeUpdate) {
        this.timeUpdate = timeUpdate;
    }
}
