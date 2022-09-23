package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BXConfigGeneral;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.List;

/**
 * Created by BOXIN on 2019/7/12.
 */

@Component("bXConfigGeneralHttpEvent")
@Scope("prototype")
public class BXConfigGeneralHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getCurrentType()", getRequestType());

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
                case ERT_BXConfigGeneral_RetrieveNC:
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
            JSONArray jaBXConfigGeneral = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> bxConfigGeneralList = (List<BaseModel>) new BXConfigGeneral().parseN(jaBXConfigGeneral);

            setListMasterTable(bxConfigGeneralList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_BXConfigGeneral_RetrieveNC得到的对象列表是：" + bxConfigGeneralList.toString());

            sqliteBO.onResultRetrieveNC(bxConfigGeneralList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的BX ConfigGeneral失败，错误消息" + e.getMessage());
        }
    }
}
