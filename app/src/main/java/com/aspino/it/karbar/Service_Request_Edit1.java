package com.aspino.it.karbar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aspino.it.karbar.Date.ChangeDate;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

public class Service_Request_Edit1 extends AppCompatActivity {
	private String karbarCode;
	private String DetailCode;
	private String CodeOrderService;
	private String ToDate;
	private String ToTime;
	private TextView tvTitleService;
	//**************************************************************
	private EditText etFromDate;
//	private EditText etToDate;
	private EditText etFromTime;
//	private EditText etToTime;
	private EditText etAddres;
	private EditText etDescription;
	private EditText etCountTimeJob;
	//**************************************************************
	private ImageView imgForward;
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
	private Spinner spAddress ;
	private Button btnAddTimeJob;
	private Button btnDesTimeJob;
	private int posisionID=-1 ;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_request_edit1);
		//****************************************************************
		imgForward = (ImageView) findViewById(R.id.imgForward);

		//**************************************************************************************
		etFromDate = (EditText) findViewById(R.id.etFromDate);
//		etToDate = (EditText) findViewById(R.id.etToDate);
		etFromTime = (EditText) findViewById(R.id.etFromTime);
//		etToTime = (EditText) findViewById(R.id.etToTime);
		etAddres = (EditText) findViewById(R.id.etAddres);
		etDescription = (EditText) findViewById(R.id.etDescription);
		spAddress = (Spinner) findViewById(R.id.spAddress);
		tvTitleService=(TextView) findViewById(R.id.tvTitleService);
		etCountTimeJob = (EditText) findViewById(R.id.etCountTimeJob);
		btnAddTimeJob = (Button) findViewById(R.id.btnAddTimeJob);
		btnDesTimeJob = (Button) findViewById(R.id.btnDesTimeJob);
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
			CodeOrderService = getIntent().getStringExtra("CodeOrderService").toString();
		}
		catch (Exception ex)
		{
			CodeOrderService="0";
		}
		try
		{

			etFromDate.setText(getIntent().getStringExtra("FromDate").toString());
			String splitStr[]=getIntent().getStringExtra("FromDate").toString().split("/");
			StartYear =splitStr[0];
			StartMonth =splitStr[1];
			StartDay =splitStr[2];

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
		try
		{
//			etFromTime.setText(getIntent().getStringExtra("FromTime").toString());
			String splitStr[]=getIntent().getStringExtra("FromTime").toString().split(":");
			StartHour =splitStr[0];
			StartMinute =splitStr[1];
		}
		catch (Exception ex)
		{
			StartHour ="0";
			StartMinute ="0";
		}
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
			db = dbh.getReadableDatabase();
			Cursor coursors = db.rawQuery("SELECT * FROM login", null);
			for (int i = 0; i < coursors.getCount(); i++) {
				coursors.moveToNext();

				karbarCode = coursors.getString(coursors.getColumnIndex("karbarCode"));
			}
			db.close();
		}
		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
			tvTitleService.setText(":"+coursors.getString(coursors.getColumnIndex("name")));
		}
		else
		{
			Toast.makeText(getBaseContext(), "نوع فرم ثبت نشده", Toast.LENGTH_LONG).show();
		}
		try {
			db.close();



			//******************************************
			FillForm();
			//********************************************




		} catch (Exception e) {
			e.printStackTrace();
		}
		imgForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr="";
				try {
					if (etAddres.getTag().toString().length() <= 0) {
						ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
					} else {
						AddressCode = etAddres.getTag().toString();
					}
				}
				catch (Exception ex)
				{
					ErrorStr += "آدرس را در قسمت تنظیمات حساب کاربری وارد نمایید." + "\n";
				}
				if(etFromDate.length()==0)
				{
					ErrorStr+="تاریخ را وارد نمایید"+"\n";
				}
				if(etFromTime.length()==0)
				{
					ErrorStr+="ساعت را وارد نمایید"+"\n";
				}
				if(etFromDate.length()<8 && etFromDate.length()>10)
				{
					ErrorStr+="تاریخ شروع را صحیح وارد نمایید"+"\n";
				}
				if (etCountTimeJob.getText().toString().compareTo("0") == 0) {

					ErrorStr += "زمان مورد نیاز سرویس را مشخص نمایید" + "\n";

				}
				Description =etDescription.getText().toString();
				if(ErrorStr.length()==0)
				{
					if(etFromDate.getText().toString().compareTo("0")!=0 && etFromTime.getText().toString().compareTo("0")!=0)
					{

						String DateGaregury= faToEn(ChangeDate.changeFarsiToMiladi(etFromDate.getText().toString())).replace("/","-");
						String strHour,strMin;
						int intHour,intMin;
						String sp[]=etFromTime.getText().toString().split(":");
						strHour=sp[0];
						strMin=sp[1];
						intHour=Integer.parseInt(strHour);
						intMin=Integer.parseInt(strMin);
						if(intHour<10)
						{
							strHour="0"+strHour;
						}
						if(intMin<10)
						{
							strMin="0"+strMin;
						}
						db=dbh.getReadableDatabase();
						String query="SELECT DATETIME('"+DateGaregury + " " +strHour +":"+strMin+":00'"
								+",'+"+etCountTimeJob.getText().toString()+" hours') as Date";
						Cursor cursor=db.rawQuery(query,null);
						if(cursor.getCount()>0)
						{
							cursor.moveToNext();
							String DateFinal=cursor.getString(cursor.getColumnIndex("Date")).replace("-","/");
							String SpaceSlit[]=DateFinal.split(" ");
							SpaceSlit[0]=faToEn(ChangeDate.changeMiladiToFarsi(SpaceSlit[0]));
							ToDate=SpaceSlit[0];
							ToTime=SpaceSlit[1];
						}
						db.close();
					}
					LoadActivity(Service_Request_Edit2.class, "karbarCode", karbarCode,
							"DetailCode", DetailCode,
							"FromDate", etFromDate.getText().toString(),
							"ToDate", ToDate,
							"FromTime", etFromTime.getText().toString(),
							"ToTime", ToTime,
							"Description", etDescription.getText().toString(),
							"TimeDiff", etCountTimeJob.getText().toString(),
							"CodeOrderService", CodeOrderService,
							"AddressCode", etAddres.getTag().toString()
					);
				}
				else
				{
					Toast.makeText(Service_Request_Edit1.this, ErrorStr, Toast.LENGTH_SHORT).show();
				}

			}
		});
		etFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					PersianCalendar now = new PersianCalendar();

					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									String Mon;
									String DayStr;
									if((monthOfYear+1)<10)
									{
										Mon="0"+String.valueOf(monthOfYear+1);
									}
									else
									{
										Mon=String.valueOf(monthOfYear+1);
									}
									if(dayOfMonth<10)
									{
										DayStr="0"+String.valueOf(dayOfMonth);
									}
									else
									{
										DayStr=String.valueOf(dayOfMonth);
									}
									etFromDate.setText(String.valueOf(year) + "/" + Mon + "/" + DayStr);
//
									GetTime();
								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());

					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								etFromDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
								GetTime();
							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");

			}
		});
		etFromTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Calendar mcurrentTime = Calendar.getInstance();
					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
					int minute = mcurrentTime.get(Calendar.MINUTE);

					TimePickerDialog mTimePicker;
					mTimePicker = new TimePickerDialog(Service_Request_Edit1.this, new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
							String AM_PM;
							if (selectedHour >= 0 && selectedHour < 12) {
								AM_PM = "AM";
							} else {
								AM_PM = "PM";
							}
							etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
						}
					}, hour, minute, false);
					mTimePicker.setTitle("انتخاب زمان شروع");
					mTimePicker.show();
				}
			}
		});
		etFromTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(Service_Request_Edit1.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						String AM_PM;
						if (selectedHour >= 0 && selectedHour < 12) {
							AM_PM = "AM";
						} else {
							AM_PM = "PM";
						}
						etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
					}
				}, hour, minute, false);
				mTimePicker.setTitle("انتخاب زمان");
				mTimePicker.show();
			}
		});

//**************************************************************************************
		btnAddTimeJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				count = Integer.parseInt(etCountTimeJob.getText().toString()) + 1;
				etCountTimeJob.setText(String.valueOf(count));
			}
		});
		btnDesTimeJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				if (Integer.parseInt(etCountTimeJob.getText().toString()) > 0) {
					count = Integer.parseInt(etCountTimeJob.getText().toString()) - 1;
					etCountTimeJob.setText(String.valueOf(count));
				}
			}
		});

//**************************************************************************************
		FillSpinner("address", "Name", spAddress);
		if(posisionID>0)
		{
			spAddress.setSelection(posisionID);
		}

		spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				db = dbh.getReadableDatabase();
				Cursor cursor = db.rawQuery("SELECT * FROM address WHERE Name='" + spAddress.getItemAtPosition(position).toString() + "'", null);
				if (cursor.getCount() > 0) {
					cursor.moveToNext();
					etAddres.setText(cursor.getString(cursor.getColumnIndex("AddressText")));
					etAddres.setTag(cursor.getString(cursor.getColumnIndex("Code")));

				}
				db.close();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {

    	LoadActivity2(Service_Request_Saved.class, "karbarCode", karbarCode,"OrderCode",CodeOrderService);
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
						 String VariableName10, String VariableValue10)
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
		Service_Request_Edit1.this.startActivity(intent);
	}
public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);
		startActivity(intent);
	}
	public void FillSpinner(String tableName,String ColumnName,Spinner spinner){
		List<String> labels = new ArrayList<String>();
		db=dbh.getReadableDatabase();
		String query="SELECT * FROM " + tableName ;
		Cursor cursors = db.rawQuery(query,null);
		String str;
		for(int i=0;i<cursors.getCount();i++){
			cursors.moveToNext();
			str=cursors.getString(cursors.getColumnIndex(ColumnName));
			if(AddressCode.compareTo(cursors.getString(cursors.getColumnIndex("Code")))==0)
			{
				posisionID=i;
			}
			labels.add(str);
		}
		db.close();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}

			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
				View v =super.getDropDownView(position, convertView, parent);


				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}
		};
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	public void GetTime()
	{
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);

		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(Service_Request_Edit1.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				String AM_PM;
				if (selectedHour >=0 && selectedHour < 12){
					AM_PM = "AM";
				} else {
					AM_PM = "PM";
				}
//				db=dbh.getWritableDatabase();
//				String query="UPDATE  DateTB SET Time = '" +String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute)+"'";
//				db.execSQL(query);
				etFromTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
			}
		}, hour, minute, false);
		mTimePicker.setTitle("انتخاب زمان");
		mTimePicker.show();

	}
	public void FillForm()
	{
		db = dbh.getReadableDatabase();
		String query="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
				"LEFT JOIN " +
				"Servicesdetails ON " +
				"Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE OrdersService.Code="+CodeOrderService;
		Cursor cursor = db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			cursor.moveToNext();
			if(cursor.getString(cursor.getColumnIndex("StartYear")).compareTo("")!=0 ||
					cursor.getString(cursor.getColumnIndex("StartYear")).compareTo("null")!=0) {
				etFromDate.setText(cursor.getString(cursor.getColumnIndex("StartYear"))+
						"/"+cursor.getString(cursor.getColumnIndex("StartMonth"))+
						"/"+cursor.getString(cursor.getColumnIndex("StartDay")));
			}
			if(cursor.getString(cursor.getColumnIndex("StartHour")).compareTo("")!=0 ||
					cursor.getString(cursor.getColumnIndex("StartHour")).compareTo("null")!=0) {
				etFromTime.setText(cursor.getString(cursor.getColumnIndex("StartHour"))+
						":"+cursor.getString(cursor.getColumnIndex("StartMinute")));
			}
			if(cursor.getString(cursor.getColumnIndex("Description")).length()!=0) {
				etDescription.setText(cursor.getString(cursor.getColumnIndex("Description")));
			}
			if(cursor.getString(cursor.getColumnIndex("DateDiff")).length()!=0) {
				etCountTimeJob.setText(cursor.getString(cursor.getColumnIndex("DateDiff")));
			}

			//*********************Address**********************************************
			int lensp;
			int positionString=0;
			db=dbh.getReadableDatabase();
			query="SELECT OrdersService.*,address.* FROM OrdersService " +
					"LEFT JOIN " +
					"address ON " +
					"address.code=OrdersService.AddressCode WHERE OrdersService.Code="+CodeOrderService;
			Cursor cursorAddress = db.rawQuery(query,null);
			if(cursorAddress.getCount()>0)
			{
				cursorAddress.moveToNext();
				lensp=spAddress.getCount();
				positionString=0;
				for(int i=0;i<lensp;i++){
					if(cursorAddress.getString(cursorAddress.getColumnIndex("Name")).compareTo(spAddress.getItemAtPosition(i).toString())==0)
					{
						positionString=i;
						break;
					}
				}
				spAddress.setSelection(positionString);
			}

		}
		else
		{
			Toast.makeText(this, "سرویس پیدا نشد! ", Toast.LENGTH_LONG).show();
		}
		db.close();
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
}
