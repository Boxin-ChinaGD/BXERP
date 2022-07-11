package wpos.presenter;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.utils.EventBus;
import wpos.allEnum.ThreadMode;
import wpos.bo.BaseSQLiteBO;
import wpos.event.SmallSheetTextSQLiteEvent;
import wpos.listener.Subscribe;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.SmallSheetFrame;
import wpos.model.SmallSheetText;
import wpos.utils.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WPNA on 2018/9/29.
 */

public class SmallSheetTextPresenterTest extends BasePresenterTest {

    private static final int Event_ID_SmallSheetTextPresenterTest = 10000;

//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }

    @BeforeClass
    public void setUp() {
        super.setUp();

//        smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//        ;
//        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
//
//        if (smallSheetTextSQLiteEvent == null) {
//            smallSheetTextSQLiteEvent = new SmallSheetTextSQLiteEvent();
//            smallSheetTextSQLiteEvent.setId(Event_ID_SmallSheetTextPresenterTest);
//        }
        smallSheetTextSQLiteEvent.setId(Event_ID_SmallSheetTextPresenterTest);
        EventBus.getDefault().register(this);
    }

    @AfterClass
    public void tearDown() {
        EventBus.getDefault().unregister(this);
        super.tearDown();
    }


    public static class DataInput {
        private static SmallSheetText textInput = null;

        protected static final SmallSheetText getSmallSheetText() throws CloneNotSupportedException {
            textInput = new SmallSheetText();
            textInput.setContent("BoXin收银POS机");
            textInput.setSize(25f);
            textInput.setBold(0);
            textInput.setGravity(17);
            textInput.setFrameId(20l);

            return (SmallSheetText) textInput.clone();
        }

        protected static final List<BaseModel> getSmallSheetTextList() throws CloneNotSupportedException {
            List<BaseModel> textList = new ArrayList<BaseModel>();
            for (int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
                textList.add(getSmallSheetText());
            }
            return textList;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSmallSheetTextSQLiteEvent(SmallSheetTextSQLiteEvent event) {
        if (event.getId() == Event_ID_SmallSheetTextPresenterTest) {
            event.onEvent();
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            System.out.println(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Test
    public void test_a_getMaxId() throws CloneNotSupportedException {
        SmallSheetText test = DataInput.getSmallSheetText();
        test = (SmallSheetText) smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, test);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "错误码不正确！");

        Integer maxID = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime, SmallSheetText.class);

        Assert.assertTrue(test.getID() == (maxID - 1), "获取的最大ID不正确！");
    }

    /**
     * 需要在测试前清空表时，请重命名函数名为test_a1_DeleteAllSync
     */
    @Test
    public void test_b_DeleteAllSync() {
        smallSheetTextPresenter.deleteNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(textList.size() == 0, "RetrieveNSync搜索到的数据数量应该=0!");

    }

    @Test
    public void test_c_CreateNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<BaseModel> textList = DataInput.getSmallSheetTextList();
        textList = (List<BaseModel>) smallSheetTextPresenter.createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, textList);

        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync测试失败,原因:返回错误码不正确!");
        //
        for (int i = 0; i < textList.size(); i++) {
            SmallSheetText sst = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, textList.get(i));
            Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateNSync测试失败,原因retrieve1ObjectSync时返回的错误码不正确！");
            Assert.assertTrue(textList.get(i).compareTo(sst) == 0, "CreateNSync测试失败,原因:所插入的数据与查询到的不一致!");
        }
    }

    @Test
    public void test_d_CreateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        //正常创建的case
        SmallSheetText smallSheetText = DataInput.getSmallSheetText();
        smallSheetText = (SmallSheetText) smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);

        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        SmallSheetText text1Retrieve1 = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
        //
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "retrieve1ObjectSync返回的错误码不正确！");
        Assert.assertTrue(smallSheetText.compareTo(text1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //异常case: 插入重复ID
        SmallSheetText smallSheetText1 = DataInput.getSmallSheetText();
        smallSheetText1.setID(text1Retrieve1.getID());
        smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText1);
        //
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1重复ID测试失败,原因:返回错误码不应该为EC_NoError!");
    }

    @Test
    public void test_e_UpdateSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetText smallSheetText = DataInput.getSmallSheetText();
        smallSheetText = (SmallSheetText) smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
        //
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        //
        SmallSheetText text1Retrieve1 = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetText);
        //
        Assert.assertTrue(smallSheetText.compareTo(text1Retrieve1) == 0, "CreateSync测试失败,原因:所插入数据与查询到的不一致!");

        //正常更新的Case
        SmallSheetText ssf = smallSheetText;
        ssf.setContent("update之后的Content");
        smallSheetTextPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync bm1测试失败,原因:返回错误码不正确!");
        //
        SmallSheetText comm1Rerieve1 = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, ssf);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync测试失败,原因:retrieve1ObjectSync返回的错误码不正确!");
        //
        Assert.assertTrue(ssf.compareTo(comm1Rerieve1) == 0, "UpdateSync测试失败,原因:查询到的数据没有更新!");

        //异常的case: update的对象ID不存在
        SmallSheetText sst2 = DataInput.getSmallSheetText();
        sst2.setContent("异常的case");
        smallSheetTextPresenter.updateObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst2);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "UpdateSync测试失败");
    }

    @Test
    public void test_f_RetrieveNSync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        List<SmallSheetText> textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "RetrieveNSync测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(textList.size() >= 0, "RetrieveNSync搜索到的数据数量应该>=0!");

        //根据条件查询 //...将来增加case：输入更多的查询条件
        SmallSheetText sst = new SmallSheetText();
        sst.setFrameId(1l);
        sst.setSql("where F_Content = '%s'");
        sst.setConditions(new String[]{"BoXin收银POS机"});
        textList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
        //
        Assert.assertTrue(textList.size() >= 0, "根据条件RetrieveNSync搜索到的数据数量应该>=0!");

        //异常case，一个通配符多个值
        SmallSheetText sst2 = new SmallSheetText();
        sst2.setFrameId(2l);
        sst2.setSql("where F_Content = '%s'");
        sst2.setConditions(new String[]{"BoXin收银POS机", "0"});
        smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst2);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常的case,多个通配符一个值
        SmallSheetText sst3 = new SmallSheetText();
        sst3.setFrameId(3l);
        sst3.setSql("where F_ID='%s' and F_Content='%s'");
        sst3.setConditions(new String[]{"BoXin收银POS机"});
        smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst3);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");

        //异常case,多个通配符多个值
        SmallSheetText sst4 = new SmallSheetText();
        sst4.setFrameId(4l);
        sst4.setSql("where F_ID ='%s' and F_Content='%s'");
        sst4.setConditions(new String[]{"1", "BoXin收银POS机"});
        smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, sst4);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "根据条件 RetrieveNSync 测试失败,原因:返回错误码不正确!");
    }

    @Test
    public void test_g_Retrieve1Sync() throws CloneNotSupportedException {
        Shared.printTestMethodStartInfo();

        SmallSheetText sstCreate = DataInput.getSmallSheetText();
        sstCreate = (SmallSheetText) smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sstCreate);
        //正常的case
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        SmallSheetText sst = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sstCreate);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(sst != null, "Retrieve1 测试失败,查询的数据不正确!");

        //异常case,根据不存在的ID查询
        SmallSheetText sst2 = DataInput.getSmallSheetText();
        sst2.setID(Shared.SQLITE_ID_Infinite);
        BaseModel bm3 = smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst2);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(bm3 == null, "Retrieve1 测试失败,返回的basemodel不为null");
    }

    @Test
    public void test_h_DeleteSync() throws CloneNotSupportedException {

        SmallSheetText sstCreate = DataInput.getSmallSheetText();
        sstCreate = (SmallSheetText) smallSheetTextPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sstCreate);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "CreateSync bm1测试失败,原因:返回错误码不正确!");
        SmallSheetText sst = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sstCreate);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(sst != null, "Retrieve1 测试失败,查询的数据不正确!");

        //正常的case
        SmallSheetText sst2 = DataInput.getSmallSheetText();
        sst2.setID(sst.getID());
        smallSheetTextPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst2);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync 测试失败,原因返回错误码不正确!");
        SmallSheetText sstRetrieve = (SmallSheetText) smallSheetTextPresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst2);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError, "Retrieve1 测试失败,原因返回错误码不正确!");
        Assert.assertTrue(sstRetrieve == null, "Retrieve1 测试失败,查询的数据不正确!");

        //删除一个ID不存在的对象
        SmallSheetText sst3 = DataInput.getSmallSheetText();
        sst3.setID(Shared.SQLITE_ID_Infinite);
        smallSheetTextPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst3);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync 测试失败,原因返回错误码不正确!");

        //删除对象的ID为null
        sst3.setID(null);
        smallSheetTextPresenter.deleteObjectSync(BaseSQLiteBO.INVALID_CASE_ID, sst3);
        Assert.assertTrue(smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError, "DeleteSync 测试失败,原因返回错误码不正确!");
    }

    /**
     * 查询本地smallSheetText表的总条数
     */
//    @Test
//    public void test_retrieveNSmallSheetText() throws Exception {
//        Shared.printTestMethodStartInfo();
//
//        SmallSheetTextPresenter smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
//        Integer total = smallSheetTextPresenter.retrieveCount();
//        System.out.println("smallSheetText表总条数：" + total);
//        org.junit.Assert.assertTrue("查询异常！", total > Shared.INVALID_CASE_TOTAL);
//    }

}
