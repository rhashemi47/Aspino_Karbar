package  com.aspino.it.karbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import android.graphics.Typeface;

public class Survey extends AppCompatActivity{
	private String karbarCode;
	private DatabaseHelper dbh;
	private SQLiteDatabase db;
	private String HamyarCode;
	private RatingBar ratingbar;
	private Button btnSaveRating;
	private EditText etDescriptionForHamyar;
	private ImageView imgRate;
	private float Rating_Value=0;
	private String OrderCode;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.survey);
	ratingbar=(RatingBar)findViewById(R.id.ratingbar);
	btnSaveRating=(Button) findViewById(R.id.btnSaveRating);
	etDescriptionForHamyar=(EditText) findViewById(R.id.etDescriptionForHamyar);
	imgRate=(ImageView) findViewById(R.id.imgRate);
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
	}
	try
	{
		HamyarCode = getIntent().getStringExtra("HamyarCode").toString();

	}
	catch (Exception e)
	{
		Toast.makeText(Survey.this,"Error HamyarCode",Toast.LENGTH_LONG).show();
	}
	try
	{
		OrderCode = getIntent().getStringExtra("OrderCode").toString();

	}
	catch (Exception e)
	{
		Toast.makeText(Survey.this,"Error OrderCode",Toast.LENGTH_LONG).show();
	}
	ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
		@Override
		public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
			Rating_Value=v;
			if(v<2)
			{
				imgRate.setImageResource(R.drawable.bad);
			}
			if(2<=v && v<3)
			{
				imgRate.setImageResource(R.drawable.indifferent);
			}
			if(3<=v && v<=5)
			{
				imgRate.setImageResource(R.drawable.happy);
			}
		}
	});
	btnSaveRating.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			//todo Send Rate
			SyncInsertUserServiceHamyarStar syncInsertUserServiceHamyarStar=new SyncInsertUserServiceHamyarStar(Survey.this,
					OrderCode,HamyarCode,String.valueOf(Rating_Value),
					etDescriptionForHamyar.getText().toString());
			syncInsertUserServiceHamyarStar.AsyncExecute();
		}
	});
}
public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
	{
		Intent intent = new Intent(getApplicationContext(),Cls);
		intent.putExtra(VariableName, VariableValue);

		this.startActivity(intent);
	}
}
