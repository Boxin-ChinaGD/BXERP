SELECT '++++++++++++++++++ Test_SP_Warehousing_Retrieve1OrderID.sql ++++++++++++++++++++';

SELECT '------------------- CASE1;PurchasingOrderIDΪ1��ѯ��Ӧ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = 1;

CALL SP_Warehousing_Retrieve1OrderID(@iErrorCode, @sErrorMsg, @iPurchasingOrderID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() > 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';


SELECT '------------------- CASE1;PurchasingOrderIDΪ-1���ѯȫ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;

CALL SP_Warehousing_Retrieve1OrderID(@iErrorCode, @sErrorMsg, @iPurchasingOrderID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() != 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';