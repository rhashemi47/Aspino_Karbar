package com.aspino.it.karbar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterListViewPager extends BaseAdapter {


    private String karbarCode;
    private int check_tab;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public AdapterListViewPager(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
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
        TextView tvTitleService;
        TextView tvStatusService;
        TextView tvDateAndTimeService;
        TextView tvAddresService;
        TextView tvNameHamyar;
        LinearLayout LinearStatusService;
        LinearLayout LinearMain;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
//        if (convertView == null) {
//            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/vazir.ttf");
            convertView = inflater.inflate(R.layout.list_item_viewpager, null);
            holder = new ViewHolder();
            holder.tvTitleService = (TextView) convertView.findViewById(R.id.tvTitleService);
            holder.tvStatusService = (TextView) convertView.findViewById(R.id.tvStatusService);
            holder.tvDateAndTimeService = (TextView) convertView.findViewById(R.id.tvDateAndTimeService);
            holder.tvAddresService = (TextView) convertView.findViewById(R.id.tvAddresService);
            holder.tvNameHamyar = (TextView) convertView.findViewById(R.id.tvNameHamyar);
            holder.LinearStatusService = (LinearLayout) convertView.findViewById(R.id.LinearStatusService);
            holder.LinearMain = (LinearLayout) convertView.findViewById(R.id.LinearMain);
            //********************************************
//            holder.tvTitleService.setTypeface(faceh);
//            holder.tvStatusService.setTypeface(faceh);
//            holder.tvDateAndTimeService.setTypeface(faceh);
//            holder.tvAddresService.setTypeface(faceh);
            //********************************************
            holder.tvTitleService.setTextSize(16);
            holder.tvStatusService.setTextSize(8);
            holder.tvDateAndTimeService.setTextSize(16);
            holder.tvAddresService.setTextSize(16);
            holder.tvNameHamyar.setTextSize(16);
            holder.tvNameHamyar.setTextSize(16);
            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        String Code = map.get("Code");
        String TitleService = map.get("TitleService");
        String Status = map.get("Status");
        String DateAndTimeService = map.get("DateAndTimeService");
        String AddresService = map.get("AddresService");
        String NameHamyar = map.get("NameHamyar");
        if(Status.compareTo("0")!=0)
        {
            holder.LinearStatusService.setBackgroundColor(Color.parseColor("#12b44f"));
            holder.tvStatusService.setText("تایید شده");
            holder.LinearMain.setOnClickListener(ItemOnclickPardakht);
        }
        else
        {
            holder.LinearStatusService.setBackgroundColor(Color.parseColor("#f0ba51"));
            holder.tvStatusService.setText("انتخاب متخصص");
            holder.LinearMain.setOnClickListener(ItemOnclickSelectHamyar);
        }
        holder.LinearMain.setTag(Code);
        holder.tvTitleService.setText(TitleService);
        holder.tvDateAndTimeService.setText(DateAndTimeService);
        holder.tvAddresService.setText(AddresService);
        holder.tvNameHamyar.setText(NameHamyar);
        return convertView;
    }


    private OnClickListener ItemOnclickSelectHamyar = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String code = ((LinearLayout)v).getTag().toString();
//            Toast.makeText(activity.getApplicationContext(),"Select Hamyar",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity.getApplicationContext(),Select_Hamyar.class);//todo SelectHamyar
            intent.putExtra("OrderCode",code);
            activity.startActivity(intent);
        }
    };
    private OnClickListener ItemOnclickPardakht = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String code = ((LinearLayout)v).getTag().toString();
//            Toast.makeText(activity.getApplicationContext(),"Pardakht Factor",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity.getApplicationContext(),Pardakh_Factor_Sefaresh.class);//todo pardakh_factor_sefaresh.xml
            intent.putExtra("OrderCode",code);
            activity.startActivity(intent);
        }
    };

}

