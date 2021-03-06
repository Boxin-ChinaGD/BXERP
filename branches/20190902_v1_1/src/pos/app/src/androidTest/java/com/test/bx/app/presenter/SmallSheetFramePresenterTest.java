package com.test.bx.app.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class

SmallSheetFramePresenterTest extends BaseAndroidTestCase {

    private static SmallSheetFramePresenter smallSheetFramePresenter;
    private static SmallSheetTextPresenter smallSheetTextPresenter;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent;

    private static final int Event_ID_SmallSheetFramePresenterTest = 10000;

    public SmallSheetFramePresenterTest() throws CloneNotSupportedException {
    }

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(Event_ID_SmallSheetFramePresenterTest);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        if (event.getId() == Event_ID_SmallSheetFramePresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "????????????Event???ID=" + event.getId());
        }
    }

    public static class DataInput {
        private static SmallSheetFrame frameInput = null;

        protected static final SmallSheetFrame getSmallSheetFrame() throws CloneNotSupportedException {
            frameInput = new SmallSheetFrame();
            frameInput.setLogo("smallSheetFrameLOGO");
            frameInput.setDelimiterToRepeat("-");
            List<SmallSheetText> textList = new ArrayList<>();
            for (int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
                SmallSheetText textInput = new SmallSheetText();
                textInput = new SmallSheetText();
                textInput.setContent("          " + "BoXin??????POS???");
                textInput.setSize(25f);
                textInput.setBold(0);
                textInput.setGravity(17);
                textInput.setFrameId(20l);
                textList.add((SmallSheetText) textInput.clone());
            }
            frameInput.setListSlave1(textList);
            return (SmallSheetFrame) frameInput.clone();
        }

        protected static final List<BaseModel> getSmallSheetFrameList() throws CloneNotSupportedException {
            List<BaseModel> frameList = new ArrayList<BaseModel>();
            for (int i = 0; i < 10; i++) {
                frameList.add(getSmallSheetFrame());
            }
            return frameList;
        }
    }

    @Test
    public void test_a_getMaxId() throws CloneNotSupportedException {
        SmallSheetFrame frame = DataInput.getSmallSheetFrame();
        frame = (SmallSheetFrame) smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frame);
        Assert.assertTrue("?????????????????????" + smallSheetFramePresenter.getLastErrorCode() + "," + smallSheetFramePresenter.getLastErrorMessage(), smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        long maxID = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);

        Assert.assertTrue("???????????????ID????????????", frame.getID() == (maxID - 1));
    }

    @Test
    public void test_b_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> frameList = DataInput.getSmallSheetFrameList();
        smallSheetFramePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList);
        for (BaseModel baseModel : frameList) {
            SmallSheetFrame ssf = (SmallSheetFrame) baseModel;
            if (ssf.getListSlave1() != null) {
                for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
                    smallSheetText.setFrameId(ssf.getID());
                    smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                    Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
            }
        }
        Assert.assertTrue("CreateNSync????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < frameList.size(); i++) {
            SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList.get(i));
            if (frame1Rerieve1 != null) {
                frame1Rerieve1.setSql("Where F_FrameID = ?");
                frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
                List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
                frame1Rerieve1.setListSlave1(list);
            }
            Assert.assertTrue("CreateNSync????????????,??????:??????????????????????????????????????????!", frameList.get(i).compareTo(frame1Rerieve1) == 0);
        }
    }

    @Test
    public void test_c_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bmCreateSync = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync ????????????,?????????TextList???????????????", bmCreateSync.getListSlave1() != null && bmCreateSync.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) bmCreateSync.getListSlave1()) {
            smallSheetText.setFrameId(bmCreateSync.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //
        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve?????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve?????????????????????????????????????????????smallsheetFrame??????", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????", list.size() > 0);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", bmCreateSync.compareTo(frame1Retrieve1) == 0);

        //??????case: ????????????ID
        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        ssf.setID(frame1Retrieve1.getID());
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);

        Assert.assertTrue("CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync????????????,?????????TextList???????????????", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve?????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve?????????????????????????????????????????????smallsheetFrame??????", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????", list.size() > 0);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", ssf.compareTo(frame1Retrieve1) == 0);

        //???????????????case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        bm1.setLogo("updateLOGO");
        bm1.setListSlave1(list);
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("UpdateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1ObjectSync ?????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync????????????????????????", frame1Rerieve1 != null);

        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> sstList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync??????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync????????????????????????", sstList.size() > 0);
        frame1Rerieve1.setListSlave1(sstList);

        //
        Assert.assertTrue("UpdateSync????????????,??????:??????????????????????????????!", bm1.getLogo().equals(frame1Rerieve1.getLogo()));

        //??????case: update?????????ID?????????
        SmallSheetFrame bm2 = DataInput.getSmallSheetFrame();
        bm2.setLogo("updateLOGO2");
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("UpdateSync????????????", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("RetrieveNSync??????????????????????????????>=0!", frameList.size() >= 0);

        //?????????????????? //...????????????case??????????????????????????????
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_LOGO = ?");
        ssf.setConditions(new String[]{"smallSheetFrameLOGO"});
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("????????????RetrieveNSync??????????????????????????????>=0!", frameList.size() >= 0);

        //?????????case,????????????????????????
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_LOGO = ?");
        ssf2.setConditions(new String[]{"smallSheetFrameLOGO", "1"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf2);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case,????????????????????????
        SmallSheetFrame ssf3 = new SmallSheetFrame();
        ssf3.setSql("where F_ID = ? and F_LOGO = ?");
        ssf3.setConditions(new String[]{"smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf3);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????case,????????????????????????
        SmallSheetFrame ssf4 = new SmallSheetFrame();
        ssf4.setSql("where F_ID = ? and F_LOGO = ?");
        ssf4.setConditions(new String[]{"1", "smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf4);
        Assert.assertTrue("???????????? RetrieveNSync ????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync????????????,?????????TextList???????????????", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve?????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve?????????????????????????????????????????????smallsheetFrame??????", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????", list.size() > 0);
        //
        Assert.assertTrue("CreateSync????????????,??????:???????????????????????????????????????!", ssf.compareTo(frame1Retrieve1) == 0);

        //???????????????case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //?????????case??????????????????ID??????
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(Shared.SQLITE_ID_Infinite);
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("Retrieve1 ????????????,??????????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Retrieve1 ????????????,?????????basemodel??????null", ssf3 == null);
    }

    @Test
    public void test_g_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync????????????,?????????TextList???????????????", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve?????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve?????????????????????????????????????????????smallsheetFrame??????", frame1Retrieve1 != null);

        //???????????????case
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(ssf.getID());
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("DeleteSync ????????????,??????????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("retrieve??????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????", ssf3 == null);

        //????????????ID??????????????????
        SmallSheetFrame ssf4 = DataInput.getSmallSheetFrame();
        ssf4.setID(Shared.SQLITE_ID_Infinite);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue("DeleteSync ????????????,??????????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //???????????????ID???null
        ssf4.setID(null);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue("DeleteSync ????????????,??????????????????????????????!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_h_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(bm1.getID());
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        Assert.assertTrue("Create????????????????????????EC_NoError???Create??????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????????????????Null???", bm1 != null);

        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1ObjectSync???????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync?????????????????????", ssf != null);
        SmallSheetFrame frame1Rerieve1 = ssf;
        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync???????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", list != null && list.size() > 0);
        frame1Rerieve1.setListSlave1(list);

        Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm1.compareTo(ssf) == 0);


        //Case: ????????????
        SmallSheetFrame bm4 = DataInput.getSmallSheetFrame();
        bm4.setID(bm1.getID());
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm4, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_i_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("CreateSync bm1????????????,??????:????????????????????????!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        for (int i = 0; i < bm1.getListSlave1().size(); i++) {
            SmallSheetText sheetText = (SmallSheetText) bm1.getListSlave1().get(i);
            sheetText.setFrameId(bm1.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sheetText);
        }

        //Case: ??????case
        bm1.setLogo("UpdateAsyncLOGO1");
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_UpdateAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("UpdateAsync?????????", false);
        }
        Assert.assertTrue("UpdateAsync?????????????????????" + smallSheetSQLiteEvent.getLastErrorCode(), smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame bmUpdate = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Update bm1????????????????????????EC_NoError???Update??????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("??????????????????????????????????????????????????????????????????", bm1.getLogo().equals(bmUpdate.getLogo()));

        //Case: update???ID?????????
        SmallSheetFrame bm2 = DataInput.getSmallSheetFrame();
        bm2.setLogo("UpdateAsyncLOGO2");
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_UpdateAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.updateObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm2, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("UpdateAsync?????????", false);
        }
        Assert.assertTrue("UpdateAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: ??????case 1
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame sheetFrame = new SmallSheetFrame();
        sheetFrame.setPageIndex("1");
        sheetFrame.setPageSize("10");
        smallSheetFramePresenter.retrieveNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, null, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("RetrieveNAsync?????????", false);
        }
        Assert.assertTrue("RetrieveNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", smallSheetSQLiteEvent.getListMasterTable().size() > 0);

        //????????????N???CASE_SmallSheetFrame_RetrieveNByConditions???case
        //...

        //Case: ??????case 2
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_Logo = ?");
        ssf.setConditions(new String[]{"smallSheetFrameLOGO"});
        ssf.setPageIndex("1");
        ssf.setPageSize("10");
        smallSheetFramePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("RetrieveNAsync?????????", false);
        }
        Assert.assertTrue("RetrieveNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", smallSheetSQLiteEvent.getListMasterTable().size() > 0);


        //??????Case:????????????????????????
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_Logo = ?");
        ssf2.setConditions(new String[]{"smallSheetFrameLOGO", "asdasd"});
        ssf2.setPageIndex("1");
        ssf2.setPageSize("10");
        smallSheetFramePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf2, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("RetrieveNAsync?????????", false);
        }
        Assert.assertTrue("RetrieveNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //case:????????????????????????
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf3 = new SmallSheetFrame();
        ssf3.setSql("where F_Logo = ? and F_ID");
        ssf3.setConditions(new String[]{"smallSheetFrameLOGO"});
        ssf3.setPageIndex("1");
        ssf3.setPageSize("10");
        smallSheetFramePresenter.retrieveNObjectAsync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf3, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("RetrieveNAsync?????????", false);
        }
        Assert.assertTrue("RetrieveNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync????????????????????????????????????", smallSheetSQLiteEvent.getListMasterTable().size() >= 0);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //?????????Case
        List<BaseModel> frameList = DataInput.getSmallSheetFrameList();
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, frameList, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("CreateNAsync?????????", false);
        }
        Assert.assertTrue("CreateNAsync???????????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (BaseModel baseModel : frameList) {
            SmallSheetFrame smallSheetFrame = (SmallSheetFrame) baseModel;
            if (smallSheetFrame.getListSlave1() != null) {
                for (SmallSheetText smallSheetText : (List<SmallSheetText>) smallSheetFrame.getListSlave1()) {
                    smallSheetText.setFrameId(smallSheetFrame.getID());
                    smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                    Assert.assertTrue("createObjectSync???????????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
            }
        }
        //
        for (int i = 0; i < frameList.size(); i++) {
            BaseModel baseModel = smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList.get(i));
            if (baseModel != null) {
                SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) baseModel;
                frame1Rerieve1.setSql("Where F_FrameID = ?");
                frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
                List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
                frame1Rerieve1.setListSlave1(list);
            }
            Assert.assertTrue("CreateNAsync???????????????????????????", frameList.get(i).compareTo(baseModel) == 0);
        }

        //???????????????case
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createNObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, frameList, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        Assert.assertTrue("createNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_l_createReplacerAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????????????????????????????
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //???????????????ID???????????????ID??????FrameOld???????????????
        long maxIDInFrame = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println("SmallSheetFrame??????ID=" + maxIDInFrame);
        Assert.assertTrue("presenter.getMaxId()?????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxIDInText = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println("SmallSheetText??????ID=" + maxIDInText);
        Assert.assertTrue("smallSheetTextPresenter.getMaxId()?????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //??????Frameold
        SmallSheetFrame frameOld = DataInput.getSmallSheetFrame();
        frameOld.setID(maxIDInFrame);
        List<SmallSheetText> textListOld = new ArrayList<>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            textListOld.add((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
        }
        for (int i = 0; i < textListOld.size(); i++) {
            textListOld.get(i).setID(maxIDInText + i);
            textListOld.get(i).setFrameId(frameOld.getID());
        }
        frameOld.setListSlave1(textListOld);
        //???????????????
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue("presenter.createObjectSync()?????????" + smallSheetFramePresenter.getLastErrorMessage(), smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        smallSheetTextPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, textListOld);
        Assert.assertTrue("smallSheetTextPresenter.createNObjectSync()?????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //??????FrameNew
        SmallSheetFrame frameNew = DataInput.getSmallSheetFrame();
        frameNew.setLogo("frameNew--aaaaaaaaaaaaddddddddddddddddd");
        long lIDFromServerDB = 1999;
        frameNew.setID(lIDFromServerDB);
        List<SmallSheetText> textListNew = new ArrayList<>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            SmallSheetText textModel = ((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
            textModel.setFrameId(frameNew.getID());
            textListNew.add(textModel);
        }
        frameNew.setListSlave1(textListNew);
        //?????????????????????Case
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, frameOld, frameNew, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createReplacerAsync?????????", false);
        }
        Assert.assertTrue("CreateReplacerAsync??????????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame oldFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue("retrieve1ObjectSync??????????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync??????????????????!", oldFrame == null);
        //
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_FrameID = ?");
        ssf.setConditions(new String[]{frameOld.getID() + ""});
        List<SmallSheetText> oldTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf);
        Assert.assertTrue("????????????", oldFrame == null && oldTextList.size() == 0);
        //
        SmallSheetFrame newFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_FrameID = ?");
        ssf2.setConditions(new String[]{String.valueOf(frameNew.getID())});
        List<SmallSheetText> newTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf2);
        Assert.assertTrue("????????????", newFrame != null && newTextList.size() > 0);

        //??????case??????????????????????????????
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateReplacerAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetFramePresenter.createReplacerObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, frameNew, null, smallSheetSQLiteEvent);
        lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createReplacerAsync?????????", false);
        }
        Assert.assertTrue("CreateReplacerAsync??????????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        Assert.assertTrue("retrieve1ObjectSync??????????????????????????????", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync??????????????????!", ssf3 == null);
    }

    @Test
    public void test_m_createMasterSlaveAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //???????????????????????????????????????ID???????????????????????????????????????
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //????????????
        SmallSheetFrame preFrame = DataInput.getSmallSheetFrame();
        preFrame.setID(smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime));
        List<SmallSheetText> preTextList = new ArrayList<SmallSheetText>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            SmallSheetText textModel = ((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
            textModel.setFrameId(preFrame.getID());
            preTextList.add(textModel);
        }
        preFrame.setListSlave1(preTextList);
        //
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        smallSheetSQLiteEvent.setHttpBO(null);
        smallSheetFramePresenter.createMasterSlaveObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, preFrame, smallSheetSQLiteEvent);
        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            Assert.assertTrue("createMasterSlaveAsync?????????", false);
        }
        Assert.assertTrue("createMasterSlaveObjectAsyn????????????????????????!", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame frame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, preFrame);
        Assert.assertTrue("rerieve1?????????????????????", frame != null);
        Assert.assertTrue("???????????????????????????frame?????????!", frame.compareTo(preFrame) == 0);
        SmallSheetFrame frame1Rerieve1 = frame;
        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) SmallSheetFramePresenterTest.smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync?????????????????????!", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", list != null && list.size() > 0);
        frame1Rerieve1.setListSlave1(list);
        //
        frame.setSql("where F_FrameID = ?");
        frame.setConditions(new String[]{String.valueOf(frame.getID())});
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame);
        Assert.assertTrue("retrieveNObjectSync????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync?????????????????????", textList != null && textList.size() > 0);
        for (int i = 0; i < textList.size(); i++) {
            Assert.assertTrue("???????????????????????????text?????????!", preTextList.get(i).compareTo(textList.get(i)) == 0);
        }
    }

    @Test
    public void test_n_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Thread.sleep(10000); //??????????????????
        //???????????????????????????????????????????????????????????????
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_CreateAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFramePresenter.bInTestMode = true;
        smallSheetFramePresenter.createObjectAsync(BaseSQLiteBO.INVALID_CASE_ID, bm1, smallSheetSQLiteEvent);

        long lTimeOut = UNIT_TEST_TimeOut;
        while (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&//
                smallSheetSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            Assert.assertTrue("createNAsync?????????", false);
        }
        // ??????????????????????????????
        SmallSheetFramePresenter.bInTestMode = false;
        wifiManager.setWifiEnabled(true);
        Thread.sleep(10000); //??????????????????
        Assert.assertTrue("createNAsync?????????????????????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(bm1.getID());
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue("smallSheetText Create???????????????????????????", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        Assert.assertTrue("Create????????????????????????EC_NoError???Create??????", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("?????????????????????????????????Null???", bm1 != null);

        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload,  new SmallSheetFrame());//????????????List
        for (Object o : frameList) {
            SmallSheetFrame ssf = (SmallSheetFrame) o;
            Assert.assertTrue("????????????????????????????????????????????????????????????????????????", bm1.compareTo(ssf) != 0);
        }

        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        SmallSheetFrame retailTradeR1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("??????????????????????????????????????????", retailTradeR1 == null);
    }

    /**
     * ????????????smallSheetFrame???????????????
     */
    @Test
    public void test_retrieveNSmallSheetFrame() throws Exception {
        Shared.printTestMethodStartInfo();

        SmallSheetFramePresenter smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        Integer total = smallSheetFramePresenter.retrieveCount();
        System.out.println("smallSheetFrame???????????????" + total);
        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
    }
}
