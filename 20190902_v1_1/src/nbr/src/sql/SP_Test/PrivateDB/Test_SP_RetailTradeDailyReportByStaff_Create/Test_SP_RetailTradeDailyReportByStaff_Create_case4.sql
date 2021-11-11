SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case4.sql+++++++++++++++++++++++';

SELECT '-------------------- Case4：当天存在未入库商品 -------------------------' AS 'Case4';
-- 零售单
SET @rt1CommotidyNO = 15;
SET @rt1CommotidyPrice = 20.000000;
SET @rt1AmuontCash = 300.00000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO * @rt1CommotidyPrice;
-- 零售单商品来源表
SET @rtcs1NO = 15;
SET @rtcs1WarehousingID = 15;
-- 商品去向表
SET @destinationCommodityID155 = 155;
SET @rtcd1NO = 8;
SET @rtcd1WarehousingID = 15;
-- 零售单收银汇总
SET @rggWorkTimeStart1='2119-05-19 00:00:00';
SET @rggWorkTimeEnd1='2119-05-19 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay;
SET @rggNO = 1;
-- 收银员
SET @iStaffID = 2;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, 100.000000, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

-- 创建一个商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '未入库商品', '普通商品', '个', 3, '包', 1, 1, 'TZQ666', 1, 
19, 19, 0, 1, 365, 3, '2119-05-19 09:00:00', 1, 0, 0, 0, 0, '2119-05-19 09:00:00', '2019-05-19 09:00:00');
SET @commID = last_insert_id();
-- 创建一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commID, 'tzq66666', '2119-05-19 09:00:00', '2119-05-19 09:00:00');
SET @barcodeID = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commID, 1);
SET @providerCommID = last_insert_id();

SET @saleDatetime = '2119-05-19 09:00:00';
SET @syncDatetime = '2119-05-19 09:00:00';
-- 进行售卖
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119051901010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rrt1AmuontAliPay,@rrt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
-- 零售单商品表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commID, '未入库商品', @barcodeID, @rt1CommotidyNO, 20, 15, @rt1CommotidyPrice, 19);
SET @rtCommID1 = last_insert_id();
-- 插入零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtCommID1, @rtcs1NO, NULL, @commID);  -- F_WarehousingID为NULL
SET @rtcSourceID1 = last_insert_id();

SET @dSaleDatetime = '2119-5-19 00:00:00';
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 售卖总金额
SET @saleTotalAmount = (@rt1CommotidyNO * @rt1CommotidyPrice);
-- 售卖商品总成本
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = NULL AND F_CommodityID = @commID));
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - (ifnull(@saleTotalCost, 0.00000));
-- 退货总金额
SET @returnTotalAmount = 0.000000;
-- 退货商品总成本
SET @returnTotalCost = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin = 0.000000;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;
SELECT @saleTotalMargin;
SET @ResultVerification1=0;
-- 进行业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffID 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case4 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID=@rtcSourceID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID1;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commID;