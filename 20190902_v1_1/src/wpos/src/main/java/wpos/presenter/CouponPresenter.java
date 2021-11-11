package wpos.presenter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.dao.CouponMapper;
import wpos.dao.CouponScopeMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

@Component("couponPresenter")
public class CouponPresenter extends BasePresenter {
    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private CouponMapper couponMapper;

    @Resource
    private CouponScopeMapper couponScopeMapper;

    public static final String QUERY_Coupon_TABLE = "SELECT F_ID,F_Status,F_Type,F_Bonus,F_LeastAmount,F_ReduceAmount,F_Discount,F_Title,F_Color,F_Description," +
            "F_PersonalLimit,F_WeekDayAvailable,F_BeginTime,F_EndTime,F_BeginDateTime,F_EndDateTime,F_Quantity,F_RemainingQuantity,F_Scope FROM T_Coupon ";

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void createTableSync() {
        couponMapper.createTable();
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行CouponPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (iUseCaseID) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        log.info("已经得到服务器返回的需要同步的Coupon数据, 准备进行同步...");
                        globalWriteLock.writeLock().lock();
                        {
                            try {
                                //删除本地所有的数据
//                        couponMapper.deleteAllInBatch();
                                couponScopeMapper.deleteAllInBatch();
                                deleteNSync(iUseCaseID, null);
                                if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                    if (bmNewList != null) {
                                        for (int i = 0; i < bmNewList.size(); i++) {
                                            couponMapper.create((Coupon) bmNewList.get(i));
                                            for (int j = 0; j < ((Coupon) bmNewList.get(i)).getListSlave1().size(); j++) {
                                                couponScopeMapper.create((CouponScope) ((Coupon) bmNewList.get(i)).getListSlave1().get(j));
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
                        globalWriteLock.writeLock().unlock();

                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponPresenter的deleteNSync，bm=" + bm);
        globalWriteLock.writeLock().lock();
        try {
//            dao.getCouponDao().deleteAll();
            couponMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("删除所有的优惠券失败，错误信息：" + e.getMessage());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CouponPresenter的retrieve1Sync，bm=" + bm);

        try {
//            BaseModel coupon = dao.getCouponDao().load(bm.getID());
            BaseModel coupon = couponMapper.findOne(bm.getID());
            if (coupon != null) {
                coupon.setSql("where F_CouponID = '%s'");
                coupon.setConditions(new String[]{String.valueOf(coupon.getID())});
                String sql = String.format(coupon.getSql(), coupon.getConditions());
                Query query = entityManager.createNativeQuery(CouponScopePresenter.QUERY_CouponScope_TABLE + sql, CouponScope.class);
                List<CouponScope> couponScopes = query.getResultList();
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
        globalWriteLock.writeLock().lock();
        Coupon coupon = null;
        try {
            couponMapper.create((Coupon) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Coupon)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Coupon_TABLE + sql, Coupon.class);
            List<Coupon> coupons = query.getResultList();
            if (coupons != null && coupons.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                coupon = coupons.get(0);

            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync失败，错误信息=" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return coupon;
    }

    @Override
    protected String getTableName() {
        if (Coupon.class.isAnnotationPresent(Table.class)) {
            Table annotation = Coupon.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Coupon.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Coupon_TABLE;
    }
}
