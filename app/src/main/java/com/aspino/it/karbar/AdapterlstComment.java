package  com.aspino.it.karbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterlstComment extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    private String karbarCode;

    public AdapterlstComment(Activity activity, ArrayList<HashMap<String, String>> list, String karbarCode) {
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
        TextView txtContentComment;
        TextView txtUsernameComment;
        TextView txtDateComment;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
//            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/vazir.ttf");
            convertView = inflater.inflate(R.layout.list_item_comment, null);
            holder = new ViewHolder();
            holder.txtContentComment = (TextView) convertView.findViewById(R.id.txtContentComment);
            holder.txtUsernameComment = (TextView) convertView.findViewById(R.id.txtUsernameComment);
            holder.txtDateComment = (TextView) convertView.findViewById(R.id.txtDateComment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String ContentComment = map.get("ContentComment");
        String UsernameComment = map.get("UsernameComment");
        String DateComment = map.get("txtDateComment");
        String Code_Comment = map.get("Code_Comment");
        holder.txtContentComment.setText(ContentComment);
        holder.txtUsernameComment.setText(UsernameComment);
        holder.txtDateComment.setText(DateComment);
        return convertView;
    }


//    private OnClickListener TextViewItemOnclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String DetailCode="";
//            DetailCode = ((TextView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),Service_Request1.class);//Goto Page Form Order Service
//            intent.putExtra("karbarCode",karbarCode);
//            intent.putExtra("DetailCode",DetailCode);
//            activity.startActivity(intent);
//        }
//    };
//    private OnClickListener ImageViewItemOnclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String DetailCode="";
//            DetailCode = ((ImageView)v).getTag().toString();
//            Intent intent = new Intent(activity.getApplicationContext(),Service_Request1.class);//Goto Page Form Order Service
//            intent.putExtra("karbarCode",karbarCode);
//            intent.putExtra("DetailCode",DetailCode);
//            activity.startActivity(intent);
//        }
//    };
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

