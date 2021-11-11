package wpos.event;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.CouponCode;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.List;

@Component("couponCodeHttpEvent")
@Scope("prototype")
public class CouponCodeHttpEvent extends BaseHttpEvent{
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误：" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_CouponCode_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                default:
                    log.info("Not yet implemented!");
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);

        //让调用者不要再等待
        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaCouponCode = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            List<BaseModel> couponCodeList = (List<BaseModel>) new CouponCode().parseN(jaCouponCode);
            setCount(count);
            if (couponCodeList.size() == 0) {
                log.info("RetrieveNC:服务器中没有数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                setCount(null);
            }
            log.info("retrieveNC得到的对象列表是：" + couponCodeList.toString());

            setListMasterTable(couponCodeList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("查询CouponCode出错,错误信息：" + e.getMessage());
        }
    }
}
