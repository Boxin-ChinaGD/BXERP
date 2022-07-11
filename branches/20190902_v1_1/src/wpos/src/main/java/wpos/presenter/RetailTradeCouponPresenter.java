package wpos.presenter;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import wpos.dao.RetailTradeCouponMapper;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeCoupon;

import javax.annotation.Resource;
import javax.persistence.Table;


@Component("retailTradeCouponPresenter")
public class RetailTradeCouponPresenter extends BasePresenter {
    private static Logger log = Logger.getLogger(RetailTradeCouponPresenter.class);

    public static final String QUERY_RetailTradeCoupon_TABLE = "SELECT F_ID," +
            "F_RetailTradeID, " +
            "F_CouponCodeID, " +
            "F_SyncDatetime " +
            "FROM T_RetailTradeCoupon ";

    @Resource
    public RetailTradeCouponMapper retailTradeCouponMapper;

    @Override
    protected String getTableName() {
        if (RetailTradeCoupon.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTradeCoupon.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTrade.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTradeCoupon_TABLE;
    }

    @Override
    public void createTableSync() {
        retailTradeCouponMapper.createTable();
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        globalWriteLock.writeLock().lock();
        log.info("正在进行RetailTradeCouponPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        try {
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
//            long id = dao.getRetailTradeCouponDao().insert((RetailTradeCoupon) bm);
//            bm.setID(id);
            retailTradeCouponMapper.save((RetailTradeCoupon) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行createSync失败，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return bm;
    }
}
