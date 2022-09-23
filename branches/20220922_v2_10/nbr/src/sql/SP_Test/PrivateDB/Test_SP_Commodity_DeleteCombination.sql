SELECT '++++++++++++++++++ Test_SP_Commodity_DeleteCombination.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:���뵥Ʒ -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'������com1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:����Ʒ�������Ʒ ����ɾ��-------------------------' AS 'Case2';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com117��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', '��', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- Case3:���������ƷID�������Ʒ�����м�¼  -------------------------' AS 'Case3';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com123��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',1);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'������com123��', @ibID, 100, 120, 100, 5, 100, NULL);
SET @irtcID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case4:������װ��Id -------------------------' AS 'Case4';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com126��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID=last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Testing Case4 Result';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case5:�ظ�ɾ�������Ʒ -------------------------' AS 'Case5';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'������com117��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '����Ʒ��ɾ���������ظ�ɾ��', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- case6:ɾ�������ڵ���Ʒ -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, -1, 3);
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- �����Ʒ���ܲ������
--	SELECT '-------------------- Case7:��Ʒ�д�������������û�б�ɾ������ô����Ʒ�����Ա�ɾ��------------------------' AS 'Case7';
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @name = '��10-1';
--	SET @status = 0;
--	SET @type = 0;
--	SET @datetimestart = now();
--	SET @datetimeend = now();
--	SET @excecutionthreshold = 10;
--	SET @excecutionamount = 1;
--	SET @excecutiondiscount = 1;
--	SET @scope = 0;
--	SET @staff = 1 ;
--	INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
--	SET @iPromotionID = last_insert_id();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������com117��','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,1,'1111111', 0,1);
--	SET @iID = last_insert_id();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,2,'1111111', 0,0);
--	SET @commID1 = LAST_INSERT_ID();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,2,'1111111', 0,0);
--	SET @commID2 = LAST_INSERT_ID();
--	-- 
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������
--	-- 
--	INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
--	INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
--	
--	-- 
--	CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iID);
--	SET @promotionScopeID = last_insert_id();
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7_1 Testing Result';
--	
--	SET @sErrorMsg = '';
--	SET @iStaffID = 3;
--	-- 
--	CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
--	SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '����Ʒ�д�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case7_2 Testing Result';
--	-- 
--	SELECT 1 FROM t_commodityhistory 
--	WHERE F_StaffID = @iStaffID 
--	AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
--	AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
--	AND F_NewValue = "" 
--	AND F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE7_3 Testing Result';
--	-- 
--	SELECT 1 FROM t_commodityhistory 
--	WHERE F_StaffID = @iStaffID 
--	AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
--	AND F_OldValue LIKE CONCAT('%', '��', '%')
--	AND F_NewValue = "" 
--	AND F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE7_4 Testing Result';
--	-- 
--	SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE7_5 Testing Result';
--	-- 
--	DELETE FROM t_promotionscope WHERE F_ID = @promotionScopeID;
--	DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
--	DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
--	DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
--	DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
--	DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2);


SELECT '-------------------- Case8:��Ʒ�д������������Ǵ����Ѿ���ɾ������Ʒ����������������ô����Ʒ���Ա�ɾ��------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 1;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com117��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);

-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8_1 Testing Result';

SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8_2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE8_3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', '��', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE8_4 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE8_5 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2) ;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2);