package com.az24.test;

import java.io.File;

public class DeleteFile {
	public static void main(String[] args) {
		File file = new File("d:/data");
		if(file.isDirectory())
		{
			for (File child : file.listFiles()) {
				if(child.isFile())
				child.delete();
			}
		}
		
	}
}
