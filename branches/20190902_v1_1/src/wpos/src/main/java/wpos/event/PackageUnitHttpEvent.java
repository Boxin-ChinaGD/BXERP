package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.PackageUnit;
import wpos.utils.StringUtils;

import java.util.List;

@Component("packageUnitHttpEvent")
@Scope("prototype")
public class PackageUnitHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("packsgeUnit网络请求出错：" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_PackageUnit_RetrieveNC:
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
            JSONArray jsonArray = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> packageUnitList = new PackageUnit().parseN(jsonArray);

            setListMasterTable(packageUnitList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_PackageUnit_RetrieveNC得到的对象列表为：" + packageUnitList.toString());

            sqliteBO.onResultRetrieveNC(packageUnitList);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("下载所有的packageUnit失败，失败原因：" + e.getMessage());
        }
    }
}
