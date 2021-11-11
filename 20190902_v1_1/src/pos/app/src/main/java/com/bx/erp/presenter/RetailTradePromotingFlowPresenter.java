package com.bx.erp.presenter;

import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions;

public class RetailTradePromotingFlowPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());

    @Inject
    public RetailTradePromotingFlowPresenter(DaoSession dao) {
        super(dao);
    }

    @Override
    protected String getTableName() {
        return dao.getRetailTradePromotingFlowDao().getTablename();
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的retrieve1Sync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.clear();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return dao.getRetailTradePromotingFlowDao().load(bm.getID());
        } catch (Exception e) {
            log.info("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }
    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的retrieveNSync，bm=" +(bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_RetailTradePromotingFlow_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    List<?> list = dao.getRetailTradePromotingFlowDao().queryRaw(bm.getSql(), bm.getConditions());
                    return list;
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return dao.getRetailTradePromotingFlowDao().loadAll();
                } catch (Exception e) {
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    log.info("错误为" + lastErrorCode);
                    return null;
                }
        }
    }
    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的createSync，bm=" +(bm == null ? null : bm.toString()));

        try {
//            RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) bm;
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            if (((RetailTradePromotingFlow)bm).getCreateDatetime() == null) {
                ((RetailTradePromotingFlow)bm).setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            }
            long id = dao.getRetailTradePromotingFlowDao().insert(((RetailTradePromotingFlow)bm));
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return bm;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的deleteNSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getRetailTradePromotingFlowDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的deleteSync，bm=" +(bm == null ? null : bm.toString()));

        try {
            dao.getRetailTradePromotingFlowDao().delete((RetailTradePromotingFlow) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }
}
