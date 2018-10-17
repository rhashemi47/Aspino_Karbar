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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import android.graphics.Typeface;

public class Select_Hamyar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private static  Cursor C;
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
	private SQLiteDatabase db;
	private DrawerLayout mDrawer;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private ImageView imgBackToggle;
	private Button btnLogout;
	private String OrderCode;
	private TextView tvContentTypeService;
	private TextView tvContentCodeService;
	private TextView tvContentAddress;
	private TextView tvDateService;
	private TextView tvTimeService;
	private RecyclerView RecylcLstHamyar;
	private Button tvCanselService;
	private Button tvAcceptAndSelectHamyar;
	private String StartDate;
	private String StartTime;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	private String CodeHamyarRequest;


	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.slide_menu_select_hamyar);
	//****************************************************************
	Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar_about);
	mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

	mNavi = (NavigationView) findViewById(R.id.navigation_view);
	View header_View = mNavi.getHeaderView(0);
	imgBackToggle = (ImageView) findViewById(R.id.imgBackToggle);
	imgBackToggle.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	});
	btnLogout = (Button) header_View.findViewById(R.id.btnLogout);
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
	tvContentTypeService=(TextView)findViewById(R.id.tvContentTypeService);
	tvContentCodeService=(TextView)findViewById(R.id.tvContentCodeService);
	tvContentAddress=(TextView)findViewById(R.id.tvContentAddress);
	tvDateService=(TextView)findViewById(R.id.tvDateService);
	tvTimeService=(TextView)findViewById(R.id.tvTimeService);
	RecylcLstHamyar=(RecyclerView)findViewById(R.id.RecylcLstHamyar);
	tvCanselService=(Button)findViewById(R.id.tvCanselService);
	tvAcceptAndSelectHamyar=(Button)findViewById(R.id.tvAcceptAndSelectHamyar);
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
	try {
		karbarCode = getIntent().getStringExtra("karbarCode").toString();

	} catch (Exception e) {
		db=dbh.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM login", null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			karbarCode = cursor.getString(cursor.getColumnIndex("karbarCode"));
		}
	}
	try
	{
		OrderCode=getIntent().getStringExtra("OrderCode").toString();
	}
	catch (Exception ex)
	{
		OrderCode="0";
	}
	db=dbh.getReadableDatabase();
	Cursor coursors;
	coursors = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name,address.AddressText FROM OrdersService  " +
			"LEFT JOIN Servicesdetails " +
			"ON Servicesdetails.code=OrdersService.ServiceDetaileCode " +
			"LEFT JOIN address " +
			"ON OrdersService.AddressCode=address.Code " +
			"WHERE OrdersService.Code_OrdersService ='"+OrderCode+"'", null);
	if(coursors.getCount()>0)
	{
		coursors.moveToNext();
		tvContentCodeService.setText(coursors.getString(coursors.getColumnIndex("OrdersService.Code_OrdersService")));
		tvContentTypeService.setText(coursors.getString(coursors.getColumnIndex("Servicesdetails.name")));
		tvContentAddress.setText(coursors.getString(coursors.getColumnIndex("address.AddressText")));
		StartDate=coursors.getString(coursors.getColumnIndex("StartYear"))+"/"+
			coursors.getString(coursors.getColumnIndex("StartMonth"))+"/"+
			coursors.getString(coursors.getColumnIndex("StartDay"));
		StartTime=coursors.getString(coursors.getColumnIndex("StartHour"))+":"+
				coursors.getString(coursors.getColumnIndex("StartMinute"));
		tvDateService.setText(StartDate);
		tvTimeService.setText(StartTime);
		String Query="SELECT A.HamyarCode,A.Code ReqCode,A.Price,B.Fname,B.img,B.HmayarStar,B.Lname" +
				" FROM UserServicesHamyarRequest A" +
				" LEFT JOIN " +
				" (select max(Code_InfoHamyar)Code_InfoHamyar, max(Fname)Fname,max(img)img,max(HmayarStar)HmayarStar,max(Lname)Lname from InfoHamyar group by Code_InfoHamyar) B ON " +
				" A.HamyarCode=B.Code_InfoHamyar" +
				" WHERE A.BsUserServicesCode='"+OrderCode+"'";
		PublicVariable.view_hamyar.clear();
		C=db.rawQuery(Query,null);
		for(int i=0;i<C.getCount();i++)
		{
			C.moveToNext();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("Code",C.getString(C.getColumnIndex("HamyarCode")));
			map.put("Name",C.getString(C.getColumnIndex("Fname")) + " " + C.getString(C.getColumnIndex("Lname")));
			map.put("imgHamyar",C.getString(C.getColumnIndex("img")));
			map.put("RateBar",C.getString(C.getColumnIndex("HmayarStar")));
			map.put("RateNumber",C.getString(C.getColumnIndex("HmayarStar")));
			map.put("UnreadCount",C.getString(C.getColumnIndex("Price")));
			map.put("CodeHamyarRequest",C.getString(C.getColumnIndex("ReqCode")));
			valuse.add(map);
		}
		db.close();
		C.close();
		coursors.close();
		AdapterInfoHamyar dataAdapter=new AdapterInfoHamyar(Select_Hamyar.this,valuse);
		RecylcLstHamyar.setAdapter(dataAdapter);
	}
	tvAcceptAndSelectHamyar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String ErrorStr="";
			db=dbh.getReadableDatabase();
			Cursor cursorhamyar_select = db.rawQuery("SELECT * FROM hamyar_select",null);
			if(cursorhamyar_select.getCount()>0)
			{
				cursorhamyar_select.moveToNext();
				CodeHamyarRequest=cursorhamyar_select.getString(cursorhamyar_select.getColumnIndex("Code"));
			}
			else
			{
				ErrorStr="پیشنهادی را انتخاب نکرده اید!";
			}
			db.close();
			if(ErrorStr.length()==0)
			{
				SyncInsertFromHamyarRequestToHamyarAccept syncInsertFromHamyarRequestToHamyarAccept = new SyncInsertFromHamyarRequestToHamyarAccept(Select_Hamyar.this,
						CodeHamyarRequest);
				syncInsertFromHamyarRequestToHamyarAccept.AsyncExecute();
			}
			else
			{
				Toast.makeText(Select_Hamyar.this, ErrorStr, Toast.LENGTH_SHORT).show();
			}
		}
	});
	tvCanselService.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LayoutInflater li = LayoutInflater.from(Select_Hamyar.this);
			View promptsView = li.inflate(R.layout.cansel, null);
			AlertDialog.Builder alertbox = new AlertDialog.Builder(Select_Hamyar.this);
			//set view
			alertbox.setView(promptsView);
			 Button btnTestService = (Button) promptsView.findViewById(R.id.btnTestService);
			 Button btnChangeTimeService = (Button) promptsView.findViewById(R.id.btnChangeTimeService);
			 Button btnFindBestPrice = (Button) promptsView.findViewById(R.id.btnFindBestPrice);
			 Button btnChangeTimeByHamyar = (Button) promptsView.findViewById(R.id.btnChangeTimeByHamyar);
			 Button btnHamyarDoNotAdherePrice = (Button) promptsView.findViewById(R.id.btnHamyarDoNotAdherePrice);
			 Button btnHamyarDonotGoodBehavior = (Button) promptsView.findViewById(R.id.btnHamyarDonotGoodBehavior);
			 Button btnCanselByQrderHamyar = (Button) promptsView.findViewById(R.id.btnCanselByQrderHamyar);
			 Button btnOtherCase = (Button) promptsView.findViewById(R.id.btnOtherCase);

			btnTestService.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"سفارشم آزمایشی بود");
					syncCanselServices.AsyncExecute();
				}
			});
			btnChangeTimeService.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"برنامه زمانی ام تغییر کرد");
					syncCanselServices.AsyncExecute();
				}
			});
			btnFindBestPrice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"قیمت مناسب تری پیدا کردم");
					syncCanselServices.AsyncExecute();
				}
			});
			btnChangeTimeByHamyar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"متخصص زمان خدمت را تغییر داد");
					syncCanselServices.AsyncExecute();
				}
			});
			btnHamyarDoNotAdherePrice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"متخصص به قیمت پایبند نبود");
					syncCanselServices.AsyncExecute();
				}
			});
			btnHamyarDonotGoodBehavior.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"متخصص رفتار مناسبی نداشت");
					syncCanselServices.AsyncExecute();
				}
			});
			btnCanselByQrderHamyar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"به درخواست متخصص لغو کردم");
					syncCanselServices.AsyncExecute();
				}
			});
			btnOtherCase.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SyncCanselServices syncCanselServices=new SyncCanselServices(Select_Hamyar.this,karbarCode,OrderCode,"دلیل دیگری دارید");
					syncCanselServices.AsyncExecute();
				}
			});


			// create alert dialog
			AlertDialog alertDialog = alertbox.create();

			// show it
			alertDialog.show();
		}
	});
}
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
								SyncProfile profile = new SyncProfile(Select_Hamyar.this, c.getString(c.getColumnIndex("karbarCode")));
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

						LoadActivity(Select_Hamyar.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
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
			LoadActivity(Paigiri.class, "karbarCode", karbarCode);
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
