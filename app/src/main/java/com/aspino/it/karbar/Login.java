package  com.aspino.it.karbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Login extends Activity {
	private Button btnEnter;
	private EditText etPhoneNumber;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
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
//        etPhoneNumber.setTypeface(FontMitra);
//		btnEnter.setTypeface(FontMitra);
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
							db.execSQL(query);if(db.isOpen()){db.close();}
							SendAcceptCode sendCode = new SendAcceptCode(Login.this, etPhoneNumber.getText().toString(), "1");
							sendCode.AsyncExecute();
							if(db.isOpen()) {
								db.close();
							}
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
			ExitApplication();
		}
		return super.onKeyDown(keyCode, event);
	}
	private void ExitApplication()
	{
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
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
				//System.exit(0);
				Intent startMain = new Intent(Intent.ACTION_MAIN);

				startMain.addCategory(Intent.CATEGORY_HOME);

				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(startMain);

				finish();

				arg0.dismiss();

			}
		});

		alertbox.show();
	}
}


