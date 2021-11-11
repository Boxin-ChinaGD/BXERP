SELECT '++++++++++++++++++ Test_SP_Barcodes_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -1;
SET @sBarcode = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Barcodes_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID AND F_Barcode = @sBarcode;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:根据商品ID查找 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @sBarcode = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Barcodes_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID AND F_Barcode = @sBarcode;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:根据条形码查找 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -1;
SET @sBarcode = '354';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Barcodes_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID AND F_Barcode = @sBarcode;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';