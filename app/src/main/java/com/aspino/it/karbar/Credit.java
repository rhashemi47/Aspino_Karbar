package com.aspino.it.karbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
//	private TextView tvRecentCreditsValue;
	private SQLiteDatabase db;
//	private Button btnIncreseCredit;
//	private ListView lstHistoryCredit;
//	private Button btnOrder;
//	private Button btnAcceptOrder;
//	private Button btncredite;
	private DrawerLayout mDrawer;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private Button btnLogout;
	private ImageView imgBackToggle;
	private EditText etCurrencyInsertCredit;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_credits);
//	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
//	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
//	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	etCurrencyInsertCredit=(EditText) findViewById(R.id.etCurrencyInsertCredit);
	//****************************************************************
	Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar);

	mtoolbar.setTitle("");
	setSupportActionBar(mtoolbar);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	getSupportActionBar().setDisplayShowHomeEnabled(true);
	mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

	mNavi = (NavigationView) findViewById(R.id.navigation_view);
	View header_View= mNavi.getHeaderView(0);
	imgBackToggle=(ImageView)findViewById(R.id.imgBackToggle);
	imgBackToggle.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	});
	btnLogout=(Button)header_View.findViewById(R.id.btnLogout);
	btnLogout.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Logout();
		}
	});
	mNavi.setNavigationItemSelectedListener(this);
	mNavi.setItemIconTintList(null);

	ActionBarDrawerToggle aToggle = new ActionBarDrawerToggle(this, mDrawer, mtoolbar, R.string.open, R.string.close);

	mDrawer.addDrawerListener(aToggle);
	aToggle.syncState();
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
		karbarCode = getIntent().getStringExtra("karbarCode").toString();

	}
	catch (Exception e)
	{
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();

			karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
		}
	}
//
//	btnIncreseCredit=(Button)findViewById(R.id.btnIncresCredit);
//	lstHistoryCredit=(ListView) findViewById(R.id.lstHistoryCredit);
	//Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
//	txtContent.setTypeface(FontMitra);
//	tvRecentCreditsValue=(TextView)findViewById(R.id.tvRecentCreditsValue);
//	tvRecentCreditsValue.setTypeface(FontMitra);
	String Query="UPDATE UpdateApp SET Status='1'";
	db=dbh.getWritableDatabase();
	db.execSQL(Query);
	try
	{
		String Content="";
		Cursor coursors = db.rawQuery("SELECT * FROM AmountCredit", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			String splitStr[]=coursors.getString(coursors.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				Content+=splitStr[0];
			}
			else
			{
				Content+=coursors.getString(coursors.getColumnIndex("Amount"));
			}

		}
		else
		{
//			lstHistoryCredit.setVisibility(View.GONE);
		}
		if(Content.compareTo("")==0){
//			tvRecentCreditsValue.setText("0"+" ریال");
		}
		else {
//			tvRecentCreditsValue.setText(Content+" ریال");
		}
	}
	catch (Exception ex){
//		tvRecentCreditsValue.setText("0"+" ریال");
//		lstHistoryCredit.setVisibility(View.GONE);
	}
//	try
//	{
//		Cursor coursors = db.rawQuery("SELECT * FROM credits", null);
//		String Content="";
//		for (int i=0;i<coursors.getCount();i++) {
//			coursors.moveToNext();
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("name","مبلغ: " +coursors.getString(coursors.getColumnIndex("Price"))+ " ریال " +"\n"
//			+"عملیات: " + coursors.getString(coursors.getColumnIndex("TransactionType"))+ "\n"
//			+"نوع تراکنش: " + coursors.getString(coursors.getColumnIndex("PaymentMethod"))+ "\n"
//			+"تاریخ: " + coursors.getString(coursors.getColumnIndex("TransactionDate"))+ "\n"
//			+"شماره سند: " + coursors.getString(coursors.getColumnIndex("DocNumber"))+ "\n"
//			+"توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")));
//			map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
//			valuse.add(map);
//		}
//		AdapterCredit dataAdapter=new AdapterCredit(Credit.this,valuse,karbarCode);
//		lstHistoryCredit.setAdapter(dataAdapter);
//		if(valuse.size()==0){
//			lstHistoryCredit.setVisibility(View.GONE);
//			txtContent.setVisibility(View.VISIBLE);
//			txtContent.setText("موردی جهت نمایش وجود ندارد");
//		}
//		else
//		{
//			lstHistoryCredit.setVisibility(View.VISIBLE);
//			txtContent.setVisibility(View.GONE);
//		}
//	}
//	catch (Exception ex){
//		lstHistoryCredit.setVisibility(View.GONE);
//		txtContent.setVisibility(View.VISIBLE);
//		tvRecentCreditsValue.setText("موردی جهت نمایش وجود ندارد");
//	}
//	btnIncreseCredit.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			if(etCurrencyInsertCredit.getText().length()>0) {
//				SyncInsertUserCredit syncInsertUserCredit = new SyncInsertUserCredit(Credit.this, etCurrencyInsertCredit.getText().toString(), karbarCode, "1", "10004", "تست");
//				syncInsertUserCredit.AsyncExecute();
//			}
//			else
//			{
//				Toast.makeText(Credit.this, "لطفا مبلغ مورد نظر خود را به ریال وارد نمایید", Toast.LENGTH_SHORT).show();
//			}
//		}
//	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	Credit.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		Credit.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
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
							SyncProfile profile = new SyncProfile(Credit.this, c.getString(c.getColumnIndex("karbarCode")));
							profile.AsyncExecute();
						}
					} else {
						LoadActivity(Profile.class, "karbarCode", karbarCode);
					}
				}
				else {
					LoadActivity(Login.class,"karbarCode","0");
				}
				db.close();
				break;

			case R.id.wallet:
				db = dbh.getReadableDatabase();
				Cursor c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
				}
				else {
					LoadActivity(Login.class,"karbarCode","0");
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
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "آسپینو" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
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
				stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
				stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));
				stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
				stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));
				stopService(new Intent(getBaseContext(), ServiceGetPerFactor.class));
				stopService(new Intent(getBaseContext(), ServiceGetServiceVisit.class));
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
