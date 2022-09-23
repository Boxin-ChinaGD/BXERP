package com.bx.erp.bo;

import android.content.Context;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeCoupon;

import org.apache.log4j.Logger;

import java.util.List;

public class RetailTradeCouponSQLiteBO extends BaseSQLiteBO{
    private Logger log = Logger.getLogger(this.getClass());

    public RetailTradeCouponSQLiteBO(Context ctx, BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        context = ctx;
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行RetailTradeCouponSQLiteBO的createSync，bm=" + bm);

        if(bm != null){
            RetailTradeCoupon rtc = (RetailTradeCoupon) bm;
            String checkMsg = rtc.checkCreate(iUseCaseID);
            if (!"".equals(checkMsg)) {
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
                return null;
            }

            switch (iUseCaseID) {
                case CASE_RetailTradeCoupon_CreateSync:
                    RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) GlobalController.getInstance().getRetailTradeCouponPresenter().createObjectSync(iUseCaseID, bm);
                    if (retailTradeCoupon != null) {
                        return retailTradeCoupon;
                    } else {
                        log.info("创建临时零售单优惠券使用表失败");
                    }
                    break;
                default:
                    log.info("未定义的事件！");
                    throw new RuntimeException("未定义的事件！");
            }
        }

        return null;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }
}
