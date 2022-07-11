package wpos.model;



import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.Shared;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class VipTest extends BaseTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    public static class DataInput {
        private static Vip vip = new Vip();

        protected static Vip getVip() throws Exception {
            Random ran = new Random();
            vip = new Vip();
            vip.setMobile("1212" + String.valueOf(System.currentTimeMillis()).substring(6));
            vip.setCardID(1);
            vip.setICID(Shared.getValidICID());
            Thread.sleep(1);
            vip.setName("Tom" + String.valueOf(System.currentTimeMillis()).substring(6));
            Thread.sleep(1);
            vip.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
            vip.setConsumeTimes(0);
            vip.setConsumeAmount(0);
            vip.setDistrict("广州");
            vip.setCategory(1);
            vip.setBirthday(new Date());
            vip.setBonus(0);
            vip.setLastConsumeDatetime(new Date());
//            vip.setPOS_SN("123456");
            return (Vip) vip.clone();
        }
    }

    @Test
    public void test_a_CheckCreate() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip vip = new Vip();
        vip.setMobile("12345678910");
        vip.setCardID(1);
//        vip.setPOS_SN("SN");
        vip.setiCID("464646199803235648");
        vip.setName("会员");
        vip.setEmail("123@xxx.com");
        vip.setConsumeTimes(0);
        vip.setConsumeAmount(0);
        vip.setDistrict("广东");
        vip.setCategory(1);
        vip.setBonus(0);
        vip.setBirthday(new Date());
        String err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        caseLog("测试手机号");
        vip.setMobile("12345678910xx");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_mobile);
        vip.setMobile("12345678910");
        caseLog("测试POS_SN");
//        vip.setPOS_SN("SN1234567890123456789012345678901234567890");
//        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
//        Assert.assertEquals(err, Pos.FIELD_ERROR_PosSN);
//        vip.setPOS_SN("SN");
        caseLog("测试身份证");
        vip.setiCID("464646199803235");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_ICID);
        vip.setiCID("445222199823232536");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_icid);
        vip.setiCID("464646199803235648");
        caseLog("测试名称");
        vip.setName("会员12345678901234567890123456789012345678901234567890");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_name);
        vip.setName("会员");
        caseLog("测试邮箱");
        vip.setEmail("123xxxcom");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_Email);
        vip.setEmail("123@xxx.com");
        caseLog("测试总消费次数");
        vip.setConsumeTimes(-1);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_consumeTimes);
        vip.setConsumeTimes(0);
        caseLog("测试总消费金额");
        vip.setConsumeAmount(-1);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_consumeAmount);
        vip.setConsumeAmount(0);
        caseLog("测试区域");
        vip.setDistrict("");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        vip.setDistrict("1234567890123456789012345678901234567890");
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_district);
        vip.setDistrict("广东");
        caseLog("测试会员分类");
        vip.setCategory(-1);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FEILD_ERROR_category);
        vip.setCategory(1);
        caseLog("测试会员积分");
        vip.setBonus(-1);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_bonus);
        vip.setBonus(0);
        caseLog("测试生日");
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        String date = "2020/12/30 23:59:59";
        vip.setBirthday(sdf.parse(date));
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        vip.setBirthday(new Date());
        caseLog("测试cardID");
        vip.setCardID(-1);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Vip.FIELD_ERROR_cardID);
        vip.setCardID(23);
        err = vip.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
    }

    @Test
    public void test_b_CheckDelete() {
        Shared.printTestMethodStartInfo();

        Vip vip = new Vip();
        vip.setID(1);
        String error = vip.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试ID");
        vip.setID(BaseSQLiteBO.INVALID_ID);
        error = vip.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vip.setID(1);
    }

    @Test
    public void test_c_CheckUpdate() throws Exception {
        Shared.printTestMethodStartInfo();

        Vip vip = new Vip();
        vip.setID(1);
        vip.setName("会员");
        vip.setBirthday(new Date());
        vip.setEmail("123@xx.com");
        String error = vip.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试ID");
        vip.setID(BaseSQLiteBO.INVALID_ID);
        error = vip.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vip.setID(1);
        caseLog("测试名称");
        vip.setName("12345678901234567890");
        error = vip.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FIELD_ERROR_name);
        vip.setName("会员");
        caseLog("测试生日");
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        String date = "2020/12/30 23:59:59";
        vip.setBirthday(sdf.parse(date));
        error = vip.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        vip.setBirthday(new Date());
        caseLog("测试邮箱");
        vip.setEmail("123@xxcom");
        error = vip.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FIELD_ERROR_Email);
        vip.setEmail("123@xx.com");
    }

    @Test
    public void test_d_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        Vip vip = new Vip();
        vip.setID(1);
        String error = vip.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        caseLog("测试ID");
        vip.setID(BaseSQLiteBO.INVALID_ID);
        error = vip.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        vip.setID(1);
    }

    @Test
    public void test_e_CheckRetrieveN() throws Exception{
        Shared.printTestMethodStartInfo();

        Vip vip = DataInput.getVip();
        //
        caseLog("测试status");
        vip.setConditions(new String[]{"0"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        String error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        //
        vip.setConditions(new String[]{"3"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, Vip.FIELD_ERROR_status);
        //
        vip.setConditions(new String[]{"-1"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, Vip.FIELD_ERROR_status);
        //
        caseLog("测试status和POS_SN");
        vip.setConditions(new String[]{"0", "123456"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        //
        vip.setConditions(new String[]{"3", "123456"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, Vip.FIELD_ERROR_status);
        //
        vip.setConditions(new String[]{"3", "123456789123456789123456789123456789"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, Vip.FIELD_ERROR_status);
        //
        vip.setConditions(new String[]{"2", "12345678912345678912345678912345"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, "");
        //
        vip.setConditions(new String[]{"2", "123456789123456789123456789123456789"});
        vip.setSubUseCaseID(BaseModel.EnumSubUseCaseID.ESUC_Long_String);
        error = vip.checkRetrieveN(BaseSQLiteBO.CASE_Vip_RetrieveNByConditions);
        Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);

        caseLog("测试手机号");
        vip.setMobile("152000000012");
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FIELD_ERROR_mobile);
        //
        vip.setMobile("152000010");
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FIELD_ERROR_mobile);
        //
        vip.setMobile("15200000011");
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        caseLog("测试category");
        vip.setCategory(0);
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FEILD_ERROR_category);
        //
        vip.setCategory(-10);
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, Vip.FEILD_ERROR_category);
        //
        vip.setCategory(1);
        error = vip.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //
        //CASE_Vip_retrieveNByMobileOrVipCardSNEx
        // mobile大于11位
        vip.setMobile("123456789012");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_mobile);

        // mobile小于11位
        vip.setMobile("1234567890");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_mobile);

        vip.setMobile("12345678901");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        vip.setVipCardSN("1234567890123456");
        vip.setMobile("");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        vip.setMobile(null);
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        // vipCardSN大于16位
        vip.setVipCardSN("12345678901234567");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_vipCardSN);

        // vipCardSN小于于16位
        vip.setVipCardSN("123456789012345");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_vipCardSN);

        vip.setVipCardSN("1234567890123456");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        vip.setMobile("12345678901");
        vip.setVipCardSN("");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        vip.setVipCardSN(null);
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, "");

        vip.setMobile("");
        vip.setVipCardSN("");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);

        vip.setMobile(null);
        vip.setVipCardSN(null);
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);

        vip.setMobile(null);
        vip.setVipCardSN("");
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);

        vip.setMobile("");
        vip.setVipCardSN(null);
        error = vip.checkRetrieveN(BaseHttpBO.CASE_Vip_retrieveNByMobileOrVipCardSNEx);
        Assert.assertEquals(error, Vip.FIELD_ERROR_RetrieveNByMobileOrVipCardSN);
    }
}
