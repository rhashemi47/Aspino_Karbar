package  com.aspino.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterGridSelectHamyar extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private String karbarCode;

    public AdapterGridSelectHamyar(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
        super();
        this.activity = activity;
        this.list = list;

        this.karbarCode = karbarCode;
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
        TextView textViewContactName;
        TextView tvRateNumber;
        TextView textViewUnreadCount;
        Button buttonInvite;
        ImageView imageViewContactLogo;
        LinearLayout LinearHamyar;
        RatingBar RatingHamyar;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        Typeface vazir = Typeface.createFromAsset(activity.getAssets(), "font/vazir.ttf");
        Typeface vazir_medium = Typeface.createFromAsset(activity.getAssets(), "font/vazir_medium.ttf");
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.list_recycle_item_hamyar, null);

            holder = new ViewHolder();
            holder.textViewContactName= (TextView) convertView.findViewById(R.id.textViewContactName);
            holder.tvRateNumber= (TextView) convertView.findViewById(R.id.tvRateNumber);
            holder.textViewUnreadCount= (TextView) convertView.findViewById(R.id.textViewUnreadCount);
            holder.buttonInvite= (Button) convertView.findViewById(R.id.buttonInvite);
            holder.imageViewContactLogo= (ImageView) convertView.findViewById(R.id.imageViewContactLogo);
            holder.LinearHamyar= (LinearLayout) convertView.findViewById(R.id.LinearHamyar);
            holder.RatingHamyar= (RatingBar) convertView.findViewById(R.id.RatingHamyar);
            //****************************************
            holder.textViewContactName.setTypeface(vazir_medium);
            holder.tvRateNumber.setTypeface(vazir);
            holder.textViewUnreadCount.setTypeface(vazir);
            //****************************************
            holder.textViewContactName.setTextSize(14);
            holder.tvRateNumber.setTextSize(14);
            holder.textViewUnreadCount.setTextSize(14);
            convertView.setTag(holder);
        }
        else
        {
            holder = (AdapterGridSelectHamyar.ViewHolder) convertView.getTag();
        }
        String name = map.get("name");
        String code = map.get("Code");
//        holder.txtValues.setText(name);todo
//        holder.txtValues.setTag(code);
//        holder.imgValues.setTag(code);
        dbh=new DatabaseHelper(activity);
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
        try { if(!db.isOpen()) { try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}}}	catch (Exception ex){	 db = dbh.getReadableDatabase();}
        Cursor cursor = db.rawQuery("SELECT * FROM services WHERE code='"+code+"'",null);
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            if(cursor.getString(cursor.getColumnIndex("Pic")).length()>5){
                holder.imageViewContactLogo.setImageBitmap(convertToBitmap(cursor.getString(cursor.getColumnIndex("Pic"))));
            }
            else
            {
                holder.imageViewContactLogo.setImageResource(R.drawable.job);
            }
            if(!cursor.isClosed()) {
            cursor.close();
            }
            if(db.isOpen()) {
                db.close();
            }

        }
        else
        {

            if(!cursor.isClosed()) {
                cursor.close();
            }
            if(db.isOpen()) {
                db.close();
            }
            holder.imageViewContactLogo.setImageResource(R.drawable.job);
        }
//        holder.txtValues.setOnClickListener(TextViewItemOnclick);
//        holder.imgValues.setOnClickListener(ImageViewItemOnclick);todo

        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String codeService="";
            codeService = ((TextView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),List_ServiceDerails.class);//Goto Page Form Order Service
            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("codeService",codeService);
            activity.startActivity(intent);
        }
    };
    private OnClickListener ImageViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String codeService="";
            codeService = ((ImageView)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),List_ServiceDerails.class);//Goto Page Form Order Service
            intent.putExtra("karbarCode",karbarCode);
            intent.putExtra("codeService",codeService);
            activity.startActivity(intent);
        }
    };
    public Bitmap convertToBitmap(String base){
        Bitmap Bmp=null;
        try
        {
            byte[] decodedByte = Base64.decode(base, Base64.DEFAULT);
            Bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
//
            return Bmp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Bmp;
        }
    }
}

