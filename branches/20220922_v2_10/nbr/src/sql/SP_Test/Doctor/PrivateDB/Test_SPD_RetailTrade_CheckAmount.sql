SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckAmount.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:�˿�֧������� -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,7,0,
1,'........','2019-5-18 17:45:31',400,200,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵���֧�����͸��ַ�ʽ֧�������ܺͲ����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case3:�������ֽ�֧�������ǽ�Ϊ0 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,2,0,
1,'........','2019-5-18 17:45:31',400,200,200,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ������ֽ�֧����ʽ�������ֽ�֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case4:���ڻ��֧�������ֽ�֧���������ֽ�֧�����Ϊ0 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,3,0,
1,'........','2019-5-18 17:45:31',200,0,200,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ����ֽ�֧����ʽ�������ֽ�֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case5:������֧����֧��������֧����֧����Ϊ0 -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����֧����֧����ʽ������֧����֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case6:����֧����֧��������֧����֧�����Ϊ0 -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,3,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���֧����֧����ʽ������֧����֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case7:������΢��֧��������΢��֧����Ϊ0 -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,2,0,
1,'........','2019-5-18 17:45:31',400,0,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����΢��֧����ʽ������΢��֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case8:����΢��֧��������΢��֧�����Ϊ0 -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���΢��֧����ʽ������΢��֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case9:������Amount1֧��������Amount1֧����Ϊ0 -------------------------' AS 'Case9';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,200,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����Amount1֧����ʽ������Amount1֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case10:����Amount1֧��������Amount1֧�����Ϊ0 -------------------------' AS 'Case10';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,9,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���Amount1֧����ʽ������Amount1֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case11:������Amount2֧��������Amount2֧����Ϊ0 -------------------------' AS 'Case11';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,200,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����Amount2֧����ʽ������Amount2֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case12:����Amount2֧��������Amount2֧�����Ϊ0 -------------------------' AS 'Case12';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,17,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���Amount2֧����ʽ������Amount2֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case13:������Amount3֧��������Amount3֧����Ϊ0 -------------------------' AS 'Case13';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,200,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����Amount3֧����ʽ������Amount3֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case14:����Amount3֧��������Amount3֧�����Ϊ0 -------------------------' AS 'Case14';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,33,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���Amount3֧����ʽ������Amount3֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case15:������Amount4֧��������Amount4֧����Ϊ0 -------------------------' AS 'Case15';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,0,200,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����Amount4֧����ʽ������Amount4֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case16:����Amount4֧��������Amount4֧�����Ϊ0 -------------------------' AS 'Case16';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,65,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���Amount4֧����ʽ������Amount4֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


SELECT '-------------------- Case17:������Amount5֧��������Amount5֧����Ϊ0 -------------------------' AS 'Case17';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,0,0,200,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĳ�����Amount5֧����ʽ������Amount5֧�����ҪΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case18:����Amount5֧��������Amount5֧�����Ϊ0 -------------------------' AS 'Case18';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- ����һ�����۵����ݣ��������ɾ��
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,129,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iRetailTradeID, '�����۵��Ĵ���Amount5֧����ʽ������Amount5֧������Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;