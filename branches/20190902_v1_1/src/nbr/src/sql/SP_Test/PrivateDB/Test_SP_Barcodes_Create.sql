SELECT '++++++++++++++++++ Test_SP_Barcodes_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 2;
SET @sBarcode = '5565616164';
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID AND F_Barcode = @sBarcode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', 's测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue = @sBarcode
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID AND F_NewValue = @sBarcode;

SELECT '-------------------- Case2:重复添加 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @sBarcode = '3548293894545';
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';	-- ... found_rows() = 1不是SP_Barcodes_Create测试的结果
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue = @sBarcode
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3:用不存在的商品创建条形码 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -999;
SET @sBarcode = '5565616164';
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue = @sBarcode
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4:条形码的长度为1时创建（条形码长度只能是7至64位） -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 2;
SET @sBarcode = '1';
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 8, '测试成功', 's测试失败') AS 'Case4 Testing Result';