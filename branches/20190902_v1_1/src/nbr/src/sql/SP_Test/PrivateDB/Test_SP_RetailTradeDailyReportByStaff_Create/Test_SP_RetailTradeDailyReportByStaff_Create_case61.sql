SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case61.sql+++++++++++++++++++++++';

SELECT '----- case61: 员工A创建一个零售单卖A一个商品，商品A为未入库商品 -------' AS 'Case61';

-- 商品表
SET @commodityPrice = 20;
-- 零售单商品表
SET @rt1CommotidyNO = 10;
SET @rt1CommotidyPrice = @commodityPrice;
SET @rt1AmuontCash = 200.00000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO * @rt1CommotidyPrice;
-- 零售单收银汇总
SET @rggWorkTimeStart1 = '2041/02/02 00:00:00';
SET @rggWorkTimeEnd1 = '2041/02/02 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay;
SET @rggNO = 1;
SET @iStaffID = 9;
-- 零售单商品来源表
SET @rtcs1NO = @rt1CommotidyNO;
SET @rtcs1WarehousingID = null;

-- 创建一个商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '未入库商品A', '普通商品A', '个', 3, '包', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00'); 
SET @commIDA = last_insert_id();
-- 创建A的一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDA = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();

-- 零售单
SET @saleDatetime = '2041/02/02 06:00:00';
SET @syncDatetime = '2041/02/02 06:00:00';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontWeChat,@rt1AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();

-- 零售单商品表
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commIDA,'未入库商品A', @barcodeIDA, @rt1CommotidyNO, @rt1CommotidyPrice, @rt1CommotidyNO, @rt1CommotidyPrice, @rt1CommotidyPrice);
SET @rtcID1 = last_insert_id();

-- 零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@rtcID1, @rtcs1NO, @rtcs1WarehousingID, @commIDA);
SET @rtcSourceID1 = last_insert_id();

-- 员工A收银汇总
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1;
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case61.1 Testing Result';

-- 售卖总金额
SET @saleTotalAmount = @rt1TotalAmount;
-- 售卖商品总成本
SET @saleTotalCost = 0;
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
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


-- 进行业绩报表的数据结果验证
SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff
WHERE F_StaffID = @iStaffID
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @saleTotalAmount
AND F_GrossMargin = @saleTotalMargin
AND F_NO = (
	SELECT COUNT(1)
	FROM t_retailtrade 
	WHERE F_SourceID = -1
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case1 Result';

-- 进行数据删除，避免污染数据库
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_providercommodity WHERE F_CommodityID = @commIDA;
DELETE FROM t_barcodes WHERE F_CommodityID = @commIDA ;
DELETE FROM t_commodity WHERE F_ID = @commIDA;