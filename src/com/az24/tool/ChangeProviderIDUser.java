package com.az24.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.az24.db.pool.C3p0AMSGAMEPool;

public class ChangeProviderIDUser {
	
	public static void updateProviderID(String username)
	{
		try {
			java.sql.Connection connection = C3p0AMSGAMEPool.getConnection();
			connection.setAutoCommit(false);
			String sql  = "UPDATE acm_user SET provider_id = 156 WHERE username = ? ";
			java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,username);
			if(preparedStatement.executeUpdate()==1)
			connection.commit();
			else connection.rollback();
			
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//SELECT username FROM acm_user WHERE provider_id = 62 AND MONTH(FROM_UNIXTIME(create_date)) = 11 LIMIT 1000
	//SELECT username FROM acm_user WHERE provider_id  IN(47,48,49,57,58,63,94) AND MONTH(FROM_UNIXTIME(create_date)) = 11 LIMIT 5000

	
	public static void main(String[] args) {
		try {
				FileInputStream propsFile = new java.io.FileInputStream("./conf/kh.xls");				
				Workbook w =Workbook.getWorkbook(propsFile);
				Sheet sheet = w.getSheet(0);
				Cell cell = null;			
				
				for (int i = 1; i < sheet.getRows(); i++) {
					cell = sheet.getCell(0, i);			
					updateProviderID(cell.getContents());
					System.out.println(cell.getContents());				
				}
			} catch (FileNotFoundException e) {			
				e.printStackTrace();
			} catch (BiffException e) {			
				e.printStackTrace();
			} catch (IOException e) {			
				e.printStackTrace();
			}
	}
}
