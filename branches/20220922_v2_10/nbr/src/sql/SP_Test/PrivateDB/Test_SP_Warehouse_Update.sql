SELECT '++++++++++++++++++ Test_SP_Warehouse_Update.sql ++++++++++++++++++++';

INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ( '�ֿ�922', 'ֲ��԰', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='�ֿ�998'; 
SET @sAddress = 'ֲ��԰';
SET @iStaffID = 2;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case2:�޸����е�����
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('�ֿ�9992', 'ֲ��԰', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='Ĭ�ϲֿ�'; 
SET @sAddress = 'ֲ��԰';
SET @iStaffID = 2;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case3���޸ĵ�StaffIDΪ�����ڵ�.
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ( '�ֿ�9991', 'ֲ��԰', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='�ֿ�99922'; 
SET @sAddress = 'ֲ��԰';
SET @iStaffID = -1;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case4:��ַ���绰Ϊ��
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('�ֿ�9995', 'ֲ��԰', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='�ֿ�99963'; 
SET @sAddress = NULL;
SET @iStaffID = 3;
SET @sPhone = NULL;

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case4 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;