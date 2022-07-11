package wpos.model;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.BaseModelTest;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;


public class SmallSheetTextTest extends BaseModelTest {
    @BeforeClass
    public void setup() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void testCheckCreate() {
        Shared.printTestMethodStartInfo();

        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setContent("*****");
        smallSheetText.setSize(1);
        smallSheetText.setBold(1);
        smallSheetText.setGravity(1);
        smallSheetText.setFrameId(1l);
        String error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试小票内容
        smallSheetText.setContent("12345678901234567890123456789012345678901234567890"
                + "1234567890123456789012345678901234567890123456789011");
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_content);
        smallSheetText.setContent("*****");
        //测试字体大小
        smallSheetText.setSize(0);
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_size);
        smallSheetText.setSize(1);
        //测试字体加粗
        smallSheetText.setBold(2);
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_bold);
        smallSheetText.setBold(0);
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        smallSheetText.setBold(1);
        //测试内容位置
        smallSheetText.setGravity(0);
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_gravity);
        smallSheetText.setGravity(1);
        //测试小票格式ID
        smallSheetText.setFrameId(-1l);
        error = smallSheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_frameID);
        smallSheetText.setFrameId(1l);
    }

    @Test
    public void testCheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setFrameId(1l);
        smallSheetText.setPageIndex("1");
        smallSheetText.setPageSize("1");
        String error = smallSheetText.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试小票格式ID
        smallSheetText.setFrameId(-1l);
        error = smallSheetText.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_frameID);
        smallSheetText.setFrameId(1l);
        //测试PageIndex
        smallSheetText.setPageIndex("0");
        error = smallSheetText.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        smallSheetText.setPageIndex("1");
        //测试PageSize
        smallSheetText.setPageSize("0");
        error = smallSheetText.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        smallSheetText.setPageSize("1");
    }

    @Test
    public void testCheckDelete() {
        Shared.printTestMethodStartInfo();

        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setFrameId(1l);
        String error = smallSheetText.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试小票ID
        smallSheetText.setFrameId(-1l);
        error = smallSheetText.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_frameID);
        smallSheetText.setFrameId(1l);
    }

    @Test
    public void testCheckUpdate() {
        Shared.printTestMethodStartInfo();

        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setID(1);
        smallSheetText.setContent("*****");
        smallSheetText.setSize(1);
        smallSheetText.setBold(1);
        smallSheetText.setGravity(1);
        smallSheetText.setFrameId(1l);
        String error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //测试ID
        smallSheetText.setID(-1);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        smallSheetText.setID(1);
        //测试小票内容
        smallSheetText.setContent("12345678901234567890123456789012345678901234567890"
                + "1234567890123456789012345678901234567890123456789011");
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_content);
        smallSheetText.setContent("*****");
        //测试字体大小
        smallSheetText.setSize(0);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_size);
        smallSheetText.setSize(1);
        //测试字体加粗
        smallSheetText.setBold(0);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        smallSheetText.setBold(2);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_bold);
        smallSheetText.setBold(1);
        //测试内容位置
        smallSheetText.setGravity(0);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_gravity);
        smallSheetText.setGravity(1);
        //测试小票格式ID
        smallSheetText.setFrameId(-1l);
        error = smallSheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetText.FIELD_ERROR_frameID);
        smallSheetText.setFrameId(1l);
    }
}
