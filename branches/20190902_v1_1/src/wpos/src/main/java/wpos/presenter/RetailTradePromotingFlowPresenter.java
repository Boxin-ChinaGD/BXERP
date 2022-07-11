package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.bo.NtpHttpBO;
import wpos.dao.RetailTradePromotingFlowMapper;
import wpos.helper.Constants;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradePromotingFlow;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

import static wpos.bo.BaseSQLiteBO.CASE_RetailTradePromotingFlow_RetrieveNByConditions;

@Component("retailTradePromotingFlowPresenter")
public class RetailTradePromotingFlowPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

//    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static final String QUERY_RetailTradePromotingFlow_TABLE = "SELECT F_ID," +
            "F_RetailTradePromotingID," +
            "F_PromotionID," +
            "F_ProcessFlow," +
            "F_CreateDatetime," +
            "F_SyncDatetime," +
            "F_SyncType " +
            "FROM T_RetailTradePromotingFlow ";

    @Resource
    private RetailTradePromotingFlowMapper retailTradePromotingFlowMapper;

//    public RetailTradePromotingFlowPresenter(RetailTradePromotingFlowMapper retailTradePromotingFlowMapper) {
//        super(retailTradePromotingFlowMapper);
//    }

    @Override
    public void createTableSync() {
        retailTradePromotingFlowMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (RetailTradePromotingFlow.class.isAnnotationPresent(Table.class)) {
            Table annotation = RetailTradePromotingFlow.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + RetailTradePromotingFlow.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_RetailTradePromotingFlow_TABLE;
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

            return retailTradePromotingFlowMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_RetailTradePromotingFlow_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
                    return dataQuery.getResultList();
                } catch (Exception e) {
                    log.info("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

                    return null;
                }
            default:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

//                    return retailTradePromotingFlowMapper.retrieveN(bm);
                    return null;
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
        log.info("正在进行RetailTradePromotingFlowPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();

        try {
//            RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) bm;
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            if (bm.getSyncDatetime() == null) {
                bm.setSyncDatetime(Constants.getDefaultSyncDatetime2());
            }
            if (((RetailTradePromotingFlow) bm).getCreateDatetime() == null) {
                ((RetailTradePromotingFlow) bm).setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            }
            retailTradePromotingFlowMapper.create((RetailTradePromotingFlow) bm);
            if (bm.getID() != null) {
                bm.setSql("WHERE F_ID = %s");
                bm.setConditions(new String[]{String.valueOf(bm.getID())});
            } else {
                bm.setSql("WHERE F_ID = (select MAX(F_ID) from T_RetailTradePromotingFlow)");
            }
            String sql = String.format(bm.getSql(), bm.getConditions());
            Query dataQuery = entityManager.createNativeQuery(QUERY_RetailTradePromotingFlow_TABLE + sql, RetailTradePromotingFlow.class);
            List<?> rtcList = dataQuery.getResultList();

            globalWriteLock.writeLock().unlock();

            if (rtcList != null && rtcList.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                return (BaseModel) rtcList.get(0);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            retailTradePromotingFlowMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteNSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行RetailTradePromotingFlowPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            retailTradePromotingFlowMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }
}
