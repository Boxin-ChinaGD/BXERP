package wpos.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.Barcodes;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component("barcodesHttpEvent")
@Scope("prototype")
public class BarcodesHttpEvent extends BaseHttpEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void onEvent() {
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
                case ERT_Barcodes_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_Barcodes_Create:
//                    handleCreate(jsonObject);
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    break;
                case ERT_Barcodes_RetrieveNC:
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


    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaStaff = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> barcodesList = new Barcodes().parseN(jaStaff);
            if (barcodesList.size() == 0) {
                log.info("RetrieveN: 服务器没有返回Barcodes数据!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(barcodesList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_Barcodes_RetrieveN得到的对象列表是: " + barcodesList.toString());

                int maxSyncSequence = 0;
                for (int i = 0; i < barcodesList.size(); i++) {
                    if (barcodesList.get(i).getSyncSequence() > maxSyncSequence) {
                        maxSyncSequence = barcodesList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据SyncSequence升序排列的List
                List<BaseModel> orderBarcodesList = new ArrayList<BaseModel>();
                for (int i = 0; i <= maxSyncSequence; i++) {
                    for (int j = 0; j < barcodesList.size(); j++) {
                        if (i == barcodesList.get(j).getSyncSequence()) {
                            orderBarcodesList.add(barcodesList.get(j));
                        }
                    }
                }
                setListMasterTable(orderBarcodesList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderBarcodesList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步barcodes出错,错误信息：" + e.getMessage());
        }
    }

    private void handleRetrieveNC(JSONObject jsonObject) {
        try {
            JSONArray jaBarcodes = jsonObject.getJSONArray("barcodesList");
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            List<BaseModel> barcodesList = new Barcodes().parseN(jaBarcodes);
            if (barcodesList.size() == 0) {
                log.info("RetrieveNC:服务器中没有数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);

                setListMasterTable(barcodesList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("retrieveNC得到的对象列表是：" + barcodesList.toString());

                setListMasterTable(barcodesList);
                setCount(count);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(barcodesList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("下载所有的barcodes出错,错误信息：" + e.getMessage());
        }
    }

//    private void handleCreate(JSONObject jsonObject) {
//        try {
//            JSONObject joBarcodes = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
//            Barcodes b = new Barcodes();
//            if (b.parse1(joBarcodes.toString()) == null) {
//                System.out.println("CREATE:Failed to parse Barcodes!");
//                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//            } else {
//                System.out.println("ERT_Barcodes_Create得到的对象是：" + b.toString());
//
//                setBaseModel1(b);
//            }
//            if (sqliteBO != null) {
//                sqliteBO.onResultCreate(b);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
//        }
//    }
}
