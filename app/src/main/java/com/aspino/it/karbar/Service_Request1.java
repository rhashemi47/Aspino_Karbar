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
	//**************************************************************
	private EditText etFromDate;
	private EditText etToDate;
	private EditText etFromTime;
	private EditText etToTime;
	private EditText etAddres;
	private EditText etDescription;
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
	private int posisionID=-1 ;
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
		etToDate = (EditText) findViewById(R.id.etToDate);
		etFromTime = (EditText) findViewById(R.id.etFromTime);
		etToTime = (EditText) findViewById(R.id.etToTime);
		etAddres = (EditText) findViewById(R.id.etAddres);
		etDescription = (EditText) findViewById(R.id.etDescription);
		spAddress = (Spinner) findViewById(R.id.spAddress);
		tvTitleService=(TextView) findViewById(R.id.tvTitleService);

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
			etToDate.setText(getIntent().getStringExtra("ToDate").toString());
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
			etFromTime.setText(getIntent().getStringExtra("FromTime").toString());
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
			etToTime.setText(getIntent().getStringExtra("ToTime").toString());
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
		db.close();
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
					ErrorStr+="تاریخ شروع را وارد نمایید"+"\n";
				}
				if(etToDate.length()==0)
				{
					ErrorStr+="تاریخ خاتمه را وارد نمایید"+"\n";
				}
				if(etFromTime.length()==0)
				{
					ErrorStr+="ساعت شروع را وارد نمایید"+"\n";
				}
				if(etToTime.length()==0)
				{
					ErrorStr+="ساعت خاتمه را وارد نمایید"+"\n";
				}
				if(etFromDate.getText().toString().compareTo(etToDate.getText().toString())>0)
				{
					ErrorStr+="تاریخ شروع نمی تواند بزرگتر از تاریخ خاتمه باشد."+"\n";
				}
				if(etFromDate.length()<8 && etFromDate.length()>10)
				{
					ErrorStr+="تاریخ شروع را صحیح وارد نمایید"+"\n";
				}
				if(etToDate.length()<8 && etToDate.length()>10)
				{
					ErrorStr+="تاریخ خاتمه را صحیح وارد نمایید"+"\n";
				}
				if(etFromTime.length()<3 && etFromTime.length()>5)
				{
					ErrorStr+="زمان شروع را صحیح وارد نمایید"+"\n";
				}
				if(etToTime.length()<3 && etToTime.length()>5)
				{
					ErrorStr+="زمان خاتمه را صحیح وارد نمایید"+"\n";
				}
//				if(etFromTime.getText().toString().compareTo(etToTime.getText().toString())>0)
//				{
//					ErrorStr+="ساعت شروع نمی تواند بزرگتر از ساعت خاتمه باشد."+"\n";
//				}

//				if(AddressCode.length()!=10)
//				{
//					ErrorStr+="آدرس را وارد نمایید"+"\n";
//				}
				Description =etDescription.getText().toString();
				if(ErrorStr.length()==0)
				{
					LoadActivity(Service_Request2.class, "karbarCode", karbarCode,
							"DetailCode", DetailCode,
							"FromDate", etFromDate.getText().toString(),
							"ToDate", etToDate.getText().toString(),
							"FromTime", etFromTime.getText().toString(),
							"ToTime", etToTime.getText().toString(),
							"Description", etDescription.getText().toString(),
							"AddressCode", etAddres.getTag().toString()
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
									etFromDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));

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

							}
						}, now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay());
				datePickerDialog.setThemeDark(true);
				datePickerDialog.show(getFragmentManager(), "tpd");

			}
		});
		etToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					PersianCalendar now = new PersianCalendar();
					DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
									etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
								}
							}, now.getPersianYear(),
							now.getPersianMonth(),
							now.getPersianDay());
					datePickerDialog.setThemeDark(true);
					datePickerDialog.show(getFragmentManager(), "tpd");
				}
			}
		});
		etToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
								etToDate.setText(String.valueOf(year) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(dayOfMonth));
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
					mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
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
				mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
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
		});
		etToTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Calendar mcurrentTime = Calendar.getInstance();
					final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
					int minute = mcurrentTime.get(Calendar.MINUTE);

					TimePickerDialog mTimePicker;
					mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
							String AM_PM;
							if (selectedHour >= 0 && selectedHour < 12) {
								AM_PM = "AM";
							} else {
								AM_PM = "PM";
							}
							etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
						}
					}, hour, minute, false);
					mTimePicker.setTitle("انتخاب زمان خاتمه");
					mTimePicker.show();
				}
			}
		});
		etToTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(Service_Request1.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						String AM_PM;
						if (selectedHour >= 0 && selectedHour < 12) {
							AM_PM = "AM";
						} else {
							AM_PM = "PM";
						}
						etToTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
					}
				}, hour, minute, false);
				mTimePicker.setTitle("انتخاب زمان خاتمه");
				mTimePicker.show();
			}
		});
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
						 String VariableName8, String VariableValue8)
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
		Service_Request1.this.startActivity(intent);
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
}
