SELECT '++++++++++++++++++ Test_SP_Warehousing_RetrieveN.sql ++++++++++++++++++++';

SELECT '------------------- CASE1;没有参数查询所有 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '------------------- CASE2:根据ID查询 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '------------------- case3:根据仓库ID查 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '--------------------- case4：根据业务员ID查 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = 1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '----------------------- CASE5:根据采购订单查 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '----------------------- case6: 根据采购订单ID和仓库ID查 -------------------------' AS 'case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = -1;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '----------------------- case7: 根据业务员跟仓库ID查 -------------------------' AS 'case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = 1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '----------------------- case8：根据采购订单跟ID查 -------------------------' AS 'case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 5;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';

SELECT '----------------------- case9:根据采购订单ID，仓库ID，业务员ID查; -------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = 3;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';

SELECT '----------------------- case10:根据所有条件查询 -------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iProviderID = 3;
SET @iwarehouseID = 1;
SET @istaffID = 3;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';

SELECT '------------------- CASE11:根据不存在的ID查询 -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 999;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '------------------- case12:根据不存在的仓库ID查 -------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 999;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT '--------------------- case13：根据不存在的业务员ID查 -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = 999;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';


SELECT '----------------------- CASE14:根据不存在的采购订单查 -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 999;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';

SELECT '----------------------- CASE15:根据供应商ID查 -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case15 Testing Result';

SELECT '----------------------- CASE16:根据供应商ID和采购订单查 -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case16 Testing Result';

SELECT '----------------------- CASE17:根据不存在的供应商ID查 -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 999;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case17 Testing Result';