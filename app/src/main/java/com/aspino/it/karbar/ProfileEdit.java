package  com.aspino.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileEdit extends Activity {
	private String karbarCode;
	private EditText etTextName;
	private EditText etTextFamily;
	private EditText etTextNationalCode;
	private EditText etTextEmail;
	private EditText etTextBthDate;
	private Button btnSaveEditProfile;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String yearStr="";
	private String  monStr="";
	private String  dayStr="";

	//	private ImageView imgUser;
//	private int color;
//	private Paint paint;
//	private Rect rect;
//	private RectF rectF;
//	private Bitmap result;
//	private Canvas canvas;
//	private float roundPx;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileedit);
//		imgUser=(ImageView)findViewById(R.id.imgUser);
		etTextName=(EditText)findViewById(R.id.etTextName);
		etTextFamily=(EditText)findViewById(R.id.etTextFamily);
		etTextNationalCode=(EditText) findViewById(R.id.etTextNationalCode);
		etTextEmail=(EditText) findViewById(R.id.etTextEmail);
		etTextBthDate=(EditText) findViewById(R.id.etTextBthDate);
		btnSaveEditProfile=(Button) findViewById(R.id.btnSaveEditProfile);
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

		try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
				coursors.moveToNext();

				karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
//				tvTextUserMobile.setText(coursors.getString(coursors.getColumnIndex("Phone")));
			}
		if(!coursors.isClosed()) {
			coursors.close();
		}
		if(db.isOpen()) {
			db.close();
		}

		Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
		try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}

		coursors = db.rawQuery("SELECT * FROM Profile",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();
			try
			{
				etTextName.setText(coursors.getString(coursors.getColumnIndex("Name")));
			}
			catch (Exception ex)
			{

			}
			try
			{
				etTextFamily.setText(coursors.getString(coursors.getColumnIndex("Fam")));
			}
			catch (Exception ex)
			{

			}
			try
			{
				etTextNationalCode.setText(coursors.getString(coursors.getColumnIndex("ShSh")));
			}
			catch (Exception ex)
			{

			}
			try
			{
				etTextEmail.setText(coursors.getString(coursors.getColumnIndex("Email")));
			}
			catch (Exception ex)
			{

			}
			try
			{
				if(coursors.getString(coursors.getColumnIndex("BthDate")).length()>=10) {
					etTextBthDate.setText(coursors.getString(coursors.getColumnIndex("BthDate")));
				}
			}
			catch (Exception ex)
			{
				etTextBthDate.setText("");
			}
//			try
//			{
//				if(coursors.getString(coursors.getColumnIndex("Pic")).length()>0) {
//					bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
//				}
//			}
//			catch (Exception ex)
//			{
//
//			}
		}
		if(!coursors.isClosed()) {
			coursors.close();
		}
		if(db.isOpen()) {
			db.close();
		}
		etTextBthDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(ProfileEdit.this);
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
						String StartYear = String.valueOf(persianCalendar.getPersianYear());
						String StartMonth;
						if(persianCalendar.getPersianMonth()<10)
						{
							StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
						}
						else
						{
							StartMonth=String.valueOf(persianCalendar.getPersianMonth());
						}
						String StartDay;
						if(persianCalendar.getPersianDay()<10)
						{
							StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
						}
						else
						{
							StartDay=String.valueOf(persianCalendar.getPersianDay());
						}

						etTextBthDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));

						if(db.isOpen()) {
							db.close();
						}
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();
			}
		});
		etTextBthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				PersianDatePickerDialog picker = new PersianDatePickerDialog(ProfileEdit.this);
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
						String StartYear = String.valueOf(persianCalendar.getPersianYear());
						String StartMonth;
						if(persianCalendar.getPersianMonth()<10)
						{
							StartMonth="0"+String.valueOf(persianCalendar.getPersianMonth());
						}
						else
						{
							StartMonth=String.valueOf(persianCalendar.getPersianMonth());
						}
						String StartDay;
						if(persianCalendar.getPersianDay()<10)
						{
							StartDay="0"+String.valueOf(persianCalendar.getPersianDay());
						}
						else
						{
							StartDay=String.valueOf(persianCalendar.getPersianDay());
						}

						etTextBthDate.setText(PersianDigitConverter.PerisanNumber(StartYear + "/" + StartMonth + "/" + StartDay));

						if(db.isOpen()) {
							db.close();
						}
					}

					@Override
					public void onDismissed() {

					}
				});
				picker.show();
			}
		});
		btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InternetConnection ic=new InternetConnection(getApplicationContext());
				if(ic.isConnectingToInternet())
				{

						insertKarbar();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "اتصال به شبکه را چک نمایید.", Toast.LENGTH_LONG).show();
				}
				if(db.isOpen()) {
					db.close();
				}
			}
		});
//		try
//		{
//			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
//		}
//		catch (Exception ex)
//		{
//			bmp = BitmapFactory.decodeResource(getResources(),R.drawable.useravatar);
//			imgUser.setImageBitmap(getRoundedRectBitmap(bmp, 1000));
//		}

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

	public void insertKarbar() {
		db=dbh.getReadableDatabase();
		String errorStr="";
		if(etTextBthDate.getText().length()>=10)
		{
			String sp[]=etTextBthDate.getText().toString().split("/");
			yearStr=sp[0];
			if(sp[1].length()<2)
			{
				monStr="0"+sp[1];
			}
			else
			{
				monStr=sp[1];
			}
			if(sp[2].length()<2)
			{
				dayStr="0"+sp[2];
			}
			else
			{
				dayStr=sp[2];
			}
		}
		else
		{
			yearStr="";
			monStr="";
			dayStr="";
		}
		if(errorStr.compareTo("")==0)
		{
			UpdateProfile updateProfile = new UpdateProfile(ProfileEdit.this, karbarCode, yearStr, monStr, dayStr,"0",etTextNationalCode.getText().toString(),etTextEmail.getText().toString());
			updateProfile.AsyncExecute();
		}
		else
		{
			Toast.makeText(ProfileEdit.this, errorStr, Toast.LENGTH_SHORT).show();
		}
		if(db.isOpen()) {                                            db.close();                                        }
	}
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )  {
	    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
	    	ProfileEdit.this.LoadActivity(Profile.class, "karbarCode", karbarCode);
	    }

	    return super.onKeyDown( keyCode, event );
	}
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
		{
			Intent intent = new Intent(getApplicationContext(),Cls);
			intent.putExtra(VariableName, VariableValue);

			ProfileEdit.this.startActivity(intent);
		}
//	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
//		{
//			Intent intent = new Intent(getApplicationContext(),Cls);
//			intent.putExtra(VariableName, VariableValue);
//			intent.putExtra(VariableName2, VariableValue2);
//
//			ProfileEdit.this.startActivity(intent);
//		}
//	public Bitmap convertToBitmap(String base){
//		Bitmap Bmp=null;
//		try
//		{
//			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
//			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
////
//			return Bmp;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			return Bmp;
//		}
//	}
//	public  Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels)
//	{
//		result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		canvas = new Canvas(result);
//
//		color = 0xff424242;
//		paint = new Paint();
//		rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//		rectF = new RectF(rect);
//		roundPx = pixels;
//
//		paint.setAntiAlias(true);
//		canvas.drawARGB(0, 0, 0, 0);
//		paint.setColor(color);
//		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//		canvas.drawBitmap(bitmap, rect, rect, paint);
//
//		return result;
//	}
}

