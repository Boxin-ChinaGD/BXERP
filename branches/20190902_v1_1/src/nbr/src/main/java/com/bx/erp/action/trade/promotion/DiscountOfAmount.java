package com.bx.erp.action.trade.promotion;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.model.trade.Promotion;

@Component("discountOfAmount")
public class DiscountOfAmount extends Discount {
	@Resource
	private CommodityBO commodityBO;

	public DiscountOfAmount() {
		setPromotionType("折扣");
	}

	public boolean isTargetPromotion(Promotion pt) {
		return (pt.getType() == 1);
	}

}
