package com.aspino.it.karbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class Login extends Activity {
	private Button btnEnter;
	private EditText etPhoneNumber;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
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
					InternetConnection ic = new InternetConnection(getApplicationContext());
					if (ic.isConnectingToInternet()) {
						String query = null;
						db = dbh.getWritableDatabase();
						query = "INSERT INTO Profile (Mobile) VALUES ('" + etPhoneNumber.getText().toString() + "')";
						db.execSQL(query);
						SendAcceptCode sendCode = new SendAcceptCode(Login.this, etPhoneNumber.getText().toString(), "1");
						sendCode.AsyncExecute();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "لطفا شماره همراه خود را وارد نمایید.", Toast.LENGTH_LONG).show();
				}
				db.close();
			}
		});
    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Login.this.startActivity(intent);
	}
	private void ExitApplication()
	{
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(Login.this);
		// set the message to display
		alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");

		// set a negative/no button and create a listener
		alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});

		// set a positive/yes button and create a listener
		alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				//Declare Object From Get Internet Connection Status For Check Internet Status
				System.exit(0);
				arg0.dismiss();

			}
		});

		alertbox.show();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
			ExitApplication();
		}
		return super.onKeyDown( keyCode, event );
	}

	public Bitmap convertToBitmap(String base) {
		Bitmap Bmp = null;
		try {
			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
			return Bmp;
		} catch (Exception e) {
			e.printStackTrace();
			return Bmp;
		}
	}
}


