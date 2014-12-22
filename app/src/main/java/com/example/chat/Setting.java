package com.example.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.awt.font.TextAttribute;

/**
 * Created by User on 16.12.2014.
 */
public class Setting extends Activity {
    TextView tvLoginName;
    ImageView ivAva;
    CheckBox cbSound;
    CheckBox cbVibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        tvLoginName = (TextView) findViewById(R.id.tvUserName);
        ivAva = (ImageView) findViewById(R.id.ivSettAva);
        cbSound = (CheckBox) findViewById(R.id.checkBoxSound);
        cbVibro = (CheckBox) findViewById(R.id.checkBoxVibrate);

        tvLoginName.setText(Singleton.getInstance().getUser().getUsername());
        if (Singleton.getInstance().getUser().get("image") != null) {
            ParseFile imageFile = (ParseFile) Singleton.getInstance().getUser().get("image");

            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory
                                .decodeByteArray(
                                        bytes, 0,
                                        bytes.length);
                        // Set the Bitmap into the
                        // ImageView
                        ivAva.setImageBitmap(bmp);
                    } else {
                        // something went wrong
                    }
                }
            });

        } else {
            ivAva.setImageResource(R.drawable.ic_launcher);
        }


    }
}



