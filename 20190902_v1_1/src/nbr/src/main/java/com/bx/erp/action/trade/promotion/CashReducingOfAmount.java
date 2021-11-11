package com.bx.erp.action.trade.promotion;

import org.springframework.stereotype.Component;
import com.bx.erp.model.trade.Promotion;

@Component("cashReducingOfAmount")
public class CashReducingOfAmount extends CashReducing {

	public CashReducingOfAmount() {
		setPromotionType("满减");
	}

	public boolean isTargetPromotion(Promotion pt) {
		return (pt.getType() == 0);
	}
}
