package com.aspino.it.karbar;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Service_Request2 extends AppCompatActivity {
	private TextView tvTitleService;
	private TextView tvTitleTypePeriodService;
	private TextView tvTitleStatus;
	private TextView tvTitleCountWoman;
	private TextView tvTitleCountMan;
	private TextView tvTitleLearning;
	private TextView tvGraid;
	private TextView tvFieldEducation;
	private TextView tvFieldArt;
	private TextView tvTitleFieldArtOther;
	private TextView tvTitleGenderStudent;
	private TextView tvTitleGenderTeacher;
	private TextView tvTitleTypeService;
	private TextView tvTitleTypeCar;
	private TextView tvLanguage;
	//**************************************************************
//	private EditText etCountWoman;
//	private EditText etCountMan;
	private EditText etDoesnotmatter;
	private EditText etTitleLearning;
	private EditText etFieldArtOther;
	//**************************************************************
	private Spinner spGraid;
	private Spinner spFieldEducation;
	private Spinner spFieldArt;
	private Spinner spLanguage;
	//**************************************************************
	private RadioGroup rgTypePeriodService;
	private RadioGroup rgStatus;
	private RadioGroup rgGenderStudent;
	private RadioGroup rgGenderTeacher;
	private RadioGroup rgTypeService;
	private RadioGroup rgTypeCar;
	//**************************************************************
//	private CheckBox chbDoesnotmatter;
	//**************************************************************
	private ImageView imgBack;
	private ImageView imgSave;
//	private LinearLayout LinearTypePeriodService;
	private LinearLayout LinearGenderExpert;
	private LinearLayout LinearTitleGenderExpert;
//	private LinearLayout LinearGenderExpertCountWoman;
//	private LinearLayout LinearCountMan;
	private LinearLayout LinearCountDoenotmatter;
	private LinearLayout LinearTitleCountDoenotmatter;
	private LinearLayout LinearLearning;
	private LinearLayout LinearGraid;
	private LinearLayout LinearFieldEducation;
	private LinearLayout LinearFieldArt;
	private LinearLayout LinearFieldArtOther;
	private LinearLayout LinearGenderStudent;
	private LinearLayout LinearGenderTeacher;
	private LinearLayout LinearTypeService;
	private LinearLayout LinearTypeCar;
	private LinearLayout LinearLanguage;
	//**************************************************************
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String typeForm;
	private String CodeService;
	///*************************************
	private String MaleCount ;
	private String FemaleCount ;
	private String HamyarCount ;
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
//	private String IsEmergency ;
	private String PeriodicServices ;
	private String EducationGrade ;
	private String FieldOfStudy ;
	private String StudentGender ;
	private String TeacherGender ;
	private String EducationTitle ;
	private String ArtField ;
	private String CarWashType ;
	private String CarType ;
	private String Language ;
	private String karbarCode;
	private String DetailCode;
	//************************************************************
	private Button btnCansel;
	private Button btnDesCountExpert;
	private Button btnAddCountExpert;
	//*************************************
	private String FromDate;
	private String ToDate;
	private String FromTime;
	private String ToTime;
	//*********************************************
	private CheckBox chbMale;
	private CheckBox chbFemale;
	private CheckBox chbMaleAndFemale;
	//*********************************************
//	private RadioButton rdbDaily;
//	private RadioButton rdbWeekly;
//	private RadioButton rdbMiddle_of_the_week;
//	private RadioButton rdbMonthly;
//	private RadioButton rdbNormal;
//	private RadioButton rdbEmergency;
	private RadioButton rdbMaleStudent;
	private RadioButton rdbFemaleStudent;
	private RadioButton rdbMaleTeacher;
	private RadioButton rdbFemaleTeacher;
	private RadioButton rdbDoesnotmatter;
	private RadioButton rdbRoshoie;
	private RadioButton rdbRoshoieAndToShoie;
	private RadioButton rdbSavari;
	private RadioButton rdbShasi;
	private RadioButton rdbVan;
	private RadioButton radioStatusButton;
	private RadioButton radioTypePeriodServiceButton;
	private RadioButton radioStudentGenderButton;
	private RadioButton radioTeacherGenderButton;
	private RadioButton radioCarWashTypeButton;
	private RadioButton radiorgTypeCarButton;
	private String TimeDiff;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.service_request2);
//		btnOrder=(Button)findViewById(R.id.btnOrderBottom);
//		btnOrder.setTypeface(FontFace);
//		btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
//		btnAcceptOrder.setTypeface(FontFace);
//		btncredite=(Button)findViewById(R.id.btncrediteBottom);
//		btncredite.setTypeface(FontFace);
		imgBack=(ImageView) findViewById(R.id.imgBack);
		imgSave=(ImageView) findViewById(R.id.imgSave);
		btnCansel=(Button)findViewById(R.id.btnCansel);
		btnAddCountExpert=(Button)findViewById(R.id.btnAddCountExpert);
		btnDesCountExpert=(Button)findViewById(R.id.btnDesCountExpert);
		tvTitleService=(TextView) findViewById(R.id.tvTitleService);
		//**************************************************************************************
		tvTitleService=(TextView)findViewById(R.id.tvTitleService);
		tvTitleTypePeriodService=(TextView)findViewById(R.id.tvTitleTypePeriodService);
		tvTitleStatus=(TextView)findViewById(R.id.tvTitleStatus);
		tvTitleCountWoman=(TextView)findViewById(R.id.tvTitleCountWoman);
		tvTitleCountMan=(TextView)findViewById(R.id.tvTitleCountMan);
		tvTitleLearning=(TextView)findViewById(R.id.tvTitleLearning);
		tvGraid=(TextView)findViewById(R.id.tvGraid);
		tvFieldEducation=(TextView)findViewById(R.id.tvFieldEducation);
		tvFieldArt=(TextView)findViewById(R.id.tvFieldArt);
		tvTitleFieldArtOther=(TextView)findViewById(R.id.tvTitleFieldArtOther);
		tvTitleGenderStudent=(TextView)findViewById(R.id.tvTitleGenderStudent);
		tvTitleGenderTeacher=(TextView)findViewById(R.id.tvTitleGenderTeacher);
		tvTitleTypeService=(TextView)findViewById(R.id.tvTitleTypeService);
		tvTitleTypeCar=(TextView)findViewById(R.id.tvTitleTypeCar);
		tvLanguage=(TextView)findViewById(R.id.tvLanguage);
//	  etCountWoman=(EditText)findViewById(R.id.etCountWoman);
//	  etCountMan=(EditText)findViewById(R.id.etCountMan);
	  etDoesnotmatter=(EditText)findViewById(R.id.etDoesnotmatter);
	  etTitleLearning=(EditText)findViewById(R.id.etTitleLearning);
	  etFieldArtOther=(EditText)findViewById(R.id.etFieldArtOther);

		//*************************************************************************
		chbMale=(CheckBox) findViewById(R.id.chbMale);
		chbFemale=(CheckBox)findViewById(R.id.chbFemale);
		chbMaleAndFemale=(CheckBox)findViewById(R.id.chbMaleAndFemale);
		//******************************************************************
	  spGraid=(Spinner)findViewById(R.id.spGraid);
	  spFieldEducation=(Spinner)findViewById(R.id.spFieldEducation);
	  spFieldArt=(Spinner)findViewById(R.id.spFieldArt);
	  spLanguage=(Spinner)findViewById(R.id.spLanguage);
	  //*****************************************************************************
	  rgTypePeriodService=(RadioGroup)findViewById(R.id.rgTypePeriodService);
	  rgStatus=(RadioGroup)findViewById(R.id.rgStatus);
	  rgGenderStudent=(RadioGroup)findViewById(R.id.rgGenderStudent);
	  rgGenderTeacher=(RadioGroup)findViewById(R.id.rgGenderTeacher);
	  rgTypeService=(RadioGroup)findViewById(R.id.rgTypeService);
	  rgTypeCar=(RadioGroup)findViewById(R.id.rgTypeCar);

//	  LinearTypePeriodService=(LinearLayout)findViewById(R.id.LinearTypePeriodService);
	  LinearGenderExpert=(LinearLayout)findViewById(R.id.LinearGenderExpert);
	  LinearTitleGenderExpert=(LinearLayout)findViewById(R.id.LinearTitleGenderExpert);
	  //LinearGenderExpertCountWoman=(LinearLayout)findViewById(R.id.//LinearGenderExpertCountWoman);
	  //LinearCountMan=(LinearLayout)findViewById(R.id.//LinearCountMan);
	  LinearCountDoenotmatter=(LinearLayout)findViewById(R.id.LinearCountDoenotmatter);
	  LinearTitleCountDoenotmatter=(LinearLayout)findViewById(R.id.LinearTitleCountDoenotmatter);
	  LinearLearning=(LinearLayout)findViewById(R.id.LinearLearning);
	  LinearGraid=(LinearLayout)findViewById(R.id.LinearGraid);
	  LinearFieldEducation=(LinearLayout)findViewById(R.id.LinearFieldEducation);
	  LinearFieldArt=(LinearLayout)findViewById(R.id.LinearFieldArt);
	  LinearFieldArtOther=(LinearLayout)findViewById(R.id.LinearFieldArtOther);
	  LinearGenderStudent=(LinearLayout)findViewById(R.id.LinearGenderStudent);
	  LinearGenderTeacher=(LinearLayout)findViewById(R.id.LinearGenderTeacher);
	  LinearTypeService=(LinearLayout)findViewById(R.id.LinearTypeService);
	  LinearTypeCar=(LinearLayout)findViewById(R.id.LinearTypeCar);
	  LinearLanguage=(LinearLayout)findViewById(R.id.LinearLanguage);


		//*********************************************************
//		rdbDaily=(RadioButton)findViewById(R.id.rdbDaily);
//		rdbWeekly=(RadioButton)findViewById(R.id.rdbWeekly);
//		rdbMiddle_of_the_week=(RadioButton)findViewById(R.id.rdbMiddle_of_the_week);
//		rdbMonthly=(RadioButton)findViewById(R.id.rdbMonthly);
//		rdbNormal=(RadioButton)findViewById(R.id.rdbNormal);
//		rdbEmergency=(RadioButton)findViewById(R.id.rdbEmergency);
		rdbMaleStudent=(RadioButton)findViewById(R.id.rdbMaleStudent);
		rdbFemaleStudent=(RadioButton)findViewById(R.id.rdbFemaleStudent);
		rdbMaleTeacher=(RadioButton)findViewById(R.id.rdbMaleTeacher);
		rdbFemaleTeacher=(RadioButton)findViewById(R.id.rdbFemaleTeacher);
		rdbDoesnotmatter=(RadioButton)findViewById(R.id.rdbDoesnotmatter);
		rdbRoshoie=(RadioButton)findViewById(R.id.rdbRoshoie);
		rdbRoshoieAndToShoie=(RadioButton)findViewById(R.id.rdbRoshoieAndToShoie);
		rdbSavari=(RadioButton)findViewById(R.id.rdbSavari);
		rdbShasi=(RadioButton)findViewById(R.id.rdbShasi);
		rdbVan=(RadioButton)findViewById(R.id.rdbVan);
		//***********************************************************************
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
		String splitStr[]=FromDate.split("/");
		StartYear =splitStr[0];
		StartMonth =splitStr[1];
		StartDay =splitStr[2];

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
		AddressCode = getIntent().getStringExtra("AddressCode").toString();
	}
	catch (Exception ex)
	{
		AddressCode="";
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
//**************************************************************************************
		btnAddCountExpert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count;
				count=Integer.parseInt(etDoesnotmatter.getText().toString())+1;
				etDoesnotmatter.setText(String.valueOf(count));
			}
		});
	btnDesCountExpert.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int count;
			if(Integer.parseInt(etDoesnotmatter.getText().toString())>0)
			{
				count = Integer.parseInt(etDoesnotmatter.getText().toString()) - 1;
				etDoesnotmatter.setText(String.valueOf(count));
			}
		}
	});


		chbMale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chbMale.isChecked())
				{
					chbFemale.setChecked(false);
					chbMaleAndFemale.setChecked(false);
				}
				else
				{
					chbMale.setChecked(true);
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
				}
				else
				{
					chbFemale.setChecked(true);
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
				}
				else
				{
					chbMaleAndFemale.setChecked(true);
				}
			}
		});
//**************************************************************************************

		db=dbh.getReadableDatabase();
		Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+DetailCode+"'",null);
		if(coursors.getCount()>0){
			coursors.moveToNext();
			typeForm=coursors.getString(coursors.getColumnIndex("type"));
			CodeService=coursors.getString(coursors.getColumnIndex("servicename"));
			tvTitleService.setText(":"+coursors.getString(coursors.getColumnIndex("name")));
		}
		else
		{
			typeForm="0";
			Toast.makeText(getBaseContext(), "نوع فرم ثبت نشده", Toast.LENGTH_LONG).show();
		}
		db.close();
	switch (typeForm)
	{
		case "0":
			form1();
			break;
		case "1":
			form1();
			break;
		case "2":
			form2();
			break;
		case "3":
			form3();
			break;
		case "4":
			form4();
			break;
		case "5":
			form5();
			break;
		case "6":
			form6();
			break;
		case "7":
			form7();
			break;
		case "8":
			form8();
			break;
		case "9":
			form9();
			break;
	}
//	chbDoesnotmatter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			if(isChecked)
//			{
//
//				//LinearGenderExpertCountWoman.setVisibility(View.GONE);
//				//LinearCountMan.setVisibility(View.GONE);
//				etDoesnotmatter.setVisibility(View.VISIBLE);
//			}
//			else
//			{
//				//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
//				//LinearCountMan.setVisibility(View.VISIBLE);
//				etDoesnotmatter.setVisibility(View.VISIBLE);
//			}
//		}
//	});
		imgSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ErrorStr="";
//				FemaleCount=etCountWoman.getText().toString();
				HamyarCount=etDoesnotmatter.getText().toString();
//				MaleCount=etCountMan.getText().toString();
				if(!chbMale.isChecked() && !chbFemale.isChecked() && !chbMaleAndFemale.isChecked())
				{
					ErrorStr+="جنسیت متخصص را انتخاب نمایید"+ "\n";
				}
				else
				{
					if(chbMale.isChecked()){
						MaleCount=etDoesnotmatter.getText().toString();
						FemaleCount="0";
						HamyarCount="0";
					}
					if(chbFemale.isChecked()){
						MaleCount="0";
						FemaleCount=etDoesnotmatter.getText().toString();
						HamyarCount="0";
					}
					if(chbMaleAndFemale.isChecked()){
						MaleCount="0";
						FemaleCount="0";
						HamyarCount=etDoesnotmatter.getText().toString();
					}
				}
				//**************************************************************
				int selectedId ;
//				selectedId = rgStatus.getCheckedRadioButtonId();
				// find the radiobutton by returned id
//				radioStatusButton = (RadioButton) findViewById(selectedId);
//				IsEmergency =radioStatusButton.getText().toString();
//				if(IsEmergency.compareTo("عادی")==0)
//				{
//					IsEmergency="0";
//				}
//				else
//				{
//					IsEmergency="1";
//				}
				//***************************************************************
				
//				selectedId = rgTypePeriodService.getCheckedRadioButtonId();
				// find the radiobutton by returned id
//				radioTypePeriodServiceButton = (RadioButton) findViewById(selectedId);
//				PeriodicServices =radioTypePeriodServiceButton.getText().toString();
//				if(PeriodicServices.compareTo("روزانه")==0)
//				{
//					PeriodicServices ="1";
//				}
//				else if(PeriodicServices.compareTo("هفته در میان")==0)
//				{
//					PeriodicServices ="2";
//				}
//				else if(PeriodicServices.compareTo("هفتگی")==0)
//				{
//					PeriodicServices ="3";
//				}
//				else
//				{
//					PeriodicServices ="4";
//				}
				//***************************************************************

				try
				{
					EducationGrade =spGraid.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					EducationGrade ="0";
				}
				try
				{
					FieldOfStudy =etTitleLearning.getText().toString();
					if(FieldOfStudy.length()==0)
					{
						FieldOfStudy="0";
					}
				}
				catch (Exception ex)
				{
					FieldOfStudy ="0";
				}

				//**************************************************************
				selectedId = rgGenderStudent.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioStudentGenderButton = (RadioButton) findViewById(selectedId);
				StudentGender  =radioStudentGenderButton.getText().toString();
				if(LinearGenderStudent.getVisibility()==View.VISIBLE) {
					if (StudentGender.compareTo("زن") == 0) {
						StudentGender = "1";
					} else {
						StudentGender = "2";
					}
				}
				else
				{
					StudentGender="0";
				}
				//***************************************************************
				selectedId = rgGenderTeacher.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioTeacherGenderButton = (RadioButton) findViewById(selectedId);
				TeacherGender  =radioTeacherGenderButton.getText().toString();
				if(LinearGenderTeacher.getVisibility()==View.VISIBLE) {
					if (TeacherGender.compareTo("زن") == 0) {
						TeacherGender = "1";
					} else if (TeacherGender.compareTo("مرد") == 0) {
						TeacherGender = "2";
					} else {
						TeacherGender = "3";
					}
				}
				else
				{
					TeacherGender="0";
				}
				//***************************************************************

				try
				{
					EducationTitle =spFieldEducation.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					EducationTitle ="0";
				}
				try
				{
					ArtField =spFieldArt.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					ArtField ="0";
				}
				//***************************************************************
				//***************************************************************
				selectedId = rgTypeService.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioCarWashTypeButton = (RadioButton) findViewById(selectedId);
				CarWashType  =radioCarWashTypeButton.getText().toString();
				if(LinearTypeService.getVisibility()==View.VISIBLE)
				{
					if(CarWashType.compareTo("روشویی")==0)
					{
						CarWashType ="1";
					}
					else
					{
						CarWashType="2";
					}
				}
				else
				{
					CarWashType="0";
				}

				//***************************************************************
				selectedId = rgTypeCar.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radiorgTypeCarButton = (RadioButton) findViewById(selectedId);
				CarType  =radiorgTypeCarButton.getText().toString();
				if(LinearTypeCar.getVisibility()==View.VISIBLE) {
					if (CarType.compareTo("سواری") == 0) {
						CarType = "1";
					} else if (CarType.compareTo("ون") == 0) {
						CarType = "3";
					} else {
						CarType = "2";
					}
				}
				else
					{
						CarType="0";
					}
				//***************************************************************


				try
				{
					Language =spLanguage.getSelectedItem().toString();
				}
				catch (Exception ex)
				{
					Language ="0";
				}
//				if(LinearCountDoenotmatter.getVisibility()==View.VISIBLE ||
						//LinearCountMan.getVisibility()== View.VISIBLE ||
						//LinearGenderExpertCountWoman.getVisibility()==View.VISIBLE) {
				if (etDoesnotmatter.getText().toString().compareTo("0")==0 && LinearCountDoenotmatter.getVisibility()==View.VISIBLE) {

							ErrorStr += "تعداد متخصص را مشخص نمایید" + "\n";

				}

				if(ErrorStr.length()==0)
					{


						SyncInsertUserServices syncInsertUserServices = new SyncInsertUserServices(Service_Request2.this,
								karbarCode, DetailCode, MaleCount, FemaleCount, HamyarCount, StartYear, StartMonth,
								StartDay, StartHour, StartMinute, EndYear, EndMonth, EndDay, EndHour, EndMinute,
								AddressCode, Description, "0", "0", EducationGrade,
								FieldOfStudy, StudentGender, TeacherGender, EducationTitle, ArtField, CarWashType, CarType, Language,TimeDiff);
						syncInsertUserServices.AsyncExecute();
					}
					else
					{
							Toast.makeText(Service_Request2.this, ErrorStr, Toast.LENGTH_SHORT).show();
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
		spFieldArt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(spFieldArt.getItemAtPosition(position).toString().compareTo("سایر")==0)
				{
					LinearFieldArtOther.setVisibility(View.VISIBLE);
					ArtField=etFieldArtOther.getText().toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

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
		Service_Request2.this.startActivity(intent);
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
	Service_Request2.this.startActivity(intent);
	}
	public void form1()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		//LinearTypePeriodService.setVisibility(View.VISIBLE);
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************
		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.VISIBLE);
		LinearTitleGenderExpert.setVisibility(View.VISIBLE);
	}
	public void form2()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		////LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.GONE);
		//LinearCountMan.setVisibility(View.GONE);
		LinearCountDoenotmatter.setVisibility(View.GONE);
		LinearTitleCountDoenotmatter.setVisibility(View.GONE);
		//**********************************************

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
	}

	public void form3()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.VISIBLE);
		LinearTitleGenderExpert.setVisibility(View.VISIBLE);
	}
	public void form4()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.VISIBLE);
		LinearTitleGenderExpert.setVisibility(View.VISIBLE);
	}
	public void form5()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		LinearLearning.setVisibility(View.VISIBLE);
		LinearGraid.setVisibility(View.VISIBLE);
		FillSpinner("Grade","Title",spGraid);
		LinearFieldEducation.setVisibility(View.VISIBLE);
		FillSpinner("FieldofEducation","Title",spFieldEducation);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
	}
	public void form6()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.VISIBLE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
	}
	public void form7()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		LinearLearning.setVisibility(View.VISIBLE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
	}
	public void form8()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************


		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.VISIBLE);
		//LinearCountMan.setVisibility(View.VISIBLE);
		LinearCountDoenotmatter.setVisibility(View.VISIBLE);
		LinearTitleCountDoenotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.VISIBLE);
		FillSpinner("Arts","Title",spFieldArt);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.VISIBLE);
		LinearGenderTeacher.setVisibility(View.VISIBLE);
		LinearTypeService.setVisibility(View.GONE);
		LinearTypeCar.setVisibility(View.GONE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
	}
	public void form9()
	{
		etDoesnotmatter.setVisibility(View.VISIBLE);
		//**********************************************

		//LinearTypePeriodService.setVisibility(View.GONE);;
		//**********************************************
		//LinearGenderExpertCountWoman.setVisibility(View.GONE);
		//LinearCountMan.setVisibility(View.GONE);
		LinearCountDoenotmatter.setVisibility(View.GONE);
		LinearTitleCountDoenotmatter.setVisibility(View.GONE);
		//**********************************************

		LinearLearning.setVisibility(View.GONE);
		LinearGraid.setVisibility(View.GONE);
		LinearFieldEducation.setVisibility(View.GONE);
		LinearFieldArt.setVisibility(View.GONE);
		LinearFieldArtOther.setVisibility(View.GONE);
		LinearGenderStudent.setVisibility(View.GONE);
		LinearGenderTeacher.setVisibility(View.GONE);
		LinearTypeService.setVisibility(View.VISIBLE);
		LinearTypeCar.setVisibility(View.VISIBLE);
		LinearLanguage.setVisibility(View.GONE);
		LinearGenderExpert.setVisibility(View.GONE);
		LinearTitleGenderExpert.setVisibility(View.GONE);
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
			labels.add(str);
		}
		db.close();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/vazir.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}

			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
				View v =super.getDropDownView(position, convertView, parent);


				Typeface typeface=Typeface.createFromAsset(getAssets(), "font/vazir.ttf");
				((TextView) v).setTypeface(typeface);

				return v;
			}
		};
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

}
