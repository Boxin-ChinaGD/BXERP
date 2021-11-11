package wpos.model.promotion;

import wpos.model.promotion.Promotion;

public class CashReducingOfAmount extends CashReducing {
    public CashReducingOfAmount() {
        setPromotionType("满减");
    }

    public boolean isTargetPromotion(Promotion pt) {
        return (pt.getType() == 0);
    }
}

