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
	private Button btnSignUp;
	private EditText etPhoneNumber;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ImageView imageView;
	private Custom_ViewFlipper viewFlipper;
	private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

		viewFlipper = (Custom_ViewFlipper) findViewById(R.id.vf);
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
		db = dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Slider", null);
		if (coursors.getCount() > 0) {
			Bitmap bpm[] = new Bitmap[coursors.getCount()];
			String link[] = new String[coursors.getCount()];
			for (int j = 0; j < coursors.getCount(); j++) {

				coursors.moveToNext();
				viewFlipper.setVisibility(View.VISIBLE);
				//slides.add();
				bpm[j] = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
				link[j] = coursors.getString(coursors.getColumnIndex("Link"));
			}
			db.close();
			int i = 0;
			while (i < bpm.length) {
				imageView = new ImageView(getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				//ImageLoader.getInstance().displayImage(slides.get(i),imageView);
				imageView.setImageBitmap(bpm[i]);
				imageView.setTag(link[i]);
				viewFlipper.addView(imageView);
				i++;
			}


			Paint paint = new Paint();
			paint.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
			viewFlipper.setPaintCurrent(paint);
			paint = new Paint();

			paint.setColor(ContextCompat.getColor(this, android.R.color.white));
			viewFlipper.setPaintNormal(paint);

			viewFlipper.setRadius(10);
			viewFlipper.setMargin(5);

			Login.CustomGestureDetector customGestureDetector = new Login.CustomGestureDetector();
			mGestureDetector = new GestureDetector(Login.this, customGestureDetector);

			viewFlipper.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					mGestureDetector.onTouchEvent(motionEvent);
					return true;
				}
			});
		} else {
			viewFlipper.setVisibility(View.GONE);
		}
		//*****************************************************
		btnEnter=(Button)findViewById(R.id.btnEnter);
		btnSignUp=(Button)findViewById(R.id.btnSignUp);
        etPhoneNumber=(EditText)findViewById(R.id.etPhoneNumber);
        //set font for element
        etPhoneNumber.setTypeface(FontMitra);
		btnSignUp.setTypeface(FontMitra);
		btnEnter.setTypeface(FontMitra);
		btnEnter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(Login.this, "برای استفاده از امکانات آسپینو باید ثبت نام کنید", Toast.LENGTH_LONG).show();
				LoadActivity(MainMenu.class,"karbarCode","0");
			}
		});
		btnSignUp.setOnClickListener(new OnClickListener() {

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
	class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			// Swipe left (next)
			if (e1.getX() > e2.getX()) {
				viewFlipper.setInAnimation(Login.this, R.anim.left_in);
				viewFlipper.setOutAnimation(Login.this, R.anim.left_out);

				viewFlipper.showNext();
			} else if (e1.getX() < e2.getX()) {
				viewFlipper.setInAnimation(Login.this, R.anim.right_in);
				viewFlipper.setOutAnimation(Login.this, R.anim.right_out);

				viewFlipper.showPrevious();
			}
			viewFlipper.setInAnimation(Login.this, R.anim.left_in);
			viewFlipper.setOutAnimation(Login.this, R.anim.left_out);

			return super.onFling(e1, e2, velocityX, velocityY);
		}
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


