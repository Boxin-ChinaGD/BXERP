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
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
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
                textInput.setContent("          " + "BoXin收银POS机");
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
        Assert.assertTrue("错误码不正确！" + smallSheetFramePresenter.getLastErrorCode() + "," + smallSheetFramePresenter.getLastErrorMessage(), smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        long maxID = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);

        Assert.assertTrue("获取的最大ID不正确！", frame.getID() == (maxID - 1));
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
                    Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
                }
            }
        }
        Assert.assertTrue("CreateNSync测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (int i = 0; i < frameList.size(); i++) {
            SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameList.get(i));
            if (frame1Rerieve1 != null) {
                frame1Rerieve1.setSql("Where F_FrameID = ?");
                frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
                List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
                frame1Rerieve1.setListSlave1(list);
            }
            Assert.assertTrue("CreateNSync测试失败,原因:所插入的数据与查询到的不一致!", frameList.get(i).compareTo(frame1Rerieve1) == 0);
        }
    }

    @Test
    public void test_c_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bmCreateSync = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("CreateSync 测试失败,创建的TextList数据不正确", bmCreateSync.getListSlave1() != null && bmCreateSync.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) bmCreateSync.getListSlave1()) {
            smallSheetText.setFrameId(bmCreateSync.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //
        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bmCreateSync);
        Assert.assertTrue("retrieve错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve返回的数据不正确，应该返回有个smallsheetFrame数据", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync返回的数据不正确，应该返回有个List<SmallSheetText>数据", list.size() > 0);
        //
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", bmCreateSync.compareTo(frame1Retrieve1) == 0);

        //异常case: 插入重复ID
        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        ssf.setID(frame1Retrieve1.getID());
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);

        Assert.assertTrue("CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_d_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync测试失败,创建的TextList数据不正确", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve返回的数据不正确，应该返回有个smallsheetFrame数据", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync返回的数据不正确，应该返回有个List<SmallSheetText>数据", list.size() > 0);
        //
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", ssf.compareTo(frame1Retrieve1) == 0);

        //正常更新的case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        bm1.setLogo("updateLOGO");
        bm1.setListSlave1(list);
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("UpdateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame frame1Rerieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1ObjectSync 失败！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync返回的数据不正确", frame1Rerieve1 != null);

        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> sstList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync失败", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync失败返回数据错误", sstList.size() > 0);
        frame1Rerieve1.setListSlave1(sstList);

        //
        Assert.assertTrue("UpdateSync测试失败,原因:查询到的数据没有更新!", bm1.getLogo().equals(frame1Rerieve1.getLogo()));

        //异常case: update的对象ID不存在
        SmallSheetFrame bm2 = DataInput.getSmallSheetFrame();
        bm2.setLogo("updateLOGO2");
        smallSheetFramePresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm2);
        Assert.assertTrue("UpdateSync测试失败", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_e_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("RetrieveNSync搜索到的数据数量应该>=0!", frameList.size() >= 0);

        //根据条件查询 //...将来增加case：输入更多的查询条件
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_LOGO = ?");
        ssf.setConditions(new String[]{"smallSheetFrameLOGO"});
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        Assert.assertTrue("根据条件RetrieveNSync搜索到的数据数量应该>=0!", frameList.size() >= 0);

        //异常的case,一个通配符多个值
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_LOGO = ?");
        ssf2.setConditions(new String[]{"smallSheetFrameLOGO", "1"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf2);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case,多个通配符一个值
        SmallSheetFrame ssf3 = new SmallSheetFrame();
        ssf3.setSql("where F_ID = ? and F_LOGO = ?");
        ssf3.setConditions(new String[]{"smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf3);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常case,多个通配符多个值
        SmallSheetFrame ssf4 = new SmallSheetFrame();
        ssf4.setSql("where F_ID = ? and F_LOGO = ?");
        ssf4.setConditions(new String[]{"1", "smallSheetFrameLOGO"});
        smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions, ssf4);
        Assert.assertTrue("根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_f_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync测试失败,创建的TextList数据不正确", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve返回的数据不正确，应该返回有个smallsheetFrame数据", frame1Retrieve1 != null);

        frame1Retrieve1.setSql("Where F_FrameID = ?");
        frame1Retrieve1.setConditions(new String[]{String.valueOf(frame1Retrieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Retrieve1);
        frame1Retrieve1.setListSlave1(list);
        Assert.assertTrue("retrieveNObjectSync错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync返回的数据不正确，应该返回有个List<SmallSheetText>数据", list.size() > 0);
        //
        Assert.assertTrue("CreateSync测试失败,原因:所插入数据与查询到的不一致!", ssf.compareTo(frame1Retrieve1) == 0);

        //正常查询的case
        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        bm1.setID(ssf.getID());
        smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //异常的case根据不存在的ID查询
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(Shared.SQLITE_ID_Infinite);
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("Retrieve1 测试失败,原因返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("Retrieve1 测试失败,返回的basemodel不为null", ssf3 == null);
    }

    @Test
    public void test_g_DeleteSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame ssf = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("CreateSync测试失败,创建的TextList数据不正确", ssf.getListSlave1() != null && ssf.getListSlave1().size() > 0);

        for (SmallSheetText smallSheetText : (List<SmallSheetText>) ssf.getListSlave1()) {
            smallSheetText.setFrameId(ssf.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
            Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        }
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        SmallSheetFrame frame1Retrieve1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue("retrieve错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve返回的数据不正确，应该返回有个smallsheetFrame数据", frame1Retrieve1 != null);

        //正常删除的case
        SmallSheetFrame ssf2 = DataInput.getSmallSheetFrame();
        ssf2.setID(ssf.getID());
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("DeleteSync 测试失败,原因返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf2);
        Assert.assertTrue("retrieve错误码不正确", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("对象未被删除！", ssf3 == null);

        //删除一个ID不存在的对象
        SmallSheetFrame ssf4 = DataInput.getSmallSheetFrame();
        ssf4.setID(Shared.SQLITE_ID_Infinite);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue("DeleteSync 测试失败,原因返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //删除对象的ID为null
        ssf4.setID(null);
        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf4);
        Assert.assertTrue("DeleteSync 测试失败,原因返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(bm1.getID());
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        Assert.assertTrue("Create时错误码应该为：EC_NoError，Create失败", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入数据后对象不应该为Null！", bm1 != null);

        SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("retrieve1ObjectSync时，错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync，数据不正确！", ssf != null);
        SmallSheetFrame frame1Rerieve1 = ssf;
        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync时，错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync的数据不正确！", list != null && list.size() > 0);
        frame1Rerieve1.setListSlave1(list);

        Assert.assertTrue("插入后的对象与查找到的对象不一致，插入失败！", bm1.compareTo(ssf) == 0);


        //Case: 主键冲突
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_i_UpdateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame bm1 = DataInput.getSmallSheetFrame();
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("CreateSync bm1测试失败,原因:返回错误码不正确!", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        for (int i = 0; i < bm1.getListSlave1().size(); i++) {
            SmallSheetText sheetText = (SmallSheetText) bm1.getListSlave1().get(i);
            sheetText.setFrameId(bm1.getID());
            smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sheetText);
        }

        //Case: 正常case
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
            Assert.assertTrue("UpdateAsync超时！", false);
        }
        Assert.assertTrue("UpdateAsync错误码不正确！" + smallSheetSQLiteEvent.getLastErrorCode(), smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        SmallSheetFrame bmUpdate = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("Update bm1时错误码应该为：EC_NoError，Update失败", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("更新后的对象与查找到的对象不一致，插入失败！", bm1.getLogo().equals(bmUpdate.getLogo()));

        //Case: update没ID的对象
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
            Assert.assertTrue("UpdateAsync超时！", false);
        }
        Assert.assertTrue("UpdateAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_j_RetrieveNAsync() throws InterruptedException {
        Shared.printTestMethodStartInfo();

        //Case: 正常case 1
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
            Assert.assertTrue("RetrieveNAsync超时！", false);
        }
        Assert.assertTrue("RetrieveNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", smallSheetSQLiteEvent.getListMasterTable().size() > 0);

        //将来增加N个CASE_SmallSheetFrame_RetrieveNByConditions的case
        //...

        //Case: 正常case 2
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
            Assert.assertTrue("RetrieveNAsync超时！", false);
        }
        Assert.assertTrue("RetrieveNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", smallSheetSQLiteEvent.getListMasterTable().size() > 0);


        //异常Case:一个通配符多个值
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
            Assert.assertTrue("RetrieveNAsync超时！", false);
        }
        Assert.assertTrue("RetrieveNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);

        //case:多个通配符一个值
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
            Assert.assertTrue("RetrieveNAsync超时！", false);
        }
        Assert.assertTrue("RetrieveNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNAsync会有查询到符合条件的数据", smallSheetSQLiteEvent.getListMasterTable().size() >= 0);
    }

    @Test
    public void test_k_CreateNAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //正常的Case
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
            Assert.assertTrue("CreateNAsync超时！", false);
        }
        Assert.assertTrue("CreateNAsync返回错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        for (BaseModel baseModel : frameList) {
            SmallSheetFrame smallSheetFrame = (SmallSheetFrame) baseModel;
            if (smallSheetFrame.getListSlave1() != null) {
                for (SmallSheetText smallSheetText : (List<SmallSheetText>) smallSheetFrame.getListSlave1()) {
                    smallSheetText.setFrameId(smallSheetFrame.getID());
                    smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                    Assert.assertTrue("createObjectSync返回错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
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
            Assert.assertTrue("CreateNAsync的数据与原数据不符", frameList.get(i).compareTo(baseModel) == 0);
        }

        //主键冲突的case
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        Assert.assertTrue("createNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
    }

    @Test
    public void test_l_createReplacerAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //清空表，以免造成干扰
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //获取到最大ID，生成临时ID，将FrameOld插入数据库
        long maxIDInFrame = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println("SmallSheetFrame最大ID=" + maxIDInFrame);
        Assert.assertTrue("presenter.getMaxId()失败！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        long maxIDInText = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println("SmallSheetText最大ID=" + maxIDInText);
        Assert.assertTrue("smallSheetTextPresenter.getMaxId()失败！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        //生成Frameold
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
        //插入数据库
        smallSheetFramePresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue("presenter.createObjectSync()失败！" + smallSheetFramePresenter.getLastErrorMessage(), smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        smallSheetTextPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, textListOld);
        Assert.assertTrue("smallSheetTextPresenter.createNObjectSync()失败！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //生成FrameNew
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
        //正常替换数据的Case
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
            Assert.assertTrue("createReplacerAsync超时！", false);
        }
        Assert.assertTrue("CreateReplacerAsync返回的错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame oldFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameOld);
        Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync的数据不正确!", oldFrame == null);
        //
        SmallSheetFrame ssf = new SmallSheetFrame();
        ssf.setSql("where F_FrameID = ?");
        ssf.setConditions(new String[]{frameOld.getID() + ""});
        List<SmallSheetText> oldTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf);
        Assert.assertTrue("删除失败", oldFrame == null && oldTextList.size() == 0);
        //
        SmallSheetFrame newFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        SmallSheetFrame ssf2 = new SmallSheetFrame();
        ssf2.setSql("where F_FrameID = ?");
        ssf2.setConditions(new String[]{String.valueOf(frameNew.getID())});
        List<SmallSheetText> newTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, ssf2);
        Assert.assertTrue("插入失败", newFrame != null && newTextList.size() > 0);

        //异常case，使用空数据对象替换
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
            Assert.assertTrue("createReplacerAsync超时！", false);
        }
        Assert.assertTrue("CreateReplacerAsync返回的错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame ssf3 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, frameNew);
        Assert.assertTrue("retrieve1ObjectSync返回的错误码不正确！", smallSheetFramePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieve1ObjectSync的数据不正确!", ssf3 == null);
    }

    @Test
    public void test_m_createMasterSlaveAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();

        //先删除所有数据，避免主表的ID已经存在于从表中，产生干扰
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        smallSheetFramePresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);

        //准备数据
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
            Assert.assertTrue("createMasterSlaveAsync超时！", false);
        }
        Assert.assertTrue("createMasterSlaveObjectAsyn返回错误码不正确!", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        SmallSheetFrame frame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, preFrame);
        Assert.assertTrue("rerieve1的数据不正确！", frame != null);
        Assert.assertTrue("插入的和查询出来的frame不一致!", frame.compareTo(preFrame) == 0);
        SmallSheetFrame frame1Rerieve1 = frame;
        frame1Rerieve1.setSql("Where F_FrameID = ?");
        frame1Rerieve1.setConditions(new String[]{String.valueOf(frame1Rerieve1.getID())});
        List<SmallSheetText> list = (List<SmallSheetText>) SmallSheetFramePresenterTest.smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame1Rerieve1);
        Assert.assertTrue("retrieveNObjectSync的错误码不正确!", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync的数据不正确！", list != null && list.size() > 0);
        frame1Rerieve1.setListSlave1(list);
        //
        frame.setSql("where F_FrameID = ?");
        frame.setConditions(new String[]{String.valueOf(frame.getID())});
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, frame);
        Assert.assertTrue("retrieveNObjectSync的错误码不正确！", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("retrieveNObjectSync的数据不正确！", textList != null && textList.size() > 0);
        for (int i = 0; i < textList.size(); i++) {
            Assert.assertTrue("插入的和查询出来的text不一致!", preTextList.get(i).compareTo(textList.get(i)) == 0);
        }
    }

    @Test
    public void test_n_CreateAsync() throws CloneNotSupportedException, InterruptedException {
        Shared.printTestMethodStartInfo();
        //
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Thread.sleep(10000); //等待硬件就绪
        //模拟上传小票格式时还未全部成功创建从表信息
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
            Assert.assertTrue("createNAsync超时！", false);
        }
        // 创建小票完毕后再重置
        SmallSheetFramePresenter.bInTestMode = false;
        wifiManager.setWifiEnabled(true);
        Thread.sleep(10000); //等待硬件就绪
        Assert.assertTrue("createNAsync错误码不正确！", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        if (bm1.getListSlave1() != null) {
            for (SmallSheetText smallSheetText : (List<SmallSheetText>) bm1.getListSlave1()) {
                smallSheetText.setFrameId(bm1.getID());
                smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
                Assert.assertTrue("smallSheetText Create失败！错误码不正确", smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
            }
        }
        Assert.assertTrue("Create时错误码应该为：EC_NoError，Create失败", smallSheetSQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入数据后对象不应该为Null！", bm1 != null);

        List<SmallSheetFrame> frameList = new ArrayList<SmallSheetFrame>();
        frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNToUpload,  new SmallSheetFrame());//找到主表List
        for (Object o : frameList) {
            SmallSheetFrame ssf = (SmallSheetFrame) o;
            Assert.assertTrue("由于未创建完成从表，所以不应该查到临时小票格式！", bm1.compareTo(ssf) != 0);
        }

        smallSheetFramePresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        SmallSheetFrame retailTradeR1 = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, bm1);
        Assert.assertTrue("没有正确删除创建的小票格式！", retailTradeR1 == null);
    }

    /**
     * 查询本地smallSheetFrame表的总条数
     */
    @Test
    public void test_retrieveNSmallSheetFrame() throws Exception {
        Shared.printTestMethodStartInfo();

        SmallSheetFramePresenter smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        Integer total = smallSheetFramePresenter.retrieveCount();
        System.out.println("smallSheetFrame表总条数：" + total);
        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
    }
}
