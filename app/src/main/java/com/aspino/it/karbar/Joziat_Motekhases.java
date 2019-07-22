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
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.BreakIterator;

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
	private String CodeHamyarRequest;
	private String strCall;
	private String Confirm="0";
	private String FinalCurrency="0";
	private int REQUEST_CODE_ASK_PERMISSIONS=123;
	private String HamyarCode;
	private String CodeMotkhases;
	private String OrderCode;
	private LinearLayout LinearComments;
	private String StrRatingHamyar;
	private RatingBar RatingHamyar;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.joziat_motekhases);
	imgHamyar=(ImageView) findViewById(R.id.imgHamyar);
	imgCall=(ImageView) findViewById(R.id.imgCall);
	imgMessage=(ImageView) findViewById(R.id.imgMessage);
	tvCommentSaved=(TextView) findViewById(R.id.tvCommentSaved);
	LinearComments=(LinearLayout) findViewById(R.id.LinearComments);
	tvHistoryJob=(TextView) findViewById(R.id.tvHistoryJob);
	tvFirstname=(TextView) findViewById(R.id.tvFirstname);
	tvLastname=(TextView) findViewById(R.id.tvLastname);
	tvBirthday=(TextView) findViewById(R.id.tvBirthday);
	tvDegreeOfEducation=(TextView) findViewById(R.id.tvDegreeOfEducation);
	tvBaseExpert=(TextView) findViewById(R.id.tvBaseExpert);
	tvHistoryJobAll=(TextView) findViewById(R.id.tvHistoryJobAll);
	tvComment_Hamyar=(TextView) findViewById(R.id.tvComment_Hamyar);
	btnSelectThisHamyar=(Button) findViewById(R.id.btnSelectThisHamyar);
	RatingHamyar=(RatingBar) findViewById(R.id.RatingHamyar);
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
		try { if(!db.isOpen()) { db=dbh.getReadableDatabase();}}	catch (Exception ex){	db=dbh.getReadableDatabase();	}
		Cursor cursor = db.rawQuery("SELECT * FROM login",null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToNext();
			karbarCode=cursor.getString(cursor.getColumnIndex("karbarCode"));
		}
		if(db.isOpen()) {
			db.close();
		}
		if(!cursor.isClosed()) {
			cursor.close();
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
		StrRatingHamyar = getIntent().getStringExtra("RatingHamyar").toString();
	}
	catch (Exception e)
	{
		StrRatingHamyar="0";
	}
	RatingHamyar.setRating(Float.valueOf(StrRatingHamyar));
	String Query="SELECT A.HamyarCode,A.Code ReqCode,A.BsUserServicesCode ,B.Mobile,B.Fname,B.Lname,B.img,B.BthDate,B.CourseCode,B.HmServices,B.WorkHistoryInMonth,B.WorkHistoryAllYear" +
//			",B.CommentToUser" +
			" FROM UserServicesHamyarRequest A" +
			" LEFT JOIN " +
			" (SELECT max(Code_InfoHamyar)Code_InfoHamyar, max(Mobile)Mobile, max(Fname)Fname,max(img)img,max(HmayarStar)HmayarStar,max(Lname)Lname ,max(BthDate)BthDate ,max(CourseCode)CourseCode ,max(HmServices)HmServices ,max(WorkHistoryInMonth)WorkHistoryInMonth,max(WorkHistoryAllYear)WorkHistoryAllYear" +
			" FROM InfoHamyar group by Code_InfoHamyar) B ON " +
			" A.HamyarCode=B.Code_InfoHamyar" +
			" WHERE A.Code='"+CodeHamyarRequest+"'";
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
	Cursor cursor = db.rawQuery(Query, null);
	if(cursor.getCount()>0){
		cursor.moveToNext();
		imgHamyar.setImageBitmap(convertToBitmap(cursor.getString(cursor.getColumnIndex("img"))));
		strCall=cursor.getString(cursor.getColumnIndex("Mobile"));
		HamyarCode=cursor.getString(cursor.getColumnIndex("HamyarCode"));
		tvFirstname.setText(cursor.getString(cursor.getColumnIndex("Fname")));
		tvLastname.setText(cursor.getString(cursor.getColumnIndex("Lname")));
		tvBirthday.setText(cursor.getString(cursor.getColumnIndex("BthDate")));
		tvDegreeOfEducation.setText(cursor.getString(cursor.getColumnIndex("CourseCode")));
		tvBaseExpert.setText(cursor.getString(cursor.getColumnIndex("HmServices")));
		tvHistoryJobAll.setText(cursor.getString(cursor.getColumnIndex("WorkHistoryAllYear")));
		tvHistoryJob.setText(cursor.getString(cursor.getColumnIndex("WorkHistoryInMonth")));
		OrderCode=cursor.getString(cursor.getColumnIndex("BsUserServicesCode"));
//		tvComment_Hamyar.setText(cursor.getString(cursor.getColumnIndex("CommentToUser")));
		Cursor cursorComment=db.rawQuery("SELECT * FROM Comment WHERE HamyarCode='"+HamyarCode+"'",null);
		tvCommentSaved.setText(String.valueOf(cursorComment.getCount()));
		cursorComment.close();
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
	btnSelectThisHamyar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			SyncInsertFromHamyarRequestToHamyarAccept syncInsertFromHamyarRequestToHamyarAccept = new SyncInsertFromHamyarRequestToHamyarAccept(Joziat_Motekhases.this,
					CodeHamyarRequest);
			syncInsertFromHamyarRequestToHamyarAccept.AsyncExecute();
		}
	});
	LinearComments.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			LoadActivity2(Comment_Customer.class,"HamyarCode",HamyarCode,
					"CodeHamyarRequest",CodeHamyarRequest,
					"StrRatingHamyar",StrRatingHamyar
			,"CodeMotkhases",CodeMotkhases);
		}
	});
}
//@Override
//public boolean onKeyDown( int keyCode, KeyEvent event )  {
//
//
//    return super.onKeyDown( keyCode, event );
//}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName1, String VariableValue1)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName1, VariableValue1);
		this.startActivity(intent);
	}
	public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue,
							  String VariableName1, String VariableValue1,
							  String VariableName2, String VariableValue2,
							  String VariableName3, String VariableValue3)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName1, VariableValue1);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
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
			LoadActivity(Select_Hamyar.class, "karbarCode", karbarCode, "OrderCode", OrderCode);//todo Send Order Code
	}
}
