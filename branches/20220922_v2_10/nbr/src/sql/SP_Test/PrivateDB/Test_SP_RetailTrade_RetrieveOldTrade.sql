SELECT '++++++++++++++++++ Test_SP_RetailTrade_RetrieveOldTrade.sql ++++++++++++++++++++';

-- INSERT INTO T_RetailTrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
-- F_Status, F_Remark, F_VipID)
-- VALUES ('SN123483222', 11,2,'url=ashasouuuuunalskd','2017-8-10',5,2,'A123460',1,'˫��777', 5);
-- SET @iID = last_insert_id();

SELECT '-------------------- Case1: web����С���ȵ����۵���������ѯ���۵�(����10λ) -------------------------' AS 'Case1';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS12348321218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'LS12348321';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case2: web����POS_SN �� POS_ID����ѯ���۵� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 1;
SET @iPOS_ID = 1;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: web����POS_ID����ѯ���۵� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 2;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: web�˲�ѯ�������۵� -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- Case5: web����ʱ��@dStartDate�������۵� -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '2017/8/6';
SET @dEndDate = now();
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-------------------- Case6: web����iLocalSN����ѯ���۵� -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 1;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

SELECT '-------------------- Case7: web����@iVipID����ѯ���۵� -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 5;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '-------------------- Case8: web����@iPaymentType����ѯ���۵� -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

SELECT '-------------------- Case9: web����@iStaffID����ѯ���۵� -------------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 5;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @iID;

-- ����ʵ��
--	SELECT '-------------------- Case10: web����С����С���ȵ����۵���������ѯ���۵���С��10λ��,�����ǲ�ѯ�������� -------------------------' AS 'Case10';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iVipID = 0;
--	SET @bRequestFromApp = 0;
--	SET @queryKeyword = 'LS1234832';
--	SET @iLocalSN = 0;
--	SET @iPOS_ID = 0;
--	SET @dStartDate = '';
--	SET @dEndDate = '';
--	SET @iPaymentType = 0;
--	SET @iPageIndex = 1;
--	SET @iPageSize = 10;
--	SET @iTotalRecord = 0;
--	SET @iStaffID = 0;
--	SET @iExcludeReturned = 0;
--	
--	CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
--	@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);
--	
--	SELECT @iErrorCode, @sErrorMsg;
--	
--	SELECT IF(@sErrorMsg = '�������ݸ�ʽ����' AND @iTotalRecord = 0 AND @iErrorCode = 8, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

--	SELECT '-------------------- Case11: web���ô�����󳤶ȵ����۵���������ѯ���۵�������26λ��,�����ǲ�ѯ�������� -------------------------' AS 'Case11';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iVipID = 0;
--	SET @bRequestFromApp = 0;
--	SET @queryKeyword = 'LS123483218000011111245456465';
--	SET @iLocalSN = 0;
--	SET @iPOS_ID = 0;
--	SET @dStartDate = '';
--	SET @dEndDate = '';
--	SET @iPaymentType = 0;
--	SET @iPageIndex = 1;
--	SET @iPageSize = 10;
--	SET @iTotalRecord = 0;
--	SET @iStaffID = 0;
--	SET @iExcludeReturned = 0;
--	
--	CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
--	@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);
--	
--	SELECT @iErrorCode, @sErrorMsg;
--	
--	SELECT IF(@sErrorMsg = '�������ݸ�ʽ����' AND @iTotalRecord = 0 AND @iErrorCode = 8, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '-------------------- Case12: web������󳤶ȵ����۵���������ѯ���۵�������26λ�� -------------------------' AS 'Case12';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS1234832180000111111234_1', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'LS1234832180000111111234_1';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case13: web��ʹ�����۵����ڵ���Ʒ���ƽ���ģ����ѯ -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '�ɱȿ���Ƭ';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

SELECT '-------------------- Case14: web��ʹ�����۵������ڵ���Ʒ���ƽ���ģ����ѯ -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '�����ڵ���Ʒ';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

SELECT '-------------------- Case15: web��ʹ��null����ģ����ѯ -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = NULL;
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';

SELECT '-------------------- Case16: web��ʹ�ÿմ�('')����ģ����ѯ -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';

SELECT '-------------------- Case17: web��ʹ�ó������ַ�(���ȴ���32λ)����ģ����ѯ -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'ѩ���鼡�ܾ���ԭѩ���鼡�ܾ���ԭѩ���鼡�ܾ���ԭ';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';

SELECT '-------------------- Case18: web��ʹ�÷Ƿ��ַ������Ժ���ѯ -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '@#$%^!&*)*)*&%@!*^*^!%*';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';

SELECT '-------------------- Case19: web��ʹ����Ʒ���Ƶ�һ���ֽ��в�ѯ�����Ժ���ѯ -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'A';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case19 Testing Result';

SELECT '-------------------- Case20: web��ʹ����Ʒ��������"_"�����ַ�����ģ����ѯ -------------------------' AS 'Case20';

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'SB201906100001',2,5,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........',-1, now(),1.5,1.1,0,0,0,0,0,0,0,2,2);

SET @iTradeID = LAST_INSERT_ID();


INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iTradeID, 1,'�����ľ���_hjjkkj', 63, 10, 1, 10, 10, 10);


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '_';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case20 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_CommodityName = '�����ľ���_hjjkkj';
DELETE FROM T_RetailTrade WHERE F_ID = @iTradeID;



SELECT '-------------------- Case21: web��iExcludeReturned = 0����ѯ�������۵������˻��� -------------------------' AS 'Case21';
-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS123483218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�A��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, 1, '�ɱȿ���Ƭ', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS223483218000', 3, 5, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', @retailTradeID, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �½�B���˻���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, 1, '�ɱȿ���Ƭ', 1, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case21 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;


SELECT '-------------------- Case22: web��iExcludeReturned = 1����ѯ�������۵��������˻��� -------------------------' AS 'Case22';

-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS323483218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�A��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, 1, '�ɱȿ���Ƭ', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS423483218000', 3, 5, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', @retailTradeID, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �½�B���˻���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, 1, '�ɱȿ���Ƭ', 1, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 1;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case22 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

SELECT '-------------------- Case23: ��pos���������10λ��sn�������� -------------------------' AS 'Case23';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS12348321218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS12348321';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case23 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case24: ��pos������մ���ѯ�������۵� -------------------------' AS 'Case24';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case24 Testing Result';

SELECT '-------------------- Case25:��pos���ô�����󳤶ȵ����۵���������ѯ���۵�������26λ��,�����ǲ�ѯ�������� -------------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS123483218000011111245456465';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case25 Testing Result';

SELECT '-------------------- Case26: ��pos����С����С���ȵ����۵���������ѯ���۵���С��10λ��,�����ǲ�ѯ�������� -------------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS1234832';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case26 Testing Result';

SELECT '-------------------- Case27: ��pos������󳤶ȵ����۵���������ѯ���۵�������26λ�� -------------------------' AS 'Case27';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS1234832180000111111234_1', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS1234832180000111111234_1';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case27 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case28: ��pos��ʹ�����Ĳ�ѯsn -------------------------' AS 'Case28';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '�ɱȿ���Ƭ';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case28 Testing Result';

SELECT '-------------------- Case29: ��pos��ʹ��null����ģ����ѯ -------------------------' AS 'Case29';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = NULL;
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case29 Testing Result';

SELECT '-------------------- Case30: pos��ʹ�ó������ַ�(���ȴ���32λ)���в�ѯ -------------------------' AS 'Case30';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS200901010001123456123456878985465';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case30 Testing Result';

SELECT '-------------------- Case31: web��ʹ�÷Ƿ��ַ������Ժ���ѯ -------------------------' AS 'Case31';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '@#$%^!&*)*)*&%@!*^*^!%*';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case31 Testing Result';