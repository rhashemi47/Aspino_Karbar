package  com.aspino.it.karbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetUserServicesHamyarRequest {
	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String pUserCode;
	private String WsResponse;
	private String LastRequestCode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetUserServicesHamyarRequest(Context activity, String pUserCode, String LastRequestCode) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.LastRequestCode = LastRequestCode;
		PublicVariable.thread_RequestHamyar=false;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();

		dbh = new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {
			PublicVariable.thread_RequestHamyar = true;
			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {
			PublicVariable.thread_RequestHamyar = true;
			throw sqle;
		}
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
				PublicVariable.thread_RequestHamyar = true;
				e.printStackTrace();
			}
		} else {
			PublicVariable.thread_RequestHamyar = true;
			//Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetUserServicesHamyarRequest");
			} catch (Exception e) {
				PublicVariable.thread_RequestHamyar = true;
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			PublicVariable.thread_RequestHamyar = true;
			if (result == null) {
				PublicVariable.thread_RequestHamyar=true;
				if (WsResponse.toString().compareTo("ER") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (WsResponse.toString().compareTo("0") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (WsResponse.toString().compareTo("2") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد", Toast.LENGTH_LONG).show();
				}
				else
				{
					InsertDataFromWsToDb(WsResponse);
				}
			} else {
				//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
				PublicVariable.thread_RequestHamyar = true;
			}
		}

		@Override
		protected void onPreExecute() {
			if (CuShowDialog) {
				this.dialog.setMessage("در حال پردازش");
				this.dialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	public void CallWsMethod(String METHOD_NAME) {
		//Create request
		SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
		PropertyInfo pUserCodePI = new PropertyInfo();
		//Set Name
		pUserCodePI.setName("UserCode");
		//Set Value
		pUserCodePI.setValue(pUserCode);
		//Set dataType
		pUserCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserCodePI);
		//****************************************************************
		PropertyInfo LastUserServiceCodePI = new PropertyInfo();
		//Set Name
		LastUserServiceCodePI.setName("LastRequestCode");
		//Set Value
		LastUserServiceCodePI.setValue(LastRequestCode);
		//Set dataType
		LastUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastUserServiceCodePI);
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Set output SOAP object
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(PV.URL);
		try {
			//Invoke web service
			androidHttpTransport.call("http://tempuri.org/" + METHOD_NAME, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			//Assign it to FinalResultForCheck static variable
			WsResponse = response.toString();
			if (WsResponse == null) WsResponse = "ER";
		} catch (Exception e) {
			WsResponse = "ER";
			e.printStackTrace();
		}
	}


	public void InsertDataFromWsToDb(String AllRecord) {
		String[] res;
		String[] value;
		res = WsResponse.split("@@");
		try {
			if (!db.isOpen()) {
				db = dbh.getWritableDatabase();
			}
		}
		catch (Exception ex)
		{
			db = dbh.getWritableDatabase();
		}
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			if (!check1(value[1], value[2])) {
				try {
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}
				catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
				String query = "INSERT INTO UserServicesHamyarRequest (" +
						"Code," +
						"BsUserServicesCode," +
						"HamyarCode," +
						"Price," +
						"HmayarStar," +
						"PriceFinal," +
						"PriceOff," +
						"TotalPrice) VALUES('" +
						value[0] + "','" +
						value[1] + "','" +
						value[2] + "','" +
						value[3] + "','" +
						value[4] + "','" +
						value[5] + "','" +
						value[6] + "','" +
						value[7] + "')";
				db.execSQL(query);if(db.isOpen()){db.close();}
				if(db.isOpen())
				{
					db.close();
				}
			}
			else
			{
				try {
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}
				catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
				String query="";
				if(check_PriceFinal(value[0],value[2],value[5])) {
					query = "UPDATE UserServicesHamyarRequest SET " +
							"Code='" + value[0] + "' , " +
							"BsUserServicesCode='" + value[1] + "' , " +
							"HamyarCode='" + value[2] + "' , " +
							"Price='" + value[3] + "' , " +
							"HmayarStar='" + value[4] + "' , " +
							"PriceFinal='" + value[5] + "' , " +
							"PriceOff='" + value[6] + "' , " +
							"ConfirmFirst='" + value[8] + "' , " +
							"ConfirmSecond='" + value[9] + "' , " +
							"TotalPrice='" + value[7] + "' " +
							"WHERE BsUserServicesCode='" + value[1] +
							"' AND HamyarCode='" + value[2] + "'";
				}
				else
				{

					query = "UPDATE UserServicesHamyarRequest SET " +
							"Code='" + value[0] + "' , " +
							"BsUserServicesCode='" + value[1] + "' , " +
							"HamyarCode='" + value[2] + "' , " +
							"HmayarStar='" + value[4] + "' , " +
							"PriceFinal='" + value[5] + "' , " +
							"PriceOff='" + value[6] + "' , " +
							"TotalPrice='" + value[7] + "' , " +
							"ConfirmFirst='" + value[8] + "' , " +
							"ConfirmSecond='" + value[9] + "'" +
							" WHERE BsUserServicesCode='" + value[1] +
							"' AND HamyarCode='" + value[2] + "'";
				}
				try {
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}
				catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
				db.execSQL(query);if(db.isOpen()){db.close();}
				if(db.isOpen())
				{
					db.close();
				}
			}
			if(!check2(value[2]))
			{
				SyncGetUserServiceHamyarPic syncGetUserServiceHamyarPic=new SyncGetUserServiceHamyarPic(activity,value[2],pUserCode);
				syncGetUserServiceHamyarPic.AsyncExecute();
			}
			else
			{
				SyncGetHamyarProfile syncGetHamyarProfile=new SyncGetHamyarProfile(activity,pUserCode,value[2]);
				syncGetHamyarProfile.AsyncExecute();
			}
			if(!check3(value[2],value[1]))
			{
				try {
					if (!db.isOpen()) {
						db = dbh.getWritableDatabase();
					}
				}
				catch (Exception ex)
				{
					db = dbh.getWritableDatabase();
				}
				String 	query = "INSERT INTO Hamyar (" +
						"CodeHamyarInfo," +
						"CodeOrder" +
						") VALUES('" +
						value[2] + "','" +
						value[1] + "')";
				db.execSQL(query);if(db.isOpen()){db.close();}
				if(db.isOpen())
				{
					db.close();
				}
			}
		}
		if(db.isOpen()) {
			db.close();
		}
	}
	public boolean check1(String CodeOrder,String HamyarCode)
	{
		try {
			if (!db.isOpen()) {
				db=dbh.getReadableDatabase();
			}
		}
		catch (Exception ex)
		{
			db=dbh.getReadableDatabase();
		}

		String query = "SELECT * FROM UserServicesHamyarRequest WHERE BsUserServicesCode='" + CodeOrder +
				"' AND HamyarCode='" + HamyarCode+"'";
		Cursor cursor=db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			if(db.isOpen()) {
				db.close();
			}
			cursor.close();
			return true;
		}
		else
		{
			if(db.isOpen()) {
				db.close();
			}
			cursor.close();
			return false;
		}
	}
	public boolean check_PriceFinal(String CodeRquest,String HamyarCode,String PriceFinal)
	{
		try {
			if (!db.isOpen()) {
				db=dbh.getReadableDatabase();
			}
		}
		catch (Exception ex)
		{
			db=dbh.getReadableDatabase();
		}
		String query = "SELECT * FROM UserServicesHamyarRequest WHERE Code='" + CodeRquest +
				"' AND HamyarCode='" + HamyarCode+"' AND PriceFinal='"+PriceFinal+"'";
		Cursor cursor=db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return true;
		}
		else
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return false;
		}
	}
	public boolean check2(String Code)
	{
		try {
			if (!db.isOpen()) {
				db=dbh.getReadableDatabase();
			}
		}
		catch (Exception ex)
		{
			db=dbh.getReadableDatabase();
		}
		String query="SELECT * FROM InfoHamyar WHERE Code_InfoHamyar='"+Code+"'";
		Cursor cursor=db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return true;
		}
		else
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return false;
		}
	}
	public boolean check3(String Code,String OrderCode)
	{
		try {
			if (!db.isOpen()) {
				db=dbh.getReadableDatabase();
			}
		}
		catch (Exception ex)
		{
			db=dbh.getReadableDatabase();
		}
		String query="SELECT * FROM Hamyar WHERE CodeHamyarInfo='"+Code+"' AND CodeOrder='"+OrderCode+"'";
		Cursor cursor=db.rawQuery(query,null);
		if(cursor.getCount()>0)
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return true;
		}
		else
		{
			if(db.isOpen())
			{
				db.close();
			}
			cursor.close();
			return false;
		}
	}
}

