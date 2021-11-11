SELECT '++++++++++++++++++ Test_SP_Inventory_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 当状态为0时 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 当状态为1时 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 当状态为2时 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: 当状态为-1时 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: 根据门店ID进行查询 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 根据门店ID和状态进行查询 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';