package com.aspino.it.karbar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterInfoHamyar extends RecyclerView.Adapter<AdapterInfoHamyar.ViewHolderCustom> {
    public ArrayList<HashMap<String, String>> itemRecycleList;
    Activity mActivity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    public AdapterInfoHamyar(Activity activity, ArrayList<HashMap<String, String>> itemRecycleList) {
        this.itemRecycleList = itemRecycleList;
        this.mActivity = activity;
    }

    @Override
    public ViewHolderCustom onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycle_item_hamyar, parent, false);
        return new ViewHolderCustom(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderCustom holder, int position) {
        HashMap<String, String> map = itemRecycleList.get(position);
        String Name = map.get("Name");
        String Code = map.get("Code");
        String imgHamyar = map.get("imgHamyar");
        String RateBar = map.get("RateBar");
        String RateNumber = map.get("RateNumber");
        String UnreadCount = map.get("UnreadCount");
        String CodeHamyarRequest = map.get("CodeHamyarRequest");
        holder.textViewContactName.setText(Name);
        if (UnreadCount.compareTo("0") == 0) {
            holder.textViewUnreadCount.setText("توافقی");
        } else {
            holder.textViewUnreadCount.setText(UnreadCount);
        }
        holder.tvRateNumber.setText(RateNumber);
        holder.RatingHamyar.setRating(Float.parseFloat(RateBar));
        holder.imgHamyar.setImageBitmap(convertToBitmap(imgHamyar));
        holder.buttonInvite.setTag(Code);
        holder.buttonInvite.setOnClickListener(ButtonItemOnClick);
        holder.LinearHamyar.setTag(CodeHamyarRequest);
        holder.LinearHamyar.setOnClickListener(ItemOnclick);
        PublicVariable.view_hamyar.add(holder.LinearHamyar);
    }

    @Override
    public int getItemCount() {
        return this.itemRecycleList.size();
    }

    public class ViewHolderCustom extends RecyclerView.ViewHolder {
        TextView textViewContactName;
        TextView tvRateNumber;
        TextView textViewUnreadCount;
        ImageView imgHamyar;
        RatingBar RatingHamyar;
        Button buttonInvite;
        LinearLayout LinearHamyar;

        public ViewHolderCustom(View itemView) {
            super(itemView);
            textViewContactName = itemView.findViewById(R.id.textViewContactName);
            tvRateNumber = itemView.findViewById(R.id.tvRateNumber);
            textViewUnreadCount = itemView.findViewById(R.id.textViewUnreadCount);
            imgHamyar = itemView.findViewById(R.id.imageViewContactLogo);
            buttonInvite = itemView.findViewById(R.id.buttonInvite);
            RatingHamyar = itemView.findViewById(R.id.RatingHamyar);
            LinearHamyar = itemView.findViewById(R.id.LinearHamyar);
        }
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

    private View.OnClickListener ButtonItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String item = ((Button) v).getTag().toString();
            Intent intent = new Intent(mActivity, Joziat_Motekhases.class);
            intent.putExtra("CodeMotkhases", item);
            mActivity.startActivity(intent);
        }
    };
    private View.OnClickListener ItemOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dbh = new DatabaseHelper(mActivity.getApplicationContext());
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

            String hamya_Code = "";
            hamya_Code = ((LinearLayout) v).getTag().toString();
            String query = "INSERT INTO hamyar_select (Code) VALUES('" + hamya_Code + "')";
            db = dbh.getWritableDatabase();
            db.execSQL("DELETE FROM hamyar_select");
            db.execSQL(query);
            for (int i = 0; i < PublicVariable.view_hamyar.size(); i++) {
                String tag1, tag2;
                tag1 = ((LinearLayout) v).getTag().toString();
                tag2 = ((LinearLayout) PublicVariable.view_hamyar.get(i)).getTag().toString();
                if (tag1.compareTo(tag2) == 0) {
                    v.setBackgroundColor(Color.parseColor("#9877ee"));
                } else {
                    PublicVariable.view_hamyar.get(i).setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        }
    };
}