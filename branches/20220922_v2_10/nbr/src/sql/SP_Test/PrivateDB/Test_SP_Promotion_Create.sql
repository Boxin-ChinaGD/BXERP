SELECT '+++++++++++++++++++++++++++++++ Test_SP_Promotion_Create +++++++++++++++++++++++++++++++++';

SELECT '------------------------------- CASE1:��Ӵ���� ֻ����������� ---------------------------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'ȫ����10��3';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = -1;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();	
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE2:��Ӵ���� ֻ���������ۿ� ---------------------------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'ȫ����10��7��';
SET @status = 0;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = -1;
SET @excecutiondiscount = 7;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE3:����������Ĵ���� �����������������ۿ� ---------------------------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'ȫ����10��3';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 7;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();	
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE4:������۽������ �����������������ۿ� ----------------------------------------' AS 'CASE4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'ȫ����10��6��';
SET @status = 0;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 6;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;