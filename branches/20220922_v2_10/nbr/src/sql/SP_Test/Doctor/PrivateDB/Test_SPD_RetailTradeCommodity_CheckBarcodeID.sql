SELECT '++++++++++++++++++ Test_SPD_RetailTradeCommodity_CheckBarcodeID.sql ++++++++++++++++++++';

-- ��������
SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTradeCommodity_CheckBarcodeID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 

-- �ò��Դ������Լ��
-- ���۵���Ʒ��BarcodeIDû�ж�Ӧ��Barcode
-- SELECT '-------------------- Case2:���۵���Ʒ��BarcodeIDû�ж�Ӧ��Barcode -------------------------' AS 'Case2';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- ����һ����Ʒ���ݣ��������ɾ��
-- INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
-- F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
-- F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
-- VALUES (0,'�ɱȿ���Ƭaaa','��Ƭ','��',1,'��',3,1,'SP',1,
-- 8,12,11.8,11,1,1,null,
-- 3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0,-1);
-- SET @iCommodityID = last_insert_id();
-- ����һ�����۵����ݣ��������ɾ��
-- INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
-- VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
-- SET @iRetailTradeID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ���ݣ��������ɾ��
-- INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_Discount, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
-- VALUES (@iRetailTradeID, @iCommodityID,'�ɱȿ���Ƭaaa', -1, 20, 222.6, 0.9, 20, 222.6, 200);
-- SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- ����һ�����۵���Ʒ��Դ���ݣ��������ɾ��
-- INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
-- VALUES (@iRetailTradeCommodityID, 20, 24, @iCommodityID);
-- SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
-- CALL SPD_RetailTradeCommodity_CheckBarcodeID(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode;
-- SELECT @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeCommodityID, '�����۵���Ʒ��BarcodeIDû�ж�Ӧ��Barcode') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
-- DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
-- DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 
