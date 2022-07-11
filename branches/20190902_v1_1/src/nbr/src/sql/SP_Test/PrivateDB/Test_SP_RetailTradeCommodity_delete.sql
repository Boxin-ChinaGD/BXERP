SELECT '++++++++++++++++++ Test_SP_RetailTradeCommodity_delete.sql ++++++++++++++++++++';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'–Ï∏£º«≈£‘˛Ã«AAA','–Ï∏£º«','øÀ',1,'œ‰',9,2,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'1111111',0);
SET @commodityID1 = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'œ≤÷Æ¿…≈£‘˛Ã«AAA','œ≤÷Æ¿…','øÀ',1,'œ‰',4,4,'SP',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'1111111',0);
SET @commodityID2 = last_insert_id();

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'2019-1-15', 2,6,'url=ashasoadigmnalskd','2019-1-15 17:45:31',1,1,0,1,'........','2019-1-15 17:45:31',9600,1.1,0,0,0,0,0,0,0,1,2);
SET @iTradeID = last_insert_id();

INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID,F_CommodityName, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iTradeID,@commodityID1,'–Ï∏£º«≈£‘˛Ã«AAA',200,254,200, 200, 200);
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID,F_CommodityName, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iTradeID,@commodityID2,'œ≤÷Æ¿…≈£‘˛Ã«AAA',200,254,200, 200, 200);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_RetailTradeCommodity_delete(@iErrorCode, @sErrorMsg, @iTradeID);

SELECT @sErrorMsg;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;

SELECT 1 FROM t_retailtrade WHERE F_ID = @iTradeID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;