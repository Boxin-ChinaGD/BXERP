SELECT '++++++++++++++++++ Test_SPD_PurchasingOrder_CheckStatus.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrder_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:�ɹ�������״̬����0~4֮�� -------------------------' AS 'Case1';
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (5, 2, 1, 'Ĭ�Ϲ�Ӧ��', NULL, '�����л���', '2016/12/6 1:01:01', '2019/5/29 9:20:37', '2017/10/17 1:01:01', '2019/5/29 9:20:37');
SET @iPurchasingorderID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrder_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�����', @iPurchasingorderID, '��״̬����0~4֮��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingorderID;