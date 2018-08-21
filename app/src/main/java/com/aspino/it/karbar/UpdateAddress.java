package com.aspino.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class UpdateAddress extends AppCompatActivity {
    private String karbarCode;

    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private EditText NameAddres;
    private EditText AddAddres;
    public double lat;
    private double lon;
    public String backToActivity;
    private String AddressCode;
    private LinearLayout LinearBottomSaveAddress;
    private GoogleMap map;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Button btnSaveLocation =  findViewById(R.id.btnSaveLocation);
        NameAddres=findViewById(R.id.NameAddres);
        AddAddres=findViewById(R.id.AddAddres);
        LinearBottomSaveAddress= findViewById(R.id.LinearBottomSaveAddress);
        try {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();
        }
        catch (Exception e) {
            karbarCode = "";
        }
        try {
            AddressCode = getIntent().getStringExtra("AddressCode").toString();
        }
        catch (Exception e) {
            AddressCode = "";
        }
        dbh = new DatabaseHelper(getApplicationContext());
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }
        try {
            backToActivity = getIntent().getStringExtra("nameActivity").toString();
        }
        catch (Exception e) {
            backToActivity = "";
        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }



        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String CodeState,CodeCity;
                db=dbh.getWritableDatabase();
                String StrnameAddress=NameAddres.getText().toString().trim();
                String StrAddAddres=AddAddres.getText().toString().trim();
                String StrError="";
                if(StrAddAddres.length()==0 || StrAddAddres.compareTo("")==0)
                {
                    StrError="آدرس دقیق محل را وارد نمایید"+"\n";
                }
                if(StrnameAddress.length()==0 || StrnameAddress.compareTo("")==0)
                {
                    StrError="نامی دلخواه برای آدرس محل وارد نمایید."+"\n";
                }
                if(StrError.length()==0 || StrError.compareTo("")==0)
                {
                    String latStr=Double.toString(lat);
                    String lonStr=Double.toString(lon);

                    SyncUpdateAddress syncUpdateAddress =new SyncUpdateAddress(UpdateAddress.this,karbarCode,AddressCode,"0",StrnameAddress,"0","0",StrAddAddres,"0",latStr,lonStr,"1","1");
                    syncUpdateAddress.AsyncExecute();
                }

                db.close();
            }
        });

        lat=35.691063;
        lon=51.407941;
        //*************************************************************************************************
        db=dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM address WHERE Status='1' AND Code='"+AddressCode+"'",null);
        if(coursors.getCount()>0)
        {

            coursors.moveToNext();
            NameAddres.setText(coursors.getString(coursors.getColumnIndex("Name")));
            AddAddres.setText(coursors.getString(coursors.getColumnIndex("AddressText")));

            lat=Double.parseDouble(coursors.getString(coursors.getColumnIndex("Lat")));
            lon=Double.parseDouble(coursors.getString(coursors.getColumnIndex("Lng")));
        }
        coursors.close();
        db.close();
        //*************************************************************************************************
        GPSTracker gps = new GPSTracker(UpdateAddress.this);

        // check if GPS enabled
        if (!gps.canGetLocation()) {

            gps.showSettingsAlert();
        }
//*************************************************************************************************
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
            @Override

            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(UpdateAddress.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(UpdateAddress.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                LatLng point;
                point = new LatLng(lat, lon);
//                db = dbh.getReadableDatabase();
//                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
//                if (coursors.getCount() > 0) {
//                    coursors.moveToNext();
//                    String latStr = coursors.getString(coursors.getColumnIndex("Lat"));
//                    String lonStr = coursors.getString(coursors.getColumnIndex("Lon"));
//                    lat = Double.parseDouble(latStr);
//                    lon = Double.parseDouble(lonStr);
//                    if (latStr.compareTo("0")!=0 && lonStr.compareTo("0")!=0) {
//                        point = new LatLng(lat, lon);
//                    }
//                }
//                coursors.close();
//                db.close();
                map.addMarker(new MarkerOptions().position(point).title("سرویس").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        map.clear();
                        map.addMarker(new MarkerOptions().position(latLng).title("محل سرویس دهی").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
                        lat=latLng.latitude;
                        lon=latLng.longitude;
                        if(LinearBottomSaveAddress.getVisibility()==View.GONE)
                        {
                            LinearBottomSaveAddress.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
//***************************************************************************************************
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            LoadActivity(List_Address.class,"karbarCode",karbarCode);
        }
        return super.onKeyDown( keyCode, event );
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        this.startActivity(intent);
    }
}

