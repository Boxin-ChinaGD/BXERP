SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckPaymentType.sql ++++++++++++++++++++';

-- ��������
SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 

-- ���۵���֧����ʽ���ǹ涨��8��֧����ʽ
SELECT '-------------------- Case2:���۵���֧����ʽ���ǹ涨��8��֧����ʽ -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,-1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���֧����ʽ�����ǹ涨8�ֵ�֧����ʽ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 



SELECT '-------------------- Case3:Դ�����ֽ�֧������ô�˿ʽ����Ϊ΢���˿� -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵����ֽ�֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 1/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 70000, 0, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����΢���˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 4/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ�����ֽ��˿��ΪԴ�����ֽ�֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


-- 
SELECT '-------------------- Case4:Դ�����ֽ�֧������ô�˿ʽ����Ϊ֧�����˿� -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵����ֽ�֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 1/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 70000, 0, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����֧�����˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 2/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ�����ֽ��˿��ΪԴ�����ֽ�֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;



SELECT '-------------------- Case5:Դ����΢��֧������ô�˿ʽ����Ϊ֧�����˿� -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵���΢��֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 4/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����֧�����˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 2/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ����΢�Ż��ֽ��˿��ΪԴ����΢��֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


SELECT '-------------------- Case6:Դ����΢��֧������ô�˿ʽ����Ϊ�ֽ��΢���˿� -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵���΢��֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 4/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����΢�ż��ֽ��˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 5/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ����΢�Ż��ֽ��˿��ΪԴ����΢��֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case7:Դ����΢�ż��ֽ�֧������ô�˿ʽ����Ϊ֧�����˿� -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵����ֽ��΢��֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 5/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����֧�����˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 3/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ����΢�š��ֽ��΢�ż��ֽ��˿��ΪԴ����΢��+�ֽ�֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case8:Դ����΢�ż��ֽ�֧������ô�˿ʽ����Ϊ֧������΢���˿� -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ���۵����ֽ��΢��֧��
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 5/*F_PaymentType*/, '0', 1, '��ʵ��', -1, '11/28/2019 04:15:25 ����', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- �˻�����֧�������ֽ��˿�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 ����', 4, 6/*F_PaymentType*/, '0', 1, '��ʵ��', @iRetailTradeID, '11/28/2019 04:15:25 ����', 70000, 0, 35000, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnRetailTradeID, '���˻������۵����˿ʽֻ����΢�š��ֽ��΢�ż��ֽ��˿��ΪԴ����΢��+�ֽ�֧��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;