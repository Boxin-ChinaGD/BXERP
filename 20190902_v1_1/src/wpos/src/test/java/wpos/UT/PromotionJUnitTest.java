package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.helper.Configuration;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.promotion.Promotion;
import wpos.utils.DatetimeUtil;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PromotionJUnitTest extends BaseHttpTestCase {
    //    private static LogoutHttpBO logoutHttpBO = null;
//    private static LogoutHttpEvent logoutHttpEvent = null;

    private static List<Promotion> list;

    private static BaseModel bmCreateSync = null;
//    private static BaseModel bmCreateAsync = null;
//    private static BaseModel bmUpdateAsync = null;
//    private static List<BaseModel> bmCreateNAsync = null;
//    @Resource
//    private PromotionPresenter promotionPresenter;
    @Resource
    private PromotionSQLiteBO promotionSQLiteBO;
    @Resource
    private PromotionSQLiteEvent promotionSQLiteEvent;
    @Resource
    private PromotionHttpBO promotionHttpBO;
    @Resource
    private PromotionHttpEvent promotionHttpEvent;
    //
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;

    private static final int Event_ID_PromotionJUnitTest = 10000;

    @BeforeClass
    public void setUp() {
        super.setUp();

//        if (promotionPresenter == null) {
//            promotionPresenter = GlobalController.getInstance().getPromotionPresenter();
//        }

        promotionHttpEvent.setId(Event_ID_PromotionJUnitTest);
        promotionSQLiteEvent.setId(Event_ID_PromotionJUnitTest);
        promotionHttpBO.setHttpEvent(promotionHttpEvent);
        promotionHttpBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionSQLiteBO.setHttpEvent(promotionHttpEvent);
        promotionSQLiteBO.setSqLiteEvent(promotionSQLiteEvent);
        promotionHttpEvent.setHttpBO(promotionHttpBO);
        promotionHttpEvent.setSqliteBO(promotionSQLiteBO);
        promotionSQLiteEvent.setHttpBO(promotionHttpBO);
        promotionSQLiteEvent.setSqliteBO(promotionSQLiteBO);
        //
        posLoginHttpEvent.setId(Event_ID_PromotionJUnitTest);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(Event_ID_PromotionJUnitTest);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        logoutHttpEvent.setId(Event_ID_PromotionJUnitTest);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosHttpEvent(PosHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffHttpEvent(StaffHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent2(StaffLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent2(PosLoginHttpEvent event) {
        if (event.getId() == Event_ID_PromotionJUnitTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
            PromotionInput.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
            PromotionInput.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
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

        //1,2步，登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + posLoginHttpBO.getHttpEvent().getStatus() + "\t" + staffLoginHttpBO.getHttpEvent().getStatus());

        //4.模拟网页创建一个Promotion
        System.out.println("第一次调用create方法");
        Promotion promotion = PromotionJUnitTest.DataInput.getPromotion();
        if (!promotionHttpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, promotion)) {
            Assert.assertTrue(false,"创建失败!");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done
                && promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"请求服务器创建Promotion超时!");
        }

        logOut();

        //POS2,STAFF2登陆
        Assert.assertTrue(Shared.login(2, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        //5.调用RN(有同步数据)
        System.out.println("第一次调用RN方法");
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false,"同步失败");
        }
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"Promotion同步超时，服务器返回错误码：" + promotionSQLiteEvent.getLastErrorCode() + ", 错误信息：" + promotionSQLiteEvent.getLastErrorMessage());
        }
        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue(PromotionList.size() > 0,"ListMasterTable为空");

        list = PromotionList;
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_FeedbackEx() throws Exception {
        Shared.printTestMethodStartInfo();

        //1,2步，登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());


        String PromotionIDs = "";
        for (int i = 0; i < list.size(); i++) {
            PromotionIDs = PromotionIDs + "," + list.get(i).getID();
        }
        PromotionIDs = PromotionIDs.substring(1, PromotionIDs.length());

        //调用Feedback,
        System.out.println("第一次调用feedback方法");
        if (!promotionHttpBO.feedback(PromotionIDs)) {
            Assert.assertTrue(false,"同步失败");
        }
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        promotionHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue(false,"Promotion Feedback超时!");
        }
        //调用RN，返回无数据
        System.out.println("第一次调用RN方法");
        if (!promotionHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            Assert.assertTrue(false,"同步失败");
        }
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"Promotion同步超时!");
        }

        List<Promotion> PromotionList = (List<Promotion>) promotionSQLiteEvent.getListMasterTable();
        Assert.assertTrue(PromotionList == null,"feedback失败");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    //全部下载
    @Test
    public void test_c_Everything() throws Exception {
        Shared.printTestMethodStartInfo();

        //登录POS和Staff
        Assert.assertTrue(Shared.login(1, posLoginHttpBO, staffLoginHttpBO),"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        //本地SQLite增加一条数据。全部同步后不存在
        bmCreateSync = PromotionJUnitTest.DataInput.getPromotion();
        BaseModel bm = promotionPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);

        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"CreateSync bm1测试失败,原因:返回错误码不正确!");

        //调用普通的RNaction去进行同步
        System.out.println("第一次调用RNaction方法");
        Promotion b = new Promotion();
        b.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        b.setPageSize(BaseHttpBO.PAGE_SIZE_LoadAll);
        b.setSubStatusOfStatus(Promotion.EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
        b.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
        if (!promotionHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, b)) {
            Assert.assertTrue(false,"同步失败！");
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        promotionSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        while (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (promotionSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"请求服务器同步Promotion超时!");
        }

        //服务器返回的数据和sqlite里的数据对比,
        List<Promotion> PromotionList = (List<Promotion>) promotionPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(promotionPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSync测试失败,原因:返回错误码不正确!");

        List<Promotion> list = (List<Promotion>) promotionHttpBO.getHttpEvent().getListMasterTable();
        if (list.size() != PromotionList.size()) {
            Assert.assertTrue(false,"全部同步失败。服务器返回的数据和本地SQLite的数据不一致");
        }
        // 检查服务器返回的促销是否是未删除，还没过期的促销
        Date now = new Date();
        for (Promotion promotion : list) {
            Assert.assertTrue(promotion.getStatus() == Promotion.EnumStatusPromotion.ESP_Active.getIndex(),"该促销已失效");
            Assert.assertTrue(now.before(promotion.getDatetimeEnd()),"该促销已过期");
        }
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }
}
