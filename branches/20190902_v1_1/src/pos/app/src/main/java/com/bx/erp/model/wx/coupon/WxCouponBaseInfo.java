package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;
import com.bx.erp.model.WxDateInfo;
import com.bx.erp.model.WxSku;
import com.bx.erp.model.WxSubMerchantInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCouponBaseInfo extends BaseWxModel {

    private static final long serialVersionUID = -4275551693303239632L;

    public static final WxCouponBaseInfoField field = new WxCouponBaseInfoField();

    private WxSubMerchantInfo wxSubMerchantInfo;

    private int couponDetailID;

    private String logo_url;

    private String code_type;

    private String brand_name;

    private String title;

    private String color;

    private String notice;

    private String description;

    private WxSku sku;

    private int skuID;

    private WxDateInfo date_info;

    private int dateInfoID;

    private boolean use_custom_code;

    private String get_custom_code_mode;

    private boolean bind_openid;

    private String service_phone;

    private String location_id_list;

    private int use_all_locations;

    private String center_title;

    private String center_sub_title;

    private String center_url;

    private String center_app_brand_user_name;

    private String center_app_brand_pass;

    private String custom_url_name;

    private String custom_url;

    private String custom_url_sub_title;

    private String custom_app_brand_user_name;

    private String custom_app_brand_pass;

    private String promotion_url_name;

    private String promotion_url;

    private String promotion_url_sub_title;

    private String promotion_app_brand_user_name;

    private String promotion_app_brand_pass;

    private int get_limit;

    private int use_limit;

    private boolean can_share;

    private boolean can_give_friend;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public WxSubMerchantInfo getWxSubMerchantInfo() {
        return wxSubMerchantInfo;
    }

    public void setWxSubMerchantInfo(WxSubMerchantInfo wxSubMerchantInfo) {
        this.wxSubMerchantInfo = wxSubMerchantInfo;
    }

    public int getCouponDetailID() {
        return couponDetailID;
    }

    public void setCouponDetailID(int couponDetailID) {
        this.couponDetailID = couponDetailID;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WxSku getSku() {
        return sku;
    }

    public void setSku(WxSku sku) {
        this.sku = sku;
    }

    public int getSkuID() {
        return skuID;
    }

    public void setSkuID(int skuID) {
        this.skuID = skuID;
    }

    public WxDateInfo getDate_info() {
        return date_info;
    }

    public void setDate_info(WxDateInfo date_info) {
        this.date_info = date_info;
    }

    public int getDateInfoID() {
        return dateInfoID;
    }

    public void setDateInfoID(int dateInfoID) {
        this.dateInfoID = dateInfoID;
    }

    public boolean getUse_custom_code() {
        return use_custom_code;
    }

    public void setUse_custom_code(boolean use_custom_code) {
        this.use_custom_code = use_custom_code;
    }

    public String getGet_custom_code_mode() {
        return get_custom_code_mode;
    }

    public void setGet_custom_code_mode(String get_custom_code_mode) {
        this.get_custom_code_mode = get_custom_code_mode;
    }

    public boolean getBind_openid() {
        return bind_openid;
    }

    public void setBind_openid(boolean bind_openid) {
        this.bind_openid = bind_openid;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getLocation_id_list() {
        return location_id_list;
    }

    public void setLocation_id_list(String location_id_list) {
        this.location_id_list = location_id_list;
    }

    public int getUse_all_locations() {
        return use_all_locations;
    }

    public void setUse_all_locations(int use_all_locations) {
        this.use_all_locations = use_all_locations;
    }

    public String getCenter_title() {
        return center_title;
    }

    public void setCenter_title(String center_title) {
        this.center_title = center_title;
    }

    public String getCenter_sub_title() {
        return center_sub_title;
    }

    public void setCenter_sub_title(String center_sub_title) {
        this.center_sub_title = center_sub_title;
    }

    public String getCenter_url() {
        return center_url;
    }

    public void setCenter_url(String center_url) {
        this.center_url = center_url;
    }

    public String getCenter_app_brand_user_name() {
        return center_app_brand_user_name;
    }

    public void setCenter_app_brand_user_name(String center_app_brand_user_name) {
        this.center_app_brand_user_name = center_app_brand_user_name;
    }

    public String getCenter_app_brand_pass() {
        return center_app_brand_pass;
    }

    public void setCenter_app_brand_pass(String center_app_brand_pass) {
        this.center_app_brand_pass = center_app_brand_pass;
    }

    public String getCustom_url_name() {
        return custom_url_name;
    }

    public void setCustom_url_name(String custom_url_name) {
        this.custom_url_name = custom_url_name;
    }

    public String getCustom_url() {
        return custom_url;
    }

    public void setCustom_url(String custom_url) {
        this.custom_url = custom_url;
    }

    public String getCustom_url_sub_title() {
        return custom_url_sub_title;
    }

    public void setCustom_url_sub_title(String custom_url_sub_title) {
        this.custom_url_sub_title = custom_url_sub_title;
    }

    public String getCustom_app_brand_user_name() {
        return custom_app_brand_user_name;
    }

    public void setCustom_app_brand_user_name(String custom_app_brand_user_name) {
        this.custom_app_brand_user_name = custom_app_brand_user_name;
    }

    public String getCustom_app_brand_pass() {
        return custom_app_brand_pass;
    }

    public void setCustom_app_brand_pass(String custom_app_brand_pass) {
        this.custom_app_brand_pass = custom_app_brand_pass;
    }

    public String getPromotion_url_name() {
        return promotion_url_name;
    }

    public void setPromotion_url_name(String promotion_url_name) {
        this.promotion_url_name = promotion_url_name;
    }

    public String getPromotion_url() {
        return promotion_url;
    }

    public void setPromotion_url(String promotion_url) {
        this.promotion_url = promotion_url;
    }

    public String getPromotion_url_sub_title() {
        return promotion_url_sub_title;
    }

    public void setPromotion_url_sub_title(String promotion_url_sub_title) {
        this.promotion_url_sub_title = promotion_url_sub_title;
    }

    public String getPromotion_app_brand_user_name() {
        return promotion_app_brand_user_name;
    }

    public void setPromotion_app_brand_user_name(String promotion_app_brand_user_name) {
        this.promotion_app_brand_user_name = promotion_app_brand_user_name;
    }

    public String getPromotion_app_brand_pass() {
        return promotion_app_brand_pass;
    }

    public void setPromotion_app_brand_pass(String promotion_app_brand_pass) {
        this.promotion_app_brand_pass = promotion_app_brand_pass;
    }

    public int getGet_limit() {
        return get_limit;
    }

    public void setGet_limit(int get_limit) {
        this.get_limit = get_limit;
    }

    public int getUse_limit() {
        return use_limit;
    }

    public void setUse_limit(int use_limit) {
        this.use_limit = use_limit;
    }

    public boolean getCan_share() {
        return can_share;
    }

    public void setCan_share(boolean can_share) {
        this.can_share = can_share;
    }

    public boolean getCan_give_friend() {
        return can_give_friend;
    }

    public void setCan_give_friend(boolean can_give_friend) {
        this.can_give_friend = can_give_friend;
    }

    @Override
    public String toString() {
        return "WxCouponBaseInfo [couponDetailID=" + couponDetailID + ", logo_url=" + logo_url + ", code_type=" + code_type + ", brand_name=" + brand_name + ", title=" + title + ", color=" + color + ", notice=" + notice + ", description="
                + description + ", sku=" + sku + ", skuID=" + skuID + ", date_info=" + date_info + ", dateInfoID=" + dateInfoID + ", use_custom_code=" + use_custom_code + ", get_custom_code_mode=" + get_custom_code_mode + ", bind_openid="
                + bind_openid + ", service_phone=" + service_phone + ", location_id_list=" + location_id_list + ", use_all_locations=" + use_all_locations + ", center_title=" + center_title + ", center_sub_title=" + center_sub_title
                + ", center_url=" + center_url + ", center_app_brand_user_name=" + center_app_brand_user_name + ", center_app_brand_pass=" + center_app_brand_pass + ", custom_url_name=" + custom_url_name + ", custom_url=" + custom_url
                + ", custom_url_sub_title=" + custom_url_sub_title + ", custom_app_brand_user_name=" + custom_app_brand_user_name + ", custom_app_brand_pass=" + custom_app_brand_pass + ", promotion_url_name=" + promotion_url_name
                + ", promotion_url=" + promotion_url + ", promotion_url_sub_title=" + promotion_url_sub_title + ", promotion_app_brand_user_name=" + promotion_app_brand_user_name + ", promotion_app_brand_pass=" + promotion_app_brand_pass
                + ", get_limit=" + get_limit + ", use_limit=" + use_limit + ", can_share=" + can_share + ", can_give_friend=" + can_give_friend + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        try {
            Object tmp = null;
            tmp = jo.get(field.getFIELD_NAME_ALIAS_wxSubMerchantInfo());
            if (tmp != null) {
                wxSubMerchantInfo = (WxSubMerchantInfo) new WxSubMerchantInfo().parse1(jo.getString(field.getFIELD_NAME_ALIAS_wxSubMerchantInfo()));
            }
            ID = jo.getInt(getFIELD_NAME_ID());
            couponDetailID = jo.getInt(field.getFIELD_NAME_couponDetailID());
            skuID = jo.getInt(field.getFIELD_NAME_skuID());
            dateInfoID = jo.getInt(field.getFIELD_NAME_dateInfoID());
            if (!(jo.getString(field.getFIELD_NAME_date_info()).equals("null")) && jo.getString(field.getFIELD_NAME_date_info()) != null) {
                date_info = (WxDateInfo) new WxDateInfo().parse1(jo.getString(field.getFIELD_NAME_date_info()));
            }
            if (!(jo.getString(field.getFIELD_NAME_sku()).equals("null")) && jo.getString(field.getFIELD_NAME_sku()) != null) {
                sku = (WxSku) new WxSku().parse1(jo.getString(field.getFIELD_NAME_sku()));
            }
            logo_url = jo.getString(field.getFIELD_NAME_logo_url());
            code_type = jo.getString(field.getFIELD_NAME_code_type());
            brand_name = jo.getString(field.getFIELD_NAME_brand_name());
            title = jo.getString(field.getFIELD_NAME_title());
            color = jo.getString(field.getFIELD_NAME_color());
            notice = jo.getString(field.getFIELD_NAME_notice());
            description = jo.getString(field.getFIELD_NAME_description());
            tmp = jo.get(field.getFIELD_NAME_use_custom_code());
            if (tmp != null) {
                use_custom_code = jo.getBoolean(field.getFIELD_NAME_use_custom_code());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_get_custom_code_mode());
            if (tmp != null) {
                get_custom_code_mode = jo.getString(field.getFIELD_NAME_get_custom_code_mode());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_bind_openid());
            if (tmp != null) {
                bind_openid = jo.getBoolean(field.getFIELD_NAME_bind_openid());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_service_phone());
            if (tmp != null) {
                service_phone = jo.getString(field.getFIELD_NAME_service_phone());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_location_id_list());
            if (tmp != null) {
                location_id_list = jo.getString(field.getFIELD_NAME_location_id_list());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_use_all_locations());
            if (tmp != null) {
                use_all_locations = jo.getInt(field.getFIELD_NAME_use_all_locations());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_center_title());
            if (tmp != null) {
                center_title = jo.getString(field.getFIELD_NAME_center_title());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_center_sub_title());
            if (tmp != null) {
                center_sub_title = jo.getString(field.getFIELD_NAME_center_sub_title());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_center_url());
            if (tmp != null) {
                center_url = jo.getString(field.getFIELD_NAME_center_url());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_center_app_brand_user_name());
            if (tmp != null) {
                center_app_brand_user_name = jo.getString(field.getFIELD_NAME_center_app_brand_user_name());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_center_app_brand_pass());
            if (tmp != null) {
                center_app_brand_pass = jo.getString(field.getFIELD_NAME_center_app_brand_pass());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_custom_url_name());
            if (tmp != null) {
                custom_url_name = jo.getString(field.getFIELD_NAME_custom_url_name());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_custom_url());
            if (tmp != null) {
                custom_url = jo.getString(field.getFIELD_NAME_custom_url());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_custom_url_sub_title());
            if (tmp != null) {
                custom_url_sub_title = jo.getString(field.getFIELD_NAME_custom_url_sub_title());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_custom_app_brand_user_name());
            if (tmp != null) {
                custom_app_brand_user_name = jo.getString(field.getFIELD_NAME_custom_app_brand_user_name());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_custom_app_brand_pass());
            if (tmp != null) {
                custom_app_brand_pass = jo.getString(field.getFIELD_NAME_custom_app_brand_pass());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_promotion_url_name());
            if (tmp != null) {
                promotion_url_name = jo.getString(field.getFIELD_NAME_promotion_url_name());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_promotion_url());
            if (tmp != null) {
                promotion_url = jo.getString(field.getFIELD_NAME_promotion_url());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_promotion_url_sub_title());
            if (tmp != null) {
                promotion_url_sub_title = jo.getString(field.getFIELD_NAME_promotion_url_sub_title());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_promotion_app_brand_user_name());
            if (tmp != null) {
                promotion_app_brand_user_name = jo.getString(field.getFIELD_NAME_promotion_app_brand_user_name());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_promotion_app_brand_pass());
            if (tmp != null) {
                promotion_app_brand_pass = jo.getString(field.getFIELD_NAME_promotion_app_brand_pass());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_get_limit());
            if (tmp != null) {
                get_limit = jo.getInt(field.getFIELD_NAME_get_limit());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_use_limit());
            if (tmp != null) {
                use_limit = jo.getInt(field.getFIELD_NAME_use_limit());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_can_share());
            if (tmp != null) {
                can_share = jo.getBoolean(field.getFIELD_NAME_can_share());
            }
            //
            tmp = jo.get(field.getFIELD_NAME_can_give_friend());
            if (tmp != null) {
                can_give_friend = jo.getBoolean(field.getFIELD_NAME_can_give_friend());
            }
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCouponBaseInfoList = new ArrayList<BaseWxModel>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < wxCouponBaseInfoList.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponBaseInfo wxCouponBaseInfo = new WxCouponBaseInfo();
                wxCouponBaseInfo.doParse1(jsonObject);
                wxCouponBaseInfoList.add(wxCouponBaseInfo);
            }
            return wxCouponBaseInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        //
        WxCouponBaseInfo wxCouponBaseInfo = (WxCouponBaseInfo) arg0;
        if ((ignoreIDInComparision == true ? true : wxCouponBaseInfo.getID() == ID && printComparator(getFIELD_NAME_ID()))//
                && wxCouponBaseInfo.getCouponDetailID() == couponDetailID && printComparator(field.getFIELD_NAME_couponDetailID())//
                && wxCouponBaseInfo.getLogo_url().equals(logo_url) && printComparator(field.getFIELD_NAME_logo_url())//
                && wxCouponBaseInfo.getCode_type().equals(code_type) && printComparator(field.getFIELD_NAME_code_type())//
                && wxCouponBaseInfo.getBrand_name().equals(brand_name) && printComparator(field.getFIELD_NAME_brand_name())//
                && wxCouponBaseInfo.getTitle().equals(title) && printComparator(field.getFIELD_NAME_title())//
                && wxCouponBaseInfo.getColor().equals(color) && printComparator(field.getFIELD_NAME_color())//
                && wxCouponBaseInfo.getNotice().equals(notice) && printComparator(field.getFIELD_NAME_notice())//
                && wxCouponBaseInfo.getDescription().equals(description) && printComparator(field.getFIELD_NAME_description())//
                // && wxCouponBaseInfo.getSku().equals(sku) &&
                // printComparator(getFIELD_NAME_sku())//
                && wxCouponBaseInfo.getSkuID() == skuID && printComparator(field.getFIELD_NAME_skuID())//
                // && wxCouponBaseInfo.getDate_info().equals(date_info) &&
                // printComparator(getFIELD_NAME_date_info())//
                && wxCouponBaseInfo.getDateInfoID() == dateInfoID && printComparator(field.getFIELD_NAME_dateInfoID())//
                && wxCouponBaseInfo.getUse_custom_code() == use_custom_code && printComparator(field.getFIELD_NAME_use_custom_code())//
                && wxCouponBaseInfo.getGet_custom_code_mode().equals(get_custom_code_mode) && printComparator(field.getFIELD_NAME_get_custom_code_mode())//
                && wxCouponBaseInfo.getBind_openid() == bind_openid && printComparator(field.getFIELD_NAME_bind_openid())//
                && wxCouponBaseInfo.getService_phone().equals(service_phone) && printComparator(field.getFIELD_NAME_service_phone())//
                && wxCouponBaseInfo.getLocation_id_list().equals(location_id_list) && printComparator(field.getFIELD_NAME_location_id_list())//
                && wxCouponBaseInfo.getUse_all_locations() == use_all_locations && printComparator(field.getFIELD_NAME_use_all_locations())//
                && wxCouponBaseInfo.getCenter_title().equals(center_title) && printComparator(field.getFIELD_NAME_center_title())//
                && wxCouponBaseInfo.getCenter_sub_title().equals(center_sub_title) && printComparator(field.getFIELD_NAME_center_sub_title())//
                && wxCouponBaseInfo.getCenter_url().equals(center_url) && printComparator(field.getFIELD_NAME_center_url())//
                && wxCouponBaseInfo.getCenter_app_brand_user_name().equals(center_app_brand_user_name) && printComparator(field.getFIELD_NAME_center_app_brand_user_name())//
                && wxCouponBaseInfo.getCenter_app_brand_pass().equals(center_app_brand_pass) && printComparator(field.getFIELD_NAME_center_app_brand_pass())//
                && wxCouponBaseInfo.getCustom_url_name().equals(custom_url_name) && printComparator(field.getFIELD_NAME_custom_url_name())//
                && wxCouponBaseInfo.getCustom_url().equals(custom_url) && printComparator(field.getFIELD_NAME_custom_url())//
                && wxCouponBaseInfo.getCustom_url_sub_title().equals(custom_url_sub_title) && printComparator(field.getFIELD_NAME_custom_url_sub_title())//
                && wxCouponBaseInfo.getCustom_app_brand_user_name().equals(custom_app_brand_user_name) && printComparator(field.getFIELD_NAME_custom_app_brand_user_name())//
                && wxCouponBaseInfo.getCustom_app_brand_pass().equals(custom_app_brand_pass) && printComparator(field.getFIELD_NAME_custom_app_brand_pass())//
                && wxCouponBaseInfo.getPromotion_url_name().equals(promotion_url_name) && printComparator(field.getFIELD_NAME_promotion_url_name())//
                && wxCouponBaseInfo.getPromotion_url().equals(promotion_url) && printComparator(field.getFIELD_NAME_promotion_url())//
                && wxCouponBaseInfo.getPromotion_url_sub_title().equals(promotion_url_sub_title) && printComparator(field.getFIELD_NAME_promotion_url_sub_title())//
                && wxCouponBaseInfo.getPromotion_app_brand_user_name().equals(promotion_app_brand_user_name) && printComparator(field.getFIELD_NAME_promotion_app_brand_user_name())//
                && wxCouponBaseInfo.getPromotion_app_brand_pass().equals(promotion_app_brand_pass) && printComparator(field.getFIELD_NAME_promotion_app_brand_pass())//
                && wxCouponBaseInfo.getGet_limit() == get_limit && printComparator(field.getFIELD_NAME_get_limit())//
                && wxCouponBaseInfo.getUse_limit() == use_limit && printComparator(field.getFIELD_NAME_use_limit())//
                && wxCouponBaseInfo.getCan_share() == can_share && printComparator(field.getFIELD_NAME_can_share())//
                && wxCouponBaseInfo.getCan_give_friend() == can_give_friend && printComparator(field.getFIELD_NAME_can_give_friend())//
                ) {
            return 0;
        }
        //
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponBaseInfo wxCouponBaseInfo = new WxCouponBaseInfo();
        //
        wxCouponBaseInfo.setCouponDetailID(couponDetailID);
        wxCouponBaseInfo.setLogo_url(logo_url);
        wxCouponBaseInfo.setCode_type(code_type);
        wxCouponBaseInfo.setBrand_name(brand_name);
        wxCouponBaseInfo.setTitle(title);
        wxCouponBaseInfo.setColor(color);
        wxCouponBaseInfo.setNotice(notice);
        wxCouponBaseInfo.setDescription(description);
        if (sku != null) {
            wxCouponBaseInfo.setSku((WxSku) sku.clone());
        }
        wxCouponBaseInfo.setSkuID(skuID);
        if (date_info != null) {
            wxCouponBaseInfo.setDate_info((WxDateInfo) date_info.clone());
        }
        wxCouponBaseInfo.setDateInfoID(dateInfoID);
        wxCouponBaseInfo.setUse_custom_code(use_custom_code);
        wxCouponBaseInfo.setGet_custom_code_mode(get_custom_code_mode);
        wxCouponBaseInfo.setBind_openid(bind_openid);
        wxCouponBaseInfo.setService_phone(service_phone);
        wxCouponBaseInfo.setLocation_id_list(location_id_list);
        wxCouponBaseInfo.setUse_all_locations(use_all_locations);
        wxCouponBaseInfo.setCenter_title(center_title);
        wxCouponBaseInfo.setCenter_sub_title(center_sub_title);
        wxCouponBaseInfo.setCenter_url(center_url);
        wxCouponBaseInfo.setCenter_app_brand_user_name(center_app_brand_user_name);
        wxCouponBaseInfo.setCenter_app_brand_pass(center_app_brand_pass);
        wxCouponBaseInfo.setCustom_url_name(custom_url_name);
        wxCouponBaseInfo.setCustom_url(custom_url);
        wxCouponBaseInfo.setCustom_url_sub_title(custom_url_sub_title);
        wxCouponBaseInfo.setCustom_app_brand_user_name(custom_app_brand_user_name);
        wxCouponBaseInfo.setCustom_app_brand_pass(custom_app_brand_pass);
        wxCouponBaseInfo.setPromotion_url_name(promotion_url_name);
        wxCouponBaseInfo.setPromotion_url(promotion_url);
        wxCouponBaseInfo.setPromotion_url_sub_title(promotion_url_sub_title);
        wxCouponBaseInfo.setPromotion_app_brand_user_name(promotion_app_brand_user_name);
        wxCouponBaseInfo.setPromotion_app_brand_pass(promotion_app_brand_pass);
        wxCouponBaseInfo.setGet_limit(get_limit);
        wxCouponBaseInfo.setUse_limit(use_limit);
        wxCouponBaseInfo.setCan_share(can_share);
        wxCouponBaseInfo.setCan_give_friend(can_give_friend);
        //
        return wxCouponBaseInfo;
    }

    public enum EnumCouponCodeType {
        ECCT_CODE_TYPE_TEXT("CODE_TYPE_TEXT", 0), //
        ECCT_CODE_TYPE_BARCODE("CODE_TYPE_BARCODE", 1), ECCT_CODE_TYPE_QRCODE("CODE_TYPE_QRCODE", 2), ECCT_CODE_TYPE_ONLY_QRCODE("CODE_TYPE_ONLY_QRCODE", 3), ECCT_CODE_TYPE_ONLY_BARCODE("CODE_TYPE_ONLY_BARCODE",
                4), ECCT_CODE_TYPE_NONE("CODE_TYPE_NONE", 5);

        private String name;
        private int index;

        private EnumCouponCodeType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCouponCodeType pt : EnumCouponCodeType.values()) {
                if (pt.getIndex() == index) {
                    return pt.name;
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
