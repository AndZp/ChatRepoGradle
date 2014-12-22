package com.example.chat;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by User on 25.11.2014.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "gdsB2DthpO1qfpXFjZ0lEMdjba27Qp2xR5vqIsbd",
                "kyx2Y3RP1yaQgU3FidBVYilUlX4f1SRR3Aa9w7tQ");

    }
}
