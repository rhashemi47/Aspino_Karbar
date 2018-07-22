package com.aspino.it.karbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
//import android.widget.Toast;
//
//import com.mikepenz.materialdrawer.Drawer;
//import com.yalantis.colormatchtabs.colormatchtabs.adapter.ColorTabAdapter;
//import com.yalantis.colormatchtabs.colormatchtabs.colortabs.ColorMatchTabLayout;
//import com.yalantis.colormatchtabs.colormatchtabs.colortabs.ColorTabView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Paigiri extends AppCompatActivity {
	private String karbarCode;

	private DatabaseHelper dbh;
//	private TextView txtContent;
	private SQLiteDatabase db;
//	private Typeface FontMitra;
	private ViewPagerAdapter viewPagerAdapter;
	private ViewPager viewPager;
	private SmartTabLayout tabLayout;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.peigiri);
	viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
	viewPager=(ViewPager)findViewById(R.id.view_pager);
	tabLayout=(SmartTabLayout) findViewById(R.id.tab_layout);
	//**********************************

	//**********************************
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

//	FontMitra = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");//set font for page
//	txtContent=(TextView)findViewById(R.id.tvTextAbout);
//	txtContent.setTypeface(FontMitra);
	//set fragment class and name in adapter
	viewPagerAdapter.addFragment(new Fragment_Service_Run(),"درحال انجام");
	viewPagerAdapter.addFragment(new Fragment_Service_Done(),"انجام شده");
	viewPagerAdapter.addFragment(new Fragment_Service_Cansel(),"لغو شده");
	//set adapter to view pager in class peigiri
	viewPager.setAdapter(viewPagerAdapter);
	//set tablayout to view pager for show diferent page cansel and done and run listView
	tabLayout.setViewPager(viewPager);

//	for (int i = 0; i < tabLayout.getTabCount(); i++) {
//		//noinspection ConstantConditions
//		TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.item_tablayout,null);
//		tv.setTypeface(FontMitra);
//		tabLayout.getTabAt(i).setCustomView(tv);
//	}
//	tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//		@Override
//		public void onTabSelected(TabLayout.Tab tab) {
//			tab.setCustomView();
//		}
//
//		@Override
//		public void onTabUnselected(TabLayout.Tab tab) {
//
//		}
//
//		@Override
//		public void onTabReselected(TabLayout.Tab tab) {
//
//		}
//	});



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
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
			, String VariableName2, String VariableValue2) {
		Intent intent = new Intent(getApplicationContext(), Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName2, VariableValue2);

		this.startActivity(intent);
	}
}
