package com.bx.erp.test;

import java.util.Date;
import java.util.Random;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.vip.VipCache;
import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;

public class VipThread extends Thread {

	private String dbName;
	private int vipID;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public static class DataInput {
		private static Vip vip = new Vip();

		protected static final Vip getVip() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			vip.setiCID(Shared.getValidICID());
			Thread.sleep(1);
			vip.setName("Tom" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			vip.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
			vip.setConsumeTimes(ran.nextInt(500) + 1);
			vip.setConsumeAmount(ran.nextInt(1000) + 1);
			vip.setDistrict("广州");
			vip.setCategory(1);
			vip.setBirthday(new Date());
			vip.setBonus(ran.nextInt(1000) + 1);
			vip.setLastConsumeDatetime(new Date());
			vip.setSn("");
			vip.setSex(0);
			vip.setLogo("123456123456");
			vip.setRemark("1111");
			
			return (Vip) vip.clone();
		}
	}

	@Override
	public void run() {
		try {
			VipCache vc = (VipCache) CacheManager.getCache(dbName, EnumCacheType.ECT_Vip);
			// System.out.println("线程执行前：" + vc.readN());
			vc.deleteAll();
			for (int i = 1; i < 10; i++) {
				Vip vip = DataInput.getVip();
				vip.setID(new Random().nextInt(10) + 1);
				vip.setLocalPosSN(this.getName());

				vc.write1(vip, dbName, vipID);
				System.out.println(i + ":" + this.getName());
				System.out.println(this.getName() + "写:" + vip);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
					Thread.sleep(0);
				}
				ErrorInfo ecOut = new ErrorInfo();
				BaseModel vip1 = vc.read1(vip.getID(), BaseBO.SYSTEM, ecOut, dbName);
				System.out.println(this.getName() + "读:" + vip1);
				if ((new Random().nextInt(100) + 1) % 2 == 0) {
					Thread.sleep(0);
				}
			}
			System.out.println("线程执行后：" + vc.readN(false, false));
		} catch (CloneNotSupportedException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
