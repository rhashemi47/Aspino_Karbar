package  com.aspino.it.karbar;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Final_ShowCodeService extends AppCompatActivity {
	private String karbarCode;
	private DatabaseHelper dbh;
	private TextView tvCodeServiceFinal;
	private TextView tvNameOrder;
	private TextView tvAddressFinalService;
	private TextView tvDateAndTimeFinalService;
	private ImageView imgOK;
	private SQLiteDatabase db;
//	private Typeface FontMitra;
	private String CodeServiceFinal;
	private String NameOrder;
	private String AddressFinalService;
	private String DateAndTimeFinalService;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.final_save_order);
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
		Cursor cursor = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			karbarCode=cursor.getString(cursor.getColumnIndex("karbarCode"));
		}

		if(!cursor.isClosed()) {
			cursor.close();
		}
		if(db.isOpen()) {
			db.close();
		}
	}

	try
	{
		CodeServiceFinal = getIntent().getStringExtra("CodeServiceFinal").toString();

	}
	catch (Exception ex)
	{
		CodeServiceFinal="0";
	}
	try
	{
		NameOrder = getIntent().getStringExtra("NameOrder").toString();

	}
	catch (Exception ex)
	{
		NameOrder="0";
	}
	try
	{
		AddressFinalService = getIntent().getStringExtra("AddressFinalService").toString();

	}
	catch (Exception ex)
	{
		AddressFinalService="0";
	}
	try
	{
		DateAndTimeFinalService = getIntent().getStringExtra("DateAndTimeFinalService").toString();

	}
	catch (Exception ex)
	{
		DateAndTimeFinalService="0";
	}

//	FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
	tvCodeServiceFinal=(TextView)findViewById(R.id.tvCodeServiceFinal);
	tvNameOrder=(TextView)findViewById(R.id.tvNameOrder);
	tvAddressFinalService=(TextView)findViewById(R.id.tvAddressFinalService);
	tvDateAndTimeFinalService=(TextView)findViewById(R.id.tvDateAndTimeFinalService);
	imgOK=(ImageView) findViewById(R.id.imgOK);
//	//************************************************************************************
//	tvCodeServiceFinal.setTypeface(FontMitra);
//	tvNameOrder.setTypeface(FontMitra);
//	tvAddressFinalService.setTypeface(FontMitra);
//	tvDateAndTimeFinalService.setTypeface(FontMitra);
	//************************************************************************************

	try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
	Cursor coursors = db.rawQuery("SELECT * FROM Servicesdetails WHERE code='"+NameOrder+"'",null);
	if(coursors.getCount()>0){
		coursors.moveToNext();
		NameOrder=coursors.getString(coursors.getColumnIndex("name"));
	}
	if(!coursors.isClosed()) {
		coursors.close();
	}
	if(db.isOpen()) {
		db.close();
	}
	//**********************
	try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
	Cursor cursorAddress = db.rawQuery("SELECT * FROM address WHERE code='"+AddressFinalService+"'",null);
	if(cursorAddress.getCount()>0)
	{
		for(int i=0;i<cursorAddress.getCount();i++){
			cursorAddress.moveToNext();
			AddressFinalService=cursorAddress.getString(cursorAddress.getColumnIndex("AddressText"));
		}
		if(!cursorAddress.isClosed())
		{
			cursorAddress.close();
		}
		if(db.isOpen()) {
			db.close();
		}
	}
	if(!coursors.isClosed()) {
		coursors.close();
	}
	if(db.isOpen()) {
		db.close();
	}
	tvCodeServiceFinal.setText(CodeServiceFinal);
	tvNameOrder.setText(NameOrder);
	tvAddressFinalService.setText(AddressFinalService);
	tvDateAndTimeFinalService.setText(DateAndTimeFinalService);
	//*************************************************************************************
	imgOK.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity(MainMenu.class, "karbarCode", karbarCode);
		}
	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
		return true;
	}
    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		this.startActivity(intent);
	}

}
