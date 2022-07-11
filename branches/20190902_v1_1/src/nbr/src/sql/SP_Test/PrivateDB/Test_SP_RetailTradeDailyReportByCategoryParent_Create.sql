SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportByCategoryParent_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:有销售记录且包含多个商品大类 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2091/10/17 00:00:00';
SET @deleteOldData=1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @ResultVerification1=0;
SET @ResultVerification2=0;
SET @iCategoryParentID = 1;
SET @iCategoryParentID2 = 2;
SET @dPriceCommodity1=45;
SET @dNoCommodity1=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建分类2
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test12', @iCategoryParentID2, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test1', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可口可乐Test1', '饮料', '毫升', 1, '箱', 1, @categoryID2, 'KK', 1, 
3.3, 3, 1, 1, NULL, 3, 30, '04/14/2018', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();

-- 新建零售单 售90
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041228', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 新建零售单C的零售商品 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test1', 3, @dNoCommodity1, 45, 45, @dPriceCommodity1, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建零售单 售50
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041229', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();

-- 新建A的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '可口可乐Test1', 1, @dNoCommodity2, 10, 10, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = (@dPriceCommodity1 * @dNoCommodity1) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 and F_TotalAmount = (@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1, '测试成功','测试失败') AS 'Test Case1 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


-- 因时间和商品大类ID为组合唯一键，第二次插入会数据库错误	
SELECT '-------------------- Case2:有销售记录且包含一个商品大类 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2019/4/18 15:30:30';
SET @deleteOldData=0;
SET @ResultVerification=0;
SET @iCategoryParentID = 1;
SET @dPriceCommodity=45.5;
SET @dNoCommodity=3;
-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test1', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 新建零售单 售136.5
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041228', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 新建零售单C的零售商品 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test1', 3, @dNoCommodity, 45, 45, @dPriceCommodity, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = (@dNoCommodity * @dPriceCommodity) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case2 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case3:零售单有销售记录且包含退货记录 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2091/1/18 17:40:34';
SET @deleteOldData=0;
SET @ResultVerification=0;
SET @iCategoryParentID = 1;
SET @dPriceCommodity=45.5;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=45.5;
SET @dReturnNoCommodity=1;
-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test1', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 新建零售单 售90
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300051228', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 新建零售单的零售商品 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test1', 3, @dNoCommodity, 45, 45, @dPriceCommodity, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单 退45.5
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300051228_1', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 新建零售单的零售商品 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test1', 3, @dReturnNoCommodity, 45, 45, @dReturnPriceCommodity, 200, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = ((@dNoCommodity * @dPriceCommodity) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case3 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case4:零售单无记录添加 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2029/1/13 17:40:34';
SET @deleteOldData=0;
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(found_rows()=0 AND @iErrorCode = 7,'测试成功','测试失败') AS 'Test Case4 Result';


SELECT '-------------------- Case5:3月2号分类1有零售单A,销售总额为50，退货单B,B对A退货30，那么这一天分类1的销售总额是20 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @retailAmount = 0;
SET @dPriceCommodity=25;
SET @dNoCommodity=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=1;
SET @ResultVerification=0;
-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test5', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test5', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test5', 1, @dNoCommodity, 321, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B，B对A退货30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test5', 1, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT F_TotalAmount INTO @retailAmount FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = ((@dNoCommodity * @dPriceCommodity) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1 ,'测试成功','测试失败') AS 'Test Case5 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case6:3月2号分类1有零售单A,销售总额为50，退货单B,B对3月1号的零售单C退货30，那么这一天分类1的销售总额是20 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @dPriceCommodity=30;
SET @dNoCommodity=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=2;
SET @ResultVerification=0;

-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test61', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test61', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 新建零售单C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- 新建零售单C的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID1, '可比克薯片Test6', 1, @dNoCommodity, 321, 500, @dPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityCID = last_insert_id();
-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test6', 1, @dNoCommodity2, 321, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B，B对3月1号的零售单C退货30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test6', 1, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount =((@dNoCommodity2 * @dPriceCommodity2) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1,'测试成功','测试失败') AS 'Test Case6 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case7:3月2号分类1有零售单A,销售总额为50，退货单B,B对3月1号的零售单C退货总额100，那么这一天分类1的销售总额就为-50 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @retailAmount = 0;
SET @dPriceCommodity=100;
SET @dNoCommodity=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=50;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=2;
SET @ResultVerification=0;

-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test7', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test7', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 新建零售单C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- 新建零售单C的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID1, '可比克薯片Test7', 1, @dNoCommodity, 321, 500, 100, 300, NULL);
SET @returnRetailTradeCommodityCID = last_insert_id();
-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test7', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B，B对3月1号的零售单C退货100
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test7', 1, 2, 321, 500, 50, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount =((@dNoCommodity2 * @dPriceCommodity2) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case7 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case8:3月2号分类X有零售单A,销售总额为50，分类2无零售，退货单B,B对3月1号分类Y零售单D退货30，那么这一天分类X的销售总额是50，分类Y销售总额-30 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @dPriceCommodity=200;
SET @dNoCommodity=100;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=12;
SET @iCategoryParentID2=11;
SET @ResultVerification=0;
SET @ResultVerification2=0;

-- 新建分类1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test81', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建分类2
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test82', @iCategoryParentID2, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test8', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可口可乐Test8', '饮料', '毫升', 1, '箱', 1, @categoryID2, 'KK', 1, 
3.3, 3, 1, 1, NULL, 3, 30, '04/14/2018', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();

-- 新建零售单C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- 新建零售单C的零售商品

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID2, '可口可乐', 3, @dNoCommodity, 254, 100, @dPriceCommodity, 200, NULL);


SET @returnRetailTradeCommodityCID = last_insert_id();
-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建A的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可口可乐Test8', 1, @dNoCommodity2, 321, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B，B对3月1号的分类Y零售单C退货30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可口可乐Test8', 2, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 AND F_Datetime = @dSaleDatetime;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2  AND F_TotalAmount=-(@dReturnPriceCommodity * @dReturnNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1 AND @ResultVerification2 = 1,'测试成功','测试失败') AS 'Test Case8_1 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case9: 当天只售卖一种类别的商品，查看数据统计和报表显示是否正常。 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=3.14159;
SET @dNoCommodity=2;
SET @ResultVerification=0;

-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test91', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test91', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 2, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test91', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity*@dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case9 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case10: 当天只售卖一种类别的商品，部分退货，查看数据统计和报表显示是否正常。 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=213.314;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=213.314;
SET @dReturnNoCommodity=1;
SET @ResultVerification=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test92', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test10', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test10', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- 新建退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test10', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=((@dPriceCommodity*@dNoCommodity) - (@dReturnNoCommodity*@dReturnPriceCommodity))AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1 ,'测试成功','测试失败') AS 'Test Case10 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;
	
SELECT '-------------------- Case11: 当天只售卖一种类别的商品，全部退货，查看数据统计和报表显示是否正常。 -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=20.3141235123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=20.3141235123;
SET @dReturnNoCommodity=3;
SET @ResultVerification=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test11', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test11', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test11', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- 新建退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test11', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=((@dPriceCommodity*@dNoCommodity) - (@dReturnNoCommodity*@dReturnPriceCommodity)) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case11 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case12: 当天只退货一种类别的商品部分退，不进行销售 -------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=2019.314123;
SET @dReturnNoCommodity=2;
SET @ResultVerification=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test12', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test12', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建一张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test12', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- 新建一张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test12', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=-(@dReturnNoCommodity * @dReturnPriceCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case12 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '--------------------case13: 当天只退货一种类别的商品全部退，不进行销售 -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/02/00 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=2019.314123;
SET @dReturnNoCommodity=3;
SET @ResultVerification=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test13', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test13', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建一张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test13', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- 新建一张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test13', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=-(@dReturnNoCommodity * @dReturnPriceCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'测试成功','测试失败') AS 'Test Case13 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case14:当天售卖多种类别的商品，单张零售单 -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @deleteOldData=1;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test14', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test141', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test14', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test141', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test14', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test141', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity * @dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1 ,'测试成功','测试失败') AS 'Test Case14 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '--------------------Case15: 当天售卖多种类别的商品，单张零售单(特殊case:当传递的数值为大于小数点后6位时产生的误差应小于等于0.000001) -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @deleteOldData=1;
SET @dPriceCommodity=2019.314120212;
SET @dNoCommodity=3;
SET @dPriceCommodity2=2019.314120212;
SET @dNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
SET @dTOLERANCE = 0.000001;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test15', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test151', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test15', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test151', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test15', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test151', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT F_TotalAmount INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_Datetime = @dSaleDatetime;
SELECT F_TotalAmount INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND (@ResultVerification1 - (@dPriceCommodity * @dNoCommodity)) <= @dTOLERANCE  AND (@ResultVerification2 - (@dPriceCommodity2 * @dNoCommodity2)) <= @dTOLERANCE,'测试成功','测试失败') AS 'Test Case14 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


SELECT '--------------------Case16: 当天售卖多种类别的商品，多张零售单 -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=2;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=1;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test16', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test161', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test16', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test161', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建第一张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test16', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test161', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- 创建第二张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '可比克薯片Test16', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '可比克薯片Test161', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=2*(@dPriceCommodity * @dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=2*(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case16 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case17:当天售卖多种类别的商品，单张零售单，部分退货 -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=4;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=2020.3145;
SET @dReturnNoCommodity1=1;
SET @dReturnPriceCommodity2=2019.314123;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test17', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test171', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test17', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test171', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test17', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test171', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- 创建退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test17', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可比克薯片Test171', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity * @dNoCommodity) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case17 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '--------------------Case18: 当天售卖多种类别的商品，单张零售单，全部退货 -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @retailAmount1 = 0;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=2019.314123;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=2019.314123;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=2020.3145;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test18', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test181', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType,
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test18', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test181', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test18', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test181', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- 创建退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test18', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可比克薯片Test181', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity1 * @dNoCommodity1) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case18 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case19:当天售卖多种类别的商品，多张零售单，部分退货 -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=20.31;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=22.31459;
SET @dNoCommodity3=2;
SET @dReturnPriceCommodity1=20.31;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=22.31459;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test19', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test191', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test19', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111',0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test191', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建第一张零售单 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test19', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test191', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- 创建第二张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '可比克薯片Test19', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '可比克薯片Test191', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
-- 创建第一张零售单的退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test19', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可比克薯片Test191', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(2*(@dPriceCommodity1 * @dNoCommodity1)) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(2*(@dPriceCommodity2 * @dNoCommodity2)) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case19 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case20: 当天售卖多种类别的商品，多张零售单，全部退货 -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=220.31;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=222.31459;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=220.31;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=222.31459;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test20', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test171', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test20', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType,
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test201', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建第一张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test20', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '可比克薯片Test201', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- 创建第二张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '可比克薯片Test20', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '可比克薯片Test201', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
-- 创建第一张零售单的退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test20', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可比克薯片Test201', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
-- 创建第二张零售单的退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID2, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '可比克薯片Test20', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '可比克薯片Test201', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(2*(@dPriceCommodity1 * @dNoCommodity1)) - 2*(@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(2*(@dPriceCommodity2 * @dNoCommodity2)) - 2*(@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case20 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case21: 当天只退货多种类别的商品部分退，不进行销售 -------------------------' AS 'Case21';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=220.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=3;
SET @dPriceCommodity2=222.31459;
SET @dNoCommodity3=2;
SET @dNoCommodity4=2;
SET @dReturnPriceCommodity1=220.321;
SET @dReturnNoCommodity1=2;
SET @dReturnPriceCommodity2=222.31459;
SET @dReturnNoCommodity2=1;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test21', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test211', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test21', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test211', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建第一张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID1, '可比克薯片Test21', 1, @dNoCommodity1, 20, 500, 20, @dPriceCommodity1, NULL);
SET @retailTradeCommodityIDYesD = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID2, '可比克薯片Test211', 1, @dNoCommodity3, 20, 500, 20, @dPriceCommodity2, NULL);
SET @retailTradeCommodityIDYesD2 = last_insert_id();
-- 创建第二张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID1, '可比克薯片Test21', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID2, '可比克薯片Test211', 1, @dNoCommodity4, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD4 = last_insert_id();
	
-- 新建第一张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test21', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
-- 新建第二张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989_1', 6, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '可比克薯片Test211', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount= - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount= - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case21 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;		
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case22:当天只退货多种类别的商品全部退，不进行销售 -------------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=2.4123;
SET @dNoCommodity1=4;
SET @dNoCommodity2=3;
SET @dPriceCommodity2=2.7543;
SET @dNoCommodity3=2;
SET @dNoCommodity4=2;
SET @dReturnPriceCommodity1=2.4123;
SET @dReturnNoCommodity1=4;
SET @dReturnNoCommodity2=3;
SET @dReturnPriceCommodity2=2.7543;
SET @dReturnNoCommodity3=2;
SET @dReturnNoCommodity4=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test22', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test221', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test22', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test221', '薯片', '克', 1, '箱', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 创建第一张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID1, '可比克薯片Test22', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID2, '可比克薯片Test221', 1, @dNoCommodity3, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD2 = last_insert_id();
-- 创建第二张假定为昨天时间零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID1, '可比克薯片Test22', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID2, '可比克薯片Test221', 1, @dNoCommodity3, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD4 = last_insert_id();
	
-- 新建第一张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test22', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '可比克薯片Test221', 1, @dReturnNoCommodity3, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
-- 新建第二张假定为今天时间退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989_1', 6, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '可比克薯片Test22', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '可比克薯片Test221', 1, @dReturnNoCommodity4, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount= - ((@dReturnNoCommodity1 * @dReturnPriceCommodity1) +(@dReturnNoCommodity2 * @dReturnPriceCommodity1)) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount= - ((@dReturnNoCommodity3 * @dReturnPriceCommodity2) + (@dReturnNoCommodity4 * @dReturnPriceCommodity2)) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'测试成功','测试失败') AS 'Test Case22 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;		
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


SELECT '--------------------Case23: 当天只售卖一种类别的商品，多张零售单，部分退货-------------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity1=21.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=21;
SET @dReturnNoCommodity1=1;
SET @ResultVerification1=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test23', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test23', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建第一张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test23', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 创建第二张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '可比克薯片Test23', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
	
-- 新建退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test23', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @dTotalAmount=((@dNoCommodity1 * @dPriceCommodity1) +(@dNoCommodity2 * @dPriceCommodity1)) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1);	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=@dTotalAmount AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1,'测试成功','测试失败') AS 'Test Case23 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '--------------------Case24: 当天只售卖一种类别的商品，多张零售单，全部退货-------------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity1=21.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=21.321;
SET @dReturnNoCommodity1=4;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
	
-- 新建分类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test24', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 新建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片Test24', '薯片', '克', 1, '箱', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '10/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- 创建第一张零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '可比克薯片Test24', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 创建第二张零售
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 创建零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '可比克薯片Test24', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
	
-- 新建退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '可比克薯片Test24', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

-- 新建退货单进行退货
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989_1', 3, 7, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- 创建退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '可比克薯片Test24', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @dTotalAmount=((@dNoCommodity1 * @dPriceCommodity1) +(@dNoCommodity2 * @dPriceCommodity1)) - ((@dReturnNoCommodity1 * @dReturnPriceCommodity1)+(@dReturnNoCommodity2 * @dReturnPriceCommodity1));
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=@dTotalAmount AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1,'测试成功','测试失败') AS 'Test Case24 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;
	