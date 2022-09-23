SELECT '++++++++++++++++++ Test_SP_Warehousing_Delete.sql ++++++++++++++++++++';

-- Case1:����ɾ��
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 5, now());
SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Warehousing_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- Case2:��ⵥ����ˣ��޷�ɾ����
SET @iID = 4;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Warehousing_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


-- Case3:��ⵥ������,ɾ��ʧ�ܣ�
SET @iID = 99999999999999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Warehousing_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';