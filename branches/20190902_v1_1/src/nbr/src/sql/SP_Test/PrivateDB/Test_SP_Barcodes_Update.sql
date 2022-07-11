SELECT '++++++++++++++++++ Test_SP_Barcodes_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:修改一个条形码 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 10;
SET @sOldBarcode = '1224599996';
SET @iStaffID = 1;
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @iID = last_insert_id();
SET @sBarcode = '2233322233';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case2:修改一个不存在的条形码 -------------------------' AS 'Case2';
-- 
SET @iCommodityID = 10;
SET @iID = -1;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case3:修改的条形码在零售单商品中有依赖 -------------------------' AS 'Case3';
SET @iID = 1;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case4:传入@iCommodityID不存在，返回7 -------------------------' AS 'Case4';
-- 
SET @iID = 1;
SET @iCommodityID = -10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case5:修改的条形码在采购订单商品中有依赖 -------------------------' AS 'Case5';
SET @iID = 101;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case6:修改的条形码在入库单商品中有依赖 -------------------------' AS 'Case6';
SET @iID = 102;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case7:修改的条形码在盘点单商品中有依赖 -------------------------' AS 'Case7';
SET @iID = 104;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;


SELECT '-------------------- Case7:当存在两个条形码时，如：1234567 12345678 修改条形码12345678为1234567 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 10;
SET @sOldBarcode = '12245999961';
SET @iStaffID = 1;
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @sOldBarcode = '1234567890';
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @iID = last_insert_id();
SET @sBarcode = '12245999961';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_Barcode = @sOldBarcode AND F_CommodityID = @iCommodityID;



SELECT '-------------------- Case8: 修改的条形码在指定促销范围有依赖, 修改失败 -------------------------' AS 'Case8';
--  创建促销  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
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
--  创建商品 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖88','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iCommodityID = last_insert_id();
--  创建条形码
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC16';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
SET @iBarcodeID = last_insert_id();
--  创建促销范围
INSERT INTO t_promotionscope (F_PromotionID, F_CommodityID, F_CommodityName)
VALUES (@iPromotionID, @iCommodityID, '阿尔卑斯棒棒糖88');
SET @iPromotionScopeID = last_insert_id();
-- 
SET @sBarcode = '12245999961';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iBarcodeID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = "修改的条形码在指定促销范围中有依赖", '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 

DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_promotionscope WHERE F_ID = @iPromotionScopeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;