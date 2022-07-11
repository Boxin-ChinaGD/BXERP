package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.model.Brand;
import wpos.utils.EventBus;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.dao.StaffMapper;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.Staff;
import wpos.utils.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;

import static wpos.bo.BaseSQLiteBO.CASE_Staff_RetrieveNByConditions;

@Component("staffPresenter")
public class StaffPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private StaffMapper staffMapper;

    public static final String QUERY_Staff_TABLE = "SELECT F_ID," +
            "F_Name," +
            "F_Phone," +
            "F_ICID," +
            "F_WeChat," +
            "F_OpenID," +
            "F_PwdEncrypted," +
            "F_Salt," +
            "F_PasswordExpireDate," +
            "F_IsFirstTimeLogin," +
            "F_ShopID," +
            "F_DepartmentID," +
            "F_Status," +
            "F_SyncDatetime," +
            "F_SyncType," +
            "F_RoleID," +
            "F_PasswordInPOS " +
            "FROM T_Staff ";

//    public StaffPresenter(final StaffMapper staffMapper) {
//        super(staffMapper);
//    }

    @Override
    protected String getTableName() {
        if (Staff.class.isAnnotationPresent(Table.class)) {
            Table annotation = Staff.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Staff.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }


    @Override
    public void createTableSync() {
        staffMapper.createTable();
    }

    @Override
    protected BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_C);
            staffMapper.create((Staff) bm);
            String sql = null;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Staff)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Staff_TABLE + sql, Staff.class);
            List<Staff> staffList = query.getResultList();
            if (staffList != null && staffList.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                globalWriteLock.writeLock().unlock();
                bm.setID(staffList.get(0).getID());
                return staffList.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean updateSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的updateSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            bm.setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            bm.setSyncType(BasePresenter.SYNC_Type_U);
//            staffMapper.update((Staff) bm);
//            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            staffMapper.save((Staff) bm);
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return true;
        } catch (Exception e) {
            log.error("执行updateSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            globalWriteLock.writeLock().unlock();
            return false;
        }
    }

    @Override
    protected BaseModel retrieve1Sync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的retrieve1Sync，bm=" + (bm == null ? null : bm.toString()));

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

//            return staffMapper.retrieve1(bm.getID());
            return staffMapper.findOne(bm.getID());
        } catch (Exception e) {
            log.error("执行retrieve1Sync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;

            return null;
        }
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的retrieveNSync，bm=" + (bm == null ? null : bm.toString()));

        switch (iUseCaseID) {
            case CASE_Staff_RetrieveNByConditions:
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

//                    return staffMapper.queryRaw(bm.getSql(), bm.getConditions());
                    String sql = String.format(bm.getSql(), bm.getConditions());
                    Query dataQuery = entityManager.createNativeQuery(QUERY_Staff_TABLE + sql, Staff.class);
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

//                    return staffMapper.retrieveN(bm);
                    return staffMapper.findAll();
                } catch (Exception e) {
                    log.error("执行retrieveNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;

                    return null;
                }
        }
    }

    @Override
    protected BaseModel deleteSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的deleteSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            staffMapper.delete(bm.getID());
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected boolean refreshByServerDataAsyncC(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行StaffPresenter的refreshByServerDataAsyncC，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (event.getEventTypeSQLite()) {
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Staff数据, 准备进行同步...");

                        if (bmNewList != null || bmNewList.size() > 0) {
                            //删除本地所有的数据
                            deleteNSync(iUseCaseID, null);
                            if (getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                                //同步所有服务器返回的数据
                                createNSync(iUseCaseID, bmNewList);
                                if (getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                                } else {

                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            }
                        }

                        event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected boolean refreshByServerDataAsync(final int iUseCaseID, final List<?> bmNewList, final BaseSQLiteEvent event) {
        log.info("正在进行StaffPresenter的refreshByServerDataAsync，bmNewList=" + (bmNewList == null ? null : bmNewList.toString()));

        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        switch (event.getEventTypeSQLite()) {
            case ESET_Staff_RetrieveResigned:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        try {
                            log.info("已经得到服务器返回的已离职的Staff数据, 准备进行删除...");

                            String resignedStaffIDs = (String) event.getData();
                            Integer[] iaStaffID = StringUtils.toIntArray(resignedStaffIDs);
                            if (iaStaffID != null) {
                                for (int i = 0; i < iaStaffID.length; i++) {
                                    Staff staff = new Staff();
                                    if (staffMapper.findOne(iaStaffID[i]) != null) {
                                        staffMapper.delete(iaStaffID[i]);
                                    }
                                }
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("执行refreshByServerDataAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        }
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
            default:
                new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        log.info("已经得到服务器返回的需要同步的Staff数据, 准备进行同步...");
                        List<BaseModel> bmList = (List<BaseModel>) bmNewList;
                        event.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Staff_RefreshByServerDataAsync_Done);
                        try {
                            if (bmList != null) {
                                for (int i = 0; i < bmList.size(); i++) {
                                    String staffType = bmList.get(i).getSyncType();

                                    if (BasePresenter.SYNC_Type_C.equals(staffType)) {
                                        Staff staff = (Staff) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (staff != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
                                        }
                                        //
                                        createSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的C型Staff数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_U.equals(staffType)) {
                                        updateSync(iUseCaseID, bmList.get(i));

                                        log.info("服务器返回的U型Staff数据成功同步到SQLite中!");
                                    } else if (BasePresenter.SYNC_Type_D.equals(staffType)) {
                                        Staff staff = (Staff) retrieve1Sync(iUseCaseID, bmList.get(i));
                                        if (staff != null) {
                                            deleteSync(BaseSQLiteBO.INVALID_CASE_ID, staff);
                                        }

                                        log.info("服务器返回的D型Staff数据成功同步到SQLite中!");
                                    }
                                    event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                    event.setListMasterTable(bmList);
                                }
                            } else {
                                event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                                event.setListMasterTable(bmList);
                            }
                        } catch (Exception e) {
                            log.info("执行refreshByServerDataAsync时出错，错误信息：" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData);
                        //
                        globalWriteLock.writeLock().unlock();
                        EventBus.getDefault().post(event);
                    }
                }.start();
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUserCaseID, BaseModel bm) {
        log.info("正在进行StaffPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));
        globalWriteLock.writeLock().lock();
        try {
            staffMapper.deleteAll();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.info("执行deleteNSync时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return null;
    }

    @Override
    protected List<BaseModel> createNSync(int iUseCaseID, final List<?> list) {
        log.info("正在进行StaffPresenter的createNSync，list=" + (list == null ? null : list.toString()));

        switch (iUseCaseID) {
            default:
                globalWriteLock.writeLock().lock();
                List<?> staffList = null;
                try {
                    for (int i = 0; i < list.size(); i++) {
                        ((Staff) list.get(i)).setSyncType(BasePresenter.SYNC_Type_C);
                        ((Staff) list.get(i)).setSyncDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
                    }

//                    if (staffMapper.save((List<BaseModel>) list) == list.size()){
//                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//                        for (int i = 1; i < list.size(); i++) {
//                            ((Staff) list.get(i)).setID(((Staff) list.get(0)).getID() + i);
//                        }
//                    } else {
//                        lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
//                    }
                    staffList = staffMapper.save((List<Staff>) list);
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
//                    for (int i = 1; i < list.size(); i++) {
//                        ((Staff) list.get(i)).setID(((Staff) list.get(0)).getID() + i);
//                    }

                } catch (Exception e) {
                    log.info("执行createNSync时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                globalWriteLock.writeLock().unlock();
                return (List<BaseModel>) staffList;
        }
    }

}
