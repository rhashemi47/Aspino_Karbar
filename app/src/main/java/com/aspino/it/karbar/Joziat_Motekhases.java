package com.aspino.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Joziat_Motekhases extends AppCompatActivity {
	private String karbarCode;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ImageView imgHamyar;
	private ImageView imgCall;
	private ImageView imgMessage;
	private TextView tvCommentSaved;
	private TextView tvHistoryJob;
	private TextView tvFirstname;
	private TextView tvLastname;
	private TextView tvBirthday;
	private TextView tvDegreeOfEducation;
	private TextView tvBaseExpert;
	private TextView tvHistoryJobAll;
	private TextView tvComment_Hamyar;
	private Button btnSelectThisHamyar;
	private String OrderCode;
	private String strCall;
	private String Confirm="0";
	private String FinalCurrency="0";
	private int REQUEST_CODE_ASK_PERMISSIONS=123;
	private String HamyarCode;
	private String CodeMotkhases;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.pardakh_factor_sefaresh);
	imgHamyar=(ImageView) findViewById(R.id.imgHamyar);
	imgCall=(ImageView) findViewById(R.id.imgCall);
	imgMessage=(ImageView) findViewById(R.id.imgMessage);
	tvCommentSaved=(TextView) findViewById(R.id.tvCommentSaved);
	tvHistoryJob=(TextView) findViewById(R.id.tvHistoryJob);
	tvFirstname=(TextView) findViewById(R.id.tvFirstname);
	tvLastname=(TextView) findViewById(R.id.tvLastname);
	tvBirthday=(TextView) findViewById(R.id.tvBirthday);
	tvDegreeOfEducation=(TextView) findViewById(R.id.tvDegreeOfEducation);
	tvBaseExpert=(TextView) findViewById(R.id.tvBaseExpert);
	tvHistoryJobAll=(TextView) findViewById(R.id.tvHistoryJobAll);
	tvComment_Hamyar=(TextView) findViewById(R.id.tvComment_Hamyar);
	btnSelectThisHamyar=(Button) findViewById(R.id.btnSelectThisHamyar);
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
		karbarCode = getIntent().getStringExtra("karbarCode").toString();

	}
	catch (Exception e)
	{
		db=dbh.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			karbarCode=cursor.getString(cursor.getColumnIndex("karbarCode"));
		}
	}
	try
	{
		OrderCode = getIntent().getStringExtra("OrderCode").toString();

	}
	catch (Exception e)
	{
		OrderCode="0";
	}
	try
	{
		CodeMotkhases = getIntent().getStringExtra("CodeMotkhases").toString();

	}
	catch (Exception e)
	{
		CodeMotkhases="0";
	}
	String Query="SELECT InfoHamyar.* FROM Hamyar " +
			"LEFT JOIN " +
			"InfoHamyar ON " +
			"Hamyar.CodeHamyarInfo=InfoHamyar.Code_InfoHamyar " +
			" WHERE Hamyar.CodeOrder ="+ OrderCode;
	Cursor cursor = db.rawQuery(Query, null);
	if(cursor.getCount()>0){
		cursor.moveToNext();
		imgHamyar.setImageBitmap(convertToBitmap(cursor.getString(cursor.getColumnIndex("img"))));
		strCall=cursor.getString(cursor.getColumnIndex("Mobile"));
		HamyarCode=cursor.getString(cursor.getColumnIndex("InfoHamyar.Code_InfoHamyar"));
	}
	if(!cursor.isClosed())
	{
		cursor.close();
	}
	if(db.isOpen()) {
		db.close();
	}
//	Query = "SELECT * FROM UserServicesHamyarRequest WHERE BsUserServicesCode='" + OrderCode + "'";
//	db=dbh.getReadableDatabase();
//	cursor = db.rawQuery(Query, null);
//	if(cursor.getCount()>0){
//		cursor.moveToNext();
//		String Price =cursor.getString(cursor.getColumnIndex("Price"));
//		String PriceFinal =cursor.getString(cursor.getColumnIndex("PriceFinal"));
//		String DiscountService =cursor.getString(cursor.getColumnIndex("PriceOff"));
//		FinalCurrency =cursor.getString(cursor.getColumnIndex("TotalPrice"));
//		Confirm =cursor.getString(cursor.getColumnIndex("Confirm"));
//		tvEstimatedPrice.setText(Price);
//		tvFinalPrice.setText(PriceFinal);
//		tvContetnDiscountService.setText(DiscountService);
//		tvContetnFinalCurrency.setText(FinalCurrency);
//		if(PriceFinal.compareTo(Price)!=0 && Confirm.compareTo("0")==0)
//		{
//			alert_final_factor(PriceFinal);
//		}
//	}
//	if(!cursor.isClosed())
//	{
//		cursor.close();
//	}
//	if(db.isOpen()) {
//		db.close();
//	}
	imgCall.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dialContactPhone(strCall);
		}
	});
	imgMessage.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//todo Send SMS
		}
	});
	btnSelectThisHamyar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//todo
		}
	});
}
//@Override
//public boolean onKeyDown( int keyCode, KeyEvent event )  {
//
//
//    return super.onKeyDown( keyCode, event );
//}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		this.startActivity(intent);
	}
	public Bitmap convertToBitmap(String base) {
		Bitmap Bmp = null;
		try {
			byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
			Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
			return Bmp;
		} catch (Exception e) {
			e.printStackTrace();
			return Bmp;
		}
	}
	public void dialContactPhone(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(Joziat_Motekhases.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		startActivity(callIntent);
	}
	@Override
	public void onBackPressed() {
			LoadActivity(Select_Hamyar.class, "karbarCode", karbarCode);//todo Send Order Code
	}
}
