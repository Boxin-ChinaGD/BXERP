SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case65.sql+++++++++++++++++++++++';

SELECT '----- case65: 员工A创建一个零售单卖A一个商品，商品A为未入库商品,创建一个退货单，对商品A进行部分退货 -------' AS 'Case65';

-- 商品表
SET @commodityPrice1_A = 10;
-- 零售单商品表
SET @rt1CommotidyNO1_A = 10;
SET @rt1CommotidyPrice1_A = @commodityPrice1_A;
SET @rt1AmuontCash = 100.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1_A * @rt1CommotidyPrice1_A;
-- 零售退货
SET @rt1ReturnCommotidyNO1_A = 5;
SET @rt1ReturnCommotidyPrice1_A = @commodityPrice1_A;
SET @rt1ReturnAmuontCash = 50.000000;
SET @rt1ReturnAmuontWeChat = 0.000000;
SET @rt1ReturnAmuontAliPay = 0.000000;
SET @rt1ReturnTotalAmount = @rt1ReturnCommotidyNO1_A * @rt1ReturnCommotidyPrice1_A;
-- 零售单收银汇总
SET @rggWorkTimeStart1 = '2041/02/02 00:00:00';
SET @rggWorkTimeEnd1 = '2041/02/02 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount - @rt1ReturnTotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash - @rt1ReturnAmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay;
SET @rggNO = 1;
SET @iStaffID = 9;
-- 零售单商品来源表
SET @rtcsNO1 = @rt1CommotidyNO1_A;
SET @rtcsWarehousingID1 = null;
-- 退货去向表
SET @rtcdNO1 = @rt1CommotidyNO1_A;
SET @rtcdWarehousingID1 = null;

-- 创建一个商品A
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
SET @saleDatetime = '2041/02/02 07:00:00';
SET @syncDatetime = '2041/02/02 07:00:00';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontWeChat,@rt1AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();

-- 零售单商品表
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commIDA,'未入库商品A', @barcodeIDA, @rt1CommotidyNO1_A, @rt1CommotidyPrice1_A, @rt1CommotidyNO1_A, @rt1CommotidyPrice1_A, @rt1CommotidyPrice1_A);
SET @rtcID1 = last_insert_id();

-- 零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@rtcID1, @rtcsNO1, @rtcsWarehousingID1, @commIDA);
SET @rtcSourceID1 = last_insert_id();

-- 零售退货单
SET @saleDatetime = '2041/02/02 08:00:00';
SET @syncDatetime = '2041/02/02 08:00:00';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1ReturnTotalAmount,@rt1ReturnAmuontCash,@rt1ReturnAmuontWeChat,@rt1ReturnAmuontAliPay,0,0,0,0,0,1,@rtID1,2);
SET @rtReturnID1 = last_insert_id();

-- 零售退货单商品表
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtReturnID1, @commIDA,'未入库商品A', @barcodeIDA, @rt1ReturnCommotidyNO1_A, @rt1ReturnCommotidyPrice1_A, 0, 0, @rt1ReturnCommotidyPrice1_A);
SET @rtcReturnID1 = last_insert_id();

-- 零售退货单商品去向表
INSERT INTO nbr.t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtcReturnID1, @commIDA, @rtcdNO1, @rtcdWarehousingID1);
SET @rtcDestinationID1 = last_insert_id();

-- 员工A收银汇总
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case65 Testing Result';

-- 售卖总金额
SET @saleTotalAmount = @rt1TotalAmount;
-- 售卖商品总成本
SET @saleTotalCost = IFNULL((@rtcsNO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcsWarehousingID1 AND F_CommodityID = @commIDA)),0);
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - ifnull(@saleTotalCost, 0);
-- 退货总金额
SET @returnTotalAmount = @rt1ReturnTotalAmount;
-- 退货商品总成本
SET @returnTotalCost = IFNULL((@rtcdNO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcdWarehousingID1 AND F_CommodityID = @commIDA)),0);
-- 退货商品总毛利
SET @returnTotalMargin = @returnTotalAmount - ifnull(@returnTotalCost, 0);
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
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1)
	FROM t_retailtrade 
	WHERE F_SourceID = -1
	AND F_StaffID = @iStaffID
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case1 Result';

-- 进行数据删除，避免污染数据库
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcDestinationID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcReturnID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtReturnID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_providercommodity WHERE F_CommodityID = @commIDA;
DELETE FROM t_barcodes WHERE F_CommodityID = @commIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;