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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
import com.aspino.it.karbar.Date.ChangeDate;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request1 extends AppCompatActivity {
	private String karbarCode;
	private String DetailCode;
	private TextView tvTitleService;
	private Button btnAddTimeJob;
	private Button btnDesTimeJob;
	//**************************************************************
	private EditText etFromDate;
//	private EditText etToDate;
//	private EditText etFromTime;
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
//	private Spinner spAddress ;
	private int posisionID=-1 ;
	private String ToDate;
	private String ToTime;
	//*********************************************
	private CheckBox chbMale;
	private CheckBox chbFemale;
	private CheckBox chbMaleAndFemale;
	private String MaleCount="0";
	private String FemaleCount="0";
	private String HamyarCount="0";
	//*********************************************

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_request1);
		//****************************************************************
		imgForward = (ImageView) findViewById(R.id.imgForward);

		//**************************************************************************************
		etFromDate = (EditText) findViewById(R.id.etFromDate);
//		etToDate = (EditText) findViewById(R.id.etToDate);
//		etFromTime = (EditText) findViewById(R.id.etFromTime);
//		etToTime = (EditText) findViewById(R.id.etToTime);
		etAddres = (EditText) findViewById(R.id.etAddres);
		etDescription = (EditText) findViewById(R.id.etDescription);
//		spAddress = (Spinner) findViewById(R.id.spAddress);
		tvTitleService=(TextView) findViewById(R.id.tvTitleService);
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
			etCountTimeJob.setText("0");
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
		db.close();
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
				if(ErrorStr.length()==0)
				{
					if(etFromDate.getText().toString().compareTo("0")!=0)
					{

						String DateGaregury= faToEn(ChangeDate.changeFarsiToMiladi(sp[0].toString().replace(" ",""))).replace("/","-");
//						String strClock,strDate;
						int intHour,intMin;
						spClock=sp[1].toString().split(":");
						intHour=Integer.parseInt(spClock[0].replace(" ",""));
						intMin=Integer.parseInt(spClock[1].replace(" ",""));
						if(intHour<10)
						{
							spClock[0]="0"+spClock[0];
						}
						if(intMin<10)
						{
							spClock[1]="0"+spClock[1];
						}
						db=dbh.getReadableDatabase();
						String query="SELECT DATETIME('"+DateGaregury + " " +spClock[0] +":"+spClock[1]+":00'"
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
					LoadActivity(Service_Request_SelectAddress.class, "karbarCode", karbarCode,
							"DetailCode", DetailCode,
							"FromDate", etFromDate.getText().toString(),
							"ToDate", ToDate,
							"FromTime", spClock[0] +":"+spClock[1],
							"ToTime", ToTime,
							"Description", etDescription.getText().toString(),
							"TimeDiff", etCountTimeJob.getText().toString(),
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
//									db=dbh.getWritableDatabase();
//									String query="UPDATE  DateTB SET Date = '" +String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth)+"'";
//									db.execSQL(query);
//
//									db.close();
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
//				db = dbh.getReadableDatabase();
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
				count=Integer.parseInt(etCountTimeJob.getText().toString())+1;
				etCountTimeJob.setText(String.valueOf(count));
			}
		});
		btnDesTimeJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				if(Integer.parseInt(etCountTimeJob.getText().toString())>0)
				{
					count = Integer.parseInt(etCountTimeJob.getText().toString()) - 1;
					etCountTimeJob.setText(String.valueOf(count));
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
//	public void FillSpinner(String tableName,String ColumnName,Spinner spinner){
//		List<String> labels = new ArrayList<String>();
//		db=dbh.getReadableDatabase();
//		String query="SELECT * FROM " + tableName +" WHERE Status='1' ORDER BY IsDefault DESC";
//		Cursor cursors = db.rawQuery(query,null);
//		String str;
//		for(int i=0;i<cursors.getCount();i++){
//			cursors.moveToNext();
//			str=cursors.getString(cursors.getColumnIndex(ColumnName));
//			if(AddressCode.compareTo(cursors.getString(cursors.getColumnIndex("Code")))==0)
//			{
//				posisionID=i;
//			}
//			labels.add(str);
//		}
//		db.close();
//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels){
//			public View getView(int position, View convertView, ViewGroup parent) {
//				View v = super.getView(position, convertView, parent);
//
//				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
//				((TextView) v).setTypeface(typeface);
//
//				return v;
//			}
//
//			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
//				View v =super.getDropDownView(position, convertView, parent);
//
//
//				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/BMitra.ttf");
//				((TextView) v).setTypeface(typeface);
//
//				return v;
//			}
//		};
//		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner.setAdapter(dataAdapter);
//	}
	public void GetTime()
	{
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);

		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(Service_Request1.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
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
				String DateStr =etFromDate.getText().toString();
				etFromDate.setText(DateStr+" - "+String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
			}
		}, hour, minute, true);
		mTimePicker.setTitle("انتخاب زمان");
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
}
