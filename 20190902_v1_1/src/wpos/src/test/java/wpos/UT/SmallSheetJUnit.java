package wpos.UT;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.allController.SmallSheetController;
import wpos.allEnum.ThreadMode;
import wpos.base.BaseHttpTestCase;
import wpos.bo.*;
import wpos.event.*;
import wpos.event.UI.*;
import wpos.helper.Configuration;
import wpos.helper.Constants;
import wpos.listener.Subscribe;
import wpos.model.*;
import wpos.utils.ResetBaseDataUtil;
import wpos.utils.Shared;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmallSheetJUnit extends BaseHttpTestCase {
    /**
     * 更新操作需要知道一个新创建的ID
     */
    private static SmallSheetFrame createdFrame = null;

    /**
     * retrieveN需要知道一个被更新的对象,判断是否存在其中
     */
    private SmallSheetFrame updatedFrame = null;

//    private static SmallSheetFramePresenter smallSheetFramePresenter = null;
//    private static SmallSheetTextPresenter smallSheetTextPresenter = null;
    @Resource
    private SmallSheetHttpBO smallSheetHttpBO;
    @Resource
    private SmallSheetSQLiteBO smallSheetSQLiteBO;
    @Resource
    private SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    @Resource
    private SmallSheetHttpEvent smallSheetHttpEvent;
    @Resource
    private PosLoginHttpBO posLoginHttpBO;
    @Resource
    private PosLoginHttpEvent posLoginHttpEvent;
    @Resource
    private StaffLoginHttpBO staffLoginHttpBO;
    @Resource
    private StaffLoginHttpEvent staffLoginHttpEvent;
    //
    @Resource
    public CouponSQLiteBO couponSQLiteBO;
    @Resource
    public CouponSQLiteEvent couponSQLiteEvent;
    @Resource
    public CouponHttpEvent couponHttpEvent;
    @Resource
    public CouponHttpBO couponHttpBO;
    @Resource
    private ResetBaseDataUtil resetBaseDataUtil;

    private static final int EVENT_ID_SmallSheetJUnit = 10000;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionSQLiteEvent(PromotionSQLiteEvent event) {
        event.onEvent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
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

    public static class DataInput {
        private static SmallSheetFrame frameInput = null;

        public static final SmallSheetFrame getSmallSheetFrame(Integer lFrameMaxIDInSQLite, Integer lTextMaxIDInSQLite) throws Exception {
            frameInput = new SmallSheetFrame();
            frameInput.setCreateDatetime(new Date());
            frameInput.setID(lFrameMaxIDInSQLite);
            frameInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
            frameInput.setDelimiterToRepeat("-");
            frameInput.setReturnObject(1);//1, 服务器返回创建的对象。0，服务器不返回创建的对象
            List<SmallSheetText> list = initSmallSheetText(lFrameMaxIDInSQLite, lTextMaxIDInSQLite);
            frameInput.setListSlave1(list);

            return (SmallSheetFrame) frameInput.clone();
        }

        protected static List<SmallSheetText> initSmallSheetText(Integer lFrameStartID, Integer lTextStartID) throws ParseException {
            List<SmallSheetText> list = new ArrayList<SmallSheetText>();

            SmallSheetText smallSheetText = new SmallSheetText();

            smallSheetText.setContent("博昕");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("单据号：12345678910");
            smallSheetText.setSize(25.f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("2018-9-14 9:40");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("总金额：￥500.0");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("支付方式：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("优惠：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("应付：");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("微信支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("支付宝支付：￥50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("现金支付：￥400.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("谢谢惠顾");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("1");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("2");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("3");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("4");
            smallSheetText.setSize(20.0f);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("5");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("6");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("7");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("8");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("9");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(51);
            list.add(smallSheetText);

            // 小票格式的从表总数必须等于20
//            smallSheetText = new SmallSheetText();
//
//            smallSheetText.setContent("博昕POS机收银系统");
//            smallSheetText.setSize(30.0f);
//            smallSheetText.setGravity(17);
//            list.add(smallSheetText);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setID(lTextStartID + i);
                list.get(i).setFrameId(Long.valueOf(lFrameStartID));
                list.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime());
            }

            return list;
        }
    }

    @BeforeClass
    public void setUp() {
        super.setUp();
//        if (framePresenter == null) {
//            framePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        }
//        if (textPresenter == null) {
//            textPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//        }
        //
        posLoginHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        posLoginHttpBO.setHttpEvent(posLoginHttpEvent);
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        staffLoginHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        staffLoginHttpBO.setHttpEvent(staffLoginHttpEvent);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        smallSheetSQLiteEvent.setId(EVENT_ID_SmallSheetJUnit);
        smallSheetHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        smallSheetSQLiteBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetSQLiteBO.setHttpEvent(smallSheetHttpEvent);
        smallSheetHttpBO.setSqLiteEvent(smallSheetSQLiteEvent);
        smallSheetHttpBO.setHttpEvent(smallSheetHttpEvent);
        //
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);
        //
        couponSQLiteEvent.setId(EVENT_ID_SmallSheetJUnit);
        couponHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        couponHttpBO.setSqLiteEvent(couponSQLiteEvent);
        couponHttpBO.setHttpEvent(couponHttpEvent);
        couponSQLiteBO.setSqLiteEvent(couponSQLiteEvent);
        couponSQLiteBO.setHttpEvent(couponHttpEvent);
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);
        //
        logoutHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        logoutHttpBO.setHttpEvent(logoutHttpEvent);
        logoutHttpEvent.setHttpBO(logoutHttpBO);
    }

    @AfterClass
    public void tearDown() {
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("未处理的Event，ID=" + event.getId());
        }
    }

    /**
     * User-Agent: Fiddler
     * Host: 192.168.1.216:8080
     * Content-Type: application/x-www-form-ulencoded
     *
     * @throws
     */
    @Test
    public void test_a_Create() throws Exception {
        Shared.printTestMethodStartInfo();

        //清空表，以免造成干扰。有需要可以反注释
//        textPresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);
//        framePresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //获取默认小票格式的从表信息，用来创建
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setID(1);
        List<BaseModel> list = (List<BaseModel>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue( list != null && list.size() > 0 && smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"查询小票格式失败");
        //
        String sql = "where F_FrameID = %s";
        String[] condition = {list.get(0).getID() + ""};
        ssf.setSql(sql);
        ssf.setConditions(condition);
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"SmallSheetPresenter根据条件查询数据时的错误码应该为EC_NoError");
        //
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        //
        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        for (int i = 0; i < textList.size(); i++) {
            textList.get(i).setID(maxTextIDInSQLite + i);
            textList.get(i).setFrameId(Long.valueOf(ssfOld.getID()));
            textList.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime());
        }
        ssfOld.setListSlave1(textList);
        //
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue(false,"创建失败！" + smallSheetSQLiteBO.getSqLiteEvent().printErrorInfo());
        }

        //等待处理完毕
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"测试失败！原因：超时。当前sqlList的状态：" + smallSheetSQLiteBO.getSqLiteEvent().getStatus() + "，错误码：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorCode() + ",错误信息：" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage());
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue(master != null,"服务器返回的对象为空");
        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue(ssfOld.compareTo(master) == 0,"服务器返回的对象和上传的对象不相等！");

        createdFrame = master;//为修改测试提供数据
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_a_Create2() throws Exception {
        Shared.printTestMethodStartInfo();
        //测试收银员权限

        if (!Shared.login(1, "15016509167", "000000", 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue( false,"创建失败！");
        }
        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData) {
            Assert.assertTrue( false,"测试失败！原因：超时");
        }
        Assert.assertTrue(smallSheetSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoPermission);
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_b_Update() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        if (createdFrame == null) {
            Assert.assertTrue(false,"CREATE测试失败，并未得到有效的frame ID");
        }

        //得到需要更新的最新小票格式
        SmallSheetFrame ssfSQLite = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createdFrame);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"retrieve1Sync返回的錯誤碼不正確");
        //
        String sql = "where F_FrameID = %s";
        String[] condition = {ssfSQLite.getID() + ""};
        ssfSQLite.setSql(sql);
        ssfSQLite.setConditions(condition);
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssfSQLite);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"SmallSheetPresenter根据条件查询数据时的错误码应该为EC_NoError");
        //
        if (textList.size() > 0) {
            textList.get(10).setContent("aaaaaaaaaaaaaaa");
        }
        ssfSQLite.setLogo("qqqqqqqqqqqqqqqqqqqqqq");   //更新logo字段
        ssfSQLite.setListSlave1(textList);
        //
        ssfSQLite.setReturnObject(1);//需要服务器返回对象

        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
        if (!smallSheetSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfSQLite)) {
            Assert.assertTrue( false,"修改失败！");
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"测试失败！原因：超时");
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue( master != null,"向服务器请求修改小票格式失败");

        //比较主从表的数据,需要全部一致，旧对象：ssfSQLite，新对象master
        Assert.assertTrue( ssfSQLite.compareTo(master) == 0,"服务器返回的对象和修改的对象不相等！");

        updatedFrame = master;
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_c_RetrieveNC() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        ssf.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, ssf)) {
            Assert.assertTrue(false,"RetrieveN失败！");
        }
        //等待处理完毕
        long lTimeout = 60;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"测试失败！原因：超时");
        }
        // 获取本地SQLite小票格式验证结果
        List<SmallSheetFrame> frameList1 = new ArrayList<SmallSheetFrame>();
        frameList1 = (List<SmallSheetFrame>) smallSheetSQLiteBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue( frameList1.size() > 0,"同步小票格式失败，本地SQLite没有同步到小票格式");
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_d_Delete() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue(false,"创建失败！");
        }

        //等待处理完毕
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue( false,"测试失败！原因：超时");
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue( master != null,"小票格式创建失败");
        // 删除数据
        deleteSmallSheetFrameInServerAndSQLite(master);
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_e_update() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //异常case1：将不存在的小票ID传到服务器
        List<SmallSheetFrame> smallSheetFrameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确!");
        SmallSheetFrame smallSheetFrame = smallSheetFrameList.get(0);
        //
        SmallSheetText text = new SmallSheetText();
        text.setSql("where F_FrameID = %s");
        text.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        text.setFrameId(Long.valueOf(smallSheetFrame.getID()));
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, text);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确!");
        //
        Integer tmpRowID = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        smallSheetFrame.setID(tmpRowID);
        // 更改主表ID要对应更改从表的主表ID
        for (BaseModel bm : smallSheetTextList) {
            SmallSheetText smallSheetText = (SmallSheetText) bm;
            smallSheetText.setFrameId(Long.valueOf(tmpRowID));
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        smallSheetFrame.setReturnObject(1);
        //
        smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue(false,"更新超时!");
        }
        Assert.assertTrue(smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData,"服务器返回的错误码不正确, 错误码：" + smallSheetHttpEvent.getLastErrorCode());
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_e_update2() throws Exception {
        Shared.printTestMethodStartInfo();
        // case2：上传到服务器的小票格式对象的主从表不匹配
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        // 创建一个小票格式并上传成功
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue( false,"创建失败！" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage());
        }

        //等待处理完毕
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue(false,"测试失败！原因：超时");
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue( master != null,"服务器返回的对象为空");
        //比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfOld，新对象master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue(ssfOld.compareTo(master) == 0,"服务器返回的对象和上传的对象不相等！");
        // 让ID和从表的FrameID不一致
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) master.getListSlave1();
        for (SmallSheetText sst : smallSheetTextList) {
            sst.setFrameId(sst.getFrameId() + 1);
        }
        //
        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, master)) {
            Assert.assertTrue( false,"主表ID和从表FrameID不一致，checkUpdate()未能检查到");
        }
        // 创建完，测试后，删除数据
        for (SmallSheetText sst : (List<SmallSheetText>) master.getListSlave1()) {
            sst.setFrameId(sst.getFrameId() - 1);
        }
        deleteSmallSheetFrameInServerAndSQLite(master);
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_f_delete1() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }

        //case1:将不存在的小票ID传到服务器
        List<SmallSheetFrame> smallSheetFrameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确!");
        SmallSheetFrame smallSheetFrame = smallSheetFrameList.get(0);
        //
        SmallSheetText text = new SmallSheetText();
        text.setSql("where F_FrameID = %s");
        text.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        text.setFrameId(Long.valueOf(smallSheetFrame.getID()));
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, text);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确!");
        //
        Integer tmpRowID = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        smallSheetFrame.setID(tmpRowID);
        smallSheetFrame.setListSlave1(smallSheetFrameList);
        //
        smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue(false,"更新超时!");
        }
        Assert.assertTrue(smallSheetHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"服务器返回的错误码不正确, 错误码：" + smallSheetHttpBO.getHttpEvent().getLastErrorCode());
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    @Test
    public void test_f_uploadDuplicatedSmallSheetFrame() throws Exception {
        Shared.printTestMethodStartInfo();
        // case:新增一个小票格式，在上传到服务器的时候，pos端断网，服务器创建小票格式成功，Pos没有接收到返回信息。之后Pos端恢复网络，再次上传该小票格式，此时服务器提示已经存在此小票格式，直接返回该小票格式给Pos端

        // 登录
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        // 创建小票格式
        Integer maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        Integer maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        Assert.assertTrue( smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"错误码不正确！");
        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        //
        SmallSheetFrame ssfInSQLite = (SmallSheetFrame) smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && ssfInSQLite != null,"创建临时的小票格式失败！");
        // 上传本地临时的小票格式，向服务器请求创建，会返回结果自动更新本地
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        ssfInSQLite.setListSlave1(ssfOld.getListSlave1());
        Shared.uploadSmallSheetTempData(ssfInSQLite, smallSheetSQLiteBO);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((smallSheetSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
                    || (smallSheetHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && smallSheetHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lTimeOut < 0) {
            Assert.assertTrue( false,"上传超时");
        }
        if (smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue( false,"上传失败");
        }
        // 重复上传
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadSmallSheetTempData(ssfInSQLite, smallSheetSQLiteBO);
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((smallSheetSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
                    || (smallSheetHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && smallSheetHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_Duplicated)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lTimeOut < 0) {
            Assert.assertTrue( false,"上传超时");
        }
        if (smallSheetHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) { // 没有返回对象，解析JSONObject失败EC_OtherError
            Assert.assertTrue(false,"重复上传小票格式，服务器返回的错误码不是EC_Duplicated:" + smallSheetHttpEvent.getLastErrorCode());
        }
        // nbr不返回对象，也就不在sqlite创建
//        if (framePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) {
//            Assert.assertTrue("重复上传并更新SQLite没导致主键冲突", false);
//        }
//
//        // 获取服务器传来的数据
//        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetHttpEvent.getBaseModel1();
//        ssfInSQLite.setIgnoreIDInComparision(true);
//        // 比较主从表的数据，除了ID和从表的FrameID不一致，其它全部一致        //旧对象：ssfInSQLite，新对象ssf
//        Assert.assertTrue("服务器并没有返回重复的小票格式", ssfInSQLite.compareTo(ssf) == 0);
        // 创建完，测试后，删除数据
        deleteSmallSheetFrameInServerAndSQLite(ssfInSQLite);
        // 退出登录，防止跑testunit.xml登录过多被nbr服务器限制访问
        logOut();
    }

    /**
     * 由于该case跑完会改变Event对象，故将改case放到最后
     * 1.清空本地SQLite的SmallSheet Table
     * 2.检查是否清空成功
     * 3.下载基础资料
     * 4.检查本地是否成功下载nbr的默认小票格式
     */
    @Test
    public void test_g_testLoad() throws Exception {
        Shared.printTestMethodStartInfo();

        // 清空数据
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(frameList.size() == 0,"RetrieveNSync SmallSheetFrameList搜索到的数据数量应该=0!");
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(textList.size() == 0,"RetrieveNSync SmallSheetTextList搜索到的数据数量应该=0!");
        //
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue(false,"登录失败，服务器=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo());
        }
        //
//        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(EVENT_ID_SmallSheetJUnit);
        try {
            resetBaseDataUtil.setEventID(EVENT_ID_SmallSheetJUnit);
            resetBaseDataUtil.initObject();
            resetBaseDataUtil.ResetBaseData(false);
            // 这里由于要在onEvent中同步其他的信息，所以检查的是最后进行同步的EventStatus
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done
                    && couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction
                    && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
                Assert.assertTrue( false,"重置超时!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("重置资料失败:" + e.getMessage());
            Assert.assertTrue(false,"重置资料失败！");
        }
        //
        List<SmallSheetFrame> frameList2 = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue( smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"RetrieveNSync测试失败,原因:返回错误码不正确!");
        Assert.assertTrue( frameList2.size() > 0,"RetrieveNSync SmallSheetFrameList搜索到的数据数量应该>0!");
        SmallSheetFrame retrieveNSmallSheetFram = new SmallSheetFrame();
        for (SmallSheetFrame smallSheetFrame : frameList2) {
            if (smallSheetFrame.getID() == SmallSheetController.Default_SmallSheetID_INPos) { // 如果mysql已经把frameId为1的删除，那么这个case就通不过
                retrieveNSmallSheetFram = smallSheetFrame;
                break;
            }
        }
        //
        SmallSheetText sst = new SmallSheetText();
        sst.setSql("where F_FrameID = %s");
        sst.setConditions(new String[]{String.valueOf(retrieveNSmallSheetFram.getID())});
        sst.setFrameId(Long.valueOf(retrieveNSmallSheetFram.getID()));
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        Assert.assertTrue(smallSheetTextList.size() == SmallSheetController.Default_SmallSheetListSlveSize,"RetrieveNSync SmallSheetTextList搜索到的数据数量应该=20!找到有" + smallSheetTextList.size() + "条数据");
    }

    protected void deleteSmallSheetFrameInServerAndSQLite(SmallSheetFrame ssf) throws Exception {
        smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, ssf);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue(false,"更新超时!");
        }
        Assert.assertTrue( smallSheetHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError,"服务器返回的错误码不正确, 错误码：" + smallSheetHttpBO.getHttpEvent().getLastErrorCode());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
            couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}
