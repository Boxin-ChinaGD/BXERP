-- ��SP����ϸ����,�ڸ�SP����õ�������SP��SPTest��
SELECT '++++++++++++++++++ Test_SP_RetailTradeCommodity_UploadTrade_CreateCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �������۵� -------------------------' AS 'Case1';

-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 500, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
--	SELECT IF((SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityID) = 480 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result'; -- ����û����ⵥ��Ϣ
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = 480 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result'; -- ����û����ⵥ��Ϣ
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;


SELECT '-------------------- Case2: @iSourceID>0 ���˻����� -------------------------' AS 'Case2';

-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 480, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES ('LS2019090412300300031234',45284,3,'url=ashaso333gmnalskd','2017-08-06',1,'A13356',0,'........',1/*F_SourceID*/,2);
SET @retailTradeID = Last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iID,'�ɱȿ���Ƭ111', 63, 10/*F_NO*/, 1, 10, 10, 10);
SET @retailTradeCommodityID = Last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES ('LS2019090412300300031235',14524 ,2,'url=ashaso333gmnalskd','2017-08-06',1,'A13356',0,'........',@retailTradeID,2);
SET @retailTradeID2 = Last_insert_id();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 23, @iID);
SET @retailtradecommoditysource = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @retailTradeID2;
SET @iCommodityID = @iID;
SET @iBarcodeID = 1;
SET @iNO = 10;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = 490 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID2);
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@retailTradeID,@retailTradeID2);
DELETE FROM t_retailtrade WHERE F_ID IN (@retailTradeID,@retailTradeID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '------- Case3: ������ͨ��ƷA3��,������������3����ͨ��ƷA����Ʒ�޸���ʷ��¼ ---------' AS 'Case3';
-- ������ͨ��ƷA
SET @iStaffID = 4;
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'�ֿ�','�ֿ�','��',1,'��',3,1,'LK',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
SET @iCommodityID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 1, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

-- ����������A
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, '111122223333', now(), now());
SET @iBarcodesID = last_insert_id();
-- ������Ӧ����ƷA
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityID, 1);
-- ������ͨ��ƷA�����۵�1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031237',111,1,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),20,20,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 20;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 20;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID1, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID1 = last_insert_id();
-- �������۵���Ʒ��Դ1
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID1, 1/*F_NO*/, 1, @iCommodityID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_FieldName = '���';
SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
-- ������ͨ��ƷA�����۵�2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031235',222,2,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),20,20,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 20;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 20;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID2, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID2 = last_insert_id();
-- �������۵���Ʒ��Դ2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID2, 1/*F_NO*/, 1, @iCommodityID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_FieldName = '���';
SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = -1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2������';
-- ������ͨ��ƷA�����۵�3
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031238',333,3,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),180,180,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ3
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 9;
SET @fPrice = 180;
SET @iNOCanReturn = 9;
SET @fPriceReturn = 180;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID3, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID3 = last_insert_id();
-- �������۵���Ʒ��Դ3
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID3, 9/*F_NO*/, 1, @iCommodityID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_FieldName = '���';
SELECT IF(@iCount = 3 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = -10 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��3������';

DELETE FROM t_providercommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iTradeCommodityID1,@iTradeCommodityID2,@iTradeCommodityID3) ;
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------- Case4: �������������ƷA,�������������ƷA������Ʒ����������Ʒ�޸���ʷ��¼ ---------' AS 'Case4';
-- ������ͨ��ƷA
SET @iStaffID = 4;
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'�ڴ�','�ڴ�','��',1,'��',3,1,'TD',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
SET @iCommodityAID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityAID, 2, 8, 12, 1, -1, -1, NULL);
SET @iCommShopInfoIDA = last_insert_id();
-- ����������A
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityAID, '111122223333', now(), now());
SET @iBarcodesAID = last_insert_id();
-- ������Ӧ����ƷA
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityAID, 1);
-- ������ͨ��ƷB
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'����','����','��',1,NULL,3,3,'BH',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
SET @iCommodityBID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityBID, 2, 8, 12, 2, -1, -1, NULL);
SET @iCommShopInfoIDB = last_insert_id();
-- ����������B
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityBID, '222211113333', now(), now());
SET @iBarcodesBID = last_insert_id();
-- ������Ӧ����ƷB
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityBID, 1);
-- ���������ƷC
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'�������Ӳ�','�������Ӳ�','ƿ',1,NULL,3,3,'FM',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',1/*F_Type*/);
SET @iCommodityCID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityCID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoIDC = last_insert_id();
-- ����������C
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityCID, '333311112222', now(), now());
SET @iBarcodesCID = last_insert_id();
-- ���������ƷC������Ʒ
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCommodityCID, @iCommodityAID, 2/*F_SubCommodityNO*/, 100);
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCommodityCID, @iCommodityBID, 3/*F_SubCommodityNO*/, 30);
-- ���������ƷC�����۵�1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300041239',444,4,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),130,130,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 130;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 130;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID1, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID1 = last_insert_id();
-- �������۵���Ʒ��Դ1
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID1, 2/*F_NO*/, 1, @iCommodityAID); 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID1, 3/*F_NO*/, 1, @iCommodityBID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) AND F_FieldName = '���';
SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -1 
	AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityBID AND F_ShopID = 2) = -1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
-- ���������ƷC�����۵�2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'SN123483218888',555,5,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),130,130,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 130;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 130;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID2, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID2 = last_insert_id();
-- �������۵���Ʒ��Դ2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID2, 2/*F_NO*/, 1, @iCommodityAID); 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID2, 3/*F_NO*/, 1, @iCommodityBID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) AND F_FieldName = '���';
SELECT IF(@iCount = 4 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -3 
	AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityBID AND F_ShopID = 2) = -4 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2������';
-- ���������ƷC�����۵�3
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300071210',777,7,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),130,130,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ3
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 130;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 130;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID3, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID3 = last_insert_id();
-- �������۵���Ʒ��Դ3
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID3, 2/*F_NO*/, 1, @iCommodityAID); 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID3, 3/*F_NO*/, 1, @iCommodityBID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) AND F_FieldName = '���';
SELECT IF(@iCount = 6 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -5 
	AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityBID AND F_ShopID = 2) = -7 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��3������';
DELETE FROM t_providercommodity WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID,@iCommodityCID);
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iTradeCommodityID1,@iTradeCommodityID2,@iTradeCommodityID3) ;
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID,@iCommodityCID);
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID,@iCommodityCID);
DELETE FROM t_subcommodity WHERE F_CommodityID  = @iCommodityCID;
DELETE FROM t_commodityshopinfo WHERE F_ID IN (@iCommShopInfoIDA, @iCommShopInfoIDB, @iCommShopInfoIDC);
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityBID,@iCommodityAID,@iCommodityCID);



SELECT '------- Case5: �������ζ��װ��ƷB,�������Ƕ��װ��ƷB�Ĳ�����Ʒ����������Ʒ�޸���ʷ��¼ ---------' AS 'Case5';
-- ����������ƷA
SET @iStaffID = 4;
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'����','����','��',1,'��',3,1,'JD',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
SET @iCommodityAID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityAID, 2, 8, 5, 4, -1, -1, NULL);
SET @iCommShopInfoIDA = last_insert_id();
-- ����������A
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityAID, '222233331111', now(), now());
SET @iBarcodesAID = last_insert_id();
-- ������Ӧ����ƷA
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityAID, 1);
-- �������װ��ƷA
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'������','������','��',2,NULL,3,3,'CSD',1,
1,1,1,1,null,
7,30,'2018-04-14','20',@iCommodityAID,5/*F_RefCommodityMultiple*/,'1111111',2/*F_Type*/);
SET @iCommodityBID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityBID, 2, 5, 25, 0, -1, -1, NULL);
SET @iCommShopInfoIDB = last_insert_id();
-- ����������B
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityBID, '111133332222', now(), now());
SET @iBarcodesBID = last_insert_id();
-- ������Ӧ����ƷB
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityBID, 1);
-- �������װ��ƷB�����۵�1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300081210',888,8,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),25,25,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 25;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 25;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID1, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID1 = last_insert_id();
-- �������۵���Ʒ��Դ1
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID1, 5/*F_NO*/, 1, @iCommodityAID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -1  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
-- �������װ��ƷB�����۵�2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031211',999,1,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),25,25,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 25;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 25;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID2, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID2 = last_insert_id();
-- �������۵���Ʒ��Դ2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID2, 5/*F_NO*/, 1, @iCommodityAID);
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -6  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2������';
-- �������װ��ƷB�����۵�3
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2019090412300300031212',1010,2,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),25,25,0,0,0,0,0,0,0,2,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ3
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 25;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 25;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID3, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID3 = last_insert_id();
-- �������۵���Ʒ��Դ3
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID3, 5/*F_NO*/, 1, @iCommodityAID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = 0;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 3 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = -11  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��3������';
DELETE FROM t_providercommodity WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID);
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iTradeCommodityID1,@iTradeCommodityID2,@iTradeCommodityID3) ;
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID);
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityBID,@iCommodityAID);
DELETE FROM t_commodityshopinfo WHERE F_ID IN (@iCommShopInfoIDA, @iCommShopInfoIDB);
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityBID,@iCommodityAID);

SELECT '------- Case6: �������η�����ƷA����������û�����ɷ�����ƷA����Ʒ�޸���ʷ��¼ ---------' AS 'Case6';
-- ����������ƷA
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'˳����','˳����','��',4,NULL,4,4,'SFKD',1,
5.8,5.5,1,1,null,
0,30,'2019-01-14','20',0,0,'˳����',3/*F_Type*/);
SET @iCommodityAID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityAID, 2, 0, 5, 0, -1, -1, NULL);
SET @iCommShopInfoIDA = last_insert_id();
-- ����������A
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityAID, '333311112222', now(), now());
SET @iBarcodesAID = last_insert_id();
-- ����������ƷA�����۵�1
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031215',1111,3,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),5,5,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 1;
SET @fPrice = 5;
SET @iNOCanReturn = 1;
SET @fPriceReturn = 5;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID1, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID1 = last_insert_id();
-- �������۵���Ʒ��Դ1
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID1, 1/*F_NO*/, 1, @iCommodityAID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = -1;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = 0  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
-- ����������ƷA�����۵�2
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2019090412300300031216',1212,4,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),10,10,0,0,0,0,0,0,0,2,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 2;
SET @fPrice = 10;
SET @iNOCanReturn = 2;
SET @fPriceReturn = 10;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID2, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID2 = last_insert_id();
-- �������۵���Ʒ��Դ2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID2, 2/*F_NO*/, 1, @iCommodityAID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = -1;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = 0  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2������';
-- ����������ƷA�����۵�3
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID, F_ShopID)
VALUES (1,'LS2019090412300300031217',1313,5,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
1,'........',now(),15,15,0,0,0,0,0,0,0,2,-1/*F_SourceID*/,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 3;
SET @fPrice = 15;
SET @iNOCanReturn = 3;
SET @fPriceReturn = 15;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID3, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SET @iTradeCommodityID3 = last_insert_id();
-- �������۵���Ʒ��Դ2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
VALUES (@iTradeCommodityID3, 3/*F_NO*/, 1, @iCommodityAID); 
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iCount = -1;
SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityAID AND F_ShopID = 2) = 0  AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��3������';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iTradeCommodityID1,@iTradeCommodityID2,@iTradeCommodityID3) ;
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID3,@iTradeID2,@iTradeID1) ;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityAID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoIDA;
DELETE FROM t_commodity WHERE F_ID = @iCommodityAID;

SELECT '-------------------- Case7: ���۵��а�����ɾ����Ʒ�������������۵���Ʒ -------------------------' AS 'Case7';

SET @iStatus = 2; -- ������ɾ����Ʒ
-- ����״̬Ϊ2�����۵�
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2019-10-22',1,1,0,'........',-1/*F_SourceID*/,@iStatus,now(),128.6,1,2);
-- �õ����۵���ID
SET @iTradeID = LAST_INSERT_ID();
-- ����״̬Ϊ2(��ɾ��)����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2/*F_Status*/,'�ɻ��ڴ�','�ڴ�','��',1,'��',3,1,'TD',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
-- �õ���Ʒ��ID
SET @iCommodityID = last_insert_id();
-- �������ֶε�ֵ
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 4;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID;
SELECT IF((SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID) is NULL AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case8: �˻����а�����ɾ����Ʒ�����������˻����۵���Ʒ -------------------------' AS 'Case8';

SET @iStatus = 2; -- ������ɾ����Ʒ
-- ����״̬Ϊ2�����۵�
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES ('LS9876543211234567899876',123456,3,'url=ashasoadigmnalskd','2019-10-22',1,1,0,'........',-1/*F_SourceID*/,@iStatus,now(),128.6,1,2);
-- �õ����۵���ID
SET @iTradeID = LAST_INSERT_ID();
-- ����״̬Ϊ2(��ɾ��)����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2/*F_Status*/,'�ɻ��ڴ�','�ڴ�','��',1,'��',3,1,'TD',1,
11.8,11,1,1,null,
30,30,now(),'20',0,0,'1111111',0/*F_Type*/);
-- �õ���Ʒ��ID
SET @iCommodityID = last_insert_id();
-- �������ֶε�ֵ
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 4;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID;
SELECT IF((SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID) is NULL AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- ����״̬Ϊ2���˻����۵�
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES ('LS9876543211234567899876_1',12345616,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@iTradeID,@iStatus,now(),128.6,1,2);
-- �õ��˻����۵���ID
SET @iReturnTradeID = LAST_INSERT_ID();
-- �������ֶε�ֵ
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 4;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @iReturnRetailTradeID = 0;
SELECT F_ID INTO @iReturnRetailTradeID FROM t_retailtradecommodity WHERE F_TradeID = @iReturnTradeID;
SELECT IF((SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iReturnTradeID) is NULL AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iReturnTradeID);
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

--	
--	SELECT '------- Case7: ������ͨ��ƷA5������1����6������2����5�� ---------' AS 'Case7';
--	-- ������ͨ��ƷA
--	SET @iStaffID = 4;
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'ƻ��','ƻ��','��',1,'��',3,1,'PG',1,
--	8,5,11.8,11,1,1,null,
--	30,30,now(),'20',0,0,'1111111', 1,0);
--	SET @iCommodityID = last_insert_id();
--	-- ����������A
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityID, 'apple111', now(), now());
--	SET @iBarcodesID = last_insert_id();
--	-- ������Ӧ����ƷA
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@iCommodityID, 1);
--	-- ������ͨ��ƷA�����۵�1
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (1,'LS2019090412300300031218',1414,7,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),25,25,0,0,0,0,0,0,0,2);
--	SET @iTradeID = last_insert_id();
--	-- �������۵���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 25;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 25;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	SET @iTradeCommodityID = last_insert_id();
--	-- �������۵���Ʒ��Դ1
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
--	VALUES (@iTradeCommodityID, 5, 1, @iCommodityID); 
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_FieldName = '���';
--	SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityID) = -4 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
--	-- ���������˻���1
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031219',1515,8,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),30,3,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID1 = last_insert_id();
--	-- ���������˻���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 6;
--	SET @fPrice = 30;
--	SET @iNOCanReturn = 6;
--	SET @fPriceReturn = 30;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID1, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_FieldName = '���';
--	SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityID) = -4 
--		AND @iErrorCode = 7 AND @sErrorMsg = '�˻��������ڿ��˻������������˻�', '���Գɹ�', '����ʧ��') AS '��1���˻�ʧ��';
--	-- ���������˻���2
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031220',1616,1,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),25,25,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID2 = last_insert_id();
--	-- ���������˻���Ʒ2
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 25;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 25;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID2, @iCommodityID, @iBarcodesID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID 
--		AND F_FieldName = '���'; -- ��Ʒ�޸���ʷ��¼������
--		
--	-- ... �����߼��󣬲��Ὣ��Դ������ݽ���ɾ�����Ὣ��Դ������ݸ��ƽ����˻�����Ʒȥ���
--	SELECT count(*) INTO @iSource FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID  
--		AND F_ReducingCommodityID = @iCommodityID; -- ����˻�������������������ɾ����Ӧ�����۵���Ʒ��Դ
--	
--	SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityID) = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2���˻�';
--	
--	
--	SELECT '------- Case8: ���������ƷC5������1����6������2����5�� ---------' AS 'Case8';
--	-- ������ͨ��ƷA
--	SET @iStaffID = 4;
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'����','����','��',1,'��',3,1,'FQ',1,
--	8,5,11.8,11,1,1,null,
--	30,30,now(),'20',0,0,'1111111', 1,0);
--	SET @iCommodityAID = last_insert_id();
--	-- ����������A
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityAID, 'tomato1111', now(), now());
--	SET @iBarcodesAID = last_insert_id();
--	-- ������Ӧ����ƷA
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@iCommodityAID, 1);
--	-- ������ͨ��ƷB
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������','������','��',1,NULL,3,3,'JDM',1,
--	8,10,11.8,11,1,1,null,
--	30,30,now(),'20',0,0,'eggNoodle111', 1,0);
--	SET @iCommodityBID = last_insert_id();
--	-- ����������B
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityBID, '222211113333', now(), now());
--	SET @iBarcodesBID = last_insert_id();
--	-- ������Ӧ����ƷB
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@iCommodityBID, 1);
--	-- ���������ƷC
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'���Ѽ�����','���Ѽ�����','��',1,NULL,3,3,'FQJDM',1,
--	8,15,11.8,11,1,1,null,
--	30,30,now(),'20',0,0,'1111111', 0,1);
--	SET @iCommodityCID = last_insert_id();
--	-- ����������C
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityCID, 'TomatoAndEggNoodle1', now(), now());
--	SET @iBarcodesCID = last_insert_id();
--	-- ���������ƷC������Ʒ
--	INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
--	VALUES (@iCommodityCID, @iCommodityAID, 2, 5);
--	INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
--	VALUES (@iCommodityCID, @iCommodityBID, 3, 10);
--	-- ���������ƷC�����۵�
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (1,'LS2019090412300300031221',1717,8,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),75,75,0,0,0,0,0,0,0,2);
--	SET @iTradeID = last_insert_id();
--	-- �������۵���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 75;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 75;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	SET @iTradeCommodityID = last_insert_id();
--	-- �������۵���Ʒ��Դ1
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
--	VALUES (@iTradeCommodityID, 10, 1, @iCommodityAID); 
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
--	VALUES (@iTradeCommodityID, 15, 1, @iCommodityBID); 
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) AND F_FieldName = '���';
--	SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = -9 
--		AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityBID) = -14 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
--	-- ���������˻���1
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031222',1818,1,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),90,90,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID1 = last_insert_id();
--	-- ���������˻���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 6;
--	SET @fPrice = 90;
--	SET @iNOCanReturn = 6;
--	SET @fPriceReturn = 90;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID1, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) AND F_FieldName = '���';
--	SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = -9 
--		AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityBID) = -14 AND @iErrorCode = 7 AND @sErrorMsg = '�˻��������ڿ��˻������������˻�', '���Գɹ�', '����ʧ��') AS '��1���˻�ʧ��';
--	-- ���������˻���2
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031223',1919,2,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),75,75,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID2 = last_insert_id();
--	-- ���������˻���Ʒ2
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 75;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 75;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID2, @iCommodityCID, @iBarcodesCID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityAID, @iCommodityBID) 
--		AND F_FieldName = '���'; -- ��Ʒ�޸���ʷ��¼������
--	SELECT count(*) INTO @iSource FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID 
--		AND F_ReducingCommodityID = @iCommodityCID; -- ����˻�������������������ɾ����Ӧ�����۵���Ʒ��Դ
--	SELECT IF(@iCount = 4 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = 1 
--		AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityBID) = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2���˻�';
--	
--	
--	
--	
--	SELECT '------- Case9: ���۶��װ��ƷB5������1����6������2����5�� ---------' AS 'Case9';
--	-- ����������ƷA
--	SET @iStaffID = 4;
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'����','����','��',1,'��',3,1,'DF',1,
--	8,5,11.8,11,1,1,null,
--	30,30,now(),'20',0,0,'1111111', 5,0);
--	SET @iCommodityAID = last_insert_id();
--	-- ����������A
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityAID, 'BeanCurd', now(), now());
--	SET @iBarcodesAID = last_insert_id();
--	-- ������Ӧ����ƷA
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@iCommodityAID, 1);
--	-- �������װ��ƷB
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'����','����','��',2,NULL,3,3,'DG',1,
--	5,25,1,1,1,1,null,
--	7,30,'2018-04-14','20',@iCommodityAID,5,'1111111', 0,2);
--	SET @iCommodityBID = last_insert_id();
--	-- ����������B
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityBID, 'DriedTofu', now(), now());
--	SET @iBarcodesBID = last_insert_id();
--	-- ������Ӧ����ƷB
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@iCommodityBID, 1);
--	-- �������װ��ƷB�����۵�
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (1,'LS2019090412300300031224',2020,3,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),125,125,0,0,0,0,0,0,0,2);
--	SET @iTradeID = last_insert_id();
--	-- �������۵���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 125;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 125;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	SET @iTradeCommodityID = last_insert_id();
--	-- �������۵���Ʒ��Դ1
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
--	VALUES (@iTradeCommodityID, 25, 1, @iCommodityAID); 
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg; 
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = -20 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
--	-- ���������˻���1
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031225',2121,4,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),150,150,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID1 = last_insert_id();
--	-- ���������˻���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 6;
--	SET @fPrice = 150;
--	SET @iNOCanReturn = 6;
--	SET @fPriceReturn = 150;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID1, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT IF(@iCount = 1 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = -20 
--		AND @iErrorCode = 7 AND @sErrorMsg = '�˻��������ڿ��˻������������˻�', '���Գɹ�', '����ʧ��') AS '��1���˻�ʧ��';
--	-- ���������˻���2
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031226',2222,5,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),125,125,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID2 = last_insert_id();
--	-- ���������˻���Ʒ2
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 5;
--	SET @fPrice = 125;
--	SET @iNOCanReturn = 5;
--	SET @fPriceReturn = 125;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID2, @iCommodityBID, @iBarcodesBID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT count(*) INTO @iSource FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID 
--		AND F_ReducingCommodityID = @iCommodityAID; -- ����˻�������������������ɾ����Ӧ�����۵���Ʒ��Դ
--	SELECT IF(@iCount = 2 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = 5 
--		AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2���˻�';
--	
--	
--	
--	
--	SELECT '------- Case10: ���۷�����ƷA1������1����2������2����1�� ---------' AS 'Case10';
--	-- ����������ƷA
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'��������','��������','��',4,NULL,4,4,'CNWL',1,
--	0,5,5.8,5.5,1,1,null,
--	0,30,now(),'20',0,0,'��������',0,3);
--	SET @iCommodityAID = last_insert_id();
--	-- ����������A
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iCommodityAID, '444555666', now(), now());
--	SET @iBarcodesAID = last_insert_id();
--	-- ����������ƷA�����۵�
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (1,'LS2019090412300300031227',2323,6,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),5,5,0,0,0,0,0,0,0,2);
--	SET @iTradeID = last_insert_id();
--	-- �������۵���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 1;
--	SET @fPrice = 5;
--	SET @iNOCanReturn = 1;
--	SET @fPriceReturn = 5;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	SET @iTradeCommodityID = last_insert_id();
--	-- �������۵���Ʒ��Դ1
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) 
--	VALUES (@iTradeCommodityID, 1, 1, @iCommodityAID); 
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg; 
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��1������';
--	-- ���������˻���1
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031228',2222,7,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),10,10,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID1 = last_insert_id();
--	-- ���������˻���Ʒ1
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 2;
--	SET @fPrice = 10;
--	SET @iNOCanReturn = 2;
--	SET @fPriceReturn = 10;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID1, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = 0 
--		AND @iErrorCode = 7 AND @sErrorMsg = '�˻��������ڿ��˻������������˻�', '���Գɹ�', '����ʧ��') AS '��1���˻�ʧ��';
--	-- ���������˻���2
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID,F_SourceID)
--	VALUES (1,'LS2019090412300300031229',2323,8,'url=ashasoadigmnalskd',now(),@iStaffID,1,0,
--	1,'........',now(),5,5,0,0,0,0,0,0,0,2,@iTradeID);
--	SET @iReturnTradeID2 = last_insert_id();
--	-- ���������˻���Ʒ2
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iNO = 1;
--	SET @fPrice = 5;
--	SET @iNOCanReturn = 1;
--	SET @fPriceReturn = 5;
--	SET @fPriceSpecialOffer = 0;
--	SET @fPriceVIPOriginal = 0;
--	CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iReturnTradeID2, @iCommodityAID, @iBarcodesAID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--	-- 
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SET @iCount = 0;
--	SELECT count(*) INTO @iCount FROM t_commodityhistory WHERE F_CommodityID = @iCommodityAID AND F_FieldName = '���';
--	SELECT count(*) INTO @iSource FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID 
--		AND F_ReducingCommodityID = @iCommodityAID; -- ����˻�������������������ɾ����Ӧ�����۵���Ʒ��Դ
--	SELECT IF(@iCount = 0 AND (SELECT F_NO FROM t_commodity WHERE F_ID = @iCommodityAID) = 0 
--		AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS '��2���˻�';
SELECT '-------------------- Case9: ������Ʒ������ -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = -1;
SET @iCommodityID = -1;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

SELECT '-------------------- Case10: ������Ʒ�������벻���� -------------------------' AS 'Case10';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID;
SET @iBarcodeID = -1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
DELETE FROM t_commodity WHERE F_ID = @iID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;

SELECT '-------------------- Case11: Ա�������� -------------------------' AS 'Case11';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iCommodityID, 'test111');
SET @iBarcodesID = last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iCommodityID;
SET @iBarcodeID = @iBarcodesID;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = -1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
DELETE FROM t_barcodes WHERE F_ID = @iBarcodesID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;

SELECT '-------------------- Case12: ���۵������� -------------------------' AS 'Case12';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iCommodityID, 'test111');
SET @iBarcodesID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = -1;
SET @iCommodityID = @iCommodityID;
SET @iBarcodeID = @iBarcodesID;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = -1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
DELETE FROM t_barcodes WHERE F_ID = @iBarcodesID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;


SELECT '-------------------- Case13: �������۵�ʱ�����ظ�����Ʒ -------------------------' AS 'Case13';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iCommodityID, 'test111');
SET @iBarcodesID = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @iTradeID = LAST_INSERT_ID();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭ111', @iBarcodesID, 1, '12.5', 1, '12.5', '1', '2');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = @iCommodityID;
SET @iBarcodeID = @iBarcodesID;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 4;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
--
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
--
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodesID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;