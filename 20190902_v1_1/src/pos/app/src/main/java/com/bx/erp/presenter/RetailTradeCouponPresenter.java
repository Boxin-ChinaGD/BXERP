package com.bx.erp.presenter;

import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeCoupon;

import org.apache.log4j.Logger;

import javax.inject.Inject;

public class RetailTradeCouponPresenter extends BasePresenter {
    private static Logger log = Logger.getLogger(RetailTradeCouponPresenter.class);

    @Inject
    public RetailTradeCouponPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getRetailTradeCouponDao().getTablename();
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradeCouponPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            long id = dao.getRetailTradeCouponDao().insert((RetailTradeCoupon) bm);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }
}
