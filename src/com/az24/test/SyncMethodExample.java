package com.az24.test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class SyncMethodExample {
	public static class Thingie {
		private  Timestamp lastAccess;
		
		 synchronized void setLastAccess(Timestamp date) {
			 this.lastAccess = new Timestamp(Calendar.getInstance().getTimeInMillis());
			 for (int i = 0; i <2; i++) {
				 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 System.out.println(this.lastAccess);
			}
			
		}
		}
		public static class MyThread extends Thread {
		private Thingie thingie;
		public MyThread(Thingie thingie) {
		this.thingie = thingie;
		}
		public void run() {
			thingie.setLastAccess(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		}
		}
		
		public static void main(String args[]) {
		Thingie thingie1 = new Thingie(),
		thingie2 = new Thingie();
		new MyThread(thingie1).start();
		new MyThread(thingie2).start();
		}
}
