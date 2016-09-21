package hdc.util.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	static public int emptyFolder(File folder, boolean ignoreCannotDel) throws IOException {
		int counter = 0 ;
		if(folder.exists() &&  folder.isDirectory()) {
			File[] child =  folder.listFiles();
			for(int i = 0; i < child.length; i++) {
				File  file =  child[i] ;
				if(file.isDirectory()) counter += emptyFolder(file, ignoreCannotDel) ;
				boolean result = file.delete() ;
				if(!result && !ignoreCannotDel) {
					throw new IOException("Cannot delete " + file.getAbsolutePath());
				} else {
					counter++ ;
				}
			}
		}
		return counter ;
	}

	static public int remove(File file, boolean ignoreCannotDelete) throws Exception {
		int counter = 0 ;
		if(file.exists())  {
			if(file.isDirectory()) {
				counter += emptyFolder(file, ignoreCannotDelete) ;
			}
			boolean result = file.delete() ;
			if(!result && !ignoreCannotDelete) {
				throw new Exception("Cannot delete " + file.getAbsolutePath());
			} else {
				counter++ ;
			}
		}
		return counter ;
	}

	static public void removeIfExist(String path) throws Exception {
		File file = new File(path) ;
		if(file.exists()) remove(file, true) ;
	}

	static public boolean exist(String path) throws Exception {
		File file = new File(path) ;
		return file.exists() ;
	}
	
	static public void mkdirs(String path) throws Exception {
		File file = new File(path) ;
		if(!file.exists()) {
			if(!file.mkdirs()) {
				throw new Exception("Cannot create directory " + path) ;
			}
		}
	}

	static public int cp(String src, String dest) throws Exception {
		int counter = 0;
		File srcFolder = new java.io.File(src) ;
		if(!srcFolder.exists()) throw new Exception(src + " does not exist") ;

		if(srcFolder.isFile()) {
			File destFolder = new java.io.File(dest);
			if (destFolder.isFile()) {
				dest = destFolder.getParent();
			}
			if (destFolder.exists()) {
				dest = dest + "/" + srcFolder.getName();
			}
			InputStream input = new java.io.FileInputStream(srcFolder) ;
			OutputStream output = new java.io.FileOutputStream(dest) ;
			byte[] buff = new byte[8192] ;
			int len = 0 ;
			while ((len = input.read(buff)) > 0) {
				output.write(buff, 0, len);
			}
			input.close();
			output.close();
			counter++ ;
		} else {
			File destFolder = new File(dest) ;
			if(!destFolder.exists()) {
				destFolder.mkdirs() ;
			}
			File[] child =  srcFolder.listFiles();
			for(int i = 0; i < child.length; i++) {
				File file =  child[i] ;
				if(file.isFile())  {
					counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" +  file.getName());
				} else {
					counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + file.getName());
				}
			}
		}
		return counter ;
	}

	static public void copyTo(InputStream src, String dest) throws IOException {
		File destFolder = new java.io.File(dest);
		if (destFolder.isFile()) {
			dest = destFolder.getParent();
		}
		OutputStream output = new java.io.FileOutputStream(dest) ;
		byte[] buff = new byte[8192] ;
		int len = 0 ;
		while ((len = src.read(buff)) > 0) {
			output.write(buff, 0, len);
		}
		src.close();
		output.close();
	}
	
	static public void writeToFile(String fname, String data, boolean append) throws IOException {
	  writeToFile(fname, data.getBytes("UTF-8"), append) ;
  }

	static public void writeToFile(String fname, byte[] data, boolean append) throws IOException {
		FileOutputStream os = new FileOutputStream(fname, append) ;
		os.write(data) ;
		os.close() ;
	}

	static public <T extends FileFilter> List<String> findFiles(File dir, T filter) throws IOException {
		List<String> holder = new ArrayList<String>() ;
		findFiles(holder, dir, filter) ;
		return holder ;
	}

	static public <T extends FileFilter> void findFiles(List<String> holder, File file, T filter) throws IOException {
		if(file.isFile() && filter.accept(file)) {
			holder.add(file.getAbsolutePath()) ;
			return ;
		} else if(file.isDirectory()) {
			for(File child : file.listFiles()) {
				findFiles(holder, child, filter) ;
			}
		}
	}
	
	public static void deleteFile(String folder) {
		File file = new File(folder);
		if(file.isDirectory())
		{
			for (File child : file.listFiles()) {
				if(child.isFile())
				child.delete();
			}
		}
		
	}
	
	 public static ArrayList<String> readFileComicUrls()
	  {
		 ArrayList<String> urls = new ArrayList<String>();
		  try{
			  FileInputStream fstream = new FileInputStream("./conf/comicUrl.txt");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));

			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  System.out.println (strLine);
				  urls.add(strLine);
			  }
			  in.close();
			  
		   }catch (Exception e){
			   System.err.println("Error: " + e.getMessage());
		  }
		return urls;
	  }
	 
	 
	 public static void saveImage(String imageUrl, String destinationFile)  {
		 try {
			 URL url = new URL(imageUrl);
				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(destinationFile);

				byte[] b = new byte[2048];
				int length;

				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}

				is.close();
				os.close();
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}
			
		}
}