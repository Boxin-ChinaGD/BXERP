package wpos.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseSQLiteBO;
import wpos.utils.FieldFormat;
import wpos.utils.MD5Util;
import wpos.utils.Shared;

import java.util.UUID;

public class PosTest extends BaseTestCase {
    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static Pos getPos() {
            Pos p = new Pos();
            p.setID(1);
            p.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT));
            p.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
            p.setShopID(1);
            p.setStatus(Pos.EnumStatusPos.ESP_Active.getIndex());
            p.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));

            return p;
        }
    }

    @Test
    public void test_a_CheckCreate() {
        Shared.printTestMethodStartInfo();

        Pos p = DataInput.getPos();
        String error = "";
        //
        caseLog("检查SN");
        p.setPos_SN(null);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
        p.setPos_SN("1234567890123456789012345678901234");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
        p.setPos_SN("123544561");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        caseLog("检查ShopID");
        p.setShopID(0);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
        p.setShopID(-99);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
        p.setShopID(1);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        caseLog("检查Salt");
        p.setSalt(null);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
        p.setSalt("1234567890123456789012345678901234");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
        p.setSalt("123544561");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        caseLog("检查Status");
        p.setStatus(2);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_Status);
        p.setStatus(-99);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_Status);
        p.setStatus(1);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        caseLog("检查密码");
        p.setPasswordInPOS(null);
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
        p.setPasswordInPOS("1234567890123456789012345678901234");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
        p.setPasswordInPOS("132465401");
        error = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
    }

    @Test
    public void test_b_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        String error = "";
        Pos p = DataInput.getPos();
        //
        caseLog("检查ID");
        p.setID(0);
        error = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        p.setID(-99);
        error = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1);
        error = p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_c_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        Pos p = DataInput.getPos();
        String error = "";
        //
        caseLog("检查SN");
        p.setPos_SN("1234567890123456789012345678901234");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
        p.setPos_SN(null);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        p.setPos_SN("12345678");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);

        //
        caseLog("检查ShopID");
        p.setShopID(-99);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(Pos.FIELD_ERROR_ShopID_RN, error);
        p.setShopID(0);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(Pos.FIELD_ERROR_ShopID_RN, error);
        p.setShopID(-1);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        p.setShopID(1);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        p.setStatus(-99);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(Pos.FIELD_ERROR_Status_RN, error);
        p.setStatus(2);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(Pos.FIELD_ERROR_Status_RN, error);
        p.setStatus(-1);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        p.setStatus(1);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        p.setStatus(0);
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        p.setPageIndex("-666");
        p.setPageSize("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageIndex("0");
        p.setPageSize("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageIndex("1");
        p.setPageSize("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        p.setPageSize("-666");
        p.setPageIndex("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageSize("0");
        p.setPageIndex("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        p.setPageSize("1");
        p.setPageIndex("10");
        error = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
    }

    @Test
    public void test_d_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        Pos p = DataInput.getPos();
        String error = "";
        //
        // CASE default
        p.setID(0);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        p.setID(-99);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        p.setID(1);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
        //
        p.setShopID(0);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
        p.setShopID(-99);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
        p.setShopID(1);
        error = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals("", error);
    }

    @Test
    public void test_e_CheckDelete() {
        Shared.printTestMethodStartInfo();

        Pos p = DataInput.getPos();
        String error = "";
        //
        p.setID(0);
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        p.setID(-99);
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1);
        error = p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_f_FromJson() throws JSONException {
        Shared.printTestMethodStartInfo();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        // case1:PasswordInPOS有值
        Pos p = DataInput.getPos();
        p.setPasswordInPOS("123456");
        JSONObject json = JSON.parseObject(gson.toJson(p));

        Pos parse1Pos = new Pos();
        parse1Pos = (Pos) parse1Pos.parse1C2(json.toString());
        System.out.println("解析后的POS1：" + parse1Pos);

        // case2:PasswordInPOS为空字符串
        Pos p2 = DataInput.getPos();
        p2.setPasswordInPOS("");
        JSONObject json2 = JSON.parseObject(gson.toJson(p2));

        Pos parse1Pos2 = new Pos();
        parse1Pos2 = (Pos) parse1Pos2.parse1C2(json2.toString());
        System.out.println("解析后的POS2：" + parse1Pos2);

        // case2:PasswordInPOS为null
        Pos p3 = DataInput.getPos();
        p3.setPasswordInPOS(null);
        JSONObject json3 = JSON.parseObject(gson.toJson(p3));

        Pos parse1Pos3 = new Pos();
        parse1Pos3 = (Pos) parse1Pos3.parse1C2(json3.toString());
        System.out.println("解析后的POS3：" + parse1Pos3);

    }


}
