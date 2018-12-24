package  com.aspino.it.karbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.aspino.it.karbar.Date.ChangeDate;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import java.io.IOException;
import java.util.Calendar;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import static android.content.DialogInterface.*;

public class Service_Request1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private String karbarCode;
	private String DetailCode;
	private Button btnAddTimeJob;
	private Button btnDesTimeJob;
	//**************************************************************
	private EditText etFromDate;
//	private EditText etAddres;
	private EditText etDescription;
	private EditText etCountTimeJob;
	//**************************************************************
	private ImageView imgForward;
	private ImageView imgBack;
	//**************************************************************
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String CodeService;
	///*************************************
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
	private int posisionID=-1 ;
	private String ToDate;
	private String ToTime;
	//*********************************************
	private CheckBox chbMale;
	private CheckBox chbFemale;
	private CheckBox chbMaleAndFemale;
	private String MaleCount="1";
	private String FemaleCount="0";
	private String HamyarCount="0";
	//*********************************************
	private DrawerLayout mDrawer;
	private NavigationView mNavi;
	private Toolbar mtoolbar;
	private Button btnLogout;
	private ImageView imgClose;
	private ImageView imgMenu;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_menu_servicerequest1);
		//****************************************************************
		Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar);

		mtoolbar.setTitle("");
		setSupportActionBar(mtoolbar);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		mNavi = (NavigationView) findViewById(R.id.navigation_view);

		View header_View= mNavi.getHeaderView(0);
		imgClose=(ImageView)findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
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
//		ActionBarDrawerToggle aToggle = new ActionBarDrawerToggle(this, mDrawer, mtoolbar, R.string.open, R.string.close);
//		mDrawer.addDrawerListener(aToggle);
//		aToggle.syncState();
		//*****************************************************************
		//****************************************************************
		imgForward = (ImageView) findViewById(R.id.imgForward);
        imgBack = (ImageView) findViewById(R.id.imgBack);

		//**************************************************************************************
		etFromDate = (EditText) findViewById(R.id.etFromDate);
//		etToDate = (EditText) findViewById(R.id.etToDate);
//		etFromTime = (EditText) findViewById(R.id.etFromTime);
//		etToTime = (EditText) findViewById(R.id.etToTime);
//		etAddres = (EditText) findViewById(R.id.etAddres);
		etDescription = (EditText) findViewById(R.id.etDescription);
//		spAddress = (Spinner) findViewById(R.id.spAddress);
//		tvTitleService=(TextView) findViewById(R.id.tvTitleService);
		etCountTimeJob=(EditText)findViewById(R.id.etCountTimeJob);
		btnAddTimeJob=(Button)findViewById(R.id.btnAddTimeJob);
		btnDesTimeJob=(Button)findViewById(R.id.btnDesTimeJob);
		//*************************************************************************
		chbMale=(CheckBox) findViewById(R.id.chbMale);
		chbFemale=(CheckBox)findViewById(R.id.chbFemale);
		chbMaleAndFemale=(CheckBox)findViewById(R.id.chbMaleAndFemale);
		//******************************************************************
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
		try
		{

			etFromDate.setText(getIntent().getStringExtra("FromDate").toString());
			String splitStr[]=getIntent().getStringExtra("FromDate").toString().split("-");
			String splitDate[]=splitStr[0].toString().split("/");
			String splitTime[]=splitStr[1].toString().split(":");
			StartYear =splitDate[0];
			StartMonth =splitDate[1];
			StartDay =splitDate[2];
			StartHour =splitTime[0];
			StartMinute =splitTime[1];

		}
		catch (Exception ex)
		{
			StartYear ="0";
			StartMonth ="0";
			StartDay ="0";
		}
		try
		{
//			etToDate.setText(getIntent().getStringExtra("ToDate").toString());
			String splitStr[]=getIntent().getStringExtra("ToDate").toString().split("/");
			EndYear =splitStr[0];
			EndMonth =splitStr[1];
			EndDay =splitStr[2];
		}
		catch (Exception ex)
		{
			EndYear ="0";
			EndMonth ="0";
			EndDay ="0";
		}
//		try
//		{
//			etFromTime.setText(getIntent().getStringExtra("FromTime").toString());
//			String splitStr[]=getIntent().getStringExtra("FromTime").toString().split(":");
//			StartHour =splitStr[0];
//			StartMinute =splitStr[1];
//		}
//		catch (Exception ex)
//		{
//			StartHour ="0";
//			StartMinute ="0";
//		}
		try
		{
//			etToTime.setText(getIntent().getStringExtra("ToTime").toString());
			String splitStr[]=getIntent().getStringExtra("ToTime").toString().split(":");
			EndHour =splitStr[0];
			EndMinute =splitStr[1];
		}
		catch (Exception ex)
		{
			EndHour ="0";
			EndMinute ="0";
		}
		try
		{
			etDescription.setText(getIntent().getStringExtra("Description").toString());
		}
		catch (Exception ex)
		{
			Description="";
		}
		try
		{
			etCountTimeJob.setText(getIntent().getStringExtra("TimeDiff").toString());
		}
		catch (Exception ex)
		{
			etCountTimeJob.setText("3 ساعت");
		}
		try
		{
			AddressCode = getIntent().getStringExtra("AddressCode").toString();
		}
		catch (Exception ex)
		{
			AddressCode="";
		}
		try {
			DetailCode = getIntent().getStringExtra("DetailCode").toString();
		} catch (Exception ex) {
			DetailCode = "0";
		}
		try {
			karbarCode = getIntent().getStringExtra("karbarCode").toString();
		} catch (Exception e) {
			try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
			Cursor coursors = db.rawQuery("SELECT * FROM login", null);
			for (int i = 0; i < coursors.getCount(); i++) {
				coursors.moveToNext();

				karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
			}
			if(db.isOpen()) {
				db.close();
			}
		}
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
//			tvTitleService.setText(":"+coursors.getString(coursors.getColumnIndex("name")));
		}
		else
		{
			Toast.makeText(getBaseContext(), "نوع فرم ثبت نشده", Toast.LENGTH_LONG).show();
		}
		if(db.isOpen()) {
			db.close();
		}
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity2(List_ServiceDerails.class, "karbarCode", karbarCode,"codeService",CodeService);
            }
        });
		imgForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr="";
//				try {
//					if (etAddres.getTag().toString().length() <= 0) {
//						ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
//					} else {
//						AddressCode = etAddres.getTag().toString();
//					}
//				}
//				catch (Exception ex)
//				{
//					ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
//				}
				if(etFromDate.length()==0)
				{
					ErrorStr+="تاریخ را وارد نمایید"+"\n";
				}
//				if(etToDate.length()==0)
//				{
//					ErrorStr+="تاریخ خاتمه را وارد نمایید"+"\n";
//				}
//				if(etFromTime.length()==0)
//				{
//					ErrorStr+="ساعت را وارد نمایید"+"\n";
//				}
//				if(etToTime.length()==0)
//				{
//					ErrorStr+="ساعت خاتمه را وارد نمایید"+"\n";
//				}
//				if(etFromDate.getText().toString().compareTo(etToDate.getText().toString())>0)
//				{
//					ErrorStr+="تاریخ شروع نمی تواند بزرگتر از تاریخ خاتمه باشد."+"\n";
////				}
//				if(etFromDate.length()<8 && etFromDate.length()>10)
//				{
//					ErrorStr+="تاریخ شروع را صحیح وارد نمایید"+"\n";
//				}
//				if (etCountTimeJob.getText().toString().compareTo("0")==0) {
//
//					ErrorStr += "زمان مورد نیاز سرویس را مشخص نمایید" + "\n";
//
//				}
//				if(etToDate.length()<8 && etToDate.length()>10)
//				{
//					ErrorStr+="تاریخ خاتمه را صحیح وارد نمایید"+"\n";
//				}
//				if(etFromTime.length()<3 && etFromTime.length()>5)
//				{
//					ErrorStr+="زمان شروع را صحیح وارد نمایید"+"\n";
//				}
				if(etCountTimeJob.getText().toString().compareTo("0")==0)
				{
					ErrorStr+="مدت زمان کار مورد نیاز را وارد نمایید"+"\n";
				}
				if(MaleCount.compareTo("0")==0 && FemaleCount.compareTo("0")==0 && HamyarCount.compareTo("0")==0)
				{
					ErrorStr+="جنسیت متخصص را وارد نمایید"+"\n";
				}

				Description =etDescription.getText().toString();
				if(Description.length()==0)
				{
					ErrorStr+="توضیحات را وارد نمایید"+"\n";
				}
				String sp[]=etFromDate.getText().toString().split("-");
				String spClock[]=null;
                String countTime="";
				if(ErrorStr.length()==0)
				{
					if(etFromDate.getText().toString().compareTo("0")!=0)
					{

						String DateGaregury=faToEn(ChangeDate.changeFarsiToMiladi(sp[1].toString().replace(" ",""))).replace("/","-");
						String DateGSP[];
						db=dbh.getReadableDatabase();
						String query="SELECT DATETIME('"+DateGaregury + " " +sp[0].toString().replace(" ","")+":00'"
								+",'+" + "1 day') as Date";
						Cursor cursor_convert=db.rawQuery(query,null);
						if(cursor_convert.getCount()>0)
						{
							cursor_convert.moveToNext();
							DateGSP=cursor_convert.getString(cursor_convert.getColumnIndex("Date")).split(" ");
							DateGaregury=DateGSP[0];
						}
						cursor_convert.close();
						if(db.isOpen()) {
							db.close();
						}
						spClock=sp[0].toString().split(":");
						spClock[0]=spClock[0].replace(" ","");
						spClock[1]=spClock[1].replace(" ","");
//						intHour=Integer.parseInt(spClock[0]);
//						intMin=Integer.parseInt(spClock[1]);
//						if(intHour<10)
//						{
//							spClock[0]="0"+spClock[0];
//						}
//						if(intMin<10)
//						{
//							spClock[1]="0"+spClock[1];
//						}
						db=dbh.getReadableDatabase();
						countTime=etCountTimeJob.getText().toString().replace(" ساعت","");
						query="SELECT DATETIME('"+DateGaregury + " " +spClock[0] +":"+spClock[1]+":00'"
								+",'+"+countTime+" hours') as Date";
						Cursor cursor=db.rawQuery(query,null);
						if(cursor.getCount()>0)
						{
							cursor.moveToNext();
							String DateFinal=faToEn(cursor.getString(cursor.getColumnIndex("Date")).replace("-","/"));
							String SpaceSlit[]=DateFinal.split(" ");
							SpaceSlit[0]=faToEn(ChangeDate.changeMiladiToFarsi(SpaceSlit[0]));
							ToDate=SpaceSlit[0];
							ToTime=SpaceSlit[1];
						}
						if(db.isOpen()) {
							db.close();
						}
					}
					LoadActivity(Service_Request_SelectAddress.class, "karbarCode", karbarCode,
							"DetailCode", DetailCode,
							"FromDate", faToEn(etFromDate.getText().toString()),
							"ToDate", ToDate,
							"FromTime", spClock[0] +":"+spClock[1],
							"ToTime", ToTime,
							"Description", etDescription.getText().toString(),
							"TimeDiff", countTime,
							"MaleCount", MaleCount,
							"FemaleCount", FemaleCount,
							"HamyarCount", HamyarCount
					);
				}
				else
				{
					Toast.makeText(Service_Request1.this, ErrorStr, Toast.LENGTH_SHORT).show();
				}

			}
		});
		etFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request1.this);
					picker.setPositiveButtonString("تایید");
					picker.setNegativeButton("انصراف");
					picker.setTodayButton("امروز");
					picker.setTodayButtonVisible(true);
					//  picker.setInitDate(initDate);
					picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
					picker.setMinYear(1300);
					picker.setActionTextColor(Color.GRAY);
					//picker.setTypeFace(FontMitra);
					picker.setListener(new Listener() {

						@Override
						public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
							//Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
							StartYear=String.valueOf(persianCalendar.getPersianYear());
							if(persianCalendar.getPersianMonth()<10)
							{
								StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
							}
							else
							{
								StartMonth=String.valueOf(persianCalendar.getPersianMonth());
							}
							if(persianCalendar.getPersianDay()<10)
							{
								StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
							}
							else
							{
								StartDay=String.valueOf(persianCalendar.getPersianDay());
							}

							etFromDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));
							try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}}	catch (Exception ex){	db=dbh.getWritableDatabase();	}
							String query="UPDATE  DateTB SET Date = '" +StartYear+"/"+StartMonth+"/"+StartDay+"'";
							db.execSQL(query);if(db.isOpen()){db.close();}

							if(db.isOpen()) {
								db.close();
							}
							GetTime();
						}

						@Override
						public void onDismissed() {

						}
					});
					picker.show();
//					PersianCalendar now = new PersianCalendar();

//					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//							new DatePickerDialog.OnDateSetListener() {
//								@Override
//								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//									String Mon;
//									String DayStr;
//									if((monthOfYear+1)<10)
//									{
//										Mon="0"+String.valueOf(monthOfYear+1);
//									}
//									else
//									{
//										Mon=String.valueOf(monthOfYear+1);
//									}
//									if(dayOfMonth<10)
//									{
//										DayStr="0"+String.valueOf(dayOfMonth);
//									}
//									else
//									{
//										DayStr=String.valueOf(dayOfMonth);
//									}
//									etFromDate.setText(String.valueOf(year) + "/" + Mon + "/" + DayStr);
////									try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}}	catch (Exception ex){	db=dbh.getWritableDatabase();	}
////									String query="UPDATE  DateTB SET Date = '" +String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth)+"'";
////									db.execSQL(query);if(db.isOpen()){db.close();}
////
////									db.close();
//									GetTime();
//								}
//							}, now.getPersianYear(),
//							now.getPersianMonth(),
//							now.getPersianDay());
//
//					datePickerDialog.setThemeDark(true);
//					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(Service_Request1.this);
				picker.setPositiveButtonString("تایید");
				picker.setNegativeButton("انصراف");
				picker.setTodayButton("امروز");
				picker.setTodayButtonVisible(true);
				//  picker.setInitDate(initDate);
				picker.setMaxYear(PersianDatePickerDialog.THIS_YEAR);
				picker.setMinYear(1300);
				picker.setActionTextColor(Color.GRAY);
				//picker.setTypeFace(FontMitra);
				picker.setListener(new Listener() {

					@Override
					public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
						//Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
						StartYear=String.valueOf(persianCalendar.getPersianYear());
						if(persianCalendar.getPersianMonth()<10)
						{
							StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
						}
						else
						{
							StartMonth=String.valueOf(persianCalendar.getPersianMonth());
						}
						if(persianCalendar.getPersianDay()<10)
						{
							StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
						}
						else
						{
							StartDay=String.valueOf(persianCalendar.getPersianDay());
						}

						etFromDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));
						try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}}	catch (Exception ex){	db=dbh.getWritableDatabase();	}
						String query="UPDATE  DateTB SET Date = '" +StartYear+"/"+StartMonth+"/"+StartDay+"'";
						db.execSQL(query);if(db.isOpen()){db.close();}

						if(db.isOpen()) {
							db.close();
						}
						GetTime();
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();

			}
		});
		//******************************************************************
		chbMale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chbMale.isChecked())
				{
					chbFemale.setChecked(false);
					chbMaleAndFemale.setChecked(false);
					FemaleCount="0";
					HamyarCount="0";
					MaleCount="1";
				}
				else
				{
					chbMale.setChecked(true);
					MaleCount="1";
				}
			}
		});
		chbFemale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chbFemale.isChecked())
				{
					chbMale.setChecked(false);
					chbMaleAndFemale.setChecked(false);
					MaleCount="0";
					HamyarCount="0";
					FemaleCount="1";
				}
				else
				{
					chbFemale.setChecked(true);
					FemaleCount="1";
				}
			}
		});
		chbMaleAndFemale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chbMaleAndFemale.isChecked())
				{
					chbMale.setChecked(false);
					chbFemale.setChecked(false);
					MaleCount="0";
					FemaleCount="0";
					HamyarCount="1";
				}
				else
				{
					chbMaleAndFemale.setChecked(true);
					HamyarCount="1";
				}
			}
		});
//**************************************************************************************
//		etToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					PersianCalendar now = new PersianCalendar();
//					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//							new DatePickerDialog.OnDateSetListener() {
//								@Override
//								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//									etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
//								}
//							}, now.getPersianYear(),
//							now.getPersianMonth(),
//							now.getPersianDay());
//					datePickerDialog.setThemeDark(true);
//					datePickerDialog.show(getFragmentManager(), "tpd");
//				}
//			}
//		});
//		etToDate.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				PersianCalendar now = new PersianCalendar();
//				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//						new DatePickerDialog.OnDateSetListener() {
//							@Override
//							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//								etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
//							}
//						}, now.getPersianYear(),
//						now.getPersianMonth(),
//						now.getPersianDay());
//				datePickerDialog.setThemeDark(true);
//				datePickerDialog.show(getFragmentManager(), "tpd");
//			}
//		});
//		etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					Calendar mcurrentTime = Calendar.getInstance();
//					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//					int minute = mcurrentTime.get(Calendar.MINUTE);
//
//					TimePickerDialog mTimePicker;
//					mTimePicker = new TimePickerDialog(Service_Request1.this,  TimePickerDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
//						@Override
//						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//							String AM_PM;
//							if (selectedHour >= 0 && selectedHour < 12) {
//								AM_PM = "AM";
//							} else {
//								AM_PM = "PM";
//							}
//							etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//						}
//					}, hour, minute, true);
//					mTimePicker.setTitle("انتخاب زمان");
//					mTimePicker.show();
//				}
//			}
//		});
//		etFromTime.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Calendar mcurrentTime = Calendar.getInstance();
//				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//				int minute = mcurrentTime.get(Calendar.MINUTE);
//
//				TimePickerDialog mTimePicker;
//				mTimePicker = new TimePickerDialog(Service_Request1.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//					@Override
//					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//						String AM_PM;
//						if (selectedHour >= 0 && selectedHour < 12) {
//							AM_PM = "AM";
//						} else {
//							AM_PM = "PM";
//						}
//						etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//					}
//				}, hour, minute, true);
//				mTimePicker.setTitle("انتخاب زمان");
//				mTimePicker.show();
//			}
//		});
//		etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					Calendar mcurrentTime = Calendar.getInstance();
//					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//					int minute = mcurrentTime.get(Calendar.MINUTE);
//
//					TimePickerDialog mTimePicker;
//					mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
//						@Override
//						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//							String AM_PM;
//							if (selectedHour >= 0 && selectedHour < 12) {
//								AM_PM = "AM";
//							} else {
//								AM_PM = "PM";
//							}
//							etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//						}
//					}, hour, minute, false);
//					mTimePicker.setTitle("انتخاب زمان خاتمه");
//					mTimePicker.show();
//				}
//			}
//		});
//		etToTime.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Calendar mcurrentTime = Calendar.getInstance();
//				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//				int minute = mcurrentTime.get(Calendar.MINUTE);
//
//				TimePickerDialog mTimePicker;
//				mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
//					@Override
//					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//						String AM_PM;
//						if (selectedHour >= 0 && selectedHour < 12) {
//							AM_PM = "AM";
//						} else {
//							AM_PM = "PM";
//						}
//						etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
//					}
//				}, hour, minute, false);
//				mTimePicker.setTitle("انتخاب زمان خاتمه");
//				mTimePicker.show();
//			}
//		});
//		FillSpinner("address", "Name", spAddress);
//		if(posisionID>0)
//		{
//			spAddress.setSelection(posisionID);
//		}

//		spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//				Cursor cursor = db.rawQuery("SELECT * FROM address WHERE Name='" + spAddress.getItemAtPosition(position).toString() + "'", null);
//				if (cursor.getCount() > 0) {
//					cursor.moveToNext();
//					etAddres.setText(cursor.getString(cursor.getColumnIndex("AddressText")));
//					etAddres.setTag(cursor.getString(cursor.getColumnIndex("Code")));
//
//				}
//				db.close();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
		//**************************************************************************************
		btnAddTimeJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				count=Integer.parseInt(etCountTimeJob.getText().toString().replace(" ساعت",""))+1;
				etCountTimeJob.setText(String.valueOf(count)+" ساعت");
			}
		});
		btnDesTimeJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				if(Integer.parseInt(etCountTimeJob.getText().toString().replace(" ساعت",""))>1)
				{
					count = Integer.parseInt(etCountTimeJob.getText().toString().replace(" ساعت","")) - 1;
					etCountTimeJob.setText(String.valueOf(count)+" ساعت");
				}
			}
		});

//**************************************************************************************
	}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {

    	LoadActivity2(List_ServiceDerails.class, "karbarCode", karbarCode,"codeService",CodeService);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName1, String VariableValue1,
						 String VariableName2, String VariableValue2,
						 String VariableName3, String VariableValue3,
						 String VariableName4, String VariableValue4,
						 String VariableName5, String VariableValue5,
						 String VariableName6, String VariableValue6,
						 String VariableName7, String VariableValue7,
						 String VariableName8, String VariableValue8,
						 String VariableName9, String VariableValue9,
						 String VariableName10, String VariableValue10,
						 String VariableName11, String VariableValue11)
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
		intent.putExtra(VariableName10, VariableValue10);
		intent.putExtra(VariableName11, VariableValue11);
		Service_Request1.this.startActivity(intent);
	}
public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		startActivity(intent);
	}
	public void GetTime()
	{
		Calendar now = Calendar.getInstance();
		MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
				// TODO Auto-generated method stub
				/*time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
						":" + String.format("%02d", minute) +
						":" + String.format("%02d", seconds));	*/
				try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}}	catch (Exception ex){	db=dbh.getWritableDatabase();	}
				String query="UPDATE  DateTB SET Time = '" +String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"'";
				db.execSQL(query);if(db.isOpen()){db.close();}
				String DateStr =etFromDate.getText().toString();
				String  hour,min;
				if(hourOfDay<10)
                {
                    hour="0"+String.valueOf(hourOfDay);
                }
                else
                {
                    hour=String.valueOf(hourOfDay);
                }
				if(minute<10)
                {
                    min="0"+String.valueOf(minute);
                }
                else
                {
                    min=String.valueOf(minute);
                }
				etFromDate.setText(hour + ":" + min + " - " + DateStr);
			}
		}, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
		mTimePicker.setTitle("انتخاب زمان");
		mTimePicker.setButton(BUTTON_POSITIVE,"تایید",mTimePicker);
		mTimePicker.setButton(BUTTON_NEGATIVE,"انصراف",mTimePicker);
		mTimePicker.show();

	}
	public static String faToEn(String num) {
		return num
				.replace("۰", "0")
				.replace("۱", "1")
				.replace("۲", "2")
				.replace("۳", "3")
				.replace("۴", "4")
				.replace("۵", "5")
				.replace("۶", "6")
				.replace("۷", "7")
				.replace("۸", "8")
				.replace("۹", "9");
	}
	public static String EnToFa(String num) {
		return num
				.replace("0", "۰")
				.replace("1", "۱")
				.replace("2", "۲")
				.replace("3", "۳")
				.replace("4", "۴")
				.replace("5", "۵")
				.replace("6", "۶")
				.replace("7", "۷")
				.replace("8", "۸")
				.replace("9", "۹");
	}
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {

		int mId = item.getItemId();

		switch (mId) {

			case R.id.profile:
				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
				Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
				if (coursors.getCount() > 0) {
					coursors.moveToNext();
					String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
					if (Status_check.compareTo("0") == 0) {
						Cursor c = db.rawQuery("SELECT * FROM login", null);
						if (c.getCount() > 0) {
							c.moveToNext();
							SyncProfile profile = new SyncProfile(Service_Request1.this, c.getString(c.getColumnIndex("karbarCode")));
							profile.AsyncExecute();
						}
					} else {
						LoadActivity2(Profile.class, "karbarCode", karbarCode,"","");
					}
				}
				else {
					LoadActivity2(Login.class,"karbarCode","0","","");
				}
				if(db.isOpen()) {
					db.close();
				}
				break;

			case R.id.wallet:
				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
				Cursor c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					LoadActivity2(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")),"","");
				}
				else {
					LoadActivity2(Login.class,"karbarCode","0","","");
				}
				if(db.isOpen()) {
					db.close();
				}
				break;
			case R.id.Order:
				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
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
				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
				c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();
					LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
				}
				if(db.isOpen()) {
					db.close();
				}
				break;

			case R.id.Invite_friends:
				sharecode("0");
				break;

			case R.id.About:
				try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
				c = db.rawQuery("SELECT * FROM login", null);
				if (c.getCount() > 0) {
					c.moveToNext();

					LoadActivity2(About.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")),"","");
				}
				if(db.isOpen()) {
					db.close();
				}
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
			LoadActivity2(MainMenu.class, "karbarCode", karbarCode,"","");
		}

	}
	public void Logout() {
		//Exit All Activity And Kill Application
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		// set the message to display
		alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

		// set a negative/no button and create a listener
		alertbox.setPositiveButton("خیر", new OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});

		// set a positive/yes button and create a listener
		alertbox.setNegativeButton("بله", new OnClickListener() {
			// do something when the button is clicked
			public void onClick(DialogInterface arg0, int arg1) {
				//Declare Object From Get Internet Connection Status For Check Internet Status
				//stopService(new Intent(getBaseContext(), ServiceGetLocation.class));                stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));				stopService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));

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
				if(db.isOpen()) {
					db.close();
				}
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
