package com.bx.erp.presenter;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;

import org.apache.log4j.Logger;

import javax.inject.Inject;

public class CouponScopePresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public CouponScopePresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponScopePresenter的deleteNSync，bm=" + bm);
        try {
            dao.getCouponScopeDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除所有的优惠券作用范围表失败，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }
}
