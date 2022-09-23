SELECT '++++++++++++++++++ Test_SPD_Promotion_CheckSN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:������Ÿ�ʽ����CX��ͷ-------------------------' AS 'Case2';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CL201907220001', 'ָ����Ʒ1�������20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('�������', @iID, '��ʽ����ȷ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '-------------------- Case3:������ų���С��14λ�ַ�-------------------------' AS 'Case3';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Cx20190722000', 'ָ����Ʒ1�������20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('�������', @iID, '�ĳ��Ȳ���С��14λ�ַ�') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;