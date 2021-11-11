package com.bx.erp.presenter;

import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.DaoSession;
import com.bx.erp.model.ErrorInfo;
import com.silencedut.taskscheduler.TaskScheduler;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

public class CouponPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());
    ReentrantLock lock = new ReentrantLock();

    @Inject
    public CouponPresenter(final DaoSession dao) {
        super(dao);
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CouponPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                TaskScheduler.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Coupon数据, 准备进行同步...");

                        log.debug("++++++++++++++++++++++++++ 开始清空 coupon 和 couponscope 表, 线程名称" + Thread.currentThread().getName());
                        lock.lock();
                        {
                            try {
                                //删除本地所有的数据
                                GlobalController.getInstance().getCouponScopePresenter().deleteNSync(iUseCaseID, null);
                                deleteNSync(iUseCaseID, null);
                                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                    if (bmNewList != null) {
                                        for (int i = 0; i < bmNewList.size(); i++) {
                                            Coupon cou = (Coupon) bmNewList.get(i);
                                            dao.getCouponDao().insert(cou);
                                            log.debug("Coupon=" + cou);
                                            for (int j = 0; j < ((Coupon) bmNewList.get(i)).getListSlave1().size(); j++) {
                                                CouponScope cs = (CouponScope) ((Coupon) bmNewList.get(i)).getListSlave1().get(j);
                                                log.debug("CouponScope=" + dao.getCouponScopeDao().insert(cs));
                                            }
                                        }
                                    } else {
                                        log.info("服务器没有Coupon返回");
                                    }
                                } else {
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                }

                                event.setListMasterTable(bmNewList);
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                            } catch (Exception e) {
                                log.debug("异常：" + e.getMessage());
                            }
                        }
                        lock.unlock();

                        EventBus.getDefault().post(event);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponPresenter的deleteNSync，bm=" + bm);
        try {
            dao.getCouponDao().deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除所有的优惠券失败，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponPresenter的retrieve1Sync，bm=" + bm);

        try {
            BaseModel coupon = dao.getCouponDao().load(bm.getID());
            if (coupon != null) {
                coupon.setSql("where F_CouponID = ?");
                coupon.setConditions(new String[]{String.valueOf(coupon.getID())});
                List<CouponScope> couponScopes = dao.getCouponScopeDao().queryRaw(coupon.getSql(), coupon.getConditions());
                coupon.setListSlave1(couponScopes);
            }
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            return coupon;
        } catch (Exception e) {
            log.error("查找优惠券失败，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponPresenter的createSync，bm=" + bm);

        try {
            long id = dao.getCouponDao().insert((Coupon) bm);
            bm.setID(id);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息=" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return bm;
    }


    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            dao.getCouponDao().deleteByKey(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }

        return null;
    }
}
