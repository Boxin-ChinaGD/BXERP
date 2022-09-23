SELECT '++++++++++++++++++ Test_SPD_Vip_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ �������� --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Vip_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT '------------------ �����ڵ�״̬ --------------------' AS 'CASE2';
-- 
INSERT INTO T_VIP (F_ICID,F_Wechat,F_QQ,F_Name,F_EmailF_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_ExpireDatetime,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('320803199807016088','tomcat556','112117891','�쳣','yc666@bx.vip',7,100.8,'����',5,999999999,
'2017-12-06','2020-01-06 12:30:29',35,'2017-02-9 23:15:10','13545678233');
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Vip_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ļ�Ա��״̬�ǷǷ���ֵ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_vip WHERE F_ID = @iID;
-- 