package com.aspino.it.karbar;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PublicVariable {
	
	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String URL = "http://aspino.ir:8010/WebERP/WebAspino/WebService/AspinoUserWSN.asmx";
//	public static final String URL = "http://94.183.241.216:8070/WebERP/WebAspino/WebService/AspinoUserWSN.asmx";
	public static String LinkFileTextCheckVersion= "http://www.aspino.ir:8010/appupdate/user/version.txt";
	public static String DownloadAppUpdateLinkAPK = "http://www.aspino.ir:8010/appupdate/user/app.apk";
	public static List<LinearLayout> view= new ArrayList<>();
	public static List<LinearLayout> view_hamyar= new ArrayList<>();
	public static final String site = "http://aspino.ir";
	public static final String Recive_NumberPhone = "+9810005152290920";
	public static String Telegram="https://t.me/aspino_team";
	public static String Instagram="http://instagram.com/Aspino_team";
	public static boolean theard_GetUserServiceStartDate=true;
	public static boolean thread_ServiceSaved = true;
	public static boolean thread_RequestHamyar = true;
}
