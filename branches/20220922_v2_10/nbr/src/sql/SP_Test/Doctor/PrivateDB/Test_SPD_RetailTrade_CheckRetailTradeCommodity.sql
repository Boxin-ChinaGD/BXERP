SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckRetailTradeCommodity.sql ++++++++++++++++++++';
-- ��������
SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
 
SELECT '-------------------- Case2:�����������۵� -------------------------' AS 'Case2'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 

SELECT '-------------------- Case3:���۵�û�����۵���Ʒ -------------------------' AS 'Case3'; 
-- �������۵�
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵�û����Ӧ�����۵���Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case4:���۵������۵���Ʒ����û�����۵���Ʒ��Դ -------------------------' AS 'Case4'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('���۵�IDΪ', @iRetailTradeID, '�����۵���Ʒû����Ӧ�����۵���Ʒ��Դ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case5:���۵����˻���һ�������˻��� -------------------------' AS 'Case5'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeReturnCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case6:���۵��ж��������˻��� -------------------------' AS 'Case6'; 
SET @noSold = 500;
SET @noReturn1 = 200;
SET @noReturn2 = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn1 - @noReturn2), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn1 * @PriceReturn),0,0,(@noReturn1 * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID1 = LAST_INSERT_ID();
-- ���������˻�����Ʒ1
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID1,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn1,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID1 = LAST_INSERT_ID();
-- ���������˻���2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20003,3,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn2 * @PriceReturn),0,0,(@noReturn2 * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iRetailTradeReturnID2 = LAST_INSERT_ID();
-- ���������˻�����Ʒ2
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeReturnID2,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn2,321,0, @PriceReturn, 300);
SET @iRetailTradeReturnCommodityID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��ж��������˻���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
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


SELECT '-------------------- Case7:���۵������˻� -------------------------' AS 'Case7';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',@WarehousingID,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case8:���۵������˻�,û�����۵���Ʒ -------------------------' AS 'Case8';
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),1.5,0,0,1.5,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,0,321,500, 300, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵�û����Ӧ�����۵���Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case9:���۵������˻�,�����۵���Ʒ,û�����۵���Ʒ��Դ -------------------------' AS 'Case9';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',@WarehousingID,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('���۵�IDΪ', @iRetailTradeID, '�����۵���Ʒû����Ӧ�����۵���Ʒ��Դ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 






SELECT '-------------------- Case10:���۵����˻�,�˻����������������� -------------------------' AS 'Case10';
SET @noSold = 50;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ԭ���۵�IDΪ', @iRetailTradeID, '�������˻������˻���������ԭ���۵�����������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case11:���۵�ȫ���˻� -------------------------' AS 'Case11';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case12:���۵�ȫ���˻�,û�����۵���Ʒ -------------------------' AS 'Case12';
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),1.5,0,0,1.5,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,0,321,0, 300, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵�û����Ӧ�����۵���Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case13:���۵�ȫ���˻�,���۵���Ʒ�������˻�����Ʒȥ��� -------------------------' AS 'Case13';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, 1, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ����ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵����������۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case14:�����˻�����һ��ԭ���۵� -------------------------' AS 'Case14';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20111,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20112,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case15:�����˻���û����Ӧ��ԭ���۵� -------------------------' AS 'Case15';
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,100000000);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻���û����Ӧ��ԭ���۵�') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case16:�����˻���ȫ���˻� -------------------------' AS 'Case16';
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case17:�����˻���,û�������˻�����Ʒ -------------------------' AS 'Case17';
SET @noSold = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
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
SELECT IF(@sErrorMsg = CONCAT('ԭ���۵�IDΪ', @iRetailTradeID, '�������˻������˻������������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case18:�����˻��������˻�,�����۵���Ʒ��Դ -------------------------' AS 'Case18';
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ���������˻�����Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('�����˻���IDΪ', @iReturnID, '�������˻�����Ʒ�����������˻�����Ʒ��Դ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iReturnCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case19:�����˻������˻���������ԭ���۵����������� -------------------------' AS 'Case19';
SET @noSold = 500;
SET @noReturn = 600;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ԭ���۵�IDΪ', @iRetailTradeID, '�������˻������˻���������ԭ���۵�����������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case19 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case20:���۵�û���˻������˻����������������� -------------------------' AS 'Case20'; 
SET @noSold = 500;
SET @NOCanReturn = 600;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ŀ��˻�����������������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case20 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case21:���۵�ȫ���˻������˻�������Ϊ0 -------------------------' AS 'Case21';
SET @noSold = 500;
SET @noReturn = 500;
SET @NOCanReturn = 100;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ѿ���ȫ�˻������˻���������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case21 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case22:���۵������˻������˻����������� -------------------------' AS 'Case22';
SET @noSold = 500;
SET @noReturn = 400;
SET @NOCanReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��˻��󣬿��˻�����������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case22 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case23:���۵�û���˻�,�������������� -------------------------' AS 'Case23'; 
SET @noSold = 0;
SET @noReturn = 400;
SET @NOCanReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),0.000001,0,0,0.000001,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵������������������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case23 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case24:���۵������˻���,���˻����������� -------------------------' AS 'Case24'; 
SET @noSold = 200;
SET @NOCanReturn = 300;
SET @PriceReturn = 5.34219;
SET @noReturn = 100;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ŀ��˻�����������������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case24 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case25:���۵�ȫ���˻���,���˻����������� -------------------------' AS 'Case25'; 
SET @noSold = 200;
SET @NOCanReturn = 300;
SET @noReturn = 200;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ŀ��˻�����������������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case25 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case26:���۵�û���˻�����������С��0 -------------------------' AS 'Case26'; 
SET @noSold = -1;
SET @PriceReturn = -5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵������������������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case26 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;





SELECT '-------------------- Case27:���۵����˻����˻�����С��0 -------------------------' AS 'Case27'; 
SET @noSold = 500;
SET @NOCanReturn = 400;
SET @noReturn = -100;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@NOCanReturn, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('ԭ���۵�IDΪ', @iRetailTradeID, '�������˻������˻������������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case27 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case28:���۵�û���˻���ȥ��� -------------------------' AS 'Case28'; 
SET @noSold = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵����������۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case28 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case29:���۵������˻���ȥ���-------------------------' AS 'Case29'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ����ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noSold, @WarehousingID);
SET @RRTCD_ID1 = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵����������۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case29 Testing Result';
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




SELECT '-------------------- Case30:���۵�ȫ���˻���ȥ���-------------------------' AS 'Case30'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ����ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailTradeCommodityID, @iCommodityID, @noSold, @WarehousingID);
SET @RRTCD_ID1 = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵����������۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case30 Testing Result';
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






SELECT '-------------------- Case31:�����˻��������˻�����Դ��-------------------------' AS 'Case31'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('�����˻���IDΪ', @iReturnID, '�������˻�����Ʒ�����������˻�����Ʒ��Դ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case31 Testing Result';
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






SELECT '-------------------- Case32:�����˻���ȫ���˻�����Դ��-------------------------' AS 'Case32'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnCommodityID, @iCommodityID, @noReturn, @WarehousingID);
SET @RRTCD_ID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('�����˻���IDΪ', @iReturnID, '�������˻�����Ʒ�����������˻�����Ʒ��Դ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case32 Testing Result';
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





SELECT '-------------------- Case33:�����˻��������˻���ȥ���-------------------------' AS 'Case33'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case33 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case33:�����˻���ȫ���˻���ȥ���-------------------------' AS 'Case33'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case33 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case34:�����˻��������˻�û��ȥ���-------------------------' AS 'Case34'; 
SET @noSold = 500;
SET @noReturn = 400;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻���û����Ӧ�����۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case34 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case35:�����˻���ȫ���˻�û��ȥ���-------------------------' AS 'Case35'; 
SET @noSold = 500;
SET @noReturn = 500;
SET @PriceReturn = 5.34219;
SET @WarehousingID = 1;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckRetailTradeCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻���û����Ӧ�����۵���Ʒ�˻�ȥ���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case35 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case36:����û���˻�,������-------------------------' AS 'Case36'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case36 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case37:���۵������˻�,������-------------------------' AS 'Case37'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case37 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case38:���۵�ȫ���˻�,������-------------------------' AS 'Case38'; 
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noSold * @PriceReturn) + 0.0001;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case38 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 



SELECT '-------------------- Case39:�����˻��������˻�,������-------------------------' AS 'Case39'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noReturn * @PriceReturn) + 0.0001;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻�����Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case39 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case40:�����˻���ȫ���˻�,������-------------------------' AS 'Case40'; 
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = (@noReturn * @PriceReturn) + 0.0001;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻�����Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case40 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case41:���۵�û���˻���������0�����ǲ������-------------------------' AS 'Case41'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case41 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case42:���۵������˻���������0�����ǲ������-------------------------' AS 'Case42'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case42 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case43:���۵�ȫ���˻���������0-------------------------' AS 'Case43';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),@ErrorPrice,0,0,@ErrorPrice,0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case43 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case44:���۵�û���˻������С��0-------------------------' AS 'Case44'; 
SET @noSold = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,@noSold, @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case44 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case45:���۵������˻������С��0-------------------------' AS 'Case45'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case45 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case46:���۵�ȫ���˻������С��0-------------------------' AS 'Case46';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @ErrorPrice),0,0,(@noSold * @ErrorPrice),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case46 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case47:���۵������˻�����ӱ���ܽ�����0-------------------------' AS 'Case47'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @ErrorPrice, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case47 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case48:���۵�ȫ���˻�����ӱ���ܽ��С��0-------------------------' AS 'Case48';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @ErrorPrice, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��ӱ���ܽ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case48 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 




SELECT '-------------------- Case49:�����˻��������˻���������0-------------------------' AS 'Case49'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻�����Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case49 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case50:���۵�ȫ���˻������С��0-------------------------' AS 'Case50';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻����Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case50 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case51:�����˻��������˻������С��0-------------------------' AS 'Case51'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻����Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case51 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case52:�����˻���ȫ���˻������С��0-------------------------' AS 'Case52';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @ErrorPrice),0,0,(@noReturn * @ErrorPrice),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @PriceReturn, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻����Ľ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case52 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 





SELECT '-------------------- Case53:���۵������˻�����ӱ���ܽ�����0-------------------------' AS 'Case53'; 
SET @noSold = 33;
SET @noReturn = 11;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = 0;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @ErrorPrice, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnID, '�������˻�����Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case53 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 







SELECT '-------------------- Case54:���۵�ȫ���˻�����ӱ���ܽ��С��0-------------------------' AS 'Case54';
SET @noSold = 33;
SET @noReturn = 33;
SET @WarehousingID = 1;
SET @PriceReturn = 5.34219;
SET @ErrorPrice = -5.34219;
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ɱȿ���Ƭ','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 500,0);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234',20001,1,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noSold * @PriceReturn),0,0,(@noSold * @PriceReturn),0,0,0,0,0,2);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID,@iCommodityID,'�ɱȿ���Ƭ',1,@noSold,321,(@noSold - @noReturn), @PriceReturn, 300);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iRetailTradeCommodityID, @noSold, @WarehousingID, @iCommodityID);
SET @iRetailTradeCommoditySourceID = LAST_INSERT_ID();
-- ���������˻���
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
VALUES (1,'LS2019090412300100011234',20002,2,'url=ashasoadigmnalskd',now(),2,4,0,
1,'........',now(),(@noReturn * @PriceReturn),0,0,(@noReturn * @PriceReturn),0,0,0,0,0,2,@iRetailTradeID);
SET @iReturnID = LAST_INSERT_ID();
-- ���������˻�����Ʒ
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iReturnID,@iCommodityID,'�ɱȿ���Ƭ',1,@noReturn,321,0, @ErrorPrice, 300);
SET @iReturnCommodityID = LAST_INSERT_ID();
-- ����ȥ���
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
SELECT IF(@sErrorMsg = CONCAT('ԭ���۵�IDΪ', @iRetailTradeID, '�������˻����ӱ���ܽ�������ڻ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case54 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @RRTCD_ID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iReturnCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 