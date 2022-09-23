SELECT '++++++++++++++++++ Test_SPD_Promotion_CheckDatetime.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ʼʱ����ڴ���ʱ��-------------------------' AS 'Case2';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, now(), '2019-12-22 11:32:09', 20, 5, NULL, 1, 4, now(), '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '��ʼʱ����ڴ���ʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case3:��ʼʱ�����(0h~24h)����ʱ��-------------------------' AS 'Case3';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, '2019-07-22 11:32:20', '2019-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '��ʼʱ�����(0h~24h)����ʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case4:����ʱ��С�ڿ�ʼʱ��-------------------------' AS 'Case4';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, now(), '2019-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��С�ڿ�ʼʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;
	
SELECT '-------------------- Case5:����ʱ����ڿ�ʼʱ��-------------------------' AS 'Case5';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, now(), now(), 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ����ڿ�ʼʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case6:����ʱ��С�ڴ���ʱ��-------------------------' AS 'Case6';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, '2019-07-23 11:32:09', '2019-07-24 11:32:00', 20, 5, NULL, 1, 4, '2019-07-25 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��С�ڴ���ʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case7:����ʱ����ڴ���ʱ��-------------------------' AS 'Case7';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, '2018-07-22 11:32:09', now(), 20, 5, NULL, 1, 4, now(), '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ����ڴ���ʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case8:��ʼʱ��С�ڴ���ʱ��-------------------------' AS 'Case8';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0, '2018-07-21 11:32:09', '2019-07-22 11:32:09', 20, 5, NULL, 1, 4, '2018-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('����', @iID, '��ʼʱ��С�ڴ���ʱ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case9:��Ӧ�����۵����۳�ʱ��,�ڿ�ʼʱ��֮ǰ-------------------------' AS 'Case9';


INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount,
 F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS2000010108493800011291', 4, 4, 'url=ashasoadigmnalskd', '2017-08-06', 2, 4, '0', 1, '........', -1, '2019-09-11 09:47:11', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);

SET @TradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradepromoting (F_TradeID, F_CreateDatetime)
VALUES (@TradeID,'2019-09-11 10:59:15');

SET @retailTradePromotingID = LAST_INSERT_ID();

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050055', '��ʼ��ȫ����10-2', 0, 0, '2019-09-12 11:01:26', '2019-09-18 11:01:26', 10, 1, 1, 0, 2, '2019-09-11 11:01:26', '2019-09-11 11:01:26');

SET @promotingID = LAST_INSERT_ID();

INSERT INTO t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (@retailTradePromotingID, @promotingID, '��������', '2019-09-11 11:01:27');
SET @retailtradepromotingflowID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('IDΪ',@TradeID, '�����۵����۳�ʱ��,������IDΪ',@promotingID,'�Ĵ����Ŀ�ʼʱ��ͽ���ʱ��֮��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

DELETE FROM t_retailtradepromotingflow WHERE F_ID = @retailtradepromotingflowID;
DELETE FROM t_retailtradepromoting WHERE F_ID = @retailTradePromotingID;
DELETE FROM t_promotion WHERE F_ID = @promotingID;
DELETE FROM t_retailtrade WHERE F_ID = @TradeID;

SELECT '-------------------- Case10:��Ӧ�����۵����۳�ʱ��,�ڿ�ʼʱ��֮��-------------------------' AS 'Case10';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount,
 F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS2000010108493800011295', 6, 4, 'url=ashasoadigmnalskd', '2025-09-18 11:01:26', 2, 4, '0', 1, '........', -1, '2019-09-11 09:47:11', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);

SET @TradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradepromoting (F_TradeID, F_CreateDatetime)
VALUES (@TradeID,'2019-09-11 10:59:15');

SET @retailTradePromotingID = LAST_INSERT_ID();

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050065', '��ʼ��ȫ����10-2', 0, 0, '2019-09-12 11:01:26', '2019-09-18 11:01:26', 10, 1, 1, 0, 2, '2019-09-11 11:01:26', '2019-09-11 11:01:26');

SET @promotingID = LAST_INSERT_ID();

INSERT INTO t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (@retailTradePromotingID, @promotingID, '��������', '2019-09-11 11:01:27');
SET @retailtradepromotingflowID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckDatetime(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('IDΪ',@TradeID, '�����۵����۳�ʱ��,������IDΪ',@promotingID,'�Ĵ����Ŀ�ʼʱ��ͽ���ʱ��֮��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

DELETE FROM t_retailtradepromotingflow WHERE F_ID = @retailtradepromotingflowID;
DELETE FROM t_retailtradepromoting WHERE F_ID = @retailTradePromotingID;
DELETE FROM t_promotion WHERE F_ID = @promotingID;
DELETE FROM t_retailtrade WHERE F_ID = @TradeID;
