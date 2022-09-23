package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.ConfigGeneral;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.List;

@Component("configGeneralHttpEvent")
@Scope("prototype")
public class ConfigGeneralHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getCurrentType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误：" + lastErrorCode);
//                GlobalController.getInstance().setSessionID(null);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_ConfigGeneral_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                case ERT_ConfigGeneral_Update:
                    handleUpdate(jsonObject);
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
            JSONArray jaConfigGeneral = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> configGeneralList = (List<BaseModel>) new ConfigGeneral().parseN(jaConfigGeneral);

            setListMasterTable(configGeneralList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_ConfigGeneral_RetrieveNC得到的对象列表是：" + configGeneralList.toString());

            sqliteBO.onResultRetrieveNC(configGeneralList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的ConfigGeneral失败，错误消息" + e.getMessage());
        }
    }

    private void handleUpdate(JSONObject jsonObject) {
        try {
            JSONObject joConfigGeneral = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            ConfigGeneral configGeneral = new ConfigGeneral();
            if (configGeneral.parse1(joConfigGeneral.toString()) == null) {
                log.info("Update:Failed to parse ConfigGeneral");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_ConfigGeneral_Update得到的对象是：" + configGeneral.toString());

                setBaseModel1(configGeneral);

                if (sqliteBO != null) {
                    sqliteBO.onResultUpdate(configGeneral);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步修改的ConfigGeneral失败，错误消息" + e.getMessage());
        }
    }
}
