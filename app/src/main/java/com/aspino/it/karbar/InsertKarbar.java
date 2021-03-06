package  com.aspino.it.karbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InsertKarbar {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String karbarCode;
	private String phonenumber;
	private String Name;
	private String Family;
	private String acceptcode;
	private String WsResponse;
	private String ReagentCode;
	private boolean CuShowDialog=true;
	private String[] res;
	private String LastMessageCode;
	private String CityCodeLocation;
	private double lat;
	private double lon;
	private GPSTracker gps;

	//Contractor
	public InsertKarbar(Activity activity, String phonenumber, String acceptcode, String Name, String Family,String ReagentCode,String CityCodeLocation) {
		this.activity = activity;
		this.phonenumber = phonenumber;		
		this.acceptcode=acceptcode;
		this.Name=Name;
		this.Family=Family;
		this.ReagentCode=ReagentCode;
		this.CityCodeLocation=CityCodeLocation;

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
		gps = new GPSTracker(this.activity.getApplicationContext());

		// check if GPS enabled
		if(gps.canGetLocation())
		{
			lat = gps.getLatitude();
			lon = gps.getLongitude();
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
		    this.dialog = new ProgressDialog(activity);
		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertPrimaryProfile");
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
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	         
	            else
	            {
	            	karbarCode=res[0];
	            	InsertDataFromWsToDb(res);
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
	    PropertyInfo phoneNuberPI = new PropertyInfo();
	    //Set Name
	    phoneNuberPI.setName("PhoneNumber");
	    //Set Value
	    phoneNuberPI.setValue(this.phonenumber);
	    //Set dataType
	    phoneNuberPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(phoneNuberPI);
	    PropertyInfo acceptcodePI = new PropertyInfo();
	    //Set Name
	    acceptcodePI.setName("AcceptCode");
	    //Set Value
	    acceptcodePI.setValue(this.acceptcode);
	    //Set dataType
	    acceptcodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(acceptcodePI);	
	    PropertyInfo NamePI = new PropertyInfo();
	    //Set Name
	    NamePI.setName("Name");
	    //Set Value
	    NamePI.setValue(this.Name);
	    //Set dataType
	    NamePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(NamePI);
	    //
	    PropertyInfo FamilyPI = new PropertyInfo();
	    //Set Name
	    FamilyPI.setName("Family");
	    //Set Value
	    FamilyPI.setValue(this.Family);
	    //Set dataType
	    FamilyPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(FamilyPI);
	    //*********
	    PropertyInfo ReagentCodePI = new PropertyInfo();
	    //Set Name
		ReagentCodePI.setName("ReagentCodeStr");
	    //Set Value
		ReagentCodePI.setValue(this.ReagentCode);
	    //Set dataType
		ReagentCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(ReagentCodePI);
	    
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
	
	
	public void InsertDataFromWsToDb(String[] AllRecord)
    {
    	String notext="";
		try { if(!db.isOpen()) { db=dbh.getWritableDatabase();}}	catch (Exception ex){	db=dbh.getWritableDatabase();	}
		db.execSQL("DELETE FROM login");
		db.execSQL("DELETE FROM Profile");
		db.execSQL("INSERT INTO login (karbarCode,islogin,Phone,AcceptCode) VALUES('"+karbarCode+"','1','"+phonenumber+"','"+acceptcode+"')");
		db.execSQL("INSERT INTO Profile " +
				"(Code,Name,Fam,BthDate,ShSh,BirthplaceCode,Sader,StartDate,Address,Tel,Mobile,ReagentName,AccountNumber,HamyarNumber,IsEmrgency,Status) " +
				"VALUES" +
				"('"+karbarCode+
				"','"+Name+
				"','"+Family+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+
				"','"+notext+"','"+notext+"','"+notext+"','"+notext+"','"+notext+"')");
		try { if(!db.isOpen()) { db=dbh.getReadableDatabase();}}	catch (Exception ex){	db=dbh.getReadableDatabase();	}

		Cursor cursors = db.rawQuery("SELECT ifnull(MAX(CAST (code AS INT)),0)as code FROM messages", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			LastMessageCode=cursors.getString(cursors.getColumnIndex("code"));
		}
		if(db.isOpen()) {
			db.close();
		}
		if(!cursors.isClosed()) {
			cursors.close();
		}
//		SyncMessage syncMessage=new SyncMessage(this.activity, karbarCode,LastMessageCode);
//		syncMessage.AsyncExecute();
		SyncProfile syncProfile=new SyncProfile(this.activity, karbarCode);
		syncProfile.AsyncExecute();
		SyncState syncState=new SyncState(this.activity);
		syncState.AsyncExecute();
		SyncCity syncCity=new SyncCity(this.activity,CityCodeLocation);
		syncCity.AsyncExecute();
		SyncGetUserAddress syncGetUserAddress=new  SyncGetUserAddress(this.activity,karbarCode,"0","0","0","0","0","0","0","0","0","0","0","0");
		syncGetUserAddress.AsyncExecute();
    }
private  String getStringLocation()
{
	Locale locale = new Locale("fa");

	Geocoder geocoder = new Geocoder(this.activity.getApplicationContext(), locale);

	List<Address> list;

	try {
		lat = gps.getLatitude();
		lon = gps.getLongitude();
		list = geocoder.getFromLocation(lat, lon, 2);

		Address address = list.get(0);

		if(address.getSubLocality()!=null)
		{
			return address.getLocality();
		}
		else {
			return address.getThoroughfare();
		}

	} catch (IOException e) {
		e.printStackTrace();
		return "";
	}

	catch (Exception e){
		return "";
	}
}
}
