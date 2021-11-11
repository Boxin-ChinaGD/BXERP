SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckRetailTradeCommodity.sql ++++++++++++++++++++';
-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
 
SELECT '-------------------- Case2:正常创建零售单 -------------------------' AS 'Case2'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 

SELECT '-------------------- Case3:零售单没有零售单商品 -------------------------' AS 'Case3'; 
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单没有相应的零售单商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case4:零售单有零售单商品，但没有零售单商品来源 -------------------------' AS 'Case4'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('零售单ID为', @iRetailTradeID, '的零售单商品没有相应的零售单商品来源') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case5:零售单有退货有一张零售退货单 -------------------------' AS 'Case5'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeReturnCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case6:零售单有多张零售退货单 -------------------------' AS 'Case6'; 
SET @noSold = 500;
SET @noReturn1 = 200;
SET @noReturn2 = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn1 - @noReturn2), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn1 * @PriceReturn),0,0,(@noReturn1 * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID1 = LAST_INSERT_ID();
-- 创建零售退货单商品1
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID1,@iCommodityID,'可比克薯片',1,@noReturn1,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID1 = LAST_INSERT_ID();
-- 创建零售退货单2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20003,3,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn2 * @PriceReturn),0,0,(@noReturn2 * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID2 = LAST_INSERT_ID();
-- 创建零售退货单商品2
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID2,@iCommodityID,'可比克薯片',1,@noReturn2,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单有多张零售退货单') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeReturnCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeReturnCommodityID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeReturnID1;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeReturnID2;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case7:零售单部分退货 -------------------------' AS 'Case7';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',@WarehousingID,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case8:零售单部分退货,没有零售单商品 -------------------------' AS 'Case8';
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),1.5,0,0,1.5,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,0,321,500, 300, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单没有相应的零售单商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case9:零售单部分退货,有零售单商品,没有零售单商品来源 -------------------------' AS 'Case9';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',@WarehousingID,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('零售单ID为', @iRetailTradeID, '的零售单商品没有相应的零售单商品来源') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 






SELECT '-------------------- Case10:零售单有退货,退货数量大于零售数量 -------------------------' AS 'Case10';
SET @noSold = 50;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('原零售单ID为', @iRetailTradeID, '的零售退货单的退货数量大于原零售单的零售数量') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case10 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case11:零售单全部退货 -------------------------' AS 'Case11';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case12:零售单全部退货,没有零售单商品 -------------------------' AS 'Case12';
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),1.5,0,0,1.5,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,0,321,0, 300, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单没有相应的零售单商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case12 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case13:零售单全部退货,零售单商品有零售退货单商品去向表 -------------------------' AS 'Case13';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, 1, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单不能有零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case13 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case14:零售退货单有一张原零售单 -------------------------' AS 'Case14';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20111,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20112,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case15:零售退货单没有相应的原零售单 -------------------------' AS 'Case15';
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,100000000);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单没有相应的原零售单') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case15 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case16:零售退货单全部退货 -------------------------' AS 'Case16';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case16 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case17:零售退货单,没有零售退货单商品 -------------------------' AS 'Case17';
SET @noSold = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),1.5,0,0,1.5,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('原零售单ID为', @iRetailTradeID, '的零售退货单的退货数量必须大于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case17 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case18:零售退货单部分退货,有零售单商品来源 -------------------------' AS 'Case18';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建零售退货单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iReturnCommodityID, @noReturn, @WarehousingID, @iCommodityID);
SET @iReturnCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('零售退货单ID为', @iReturnID, '的零售退货单商品不能有零售退货单商品来源') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case18 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iReturnCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case19:零售退货单的退货数量大于原零售单的零售数量 -------------------------' AS 'Case19';
SET @noSold = 500;
SET @noReturn = 600;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('原零售单ID为', @iRetailTradeID, '的零售退货单的退货数量大于原零售单的零售数量') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case19 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case20:零售单没有退货单可退货数量大于售卖数量 -------------------------' AS 'Case20'; 
SET @noSold = 500;
SET @NOCanReturn = 600;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的可退货数量大于零售数量') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case20 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case21:零售单全部退货但可退货数量不为0 -------------------------' AS 'Case21';
SET @noSold = 500;
SET @noReturn = 500;
SET @NOCanReturn = 100;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单已经完全退货，可退货数量必须为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case21 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case22:零售单部分退货但可退货数量不正常 -------------------------' AS 'Case22';
SET @noSold = 500;
SET @noReturn = 400;
SET @NOCanReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单退货后，可退货数量不正常') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case22 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case23:零售单没有退货,零售数量不正常 -------------------------' AS 'Case23'; 
SET @noSold = 0;
SET @noReturn = 400;
SET @NOCanReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的零售数量必须大于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case23 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case24:零售单部分退货后,可退货数量不正常 -------------------------' AS 'Case24'; 
SET @noSold = 200;
SET @NOCanReturn = 300;
SET @PriceReturn = 5.34219;
SET @noReturn = 100;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的可退货数量大于零售数量') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case24 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case25:零售单全部退货后,可退货数量不正常 -------------------------' AS 'Case25'; 
SET @noSold = 200;
SET @NOCanReturn = 300;
SET @noReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的可退货数量大于零售数量') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case25 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case26:零售单没有退货，售卖数量小于0 -------------------------' AS 'Case26'; 
SET @noSold = -1;
SET @PriceReturn = -5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的零售数量必须大于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case26 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;





SELECT '-------------------- Case27:零售单有退货，退货数量小于0 -------------------------' AS 'Case27'; 
SET @noSold = 500;
SET @NOCanReturn = 400;
SET @noReturn = -100;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('原零售单ID为', @iRetailTradeID, '的零售退货单的退货数量必须大于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case27 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case28:零售单没有退货有去向表 -------------------------' AS 'Case28'; 
SET @noSold = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noSold, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单不能有零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case28 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case29:零售单部分退货有去向表-------------------------' AS 'Case29'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noSold, @WarehousingID);
SET @RRTCD_ID1 = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单不能有零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case29 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case30:零售单全部退货有去向表-------------------------' AS 'Case30'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noSold, @WarehousingID);
SET @RRTCD_ID1 = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单不能有零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case30 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 






SELECT '-------------------- Case31:零售退货单部分退货有来源表-------------------------' AS 'Case31'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iReturnCommodityID, @noReturn, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('零售退货单ID为', @iReturnID, '的零售退货单商品不能有零售退货单商品来源') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case31 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 






SELECT '-------------------- Case32:零售退货单全部退货有来源表-------------------------' AS 'Case32'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iReturnCommodityID, @noReturn, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('零售退货单ID为', @iReturnID, '的零售退货单商品不能有零售退货单商品来源') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case32 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case33:零售退货单部分退货有去向表-------------------------' AS 'Case33'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case33 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case33:零售退货单全部退货有去向表-------------------------' AS 'Case33'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case33 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case34:零售退货单部分退货没有去向表-------------------------' AS 'Case34'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单没有相应的零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case34 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case35:零售退货单全部退货没有去向表-------------------------' AS 'Case35'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单没有相应的零售单商品退货去向表') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case35 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case36:零售没有退货,金额不正常-------------------------' AS 'Case36'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case36 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case37:零售单部分退货,金额不正常-------------------------' AS 'Case37'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case37 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case38:零售单全部退货,金额不正常-------------------------' AS 'Case38'; 
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case38 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case39:零售退货单部分退货,金额不正常-------------------------' AS 'Case39'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noReturn * @PriceReturn) + 0.0001;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case39 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case40:零售退货单全部退货,金额不正常-------------------------' AS 'Case40'; 
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noReturn * @PriceReturn) + 0.0001;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case40 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case41:零售单没有退货，金额等于0，这是不允许的-------------------------' AS 'Case41'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case41 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case42:零售单部分退货，金额等于0，这是不允许的-------------------------' AS 'Case42'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case42 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case43:零售单全部退货，金额等于0-------------------------' AS 'Case43';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case43 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case44:零售单没有退货，金额小于0-------------------------' AS 'Case44'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case44 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case45:零售单部分退货，金额小于0-------------------------' AS 'Case45'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case45 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case46:零售单全部退货，金额小于0-------------------------' AS 'Case46';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case46 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case47:零售单部分退货，其从表的总金额等于0-------------------------' AS 'Case47'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @ErrorPrice, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case47 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case48:零售单全部退货，其从表的总金额小于0-------------------------' AS 'Case48';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @ErrorPrice, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单从表的总金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case48 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case49:零售退货单部分退货，金额等于0-------------------------' AS 'Case49'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case49 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case50:零售单全部退货，金额小于0-------------------------' AS 'Case50';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case50 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case51:零售退货单部分退货，金额小于0-------------------------' AS 'Case51'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case51 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case52:零售退货单全部退货，金额小于0-------------------------' AS 'Case52';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case52 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case53:零售单部分退货，其从表的总金额等于0-------------------------' AS 'Case53'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @ErrorPrice, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnID, '的零售退货单的应收价与其所有零售单商品的退货价总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case53 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case54:零售单全部退货，其从表的总金额小于0-------------------------' AS 'Case54';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'可比克薯片','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 创建零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 创建零售单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'可比克薯片',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 创建零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- 创建零售退货单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- 创建零售退货单商品
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'可比克薯片',1,@noReturn,321,0, @ErrorPrice, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 创建去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('原零售单ID为', @iRetailTradeID, '的零售退货单从表的总金额必须大于或等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case54 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 