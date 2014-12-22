package com.example.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * Created by User on 25.11.2014.
 */
public class MyAdapter extends android.widget.BaseAdapter {

    private Context mContext;


    RelativeLayout rl;

    public MyAdapter(Context mContext) {
        this.mContext = mContext;


    }

    @Override
    public int getCount() {
        return Singleton.getInstance().getMessageVector().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        rl = null;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.chatmess, viewGroup, false);


        TextView tvMass = (TextView) rl.findViewById(R.id.tvMass);
        tvMass.setText(Singleton.getInstance().getMessageVector().get(i).getFrom() + ": " + Singleton.getInstance().getMessageVector().get(i).getMessage());


        TextView tvFrom = (TextView) rl.findViewById(R.id.tvFrom);
        tvFrom.setText("TO: " + Singleton.getInstance().getMessageVector().get(i).getTo());


        TextView tvTime = (TextView) rl.findViewById(R.id.tvTime);
        tvTime.setText(Singleton.getInstance().getMessageVector().get(i).getDate().toString().substring(11, 16));

        final ImageView ivAva = (ImageView) rl.findViewById(R.id.ivAvaChat);


        if (Singleton.getInstance().getMessageVector().get(i).getFrom().equals(Singleton.getInstance().getUser().getUsername())) {

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

            return rl;
        }
        for (int x = 0; x < Singleton.getInstance().getUsersVector().size(); x++) {
            if (Singleton.getInstance().getUsersVector().get(x).getUsername().equals(Singleton.getInstance().getMessageVector().get(i).getFrom())) {

                if (Singleton.getInstance().getUsersVector().get(x).get("image") != null) {
                    ParseFile imageFile = (ParseFile) Singleton.getInstance().getUsersVector().get(x).get("image");
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
                                System.out.println("parseFOTO EX" + e );
                            }
                        }
                    });

                } else {
                    ivAva.setImageResource(R.drawable.ic_launcher);
                }
            }


        }
        return rl;
    }
}



