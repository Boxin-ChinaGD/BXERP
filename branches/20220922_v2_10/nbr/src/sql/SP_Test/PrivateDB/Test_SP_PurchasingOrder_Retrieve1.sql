SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ������ѯ -------------------------' AS 'Case1';

INSERT INTO T_PurchasingOrder(
		F_ShopID,
		F_Status, 
		F_StaffID,
		F_ProviderID,
		F_ProviderName,
		F_CreateDatetime, 
		F_ApproveDatetime, 
		F_EndDatetime
	) 
	VALUES(
		2,
		0,
		5,
		1,
		'Ĭ�Ϲ�Ӧ��',
		now(),
		now(),
		now()
	);


SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ��ѯ��ɾ��״̬ -------------------------' AS 'Case2';
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @iID); 

CALL SP_PurchasingOrder_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID AND F_Status = 4;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iID;

SELECT '-------------------- Case3: ��ѯһ�������ڵ�ID -------------------------' AS 'Case3';

SET @iID = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_PurchasingOrder_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';