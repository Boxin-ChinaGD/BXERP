package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.BaseModel;
import wpos.model.Brand;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component("brandHttpEvent")
@Scope("prototype")
public class BrandHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
//        ConfigureLog4J configureLog4J = new ConfigureLog4J();
//        configureLog4J.configure();
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

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
                case ERT_Brand_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_Brand_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    break;
                case ERT_Brand_Update:
                    handleUpdate(jsonObject);
                    break;
                case ERT_Brand_RetrieveNC:
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

    private void handleUpdate(JSONObject jsonObject) {
        try {
            JSONObject jobrand = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Brand brand = new Brand();
            if (brand.parse1(jobrand.toString()) == null) {
                log.info("UPDATE:Failed to parse brand!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_brand_Update得到的对象是：" + brand.toString());

                setBaseModel1(brand);

                if (sqliteBO != null) {
                    sqliteBO.onResultUpdate(brand);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("模拟网页修改Brand同步出错,错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> brandList = new Brand().parseN(jaStaff);
            if (brandList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回Brand数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(brandList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Brand_RetrieveN得到的对象列表是: " + brandList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < brandList.size(); i++) {
                    if (brandList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = brandList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderBrandList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < brandList.size(); j++) {
                        if (i == brandList.get(j).getSyncSequence()) {
                            orderBrandList.add(brandList.get(j));
                        }
                    }
                }
                setListMasterTable(orderBrandList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderBrandList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步Brand出错,错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray("brandList");
            List<BaseModel> brandList = new Brand().parseN(jaStaff);

            setListMasterTable(brandList);
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

            log.info("ERT_Brand_RetrieveN得到的对象列表是: " + brandList.toString());

            sqliteBO.onResultRetrieveNC(brandList);
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析BrandList出错,错误信息：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joBrand = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            Brand b = new Brand();
            if (b.parse1(joBrand.toString()) == null) {
                log.info("CREATE:Failed to parse Brand!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_Brand_Create得到的对象是：" + b.toString());

                setBaseModel1(b);
            }
            if (sqliteBO != null) {
                sqliteBO.onResultCreate(b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("解析Brand出错,错误信息：" + e.getMessage());
        }
    }
}
