package com.bx.erp.test.commodity;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.commodity.CommodityCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;

public class CommodityThread extends Thread {
	private String dbName;
	private int staffID;
	
	/**
	 * 随便一个<>0的数
	 */
	public static final double RANDOM_NUM = 1000d;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	@AfterClass
	public void tearDown() {
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).deleteAll();
		
		Shared.printTestClassEndInfo();
	}

	@Override
	public void run() {
		try {
			CommodityCache sc = (CommodityCache) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity);
			sc.deleteAll();
			for (int i = 1; i <= 10; i++) {
				Commodity comm = BaseCommodityTest.DataInput.getCommodity();
				comm.setID(new Random().nextInt(10) + 10000000); //设置成一个不存在的商品ID：10000000，以免和现在有的ID发生冲突
				comm.setPriceRetail(RANDOM_NUM); // 随便一个<>0的数
				//
				sc.write1(comm, dbName, staffID);
				System.out.println(i + ":" + this.getName());
				System.out.println(this.getName() + "写:" + comm);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
					Thread.sleep(0);
				}
				//
				ErrorInfo ecOut = new ErrorInfo();
				BaseModel comm1 = sc.read1(comm.getID(), BaseBO.SYSTEM, ecOut, dbName);
				System.out.println(this.getName() + "读:" + comm1);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
				}
				// 写进内存的对象和读出的对象相等
				Assert.assertEquals(((Commodity)comm1).getPriceRetail(), comm.getPriceRetail());
				Assert.assertTrue(comm1.compareTo(comm) == 0);
				//清除污染
				sc.delete1(comm1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
