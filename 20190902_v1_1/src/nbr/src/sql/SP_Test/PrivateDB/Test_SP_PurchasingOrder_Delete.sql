SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 状态值为0做删除操作 -------------------------' AS 'Case1';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_ProviderName,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		0,
		1,
		1,
		'默认供应商',
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 状态值为1做删除操作，错误码为7 -------------------------' AS 'Case2';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		1,
		1,
		1,
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iID; 

SELECT '-------------------- Case3: 状态值为2做删除操作，错误码为7 -------------------------' AS 'Case3';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		2,
		1,
		1,
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE3 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iID; 

SELECT '-------------------- Case4: 状态值为3做删除操作，错误码为7 -------------------------' AS 'Case4';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		3,
		1,
		1,
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iID; 

SELECT '-------------------- Case5: 状态值为4做删除操作，错误码为0 -------------------------' AS 'Case5';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		4,
		1,
		1,
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 对不存在的采购订单做删除操作，错误码为7 -------------------------' AS 'Case6';

SET @iID = 0;
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: 采购单已被使用，不能删除 -------------------------' AS 'Case7';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_ProviderName,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		0,
		1,
		1,
		'默认供应商',
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
VALUES (0, 3, 1, 5, now(), @iID);
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE7 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iID;



SELECT '-------------------- Case8: 采购订单被删除后，它的从表也被删除了 -------------------------' AS 'Case8';
-- 
INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_ProviderName,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		0,
		1,
		1,
		'默认供应商',
		now(),
		now(),
		now()
	);
	
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'辣条','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3年','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @iCommodityNO, @iBarcodeID1, @fPriceSuggestion);
-- 
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8_1 Testing Result';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8_2 Testing Result';
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE8_3 Testing Result';