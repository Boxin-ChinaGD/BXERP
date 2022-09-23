SELECT '++++++++++++++++++ Test_SP_Warehouse_RetrieveN.sql ++++++++++++++++++++';

INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('�ֿ�90909', 'ֲ��԰', 0, 1, '123456');

SET @sName = '�ֿ�';
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehouse_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_warehouse WHERE F_ID = last_insert_id();