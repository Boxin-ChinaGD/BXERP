package wpos.event;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.ReturnCommoditySheet;
import wpos.utils.StringUtils;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component("returnCommoditySheetHttpEvent")
@Scope("prototype")
public class ReturnCommoditySheetHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public ReturnCommoditySheetHttpEvent() {

    }

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
                case ERT_CreateReturnCommoditySheet:
                    handleCreate(jsonObject);
                    break;
                case ERT_ApproveReturnCommoditySheet:
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

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joSmallSheetFrame = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            ReturnCommoditySheet rcs = new ReturnCommoditySheet();
            if (rcs.parse1(joSmallSheetFrame.toString()) == null) {
                log.info("CREATE:Failed to parse smallsheet!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_ReturnCommoditySheet_Create得到的对象是：" + rcs.toString());
                setBaseModel1(rcs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步失败，失败原因：" + e.getMessage());
        }
    }
}
