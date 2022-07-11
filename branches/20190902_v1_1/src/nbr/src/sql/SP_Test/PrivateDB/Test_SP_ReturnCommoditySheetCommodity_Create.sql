SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheetCommodity_Create.sql ++++++++++++++++++++';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SELECT '-------------------- Case1:正常创建 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iCommodityID = 63;
SET @iBarcodeID = 67;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity 
WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID
AND F_CommodityID = @iCommodityID
AND F_CommodityName IN (SELECT F_Name FROM t_commodity WHERE F_ID = @iCommodityID)
AND F_BarcodeID = @iBarcodeID
AND F_Specification = @sSpecification
AND F_PurchasingPrice = @sPurchasingPrice;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ID = last_insert_id();
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case2:创建重复的退货单商品表到同一退货单中 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = 4;
SET @iCommodityID = 6;
SET @iBarcodeID = 8;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'case2 Testing Result';

SELECT '-------------------- Case3:创建退货单商品到已审核退货单中 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = 2;
SET @iCommodityID = 20;
SET @iBarcodeID = 24;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case3 Testing Result';

SELECT '-------------------- Case4:创建一个不存在的退货单的退货单商品 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = -1;
SET @iCommodityID = 20;
SET @iBarcodeID = 24;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case4 Testing Result';

SELECT '-------------------- Case5.1:创建一个不存在的商品的退货单商品 -------------------------' AS 'Case5.1';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iCommodityID = -1;
SET @iBarcodeID = 10;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case5.1 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case5.2:创建一个预删除（status=1）的商品的退货单商品 -------------------------' AS 'Case5.2';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case5.2 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case5.3:创建一个已删除（status=2）的商品的退货单商品 -------------------------' AS 'Case5.3';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iBarcodeID = 10;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case5.3 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

--	SELECT '-------------------- Case6:创建一个负数数量的退货单商品表 -------------------------' AS 'Case6';
--	
--	INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
--	VALUES (3, 3);	
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iReturnCommoditySheetID = last_insert_id();
--	SET @iCommodityID = 5;
--	SET @iBarcodeID = 7;
--	SET @iNO = -100;
--	SET @sSpecification = '箱';
--	SET @sPurchasingPrice = 10;
--	
--	CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);
--	
--	SELECT @sErrorMsg;
--	SELECT @iErrorCode;
--	
--	SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case6 Testing Result';
--	
--	DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case7:创建一个不存在的条形码的退货单商品表 -------------------------' AS 'Case7';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iBarcodeID = -7;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case7 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case8:创建退货单商品时传负数的采购价 -------------------------' AS 'Case8';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iBarcodeID = 10;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = -10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case8 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case9:创建退货单商品时，添加的商品类型为服务类商品，期望是创建失败 -------------------------' AS 'Case9';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'',3,3,'SP',1,
130,11,1,1,'url=116843434834',
0,'15','2018-04-14','20',0,0,'1111111',3);

SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iBarcodeID = 7;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT IF(@sErrorMsg = '商品类型为组合商品或服务商品,不能加到退货单中' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'case9 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;



SELECT '-------------------- Case10:条形码ID与商品实际ID不对应 -------------------------' AS 'Case1';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);	
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iReturnCommoditySheetID = last_insert_id();
SET @iCommodityID = 63;
SET @iBarcodeID = 1;
SET @iNO = 100;
SET @sSpecification = '箱';
SET @sPurchasingPrice = 10;

CALL SP_ReturnCommoditySheetCommodity_Create (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID, @iBarcodeID, @iNO, @sSpecification, @sPurchasingPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity 
WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID
AND F_CommodityID = @iCommodityID
AND F_CommodityName IN (SELECT F_Name FROM t_commodity WHERE F_ID = @iCommodityID)
AND F_BarcodeID = @iBarcodeID
AND F_Specification = @sSpecification
AND F_PurchasingPrice = @sPurchasingPrice;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '条形码ID与商品实际条形码ID不对应', '测试成功', '测试失败') AS 'case10 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ID = last_insert_id();
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;