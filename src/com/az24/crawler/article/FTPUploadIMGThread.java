package com.az24.crawler.article;

import java.io.File;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.az24.util.FTPUtil;
	
public class FTPUploadIMGThread {
		private static FTPUploadIMGThread cFileThread;
		private static long i;
		private static boolean isRun = false;
	
		private FTPUploadIMGThread() {
		}
	
		public static FTPUploadIMGThread getInstance() {
			cFileThread = cFileThread != null ? cFileThread : new FTPUploadIMGThread();
			return cFileThread;
		}
	
		public void run() {
			if (!isRun) {
				Timer timer = new Timer();
				Calendar date = Calendar.getInstance();
				timer.schedule(new CDRUploadFile(), date.getTime(), 1000 * 60 * 2);
			} else {
				System.out.println("CDR File has run" + i);
			}
			isRun = true;
		}
	
		public static void main(String args[]) {
			FTPUploadIMGThread cdFileThread = FTPUploadIMGThread.getInstance();
			cdFileThread.run();
			
		}
	}
	
	
	class CDRUploadFile extends TimerTask {
		public CDRUploadFile() {
	
		}
	
		public void run() {
			int i = 0;
	        boolean kq = false;
	        File folderCDR=new File("D:/data/picture_auto/2012/0331/");
	        FTPUtil.connection();
	        if(folderCDR.exists())
	        {
	        	File[] files = folderCDR.listFiles();
	        	File file = null;
	        	if(files!=null&&files.length>0)
	        	{
	        		i = 0;
	        		while(i<files.length)
	        		{
	        			file = files[i];
	        			kq = FTPUtil.upload(file);
	        			//if(kq) file.delete();
	        			i++;
	        		}
	        	}
	        }
	        FTPUtil.close();
		}
		
}
