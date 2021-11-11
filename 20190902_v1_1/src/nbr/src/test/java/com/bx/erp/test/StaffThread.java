package com.bx.erp.test;

import java.util.Date;
import java.util.Random;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffCache;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Staff.EnumStatusStaff;

public class StaffThread extends Thread {
	private String dbName;

	public String getDBName() {
		return dbName;
	}

	public void setDBName(String dBName) {
		dbName = dBName;
	}

	public static class DataInput {
		private static Staff staffInput = new Staff();

		protected static final Staff getStaff() throws Exception {
			staffInput.setPhone(Shared.getValidStaffPhone());
			Thread.sleep(1);
			staffInput.setICID(Shared.getValidICID());
			Thread.sleep(1);
			staffInput.setWeChat("广州" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			staffInput.setPasswordExpireDate(new Date());
			staffInput.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex());
			Thread.sleep(1);
			staffInput.setShopID(1);
			staffInput.setDepartmentID(1);
			staffInput.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());

			return (Staff) staffInput.clone();
		}
	}

	@Override
	public void run() {
		try {
			StaffCache sc = (StaffCache) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff);
			// System.out.println("线程执行前："+sc.readN());
			sc.deleteAll();
			for (int i = 1; i <= 10; i++) {
				Staff staff = DataInput.getStaff();
				staff.setID(new Random().nextInt(10) + 1);
				staff.setPhone(this.getName());
				//
				sc.write1(staff, dbName, staff.getID());
				System.out.println(i + ":" + this.getName());
				System.out.println(this.getName() + "写:" + staff);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
					Thread.sleep(0);
				}

				//
				ErrorInfo ecOut = new ErrorInfo();
				BaseModel staff1 = sc.read1(staff.getID(), BaseBO.SYSTEM, ecOut, dbName);
				System.out.println(this.getName() + "读:" + staff1);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
					Thread.sleep(0);
				}
			}
			System.out.println("线程执行后：" + sc.readN(false, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
