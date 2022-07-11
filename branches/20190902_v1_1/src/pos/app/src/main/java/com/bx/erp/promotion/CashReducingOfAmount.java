package com.bx.erp.promotion;

import com.bx.erp.model.Promotion;

public class CashReducingOfAmount extends CashReducing {
    public CashReducingOfAmount() {
        setPromotionType("满减");
    }

    public boolean isTargetPromotion(Promotion pt) {
        return (pt.getType() == Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
    }
}

