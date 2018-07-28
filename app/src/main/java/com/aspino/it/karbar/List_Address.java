	package com.aspino.it.karbar;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;

    import android.view.KeyEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ListView;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;

    import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

    public class List_Address extends Activity {
        private String karbarCode;
        private String backToActivity;
        private String codeService;
        private ListView lvAddress;
        private DatabaseHelper dbh;
        private SQLiteDatabase db;
        private Button btnAdd_New_Address;
//        private Button btnAcceptOrder;
//        private Button btncredite;
        private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu_listaddress);
            btnAdd_New_Address=(Button)findViewById(R.id.btnAdd_New_Address);
//            btnAcceptOrder=(Button)findViewById(R.id.btnAcceptOrderBottom);
//            btncredite=(Button)findViewById(R.id.btncrediteBottom);
            lvAddress=(ListView)findViewById(R.id.listViewAddress);
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
            try {
                codeService = getIntent().getStringExtra("codeService").toString();
            }
            catch (Exception e) {
                codeService = "";
            }
        try
        {
            karbarCode = getIntent().getStringExtra("karbarCode").toString();

        }
        catch (Exception e)
        {
            db=dbh.getReadableDatabase();
            Cursor coursors = db.rawQuery("SELECT * FROM login",null);
            for(int i=0;i<coursors.getCount();i++){
                coursors.moveToNext();

                karbarCode=coursors.getString(coursors.getColumnIndex("karbarCode"));
            }
            db.close();
        }
            try {
                backToActivity = getIntent().getStringExtra("nameActivity").toString();
            }
            catch (Exception e) {
                backToActivity = "";
            }
//        db=dbh.getReadableDatabase();
//        Cursor coursors = db.rawQuery("SELECT * FROM address WHERE Status='1'",null);
//        if(coursors.getCount()>0)
//        {
//            for(int i=0;i<coursors.getCount();i++){
//                coursors.moveToNext();
//
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("name","نام: "+coursors.getString(coursors.getColumnIndex("Name"))+"\n"
//                        +"آدرس: "+coursors.getString(coursors.getColumnIndex("AddressText")));
//                map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
//                valuse.add(map);
//            }
//            AdapterUpdateAddress dataAdapter=new AdapterUpdateAddress(List_Address.this,valuse,karbarCode,backToActivity);
//            lvAddress.setAdapter(dataAdapter);
//        }
            db=dbh.getReadableDatabase();
            Cursor cursorAddress = db.rawQuery("SELECT * FROM address WHERE Status='1'",null);
            if(cursorAddress.getCount()>0)
            {
                for(int i=0;i<cursorAddress.getCount();i++){
                    cursorAddress.moveToNext();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("TitleAddress",cursorAddress.getString(cursorAddress.getColumnIndex("Name")));
                    map.put("ContentAddress",cursorAddress.getString(cursorAddress.getColumnIndex("AddressText")));
                    map.put("Code",cursorAddress.getString(cursorAddress.getColumnIndex("Code")));
                    valuse.add(map);
                }
                AdapterUpdateAddress dataAdapter=new AdapterUpdateAddress(List_Address.this,valuse);
                lvAddress.setAdapter(dataAdapter);
            }
            db.close();
            btnAdd_New_Address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadActivity2(Map.class,"karbarCode", karbarCode,"nameActivity","List_Address");
                }
            });
        //**************************************************************************************************************
//            db=dbh.getReadableDatabase();
//            Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                    "LEFT JOIN " +
//                    "Servicesdetails ON " +
//                    "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
//            if (cursor2.getCount() > 0) {
//                btnOrder.setText("درخواست ها: " + cursor2.getCount());
//            }
//            cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
//            if (cursor2.getCount() > 0) {
//                btnAcceptOrder.setText("پذیرفته شده ها: " + cursor2.getCount());
//            }
//            cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//            if (cursor2.getCount() > 0) {
//                cursor2.moveToNext();
//                try {
//			String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//			if(splitStr[1].compareTo("00")==0)
//			{
//				btncredite.setText("اعتبار: " +splitStr[0]);
//			}
//			else
//			{
//				btncredite.setText("اعتبار: " + cursor2.getString(cursor2.getColumnIndex("Amount")));
//			}
//
//		} catch (Exception ex) {
//			btncredite.setText("اعتبار: " + "0");
//		}
//            }
//            db.close();
//            btnOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String QueryCustom;
//                    QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                            "LEFT JOIN " +
//                            "Servicesdetails ON " +
//                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'";
//                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//                }
//            });
//            btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String QueryCustom;
//                    QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                            "LEFT JOIN " +
//                            "Servicesdetails ON " +
//                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13)";
//                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//                }
//            });
//            btncredite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    LoadActivity(Credit.class, "karbarCode", karbarCode);
//                }
//            });
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if(backToActivity.compareTo("Profile")==0){
                LoadActivity(Profile.class, "karbarCode", karbarCode);
            }else
            {
                LoadActivity2(MainMenu.class, "karbarCode", karbarCode,"codeService",codeService);
            }
        }

        return super.onKeyDown( keyCode, event );
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue)
        {
            Intent intent = new Intent(getApplicationContext(),Cls);
            intent.putExtra(VariableName, VariableValue);

            List_Address.this.startActivity(intent);
        }
        public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
                , String VariableName2, String VariableValue2) {
            Intent intent = new Intent(getApplicationContext(), Cls);
            intent.putExtra(VariableName, VariableValue);
            intent.putExtra(VariableName2, VariableValue2);

            this.startActivity(intent);
        }
    }
