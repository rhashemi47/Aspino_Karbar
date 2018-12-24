package  com.aspino.it.karbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by hashemi on 10/08/2018.
 */

class SyncGetHamyarProfile {
    //Primary Variable
    DatabaseHelper dbh;
    SQLiteDatabase db;
    PublicVariable PV;
    InternetConnection IC;
    private Context activity;
    private String HamyarCode;
    private String UserCode;
    private String WsResponse;
    private boolean CuShowDialog = false;

    //Contractor
    public SyncGetHamyarProfile(Context activity, String UserCode, String HamyarCode) {
        this.activity = activity;
        this.HamyarCode = HamyarCode;
        this.UserCode = UserCode;

        IC = new InternetConnection(this.activity.getApplicationContext());
        PV = new PublicVariable();

        dbh = new DatabaseHelper(this.activity.getApplicationContext());
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

    public void AsyncExecute() {
        if (IC.isConnectingToInternet() == true) {
            try {
                SyncGetHamyarProfile.AsyncCallWS task = new SyncGetHamyarProfile.AsyncCallWS(this.activity);
                task.execute();
            } catch (Exception e) {
                //Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
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
                CallWsMethod("GetHamyarProfile");
            } catch (Exception e) {
                result = e.getMessage().toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
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
        PropertyInfo HamyarCodePI = new PropertyInfo();
        //Set Name
        HamyarCodePI.setName("HamyarCode");
        //Set Value
        HamyarCodePI.setValue(HamyarCode);
        //Set dataType
        HamyarCodePI.setType(String.class);
        //Add the property to request object
        request.addProperty(HamyarCodePI);
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
        String query;
        String value[] =WsResponse.split("##");
        query = "UPDATE InfoHamyar SET " +
                "Fname='" + value[0] + "' , " +
                "Lname='" +value[1] + "', " +
                "Mobile='" +value[2] + "', " +
                "BthDate='" +value[3] + "', " +
                "CourseCode='" +value[4] + "', " +
                "HmServices='" +value[5] + "', " +
                "WorkHistoryInMonth='" +value[6] + "', " +
                "WorkHistoryAllYear='" +0 + "', " +//todo value[7]<-------
                "HmayarStar='" +value[7].replace("@@","") + "' WHERE Code_InfoHamyar='" + HamyarCode + "'";
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
        SyncGetHamyarComment syncGetHamyarComment=new SyncGetHamyarComment(activity,UserCode,HamyarCode);
        syncGetHamyarComment.AsyncExecute();
    }
}
