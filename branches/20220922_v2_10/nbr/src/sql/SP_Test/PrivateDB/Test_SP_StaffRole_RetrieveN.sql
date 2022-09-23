SELECT '++++++++++++++++++Test_SP_StaffRole_RetrieveN.sql+++++++++++++++++++++++';

-- ��ɫ
SELECT '-----------------Case1: ��ѯ���н�ɫ(��ְ����ְ)------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case2: ��ѯ������ְ�Ľ�ɫ------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;


SELECT '-----------------Case3: ��ѯ������ְ�Ľ�ɫ------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- ����Ա
SELECT '-----------------Case4: ��ѯ����Ա(��ְ����ְ)------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case5: ��ѯ��ְ������Ա------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case6: ��ѯ��ְ������Ա------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- ����
SELECT '-----------------Case7: ��ѯ����(��ְ����ְ)------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case8: ��ѯ��ְ�ľ���------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case9: ��ѯ��ְ�ľ���------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- ���곤
SELECT '-----------------Case10: ��ѯ���곤(��ְ����ְ)------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case11: ��ѯ��ְ�ĸ��곤------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case12: ��ѯ��ְ�ĸ��곤------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- �곤
SELECT '-----------------Case13: ��ѯ�곤(��ְ����ְ)------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case14: ��ѯ��ְ�ĵ곤------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case15: ��ѯ��ְ�ĵ곤------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- ҵ����
SELECT '-----------------Case16: ��ѯҵ����(��ְ����ְ)------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case17: ��ѯ��ְ��ҵ����------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case18: ��ѯ��ְ��ҵ����------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- �����ǰ
SELECT '-----------------Case19_1: �ϰ��ѯ�����ǰ(��ְ����ְ)------------------' AS 'Case19_1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case19_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case19_2: �ϰ��ѯ�����ǰ(��ְ����ְ)------------------' AS 'Case19_2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = -1;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '���Գɹ�', '����ʧ��') AS 'Case19_2 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case20_1: ��ѯ��ְ�Ĳ����ǰ------------------' AS 'Case20_1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case20_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case20_2: ��ѯ��ְ�Ĳ����ǰ------------------' AS 'Case20_2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 0;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '���Գɹ�', '����ʧ��') AS 'Case20_2 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case21_1: ��ѯ��ְ�Ĳ����ǰ------------------' AS 'Case21_1';
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0  AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case21_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

SELECT '-----------------Case21_2: ��ѯ��ְ�Ĳ����ǰ------------------' AS 'Case21_2';
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 1;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0  AND @iTotalRecord = 1, '���Գɹ�', '����ʧ��') AS 'Case21_2 Testing Result';
--
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

-- �����ڵ�ֵ
SELECT '-----------------Case22: ʹ�ò����ڵ�ֵ(����)��ѯ��ɫ------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -9999999999;
SET @iStatus = -9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case23: ʹ�ò����ڵĽ�ɫID(����)��ѯ��ɫ------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -9999999999;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case24: ʹ�ò����ڵ�״̬(����)��ѯ��ɫ------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = -9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;


SELECT '-----------------Case25: ʹ�ò����ڵ�ֵ(����)��ѯ��ɫ------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 9999999999;
SET @iStatus = 9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case26: ʹ�ò����ڵĽ�ɫID(����)��ѯ��ɫ------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 9999999999;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case27: ʹ�ò����ڵ�״̬(����)��ѯ��ɫ------------------' AS 'Case27';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;