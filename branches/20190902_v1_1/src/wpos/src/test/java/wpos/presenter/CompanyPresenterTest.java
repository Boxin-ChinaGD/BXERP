package wpos.presenter;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.event.UI.CompanySQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.Company;
import wpos.model.ErrorInfo;
import wpos.utils.Shared;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CompanyPresenterTest extends BasePresenterTest{
    private static final int Event_ID_CompanyPresenterTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();

        EventBus.getDefault().register(this);
        if (companySQLiteEvent != null) {
            companySQLiteEvent.setId(Event_ID_CompanyPresenterTest);
        }
    }

    @AfterClass
    public void tearDown() {
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
            companyInput.setBossPhone(Shared.getValidStaffPhone());
            companyInput.setBossPassword("000000");
            companyInput.setBossWechat("cjs123456");
            companyInput.setDbName(Shared.GenerateDBName());
            companyInput.setKey(UUID.randomUUID().toString().replace("-", "").toUpperCase());
            companyInput.setStatus(0);
            companyInput.setSN("668" + (new Random().nextInt(900) + 100));
            companyInput.setName("??????" + String.valueOf(System.currentTimeMillis()).substring(6));
            companyInput.setBossName("??????");
            companyInput.setDbUserName(Company.DBUserName_Default);
            companyInput.setDbUserPassword(Company.DBUserPassword_Default);
            companyInput.setBrandName("?????????");
            companyInput.setExpireDatetime(new Date());
            companyInput.setCreateDatetime(new Date());
            companyInput.setUpdateDatetime(new Date());
            companyInput.setLogo(String.valueOf(System.currentTimeMillis()).substring(0, 6));

            return (Company) companyInput.clone();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCompanySQLiteEvent(CompanySQLiteEvent event) {
        if (event.getId() == Event_ID_CompanyPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());        }
    }

    @Test
    public void test_a1_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1: ????????????");

        Company company = DataInput.getCompany();
        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue(companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync??????");
    }

    @Test
    public void test_a2_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2???????????????");

        Company company = DataInput.getCompany();
        company = (Company) companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue(companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createSync??????");

        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue(companyPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createSync??????");
    }

    @Test
    public void test_a3_createSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("??????case3???????????????????????????null??????????????????");

        Company company = DataInput.getCompany();
        company.setDbName(null);
        companyPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, company);
        Assert.assertTrue(companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createSync??????" + companyPresenter.getLastErrorCode());
    }

    @Test
    public void test_b1_retrieveNASync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1?????????RN");

        Company company = DataInput.getCompany();
        createCompany(company);

        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companyPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_Done && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "RetrieveN????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveN???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());
        Assert.assertTrue(companySQLiteEvent.getListMasterTable().size() > 0, "RetrieveN??????????????????size()????????????" + companySQLiteEvent.getListMasterTable().size());
    }

    @Test
    public void test_c1_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1???????????????");

        Company company = DataInput.getCompany();
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());

        Company companyCreated = (Company) companySQLiteEvent.getBaseModel1();
        Assert.assertTrue(company.compareTo(companyCreated) == 0, "createObjectAsync??????????????????compareTo??????");

    }

    @Test
    public void test_c2_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case2??????????????????null???????????????");

        Company company = DataInput.getCompany();
        company.setName(null);
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField, "createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());
    }

    @Test
    public void test_c3_createASync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case3???SN???null???????????????");

        Company company = DataInput.getCompany();
        company.setSN(null);
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_OtherError, "createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());
    }

    // TODO Pos??????????????????????????????????????????NBR???????????????
//    @Test
//    public void test_c4_createASync() throws Exception {
//        Shared.printTestMethodStartInfo();
//        Shared.caseLog("case4?????????????????????");
//
//        Shared.caseLog("????????????");
//        Company company = DataInput.getCompany();
//        company.setStatus(1);
//        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
//        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
//        long lTimeOut = Shared.UNIT_TEST_TimeOut;
//        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
//                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
//            Thread.sleep(1000);
//        }
//
//        Assert.assertTrue("createObjectAsync????????????????????????", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//
//        Shared.caseLog("????????????????????????");
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
//        Assert.assertTrue("createObjectAsync????????????????????????", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//
//        Shared.caseLog("?????????????????????");
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
//        Assert.assertTrue("createObjectAsync????????????????????????", companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
//        Assert.assertTrue("createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode(), companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
//    }

    @Test
    public void test_d1_deleteSync() throws Exception {
        Shared.printTestMethodStartInfo();
        Shared.caseLog("case1???????????????");

        Company company = DataInput.getCompany();
        Company companyCreated = createCompany(company);

        companyPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(companyPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "deleteNObjectSync???????????????????????????????????????" + companyPresenter.getLastErrorCode());

        List<Company> cmList = retrieveNCompany();
        Assert.assertTrue(cmList.size() == 0, "deleteNObjectSync????????????????????????????????????");
    }

    private Company createCompany(Company company) throws Exception {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_CreateAsync);
        companyPresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, company, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData, "createObjectAsync????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectAsync???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());

        Company companyCreated = (Company) companySQLiteEvent.getBaseModel1();
        Assert.assertTrue(company.compareTo(companyCreated) == 0, "createObjectAsync??????????????????compareTo??????");

        return companyCreated;
    }

    private List<Company> retrieveNCompany() throws Exception {
        companySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        companySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Company_RetrieveNAsync);
        companyPresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, companySQLiteEvent);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_Done && //
                companySQLiteEvent.getStatus() != BaseSQLiteEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }

        List<Company> bmList = (List<Company>) companySQLiteEvent.getListMasterTable();

        Assert.assertTrue(companySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_ToDo, "RetrieveN????????????????????????");
        Assert.assertTrue(companySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveN???????????????????????????????????????" + companySQLiteEvent.getLastErrorCode());

        return bmList;
    }

    /**
     * ????????????T_Company???????????????
     */
    @Test
    public void test_retrieveNCompany() throws Exception {
        Shared.printTestMethodStartInfo();

//        CompanyPresenter companyPresenter = GlobalController.getInstance().getCompanyPresenter();
        Integer total = companyPresenter.retrieveCount();
        System.out.println("T_Company???????????????" + total);
        Assert.assertTrue(total > Shared.INVALID_CASE_TOTAL, "???????????????");
    }

}
