
package com.test.bx.app;

import android.content.Intent;
import android.os.Message;

import com.base.BaseHttpAndroidTestCase;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CouponHttpBO;
import com.bx.erp.bo.CouponSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BarcodesHttpEvent;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BrandHttpEvent;
import com.bx.erp.event.CommodityCategoryHttpEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.CouponHttpEvent;
import com.bx.erp.event.CouponSQLiteEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PackageUnitHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.PromotionHttpEvent;
import com.bx.erp.event.PromotionSQLiteEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BarcodesSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.BrandSQLiteEvent;
import com.bx.erp.event.UI.CommodityCategorySQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.PackageUnitSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.helper.Configuration;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.ResetBaseDataUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.view.activity.Base1FragmentActivity;
import com.bx.erp.view.activity.BaseActivity;
import com.bx.erp.view.activity.SmallSheet1Activity;
import com.bx.erp.view.activity.SyncData1Activity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.bx.erp.view.activity.SmallSheetActivity;

public class SmallSheetJUnit extends BaseHttpAndroidTestCase {
    /**
     * ??????????????????????????????????????????ID
     */
    private static SmallSheetFrame createdFrame = null;

    /**
     * retrieveN????????????????????????????????????,????????????????????????
     */
    private static SmallSheetFrame updatedFrame = null;

    private static SmallSheetFramePresenter framePresenter = null;
    private static SmallSheetTextPresenter textPresenter = null;

    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    private static SmallSheetHttpEvent smallSheetHttpEvent;
    private static PosLoginHttpBO posLoginHttpBO = null;
    private static PosLoginHttpEvent posLoginHttpEvent = null;
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
    public static CouponSQLiteBO couponSQLiteBO;
    public static CouponSQLiteEvent couponSQLiteEvent;
    public static CouponHttpEvent couponHttpEvent;
    public static CouponHttpBO couponHttpBO;

    private static final int EVENT_ID_SmallSheetJUnit = 10000;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesHttpEvent(BarcodesHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBarcodesSQLiteEvent(BarcodesSQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandHttpEvent(BrandHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBrandSQLiteEvent(BrandSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategoryHttpEvent(CommodityCategoryHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityCategorySQLiteEvent(CommodityCategorySQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackageUnitHttpEvent(PackageUnitHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPackaheUnitSQLiteEvent(PackageUnitSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommodityHttpEvent(CommodityHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCommoditySQLiteEvent(CommoditySQLiteEvent event) throws InterruptedException {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPromotionHttpEvent(PromotionHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutHttpEvent(LogoutHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
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
        private static SmallSheetFrame frameInput = null;

        protected static final SmallSheetFrame getSmallSheetFrame(long lFrameMaxIDInSQLite, long lTextMaxIDInSQLite) throws Exception {
            frameInput = new SmallSheetFrame();
            frameInput.setCreateDatetime(new Date());
            frameInput.setID(lFrameMaxIDInSQLite);
            frameInput.setLogo("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
            frameInput.setDelimiterToRepeat("-");
            frameInput.setReturnObject(1);//1, ?????????????????????????????????0????????????????????????????????????
            List<SmallSheetText> list = initSmallSheetText(lFrameMaxIDInSQLite, lTextMaxIDInSQLite);
            frameInput.setListSlave1(list);

            return (SmallSheetFrame) frameInput.clone();
        }

        protected static List<SmallSheetText> initSmallSheetText(long lFrameStartID, long lTextStartID) throws ParseException {
            List<SmallSheetText> list = new ArrayList<SmallSheetText>();

            SmallSheetText smallSheetText = new SmallSheetText();

            smallSheetText.setContent("??????");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(17);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("????????????12345678910");
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

            smallSheetText.setContent("???????????????500.0");
            smallSheetText.setSize(25.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("???????????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("?????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("?????????");
            smallSheetText.setSize(50.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("??????????????????50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("?????????????????????50.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("??????????????????400.0");
            smallSheetText.setSize(20.0f);
            smallSheetText.setBold(0);
            smallSheetText.setGravity(53);
            list.add(smallSheetText);

            smallSheetText = new SmallSheetText();

            smallSheetText.setContent("????????????");
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

            // ???????????????????????????????????????20
//            smallSheetText = new SmallSheetText();
//
//            smallSheetText.setContent("??????POS???????????????");
//            smallSheetText.setSize(30.0f);
//            smallSheetText.setGravity(17);
//            list.add(smallSheetText);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setID(lTextStartID + i);
                list.get(i).setFrameId(lFrameStartID);
                list.get(i).setSyncDatetime(Constants.getDefaultSyncDatetime());
            }

            return list;
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (framePresenter == null) {
            framePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        }
        if (textPresenter == null) {
            textPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        }
        //
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);
        //
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }

        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);

        //
        if (couponSQLiteEvent == null) {
            couponSQLiteEvent = new CouponSQLiteEvent();
            couponSQLiteEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (couponHttpEvent == null) {
            couponHttpEvent = new CouponHttpEvent();
            couponHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
        }
        if (couponHttpBO == null) {
            couponHttpBO = new CouponHttpBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        if (couponSQLiteBO == null) {
            couponSQLiteBO = new CouponSQLiteBO(GlobalController.getInstance().getContext(), couponSQLiteEvent, couponHttpEvent);
        }
        couponHttpEvent.setHttpBO(couponHttpBO);
        couponHttpEvent.setSqliteBO(couponSQLiteBO);
        couponSQLiteEvent.setHttpBO(couponHttpBO);
        couponSQLiteEvent.setSqliteBO(couponSQLiteBO);

        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(EVENT_ID_SmallSheetJUnit);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPosLoginHttpEvent(PosLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStaffLoginHttpEvent(StaffLoginHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
        } else {
            System.out.println("????????????Event???ID=" + event.getId());
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

        //?????????????????????????????????????????????????????????
//        textPresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);
//        framePresenter.deleteNObjectSync(Constants.INVALID_CASE_ID, null);
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //??????????????????????????????????????????????????????
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setID(1l);
        List<BaseModel> list = (List<BaseModel>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("????????????????????????", list != null && list.size() > 0 && framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        String sql = "where F_FrameID = ? ";
        String[] condition = {list.get(0).getID() + ""};
        ssf.setSql(sql);
        ssf.setConditions(condition);
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf);
        Assert.assertTrue("SmallSheetPresenter????????????????????????????????????????????????EC_NoError", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        ssfOld.setListSlave1(textList);
        //
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("???????????????" + smallSheetSQLiteBO.getSqLiteEvent().printErrorInfo(), false);
        }

        //??????????????????
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("???????????????????????????????????????sqlList????????????" + smallSheetSQLiteBO.getSqLiteEvent().getStatus() + "???????????????" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorCode() + ",???????????????" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), false);
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????????????????????????????", master != null);
        //?????????????????????????????????ID????????????FrameID??????????????????????????????        //????????????ssfOld????????????master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue("??????????????????????????????????????????????????????", ssfOld.compareTo(master) == 0);

        createdFrame = master;//???????????????????????????
    }

    @Test
    public void test_a_Create2() throws Exception {
        Shared.printTestMethodStartInfo();
        //?????????????????????

        if (!Shared.login(1, "15016509167", "000000", 0, "668866", posLoginHttpBO, staffLoginHttpBO, 0)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("???????????????", false);
        }
        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }
        Assert.assertTrue(smallSheetSQLiteBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoPermission);
    }

    @Test
    public void test_b_Update() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        if (createdFrame == null) {
            Assert.assertTrue("CREATE????????????????????????????????????frame ID", false);
        }

        //???????????????????????????????????????
        SmallSheetFrame ssfSQLite = (SmallSheetFrame) framePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, createdFrame);
        Assert.assertTrue("retrieve1Sync???????????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        String sql = "where F_FrameID = ? ";
        String[] condition = {ssfSQLite.getID() + ""};
        ssfSQLite.setSql(sql);
        ssfSQLite.setConditions(condition);
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssfSQLite);
        Assert.assertTrue("SmallSheetPresenter????????????????????????????????????????????????EC_NoError", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        if (textList.size() > 0) {
            textList.get(10).setContent("aaaaaaaaaaaaaaa");
        }
        ssfSQLite.setLogo("qqqqqqqqqqqqqqqqqqqqqq");   //??????logo??????
        ssfSQLite.setListSlave1(textList);
        //
        ssfSQLite.setReturnObject(1);//???????????????????????????

        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync);
        if (!smallSheetSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfSQLite)) {
            Assert.assertTrue("???????????????", false);
        }

        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????????????????????????????????????????", master != null);

        //????????????????????????,?????????????????????????????????ssfSQLite????????????master
        Assert.assertTrue("??????????????????????????????????????????????????????", ssfSQLite.compareTo(master) == 0);

        updatedFrame = master;
    }

    @Test
    public void test_c_RetrieveNC() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        ssf.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
        if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, ssf)) {
            Assert.assertTrue("RetrieveN?????????", false);
        }
        //??????????????????
        long lTimeout = 60;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }
        // ????????????SQLite????????????????????????
        List<SmallSheetFrame> frameList1 = new ArrayList<SmallSheetFrame>();
        frameList1 = (List<SmallSheetFrame>) smallSheetSQLiteBO.getHttpEvent().getListMasterTable();
        Assert.assertTrue("?????????????????????????????????SQLite???????????????????????????", frameList1.size() > 0);
    }

    @Test
    public void test_d_Delete() throws Exception {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("???????????????", false);
        }

        //??????????????????
        long lTimeout = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getHttpEvent().getBaseModel1();
        Assert.assertTrue("????????????????????????", master != null);
        // ????????????
        deleteSmallSheetFrameInServerAndSQLite(master);

    }

    @Test
    public void test_e_update() throws InterruptedException, CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //??????case1????????????????????????ID???????????????
        List<SmallSheetFrame> smallSheetFrameList = (List<SmallSheetFrame>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("??????????????????!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame smallSheetFrame = smallSheetFrameList.get(0);
        //
        SmallSheetText text = new SmallSheetText();
        text.setSql("where F_FrameID = ?");
        text.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        text.setFrameId(smallSheetFrame.getID());
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, text);
        Assert.assertTrue("??????????????????!", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        long tmpRowID = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        smallSheetFrame.setID(tmpRowID);
        // ????????????ID??????????????????????????????ID
        for (BaseModel bm : smallSheetTextList) {
            SmallSheetText smallSheetText = (SmallSheetText) bm;
            smallSheetText.setFrameId(tmpRowID);
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
            Assert.assertTrue("????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????, ????????????" + smallSheetHttpEvent.getLastErrorCode(), smallSheetHttpEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoSuchData);
    }

    @Test
    public void test_e_update2() throws Exception {
        Shared.printTestMethodStartInfo();
        // case2???????????????????????????????????????????????????????????????
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        // ???????????????????????????????????????
        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);

        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld)) {
            Assert.assertTrue("???????????????" + smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), false);
        }

        //??????????????????
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("??????????????????????????????", false);
        }

        SmallSheetFrame master = (SmallSheetFrame) smallSheetSQLiteBO.getSqLiteEvent().getBaseModel1();
        Assert.assertTrue("??????????????????????????????", master != null);
        //?????????????????????????????????ID????????????FrameID??????????????????????????????        //????????????ssfOld????????????master
        ssfOld.setIgnoreIDInComparision(true);
        Assert.assertTrue("??????????????????????????????????????????????????????", ssfOld.compareTo(master) == 0);
        // ???ID????????????FrameID?????????
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) master.getListSlave1();
        for (SmallSheetText sst : smallSheetTextList) {
            sst.setFrameId(sst.getFrameId() + 1);
        }
        //
        smallSheetHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        if (smallSheetHttpBO.updateAsync(BaseHttpBO.INVALID_CASE_ID, master)) {
            Assert.assertTrue("??????ID?????????FrameID????????????checkUpdate()???????????????", false);
        }
        // ????????????????????????????????????
        for (SmallSheetText sst : (List<SmallSheetText>) master.getListSlave1()) {
            sst.setFrameId(sst.getFrameId() - 1);
        }
        deleteSmallSheetFrameInServerAndSQLite(master);
    }

    @Test
    public void test_f_delete1() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }

        //case1:?????????????????????ID???????????????
        List<SmallSheetFrame> smallSheetFrameList = (List<SmallSheetFrame>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("??????????????????!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame smallSheetFrame = smallSheetFrameList.get(0);
        //
        SmallSheetText text = new SmallSheetText();
        text.setSql("where F_FrameID = ?");
        text.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        text.setFrameId(smallSheetFrame.getID());
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, text);
        Assert.assertTrue("??????????????????!", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        long tmpRowID = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        smallSheetFrame.setID(tmpRowID);
        smallSheetFrame.setListSlave1(smallSheetFrameList);
        //
        smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, smallSheetFrame);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue("????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????, ????????????" + smallSheetHttpBO.getHttpEvent().getLastErrorCode(), smallSheetHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f_uploadDuplicatedSmallSheetFrame() throws Exception {
        Shared.printTestMethodStartInfo();
        // case:????????????????????????????????????????????????????????????pos????????????????????????????????????????????????Pos????????????????????????????????????Pos?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Pos???

        // ??????
        if (!Shared.login(1, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        // ??????????????????
        Long maxFrameIDInSQLite = framePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxTextIDInSQLite = textPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        Assert.assertTrue("?????????????????????", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame ssfOld = DataInput.getSmallSheetFrame(maxFrameIDInSQLite, maxTextIDInSQLite);
        //
        SmallSheetFrame ssfInSQLite = (SmallSheetFrame) framePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssfOld);
        Assert.assertTrue("????????????????????????????????????", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && ssfInSQLite != null);
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        smallSheetSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadSmallSheetTempData(ssfInSQLite, smallSheetSQLiteBO);
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((smallSheetSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//????????????Presenter???????????????????????????event????????????????????????????????????????????????????????????
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
            Assert.assertTrue("????????????", false);
        }
        if (smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("????????????", false);
        }
        // ????????????
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        Shared.uploadSmallSheetTempData(ssfInSQLite, smallSheetSQLiteBO);
        //
        lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((smallSheetSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//????????????Presenter???????????????????????????event????????????????????????????????????????????????????????????
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
            Assert.assertTrue("????????????", false);
        }
        if (smallSheetHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) { // ???????????????????????????JSONObject??????EC_OtherError
            Assert.assertTrue("????????????????????????????????????????????????????????????EC_Duplicated:" + smallSheetHttpEvent.getLastErrorCode(), false);
        }
        // nbr??????????????????????????????sqlite??????
//        if (framePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_OtherError) {
//            Assert.assertTrue("?????????????????????SQLite?????????????????????", false);
//        }
//
//        // ??????????????????????????????
//        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetHttpEvent.getBaseModel1();
//        ssfInSQLite.setIgnoreIDInComparision(true);
//        // ?????????????????????????????????ID????????????FrameID??????????????????????????????        //????????????ssfInSQLite????????????ssf
//        Assert.assertTrue("?????????????????????????????????????????????", ssfInSQLite.compareTo(ssf) == 0);
        // ????????????????????????????????????
        deleteSmallSheetFrameInServerAndSQLite(ssfInSQLite);
    }

    /**
     * ?????????case???????????????Event??????????????????case????????????
     * 1.????????????SQLite???SmallSheet Table
     * 2.????????????????????????
     * 3.??????????????????
     * 4.??????????????????????????????nbr?????????????????????
     */
    @Test
    public void test_g_testLoad() throws Exception {
        Shared.printTestMethodStartInfo();

        // ????????????
        textPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        framePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        //
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync SmallSheetFrameList??????????????????????????????=0!", frameList.size() == 0);
        //
        List<SmallSheetText> textList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync SmallSheetTextList??????????????????????????????=0!", textList.size() == 0);
        //
        if (!Shared.login(1l, posLoginHttpBO, staffLoginHttpBO)) {
            Assert.assertTrue("????????????????????????=" + Configuration.HTTP_IP + "\t" + posLoginHttpBO.getHttpEvent().printErrorInfo() + "\t" + staffLoginHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        //
        ResetBaseDataUtil resetBaseDataUtil = new ResetBaseDataUtil(EVENT_ID_SmallSheetJUnit);
        try {
            resetBaseDataUtil.ResetBaseData(false);
            // ??????????????????onEvent??????????????????????????????????????????????????????????????????EventStatus
            long lTimeout = Shared.UNIT_TEST_TimeOut;
            while (couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done
                    && couponSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction
                    && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (couponSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
                Assert.assertTrue("????????????!", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("??????????????????:" + e.getMessage());
            Assert.assertTrue("?????????????????????", false);
        }
        //
        List<SmallSheetFrame> frameList2 = (List<SmallSheetFrame>) framePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", framePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync SmallSheetFrameList??????????????????????????????>0!", frameList2.size() > 0);
        SmallSheetFrame retrieveNSmallSheetFram = new SmallSheetFrame();
        for (SmallSheetFrame smallSheetFrame : frameList2) {
            if (smallSheetFrame.getID() == SmallSheet1Activity.Default_SmallSheetID_INPos) { // ??????mysql?????????frameId???1????????????????????????case????????????
                retrieveNSmallSheetFram = smallSheetFrame;
                break;
            }
        }
        //
        SmallSheetText sst = new SmallSheetText();
        sst.setSql("where F_FrameID = ?");
        sst.setConditions(new String[]{String.valueOf(retrieveNSmallSheetFram.getID())});
        sst.setFrameId(retrieveNSmallSheetFram.getID());
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) textPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", textPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("RetrieveNSync SmallSheetTextList??????????????????????????????=20!?????????" + smallSheetTextList.size() + "?????????", smallSheetTextList.size() == SmallSheet1Activity.Default_SmallSheetListSlveSize);
    }

    protected void deleteSmallSheetFrameInServerAndSQLite(SmallSheetFrame ssf) throws Exception {
        smallSheetHttpBO.deleteAsync(BaseHttpBO.INVALID_CASE_ID, ssf);
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (smallSheetHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetHttpBO.getHttpEvent().getStatus() == BaseEvent.EnumEventStatus.EES_Http_ToDo) {
            Assert.assertTrue("????????????!", false);
        }
        Assert.assertTrue("????????????????????????????????????, ????????????" + smallSheetHttpBO.getHttpEvent().getLastErrorCode(), smallSheetHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponHttpEvent(CouponHttpEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                event.onEvent();
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCouponSQLiteEvent(CouponSQLiteEvent event) {
        if (event.getId() == EVENT_ID_SmallSheetJUnit) {
            event.onEvent();
            couponSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }
}
