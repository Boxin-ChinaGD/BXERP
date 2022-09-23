SELECT '++++++++++++++++++ Test_SPD_RetailTradeCommoditySource_CheckNO.sql ++++++++++++++++++++';
-- ��������
SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


-- ���۵���Ʒ�е���Ʒ���Ͳ�����
SELECT '-------------------- Case2:���۵���Ʒ�е���Ʒ���Ͳ����� -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,-1/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'�ɱȿ���Ƭaaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 20/*F_NO*/, 24, @iCommodityID);
SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailtradecommoditysourceID, '�����۵���Ʒ��Դ��Ӧ����Ʒ�����Ͳ���ȷ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 

-- ���۵���Ʒ����ͨ��Ʒ������ͨ��Ʒ�Ŀ�治��ȷ
SELECT '-------------------- Case3:���۵���Ʒ����ͨ��Ʒ������ͨ��Ʒ�Ŀ�治��ȷ -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'�ɱȿ���Ƭaaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 10/*F_NO*/, 24, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID); 
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- ���۵���Ʒ�������Ʒ���������Ʒ�Ŀ�治��ȷ
SELECT '-------------------- Case4:���۵���Ʒ�������Ʒ���������Ʒ�Ŀ�治��ȷ -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��(��ͨ��Ʒ)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ����һ����Ʒ���ݣ��������ɾ��(�����Ʒ)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭbbb','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,1/*F_Type*/);
SET @iCommodityID2 = last_insert_id();
-- ����һ����Ʒ���ݣ��������ɾ��(����Ʒ)
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID2, @iCommodityID, 3/*F_SubCommodityNO*/, 10, now(), now());
SET @iSubCommodityID = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID2,'�ɱȿ���Ƭaaa', 1, 10/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 60/*F_NO*/, 24, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID); 
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- ���۵���Ʒ�Ƕ��װ��Ʒ�������װ��Ʒ�Ŀ�治��ȷ
SELECT '-------------------- Case5:���۵���Ʒ�Ƕ��װ��Ʒ�������װ��Ʒ�Ŀ�治��ȷ -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��(��ͨ��Ʒ)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ����һ����Ʒ���ݣ��������ɾ��(���װ��Ʒ)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭbbb','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',@iCommodityID,3/*F_RefCommodityMultiple*/,'1111111', 0/*F_NO*/,2/*F_Type*/);
SET @iCommodityID2 = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID2,'�ɱȿ���Ƭaaa', 1, 10/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 60/*F_NO*/, 24, @iCommodityID);
-- 
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID);
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- ���۵���Ʒ�Ƿ�����Ʒ����������Ʒ�Ŀ�治��ȷ
SELECT '-------------------- Case6:���۵���Ʒ�Ƿ�����Ʒ����������Ʒ�Ŀ�治��ȷ -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'���AA','kd','��',4,NULL,4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','20',0,0,'���AA',0/*F_NO*/,3/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'���AA', 1, 1/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 2/*F_NO*/, 24, @iCommodityID);
SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailtradecommoditysourceID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case7:(���)���۵���Ʒ����ͨ��Ʒ������ͨ��Ʒ�Ŀ�治��ȷ -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- ������ⵥ1
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ2
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID2 = LAST_INSERT_ID();
-- ������ⵥ��Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- ������ⵥ��Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- �������۵�
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'�ɱȿ���Ƭaaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 10/*F_NO*/, @iWarehousingID, @iCommodityID);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 5/*F_NO*/, @iWarehousingID2, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID);
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;