package wpos.presenter;

import java.util.Random;

public class RetailTradeCouponPresenterTest {
//    private RetailTradeCouponPresenter retailTradeCouponPresenter = null;
//    private RetailTradeCouponSQLiteBO retailTradeCouponSQLiteBO = null;
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        retailTradeCouponPresenter = GlobalController.getInstance().getRetailTradeCouponPresenter();
//        retailTradeCouponSQLiteBO = new RetailTradeCouponSQLiteBO(GlobalController.getInstance().getContext(), null, null);
//    }
//
//    @Override
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
//
//    @BeforeClass
//    public static void beforeClass() {
//        Shared.printTestClassStartInfo();
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Shared.printTestClassEndInfo();
//    }
//
//    public static class DataInput{
//        private static RetailTradeCoupon retailTradeCouponInput = null;
//        public static RetailTradeCoupon getRetailTradeCoupon(){
//            retailTradeCouponInput = new RetailTradeCoupon();
//            retailTradeCouponInput.setRetailTradeID(new Random().nextInt(6) + 1);
//            retailTradeCouponInput.setCouponCodeID(1);
//            return retailTradeCouponInput;
//        }
//    }
//
//    @Test
//    public void test_a_createSyncCase1(){
//        Shared.printTestMethodStartInfo();
//
//        caseLog("正常添加");
//        RetailTradeCoupon retailTradeCoupon = DataInput.getRetailTradeCoupon();
//
//        RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCoupon);
//        Assert.assertTrue("创建RetailTradeCoupon失败", retailTradeCouponCreate == null || retailTradeCouponPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
//
//        retailTradeCouponCreate.setIgnoreIDInComparision(true);
//        if (retailTradeCouponCreate.compareTo(retailTradeCoupon) != 0){
//            Assert.assertTrue("创建对象和DB中的不一致失败", false);
//        }
//    }
//
//    @Test
//    public void test_a_createSyncCase3(){
//        Shared.printTestMethodStartInfo();
//
//        caseLog("使用重复的ID进行创建");
//        RetailTradeCoupon retailTradeCoupon = DataInput.getRetailTradeCoupon();
//
//        RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCoupon);
//        Assert.assertTrue("创建RetailTradeCoupon失败", retailTradeCouponCreate == null || retailTradeCouponPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
//
//        retailTradeCouponCreate.setIgnoreIDInComparision(true);
//        if (retailTradeCouponCreate.compareTo(retailTradeCoupon) != 0){
//            Assert.assertTrue("创建对象和DB中的不一致失败", false);
//        }
//
//        retailTradeCoupon = DataInput.getRetailTradeCoupon();
//        retailTradeCoupon.setID(retailTradeCouponCreate.getID());
//        //
//        retailTradeCouponPresenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeCoupon);
//        Assert.assertTrue("使用重复的id创建RetailTradeCoupon成功", retailTradeCouponPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError);
//    }
}
