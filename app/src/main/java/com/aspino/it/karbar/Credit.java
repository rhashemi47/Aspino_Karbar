package com.aspino.it.karbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Credit extends Activity {
	private String karbarCode;

	private DatabaseHelper dbh;
	private TextView txtContent;
//	private TextView tvRecentCreditsValue;
	private SQLiteDatabase db;
//	private Button btnIncreseCredit;
//	private ListView lstHistoryCredit;
//	private Button btnOrder;
//	private Button btnAcceptOrder;
//	private Button btncredite;
	private EditText etCurrencyInsertCredit;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.credits);
//	btnOrder=(Button)findViewById(R.id.btnOrderBottom);
//	btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
//	btncredite=(Button)findViewById(R.id.btncrediteBottom);
	etCurrencyInsertCredit=(EditText) findViewById(R.id.etCurrencyInsertCredit);
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
		Cursor coursors = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<coursors.getCount();i++){
			coursors.moveToNext();

			karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
		}
	}
//
//	btnIncreseCredit=(Button)findViewById(R.id.btnIncresCredit);
//	lstHistoryCredit=(ListView) findViewById(R.id.lstHistoryCredit);
	//Typeface FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
	txtContent=(TextView)findViewById(R.id.tvHistoryCredits);
//	txtContent.setTypeface(FontMitra);
//	tvRecentCreditsValue=(TextView)findViewById(R.id.tvRecentCreditsValue);
//	tvRecentCreditsValue.setTypeface(FontMitra);
	String Query="UPDATE UpdateApp SET Status='1'";
	db=dbh.getWritableDatabase();
	db.execSQL(Query);
	try
	{
		String Content="";
		Cursor coursors = db.rawQuery("SELECT * FROM AmountCredit", null);
		if (coursors.getCount() > 0) {
			coursors.moveToNext();
			String splitStr[]=coursors.getString(coursors.getColumnIndex("Amount")).toString().split("\\.");
			if(splitStr[1].compareTo("00")==0)
			{
				Content+=splitStr[0];
			}
			else
			{
				Content+=coursors.getString(coursors.getColumnIndex("Amount"));
			}

		}
		else
		{
//			lstHistoryCredit.setVisibility(View.GONE);
		}
		if(Content.compareTo("")==0){
//			tvRecentCreditsValue.setText("0"+" ریال");
		}
		else {
//			tvRecentCreditsValue.setText(Content+" ریال");
		}
	}
	catch (Exception ex){
//		tvRecentCreditsValue.setText("0"+" ریال");
//		lstHistoryCredit.setVisibility(View.GONE);
	}
//	try
//	{
//		Cursor coursors = db.rawQuery("SELECT * FROM credits", null);
//		String Content="";
//		for (int i=0;i<coursors.getCount();i++) {
//			coursors.moveToNext();
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("name","مبلغ: " +coursors.getString(coursors.getColumnIndex("Price"))+ " ریال " +"\n"
//			+"عملیات: " + coursors.getString(coursors.getColumnIndex("TransactionType"))+ "\n"
//			+"نوع تراکنش: " + coursors.getString(coursors.getColumnIndex("PaymentMethod"))+ "\n"
//			+"تاریخ: " + coursors.getString(coursors.getColumnIndex("TransactionDate"))+ "\n"
//			+"شماره سند: " + coursors.getString(coursors.getColumnIndex("DocNumber"))+ "\n"
//			+"توضیحات: " + coursors.getString(coursors.getColumnIndex("Description")));
//			map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
//			valuse.add(map);
//		}
//		AdapterCredit dataAdapter=new AdapterCredit(Credit.this,valuse,karbarCode);
//		lstHistoryCredit.setAdapter(dataAdapter);
//		if(valuse.size()==0){
//			lstHistoryCredit.setVisibility(View.GONE);
//			txtContent.setVisibility(View.VISIBLE);
//			txtContent.setText("موردی جهت نمایش وجود ندارد");
//		}
//		else
//		{
//			lstHistoryCredit.setVisibility(View.VISIBLE);
//			txtContent.setVisibility(View.GONE);
//		}
//	}
//	catch (Exception ex){
//		lstHistoryCredit.setVisibility(View.GONE);
//		txtContent.setVisibility(View.VISIBLE);
//		tvRecentCreditsValue.setText("موردی جهت نمایش وجود ندارد");
//	}
//	btnIncreseCredit.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			if(etCurrencyInsertCredit.getText().length()>0) {
//				SyncInsertUserCredit syncInsertUserCredit = new SyncInsertUserCredit(Credit.this, etCurrencyInsertCredit.getText().toString(), karbarCode, "1", "10004", "تست");
//				syncInsertUserCredit.AsyncExecute();
//			}
//			else
//			{
//				Toast.makeText(Credit.this, "لطفا مبلغ مورد نظر خود را به ریال وارد نمایید", Toast.LENGTH_SHORT).show();
//			}
//		}
//	});
}
@Override
public boolean onKeyDown( int keyCode, KeyEvent event )  {
    if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
    	Credit.this.LoadActivity(MainMenu.class, "karbarCode", karbarCode);
    }

    return super.onKeyDown( keyCode, event );
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		Credit.this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
}
