package com.aspino.it.karbar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Pardakh_Factor_Sefaresh extends AppCompatActivity {
	private String karbarCode;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private ImageView imgHamyar;
	private ImageView imgCall;
	private ImageView imgMessage;
	private TextView tvEstimatedPrice;
	private TextView tvFinalPrice;
	private TextView tvContentTypeService;
	private TextView tvDateService;
	private TextView tvContentAddress;
	private TextView tvCodeService;
	private TextView tvDateAndTimeVisitService;
	private TextView tvStartService;
	private TextView tvFinishService;
	private TextView tvContetnDiscountService;
	private TextView tvContetnFinalCurrency;
	private Button btnCachFactor;
	private String OrderCode;
	private String strCall;
	private int REQUEST_CODE_ASK_PERMISSIONS=123;

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
	tvEstimatedPrice=(TextView) findViewById(R.id.tvEstimatedPrice);
	tvFinalPrice=(TextView) findViewById(R.id.tvFinalPrice);
	tvContentTypeService=(TextView) findViewById(R.id.tvContentTypeService);
	tvDateService=(TextView) findViewById(R.id.tvDateService);
	tvContentAddress=(TextView) findViewById(R.id.tvContentAddress);
	tvCodeService=(TextView) findViewById(R.id.tvCodeService);
	tvDateAndTimeVisitService=(TextView) findViewById(R.id.tvDateAndTimeVisitService);
	tvStartService=(TextView) findViewById(R.id.tvStartService);
	tvFinishService=(TextView) findViewById(R.id.tvFinishService);
	tvContetnDiscountService=(TextView) findViewById(R.id.tvContetnDiscountService);
	tvContetnFinalCurrency=(TextView) findViewById(R.id.tvContetnFinalCurrency);
	btnCachFactor=(Button) findViewById(R.id.btnCachFactor);
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
	db=dbh.getReadableDatabase();
	Cursor coursors,cursor;
	String Query="SELECT OrdersService.*,Servicesdetails.name,address.name adname FROM OrdersService " +
			"LEFT JOIN " +
			"Servicesdetails ON " +
			"Servicesdetails.code=OrdersService.ServiceDetaileCode " +
			"LEFT JOIN " +
			"address ON " +
			"OrdersService.AddressCode=address.code WHERE OrdersService.Code ="+ OrderCode;
	coursors = db.rawQuery(Query, null);
	for(int i=0;i<coursors.getCount();i++){
		coursors.moveToNext();
		tvContentTypeService.setText(coursors.getString(coursors.getColumnIndex("name")));
		tvDateService.setText(coursors.getString(coursors.getColumnIndex("StartYear"))+"/"+
				coursors.getString(coursors.getColumnIndex("StartMonth"))+"/"+
				coursors.getString(coursors.getColumnIndex("StartDay")) + " - " +
		coursors.getString(coursors.getColumnIndex("StartHour"))+":"+
				coursors.getString(coursors.getColumnIndex("StartMinute")));
		tvContentAddress.setText(coursors.getString(coursors.getColumnIndex("adname")));
		tvCodeService.setText(coursors.getString(coursors.getColumnIndex("OrdersService.Code")));
	}
	if(!coursors.isClosed())
	{
		coursors.close();
	}
	Query="SELECT Hamyar.*,InfoHamyar.* FROM Hamyar " +
			"LEFT JOIN " +
			"InfoHamyar ON " +
			"Hamyar.CodeHamyarInfo=InfoHamyar.Code " +
			" WHERE Hamyar.CodeOrder ="+ OrderCode;
	cursor = db.rawQuery(Query, null);
	if(cursor.getCount()>0){
		cursor.moveToNext();
		imgHamyar.setImageBitmap(convertToBitmap(cursor.getString(cursor.getColumnIndex("img"))));
		strCall=cursor.getString(cursor.getColumnIndex("Mobile"));
	}
	if(!cursor.isClosed())
	{
		cursor.close();
	}
	if(db.isOpen()) {
		db.close();
	}
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
	btnCachFactor.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//todo
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
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
			ActivityCompat.requestPermissions(Pardakh_Factor_Sefaresh.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		startActivity(callIntent);
	}
	@Override
	public void onBackPressed() {

		LoadActivity(Paigiri.class, "karbarCode", karbarCode);
	}
}
