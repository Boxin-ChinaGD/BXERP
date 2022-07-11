SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case67.sql+++++++++++++++++++++++';

SELECT '----- case67:  ‘±π§A¥¥Ω®“ª∏ˆ¡„ €µ•¬ÙA°¢B…Ã∆∑£¨…Ã∆∑A°¢BŒ™Œ¥»Îø‚…Ã∆∑£¨∂‘A≤ø∑÷ÕÀªı [A] -------' AS 'Case67';

-- …Ã∆∑±Ì
SET @commodityPrice1_A = 10;
SET @commodityPrice1_B = 20;
-- ¡„ €µ•…Ã∆∑±Ì
SET @rt1CommotidyNO1_A = 10;
SET @rt1CommotidyPrice1_A = @commodityPrice1_A;
SET @rt1CommotidyNO1_B = 10;
SET @rt1CommotidyPrice1_B = @commodityPrice1_B;
SET @rt1AmuontCash = 300.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1_A * @rt1CommotidyPrice1_A + @rt1CommotidyNO1_B * @rt1CommotidyPrice1_B;
-- ¡„ €ÕÀªı
SET @rt1ReturnCommotidyNO1_A = 5;
SET @rt1ReturnCommotidyPrice1_A = @commodityPrice1_A;
SET @rt1ReturnAmuontCash = 50.000000;
SET @rt1ReturnAmuontWeChat = 0.000000;
SET @rt1ReturnAmuontAliPay = 0.000000;
SET @rt1ReturnTotalAmount = @rt1ReturnCommotidyNO1_A * @rt1ReturnCommotidyPrice1_A;
-- ¡„ €µ• ’“¯ª„◊‹
SET @rggWorkTimeStart1 = '2041/02/02 00:00:00';
SET @rggWorkTimeEnd1 = '2041/02/02 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount - @rt1ReturnTotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash - @rt1ReturnAmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay;
SET @rggNO = 1;
SET @iStaffID = 9;
-- ¡„ €µ•…Ã∆∑¿¥‘¥±Ì
SET @rtcsNO1 = @rt1CommotidyNO1_A;
SET @rtcsWarehousingID1 = null;
SET @rtcsNO2 = @rt1CommotidyNO1_B;
SET @rtcsWarehousingID2 = null;
-- ÕÀªı»•œÚ±Ì
SET @rtcdNO1 = @rt1CommotidyNO1_A;
SET @rtcdWarehousingID1 = null;


-- ¥¥Ω®“ª∏ˆ…Ã∆∑A
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, 'Œ¥»Îø‚…Ã∆∑A', '∆’Õ®…Ã∆∑A', '∏ˆ', 3, '∞¸', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00'); 
SET @commIDA = last_insert_id();
-- ¥¥Ω®Aµƒ“ª∏ˆÃı–Œ¬Î
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDA = last_insert_id();
-- π©”¶…Ã”Î…Ã∆∑Ω¯––πÿ¡™
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();

-- ¥¥Ω®“ª∏ˆ…Ã∆∑B
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, 'Œ¥»Îø‚…Ã∆∑B', '∆’Õ®…Ã∆∑B', '∏ˆ', 3, '∞¸', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00'); 
SET @commIDB = last_insert_id();
-- ¥¥Ω®Aµƒ“ª∏ˆÃı–Œ¬Î
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDB, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDB = last_insert_id();
-- π©”¶…Ã”Î…Ã∆∑Ω¯––πÿ¡™
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDB, 1);
SET @providerCommIDB = last_insert_id();

-- ¡„ €µ•
SET @saleDatetime = '2041/02/02 07:00:00';
SET @syncDatetime = '2041/02/02 07:00:00';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontWeChat,@rt1AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();

-- ¡„ €µ•…Ã∆∑±Ì
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commIDA,'Œ¥»Îø‚…Ã∆∑A', @barcodeIDA, @rt1CommotidyNO1_A, @rt1CommotidyPrice1_A, @rt1CommotidyNO1_A, @rt1CommotidyPrice1_A, @rt1CommotidyPrice1_A);
SET @rtcID1 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commIDB,'Œ¥»Îø‚…Ã∆∑A', @barcodeIDB, @rt1CommotidyNO1_B, @rt1CommotidyPrice1_B, @rt1CommotidyNO1_B, @rt1CommotidyPrice1_B, @rt1CommotidyPrice1_B);
SET @rtcID2 = last_insert_id();

-- ¡„ €µ•…Ã∆∑¿¥‘¥±Ì
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@rtcID1, @rtcsNO1, @rtcsWarehousingID1, @commIDA);
SET @rtcSourceID1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@rtcID2, @rtcsNO2, @rtcsWarehousingID2, @commIDB);
SET @rtcSourceID2 = last_insert_id();

-- ¡„ €ÕÀªıµ•
SET @saleDatetime = '2041/02/02 08:00:00';
SET @syncDatetime = '2041/02/02 08:00:00';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1ReturnTotalAmount,@rt1ReturnAmuontCash,@rt1ReturnAmuontWeChat,@rt1ReturnAmuontAliPay,0,0,0,0,0,1,@rtID1,2);
SET @rtReturnID1 = last_insert_id();

-- ¡„ €ÕÀªıµ•…Ã∆∑±Ì
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtReturnID1, @commIDA,'Œ¥»Îø‚…Ã∆∑A', @barcodeIDA, @rt1ReturnCommotidyNO1_A, @rt1ReturnCommotidyPrice1_A, 0, 0, @rt1ReturnCommotidyPrice1_A);
SET @rtcReturnID1 = last_insert_id();

-- ¡„ €ÕÀªıµ•…Ã∆∑»•œÚ±Ì
INSERT INTO nbr.t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtcReturnID1, @commIDA, @rtcdNO1, @rtcdWarehousingID1);
SET @rtcDestinationID1 = last_insert_id();

-- ‘±π§A ’“¯ª„◊‹
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1; -- Ωˆ≤‚ ‘ π”√ 1£¨…æ≥˝æ…µƒ ˝æ›°£÷ªƒ‹‘⁄≤‚ ‘¥˙¬Î÷– π”√°£0£¨≤ª…æ≥˝æ…µƒ ˝æ›°£÷ªƒ‹‘⁄π¶ƒ‹¥˙¬Î÷– π”√°
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case64 Testing Result';

--  €¬Ù◊‹Ω∂Ó
SET @saleTotalAmount = @rt1TotalAmount;
--  €¬Ù…Ã∆∑◊‹≥…±æ
SET @saleTotalCost = IFNULL((@rtcsNO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcsWarehousingID1 AND F_CommodityID = @commIDA)),0)
					+ IFNULL((@rtcsNO2 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcsWarehousingID2 AND F_CommodityID = @commIDB)),0);
--  €¬Ù…Ã∆∑◊‹√´¿˚
SET @saleTotalMargin = @saleTotalAmount - ifnull(@saleTotalCost, 0);
-- ÕÀªı◊‹Ω∂Ó
SET @returnTotalAmount = @rt1ReturnTotalAmount;
-- ÕÀªı…Ã∆∑◊‹≥…±æ
SET @returnTotalCost = IFNULL((@rtcdNO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcdWarehousingID1 AND F_CommodityID = @commIDA)),0);
-- ÕÀªı…Ã∆∑◊‹√´¿˚
SET @returnTotalMargin = @returnTotalAmount - ifnull(@returnTotalCost, 0);
-- ◊‹Ω∂Ó£® €¬Ù◊‹Ω∂Ó - ÕÀªı◊‹Ω∂Ó£©
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- ◊‹√´¿˚£® €¬Ù…Ã∆∑◊‹√´¿˚ - ÕÀªı…Ã∆∑◊‹√´¿˚£©
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

-- Ω¯––“µº®±®±Ìµƒ ˝æ›Ω·π˚—È÷§
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
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'≤‚ ‘≥…π¶','≤‚ ‘ ß∞‹') AS 'Test Case1 Result';

-- Ω¯–– ˝æ›…æ≥˝£¨±‹√‚Œ€»æ ˝æ›ø‚
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcDestinationID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcReturnID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID2;
DELETE FROM t_retailtrade WHERE F_ID = @rtReturnID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_providercommodity WHERE F_CommodityID IN (@commIDA,@commIDB);
DELETE FROM t_barcodes WHERE F_CommodityID IN (@commIDA,@commIDB);
DELETE FROM t_commodity WHERE F_ID IN (@commIDA,@commIDB);