package com.bx.erp.model;

import java.util.ArrayList;
import java.util.List;

import com.bx.erp.model.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class BaseWxModel extends BaseModel {
    private static final long serialVersionUID = 105418236548L;

    protected long ID;
    protected String authCode;
    public final static String WXCard = "card";
    public final static String WXCard_type = "card_type";
    public final static String WXCard_baseInfo = "base_info";
    public final static String WXCard_createTime = "create_time";

    public final static String WXCardType_GROUPON = "GROUPON";
    public final static String WXCardType_DISCOUNT = "DISCOUNT";
    public final static String WXCardType_GIFT = "GIFT";
    public final static String WXCardType_CASH = "CASH";
    public final static String WXCardType_GENERAL_COUPON = "GENERAL_COUPON";
    public final static String WXCardType_MEMBER_CARD = "MEMBER_CARD";
    public final static String WXCardType_SCENIC_TICKET = "SCENIC_TICKET";
    public final static String WXCardType_MOVIE_TICKET = "MOVIE_TICKET";
    public final static String WXCardType_BOARDING_PASS = "BOARDING_PASS";
    public final static String WXCardType_MEETING_TICKET = "MEETING_TICKET";
    public final static String WXCardType_BUS_TICKET = "BUS_TICKET";

    public static final int OFFSET_Default = 1;
    public static final int COUNT_Default = 10;

    public String getFIELD_NAME_authCode() {
        return "authCode";
    }

    public String getFIELD_NAME_ID(){
        return "ID";
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public List<?> parseN(int iUseCaseID, String s) {
        throw new RuntimeException("该函数尚未实现！");
    }

    public List<?> parseN(int iUseCaseID, JSONArray jsonArray) {
        List<BaseModel> bmList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (this.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                bmList.add(this.clone());
            }
            return bmList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BaseModel parse1(int iUseCaseID, String s) {
        try {
            return doParse1(iUseCaseID, new JSONObject(s));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected BaseModel doParse1(int iUseCaseID, JSONObject jo) {
        throw new RuntimeException("尚未实现doParse1()方法！");
    }

    public enum EnumWxCardAndCouponType {
        EWCACT_GROUPON("GROUPON", 0), // 团购券类型
        EWCACT_CASH("CASH", 1), // 代金券类型
        EWCACT_DISCOUNT("DISCOUNT", 2), // 折扣券类型
        EWCACT_GIFT("GIFT", 3), // 兑换券类型
        EWCACT_GENERAL_COUPON("GENERAL_COUPON", 4), // 优惠券类型
        EWCACT_MEMBER_CARD("MEMBER_CARD", 5); // 会员卡类型

        private String name;
        private int index;

        private EnumWxCardAndCouponType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumWxCardAndCouponType c : EnumWxCardAndCouponType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public enum EnumCardAndCouponStatus {
        EWCACS_CARD_STATUS_NOT_VERIFY("CARD_STATUS_NOT_VERIFY", 0), // 待审核
        EWCACS_CARD_STATUS_VERIFY_FAIL("CARD_STATUS_VERIFY_FAIL", 1), // 审核失败
        EWCACS_CARD_STATUS_VERIFY_OK("CARD_STATUS_VERIFY_OK", 2), // 通过审核
        EWCACS_CARD_STATUS_DELETE("CARD_STATUS_DELETE", 3), // 卡券被商户删除
        EWCACS_CARD_STATUS_DISPATCH("CARD_STATUS_DISPATCH", 4); // 在公众平台投放过的卡券

        private String name;
        private int index;

        private EnumCardAndCouponStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCardAndCouponStatus c : EnumCardAndCouponStatus.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
