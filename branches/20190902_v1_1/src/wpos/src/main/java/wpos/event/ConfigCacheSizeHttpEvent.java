package wpos.event;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.ConfigCacheSize;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.List;

@Component("configCacheSizeHttpEvent")
@Scope("prototype")
public class ConfigCacheSizeHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误，" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_ConfigCacheSize_RetrieveN:
                    handleRetrieveN(getResponseData(), jsonObject);
                    break;
                case ERT_ConfigCacheSize_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                default:
                    log.info("Not yet implemented!");
                    throw new RuntimeException("Not yet implemented!");
            }
        } while (false);

        //让调用者不要再等待
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleRetrieveN(String rsp, JSONObject jsonObject) {
        try {
            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());

            JSONArray jaConfig = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> ccsList = (List<BaseModel>) new ConfigCacheSize().parseN(jaConfig);
            if (ccsList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回RetailTrade数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(ccsList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("对象列表是: " + ccsList.toString());
                setListMasterTable(ccsList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(ccsList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步configCacheSize失败，错误消息：" + e.getMessage());
        }
    }

    public void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaConfigCacheSize = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> configCacheSizeList = new ConfigCacheSize().parseN(jaConfigCacheSize);

            setListMasterTable(configCacheSizeList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_ConfigCacheSize_RetrieveNC得到的对象的列表是：" + configCacheSizeList.toString());

            sqliteBO.onResultRetrieveNC(configCacheSizeList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的configCacheSize失败，错误消息：" + e.getMessage());
        }
    }
}
