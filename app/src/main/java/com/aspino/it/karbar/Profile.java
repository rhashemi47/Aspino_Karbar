package com.aspino.it.karbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
//import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends Activity implements NavigationView.OnNavigationItemSelectedListener{
	private String karbarCode;
	private TextView tvTextUserName;
	private TextView tvTextUserMobile;
	private TextView tvTextEditProfile;
	private TextView tvTextCallSupport;
	private TextView tvTextExitAccount;
	private TextView tvTitleAdressAdd;
	private TextView tvCodeMoaref;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ImageView imgUser;
	private int color;
	private Paint paint;
	private Rect rect;
	private RectF rectF;
	private Bitmap result;
	private Canvas canvas;
	private float roundPx;
	private DrawerLayout mDrawer;
	private ImageView imgMenu;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private Button btnLogout;
	private ImageView imgBackToggle;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu_profile);
		imgUser=(ImageView)findViewById(R.id.imgUser);
		tvTextUserName=(TextView)findViewById(R.id.tvTextUserName);
		tvTextUserMobile=(TextView)findViewById(R.id.tvTextUserMobile);
		tvTextEditProfile=(TextView) findViewById(R.id.tvTextEditProfile);
		tvTextCallSupport=(TextView) findViewById(R.id.tvTextCallSupport);
		tvTextExitAccount=(TextView) findViewById(R.id.tvTextExitAccount);
		//*****************************************************************
		Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar);

		mtoolbar.setTitle("");
//		setSupportActionBar(mtoolbar);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowHomeEnabled(true);
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
			db=dbh.getReadableDatabase();
			Cursor coursors = db.rawQuery("SELECT * FROM login",null);
			for(int i=0;i<coursors.getCount();i++){
				coursors.moveToNext();

				karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
				tvTextUserMobile.setText(coursors.getString(coursors.getColumnIndex("Phone")));
			}
			db.close();

		Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		db=dbh.getReadableDatabase();
		coursors = db.rawQuery("SELECT * FROM Profile",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			tvTextUserName.setText(coursors.getString(coursors.getColumnIndex("Name")) + " " + coursors.getString(coursors.getColumnIndex("Fam")));
			try
			{
				if(coursors.getString(coursors.getColumnIndex("Pic")).length()>0) {
					bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
				}
			}
			catch (Exception ex)
			{

			}
		}
		try
		{
			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
		}
		catch (Exception ex)
		{
			bmp = BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
		}
		tvTextEditProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity(ProfileEdit.class,"karbarCode",karbarCode);
			}
		});
		tvTextCallSupport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				db=dbh.getReadableDatabase();
				Cursor cursor = db.rawQuery("SELECT * FROM Supportphone",null);
				if(cursor.getCount()>0)
				{
					cursor.moveToNext();
					dialContactPhone(cursor.getString(cursor.getColumnIndex("PhoneNumber")));
				}
				db.close();
			}
		});
		tvTextExitAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Logout();
			}
		});
//		btnEditAdres=(Button)findViewById(R.id.btnEditAdres);
//		btnEditAdres.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","Profile");
//			}
//		});
//		btnAddAdres=(Button)findViewById(R.id.btnAddAdres);
//		btnAddAdres.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//				LoadActivity2(Map.class,"karbarCode",karbarCode,"nameActivity","Profile");
//			}
//		});
//		btnSaveProfile=(Button)findViewById(R.id.btnSendProfile);
//		btnSaveProfile.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				InternetConnection ic=new InternetConnection(getApplicationContext());
//				if(ic.isConnectingToInternet())
//				{
//					if(ReagentCode.length()>0 && ReagentCode.length()<=5)
//					{
//						Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
//					}
//					else
//					{
//						insertKarbar();
//					}
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
//				}
//				db.close();
//			}
//		});
//
//		brithday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//				PersianCalendar now = new PersianCalendar();
//				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//						new DatePickerDialog.OnDateSetListener() {
//							@Override
//							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//								brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
//								yearStr=String.valueOf(year);
//								monStr=String.valueOf(monthOfYear+1);
//								dayStr=String.valueOf(dayOfMonth);
//							}
//						}, now.getPersianYear(),
//						now.getPersianMonth(),
//						now.getPersianDay());
//				datePickerDialog.setThemeDark(true);
//				datePickerDialog.show(getFragmentManager(), "tpd");
//
//			}
//
//		});
//		brithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(hasFocus)
//				{
//					PersianCalendar now = new PersianCalendar();
//					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//							new DatePickerDialog.OnDateSetListener() {
//								@Override
//								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//									brithday.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth));
//									yearStr=String.valueOf(year);
//									monStr=String.valueOf(monthOfYear+1);
//									dayStr=String.valueOf(dayOfMonth);
//								}
//							}, now.getPersianYear(),
//							now.getPersianMonth(),
//							now.getPersianDay());
//					datePickerDialog.setThemeDark(true);
//					datePickerDialog.show(getFragmentManager(), "tpd");
//				}
//			}
//		});
//		db=dbh.getReadableDatabase();
//		Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//				"LEFT JOIN " +
//				"Servicesdetails ON " +
//				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
//		if (cursor2.getCount() > 0) {
//			btnOrder.setText("درخواست ها: " + cursor2.getCount());
//		}
//		cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
//		if (cursor2.getCount() > 0) {
//			btnAcceptOrder.setText("پذیرفته شده ها: " + cursor2.getCount());
//		}
//		cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//		if (cursor2.getCount() > 0) {
//			cursor2.moveToNext();
//			try {
//				String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//				if(splitStr[1].compareTo("00")==0)
//				{
//					btncredite.setText("اعتبار: " +splitStr[0]);
//				}
//				else
//				{
//					btncredite.setText("اعتبار: " + cursor2.getString(cursor2.getColumnIndex("Amount")));
//				}
//
//			} catch (Exception ex) {
//				btncredite.setText("اعتبار: " + "0");
//			}
//		}
//		db.close();
//		btnOrder.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String QueryCustom;
//				QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//						"LEFT JOIN " +
//						"Servicesdetails ON " +
//						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'";
//				LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//			}
//		});
//		btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String QueryCustom;
//				QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//						"LEFT JOIN " +
//						"Servicesdetails ON " +
//						"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13)";
//				LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//			}
//		});
//		btncredite.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				LoadActivity(Credit.class, "karbarCode", karbarCode);
//			}
//		});
	}

	public void dialContactPhone(String phoneNumber) {
		//startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}


		startActivity(callIntent);
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	Profile.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);

			Profile.this.startActivity(intent);
		}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);
			intent.putExtra(VariableName2, VariableValue2);

			Profile.this.startActivity(intent);
		}
	public Bitmap convertToBitmap(String base){
		Bitmap Bmp=null;
		try
		{
			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
			return Bmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Bmp;
		}
	}
	public  Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels)
	{
		result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		canvas = new Canvas(result);

		color = 0xff424242;
		paint = new Paint();
		rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		rectF = new RectF(rect);
		roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return result;
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
							SyncProfile profile = new SyncProfile(Profile.this, c.getString(c.getColumnIndex("karbarCode")));
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

