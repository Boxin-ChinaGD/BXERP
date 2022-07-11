package com.test.bx.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SubCommodity;
import com.bx.erp.presenter.CommodityPresenter;
import com.bx.erp.utils.Shared;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.bx.erp.utils.Shared.UNIT_TEST_TimeOut;

public class BaseCommodityTest {

    public static final int RETURN_OBJECT = 1;

    public static final int STAFF_ID1 = 1;
    public static final int STAFF_ID2 = 2;
    public static final int STAFF_ID3 = 3;
    public static final int STAFF_ID4 = 4;

    private static CommodityPresenter presenter = GlobalController.getInstance().getCommodityPresenter();


    public static class DataInput {
        private static Commodity commodityInput = null;

        /**
         * 生成一个平常商品
         *
         * @param priceRetail 这个为商品价格
         * @return
         * @throws CloneNotSupportedException
         */
        protected static final Commodity getNormalCommodity(Double priceRetail) throws CloneNotSupportedException, InterruptedException {
            commodityInput = new Commodity();
            commodityInput.setBarcode("20200428" + System.currentTimeMillis() % 1000000);
            commodityInput.setStatus(0);
            Thread.sleep(1000);
            commodityInput.setName("平常商品" + System.currentTimeMillis() % 1000000);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("瓶");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble() * 1000));
            commodityInput.setPriceRetail(priceRetail);
            commodityInput.setPriceVIP(priceRetail);
            commodityInput.setPriceWholesale(priceRetail);
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(Math.abs(new Random().nextInt(18) + 1));
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1) + 1));//
            commodityInput.setRefCommodityID(0);
            commodityInput.setRefCommodityMultiple(0);
            commodityInput.setTag("111");
            commodityInput.setNO(Math.abs(new Random().nextInt(18000)));
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark(""); // 期初值备注
            commodityInput.setReturnObject(1);
            commodityInput.setOperatorStaffID(1); // StaffID
            commodityInput.setPropertyValue1("自定义内容1");
            commodityInput.setPropertyValue2("自定义内容2");
            commodityInput.setPropertyValue3("自定义内容3");
            commodityInput.setPropertyValue4("自定义内容4");
            commodityInput.setCreateDatetime(new Date());
            commodityInput.setUpdateDatetime(new Date());
            commodityInput.setCreateDate(new Date());
            commodityInput.setProviderIDs("1");

            commodityInput.setMultiPackagingInfo(commodityInput.getBarcode() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" +
                    commodityInput.getPriceRetail() + ";" + commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
            return (Commodity) commodityInput.clone();
        }

        //生成一个组合商品
        public static final Commodity getCombinationCommodity(Double priceRetail) throws CloneNotSupportedException, InterruptedException, ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
            String date = sdf.format(new Date());
            commodityInput = new Commodity();
            commodityInput.setStatus(Commodity.EnumStatusCommodity.ESC_Normal.getIndex());
            commodityInput.setName("组合商品" + System.currentTimeMillis() % 1000000);
            // Thread.sleep(500);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setPurchasingUnit("箱");
            commodityInput.setBrandID(3);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Math.abs(new Random().nextDouble()));
            commodityInput.setPriceRetail(priceRetail);
            commodityInput.setPriceVIP(priceRetail);
            commodityInput.setPriceWholesale(priceRetail);
            // commodityInput.setRatioGrossMargin(Math.abs(new Random().nextDouble()));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(0);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setShelfLife(0);
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(0);//
            commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
            commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
            commodityInput.setTag("111");
            commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
            // commodityInput.setNOAccumulated(0);
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
            commodityInput.setnOStart(Commodity.NO_START_Default);
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
            commodityInput.setStartValueRemark(""); // 期初值备注
            commodityInput.setReturnObject(1); // 返回对象的值
            commodityInput.setBarcode("122111" + System.currentTimeMillis() % 1000000);
            // Thread.sleep(500);
            commodityInput.setPropertyValue1("自定义内容1");
            commodityInput.setPropertyValue2("自定义内容2");
            commodityInput.setPropertyValue3("自定义内容3");
            commodityInput.setPropertyValue4("自定义内容4");
            commodityInput.setCreateDate(sdf.parse(date));
            commodityInput.setCreateDatetime(sdf.parse(date));
            commodityInput.setUpdateDatetime(sdf.parse(date));
            commodityInput.setProviderIDs("1");
            commodityInput.setOperatorStaffID(1);
            commodityInput.setShelfLife(-1);//有效天数
            commodityInput.setPurchaseFlag(-1);
            commodityInput.setRuleOfPoint(-1);
            commodityInput.setRefCommodityID(0);
            commodityInput.setRefCommodityMultiple(0);

            List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();
            SubCommodity subCommodity1 = new SubCommodity();
            subCommodity1.setSubCommodityNO(2);
            subCommodity1.setSubCommodityID(2);
            subCommodity1.setPrice(3);
            subCommodities.add(subCommodity1);

            SubCommodity subCommodity2 = new SubCommodity();
            subCommodity2.setSubCommodityNO(3);
            subCommodity2.setSubCommodityID(3);
            subCommodity2.setPrice(3);
            subCommodities.add(subCommodity2);

            commodityInput.setListSlave1(subCommodities);
            return (Commodity) commodityInput.clone();
        }

        //生成一个多包装商品
        public static final Commodity getMultiPackagingCommodity(Double priceRetail) throws CloneNotSupportedException, InterruptedException, ParseException {
            commodityInput = new Commodity();
            commodityInput.setStatus(Commodity.EnumStatusCommodity.ESC_Normal.getIndex());
            commodityInput.setBarcode("20200428" + System.currentTimeMillis() % 1000000);
            commodityInput.setName("多包装商品" + System.currentTimeMillis() % 1000000);
            Thread.sleep(1000);
            commodityInput.setShortName("薯片");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setBrandID(1);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
            commodityInput.setPriceRetail(priceRetail);
            commodityInput.setPriceVIP(priceRetail);
            commodityInput.setPriceWholesale(priceRetail);
            commodityInput.setCanChangePrice(1);
            commodityInput.setRuleOfPoint(1);
            commodityInput.setShelfLife(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1)) + 1);//
            commodityInput.setPicture("url=116843435555");
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
            commodityInput.setTag("111");
            commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
            commodityInput.setOperatorStaffID(STAFF_ID4);
            commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
            commodityInput.setStartValueRemark("");
            Thread.sleep(1000);
            commodityInput.setPropertyValue1("自定义属性1");
            commodityInput.setPropertyValue2("自定义属性2");
            commodityInput.setPropertyValue3("自定义属性3");
            commodityInput.setPropertyValue4("自定义属性4");
            commodityInput.setPurchasingUnit("箱");
            commodityInput.setProviderIDs("7,2,3");
            commodityInput.setLatestPricePurchase(1000000);
            commodityInput.setReturnObject(BaseModel.EnumBoolean.EB_Yes.getIndex());
            commodityInput.setRefCommodityID(1); // ...
            commodityInput.setRefCommodityMultiple(2);
            commodityInput.setMultiPackagingInfo("251135499823755628" + "," + "222" + System.currentTimeMillis()%1000000 + ",3332" + System.currentTimeMillis()%1000000 + ";1,2,3;1,1,0;1,5,10;8,3,8;8,8,8;" //
                    + "商品A" + System.currentTimeMillis()%1000000 + "," + "商品B" + System.currentTimeMillis()%1000000 + "," + "商品C" + System.currentTimeMillis()%1000000 + ";");
            commodityInput.setProviderIDs("1");

            return (Commodity) commodityInput.clone();
        }

        //生成一个服务型商品
        public static final Commodity getServiceCommodity(Double priceRetail) throws Exception {
            commodityInput = new Commodity();
            commodityInput.setStatus(Commodity.EnumStatusCommodity.ESC_Normal.getIndex());
            commodityInput.setBarcode("20200428" + System.currentTimeMillis() % 1000000);
            commodityInput.setName("服务型商品" + System.currentTimeMillis() % 1000000);
            Thread.sleep(1000);
            commodityInput.setShortName("商品");
            commodityInput.setSpecification("克");
            commodityInput.setPackageUnitID(1);
            commodityInput.setBrandID(1);
            commodityInput.setCategoryID(1);
            commodityInput.setMnemonicCode("SP");
            commodityInput.setPricingType(1);
            commodityInput.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
            commodityInput.setPriceRetail(priceRetail); // 零售价
            commodityInput.setPriceVIP(priceRetail); // 会员价
            commodityInput.setPurchasingUnit("");
            commodityInput.setPriceWholesale(priceRetail); // 批发价
            // commodityInput.setRatioGrossMargin(Math.abs(new Random().nextDouble()));
            commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
            commodityInput.setRuleOfPoint(1);
            commodityInput.setPicture("url=116843435555");
            commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
            commodityInput.setPurchaseFlag(Commodity.DEFAULT_VALUE_PurchaseFlag);//
            commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
            commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
            // commodityInput.setIsGift(Math.abs(new Random().nextInt(1)));
            commodityInput.setTag("111");
            commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
            commodityInput.setOperatorStaffID(1);
            // commodityInput.setNOAccumulated(Math.abs(new Random().nextInt(18000)));
            commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
            commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
            commodityInput.setType(CommodityType.EnumCommodityType.ECT_Service.getIndex());
            commodityInput.setStartValueRemark("");
            Thread.sleep(1000);
            commodityInput.setPropertyValue1("自定义属性1");
            commodityInput.setPropertyValue2("自定义属性2");
            commodityInput.setPropertyValue3("自定义属性3");
            commodityInput.setPropertyValue4("自定义属性4");
            commodityInput.setReturnObject(1); // 返回对象的值
            commodityInput.setProviderIDs("1");

            commodityInput.setMultiPackagingInfo(commodityInput.getBarcode() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" +
                    commodityInput.getPriceRetail() + ";" + commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
            return (Commodity) commodityInput.clone();
        }
    }

    public static void createSyncViaSQLite(Commodity comm) {
        //将Commodity插入到本地SQLite
        presenter.createObjectSync(BaseSQLiteBO.INVALID_CASE_ID, comm);
        //
        Assert.assertTrue("createSync返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        //
        comm = (Commodity) presenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, comm);
        Assert.assertTrue("retrieve1返回的错误码不正确!", presenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("插入Commodity失败!", comm.compareTo(comm) == 0);
    }

    /**
     * 调用者必须实现@Subscriber onXXXXEvent()
     */
    public static void createSyncViaHttp(CommodityHttpBO commodityHttpBO, CommoditySQLiteBO commoditySQLiteBO, Commodity comm) throws CloneNotSupportedException, InterruptedException {
        int caseID = 0;
        //
        commoditySQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_CreateAsync);
        //
        commodityHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);

        if (comm.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
            caseID = BaseHttpBO.INVALID_CASE_ID;
        } else if (comm.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
            caseID = BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging;
        } else if (comm.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
            caseID = BaseSQLiteBO.CASE_Commodity_CreateComposition;
        } else {
            caseID = BaseSQLiteBO.CASE_Commodity_CreateService;
        }

        if (!commodityHttpBO.createAsync(caseID, comm)) {
            Assert.assertTrue("Commodity创建失败！", false);
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commodityHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            Assert.assertTrue("commodity创建超时！", false);
        }
        //
        Assert.assertTrue("请求创建commodity之后，服务器返回的错误码不正确", commodityHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
    }

    public static BaseModel retrieveNSyncInSQLite(CommoditySQLiteBO commoditySQLiteBO, CommoditySQLiteEvent commoditySQLiteEvent, Commodity comm) throws InterruptedException {
        commoditySQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        commoditySQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_Commodity_RetrieveNAsync);
        if (!commoditySQLiteBO.retrieveNAsync(BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions, comm)) {
            Assert.assertTrue("RN查询失败！", false);
        }
        long lTimeOut = UNIT_TEST_TimeOut;
        while (commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done &&
                commoditySQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_ToDo) {
            Assert.assertTrue("retrieveNAsync超时！", false);
        }
        //
        Assert.assertTrue("根据条件retrieveN 返回的错误码不正确!", commoditySQLiteEvent.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        Assert.assertTrue("根据条件retrieveN 应该要有数据返回!", commoditySQLiteEvent.getListMasterTable().size() != 0);

        List<Commodity> commodityLists = (List<Commodity>) commoditySQLiteEvent.getListMasterTable();
        return commodityLists.get(0);
    }

}
