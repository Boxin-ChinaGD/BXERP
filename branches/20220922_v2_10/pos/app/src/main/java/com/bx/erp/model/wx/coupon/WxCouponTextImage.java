package com.bx.erp.model.wx.coupon;

import java.util.ArrayList;
import java.util.List;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseWxModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WxCouponTextImage extends BaseWxModel {

    private static final long serialVersionUID = 1L;

    public static final WxCouponTextImageField field = new WxCouponTextImageField();

    private int advancedInfoID;

    private String image_url;

    private String text;

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getAdvancedInfoID() {
        return advancedInfoID;
    }

    public void setAdvancedInfoID(int advancedInfoID) {
        this.advancedInfoID = advancedInfoID;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "WxCouponTextImage [advancedInfoID=" + advancedInfoID + ", image_url=" + image_url + ", text=" + text + ", ID=" + ID + "]";
    }

    @Override
    protected BaseWxModel doParse1(JSONObject jo) {
        try {
            ID = jo.getInt(getFIELD_NAME_ID());
            advancedInfoID = jo.getInt(field.getFIELD_NAME_advancedInfoID());
            image_url = jo.getString(field.getFIELD_NAME_image_url());
            text = jo.getString(field.getFIELD_NAME_text());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseWxModel> parseN(String s) {
        List<BaseWxModel> wxCouponTextImageList = new ArrayList<BaseWxModel>();
        //
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WxCouponTextImage wxCouponTextImage = new WxCouponTextImage();
                wxCouponTextImage.doParse1(jsonObject);
                wxCouponTextImageList.add(wxCouponTextImage);
            }
            //
            return wxCouponTextImageList;
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
        WxCouponTextImage wxCouponTextImage = (WxCouponTextImage) arg0;
        if ((ignoreIDInComparision == true ? true : (wxCouponTextImage.getID() == ID && printComparator(getFIELD_NAME_ID())))//
                && wxCouponTextImage.getAdvancedInfoID() == advancedInfoID && printComparator(field.getFIELD_NAME_advancedInfoID())//
                && wxCouponTextImage.getImage_url().equals(image_url) && printComparator(field.getFIELD_NAME_image_url())//
                && wxCouponTextImage.getText().equals(text) && printComparator(field.getFIELD_NAME_text())//
                ) {
            return 0;
        }
        //
        return -1;
    }

    @Override
    public BaseWxModel clone() throws CloneNotSupportedException {
        WxCouponTextImage wxCouponTextImage = new WxCouponTextImage();
        //
        wxCouponTextImage.setID(ID);
        wxCouponTextImage.setAdvancedInfoID(advancedInfoID);
        wxCouponTextImage.setImage_url(image_url);
        wxCouponTextImage.setText(text);
        //
        return wxCouponTextImage;
    }
}
