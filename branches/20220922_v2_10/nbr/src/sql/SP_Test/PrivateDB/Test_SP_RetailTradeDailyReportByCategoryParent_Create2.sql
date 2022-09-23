SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportByCategoryParent_Create2.sql ++++++++++++++++++++';
--	当月1至3号只售卖同一类别的商品（都售卖类别1），
--	4至6号各售卖一种类别的商品（分别售卖类别1、类别2、类别3），
--	7至9号无销售数据，
--	10至12号各售卖互不相同的多种类别的商品（分别售卖类别1-3，类别4-6，类别7-9），
--	13至15号售卖相同的多种类别的商品（都售卖类别1-3），
--	16至18号只退货同一类别的商品（都退货类别1），
--	19至21号各退货一种类别的商品（分别退货类别1、类别2、类别3），
--	22至24号各退货互不相同的多种类别的商品（分别退货类别1-3，类别4-6，类别7-9），
--	25至27号退货相同的多种类别的商品（都退货类别1-3），
--	28至30号售卖和退货多种类别的商品（分别售卖和退货类别1-2，类别2-3，类别3-4）
SELECT '-------------------- Case1:当月1号售卖类别一的商品(无退货) -------------------------' AS 'Case1';
-- 创建类别一的商品数据
SET @iCategoryParentID1 = 2; 
SET @iCategoryID_CategoryParent1 = 0;
SET @iSingleCommodityID_CategoryParent1 = 0;
SET @dPrice_SingleCommodity_CategoryParent1 = 3.5;
SET @iSingleCommodityID2_CategoryParent1 = 0;
SET @dPrice_SingleCommodity2_CategoryParent1 = 3.5;
SET @iCompositionCommodityID_CategoryParent1 = 0;
SET @dPrice_CompositionCommodity_CategoryParent1 = 21;
SET @iMultiPackagingCommodityID_CategoryParent1 = 0;
SET @iRefCommodityMultiple_CategoryParent1 = 24;
SET @dPrice_MultiPackagingCommodity_CategoryParent1 = 84;
SET @iServiceCommodityID_CategoryParent1 = 0;
SET @dPrice_ServiceCommodity_CategoryParent1 = 5;
-- 创建类别一的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP1', @iCategoryParentID1, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent1 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可口可乐_CategoryParent1', '可口可乐', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent1, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent1 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '百事可乐_CategoryParent1', '百事可乐', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent1, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent1 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可乐大礼包_CategoryParent1', '可乐', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent1, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent1 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent1, @iSingleCommodityID_CategoryParent1, 3, @dPrice_SingleCommodity_CategoryParent1);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent1, @iSingleCommodityID2_CategoryParent1, 3, @dPrice_SingleCommodity2_CategoryParent1);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一听百事可乐_CategoryParent1', '百事可乐', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent1, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID2_CategoryParent1, @iRefCommodityMultiple_CategoryParent1, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent1 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent1','kd','个',4,NULL,4,@iCategoryID_CategoryParent1,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递A',3);
SET @iServiceCommodityID_CategoryParent1 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test1 ='2099/06/01 08:08:08';
SET @deleteOldData=1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iNO_SingleCommodity_Test1 = 3;
SET @iNO_SingleCommodity2_Test1 = 5;
SET @iNO_CompositionCommodity_Test1 = 2;
SET @iNO_MultiPackagingCommodity_Test1 = 3;
SET @iNO_ServiceCommodity_Test1 = 1;
SET @iRetailTradeID1_Test1 = 0;
SET @iRetailTradeID2_Test1 = 0;
SET @iResultVerification_Test1 = 0;
-- 创建零售单1 (售卖3件普通商品1、2件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test1, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test1, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test1 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test1, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_Test1, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test1, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test1, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_Test1, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test1, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖5件普通商品2、3件多包装商品、1件服务商品。共274.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test1, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test1, 274.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test1 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test1, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test1, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_Test1, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test1, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_Test1, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test1, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test1, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d'); -- 
SET @dRetailTradeID1_TotalAmount_Test1 = (@iNO_SingleCommodity_Test1 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_Test1 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test1 = (@iNO_MultiPackagingCommodity_Test1 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iNO_SingleCommodity2_Test1 * @dPrice_SingleCommodity2_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test1 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount =  @dRetailTradeID1_TotalAmount_Test1 +  @dRetailTradeID2_TotalAmount_Test1 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test1;
SELECT IF(@iResultVerification_Test1 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case1 Result';




SELECT '-------------------- Case2:当月2号售卖类别一的商品(无退货) -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test2 ='2099/06/02 08:08:08';
SET @iNO_SingleCommodity_Test2 = 1;
SET @iNO_SingleCommodity2_Test2 = 1;
SET @iNO_CompositionCommodity_Test2 = 1;
SET @iNO_MultiPackagingCommodity_Test2 = 2;
SET @iNO_ServiceCommodity_Test2 = 1;
SET @iRetailTradeID1_Test2 = 0;
SET @iRetailTradeID2_Test2 = 0;
SET @iResultVerification_Test2 = 0;
-- 创建零售单1 (售卖1件普通商品1、2件多包装商品。共171.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000001', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test2, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test2, 171.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test2 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test2, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_Test2, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test2, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test2, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test2, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test2, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品2、1件组合商品、1件服务商品。共29.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000002', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test2, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test2, 29.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test2 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_Test2, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test2, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_Test2, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test2, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_Test2, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test2, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test2, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test2 = (@iNO_SingleCommodity_Test2 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_MultiPackagingCommodity_Test2 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test2 = (@iNO_CompositionCommodity_Test2 * @dPrice_CompositionCommodity_CategoryParent1) + (@iNO_SingleCommodity2_Test2 * @dPrice_SingleCommodity2_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test2 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test2 + @dRetailTradeID2_TotalAmount_Test2 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d'); --  

SELECT @iResultVerification_Test2;
SELECT IF(@iResultVerification_Test2 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case2 Result';



SELECT '-------------------- Case3:当月3号售卖类别一的商品(无退货) -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test3 ='2099/06/03 08:08:08';
SET @iNO_SingleCommodity_Test3 = 3;
SET @iNO_SingleCommodity2_Test3 = 1;
SET @iNO_CompositionCommodity_Test3 = 1;
SET @iNO_MultiPackagingCommodity_Test3 = 1;
SET @iNO_ServiceCommodity_Test3 = 1;
SET @iRetailTradeID1_Test3 = 0;
SET @iRetailTradeID2_Test3 = 0;
SET @iResultVerification_Test3 = 0;
-- 创建零售单1 (售卖3件普通商品1、1件服务商品。共15.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000001', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test3, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test3, 15.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test3 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test3, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_Test3, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test3, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test3, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_Test3, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test3, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品2、1件组合商品、1件多包装商品。共108.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000002', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test3, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test3, 108.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test3 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_Test3, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test3, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_Test3, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test3, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test3, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test3, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test3, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test3 = (@iNO_SingleCommodity_Test3 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_ServiceCommodity_Test3 * @dPrice_ServiceCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test3 = (@iNO_SingleCommodity2_Test3 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_CompositionCommodity_Test3 * @dPrice_CompositionCommodity_CategoryParent1) -- 
											+ (@iNO_MultiPackagingCommodity_Test3 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test3 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test3 +  @dRetailTradeID2_TotalAmount_Test3 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test3;
SELECT IF(@iResultVerification_Test3 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case3 Result';



SELECT '-------------------- Case4:当月4号售卖同一类别的商品(无退货) -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test4 ='2099/06/04 08:08:08';
SET @iNO_SingleCommodity_Test4 = 5;
SET @iNO_SingleCommodity2_Test4 = 1;
SET @iNO_CompositionCommodity_Test4 = 1;
SET @iNO_MultiPackagingCommodity_Test4 = 1;
SET @iNO_ServiceCommodity_Test4 = 1;
SET @iRetailTradeID1_Test4 = 0;
SET @iRetailTradeID2_Test4 = 0;
SET @iResultVerification_Test4 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件组合商品。共38.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000001', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test4, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test4, 28.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test4 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test4, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_Test4, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test4, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test4, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_Test4, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test4, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品2、1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000002', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test4, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test4, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test4 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_Test4, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test4, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test4, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test4, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_Test4, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test4, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test4, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test4 = (@iNO_SingleCommodity_Test4 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_Test4 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test4 = (@iNO_SingleCommodity2_Test4 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_MultiPackagingCommodity_Test4 * @dPrice_MultiPackagingCommodity_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test4 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test4 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test4 + @dRetailTradeID2_TotalAmount_Test4 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test4;
SELECT IF(@iResultVerification_Test4 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case4 Result';




SELECT '-------------------- Case5:当月5号售卖类别二的商品(无退货) -------------------------' AS 'Case5';
-- -- 创建类别二的商品数据
SET @iCategoryParentID2 = 3; 
SET @iCategoryID_CategoryParent2 = 0;
SET @iSingleCommodityID_CategoryParent2 = 0;
SET @dPrice_SingleCommodity_CategoryParent2 = 4.99;
SET @iSingleCommodityID2_CategoryParent2 = 0;
SET @dPrice_SingleCommodity2_CategoryParent2 = 4.99;
SET @iCompositionCommodityID_CategoryParent2 = 0;
SET @dPrice_CompositionCommodity_CategoryParent2 = 49.9;
SET @iMultiPackagingCommodityID_CategoryParent2 = 0;
SET @iRefCommodityMultiple_CategoryParent2 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent2 = 9.98;
SET @iServiceCommodityID_CategoryParent2 = 0;
SET @dPrice_ServiceCommodity_CategoryParent2 = 5;
-- 创建类别二的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP2', @iCategoryParentID2, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent2 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '乐事薯片_CategoryParent2', '乐事薯片', '包', 1, '箱', 3, @iCategoryID_CategoryParent2, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent2 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片_CategoryParent2', '可比克薯片', '包', 1, '箱', 3, @iCategoryID_CategoryParent2, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent2 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '薯片大礼包_CategoryParent2', '薯片', '包', 1, '箱', 3, @iCategoryID_CategoryParent2, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent2 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent2, @iSingleCommodityID_CategoryParent2, 5, @dPrice_SingleCommodity_CategoryParent2);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent2, @iSingleCommodityID2_CategoryParent2, 5, @dPrice_SingleCommodity2_CategoryParent2);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一箱乐事薯片_CategoryParent2', '百事薯片', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent2, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID_CategoryParent2, @iRefCommodityMultiple_CategoryParent2, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent2 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent2','kd','个',4,NULL,4,@iCategoryID_CategoryParent2,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递B',3);
SET @iServiceCommodityID_CategoryParent2 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test5 ='2099/06/05 08:08:08';
SET @iNO_SingleCommodity_Test5 = 2;
SET @iNO_SingleCommodity2_Test5 = 2;
SET @iNO_CompositionCommodity_Test5 = 1;
SET @iNO_MultiPackagingCommodity_Test5 = 2;
SET @iNO_ServiceCommodity_Test5 = 1;
SET @iRetailTradeID1_Test5 = 0;
SET @iRetailTradeID2_Test5 = 0;
SET @iResultVerification_Test5 = 0;
-- 创建零售单1 (售卖2件普通商品2、2件多包装商品。共29.94元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000001', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test5, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test5, 29.96, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test5 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test5, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_Test5, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_Test5, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test5, @iMultiPackagingCommodityID_CategoryParent2, '一箱百事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_Test5, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_Test5, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖2件普通商品1、1件组合商品、1件服务商品。共64.88元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000002', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test5, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test5, 64.88, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test5 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_Test5, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_Test5, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_Test5, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_Test5, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_Test5, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_Test5, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test5, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test5 = (@iNO_SingleCommodity2_Test5 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_MultiPackagingCommodity_Test5 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_Test5 = (@iNO_SingleCommodity_Test5 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_CompositionCommodity_Test5 * @dPrice_CompositionCommodity_CategoryParent2)
											+ (@iNO_ServiceCommodity_Test5 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_Test5 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test5 + @dRetailTradeID2_TotalAmount_Test5 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test5;
SELECT IF(@iResultVerification_Test5 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case5 Result';





SELECT '-------------------- Case6:当月6号售卖类别三的商品(无退货) -------------------------' AS 'Case6';
-- -- 创建类别三的商品数据
SET @iCategoryParentID3 = 5; 
SET @iCategoryID_CategoryParent3 = 0;
SET @iSingleCommodityID_CategoryParent3 = 0;
SET @dPrice_SingleCommodity_CategoryParent3 = 3.88;
SET @iSingleCommodityID2_CategoryParent3 = 0;
SET @dPrice_SingleCommodity2_CategoryParent3 = 3.88;
SET @iCompositionCommodityID_CategoryParent3 = 0;
SET @dPrice_CompositionCommodity_CategoryParent3 = 23.28;
SET @iMultiPackagingCommodityID_CategoryParent3 = 0;
SET @iRefCommodityMultiple_CategoryParent3 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent3 = 19.4;
SET @iServiceCommodityID_CategoryParent3 = 0;
SET @dPrice_ServiceCommodity_CategoryParent3 = 5;
-- 创建类别三的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP3', @iCategoryParentID3, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent3 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '伊利牛奶_CategoryParent3', '伊利牛奶', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent3, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent3 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '蒙牛牛奶_CategoryParent3', '蒙牛牛奶', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent3, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent3 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '牛奶大礼包_CategoryParent3', '牛奶', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent3, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent3 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent3, @iSingleCommodityID_CategoryParent3, 3, @dPrice_SingleCommodity_CategoryParent3);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent3, @iSingleCommodityID2_CategoryParent3, 3, @dPrice_SingleCommodity2_CategoryParent3);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一箱伊利牛奶_CategoryParent3', '伊利牛奶', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent3, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @dPrice_SingleCommodity_CategoryParent3, @iRefCommodityMultiple_CategoryParent3, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent3 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent3','kd','个',4,NULL,4,@iCategoryID_CategoryParent3,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递C',3);
SET @iServiceCommodityID_CategoryParent3 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test6 ='2099/06/06 08:08:08';
SET @iNO_SingleCommodity_Test6 = 2;
SET @iNO_SingleCommodity2_Test6 = 1;
SET @iNO_CompositionCommodity_Test6 = 1;
SET @iNO_MultiPackagingCommodity_Test6 = 1;
SET @iNO_ServiceCommodity_Test6 = 1;
SET @iRetailTradeID1_Test6 = 0;
SET @iRetailTradeID2_Test6 = 0;
SET @iResultVerification_Test6 = 0;
-- 创建零售单1 (售卖2件普通商品2、1件服务商品。共12.76元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000001', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test6, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test6, 12.76, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test6 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test6, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_Test6, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_Test6, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test6, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_Test6, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_Test6, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品1、1件组合商品、1件多包装商品。共46.56元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000002', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test6, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test6, 46.56, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test6 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_Test6, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_Test6, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_Test6, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_Test6, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_Test6, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_Test6, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test6, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test6 = (@iNO_SingleCommodity2_Test6 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_ServiceCommodity_Test6 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_Test6 = (@iNO_SingleCommodity_Test6 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_CompositionCommodity_Test6 * @dPrice_CompositionCommodity_CategoryParent3) -- 
											+ (@iNO_MultiPackagingCommodity_Test6 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_Test6 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test6 + @dRetailTradeID2_TotalAmount_Test6 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test6;
SELECT IF(@iResultVerification_Test6 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case6 Result';


SELECT '-------------------- Case7:当月7号没有零售商品(无退货) -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test7 ='2099/06/07 08:08:08';
SET @iResultVerification_Test7 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test7, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test7,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test7 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test7 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '当天零售笔数为0' ,'测试成功','测试失败') AS 'Test Case7 Result';



SELECT '-------------------- Case8:当月8号没有零售商品(无退货) -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test8 ='2099/06/08 08:08:08';
SET @iResultVerification_Test8 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test8, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test8,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test8 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test8 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '当天零售笔数为0' ,'测试成功','测试失败') AS 'Test Case8 Result';



SELECT '-------------------- Case9:当月9号没有零售商品(无退货) -------------------------' AS 'Case9';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test9 ='2099/06/09 08:08:08';
SET @iResultVerification_Test9 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test9, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test9,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test9 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test9 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '当天零售笔数为0' ,'测试成功','测试失败') AS 'Test Case9 Result';




SELECT '-------------------- Case10:当月10号零售分别类别一、类别二和类别三的商品(无退货) -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test10 ='2099/06/10 08:08:08';
-- 零售类别一的商品
SET @iNO_SingleCommodity_CP1_Test10 = 9;
SET @iNO_SingleCommodity2_CP1_Test10 = 1;
SET @iNO_CompositionCommodity_CP1_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test10 = 1;
SET @iNO_ServiceCommodity_CP1_Test10 = 1;
SET @iRetailTradeID1_CP1_Test10 = 0;
SET @iRetailTradeID2_CP1_Test10 = 0;
SET @iResultVerification_CP1_Test10 = 0;
-- 创建零售单1 (售卖9件普通商品1、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000001', 7, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test10 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test10, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test10, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test10, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test10, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test10, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test10, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品2、1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000002', 7, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test10 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test10, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test10, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test10, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test10, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test10, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test10, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 零售类别二的商品
SET @iNO_SingleCommodity_CP2_Test10 = 5;
SET @iNO_SingleCommodity2_CP2_Test10 = 3;
SET @iNO_CompositionCommodity_CP2_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test10 = 1;
SET @iNO_ServiceCommodity_CP2_Test10 = 1;
SET @iRetailTradeID1_CP2_Test10 = 0;
SET @iRetailTradeID2_CP2_Test10 = 0;
SET @iResultVerification_CP2_Test10 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000003', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test10 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test10, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test10, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test10, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test10, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test10, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test10, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件服务商品。共69.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000004', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test10 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test10, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test10, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test10, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test10, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test10, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test10, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- 零售类别三的商品
SET @iNO_SingleCommodity_CP3_Test10 = 5;
SET @iNO_SingleCommodity2_CP3_Test10 = 3;
SET @iNO_CompositionCommodity_CP3_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test10 = 1;
SET @iNO_ServiceCommodity_CP3_Test10 = 1;
SET @iRetailTradeID1_CP3_Test10 = 0;
SET @iRetailTradeID2_CP3_Test10 = 0;
SET @iResultVerification_CP3_Test10 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000005', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test10 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test10, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test10, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test10, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test10, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test10, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test10, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000006', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test10 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test10, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test10, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test10, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test10, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test10, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test10, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test10, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test10 = (@iNO_SingleCommodity_CP1_Test10 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test10 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test10 = (@iNO_SingleCommodity2_CP1_Test10 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test10 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test10 + @dRetailTradeID2_TotalAmount_CP1_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test10 = (@iNO_SingleCommodity_CP2_Test10 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test10 = (@iNO_SingleCommodity2_CP2_Test10 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test10 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test10 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test10 +  @dRetailTradeID2_TotalAmount_CP2_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test10 = (@iNO_SingleCommodity_CP3_Test10 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test10 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test10 = (@iNO_SingleCommodity2_CP3_Test10 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
												+ (@iNO_CompositionCommodity_CP3_Test10 * @dPrice_CompositionCommodity_CategoryParent3);  
SELECT 1 INTO @iResultVerification_CP3_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test10 + @dRetailTradeID2_TotalAmount_CP3_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test10, @iResultVerification_CP2_Test10, @iResultVerification_CP3_Test10;	 
SELECT IF(@iResultVerification_CP1_Test10 = 1 AND @iResultVerification_CP2_Test10 = 1 AND @iResultVerification_CP3_Test10 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case10 Result';


SELECT '-------------------- Case11:当月11号零售分别类别四、类别五和类别六的商品(无退货) -------------------------' AS 'Case11';
-- 创建类别四的商品数据
SET @iCategoryParentID4 = 6; 
SET @iCategoryID_CategoryParent4 = 0;
SET @iSingleCommodityID_CategoryParent4 = 0;
SET @dPrice_SingleCommodity_CategoryParent4 = 35.9;
SET @iSingleCommodityID2_CategoryParent4 = 0;
SET @dPrice_SingleCommodity2_CategoryParent4 = 52;
SET @iCompositionCommodityID_CategoryParent4 = 0;
SET @dPrice_CompositionCommodity_CategoryParent4 = 175.8;
SET @iMultiPackagingCommodityID_CategoryParent4 = 0;
SET @iRefCommodityMultiple_CategoryParent4 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent4 = 104;
SET @iServiceCommodityID_CategoryParent4 = 0;
SET @dPrice_ServiceCommodity_CategoryParent4 = 5;
-- 创建类别四的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP4', @iCategoryParentID4, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent4 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '威猛先生_CategoryParent4', '威猛先生', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent4, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent4 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '蓝月亮_CategoryParent4', '蓝月亮', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent4, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent4 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '清洁大礼包_CategoryParent4', '清洁', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent4, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent4 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent4, @iSingleCommodityID_CategoryParent4, 2, @dPrice_SingleCommodity_CategoryParent4);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent4, @iSingleCommodityID2_CategoryParent4, 2, @dPrice_SingleCommodity2_CategoryParent4);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一箱蓝月亮_CategoryParent4', '蓝月亮', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent4, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID2_CategoryParent4, @iRefCommodityMultiple_CategoryParent4, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent4 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent4','kd','个',4,NULL,4,@iCategoryID_CategoryParent4,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递D',3);
SET @iServiceCommodityID_CategoryParent4 = last_insert_id();
-- 创建类别五的商品数据 
SET @iCategoryParentID5 = 7; 
SET @iCategoryID_CategoryParent5 = 0;
SET @iSingleCommodityID_CategoryParent5 = 0;
SET @dPrice_SingleCommodity_CategoryParent5 = 99;
SET @iSingleCommodityID2_CategoryParent5 = 0;
SET @dPrice_SingleCommodity2_CategoryParent5 = 189;
SET @iCompositionCommodityID_CategoryParent5 = 0;
SET @dPrice_CompositionCommodity_CategoryParent5 = 576;
SET @iMultiPackagingCommodityID_CategoryParent5 = 0;
SET @iRefCommodityMultiple_CategoryParent5 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent5 = 378;
SET @iServiceCommodityID_CategoryParent5 = 0;
SET @dPrice_ServiceCommodity_CategoryParent5 = 5;
-- 创建类别五的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP5', @iCategoryParentID5, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent5 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '润泽玻尿酸面膜_CategoryParent5', '润泽玻尿酸面膜', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent5, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent5 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '雪晶灵肌密水_CategoryParent5', '雪晶灵肌密水', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent5, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent5 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '护肤大礼包_CategoryParent5', '护肤', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent5, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent5 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent5, @iSingleCommodityID_CategoryParent5, 2, @dPrice_SingleCommodity_CategoryParent5);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent5, @iSingleCommodityID2_CategoryParent5, 2, @dPrice_SingleCommodity2_CategoryParent5);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一箱润泽玻尿酸面膜_CategoryParent5', '润泽玻尿酸面膜', '盒', 1, '箱', 3, @iCategoryID_CategoryParent5, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID2_CategoryParent5, @iRefCommodityMultiple_CategoryParent5, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent5 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent5','kd','个',4,NULL,4,@iCategoryID_CategoryParent5,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递E',3);
SET @iServiceCommodityID_CategoryParent5 = last_insert_id();
-- 创建类别六的商品数据 
SET @iCategoryParentID6 = 8; 
SET @iCategoryID_CategoryParent6 = 0;
SET @iSingleCommodityID_CategoryParent6 = 0;
SET @dPrice_SingleCommodity_CategoryParent6 = 5.5;
SET @iSingleCommodityID2_CategoryParent6 = 0;
SET @dPrice_SingleCommodity2_CategoryParent6 = 5.5;
SET @iCompositionCommodityID_CategoryParent6 = 0;
SET @dPrice_CompositionCommodity_CategoryParent6 = 33;
SET @iMultiPackagingCommodityID_CategoryParent6 = 0;
SET @iRefCommodityMultiple_CategoryParent6 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent6 = 27.5;
SET @iServiceCommodityID_CategoryParent6 = 0;
SET @dPrice_ServiceCommodity_CategoryParent6 = 5;
-- 创建类别六的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP6', @iCategoryParentID6, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent6 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '老坛酸菜牛肉面_CategoryParent6', '老坛酸菜牛肉面', '桶', 1, '箱', 3, @iCategoryID_CategoryParent6, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent6 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '红烧牛肉面_CategoryParent6', '红烧牛肉面', '桶', 1, '箱', 3, @iCategoryID_CategoryParent6, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent6 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '桶面大礼包_CategoryParent6', '桶面', '桶', 1, '箱', 3, @iCategoryID_CategoryParent6, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent6 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent6, @iSingleCommodityID_CategoryParent6, 3, @dPrice_SingleCommodity_CategoryParent6);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent6, @iSingleCommodityID2_CategoryParent6, 3, @dPrice_SingleCommodity2_CategoryParent6);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一箱老坛酸菜牛肉面_CategoryParent6', '老坛酸菜牛肉面', '桶', 1, '箱', 3, @iCategoryID_CategoryParent6, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @dPrice_SingleCommodity_CategoryParent6, @iRefCommodityMultiple_CategoryParent6, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent6 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent6','kd','个',4,NULL,4,@iCategoryID_CategoryParent6,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递F',3);
SET @iServiceCommodityID_CategoryParent6 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test11 ='2099/06/11 08:08:08';
-- 零售类别四的商品
SET @iNO_SingleCommodity_CP4_Test11 = 2;
SET @iNO_SingleCommodity2_CP4_Test11 = 3;
SET @iNO_CompositionCommodity_CP4_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP4_Test11 = 1;
SET @iNO_ServiceCommodity_CP4_Test11 = 1;
SET @iRetailTradeID1_CP4_Test11 = 0;
SET @iRetailTradeID2_CP4_Test11 = 0;
SET @iResultVerification_CP4_Test11 = 0;
-- 创建零售单1 (售卖2件普通商品1、1件组合商品。共247.6元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000001', 8, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP4_Test11 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test11, @iSingleCommodityID_CategoryParent4, '威猛先生_CategoryParent4', 1, @iNO_SingleCommodity_CP4_Test11, @dPrice_SingleCommodity_CategoryParent4, @iNO_SingleCommodity_CP4_Test11, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test11, @iCompositionCommodityID_CategoryParent4, '清洁大礼包_CategoryParent4', 1, @iNO_CompositionCommodity_CP4_Test11, @dPrice_CompositionCommodity_CategoryParent4, @iNO_CompositionCommodity_CP4_Test11, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件多包装商品、1件服务商品。共265元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000002', 8, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 265, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP4_Test11 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iSingleCommodityID2_CategoryParent4, '蓝月亮_CategoryParent4', 1, @iNO_SingleCommodity2_CP4_Test11, @dPrice_SingleCommodity2_CategoryParent4, @iNO_SingleCommodity2_CP4_Test11, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iServiceCommodityID_CategoryParent4, '快递_CategoryParent4', 1, @iNO_ServiceCommodity_CP4_Test11, @dPrice_ServiceCommodity_CategoryParent4, @iNO_ServiceCommodity_CP4_Test11, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iMultiPackagingCommodityID_CategoryParent4, '一箱蓝月亮_CategoryParent4', 1, @iNO_MultiPackagingCommodity_CP4_Test11, @dPrice_MultiPackagingCommodity_CategoryParent4, @iNO_MultiPackagingCommodity_CP4_Test11, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);
-- 零售类别五的商品
SET @iNO_SingleCommodity_CP5_Test11 = 3;
SET @iNO_SingleCommodity2_CP5_Test11 = 4;
SET @iNO_CompositionCommodity_CP5_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP5_Test11 = 1;
SET @iNO_ServiceCommodity_CP5_Test11 = 1;
SET @iRetailTradeID1_CP5_Test11 = 0;
SET @iRetailTradeID2_CP5_Test11 = 0;
SET @iResultVerification_CP5_Test11 = 0;
-- 创建零售单1 (售卖3件普通商品1、1件多包装商品。共675元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000003', 8, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 675, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP5_Test11 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP5_Test11, @iSingleCommodityID_CategoryParent5, '润泽玻尿酸面膜_CategoryParent5', 1, @iNO_SingleCommodity_CP5_Test11, @dPrice_SingleCommodity_CategoryParent5, @iNO_SingleCommodity_CP5_Test11, @dPrice_SingleCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP5_Test11, @iMultiPackagingCommodityID_CategoryParent5, '一箱润泽玻尿酸面膜_CategoryParent5', 1, @iNO_MultiPackagingCommodity_CP5_Test11, @dPrice_MultiPackagingCommodity_CategoryParent5, @iNO_MultiPackagingCommodity_CP5_Test11, @dPrice_MultiPackagingCommodity_CategoryParent5, NULL, NULL);
-- 创建零售单1 (售卖4件普通商品2、1件组合商品、1件服务商品。共1337元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000004', 8, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 1337, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP5_Test11 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iSingleCommodityID2_CategoryParent5, '雪晶灵肌密水_CategoryParent5', 1, @iNO_SingleCommodity2_CP5_Test11, @dPrice_SingleCommodity2_CategoryParent5, @iNO_SingleCommodity2_CP5_Test11, @dPrice_SingleCommodity2_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iServiceCommodityID_CategoryParent5, '快递_CategoryParent5', 1, @iNO_ServiceCommodity_CP5_Test11, @dPrice_ServiceCommodity_CategoryParent5, @iNO_ServiceCommodity_CP5_Test11, @dPrice_ServiceCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iCompositionCommodityID_CategoryParent5, '护肤大礼包_CategoryParent5', 1, @iNO_CompositionCommodity_CP5_Test11, @dPrice_CompositionCommodity_CategoryParent5, @iNO_CompositionCommodity_CP5_Test11, @dPrice_CompositionCommodity_CategoryParent5, NULL, NULL);
-- 零售类别六的商品
SET @iNO_SingleCommodity_CP6_Test11 = 3;
SET @iNO_SingleCommodity2_CP6_Test11 = 5;
SET @iNO_CompositionCommodity_CP6_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP6_Test11 = 1;
SET @iNO_ServiceCommodity_CP6_Test11 = 1;
SET @iRetailTradeID1_CP6_Test11 = 0;
SET @iRetailTradeID2_CP6_Test11 = 0;
SET @iResultVerification_CP6_Test11 = 0;
-- 创建零售单1 (售卖3件普通商品1、1件服务商品。共21.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000005', 8, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 21.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP6_Test11 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP6_Test11, @iSingleCommodityID_CategoryParent6, '老坛酸菜牛肉面_CategoryParent6', 1, @iNO_SingleCommodity_CP6_Test11, @dPrice_SingleCommodity_CategoryParent6, @iNO_SingleCommodity_CP6_Test11, @dPrice_SingleCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP6_Test11, @iServiceCommodityID_CategoryParent6, '快递_CategoryParent6', 1, @iNO_ServiceCommodity_CP6_Test11, @dPrice_ServiceCommodity_CategoryParent6, @iNO_ServiceCommodity_CP6_Test11, @dPrice_ServiceCommodity_CategoryParent6, NULL, NULL);
-- 创建零售单2 (售卖5件普通商品2、1件组合商品、1件多包装商品。共87.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000006', 8, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP6_Test11 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iSingleCommodityID2_CategoryParent6, '红烧牛肉面_CategoryParent6', 1, @iNO_SingleCommodity2_CP6_Test11, @dPrice_SingleCommodity2_CategoryParent6, @iNO_SingleCommodity2_CP6_Test11, @dPrice_SingleCommodity2_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iCompositionCommodityID_CategoryParent6, '桶面大礼包_CategoryParent6', 1, @iNO_CompositionCommodity_CP6_Test11, @dPrice_CompositionCommodity_CategoryParent6, @iNO_CompositionCommodity_CP6_Test11, @dPrice_CompositionCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iMultiPackagingCommodityID_CategoryParent6, '一箱老坛酸菜牛肉面_CategoryParent6', 1, @iNO_MultiPackagingCommodity_CP6_Test11, @dPrice_MultiPackagingCommodity_CategoryParent6, @iNO_MultiPackagingCommodity_CP6_Test11, @dPrice_MultiPackagingCommodity_CategoryParent6, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test11, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP4_Test11 = (@iNO_SingleCommodity_CP4_Test11 * @dPrice_SingleCommodity_CategoryParent4) + (@iNO_CompositionCommodity_CP4_Test11 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dRetailTradeID2_TotalAmount_CP4_Test11 = (@iNO_SingleCommodity2_CP4_Test11 * @dPrice_SingleCommodity2_CategoryParent4) + (@iNO_MultiPackagingCommodity_CP4_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent4) -- 
											   + (@iNO_ServiceCommodity_CP4_Test11 * @dPrice_ServiceCommodity_CategoryParent4);	
SELECT 1 INTO @iResultVerification_CP4_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP4_Test11 + @dRetailTradeID2_TotalAmount_CP4_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP5_Test11 = (@iNO_SingleCommodity_CP5_Test11 * @dPrice_SingleCommodity_CategoryParent5) + (@iNO_MultiPackagingCommodity_CP5_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent5);
SET @dRetailTradeID2_TotalAmount_CP5_Test11 = (@iNO_ServiceCommodity_CP5_Test11 * @dPrice_ServiceCommodity_CategoryParent5) + (@iNO_SingleCommodity2_CP5_Test11 * @dPrice_SingleCommodity2_CategoryParent5) -- 
												+ (@iNO_CompositionCommodity_CP5_Test11 * @dPrice_CompositionCommodity_CategoryParent5);
SELECT 1 INTO @iResultVerification_CP5_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID5 --  
		AND F_TotalAmount =  @dRetailTradeID1_TotalAmount_CP5_Test11 + @dRetailTradeID2_TotalAmount_CP5_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP6_Test11 = (@iNO_SingleCommodity_CP6_Test11 * @dPrice_SingleCommodity_CategoryParent6) + (@iNO_ServiceCommodity_CP6_Test11 * @dPrice_ServiceCommodity_CategoryParent6);
SET @dRetailTradeID2_TotalAmount_CP6_Test11 = (@iNO_SingleCommodity2_CP6_Test11 * @dPrice_SingleCommodity2_CategoryParent6) + (@iNO_CompositionCommodity_CP6_Test11 * @dPrice_CompositionCommodity_CategoryParent6) -- 
												+ (@iNO_MultiPackagingCommodity_CP6_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent6);
SELECT 1 INTO @iResultVerification_CP6_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID6 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP6_Test11 + @dRetailTradeID2_TotalAmount_CP6_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP4_Test11, @iResultVerification_CP5_Test11, @iResultVerification_CP6_Test11;		 
SELECT IF(@iResultVerification_CP4_Test11 = 1 AND @iResultVerification_CP5_Test11 = 1 AND @iResultVerification_CP6_Test11 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case11 Result';



SELECT '-------------------- Case12:当月12号零售分别类别七、类别八和类别九的商品(无退货) -------------------------' AS 'Case12';
-- 创建类别七的商品数据
SET @iCategoryParentID7 = 9; 
SET @iCategoryID_CategoryParent7 = 0;
SET @iSingleCommodityID_CategoryParent7 = 0;
SET @dPrice_SingleCommodity_CategoryParent7 = 3;
SET @iSingleCommodityID2_CategoryParent7 = 0;
SET @dPrice_SingleCommodity2_CategoryParent7 = 3;
SET @iCompositionCommodityID_CategoryParent7 = 0;
SET @dPrice_CompositionCommodity_CategoryParent7 = 60;
SET @iMultiPackagingCommodityID_CategoryParent7 = 0;
SET @iRefCommodityMultiple_CategoryParent7 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent7 = 15;
SET @iServiceCommodityID_CategoryParent7 = 0;
SET @dPrice_ServiceCommodity_CategoryParent7 = 5;
-- 创建类别七的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP7', @iCategoryParentID7, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent7 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '晨光圆珠笔_CategoryParent7', '晨光圆珠笔', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent7, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent7 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '真彩圆珠笔_CategoryParent7', '真彩圆珠笔', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent7, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent7 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '文具大礼包_CategoryParent7', '清洁', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent7, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent7 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent7, @iSingleCommodityID_CategoryParent7, 10, @dPrice_SingleCommodity_CategoryParent7);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent7, @iSingleCommodityID2_CategoryParent7, 10, @dPrice_SingleCommodity2_CategoryParent7);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一盒晨光圆珠笔_CategoryParent7', '晨光圆珠笔', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent7, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID2_CategoryParent7, @iRefCommodityMultiple_CategoryParent7, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent7 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent7','kd','个',4,NULL,4,@iCategoryID_CategoryParent7,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递G',3);
SET @iServiceCommodityID_CategoryParent7 = last_insert_id();
-- 创建类别八的商品数据 
SET @iCategoryParentID8 = 10; 
SET @iCategoryID_CategoryParent8 = 0;
SET @iSingleCommodityID_CategoryParent8 = 0;
SET @dPrice_SingleCommodity_CategoryParent8 = 199;
SET @iSingleCommodityID2_CategoryParent8 = 0;
SET @dPrice_SingleCommodity2_CategoryParent8 = 39;
SET @iCompositionCommodityID_CategoryParent8 = 0;
SET @dPrice_CompositionCommodity_CategoryParent8 = 238;
SET @iMultiPackagingCommodityID_CategoryParent8 = 0;
SET @iRefCommodityMultiple_CategoryParent8 = 3;
SET @dPrice_MultiPackagingCommodity_CategoryParent8 = 117;
SET @iServiceCommodityID_CategoryParent8 = 0;
SET @dPrice_ServiceCommodity_CategoryParent8 = 5;
-- 创建类别八的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP8', @iCategoryParentID8, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent8 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '电动牙刷_CategoryParent8', '电动牙刷', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent8, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent8 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '云南白药牙膏_CategoryParent8', '云南白药牙膏', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent8, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent8 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '牙牙乐大礼包_CategoryParent8', '牙', '瓶', 1, '箱', 3, @iCategoryID_CategoryParent8, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent8 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent8, @iSingleCommodityID_CategoryParent8, 1, @dPrice_SingleCommodity_CategoryParent8);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent8, @iSingleCommodityID2_CategoryParent8, 1, @dPrice_SingleCommodity2_CategoryParent8);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '三只云南白药_CategoryParent8', '云南白药', '盒', 1, '箱', 3, @iCategoryID_CategoryParent8, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @iSingleCommodityID2_CategoryParent8, @iRefCommodityMultiple_CategoryParent8, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent8 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent8','kd','个',4,NULL,4,@iCategoryID_CategoryParent8,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递I',3);
SET @iServiceCommodityID_CategoryParent8 = last_insert_id();
-- 创建类别九的商品数据 
SET @iCategoryParentID9 = 11; 
SET @iCategoryID_CategoryParent9 = 0;
SET @iSingleCommodityID_CategoryParent9 = 0;
SET @dPrice_SingleCommodity_CategoryParent9 = 4.5;
SET @iSingleCommodityID2_CategoryParent9 = 0;
SET @dPrice_SingleCommodity2_CategoryParent9 = 3.5;
SET @iCompositionCommodityID_CategoryParent9 = 0;
SET @dPrice_CompositionCommodity_CategoryParent9 = 8;
SET @iMultiPackagingCommodityID_CategoryParent9 = 0;
SET @iRefCommodityMultiple_CategoryParent9 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent9 = 9;
SET @iServiceCommodityID_CategoryParent9 = 0;
SET @dPrice_ServiceCommodity_CategoryParent9 = 5;
-- 创建类别九的小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP9', @iCategoryParentID9, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent9 = last_insert_id();
-- 创建普通商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '西红柿_CategoryParent9', '西红柿', '桶', 1, '箱', 3, @iCategoryID_CategoryParent9, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent9 = last_insert_id();
-- 创建普通商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '茄子_CategoryParent9', '茄子', '桶', 1, '箱', 3, @iCategoryID_CategoryParent9, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent9 = last_insert_id();
-- 创建组合商品(子商品分别是普通商品1和普通商品2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '蔬菜大礼包_CategoryParent9', '蔬菜', '桶', 1, '箱', 3, @iCategoryID_CategoryParent9, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent9 = last_insert_id();
-- 子商品1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent9, @iSingleCommodityID_CategoryParent9, 1, @dPrice_SingleCommodity_CategoryParent9);
-- 子商品2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent9, @iSingleCommodityID2_CategoryParent9, 1, @dPrice_SingleCommodity2_CategoryParent9);
-- 创建多包装商品(参考普通商品1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '一斤茄子_CategoryParent9', '茄子', '桶', 1, '箱', 3, @iCategoryID_CategoryParent9, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 上午', 20, @dPrice_SingleCommodity_CategoryParent9, @iRefCommodityMultiple_CategoryParent9, '1111111', 2, NULL, 
'10/24/2098 17:00:00 下午', '10/24/2098 17:00:00 下午', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent9 = last_insert_id();
-- 创建服务商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'快递_CategoryParent9','kd','个',4,NULL,4,@iCategoryID_CategoryParent9,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 上午','20',0,0,'快递J',3);
SET @iServiceCommodityID_CategoryParent9 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test12 ='2099/06/12 08:08:08';
-- 零售类别七的商品
SET @iNO_SingleCommodity_CP7_Test12 = 2;
SET @iNO_SingleCommodity2_CP7_Test12 = 3;
SET @iNO_CompositionCommodity_CP7_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP7_Test12 = 1;
SET @iNO_ServiceCommodity_CP7_Test12 = 1;
SET @iRetailTradeID1_CP7_Test12 = 0;
SET @iRetailTradeID2_CP7_Test12 = 0;
SET @iResultVerification_CP7_Test12 = 0;
-- 创建零售单1 (售卖2件普通商品1、1件组合商品。共66元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 66, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP7_Test12 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP7_Test12, @iSingleCommodityID_CategoryParent7, '晨光圆珠笔_CategoryParent7', 1, @iNO_SingleCommodity_CP7_Test12, @dPrice_SingleCommodity_CategoryParent7, @iNO_SingleCommodity_CP7_Test12, @dPrice_SingleCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP7_Test12, @iCompositionCommodityID_CategoryParent7, '文具大礼包_CategoryParent7', 1, @iNO_CompositionCommodity_CP7_Test12, @dPrice_CompositionCommodity_CategoryParent7, @iNO_CompositionCommodity_CP7_Test12, @dPrice_CompositionCommodity_CategoryParent7, NULL, NULL);
-- 创建零售单1 (售卖3件普通商品2、1件多包装商品、1件服务商品。共29元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 29, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP7_Test12 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iSingleCommodityID2_CategoryParent7, '真彩圆珠笔_CategoryParent7', 1, @iNO_SingleCommodity2_CP7_Test12, @dPrice_SingleCommodity2_CategoryParent7, @iNO_SingleCommodity2_CP7_Test12, @dPrice_SingleCommodity2_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iServiceCommodityID_CategoryParent7, '快递_CategoryParent7', 1, @iNO_ServiceCommodity_CP7_Test12, @dPrice_ServiceCommodity_CategoryParent7, @iNO_ServiceCommodity_CP7_Test12, @dPrice_ServiceCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iMultiPackagingCommodityID_CategoryParent7, '一盒晨光圆珠笔_CategoryParent7', 1, @iNO_MultiPackagingCommodity_CP7_Test12, @dPrice_MultiPackagingCommodity_CategoryParent7, @iNO_MultiPackagingCommodity_CP7_Test12, @dPrice_MultiPackagingCommodity_CategoryParent7, NULL, NULL);
-- 零售类别八的商品
SET @iNO_SingleCommodity_CP8_Test12 = 3;
SET @iNO_SingleCommodity2_CP8_Test12 = 4;
SET @iNO_CompositionCommodity_CP8_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP8_Test12 = 1;
SET @iNO_ServiceCommodity_CP8_Test12 = 1;
SET @iRetailTradeID1_CP8_Test12 = 0;
SET @iRetailTradeID2_CP8_Test12 = 0;
SET @iResultVerification_CP8_Test12 = 0;
-- 创建零售单1 (售卖3件普通商品1、1件多包装商品。共714元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000003', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 714, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP8_Test12 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP8_Test12, @iSingleCommodityID_CategoryParent8, '电动牙刷_CategoryParent8', 1, @iNO_SingleCommodity_CP8_Test12, @dPrice_SingleCommodity_CategoryParent8, @iNO_SingleCommodity_CP8_Test12, @dPrice_SingleCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP8_Test12, @iMultiPackagingCommodityID_CategoryParent8, '三只云南白药_CategoryParent8', 1, @iNO_MultiPackagingCommodity_CP8_Test12, @dPrice_MultiPackagingCommodity_CategoryParent8, @iNO_MultiPackagingCommodity_CP8_Test12, @dPrice_MultiPackagingCommodity_CategoryParent8, NULL, NULL);
-- 创建零售单1 (售卖4件普通商品2、1件组合商品、1件服务商品。共399元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000004', 1, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 399, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP8_Test12 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iSingleCommodityID2_CategoryParent8, '南白药牙膏_CategoryParent8', 1, @iNO_SingleCommodity2_CP8_Test12, @dPrice_SingleCommodity2_CategoryParent8, @iNO_SingleCommodity2_CP8_Test12, @dPrice_SingleCommodity2_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iServiceCommodityID_CategoryParent8, '快递_CategoryParent8', 1, @iNO_ServiceCommodity_CP8_Test12, @dPrice_ServiceCommodity_CategoryParent8, @iNO_ServiceCommodity_CP8_Test12, @dPrice_ServiceCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iCompositionCommodityID_CategoryParent8, '牙牙乐大礼包_CategoryParent8', 1, @iNO_CompositionCommodity_CP8_Test12, @dPrice_CompositionCommodity_CategoryParent8, @iNO_CompositionCommodity_CP8_Test12, @dPrice_CompositionCommodity_CategoryParent8, NULL, NULL);
-- 零售类别九的商品
SET @iNO_SingleCommodity_CP9_Test12 = 3;
SET @iNO_SingleCommodity2_CP9_Test12 = 5;
SET @iNO_CompositionCommodity_CP9_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP9_Test12 = 1;
SET @iNO_ServiceCommodity_CP9_Test12 = 1;
SET @iRetailTradeID1_CP9_Test12 = 0;
SET @iRetailTradeID2_CP9_Test12 = 0;
SET @iResultVerification_CP9_Test12 = 0;
-- 创建零售单1 (售卖3件普通商品1、1件服务商品。共18.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000005', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 18.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP9_Test12 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP9_Test12, @iSingleCommodityID_CategoryParent9, '西红柿_CategoryParent9', 1, @iNO_SingleCommodity_CP9_Test12, @dPrice_SingleCommodity_CategoryParent9, @iNO_SingleCommodity_CP9_Test12, @dPrice_SingleCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP9_Test12, @iServiceCommodityID_CategoryParent9, '快递_CategoryParent9', 1, @iNO_ServiceCommodity_CP9_Test12, @dPrice_ServiceCommodity_CategoryParent9, @iNO_ServiceCommodity_CP9_Test12, @dPrice_ServiceCommodity_CategoryParent9, NULL, NULL);
-- 创建零售单2 (售卖5件普通商品2、1件组合商品、1件多包装商品。共34.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000006', 1, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 34.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP9_Test12 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iSingleCommodityID2_CategoryParent9, '茄子_CategoryParent9', 1, @iNO_SingleCommodity2_CP9_Test12, @dPrice_SingleCommodity2_CategoryParent9, @iNO_SingleCommodity2_CP9_Test12, @dPrice_SingleCommodity2_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iCompositionCommodityID_CategoryParent9, '蔬菜大礼包_CategoryParent9', 1, @iNO_CompositionCommodity_CP9_Test12, @dPrice_CompositionCommodity_CategoryParent9, @iNO_CompositionCommodity_CP9_Test12, @dPrice_CompositionCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iMultiPackagingCommodityID_CategoryParent9, '一斤茄子_CategoryParent9', 1, @iNO_MultiPackagingCommodity_CP9_Test12, @dPrice_MultiPackagingCommodity_CategoryParent9, @iNO_MultiPackagingCommodity_CP9_Test12, @dPrice_MultiPackagingCommodity_CategoryParent9, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test12, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP7_Test12 = (@iNO_SingleCommodity_CP7_Test12 * @dPrice_SingleCommodity_CategoryParent7) + (@iNO_CompositionCommodity_CP7_Test12 * @dPrice_CompositionCommodity_CategoryParent7);
SET @dRetailTradeID2_TotalAmount_CP7_Test12 = (@iNO_SingleCommodity2_CP7_Test12 * @dPrice_SingleCommodity2_CategoryParent7) + (@iNO_MultiPackagingCommodity_CP7_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent7) -- 
												+ (@iNO_ServiceCommodity_CP7_Test12 * @dPrice_ServiceCommodity_CategoryParent7);
SELECT 1 INTO @iResultVerification_CP7_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID7 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP7_Test12 + @dRetailTradeID2_TotalAmount_CP7_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP8_Test12 = (@iNO_SingleCommodity_CP8_Test12 * @dPrice_SingleCommodity_CategoryParent8) + (@iNO_MultiPackagingCommodity_CP8_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent8);
SET @dRetailTradeID2_TotalAmount_CP8_Test12 = (@iNO_ServiceCommodity_CP8_Test12 * @dPrice_ServiceCommodity_CategoryParent8) + (@iNO_SingleCommodity2_CP8_Test12 * @dPrice_SingleCommodity2_CategoryParent8) -- 
												+ (@iNO_CompositionCommodity_CP8_Test12 * @dPrice_CompositionCommodity_CategoryParent8);
SELECT 1 INTO @iResultVerification_CP8_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID8 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP8_Test12 + @dRetailTradeID2_TotalAmount_CP8_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP9_Test12 = (@iNO_SingleCommodity_CP9_Test12 * @dPrice_SingleCommodity_CategoryParent9) + (@iNO_ServiceCommodity_CP9_Test12 * @dPrice_ServiceCommodity_CategoryParent9);
SET @dRetailTradeID2_TotalAmount_CP9_Test12 = (@iNO_SingleCommodity2_CP9_Test12 * @dPrice_SingleCommodity2_CategoryParent9) + (@iNO_CompositionCommodity_CP9_Test12 * @dPrice_CompositionCommodity_CategoryParent9) -- 
												+ (@iNO_MultiPackagingCommodity_CP9_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent9);
SELECT 1 INTO @iResultVerification_CP9_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID9 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP9_Test12 +  @dRetailTradeID2_TotalAmount_CP9_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP7_Test12, @iResultVerification_CP8_Test12, @iResultVerification_CP9_Test12;		 
SELECT IF(@iResultVerification_CP7_Test12 = 1 AND @iResultVerification_CP8_Test12 = 1 AND @iResultVerification_CP9_Test12 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case12 Result';




SELECT '-------------------- Case13:当月13号零售分别类别一、类别二和类别三的商品(无退货) -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test13 ='2099/06/13 08:08:08';
-- 零售类别一的商品
SET @iNO_SingleCommodity_CP1_Test13 = 9;
SET @iNO_SingleCommodity2_CP1_Test13 = 1;
SET @iNO_CompositionCommodity_CP1_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test13 = 1;
SET @iNO_ServiceCommodity_CP1_Test13 = 1;
SET @iRetailTradeID1_CP1_Test13 = 0;
SET @iRetailTradeID2_CP1_Test13 = 0;
SET @iResultVerification_CP1_Test13 = 0;
-- 创建零售单1 (售卖9件普通商品1、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000001', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test13 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test13, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test13, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test13, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test13, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test13, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test13, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品2、1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000002', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test13 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test13, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test13, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test13, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test13, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test13, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test13, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 零售类别二的商品
SET @iNO_SingleCommodity_CP2_Test13 = 5;
SET @iNO_SingleCommodity2_CP2_Test13 = 3;
SET @iNO_CompositionCommodity_CP2_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test13 = 1;
SET @iNO_ServiceCommodity_CP2_Test13 = 1;
SET @iRetailTradeID1_CP2_Test13 = 0;
SET @iRetailTradeID2_CP2_Test13 = 0;
SET @iResultVerification_CP2_Test13 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000003', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test13 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test13, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test13, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test13, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test13, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test13, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test13, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件服务商品。共69.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000004', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test13 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test13, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test13, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test13, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test13, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test13, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test13, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- 零售类别三的商品
SET @iNO_SingleCommodity_CP3_Test13 = 5;
SET @iNO_SingleCommodity2_CP3_Test13 = 3;
SET @iNO_CompositionCommodity_CP3_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test13 = 1;
SET @iNO_ServiceCommodity_CP3_Test13 = 1;
SET @iRetailTradeID1_CP3_Test13 = 0;
SET @iRetailTradeID2_CP3_Test13 = 0;
SET @iResultVerification_CP3_Test13 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000005', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test13 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test13, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test13, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test13, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test13, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test13, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test13, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000006', 2, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test13 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test13, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test13, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test13, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test13, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test13, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test13, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test13, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test13 = (@iNO_SingleCommodity_CP1_Test13 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test13 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test13 = (@iNO_SingleCommodity2_CP1_Test13 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test13 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test13 +  @dRetailTradeID2_TotalAmount_CP1_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test13 = (@iNO_SingleCommodity_CP2_Test13 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test13 = (@iNO_SingleCommodity2_CP2_Test13 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test13 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test13 * @dPrice_ServiceCommodity_CategoryParent2);	 
SELECT 1 INTO @iResultVerification_CP2_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test13 + @dRetailTradeID2_TotalAmount_CP2_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test13 = (@iNO_SingleCommodity_CP3_Test13 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test13 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test13 = (@iNO_SingleCommodity2_CP3_Test13 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
												+ (@iNO_CompositionCommodity_CP3_Test13 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test13 + @dRetailTradeID2_TotalAmount_CP3_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test13, @iResultVerification_CP2_Test13, @iResultVerification_CP3_Test13;	 
SELECT IF(@iResultVerification_CP1_Test13 = 1 AND @iResultVerification_CP2_Test13 = 1 AND @iResultVerification_CP3_Test13 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case13 Result';



	
SELECT '-------------------- Case14:当月14号零售分别类别一、类别二和类别三的商品(无退货) -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test14 ='2099/06/14 08:08:08';
-- 零售类别一的商品
SET @iNO_SingleCommodity_CP1_Test14 = 9;
SET @iNO_SingleCommodity2_CP1_Test14 = 1;
SET @iNO_CompositionCommodity_CP1_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test14 = 1;
SET @iNO_ServiceCommodity_CP1_Test14 = 1;
SET @iRetailTradeID1_CP1_Test14 = 0;
SET @iRetailTradeID2_CP1_Test14 = 0;
SET @iResultVerification_CP1_Test14 = 0;
-- 创建零售单1 (售卖9件普通商品2、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000001', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test14 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test14, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test14, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test14, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test14, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test14, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test14, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件普通商品1、1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000002', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test14 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test14, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test14, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test14, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test14, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test14, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test14, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 零售类别二的商品
SET @iNO_SingleCommodity_CP2_Test14 = 5;
SET @iNO_SingleCommodity2_CP2_Test14 = 3;
SET @iNO_CompositionCommodity_CP2_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test14 = 1;
SET @iNO_ServiceCommodity_CP2_Test14 = 1;
SET @iRetailTradeID1_CP2_Test14 = 0;
SET @iRetailTradeID2_CP2_Test14 = 0;
SET @iResultVerification_CP2_Test14 = 0;
-- 创建零售单1 (售卖5件普通商品2、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000003', 3, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test14 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test14, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test14, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test14, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test14, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test14, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test14, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品1、1件组合商品、1件服务商品。共69.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000004', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test14 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test14, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test14, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test14, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test14, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test14, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test14, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- 零售类别三的商品
SET @iNO_SingleCommodity_CP3_Test14 = 5;
SET @iNO_SingleCommodity2_CP3_Test14 = 3;
SET @iNO_CompositionCommodity_CP3_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test14 = 1;
SET @iNO_ServiceCommodity_CP3_Test14 = 1;
SET @iRetailTradeID1_CP3_Test14 = 0;
SET @iRetailTradeID2_CP3_Test14 = 0;
SET @iResultVerification_CP3_Test14 = 0;
-- 创建零售单1 (售卖5件普通商品2、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000005', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test14 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test14, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test14, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test14, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test14, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test14, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test14, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品1、1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000006', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test14 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test14, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test14, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test14, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test14, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test14, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test14, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test14, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test14 = (@iNO_SingleCommodity2_CP1_Test14 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test14 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity_CP1_Test14 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test14 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test14 + @dRetailTradeID2_TotalAmount_CP2_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity_CP2_Test14 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity2_CP2_Test14 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test14 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test14 * @dPrice_ServiceCommodity_CategoryParent2); 
SELECT 1 INTO @iResultVerification_CP2_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test14 + @dRetailTradeID2_TotalAmount_CP2_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test14 = (@iNO_SingleCommodity2_CP3_Test14 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test14 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test14 = (@iNO_SingleCommodity_CP3_Test14 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
		   										+ (@iNO_CompositionCommodity_CP3_Test14 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test14 + @dRetailTradeID2_TotalAmount_CP3_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test14, @iResultVerification_CP2_Test14, @iResultVerification_CP3_Test14;	 
SELECT IF(@iResultVerification_CP1_Test14 = 1 AND @iResultVerification_CP2_Test14 = 1 AND @iResultVerification_CP3_Test14 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case14 Result';



SELECT '-------------------- Case15:当月15号零售分别类别一、类别二和类别三的商品(无退货) -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test15 ='2099/06/15 08:08:08';
-- 零售类别一的商品
SET @iNO_SingleCommodity_CP1_Test15 = 9;
SET @iNO_SingleCommodity2_CP1_Test15 = 1;
SET @iNO_CompositionCommodity_CP1_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test15 = 1;
SET @iNO_ServiceCommodity_CP1_Test15 = 1;
SET @iRetailTradeID1_CP1_Test15 = 0;
SET @iRetailTradeID2_CP1_Test15 = 0;
SET @iResultVerification_CP1_Test15 = 0;
-- 创建零售单1 (售卖9件普通商品1、1件普通商品2、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000001', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test15 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test15, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test15, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test15, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test15, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test15, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test15, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2 (售卖1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000002', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test15 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test15, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test15, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test15, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test15, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test15, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test15, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 零售类别二的商品
SET @iNO_SingleCommodity_CP2_Test15 = 5;
SET @iNO_SingleCommodity2_CP2_Test15 = 3;
SET @iNO_CompositionCommodity_CP2_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test15 = 1;
SET @iNO_ServiceCommodity_CP2_Test15 = 1;
SET @iRetailTradeID1_CP2_Test15 = 0;
SET @iRetailTradeID2_CP2_Test15 = 0;
SET @iResultVerification_CP2_Test15 = 0;
-- 创建零售单1 (售卖5件普通商品1、3件普通商品2、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000003', 4, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test15 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test15, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test15, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test15, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test15, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test15, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test15, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖1件组合商品、1件服务商品。共69.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000004', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test15 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test15, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test15, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test15, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test15, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test15, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test15, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- 零售类别三的商品
SET @iNO_SingleCommodity_CP3_Test15 = 5;
SET @iNO_SingleCommodity2_CP3_Test15 = 3;
SET @iNO_CompositionCommodity_CP3_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test15 = 1;
SET @iNO_ServiceCommodity_CP3_Test15 = 1;
SET @iRetailTradeID1_CP3_Test15 = 0;
SET @iRetailTradeID2_CP3_Test15 = 0;
SET @iResultVerification_CP3_Test15 = 0;
-- 创建零售单1 (售卖5件普通商品1、3件普通商品2、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000005', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test15 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test15, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test15, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test15, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test15, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test15, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test15, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000006', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test15 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test15, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test15, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test15, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test15, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test15, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test15, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test15, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test15 = (@iNO_SingleCommodity_CP1_Test15 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test15 * @dPrice_CompositionCommodity_CategoryParent1) -- 
												+ (@iNO_SingleCommodity2_CP1_Test15 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test15 = (@iNO_ServiceCommodity_CP1_Test15 * @dPrice_ServiceCommodity_CategoryParent1) + (@iNO_MultiPackagingCommodity_CP1_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test15 + @dRetailTradeID2_TotalAmount_CP1_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test15 = (@iNO_SingleCommodity_CP2_Test15 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent2) -- 
												+ (@iNO_SingleCommodity2_CP2_Test15 * @dPrice_SingleCommodity2_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test15 = (@iNO_CompositionCommodity_CP2_Test15 * @dPrice_CompositionCommodity_CategoryParent2) + (@iNO_ServiceCommodity_CP2_Test15 * @dPrice_ServiceCommodity_CategoryParent2); 		
SELECT 1 INTO @iResultVerification_CP2_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test15 + @dRetailTradeID2_TotalAmount_CP2_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test15 = (@iNO_SingleCommodity_CP3_Test15 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test15 * @dPrice_ServiceCommodity_CategoryParent3) -- 
												+ (@iNO_SingleCommodity2_CP3_Test15 * @dPrice_SingleCommodity2_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test15 = (@iNO_MultiPackagingCommodity_CP3_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test15 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test15 + @dRetailTradeID2_TotalAmount_CP3_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test15, @iResultVerification_CP2_Test15, @iResultVerification_CP3_Test15;	 
SELECT IF(@iResultVerification_CP1_Test15 = 1 AND @iResultVerification_CP2_Test15 = 1 AND @iResultVerification_CP3_Test15 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case15 Result';




SELECT '-------------------- Case16:当月16号退当月1号售卖的类别一的商品(没有售卖商品) -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test16 ='2099/06/16 08:08:08';
SET @iResultVerification_Test16 = 0;
-- 对当月1号单号为"LS2099060100000000000001"的零售单进行全部退货(共52.5元)
SET @iReturnNo_SingleCommodity_All_Test16 = 3;
SET @iReturnNo_CompositionCommodity_All_Test16 = 2;
SET @iReturnRetailTradeID_All_Test16 = 0;
-- 创建零售退货单1 (退3件普通商品1、2件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000001_1', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test16, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test1, @dSaleDatetime_Test16, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test16 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test16, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test16, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test16, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test16, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_Test16, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_Test16, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 对当月1号单号为"LS2099060100000000000002"的零售单进行部分退货(共92.5元)
SET @iReturnNo_SingleCommodity2_Part_Test16 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test16 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test16 = 1;
SET @iReturnRetailTradeID_Part_Test16 = 0;
-- 创建零售退货单2 (退1件普通商品2、1件多包装商品、1件服务商品。共92.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000002', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test16, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test1, @dSaleDatetime_Test16, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test16 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test16, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test16, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test16, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test16, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_Test16, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_Test16, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test16, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test16 = (@iReturnNo_SingleCommodity_All_Test16 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_Test16 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test16 = (@iReturnNo_SingleCommodity2_Part_Test16 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_Test16 * @dPrice_MultiPackagingCommodity_CategoryParent1) -- 
														+ (@iReturnNo_ServiceCommodity_Part_Test16 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test16 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test16 - @dReturnRetailTradeID_Part_TotalAmount_Test16 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test16;
SELECT IF(@iResultVerification_Test16 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case16 Result';


SELECT '-------------------- Case17:当月17号退当月2号售卖的类别一的商品(没有售卖商品) -------------------------' AS 'Case17'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test17 ='2099/06/17 08:08:08';
SET @iResultVerification_Test17 = 0;
-- 对当月2号单号为"LS2099060200000000000001"的零售单进行全部退货(共171.5元)
SET @iReturnNo_SingleCommodity_All_Test17 = 1;
SET @iReturnNo_MultiPackagingCommodity_All_Test17 = 2;
SET @iReturnRetailTradeID_All_Test17 = 0;
-- 创建零售退货单1 (退1件普通商品1、2件多包装商品。共171.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000001_1', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test17, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test2, @dSaleDatetime_Test17, 171.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test17 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test17, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test17, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test17, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test17, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_All_Test17, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_All_Test17, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 对当月2号单号为"LS2099060200000000000002"的零售单进行部分退货(共8.5元)
SET @iReturnNo_SingleCommodity2_Part_Test17 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test17 = 1;
SET @iReturnRetailTradeID_Part_Test17 = 0;
-- 创建零售退货单2 (退1件普通商品2、1件服务商品。共8.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000002_1', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test17, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test2, @dSaleDatetime_Test17, 8.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test17 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test17, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_Test17, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_Test17, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test17, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test17, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test17, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test17, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test17 = (@iReturnNo_SingleCommodity_All_Test17 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_All_Test17 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test17 = (@iReturnNo_SingleCommodity2_Part_Test17 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_Test17 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT @dReturnRetailTradeID_All_TotalAmount_Test17, @dReturnRetailTradeID_Part_TotalAmount_Test17;
SELECT 1 INTO @iResultVerification_Test17 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test17 - @dReturnRetailTradeID_Part_TotalAmount_Test17 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test17;
SELECT IF(@iResultVerification_Test17 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case17 Result';



SELECT '-------------------- Case18:当月18号退当月3号售卖的类别一的商品(没有售卖商品) -------------------------' AS 'Case18'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test18 ='2099/06/18 08:08:08';
SET @iResultVerification_Test18 = 0;
-- 对当月3号单号为"LS2099060300000000000001"的零售单进行全部退货(共15.5元)
SET @iReturnNo_SingleCommodity_All_Test18 = 3;
SET @iReturnNo_ServiceCommodity_All_Test18 = 1;
SET @iReturnRetailTradeID_All_Test18 = 0;
-- 创建零售退货单1 (退3件普通商品1、1件服务商品。共15.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000001_1', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test18, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test3, @dSaleDatetime_Test18, 15.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test18 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test18, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test18, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test18, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test18, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_All_Test18, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_All_Test18, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 对当月3号单号为"LS2099060300000000000002"的零售单进行部分退货(共15.5元)
SET @iReturnNo_CompositionCommodity_Part_Test18 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test18 = 1;
SET @iReturnRetailTradeID_Part_Test18 = 0;
-- 创建零售退货单2 (退1件组合商品、1件多包装商品。共105元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000002_1', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test18, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test3, @dSaleDatetime_Test18, 105, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test18 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test18, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_Part_Test18, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_Part_Test18, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test18, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test18, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test18, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test18, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test18 = (@iReturnNo_SingleCommodity_All_Test18 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_All_Test18 * @dPrice_ServiceCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test18 = (@iReturnNo_CompositionCommodity_Part_Test18 * @dPrice_CompositionCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_Test18 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test18 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test18 -  @dReturnRetailTradeID_Part_TotalAmount_Test18 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test18;
SELECT IF(@iResultVerification_Test18 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case18 Result';



SELECT '-------------------- Case19:当月19号退当月4号售卖的类别一的商品(没有售卖商品) -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test19 ='2099/06/19 08:08:08';
SET @iResultVerification_Test19 = 0;
-- 对当月4号单号为"LS2099060400000000000001"的零售单进行全部退货(共38.5元)
SET @iReturnNo_SingleCommodity_All_Test19 = 5;
SET @iReturnNo_CompositionCommodity_All_Test19 = 1;
SET @iReturnRetailTradeID_All_Test19 = 0;
-- 创建零退货售单1 (退5件普通商品1、1件组合商品。共38.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000001_1', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test19, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test4, @dSaleDatetime_Test19, 38.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test19 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test19, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test19, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test19, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test19, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_Test19, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_Test19, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 对当月4号单号为"LS2099060400000000000002"的零售单进行部分退货(共89元)
SET @iReturnNo_MultiPackagingCommodity_Part_Test19 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test19 = 1;
SET @iReturnRetailTradeID_Part_Test19 = 0;
-- 创建零售退货单2 (退1件多包装商品、1件服务商品。共89元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000002_1', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test19, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test4, @dSaleDatetime_Test19, 89, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test19 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test19, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test19, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test19, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test19, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test19, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test19, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test19, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test19 = (@iReturnNo_SingleCommodity_All_Test19 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_Test19 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test19 = (@iReturnNo_MultiPackagingCommodity_Part_Test19 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_Test19 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test19 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test19 - @dReturnRetailTradeID_Part_TotalAmount_Test19 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test19;
SELECT IF(@iResultVerification_Test19 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case19 Result';



SELECT '-------------------- Case20:当月20号退当月5号售卖的类别二的商品(没有售卖商品) -------------------------' AS 'Case20';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test20 ='2099/06/20 08:08:08';
SET @iResultVerification_Test20 = 0;
-- 对当月5号单号为"LS2099060500000000000001"的零售单进行全部退货(共29.94元)
SET @iReturnNo_SingleCommodity2_All_Test20 = 2;
SET @iReturnNo_MultiPackagingCommodity_All_Test20 = 2;
SET @iReturnRetailTradeID_All_Test20 = 0;
-- 创建零售退货单1 (退2件普通商品2、2件多包装商品。共29.94元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000001_1', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test20, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test5, @dSaleDatetime_Test20, 29.94, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test20 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test20, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_Test20, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_Test20, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test20, @iMultiPackagingCommodityID_CategoryParent2, '一箱百事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_Test20, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_Test20, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 对当月5号单号为"LS2099060500000000000002"的零售单进行部分退货(共54.9元)
SET @iReturnNo_CompositionCommodity_Part_Test20 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test20 = 1;
SET @iReturnRetailTradeID_Part_Test20 = 0;
-- 创建零售退货单2 (1件组合商品、1件服务商品。共54.9元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000002_1', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test20, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test5, @dSaleDatetime_Test20, 54.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test20 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test20, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_Test20, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_Test20, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test20, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_Test20, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_Test20, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test20, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test20 = (@iReturnNo_SingleCommodity2_All_Test20 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_Test20 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_TotalAmount_Test20 = (@iReturnNo_CompositionCommodity_Part_Test20 * @dPrice_CompositionCommodity_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_Test20 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_Test20 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test20 - @dReturnRetailTradeID_Part_TotalAmount_Test20 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test20;
SELECT IF(@iResultVerification_Test20 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case20 Result';



SELECT '-------------------- Case21:当月21号退当月6号售卖的类别三的商品(没有售卖商品) -------------------------' AS 'Case21';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test21 ='2099/06/21 08:08:08';
SET @iResultVerification_Test21 = 0;
-- 对当月6号单号为"LS2099060600000000000001"的零售单进行全部退货(共12.76元)
SET @iReturnNo_SingleCommodity2_All_Test21 = 2;
SET @iReturnNo_ServiceCommodity_All_Test21 = 1;
SET @iReturnRetailTradeID_All_Test21 = 0;
-- 创建零售退货单1 (退2件普通商品2、1件服务商品。共12.76元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000001_1', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test21, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test6, @dSaleDatetime_Test21, 12.76, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test21 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test21, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_Test21, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_Test21, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test21, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_Test21, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_Test21, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 对当月6号单号为"LS2099060600000000000002"的零售单进行部分退货(共42.68元)
SET @iReturnNo_CompositionCommodity_Part_Test21 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test21 = 1;
SET @iReturnRetailTradeID_Part_Test21 = 0;
-- 创建零售退货单2 (退1件组合商品、1件多包装商品。共42.68元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000002_1', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test21, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test6, @dSaleDatetime_Test21, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test21 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test21, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_Test21, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_Test21, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test21, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_Test21, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_Test21, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test21, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test21 = (@iReturnNo_SingleCommodity2_All_Test21 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_Test21 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_TotalAmount_Test21 = (@iReturnNo_CompositionCommodity_Part_Test21 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_Test21 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_Test21 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test21 - @dReturnRetailTradeID_Part_TotalAmount_Test21 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test21;
SELECT IF(@iResultVerification_Test21 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case21 Result';



SELECT '-------------------- Case22:当月22号退当月10号售卖的类别一、类别二、类别三的商品(没有售卖商品) -------------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test22 ='2099/06/22 08:08:08';
SET @iResultVerification_CP1_Test22 = 0;
SET @iResultVerification_CP2_Test22 = 0;
SET @iResultVerification_CP3_Test22 = 0;
-- 对当月10号单号为"LS2099061000000000000001"的零售单进行全部退货(共52.5元)
SET @iReturnNo_SingleCommodity_All_CP1_Test22 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test22 = 1;
SET @iReturnRetailTradeID_All_CP1_Test22 = 0;
-- 创建零售退货单1 (退9件普通商品1、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000001_1', 7, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test10, @dSaleDatetime_Test22, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test22 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test22, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test22, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test22, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test22, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test22, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test22, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 对当月10号单号为"LS2099061000000000000002"的零售单进行部分退货(共87.5元)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test22 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test22 = 0;
-- 创建零售退货单2 (退1件普通商品2、1件多包装商品。共87.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000002_1', 7, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test10, @dSaleDatetime_Test22, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test22 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test22, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test22, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test22, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test22, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- 对当月10号单号为"LS2099061000000000000003"的零售单进行全部退货(共34.93元)
SET @iReturnNo_SingleCommodity_All_CP2_Test22 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test22 = 1;
SET @iReturnRetailTradeID_All_CP2_Test22 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000003_1', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test10, @dSaleDatetime_Test22, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test22 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test22, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test22, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test22, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test22, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test22, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test22, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 对当月10号单号为"LS2099061000000000000004"的零售单进行部分退货(共19.97元)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test22 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test22 = 0;
-- 创建零售退货单2 (退3件普通商品2、1件服务商品。共19.97元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000004_1', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test10, @dSaleDatetime_Test22, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test22 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test22, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test22, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test22, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test22, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test22, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test22, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- 对当月10号单号为"LS2099061000000000000005"的零售单进行全部退货(共24.4元)
SET @iReturnNo_SingleCommodity_All_CP3_Test22 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test22 = 1;
SET @iReturnRetailTradeID_All_CP3_Test22 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000005_1', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test10, @dSaleDatetime_Test22, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test22 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test22, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test22, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test22, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test22, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test22, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test22, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 对当月10号单号为"LS2099061000000000000006"的零售单进行部分退货(共46.68元)
SET @iReturnNo_CompositionCommodity_Part_CP3_Test22 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test22 = 0;
-- 创建零售退货单2 (退1件组合商品、1件多包装商品。共42.68元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000006_1', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test10, @dSaleDatetime_Test22, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test22 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test22, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test22, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test22, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test22, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test22, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP1_Test22 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test22 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test22 = (@iReturnNo_SingleCommodity2_Part_CP1_Test22 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); --

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP2_Test22 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_CP2_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test22 = (@iReturnNo_SingleCommodity2_Part_CP2_Test22 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test22 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); --

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP3_Test22 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test22 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test22 = (@iReturnNo_CompositionCommodity_Part_CP3_Test22 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test22, @iResultVerification_CP2_Test22, @iResultVerification_CP3_Test22;
SELECT IF(@iResultVerification_CP1_Test22 = 1 AND @iResultVerification_CP2_Test22 = 1 AND @iResultVerification_CP3_Test22 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case22 Result';




SELECT '-------------------- Case23:当月23号退当月11号售卖的类别四、类别五、类别六的商品(没有售卖商品) -------------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test23 ='2099/06/23 08:08:08';
SET @iResultVerification_CP4_Test23 = 0;
SET @iResultVerification_CP5_Test23 = 0;
SET @iResultVerification_CP6_Test23 = 0;
-- 对当月11号单号为"LS2099061100000000000001"的零售单进行全部退货(共247.6元)
SET @iReturnNo_SingleCommodity_All_CP4_Test23 = 2;
SET @iReturnNo_CompositionCommodity_All_CP4_Test23 = 1;
SET @iReturnRetailTradeID_All_CP4_Test23 = 0;
-- 创建零售退货单1 (退2件普通商品1、1件组合商品。共247.6元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000001_1', 8, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP4_Test11, @dSaleDatetime_Test23, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP4_Test23 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test23, @iSingleCommodityID_CategoryParent4, '威猛先生_CategoryParent4', 1, @iReturnNo_SingleCommodity_All_CP4_Test23, @dPrice_SingleCommodity_CategoryParent4, @iReturnNo_SingleCommodity_All_CP4_Test23, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test23, @iCompositionCommodityID_CategoryParent4, '清洁大礼包_CategoryParent4', 1, @iReturnNo_CompositionCommodity_All_CP4_Test23, @dPrice_CompositionCommodity_CategoryParent4, @iReturnNo_CompositionCommodity_All_CP4_Test23, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- 对当月11号单号为"LS2099061100000000000002"的零售单进行部分退货(共109元)
SET @iReturnNo_ServiceCommodity_Part_CP4_Test23 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP4_Test23 = 0;
-- 创建零售退货单2 (退1件多包装商品、1件服务商品。共109元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000002_1', 8, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP4_Test11, @dSaleDatetime_Test23, 109, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP4_Test23 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test23, @iServiceCommodityID_CategoryParent4, '快递_CategoryParent4', 1, @iReturnNo_ServiceCommodity_Part_CP4_Test23, @dPrice_ServiceCommodity_CategoryParent4, @iReturnNo_ServiceCommodity_Part_CP4_Test23, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test23, @iMultiPackagingCommodityID_CategoryParent4, '一箱蓝月亮_CategoryParent4', 1, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23, @dPrice_MultiPackagingCommodity_CategoryParent4, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);

-- 对当月11号单号为"LS2099061100000000000003"的零售单进行全部退货(共675元)
SET @iReturnNo_SingleCommodity_All_CP5_Test23 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP5_Test23 = 1;
SET @iReturnRetailTradeID_All_CP5_Test23 = 0;
-- 创建零售退货单1 (退3件普通商品1、1件多包装商品。共675元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000003_1', 8, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP5_Test11, @dSaleDatetime_Test23, 675, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP5_Test23 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP5_Test23, @iSingleCommodityID_CategoryParent5, '润泽玻尿酸面膜_CategoryParent5', 1, @iReturnNo_SingleCommodity_All_CP5_Test23, @dPrice_SingleCommodity_CategoryParent5, @iReturnNo_SingleCommodity_All_CP5_Test23, @dPrice_SingleCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP5_Test23, @iMultiPackagingCommodityID_CategoryParent5, '一箱润泽玻尿酸面膜_CategoryParent5', 1, @iReturnNo_MultiPackagingCommodity_All_CP5_Test23, @dPrice_MultiPackagingCommodity_CategoryParent5, @iReturnNo_MultiPackagingCommodity_All_CP5_Test23, @dPrice_MultiPackagingCommodity_CategoryParent5, NULL, NULL);
-- 对当月11号单号为"LS2099061100000000000004"的零售单进行部分退货(共761元)
SET @iReturnNo_SingleCommodity2_Part_CP5_Test23 = 4;
SET @iReturnNo_ServiceCommodity_Part_CP5_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP5_Test23 = 0;
-- 创建零售退货单2 (退4件普通商品2、1件服务商品。共761元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000004_1', 8, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP5_Test11, @dSaleDatetime_Test23, 761, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP5_Test23 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP5_Test23, @iSingleCommodityID2_CategoryParent5, '雪晶灵肌密水_CategoryParent5', 1, @iReturnNo_SingleCommodity2_Part_CP5_Test23, @dPrice_SingleCommodity2_CategoryParent5, @iReturnNo_SingleCommodity2_Part_CP5_Test23, @dPrice_SingleCommodity2_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP5_Test23, @iServiceCommodityID_CategoryParent5, '快递_CategoryParent5', 1, @iReturnNo_ServiceCommodity_Part_CP5_Test23, @dPrice_ServiceCommodity_CategoryParent5, @iReturnNo_ServiceCommodity_Part_CP5_Test23, @dPrice_ServiceCommodity_CategoryParent5, NULL, NULL);

-- 对当月11号单号为"LS2099061100000000000005"的零售单进行全部退货(共21.5元)
SET @iReturnNo_SingleCommodity_All_CP6_Test23 = 3;
SET @iReturnNo_ServiceCommodity_All_CP6_Test23 = 1;
SET @iReturnRetailTradeID_All_CP6_Test23 = 0;
-- 创建零售退货单1 (退3件普通商品1、1件服务商品。共21.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000005_1', 8, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP6_Test11, @dSaleDatetime_Test23, 21.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP6_Test23 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP6_Test23, @iSingleCommodityID_CategoryParent6, '老坛酸菜牛肉面_CategoryParent6', 1, @iReturnNo_SingleCommodity_All_CP6_Test23, @dPrice_SingleCommodity_CategoryParent6, @iReturnNo_SingleCommodity_All_CP6_Test23, @dPrice_SingleCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP6_Test23, @iServiceCommodityID_CategoryParent6, '快递_CategoryParent6', 1, @iReturnNo_ServiceCommodity_All_CP6_Test23, @dPrice_ServiceCommodity_CategoryParent6, @iReturnNo_ServiceCommodity_All_CP6_Test23, @dPrice_ServiceCommodity_CategoryParent6, NULL, NULL);
-- 对当月11号单号为"LS2099061100000000000006"的零售单进行部分退货(共60.5元)
SET @iReturnNo_SingleCommodity2_Part_CP6_Test23 = 5;
SET @iReturnNo_CompositionCommodity_Part_CP6_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP6_Test23 = 0;
-- 创建零售退货单2 (退5件普通商品2、1件组合商品。共60.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000006_1', 8, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP6_Test11, @dSaleDatetime_Test23, 60.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP6_Test23 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP6_Test23, @iSingleCommodityID2_CategoryParent6, '红烧牛肉面_CategoryParent6', 1, @iReturnNo_SingleCommodity2_Part_CP6_Test23, @dPrice_SingleCommodity2_CategoryParent6, @iReturnNo_SingleCommodity2_Part_CP6_Test23, @dPrice_SingleCommodity2_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP6_Test23, @iCompositionCommodityID_CategoryParent6, '桶面大礼包_CategoryParent6', 1, @iReturnNo_CompositionCommodity_Part_CP6_Test23, @dPrice_CompositionCommodity_CategoryParent6, @iReturnNo_CompositionCommodity_Part_CP6_Test23, @dPrice_CompositionCommodity_CategoryParent6, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test23, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP4_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP4_Test23 * @dPrice_SingleCommodity_CategoryParent4) + (@iReturnNo_CompositionCommodity_All_CP4_Test23 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dReturnRetailTradeID_Part_CP4_TotalAmount_Test23 = (@iReturnNo_ServiceCommodity_Part_CP4_Test23 * @dPrice_ServiceCommodity_CategoryParent4) + (@iReturnNo_MultiPackagingCommodity_Part_CP4_Test23 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SELECT 1 INTO @iResultVerification_CP4_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP4_TotalAmount_Test23 - @dReturnRetailTradeID_Part_CP4_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 
SET @dReturnRetailTradeID_All_CP5_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP5_Test23 * @dPrice_SingleCommodity_CategoryParent5) + (@iReturnNo_MultiPackagingCommodity_All_CP5_Test23 * @dPrice_MultiPackagingCommodity_CategoryParent5);
SET @dReturnRetailTradeID_Part_CP5_TotalAmount_Test23 = (@iReturnNo_SingleCommodity2_Part_CP5_Test23 * @dPrice_SingleCommodity2_CategoryParent5) + (@iReturnNo_ServiceCommodity_Part_CP5_Test23 * @dPrice_ServiceCommodity_CategoryParent5);
SELECT 1 INTO @iResultVerification_CP5_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID5 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP5_TotalAmount_Test23 -  @dReturnRetailTradeID_Part_CP5_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 
SET @dReturnRetailTradeID_All_CP6_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP6_Test23 * @dPrice_SingleCommodity_CategoryParent6) + (@iReturnNo_ServiceCommodity_All_CP6_Test23 * @dPrice_ServiceCommodity_CategoryParent6);
SET @dReturnRetailTradeID_Part_CP6_TotalAmount_Test23 = (@iReturnNo_SingleCommodity2_Part_CP6_Test23 * @dPrice_SingleCommodity2_CategoryParent6) + (@iReturnNo_CompositionCommodity_Part_CP6_Test23 * @dPrice_CompositionCommodity_CategoryParent6);
SELECT 1 INTO @iResultVerification_CP6_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID6 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP6_TotalAmount_Test23 - @dReturnRetailTradeID_Part_CP6_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP4_Test23, @iResultVerification_CP5_Test23, @iResultVerification_CP6_Test23;
SELECT IF(@iResultVerification_CP4_Test23 = 1 AND @iResultVerification_CP5_Test23 = 1 AND @iResultVerification_CP6_Test23 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case23 Result';






SELECT '-------------------- Case24:当月24号退当月12号售卖的类别七、类别八、类别九的商品(没有售卖商品) -------------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test24 ='2099/06/24 08:08:08';
SET @iResultVerification_CP7_Test24 = 0;
SET @iResultVerification_CP8_Test24 = 0;
SET @iResultVerification_CP9_Test24 = 0;
-- 对当月12号单号为"LS2099061200000000000001"的零售单进行全部退货(共66元)
SET @iReturnNo_SingleCommodity_All_CP7_Test24 = 2;
SET @iReturnNo_CompositionCommodity_All_CP7_Test24 = 1;
SET @iReturnRetailTradeID_All_CP7_Test24 = 0;
-- 创建零售退货单1 (退2件普通商品1、1件组合商品。共66元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000001_1', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP7_Test12, @dSaleDatetime_Test24, 66, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP7_Test24 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP7_Test24, @iSingleCommodityID_CategoryParent7, '晨光圆珠笔_CategoryParent7', 1, @iReturnNo_SingleCommodity_All_CP7_Test24, @dPrice_SingleCommodity_CategoryParent7, @iReturnNo_SingleCommodity_All_CP7_Test24, @dPrice_SingleCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP7_Test24, @iCompositionCommodityID_CategoryParent7, '文具大礼包_CategoryParent7', 1, @iReturnNo_CompositionCommodity_All_CP7_Test24, @dPrice_CompositionCommodity_CategoryParent7, @iReturnNo_CompositionCommodity_All_CP7_Test24, @dPrice_CompositionCommodity_CategoryParent7, NULL, NULL);
-- 对当月12号单号为"LS2099061200000000000002"的零售单进行部分退货(共24元)
SET @iReturnNo_SingleCommodity2_Part_CP7_Test24 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP7_Test24 = 0;
-- 创建零售退货单2 (退3件普通商品2、1件多包装商品。共24元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000002_1', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP7_Test12, @dSaleDatetime_Test24, 24, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP7_Test24 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP7_Test24, @iSingleCommodityID2_CategoryParent7, '真彩圆珠笔_CategoryParent7', 1, @iReturnNo_SingleCommodity2_Part_CP7_Test24, @dPrice_SingleCommodity2_CategoryParent7, @iReturnNo_SingleCommodity2_Part_CP7_Test24, @dPrice_SingleCommodity2_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP7_Test24, @iMultiPackagingCommodityID_CategoryParent7, '一盒晨光圆珠笔_CategoryParent7', 1, @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24, @dPrice_MultiPackagingCommodity_CategoryParent7, @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24, @dPrice_MultiPackagingCommodity_CategoryParent7, NULL, NULL);

-- 对当月12号单号为"LS2099061200000000000003"的零售单进行全部退货(共714元)
SET @iReturnNo_SingleCommodity_All_CP8_Test24 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP8_Test24 = 1;
SET @iReturnRetailTradeID_All_CP8_Test24 = 0;
-- 创建零售退货单1 (退3件普通商品1、1件多包装商品。共714元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000003_1', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP8_Test12, @dSaleDatetime_Test24, 714, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP8_Test24 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP8_Test24, @iSingleCommodityID_CategoryParent8, '电动牙刷_CategoryParent8', 1, @iReturnNo_SingleCommodity_All_CP8_Test24, @dPrice_SingleCommodity_CategoryParent8, @iReturnNo_SingleCommodity_All_CP8_Test24, @dPrice_SingleCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP8_Test24, @iMultiPackagingCommodityID_CategoryParent8, '三只云南白药_CategoryParent8', 1, @iReturnNo_MultiPackagingCommodity_All_CP8_Test24, @dPrice_MultiPackagingCommodity_CategoryParent8, @iReturnNo_MultiPackagingCommodity_All_CP8_Test24, @dPrice_MultiPackagingCommodity_CategoryParent8, NULL, NULL);

-- 对当月12号单号为"LS2099061200000000000004"的零售单进行部分退货(共161元)
SET @iReturnNo_SingleCommodity2_Part_CP8_Test24 = 4;
SET @iReturnNo_ServiceCommodity_Part_CP8_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP8_Test24 = 0;
-- 创建零售退货单2 (退4件普通商品2、1件服务商品。共161元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000004_1', 1, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP8_Test12, @dSaleDatetime_Test24, 161, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP8_Test24 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP8_Test24, @iSingleCommodityID2_CategoryParent8, '南白药牙膏_CategoryParent8', 1, @iReturnNo_SingleCommodity2_Part_CP8_Test24, @dPrice_SingleCommodity2_CategoryParent8, @iReturnNo_SingleCommodity2_Part_CP8_Test24, @dPrice_SingleCommodity2_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP8_Test24, @iServiceCommodityID_CategoryParent8, '快递_CategoryParent8', 1, @iReturnNo_ServiceCommodity_Part_CP8_Test24, @dPrice_ServiceCommodity_CategoryParent8, @iReturnNo_ServiceCommodity_Part_CP8_Test24, @dPrice_ServiceCommodity_CategoryParent8, NULL, NULL);

-- 对当月12号单号为"LS2099061200000000000005"的零售单进行全部退货(共18.5元)
SET @iReturnNo_SingleCommodity_All_CP9_Test24 = 3;
SET @iReturnNo_ServiceCommodity_All_CP9_Test24 = 1;
SET @iReturnRetailTradeID_All_CP9_Test24 = 0;
-- 创建零售退货单1 (退3件普通商品1、1件服务商品。共18.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000005_1', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP9_Test12, @dSaleDatetime_Test24, 18.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP9_Test24 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP9_Test24, @iSingleCommodityID_CategoryParent9, '西红柿_CategoryParent9', 1, @iReturnNo_SingleCommodity_All_CP9_Test24, @dPrice_SingleCommodity_CategoryParent9, @iReturnNo_SingleCommodity_All_CP9_Test24, @dPrice_SingleCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP9_Test24, @iServiceCommodityID_CategoryParent9, '快递_CategoryParent9', 1, @iReturnNo_ServiceCommodity_All_CP9_Test24, @dPrice_ServiceCommodity_CategoryParent9, @iReturnNo_ServiceCommodity_All_CP9_Test24, @dPrice_ServiceCommodity_CategoryParent9, NULL, NULL);

-- 对当月12号单号为"LS2099061200000000000006"的零售单进行部分退货(共17元)
SET @iReturnNo_CompositionCommodity_Part_CP9_Test24 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP9_Test24 = 0;
-- 创建零售退货单2 (退1件组合商品、1件多包装商品。共17元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000006_1', 1, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP9_Test12, @dSaleDatetime_Test24, 17, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP9_Test24 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP9_Test24, @iCompositionCommodityID_CategoryParent9, '蔬菜大礼包_CategoryParent9', 1, @iReturnNo_CompositionCommodity_Part_CP9_Test24, @dPrice_CompositionCommodity_CategoryParent9, @iReturnNo_CompositionCommodity_Part_CP9_Test24, @dPrice_CompositionCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP9_Test24, @iMultiPackagingCommodityID_CategoryParent9, '一斤茄子_CategoryParent9', 1, @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24, @dPrice_MultiPackagingCommodity_CategoryParent9, @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24, @dPrice_MultiPackagingCommodity_CategoryParent9, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test24, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP7_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP7_Test24 * @dPrice_SingleCommodity_CategoryParent7) + (@iReturnNo_CompositionCommodity_All_CP7_Test24 * @dPrice_CompositionCommodity_CategoryParent7);
SET @dReturnRetailTradeID_Part_CP7_TotalAmount_Test24 = (@iReturnNo_SingleCommodity2_Part_CP7_Test24 * @dPrice_SingleCommodity2_CategoryParent7) + (@iReturnNo_MultiPackagingCommodity_Part_CP7_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent7);
SELECT 1 INTO @iResultVerification_CP7_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID7 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP7_TotalAmount_Test24 - @dReturnRetailTradeID_Part_CP7_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP8_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP8_Test24 * @dPrice_SingleCommodity_CategoryParent8) + (@iReturnNo_MultiPackagingCommodity_All_CP8_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent8);
SET @dReturnRetailTradeID_Part_CP8_TotalAmount_Test24 = (@iReturnNo_SingleCommodity2_Part_CP8_Test24 * @dPrice_SingleCommodity2_CategoryParent8) + (@iReturnNo_ServiceCommodity_Part_CP8_Test24 * @dPrice_ServiceCommodity_CategoryParent8);
SELECT 1 INTO @iResultVerification_CP8_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID8 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP8_TotalAmount_Test24 - @dReturnRetailTradeID_Part_CP8_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP9_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP9_Test24 * @dPrice_SingleCommodity_CategoryParent9) + (@iReturnNo_ServiceCommodity_All_CP9_Test24 * @dPrice_ServiceCommodity_CategoryParent9);
SET @dReturnRetailTradeID_Part_CP9_TotalAmount_Test24 = (@iReturnNo_CompositionCommodity_Part_CP9_Test24 * @dPrice_CompositionCommodity_CategoryParent9) + (@iReturnNo_MultiPackagingCommodity_Part_CP9_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent9);
SELECT 1 INTO @iResultVerification_CP9_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID9 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP9_TotalAmount_Test24 -  @dReturnRetailTradeID_Part_CP9_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP7_Test24, @iResultVerification_CP8_Test24, @iResultVerification_CP9_Test24;
SELECT IF(@iResultVerification_CP7_Test24 = 1 AND @iResultVerification_CP8_Test24 = 1 AND @iResultVerification_CP9_Test24 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case24 Result';




SELECT '-------------------- Case25:当月25号退当月13号售卖的类别一、类别二、类别三的商品(没有售卖商品) -------------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test25 ='2099/06/25 08:08:08';
SET @iResultVerification_CP1_Test25 = 0;
SET @iResultVerification_CP2_Test25 = 0;
SET @iResultVerification_CP3_Test25 = 0;
-- 对当月13号单号为"LS2099061300000000000001"的零售单进行全部退货(共52.5元)
SET @iReturnNo_SingleCommodity_All_CP1_Test25 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test25 = 1;
SET @iReturnRetailTradeID_All_CP1_Test25 = 0;
-- 创建零售退货单1 (退9件普通商品1、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000001_1', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test13, @dSaleDatetime_Test25, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test25 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test25, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test25, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test25, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test25, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test25, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test25, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 对当月13号单号为"LS2099061300000000000002"的零售单进行部分退货(共87.5元)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test25 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test25 = 0;
-- 创建零售退货单2 (退1件普通商品2、1件多包装商品。共87.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000002_1', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test13, @dSaleDatetime_Test25, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test25 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test25, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test25, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test25, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test25, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- 对当月13号单号为"LS2099061300000000000003"的零售单进行全部退货(共34.93元)
SET @iReturnNo_SingleCommodity_All_CP2_Test25 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test25 = 1;
SET @iReturnRetailTradeID_All_CP2_Test25 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000003_1', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test13, @dSaleDatetime_Test25, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test25 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test25, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test25, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test25, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test25, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test25, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test25, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 对当月13号单号为"LS2099061300000000000004"的零售单进行部分退货(共19.97元)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test25 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test25 = 0;
-- 创建零售退货单2 (退3件普通商品2、1件服务商品。共19.97元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000004_1', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test13, @dSaleDatetime_Test25, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test25 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test25, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test25, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test25, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test25, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- 对当月13号单号为"LS2099061300000000000005"的零售单进行全部退货(共24.4元)
SET @iReturnNo_SingleCommodity_All_CP3_Test25 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test25 = 1;
SET @iReturnRetailTradeID_All_CP3_Test25 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000005_1', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test13, @dSaleDatetime_Test25, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test25 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test25, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test25, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test25, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test25, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test25, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test25, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 对当月13号单号为"LS2099061300000000000006"的零售单进行部分退货(共42.68元)
SET @iReturnNo_CompositionCommodity_Part_CP3_Test25 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test25 = 0;
-- 创建零售退货单2 (退1件组合商品、1件多包装商品。共42.68元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000006_1', 2, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test13, @dSaleDatetime_Test25, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test25 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test25, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test25, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test25, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test25, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test25, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP1_Test25 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test25 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test25 = (@iReturnNo_SingleCommodity2_Part_CP1_Test25 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP2_Test25 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_CP2_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test25 = (@iReturnNo_SingleCommodity2_Part_CP2_Test25 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_Part_CP2_Test25 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP3_Test25 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test25 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test25 = (@iReturnNo_CompositionCommodity_Part_CP3_Test25 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test25, @iResultVerification_CP2_Test25, @iResultVerification_CP3_Test25;
SELECT IF(@iResultVerification_CP1_Test25 = 1 AND @iResultVerification_CP2_Test25 = 1 AND @iResultVerification_CP3_Test25 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case25 Result';





SELECT '-------------------- Case26:当月26号退当月14号售卖的类别一、类别二、类别三的商品(没有售卖商品) -------------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test26 ='2099/06/26 08:08:08';
SET @iResultVerification_CP1_Test26 = 0;
SET @iResultVerification_CP2_Test26 = 0;
SET @iResultVerification_CP3_Test26 = 0;
-- 对当月14号单号为"LS2099061400000000000001"的零售单进行全部退货(共52.5元)
SET @iReturnNo_SingleCommodity2_All_CP1_Test26 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test26 = 1;
SET @iReturnRetailTradeID_All_CP1_Test26 = 0;
-- 创建零售退货单1 (退9件普通商品2、1件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000001_1', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test14, @dSaleDatetime_Test26, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test26 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test26, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_All_CP1_Test26, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_All_CP1_Test26, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test26, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test26, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test26, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 对当月14号单号为"LS2099061400000000000002"的零售单进行部分退货(共89元)
SET @iReturnNo_ServiceCommodity_Part_CP1_Test26 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test26 = 0;
-- 创建零售退货单2 (退1件多包装商品、1件服务商品。共89元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000002_1', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test14, @dSaleDatetime_Test26, 89, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test26 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test26, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_CP1_Test26, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_CP1_Test26, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test26, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- 对当月14号单号为"LS2099061400000000000003"的零售单进行全部退货(共34.93元)
SET @iReturnNo_SingleCommodity2_All_CP2_Test26 = 5;
SET @iReturnNo_CompositionCommodity_All_CP2_Test26 = 1;
SET @iReturnRetailTradeID_All_CP2_Test26 = 0;
-- 创建零售退货单1 (退5件普通商品2、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000003_1', 3, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test14, @dSaleDatetime_Test26, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test26 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test26, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_CP2_Test26, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_CP2_Test26, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test26, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iReturnNo_CompositionCommodity_All_CP2_Test26, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_All_CP2_Test26, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 对当月14号单号为"LS2099061400000000000004"的零售单进行部分退货(共19.97元)
SET @iReturnNo_SingleCommodity_Part_CP2_Test26 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test26 = 0;
-- 创建零售退货单2 (退3件普通商品1、1件服务商品。共19.97元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000004_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test14, @dSaleDatetime_Test26, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test26 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test26, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_Part_CP2_Test26, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_Part_CP2_Test26, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test26, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test26, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test26, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- 对当月14号单号为"LS2099061400000000000005"的零售单进行全部退货(共24.4元)
SET @iReturnNo_SingleCommodity2_All_CP3_Test26 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test26 = 1;
SET @iReturnRetailTradeID_All_CP3_Test26 = 0;
-- 创建零售退货单1 (退5件普通商品2、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000005_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test14, @dSaleDatetime_Test26, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test26 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test26, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_CP3_Test26, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_CP3_Test26, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test26, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test26, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test26, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 对当月14号单号为"LS2099061400000000000006"的零售单进行部分退货(共34.92元)
SET @iReturnNo_SingleCommodity_Part_CP3_Test26 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test26 = 0;
-- 创建零售退货单2 (退3件普通商品1、1件组合商品。共34.92元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000006_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test14, @dSaleDatetime_Test26, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test26 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test26, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_Part_CP3_Test26, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_Part_CP3_Test26, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test26, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test26, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test26, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test26, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d');

SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP1_Test26 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test26 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test26 = (@iReturnNo_ServiceCommodity_Part_CP1_Test26 * @dPrice_ServiceCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test26 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP2_Test26 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_CompositionCommodity_All_CP2_Test26 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test26 = (@iReturnNo_SingleCommodity_Part_CP2_Test26 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test26 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP3_Test26 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test26 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test26 = (@iReturnNo_SingleCommodity_Part_CP3_Test26 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test26 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test26, @iResultVerification_CP2_Test26, @iResultVerification_CP3_Test26;
SELECT IF(@iResultVerification_CP1_Test26 = 1 AND @iResultVerification_CP2_Test26 = 1 AND @iResultVerification_CP3_Test26 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case26 Result';





SELECT '-------------------- Case27:当月27号退当月15号售卖的类别一、类别二、类别三的商品(没有售卖商品) -------------------------' AS 'Case27';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test27 ='2099/06/27 08:08:08';
SET @iResultVerification_CP1_Test27 = 0;
SET @iResultVerification_CP2_Test27 = 0;
SET @iResultVerification_CP3_Test27 = 0;
-- 对当月14号单号为"LS2099061500000000000001"的零售单进行全部退货(共56元)
SET @iReturnNo_SingleCommodity_All_CP1_Test27 = 9;
SET @iReturnNo_SingleCommodity2_All_CP1_Test27 = 1;
SET @iReturnNo_CompositionCommodity_All_CP1_Test27 = 1;
SET @iReturnRetailTradeID_All_CP1_Test27 = 0;
-- 创建零售退货单1 (退9件普通商品1、1件普通商品2、1件组合商品。共56元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000001_1', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test15, @dSaleDatetime_Test27, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test27 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test27, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test27, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_All_CP1_Test27, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_All_CP1_Test27, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test27, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test27, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);

-- 对当月14号单号为"LS2099061500000000000002"的零售单进行部分退货(共5元)
SET @iReturnNo_ServiceCommodity_Part_CP1_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test27 = 0;
-- 创建零售退货单2 (退1件多包装商品、1件服务商品。共5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000002_1', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test15, @dSaleDatetime_Test27, 5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test27 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test27, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_CP1_Test27, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_CP1_Test27, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);

-- 对当月14号单号为"LS2099061500000000000003"的零售单进行全部退货(共49.9元)
SET @iReturnNo_SingleCommodity_All_CP2_Test27 = 5;
SET @iReturnNo_SingleCommodity2_All_CP2_Test27 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test27 = 1;
SET @iReturnRetailTradeID_All_CP2_Test27 = 0;
-- 创建零售退货单1 (退5件普通商品1、3件普通商品2、1件多包装商品。共49.9元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000003_1', 4, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test15, @dSaleDatetime_Test27, 49.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test27 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test27, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test27, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_CP2_Test27, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_CP2_Test27, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test27, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test27, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);

-- 对当月14号单号为"LS2099061500000000000004"的零售单进行部分退货(共49.9元)
SET @iReturnNo_CompositionCommodity_Part_CP2_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test27 = 0;
-- 创建零售退货单2 (退1件组合商品。共49.9元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000004_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test15, @dSaleDatetime_Test27, 49.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test27 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test27, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_CP2_Test27, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_CP2_Test27, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- 对当月14号单号为"LS2099061500000000000005"的零售单进行全部退货(共36.04元)
SET @iReturnNo_SingleCommodity_All_CP3_Test27 = 5;
SET @iReturnNo_SingleCommodity2_All_CP3_Test27 = 3;
SET @iReturnNo_ServiceCommodity_All_CP3_Test27 = 1;
SET @iReturnRetailTradeID_All_CP3_Test27 = 0;
-- 创建零售退货单1 (退5件普通商品1、3件普通商品2、1件服务商品。共36.04元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000005_1', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test15, @dSaleDatetime_Test27, 36.04, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test27 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test27, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test27, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_CP3_Test27, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_CP3_Test27, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test27, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test27, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- 对当月14号单号为"LS2099061500000000000006"的零售单进行部分退货(共19.4元)
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test27 = 0;
-- 创建零售退货单2 (退1件多包装商品。共19.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000006_1', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test15, @dSaleDatetime_Test27, 19.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test27 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test27, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test27, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d');

SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP1_Test27 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_SingleCommodity2_All_CP1_Test27 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test27 = (@iReturnNo_CompositionCommodity_All_CP1_Test27 * @dPrice_CompositionCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_CP1_Test27 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP2_Test27 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_SingleCommodity2_All_CP2_Test27 * @dPrice_SingleCommodity2_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test27 = (@iReturnNo_MultiPackagingCommodity_All_CP2_Test27 * @dPrice_MultiPackagingCommodity_CategoryParent2) + (@iReturnNo_CompositionCommodity_Part_CP2_Test27 * @dPrice_CompositionCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP3_Test27 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_SingleCommodity2_All_CP3_Test27 * @dPrice_SingleCommodity2_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test27 = (@iReturnNo_ServiceCommodity_All_CP3_Test27 * @dPrice_ServiceCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test27 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test27, @iResultVerification_CP2_Test27, @iResultVerification_CP3_Test27;
SELECT IF(@iResultVerification_CP1_Test27 = 1 AND @iResultVerification_CP2_Test27 = 1 AND @iResultVerification_CP3_Test27 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case27 Result';




SELECT '-------------------- Case28:当月28号售卖类别一和类别二的商品，并且当天进行退货 -------------------------' AS 'Case28';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test28 ='2099/06/28 08:08:08';
SET @iResultVerification_CP1_Test28 = 0;
SET @iResultVerification_CP2_Test28 = 0;
-- 售卖类别一的商品
SET @iNO_SingleCommodity_CP1_Test28 = 3;
SET @iNO_CompositionCommodity_CP1_Test28 = 2;
SET @iNO_SingleCommodity2_CP1_Test28 = 5;
SET @iNO_MultiPackagingCommodity_CP1_Test28 = 3;
SET @iNO_ServiceCommodity_CP1_Test28 = 1;
SET @iRetailTradeID1_CP1_Test28 = 0;
SET @iRetailTradeID2_CP1_Test28 = 0;
-- 创建零售单1(售卖3件普通商品1、2件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test28 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test28, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test28, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- 创建零售单2(售卖5件普通商品2、3件多包装商品、1件服务商品。共274.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 274.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test28 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iServiceCommodityID_CategoryParent1, '快递_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test28, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test28, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- 售卖类别二的商品
SET @iNO_SingleCommodity_CP2_Test28 = 3;
SET @iNO_MultiPackagingCommodity_CP2_Test28 = 2;
SET @iNO_SingleCommodity2_CP2_Test28 = 3;
SET @iNO_CompositionCommodity_CP2_Test28 = 2;
SET @iNO_ServiceCommodity_CP2_Test28 = 1;
SET @iRetailTradeID1_CP2_Test28 = 0;
SET @iRetailTradeID2_CP2_Test28 = 0;
-- 创建零售单1(售卖3件普通商品1、2件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000003', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test28 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test28, @iMultiPackagingCommodityID_CategoryParent2, '一箱百事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test28, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2(售卖3件普通商品2、2件组合商品、1件服务商品。共119.77元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000004', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 119.77, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test28 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test28, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test28, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- 对当天单号为"LS2099062800000000000001"的零售单进行全部退货(共52.5元)
SET @iReturnNo_SingleCommodity_All_CP1_Test28 = 3;
SET @iReturnNo_CompositionCommodity_All_CP1_Test28 = 2;
SET @iReturnRetailTradeID_All_CP1_Test28 = 0;
-- 创建零售退货单1(退3件普通商品1、2件组合商品。共52.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000001_1', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test28, @dSaleDatetime_Test28, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test28 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test28, @iSingleCommodityID_CategoryParent1, '可口可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test28, @iCompositionCommodityID_CategoryParent1, '可乐大礼包_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);

-- 对当天单号为"LS2099062800000000000002"的零售单进行部分退货(共269.5元)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test28 = 5;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28 = 3;
SET @iReturnRetailTradeID_Part_CP1_Test28 = 0;
-- 创建零售退货单2(退5件普通商品2、3件多包装商品。共269.5元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000002_1', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test28, @dSaleDatetime_Test28, 269.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test28 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test28, @iMultiPackagingCommodityID_CategoryParent1, '一听百事可乐_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test28, @iSingleCommodityID2_CategoryParent1, '百事可乐_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);

-- 对当天单号为"LS2099062800000000000003"的零售单进行全部退货(共34.93元)
SET @iReturnNo_SingleCommodity_All_CP2_Test28 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test28 = 2;
SET @iReturnRetailTradeID_All_CP2_Test28 = 0;
-- 创建零售退货单1(退3件普通商品1、2件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000003_1', 5, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........',@iRetailTradeID1_CP2_Test28, @dSaleDatetime_Test28, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test28 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test28, @iMultiPackagingCommodityID_CategoryParent2, '一箱百事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test28, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);

-- 对当天单号为"LS2099062800000000000004"的零售单进行部分退货(共19.97元)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test28 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test28 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test28 = 0;
-- 创建零售退货单2(退3件普通商品2、1件服务商品。共19.97元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000004_1', 5, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test28, @dSaleDatetime_Test28, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test28 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test28, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test28, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test28, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d');

SET @dRetailTradeID1_CP1_TotalAmount_Test28 = (@iNO_SingleCommodity_CP1_Test28 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test28 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_CP1_TotalAmount_Test28 = (@iNO_MultiPackagingCommodity_CP1_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test28 * @dPrice_ServiceCommodity_CategoryParent1) -- 
												+ (@iNO_SingleCommodity2_CP1_Test28 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test28 = (@iReturnNo_SingleCommodity_All_CP1_Test28 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test28 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test28 = (@iReturnNo_SingleCommodity2_Part_CP1_Test28 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test28 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_CP1_TotalAmount_Test28 + @dRetailTradeID2_CP1_TotalAmount_Test28 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test28 --  
		- @dReturnRetailTradeID_Part_CP1_TotalAmount_Test28 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP2_TotalAmount_Test28 = (@iNO_MultiPackagingCommodity_CP2_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent2) + (@iNO_SingleCommodity_CP2_Test28 * @dPrice_SingleCommodity_CategoryParent2);
SET @dRetailTradeID2_CP2_TotalAmount_Test28 = (@iNO_SingleCommodity2_CP2_Test28 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test28 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test28 * @dPrice_ServiceCommodity_CategoryParent2);
SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test28 = (@iReturnNo_MultiPackagingCommodity_All_CP2_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent2) +(@iReturnNo_SingleCommodity_All_CP2_Test28 * @dPrice_SingleCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test28 = (@iReturnNo_SingleCommodity2_Part_CP2_Test28 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test28 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test28 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_CP2_TotalAmount_Test28 + @dRetailTradeID2_CP2_TotalAmount_Test28 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test28 -- 
		- @dReturnRetailTradeID_Part_CP2_TotalAmount_Test28 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test28, @iResultVerification_CP2_Test28;
SELECT IF(@iResultVerification_CP1_Test28 = 1 AND @iResultVerification_CP2_Test28 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case28 Result';









SELECT '-------------------- Case29:当月29号售卖类别二和类别三的商品，并且当天进行退货 -------------------------' AS 'Case29';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test29 ='2099/06/29 08:08:08';
SET @iResultVerification_CP2_Test29 = 0;
SET @iResultVerification_CP3_Test29 = 0;
-- 售卖类别二的商品
SET @iNO_SingleCommodity_CP2_Test29 = 5;
SET @iNO_MultiPackagingCommodity_CP2_Test29 = 1;
SET @iNO_SingleCommodity2_CP2_Test29 = 3;
SET @iNO_CompositionCommodity_CP2_Test29 = 1;
SET @iNO_ServiceCommodity_CP2_Test29 = 1;
SET @iRetailTradeID1_CP2_Test29 = 0;
SET @iRetailTradeID2_CP2_Test29 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000001', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test29 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test29, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test29, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件服务商品。共69.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000002', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test29 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iServiceCommodityID_CategoryParent2, '快递_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test29, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test29, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- 售卖类别三的商品
SET @iNO_SingleCommodity_CP3_Test29 = 5;
SET @iNO_ServiceCommodity_CP3_Test29 = 1;
SET @iNO_SingleCommodity2_CP3_Test29 = 3;
SET @iNO_CompositionCommodity_CP3_Test29 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test29 = 1;
SET @iRetailTradeID1_CP3_Test29 = 0;
SET @iRetailTradeID2_CP3_Test29 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000003', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test29 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test29, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test29, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000004', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test29 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test29, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test29, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);

-- 对当天单号为"LS2099062900000000000001"的零售单进行全部退货(共34.93元)
SET @iReturnNo_SingleCommodity_All_CP2_Test29 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test29 = 1;
SET @iReturnRetailTradeID_All_CP2_Test29 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件多包装商品。共34.93元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000001', 3, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test29, @dSaleDatetime_Test29, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test29 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test29, @iSingleCommodityID_CategoryParent2, '乐事薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test29, @iMultiPackagingCommodityID_CategoryParent2, '一箱乐事薯片_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);

-- 对当天单号为"LS2099062900000000000002"的零售单进行部分退货(共64.87元)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test29 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP2_Test29 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test29 = 0;
-- 创建零售退货单2 (退3件普通商品2、1件组合商品。共64.87元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000002', 4, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test29, @dSaleDatetime_Test29, 64.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test29 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test29, @iSingleCommodityID2_CategoryParent2, '可比克薯片_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test29, @iCompositionCommodityID_CategoryParent2, '薯片大礼包_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- 对当天单号为"LS2099062900000000000003"的零售单进行全部退货(共24.4元)
SET @iReturnNo_SingleCommodity_All_CP3_Test29 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test29 = 1;
SET @iReturnRetailTradeID_All_CP3_Test29 = 0;
-- 创建零售退货单1 (退5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000003', 5, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test29, @dSaleDatetime_Test29, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test29 = last_insert_id();
-- 创建零售退货商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test29, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test29, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- 对当天单号为"LS2099062900000000000004"的零售单进行部分退货(共34.92元)
SET @iReturnNo_SingleCommodity2_Part_CP3_Test29 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test29 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test29 = 0;
-- 创建零售退货单2 (退3件普通商品2、1件组合商品。共34.92元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000004', 6, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test29, @dSaleDatetime_Test29, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test29 = last_insert_id();
-- 创建零售退货商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test29, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity2_Part_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_Part_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test29, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test29, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d');

SET @dRetailTradeID1_CP2_TotalAmount_Test29 = (@iNO_SingleCommodity_CP2_Test29 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_CP2_TotalAmount_Test29 = (@iNO_SingleCommodity2_CP2_Test29 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_ServiceCommodity_CP2_Test29 * @dPrice_ServiceCommodity_CategoryParent2) -- 
											+ (@iNO_CompositionCommodity_CP2_Test29 * @dPrice_CompositionCommodity_CategoryParent2);
SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test29 = (@iReturnNo_SingleCommodity_All_CP2_Test29 * @dPrice_SingleCommodity_CategoryParent2) +(@iReturnNo_MultiPackagingCommodity_All_CP2_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test29 = (@iReturnNo_SingleCommodity2_Part_CP2_Test29 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_CompositionCommodity_Part_CP2_Test29 * @dPrice_CompositionCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test29 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_CP2_TotalAmount_Test29 + @dRetailTradeID2_CP2_TotalAmount_Test29 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test29 -- 
		- @dReturnRetailTradeID_Part_CP2_TotalAmount_Test29 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP3_TotalAmount_Test29 = (@iNO_SingleCommodity_CP3_Test29 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test29 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_CP3_TotalAmount_Test29 = (@iNO_SingleCommodity2_CP3_Test29 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test29 * @dPrice_CompositionCommodity_CategoryParent3) -- 
												+ (@iNO_MultiPackagingCommodity_CP3_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test29 = (@iReturnNo_SingleCommodity_All_CP3_Test29 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test29 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test29 = (@iReturnNo_SingleCommodity2_Part_CP3_Test29 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test29 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test29 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_CP3_TotalAmount_Test29 + @dRetailTradeID2_CP3_TotalAmount_Test29 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test29 -- 
		- @dReturnRetailTradeID_Part_CP3_TotalAmount_Test29 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP2_Test29, @iResultVerification_CP3_Test29;
SELECT IF(@iResultVerification_CP2_Test29 = 1 AND @iResultVerification_CP3_Test29 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case29 Result';










SELECT '-------------------- Case30:当月30号售卖类别三和类别四的商品，并且当天进行退货 -------------------------' AS 'Case30';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test30 ='2099/06/30 08:08:08';
SET @iResultVerification_CP3_Test30 = 0;
SET @iResultVerification_CP4_Test30 = 0;
-- 售卖类别三的商品
SET @iNO_SingleCommodity_CP3_Test30 = 5;
SET @iNO_ServiceCommodity_CP3_Test30 = 1;
SET @iNO_SingleCommodity2_CP3_Test30 = 3;
SET @iNO_CompositionCommodity_CP3_Test30 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test30 = 1;
SET @iRetailTradeID1_CP3_Test30 = 0;
SET @iRetailTradeID2_CP3_Test30 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000001', 2, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test30 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test30, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test30, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件组合商品、1件多包装商品。共54.32元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000002', 4, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test30 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iMultiPackagingCommodityID_CategoryParent3, '一箱伊利牛奶_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test30, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test30, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);

-- 售卖类别四的商品
SET @iNO_SingleCommodity_CP4_Test30 = 2;
SET @iNO_CompositionCommodity_CP4_Test30 = 1;
SET @iNO_SingleCommodity2_CP4_Test30 = 3;
SET @iNO_ServiceCommodity_CP4_Test30 = 1;
SET @iNO_MultiPackagingCommodity_CP4_Test30 = 1;
SET @iRetailTradeID1_CP4_Test30 = 0;
SET @iRetailTradeID2_CP4_Test30 = 0;
-- 创建零售单1 (售卖2件普通商品1、1件组合商品。共247.6元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000003', 3, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP4_Test30 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test30, @iSingleCommodityID_CategoryParent4, '威猛先生_CategoryParent4', 1, @iNO_SingleCommodity_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, @iNO_SingleCommodity_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test30, @iCompositionCommodityID_CategoryParent4, '清洁大礼包_CategoryParent4', 1, @iNO_CompositionCommodity_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, @iNO_CompositionCommodity_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- 创建零售单2 (售卖3件普通商品2、1件多包装商品、1件服务商品。共265元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000004', 1, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 265, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP4_Test30 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iSingleCommodityID2_CategoryParent4, '蓝月亮_CategoryParent4', 1, @iNO_SingleCommodity2_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, @iNO_SingleCommodity2_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iServiceCommodityID_CategoryParent4, '快递_CategoryParent4', 1, @iNO_ServiceCommodity_CP4_Test30, @dPrice_ServiceCommodity_CategoryParent4, @iNO_ServiceCommodity_CP4_Test30, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iMultiPackagingCommodityID_CategoryParent4, '一箱蓝月亮_CategoryParent4', 1, @iNO_MultiPackagingCommodity_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, @iNO_MultiPackagingCommodity_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);

-- 对当天单号为"LS2099063000000000000001"的零售单进行全部退货(共24.4元)
SET @iReturnNo_SingleCommodity_All_CP3_Test30 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test30 = 1;
SET @iReturnRetailTradeID_All_CP3_Test30 = 0;
-- 创建零售单1 (售卖5件普通商品1、1件服务商品。共24.4元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000001_1', 5, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test30, @dSaleDatetime_Test30, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test30 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test30, @iSingleCommodityID_CategoryParent3, '伊利牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test30, @iServiceCommodityID_CategoryParent3, '快递_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- 对当天单号为"LS2099063000000000000002"的零售单进行部分退货(共34.92元)
SET @iReturnNo_SingleCommodity2_Part_CP3_Test30 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test30 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test30 = 0;
-- 创建零售单2 (售卖3件普通商品2、1件组合商品。共34.92元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000002_1', 6, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test30, @dSaleDatetime_Test30, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test30 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test30, @iSingleCommodityID2_CategoryParent3, '蒙牛牛奶_CategoryParent3', 1, @iReturnNo_SingleCommodity2_Part_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_Part_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test30, @iCompositionCommodityID_CategoryParent3, '牛奶大礼包_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);

-- 对当天单号为"LS2099063000000000000003"的零售单进行全部退货(共247.6元)
SET @iReturnNo_SingleCommodity_All_CP4_Test30 = 2;
SET @iReturnNo_CompositionCommodity_All_CP4_Test30 = 1;
SET @iReturnRetailTradeID_All_CP4_Test30 = 0;
-- 创建零售单1 (售卖2件普通商品1、1件组合商品。共247.6元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000003_1', 7, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........',@iRetailTradeID1_CP4_Test30, @dSaleDatetime_Test30, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP4_Test30 = last_insert_id();
-- 创建零售商品1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test30, @iSingleCommodityID_CategoryParent4, '威猛先生_CategoryParent4', 1, @iReturnNo_SingleCommodity_All_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, @iReturnNo_SingleCommodity_All_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test30, @iCompositionCommodityID_CategoryParent4, '清洁大礼包_CategoryParent4', 1, @iReturnNo_CompositionCommodity_All_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, @iReturnNo_CompositionCommodity_All_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);

-- 对当天单号为"LS2099063000000000000004"的零售单进行部分退货(共260元)
SET @iReturnNo_SingleCommodity2_Part_CP4_Test30 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30 = 1;
SET @iReturnRetailTradeID_Part_CP4_Test30 = 0;
-- 创建零售单2 (售卖3件普通商品2、1件多包装商品。共260元)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000004_1', 8, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP4_Test30, @dSaleDatetime_Test30, 260, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP4_Test30 = last_insert_id();
-- 创建零售商品2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test30, @iSingleCommodityID2_CategoryParent4, '蓝月亮_CategoryParent4', 1, @iReturnNo_SingleCommodity2_Part_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, @iReturnNo_SingleCommodity2_Part_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test30, @iMultiPackagingCommodityID_CategoryParent4, '一箱蓝月亮_CategoryParent4', 1, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test30, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d');

SET @dRetailTradeID1_CP3_TotalAmount_Test30 = (@iNO_SingleCommodity_CP3_Test30 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test30 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_CP3_TotalAmount_Test30 = (@iNO_SingleCommodity2_CP3_Test30 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test30 * @dPrice_CompositionCommodity_CategoryParent3) -- 
	   											+ (@iNO_MultiPackagingCommodity_CP3_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test30 = (@iReturnNo_SingleCommodity_All_CP3_Test30 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test30 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test30 = (@iReturnNo_SingleCommodity2_Part_CP3_Test30 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test30 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test30 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_CP3_TotalAmount_Test30 + @dRetailTradeID2_CP3_TotalAmount_Test30 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test30 -- 
		- @dReturnRetailTradeID_Part_CP3_TotalAmount_Test30 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP4_TotalAmount_Test30 = (@iNO_SingleCommodity_CP4_Test30 * @dPrice_SingleCommodity_CategoryParent4) + (@iNO_CompositionCommodity_CP4_Test30 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dRetailTradeID2_CP4_TotalAmount_Test30 = (@iNO_SingleCommodity2_CP4_Test30 * @dPrice_SingleCommodity2_CategoryParent4) + (@iNO_ServiceCommodity_CP4_Test30 * @dPrice_ServiceCommodity_CategoryParent4) -- 
												+ (@iNO_MultiPackagingCommodity_CP4_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SET @dReturnRetailTradeID_All_CP4_TotalAmount_Test30 = (@iReturnNo_SingleCommodity_All_CP4_Test30 * @dPrice_SingleCommodity_CategoryParent4) + (@iReturnNo_CompositionCommodity_All_CP4_Test30 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dReturnRetailTradeID_Part_CP4_TotalAmount_Test30 = (@iReturnNo_SingleCommodity2_Part_CP4_Test30 * @dPrice_SingleCommodity2_CategoryParent4) + (@iReturnNo_MultiPackagingCommodity_Part_CP4_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SELECT 1 INTO @iResultVerification_CP4_Test30 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = @dRetailTradeID1_CP4_TotalAmount_Test30 + @dRetailTradeID2_CP4_TotalAmount_Test30 - @dReturnRetailTradeID_All_CP4_TotalAmount_Test30 -- 
		- @dReturnRetailTradeID_Part_CP4_TotalAmount_Test30 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP3_Test30, @iResultVerification_CP4_Test30, @iErrorCode;
SELECT IF(@iResultVerification_CP3_Test30 = 1 AND @iResultVerification_CP4_Test30 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'测试成功','测试失败') AS 'Test Case30 Result';






-- 删除2099/06/01的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test1;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test1;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test1;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test1;
-- 删除2099/06/02的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test2;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test2;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test2;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test2;
-- 删除2099/06/03的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test3;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test3;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test3;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test3;
-- 删除2099/06/04的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test4;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test4;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test4;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test4;
-- 删除2099/06/05的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test5;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test5;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test5;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test5;
-- 删除2099/06/06的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test6;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test6;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test6;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test6;
-- 删除2099/06/10的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test10;
-- 删除2099/06/11的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP4_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP4_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP4_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP4_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP5_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP5_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP5_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP5_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP6_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP6_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP6_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP6_Test11;
-- 删除2099/06/12的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP7_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP7_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP7_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP7_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP8_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP8_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP8_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP8_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP9_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP9_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP9_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP9_Test12;
-- 删除2099/06/13的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test13;
-- 删除2099/06/14的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test14;
-- 删除2099/06/15的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test15;
-- 删除2099/06/16的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test16;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test16;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test16;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test16;
-- 删除2099/06/17的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test17;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test17;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test17;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test17;
-- 删除2099/06/18的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test18;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test18;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test18;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test18;
-- 删除2099/06/19的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test19;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test19;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test19;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test19;
-- 删除2099/06/20的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test20;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test20;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test20;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test20;
-- 删除2099/06/21的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test21;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test21;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test21;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test21;
-- 删除2099/06/22的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test22;
-- 删除2099/06/23的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP4_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP4_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP4_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP4_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP5_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP5_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP5_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP5_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP6_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP6_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP6_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP6_Test23;
-- 删除2099/06/24的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP7_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP7_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP7_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP7_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP8_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP8_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP8_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP8_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP9_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP9_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP9_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP9_Test24;
-- 删除2099/06/25的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test25;
-- 删除2099/06/26的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test26;
-- 删除2099/06/27的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test27;
-- 删除2099/06/28的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d');
-- 零售单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test28;
-- 零售退货单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test28;
-- 删除2099/06/29的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d');
-- 零售单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test29;
-- 零售退货单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test29;
-- 删除2099/06/30的日报表
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d');
-- 零售单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP4_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP4_Test30;
-- 零售退货单
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP4_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP4_Test30;





-- 删除类别一的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent1;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent1;
-- 删除类别二的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent2;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent2;
-- 删除类别三的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent3;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent3;
-- 删除类别四的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent4;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent4;
-- 删除类别五的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent5;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent5;
-- 删除类别六的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent6;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent6;
-- 删除类别七的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent7;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent7;
-- 删除类别八的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent8;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent8;
-- 删除类别九的商品
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent9;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent9;