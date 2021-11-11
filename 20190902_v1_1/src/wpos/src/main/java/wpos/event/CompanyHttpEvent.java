package wpos.event;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.Company;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Component("companyHttpEvent")
@Scope("prototype")
public class CompanyHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

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
//                    case ERT_Company_Create:
//                        log.info("请求服务创建Company成功！");
//                        handleCreate(jsonObject);
//                        break;
                    case ERT_Company_Delete:
                        log.info("请求服务器删除Company成功！");
                        break;
                    case ERT_Company_RetrieveN:
                        handleRetrieveN(jsonObject);
                        break;
                    case ERT_Company_UploadBusinessLicensePicture:
                        log.info(jsonObject);
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

//    private void handleCreate(JSONObject jsonObject) {
//        // 由于Pos机无法创建Company，故该方法无效
//        try {
//            JSONObject joCompany = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//            Company company = new Company();
//            if (company.parse1(joCompany.toString()) == null) {
//                log.info("CREATE:Failed to parse company!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                log.info("ERT_company_Create得到的对象是：" + company.toString());
//                setBaseModel1(company);
//            }
////            setString1(String.valueOf(jsonObject.getInt(Shop.SHOPID)));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            log.info("解析company失败，错误消息：" + e.getMessage());
//        }
//    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaCompany = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> companyList = new ArrayList<BaseModel>();
            companyList = (List<BaseModel>) new Company().parseN(jaCompany);
            if (companyList.size() == 0) {
                log.info("RETRIEVEN:Failed to parse company list!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Company_RetrieveN得到的对象是：" + companyList.toString());

                setListMasterTable(companyList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                if (sqliteBO != null) {
                    sqliteBO.onResultCreate(companyList.get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步company失败，错误消息：" + e.getMessage());
        }
    }
}
