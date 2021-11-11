package com.bx.erp.promotion;
import com.bx.erp.model.Promotion;

public class DiscountOfAmount extends Discount {

    public DiscountOfAmount() {
        setPromotionType("折扣");
    }

    public boolean isTargetPromotion(Promotion pt) {
        return (pt.getType() == 1);
    }
}
