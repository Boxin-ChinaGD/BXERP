SELECT '++++++++++++++++++ Test_SP_Shop_Retrieve1.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++CASE1:������ѯ++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_Shop_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_shop WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '++++++++++++++++++CASE2:�ò����ڵ�ID��ѯ++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;

CALL SP_Shop_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_shop WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';