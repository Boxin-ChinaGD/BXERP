SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportByCategoryParent_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�����ۼ�¼�Ұ��������Ʒ���� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2091/10/17 00:00:00';
SET @deleteOldData=1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @ResultVerification1=0;
SET @ResultVerification2=0;
SET @iCategoryParentID = 1;
SET @iCategoryParentID2 = 2;
SET @dPriceCommodity1=45;
SET @dNoCommodity1=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½�����2
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test12', @iCategoryParentID2, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest1', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɿڿ���Test1', '����', '����', 1, '��', 1, @categoryID2, 'KK', 1, 
3.3, 3, 1, 1, NULL, 3, 30, '04/14/2018', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();

-- �½����۵� ��90
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041228', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �½����۵�C��������Ʒ 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest1', 3, @dNoCommodity1, 45, 45, @dPriceCommodity1, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½����۵� ��50
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041229', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();

-- �½�A��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '�ɿڿ���Test1', 1, @dNoCommodity2, 10, 10, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = (@dPriceCommodity1 * @dNoCommodity1) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 and F_TotalAmount = (@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1, '���Գɹ�','����ʧ��') AS 'Test Case1 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


-- ��ʱ�����Ʒ����IDΪ���Ψһ�����ڶ��β�������ݿ����	
SELECT '-------------------- Case2:�����ۼ�¼�Ұ���һ����Ʒ���� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2019/4/18 15:30:30';
SET @deleteOldData=0;
SET @ResultVerification=0;
SET @iCategoryParentID = 1;
SET @dPriceCommodity=45.5;
SET @dNoCommodity=3;
-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest1', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �½����۵� ��136.5
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300041228', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �½����۵�C��������Ʒ 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest1', 3, @dNoCommodity, 45, 45, @dPriceCommodity, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = (@dNoCommodity * @dPriceCommodity) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case2 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case3:���۵������ۼ�¼�Ұ����˻���¼ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2091/1/18 17:40:34';
SET @deleteOldData=0;
SET @ResultVerification=0;
SET @iCategoryParentID = 1;
SET @dPriceCommodity=45.5;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=45.5;
SET @dReturnNoCommodity=1;
-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test1', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest1', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �½����۵� ��90
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300051228', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �½����۵���������Ʒ 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest1', 3, @dNoCommodity, 45, 45, @dPriceCommodity, 200, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻��� ��45.5
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2091090412300300051228_1', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �½����۵���������Ʒ 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest1', 3, @dReturnNoCommodity, 45, 45, @dReturnPriceCommodity, 200, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = ((@dNoCommodity * @dPriceCommodity) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case3 Result';

DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case4:���۵��޼�¼��� -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='2029/1/13 17:40:34';
SET @deleteOldData=0;
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(found_rows()=0 AND @iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Test Case4 Result';


SELECT '-------------------- Case5:3��2�ŷ���1�����۵�A,�����ܶ�Ϊ50���˻���B,B��A�˻�30����ô��һ�����1�������ܶ���20 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @retailAmount = 0;
SET @dPriceCommodity=25;
SET @dNoCommodity=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=1;
SET @ResultVerification=0;
-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test5', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest5', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest5', 1, @dNoCommodity, 321, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B��B��A�˻�30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest5', 1, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT F_TotalAmount INTO @retailAmount FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount = ((@dNoCommodity * @dPriceCommodity) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1 ,'���Գɹ�','����ʧ��') AS 'Test Case5 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case6:3��2�ŷ���1�����۵�A,�����ܶ�Ϊ50���˻���B,B��3��1�ŵ����۵�C�˻�30����ô��һ�����1�������ܶ���20 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @dPriceCommodity=30;
SET @dNoCommodity=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=2;
SET @ResultVerification=0;

-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test61', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest61', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �½����۵�C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- �½����۵�C��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID1, '�ɱȿ���ƬTest6', 1, @dNoCommodity, 321, 500, @dPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityCID = last_insert_id();
-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest6', 1, @dNoCommodity2, 321, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B��B��3��1�ŵ����۵�C�˻�30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest6', 1, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount =((@dNoCommodity2 * @dPriceCommodity2) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1,'���Գɹ�','����ʧ��') AS 'Test Case6 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case7:3��2�ŷ���1�����۵�A,�����ܶ�Ϊ50���˻���B,B��3��1�ŵ����۵�C�˻��ܶ�100����ô��һ�����1�������ܶ��Ϊ-50 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @retailAmount = 0;
SET @dPriceCommodity=100;
SET @dNoCommodity=2;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=50;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=2;
SET @ResultVerification=0;

-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test7', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest7', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �½����۵�C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- �½����۵�C��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID1, '�ɱȿ���ƬTest7', 1, @dNoCommodity, 321, 500, 100, 300, NULL);
SET @returnRetailTradeCommodityCID = last_insert_id();
-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest7', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B��B��3��1�ŵ����۵�C�˻�100
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest7', 1, 2, 321, 500, 50, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID and F_TotalAmount =((@dNoCommodity2 * @dPriceCommodity2) - (@dReturnNoCommodity * @dReturnPriceCommodity)) AND F_Datetime = DATE_FORMAT(@dSaleDatetime,'%Y-%m-%d');
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case7 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;
-- 
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;


SELECT '-------------------- Case8:3��2�ŷ���X�����۵�A,�����ܶ�Ϊ50������2�����ۣ��˻���B,B��3��1�ŷ���Y���۵�D�˻�30����ô��һ�����X�������ܶ���50������Y�����ܶ�-30 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @dSaleDatetimeC ='3029/03/01 00:00:00';
SET @deleteOldData=1;
SET @dPriceCommodity=200;
SET @dNoCommodity=100;
SET @dPriceCommodity2=25;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity=15;
SET @dReturnNoCommodity=2;
SET @iCategoryParentID=12;
SET @iCategoryParentID2=11;
SET @ResultVerification=0;
SET @ResultVerification2=0;

-- �½�����1
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test81', @iCategoryParentID, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½�����2
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test82', @iCategoryParentID2, '08/20/2019 11:47:50', '08/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest8', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɿڿ���Test8', '����', '����', 1, '��', 1, @categoryID2, 'KK', 1, 
3.3, 3, 1, 1, NULL, 3, 30, '04/14/2018', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '08/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();

-- �½����۵�C
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test423483218000', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetimeC, 2, 4, '0', 1, '........', -1, @dSaleDatetimeC, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeCID = last_insert_id();
-- �½����۵�C��������Ʒ

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeCID, @commodityID2, '�ɿڿ���', 3, @dNoCommodity, 254, 100, @dPriceCommodity, 200, NULL);


SET @returnRetailTradeCommodityCID = last_insert_id();
-- �½����۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- �½�A��������Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɿڿ���Test8', 1, @dNoCommodity2, 321, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �½��˻���B��B��3��1�ŵķ���Y���۵�C�˻�30
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɿڿ���Test8', 2, @dReturnNoCommodity, 321, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 AND F_Datetime = @dSaleDatetime;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2  AND F_TotalAmount=-(@dReturnPriceCommodity * @dReturnNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1 AND @ResultVerification2 = 1,'���Գɹ�','����ʧ��') AS 'Test Case8_1 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityCID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeCID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case9: ����ֻ����һ��������Ʒ���鿴����ͳ�ƺͱ�����ʾ�Ƿ������� -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=3.14159;
SET @dNoCommodity=2;
SET @ResultVerification=0;

-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test91', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest91', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 2, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest91', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity*@dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case9 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case10: ����ֻ����һ��������Ʒ�������˻����鿴����ͳ�ƺͱ�����ʾ�Ƿ������� -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=213.314;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=213.314;
SET @dReturnNoCommodity=1;
SET @ResultVerification=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test92', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest10', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest10', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- �½��˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest10', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=((@dPriceCommodity*@dNoCommodity) - (@dReturnNoCommodity*@dReturnPriceCommodity))AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1 ,'���Գɹ�','����ʧ��') AS 'Test Case10 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;
	
SELECT '-------------------- Case11: ����ֻ����һ��������Ʒ��ȫ���˻����鿴����ͳ�ƺͱ�����ʾ�Ƿ������� -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=20.3141235123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=20.3141235123;
SET @dReturnNoCommodity=3;
SET @ResultVerification=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test11', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest11', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest11', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- �½��˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest11', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=((@dPriceCommodity*@dNoCommodity) - (@dReturnNoCommodity*@dReturnPriceCommodity)) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case11 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case12: ����ֻ�˻�һ��������Ʒ�����ˣ����������� -------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=2019.314123;
SET @dReturnNoCommodity=2;
SET @ResultVerification=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test12', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest12', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- ����һ�żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest12', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- �½�һ�żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest12', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=-(@dReturnNoCommodity * @dReturnPriceCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case12 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '--------------------case13: ����ֻ�˻�һ��������Ʒȫ���ˣ����������� -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/02/00 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dReturnPriceCommodity=2019.314123;
SET @dReturnNoCommodity=3;
SET @ResultVerification=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test13', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest13', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- ����һ�żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest13', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
	
-- �½�һ�żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023989_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest13', 1, @dReturnNoCommodity, 20, 500, @dReturnPriceCommodity, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=-(@dReturnNoCommodity * @dReturnPriceCommodity) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification=1,'���Գɹ�','����ʧ��') AS 'Test Case13 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '-------------------- Case14:������������������Ʒ���������۵� -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @deleteOldData=1;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=3;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test14', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test141', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest14', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest141', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest14', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest141', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity * @dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1 ,'���Գɹ�','����ʧ��') AS 'Test Case14 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '--------------------Case15: ������������������Ʒ���������۵�(����case:�����ݵ���ֵΪ����С�����6λʱ���������ӦС�ڵ���0.000001) -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @deleteOldData=1;
SET @dPriceCommodity=2019.314120212;
SET @dNoCommodity=3;
SET @dPriceCommodity2=2019.314120212;
SET @dNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
SET @dTOLERANCE = 0.000001;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test15', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test151', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest15', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest151', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest15', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest151', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT F_TotalAmount INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_Datetime = @dSaleDatetime;
SELECT F_TotalAmount INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND (@ResultVerification1 - (@dPriceCommodity * @dNoCommodity)) <= @dTOLERANCE  AND (@ResultVerification2 - (@dPriceCommodity2 * @dNoCommodity2)) <= @dTOLERANCE,'���Գɹ�','����ʧ��') AS 'Test Case14 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


SELECT '--------------------Case16: ������������������Ʒ���������۵� -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=2;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=1;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test16', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test161', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest16', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest161', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- ������һ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest16', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest161', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- �����ڶ������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '�ɱȿ���ƬTest16', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '�ɱȿ���ƬTest161', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=2*(@dPriceCommodity * @dNoCommodity) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=2*(@dPriceCommodity2 * @dNoCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case16 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case17:������������������Ʒ���������۵��������˻� -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity=2019.314123;
SET @dNoCommodity=4;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=2020.3145;
SET @dReturnNoCommodity1=1;
SET @dReturnPriceCommodity2=2019.314123;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test17', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test171', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest17', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest171', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest17', 1, @dNoCommodity, 20, 500, @dPriceCommodity, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest171', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest17', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɱȿ���ƬTest171', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity * @dNoCommodity) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case17 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '--------------------Case18: ������������������Ʒ���������۵���ȫ���˻� -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @retailAmount1 = 0;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=2019.314123;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=2020.3145;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=2019.314123;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=2020.3145;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test18', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test181', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType,
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest18', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest181', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest18', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest181', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500023988_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest18', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɱȿ���ƬTest181', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(@dPriceCommodity1 * @dNoCommodity1) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(@dPriceCommodity2 * @dNoCommodity2) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case18 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case19:������������������Ʒ���������۵��������˻� -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=20.31;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=22.31459;
SET @dNoCommodity3=2;
SET @dReturnPriceCommodity1=20.31;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=22.31459;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test19', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test191', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest19', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111',0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest191', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- ������һ�����۵� 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest19', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest191', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- �����ڶ������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '�ɱȿ���ƬTest19', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '�ɱȿ���ƬTest191', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
-- ������һ�����۵����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest19', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɱȿ���ƬTest191', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(2*(@dPriceCommodity1 * @dNoCommodity1)) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(2*(@dPriceCommodity2 * @dNoCommodity2)) - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case19 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case20: ������������������Ʒ���������۵���ȫ���˻� -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=220.31;
SET @dNoCommodity1=4;
SET @dPriceCommodity2=222.31459;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=220.31;
SET @dReturnNoCommodity1=4;
SET @dReturnPriceCommodity2=222.31459;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test20', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test171', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest20', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType,
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest201', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- ������һ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest20', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2, '�ɱȿ���ƬTest201', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
-- �����ڶ������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '�ɱȿ���ƬTest20', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID2, '�ɱȿ���ƬTest201', 1, @dNoCommodity2, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityID4 = last_insert_id();
-- ������һ�����۵����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023988_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest20', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɱȿ���ƬTest201', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
-- �����ڶ������۵����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103005212500023989_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID2, @dSaleDatetime, 100, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '�ɱȿ���ƬTest20', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '�ɱȿ���ƬTest201', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=(2*(@dPriceCommodity1 * @dNoCommodity1)) - 2*(@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount=(2*(@dPriceCommodity2 * @dNoCommodity2)) - 2*(@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_TotalAmount=0 AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case20 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;

DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case21: ����ֻ�˻�����������Ʒ�����ˣ����������� -------------------------' AS 'Case21';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=220.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=3;
SET @dPriceCommodity2=222.31459;
SET @dNoCommodity3=2;
SET @dNoCommodity4=2;
SET @dReturnPriceCommodity1=220.321;
SET @dReturnNoCommodity1=2;
SET @dReturnPriceCommodity2=222.31459;
SET @dReturnNoCommodity2=1;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test21', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test211', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest21', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest211', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- ������һ�żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID1, '�ɱȿ���ƬTest21', 1, @dNoCommodity1, 20, 500, 20, @dPriceCommodity1, NULL);
SET @retailTradeCommodityIDYesD = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID2, '�ɱȿ���ƬTest211', 1, @dNoCommodity3, 20, 500, 20, @dPriceCommodity2, NULL);
SET @retailTradeCommodityIDYesD2 = last_insert_id();
-- �����ڶ��żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID1, '�ɱȿ���ƬTest21', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID2, '�ɱȿ���ƬTest211', 1, @dNoCommodity4, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD4 = last_insert_id();
	
-- �½���һ�żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest21', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
-- �½��ڶ��żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989_1', 6, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '�ɱȿ���ƬTest211', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount= - (@dReturnNoCommodity1 * @dReturnPriceCommodity1) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount= - (@dReturnNoCommodity2 * @dReturnPriceCommodity2) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case21 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;		
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;

SELECT '-------------------- Case22:����ֻ�˻�����������Ʒȫ���ˣ����������� -------------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/03 00:00:00';
SET @dSaleDatetimeYesterday='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @iCategoryParentID2 = 10;
SET @dPriceCommodity1=2.4123;
SET @dNoCommodity1=4;
SET @dNoCommodity2=3;
SET @dPriceCommodity2=2.7543;
SET @dNoCommodity3=2;
SET @dNoCommodity4=2;
SET @dReturnPriceCommodity1=2.4123;
SET @dReturnNoCommodity1=4;
SET @dReturnNoCommodity2=3;
SET @dReturnPriceCommodity2=2.7543;
SET @dReturnNoCommodity3=2;
SET @dReturnNoCommodity4=2;
SET @ResultVerification1=0;
SET @ResultVerification2=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test22', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test221', @iCategoryParentID2, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID2 = last_insert_id();
SELECT @categoryID2;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest22', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest221', '��Ƭ', '��', 1, '��', 3, @categoryID2, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- ������һ�żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID1, '�ɱȿ���ƬTest22', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD, @commodityID2, '�ɱȿ���ƬTest221', 1, @dNoCommodity3, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD2 = last_insert_id();
-- �����ڶ��żٶ�Ϊ����ʱ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetimeYesterday, 2, 4, '0', 1, '........', -1, @dSaleDatetimeYesterday, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeIDYesD2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID1, '�ɱȿ���ƬTest22', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityIDYesD3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeIDYesD2, @commodityID2, '�ɱȿ���ƬTest221', 1, @dNoCommodity3, 20, 500, @dPriceCommodity2, 300, NULL);
SET @retailTradeCommodityIDYesD4 = last_insert_id();
	
-- �½���һ�żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013988_1', 5, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest22', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID2, '�ɱȿ���ƬTest221', 1, @dReturnNoCommodity3, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
-- �½��ڶ��żٶ�Ϊ����ʱ���˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500013989_1', 6, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeIDYesD2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '�ɱȿ���ƬTest22', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID2, '�ɱȿ���ƬTest221', 1, @dReturnNoCommodity4, 20, 500, @dReturnPriceCommodity2, 300, NULL);
SET @returnRetailTradeCommodityID4 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount= - ((@dReturnNoCommodity1 * @dReturnPriceCommodity1) +(@dReturnNoCommodity2 * @dReturnPriceCommodity1)) AND F_Datetime = @dSaleDatetime;
SELECT 1 INTO @ResultVerification2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID2 AND F_TotalAmount= - ((@dReturnNoCommodity3 * @dReturnPriceCommodity2) + (@dReturnNoCommodity4 * @dReturnPriceCommodity2)) AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1 AND @ResultVerification2=1,'���Գɹ�','����ʧ��') AS 'Test Case22 Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityIDYesD4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeIDYesD2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;		
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_category WHERE F_ID = @categoryID1;
DELETE FROM t_category WHERE F_ID = @categoryID2;


SELECT '--------------------Case23: ����ֻ����һ��������Ʒ���������۵��������˻�-------------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity1=21.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=21;
SET @dReturnNoCommodity1=1;
SET @ResultVerification1=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test23', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest23', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- ������һ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest23', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �����ڶ������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '�ɱȿ���ƬTest23', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
	
-- �½��˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest23', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @dTotalAmount=((@dNoCommodity1 * @dPriceCommodity1) +(@dNoCommodity2 * @dPriceCommodity1)) - (@dReturnNoCommodity1 * @dReturnPriceCommodity1);	
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=@dTotalAmount AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1,'���Գɹ�','����ʧ��') AS 'Test Case23 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;

SELECT '--------------------Case24: ����ֻ����һ��������Ʒ���������۵���ȫ���˻�-------------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime ='3029/03/02 00:00:00';
SET @deleteOldData=1;
SET @iCategoryParentID = 11;
SET @dPriceCommodity1=21.321;
SET @dNoCommodity1=4;
SET @dNoCommodity2=2;
SET @dReturnPriceCommodity1=21.321;
SET @dReturnNoCommodity1=4;
SET @dReturnNoCommodity2=2;
SET @ResultVerification1=0;
	
-- �½�����
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Test24', @iCategoryParentID, '10/20/2019 11:47:50', '10/20/2019 11:47:50');
SET @categoryID1 = last_insert_id();
SELECT @categoryID1;
-- �½���Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���ƬTest24', '��Ƭ', '��', 1, '��', 3, @categoryID1, 'SP', 1, 
2, 11, 1, 1, NULL, 3, 30, '10/14/2018 01:00:01 ����', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 ����', '10/19/2019 05:50:26 ����', NULL, NULL, NULL, NULL);
SET @commodityID1 = last_insert_id();
-- ������һ�����۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID1, '�ɱȿ���ƬTest24', 1, @dNoCommodity1, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- �����ڶ�������
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 60, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID1, '�ɱȿ���ƬTest24', 1, @dNoCommodity2, 20, 500, @dPriceCommodity1, 300, NULL);
SET @retailTradeCommodityID2 = last_insert_id();
	
-- �½��˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024988_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID1, '�ɱȿ���ƬTest24', 1, @dReturnNoCommodity1, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

-- �½��˻��������˻�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019103015212500024989_1', 3, 7, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', @retailTradeID2, @dSaleDatetime, 20, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID2 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID2, @commodityID1, '�ɱȿ���ƬTest24', 1, @dReturnNoCommodity2, 20, 500, @dReturnPriceCommodity1, 300, NULL);
SET @returnRetailTradeCommodityID2 = last_insert_id();
SET @iShopID = 2;
	
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime, @deleteOldData);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SET @dTotalAmount=((@dNoCommodity1 * @dPriceCommodity1) +(@dNoCommodity2 * @dPriceCommodity1)) - ((@dReturnNoCommodity1 * @dReturnPriceCommodity1)+(@dReturnNoCommodity2 * @dReturnPriceCommodity1));
SELECT 1 INTO @ResultVerification1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID=@iCategoryParentID AND F_TotalAmount=@dTotalAmount AND F_Datetime = @dSaleDatetime;
SELECT IF(@iErrorCode = 0 AND @ResultVerification1=1,'���Գɹ�','����ʧ��') AS 'Test Case24 Result';
	
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID2;
	
DELETE FROM t_commodity WHERE F_ID = @commodityID1;
DELETE FROM t_category WHERE F_ID = @categoryID1;
	