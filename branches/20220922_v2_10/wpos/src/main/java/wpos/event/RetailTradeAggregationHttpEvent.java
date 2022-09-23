
package wpos.event;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeAggregation;
import wpos.presenter.BasePresenter;
import wpos.utils.StringUtils;

@Component("retailTradeAggregationHttpEvent")
@Scope("prototype")
public class RetailTradeAggregationHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    public RetailTradeAggregationHttpEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("网络请求错误," + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError && lastErrorCode != ErrorInfo.EnumErrorCode.EC_Duplicated) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_RetailTradeAggregation_Create:
                    handleCreate(jsonObject);
                    break;
                default:
                    throw new RuntimeException("未处理的类型！");
            }
        } while (false); // 优雅避免if多嵌套

        setStatus(EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joRTA = jsonObject.getJSONObject("rta");
            RetailTradeAggregation rta = new RetailTradeAggregation();
            setSyncType(BasePresenter.SYNC_Type_C);
            if (rta.parse1(joRTA.toString()) == null) {
                log.info("CREATE:Failed to parse RetailTradeAggregation!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_RetailTradeAggregation_Create得到的对象是：" + rta);
                setBaseModel2(rta); //此时旧的是BaseModel1，服务器返回的是BaseModel2

                if (sqliteBO != null) {
                    sqliteBO.onResultCreate(rta);//对于收银汇总来说，不需要将服务器返回的对象插入SQLite，只需要将旧的删除
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步失败，失败原因：" + e.getMessage());
        }
    }
}
