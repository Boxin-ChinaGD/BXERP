package com.test.bx.app.model;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;
import com.test.bx.app.BaseModelTest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BarcodesTest extends BaseModelTest {

    private  static  String commodityID = "commodityID";

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
        // 纯英文
        Barcodes barcodes = new Barcodes();
        String error = "";
        //
        barcodes.setCommodityID(0);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
        barcodes.setCommodityID(-99);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
        barcodes.setCommodityID(1);
        //
        // 条形码传入其他字符
        barcodes.setBarcode("！@#￥%……&");
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        barcodes.setBarcode("1234567");
        //
        barcodes.setOperatorStaffID(0);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
        barcodes.setOperatorStaffID(-11);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
        barcodes.setOperatorStaffID(1);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        // 专门检查barcodes字段
        barcodes.setID(1L);
        String b = "zxcvbnm";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // 纯数字，长度为1
        b = "1234567";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // 英文和数字组合，长度为64
        b = "zxcvbnmasdfghjklqwertyuiop12345678901234567890123456789012345678";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        // 条形码传入其他字符
        b = "！@#￥%……&";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "！@#￥%……&123";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "！@#asd￥%……&";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "！@#as爱d￥%你……123&";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        // 带有空格的组合
        b = "Asd 123";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "Asd 1 23 ";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "  Asd 1 23 ";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        // 长度不在7~64之间
        b = "";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        b = "zxcvbnmasdfghjklqwertyuiop12345678901234567890123456789012345678909asdfghj";
        barcodes.setBarcode(b);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        //
        barcodes.setBarcode("zxcvbnm");
        barcodes.setOperatorStaffID(1);
        error = barcodes.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testCheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        String error = "";
        Barcodes barcodes = new Barcodes();
        barcodes.setID(0l);
        error = barcodes.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(-99L);
        error = barcodes.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        barcodes.setID(1L);
        error = barcodes.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testCheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = new Barcodes();
        barcodes.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        barcodes.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        String error = "";
        //
//        barcodes.setCommodityID(-99);
//        error = barcodes.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
//        Assert.assertEquals(error, String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, commodityID));
//        barcodes.setCommodityID(-1);
        //
//        barcodes.setBarcode();
        barcodes.setConditions(new String[]{"！@#￥%……&"});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        error = barcodes.checkRetrieveN(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        barcodes.setConditions(new String[]{"12323 fgdfg"});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        error = barcodes.checkRetrieveN(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        barcodes.setConditions(new String[]{"weqw1231fdf12313d"});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String);
        error = barcodes.checkRetrieveN(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        barcodes.setConditions(new String[]{"weqw1231fdf12313d", "0"});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        error = barcodes.checkRetrieveN(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setConditions(new String[]{"weqw1231fdf12313d", "-1"});
        barcodes.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_String_Long);
        error = barcodes.checkRetrieveN(BaseSQLiteBO.CASE_Barcodes_RetrieveNByConditions);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        barcodes.setPageIndex(String.valueOf(-88));
        error = barcodes.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        barcodes.setPageIndex(String.valueOf(0));
        error = barcodes.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        barcodes.setPageIndex(String.valueOf(1));
        //
        barcodes.setPageSize(String.valueOf(-88));
        error = barcodes.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        barcodes.setPageSize(String.valueOf(0));
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        barcodes.setPageSize(String.valueOf(1));
        //
        error = barcodes.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testCheckUpdate() {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = new Barcodes();
        String error = "";
        //
        barcodes.setID(0l);
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(-99l);
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(1l);
        //
        barcodes.setCommodityID(0);
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
        barcodes.setCommodityID(-99);
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
        barcodes.setCommodityID(1);
        //
        // 条形码传入其他字符
        barcodes.setBarcode("！@#￥%……&");
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
        barcodes.setBarcode("1234567");
        //
        error = barcodes.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void testCheckDelete() {
        Shared.printTestMethodStartInfo();

        Barcodes barcodes = new Barcodes();
        String error = "";
        barcodes.setID(0L);
        error = barcodes.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(-99l);
        error = barcodes.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(null);
        error = barcodes.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        barcodes.setID(1L);
        //
        error = barcodes.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }
}
