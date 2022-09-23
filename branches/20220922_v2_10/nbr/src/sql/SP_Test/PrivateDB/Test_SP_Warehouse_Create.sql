SELECT '++++++++++++++++++ Test_SP_Warehouse_Create.sql ++++++++++++++++++++';

-- case1:��ȷ����Ӳֿ�
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='�ֿ�10';
SET @sAddress = 'ֲ��԰';
SET @iStatus = 0;
SET @iStaffID = 1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result case1';

DELETE FROM t_warehouse WHERE F_ID = LAST_INSERT_ID();

-- case2:�������ͬ�����ֵĲֿ�
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='Ĭ�ϲֿ�'; 
SET @sAddress = 'ֲ��԰';
SET @iStatus = 0;
SET @iStaffID = 1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result case2';

-- case3����ϵ��Ϊ�����ڵ�
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='�ֿ�12'; 
SET @sAddress = 'ֲ��԰';
SET @iStatus = 0;
SET @iStaffID = -1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result case3';

-- case4:�绰����ַΪ��
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='�ֿ�case5'; 
SET @sAddress = NULL;
SET @iStatus = 0;
SET @iStaffID = 3;
SET @sPhone = NULL;

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
DELETE FROM t_warehouse WHERE F_ID = LAST_INSERT_ID();

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5Testing Result case5';

