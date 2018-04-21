package com.aspino.it.karbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by hashemi on 04/17/2018.
 */

public class Main_Activity  extends AppCompatActivity{
    private String karbarCode;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private TextView tvSignUp;
    private Button btnServiceOrder;
    private ImageView imageView;
    private Custom_ViewFlipper viewFlipper;
    private GestureDetector mGestureDetector;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tvSignUp=(TextView)findViewById(R.id.tvSignUp);
        btnServiceOrder=(Button) findViewById(R.id.btnServiceOrder);
        viewFlipper=(Custom_ViewFlipper) findViewById(R.id.vf);
        dbh = new DatabaseHelper(getApplicationContext());
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

            db = dbh.getReadableDatabase();
                Cursor cursor= db.rawQuery("SELECT * FROM login", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    karbarCode = cursor.getString(cursor.getColumnIndex("karbarCode"));
                    db.close();
                }
                if(karbarCode.compareTo("0")!=0)
                {
                    tvSignUp.setVisibility(View.GONE);
                }
                else
                {
                    tvSignUp.setVisibility(View.VISIBLE);
                }

        } catch (Exception e) {
           // throw new Error("Error Opne Activity");
        }

        //***********************Start Service***************************************
        startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
        startService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
        //***************************************************************************
        db = dbh.getReadableDatabase();
        Cursor coursors = db.rawQuery("SELECT * FROM Slider", null);
        if (coursors.getCount() > 0) {
            Bitmap bpm[] = new Bitmap[coursors.getCount()];
            String link[] = new String[coursors.getCount()];
            for (int j = 0; j < coursors.getCount(); j++) {

                coursors.moveToNext();
                viewFlipper.setVisibility(View.VISIBLE);
                //slides.add();
                bpm[j] = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
                link[j] = coursors.getString(coursors.getColumnIndex("Link"));
            }
            db.close();
            int i = 0;
            while (i < bpm.length) {
                imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //ImageLoader.getInstance().displayImage(slides.get(i),imageView);
                imageView.setImageBitmap(bpm[i]);
                imageView.setTag(link[i]);
                viewFlipper.addView(imageView);
                i++;
            }


            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            viewFlipper.setPaintCurrent(paint);
            paint = new Paint();

            paint.setColor(ContextCompat.getColor(this, android.R.color.white));
            viewFlipper.setPaintNormal(paint);

            viewFlipper.setRadius(10);
            viewFlipper.setMargin(5);

            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            mGestureDetector = new GestureDetector(Main_Activity.this, customGestureDetector);

            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        } else {
            viewFlipper.setVisibility(View.GONE);
        }

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(Login.class,"karbarCode","0");
            }
        });
        //******************************************************************************
        btnServiceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(karbarCode.compareTo("0")==0)
                    {
                        Toast.makeText(getApplicationContext(),"جهت استفاده از امکانات آسپینو وارد حساب کاربری خود شوید",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"جهت استفاده از امکانات آسپینو وارد حساب کاربری خود شوید",Toast.LENGTH_LONG).show();
                }
                db = dbh.getReadableDatabase();
                Cursor coursors = db.rawQuery("SELECT * FROM services", null);
                if (coursors.getCount() > 0) {
                    LoadActivity(MainMenu.class,"karbarCode",karbarCode);
                    db.close();
                }
                else
                {
                    SyncServices syncServices=new SyncServices(Main_Activity.this,karbarCode);
                    syncServices.AsyncExecute();
                }

            }
        });
    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
                @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(Main_Activity.this, R.anim.left_in);
                viewFlipper.setOutAnimation(Main_Activity.this, R.anim.left_out);

                viewFlipper.showNext();
            } else if (e1.getX() < e2.getX()) {
                viewFlipper.setInAnimation(Main_Activity.this, R.anim.right_in);
                viewFlipper.setOutAnimation(Main_Activity.this, R.anim.right_out);

                viewFlipper.showPrevious();
            }
            viewFlipper.setInAnimation(Main_Activity.this, R.anim.left_in);
            viewFlipper.setOutAnimation(Main_Activity.this, R.anim.left_out);

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);

        this.startActivity(intent);
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
    private void ExitApplication() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از برنامه خارج شوید ؟");

        // set a negative/no button and create a listener
        alertbox.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        // set a positive/yes button and create a listener
        alertbox.setNegativeButton("بله", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Declare Object From Get Internet Connection Status For Check Internet Status
                //System.exit(0);
                Intent startMain = new Intent(Intent.ACTION_MAIN);

                startMain.addCategory(Intent.CATEGORY_HOME);

                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(startMain);

                finish();

                arg0.dismiss();

            }
        });

        alertbox.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
           ExitApplication();
        }

        return super.onKeyDown(keyCode, event);
    }
}
