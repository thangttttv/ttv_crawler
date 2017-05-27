package com.vtc.crawler;

import com.az24.dao.VietlotoDAO;

public class ThongKeBoSo645 extends Thread {
	public void run(){
		VietlotoDAO vietlotoDAO = new VietlotoDAO();
		int i = 1;
		while(i<=45){
			String boso = i<10?"0"+i:""+i;
			System.out.println(boso);
			int solan = vietlotoDAO.countSolanVe645(boso);
			int id = vietlotoDAO.checkThongKe645(boso);
			if(solan>0){
			if(id==0){
				String ngay_ve_cuoi = vietlotoDAO.lastdateByBoSo(boso);
				vietlotoDAO.saveThongKe645(boso, solan, ngay_ve_cuoi);
			}else{
				String ngay_ve_cuoi = vietlotoDAO.lastdateByBoSo(boso);
				vietlotoDAO.updateThongKe645(id, solan, ngay_ve_cuoi);
			}}
			i++;
		}
	}
	
	public static void main(String[] args) {
		ThongKeBoSo645 boSo645 = new ThongKeBoSo645();
		boSo645.start();
	}
}
