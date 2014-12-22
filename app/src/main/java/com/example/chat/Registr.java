package com.example.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


public class Registr extends Activity {
    EditText etLogin;
    EditText etPass;
    EditText etPassReap;
    EditText etMail;
    Button btnRegistr;

    private String login;
    private String password;
    private String email;
    private Location locationUser;
    private int colorText;
    private int avatarka;
    private boolean gender;
    private boolean online;
    private ImageView imageView;
    private Bitmap imageBitmap;
    static final int GALLERY_REQUEST = 1;
    public void registration(View view) {
        if (etLogin.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Введите логин", Toast.LENGTH_SHORT).show();
            return;
        }
        login = etLogin.getText().toString().trim();
        if (etPass.getText().toString().trim().equals(etPassReap.getText().toString().trim())) {
            password = etPass.getText().toString().trim();
        } else {
            Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }
        email = etMail.getText().toString().toLowerCase().trim();


        final ParseUser user = new ParseUser();
        user.setUsername(login);
        Singleton.getInstance().getUser().setUsername(login);
        user.setPassword(password);

        user.setEmail(email);
        Singleton.getInstance().getUser().setEmail(email);
        user.put("gender", gender);

        try {
            user.signUp();
        } catch (Exception e) {
            System.out.println("registr test foto"+e);;
        }


        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    System.out.println(e);
                } else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] data = stream.toByteArray();
                    ParseFile imgFile = new ParseFile(login+".png", data);
                    imgFile.saveInBackground();

                    user.put("image", imgFile);
                    user.saveInBackground();




                    Singleton.getInstance().getUser().setObjectId(user.getObjectId());
                    Singleton.getInstance().setUser(user);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("login", login);
                    startActivity(intent);
                }
            }
        });


    }

    public void camera ( View view) {Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 0);
        }

    }

    public void openGalery (View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

        }

        if (requestCode==1 && resultCode == RESULT_OK) {
            imageBitmap = null;

            switch(requestCode) {
                case GALLERY_REQUEST:
                    if(resultCode == RESULT_OK){
                        Uri selectedImage = data.getData();
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                       imageView.setImageBitmap(imageBitmap);
                    }
            }
        }




        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);
        etLogin = (EditText) findViewById(R.id.etRegLog);
        etPass = (EditText) findViewById(R.id.etRegPass);
        etPassReap = (EditText) findViewById(R.id.etRegPassReP);
        etMail = (EditText) findViewById(R.id.etRegMail);
        Spinner spGenfer = (Spinner) findViewById(R.id.spinerGender);
        imageView = (ImageView) findViewById(R.id.ivRegAva);

        final ArrayAdapter<String> genderArr = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, new String[]{"Муж", "Жен"});

        spGenfer.setAdapter(genderArr);
        spGenfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) gender = true;
                if (i==1) gender=false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registr, menu);
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
