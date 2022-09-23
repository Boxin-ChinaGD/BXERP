SELECT '++++++++++++++++++ Test_SP_Barcodes_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:����ɾ�� -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBarcode = '124599996';
SET @iStaffID = 3;
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sBarcode);
-- 
SET @iID = last_insert_id();
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2:ɾ���������������۵���Ʒ�������� -------------------------' AS 'Case2';
-- 
SET @sErrorMsg = '';
SET @iID = 1;
SET @iStaffID = 3;
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT '-------------------- Case3:ɾ�����������ڲɹ�������Ʒ�������� -------------------------' AS 'Case2';
-- 
SET @sErrorMsg = '';
SET @iID = 101;
SET @iStaffID = 3;
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT '-------------------- Case4:ɾ��������������ⵥ��Ʒ�������� -------------------------' AS 'Case2';
-- 
SET @sErrorMsg = '';
SET @iID = 102;
SET @iStaffID = 3;
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT '-------------------- Case5:ɾ�������������̵㵥��Ʒ�������� -------------------------' AS 'Case2';
-- 
SET @sErrorMsg = '';
SET @iID = 104;
SET @iStaffID = 3;
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT '-------------------- Case6:ɾ���������벻���� -------------------------' AS 'Case3';
-- 
SET @sErrorMsg = '';
SET @iID = -1;
SET @iStaffID = 3;
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '--------------- Case7:�����Ʒ�ڲɹ�������Ʒ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID1, @fPriceSuggestion);
-- 
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE7_1 Testing Result';
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�����������ڲɹ�������Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case7_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�����������ڲɹ�������Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case7_3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '--------------- Case8:�����Ʒ�������Ʒ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case8';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 3;
SET @iNO = 300;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;
-- 
CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID1, @iPrice, @iAmount, @iShelfLife);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case8_1 Testing Result';
SET @warehousingcommodityID = last_insert_id();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ��������������ⵥ��Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case8_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ��������������ⵥ��Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case8_3 Testing Result';
-- 
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingcommodityID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case9:�����Ʒ���̵㵥��Ʒ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case9';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 5;
SET @iNOReal = 20;
-- 
CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID1);
-- 
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9_1 Testing Result';
SET @inventorycommodityID = last_insert_id();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�������������̵㵥��Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case9_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�������������̵㵥��Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case9_3 Testing Result';
-- 
DELETE FROM t_inventorycommodity WHERE F_ID = @inventorycommodityID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '--------------- Case10:�����Ʒ���˻�����Ʒ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case10';
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,1);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 100;
SET @sSpecification = '��';
SET @sPurchasingPrice = 10;
-- 
CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID1, @iNO, @sSpecification, @sPurchasingPrice);
-- 
SELECT 1 FROM t_returncommoditysheetcommodity 
WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID
AND F_CommodityID = @iCommodityID
AND F_CommodityName IN (SELECT F_Name FROM t_commodity WHERE F_ID = @iCommodityID)
AND F_BarcodeID = @iBarcodeID1
AND F_Specification = @sSpecification
AND F_PurchasingPrice = @sPurchasingPrice;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case10_1 Testing Result';
SET @returnCommoditySheetCommodityID = last_insert_id();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�������������˻�����Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case10_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ�������������˻�����Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case10_3 Testing Result';
-- 
DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ID = @returnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case11:�����Ʒ�����۵���Ʒ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case11';
-- 
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123458,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1, 2);
SET @iTradeID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID1, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF(found_rows() = 1 AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = -20 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11_1 Testing Result'; -- ����û����ⵥ��Ϣ
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ������������������Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case11_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ������������������Ʒ��������', '���Գɹ�', '����ʧ��') AS 'Case11_3 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID2;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case12:�����Ʒ��ָ��������Χ������������ô���������붼�ǲ��ܹ�ɾ���� ------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
-- SET @datetimestart = '2017/7/2 11:23:05';
-- SET @datetimeend = '2018/7/2 11:23:05';
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12_1 Testing Result';

CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case12_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case12_3 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case13:�����Ʒ��ָ��������Χ�������������Ǵ����Ѿ������ˣ���Ʒ��û��������������ô���������뻹�ǲ��ܹ�ɾ���� ------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = '2017/7/2 11:23:05';
SET @datetimeend = '2018/7/2 11:23:05';
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13_1 Testing Result';

CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case13_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case13_3 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case14:�����Ʒ��ָ��������Χ�������������Ǵ����Ѿ�ɾ���ˣ���Ʒ��û��������������ô���������뻹�ǲ��ܹ�ɾ���� ------------' AS 'Case14';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14_1 Testing Result';
-- ɾ������
UPDATE t_promotion SET F_Status = 1 WHERE F_ID = @iPromotionID;

CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case14_2 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ɾ������������ָ��������Χ��������', '���Գɹ�', '����ʧ��') AS 'Case14_3 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '--------------- Case15:�����Ʒ������ȫ����������Ʒ��û��������������ô�������������ܹ�ɾ���� ------------' AS 'Case15';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case15_1 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case15_2 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '--------------- Case16:�����Ʒ������ȫ�������������Ѿ���ɾ���ˣ���Ʒ��û��������������ô�������������ܹ�ɾ���� ------------' AS 'Case16';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
UPDATE t_promotion SET F_Status = 1 WHERE F_ID = @iPromotionID;
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case16_1 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case16_2 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '--------------- Case17:�����Ʒ�ڲɹ�������Ʒ��������������ɹ�������ɾ���ˣ���Ʒ��������������ô�����������ǿ���ɾ����һ���� ------------' AS 'Case17';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID1, @fPriceSuggestion);
-- 
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE17_1 Testing Result';
-- ɾ���ɹ�����
CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iPurchasingOrderID);
-- 
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE17_2 Testing Result';
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID1, @iStaffID);
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case17_3 Testing Result';
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '923456789');
SET @iBarcodeID2 = last_insert_ID();
-- 
CALL SP_Barcodes_Delete(@iErrorCode, @sErrorMsg, @iBarcodeID2, @iStaffID);
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case7_4 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;