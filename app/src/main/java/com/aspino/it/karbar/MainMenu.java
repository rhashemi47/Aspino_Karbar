package  com.aspino.it.karbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String karbarCode="0";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private DatabaseHelper dbh;
    private SQLiteDatabase db;
    private Drawer drawer = null;
    private String countMessage;
    private GridView GridViewServices;
    private boolean IsActive = true;
    private ArrayList<HashMap<String, String>> valuse;
    private EditText etSearch;
    private ListView lstSearchDetailService;
    private LinearLayout LinearListOrder;
    private LinearLayout LinearSupportContact;
    private Button btnLogout;
    private ImageView imgMenu;
    private DrawerLayout mDrawer;
    private NavigationView mNavi;
    private Toolbar mtoolbar;
    private TextView tvUserName;
    private TextView tvCredits;
    private ImageView imgPicProfile;

    ImageView imageView;
    Custom_ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    private String countOrder;
    private String AppVersion;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu_mainmenu);
        //****************************************************************
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
            if(!db.isOpen())
            {
                db = dbh.getReadableDatabase();
            }
        }
        catch (Exception ex){
            db = dbh.getReadableDatabase();
        }
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        if(version.length()>0) {
            AppVersion = version;
            WsDownLoadUpdate wsDownLoadUpdate=new WsDownLoadUpdate(MainMenu.this,AppVersion, PublicVariable.LinkFileTextCheckVersion,PublicVariable.DownloadAppUpdateLinkAPK);
            wsDownLoadUpdate.AsyncExecute();
        }
        //****************************************************************
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.m_toolbar);
        mtoolbar.setTitle("");

//        setSupportActionBar(mtoolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btnback);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavi = (NavigationView) findViewById(R.id.navigation_view);
        View header_View= mNavi.getHeaderView(0);
        btnLogout=(Button)header_View.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        mNavi.setNavigationItemSelectedListener(this);
        mNavi.setItemIconTintList(null);
        imgMenu=(ImageView)findViewById(R.id.imgMenu);
        imgPicProfile=(ImageView)header_View.findViewById(R.id.imgPicProfile);
        tvUserName=(TextView) header_View.findViewById(R.id.tvUserName);
        tvCredits=(TextView) header_View.findViewById(R.id.tvCredits);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

//        ActionBarDrawerToggle aToggle = new ActionBarDrawerToggle(this, mDrawer, mtoolbar, R.string.open, R.string.close);

//        mDrawer.addDrawerListener(aToggle);
//        aToggle.syncState();


        try
        {
            try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
            Cursor cursor = db.rawQuery("SELECT * FROM login", null);
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                karbarCode = cursor.getString(cursor.getColumnIndex("karbarCode"));
                if(db.isOpen()) {
                    db.close();
                }
            }
            if (karbarCode.compareTo("0") == 0) {
                LoadActivity(Login.class, "karbarCode", "0");
            }

        } catch (Exception e) {
            // throw new Error("Error Opne Activity");
        }
        //***********************Start Service***************************************
        startService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
        startService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
        startService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));
        startService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
        //***************************************************************************
//        facehVazir = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");
        etSearch = (EditText) findViewById(R.id.etSearch);
        lstSearchDetailService = (ListView) findViewById(R.id.lstSearchDetailService);
        LinearSupportContact = (LinearLayout) findViewById(R.id.LinearSupportContact);
        LinearListOrder = (LinearLayout) findViewById(R.id.LinearListOrder);
        GridViewServices = (GridView) findViewById(R.id.GridViewServices);
        viewFlipper=(Custom_ViewFlipper) findViewById(R.id.vf);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainMenu.this));
        dbh = new DatabaseHelper(getApplicationContext());
//        try {
//
//            dbh.createDataBase();
//
//        } catch (IOException ioe) {
//
//            throw new Error("Unable to create database");
//
//        }
//
//        try {
//
//            dbh.openDataBase();
//
//        } catch (SQLException sqle) {
//
//            throw sqle;
//        }
        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        Cursor coursors = db.rawQuery("SELECT * FROM messages WHERE IsReade='0' AND IsDelete='0'", null);
        if (coursors.getCount() > 0) {
            countMessage = String.valueOf(coursors.getCount());
        }
        if(db.isOpen()) {
            db.close();
        }
        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        final Cursor cursor = db.rawQuery("SELECT * FROM OrdersService WHERE Status ='1'", null);
        if (cursor.getCount() > 0) {
            countOrder = String.valueOf(cursor.getCount());
        }
        if(db.isOpen()) {
            db.close();
        }
        //**************LinearListOrder***********************************************************************************
        LinearListOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    LoadActivity(Paigiri.class, "karbarCode", karbarCode);
            }
        });
        //**************LinearSupportContact***********************************************************************************
        LinearSupportContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbh.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM Supportphone",null);
                if(cursor.getCount()>0)
                {
                    cursor.moveToNext();
                    dialContactPhone(cursor.getString(cursor.getColumnIndex("PhoneNumber")));
                }
                if(db.isOpen()) {
                    db.close();
                }
            }
        });
//        ***************************************************************************************************
        lstSearchDetailService.setVisibility(View.GONE);
        GridViewServices.setVisibility(View.VISIBLE);
        //Create Button For Services in Grid View

        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        Cursor cursor1 = db.rawQuery("SELECT * FROM services", null);
        if (cursor1.getCount() > 0) {
            valuse = new ArrayList<HashMap<String, String>>();
            for (int x = 0; x < cursor1.getCount(); x++) {
                cursor1.moveToNext();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor1.getString(cursor1.getColumnIndex("servicename")));
                map.put("Code", cursor1.getString(cursor1.getColumnIndex("code")));
                valuse.add(map);
            }
            if(db.isOpen()) {
                db.close();
            }
            AdapterGridServices adapterGridServices = new AdapterGridServices(MainMenu.this, valuse, karbarCode);
            GridViewServices.setAdapter(adapterGridServices);
        }
        //*****************************************************************************************************
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strSeach;
                strSeach = s.toString().trim();
                if (strSeach.length() == 0) {
                    lstSearchDetailService.setVisibility(View.GONE);
                    GridViewServices.setVisibility(View.VISIBLE);
                    //Create Button For Services in Grid View
                    try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                    Cursor cursor = db.rawQuery("SELECT * FROM services", null);
                    valuse = new ArrayList<HashMap<String, String>>();
                    for (int x = 0; x < cursor.getCount(); x++) {
                        cursor.moveToNext();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", cursor.getString(cursor.getColumnIndex("servicename")));
                        map.put("Code", cursor.getString(cursor.getColumnIndex("code")));
                        valuse.add(map);
                    }
                    if(db.isOpen()) {
                        db.close();
                    }
                    AdapterGridServices adapterGridServices = new AdapterGridServices(MainMenu.this, valuse, karbarCode);
                    GridViewServices.setAdapter(adapterGridServices);
                } else {
                    lstSearchDetailService.setVisibility(View.VISIBLE);
                    GridViewServices.setVisibility(View.GONE);
                    valuse = new ArrayList<HashMap<String, String>>();
                    //Create Button For Services in Grid View
                    try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                    Cursor cursor = db.rawQuery("SELECT * FROM servicesdetails WHERE name LIKE '%" + strSeach + "%'", null);
                    for (int x = 0; x < cursor.getCount(); x++) {
                        cursor.moveToNext();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                        map.put("Code", cursor.getString(cursor.getColumnIndex("code")));
                        valuse.add(map);
                    }
                    if(db.isOpen()) {
                        db.close();
                    }
                    AdapterServiceDetails adapterServiceDetails = new AdapterServiceDetails(MainMenu.this, valuse, karbarCode);
                    lstSearchDetailService.setAdapter(adapterServiceDetails);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //********************************************************************Start And Stop Service BackGround
       try {
           if (karbarCode.compareTo("0") != 0) {
               //startService(new Intent(getBaseContext(), ServiceGetLocation.class));
               startService(new Intent(getBaseContext(), ServiceGetUserServicesHamyarRequest.class));
           }
       }
       catch (Exception ex)
       {

       }
        //**************************************************************************

        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        coursors = db.rawQuery("SELECT * FROM Slider", null);
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
            if(db.isOpen()) {                                            db.close();                                        }
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
            mGestureDetector = new GestureDetector(MainMenu.this, customGestureDetector);

            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mGestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        } else {
            viewFlipper.setVisibility(View.VISIBLE);
        }
        coursors.close();
        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
        coursors = db.rawQuery("SELECT * FROM Profile", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Name")).compareTo("null")!=0){
                    tvUserName.setText(coursors.getString(coursors.getColumnIndex("Name")));
                }
                else
                {
                    tvUserName.setText("کاربر");
                }

            }
            catch (Exception ex){
                tvUserName.setText("کاربر");
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null")!=0){
                    tvUserName.setText(tvUserName.getText() +" "+ coursors.getString(coursors.getColumnIndex("Fam")));
                }
                else
                {
                    tvUserName.setText(tvUserName.getText() + "مهمان");
                }

            }
            catch (Exception ex){
                tvUserName.setText(tvUserName.getText() + "مهمان");
            }
            try
            {
                if(coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null")!=0){
                    imgPicProfile.setImageBitmap(convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic"))));
                }
                else
                {
                    imgPicProfile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo1));
                }

            }
            catch (Exception ex){
                imgPicProfile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo1));
            }
        }
        else
        {
            tvUserName.setText(tvUserName.getText() + "کاربر مهمان");
        }

        coursors.close();
        if(db.isOpen()) {
            db.close();
        }
        db=dbh.getReadableDatabase();
        coursors = db.rawQuery("SELECT * FROM AmountCredit", null);
        if (coursors.getCount() > 0) {
            coursors.moveToNext();
            try {
                String splitStr[]=coursors.getString(coursors.getColumnIndex("Amount")).toString().split("\\.");
                if(splitStr[1].compareTo("00")==0)
                {
                    tvCredits.setText(splitStr[0]);
                }
                else
                {
                    tvCredits.setText(coursors.getString(coursors.getColumnIndex("Amount")));
                }

            } catch (Exception ex) {
                tvCredits.setText("0");
            }
        }
        if(db.isOpen()) {
            db.close();
        }
        coursors.close();
//*******************************************************************************************************************
//        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//        Cursor cursor2 = db.rawQuery("SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                "LEFT JOIN " +
//                "Servicesdetails ON " +
//                "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'", null);
//        if (cursor2.getCount() > 0) {
//            btnOrder.setText("درخواست ها: " + cursor2.getCount());
//        }
//        cursor2 = db.rawQuery("SELECT * FROM OrdersService WHERE Status in (1,2,6,7,12,13)", null);
//        if (cursor2.getCount() > 0) {
//            btnAcceptOrder.setText("پذیرفته شده ها: " + cursor2.getCount());
//        }
//        cursor2 = db.rawQuery("SELECT * FROM AmountCredit", null);
//        if (cursor2.getCount() > 0) {
//            cursor2.moveToNext();
//            try {
//                String splitStr[]=cursor2.getString(cursor2.getColumnIndex("Amount")).toString().split("\\.");
//                if(splitStr[1].compareTo("00")==0)
//                {
//                    btncredite.setText("اعتبار: " +splitStr[0]);
//                }
//                else
//                {
//                    btncredite.setText("اعتبار: " + cursor2.getString(cursor2.getColumnIndex("Amount")));
//                }
//
//            } catch (Exception ex) {
//                btncredite.setText("اعتبار: " + "0");
//            }
//        }
//        db.close();
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String QueryCustom;
//                QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                        "LEFT JOIN " +
//                        "Servicesdetails ON " +
//                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status ='0'";
//                LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//            }
//        });
//        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String QueryCustom;
//                QueryCustom="SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                        "LEFT JOIN " +
//                        "Servicesdetails ON " +
//                        "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status in (1,2,6,7,12,13)";
//                LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//            }
//        });
//        btncredite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LoadActivity(Credit.class, "karbarCode", karbarCode);
//            }
//        });
//        btnServiceEmergency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(MainMenu.this,
//                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainMenu.this, Manifest.permission.CALL_PHONE))
//                    {
//                        //do nothing
//                    }
//                    else{
//
//                        ActivityCompat.requestPermissions(MainMenu.this,new String[]{Manifest.permission.CALL_PHONE},2);
//                    }
//
//                }
//                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
//                if (cursorPhone.getCount() > 0) {
//                    cursorPhone.moveToNext();
//                    dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
//                }
//                db.close();
//            }
//        });
        //****************************************************************************************
        //CreateMenu(toolbar);
        //***************************************************************************************************************************
    }

//    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            // Swipe left (next)
//            if (e1.getX() > e2.getX()) {
//                viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
//                viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);
//
//                viewFlipper.showNext();
//            } else if (e1.getX() < e2.getX()) {
//                viewFlipper.setInAnimation(MainMenu.this, R.anim.right_in);
//                viewFlipper.setOutAnimation(MainMenu.this, R.anim.right_out);
//
//                viewFlipper.showPrevious();
//            }
//            viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
//            viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);
//
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }
//    }

    public void Logout() {
        //Exit All Activity And Kill Application
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // set the message to display
        alertbox.setMessage("آیا می خواهید از کاربری خارج شوید ؟");

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
                //stopService(new Intent(getBaseContext(), ServiceGetLocation.class));
                stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));				stopService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
                stopService(new Intent(getBaseContext(), ServiceGetServiceSaved.class));				stopService(new Intent(getBaseContext(), ServiceGetUserServiceStartDate.class));
                stopService(new Intent(getBaseContext(), ServiceGetServicesAndServiceDetails.class));
                stopService(new Intent(getBaseContext(), ServiceGetSliderPic.class));
//                stopService(new Intent(getBaseContext(), ServiceSyncMessage.class));
                
                
                stopService(new Intent(getBaseContext(), ServiceGetUserServicesHamyarRequest.class));
                db = dbh.getWritableDatabase();
                db.execSQL("DELETE FROM address");
                db.execSQL("DELETE FROM AmountCredit");
                db.execSQL("DELETE FROM android_metadata");
                db.execSQL("DELETE FROM Arts");
                db.execSQL("DELETE FROM BsFaktorUserDetailes");
                db.execSQL("DELETE FROM BsFaktorUsersHead");
                db.execSQL("DELETE FROM City");
                db.execSQL("DELETE FROM credits");
                db.execSQL("DELETE FROM DateTB");
                db.execSQL("DELETE FROM FieldofEducation");
                db.execSQL("DELETE FROM Grade");
                db.execSQL("DELETE FROM Hamyar");
                db.execSQL("DELETE FROM InfoHamyar");
                db.execSQL("DELETE FROM Language");
                db.execSQL("DELETE FROM login");
                db.execSQL("DELETE FROM messages");
                db.execSQL("DELETE FROM OrdersService");
                db.execSQL("DELETE FROM Profile");
                db.execSQL("DELETE FROM services");
                db.execSQL("DELETE FROM servicesdetails");
                db.execSQL("DELETE FROM Slider");
                db.execSQL("DELETE FROM sqlite_sequence");
                db.execSQL("DELETE FROM State");
                db.execSQL("DELETE FROM Supportphone");
                db.execSQL("DELETE FROM Unit");
                db.execSQL("DELETE FROM UpdateApp");
                db.execSQL("DELETE FROM visit");
                if(db.isOpen()) {
                    db.close();
                }
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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void CreateMenu(Toolbar toolbar) {
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
//        String name = "";
//        String family = "";
//        String Mobile = "";
//        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//        Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
//        if (coursors.getCount() > 0) {
//            coursors.moveToNext();
//            try
//            {
//                if(coursors.getString(coursors.getColumnIndex("Name")).compareTo("null")!=0){
//                    name = coursors.getString(coursors.getColumnIndex("Name"));
//                }
//                else
//                {
//                    name = "کاربر";
//                }
//
//            }
//            catch (Exception ex){
//                name = "کاربر";
//            }
//            try
//            {
//                if(coursors.getString(coursors.getColumnIndex("Fam")).compareTo("null")!=0){
//                    family = coursors.getString(coursors.getColumnIndex("Fam"));
//                }
//                else
//                {
//                    family = "مهمان";
//                }
//
//            }
//            catch (Exception ex){
//                family = "مهمان";
//            }
//            try
//            {
//                if(coursors.getString(coursors.getColumnIndex("Mobile")).compareTo("null")!=0){
//                    Mobile = coursors.getString(coursors.getColumnIndex("Mobile"));
//                }
//                else
//                {
//                    Mobile = "";
//                }
//
//            }
//            catch (Exception ex){
//                Mobile = "";
//            }
//            try
//            {
//                if(coursors.getString(coursors.getColumnIndex("Pic")).compareTo("null")!=0){
//                    bmp = convertToBitmap(coursors.getString(coursors.getColumnIndex("Pic")));
//                }
//                else
//                {
//                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
//                }
//
//            }
//            catch (Exception ex){
//                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.useravatar);
//            }
//        }
//        else
//        {
//            name = "کاربر";
//            family = "مهمان";
//        }
//        db.close();
//        int drawerGravity = Gravity.END;
//        Configuration config = getResources().getConfiguration();
//        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            drawerGravity = Gravity.START;
//        }
//
//        // Create the AccountHeader
//        AccountHeader headerResult = new AccountHeaderBuilder()
//                .withActivity(this)
//                .withHeaderBackground(R.drawable.header_menu)
//                .addProfiles(new ProfileDrawerItem().withName(name + " " + family+"\n"+Mobile).withIcon(bmp)).withSelectionListEnabledForSingleProfile(false)
////                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
////                    @Override
////                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
////                        return false;
////                    }
////                })
//                .build();
//
//        drawer = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .withAccountHeader(headerResult)
//                .withDrawerGravity(drawerGravity)
//                .withShowDrawerOnFirstLaunch(true)
//                .addDrawerItems(
//                        new SecondaryDrawerItem().withName(R.string.Profile).withIcon(R.drawable.profile).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.wallet).withIcon(R.drawable.wallet).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.Order).withIcon(R.drawable.clock).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.AddresManagement).withIcon(R.drawable.manage_addres).withSelectable(false).withEnabled(IsActive),
//                        new SecondaryDrawerItem().withName(R.string.Invite_friends).withIcon(R.drawable.share).withSelectable(false).withEnabled(IsActive),
////                        new SecondaryDrawerItem().withName(R.string.TermsـandـConditions).withIcon(R.drawable.rouls).withSelectable(false).withEnabled(IsActive),
////                        new SecondaryDrawerItem().withName(R.string.Contact).withIcon(R.drawable.contact_with_us).withSelectable(false),
//                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive)
////                        new SecondaryDrawerItem().withName(R.string.Credits).withIcon(R.drawable.creditinmenu).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
////                        new SecondaryDrawerItem().withName(R.string.Order).withIcon(R.drawable.invit_friend).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
////                        new SecondaryDrawerItem().withName(R.string.Logout).withIcon(R.drawable.logout).withSelectable(false)
//
//
//
////                        new SecondaryDrawerItem().withName(R.string.Messages).withIcon(R.drawable.messages).withBadge(countMessage).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSelectable(false).withEnabled(IsActive),
//                        // new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
////                        new SecondaryDrawerItem().withName(R.string.Yourcommitment).withIcon(R.drawable.yourcommitment).withSelectable(false),
////                        new SecondaryDrawerItem().withName(R.string.Ourcommitment).withIcon(R.drawable.ourcommitment).withSelectable(false),
//                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
//                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
////                        new SecondaryDrawerItem().withName(R.string.About).withIcon(R.drawable.about).withSelectable(false),
////                        new SecondaryDrawerItem().withName(R.string.Help).withIcon(R.drawable.help).withSelectable(false),
//
//                        //new SectionDrawerItem().withName("").withDivider(true).withTextColor(ContextCompat.getColor(this,R.color.md_grey_500)),
//                        //new SecondaryDrawerItem().withName(R.string.Exit).withIcon(R.drawable.exit).withSelectable(false),
////                ).addStickyDrawerItems(new PrimaryDrawerItem().withName(R.string.RelateUs).withSelectable(false).withEnabled(false),
////                        new PrimaryDrawerItem().withName(R.string.telegram).withIcon(R.drawable.telegram).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.instagram).withIcon(R.drawable.instagram).withSelectable(false))
//                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        switch (position) {
//                            case 1://Profile
//                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
//                                if (coursors.getCount() > 0) {
//                                    coursors.moveToNext();
//                                    String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
//                                    if (Status_check.compareTo("0") == 0) {
//                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
//                                        if (c.getCount() > 0) {
//                                            c.moveToNext();
//                                            SyncProfile profile = new SyncProfile(MainMenu.this, c.getString(c.getColumnIndex("karbarCode")));
//                                            profile.AsyncExecute();
//                                        }
//                                    } else {
//                                        LoadActivity(Profile.class, "karbarCode", karbarCode);
//                                    }
//                                }
//                                else {
//                                    LoadActivity(Login.class,"karbarCode","0");
//                                }
//                                db.close();
//                                break;
//                            case 2://Credit
//                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
//                                        if (c.getCount() > 0) {
//                                            c.moveToNext();
//                                            LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
//                                        }
//                                else {
//                                    LoadActivity(Login.class,"karbarCode","0");
//                                }
//                                db.close();
//                                break;
//                            case 3:
//                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                 c = db.rawQuery("SELECT * FROM login", null);
//                                if (c.getCount() > 0) {
//                                    c.moveToNext();
//                                    String QueryCustom;
//                                    QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
//                                            "LEFT JOIN " +
//                                            "Servicesdetails ON " +
//                                            "Servicesdetails.code=OrdersService.ServiceDetaileCode";
//                                    LoadActivity2(Paigiri.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
//                                }
//                                break;
//                            case 4:
//                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                         c = db.rawQuery("SELECT * FROM login", null);
//                                        if (c.getCount() > 0) {
//                                            c.moveToNext();
//                                            LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
//                                        }
//                                        db.close();
////                                AlertDialog.Builder alertbox1 = new AlertDialog.Builder(MainMenu.this);
////                                // set the message to display
////                                alertbox1.setMessage("مدیریت آدرس ها");
////
////                                // set a negative/no button and create a listener
////                                alertbox1.setPositiveButton("ثبت آدرس", new DialogInterface.OnClickListener() {
////                                    // do something when the button is clicked
////                                    public void onClick(DialogInterface arg0, int arg1) {
////                                        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
////                                        if (c.getCount() > 0) {
////                                            c.moveToNext();
////                                            LoadActivity2(Map.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
////                                        }
////                                        db.close();
////                                        arg0.dismiss();
////                                    }
////                                });
////
////                                // set a positive/yes button and create a listener
////                                alertbox1.setNegativeButton("لیست آدرس های ثبت شده", new DialogInterface.OnClickListener() {
////                                    // do something when the button is clicked
////                                    public void onClick(DialogInterface arg0, int arg1) {
////                                        //Declare Object From Get Internet Connection Status For Check Internet Status
////                                        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
////                                        if (c.getCount() > 0) {
////                                            c.moveToNext();
////                                            LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
////                                        }
////                                        db.close();
////                                        arg0.dismiss();
////
////                                    }
////                                });
////                                alertbox1.show();
////                                db.close();
//                                break;
//                            case 5:
////                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                c = db.rawQuery("SELECT * FROM Profile", null);
////                                if (c.getCount() > 0) {
////                                    c.moveToNext();
////                                    sharecode(c.getString(c.getColumnIndex("karbarCodeForReagent")));
////                                    // LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                }
////                                db.close();
////                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                sharecode("0");
//                                break;
////                            case 6:
////                                Toast.makeText(MainMenu.this, "تنظیمات", Toast.LENGTH_SHORT).show();
////                                AlertDialog.Builder alertbox = new AlertDialog.Builder(MainMenu.this);
////                                // set the message to display
////                                alertbox.setMessage("قوانین و مقررات");
////
////                                // set a negative/no button and create a listener
////                                alertbox.setPositiveButton("تعهدات ما", new DialogInterface.OnClickListener() {
////                                    // do something when the button is clicked
////                                    public void onClick(DialogInterface arg0, int arg1) {
////                                        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
////                                        if (c.getCount() > 0) {
////                                            c.moveToNext();
////
////                                            LoadActivity(OurCommitment.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                        }
////                                        db.close();
////                                        arg0.dismiss();
////                                    }
////                                });
////
////                                // set a positive/yes button and create a listener
////                                alertbox.setNegativeButton("تعهدات شما", new DialogInterface.OnClickListener() {
////                                    // do something when the button is clicked
////                                    public void onClick(DialogInterface arg0, int arg1) {
////                                        //Declare Object From Get Internet Connection Status For Check Internet Status
////                                        try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                        Cursor c = db.rawQuery("SELECT * FROM login", null);
////                                        if (c.getCount() > 0) {
////                                            c.moveToNext();
////
////                                            LoadActivity(YourCommitment.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                        }
////                                        db.close();
////                                        arg0.dismiss();
////
////                                    }
////                                });
////                                alertbox.show();
////                                db.close();
////                                break;
////                            case 7:
////                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                c = db.rawQuery("SELECT * FROM login", null);
////                                if (c.getCount() > 0) {
////                                    c.moveToNext();
////
////                                    LoadActivity(Contact.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                }
////                                db.close();
////
////                                break;
//                            case 6:
//                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
//                                c = db.rawQuery("SELECT * FROM login", null);
//                                if (c.getCount() > 0) {
//                                    c.moveToNext();
//
//                                    LoadActivity(About.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
//                                }
//                                db.close();
//                                break;
////                            case 8:
////                                db=dbh.getReadableDatabase();
////                                c = db.rawQuery("SELECT * FROM login", null);
////                                if (c.getCount() > 0) {
////                                    c.moveToNext();
////
////                                    LoadActivity(GiftBank.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                }
////                                db.close();//
////                                break;
////                            case 9:
////                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                c = db.rawQuery("SELECT * FROM login", null);
////                                if (c.getCount() > 0) {
////                                    c.moveToNext();
////                                    Cursor creditCussor=db.rawQuery("SELECT * FROM credits",null);
////                                    if(creditCussor.getCount()>0) {
////                                        LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
////                                    }
////                                    else
////                                    {
////                                        SyncGettUserCreditHistory syncGettUserCreditHistory =new SyncGettUserCreditHistory(MainMenu.this,c.getString(c.getColumnIndex("karbarCode")),"0");
////                                        syncGettUserCreditHistory.AsyncExecute();
////                                    }
////
////                                }
////                                db.close();
////
////                                break;
////                            case 10:
////                                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
////                                c = db.rawQuery("SELECT * FROM login", null);
////                                if (c.getCount() > 0) {
////                                    c.moveToNext();
////                                    String QueryCustom;
////                                    QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
////                                            "LEFT JOIN " +
////                                            "Servicesdetails ON " +
////                                            "Servicesdetails.code=OrdersService.ServiceDetaileCode WHERE Status<>3";
////                                    LoadActivity2(List_Order.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
////                                }
////                                break;
////                            case 11:
////                                Logout();
////                                break;
//                        }
//                        return true;
//                    }
//                })
//                .build();
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int mId = item.getItemId();

        switch (mId) {

            case R.id.profile:
                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                Cursor coursors = db.rawQuery("SELECT * FROM Profile", null);
                if (coursors.getCount() > 0) {
                    coursors.moveToNext();
                    String Status_check = coursors.getString(coursors.getColumnIndex("Status"));
                    if (Status_check.compareTo("0") == 0) {
                        Cursor c = db.rawQuery("SELECT * FROM login", null);
                        if (c.getCount() > 0) {
                            c.moveToNext();
                            SyncProfile profile = new SyncProfile(MainMenu.this, c.getString(c.getColumnIndex("karbarCode")));
                            profile.AsyncExecute();
                        }
                    } else {
                        LoadActivity(Profile.class, "karbarCode", karbarCode);
                    }
                }
                else {
                    LoadActivity(Login.class,"karbarCode","0");
                }
                if(db.isOpen()) {
                    db.close();
                }
                break;

            case R.id.wallet:
                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                Cursor c = db.rawQuery("SELECT * FROM login", null);
                if (c.getCount() > 0) {
                    c.moveToNext();
                    LoadActivity(Credit.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                }
                else {
                    LoadActivity(Login.class,"karbarCode","0");
                }
                if(db.isOpen()) {
                    db.close();
                }
                break;
            case R.id.Order:
                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                c = db.rawQuery("SELECT * FROM login", null);
                if (c.getCount() > 0) {
                    c.moveToNext();
                    String QueryCustom;
                    QueryCustom = "SELECT OrdersService.*,Servicesdetails.name FROM OrdersService " +
                            "LEFT JOIN " +
                            "Servicesdetails ON " +
                            "Servicesdetails.code=OrdersService.ServiceDetaileCode";
                    LoadActivity2(Paigiri.class, "karbarCode", karbarCode, "QueryCustom", QueryCustom);
                }
                break;

            case R.id.AddresManagement:
                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                c = db.rawQuery("SELECT * FROM login", null);
                if (c.getCount() > 0) {
                    c.moveToNext();
                    LoadActivity2(List_Address.class,"karbarCode",karbarCode,"nameActivity","MainMenu");
                }
                if(db.isOpen()) {
                    db.close();
                }
                break;

            case R.id.Invite_friends:
                sharecode("0");
                break;

            case R.id.About:
                try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                c = db.rawQuery("SELECT * FROM login", null);
                if (c.getCount() > 0) {
                    c.moveToNext();

                    LoadActivity(About.class, "karbarCode", c.getString(c.getColumnIndex("karbarCode")));
                }
                if(db.isOpen()) {
                    db.close();
                }
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;

    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

                viewFlipper.showNext();
            } else if (e1.getX() < e2.getX()) {
                viewFlipper.setInAnimation(MainMenu.this, R.anim.right_in);
                viewFlipper.setOutAnimation(MainMenu.this, R.anim.right_out);

                viewFlipper.showPrevious();
            }
            viewFlipper.setInAnimation(MainMenu.this, R.anim.left_in);
            viewFlipper.setOutAnimation(MainMenu.this, R.anim.left_out);

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            ExitApplication();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);

        this.startActivity(intent);
    }
    public void LoadActivity2(Class<?> Cls, String VariableName, String VariableValue
            , String VariableName2, String VariableValue2) {
        Intent intent = new Intent(getApplicationContext(), Cls);
        intent.putExtra(VariableName, VariableValue);
        intent.putExtra(VariableName2, VariableValue2);

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

//    public void dialContactPhone(String phoneNumber) {
//        //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + phoneNumber));
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//
//        startActivity(callIntent);
//    }
    void sharecode(String shareStr)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "آسپینو" + "\n"+"کد معرف: "+shareStr+"\n"+"آدرس سایت: " + PublicVariable.site;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "عنوان");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "اشتراک گذاری با"));
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
    public void dialContactPhone(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainMenu.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        startActivity(callIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try { if(!db.isOpen()) { db = dbh.getReadableDatabase();}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
                    Cursor cursorPhone = db.rawQuery("SELECT * FROM Supportphone", null);
                    if (cursorPhone.getCount() > 0) {
                        cursorPhone.moveToNext();
                        dialContactPhone(cursorPhone.getString(cursorPhone.getColumnIndex("PhoneNumber")));
                    }
                    if(db.isOpen()) {
                        db.close();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(MainMenu.this, "مجوز تماس از طریق برنامه لغو شده برای بر قراری تماس از درون برنامه باید مجوز دسترسی تماس را فعال نمایید.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START))
        {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            ExitApplication();
        }

    }
}
