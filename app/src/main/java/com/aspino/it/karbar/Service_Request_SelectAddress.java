package com.aspino.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request_SelectAddress extends AppCompatActivity {
//	private TextView tvTitleService;
	private ImageView imgBack;
	private ImageView imgSave;
	//**************************************************************
	private Button btnAdd_New_Address;
	//**************************************************************
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
	//***************************************************
	private ListView listViewAddress;
	private ArrayList<HashMap<String,String>> valuse=new ArrayList<HashMap<String, String>>();

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.select_address);

		imgBack=(ImageView) findViewById(R.id.imgback);
		imgSave=(ImageView) findViewById(R.id.imgForward);
		listViewAddress=(ListView) findViewById(R.id.listViewAddress);
		btnAdd_New_Address=(Button) findViewById(R.id.btnAdd_New_Address);
//		tvTitleService=(TextView) findViewById(R.id.tvTitleService);


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
		String splitStrDate[]=sp[0].split("/");
		StartYear =splitStrDate[0];
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
		Cursor cursorAddress = db.rawQuery("SELECT * FROM address WHERE Status='1'",null);
		if(cursorAddress.getCount()>0)
		{
			for(int i=0;i<cursorAddress.getCount();i++){
				cursorAddress.moveToNext();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("TitleAddress",cursorAddress.getString(cursorAddress.getColumnIndex("Name")));
				map.put("ContentAddress",cursorAddress.getString(cursorAddress.getColumnIndex("AddressText")));
				map.put("Code",cursorAddress.getString(cursorAddress.getColumnIndex("Code")));
				valuse.add(map);
			}
			AdapterSelectAddress dataAdapter=new AdapterSelectAddress(Service_Request_SelectAddress.this,valuse);
			listViewAddress.setAdapter(dataAdapter);
		}
		db.close();
//**************************************************************
//		db=dbh.getReadableDatabase();
//		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
//		if(coursors.getCount()>0){
//			coursors.moveToNext();
//			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
////			tvTitleService.setText(":"+coursors.getString(coursors.getColumnIndex("name")));
//		}
//		db.close();
		btnAdd_New_Address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadActivity_Map(Map.class,"karbarCode", karbarCode,"nameActivity","Service_Request_SelectAddress");
			}
		});
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
						SyncInsertUserServices syncInsertUserServices = new SyncInsertUserServices(Service_Request_SelectAddress.this,
								karbarCode, DetailCode, MaleCount, FemaleCount, HamyarCount, StartYear, StartMonth,
								StartDay, StartHour, StartMinute, EndYear, EndMonth, EndDay, EndHour, EndMinute,
								AddressCode, Description, "0", "0", "0",
								"0", "0", "0", "0", "0", "0", "0", "0",TimeDiff);
						syncInsertUserServices.AsyncExecute();
					}
					else
					{
							Toast.makeText(Service_Request_SelectAddress.this, ErrorStr, Toast.LENGTH_SHORT).show();
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
		Service_Request_SelectAddress.this.startActivity(intent);
	}
public void LoadActivity_Map(Class<?> Cls, String VariableName, String VariableValue, String VariableName1, String VariableValue1)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName1, VariableValue1);
		Service_Request_SelectAddress.this.startActivity(intent);
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
	Service_Request_SelectAddress.this.startActivity(intent);
	}
}
