package com.aspino.it.karbar;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class Login extends Activity {
	private Button btnEnter;
	private EditText etPhoneNumber;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");//set font for page
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remive page title
        dbh=new DatabaseHelper(getApplicationContext());
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
		gps = new GPSTracker(Login.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			//nothing
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		//*****************************************************
		btnEnter=(Button)findViewById(R.id.btnEnter);
        etPhoneNumber=(EditText)findViewById(R.id.etPhoneNumber);
        //set font for element
        etPhoneNumber.setTypeface(FontMitra);
		btnEnter.setTypeface(FontMitra);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String Phone=etPhoneNumber.getText().toString();
				if(Phone.compareTo("")!=0) {
					if (gps.canGetLocation())
					{
						InternetConnection ic = new InternetConnection(getApplicationContext());
						if (ic.isConnectingToInternet()) {
							String query = null;
							db = dbh.getWritableDatabase();
							query = "INSERT INTO Profile (Mobile) VALUES ('" + etPhoneNumber.getText().toString() + "')";
							db.execSQL(query);
							SendAcceptCode sendCode = new SendAcceptCode(Login.this, etPhoneNumber.getText().toString(), "1");
							sendCode.AsyncExecute();
							db.close();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
						}
					}
					else
						{
						// can't get location
						// GPS or Network is not enabled
						// Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "لطفا شماره همراه خود را وارد نمایید.", Toast.LENGTH_LONG).show();
				}
			}
		});
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Login.this.startActivity(intent);
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			LoadActivity(Main_Activity.class,"karbarCode","0");
		}
		return super.onKeyDown(keyCode, event);
	}
}


