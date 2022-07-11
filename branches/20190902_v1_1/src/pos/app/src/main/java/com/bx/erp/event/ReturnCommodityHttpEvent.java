package com.bx.erp.event;
/**
 * 零售单退货时，需要先进行退款(微信/现金)，成功后才创建本地的零售退货单。
 * 无论哪步成功和失败导致退货结束，都需要标记其状态并在事件处理函数中作相应的处理，比如提示退货失败原因之类的
 */
public class ReturnCommodityHttpEvent  extends BaseHttpEvent {
    @Override
    public void onEvent() {
        setEventProcessed(true);
    }
}
