SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckSmallSheetID.sql ++++++++++++++++++++';
-- ��������
SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckSmallSheetID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 

-- �ò��Դ���Լ����F_SmallSheetID�ǲ���Ϊ�յ�
-- ���۵���СƱ��ʽIDΪ��
-- SELECT '-------------------- Case2:���۵���СƱ��ʽIDΪ�� -------------------------' AS 'Case2';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- ����һ�����۵����ݣ��������ɾ��
-- INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
-- VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,NULL);
-- SET @iRetailTradeID = LAST_INSERT_ID();
-- 
-- CALL SPD_RetailTrade_CheckSmallSheetID(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode;
-- SELECT @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 

-- ���۵���СƱ��ʽIDС��0
SELECT '-------------------- Case3:���۵���СƱ��ʽIDС��0 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,-1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckSmallSheetID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���SmallSheetID�������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 

-- ���۵���СƱ��ʽID����0
SELECT '-------------------- Case4:���۵���СƱ��ʽID����0 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,0);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckSmallSheetID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���SmallSheetID�������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 