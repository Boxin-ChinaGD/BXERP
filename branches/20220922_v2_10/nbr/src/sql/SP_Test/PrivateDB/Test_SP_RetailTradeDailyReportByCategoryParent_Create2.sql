SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportByCategoryParent_Create2.sql ++++++++++++++++++++';
--	����1��3��ֻ����ͬһ������Ʒ�����������1����
--	4��6�Ÿ�����һ��������Ʒ���ֱ��������1�����2�����3����
--	7��9�����������ݣ�
--	10��12�Ÿ�����������ͬ�Ķ���������Ʒ���ֱ��������1-3�����4-6�����7-9����
--	13��15��������ͬ�Ķ���������Ʒ�����������1-3����
--	16��18��ֻ�˻�ͬһ������Ʒ�����˻����1����
--	19��21�Ÿ��˻�һ��������Ʒ���ֱ��˻����1�����2�����3����
--	22��24�Ÿ��˻�������ͬ�Ķ���������Ʒ���ֱ��˻����1-3�����4-6�����7-9����
--	25��27���˻���ͬ�Ķ���������Ʒ�����˻����1-3����
--	28��30���������˻�����������Ʒ���ֱ��������˻����1-2�����2-3�����3-4��
SELECT '-------------------- Case1:����1���������һ����Ʒ(���˻�) -------------------------' AS 'Case1';
-- �������һ����Ʒ����
SET @iCategoryParentID1 = 2; 
SET @iCategoryID_CategoryParent1 = 0;
SET @iSingleCommodityID_CategoryParent1 = 0;
SET @dPrice_SingleCommodity_CategoryParent1 = 3.5;
SET @iSingleCommodityID2_CategoryParent1 = 0;
SET @dPrice_SingleCommodity2_CategoryParent1 = 3.5;
SET @iCompositionCommodityID_CategoryParent1 = 0;
SET @dPrice_CompositionCommodity_CategoryParent1 = 21;
SET @iMultiPackagingCommodityID_CategoryParent1 = 0;
SET @iRefCommodityMultiple_CategoryParent1 = 24;
SET @dPrice_MultiPackagingCommodity_CategoryParent1 = 84;
SET @iServiceCommodityID_CategoryParent1 = 0;
SET @dPrice_ServiceCommodity_CategoryParent1 = 5;
-- �������һ��С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP1', @iCategoryParentID1, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent1 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɿڿ���_CategoryParent1', '�ɿڿ���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent1, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent1 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '���¿���_CategoryParent1', '���¿���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent1, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent1 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '���ִ����_CategoryParent1', '����', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent1, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent1 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent1, @iSingleCommodityID_CategoryParent1, 3, @dPrice_SingleCommodity_CategoryParent1);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent1, @iSingleCommodityID2_CategoryParent1, 3, @dPrice_SingleCommodity2_CategoryParent1);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ�����¿���_CategoryParent1', '���¿���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent1, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID2_CategoryParent1, @iRefCommodityMultiple_CategoryParent1, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent1 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent1','kd','��',4,NULL,4,@iCategoryID_CategoryParent1,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���A',3);
SET @iServiceCommodityID_CategoryParent1 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test1 ='2099/06/01 08:08:08';
SET @deleteOldData=1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iNO_SingleCommodity_Test1 = 3;
SET @iNO_SingleCommodity2_Test1 = 5;
SET @iNO_CompositionCommodity_Test1 = 2;
SET @iNO_MultiPackagingCommodity_Test1 = 3;
SET @iNO_ServiceCommodity_Test1 = 1;
SET @iRetailTradeID1_Test1 = 0;
SET @iRetailTradeID2_Test1 = 0;
SET @iResultVerification_Test1 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��2�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test1, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test1, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test1 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test1, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_Test1, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test1, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test1, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_Test1, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test1, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����5����ͨ��Ʒ2��3�����װ��Ʒ��1��������Ʒ����274.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test1, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test1, 274.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test1 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test1, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test1, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_Test1, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test1, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test1, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_Test1, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test1, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test1, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d'); -- 
SET @dRetailTradeID1_TotalAmount_Test1 = (@iNO_SingleCommodity_Test1 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_Test1 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test1 = (@iNO_MultiPackagingCommodity_Test1 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iNO_SingleCommodity2_Test1 * @dPrice_SingleCommodity2_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test1 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test1 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount =  @dRetailTradeID1_TotalAmount_Test1 +  @dRetailTradeID2_TotalAmount_Test1 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test1;
SELECT IF(@iResultVerification_Test1 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case1 Result';




SELECT '-------------------- Case2:����2���������һ����Ʒ(���˻�) -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test2 ='2099/06/02 08:08:08';
SET @iNO_SingleCommodity_Test2 = 1;
SET @iNO_SingleCommodity2_Test2 = 1;
SET @iNO_CompositionCommodity_Test2 = 1;
SET @iNO_MultiPackagingCommodity_Test2 = 2;
SET @iNO_ServiceCommodity_Test2 = 1;
SET @iRetailTradeID1_Test2 = 0;
SET @iRetailTradeID2_Test2 = 0;
SET @iResultVerification_Test2 = 0;
-- �������۵�1 (����1����ͨ��Ʒ1��2�����װ��Ʒ����171.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000001', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test2, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test2, 171.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test2 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test2, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_Test2, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test2, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test2, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test2, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test2, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����29.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000002', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test2, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test2, 29.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test2 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_Test2, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test2, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_Test2, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test2, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test2, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_Test2, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test2, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test2, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test2 = (@iNO_SingleCommodity_Test2 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_MultiPackagingCommodity_Test2 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test2 = (@iNO_CompositionCommodity_Test2 * @dPrice_CompositionCommodity_CategoryParent1) + (@iNO_SingleCommodity2_Test2 * @dPrice_SingleCommodity2_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test2 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test2 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test2 + @dRetailTradeID2_TotalAmount_Test2 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d'); --  

SELECT @iResultVerification_Test2;
SELECT IF(@iResultVerification_Test2 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case2 Result';



SELECT '-------------------- Case3:����3���������һ����Ʒ(���˻�) -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test3 ='2099/06/03 08:08:08';
SET @iNO_SingleCommodity_Test3 = 3;
SET @iNO_SingleCommodity2_Test3 = 1;
SET @iNO_CompositionCommodity_Test3 = 1;
SET @iNO_MultiPackagingCommodity_Test3 = 1;
SET @iNO_ServiceCommodity_Test3 = 1;
SET @iRetailTradeID1_Test3 = 0;
SET @iRetailTradeID2_Test3 = 0;
SET @iResultVerification_Test3 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��1��������Ʒ����15.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000001', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test3, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test3, 15.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test3 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test3, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_Test3, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test3, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test3, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_Test3, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test3, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����108.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000002', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test3, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test3, 108.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test3 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_Test3, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test3, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_Test3, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test3, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test3, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test3, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test3, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test3, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test3 = (@iNO_SingleCommodity_Test3 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_ServiceCommodity_Test3 * @dPrice_ServiceCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test3 = (@iNO_SingleCommodity2_Test3 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_CompositionCommodity_Test3 * @dPrice_CompositionCommodity_CategoryParent1) -- 
											+ (@iNO_MultiPackagingCommodity_Test3 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test3 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test3 +  @dRetailTradeID2_TotalAmount_Test3 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test3;
SELECT IF(@iResultVerification_Test3 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case3 Result';



SELECT '-------------------- Case4:����4������ͬһ������Ʒ(���˻�) -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test4 ='2099/06/04 08:08:08';
SET @iNO_SingleCommodity_Test4 = 5;
SET @iNO_SingleCommodity2_Test4 = 1;
SET @iNO_CompositionCommodity_Test4 = 1;
SET @iNO_MultiPackagingCommodity_Test4 = 1;
SET @iNO_ServiceCommodity_Test4 = 1;
SET @iRetailTradeID1_Test4 = 0;
SET @iRetailTradeID2_Test4 = 0;
SET @iResultVerification_Test4 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1�������Ʒ����38.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000001', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test4, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test4, 28.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test4 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test4, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_Test4, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_Test4, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test4, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_Test4, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_Test4, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000002', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test4, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test4, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test4 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_Test4, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_Test4, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_Test4, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_Test4, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test4, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_Test4, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_Test4, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test4, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test4 = (@iNO_SingleCommodity_Test4 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_Test4 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_Test4 = (@iNO_SingleCommodity2_Test4 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_MultiPackagingCommodity_Test4 * @dPrice_MultiPackagingCommodity_CategoryParent1) -- 
											+ (@iNO_ServiceCommodity_Test4 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test4 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test4 + @dRetailTradeID2_TotalAmount_Test4 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test4;
SELECT IF(@iResultVerification_Test4 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case4 Result';




SELECT '-------------------- Case5:����5��������������Ʒ(���˻�) -------------------------' AS 'Case5';
-- -- ������������Ʒ����
SET @iCategoryParentID2 = 3; 
SET @iCategoryID_CategoryParent2 = 0;
SET @iSingleCommodityID_CategoryParent2 = 0;
SET @dPrice_SingleCommodity_CategoryParent2 = 4.99;
SET @iSingleCommodityID2_CategoryParent2 = 0;
SET @dPrice_SingleCommodity2_CategoryParent2 = 4.99;
SET @iCompositionCommodityID_CategoryParent2 = 0;
SET @dPrice_CompositionCommodity_CategoryParent2 = 49.9;
SET @iMultiPackagingCommodityID_CategoryParent2 = 0;
SET @iRefCommodityMultiple_CategoryParent2 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent2 = 9.98;
SET @iServiceCommodityID_CategoryParent2 = 0;
SET @dPrice_ServiceCommodity_CategoryParent2 = 5;
-- ����������С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP2', @iCategoryParentID2, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent2 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '������Ƭ_CategoryParent2', '������Ƭ', '��', 1, '��', 3, @iCategoryID_CategoryParent2, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent2 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱȿ���Ƭ_CategoryParent2', '�ɱȿ���Ƭ', '��', 1, '��', 3, @iCategoryID_CategoryParent2, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent2 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '��Ƭ�����_CategoryParent2', '��Ƭ', '��', 1, '��', 3, @iCategoryID_CategoryParent2, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent2 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent2, @iSingleCommodityID_CategoryParent2, 5, @dPrice_SingleCommodity_CategoryParent2);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent2, @iSingleCommodityID2_CategoryParent2, 5, @dPrice_SingleCommodity2_CategoryParent2);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ��������Ƭ_CategoryParent2', '������Ƭ', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent2, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID_CategoryParent2, @iRefCommodityMultiple_CategoryParent2, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent2 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent2','kd','��',4,NULL,4,@iCategoryID_CategoryParent2,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���B',3);
SET @iServiceCommodityID_CategoryParent2 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test5 ='2099/06/05 08:08:08';
SET @iNO_SingleCommodity_Test5 = 2;
SET @iNO_SingleCommodity2_Test5 = 2;
SET @iNO_CompositionCommodity_Test5 = 1;
SET @iNO_MultiPackagingCommodity_Test5 = 2;
SET @iNO_ServiceCommodity_Test5 = 1;
SET @iRetailTradeID1_Test5 = 0;
SET @iRetailTradeID2_Test5 = 0;
SET @iResultVerification_Test5 = 0;
-- �������۵�1 (����2����ͨ��Ʒ2��2�����װ��Ʒ����29.94Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000001', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test5, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test5, 29.96, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test5 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test5, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_Test5, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_Test5, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test5, @iMultiPackagingCommodityID_CategoryParent2, 'һ�������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_Test5, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_Test5, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����2����ͨ��Ʒ1��1�������Ʒ��1��������Ʒ����64.88Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000002', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test5, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test5, 64.88, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test5 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_Test5, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_Test5, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_Test5, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_Test5, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test5, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_Test5, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_Test5, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test5, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test5 = (@iNO_SingleCommodity2_Test5 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_MultiPackagingCommodity_Test5 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_Test5 = (@iNO_SingleCommodity_Test5 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_CompositionCommodity_Test5 * @dPrice_CompositionCommodity_CategoryParent2)
											+ (@iNO_ServiceCommodity_Test5 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_Test5 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test5 + @dRetailTradeID2_TotalAmount_Test5 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test5;
SELECT IF(@iResultVerification_Test5 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case5 Result';





SELECT '-------------------- Case6:����6���������������Ʒ(���˻�) -------------------------' AS 'Case6';
-- -- �������������Ʒ����
SET @iCategoryParentID3 = 5; 
SET @iCategoryID_CategoryParent3 = 0;
SET @iSingleCommodityID_CategoryParent3 = 0;
SET @dPrice_SingleCommodity_CategoryParent3 = 3.88;
SET @iSingleCommodityID2_CategoryParent3 = 0;
SET @dPrice_SingleCommodity2_CategoryParent3 = 3.88;
SET @iCompositionCommodityID_CategoryParent3 = 0;
SET @dPrice_CompositionCommodity_CategoryParent3 = 23.28;
SET @iMultiPackagingCommodityID_CategoryParent3 = 0;
SET @iRefCommodityMultiple_CategoryParent3 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent3 = 19.4;
SET @iServiceCommodityID_CategoryParent3 = 0;
SET @dPrice_ServiceCommodity_CategoryParent3 = 5;
-- �����������С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP3', @iCategoryParentID3, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent3 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����ţ��_CategoryParent3', '����ţ��', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent3, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent3 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '��ţţ��_CategoryParent3', '��ţţ��', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent3, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent3 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'ţ�̴����_CategoryParent3', 'ţ��', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent3, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent3 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent3, @iSingleCommodityID_CategoryParent3, 3, @dPrice_SingleCommodity_CategoryParent3);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent3, @iSingleCommodityID2_CategoryParent3, 3, @dPrice_SingleCommodity2_CategoryParent3);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ������ţ��_CategoryParent3', '����ţ��', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent3, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @dPrice_SingleCommodity_CategoryParent3, @iRefCommodityMultiple_CategoryParent3, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent3 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent3','kd','��',4,NULL,4,@iCategoryID_CategoryParent3,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���C',3);
SET @iServiceCommodityID_CategoryParent3 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test6 ='2099/06/06 08:08:08';
SET @iNO_SingleCommodity_Test6 = 2;
SET @iNO_SingleCommodity2_Test6 = 1;
SET @iNO_CompositionCommodity_Test6 = 1;
SET @iNO_MultiPackagingCommodity_Test6 = 1;
SET @iNO_ServiceCommodity_Test6 = 1;
SET @iRetailTradeID1_Test6 = 0;
SET @iRetailTradeID2_Test6 = 0;
SET @iResultVerification_Test6 = 0;
-- �������۵�1 (����2����ͨ��Ʒ2��1��������Ʒ����12.76Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000001', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test6, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test6, 12.76, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_Test6 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test6, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_Test6, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_Test6, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_Test6, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_Test6, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_Test6, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ1��1�������Ʒ��1�����װ��Ʒ����46.56Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000002', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test6, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test6, 46.56, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_Test6 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_Test6, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_Test6, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_Test6, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_Test6, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_Test6, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_Test6, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_Test6, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test6, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_Test6 = (@iNO_SingleCommodity2_Test6 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_ServiceCommodity_Test6 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_Test6 = (@iNO_SingleCommodity_Test6 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_CompositionCommodity_Test6 * @dPrice_CompositionCommodity_CategoryParent3) -- 
											+ (@iNO_MultiPackagingCommodity_Test6 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_Test6 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_Test6 + @dRetailTradeID2_TotalAmount_Test6 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test6;
SELECT IF(@iResultVerification_Test6 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case6 Result';


SELECT '-------------------- Case7:����7��û��������Ʒ(���˻�) -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test7 ='2099/06/07 08:08:08';
SET @iResultVerification_Test7 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test7, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test7,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test7 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test7 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�������۱���Ϊ0' ,'���Գɹ�','����ʧ��') AS 'Test Case7 Result';



SELECT '-------------------- Case8:����8��û��������Ʒ(���˻�) -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test8 ='2099/06/08 08:08:08';
SET @iResultVerification_Test8 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test8, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test8,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test8 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test8 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�������۱���Ϊ0' ,'���Գɹ�','����ʧ��') AS 'Test Case8 Result';



SELECT '-------------------- Case9:����9��û��������Ʒ(���˻�) -------------------------' AS 'Case9';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test9 ='2099/06/09 08:08:08';
SET @iResultVerification_Test9 = 0;
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test9, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test9,'%Y-%m-%d'); 
SELECT 1 INTO @iResultVerification_Test9 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = NULL AND F_TotalAmount = NULL AND F_Datetime = NULL; -- 
SELECT IF(found_rows() = 0 AND @iResultVerification_Test9 = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�������۱���Ϊ0' ,'���Գɹ�','����ʧ��') AS 'Test Case9 Result';




SELECT '-------------------- Case10:����10�����۷ֱ����һ�����������������Ʒ(���˻�) -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test10 ='2099/06/10 08:08:08';
-- �������һ����Ʒ
SET @iNO_SingleCommodity_CP1_Test10 = 9;
SET @iNO_SingleCommodity2_CP1_Test10 = 1;
SET @iNO_CompositionCommodity_CP1_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test10 = 1;
SET @iNO_ServiceCommodity_CP1_Test10 = 1;
SET @iRetailTradeID1_CP1_Test10 = 0;
SET @iRetailTradeID2_CP1_Test10 = 0;
SET @iResultVerification_CP1_Test10 = 0;
-- �������۵�1 (����9����ͨ��Ʒ1��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000001', 7, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test10 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test10, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test10, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test10, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test10, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test10, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test10, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000002', 7, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test10 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test10, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test10, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test10, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test10, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test10, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test10, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test10, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test10 = 5;
SET @iNO_SingleCommodity2_CP2_Test10 = 3;
SET @iNO_CompositionCommodity_CP2_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test10 = 1;
SET @iNO_ServiceCommodity_CP2_Test10 = 1;
SET @iRetailTradeID1_CP2_Test10 = 0;
SET @iRetailTradeID2_CP2_Test10 = 0;
SET @iResultVerification_CP2_Test10 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000003', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test10 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test10, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test10, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test10, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test10, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test10, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test10, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����69.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000004', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test10 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test10, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test10, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test10, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test10, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test10, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test10, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test10, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test10 = 5;
SET @iNO_SingleCommodity2_CP3_Test10 = 3;
SET @iNO_CompositionCommodity_CP3_Test10 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test10 = 1;
SET @iNO_ServiceCommodity_CP3_Test10 = 1;
SET @iRetailTradeID1_CP3_Test10 = 0;
SET @iRetailTradeID2_CP3_Test10 = 0;
SET @iResultVerification_CP3_Test10 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000005', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test10 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test10, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test10, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test10, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test10, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test10, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test10, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000006', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test10, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test10, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test10 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test10, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test10, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test10, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test10, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test10, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test10, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test10, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test10, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test10 = (@iNO_SingleCommodity_CP1_Test10 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test10 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test10 = (@iNO_SingleCommodity2_CP1_Test10 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test10 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test10 + @dRetailTradeID2_TotalAmount_CP1_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test10 = (@iNO_SingleCommodity_CP2_Test10 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test10 = (@iNO_SingleCommodity2_CP2_Test10 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test10 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test10 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test10 +  @dRetailTradeID2_TotalAmount_CP2_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test10 = (@iNO_SingleCommodity_CP3_Test10 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test10 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test10 = (@iNO_SingleCommodity2_CP3_Test10 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test10 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
												+ (@iNO_CompositionCommodity_CP3_Test10 * @dPrice_CompositionCommodity_CategoryParent3);  
SELECT 1 INTO @iResultVerification_CP3_Test10 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test10 + @dRetailTradeID2_TotalAmount_CP3_Test10 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test10, @iResultVerification_CP2_Test10, @iResultVerification_CP3_Test10;	 
SELECT IF(@iResultVerification_CP1_Test10 = 1 AND @iResultVerification_CP2_Test10 = 1 AND @iResultVerification_CP3_Test10 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case10 Result';


SELECT '-------------------- Case11:����11�����۷ֱ�����ġ����������������Ʒ(���˻�) -------------------------' AS 'Case11';
-- ��������ĵ���Ʒ����
SET @iCategoryParentID4 = 6; 
SET @iCategoryID_CategoryParent4 = 0;
SET @iSingleCommodityID_CategoryParent4 = 0;
SET @dPrice_SingleCommodity_CategoryParent4 = 35.9;
SET @iSingleCommodityID2_CategoryParent4 = 0;
SET @dPrice_SingleCommodity2_CategoryParent4 = 52;
SET @iCompositionCommodityID_CategoryParent4 = 0;
SET @dPrice_CompositionCommodity_CategoryParent4 = 175.8;
SET @iMultiPackagingCommodityID_CategoryParent4 = 0;
SET @iRefCommodityMultiple_CategoryParent4 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent4 = 104;
SET @iServiceCommodityID_CategoryParent4 = 0;
SET @dPrice_ServiceCommodity_CategoryParent4 = 5;
-- ��������ĵ�С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP4', @iCategoryParentID4, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent4 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '��������_CategoryParent4', '��������', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent4, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent4 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '������_CategoryParent4', '������', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent4, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent4 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�������_CategoryParent4', '���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent4, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent4 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent4, @iSingleCommodityID_CategoryParent4, 2, @dPrice_SingleCommodity_CategoryParent4);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent4, @iSingleCommodityID2_CategoryParent4, 2, @dPrice_SingleCommodity2_CategoryParent4);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ��������_CategoryParent4', '������', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent4, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID2_CategoryParent4, @iRefCommodityMultiple_CategoryParent4, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent4 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent4','kd','��',4,NULL,4,@iCategoryID_CategoryParent4,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���D',3);
SET @iServiceCommodityID_CategoryParent4 = last_insert_id();
-- ������������Ʒ���� 
SET @iCategoryParentID5 = 7; 
SET @iCategoryID_CategoryParent5 = 0;
SET @iSingleCommodityID_CategoryParent5 = 0;
SET @dPrice_SingleCommodity_CategoryParent5 = 99;
SET @iSingleCommodityID2_CategoryParent5 = 0;
SET @dPrice_SingleCommodity2_CategoryParent5 = 189;
SET @iCompositionCommodityID_CategoryParent5 = 0;
SET @dPrice_CompositionCommodity_CategoryParent5 = 576;
SET @iMultiPackagingCommodityID_CategoryParent5 = 0;
SET @iRefCommodityMultiple_CategoryParent5 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent5 = 378;
SET @iServiceCommodityID_CategoryParent5 = 0;
SET @dPrice_ServiceCommodity_CategoryParent5 = 5;
-- ����������С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP5', @iCategoryParentID5, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent5 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����������Ĥ_CategoryParent5', '����������Ĥ', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent5, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent5 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'ѩ���鼡��ˮ_CategoryParent5', 'ѩ���鼡��ˮ', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent5, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent5 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '���������_CategoryParent5', '����', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent5, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent5 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent5, @iSingleCommodityID_CategoryParent5, 2, @dPrice_SingleCommodity_CategoryParent5);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent5, @iSingleCommodityID2_CategoryParent5, 2, @dPrice_SingleCommodity2_CategoryParent5);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ������������Ĥ_CategoryParent5', '����������Ĥ', '��', 1, '��', 3, @iCategoryID_CategoryParent5, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID2_CategoryParent5, @iRefCommodityMultiple_CategoryParent5, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent5 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent5','kd','��',4,NULL,4,@iCategoryID_CategoryParent5,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���E',3);
SET @iServiceCommodityID_CategoryParent5 = last_insert_id();
-- �������������Ʒ���� 
SET @iCategoryParentID6 = 8; 
SET @iCategoryID_CategoryParent6 = 0;
SET @iSingleCommodityID_CategoryParent6 = 0;
SET @dPrice_SingleCommodity_CategoryParent6 = 5.5;
SET @iSingleCommodityID2_CategoryParent6 = 0;
SET @dPrice_SingleCommodity2_CategoryParent6 = 5.5;
SET @iCompositionCommodityID_CategoryParent6 = 0;
SET @dPrice_CompositionCommodity_CategoryParent6 = 33;
SET @iMultiPackagingCommodityID_CategoryParent6 = 0;
SET @iRefCommodityMultiple_CategoryParent6 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent6 = 27.5;
SET @iServiceCommodityID_CategoryParent6 = 0;
SET @dPrice_ServiceCommodity_CategoryParent6 = 5;
-- �����������С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP6', @iCategoryParentID6, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent6 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '��̳���ţ����_CategoryParent6', '��̳���ţ����', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent6, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent6 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����ţ����_CategoryParent6', '����ţ����', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent6, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent6 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'Ͱ������_CategoryParent6', 'Ͱ��', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent6, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent6 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent6, @iSingleCommodityID_CategoryParent6, 3, @dPrice_SingleCommodity_CategoryParent6);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent6, @iSingleCommodityID2_CategoryParent6, 3, @dPrice_SingleCommodity2_CategoryParent6);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ����̳���ţ����_CategoryParent6', '��̳���ţ����', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent6, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @dPrice_SingleCommodity_CategoryParent6, @iRefCommodityMultiple_CategoryParent6, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent6 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent6','kd','��',4,NULL,4,@iCategoryID_CategoryParent6,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���F',3);
SET @iServiceCommodityID_CategoryParent6 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test11 ='2099/06/11 08:08:08';
-- ��������ĵ���Ʒ
SET @iNO_SingleCommodity_CP4_Test11 = 2;
SET @iNO_SingleCommodity2_CP4_Test11 = 3;
SET @iNO_CompositionCommodity_CP4_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP4_Test11 = 1;
SET @iNO_ServiceCommodity_CP4_Test11 = 1;
SET @iRetailTradeID1_CP4_Test11 = 0;
SET @iRetailTradeID2_CP4_Test11 = 0;
SET @iResultVerification_CP4_Test11 = 0;
-- �������۵�1 (����2����ͨ��Ʒ1��1�������Ʒ����247.6Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000001', 8, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP4_Test11 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test11, @iSingleCommodityID_CategoryParent4, '��������_CategoryParent4', 1, @iNO_SingleCommodity_CP4_Test11, @dPrice_SingleCommodity_CategoryParent4, @iNO_SingleCommodity_CP4_Test11, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test11, @iCompositionCommodityID_CategoryParent4, '�������_CategoryParent4', 1, @iNO_CompositionCommodity_CP4_Test11, @dPrice_CompositionCommodity_CategoryParent4, @iNO_CompositionCommodity_CP4_Test11, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����265Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000002', 8, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 265, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP4_Test11 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iSingleCommodityID2_CategoryParent4, '������_CategoryParent4', 1, @iNO_SingleCommodity2_CP4_Test11, @dPrice_SingleCommodity2_CategoryParent4, @iNO_SingleCommodity2_CP4_Test11, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iServiceCommodityID_CategoryParent4, '���_CategoryParent4', 1, @iNO_ServiceCommodity_CP4_Test11, @dPrice_ServiceCommodity_CategoryParent4, @iNO_ServiceCommodity_CP4_Test11, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test11, @iMultiPackagingCommodityID_CategoryParent4, 'һ��������_CategoryParent4', 1, @iNO_MultiPackagingCommodity_CP4_Test11, @dPrice_MultiPackagingCommodity_CategoryParent4, @iNO_MultiPackagingCommodity_CP4_Test11, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP5_Test11 = 3;
SET @iNO_SingleCommodity2_CP5_Test11 = 4;
SET @iNO_CompositionCommodity_CP5_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP5_Test11 = 1;
SET @iNO_ServiceCommodity_CP5_Test11 = 1;
SET @iRetailTradeID1_CP5_Test11 = 0;
SET @iRetailTradeID2_CP5_Test11 = 0;
SET @iResultVerification_CP5_Test11 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��1�����װ��Ʒ����675Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000003', 8, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 675, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP5_Test11 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP5_Test11, @iSingleCommodityID_CategoryParent5, '����������Ĥ_CategoryParent5', 1, @iNO_SingleCommodity_CP5_Test11, @dPrice_SingleCommodity_CategoryParent5, @iNO_SingleCommodity_CP5_Test11, @dPrice_SingleCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP5_Test11, @iMultiPackagingCommodityID_CategoryParent5, 'һ������������Ĥ_CategoryParent5', 1, @iNO_MultiPackagingCommodity_CP5_Test11, @dPrice_MultiPackagingCommodity_CategoryParent5, @iNO_MultiPackagingCommodity_CP5_Test11, @dPrice_MultiPackagingCommodity_CategoryParent5, NULL, NULL);
-- �������۵�1 (����4����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����1337Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000004', 8, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 1337, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP5_Test11 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iSingleCommodityID2_CategoryParent5, 'ѩ���鼡��ˮ_CategoryParent5', 1, @iNO_SingleCommodity2_CP5_Test11, @dPrice_SingleCommodity2_CategoryParent5, @iNO_SingleCommodity2_CP5_Test11, @dPrice_SingleCommodity2_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iServiceCommodityID_CategoryParent5, '���_CategoryParent5', 1, @iNO_ServiceCommodity_CP5_Test11, @dPrice_ServiceCommodity_CategoryParent5, @iNO_ServiceCommodity_CP5_Test11, @dPrice_ServiceCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP5_Test11, @iCompositionCommodityID_CategoryParent5, '���������_CategoryParent5', 1, @iNO_CompositionCommodity_CP5_Test11, @dPrice_CompositionCommodity_CategoryParent5, @iNO_CompositionCommodity_CP5_Test11, @dPrice_CompositionCommodity_CategoryParent5, NULL, NULL);
-- �������������Ʒ
SET @iNO_SingleCommodity_CP6_Test11 = 3;
SET @iNO_SingleCommodity2_CP6_Test11 = 5;
SET @iNO_CompositionCommodity_CP6_Test11 = 1;
SET @iNO_MultiPackagingCommodity_CP6_Test11 = 1;
SET @iNO_ServiceCommodity_CP6_Test11 = 1;
SET @iRetailTradeID1_CP6_Test11 = 0;
SET @iRetailTradeID2_CP6_Test11 = 0;
SET @iResultVerification_CP6_Test11 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��1��������Ʒ����21.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000005', 8, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 21.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP6_Test11 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP6_Test11, @iSingleCommodityID_CategoryParent6, '��̳���ţ����_CategoryParent6', 1, @iNO_SingleCommodity_CP6_Test11, @dPrice_SingleCommodity_CategoryParent6, @iNO_SingleCommodity_CP6_Test11, @dPrice_SingleCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP6_Test11, @iServiceCommodityID_CategoryParent6, '���_CategoryParent6', 1, @iNO_ServiceCommodity_CP6_Test11, @dPrice_ServiceCommodity_CategoryParent6, @iNO_ServiceCommodity_CP6_Test11, @dPrice_ServiceCommodity_CategoryParent6, NULL, NULL);
-- �������۵�2 (����5����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����87.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000006', 8, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test11, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test11, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP6_Test11 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iSingleCommodityID2_CategoryParent6, '����ţ����_CategoryParent6', 1, @iNO_SingleCommodity2_CP6_Test11, @dPrice_SingleCommodity2_CategoryParent6, @iNO_SingleCommodity2_CP6_Test11, @dPrice_SingleCommodity2_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iCompositionCommodityID_CategoryParent6, 'Ͱ������_CategoryParent6', 1, @iNO_CompositionCommodity_CP6_Test11, @dPrice_CompositionCommodity_CategoryParent6, @iNO_CompositionCommodity_CP6_Test11, @dPrice_CompositionCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP6_Test11, @iMultiPackagingCommodityID_CategoryParent6, 'һ����̳���ţ����_CategoryParent6', 1, @iNO_MultiPackagingCommodity_CP6_Test11, @dPrice_MultiPackagingCommodity_CategoryParent6, @iNO_MultiPackagingCommodity_CP6_Test11, @dPrice_MultiPackagingCommodity_CategoryParent6, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test11, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP4_Test11 = (@iNO_SingleCommodity_CP4_Test11 * @dPrice_SingleCommodity_CategoryParent4) + (@iNO_CompositionCommodity_CP4_Test11 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dRetailTradeID2_TotalAmount_CP4_Test11 = (@iNO_SingleCommodity2_CP4_Test11 * @dPrice_SingleCommodity2_CategoryParent4) + (@iNO_MultiPackagingCommodity_CP4_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent4) -- 
											   + (@iNO_ServiceCommodity_CP4_Test11 * @dPrice_ServiceCommodity_CategoryParent4);	
SELECT 1 INTO @iResultVerification_CP4_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP4_Test11 + @dRetailTradeID2_TotalAmount_CP4_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP5_Test11 = (@iNO_SingleCommodity_CP5_Test11 * @dPrice_SingleCommodity_CategoryParent5) + (@iNO_MultiPackagingCommodity_CP5_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent5);
SET @dRetailTradeID2_TotalAmount_CP5_Test11 = (@iNO_ServiceCommodity_CP5_Test11 * @dPrice_ServiceCommodity_CategoryParent5) + (@iNO_SingleCommodity2_CP5_Test11 * @dPrice_SingleCommodity2_CategoryParent5) -- 
												+ (@iNO_CompositionCommodity_CP5_Test11 * @dPrice_CompositionCommodity_CategoryParent5);
SELECT 1 INTO @iResultVerification_CP5_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID5 --  
		AND F_TotalAmount =  @dRetailTradeID1_TotalAmount_CP5_Test11 + @dRetailTradeID2_TotalAmount_CP5_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP6_Test11 = (@iNO_SingleCommodity_CP6_Test11 * @dPrice_SingleCommodity_CategoryParent6) + (@iNO_ServiceCommodity_CP6_Test11 * @dPrice_ServiceCommodity_CategoryParent6);
SET @dRetailTradeID2_TotalAmount_CP6_Test11 = (@iNO_SingleCommodity2_CP6_Test11 * @dPrice_SingleCommodity2_CategoryParent6) + (@iNO_CompositionCommodity_CP6_Test11 * @dPrice_CompositionCommodity_CategoryParent6) -- 
												+ (@iNO_MultiPackagingCommodity_CP6_Test11 * @dPrice_MultiPackagingCommodity_CategoryParent6);
SELECT 1 INTO @iResultVerification_CP6_Test11 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID6 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP6_Test11 + @dRetailTradeID2_TotalAmount_CP6_Test11 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP4_Test11, @iResultVerification_CP5_Test11, @iResultVerification_CP6_Test11;		 
SELECT IF(@iResultVerification_CP4_Test11 = 1 AND @iResultVerification_CP5_Test11 = 1 AND @iResultVerification_CP6_Test11 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case11 Result';



SELECT '-------------------- Case12:����12�����۷ֱ�����ߡ����˺����ŵ���Ʒ(���˻�) -------------------------' AS 'Case12';
-- ��������ߵ���Ʒ����
SET @iCategoryParentID7 = 9; 
SET @iCategoryID_CategoryParent7 = 0;
SET @iSingleCommodityID_CategoryParent7 = 0;
SET @dPrice_SingleCommodity_CategoryParent7 = 3;
SET @iSingleCommodityID2_CategoryParent7 = 0;
SET @dPrice_SingleCommodity2_CategoryParent7 = 3;
SET @iCompositionCommodityID_CategoryParent7 = 0;
SET @dPrice_CompositionCommodity_CategoryParent7 = 60;
SET @iMultiPackagingCommodityID_CategoryParent7 = 0;
SET @iRefCommodityMultiple_CategoryParent7 = 5;
SET @dPrice_MultiPackagingCommodity_CategoryParent7 = 15;
SET @iServiceCommodityID_CategoryParent7 = 0;
SET @dPrice_ServiceCommodity_CategoryParent7 = 5;
-- ��������ߵ�С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP7', @iCategoryParentID7, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent7 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����Բ���_CategoryParent7', '����Բ���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent7, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent7 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '���Բ���_CategoryParent7', '���Բ���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent7, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent7 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ľߴ����_CategoryParent7', '���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent7, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent7 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent7, @iSingleCommodityID_CategoryParent7, 10, @dPrice_SingleCommodity_CategoryParent7);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent7, @iSingleCommodityID2_CategoryParent7, 10, @dPrice_SingleCommodity2_CategoryParent7);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ�г���Բ���_CategoryParent7', '����Բ���', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent7, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID2_CategoryParent7, @iRefCommodityMultiple_CategoryParent7, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent7 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent7','kd','��',4,NULL,4,@iCategoryID_CategoryParent7,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���G',3);
SET @iServiceCommodityID_CategoryParent7 = last_insert_id();
-- �������˵���Ʒ���� 
SET @iCategoryParentID8 = 10; 
SET @iCategoryID_CategoryParent8 = 0;
SET @iSingleCommodityID_CategoryParent8 = 0;
SET @dPrice_SingleCommodity_CategoryParent8 = 199;
SET @iSingleCommodityID2_CategoryParent8 = 0;
SET @dPrice_SingleCommodity2_CategoryParent8 = 39;
SET @iCompositionCommodityID_CategoryParent8 = 0;
SET @dPrice_CompositionCommodity_CategoryParent8 = 238;
SET @iMultiPackagingCommodityID_CategoryParent8 = 0;
SET @iRefCommodityMultiple_CategoryParent8 = 3;
SET @dPrice_MultiPackagingCommodity_CategoryParent8 = 117;
SET @iServiceCommodityID_CategoryParent8 = 0;
SET @dPrice_ServiceCommodity_CategoryParent8 = 5;
-- �������˵�С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP8', @iCategoryParentID8, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent8 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�綯��ˢ_CategoryParent8', '�綯��ˢ', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent8, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent8 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '���ϰ�ҩ����_CategoryParent8', '���ϰ�ҩ����', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent8, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent8 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�����ִ����_CategoryParent8', '��', 'ƿ', 1, '��', 3, @iCategoryID_CategoryParent8, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent8 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent8, @iSingleCommodityID_CategoryParent8, 1, @dPrice_SingleCommodity_CategoryParent8);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent8, @iSingleCommodityID2_CategoryParent8, 1, @dPrice_SingleCommodity2_CategoryParent8);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '��ֻ���ϰ�ҩ_CategoryParent8', '���ϰ�ҩ', '��', 1, '��', 3, @iCategoryID_CategoryParent8, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @iSingleCommodityID2_CategoryParent8, @iRefCommodityMultiple_CategoryParent8, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent8 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent8','kd','��',4,NULL,4,@iCategoryID_CategoryParent8,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���I',3);
SET @iServiceCommodityID_CategoryParent8 = last_insert_id();
-- �������ŵ���Ʒ���� 
SET @iCategoryParentID9 = 11; 
SET @iCategoryID_CategoryParent9 = 0;
SET @iSingleCommodityID_CategoryParent9 = 0;
SET @dPrice_SingleCommodity_CategoryParent9 = 4.5;
SET @iSingleCommodityID2_CategoryParent9 = 0;
SET @dPrice_SingleCommodity2_CategoryParent9 = 3.5;
SET @iCompositionCommodityID_CategoryParent9 = 0;
SET @dPrice_CompositionCommodity_CategoryParent9 = 8;
SET @iMultiPackagingCommodityID_CategoryParent9 = 0;
SET @iRefCommodityMultiple_CategoryParent9 = 2;
SET @dPrice_MultiPackagingCommodity_CategoryParent9 = 9;
SET @iServiceCommodityID_CategoryParent9 = 0;
SET @dPrice_ServiceCommodity_CategoryParent9 = 5;
-- �������ŵ�С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CategoryP9', @iCategoryParentID9, '10/24/2098 17:00:00', '10/24/2098 17:00:00');
SET @iCategoryID_CategoryParent9 = last_insert_id();
-- ������ͨ��Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '������_CategoryParent9', '������', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent9, 'KKKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID_CategoryParent9 = last_insert_id();
-- ������ͨ��Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '����_CategoryParent9', '����', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent9, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 0, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iSingleCommodityID2_CategoryParent9 = last_insert_id();
-- ���������Ʒ(����Ʒ�ֱ�����ͨ��Ʒ1����ͨ��Ʒ2)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�߲˴����_CategoryParent9', '�߲�', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent9, 'KL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, 0, 0, '1111111', 1, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iCompositionCommodityID_CategoryParent9 = last_insert_id();
-- ����Ʒ1
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent9, @iSingleCommodityID_CategoryParent9, 1, @dPrice_SingleCommodity_CategoryParent9);
-- ����Ʒ2
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCompositionCommodityID_CategoryParent9, @iSingleCommodityID2_CategoryParent9, 1, @dPrice_SingleCommodity2_CategoryParent9);
-- �������װ��Ʒ(�ο���ͨ��Ʒ1)
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, 
F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, 'һ������_CategoryParent9', '����', 'Ͱ', 1, '��', 3, @iCategoryID_CategoryParent9, 'BSKL', 
1, 4.5, 2, 1, 1, NULL, 3, 30, 
'10/24/2098 17:00:00 ����', 20, @dPrice_SingleCommodity_CategoryParent9, @iRefCommodityMultiple_CategoryParent9, '1111111', 2, NULL, 
'10/24/2098 17:00:00 ����', '10/24/2098 17:00:00 ����', NULL, NULL, NULL, NULL);
SET @iMultiPackagingCommodityID_CategoryParent9 = last_insert_id();
-- ����������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'���_CategoryParent9','kd','��',4,NULL,4,@iCategoryID_CategoryParent9,'SP',1,
4,3,1,1,null,
0,30,'10/24/2098 17:00:00 ����','20',0,0,'���J',3);
SET @iServiceCommodityID_CategoryParent9 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test12 ='2099/06/12 08:08:08';
-- ��������ߵ���Ʒ
SET @iNO_SingleCommodity_CP7_Test12 = 2;
SET @iNO_SingleCommodity2_CP7_Test12 = 3;
SET @iNO_CompositionCommodity_CP7_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP7_Test12 = 1;
SET @iNO_ServiceCommodity_CP7_Test12 = 1;
SET @iRetailTradeID1_CP7_Test12 = 0;
SET @iRetailTradeID2_CP7_Test12 = 0;
SET @iResultVerification_CP7_Test12 = 0;
-- �������۵�1 (����2����ͨ��Ʒ1��1�������Ʒ����66Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 66, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP7_Test12 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP7_Test12, @iSingleCommodityID_CategoryParent7, '����Բ���_CategoryParent7', 1, @iNO_SingleCommodity_CP7_Test12, @dPrice_SingleCommodity_CategoryParent7, @iNO_SingleCommodity_CP7_Test12, @dPrice_SingleCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP7_Test12, @iCompositionCommodityID_CategoryParent7, '�ľߴ����_CategoryParent7', 1, @iNO_CompositionCommodity_CP7_Test12, @dPrice_CompositionCommodity_CategoryParent7, @iNO_CompositionCommodity_CP7_Test12, @dPrice_CompositionCommodity_CategoryParent7, NULL, NULL);
-- �������۵�1 (����3����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����29Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 29, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP7_Test12 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iSingleCommodityID2_CategoryParent7, '���Բ���_CategoryParent7', 1, @iNO_SingleCommodity2_CP7_Test12, @dPrice_SingleCommodity2_CategoryParent7, @iNO_SingleCommodity2_CP7_Test12, @dPrice_SingleCommodity2_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iServiceCommodityID_CategoryParent7, '���_CategoryParent7', 1, @iNO_ServiceCommodity_CP7_Test12, @dPrice_ServiceCommodity_CategoryParent7, @iNO_ServiceCommodity_CP7_Test12, @dPrice_ServiceCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP7_Test12, @iMultiPackagingCommodityID_CategoryParent7, 'һ�г���Բ���_CategoryParent7', 1, @iNO_MultiPackagingCommodity_CP7_Test12, @dPrice_MultiPackagingCommodity_CategoryParent7, @iNO_MultiPackagingCommodity_CP7_Test12, @dPrice_MultiPackagingCommodity_CategoryParent7, NULL, NULL);
-- �������˵���Ʒ
SET @iNO_SingleCommodity_CP8_Test12 = 3;
SET @iNO_SingleCommodity2_CP8_Test12 = 4;
SET @iNO_CompositionCommodity_CP8_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP8_Test12 = 1;
SET @iNO_ServiceCommodity_CP8_Test12 = 1;
SET @iRetailTradeID1_CP8_Test12 = 0;
SET @iRetailTradeID2_CP8_Test12 = 0;
SET @iResultVerification_CP8_Test12 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��1�����װ��Ʒ����714Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000003', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 714, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP8_Test12 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP8_Test12, @iSingleCommodityID_CategoryParent8, '�綯��ˢ_CategoryParent8', 1, @iNO_SingleCommodity_CP8_Test12, @dPrice_SingleCommodity_CategoryParent8, @iNO_SingleCommodity_CP8_Test12, @dPrice_SingleCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP8_Test12, @iMultiPackagingCommodityID_CategoryParent8, '��ֻ���ϰ�ҩ_CategoryParent8', 1, @iNO_MultiPackagingCommodity_CP8_Test12, @dPrice_MultiPackagingCommodity_CategoryParent8, @iNO_MultiPackagingCommodity_CP8_Test12, @dPrice_MultiPackagingCommodity_CategoryParent8, NULL, NULL);
-- �������۵�1 (����4����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����399Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000004', 1, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 399, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP8_Test12 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iSingleCommodityID2_CategoryParent8, '�ϰ�ҩ����_CategoryParent8', 1, @iNO_SingleCommodity2_CP8_Test12, @dPrice_SingleCommodity2_CategoryParent8, @iNO_SingleCommodity2_CP8_Test12, @dPrice_SingleCommodity2_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iServiceCommodityID_CategoryParent8, '���_CategoryParent8', 1, @iNO_ServiceCommodity_CP8_Test12, @dPrice_ServiceCommodity_CategoryParent8, @iNO_ServiceCommodity_CP8_Test12, @dPrice_ServiceCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP8_Test12, @iCompositionCommodityID_CategoryParent8, '�����ִ����_CategoryParent8', 1, @iNO_CompositionCommodity_CP8_Test12, @dPrice_CompositionCommodity_CategoryParent8, @iNO_CompositionCommodity_CP8_Test12, @dPrice_CompositionCommodity_CategoryParent8, NULL, NULL);
-- �������ŵ���Ʒ
SET @iNO_SingleCommodity_CP9_Test12 = 3;
SET @iNO_SingleCommodity2_CP9_Test12 = 5;
SET @iNO_CompositionCommodity_CP9_Test12 = 1;
SET @iNO_MultiPackagingCommodity_CP9_Test12 = 1;
SET @iNO_ServiceCommodity_CP9_Test12 = 1;
SET @iRetailTradeID1_CP9_Test12 = 0;
SET @iRetailTradeID2_CP9_Test12 = 0;
SET @iResultVerification_CP9_Test12 = 0;
-- �������۵�1 (����3����ͨ��Ʒ1��1��������Ʒ����18.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000005', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 18.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP9_Test12 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP9_Test12, @iSingleCommodityID_CategoryParent9, '������_CategoryParent9', 1, @iNO_SingleCommodity_CP9_Test12, @dPrice_SingleCommodity_CategoryParent9, @iNO_SingleCommodity_CP9_Test12, @dPrice_SingleCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP9_Test12, @iServiceCommodityID_CategoryParent9, '���_CategoryParent9', 1, @iNO_ServiceCommodity_CP9_Test12, @dPrice_ServiceCommodity_CategoryParent9, @iNO_ServiceCommodity_CP9_Test12, @dPrice_ServiceCommodity_CategoryParent9, NULL, NULL);
-- �������۵�2 (����5����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����34.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000006', 1, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test12, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test12, 34.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP9_Test12 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iSingleCommodityID2_CategoryParent9, '����_CategoryParent9', 1, @iNO_SingleCommodity2_CP9_Test12, @dPrice_SingleCommodity2_CategoryParent9, @iNO_SingleCommodity2_CP9_Test12, @dPrice_SingleCommodity2_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iCompositionCommodityID_CategoryParent9, '�߲˴����_CategoryParent9', 1, @iNO_CompositionCommodity_CP9_Test12, @dPrice_CompositionCommodity_CategoryParent9, @iNO_CompositionCommodity_CP9_Test12, @dPrice_CompositionCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP9_Test12, @iMultiPackagingCommodityID_CategoryParent9, 'һ������_CategoryParent9', 1, @iNO_MultiPackagingCommodity_CP9_Test12, @dPrice_MultiPackagingCommodity_CategoryParent9, @iNO_MultiPackagingCommodity_CP9_Test12, @dPrice_MultiPackagingCommodity_CategoryParent9, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test12, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP7_Test12 = (@iNO_SingleCommodity_CP7_Test12 * @dPrice_SingleCommodity_CategoryParent7) + (@iNO_CompositionCommodity_CP7_Test12 * @dPrice_CompositionCommodity_CategoryParent7);
SET @dRetailTradeID2_TotalAmount_CP7_Test12 = (@iNO_SingleCommodity2_CP7_Test12 * @dPrice_SingleCommodity2_CategoryParent7) + (@iNO_MultiPackagingCommodity_CP7_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent7) -- 
												+ (@iNO_ServiceCommodity_CP7_Test12 * @dPrice_ServiceCommodity_CategoryParent7);
SELECT 1 INTO @iResultVerification_CP7_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID7 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP7_Test12 + @dRetailTradeID2_TotalAmount_CP7_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP8_Test12 = (@iNO_SingleCommodity_CP8_Test12 * @dPrice_SingleCommodity_CategoryParent8) + (@iNO_MultiPackagingCommodity_CP8_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent8);
SET @dRetailTradeID2_TotalAmount_CP8_Test12 = (@iNO_ServiceCommodity_CP8_Test12 * @dPrice_ServiceCommodity_CategoryParent8) + (@iNO_SingleCommodity2_CP8_Test12 * @dPrice_SingleCommodity2_CategoryParent8) -- 
												+ (@iNO_CompositionCommodity_CP8_Test12 * @dPrice_CompositionCommodity_CategoryParent8);
SELECT 1 INTO @iResultVerification_CP8_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID8 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP8_Test12 + @dRetailTradeID2_TotalAmount_CP8_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP9_Test12 = (@iNO_SingleCommodity_CP9_Test12 * @dPrice_SingleCommodity_CategoryParent9) + (@iNO_ServiceCommodity_CP9_Test12 * @dPrice_ServiceCommodity_CategoryParent9);
SET @dRetailTradeID2_TotalAmount_CP9_Test12 = (@iNO_SingleCommodity2_CP9_Test12 * @dPrice_SingleCommodity2_CategoryParent9) + (@iNO_CompositionCommodity_CP9_Test12 * @dPrice_CompositionCommodity_CategoryParent9) -- 
												+ (@iNO_MultiPackagingCommodity_CP9_Test12 * @dPrice_MultiPackagingCommodity_CategoryParent9);
SELECT 1 INTO @iResultVerification_CP9_Test12 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID9 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP9_Test12 +  @dRetailTradeID2_TotalAmount_CP9_Test12 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP7_Test12, @iResultVerification_CP8_Test12, @iResultVerification_CP9_Test12;		 
SELECT IF(@iResultVerification_CP7_Test12 = 1 AND @iResultVerification_CP8_Test12 = 1 AND @iResultVerification_CP9_Test12 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case12 Result';




SELECT '-------------------- Case13:����13�����۷ֱ����һ�����������������Ʒ(���˻�) -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test13 ='2099/06/13 08:08:08';
-- �������һ����Ʒ
SET @iNO_SingleCommodity_CP1_Test13 = 9;
SET @iNO_SingleCommodity2_CP1_Test13 = 1;
SET @iNO_CompositionCommodity_CP1_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test13 = 1;
SET @iNO_ServiceCommodity_CP1_Test13 = 1;
SET @iRetailTradeID1_CP1_Test13 = 0;
SET @iRetailTradeID2_CP1_Test13 = 0;
SET @iResultVerification_CP1_Test13 = 0;
-- �������۵�1 (����9����ͨ��Ʒ1��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000001', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test13 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test13, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test13, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test13, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test13, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test13, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test13, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000002', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test13 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test13, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test13, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test13, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test13, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test13, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test13, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test13, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test13 = 5;
SET @iNO_SingleCommodity2_CP2_Test13 = 3;
SET @iNO_CompositionCommodity_CP2_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test13 = 1;
SET @iNO_ServiceCommodity_CP2_Test13 = 1;
SET @iRetailTradeID1_CP2_Test13 = 0;
SET @iRetailTradeID2_CP2_Test13 = 0;
SET @iResultVerification_CP2_Test13 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000003', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test13 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test13, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test13, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test13, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test13, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test13, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test13, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����69.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000004', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test13 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test13, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test13, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test13, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test13, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test13, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test13, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test13, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test13 = 5;
SET @iNO_SingleCommodity2_CP3_Test13 = 3;
SET @iNO_CompositionCommodity_CP3_Test13 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test13 = 1;
SET @iNO_ServiceCommodity_CP3_Test13 = 1;
SET @iRetailTradeID1_CP3_Test13 = 0;
SET @iRetailTradeID2_CP3_Test13 = 0;
SET @iResultVerification_CP3_Test13 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000005', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test13 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test13, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test13, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test13, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test13, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test13, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test13, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000006', 2, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test13, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test13, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test13 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test13, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test13, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test13, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test13, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test13, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test13, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test13, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test13, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test13 = (@iNO_SingleCommodity_CP1_Test13 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test13 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test13 = (@iNO_SingleCommodity2_CP1_Test13 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test13 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test13 +  @dRetailTradeID2_TotalAmount_CP1_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test13 = (@iNO_SingleCommodity_CP2_Test13 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test13 = (@iNO_SingleCommodity2_CP2_Test13 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test13 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test13 * @dPrice_ServiceCommodity_CategoryParent2);	 
SELECT 1 INTO @iResultVerification_CP2_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test13 + @dRetailTradeID2_TotalAmount_CP2_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test13 = (@iNO_SingleCommodity_CP3_Test13 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test13 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test13 = (@iNO_SingleCommodity2_CP3_Test13 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test13 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
												+ (@iNO_CompositionCommodity_CP3_Test13 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test13 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test13 + @dRetailTradeID2_TotalAmount_CP3_Test13 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test13, @iResultVerification_CP2_Test13, @iResultVerification_CP3_Test13;	 
SELECT IF(@iResultVerification_CP1_Test13 = 1 AND @iResultVerification_CP2_Test13 = 1 AND @iResultVerification_CP3_Test13 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case13 Result';



	
SELECT '-------------------- Case14:����14�����۷ֱ����һ�����������������Ʒ(���˻�) -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test14 ='2099/06/14 08:08:08';
-- �������һ����Ʒ
SET @iNO_SingleCommodity_CP1_Test14 = 9;
SET @iNO_SingleCommodity2_CP1_Test14 = 1;
SET @iNO_CompositionCommodity_CP1_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test14 = 1;
SET @iNO_ServiceCommodity_CP1_Test14 = 1;
SET @iRetailTradeID1_CP1_Test14 = 0;
SET @iRetailTradeID2_CP1_Test14 = 0;
SET @iResultVerification_CP1_Test14 = 0;
-- �������۵�1 (����9����ͨ��Ʒ2��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000001', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test14 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test14, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test14, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test14, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test14, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test14, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test14, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1����ͨ��Ʒ1��1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000002', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test14 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test14, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test14, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test14, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test14, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test14, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test14, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test14, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test14 = 5;
SET @iNO_SingleCommodity2_CP2_Test14 = 3;
SET @iNO_CompositionCommodity_CP2_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test14 = 1;
SET @iNO_ServiceCommodity_CP2_Test14 = 1;
SET @iRetailTradeID1_CP2_Test14 = 0;
SET @iRetailTradeID2_CP2_Test14 = 0;
SET @iResultVerification_CP2_Test14 = 0;
-- �������۵�1 (����5����ͨ��Ʒ2��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000003', 3, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test14 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test14, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test14, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test14, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test14, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test14, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test14, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ1��1�������Ʒ��1��������Ʒ����69.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000004', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test14 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test14, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test14, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test14, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test14, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test14, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test14, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test14, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test14 = 5;
SET @iNO_SingleCommodity2_CP3_Test14 = 3;
SET @iNO_CompositionCommodity_CP3_Test14 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test14 = 1;
SET @iNO_ServiceCommodity_CP3_Test14 = 1;
SET @iRetailTradeID1_CP3_Test14 = 0;
SET @iRetailTradeID2_CP3_Test14 = 0;
SET @iResultVerification_CP3_Test14 = 0;
-- �������۵�1 (����5����ͨ��Ʒ2��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000005', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test14 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test14, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test14, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test14, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test14, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test14, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test14, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ1��1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000006', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test14, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test14, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test14 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test14, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test14, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test14, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test14, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test14, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test14, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test14, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test14, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test14 = (@iNO_SingleCommodity2_CP1_Test14 * @dPrice_SingleCommodity2_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test14 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity_CP1_Test14 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test14 * @dPrice_ServiceCommodity_CategoryParent1) --  
												+ (@iNO_MultiPackagingCommodity_CP1_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test14 + @dRetailTradeID2_TotalAmount_CP2_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity_CP2_Test14 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test14 = (@iNO_SingleCommodity2_CP2_Test14 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test14 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test14 * @dPrice_ServiceCommodity_CategoryParent2); 
SELECT 1 INTO @iResultVerification_CP2_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test14 + @dRetailTradeID2_TotalAmount_CP2_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test14 = (@iNO_SingleCommodity2_CP3_Test14 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test14 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test14 = (@iNO_SingleCommodity_CP3_Test14 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_MultiPackagingCommodity_CP3_Test14 * @dPrice_MultiPackagingCommodity_CategoryParent3) -- 
		   										+ (@iNO_CompositionCommodity_CP3_Test14 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test14 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test14 + @dRetailTradeID2_TotalAmount_CP3_Test14 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test14, @iResultVerification_CP2_Test14, @iResultVerification_CP3_Test14;	 
SELECT IF(@iResultVerification_CP1_Test14 = 1 AND @iResultVerification_CP2_Test14 = 1 AND @iResultVerification_CP3_Test14 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case14 Result';



SELECT '-------------------- Case15:����15�����۷ֱ����һ�����������������Ʒ(���˻�) -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test15 ='2099/06/15 08:08:08';
-- �������һ����Ʒ
SET @iNO_SingleCommodity_CP1_Test15 = 9;
SET @iNO_SingleCommodity2_CP1_Test15 = 1;
SET @iNO_CompositionCommodity_CP1_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP1_Test15 = 1;
SET @iNO_ServiceCommodity_CP1_Test15 = 1;
SET @iRetailTradeID1_CP1_Test15 = 0;
SET @iRetailTradeID2_CP1_Test15 = 0;
SET @iResultVerification_CP1_Test15 = 0;
-- �������۵�1 (����9����ͨ��Ʒ1��1����ͨ��Ʒ2��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000001', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test15 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test15, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test15, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test15, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test15, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test15, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test15, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test15, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2 (����1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000002', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test15 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test15, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test15, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test15, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test15, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test15, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test15, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test15 = 5;
SET @iNO_SingleCommodity2_CP2_Test15 = 3;
SET @iNO_CompositionCommodity_CP2_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP2_Test15 = 1;
SET @iNO_ServiceCommodity_CP2_Test15 = 1;
SET @iRetailTradeID1_CP2_Test15 = 0;
SET @iRetailTradeID2_CP2_Test15 = 0;
SET @iResultVerification_CP2_Test15 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��3����ͨ��Ʒ2��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000003', 4, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test15 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test15, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test15, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test15, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test15, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test15, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test15, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test15, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����1�������Ʒ��1��������Ʒ����69.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000004', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test15 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test15, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test15, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test15, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test15, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test15, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test15, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test15 = 5;
SET @iNO_SingleCommodity2_CP3_Test15 = 3;
SET @iNO_CompositionCommodity_CP3_Test15 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test15 = 1;
SET @iNO_ServiceCommodity_CP3_Test15 = 1;
SET @iRetailTradeID1_CP3_Test15 = 0;
SET @iRetailTradeID2_CP3_Test15 = 0;
SET @iResultVerification_CP3_Test15 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��3����ͨ��Ʒ2��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000005', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test15 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test15, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test15, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test15, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test15, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test15, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test15, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test15, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000006', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test15, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test15, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test15 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test15, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test15, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test15, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test15, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test15, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test15, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test15, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d');
SET @dRetailTradeID1_TotalAmount_CP1_Test15 = (@iNO_SingleCommodity_CP1_Test15 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test15 * @dPrice_CompositionCommodity_CategoryParent1) -- 
												+ (@iNO_SingleCommodity2_CP1_Test15 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dRetailTradeID2_TotalAmount_CP1_Test15 = (@iNO_ServiceCommodity_CP1_Test15 * @dPrice_ServiceCommodity_CategoryParent1) + (@iNO_MultiPackagingCommodity_CP1_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP1_Test15 + @dRetailTradeID2_TotalAmount_CP1_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP2_Test15 = (@iNO_SingleCommodity_CP2_Test15 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent2) -- 
												+ (@iNO_SingleCommodity2_CP2_Test15 * @dPrice_SingleCommodity2_CategoryParent2);
SET @dRetailTradeID2_TotalAmount_CP2_Test15 = (@iNO_CompositionCommodity_CP2_Test15 * @dPrice_CompositionCommodity_CategoryParent2) + (@iNO_ServiceCommodity_CP2_Test15 * @dPrice_ServiceCommodity_CategoryParent2); 		
SELECT 1 INTO @iResultVerification_CP2_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP2_Test15 + @dRetailTradeID2_TotalAmount_CP2_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_TotalAmount_CP3_Test15 = (@iNO_SingleCommodity_CP3_Test15 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test15 * @dPrice_ServiceCommodity_CategoryParent3) -- 
												+ (@iNO_SingleCommodity2_CP3_Test15 * @dPrice_SingleCommodity2_CategoryParent3);
SET @dRetailTradeID2_TotalAmount_CP3_Test15 = (@iNO_MultiPackagingCommodity_CP3_Test15 * @dPrice_MultiPackagingCommodity_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test15 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test15 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_TotalAmount_CP3_Test15 + @dRetailTradeID2_TotalAmount_CP3_Test15 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d'); --  

SELECT @iResultVerification_CP1_Test15, @iResultVerification_CP2_Test15, @iResultVerification_CP3_Test15;	 
SELECT IF(@iResultVerification_CP1_Test15 = 1 AND @iResultVerification_CP2_Test15 = 1 AND @iResultVerification_CP3_Test15 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case15 Result';




SELECT '-------------------- Case16:����16���˵���1�����������һ����Ʒ(û��������Ʒ) -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test16 ='2099/06/16 08:08:08';
SET @iResultVerification_Test16 = 0;
-- �Ե���1�ŵ���Ϊ"LS2099060100000000000001"�����۵�����ȫ���˻�(��52.5Ԫ)
SET @iReturnNo_SingleCommodity_All_Test16 = 3;
SET @iReturnNo_CompositionCommodity_All_Test16 = 2;
SET @iReturnRetailTradeID_All_Test16 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��2�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000001_1', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test16, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test1, @dSaleDatetime_Test16, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test16 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test16, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test16, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test16, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test16, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_Test16, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_Test16, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �Ե���1�ŵ���Ϊ"LS2099060100000000000002"�����۵����в����˻�(��92.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_Test16 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test16 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test16 = 1;
SET @iReturnRetailTradeID_Part_Test16 = 0;
-- ���������˻���2 (��1����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����92.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060100000000000002', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test16, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test1, @dSaleDatetime_Test16, 92.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test16 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test16, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test16, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test16, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test16, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test16, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_Test16, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_Test16, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test16, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test16 = (@iReturnNo_SingleCommodity_All_Test16 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_Test16 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test16 = (@iReturnNo_SingleCommodity2_Part_Test16 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_Test16 * @dPrice_MultiPackagingCommodity_CategoryParent1) -- 
														+ (@iReturnNo_ServiceCommodity_Part_Test16 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test16 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test16 - @dReturnRetailTradeID_Part_TotalAmount_Test16 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test16;
SELECT IF(@iResultVerification_Test16 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case16 Result';


SELECT '-------------------- Case17:����17���˵���2�����������һ����Ʒ(û��������Ʒ) -------------------------' AS 'Case17'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test17 ='2099/06/17 08:08:08';
SET @iResultVerification_Test17 = 0;
-- �Ե���2�ŵ���Ϊ"LS2099060200000000000001"�����۵�����ȫ���˻�(��171.5Ԫ)
SET @iReturnNo_SingleCommodity_All_Test17 = 1;
SET @iReturnNo_MultiPackagingCommodity_All_Test17 = 2;
SET @iReturnRetailTradeID_All_Test17 = 0;
-- ���������˻���1 (��1����ͨ��Ʒ1��2�����װ��Ʒ����171.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000001_1', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test17, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test2, @dSaleDatetime_Test17, 171.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test17 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test17, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test17, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test17, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test17, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_All_Test17, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_All_Test17, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- �Ե���2�ŵ���Ϊ"LS2099060200000000000002"�����۵����в����˻�(��8.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_Test17 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test17 = 1;
SET @iReturnRetailTradeID_Part_Test17 = 0;
-- ���������˻���2 (��1����ͨ��Ʒ2��1��������Ʒ����8.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060200000000000002_1', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test17, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test2, @dSaleDatetime_Test17, 8.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test17 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test17, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_Test17, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_Test17, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test17, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test17, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test17, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test17, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test17 = (@iReturnNo_SingleCommodity_All_Test17 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_All_Test17 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test17 = (@iReturnNo_SingleCommodity2_Part_Test17 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_Test17 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT @dReturnRetailTradeID_All_TotalAmount_Test17, @dReturnRetailTradeID_Part_TotalAmount_Test17;
SELECT 1 INTO @iResultVerification_Test17 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test17 - @dReturnRetailTradeID_Part_TotalAmount_Test17 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test17;
SELECT IF(@iResultVerification_Test17 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case17 Result';



SELECT '-------------------- Case18:����18���˵���3�����������һ����Ʒ(û��������Ʒ) -------------------------' AS 'Case18'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test18 ='2099/06/18 08:08:08';
SET @iResultVerification_Test18 = 0;
-- �Ե���3�ŵ���Ϊ"LS2099060300000000000001"�����۵�����ȫ���˻�(��15.5Ԫ)
SET @iReturnNo_SingleCommodity_All_Test18 = 3;
SET @iReturnNo_ServiceCommodity_All_Test18 = 1;
SET @iReturnRetailTradeID_All_Test18 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��1��������Ʒ����15.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000001_1', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test18, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test3, @dSaleDatetime_Test18, 15.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test18 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test18, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test18, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test18, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test18, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_All_Test18, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_All_Test18, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- �Ե���3�ŵ���Ϊ"LS2099060300000000000002"�����۵����в����˻�(��15.5Ԫ)
SET @iReturnNo_CompositionCommodity_Part_Test18 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test18 = 1;
SET @iReturnRetailTradeID_Part_Test18 = 0;
-- ���������˻���2 (��1�������Ʒ��1�����װ��Ʒ����105Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060300000000000002_1', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test18, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test3, @dSaleDatetime_Test18, 105, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test18 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test18, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_Part_Test18, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_Part_Test18, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test18, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test18, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test18, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test18, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test18 = (@iReturnNo_SingleCommodity_All_Test18 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_All_Test18 * @dPrice_ServiceCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test18 = (@iReturnNo_CompositionCommodity_Part_Test18 * @dPrice_CompositionCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_Test18 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test18 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test18 -  @dReturnRetailTradeID_Part_TotalAmount_Test18 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test18;
SELECT IF(@iResultVerification_Test18 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case18 Result';



SELECT '-------------------- Case19:����19���˵���4�����������һ����Ʒ(û��������Ʒ) -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test19 ='2099/06/19 08:08:08';
SET @iResultVerification_Test19 = 0;
-- �Ե���4�ŵ���Ϊ"LS2099060400000000000001"�����۵�����ȫ���˻�(��38.5Ԫ)
SET @iReturnNo_SingleCommodity_All_Test19 = 5;
SET @iReturnNo_CompositionCommodity_All_Test19 = 1;
SET @iReturnRetailTradeID_All_Test19 = 0;
-- �������˻��۵�1 (��5����ͨ��Ʒ1��1�������Ʒ����38.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000001_1', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test19, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test4, @dSaleDatetime_Test19, 38.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test19 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test19, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_Test19, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_Test19, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test19, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_Test19, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_Test19, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �Ե���4�ŵ���Ϊ"LS2099060400000000000002"�����۵����в����˻�(��89Ԫ)
SET @iReturnNo_MultiPackagingCommodity_Part_Test19 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test19 = 1;
SET @iReturnRetailTradeID_Part_Test19 = 0;
-- ���������˻���2 (��1�����װ��Ʒ��1��������Ʒ����89Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060400000000000002_1', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test19, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test4, @dSaleDatetime_Test19, 89, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test19 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test19, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_Test19, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_Test19, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test19, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_Test19, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_Test19, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test19, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test19 = (@iReturnNo_SingleCommodity_All_Test19 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_Test19 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_TotalAmount_Test19 = (@iReturnNo_MultiPackagingCommodity_Part_Test19 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_Test19 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_Test19 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test19 - @dReturnRetailTradeID_Part_TotalAmount_Test19 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test19;
SELECT IF(@iResultVerification_Test19 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case19 Result';



SELECT '-------------------- Case20:����20���˵���5����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case20';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test20 ='2099/06/20 08:08:08';
SET @iResultVerification_Test20 = 0;
-- �Ե���5�ŵ���Ϊ"LS2099060500000000000001"�����۵�����ȫ���˻�(��29.94Ԫ)
SET @iReturnNo_SingleCommodity2_All_Test20 = 2;
SET @iReturnNo_MultiPackagingCommodity_All_Test20 = 2;
SET @iReturnRetailTradeID_All_Test20 = 0;
-- ���������˻���1 (��2����ͨ��Ʒ2��2�����װ��Ʒ����29.94Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000001_1', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test20, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test5, @dSaleDatetime_Test20, 29.94, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test20 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test20, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_Test20, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_Test20, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test20, @iMultiPackagingCommodityID_CategoryParent2, 'һ�������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_Test20, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_Test20, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �Ե���5�ŵ���Ϊ"LS2099060500000000000002"�����۵����в����˻�(��54.9Ԫ)
SET @iReturnNo_CompositionCommodity_Part_Test20 = 1;
SET @iReturnNo_ServiceCommodity_Part_Test20 = 1;
SET @iReturnRetailTradeID_Part_Test20 = 0;
-- ���������˻���2 (1�������Ʒ��1��������Ʒ����54.9Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060500000000000002_1', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test20, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test5, @dSaleDatetime_Test20, 54.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test20 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test20, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_Test20, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_Test20, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test20, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_Test20, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_Test20, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test20, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test20 = (@iReturnNo_SingleCommodity2_All_Test20 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_Test20 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_TotalAmount_Test20 = (@iReturnNo_CompositionCommodity_Part_Test20 * @dPrice_CompositionCommodity_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_Test20 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_Test20 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test20 - @dReturnRetailTradeID_Part_TotalAmount_Test20 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test20;
SELECT IF(@iResultVerification_Test20 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case20 Result';



SELECT '-------------------- Case21:����21���˵���6�����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case21';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test21 ='2099/06/21 08:08:08';
SET @iResultVerification_Test21 = 0;
-- �Ե���6�ŵ���Ϊ"LS2099060600000000000001"�����۵�����ȫ���˻�(��12.76Ԫ)
SET @iReturnNo_SingleCommodity2_All_Test21 = 2;
SET @iReturnNo_ServiceCommodity_All_Test21 = 1;
SET @iReturnRetailTradeID_All_Test21 = 0;
-- ���������˻���1 (��2����ͨ��Ʒ2��1��������Ʒ����12.76Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000001_1', 6, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test21, 2, 4, '0', 
1, '........', @iRetailTradeID1_Test6, @dSaleDatetime_Test21, 12.76, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_Test21 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test21, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_Test21, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_Test21, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_Test21, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_Test21, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_Test21, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �Ե���6�ŵ���Ϊ"LS2099060600000000000002"�����۵����в����˻�(��42.68Ԫ)
SET @iReturnNo_CompositionCommodity_Part_Test21 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_Test21 = 1;
SET @iReturnRetailTradeID_Part_Test21 = 0;
-- ���������˻���2 (��1�������Ʒ��1�����װ��Ʒ����42.68Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099060600000000000002_1', 6, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test21, 2, 4, '0', 
1, '........', @iRetailTradeID2_Test6, @dSaleDatetime_Test21, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_Test21 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test21, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_Test21, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_Test21, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_Test21, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_Test21, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_Test21, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test21, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_TotalAmount_Test21 = (@iReturnNo_SingleCommodity2_All_Test21 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_Test21 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_TotalAmount_Test21 = (@iReturnNo_CompositionCommodity_Part_Test21 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_Test21 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_Test21 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_TotalAmount_Test21 - @dReturnRetailTradeID_Part_TotalAmount_Test21 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d'); -- 

SELECT @iResultVerification_Test21;
SELECT IF(@iResultVerification_Test21 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case21 Result';



SELECT '-------------------- Case22:����22���˵���10�����������һ�����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test22 ='2099/06/22 08:08:08';
SET @iResultVerification_CP1_Test22 = 0;
SET @iResultVerification_CP2_Test22 = 0;
SET @iResultVerification_CP3_Test22 = 0;
-- �Ե���10�ŵ���Ϊ"LS2099061000000000000001"�����۵�����ȫ���˻�(��52.5Ԫ)
SET @iReturnNo_SingleCommodity_All_CP1_Test22 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test22 = 1;
SET @iReturnRetailTradeID_All_CP1_Test22 = 0;
-- ���������˻���1 (��9����ͨ��Ʒ1��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000001_1', 7, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test10, @dSaleDatetime_Test22, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test22 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test22, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test22, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test22, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test22, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test22, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test22, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �Ե���10�ŵ���Ϊ"LS2099061000000000000002"�����۵����в����˻�(��87.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test22 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test22 = 0;
-- ���������˻���2 (��1����ͨ��Ʒ2��1�����װ��Ʒ����87.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000002_1', 7, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test10, @dSaleDatetime_Test22, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test22 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test22, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test22, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test22, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test22, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test22, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- �Ե���10�ŵ���Ϊ"LS2099061000000000000003"�����۵�����ȫ���˻�(��34.93Ԫ)
SET @iReturnNo_SingleCommodity_All_CP2_Test22 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test22 = 1;
SET @iReturnRetailTradeID_All_CP2_Test22 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000003_1', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test10, @dSaleDatetime_Test22, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test22 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test22, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test22, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test22, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test22, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test22, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test22, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �Ե���10�ŵ���Ϊ"LS2099061000000000000004"�����۵����в����˻�(��19.97Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test22 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test22 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ2��1��������Ʒ����19.97Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000004_1', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test10, @dSaleDatetime_Test22, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test22 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test22, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test22, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test22, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test22, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test22, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test22, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- �Ե���10�ŵ���Ϊ"LS2099061000000000000005"�����۵�����ȫ���˻�(��24.4Ԫ)
SET @iReturnNo_SingleCommodity_All_CP3_Test22 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test22 = 1;
SET @iReturnRetailTradeID_All_CP3_Test22 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000005_1', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test10, @dSaleDatetime_Test22, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test22 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test22, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test22, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test22, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test22, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test22, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test22, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �Ե���10�ŵ���Ϊ"LS2099061000000000000006"�����۵����в����˻�(��46.68Ԫ)
SET @iReturnNo_CompositionCommodity_Part_CP3_Test22 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test22 = 0;
-- ���������˻���2 (��1�������Ʒ��1�����װ��Ʒ����42.68Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061000000000000006_1', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test22, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test10, @dSaleDatetime_Test22, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test22 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test22, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test22, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test22, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test22, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test22, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test22, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP1_Test22 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test22 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test22 = (@iReturnNo_SingleCommodity2_Part_CP1_Test22 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); --

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP2_Test22 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_CP2_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test22 = (@iReturnNo_SingleCommodity2_Part_CP2_Test22 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test22 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); --

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test22 = (@iReturnNo_SingleCommodity_All_CP3_Test22 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test22 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test22 = (@iReturnNo_CompositionCommodity_Part_CP3_Test22 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test22 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test22 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test22 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test22 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test22, @iResultVerification_CP2_Test22, @iResultVerification_CP3_Test22;
SELECT IF(@iResultVerification_CP1_Test22 = 1 AND @iResultVerification_CP2_Test22 = 1 AND @iResultVerification_CP3_Test22 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case22 Result';




SELECT '-------------------- Case23:����23���˵���11������������ġ�����塢���������Ʒ(û��������Ʒ) -------------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test23 ='2099/06/23 08:08:08';
SET @iResultVerification_CP4_Test23 = 0;
SET @iResultVerification_CP5_Test23 = 0;
SET @iResultVerification_CP6_Test23 = 0;
-- �Ե���11�ŵ���Ϊ"LS2099061100000000000001"�����۵�����ȫ���˻�(��247.6Ԫ)
SET @iReturnNo_SingleCommodity_All_CP4_Test23 = 2;
SET @iReturnNo_CompositionCommodity_All_CP4_Test23 = 1;
SET @iReturnRetailTradeID_All_CP4_Test23 = 0;
-- ���������˻���1 (��2����ͨ��Ʒ1��1�������Ʒ����247.6Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000001_1', 8, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP4_Test11, @dSaleDatetime_Test23, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP4_Test23 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test23, @iSingleCommodityID_CategoryParent4, '��������_CategoryParent4', 1, @iReturnNo_SingleCommodity_All_CP4_Test23, @dPrice_SingleCommodity_CategoryParent4, @iReturnNo_SingleCommodity_All_CP4_Test23, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test23, @iCompositionCommodityID_CategoryParent4, '�������_CategoryParent4', 1, @iReturnNo_CompositionCommodity_All_CP4_Test23, @dPrice_CompositionCommodity_CategoryParent4, @iReturnNo_CompositionCommodity_All_CP4_Test23, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- �Ե���11�ŵ���Ϊ"LS2099061100000000000002"�����۵����в����˻�(��109Ԫ)
SET @iReturnNo_ServiceCommodity_Part_CP4_Test23 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP4_Test23 = 0;
-- ���������˻���2 (��1�����װ��Ʒ��1��������Ʒ����109Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000002_1', 8, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP4_Test11, @dSaleDatetime_Test23, 109, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP4_Test23 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test23, @iServiceCommodityID_CategoryParent4, '���_CategoryParent4', 1, @iReturnNo_ServiceCommodity_Part_CP4_Test23, @dPrice_ServiceCommodity_CategoryParent4, @iReturnNo_ServiceCommodity_Part_CP4_Test23, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test23, @iMultiPackagingCommodityID_CategoryParent4, 'һ��������_CategoryParent4', 1, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23, @dPrice_MultiPackagingCommodity_CategoryParent4, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test23, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);

-- �Ե���11�ŵ���Ϊ"LS2099061100000000000003"�����۵�����ȫ���˻�(��675Ԫ)
SET @iReturnNo_SingleCommodity_All_CP5_Test23 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP5_Test23 = 1;
SET @iReturnRetailTradeID_All_CP5_Test23 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��1�����װ��Ʒ����675Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000003_1', 8, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP5_Test11, @dSaleDatetime_Test23, 675, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP5_Test23 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP5_Test23, @iSingleCommodityID_CategoryParent5, '����������Ĥ_CategoryParent5', 1, @iReturnNo_SingleCommodity_All_CP5_Test23, @dPrice_SingleCommodity_CategoryParent5, @iReturnNo_SingleCommodity_All_CP5_Test23, @dPrice_SingleCommodity_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP5_Test23, @iMultiPackagingCommodityID_CategoryParent5, 'һ������������Ĥ_CategoryParent5', 1, @iReturnNo_MultiPackagingCommodity_All_CP5_Test23, @dPrice_MultiPackagingCommodity_CategoryParent5, @iReturnNo_MultiPackagingCommodity_All_CP5_Test23, @dPrice_MultiPackagingCommodity_CategoryParent5, NULL, NULL);
-- �Ե���11�ŵ���Ϊ"LS2099061100000000000004"�����۵����в����˻�(��761Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP5_Test23 = 4;
SET @iReturnNo_ServiceCommodity_Part_CP5_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP5_Test23 = 0;
-- ���������˻���2 (��4����ͨ��Ʒ2��1��������Ʒ����761Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000004_1', 8, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP5_Test11, @dSaleDatetime_Test23, 761, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP5_Test23 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP5_Test23, @iSingleCommodityID2_CategoryParent5, 'ѩ���鼡��ˮ_CategoryParent5', 1, @iReturnNo_SingleCommodity2_Part_CP5_Test23, @dPrice_SingleCommodity2_CategoryParent5, @iReturnNo_SingleCommodity2_Part_CP5_Test23, @dPrice_SingleCommodity2_CategoryParent5, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP5_Test23, @iServiceCommodityID_CategoryParent5, '���_CategoryParent5', 1, @iReturnNo_ServiceCommodity_Part_CP5_Test23, @dPrice_ServiceCommodity_CategoryParent5, @iReturnNo_ServiceCommodity_Part_CP5_Test23, @dPrice_ServiceCommodity_CategoryParent5, NULL, NULL);

-- �Ե���11�ŵ���Ϊ"LS2099061100000000000005"�����۵�����ȫ���˻�(��21.5Ԫ)
SET @iReturnNo_SingleCommodity_All_CP6_Test23 = 3;
SET @iReturnNo_ServiceCommodity_All_CP6_Test23 = 1;
SET @iReturnRetailTradeID_All_CP6_Test23 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��1��������Ʒ����21.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000005_1', 8, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP6_Test11, @dSaleDatetime_Test23, 21.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP6_Test23 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP6_Test23, @iSingleCommodityID_CategoryParent6, '��̳���ţ����_CategoryParent6', 1, @iReturnNo_SingleCommodity_All_CP6_Test23, @dPrice_SingleCommodity_CategoryParent6, @iReturnNo_SingleCommodity_All_CP6_Test23, @dPrice_SingleCommodity_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP6_Test23, @iServiceCommodityID_CategoryParent6, '���_CategoryParent6', 1, @iReturnNo_ServiceCommodity_All_CP6_Test23, @dPrice_ServiceCommodity_CategoryParent6, @iReturnNo_ServiceCommodity_All_CP6_Test23, @dPrice_ServiceCommodity_CategoryParent6, NULL, NULL);
-- �Ե���11�ŵ���Ϊ"LS2099061100000000000006"�����۵����в����˻�(��60.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP6_Test23 = 5;
SET @iReturnNo_CompositionCommodity_Part_CP6_Test23 = 1;
SET @iReturnRetailTradeID_Part_CP6_Test23 = 0;
-- ���������˻���2 (��5����ͨ��Ʒ2��1�������Ʒ����60.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061100000000000006_1', 8, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test23, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP6_Test11, @dSaleDatetime_Test23, 60.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP6_Test23 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP6_Test23, @iSingleCommodityID2_CategoryParent6, '����ţ����_CategoryParent6', 1, @iReturnNo_SingleCommodity2_Part_CP6_Test23, @dPrice_SingleCommodity2_CategoryParent6, @iReturnNo_SingleCommodity2_Part_CP6_Test23, @dPrice_SingleCommodity2_CategoryParent6, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP6_Test23, @iCompositionCommodityID_CategoryParent6, 'Ͱ������_CategoryParent6', 1, @iReturnNo_CompositionCommodity_Part_CP6_Test23, @dPrice_CompositionCommodity_CategoryParent6, @iReturnNo_CompositionCommodity_Part_CP6_Test23, @dPrice_CompositionCommodity_CategoryParent6, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test23, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP4_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP4_Test23 * @dPrice_SingleCommodity_CategoryParent4) + (@iReturnNo_CompositionCommodity_All_CP4_Test23 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dReturnRetailTradeID_Part_CP4_TotalAmount_Test23 = (@iReturnNo_ServiceCommodity_Part_CP4_Test23 * @dPrice_ServiceCommodity_CategoryParent4) + (@iReturnNo_MultiPackagingCommodity_Part_CP4_Test23 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SELECT 1 INTO @iResultVerification_CP4_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP4_TotalAmount_Test23 - @dReturnRetailTradeID_Part_CP4_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 
SET @dReturnRetailTradeID_All_CP5_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP5_Test23 * @dPrice_SingleCommodity_CategoryParent5) + (@iReturnNo_MultiPackagingCommodity_All_CP5_Test23 * @dPrice_MultiPackagingCommodity_CategoryParent5);
SET @dReturnRetailTradeID_Part_CP5_TotalAmount_Test23 = (@iReturnNo_SingleCommodity2_Part_CP5_Test23 * @dPrice_SingleCommodity2_CategoryParent5) + (@iReturnNo_ServiceCommodity_Part_CP5_Test23 * @dPrice_ServiceCommodity_CategoryParent5);
SELECT 1 INTO @iResultVerification_CP5_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID5 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP5_TotalAmount_Test23 -  @dReturnRetailTradeID_Part_CP5_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 
SET @dReturnRetailTradeID_All_CP6_TotalAmount_Test23 = (@iReturnNo_SingleCommodity_All_CP6_Test23 * @dPrice_SingleCommodity_CategoryParent6) + (@iReturnNo_ServiceCommodity_All_CP6_Test23 * @dPrice_ServiceCommodity_CategoryParent6);
SET @dReturnRetailTradeID_Part_CP6_TotalAmount_Test23 = (@iReturnNo_SingleCommodity2_Part_CP6_Test23 * @dPrice_SingleCommodity2_CategoryParent6) + (@iReturnNo_CompositionCommodity_Part_CP6_Test23 * @dPrice_CompositionCommodity_CategoryParent6);
SELECT 1 INTO @iResultVerification_CP6_Test23 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID6 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP6_TotalAmount_Test23 - @dReturnRetailTradeID_Part_CP6_TotalAmount_Test23 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP4_Test23, @iResultVerification_CP5_Test23, @iResultVerification_CP6_Test23;
SELECT IF(@iResultVerification_CP4_Test23 = 1 AND @iResultVerification_CP5_Test23 = 1 AND @iResultVerification_CP6_Test23 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case23 Result';






SELECT '-------------------- Case24:����24���˵���12������������ߡ����ˡ����ŵ���Ʒ(û��������Ʒ) -------------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test24 ='2099/06/24 08:08:08';
SET @iResultVerification_CP7_Test24 = 0;
SET @iResultVerification_CP8_Test24 = 0;
SET @iResultVerification_CP9_Test24 = 0;
-- �Ե���12�ŵ���Ϊ"LS2099061200000000000001"�����۵�����ȫ���˻�(��66Ԫ)
SET @iReturnNo_SingleCommodity_All_CP7_Test24 = 2;
SET @iReturnNo_CompositionCommodity_All_CP7_Test24 = 1;
SET @iReturnRetailTradeID_All_CP7_Test24 = 0;
-- ���������˻���1 (��2����ͨ��Ʒ1��1�������Ʒ����66Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000001_1', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP7_Test12, @dSaleDatetime_Test24, 66, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP7_Test24 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP7_Test24, @iSingleCommodityID_CategoryParent7, '����Բ���_CategoryParent7', 1, @iReturnNo_SingleCommodity_All_CP7_Test24, @dPrice_SingleCommodity_CategoryParent7, @iReturnNo_SingleCommodity_All_CP7_Test24, @dPrice_SingleCommodity_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP7_Test24, @iCompositionCommodityID_CategoryParent7, '�ľߴ����_CategoryParent7', 1, @iReturnNo_CompositionCommodity_All_CP7_Test24, @dPrice_CompositionCommodity_CategoryParent7, @iReturnNo_CompositionCommodity_All_CP7_Test24, @dPrice_CompositionCommodity_CategoryParent7, NULL, NULL);
-- �Ե���12�ŵ���Ϊ"LS2099061200000000000002"�����۵����в����˻�(��24Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP7_Test24 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP7_Test24 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ2��1�����װ��Ʒ����24Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000002_1', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP7_Test12, @dSaleDatetime_Test24, 24, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP7_Test24 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP7_Test24, @iSingleCommodityID2_CategoryParent7, '���Բ���_CategoryParent7', 1, @iReturnNo_SingleCommodity2_Part_CP7_Test24, @dPrice_SingleCommodity2_CategoryParent7, @iReturnNo_SingleCommodity2_Part_CP7_Test24, @dPrice_SingleCommodity2_CategoryParent7, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP7_Test24, @iMultiPackagingCommodityID_CategoryParent7, 'һ�г���Բ���_CategoryParent7', 1, @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24, @dPrice_MultiPackagingCommodity_CategoryParent7, @iReturnNo_MultiPackagingCommodity_Part_CP7_Test24, @dPrice_MultiPackagingCommodity_CategoryParent7, NULL, NULL);

-- �Ե���12�ŵ���Ϊ"LS2099061200000000000003"�����۵�����ȫ���˻�(��714Ԫ)
SET @iReturnNo_SingleCommodity_All_CP8_Test24 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP8_Test24 = 1;
SET @iReturnRetailTradeID_All_CP8_Test24 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��1�����װ��Ʒ����714Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000003_1', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP8_Test12, @dSaleDatetime_Test24, 714, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP8_Test24 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP8_Test24, @iSingleCommodityID_CategoryParent8, '�綯��ˢ_CategoryParent8', 1, @iReturnNo_SingleCommodity_All_CP8_Test24, @dPrice_SingleCommodity_CategoryParent8, @iReturnNo_SingleCommodity_All_CP8_Test24, @dPrice_SingleCommodity_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP8_Test24, @iMultiPackagingCommodityID_CategoryParent8, '��ֻ���ϰ�ҩ_CategoryParent8', 1, @iReturnNo_MultiPackagingCommodity_All_CP8_Test24, @dPrice_MultiPackagingCommodity_CategoryParent8, @iReturnNo_MultiPackagingCommodity_All_CP8_Test24, @dPrice_MultiPackagingCommodity_CategoryParent8, NULL, NULL);

-- �Ե���12�ŵ���Ϊ"LS2099061200000000000004"�����۵����в����˻�(��161Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP8_Test24 = 4;
SET @iReturnNo_ServiceCommodity_Part_CP8_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP8_Test24 = 0;
-- ���������˻���2 (��4����ͨ��Ʒ2��1��������Ʒ����161Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000004_1', 1, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP8_Test12, @dSaleDatetime_Test24, 161, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP8_Test24 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP8_Test24, @iSingleCommodityID2_CategoryParent8, '�ϰ�ҩ����_CategoryParent8', 1, @iReturnNo_SingleCommodity2_Part_CP8_Test24, @dPrice_SingleCommodity2_CategoryParent8, @iReturnNo_SingleCommodity2_Part_CP8_Test24, @dPrice_SingleCommodity2_CategoryParent8, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP8_Test24, @iServiceCommodityID_CategoryParent8, '���_CategoryParent8', 1, @iReturnNo_ServiceCommodity_Part_CP8_Test24, @dPrice_ServiceCommodity_CategoryParent8, @iReturnNo_ServiceCommodity_Part_CP8_Test24, @dPrice_ServiceCommodity_CategoryParent8, NULL, NULL);

-- �Ե���12�ŵ���Ϊ"LS2099061200000000000005"�����۵�����ȫ���˻�(��18.5Ԫ)
SET @iReturnNo_SingleCommodity_All_CP9_Test24 = 3;
SET @iReturnNo_ServiceCommodity_All_CP9_Test24 = 1;
SET @iReturnRetailTradeID_All_CP9_Test24 = 0;
-- ���������˻���1 (��3����ͨ��Ʒ1��1��������Ʒ����18.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000005_1', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP9_Test12, @dSaleDatetime_Test24, 18.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP9_Test24 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP9_Test24, @iSingleCommodityID_CategoryParent9, '������_CategoryParent9', 1, @iReturnNo_SingleCommodity_All_CP9_Test24, @dPrice_SingleCommodity_CategoryParent9, @iReturnNo_SingleCommodity_All_CP9_Test24, @dPrice_SingleCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP9_Test24, @iServiceCommodityID_CategoryParent9, '���_CategoryParent9', 1, @iReturnNo_ServiceCommodity_All_CP9_Test24, @dPrice_ServiceCommodity_CategoryParent9, @iReturnNo_ServiceCommodity_All_CP9_Test24, @dPrice_ServiceCommodity_CategoryParent9, NULL, NULL);

-- �Ե���12�ŵ���Ϊ"LS2099061200000000000006"�����۵����в����˻�(��17Ԫ)
SET @iReturnNo_CompositionCommodity_Part_CP9_Test24 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24 = 1;
SET @iReturnRetailTradeID_Part_CP9_Test24 = 0;
-- ���������˻���2 (��1�������Ʒ��1�����װ��Ʒ����17Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061200000000000006_1', 1, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test24, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP9_Test12, @dSaleDatetime_Test24, 17, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP9_Test24 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP9_Test24, @iCompositionCommodityID_CategoryParent9, '�߲˴����_CategoryParent9', 1, @iReturnNo_CompositionCommodity_Part_CP9_Test24, @dPrice_CompositionCommodity_CategoryParent9, @iReturnNo_CompositionCommodity_Part_CP9_Test24, @dPrice_CompositionCommodity_CategoryParent9, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP9_Test24, @iMultiPackagingCommodityID_CategoryParent9, 'һ������_CategoryParent9', 1, @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24, @dPrice_MultiPackagingCommodity_CategoryParent9, @iReturnNo_MultiPackagingCommodity_Part_CP9_Test24, @dPrice_MultiPackagingCommodity_CategoryParent9, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test24, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP7_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP7_Test24 * @dPrice_SingleCommodity_CategoryParent7) + (@iReturnNo_CompositionCommodity_All_CP7_Test24 * @dPrice_CompositionCommodity_CategoryParent7);
SET @dReturnRetailTradeID_Part_CP7_TotalAmount_Test24 = (@iReturnNo_SingleCommodity2_Part_CP7_Test24 * @dPrice_SingleCommodity2_CategoryParent7) + (@iReturnNo_MultiPackagingCommodity_Part_CP7_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent7);
SELECT 1 INTO @iResultVerification_CP7_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID7 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP7_TotalAmount_Test24 - @dReturnRetailTradeID_Part_CP7_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP8_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP8_Test24 * @dPrice_SingleCommodity_CategoryParent8) + (@iReturnNo_MultiPackagingCommodity_All_CP8_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent8);
SET @dReturnRetailTradeID_Part_CP8_TotalAmount_Test24 = (@iReturnNo_SingleCommodity2_Part_CP8_Test24 * @dPrice_SingleCommodity2_CategoryParent8) + (@iReturnNo_ServiceCommodity_Part_CP8_Test24 * @dPrice_ServiceCommodity_CategoryParent8);
SELECT 1 INTO @iResultVerification_CP8_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID8 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP8_TotalAmount_Test24 - @dReturnRetailTradeID_Part_CP8_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP9_TotalAmount_Test24 = (@iReturnNo_SingleCommodity_All_CP9_Test24 * @dPrice_SingleCommodity_CategoryParent9) + (@iReturnNo_ServiceCommodity_All_CP9_Test24 * @dPrice_ServiceCommodity_CategoryParent9);
SET @dReturnRetailTradeID_Part_CP9_TotalAmount_Test24 = (@iReturnNo_CompositionCommodity_Part_CP9_Test24 * @dPrice_CompositionCommodity_CategoryParent9) + (@iReturnNo_MultiPackagingCommodity_Part_CP9_Test24 * @dPrice_MultiPackagingCommodity_CategoryParent9);
SELECT 1 INTO @iResultVerification_CP9_Test24 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID9 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP9_TotalAmount_Test24 -  @dReturnRetailTradeID_Part_CP9_TotalAmount_Test24 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP7_Test24, @iResultVerification_CP8_Test24, @iResultVerification_CP9_Test24;
SELECT IF(@iResultVerification_CP7_Test24 = 1 AND @iResultVerification_CP8_Test24 = 1 AND @iResultVerification_CP9_Test24 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case24 Result';




SELECT '-------------------- Case25:����25���˵���13�����������һ�����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test25 ='2099/06/25 08:08:08';
SET @iResultVerification_CP1_Test25 = 0;
SET @iResultVerification_CP2_Test25 = 0;
SET @iResultVerification_CP3_Test25 = 0;
-- �Ե���13�ŵ���Ϊ"LS2099061300000000000001"�����۵�����ȫ���˻�(��52.5Ԫ)
SET @iReturnNo_SingleCommodity_All_CP1_Test25 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test25 = 1;
SET @iReturnRetailTradeID_All_CP1_Test25 = 0;
-- ���������˻���1 (��9����ͨ��Ʒ1��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000001_1', 2, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test13, @dSaleDatetime_Test25, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test25 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test25, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test25, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test25, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test25, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test25, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test25, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �Ե���13�ŵ���Ϊ"LS2099061300000000000002"�����۵����в����˻�(��87.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test25 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test25 = 0;
-- ���������˻���2 (��1����ͨ��Ʒ2��1�����װ��Ʒ����87.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000002_1', 2, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test13, @dSaleDatetime_Test25, 87.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test25 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test25, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test25, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test25, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test25, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test25, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- �Ե���13�ŵ���Ϊ"LS2099061300000000000003"�����۵�����ȫ���˻�(��34.93Ԫ)
SET @iReturnNo_SingleCommodity_All_CP2_Test25 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test25 = 1;
SET @iReturnRetailTradeID_All_CP2_Test25 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000003_1', 2, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test13, @dSaleDatetime_Test25, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test25 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test25, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test25, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test25, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test25, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test25, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test25, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �Ե���13�ŵ���Ϊ"LS2099061300000000000004"�����۵����в����˻�(��19.97Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test25 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test25 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ2��1��������Ʒ����19.97Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000004_1', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test13, @dSaleDatetime_Test25, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test25 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test25, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test25, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test25, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test25, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_Part_CP2_Test25, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- �Ե���13�ŵ���Ϊ"LS2099061300000000000005"�����۵�����ȫ���˻�(��24.4Ԫ)
SET @iReturnNo_SingleCommodity_All_CP3_Test25 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test25 = 1;
SET @iReturnRetailTradeID_All_CP3_Test25 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000005_1', 2, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test13, @dSaleDatetime_Test25, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test25 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test25, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test25, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test25, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test25, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test25, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test25, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �Ե���13�ŵ���Ϊ"LS2099061300000000000006"�����۵����в����˻�(��42.68Ԫ)
SET @iReturnNo_CompositionCommodity_Part_CP3_Test25 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test25 = 0;
-- ���������˻���2 (��1�������Ʒ��1�����װ��Ʒ����42.68Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061300000000000006_1', 2, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test25, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test13, @dSaleDatetime_Test25, 42.68, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test25 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test25, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test25, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test25, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test25, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test25, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test25, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d');
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP1_Test25 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test25 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test25 = (@iReturnNo_SingleCommodity2_Part_CP1_Test25 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP2_Test25 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_All_CP2_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test25 = (@iReturnNo_SingleCommodity2_Part_CP2_Test25 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_MultiPackagingCommodity_Part_CP2_Test25 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test25 = (@iReturnNo_SingleCommodity_All_CP3_Test25 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test25 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test25 = (@iReturnNo_CompositionCommodity_Part_CP3_Test25 * @dPrice_CompositionCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test25 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test25 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test25 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test25 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test25, @iResultVerification_CP2_Test25, @iResultVerification_CP3_Test25;
SELECT IF(@iResultVerification_CP1_Test25 = 1 AND @iResultVerification_CP2_Test25 = 1 AND @iResultVerification_CP3_Test25 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case25 Result';





SELECT '-------------------- Case26:����26���˵���14�����������һ�����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test26 ='2099/06/26 08:08:08';
SET @iResultVerification_CP1_Test26 = 0;
SET @iResultVerification_CP2_Test26 = 0;
SET @iResultVerification_CP3_Test26 = 0;
-- �Ե���14�ŵ���Ϊ"LS2099061400000000000001"�����۵�����ȫ���˻�(��52.5Ԫ)
SET @iReturnNo_SingleCommodity2_All_CP1_Test26 = 9;
SET @iReturnNo_CompositionCommodity_All_CP1_Test26 = 1;
SET @iReturnRetailTradeID_All_CP1_Test26 = 0;
-- ���������˻���1 (��9����ͨ��Ʒ2��1�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000001_1', 3, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test14, @dSaleDatetime_Test26, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test26 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test26, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_All_CP1_Test26, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_All_CP1_Test26, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test26, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test26, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test26, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �Ե���14�ŵ���Ϊ"LS2099061400000000000002"�����۵����в����˻�(��89Ԫ)
SET @iReturnNo_ServiceCommodity_Part_CP1_Test26 = 1;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test26 = 0;
-- ���������˻���2 (��1�����װ��Ʒ��1��������Ʒ����89Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000002_1', 3, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test14, @dSaleDatetime_Test26, 89, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test26 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test26, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_CP1_Test26, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_CP1_Test26, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test26, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test26, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061400000000000003"�����۵�����ȫ���˻�(��34.93Ԫ)
SET @iReturnNo_SingleCommodity2_All_CP2_Test26 = 5;
SET @iReturnNo_CompositionCommodity_All_CP2_Test26 = 1;
SET @iReturnRetailTradeID_All_CP2_Test26 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ2��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000003_1', 3, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test14, @dSaleDatetime_Test26, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test26 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test26, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_CP2_Test26, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_CP2_Test26, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test26, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iReturnNo_CompositionCommodity_All_CP2_Test26, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_All_CP2_Test26, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �Ե���14�ŵ���Ϊ"LS2099061400000000000004"�����۵����в����˻�(��19.97Ԫ)
SET @iReturnNo_SingleCommodity_Part_CP2_Test26 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test26 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ1��1��������Ʒ����19.97Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000004_1', 3, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test14, @dSaleDatetime_Test26, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test26 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test26, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_Part_CP2_Test26, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_Part_CP2_Test26, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test26, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test26, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test26, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061400000000000005"�����۵�����ȫ���˻�(��24.4Ԫ)
SET @iReturnNo_SingleCommodity2_All_CP3_Test26 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test26 = 1;
SET @iReturnRetailTradeID_All_CP3_Test26 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ2��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000005_1', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test14, @dSaleDatetime_Test26, 24.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test26 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test26, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_CP3_Test26, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_CP3_Test26, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test26, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test26, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test26, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �Ե���14�ŵ���Ϊ"LS2099061400000000000006"�����۵����в����˻�(��34.92Ԫ)
SET @iReturnNo_SingleCommodity_Part_CP3_Test26 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test26 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test26 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ1��1�������Ʒ����34.92Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061400000000000006_1', 3, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test26, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test14, @dSaleDatetime_Test26, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test26 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test26, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_Part_CP3_Test26, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_Part_CP3_Test26, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test26, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test26, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test26, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test26, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d');

SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP1_Test26 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test26 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test26 = (@iReturnNo_ServiceCommodity_Part_CP1_Test26 * @dPrice_ServiceCommodity_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test26 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP2_Test26 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_CompositionCommodity_All_CP2_Test26 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test26 = (@iReturnNo_SingleCommodity_Part_CP2_Test26 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test26 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test26 = (@iReturnNo_SingleCommodity2_All_CP3_Test26 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test26 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test26 = (@iReturnNo_SingleCommodity_Part_CP3_Test26 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test26 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test26 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test26 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test26 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test26, @iResultVerification_CP2_Test26, @iResultVerification_CP3_Test26;
SELECT IF(@iResultVerification_CP1_Test26 = 1 AND @iResultVerification_CP2_Test26 = 1 AND @iResultVerification_CP3_Test26 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case26 Result';





SELECT '-------------------- Case27:����27���˵���15�����������һ�����������������Ʒ(û��������Ʒ) -------------------------' AS 'Case27';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test27 ='2099/06/27 08:08:08';
SET @iResultVerification_CP1_Test27 = 0;
SET @iResultVerification_CP2_Test27 = 0;
SET @iResultVerification_CP3_Test27 = 0;
-- �Ե���14�ŵ���Ϊ"LS2099061500000000000001"�����۵�����ȫ���˻�(��56Ԫ)
SET @iReturnNo_SingleCommodity_All_CP1_Test27 = 9;
SET @iReturnNo_SingleCommodity2_All_CP1_Test27 = 1;
SET @iReturnNo_CompositionCommodity_All_CP1_Test27 = 1;
SET @iReturnRetailTradeID_All_CP1_Test27 = 0;
-- ���������˻���1 (��9����ͨ��Ʒ1��1����ͨ��Ʒ2��1�������Ʒ����56Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000001_1', 4, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test15, @dSaleDatetime_Test27, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test27 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test27, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test27, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_All_CP1_Test27, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_All_CP1_Test27, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test27, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test27, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test27, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061500000000000002"�����۵����в����˻�(��5Ԫ)
SET @iReturnNo_ServiceCommodity_Part_CP1_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP1_Test27 = 0;
-- ���������˻���2 (��1�����װ��Ʒ��1��������Ʒ����5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000002_1', 4, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test15, @dSaleDatetime_Test27, 5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test27 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test27, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iReturnNo_ServiceCommodity_Part_CP1_Test27, @dPrice_ServiceCommodity_CategoryParent1, @iReturnNo_ServiceCommodity_Part_CP1_Test27, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061500000000000003"�����۵�����ȫ���˻�(��49.9Ԫ)
SET @iReturnNo_SingleCommodity_All_CP2_Test27 = 5;
SET @iReturnNo_SingleCommodity2_All_CP2_Test27 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test27 = 1;
SET @iReturnRetailTradeID_All_CP2_Test27 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��3����ͨ��Ʒ2��1�����װ��Ʒ����49.9Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000003_1', 4, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test15, @dSaleDatetime_Test27, 49.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test27 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test27, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test27, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_All_CP2_Test27, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_All_CP2_Test27, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test27, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test27, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test27, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061500000000000004"�����۵����в����˻�(��49.9Ԫ)
SET @iReturnNo_CompositionCommodity_Part_CP2_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test27 = 0;
-- ���������˻���2 (��1�������Ʒ����49.9Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000004_1', 4, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test15, @dSaleDatetime_Test27, 49.9, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test27 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test27, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_CP2_Test27, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_CP2_Test27, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061500000000000005"�����۵�����ȫ���˻�(��36.04Ԫ)
SET @iReturnNo_SingleCommodity_All_CP3_Test27 = 5;
SET @iReturnNo_SingleCommodity2_All_CP3_Test27 = 3;
SET @iReturnNo_ServiceCommodity_All_CP3_Test27 = 1;
SET @iReturnRetailTradeID_All_CP3_Test27 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��3����ͨ��Ʒ2��1��������Ʒ����36.04Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000005_1', 4, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test15, @dSaleDatetime_Test27, 36.04, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test27 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test27, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test27, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity2_All_CP3_Test27, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_All_CP3_Test27, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test27, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test27, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test27, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- �Ե���14�ŵ���Ϊ"LS2099061500000000000006"�����۵����в����˻�(��19.4Ԫ)
SET @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test27 = 0;
-- ���������˻���2 (��1�����װ��Ʒ����19.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099061500000000000006_1', 4, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test27, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test15, @dSaleDatetime_Test27, 19.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test27 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test27, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27, @dPrice_MultiPackagingCommodity_CategoryParent3, @iReturnNo_MultiPackagingCommodity_Part_CP3_Test27, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test27, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d');

SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP1_Test27 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_SingleCommodity2_All_CP1_Test27 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test27 = (@iReturnNo_CompositionCommodity_All_CP1_Test27 * @dPrice_CompositionCommodity_CategoryParent1) + (@iReturnNo_ServiceCommodity_Part_CP1_Test27 * @dPrice_ServiceCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP1_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP2_Test27 * @dPrice_SingleCommodity_CategoryParent2) + (@iReturnNo_SingleCommodity2_All_CP2_Test27 * @dPrice_SingleCommodity2_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test27 = (@iReturnNo_MultiPackagingCommodity_All_CP2_Test27 * @dPrice_MultiPackagingCommodity_CategoryParent2) + (@iReturnNo_CompositionCommodity_Part_CP2_Test27 * @dPrice_CompositionCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP2_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test27 = (@iReturnNo_SingleCommodity_All_CP3_Test27 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_SingleCommodity2_All_CP3_Test27 * @dPrice_SingleCommodity2_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test27 = (@iReturnNo_ServiceCommodity_All_CP3_Test27 * @dPrice_ServiceCommodity_CategoryParent3) + (@iReturnNo_MultiPackagingCommodity_Part_CP3_Test27 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test27 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = 0 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test27 - @dReturnRetailTradeID_Part_CP3_TotalAmount_Test27 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test27, @iResultVerification_CP2_Test27, @iResultVerification_CP3_Test27;
SELECT IF(@iResultVerification_CP1_Test27 = 1 AND @iResultVerification_CP2_Test27 = 1 AND @iResultVerification_CP3_Test27 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case27 Result';




SELECT '-------------------- Case28:����28���������һ����������Ʒ�����ҵ�������˻� -------------------------' AS 'Case28';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test28 ='2099/06/28 08:08:08';
SET @iResultVerification_CP1_Test28 = 0;
SET @iResultVerification_CP2_Test28 = 0;
-- �������һ����Ʒ
SET @iNO_SingleCommodity_CP1_Test28 = 3;
SET @iNO_CompositionCommodity_CP1_Test28 = 2;
SET @iNO_SingleCommodity2_CP1_Test28 = 5;
SET @iNO_MultiPackagingCommodity_CP1_Test28 = 3;
SET @iNO_ServiceCommodity_CP1_Test28 = 1;
SET @iRetailTradeID1_CP1_Test28 = 0;
SET @iRetailTradeID2_CP1_Test28 = 0;
-- �������۵�1(����3����ͨ��Ʒ1��2�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000001', 1, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP1_Test28 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test28, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iNO_SingleCommodity_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, @iNO_SingleCommodity_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP1_Test28, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iNO_CompositionCommodity_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, @iNO_CompositionCommodity_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);
-- �������۵�2(����5����ͨ��Ʒ2��3�����װ��Ʒ��1��������Ʒ����274.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000002', 1, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 274.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP1_Test28 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iNO_MultiPackagingCommodity_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, @iNO_MultiPackagingCommodity_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iServiceCommodityID_CategoryParent1, '���_CategoryParent1', 1, @iNO_ServiceCommodity_CP1_Test28, @dPrice_ServiceCommodity_CategoryParent1, @iNO_ServiceCommodity_CP1_Test28, @dPrice_ServiceCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP1_Test28, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iNO_SingleCommodity2_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, @iNO_SingleCommodity2_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test28 = 3;
SET @iNO_MultiPackagingCommodity_CP2_Test28 = 2;
SET @iNO_SingleCommodity2_CP2_Test28 = 3;
SET @iNO_CompositionCommodity_CP2_Test28 = 2;
SET @iNO_ServiceCommodity_CP2_Test28 = 1;
SET @iRetailTradeID1_CP2_Test28 = 0;
SET @iRetailTradeID2_CP2_Test28 = 0;
-- �������۵�1(����3����ͨ��Ʒ1��2�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000003', 5, 1, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test28 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test28, @iMultiPackagingCommodityID_CategoryParent2, 'һ�������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test28, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2(����3����ͨ��Ʒ2��2�������Ʒ��1��������Ʒ����119.77Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000004', 5, 2, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test28, 119.77, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test28 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test28, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test28, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test28, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062800000000000001"�����۵�����ȫ���˻�(��52.5Ԫ)
SET @iReturnNo_SingleCommodity_All_CP1_Test28 = 3;
SET @iReturnNo_CompositionCommodity_All_CP1_Test28 = 2;
SET @iReturnRetailTradeID_All_CP1_Test28 = 0;
-- ���������˻���1(��3����ͨ��Ʒ1��2�������Ʒ����52.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000001_1', 1, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP1_Test28, @dSaleDatetime_Test28, 52.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP1_Test28 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test28, @iSingleCommodityID_CategoryParent1, '�ɿڿ���_CategoryParent1', 1, @iReturnNo_SingleCommodity_All_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, @iReturnNo_SingleCommodity_All_CP1_Test28, @dPrice_SingleCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP1_Test28, @iCompositionCommodityID_CategoryParent1, '���ִ����_CategoryParent1', 1, @iReturnNo_CompositionCommodity_All_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, @iReturnNo_CompositionCommodity_All_CP1_Test28, @dPrice_CompositionCommodity_CategoryParent1, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062800000000000002"�����۵����в����˻�(��269.5Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP1_Test28 = 5;
SET @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28 = 3;
SET @iReturnRetailTradeID_Part_CP1_Test28 = 0;
-- ���������˻���2(��5����ͨ��Ʒ2��3�����װ��Ʒ����269.5Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000002_1', 1, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP1_Test28, @dSaleDatetime_Test28, 269.5, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP1_Test28 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test28, @iMultiPackagingCommodityID_CategoryParent1, 'һ�����¿���_CategoryParent1', 1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, @iReturnNo_MultiPackagingCommodity_Part_CP1_Test28, @dPrice_MultiPackagingCommodity_CategoryParent1, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP1_Test28, @iSingleCommodityID2_CategoryParent1, '���¿���_CategoryParent1', 1, @iReturnNo_SingleCommodity2_Part_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, @iReturnNo_SingleCommodity2_Part_CP1_Test28, @dPrice_SingleCommodity2_CategoryParent1, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062800000000000003"�����۵�����ȫ���˻�(��34.93Ԫ)
SET @iReturnNo_SingleCommodity_All_CP2_Test28 = 3;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test28 = 2;
SET @iReturnRetailTradeID_All_CP2_Test28 = 0;
-- ���������˻���1(��3����ͨ��Ʒ1��2�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000003_1', 5, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........',@iRetailTradeID1_CP2_Test28, @dSaleDatetime_Test28, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test28 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test28, @iMultiPackagingCommodityID_CategoryParent2, 'һ�������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test28, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test28, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test28, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062800000000000004"�����۵����в����˻�(��19.97Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test28 = 3;
SET @iReturnNo_ServiceCommodity_Part_CP2_Test28 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test28 = 0;
-- ���������˻���2(��3����ͨ��Ʒ2��1��������Ʒ����19.97Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062800000000000004_1', 5, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test28, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test28, @dSaleDatetime_Test28, 19.97, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test28 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test28, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test28, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test28, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iReturnNo_ServiceCommodity_Part_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, @iReturnNo_ServiceCommodity_Part_CP2_Test28, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test28, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d');

SET @dRetailTradeID1_CP1_TotalAmount_Test28 = (@iNO_SingleCommodity_CP1_Test28 * @dPrice_SingleCommodity_CategoryParent1) + (@iNO_CompositionCommodity_CP1_Test28 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dRetailTradeID2_CP1_TotalAmount_Test28 = (@iNO_MultiPackagingCommodity_CP1_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent1) + (@iNO_ServiceCommodity_CP1_Test28 * @dPrice_ServiceCommodity_CategoryParent1) -- 
												+ (@iNO_SingleCommodity2_CP1_Test28 * @dPrice_SingleCommodity2_CategoryParent1);
SET @dReturnRetailTradeID_All_CP1_TotalAmount_Test28 = (@iReturnNo_SingleCommodity_All_CP1_Test28 * @dPrice_SingleCommodity_CategoryParent1) + (@iReturnNo_CompositionCommodity_All_CP1_Test28 * @dPrice_CompositionCommodity_CategoryParent1);
SET @dReturnRetailTradeID_Part_CP1_TotalAmount_Test28 = (@iReturnNo_SingleCommodity2_Part_CP1_Test28 * @dPrice_SingleCommodity2_CategoryParent1) + (@iReturnNo_MultiPackagingCommodity_Part_CP1_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent1);
SELECT 1 INTO @iResultVerification_CP1_Test28 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID1 --  
		AND F_TotalAmount = @dRetailTradeID1_CP1_TotalAmount_Test28 + @dRetailTradeID2_CP1_TotalAmount_Test28 - @dReturnRetailTradeID_All_CP1_TotalAmount_Test28 --  
		- @dReturnRetailTradeID_Part_CP1_TotalAmount_Test28 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP2_TotalAmount_Test28 = (@iNO_MultiPackagingCommodity_CP2_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent2) + (@iNO_SingleCommodity_CP2_Test28 * @dPrice_SingleCommodity_CategoryParent2);
SET @dRetailTradeID2_CP2_TotalAmount_Test28 = (@iNO_SingleCommodity2_CP2_Test28 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_CompositionCommodity_CP2_Test28 * @dPrice_CompositionCommodity_CategoryParent2) -- 
												+ (@iNO_ServiceCommodity_CP2_Test28 * @dPrice_ServiceCommodity_CategoryParent2);
SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test28 = (@iReturnNo_MultiPackagingCommodity_All_CP2_Test28 * @dPrice_MultiPackagingCommodity_CategoryParent2) +(@iReturnNo_SingleCommodity_All_CP2_Test28 * @dPrice_SingleCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test28 = (@iReturnNo_SingleCommodity2_Part_CP2_Test28 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_ServiceCommodity_Part_CP2_Test28 * @dPrice_ServiceCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test28 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_CP2_TotalAmount_Test28 + @dRetailTradeID2_CP2_TotalAmount_Test28 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test28 -- 
		- @dReturnRetailTradeID_Part_CP2_TotalAmount_Test28 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP1_Test28, @iResultVerification_CP2_Test28;
SELECT IF(@iResultVerification_CP1_Test28 = 1 AND @iResultVerification_CP2_Test28 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case28 Result';









SELECT '-------------------- Case29:����29���������������������Ʒ�����ҵ�������˻� -------------------------' AS 'Case29';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test29 ='2099/06/29 08:08:08';
SET @iResultVerification_CP2_Test29 = 0;
SET @iResultVerification_CP3_Test29 = 0;
-- ������������Ʒ
SET @iNO_SingleCommodity_CP2_Test29 = 5;
SET @iNO_MultiPackagingCommodity_CP2_Test29 = 1;
SET @iNO_SingleCommodity2_CP2_Test29 = 3;
SET @iNO_CompositionCommodity_CP2_Test29 = 1;
SET @iNO_ServiceCommodity_CP2_Test29 = 1;
SET @iRetailTradeID1_CP2_Test29 = 0;
SET @iRetailTradeID2_CP2_Test29 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000001', 7, 3, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP2_Test29 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test29, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iNO_SingleCommodity_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, @iNO_SingleCommodity_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP2_Test29, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iNO_MultiPackagingCommodity_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, @iNO_MultiPackagingCommodity_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1��������Ʒ����69.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000002', 7, 4, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 69.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP2_Test29 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iNO_SingleCommodity2_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, @iNO_SingleCommodity2_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iServiceCommodityID_CategoryParent2, '���_CategoryParent2', 1, @iNO_ServiceCommodity_CP2_Test29, @dPrice_ServiceCommodity_CategoryParent2, @iNO_ServiceCommodity_CP2_Test29, @dPrice_ServiceCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP2_Test29, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iNO_CompositionCommodity_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, @iNO_CompositionCommodity_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test29 = 5;
SET @iNO_ServiceCommodity_CP3_Test29 = 1;
SET @iNO_SingleCommodity2_CP3_Test29 = 3;
SET @iNO_CompositionCommodity_CP3_Test29 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test29 = 1;
SET @iRetailTradeID1_CP3_Test29 = 0;
SET @iRetailTradeID2_CP3_Test29 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000003', 7, 5, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test29 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test29, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test29, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000004', 7, 6, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test29, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test29 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test29, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test29, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test29, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062900000000000001"�����۵�����ȫ���˻�(��34.93Ԫ)
SET @iReturnNo_SingleCommodity_All_CP2_Test29 = 5;
SET @iReturnNo_MultiPackagingCommodity_All_CP2_Test29 = 1;
SET @iReturnRetailTradeID_All_CP2_Test29 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1�����װ��Ʒ����34.93Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000001', 3, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP2_Test29, @dSaleDatetime_Test29, 34.93, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP2_Test29 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test29, @iSingleCommodityID_CategoryParent2, '������Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity_All_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, @iReturnNo_SingleCommodity_All_CP2_Test29, @dPrice_SingleCommodity_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP2_Test29, @iMultiPackagingCommodityID_CategoryParent2, 'һ��������Ƭ_CategoryParent2', 1, @iReturnNo_MultiPackagingCommodity_All_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, @iReturnNo_MultiPackagingCommodity_All_CP2_Test29, @dPrice_MultiPackagingCommodity_CategoryParent2, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062900000000000002"�����۵����в����˻�(��64.87Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP2_Test29 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP2_Test29 = 1;
SET @iReturnRetailTradeID_Part_CP2_Test29 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ2��1�������Ʒ����64.87Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000002', 4, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP2_Test29, @dSaleDatetime_Test29, 64.87, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP2_Test29 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test29, @iSingleCommodityID2_CategoryParent2, '�ɱȿ���Ƭ_CategoryParent2', 1, @iReturnNo_SingleCommodity2_Part_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, @iReturnNo_SingleCommodity2_Part_CP2_Test29, @dPrice_SingleCommodity2_CategoryParent2, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP2_Test29, @iCompositionCommodityID_CategoryParent2, '��Ƭ�����_CategoryParent2', 1, @iReturnNo_CompositionCommodity_Part_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, @iReturnNo_CompositionCommodity_Part_CP2_Test29, @dPrice_CompositionCommodity_CategoryParent2, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062900000000000003"�����۵�����ȫ���˻�(��24.4Ԫ)
SET @iReturnNo_SingleCommodity_All_CP3_Test29 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test29 = 1;
SET @iReturnRetailTradeID_All_CP3_Test29 = 0;
-- ���������˻���1 (��5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000003', 5, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test29, @dSaleDatetime_Test29, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test29 = last_insert_id();
-- ���������˻���Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test29, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test29, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test29, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test29, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099062900000000000004"�����۵����в����˻�(��34.92Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP3_Test29 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test29 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test29 = 0;
-- ���������˻���2 (��3����ͨ��Ʒ2��1�������Ʒ����34.92Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099062900000000000004', 6, 7, 'url=ashasoadigmnalskd', @dSaleDatetime_Test29, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test29, @dSaleDatetime_Test29, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test29 = last_insert_id();
-- ���������˻���Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test29, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity2_Part_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_Part_CP3_Test29, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test29, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test29, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test29, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d');

SET @dRetailTradeID1_CP2_TotalAmount_Test29 = (@iNO_SingleCommodity_CP2_Test29 * @dPrice_SingleCommodity_CategoryParent2) + (@iNO_MultiPackagingCommodity_CP2_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dRetailTradeID2_CP2_TotalAmount_Test29 = (@iNO_SingleCommodity2_CP2_Test29 * @dPrice_SingleCommodity2_CategoryParent2) + (@iNO_ServiceCommodity_CP2_Test29 * @dPrice_ServiceCommodity_CategoryParent2) -- 
											+ (@iNO_CompositionCommodity_CP2_Test29 * @dPrice_CompositionCommodity_CategoryParent2);
SET @dReturnRetailTradeID_All_CP2_TotalAmount_Test29 = (@iReturnNo_SingleCommodity_All_CP2_Test29 * @dPrice_SingleCommodity_CategoryParent2) +(@iReturnNo_MultiPackagingCommodity_All_CP2_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent2);
SET @dReturnRetailTradeID_Part_CP2_TotalAmount_Test29 = (@iReturnNo_SingleCommodity2_Part_CP2_Test29 * @dPrice_SingleCommodity2_CategoryParent2) + (@iReturnNo_CompositionCommodity_Part_CP2_Test29 * @dPrice_CompositionCommodity_CategoryParent2);
SELECT 1 INTO @iResultVerification_CP2_Test29 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID2 --  
		AND F_TotalAmount = @dRetailTradeID1_CP2_TotalAmount_Test29 + @dRetailTradeID2_CP2_TotalAmount_Test29 - @dReturnRetailTradeID_All_CP2_TotalAmount_Test29 -- 
		- @dReturnRetailTradeID_Part_CP2_TotalAmount_Test29 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP3_TotalAmount_Test29 = (@iNO_SingleCommodity_CP3_Test29 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test29 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_CP3_TotalAmount_Test29 = (@iNO_SingleCommodity2_CP3_Test29 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test29 * @dPrice_CompositionCommodity_CategoryParent3) -- 
												+ (@iNO_MultiPackagingCommodity_CP3_Test29 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test29 = (@iReturnNo_SingleCommodity_All_CP3_Test29 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test29 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test29 = (@iReturnNo_SingleCommodity2_Part_CP3_Test29 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test29 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test29 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_CP3_TotalAmount_Test29 + @dRetailTradeID2_CP3_TotalAmount_Test29 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test29 -- 
		- @dReturnRetailTradeID_Part_CP3_TotalAmount_Test29 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP2_Test29, @iResultVerification_CP3_Test29;
SELECT IF(@iResultVerification_CP2_Test29 = 1 AND @iResultVerification_CP3_Test29 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case29 Result';










SELECT '-------------------- Case30:����30�����������������ĵ���Ʒ�����ҵ�������˻� -------------------------' AS 'Case30';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime_Test30 ='2099/06/30 08:08:08';
SET @iResultVerification_CP3_Test30 = 0;
SET @iResultVerification_CP4_Test30 = 0;
-- �������������Ʒ
SET @iNO_SingleCommodity_CP3_Test30 = 5;
SET @iNO_ServiceCommodity_CP3_Test30 = 1;
SET @iNO_SingleCommodity2_CP3_Test30 = 3;
SET @iNO_CompositionCommodity_CP3_Test30 = 1;
SET @iNO_MultiPackagingCommodity_CP3_Test30 = 1;
SET @iRetailTradeID1_CP3_Test30 = 0;
SET @iRetailTradeID2_CP3_Test30 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000001', 2, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP3_Test30 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test30, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iNO_SingleCommodity_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, @iNO_SingleCommodity_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP3_Test30, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iNO_ServiceCommodity_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, @iNO_ServiceCommodity_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ��1�����װ��Ʒ����54.32Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000002', 4, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 54.32, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP3_Test30 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iNO_SingleCommodity2_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, @iNO_SingleCommodity2_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iNO_CompositionCommodity_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, @iNO_CompositionCommodity_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP3_Test30, @iMultiPackagingCommodityID_CategoryParent3, 'һ������ţ��_CategoryParent3', 1, @iNO_MultiPackagingCommodity_CP3_Test30, @dPrice_MultiPackagingCommodity_CategoryParent3, @iNO_MultiPackagingCommodity_CP3_Test30, @dPrice_MultiPackagingCommodity_CategoryParent3, NULL, NULL);

-- ��������ĵ���Ʒ
SET @iNO_SingleCommodity_CP4_Test30 = 2;
SET @iNO_CompositionCommodity_CP4_Test30 = 1;
SET @iNO_SingleCommodity2_CP4_Test30 = 3;
SET @iNO_ServiceCommodity_CP4_Test30 = 1;
SET @iNO_MultiPackagingCommodity_CP4_Test30 = 1;
SET @iRetailTradeID1_CP4_Test30 = 0;
SET @iRetailTradeID2_CP4_Test30 = 0;
-- �������۵�1 (����2����ͨ��Ʒ1��1�������Ʒ����247.6Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000003', 3, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID1_CP4_Test30 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test30, @iSingleCommodityID_CategoryParent4, '��������_CategoryParent4', 1, @iNO_SingleCommodity_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, @iNO_SingleCommodity_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID1_CP4_Test30, @iCompositionCommodityID_CategoryParent4, '�������_CategoryParent4', 1, @iNO_CompositionCommodity_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, @iNO_CompositionCommodity_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);
-- �������۵�2 (����3����ͨ��Ʒ2��1�����װ��Ʒ��1��������Ʒ����265Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000004', 1, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', -1, @dSaleDatetime_Test30, 265, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailTradeID2_CP4_Test30 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iSingleCommodityID2_CategoryParent4, '������_CategoryParent4', 1, @iNO_SingleCommodity2_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, @iNO_SingleCommodity2_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iServiceCommodityID_CategoryParent4, '���_CategoryParent4', 1, @iNO_ServiceCommodity_CP4_Test30, @dPrice_ServiceCommodity_CategoryParent4, @iNO_ServiceCommodity_CP4_Test30, @dPrice_ServiceCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iRetailTradeID2_CP4_Test30, @iMultiPackagingCommodityID_CategoryParent4, 'һ��������_CategoryParent4', 1, @iNO_MultiPackagingCommodity_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, @iNO_MultiPackagingCommodity_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099063000000000000001"�����۵�����ȫ���˻�(��24.4Ԫ)
SET @iReturnNo_SingleCommodity_All_CP3_Test30 = 5;
SET @iReturnNo_ServiceCommodity_All_CP3_Test30 = 1;
SET @iReturnRetailTradeID_All_CP3_Test30 = 0;
-- �������۵�1 (����5����ͨ��Ʒ1��1��������Ʒ����24.4Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000001_1', 5, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID1_CP3_Test30, @dSaleDatetime_Test30, 24.4, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP3_Test30 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test30, @iSingleCommodityID_CategoryParent3, '����ţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity_All_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, @iReturnNo_SingleCommodity_All_CP3_Test30, @dPrice_SingleCommodity_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP3_Test30, @iServiceCommodityID_CategoryParent3, '���_CategoryParent3', 1, @iReturnNo_ServiceCommodity_All_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, @iReturnNo_ServiceCommodity_All_CP3_Test30, @dPrice_ServiceCommodity_CategoryParent3, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099063000000000000002"�����۵����в����˻�(��34.92Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP3_Test30 = 3;
SET @iReturnNo_CompositionCommodity_Part_CP3_Test30 = 1;
SET @iReturnRetailTradeID_Part_CP3_Test30 = 0;
-- �������۵�2 (����3����ͨ��Ʒ2��1�������Ʒ����34.92Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000002_1', 6, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP3_Test30, @dSaleDatetime_Test30, 34.92, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP3_Test30 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test30, @iSingleCommodityID2_CategoryParent3, '��ţţ��_CategoryParent3', 1, @iReturnNo_SingleCommodity2_Part_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, @iReturnNo_SingleCommodity2_Part_CP3_Test30, @dPrice_SingleCommodity2_CategoryParent3, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP3_Test30, @iCompositionCommodityID_CategoryParent3, 'ţ�̴����_CategoryParent3', 1, @iReturnNo_CompositionCommodity_Part_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, @iReturnNo_CompositionCommodity_Part_CP3_Test30, @dPrice_CompositionCommodity_CategoryParent3, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099063000000000000003"�����۵�����ȫ���˻�(��247.6Ԫ)
SET @iReturnNo_SingleCommodity_All_CP4_Test30 = 2;
SET @iReturnNo_CompositionCommodity_All_CP4_Test30 = 1;
SET @iReturnRetailTradeID_All_CP4_Test30 = 0;
-- �������۵�1 (����2����ͨ��Ʒ1��1�������Ʒ����247.6Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000003_1', 7, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........',@iRetailTradeID1_CP4_Test30, @dSaleDatetime_Test30, 247.6, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_All_CP4_Test30 = last_insert_id();
-- ����������Ʒ1
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test30, @iSingleCommodityID_CategoryParent4, '��������_CategoryParent4', 1, @iReturnNo_SingleCommodity_All_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, @iReturnNo_SingleCommodity_All_CP4_Test30, @dPrice_SingleCommodity_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_All_CP4_Test30, @iCompositionCommodityID_CategoryParent4, '�������_CategoryParent4', 1, @iReturnNo_CompositionCommodity_All_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, @iReturnNo_CompositionCommodity_All_CP4_Test30, @dPrice_CompositionCommodity_CategoryParent4, NULL, NULL);

-- �Ե��쵥��Ϊ"LS2099063000000000000004"�����۵����в����˻�(��260Ԫ)
SET @iReturnNo_SingleCommodity2_Part_CP4_Test30 = 3;
SET @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30 = 1;
SET @iReturnRetailTradeID_Part_CP4_Test30 = 0;
-- �������۵�2 (����3����ͨ��Ʒ2��1�����װ��Ʒ����260Ԫ)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, 
F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, 
F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (NULL, 'LS2099063000000000000004_1', 8, 8, 'url=ashasoadigmnalskd', @dSaleDatetime_Test30, 2, 4, '0', 
1, '........', @iRetailTradeID2_CP4_Test30, @dSaleDatetime_Test30, 260, 1, 0, 0, 
0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iReturnRetailTradeID_Part_CP4_Test30 = last_insert_id();
-- ����������Ʒ2
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test30, @iSingleCommodityID2_CategoryParent4, '������_CategoryParent4', 1, @iReturnNo_SingleCommodity2_Part_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, @iReturnNo_SingleCommodity2_Part_CP4_Test30, @dPrice_SingleCommodity2_CategoryParent4, NULL, NULL);
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnRetailTradeID_Part_CP4_Test30, @iMultiPackagingCommodityID_CategoryParent4, 'һ��������_CategoryParent4', 1, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, @iReturnNo_MultiPackagingCommodity_Part_CP4_Test30, @dPrice_MultiPackagingCommodity_CategoryParent4, NULL, NULL);
-- 
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByCategoryParent_Create(@iErrorCode,@sErrorMsg,@iShopID,@dSaleDatetime_Test30, @deleteOldData);
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT * FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d');

SET @dRetailTradeID1_CP3_TotalAmount_Test30 = (@iNO_SingleCommodity_CP3_Test30 * @dPrice_SingleCommodity_CategoryParent3) + (@iNO_ServiceCommodity_CP3_Test30 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dRetailTradeID2_CP3_TotalAmount_Test30 = (@iNO_SingleCommodity2_CP3_Test30 * @dPrice_SingleCommodity2_CategoryParent3) + (@iNO_CompositionCommodity_CP3_Test30 * @dPrice_CompositionCommodity_CategoryParent3) -- 
	   											+ (@iNO_MultiPackagingCommodity_CP3_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent3);
SET @dReturnRetailTradeID_All_CP3_TotalAmount_Test30 = (@iReturnNo_SingleCommodity_All_CP3_Test30 * @dPrice_SingleCommodity_CategoryParent3) + (@iReturnNo_ServiceCommodity_All_CP3_Test30 * @dPrice_ServiceCommodity_CategoryParent3);
SET @dReturnRetailTradeID_Part_CP3_TotalAmount_Test30 = (@iReturnNo_SingleCommodity2_Part_CP3_Test30 * @dPrice_SingleCommodity2_CategoryParent3) + (@iReturnNo_CompositionCommodity_Part_CP3_Test30 * @dPrice_CompositionCommodity_CategoryParent3);
SELECT 1 INTO @iResultVerification_CP3_Test30 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID3 --  
		AND F_TotalAmount = @dRetailTradeID1_CP3_TotalAmount_Test30 + @dRetailTradeID2_CP3_TotalAmount_Test30 - @dReturnRetailTradeID_All_CP3_TotalAmount_Test30 -- 
		- @dReturnRetailTradeID_Part_CP3_TotalAmount_Test30 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d'); -- 

SET @dRetailTradeID1_CP4_TotalAmount_Test30 = (@iNO_SingleCommodity_CP4_Test30 * @dPrice_SingleCommodity_CategoryParent4) + (@iNO_CompositionCommodity_CP4_Test30 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dRetailTradeID2_CP4_TotalAmount_Test30 = (@iNO_SingleCommodity2_CP4_Test30 * @dPrice_SingleCommodity2_CategoryParent4) + (@iNO_ServiceCommodity_CP4_Test30 * @dPrice_ServiceCommodity_CategoryParent4) -- 
												+ (@iNO_MultiPackagingCommodity_CP4_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SET @dReturnRetailTradeID_All_CP4_TotalAmount_Test30 = (@iReturnNo_SingleCommodity_All_CP4_Test30 * @dPrice_SingleCommodity_CategoryParent4) + (@iReturnNo_CompositionCommodity_All_CP4_Test30 * @dPrice_CompositionCommodity_CategoryParent4);
SET @dReturnRetailTradeID_Part_CP4_TotalAmount_Test30 = (@iReturnNo_SingleCommodity2_Part_CP4_Test30 * @dPrice_SingleCommodity2_CategoryParent4) + (@iReturnNo_MultiPackagingCommodity_Part_CP4_Test30 * @dPrice_MultiPackagingCommodity_CategoryParent4);
SELECT 1 INTO @iResultVerification_CP4_Test30 FROM t_retailtradedailyreportbycategoryparent WHERE F_CategoryParentID = @iCategoryParentID4 --  
		AND F_TotalAmount = @dRetailTradeID1_CP4_TotalAmount_Test30 + @dRetailTradeID2_CP4_TotalAmount_Test30 - @dReturnRetailTradeID_All_CP4_TotalAmount_Test30 -- 
		- @dReturnRetailTradeID_Part_CP4_TotalAmount_Test30 AND F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d'); -- 

SELECT @iResultVerification_CP3_Test30, @iResultVerification_CP4_Test30, @iErrorCode;
SELECT IF(@iResultVerification_CP3_Test30 = 1 AND @iResultVerification_CP4_Test30 = 1 AND @iErrorCode = 0 AND @sErrorMsg = '' ,'���Գɹ�','����ʧ��') AS 'Test Case30 Result';






-- ɾ��2099/06/01���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test1,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test1;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test1;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test1;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test1;
-- ɾ��2099/06/02���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test2,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test2;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test2;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test2;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test2;
-- ɾ��2099/06/03���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test3,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test3;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test3;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test3;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test3;
-- ɾ��2099/06/04���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test4,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test4;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test4;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test4;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test4;
-- ɾ��2099/06/05���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test5,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test5;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test5;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test5;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test5;
-- ɾ��2099/06/06���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test6,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_Test6;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_Test6;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_Test6;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_Test6;
-- ɾ��2099/06/10���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test10,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test10;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test10;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test10;
-- ɾ��2099/06/11���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test11,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP4_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP4_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP4_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP4_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP5_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP5_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP5_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP5_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP6_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP6_Test11;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP6_Test11;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP6_Test11;
-- ɾ��2099/06/12���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test12,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP7_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP7_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP7_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP7_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP8_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP8_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP8_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP8_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP9_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP9_Test12;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP9_Test12;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP9_Test12;
-- ɾ��2099/06/13���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test13,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test13;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test13;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test13;
-- ɾ��2099/06/14���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test14,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test14;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test14;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test14;
-- ɾ��2099/06/15���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test15,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test15;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test15;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test15;
-- ɾ��2099/06/16���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test16,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test16;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test16;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test16;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test16;
-- ɾ��2099/06/17���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test17,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test17;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test17;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test17;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test17;
-- ɾ��2099/06/18���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test18,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test18;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test18;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test18;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test18;
-- ɾ��2099/06/19���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test19,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test19;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test19;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test19;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test19;
-- ɾ��2099/06/20���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test20,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test20;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test20;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test20;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test20;
-- ɾ��2099/06/21���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test21,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_Test21;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_Test21;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_Test21;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_Test21;
-- ɾ��2099/06/22���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test22,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test22;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test22;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test22;
-- ɾ��2099/06/23���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test23,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP4_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP4_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP4_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP4_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP5_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP5_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP5_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP5_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP6_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP6_Test23;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP6_Test23;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP6_Test23;
-- ɾ��2099/06/24���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test24,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP7_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP7_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP7_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP7_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP8_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP8_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP8_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP8_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP9_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP9_Test24;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP9_Test24;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP9_Test24;
-- ɾ��2099/06/25���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test25,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test25;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test25;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test25;
-- ɾ��2099/06/26���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test26,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test26;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test26;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test26;
-- ɾ��2099/06/27���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test27,'%Y-%m-%d');
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test27;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test27;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test27;
-- ɾ��2099/06/28���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test28,'%Y-%m-%d');
-- ���۵�
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test28;
-- �����˻���
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP1_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP1_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test28;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test28;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test28;
-- ɾ��2099/06/29���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test29,'%Y-%m-%d');
-- ���۵�
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test29;
-- �����˻���
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP2_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP2_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test29;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test29;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test29;
-- ɾ��2099/06/30���ձ���
DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(@dSaleDatetime_Test30,'%Y-%m-%d');
-- ���۵�
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID1_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID1_CP4_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailTradeID2_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID2_CP4_Test30;
-- �����˻���
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP3_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP3_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_All_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_All_CP4_Test30;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iReturnRetailTradeID_Part_CP4_Test30;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID_Part_CP4_Test30;





-- ɾ�����һ����Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent1;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent1;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent1;
-- ɾ����������Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent2;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent2;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent2;
-- ɾ�����������Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent3;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent3;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent3;
-- ɾ������ĵ���Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent4;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent4;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent4;
-- ɾ����������Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent5;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent5;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent5;
-- ɾ�����������Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent6;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent6;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent6;
-- ɾ������ߵ���Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent7;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent7;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent7;
-- ɾ�����˵���Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent8;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent8;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent8;
-- ɾ�����ŵ���Ʒ
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCompositionCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iCompositionCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iMultiPackagingCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iServiceCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID_CategoryParent9;
DELETE FROM t_commodity WHERE F_ID = @iSingleCommodityID2_CategoryParent9;
DELETE FROM t_category WHERE F_ID = @iCategoryID_CategoryParent9;