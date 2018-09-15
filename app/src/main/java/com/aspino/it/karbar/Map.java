package com.aspino.it.karbar;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by hashemi on 01/23/2018.
 */

public class Map extends AppCompatActivity {
    private Button btnSaveLocation;
    private EditText NameAddres;
    private EditText AddAddres;
    public double lat = 0;
    private double lon = 0;
    private GoogleMap map;
    private String backToActivity;
    private String codeService;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String Description;
    private String karbarCode;
    private String DetailCode;
    private String FromDate;
    private String ToDate;
    private String FromTime;
    private String ToTime;
    private String TimeDiff;
    private String MaleCount;
    private String FemaleCount;
    private String HamyarCount;
    private LinearLayout LinearBottomSaveAddress;
    private GPSTracker  gps;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Handler mHandler;
    private boolean continue_or_stop=true;
    private AlertDialog.Builder alertDialog = null;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        dbh = new DatabaseHelper(getApplicationContext());
        try {

            dbh.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbh.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;
        }
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        NameAddres = (EditText) findViewById(R.id.NameAddres);
        AddAddres = (EditText) findViewById(R.id.AddAddres);
        LinearBottomSaveAddress = (LinearLayout) findViewById(R.id.LinearBottomSaveAddress);
        try {

            FromDate = getIntent().getStringExtra("FromDate").toString();

        } catch (Exception ex) {
            FromDate = "0";
        }
        try {
            ToDate = getIntent().getStringExtra("ToDate").toString();
        } catch (Exception ex) {
            ToDate = "0";
        }
        try {
            FromTime = getIntent().getStringExtra("FromTime").toString();
        } catch (Exception ex) {
            FromTime = "0";
        }
        try {
            ToTime = getIntent().getStringExtra("ToTime").toString();
        } catch (Exception ex) {
            ToTime = "0";
        }
        try {
            Description = getIntent().getStringExtra("Description").toString();
        } catch (Exception ex) {
            Description = "";
        }
        try {
            TimeDiff = getIntent().getStringExtra("TimeDiff").toString();
        } catch (Exception ex) {
            TimeDiff = "0";
        }
        try {
            DetailCode = getIntent().getStringExtra("DetailCode").toString();
        } catch (Exception ex) {
            DetailCode = "0";
        }
        try {
            MaleCount = getIntent().getStringExtra("MaleCount").toString();
        } catch (Exception ex) {
            MaleCount = "0";
        }
        try {
            FemaleCount = getIntent().getStringExtra("FemaleCount").toString();
        } catch (Exception ex) {
            FemaleCount = "0";
        }
        try {
            HamyarCount = getIntent().getStringExtra("HamyarCount").toString();
        } catch (Exception ex) {
            HamyarCount = "0";
        }
        try {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();
        } catch (Exception e) {
            db = dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login", null);
            for (int i = 0; i < coursors.getCount(); i++) {
                coursors.moveToNext();

                karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
            }
            db.close();
        }
        try {
            backToActivity = getIntent().getStringExtra("nameActivity").toString();
        } catch (Exception e) {
            backToActivity = "";
        }
        try {
            codeService = getIntent().getStringExtra("codeService").toString();
        } catch (Exception e) {
            codeService = "";
        }
//********************
   Run_thered();
//******************************************************************************





        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CodeState="",CodeCity="";
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
                    StrError="نامی دلخواه برای آدرس محل وارد نمایید!"+"\n";
                }
                db=dbh.getReadableDatabase();
                Cursor cursorAddress = db.rawQuery("SELECT * FROM address WHERE Name='"+StrnameAddress+"'",null);
                if(cursorAddress.getCount()>0) {
                    StrError="نام آدرس تکراری است!"+"\n";
                }
                if(StrError.length()==0 || StrError.compareTo("")==0)
                {
                    String latStr=Double.toString(lat);
                    String lonStr=Double.toString(lon);
                    SyncAddress syncAddress=new SyncAddress(Map.this,karbarCode,"0",StrnameAddress,"0","0",StrAddAddres,"0",latStr,lonStr);
                    syncAddress.AsyncExecute();
                }
                else
                {
                    Toast.makeText(Map.this,StrError,Toast.LENGTH_LONG).show();
                }
                db.close();
            }
        });

    }

    private void Run_thered() {
        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (continue_or_stop) {
                    try {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {
//                                    continue_or_stop=false;
                                    ActivityCompat.requestPermissions(Map.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);

//            return;
                                }
                                else {
                                    Check_GPS();
                                }
                            }
                        });

                        Thread.sleep(1000); // every 5 seconds
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    private void Check_GPS() {
        gps = new GPSTracker(Map.this);
        if (gps.canGetLocation()) {
            continue_or_stop=false;
            PrepareMap();
        } else {
            if (alertDialog == null) {
                alertDialog = new AlertDialog.Builder(Map.this);

                // Setting Dialog Title
                alertDialog.setTitle("تنظیمات جی پی اس");

                // Setting Dialog Message
                alertDialog.setMessage("جی پی اس شما غیرفعال می باشد.لطفا جهت کار کرد صحیح نرم افزار آن را فعال نمایید");

                // On pressing Settings button
                alertDialog.setPositiveButton("فعال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        continue_or_stop = true;
                        Run_thered();
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        continue_or_stop = true;
                        Run_thered();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(backToActivity.compareTo("Service_Request_SelectAddress")==0){
                LoadActivity(Service_Request_SelectAddress.class
                , "karbarCode", karbarCode
                ,"FromDate",FromDate
                ,"FromTime",FromTime
                ,"ToTime",ToTime
                ,"Description",Description
                ,"TimeDiff",TimeDiff
                ,"DetailCode",DetailCode
                ,"MaleCount",MaleCount
                ,"FemaleCount",FemaleCount
                ,"HamyarCount",HamyarCount
                ,"ToDate",ToDate);
            }else
            {
                LoadActivity2(List_Address.class, "karbarCode", karbarCode,"codeService",codeService);
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    public void LoadActivity(Class<?> Cls
            , String VariableName1, String VariableValue1
            , String VariableName2, String VariableValue2
            , String VariableName3, String VariableValue3
            , String VariableName4, String VariableValue4
            , String VariableName5, String VariableValue5
            , String VariableName6, String VariableValue6
            , String VariableName7, String VariableValue7
            , String VariableName8, String VariableValue8
            , String VariableName9, String VariableValue9
            , String VariableName10, String VariableValue10
            , String VariableName11, String VariableValue11)
    {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName1, VariableValue1);
        intent.putExtra(VariableName2, VariableValue2);
        intent.putExtra(VariableName3, VariableValue3);
        intent.putExtra(VariableName4, VariableValue4);
        intent.putExtra(VariableName5, VariableValue5);
        intent.putExtra(VariableName6, VariableValue6);
        intent.putExtra(VariableName7, VariableValue7);
        intent.putExtra(VariableName8, VariableValue8);
        intent.putExtra(VariableName9, VariableValue9);
        intent.putExtra(VariableName10, VariableValue10);
        intent.putExtra(VariableName11, VariableValue11);
        this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);
        this.startActivity(intent);
    }
    public void PrepareMap()
    {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
                LatLng point;
                lat=35.691063;
                lon=51.407941;
                point = new LatLng(lat, lon);
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    String latStr = coursors.getString(coursors.getColumnIndex("Lat"));
                    String lonStr = coursors.getString(coursors.getColumnIndex("Lon"));
                    lat = Double.parseDouble(latStr);
                    lon = Double.parseDouble(lonStr);
                    if (latStr.compareTo("0")!=0 && lonStr.compareTo("0")!=0) {
                        point = new LatLng(lat, lon);
                    }
                }
                db.close();
                map.addMarker(new MarkerOptions().position(point).title("سرویس").icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));


                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        String str = latLng.toString();
                        //  Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
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
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                try {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
//                        continue_or_stop = true;
                    } else {
                        // Permission Denied
//                        continue_or_stop = true;
                        Toast.makeText(Map.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                catch (Exception ex)
                {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

