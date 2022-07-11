package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.Vip;

public class CouponCodeDataProvider {
	private static int MAX_CouponCodeCreateNums = 15;

	protected List<Vip> vipList;

	protected List<Coupon> couponList;

	public List<Vip> getVipList() {
		return vipList;
	}

	public void setVipList(List<Vip> vipList) {
		this.vipList = vipList;
	}

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}

	public List<CouponCode> generateRandomCouponCodes() throws InterruptedException {
		List<CouponCode> couponCodeList = new ArrayList<>();
		Random random = new Random();
		int count = random.nextInt(MAX_CouponCodeCreateNums) + 1;
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = generateRandomCouponCode();
			if (couponCode != null) {
				couponCodeList.add(couponCode);
			}
		}
		return couponCodeList;
	}

	public CouponCode generateRandomCouponCode() throws InterruptedException {
		Random random = new Random();
		Vip vip = vipList.get(random.nextInt(vipList.size()));
		Coupon coupon = couponList.get(random.nextInt(couponList.size()));
		//
		int count = 10;
		while (vip.getBonus() < coupon.getBonus() && count > 0) {
			vip = vipList.get(random.nextInt(vipList.size()));
			count--;
		}
		if (vip.getBonus() < coupon.getBonus()) {
			return null;
		}

		CouponCode couponCodeInput = new CouponCode();
		couponCodeInput.setVipID(vip.getID());
		couponCodeInput.setCouponID(coupon.getID());
		couponCodeInput.setStatus(EnumCouponCodeStatus.ECCS_Normal.getIndex());
		return couponCodeInput;
	}
}
