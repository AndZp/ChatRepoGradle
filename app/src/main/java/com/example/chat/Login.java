package com.example.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.*;

public class Login extends Activity {

    EditText etLog;
    EditText etPass;
    private String login;
    private String password;
    protected SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    public void regNew(View view) {
        Intent intent = new Intent(getApplicationContext(), Registr.class);
        startActivity(intent);
    }

    public void startChat(View view) {
        login = etLog.getText().toString().trim();
        password = etPass.getText().toString().trim();


        ParseUser.logInInBackground(login, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if ( parseUser!=null) {
                    Singleton.getInstance().setUser(parseUser);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("login", login);
                    startActivity(intent);
                    ParsePush.subscribeInBackground("chatLogin" + login, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null)
                                System.out.println("subScr error "+e.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Неверно введён логин/пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLog = (EditText) findViewById(R.id.etLogin);
        etPass = (EditText) findViewById(R.id.etPassword);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Singleton.getInstance().setUser(currentUser);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            mPreferences = getSharedPreferences("configur", MODE_PRIVATE);
            String email = mPreferences.getString("Email", "");
            String name = mPreferences.getString("Name", "");
            String password = mPreferences.getString("Password", "");
            if (!"".equals(email)&&!"".equals(password)){
                ParseUser.logInInBackground(name, password,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // Login successful
                                    Singleton.getInstance().setUser(
                                            user);
                                    Intent intent = new Intent(
                                            Login.this,
                                            MainActivity.class);
                                    Login.this.startActivity(intent);
                                }
                                else {
                                    //TODO: Login failed
                                }
                            }
                        });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
