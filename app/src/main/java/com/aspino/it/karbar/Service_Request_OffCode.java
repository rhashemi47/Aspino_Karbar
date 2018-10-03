package com.aspino.it.karbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request_OffCode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//	private TextView tvTitleService;
	private ImageView imgBack;
	private ImageView imgSave;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String StartYear ;
	private String StartMonth ;
	private String StartDay ;
	private String StartHour ;
	private String StartMinute ;
	private String EndYear ;
	private String EndMonth ;
	private String EndDay ;
	private String EndHour ;
	private String EndMinute ;
	private String AddressCode ;
	private String Description ;
	private String karbarCode;
	private String DetailCode;
	private String FromDate;
	private String ToDate;
	private String FromTime;
	private String ToTime;
	private String TimeDiff;
	private String CodeService;
	private String MaleCount;
	private String FemaleCount;
	private String HamyarCount;
	//*********************************************
	private DrawerLayout mDrawer;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private Button btnLogout;
	private Button btnSetOffCode;
	private ImageView imgMenu;
	private String OffCodeSTR="";
	private EditText etOffCode;
	//***************************************************

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_offcode);

		imgBack=(ImageView) findViewById(R.id.imgBack);
		imgSave=(ImageView) findViewById(R.id.imgForward);
		btnSetOffCode=(Button) findViewById(R.id.btnSetOffCode);
		etOffCode=(EditText) findViewById(R.id.etOffCode);

//****************************************************************
		Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar);

//		mtoolbar.setTitle("");
//		setSupportActionBar(mtoolbar);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		mNavi = (NavigationView) findViewById(R.id.navigation_view);

		View header_View= mNavi.getHeaderView(0);

		btnLogout=(Button)header_View.findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Logout();
			}
		});
		mNavi.setNavigationItemSelectedListener(this);
		mNavi.setItemIconTintList(null);
		imgMenu=(ImageView)findViewById(R.id.imgMenu);
		imgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.openDrawer(GravityCompat.START);
			}
		});
//		ActionBarDrawerToggle aToggle = new ActionBarDrawerToggle(this, mDrawer, mtoolbar, R.string.open, R.string.close);
//		mDrawer.addDrawerListener(aToggle);
//		aToggle.syncState();
		//*****************************************************************
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
	try
	{

		FromDate = getIntent().getStringExtra("FromDate").toString();
		String sp[]=FromDate.split("-");
		String splitStrDate[]=sp[1].split("/");
		StartYear =splitStrDate[0].replace(" ","");
		StartMonth =splitStrDate[1];
		StartDay =splitStrDate[2];

	}
	catch (Exception ex)
	{
		FromDate="0";
		StartYear ="0";
		StartMonth ="0";
		StartDay ="0";
	}
	try
	{
		ToDate = getIntent().getStringExtra("ToDate").toString();
		String splitStr[]=ToDate.split("/");
		EndYear =splitStr[0];
		EndMonth =splitStr[1];
		EndDay =splitStr[2];
	}
	catch (Exception ex)
	{
		ToDate="0";
		EndYear ="0";
		EndMonth ="0";
		EndDay ="0";
	}
	try
	{
		FromTime = getIntent().getStringExtra("FromTime").toString();
		String splitStr[]=FromTime.split(":");
		StartHour =splitStr[0];
		StartMinute =splitStr[1];
	}
	catch (Exception ex)
	{
		FromTime="0";
		StartHour ="0";
		StartMinute ="0";
	}
	try
	{
		ToTime = getIntent().getStringExtra("ToTime").toString();
		String splitStr[]=ToTime.split(":");
		EndHour =splitStr[0];
		EndMinute =splitStr[1];
	}
	catch (Exception ex)
	{
		ToTime="0";
		EndHour ="0";
		EndMinute ="0";
	}
	try
	{
		Description = getIntent().getStringExtra("Description").toString();
	}
	catch (Exception ex)
	{
		Description="";
	}
	try
	{
		TimeDiff = getIntent().getStringExtra("TimeDiff").toString();
	}
	catch (Exception ex)
	{
		TimeDiff="0";
	}
	try
	{
		DetailCode = getIntent().getStringExtra("DetailCode").toString();
	}
	catch (Exception ex)
	{
		DetailCode="0";
	}
	try
	{
		MaleCount = getIntent().getStringExtra("MaleCount").toString();
	}
	catch (Exception ex)
	{
		MaleCount="0";
	}
	try
	{
		FemaleCount = getIntent().getStringExtra("FemaleCount").toString();
	}
	catch (Exception ex)
	{
		FemaleCount="0";
	}
	try
	{
		HamyarCount = getIntent().getStringExtra("HamyarCount").toString();
	}
	catch (Exception ex)
	{
		HamyarCount="0";
	}
	try
	{
		karbarCode = getIntent().getStringExtra("karbarCode").toString();
	}
	catch (Exception e)
	{
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();

			karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
		}
		db.close();
	}
//**************************************************************
		db=dbh.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT * FROM OffCode",null);
		if(cursor.getCount()>0)
		{
			OffCodeSTR=cursor.getString(cursor.getColumnIndex("Value"));
			etOffCode.setText(OffCodeSTR);
		}
		else
		{
			OffCodeSTR="0";
			etOffCode.setText(OffCodeSTR);
		}
//**************************************************************
		btnSetOffCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SyncGetUserOffCode syncGetUserOffCode=new SyncGetUserOffCode(Service_Request_OffCode.this,etOffCode.getText().toString());
				syncGetUserOffCode.AsyncExecute();
			}
		});
//**************************************************************
		imgSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr="";
				db=dbh.getReadableDatabase();
				Cursor cursorAddress = db.rawQuery("SELECT * FROM address_select",null);
				if(cursorAddress.getCount()>0)
				{
					cursorAddress.moveToNext();
					AddressCode=cursorAddress.getString(cursorAddress.getColumnIndex("Code"));
				}
				else
				{
					AddressCode="0";
				}
				db.close();
				if(ErrorStr.length()==0)
					{

						SyncInsertUserServices syncInsertUserServices = new SyncInsertUserServices(Service_Request_OffCode.this,
								karbarCode, DetailCode, MaleCount, FemaleCount, HamyarCount, StartYear, StartMonth,
								StartDay, StartHour, StartMinute, EndYear, EndMonth, EndDay, EndHour, EndMinute,
								AddressCode, Description, "0", "0", "0",
								"0", "0", "0", "0", "0", "0", "0", "0",TimeDiff,etOffCode.getText().toString());
						syncInsertUserServices.AsyncExecute();
					}
					else
					{
							Toast.makeText(Service_Request_OffCode.this, ErrorStr, Toast.LENGTH_SHORT).show();
					}
		}
	});
		imgBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoadActivity2(Service_Request1.class,"karbarCode", karbarCode,
						"DetailCode", DetailCode,
						"FromDate", FromDate,
						"ToDate", ToDate,
						"FromTime",FromTime,
						"ToTime", ToTime,
						"Description", Description,
						"TimeDiff", TimeDiff,
						"AddressCode", AddressCode);
			}
		});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity2(Service_Request1.class,"karbarCode", karbarCode,
				"DetailCode", DetailCode,
				"FromDate", FromDate,
				"ToDate", ToDate,
				"FromTime",FromTime,
				"ToTime", ToTime,
				"Description", Description,
				"TimeDiff", TimeDiff,
				"AddressCode", AddressCode);
    }
    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		Service_Request_OffCode.this.startActivity(intent);
	}

public void LoadActivity2(Class<?> Cls, String VariableName1, String VariableValue1,
						  String VariableName2, String VariableValue2,
						  String VariableName3, String VariableValue3,
						  String VariableName4, String VariableValue4,
						  String VariableName5, String VariableValue5,
						  String VariableName6, String VariableValue6,
						  String VariableName7, String VariableValue7,
						  String VariableName8, String VariableValue8,
						  String VariableName9, String VariableValue9)
{
	Intent intent = new Intent(getApplicationContext(),Cls);
	intent.putExtra(VariableName1, VariableValue1);
	intent.putExtra(VariableName2, VariableValue2);
	intent.putExtra(VariableName3, VariableValue3);
	intent.putExtra(VariableName4, VariableValue4);
	intent.putExtra(VariableName5, VariableValue5);
	intent.putExtra(VariableName6, VariableValue6);
	intent.putExtra(VariableName7, VariableValue7);
	intent.putExtra(VariableName8, VariableValue8);
	intent.putExtra(VariableName9, VariableValue9);
	Service_Request_OffCode.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		startActivity(intent);
	}
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {

		int mId = item.getItemId();

		switch (mId) {

			case R.id.profile:
				db = dbh.getReadableDatabase();
				Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
				if (coursors.getCount() > 0) {
					coursors.moveToNext();
					String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
					if (Status_check.compareTo("0") == 0) {
						Cursor c = db.rawQuery("SELECT * FROM login", null);
						if (c.getCount() > 0) {
							c.moveToNext();
							SyncProfile profile = new SyncProfile(Service_Request_OffCode.this, c.getString(c.getColumnIndex("karbarCode")));
							profile.AsyncExecute();
						}
					} else {
						LoadActivity2(Profile.class, "karbarCode", karbarCode,"","");
					}
				}
				else {
					LoadActivity2(Login.class,"karbarCode","0","","");
				}
				db.close();
				break;

			case R.id.wallet:
				db = dbh.getReadableDatabase();
				Cursor c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					LoadActivity2(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")),"","");
				}
				else {
					LoadActivity2(Login.class,"karbarCode","0","","");
				}
				db.close();
				break;
			case R.id.Order:
				db = dbh.getReadableDatabase();
				c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					String QueryCustom;
					QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
							"LEFT JOIN " +
							"Servicesdetails ON " +
							"Servicesdetails.code=OrdersService.ServiceDetaileCode";
					LoadActivity2(Paigiri.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
				}
				break;

			case R.id.AddresManagement:
				db = dbh.getReadableDatabase();
				c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
				}
				db.close();
				break;

			case R.id.Invite_friends:
				sharecode("0");
				break;

			case R.id.About:
				db = dbh.getReadableDatabase();
				c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();

					LoadActivity(About.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
				}
				db.close();
				break;
		}

		mDrawer.closeDrawer(GravityCompat.START);
		return true;

	}
	void sharecode(String shareStr)
	{
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "آسپینو" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "عنوان");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
	}
	@Override
	public void onBackPressed() {

		if (mDrawer.isDrawerOpen(GravityCompat.START)) {

			mDrawer.closeDrawer(GravityCompat.START);

		} else {

//			super.onBackPressed();
			LoadActivity(MainMenu.class, "karbarCode", karbarCode);
		}

	}
	public void Logout() {
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		// set the message to display
		alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

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
				//stopService(new Intent(getBaseContext(), ServiceGetLocation.class));                stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));

				stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
//				stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));


				db = dbh.getWritableDatabase();
				db.execSQL("DELETE FROM address");
				db.execSQL("DELETE FROM AmountCredit");
				db.execSQL("DELETE FROM android_metadata");
				db.execSQL("DELETE FROM Arts");
				db.execSQL("DELETE FROM BsFaktorUserDetailes");
				db.execSQL("DELETE FROM BsFaktorUsersHead");
				db.execSQL("DELETE FROM City");
				db.execSQL("DELETE FROM credits");
				db.execSQL("DELETE FROM DateTB");
				db.execSQL("DELETE FROM FieldofEducation");
				db.execSQL("DELETE FROM Grade");
				db.execSQL("DELETE FROM Hamyar");
				db.execSQL("DELETE FROM InfoHamyar");
				db.execSQL("DELETE FROM Language");
				db.execSQL("DELETE FROM login");
				db.execSQL("DELETE FROM messages");
				db.execSQL("DELETE FROM OrdersService");
				db.execSQL("DELETE FROM Profile");
				db.execSQL("DELETE FROM services");
				db.execSQL("DELETE FROM servicesdetails");
				db.execSQL("DELETE FROM Slider");
				db.execSQL("DELETE FROM sqlite_sequence");
				db.execSQL("DELETE FROM State");
				db.execSQL("DELETE FROM Supportphone");
				db.execSQL("DELETE FROM Unit");
				db.execSQL("DELETE FROM UpdateApp");
				db.execSQL("DELETE FROM visit");
				db.close();
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
