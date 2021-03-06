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
import wpos.event.UI.SmallSheetSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.SmallSheetFrame;
import wpos.model.SmallSheetText;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;

import static wpos.utils.Shared.UNIT_TEST_TimeOut;

public class SmallSheetFramePresenterTest extends BasePresenterTest {

//    private static SmallSheetFramePresenter smallSheetFramePresenter;
//    private static SmallSheetTextPresenter smallSheetTextPresenter;
//    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent;

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

    @BeforeClass
    public void setUp() {
        super.setUp();
        smallSheetSQLiteEvent.setId(Event_ID_SmallSheetFramePresenterTest);

        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "?????????????????????" + smallSheetFramePresenter.getLastErrorCode() + "," + smallSheetFramePresenter.getLastErrorMessage());

        long maxID = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);

        Assert.assertTrue(frame.getID() == (maxID - 1), "???????????????ID????????????");
    }

    @Test
    public void test_b_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> frameList = DataInput.getSmallSheetFrameList();
        List<SmallSheetFrame> smallSheetFrames = (List<SmallSheetFrame>) smallSheetFramePresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList);
        for (BaseModel bm : frameList) {
            SmallSheetFrame ssf = (SmallSheetFrame) bm;
            if (ssf.getListSlave1() != null) {
                for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
                    smallSheetText.setFrameId(Long.valueOf(ssf.getID()));
                    smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                    Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
                }
            }
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync????????????,??????:????????????????????????!");
        //
        for (int i = 0; i < frameList.size(); i++) {
            SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList.get(i));
            if (frame1Rerieve1 != null) {
                frame1Rerieve1.setSql("Where F_FrameID = '%s'");
                frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
                List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
                frame1Rerieve1.setListSlave1(list);
            }
            Assert.assertTrue(frameList.get(i).compareTo(frame1Rerieve1) == 0, "CreateNSync????????????,??????:??????????????????????????????????????????!");
        }
    }

    @Test
    public void test_c_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bmCreateSync = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(bmCreateSync.getListSlave1() != null && bmCreateSync.getListSlave1().size() > 0, "CreateSync ????????????,?????????TextList???????????????");

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) bmCreateSync.getListSlave1()) {
            smallSheetText.setFrameId(Long.valueOf(bmCreateSync.getID()));
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        //
        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve?????????????????????");
        Assert.assertTrue(frame1Retrieve1 != null, "retrieve?????????????????????????????????????????????smallsheetFrame??????");

        frame1Retrieve1.setSql("Where F_FrameID = '%s'");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(list.size() > 0, "retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????");
        //
        Assert.assertTrue(bmCreateSync.compareTo(frame1Retrieve1) == 0, "CreateSync????????????,??????:???????????????????????????????????????!");

        //??????case: ????????????ID
        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        ssf.setID(frame1Retrieve1.getID());
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);

        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1??????ID????????????,??????:???????????????????????????EC_NoError!");
    }

    @Test
    public void test_d_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0, "CreateSync????????????,?????????TextList???????????????");

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(Long.valueOf(ssf.getID()));
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve?????????????????????");
        Assert.assertTrue(frame1Retrieve1 != null, "retrieve?????????????????????????????????????????????smallsheetFrame??????");

        frame1Retrieve1.setSql("Where F_FrameID = '%s'");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(list.size() > 0, "retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????");
        //
        Assert.assertTrue(ssf.compareTo(frame1Retrieve1) == 0, "CreateSync????????????,??????:???????????????????????????????????????!");

        //???????????????case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        bm1.setLogo("updateLOGO");
        bm1.setListSlave1(list);
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1????????????,??????:????????????????????????!");
        //
        SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync ?????????");
        Assert.assertTrue(frame1Rerieve1 != null, "retrieve1ObjectSync????????????????????????");

        frame1Rerieve1.setSql("Where F_FrameID = '%s'");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> sstList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync??????");
        Assert.assertTrue(sstList.size() > 0, "retrieveNObjectSync????????????????????????");
        frame1Rerieve1.setListSlave1(sstList);

        //
        Assert.assertTrue(bm1.getLogo().equals(frame1Rerieve1.getLogo()), "UpdateSync????????????,??????:??????????????????????????????!");

        //??????case: update?????????ID?????????
        SmallSheetFrame bm2 = DataInput.getSmallSheetFrame();
        bm2.setLogo("updateLOGO2");
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync????????????");
    }

    @Test
    public void test_e_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync????????????,??????:????????????????????????!");
        //
        Assert.assertTrue(frameList.size() >= 0, "RetrieveNSync??????????????????????????????>=0!");

        //?????????????????? //...????????????case??????????????????????????????
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_LOGO = '%s'");
        ssf.setConditions(new String[]{"smallSheetFrameLOGO"});
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
        //
        Assert.assertTrue(frameList.size() >= 0, "????????????RetrieveNSync??????????????????????????????>=0!");

        //?????????case,????????????????????????
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_LOGO = '%s'");
        ssf2.setConditions(new String[]{"smallSheetFrameLOGO", "1"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf2);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");

        //??????case,????????????????????????
        SmallSheetFrame ssf3 = new SmallSheetFrame();
        ssf3.setSql("where F_ID = '%s' and F_LOGO = '%s'");
        ssf3.setConditions(new String[]{"smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf3);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");

        //??????case,????????????????????????
        SmallSheetFrame ssf4 = new SmallSheetFrame();
        ssf4.setSql("where F_ID = '%s' and F_LOGO = '%s'");
        ssf4.setConditions(new String[]{"1", "smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf4);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "???????????? RetrieveNSync ????????????,??????:????????????????????????!");
    }

    @Test
    public void test_f_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0, "CreateSync????????????,?????????TextList???????????????");

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(Long.valueOf(ssf.getID()));
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve?????????????????????");
        Assert.assertTrue(frame1Retrieve1 != null, "retrieve?????????????????????????????????????????????smallsheetFrame??????");

        frame1Retrieve1.setSql("Where F_FrameID = '%s'");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????");
        Assert.assertTrue(list.size() > 0, "retrieveNObjectSync?????????????????????????????????????????????List<SmallSheetText>??????");
        //
        Assert.assertTrue(ssf.compareTo(frame1Retrieve1) == 0, "CreateSync????????????,??????:???????????????????????????????????????!");

        //???????????????case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 ????????????,??????????????????????????????!");

        //?????????case??????????????????ID??????
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(Shared.SQLITE_ID_Infinite);
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 ????????????,??????????????????????????????!");
        Assert.assertTrue(ssf3 == null, "Retrieve1 ????????????,?????????basemodel??????null");
    }

    @Test
    public void test_g_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0, "CreateSync????????????,?????????TextList???????????????");

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(Long.valueOf(ssf.getID()));
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve?????????????????????");
        Assert.assertTrue(frame1Retrieve1 != null, "retrieve?????????????????????????????????????????????smallsheetFrame??????");

        //???????????????case
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(ssf.getID());
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync ????????????,??????????????????????????????!");
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve??????????????????");
        Assert.assertTrue(ssf3 == null, "?????????????????????");

        //????????????ID??????????????????
        SmallSheetFrame ssf4 = DataInput.getSmallSheetFrame();
        ssf4.setID(Shared.SQLITE_ID_Infinite);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync ????????????,??????????????????????????????!");

        //???????????????ID???null
        ssf4.setID(null);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync ????????????,??????????????????????????????!");
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
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(Long.valueOf(bm1.getID()));
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
            }
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Create????????????????????????EC_NoError???Create??????");
        Assert.assertTrue(bm1 != null, "?????????????????????????????????Null???");

        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync???????????????????????????");
        Assert.assertTrue(ssf != null, "retrieve1ObjectSync?????????????????????");
        SmallSheetFrame frame1Rerieve1 = ssf;
        frame1Rerieve1.setSql("Where F_FrameID = '%s'");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync???????????????????????????");
        Assert.assertTrue(list != null && list.size() > 0, "retrieveNObjectSync?????????????????????");
        frame1Rerieve1.setListSlave1(list);

        Assert.assertTrue(bm1.compareTo(ssf) == 0, "??????????????????????????????????????????????????????????????????");


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
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
    }

    @Test
    public void test_i_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1????????????,??????:????????????????????????!");

        for (int i = 0; i < bm1.getListSlave1().size(); i++) {
            SmallSheetText sheetText = (SmallSheetText) bm1.getListSlave1().get(i);
            sheetText.setFrameId(Long.valueOf(bm1.getID()));
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
            Assert.assertTrue(false, "UpdateAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateAsync?????????????????????" + smallSheetSQLiteEvent.getLastErrorCode());
        SmallSheetFrame bmUpdate = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Update bm1????????????????????????EC_NoError???Update??????");
        Assert.assertTrue(bm1.getLogo().equals(bmUpdate.getLogo()), "??????????????????????????????????????????????????????????????????");

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
            Assert.assertTrue(false, "UpdateAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "UpdateAsync?????????????????????");
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
            Assert.assertTrue(false, "RetrieveNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNAsync?????????????????????");
        Assert.assertTrue(smallSheetSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync????????????????????????????????????");

        //????????????N???CASE_SmallSheetFrame_RetrieveNByConditions???case
        //...

        //Case: ??????case 2
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_Logo = '%s'");
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
            Assert.assertTrue(false, "RetrieveNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNAsync?????????????????????");
        Assert.assertTrue(smallSheetSQLiteEvent.getListMasterTable().size() > 0, "retrieveNAsync????????????????????????????????????");


        //??????Case:????????????????????????
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_Logo = '%s'");
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
            Assert.assertTrue(false, "RetrieveNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNAsync?????????????????????");

        //case:????????????????????????
        smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheetFrame_RetrieveNAsync);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        SmallSheetFrame ssf3 = new SmallSheetFrame();
        ssf3.setSql("where F_Logo = '%s' and F_ID");
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
            Assert.assertTrue(false, "RetrieveNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNAsync?????????????????????");
        Assert.assertTrue(smallSheetSQLiteEvent.getListMasterTable().size() >= 0, "retrieveNAsync????????????????????????????????????");
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
            Assert.assertTrue(false, "CreateNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNAsync???????????????????????????");
        //
        for (BaseModel baseModel : frameList) {
            SmallSheetFrame smallSheetFrame = (SmallSheetFrame) baseModel;
            if (smallSheetFrame.getListSlave1() != null) {
                for (SmallSheetText smallSheetText : (List<SmallSheetText>) smallSheetFrame.getListSlave1()) {
                    smallSheetText.setFrameId(Long.valueOf(smallSheetFrame.getID()));
                    smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                    Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createObjectSync???????????????????????????");
                }
            }
        }
        //
        for (int i = 0; i < frameList.size(); i++) {
            BaseModel baseModel = smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList.get(i));
            if (baseModel != null) {
                SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) baseModel;
                frame1Rerieve1.setSql("Where F_FrameID = '%s'");
                frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
                List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
                frame1Rerieve1.setListSlave1(list);
            }
            Assert.assertTrue(frameList.get(i).compareTo(baseModel) == 0, "CreateNAsync???????????????????????????");
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
            Assert.assertTrue(false, "createNAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");
    }

    @Test
    public void test_l_createReplacerAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //??????????????????????????????
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //???????????????ID???????????????ID??????FrameOld???????????????
        int maxIDInFrame = (int) smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class);
        System.out.println("SmallSheetFrame??????ID=" + maxIDInFrame);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "presenter.getMaxId()?????????");
        int maxIDInText = (int) smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);
        System.out.println("SmallSheetText??????ID=" + maxIDInText);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetTextPresenter.getMaxId()?????????");

        //??????Frameold
        SmallSheetFrame frameOld = DataInput.getSmallSheetFrame();
        frameOld.setID(maxIDInFrame);
        List<SmallSheetText> textListOld = new ArrayList<SmallSheetText>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            textListOld.add((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
        }
        for (int i = 0; i < textListOld.size(); i++) {
            textListOld.get(i).setID(maxIDInText + i);
            textListOld.get(i).setFrameId(Long.valueOf(frameOld.getID()));
        }
        frameOld.setListSlave1(textListOld);
        //???????????????
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "presenter.createObjectSync()?????????" + smallSheetFramePresenter.getLastErrorMessage());
        smallSheetTextPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, textListOld);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetTextPresenter.createNObjectSync()?????????");
        //??????FrameNew
        SmallSheetFrame frameNew = DataInput.getSmallSheetFrame();
        frameNew.setLogo("frameNew--aaaaaaaaaaaaddddddddddddddddd");
        int lIDFromServerDB = 1999;
        frameNew.setID(lIDFromServerDB);
        List<SmallSheetText> textListNew = new ArrayList<SmallSheetText>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            SmallSheetText textModel = ((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
            textModel.setFrameId(Long.valueOf(frameNew.getID()));
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
            Assert.assertTrue(false, "createReplacerAsync?????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateReplacerAsync??????????????????????????????");
        //
        SmallSheetFrame oldFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync??????????????????????????????");
        Assert.assertTrue(oldFrame == null, "retrieve1ObjectSync??????????????????!");
        //
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_FrameID = '%s'");
        ssf.setConditions(new String[]{frameOld.getID() + ""});
        List<SmallSheetText> oldTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf);
        Assert.assertTrue(oldFrame == null && oldTextList.size() == 0, "????????????");
        //
        SmallSheetFrame newFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_FrameID = '%s'");
        ssf2.setConditions(new String[]{String.valueOf(frameNew.getID())});
        List<SmallSheetText> newTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf2);
        Assert.assertTrue(newFrame != null && newTextList.size() > 0, "????????????");
        frameNew.setListSlave1(newTextList);

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
            Assert.assertTrue(false, "createReplacerAsync?????????");
        }
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateReplacerAsync??????????????????????????????");
        //
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        Assert.assertTrue(smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync??????????????????????????????");
        Assert.assertTrue(ssf3 == null, "retrieve1ObjectSync??????????????????!");
    }

    @Test
    public void test_m_createMasterSlaveAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //???????????????????????????????????????ID???????????????????????????????????????
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //????????????
        SmallSheetFrame preFrame = DataInput.getSmallSheetFrame();
        preFrame.setID((int) smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetFrame.class));
        List<SmallSheetText> preTextList = new ArrayList<SmallSheetText>();
        for (int i = 0; i < SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().size(); i++) {
            SmallSheetText textModel = ((SmallSheetText) SmallSheetTextPresenterTest.DataInput.getSmallSheetTextList().get(i));
            textModel.setFrameId(Long.valueOf(preFrame.getID()));
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
            Assert.assertTrue(false, "createMasterSlaveAsync?????????");
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createMasterSlaveObjectAsyn????????????????????????!");
        //
        SmallSheetFrame frame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, preFrame);
        Assert.assertTrue(frame != null, "rerieve1?????????????????????");
        Assert.assertTrue(frame.compareTo(preFrame) == 0, "???????????????????????????frame?????????!");
        SmallSheetFrame frame1Rerieve1 = frame;
        frame1Rerieve1.setSql("Where F_FrameID = '%s'");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync?????????????????????!");
        Assert.assertTrue(list != null && list.size() > 0, "retrieveNObjectSync?????????????????????");
        frame1Rerieve1.setListSlave1(list);
        //
        frame.setSql("where F_FrameID = '%s'");
        frame.setConditions(new String[]{String.valueOf(frame.getID())});
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieveNObjectSync????????????????????????");
        Assert.assertTrue(textList != null && textList.size() > 0, "retrieveNObjectSync?????????????????????");
        for (int i = 0; i < textList.size(); i++) {
            Assert.assertTrue(preTextList.get(i).compareTo(textList.get(i)) == 0, "???????????????????????????text?????????!");
        }
    }

    @Test
    public void test_n_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //
//        @SuppressLint("WifiManagerLeak")
//        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(false);
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
            Assert.assertTrue(false, "createNAsync?????????");
        }
        // ???????????????????????????????????? bInTestMode
        SmallSheetFramePresenter.bInTestMode = false;
//        wifiManager.setWifiEnabled(true);
        Thread.sleep(10000); //??????????????????
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "createNAsync?????????????????????");

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(bm1.getID().longValue());
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "smallSheetText Create???????????????????????????");
            }
        }
        Assert.assertTrue(smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Create????????????????????????EC_NoError???Create??????");
        Assert.assertTrue(bm1 != null, "?????????????????????????????????Null???");

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        smallSheetFrame.setSql("where F_SyncDatetime = %s and F_SlaveCreated = %s");
        smallSheetFrame.setConditions(new String[]{"0", String.valueOf(BaseModel.EnumBoolean.EB_Yes.getIndex())});
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);//????????????List
        for (Object o : frameList) {
            SmallSheetFrame ssf = (SmallSheetFrame) o;
            Assert.assertTrue(bm1.compareTo(ssf) != 0, "????????????????????????????????????????????????????????????????????????");
        }

        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        SmallSheetFrame retailTradeR1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue(retailTradeR1 == null, "??????????????????????????????????????????");
    }


    /**
     * ????????????smallSheetFrame???????????????
     */
//    @Test
//    public void test_retrieveNSmallSheetFrame() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        SmallSheetFramePresenter smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//        Integer total = smallSheetFramePresenter.retrieveCount();
//        System.out.println("smallSheetFrame???????????????" + total);
//        org.junit.Assert.assertTrue("???????????????", total > Shared.INVALID_CASE_TOTAL);
//    }
}
