package com.test.bx.app;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.PromotionHttpBO;
import com.bx.erp.bo.PromotionSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.StaffHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Promotion;
import com.bx.erp.model.Promotion.EnumTypePromotion;
import com.bx.erp.model.Promotion.EnumStatusPromotion;
import com.bx.erp.presenter.PromotionPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PromotionJUnitTest extends BaseHttpAndroidTestCase {
//    private static LogoutHttpBO logoutHttpBO = null;
//    private static LogoutHttpEvent logoutHttpEvent = null;

    private static List<Promotion> list;

    private static BaseModel bmCreateSync = null;
//    private static BaseModel bmCreateAsync = null;
//    private static BaseModel bmUpdateAsync = null;
//    private static List<BaseModel> bmCreateNAsync = null;

    private static PromotionPresenter promotionPresenter;
    private static PromotionSQLiteBO promotionSQLiteBO;
    private static PromotionSQLiteEvent promotionSQLiteEvent;
    private static PromotionHttpBO promotionHttpBO;
    private static PromotionHttpEvent promotionHttpEvent;
    //
    private static PosLoginHttpBO posLoginHttpBO;
    private static PosLoginHttpEvent posLoginHttpEvent;

    private static StaffLoginHttpBO staffLoginHttpBO;
    private static StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_PromotionJUnitTest = 10000;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (promotionPresenter == null) {
            promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
        }

        if (promotionHttpEvent == null) {
            promotionHttpEvent = new PromotionHttpEvent();
            promotionHttpEvent.setId(Event_ID_PromotionJUnitTest);
        }
        if (promotionSQLiteEvent == null) {
            promotionSQLiteEvent = new PromotionSQLiteEvent();
            promotionSQLiteEvent.setId(Event_ID_PromotionJUnitTest);
        }
        if (promotionHttpBO == null) {
            promotionHttpBO = new PromotionHttpBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        if (promotionSQLiteBO == null) {
            promotionSQLiteBO = new PromotionSQLiteBO(GlobalController.getInstance().getContext(), promotionSQLiteEvent, promotionHttpEvent);
        }
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_PromotionJUnitTest);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_PromotionJUnitTest);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_PromotionJUnitTest);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static Promotion PromotionInput = null;
        private static Random r = new Random();

        protected static final Promotion getPromotion() throws CloneNotSupportedException, InterruptedException {
            Date date = new Date();
            Date dt1 = DatetimeUtil.getDays(new Date(), 2);
            Date dt2 = DatetimeUtil.getDays(new Date(), 3);

            PromotionInput = new Promotion();
            PromotionInput.setName("a" + System.currentTimeMillis() % 1000000);
            PromotionInput.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            PromotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
            PromotionInput.setDatetimeStart(dt1);
            PromotionInput.setDatetimeEnd(dt2);
            PromotionInput.setExcecutionThreshold(10);
            PromotionInput.setExcecutionAmount(8);
            PromotionInput.setExcecutionDiscount(0.8d);
            PromotionInput.setScope(1);
            PromotionInput.setStaff(1);
            PromotionInput.setPageIndex(String.valueOf(1));
            PromotionInput.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
            PromotionInput.setReturnObject(1);
            PromotionInput.setCommodityIDs("119");
            Thread.sleep(1000);
            PromotionInput.setSn("CX" + System.currentTimeMillis() % 1000000);

            return (Promotion) PromotionInput.clone();
        }

        protected static final List<?> getPromotionList() throws CloneNotSupportedException, InterruptedException {
            List<BaseModel> commList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                commList.add(getPromotion());
            }
            return commList;
        }
    }

    @Test
    public void test_a_RetrieveNEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));

        //4.????????????????????????Promotion
        System.out.println("???????????????create??????");
        Promotion promotion = PromotionJUnitTest.DataInput.getPromotion();
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue("????????????!", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done
                && promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("?????????????????????Promotion??????!", false);
        }

        logOut();

        //POS2,STAFF2??????
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(2L, posLoginHttpBO, staffLoginHttpBO));
        //5.??????RN(???????????????)
        System.out.println("???????????????RN??????");
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Promotion??????????????????????????????????????????" + promotionSQLiteEvent.getLastErrorCode() + ", ???????????????" + promotionSQLiteEvent.getLastErrorMessage(), false);
        }
        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue("ListMasterTable??????", PromotionList.size() > 0);

        list = PromotionList;
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2????????????POS???Staff
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));


        String PromotionIDs = "";
        for (int i = 0; i < list.size(); i++) {
            PromotionIDs = PromotionIDs + "," + list.get(i).getID();
        }
        PromotionIDs = PromotionIDs.substring(1, PromotionIDs.length());

        //??????Feedback,
        System.out.println("???????????????feedback??????");
        if (!promotionHttpBO.feedback(PromotionIDs)) {
            Assert.assertTrue("????????????", false);
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        promotionHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("Promotion Feedback??????!", false);
        }
        //??????RN??????????????????
        System.out.println("???????????????RN??????");
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue("????????????", false);
        }
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("Promotion????????????!", false);
        }

        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue("feedback??????", PromotionList == null);
    }

    //????????????
    public void test_c_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //??????POS???Staff
        Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), Shared.login(1L, posLoginHttpBO, staffLoginHttpBO));
        //??????SQLite?????????????????????????????????????????????
        bmCreateSync = PromotionJUnitTest.DataInput.getPromotion();
        BaseModel bm = promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????RNaction???????????????
        System.out.println("???????????????RNaction??????");
        Promotion b = new Promotion();
        b.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        b.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        b.setSubStatusOfStatus(Promotion.EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
        b.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
        if (!promotionHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, b)) {
            Assert.assertTrue("???????????????", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("?????????????????????Promotion??????!", false);
        }

        //???????????????????????????sqlite??????????????????,
        List<Promotion> PromotionList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        List<Promotion> list = (List<Promotion>) promotionHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != PromotionList.size()) {
            Assert.assertTrue("??????????????????????????????????????????????????????SQLite??????????????????", false);
        }
        // ????????????????????????????????????????????????????????????????????????
        Date now = new Date();
        for (Promotion promotion : list) {
            Assert.assertTrue("??????????????????", promotion.getStatus() == EnumStatusPromotion.ESP_Active.getIndex());
            Assert.assertTrue("??????????????????", now.before(promotion.getDatetimeEnd()));
        }
    }
}
