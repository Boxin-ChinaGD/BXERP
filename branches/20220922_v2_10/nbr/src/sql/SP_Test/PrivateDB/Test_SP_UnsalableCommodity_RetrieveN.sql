SELECT '++++++++++++++++++ Test_SP_UnsalableCommodity_RetrieveN.sql ++++++++++++++++++++';


SELECT '-------------------- Case1:  ��ѯ����dStartDate��dEndDate֮��û�н������۹�����Ʒ(��������Ʒ) -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SET @totalRecord = 0;
SELECT count(*) INTO @totalRecord FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0 AND @totalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case2:  ����������Ϣ����ID -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 1000000;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '�����ڸ���Ϣ���' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case3:  �������Ĺ�˾ID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1000000;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '�����ڸù�˾' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case4:  �½�һ����Ʒ,�����Ʒ������ʱ����dStartDate��dEndDate֮��,�����Ʒ����������Ʒ -------------------------' AS 'Case4';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 29 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case4_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '-------------------- Case5:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,�����Ʒ��������Ʒ -------------------------' AS 'Case5';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2, 8, 12, 800, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() > 0, '���Գɹ�', '����ʧ��') AS 'Case5_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case6:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,�����˻�ʱ����������������֮�䣬�����Ʒ����������Ʒ -------------------------' AS 'Case6';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2, 8, 12, 800, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();


-- �½��˻���B��B������A�˻�
SET @dSaleDatetimeB = DATE_SUB(now(), INTERVAL 29 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetimeB, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID, '����������Ʒ��', 2, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() > 0, '���Գɹ�', '����ʧ��') AS 'Case6_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case7:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,���Ǹ���Ʒ�Ŀ��Ϊ0�������Ʒ����������Ʒ -------------------------' AS 'Case7';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case7_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '-------------------- Case8:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,���Ǹ���Ʒ�������Ʒ�������Ʒ����������Ʒ -------------------------' AS 'Case8';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 1, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case8_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case9:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,���Ǹ���Ʒ�Ƕ��װ��Ʒ�������Ʒ����������Ʒ -------------------------' AS 'Case9';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 2, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case9_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '-------------------- Case10:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,���Ǹ���Ʒ�Ƿ�����Ʒ�������Ʒ����������Ʒ -------------------------' AS 'Case10';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 3, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case10_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case11:  �½�һ����Ʒ,�����Ʒ������ʱ�䲻��dStartDate��dEndDate֮��,���Ǹ���ƷΪ��ɾ��״̬�������Ʒ����������Ʒ -------------------------' AS 'Case11';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (2, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case11_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

SELECT '-------------------- Case12:  �½�һ����Ʒ,����Ʒ���ۼ�Ϊ0����Ʒ������ʱ�䲻��dStartDate��dEndDate֮�䣬����Ʒ����������Ʒ -------------------------' AS 'Case11';

-- �½�һ����Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (2, '�Ҳ���������Ʒ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- �½�һ�����۵�retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½����۵�A��������ƷretailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '�ɿڿ���Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"��Ʒ����\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'Case11_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;