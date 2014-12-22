package com.example.chat;

import android.os.Binder;

/**
 * Created by User on 25.11.2014.
 */
public class MyBinding extends Binder {
    MyService myService =new MyService();
    MainActivity mActivity;

    public MyService getMyService() {
        return myService;
    }

    public void setMyService(MyService myService) {
        this.myService = myService;
    }

    public MyBinding(MyService myService) {

        this.myService = myService;
    }

    public void setmActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }
}
