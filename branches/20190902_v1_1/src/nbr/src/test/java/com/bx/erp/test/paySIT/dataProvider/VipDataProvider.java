package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bx.erp.action.VipAction;
import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Vip.EnumSexVip;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

public class VipDataProvider {
	
	public static final String[] realVipName = {"赵", "钱", "孙", "李"};
	public static final String[] district = {"广州", "深圳", "武汉", "", "北京", "杭州", "哈尔滨", "乌鲁木齐", "海口", "哈尔滨"};
	private static int MIN_randomVipBirthDay = 365 * 100;
	
	private static int MAX_VipCreateNums = 15;
	
	private static int MAX_VipBonus = 300;
	
	public List<Vip> generateRandomVips() throws InterruptedException {
		List<Vip> vipList = new ArrayList<>();
		Random random = new Random();
		int count = random.nextInt(MAX_VipCreateNums) + 1;
		for (int i = 0; i < count; i++) {
			vipList.add(generateRandomVip());
		}
		return vipList;
	}
	
	public Vip generateRandomVip() throws InterruptedException {
		Vip vipInput = new Vip();
		// 目前只有一种会员
		setCommonProperty(vipInput);
		return vipInput;
	}
	
	private void setCommonProperty(Vip vipInput) throws InterruptedException {
		Random random = new Random();
		vipInput.setCardID(VipAction.DEFAULT_VipCardID);
		if(random.nextBoolean()) {
			vipInput.setSex(EnumSexVip.ESV_Female.getIndex());
		}
		vipInput.setiCID(Shared.getValidICID());
		vipInput.setMobile(Shared.getValidStaffPhone());
		vipInput.setName(realVipName[random.nextInt(realVipName.length)] + Shared.generateStringByTime(9));
		vipInput.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
		vipInput.setConsumeTimes(0);
		vipInput.setConsumeAmount(0);
		vipInput.setDistrict(district[random.nextInt(district.length)]);
		vipInput.setCategory(VipAction.DEFAULT_VipCategoryID);
		Date now = new Date();
		vipInput.setBirthday(DatetimeUtil.getDays(now, -random.nextInt(MIN_randomVipBirthDay)));
		vipInput.setLastConsumeDatetime(new Date());
	}
	
	public static void updateBonus(Vip vip) {
		Random random = new Random();
		// 会员创建时，默认有10积分，让其update后的积分大于0  
		vip.setBonus(random.nextInt(MAX_VipBonus) + 10);
		vip.setManuallyAdded(EnumBoolean.EB_Yes.getIndex());
		vip.setRemarkForBonusHistory("PaySIT给随机会员增加积分");
	}
}
