package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

public class PromotionDataProvider {
	protected List<BaseModel> commodityList; // 普通商品

	public void setCommodityList(List<BaseModel> commodityList) {
		this.commodityList = commodityList;
	}

	public List<Promotion> generateRandomPromotions() throws Exception {
		List<Promotion> list = new ArrayList<Promotion>();
		//
		Random n = new Random();
		int count = n.nextInt(10) + 1;
		for (int i = 0; i < count; i++) {
			list.add(generateRandomPromotion());
		}
		return list;
	}

	private Promotion generateRandomPromotion() {
		Promotion promotion = new Promotion();
		promotion.setStaff(Shared.BossID);
		Random n = new Random();
		promotion.setStatus(n.nextBoolean() ? Promotion.EnumStatusPromotion.ESP_Active.getIndex() : Promotion.EnumStatusPromotion.ESP_Deleted.getIndex());
		promotion.setType(n.nextBoolean() ? Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex() : Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		if (n.nextBoolean()) {
			// 是否生成立即生效的
			promotion.setDatetimeStart(new Date());
		} else {
			int days = n.nextInt(30);
			promotion.setDatetimeStart(DatetimeUtil.getDays(new Date(), n.nextBoolean() ? days : Integer.parseInt("-" + days)));

		}
		promotion.setDatetimeEnd(DatetimeUtil.getDays(promotion.getDatetimeStart(), n.nextInt(10) + 1));
		promotion.setExcecutionThreshold(n.nextInt(500) + 1);
		promotion.setExcecutionAmount(promotion.getType() == Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex() ? n.nextInt((int) promotion.getExcecutionThreshold()) + 1 : 0);
		promotion.setExcecutionDiscount(promotion.getType() == Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex() ? Double.valueOf(("0." + n.nextInt(9) + 1)) : 0);
		promotion.setScope(n.nextBoolean() ? EnumBoolean.EB_NO.getIndex() : EnumBoolean.EB_Yes.getIndex());
		promotion.setName((promotion.getScope() == EnumBoolean.EB_NO.getIndex() ? "全场" : "指定商品") + "满" + promotion.getExcecutionThreshold()
				+ (promotion.getType() == Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex() ? "减" + promotion.getExcecutionAmount() + "元" : "打" + promotion.getExcecutionDiscount() + "折"));
		promotion.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		//
		if (promotion.getScope() == EnumBoolean.EB_Yes.getIndex()) {
			List<PromotionScope> list = new ArrayList<PromotionScope>();
			List<Integer> commIDList = new ArrayList<Integer>();
			int count = n.nextInt(20) + 1;
			for (int i = 0; i < count; i++) {
				PromotionScope ps = new PromotionScope();
				Commodity comm = null;
				do {
					comm = (Commodity) commodityList.get(n.nextInt(commodityList.size()));
				} while (commIDList.contains(comm.getID()));
				ps.setCommodityID(comm.getID());
				commIDList.add(comm.getID());
				list.add(ps);
			}
			promotion.setListSlave1(list);
		}
		return promotion;
	}
}
