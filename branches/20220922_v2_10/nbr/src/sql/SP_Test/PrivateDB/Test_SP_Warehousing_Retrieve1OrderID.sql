SELECT '++++++++++++++++++ Test_SP_Warehousing_Retrieve1OrderID.sql ++++++++++++++++++++';

SELECT '------------------- CASE1;PurchasingOrderID为1查询对应的 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = 1;

CALL SP_Warehousing_Retrieve1OrderID(@iErrorCode, @sErrorMsg, @iPurchasingOrderID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() > 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';


SELECT '------------------- CASE1;PurchasingOrderID为-1则查询全部 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;

CALL SP_Warehousing_Retrieve1OrderID(@iErrorCode, @sErrorMsg, @iPurchasingOrderID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() != 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case2 Testing Result';