SELECT '++++++++++++++++++ Test_SPD_Promotion_CheckScope.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';




SELECT '-------------------- Case2:������Χֻ������Ч״̬0������ɾ��״̬1-------------------------' AS 'Case2';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 2, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('������Χ', @iID, 'ֻ����ȫ����Ʒ0,��ָ����Ʒ1') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;