package com.test.bx.app.presenter;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CompanySQLiteEvent;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.presenter.CompanyPresenter;
import com.bx.erp.utils.Shared;

import junit.framework.Assert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;


public class CompanyPresenterTest extends BaseAndroidTestCase {

    CompanyPresenter companyPresenter;
    CompanySQLiteEvent companySQLiteEvent;

    private static final int Event_ID_CompanyPresenterTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EventBus.getDefault().register(this);

        companyPresenter = GlobalController.getInstance().getCompanyPresenter();
        if (companySQLiteEvent == null) {
            companySQLiteEvent = new CompanySQLiteEvent();
            companySQLiteEvent.setId(Event_ID_CompanyPresenterTest);
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        EventBus.getDefault().unregister(this);
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static Company companyInput = null;

        protected static final Company getCompany() throws ParseException, CloneNotSupportedException, InterruptedException {
            companyInput = new Company();
            companyInput.setBusinessLicenseSN("666668888" + String.valueOf(System.currentTimeMillis()).substring(0, 6));
            companyInput.setBusinessLicensePicture("/p/common_db/license/nbr_test_Default_" + String.valueOf(System.currentTimeMillis()).substring(6) + ".jpg");
            companyInput.setBossPhone(getValidStaffPhone());
            companyInput.setBossPassword("000000");
            companyInput.setBossWechat("cjs123456");
            companyInput.setDbName(GenerateDBName());
            companyInput.setKey(UUID.randomUUID().toString().replace("-", "").toUpperCase());
            companyInput.setStatus(0);
            companyInput.setSN("668" + (new Random().nextInt(900) + 100));
            companyInput.setName("公司" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBossName("老板");
            companyInput.setDbUserName(Company.DBUserName_Default);
            companyInput.setDbUserPassword(Company.DBUserPassword_Default);
            companyInput.setBrandName("娃哈哈");
            companyInput.setExpireDatetime(new Date());
            companyInput.setCreateDatetime(new Date());
            companyInput.setUpdateDatetime(new Date());
            companyInput.setLogo(String.valueOf(System.currentTimeMillis()).substring(0, 6));

            return (Company) companyInput.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompanySQLiteEvent(CompanySQLiteEvent event) {
        if (event.getId() == Event_ID_CompanyPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());        }
    }

    @Test
    public void test_a1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1: 正常创建");

        Company company = DataInput.getCompany();
        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue("createSync失败", companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_a2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("异常case2：重复插入（插入失败）");

        Company company = DataInput.getCompany();
        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue("createSync失败", companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue("createSync失败", companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_a3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("异常case3：插入的非空字段为null（插入失败）");

        Company company = DataInput.getCompany();
        company.setDbName(null);
        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue("createSync失败" + companyPresenter.getLastErrorCode(), companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    @Test
    public void test_b1_retrieveNASync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1：正常RN");

        Company company = DataInput.getCompany();
        createCompany(company);

        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companyPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_Done && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue("RetrieveN失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("RetrieveN失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveN失败，原因：size()不正确：" + companySQLiteEvent.getListMasterTable().size(), companySQLiteEvent.getListMasterTable().size() > 0);
    }

    @Test
    public void test_c1_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1：正常创建");

        Company company = DataInput.getCompany();
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Company companyCreated = (Company) companySQLiteEvent.getBaseModel1();
        Assert.assertTrue("createObjectAsync失败，原因：compareTo错误", company.compareTo(companyCreated) == 0);

    }

    @Test
    public void test_c2_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case2：必填字段为null，创建失败");

        Company company = DataInput.getCompany();
        company.setName(null);
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
    }

    @Test
    public void test_c3_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case3：SN为null，创建失败");

        Company company = DataInput.getCompany();
        company.setSN(null);
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError);
    }

    // TODO Pos没有必要知道公司的太多信息。NBR也不会返回
//    @Test
//    public void test_c4_createASync() throws Exception {
//        Shared.printTestMethodStartInfo();
//        caseLog("case4：字段验证不符");
//
//        caseLog("状态不符");
//        Company company = DataInput.getCompany();
//        company.setStatus(1);
//        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
//        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
//        long lTimeOut = UNIT_TEST_TimeOut;
//        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//
//        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//
//        caseLog("营业执照号码不符");
//        Company company2 = DataInput.getCompany();
//        company2.setBusinessLicenseSN("1234567890123456");
//        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
//        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company2, companySQLiteEvent);
//        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//
//        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//
//        caseLog("数据库密码不符");
//        Company company3 = DataInput.getCompany();
//        company3.setDbUserPassword("12345");
//        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
//        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company3, companySQLiteEvent);
//        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//
//        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//    }

    @Test
    public void test_d1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        caseLog("case1：正常删除");

        Company company = DataInput.getCompany();
        Company companyCreated = createCompany(company);

        companyPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("deleteNObjectSync错误，原因：错误码不正确：" + companyPresenter.getLastErrorCode(), companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Company> cmList = retrieveNCompany();
        Assert.assertTrue("deleteNObjectSync错误，原因：没有全部删除", cmList.size() == 0);
    }

    private Company createCompany(Company company) throws Exception {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue("createObjectAsync失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Assert.assertTrue("createObjectAsync失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        Company companyCreated = (Company) companySQLiteEvent.getBaseModel1();
        Assert.assertTrue("createObjectAsync失败，原因：compareTo错误", company.compareTo(companyCreated) == 0);

        return companyCreated;
    }

    private List<Company> retrieveNCompany() throws Exception {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companyPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, companySQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_Done && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        List<Company> bmList = (List<Company>) companySQLiteEvent.getListMasterTable();

        Assert.assertTrue("RetrieveN失败，原因：超时", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        Assert.assertTrue("RetrieveN失败，原因：错误码不正确：" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        return bmList;
    }

    /**
     * 查询本地T_Company表的总条数
     */
    @Test
    public void test_retrieveNCompany() throws Exception {
        Shared.printTestMethodStartInfo();

        CompanyPresenter companyPresenter = GlobalController.getInstance().getCompanyPresenter();
        Integer total = companyPresenter.retrieveCount();
        System.out.println("T_Company表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }

}
