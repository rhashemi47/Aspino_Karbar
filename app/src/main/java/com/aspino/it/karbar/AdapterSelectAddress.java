package com.aspino.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterSelectAddress extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;
    private String backToActivity;
    private ViewHolder holder;

    public AdapterSelectAddress(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;

        this.karbarCode = karbarCode;
        this.backToActivity = backToActivity;
    }

    // @Override
    public int getCount() {
        return list.size();
    }

    // @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtTitleAddress;
        TextView txtContentAddress;
        LinearLayout LinearAddress;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/BMitra.ttf");
            convertView = inflater.inflate(R.layout.list_item_address, null);
            holder = new ViewHolder();
            holder.LinearAddress = (LinearLayout) convertView.findViewById(R.id.LinearAddress);
            holder.txtTitleAddress = (TextView) convertView.findViewById(R.id.txtTitleAddress);
            holder.txtTitleAddress.setTypeface(faceh);
            holder.txtTitleAddress.setTextSize(24);
            holder.txtContentAddress = (TextView) convertView.findViewById(R.id.txtContentAddress);
            holder.txtContentAddress.setTypeface(faceh);
            holder.txtContentAddress.setTextSize(24);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String TitleAddress = map.get("TitleAddress");
        String ContentAddress = map.get("ContentAddress");
        String code = map.get("Code");
        holder.txtTitleAddress.setText(TitleAddress);
        holder.txtContentAddress.setText(ContentAddress);
        holder.LinearAddress.setTag(code);
        holder.LinearAddress.setOnClickListener(ItemOnclick);

        return convertView;
    }


    private OnClickListener ItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dbh=new DatabaseHelper(activity.getApplicationContext());
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

            String AddressCode="";
            AddressCode = ((LinearLayout)v).getTag().toString();
            String query="INSERT INTO address_select (Code) VALUES('"+AddressCode+ "')";
            db=dbh.getWritableDatabase();
            db.execSQL("DELETE FROM address_select");
            db.execSQL(query);
            holder.LinearAddress.setBackgroundColor(Color.parseColor("#9877ee"));
//            Intent intent = new Intent(activity.getApplicationContext(),UpdateAddress.class);
//
//            intent.putExtra("karbarCode",karbarCode);
//            intent.putExtra("AddressCode",AddressCode);
//            intent.putExtra("backToActivity",backToActivity);
//            activity.startActivity(intent);
        }
    };
}

