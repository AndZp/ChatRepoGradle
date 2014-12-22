package com.example.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.Vector;


public class Mapa extends Activity {

    private MapView mapView;
    private GeoPoint geoPoint;
    MyItemizedOverlay myItemizedOverlay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setZoom(16);

        Intent intent = getIntent();
        geoPoint = new GeoPoint(intent.getDoubleExtra("latitude", 51), intent.getDoubleExtra("longitude", 51));


        //geoPoint = new GeoPoint(Singleton.getInstance().getUser().getParseGeoPoint("location").getLatitude(),
        //Singleton.getInstance().getUser().getParseGeoPoint("location").getLongitude());
        mapView.getController().setCenter(geoPoint);

        Vector<OverlayItem> overlayItemVector = new Vector<OverlayItem>();



        for (int i =0; i<Singleton.getInstance().getUsersVector().size(); i++) {

            try {

                GeoPoint tmpGeoPoint = new GeoPoint(Singleton.getInstance().getUsersVector().get(i).getParseGeoPoint("location").getLatitude(),
                        Singleton.getInstance().getUsersVector().get(i).getParseGeoPoint("location").getLongitude());

                OverlayItem overlayItem1 = new OverlayItem(
                        Singleton.getInstance().getUsersVector().get(i).getUsername(),
                        Singleton.getInstance().getUsersVector().get(i).getEmail().toString(),
                        tmpGeoPoint);
                overlayItemVector.add(overlayItem1);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            myItemizedOverlay = new MyItemizedOverlay(this, overlayItemVector);
            mapView.getOverlays().add(myItemizedOverlay);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
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