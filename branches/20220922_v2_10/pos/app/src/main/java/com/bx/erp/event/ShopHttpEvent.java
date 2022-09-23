package com.bx.erp.event;


import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopHttpEvent extends BaseHttpEvent {
    private static final String JSON_KEY_SHOP = "shop";
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                switch (getRequestType()) {
                    case ERT_Shop_Create:
                        log.info("请求服务创建Shop成功！");
                        handleCreate(jsonObject);
                        break;
                    case ERT_Shop_Delete:
                        log.info("请求服务器删除Shop成功！");
                        break;
                    default:
                        log.info("Not yet implemented!");
                        throw new RuntimeException("Not yet implemented!");
                }
            }
        } while (false);

        //让调用者不要再等待
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joRetailTrade = jsonObject.getJSONObject(JSON_KEY_SHOP);
            Shop shop = new Shop();
            if (shop.parse1(joRetailTrade.toString()) == null) {
                log.info("CREATE:Failed to parse shop!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_shop_Create得到的对象是：" + shop.toString());
                setBaseModel1(shop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步shop失败，失败原因：" + e.getMessage());
        }
    }

}
