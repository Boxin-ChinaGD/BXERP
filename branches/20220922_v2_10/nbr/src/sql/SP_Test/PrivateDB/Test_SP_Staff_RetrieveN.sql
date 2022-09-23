SELECT '++++++++++++++++++ Test_SP_Staff_RetrieveN.sql ++++++++++++++++++++';

SELECT '--------------------- case1:iStatus��0��ʱ��ֻ���Բ鵽��ְ��Ա��-------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT '--------------------- case2:iStatus��0��ʱ����nameֵ��ѯ�����Բ鵽��ְ����ӦԱ�� -------------------------' AS 'Case2';
INSERT INTO t_staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��Ա6��','189154954','4483198412115667','e12asf','7DBCB7F471CB4C224C6B862BA2BE04','2019-01-01','1',5,1,0,now(),now());

SET @iStatus = 0;
SET @iOperator = 0;
SET @sErrorMsg = '';
SET @string3 = '6��';
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';

DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '--------------------- case3:iStatus��-1��ʱ����Բ鵽���е�Ա�� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';

SELECT '--------------------- case4:iStatus��-1��ʱ����nameֵ��ѯ�����Բ鵽���е���ӦԱ�� -------------------------' AS 'Case4';
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��Ա7��','189154954','4483198412115667','e12asf','7DBCB7F471CB4C224C6B862BA2BE04','2019-01-01','1',5,1,0,now(),now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '7��';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '--------------------- case5:iStatus����1��ʱ��ֻ���Բ�ѯ����ְ��Ա�� -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT '--------------------- case6��iStatus��1��ʱ����nameֵ��ѯ�����Բ鵽���е���ְԱ�� -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '4��';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT '--------------------- case7��iStatus��-1��ʱ���ò����ڵ�nameֵ��ѯ -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '9999��';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '--------------------- case8:iStatus��0��ʱ����phoneֵ��ѯ�����Բ鵽��ְ����ӦԱ�� -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '13144496272';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';


SELECT '--------------------- case9:iStatus��2��ʱ����phoneֵ��ѯ�����Բ鵽������ӦԱ�� -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '13144496272';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT '--------------------- case10:�ò����ڵ�phoneֵ��ѯ -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '131111';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case10 Testing Result';

SELECT '--------------------- case11:iStatus��-1��iOperatorΪ0��ʱ�򣬿��Բ鵽����ǰ�˺��������Ա��-------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- Ϊ�˺���������֤�Ա�(û�뵽���õıȽϷ���)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- ������@iTotalRecord���Աȣ���@iTotalRecord��1��
SELECT count(1) INTO @iTotalRecord2 FROM t_staff;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'case11 Testing Result';

SELECT '--------------------- case12:��ǰΪδɾ��ʱ��iStatus��0��iOperatorΪ0��ʱ�򣬿��Բ鵽����ǰ�˺��������Ա��-------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- Ϊ�˺���������֤�Ա�(û�뵽���õıȽϷ���)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- ������@iTotalRecord���Աȣ���@iTotalRecord��1��
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'case12 Testing Result';

SELECT '--------------------- case13:��ǰΪɾ��ʱ��iStatus��1��iOperatorΪ0��ʱ�򣬿��Բ鵽����ǰ���˺ŵ�����Ա��-------------------------' AS 'Case13';
-- 
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- Ϊ�˺���������֤�Ա�(û�뵽���õıȽϷ���)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- ������@iTotalRecord���Աȣ���@iTotalRecord��1��
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'case13 Testing Result';
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

SELECT '--------------------- case14:��ǰΪδɾ��ʱ��iStatus��0��iOperatorΪ1��ʱ�򣬿��Բ鵽����Ա��-------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- Ϊ�˺���������֤�Ա�(û�뵽���õıȽϷ���)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- ������@iTotalRecord���Աȣ���@iTotalRecord��1��
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord, '���Գɹ�', '����ʧ��') AS 'case14 Testing Result';