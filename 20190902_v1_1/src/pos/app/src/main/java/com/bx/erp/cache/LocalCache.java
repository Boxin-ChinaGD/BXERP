package com.bx.erp.cache;

import com.bx.erp.model.Vip;
import com.bx.erp.model.wx.WxVip;
import com.bx.erp.model.wx.WxVipCardDetail;

/**
 * 存储会话信息、临时会员信息、部分零售单信息
 */
public class LocalCache {
    /**
     * 存放查询会员信息后的相关数据，在支付涉及到积分操作时使用
     */
    public static Vip vip;
    public static WxVip wxVip;
    public static WxVipCardDetail wxVipCardDetail;
}
