package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.utils.EventBus;
import wpos.dao.CompanyMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.Company;
import wpos.model.ErrorInfo;

import javax.annotation.Resource;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.List;

@Component("companyPresenter")
@Scope("prototype")
public class CompanyPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    public CompanyMapper companyMapper;

//    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static final String QUERY_Company_TABLE = "SELECT F_ID,F_Name,F_SN,F_BusinessLicenseSN,F_BusinessLicensePicture,F_BossName,F_BossPhone," +
            "F_BossPassword,F_BossWechat,F_DBName,F_Key,F_DBUserName,F_DBUserPassword,F_Status,F_Submchid,F_BrandName,F_CreateDatetime," +
            "F_UpdateDatetime,F_ExpireDatetime,F_Logo" +
            "  FROM T_Company ";

//    public CompanyPresenter(final CompanyMapper companyMapper) {
//        super(companyMapper);
//    }

    @Override
    public void createTableSync() {
        companyMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (Company.class.isAnnotationPresent(Table.class)) {
            Table annotation = Company.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + Company.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    public BaseModel createSync(int iUseCaseID, BaseModel bm) {
        log.info("正在进行CompanyPresenter的createSync，bm=" + (bm == null ? null : bm.toString()));

        globalWriteLock.writeLock().lock();
        BaseModel company = null;
        try {
            companyMapper.create((Company) bm);

            String sql;
            if (bm.getID() != null) {
                sql = "WHERE F_ID = " + bm.getID();
            } else {
                sql = "WHERE F_ID = (SELECT MAX(F_ID) FROM T_Company)";
            }
            Query query = entityManager.createNativeQuery(QUERY_Company_TABLE + sql, Company.class);
            List<Company> companies = query.getResultList();
            if (companies != null && companies.size() > 0) {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                company = companies.get(0);
            } else {
                lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            }
        } catch (Exception e) {
            log.error("执行createSync出现错误，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        globalWriteLock.writeLock().unlock();
        return company;
    }

    @Override
    protected boolean retrieveNAsync(int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CompanyPresenter的retrieveNAsync，bm=" + (bm == null ? null : bm.toString()));

        //eventTypeSQLite:ESET_Company_RetrieveNAsync
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        List<Company> companyList = null;
                        //
                        try {
                            companyList = companyMapper.findAll();
                            if (companyList.size() > 0) {
                                Company company = new Company();
                                company = (Company) companyList.get(0);
                                if (company.getSN() != null && !"".equals(company.getSN())) {
                                    event.setBaseModel1(company);
                                } else {
                                    event.setBaseModel1(null);
                                }
                            } else {
                                event.setBaseModel1(null);
                            }
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                            log.error("在本地查询所有Company出错，错误信息为：" + e.getMessage());
                        }
                        event.setListMasterTable(companyList);
                        //
                        EventBus.getDefault().post(event);
                    }
                }).start();
                break;
        }
        return true;
    }

    public boolean createAsync(final int iUseCaseID, final BaseModel bm, final BaseSQLiteEvent event) {
        log.info("正在进行CompanyPresenter的createAsync，bm=" + (bm == null ? null : bm.toString()));

        //eventTypeSQLite:ESET_Company_CreateAsync
        switch (iUseCaseID) {
            default:
                (new Thread() {
                    @Override
                    public void run() {
                        globalWriteLock.writeLock().lock();
                        Company c = (Company) bm;
                        try {
                            c.setSql("where F_SN = '%s'");
                            c.setConditions(new String[]{c.getSN()});
                            String sql = String.format(c.getSql(), c.getConditions());
                            Query query = entityManager.createNativeQuery(QUERY_Company_TABLE + sql, Company.class);
                            List<Company> companyList = query.getResultList();
                            if (companyList.size() > 0) {
                                companyMapper.delete(c);
                            }
                            companyMapper.create(c);
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
                        } catch (Exception e) {
                            log.error("执行createAsync失败，错误信息为" + e.getMessage());
                            e.printStackTrace();
                            event.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_OtherError);
                        }
                        event.setBaseModel1(c);
                        //
                        EventBus.getDefault().post(event);
                        globalWriteLock.writeLock().unlock();
                    }
                }).start();
                break;
        }
        return true;
    }

    @Override
    protected BaseModel deleteNSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在进行CompanyPresenter的deleteNSync，bm=" + (bm == null ? null : bm.toString()));

        try {
            companyMapper.deleteAllInBatch();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
        } catch (Exception e) {
            log.error("执行deleteNSync出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
        }
        return null;
    }

    @Override
    protected String getQueryTable() {
        return QUERY_Company_TABLE;
    }
}
