package com.aspino.it.karbar;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Service_Run extends Fragment {


    private ListView lstServiceRun;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private ArrayList<HashMap<String ,String>> valuse=new ArrayList<HashMap<String, String>>();
    private String karbarCode;

    public Fragment_Service_Run() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment__service__run, container, false);
        ListView lstServiceRun = (ListView)rootView.findViewById(R.id.lstServiceRun);
        dbh=new DatabaseHelper(getContext());
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
        db=dbh.getReadableDatabase();
        Cursor coursors,C;
            coursors = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name,address.AddressText FROM OrdersService  " +
                    "LEFT JOIN Servicesdetails " +
                    "ON Servicesdetails.code=OrdersService.ServiceDetaileCode " +
                    "LEFT JOIN address " +
                    "ON OrdersService.AddressCode=address.Code " +
                    "WHERE OrdersService.Status ='1' OR OrdersService.Status ='0'", null);
        for(int i=0;i<coursors.getCount();i++){
            coursors.moveToNext();
            HashMap<String, String> map = new HashMap<String, String>();
            String StartDate;
            String StartTime;
            C=db.rawQuery("SELECT * FROM UserServicesHamyarRequest WHERE BsUserServicesCode='"+coursors.getString(coursors.getColumnIndex("Code"))+"'",null);
            map.put("NameHamyar",String.valueOf(C.getCount()));
            if(C.getCount()>0) {
                C.moveToNext();
                map.put("CodeHamyarRequest", C.getString(C.getColumnIndex("Code")));
            }
            else
            {
                map.put("CodeHamyarRequest", "0");
            }
            StartDate=coursors.getString(coursors.getColumnIndex("StartYear"))+"/"+
                    coursors.getString(coursors.getColumnIndex("StartMonth"))+"/"+
                    coursors.getString(coursors.getColumnIndex("StartDay"));
            StartTime=coursors.getString(coursors.getColumnIndex("StartHour"))+":"+
                    coursors.getString(coursors.getColumnIndex("StartMinute"));
//            EndTime=coursors.getString(coursors.getColumnIndex("EndHour"))+":"+
//                    coursors.getString(coursors.getColumnIndex("EndMinute"));
//            String StrStatus="";
//            switch (coursors.getString(coursors.getColumnIndex("Status")))
//            {
//                case "0":
//                    StrStatus="آزاد";
//                    break;
//                case "1":
//                    StrStatus="در نوبت انجام";
//                    break;
//                case "2":
//                    StrStatus="در حال انجام";
//                    break;
//                case "3":
//                    StrStatus="لغو شده";
//                    break;
//                case "4":
//                    StrStatus="اتمام و تسویه شده";
//                    break;
//                case "5":
//                    StrStatus="اتمام و تسویه نشده";
//                    break;
//                case "6":
//                    StrStatus="اعلام شکایت";
//                    break;
//                case "7":
//                    StrStatus="درحال پیگیری شکایت و یا رفع خسارت";
//                    break;
//                case "8":
//                    StrStatus="رفع عیب و خسارت شده توسط متخصص";
//                    break;
//                case "9":
//                    StrStatus="پرداخت خسارت";
//                    break;
//                case "10":
//                    StrStatus="پرداخت جریمه";
//                    break;
//                case "11":
//                    StrStatus="تسویه حساب با متخصص";
//                    break;
//                case "12":
//                    StrStatus="متوقف و تسویه شده";
//                    break;
//                case "13":
//                    StrStatus="متوقف و تسویه نشده";
//                    break;
//            }
            map.put("Code",coursors.getString(coursors.getColumnIndex("Code")));
            map.put("DateAndTimeService",StartDate + " ساعت " +StartTime);
            map.put("TitleService",coursors.getString(coursors.getColumnIndex("name")));
            map.put("AddresService",coursors.getString(coursors.getColumnIndex("AddressText")));
            map.put("Status",coursors.getString(coursors.getColumnIndex("Status")));

            valuse.add(map);
        }
        db.close();

        AdapterListViewPager dataAdapter=new AdapterListViewPager(getActivity(),valuse);
        lstServiceRun.setAdapter(dataAdapter);
        return rootView;

    }

}
