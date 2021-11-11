package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.Coupon.EnumCouponStatus;
import com.bx.erp.model.Coupon.EnumCouponType;
import com.bx.erp.model.CouponScope.EnumCouponScope;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;

public class CouponDataProvider {
	private static int MAX_randomBonus = 100;
	private static int MAX_randomPersonnalLimit = 1000;
	private static int MAX_randomEndDateTime = 365;
	private static int MIN_randomEndDateTime = 365;
	private static int MAX_randomLeastAmount = 1000;
	private static int MAX_couponCreateNums = 15;

	private static boolean firstTimeCreate = true; // 第一次创建优惠券必须是不过期的

	protected List<BaseModel> simpleCommodityList;

	public List<BaseModel> getSimpleCommodityList() {
		return simpleCommodityList;
	}

	public void setSimpleCommodityList(List<BaseModel> nomalCommodityList) {
		this.simpleCommodityList = nomalCommodityList;
	}

	public List<Coupon> generateRandomCoupons() throws InterruptedException {
		List<Coupon> couponList = new ArrayList<>();
		Random random = new Random();
		int count = random.nextInt(MAX_couponCreateNums) + 1;
		for (int i = 0; i < count; i++) {
			couponList.add(generateRandomCoupon());
		}
		return couponList;
	}

	public Coupon generateRandomCoupon() throws InterruptedException {
		Coupon couponInput = new Coupon();
		setCommonProperty(couponInput, firstTimeCreate);
		//
		Random random = new Random();
		int couponType = random.nextInt(Coupon.EnumCouponType.values().length);
		switch (EnumCouponType.values()[couponType]) {
		case ECT_Cash:
			setCashProperty(couponInput);
			break;
		case ECT_Discount:
			setDiscountProperty(couponInput);
			break;
		default:
			break;
		}

		return couponInput;
	}

	private void setCommonProperty(Coupon couponInput, boolean firstCreate) throws InterruptedException {
		Random random = new Random();
		if (random.nextBoolean() && !firstCreate) {
			couponInput.setStatus(EnumCouponStatus.ECS_Expired.getIndex());
		}
		if (random.nextBoolean()) { // 积分等于0或大于0
			couponInput.setBonus(random.nextInt(MAX_randomBonus) + 1);
		}
		couponInput.setColor("#" + generateRandomHexStr(6));
		couponInput.setDescription("默认说明" + Shared.generateStringByTime(3));
		couponInput.setPersonalLimit(random.nextInt(MAX_randomPersonnalLimit) + 1);
		couponInput.setWeekDayAvailable(random.nextInt(Coupon.WEEKDAY_Available + 1));
		couponInput.setLeastAmount(Math.floor((random.nextDouble() + MAX_randomLeastAmount) * 1000) / 1000);
		setWhenAvailableInADay(couponInput);
		Date now = new Date();
		couponInput.setBeginDateTime(DatetimeUtil.getDays(now, -random.nextInt(MIN_randomEndDateTime)));
		couponInput.setEndDateTime(DatetimeUtil.getDays(now, random.nextInt(MAX_randomEndDateTime)));
		couponInput.setQuantity(random.nextInt(Coupon.MaxQuantity) + 1);
		couponInput.setRemainingQuantity(couponInput.getQuantity());
		if (random.nextBoolean()) { // 全场或指定商品
			couponInput.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
			if (simpleCommodityList.size() > 0) {
				int max_specifiedCommodityNO = random.nextInt(simpleCommodityList.size()) + 1;
				HashSet<Integer> commodityIdSet = new HashSet<>();
				Commodity nomalCommodity = null;
				while (commodityIdSet.size() < max_specifiedCommodityNO) {
					nomalCommodity = (Commodity) simpleCommodityList.get(random.nextInt(simpleCommodityList.size()));
					commodityIdSet.add(nomalCommodity.getID());
				}
				Integer[] commodityIdArr = new Integer[commodityIdSet.size()];
				commodityIdSet.toArray(commodityIdArr);
				String commodityIdStr = "";
				for (int i = 0; i < commodityIdArr.length; i++) {
					commodityIdStr += commodityIdArr[i] + ",";
				}
				couponInput.setCommodityIDs(commodityIdStr);
			} else {
				couponInput.setCommodityIDs("1"); // 极端情况：没有生成任何一个单品，只能采用NBR默认DB里的F_ID=1的单品
			}
		}
		firstCreate = false;
	}

	private void setCashProperty(Coupon couponInput) {
		Random random = new Random();
		couponInput.setType(EnumCouponType.ECT_Cash.getIndex());
		couponInput.setReduceAmount(couponInput.getLeastAmount() - random.nextInt((int) couponInput.getLeastAmount()));
		couponInput.setTitle((couponInput.getScope() == EnumCouponScope.ECS_SpecifiedCommodities.getIndex() ? "指定商品满减券" : "全场满减券")); //
	}

	private void setDiscountProperty(Coupon couponInput) {
		Random random = new Random();
		couponInput.setType(EnumCouponType.ECT_Discount.getIndex());
		double discount = Math.floor((random.nextDouble()) * 100) / 100; //
		while (Math.abs(GeneralUtil.sub(discount, 0)) < Coupon.TOLERANCE) { // 防止偶尔出现0.0的情况
			discount = Math.floor((random.nextDouble()) * 100) / 100;
		}
		couponInput.setTitle((couponInput.getScope() == EnumCouponScope.ECS_SpecifiedCommodities.getIndex() ? "指定商品满折券" : "全场满折券")); //
		couponInput.setDiscount(discount);
	}

	public static String generateRandomHexStr(int len) {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < len; i++) {
				// 随机生成0-15的数值并转换成16进制
				stringBuffer.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return stringBuffer.toString().toUpperCase();
		} catch (Exception e) {
			System.out.println("获取16进制字符串异常，返回默认...");
			return "00CCCC";
		}
	}

	public static void setWhenAvailableInADay(Coupon coupon) {
		Random random = new Random();
		String str = "%02d";
		int ihourEnd = random.nextInt(23) + 1;
		String hourEnd = String.format(str, ihourEnd);
		String minuteEnd = String.format(str, random.nextInt(60));
		String secondEnd = String.format(str, random.nextInt(60));
		coupon.setEndTime(hourEnd + ":" + minuteEnd + ":" + secondEnd);
		String hourBegin = String.format(str, random.nextInt(ihourEnd));
		String minuteBegin = String.format(str, random.nextInt(60));
		String secondBegin = String.format(str, random.nextInt(60));
		coupon.setBeginTime(hourBegin + ":" + minuteBegin + ":" + secondBegin);
	}
}
