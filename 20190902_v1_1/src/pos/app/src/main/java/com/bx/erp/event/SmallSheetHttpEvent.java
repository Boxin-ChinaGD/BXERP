package com.bx.erp.event;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SmallSheetHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public SmallSheetHttpEvent() {
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
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError && lastErrorCode != ErrorInfo.EnumErrorCode.EC_Duplicated) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_SmallSheet_Create:
                    handleCreate(jsonObject);
                    break;
                case ERT_SmallSheet_Update:
                    handleUpdate(jsonObject);
                    break;
                case ERT_SmallSheet_RetrieveN:
                    handleRetrieveN(jsonObject);
                    break;
                case ERT_SmallSheet_Delete:
                    setSyncType(BasePresenter.SYNC_Type_D);
                    if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                        if (sqliteBO != null) {
                            sqliteBO.onResultDelete(getBaseModel1());
                        }
                    } else {
                        sqliteBO.onResultDelete(null);
                    }
                    break;
                case ERT_FeedBack:
                    if (sqliteBO != null) {
                        sqliteBO.onResultFeedback();
                    }
                    setStatus(EnumEventStatus.EES_Http_Done);
                    break;
                case ERT_SmallSheet_RetrieveNC:
                    handleRetrieveNC(getResponseData(), jsonObject);
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


    private void handleRetrieveNC(String rsp, JSONObject jsonObject) {
        try {
            log.info("RN服务器返回的需要同步的数据:" + rsp.toString());

            JSONArray jaSmallSheet = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            String count = jsonObject.getString(BaseModel.KEY_HTMLTable_Parameter_TotalRecord);
            List<BaseModel> rtList = (List<BaseModel>) new SmallSheetFrame().parseN(jaSmallSheet);
            if (rtList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回SmallSheet数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(rtList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(null);
                }
            } else {
                log.info("ERT_SmallSheet_RetrieveNC得到的对象列表是: " + rtList.toString());

                setListMasterTable(rtList);
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                setCount(count);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveNC(rtList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步smallSheet失败，失败原因：" + e.getMessage());
        }
    }

    private void handleRetrieveN(JSONObject jsonObject) {
        try {
            JSONArray jaSmallSheetFrame = jsonObject.getJSONArray(BaseModel.JSON_OBJECTLIST_KEY);
            List<BaseModel> frameList = new SmallSheetFrame().parseN(jaSmallSheetFrame);
            if (frameList.size() == 0) {
                log.info("RETRIEVE N:服务器没有返回Smallsheet数据！");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);

                setListMasterTable(frameList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(null);
                }
            } else {
                log.info("ERT_SmallSheet_RetrieveN得到的对象列表是: " + frameList.toString());

                int maxInt2 = 0;
                for (int i = 0; i < frameList.size(); i++) {
                    if (frameList.get(i).getSyncSequence() > maxInt2) {
                        maxInt2 = frameList.get(i).getSyncSequence();
                    }
                }

                //生成一个根据int2升序排列的list
                List<BaseModel> orderFrameList = new ArrayList<BaseModel>();
                for (int i = 0; i < maxInt2; i++) {
                    for (int j = 0; j < frameList.size(); j++) {
                        if (i + 1 == frameList.get(j).getSyncSequence()) {
                            orderFrameList.add(frameList.get(j));
                        }
                    }
                }   //... 参考ComparatorBaseModel，实现更加灵活的比较器
                setListMasterTable(orderFrameList);

                if (sqliteBO != null) {
                    sqliteBO.onResultRetrieveN(orderFrameList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步smallSheet失败，失败原因：" + e.getMessage());
        }
    }

    private void handleUpdate(JSONObject jsonObject) {
        try {
            JSONObject joSmallSheetFrame = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            SmallSheetFrame frame = new SmallSheetFrame();
            setSyncType(BasePresenter.SYNC_Type_U);
            if (frame.parse1(joSmallSheetFrame.toString()) == null) {
                log.info("UPDATE:Failed to parse smallsheet!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_SmallSheet_Update得到的对象是：" + frame.toString());

                setBaseModel1(frame);

                if (sqliteBO != null) {
                    sqliteBO.onResultUpdate(frame);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步smallSheet失败，失败原因：" + e.getMessage());
        }
    }

    private void handleCreate(JSONObject jsonObject) {
        try {
            JSONObject joSmallSheetFrame = jsonObject.getJSONObject(BaseModel.JSON_OBJECT_KEY);
            SmallSheetFrame frame = new SmallSheetFrame();
            setSyncType(BasePresenter.SYNC_Type_C);
            if (frame.parse1(joSmallSheetFrame.toString()) == null) {
                log.info("CREATE:Failed to parse smallsheet!");
                setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            } else {
                log.info("ERT_SmallSheet_Create得到的对象是：" + frame.toString());
                setBaseModel1(frame);

                if (sqliteBO != null) {
                    sqliteBO.onResultCreate(frame);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
            log.info("同步smallSheet失败，失败原因：" + e.getMessage());
        }
    }
}
