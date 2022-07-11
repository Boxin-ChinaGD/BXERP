package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.http.HttpRequestUnit;
import wpos.model.BaseModel;
import wpos.model.Company;
import wpos.model.ErrorInfo;
import wpos.model.Pos;
import wpos.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Component("posHttpEvent")
@Scope("prototype")
public class PosHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("POS网络请求错误：" + lastErrorCode);
//                    GlobalController.getInstance().setSessionID(null);  //...
                    break;
            }
            log.info(getResponseData());
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_POS_Delete:
                    break;
                case ERT_PosGetToken:
//                    GlobalController.getInstance().setSessionID(getTempSessionID());
                    break;
                case ERT_Pos_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_Pos_CreateSync:
                    log.info("请求服务器创建POS已完成");
                    try {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
                        handleCreate(jsonObject1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        log.info("解析失败，失败原因：" + e.getMessage());
                        break;
                    }
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    setRequestType(HttpRequestUnit.EnumRequestType.ERT_FeedBack);
                    break;
                case ERT_Pos_RetrieveNC:
                    handleRetrieveNC(jsonObject);
                    break;
                case ERT_POS_Retrieve1BySN:
                    try {
                        if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                            log.error("查询Pos机失败," + jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_msg));
                            setData(jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_msg)); // 服务器返回的错误信息
                            break;
                        } else if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                            handleRetrieve1BySN(jsonObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (jsonObject == null) {
                            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoSuchData);
                        } else {
                            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        log.info("解析失败，失败原因：" + e.getMessage());
                        break;
                    }
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
            Pos pos = new Pos();
            pos = (Pos) pos.parse1C(jsonObject.toString());
            log.info("Craeate: 服务器返回的POS数据是：" + pos);

            setBaseModel1(pos);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieve1BySN(JSONObject jsonObject) {
        try {
            JSONObject joPos = jsonObject.getJSONObject(Pos.TAG);
            Pos pos = new Pos();
            pos = (Pos) pos.parse1C2(joPos.toString());
            //
            log.info("Retrieve1BySN: 服务器返回的POS数据是：" + pos);
            setBaseModel1(pos);

            JSONObject joCompany = jsonObject.getJSONObject(Company.TAG);
            Company company = new Company();
            company = (Company) company.parse1(joCompany.toString());
            //
            log.info("Retrieve1BySN: 服务器返回的Company数据是：" + company);
            setBaseModel2(company);

            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
//            if (sqliteBO != null) {
//                sqliteBO.onResultCreate(pos);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaPos = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> posList = new Pos().parseN(jaPos);

            setListMasterTable(posList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_Brand_RetrieveN得到的对象列表是: " + posList.toString());

            sqliteBO.onResultRetrieveNC(posList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的pos失败,失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaPos = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> posList = new Pos().parseN(jaPos);
            if (posList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回Pos数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(posList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Brand_RetrieveN得到的对象列表是: " + posList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < posList.size(); i++) {
                    if (posList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = posList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderPosList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < posList.size(); j++) {
                        if (i == posList.get(j).getSyncSequence()) {
                            orderPosList.add(posList.get(j));
                        }
                    }
                }
                setListMasterTable(orderPosList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderPosList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步所有的POS失败，失败原因：" + e.getMessage());
        }
    }

//    private void handleCreate(JSONObject jsonObject) {
//        try {
//            JSONObject joPos = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//            Pos pos = new Pos();
//            if (pos.parse1(joPos.toString()) == null) {
//                log.info("CREATE:Failed to parse Pos!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                log.info("ERT_Pos_Create得到的对象是：" + pos.toString());
//
//                setBaseModel1(pos);
//            }
//            if (sqliteBO != null) {
//                sqliteBO.onResultCreate(pos);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
//    }
}
