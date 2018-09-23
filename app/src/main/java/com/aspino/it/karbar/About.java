package com.aspino.it.karbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
//	private Button btnOrder;
//	private Button btnAcceptOrder;
//	private Button btncredite;
//	private GoogleMap map;
//	private Typeface FontMitra;
//	private LatLng point;
	private DrawerLayout mDrawer;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private Button btnLogout;
	private ImageView imgBackToggle;
	private ImageView imgMenu;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_about);
//	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
//	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
//	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	//****************************************************************
	Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar_about);

	mtoolbar.setTitle("");
//	setSupportActionBar(mtoolbar);
//	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	getSupportActionBar().setDisplayShowHomeEnabled(true);
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

	imgMenu=(ImageView)findViewById(R.id.imgMenu);
	imgMenu.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mDrawer.openDrawer(GravityCompat.START);
		}
	});

//        ActionBarDrawerToggle aToggle = new ActionBarDrawerToggle(this, mDrawer, mtoolbar, R.string.open, R.string.close);

//        mDrawer.addDrawerListener(aToggle);
//        aToggle.syncState();
	//*****************************************************************
//	btnLogout.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View view) {
//			Logout();
//		}
//	});
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
		Cursor cursor = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			karbarCode=cursor.getString(cursor.getColumnIndex("karbarCode"));
		}
	}
//	FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvTextAbout);
//	txtContent.setTypeface(FontMitra);

}
//@Override
//public boolean onKeyDown( int keyCode, KeyEvent event )  {
//    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
//    	LoadActivity(MainMenu.class, "karbarCode", karbarCode);
//    }
//
//    return super.onKeyDown( keyCode, event );
//}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home:
				onBackPressed();
				break;
		}
		return  true;
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
								SyncProfile profile = new SyncProfile(About.this, c.getString(c.getColumnIndex("karbarCode")));
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

				stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
				stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));

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
