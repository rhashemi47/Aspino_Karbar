package com.aspino.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Info_Person extends Activity {
	private Button btnSendInfo;
	private String phonenumber;
	private String Acceptcode;
	private String CityCodeLocation;
	private EditText fname;
	private EditText lname;
	private EditText etBrithday;
	private	DatabaseHelper dbh;
	private SQLiteDatabase db;
	private EditText etReagentCode;
	private String ReagentCode="";
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.info_personal);
		super.onCreate(savedInstanceState);
		etReagentCode=(EditText)findViewById(R.id.etReagentCodeInsertInfo);
		fname=(EditText)findViewById(R.id.etFname);
		lname=(EditText)findViewById(R.id.etLname);
		etBrithday=(EditText)findViewById(R.id.etBrithday);
		btnSendInfo=(Button)findViewById(R.id.btnSendInfo);
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
        	phonenumber = getIntent().getStringExtra("phonenumber").toString();
        	
        }
        catch (Exception e) {
			db = dbh.getReadableDatabase();
			String query = "SELECT * FROM Profile";
			Cursor cursor=db.rawQuery(query,null);
			if(cursor.getCount()>0)
			{
				cursor.moveToNext();
				phonenumber=cursor.getString(cursor.getColumnIndex("Mobile"));
			}
			else {
				phonenumber="0";
			}
			db.close();
		}		
   		try
        {
   			Acceptcode = getIntent().getStringExtra("acceptcode").toString();
        }
        catch (Exception e) {
			Acceptcode="0";
		}
		try
        {
			CityCodeLocation = getIntent().getStringExtra("CityCodeLocation").toString();
        }
        catch (Exception e) {
			CityCodeLocation="مشهد";
		}
		etBrithday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					PersianDatePickerDialog picker = new PersianDatePickerDialog(Info_Person.this);
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
							etBrithday.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());
						}

						@Override
						public void onDismissed() {

						}
					});
					picker.show();
				}

			}
		});
		etBrithday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(Info_Person.this);
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
						etBrithday.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();
			}
		});
		btnSendInfo.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0) {
			InternetConnection ic=new InternetConnection(getApplicationContext());
			if(ic.isConnectingToInternet())
			{
				ReagentCode=etReagentCode.getText().toString().trim();
				ReagentCode=ReagentCode.replace(" ","");
				if(ReagentCode.length()>0 && ReagentCode.length()<=5)
				{
					Toast.makeText(getApplicationContext(), "کد معرف به درستی وارد نشده!", Toast.LENGTH_LONG).show();
				}
				else
				{
					insertKarbar();
				}
			}

		}
	});
}

		public void insertKarbar() {
		db=dbh.getReadableDatabase();
		String errorStr="";
		if(fname.getText().toString().compareTo("")==0){
			errorStr+="لطفا نام خود راوارد نمایید\n";
		}
		if(lname.getText().toString().compareTo("")==0){
			errorStr+="لطفا نام خانوادگی خود راوارد نمایید\n";
		}
		if(ReagentCode.compareTo("")==0)
		{
			ReagentCode="0";
		}
		if(errorStr.compareTo("")==0)
		{
			InsertKarbar insertKarbar = new InsertKarbar(Info_Person.this, phonenumber, Acceptcode, fname.getText().toString(), lname.getText().toString(),ReagentCode,CityCodeLocation);
			insertKarbar.AsyncExecute();
		}
		else
		{
			Toast.makeText(Info_Person.this, errorStr, Toast.LENGTH_SHORT).show();
		}
	}
}
