package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * Created by User on 16.12.2014.
 */
public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private Button btnChat;
    private Button btnMap;
    private Button btnMail;



    RelativeLayout rl;

    public ContactAdapter(Context mContext) {

        this.mContext = mContext;


    }

    @Override
    public int getCount() {
        return Singleton.getInstance().getUsersVector().size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        rl = null;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.contactitem, viewGroup, false);

        TextView tvUserName = (TextView) rl.findViewById(R.id.tvContUserName);
        tvUserName.setText(Singleton.getInstance().getUsersVector().get(i).getUsername());

        TextView tvPol = (TextView) rl.findViewById(R.id.tvContPol);
        if (Singleton.getInstance().getUsersVector().get(i).getBoolean("gender")==true) {
            tvPol.setText("Мужык");
            tvPol.setTextColor(Color.BLUE);
          }
        else {
            tvPol.setText("Не мужык");
            tvPol.setTextColor(Color.RED);
        }

        ImageView statusOnline = (ImageView)rl.findViewById(R.id.ivConOnline);
        if (Singleton.getInstance().getUsersVector().get(i).getBoolean("online")==true) {
           statusOnline.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.presence_online));
        }
        else {
            statusOnline.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.presence_busy));
        }

        final ImageView ivAva = (ImageView)rl.findViewById(R.id.ivContAva);
        if (Singleton.getInstance().getUsersVector().get(i).get("image") != null) {
            ParseFile imageFile = (ParseFile) Singleton.getInstance().getUsersVector().get(i).get("image");
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

        btnChat = (Button) rl.findViewById(R.id.btnContChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", Singleton.getInstance().getUsersVector().get(i).getUsername());
                mContext.startActivity(intent);
            }
        });

        btnMap = (Button) rl.findViewById(R.id.btnContMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Mapa.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", Singleton.getInstance().getUsersVector().get(i).getUsername());
                intent.putExtra("latitude", Singleton.getInstance().getUsersVector().get(i).getParseGeoPoint("location").getLatitude());
                intent.putExtra("longitude", Singleton.getInstance().getUsersVector().get(i).getParseGeoPoint("location").getLongitude());
                mContext.startActivity(intent);
            }
        });

        btnMail =(Button) rl.findViewById(R.id.btnContEmail);
        btnMail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{Singleton.getInstance().getUsersVector().get(i).getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "From Chat");
                intent.putExtra(Intent.EXTRA_TEXT   , "......");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    mContext.startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rl;
    }
}
