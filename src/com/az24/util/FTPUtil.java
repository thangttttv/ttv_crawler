package com.az24.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTPClient;

public class FTPUtil {
	public static  FTPClient client = new FTPClient();
	public static void connection()
	{
	
		try {
			client.connect("210.211.97.12");
			client.login("hdcTinhhinH", "HDCtinhhinh!@#");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public static void close()
	{
		try {
			client.logout();
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean upload(File file)
	{
		FileInputStream fis = null;
		boolean kq = false;
		Calendar calendar = Calendar.getInstance();		
		int month = calendar.get(Calendar.MONTH)+1;
		String strMonth = month<10?"0"+month:month+"";
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String strDay = day<10?"0"+day:day+"";
		String strYear = calendar.get(Calendar.YEAR)+"";
		try {
			fis = new FileInputStream(file);
			System.out.println(file.getName());
			kq = client.storeFile("/picture/article/"+strYear+"/"+strMonth+strDay+"/"+file.getName(), fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return kq;
	}
	
	public static void main(String[] args) {
		FTPUtil.connection();
		FTPUtil.upload(new File("D:/data/picture_auto/2012/0331/Chieu_nay__phu94f768c9378ab7.jpg"));
		FTPUtil.close();
	}
	
}
