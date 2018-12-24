package  com.aspino.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetUserAddress {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String pUserCode;
	private String WsResponse;
	private String Flag;
	//*******************
	private String AddressCode;
	private String DetailCode;
	private String FromDate;
	private String ToDate;
	private String FromTime;
	private String ToTime;
	private String Description;
	private String TimeDiff;
	private String MaleCount;
	private String FemaleCount;
	private String HamyarCount;
	//private String acceptcode;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetUserAddress(Activity activity,
							  String pUserCode,
							  String Flag,
							  String AddressCode,
							  String DetailCode,
							   String FromDate,
							   String ToDate,
							   String FromTime,
							   String ToTime,
							   String Description,
							   String TimeDiff,
							   String MaleCount,
							   String FemaleCount,
							   String HamyarCount) {
		this.activity = activity;
		this.pUserCode = pUserCode;
		this.Flag = Flag;
		this.AddressCode=AddressCode;
		this.DetailCode=DetailCode;
		this.FromDate=FromDate;
		this.ToDate=ToDate;
		this.FromTime=FromTime;
		this.ToTime=ToTime;
		this.Description=Description;
		this.TimeDiff=TimeDiff;
		this.MaleCount=MaleCount;
		this.FemaleCount=FemaleCount;
		this.HamyarCount=HamyarCount;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
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
	}
	
	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			}	
			 catch (Exception e) {
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
			 }
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;
		
		public AsyncCallWS(Activity activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("GetUserAddress");
        	}
	    	catch (Exception e) {
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
	            }
        	}
        	else
        	{
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {}
        }
 
        @Override
        protected void onPreExecute() {
        	if(CuShowDialog)
        	{
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
	        androidHttpTransport.call("http://tempuri.org/"+METHOD_NAME, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to FinalResultForCheck static variable
	        WsResponse = response.toString();	
	        if(WsResponse == null) WsResponse="ER";
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
		db.execSQL("DELETE FROM address");
		for(int i=0;i<res.length;i++){
			value=res[i].split("##");
			db.execSQL("INSERT INTO address (" +
					"Code," +
					"UserCode," +
					"IsDefault," +
					"Name," +
					"State," +
					"City," +
					"AddressText," +
					"Email," +
					"Lat," +
					"Lng," +
					"Status) VALUES('"+
						  value[0]+
					"','"+value[1]+
					"','"+value[2]+
					"','"+value[3]+
					"','"+value[4]+
					"','"+value[5]+
					"','"+value[6]+
					"','"+value[7]+
					"','"+value[8]+
					"','"+value[9]+
					"','"+value[10]+
					"')");
		}
		if(db.isOpen()){
			db.close();
		}
		if(this.Flag.compareTo("1")==0) {
			Toast.makeText(activity, "آدرس ثبت شد.", Toast.LENGTH_LONG).show();
			String query="INSERT INTO address_select (Code) VALUES('"+AddressCode+ "')";
			try {
				if (!db.isOpen()) {
					db = dbh.getWritableDatabase();
				}
			}
			catch (Exception ex)
			{
				db = dbh.getWritableDatabase();
			}
			db.execSQL("DELETE FROM address_select");
			db.execSQL(query);if(db.isOpen()){db.close();}
			if(db.isOpen()){
				db.close();
			}
			LoadActivity(Service_Request_OffCode.class, "karbarCode", pUserCode,
					"DetailCode", DetailCode,
					"FromDate", FromDate,
					"ToDate", ToDate,
					"FromTime", FromTime,
					"ToTime", ToTime,
					"Description", Description,
					"TimeDiff", TimeDiff,
					"MaleCount", MaleCount,
					"FemaleCount", FemaleCount,
					"HamyarCount", HamyarCount
			);
		}
    }
	public void LoadActivity(Class<?> Cls, String VariableName1, String VariableValue1,
							  String VariableName2, String VariableValue2,
							  String VariableName3, String VariableValue3,
							  String VariableName4, String VariableValue4,
							  String VariableName5, String VariableValue5,
							  String VariableName6, String VariableValue6,
							  String VariableName7, String VariableValue7,
							  String VariableName8, String VariableValue8,
							  String VariableName9, String VariableValue9,
							  String VariableName10, String VariableValue10,
							  String VariableName11, String VariableValue11)
	{
		Intent intent = new Intent(this.activity,Cls);
		intent.putExtra(VariableName1, VariableValue1);
		intent.putExtra(VariableName2, VariableValue2);
		intent.putExtra(VariableName3, VariableValue3);
		intent.putExtra(VariableName4, VariableValue4);
		intent.putExtra(VariableName5, VariableValue5);
		intent.putExtra(VariableName6, VariableValue6);
		intent.putExtra(VariableName7, VariableValue7);
		intent.putExtra(VariableName8, VariableValue8);
		intent.putExtra(VariableName9, VariableValue9);
		intent.putExtra(VariableName10, VariableValue10);
		intent.putExtra(VariableName11, VariableValue11);
		activity.startActivity(intent);
	}
}
