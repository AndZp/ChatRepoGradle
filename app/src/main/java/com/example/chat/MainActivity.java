package com.example.chat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Vector;


public class MainActivity extends Activity implements ServiceConnection, LocationListener {
    private ListView lvChat;
    private Button btnSend;
    private EditText etTo;
    private EditText etMass;
    private String login;
    private MyAdapter myAdapter;
    private MyService mService;
    private Vector<Message> messageVector;
    private Vector<User> userVector;
    private Spinner spinner;
    private String to;
    private int userToIndex = 0;
    private ParseUser user;
    private Vector<String> userLoginVector;
    private LocationManager locationManager;
    private Location location;
    Intent intentServ;


    @Override
    protected void onStop() {
        super.onStop();


    }

    public void push(String pushFrom, String pushTo) {
        JSONObject data;
        try {
            data = new JSONObject();
            data.put("action", "COM.EXAMPLE.NEWMESSPUSH");
            data.put("name", "pushFrom");
            ParsePush push = new ParsePush();
            push.setChannel("chatLogin" + pushTo);
            push.setData(data);
            push.sendInBackground();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        user.put("online", false);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Offline: " + e);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvChat = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter(getApplicationContext());
        lvChat.setAdapter(myAdapter);

        Intent intent = getIntent();
        String to = intent.getStringExtra("name");
        if (to != null) {
            int t = Singleton.getInstance().getUserIndex(to);
            if (t != -1) {
                userToIndex = t;
            }
        }
        login = Singleton.getInstance().getUser().getUsername();


        Intent intentServ = new Intent(this, MyService.class);
        startService(intentServ);
        bindService(intentServ, this, BIND_AUTO_CREATE);
        dowloadUsers();

        user = ParseUser.getCurrentUser();
        user.put("online", true);
        user.saveInBackground();
        refreshUser();
        setLocation();
    }

    private void setLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Toast.makeText(getApplicationContext(), ("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude()), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "No location", Toast.LENGTH_SHORT).show();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, (android.location.LocationListener) this);

        if (location != null) {
            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            Singleton.getInstance().getUser().put("location", point);
            Singleton.getInstance().getUser().saveInBackground();
            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();


        }
    }

    public void send(View v) {


        etMass = (EditText) findViewById(R.id.etMass);
        etMass.setHint("Введите сообщение");
        final String from = login;
        final String to = this.to;
        final String message = etMass.getText().toString().trim();
        if (to != "" && message != "") {
            ParseObject massage = new ParseObject("CHAT");
            massage.put("FROM", login);
            massage.put("TO", to);
            massage.put("MESSAGE", message);
            massage.put("READ", false);
            massage.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException arg0) {
                    try {
                        // TODO Auto-generated method stub
                        if (arg0 == null) {
                            Singleton.getInstance().getMessageVector().add(new Message(login, to, message, true, new Date()));

                            System.out.println("Everything is OK");
                            push(Singleton.getInstance().getUser().getUsername().toString(), to);
                            lvChat.invalidateViews();
                            /// send Push
                        } else {
                            System.out.println(arg0);
                        }
                    } catch (Exception e) {
                        System.out.println("говновылет" + e);
                    }
                }
            });
            etMass.setText(" ");
        } else {
            Toast.makeText(getApplicationContext(), "Введите получятеля и логин", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), Setting.class));

            return true;
        }

        if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), Contactlist.class));

            return true;
        }

        if (id == R.id.clean) {
            Singleton.getInstance().getMessageVector().clear();
            lvChat.invalidateViews();

            return true;
        }

        if (id == R.id.findMap) {
            Intent intent = new Intent(getApplicationContext(), Mapa.class);
            intent.putExtra("latitude", Singleton.getInstance().getUser().getParseGeoPoint("location").getLatitude());
            intent.putExtra("longitude", Singleton.getInstance().getUser().getParseGeoPoint("location").getLongitude());
            startActivity(intent);

            return true;
        }
        if (id == R.id.logOut) {
            ParseUser.logOut();
            Singleton.getInstance().logOut();
            this.getSharedPreferences("configur", MODE_PRIVATE).edit().clear().commit();
            ParsePush.unsubscribeInBackground("chatLogin" + login);

            startActivity(new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MyBinding binder = (MyBinding) iBinder;
        mService = binder.getMyService();
        mService.setmActivity(this);


    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }


    public void refresh() {
        dowloadUsers();
        lvChat.invalidateViews();
    }

    public void dowloadUsers() {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("objectId", Singleton.getInstance().getUser().getObjectId());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    if (Singleton.getInstance().getUsersVector().size() == 0) {
                        for (ParseUser parseUser : parseUsers) {
                            Singleton.getInstance().getUsersVector().add(parseUser);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    refreshUser();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }


                            }
                        });
                    }
                    if (Singleton.getInstance().getUsersVector().size() != parseUsers.size()) {
                        Singleton.getInstance().getUsersVector().clear();

                        for (ParseUser parseUser : parseUsers) {


                            Singleton.getInstance().getUsersVector().add(parseUser);


                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshUser();
                            }
                        });
                    } else {
                        boolean flag = false;
                        for (int i = 0; i < Singleton.getInstance().getUsersVector().size(); i++) {
                            if (Singleton.getInstance().getUsersVector().get(i).getBoolean("online") != parseUsers.get(i).getBoolean("online")) {
                                flag = true;
                                boolean tmpOnline = !Singleton.getInstance().getUsersVector().get(i).getBoolean("online");
                                Singleton.getInstance().getUsersVector().get(i).put("online", tmpOnline);

                            }
                        }
                        if (flag == true) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshUser();
                                    System.out.println("Rabotay SSUKA");
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void refreshUser() {

        spinner = (Spinner) findViewById(R.id.spinner);
        userLoginVector = new Vector<String>();
        for (int i = 0; i < Singleton.getInstance().getUsersVector().size(); i++) {

            if (Singleton.getInstance().getUsersVector().get(i).getBoolean("online") == false) {
                userLoginVector.add(Singleton.getInstance().getUsersVector().get(i).getUsername());
            } else {
                userLoginVector.add(Singleton.getInstance().getUsersVector().get(i).getUsername() + " online");
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, userLoginVector);
        spinner.setAdapter(adapter);
        spinner.setSelection(userToIndex);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                to = Singleton.getInstance().getUsersVector().get(i).getUsername().toString();
                userToIndex = i;

                return;
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                to = Singleton.getInstance().getUsersVector().get(0).getUsername();
                userToIndex = 0;
            }

        });


    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        final ParseGeoPoint parseLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        Singleton.getInstance().getUser().put("location", parseLocation);
        Singleton.getInstance().getUser().saveInBackground();

        Toast.makeText(getApplicationContext(), "Location is change", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void sound() {
        Vibrator v = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.drawable.alertnote);
        mediaPlayer.start();
        v.vibrate(500);

    }
}

