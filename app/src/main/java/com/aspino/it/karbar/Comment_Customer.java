package  com.aspino.it.karbar;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Comment_Customer extends AppCompatActivity {
	private String HamyarCode;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private TextView tvCountComment;
	private RatingBar RatingHamyar;
	private ListView lstComment;
	private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
	private String CodeHamyarRequest;
	private String CodeMotkhases;
	private String StrRatingHamyar;


	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.comment_customer);
	tvCountComment=(TextView)findViewById(R.id.tvCountComment);
	RatingHamyar=(RatingBar) findViewById(R.id.RatingHamyar);
	lstComment=(ListView) findViewById(R.id.lstComment);
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
		HamyarCode = getIntent().getStringExtra("HamyarCode").toString();
	}
	catch (Exception e)
	{
		try { if(!db.isOpen()) { try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}}}	catch (Exception ex){	 db = dbh.getReadableDatabase();}
		Cursor cursor = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			HamyarCode=cursor.getString(cursor.getColumnIndex("HamyarCode"));
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
		CodeHamyarRequest = getIntent().getStringExtra("CodeHamyarRequest").toString();

	}
	catch (Exception e)
	{
		CodeHamyarRequest="0";
	}
	try
	{
		CodeMotkhases = getIntent().getStringExtra("CodeMotkhases").toString();
	}
	catch (Exception e)
	{
		CodeMotkhases="0";
	}
	try
	{
		StrRatingHamyar = getIntent().getStringExtra("StrRatingHamyar").toString();
	}
	catch (Exception e)
	{
		StrRatingHamyar="0";
	}
	try
	{
		if(!db.isOpen())
		{
			db=dbh.getReadableDatabase();
		}
	}
	catch (Exception ex)
	{
		db=dbh.getReadableDatabase();
	}
	RatingHamyar.setRating(Float.valueOf(StrRatingHamyar));
	String Query="SELECT * FROM Comment WHERE HamyarCode='"+HamyarCode+"'";
	Cursor cursor = db.rawQuery(Query, null);
	tvCountComment.setText(String.valueOf(cursor.getCount()));
	for(int i=0;cursor.getCount()>i;i++){
		cursor.moveToNext();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ContentComment",cursor.getString(cursor.getColumnIndex("Comment")));
		map.put("UsernameComment",cursor.getString(cursor.getColumnIndex("NameFamily")));
		map.put("txtDateComment",cursor.getString(cursor.getColumnIndex("InsertDate")));
		map.put("Code_Comment",cursor.getString(cursor.getColumnIndex("Code_Comment")));
		valuse.add(map);
	}
	AdapterlstComment dataAdapter=new AdapterlstComment(this,valuse,HamyarCode);
	lstComment.setAdapter(dataAdapter);
	if(!cursor.isClosed())
	{
		cursor.close();
	}
	if(db.isOpen()) {
		db.close();
	}
}

public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue,
						 String VariableName1, String VariableValue1,
						 String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName1, VariableValue1);
		intent.putExtra(VariableName2, VariableValue2);
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
	@Override
	public void onBackPressed() {
			LoadActivity(Joziat_Motekhases.class, "HamyarCode", HamyarCode, "CodeMotkhases", CodeMotkhases,"CodeHamyarRequest",CodeHamyarRequest);//todo Send Order Code
	}
}
