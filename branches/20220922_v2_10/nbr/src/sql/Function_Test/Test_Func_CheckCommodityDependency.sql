SELECT '-------------------- Case1������Ʒ������ȼ�¼������ɾ�� -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2������Ʒ�п�棬����ɾ�� -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 50, 1, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ���п�棬����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3������Ʒ����Դ������������ɾ�� -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (1, @iID, 1, 1);
SET @iRtcSourceID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ����Ʒ��Դ������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRtcSourceID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case4������Ʒ�вɹ�������������ɾ�� -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '11', 1, 'aa', now(), now(), now(), now());
SET @iPurchasingOrderID = Last_insert_id();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_PriceSuggestion,F_BarcodeID,F_PackageUnitID, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, @iID, 100, 'AA', 5, 1, 1, now(), now());
SET @iPurchasingOrderCommodityID = Last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�вɹ�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case5������Ʒ���̵㵥����������ɾ�� -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,300,0,1,now(),'a...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iInventorySheetID, @iID, 1, 1, 0, 0);
SET @iInventorySheetCommodityID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ���̵㵥����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case6������Ʒ�����۵�����������ɾ�� -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011234', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�����۵�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case7������Ʒ����ⵥ����������ɾ�� -------------------------' AS 'Case7';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, @iID, 200, 1, '�ɱȿ���Ƭ', 1, 11.1, 11.1, now(), 36, now(), now(), now());
SET @iWarehousingCommodityID = Last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ����ⵥ����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iID;

-- ֻ�ܲɹ���Ʒ�����ܲɹ����װ��Ʒ
--	SELECT '-------------------- Case8���õ�Ʒ�Ķ��װ��Ʒ�вɹ�������������ɾ�� -------------------------' AS 'Case8';
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',56,3,'1111111', 0,0);
--	SET @iID = last_insert_id();
--	
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',@iID,3,'1111111', 0,2);
--	SET @iRefID = last_insert_id();
--	
--	INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
--	VALUES (0, 1, 1, '11', 1, 'aa', now(), now(), now(), now());
--	SET @iPurchasingOrderID = Last_insert_id();
--	
--	INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_PriceSuggestion,F_BarcodeID,F_PackageUnitID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iPurchasingOrderID, @iRefID, 100, 'AA', 5, 1, 1, now(), now());
--	SET @iPurchasingOrderCommodityID = Last_insert_id();
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ�вɹ�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
--	
--	DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
--	DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;
--	DELETE FROM t_commodity WHERE F_ID = @iRefID;
--	DELETE FROM t_commodity WHERE F_ID = @iID;

-- �����̵��Ԥ��̭״̬��Ԥ��̭״̬����ͨ��Ʒ��
--	SELECT '-------------------- Case9���õ�Ʒ�Ķ��װ��Ʒ���̵㵥����������ɾ�� -------------------------' AS 'Case9';
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',56,3,'1111111', 0,0);
--	SET @iID = last_insert_id();
--	
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',@iID,3,'1111111', 0,2);
--	SET @iRefID = last_insert_id();
--	
--	INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
--	VALUES (1,300,0,1,now(),'a...........................');
--	SET @iInventorySheetID = last_insert_id();
--	
--	INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
--	VALUES ( @iInventorySheetID, @iRefID, 1, 1, 0, 0);
--	SET @iInventorySheetCommodityID = last_insert_id();
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ���̵㵥����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
--	
--	DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorySheetCommodityID;
--	DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;
--	DELETE FROM t_commodity WHERE F_ID = @iRefID;
--	DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case10���õ�Ʒ�Ķ��װ��Ʒ�����۵�����������ɾ�� -------------------------' AS 'Case10';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,3,'1111111',2);
SET @iRefID = last_insert_id();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011235', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iRefID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ�����۵�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodity WHERE F_ID IN (@iRefID,@iID);
-- ֻ������Ԥ��̭״̬����ͨ��Ʒ��
--	SELECT '-------------------- Case11���õ�Ʒ�Ķ��װ��Ʒ����ⵥ����������ɾ�� -------------------------' AS 'Case11';
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',56,3,'1111111', 0,0);
--	SET @iID = last_insert_id();
--	
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',@iID,3,'1111111', 0,2);
--	SET @iRefID = last_insert_id();
--	
--	INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
--	VALUES (0, 3, 1, 1, now(), 1, now());
--	SET @iWarehousingID = Last_insert_id();
--	
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@iWarehousingID, @iRefID, 200, 1, '�ɱȿ���Ƭ', 1, 11.1, 11.1, now(), 36, now(), now(), now());
--	SET @iWarehousingCommodityID = Last_insert_id();
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ����ⵥ����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
--	
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
--	DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
--	DELETE FROM t_commodity WHERE F_ID = @iRefID;
--	DELETE FROM t_commodity WHERE F_ID = @iID;


SELECT '-------------------- Case12������Ʒ����Դ������������ɾ�� -------------------------' AS 'Case12';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (1, @iID, 1, 1);
SET @iRtcSourceID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ����Ʒ��Դ������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRtcSourceID;
DELETE FROM t_commodity WHERE F_ID = @iID;
-- ���ڲ�������Ʒ��ʷ������
-- SELECT '-------------------- Case13������Ʒ����Ʒ��ʷ����������ɾ�� -------------------------' AS 'Case13';
-- INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
-- F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
-- F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
-- VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
-- 8,50,130,11,1,1,'url=116843434834',
-- 3,'15','2018-04-14','20',0,1,'1111111', 0,0);
-- SET @iID = last_insert_id();
-- 
-- INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime)
-- VALUES (@iID, '���ۼ�', 1, 2, 1, 0, NOW());
-- SET @iCommodityHistoryID = last_insert_id();
-- 
-- SET @sErrorMsg = '';
-- SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
-- SELECT IF(@sErrorMsg = '����Ʒ����Ʒ��ʷ����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
-- 
-- DELETE FROM t_commodityhistory WHERE F_ID = @iCommodityHistoryID;
-- DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case14������Ʒ�������ձ����ܱ�����������ɾ�� -------------------------' AS 'Case14';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_retailtradedailyreportsummary (F_ShopID, F_Datetime, F_TotalNO, F_PricePurchase, F_TotalAmount, F_AverageAmountOfCustomer, F_TotalGross, F_TopSaleCommodityID, F_TopSaleCommodityNO, F_TopSaleCommodityAmount, F_TopPurchaseCustomerName, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), 1, 1, 1, 1, 1, @iID, 1, 0, 'a', now(), now());
SET @iRdrID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�������ձ����ܱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

DELETE FROM t_retailtradedailyreportsummary WHERE F_ID = @iRdrID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case15������Ʒ����Ʒ���۱�������������ɾ�� -------------------------' AS 'Case15';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_retailtradedailyreportbycommodity (F_ShopID, F_Datetime, F_CommodityID, F_NO, F_TotalPurchasingAmount, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), @iID, 1, 1, 1, 1, now(), now());
SET @iRdrByCommodityID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ����Ʒ���۱�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';

DELETE FROM t_retailtradedailyreportbycommodity WHERE F_ID = @iRdrByCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iID;
--	���ڲ�������Ʒ��ʷ������
--	SELECT '-------------------- Case16���õ�Ʒ�Ķ��װ��Ʒ����Ʒ��ʷ����������ɾ�� -------------------------' AS 'Case16';
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
-- 8,50,130,11,1,1,'url=116843434834',
-- 3,'15','2018-04-14','20',0,1,'1111111', 0,0);
--	SET @iID = last_insert_id();
--	
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
-- 8,50,130,11,1,1,'url=116843434834',
-- 3,'15','2018-04-14','20',@iID,3,'1111111', 0,2);
--	SET @iRefID = last_insert_id();
--	
--	INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime)
--	VALUES (@iRefID, '���ۼ�', 1, 2, 1, 0, NOW());
--	SET @iCommodityHistoryID = last_insert_id();
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ����Ʒ��ʷ����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';
--	
--	DELETE FROM t_commodityhistory WHERE F_ID = @iCommodityHistoryID;
--	DELETE FROM t_commodity WHERE F_ID = @iRefID;
--	DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case17���õ�Ʒ�Ķ��װ��Ʒ�������ձ����ܱ�����������ɾ�� -------------------------' AS 'Case17';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,3,'1111111',2);
SET @iRefID = last_insert_id();

INSERT INTO t_retailtradedailyreportsummary (F_ShopID, F_Datetime, F_TotalNO, F_PricePurchase, F_TotalAmount, F_AverageAmountOfCustomer, F_TotalGross, F_TopSaleCommodityID, F_TopSaleCommodityNO, F_TopSaleCommodityAmount, F_TopPurchaseCustomerName, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), 1, 1, 1, 1, 1, @iRefID, 1, 0, 'a', now(), now());
SET @iRdrID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ�������ձ����ܱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';

DELETE FROM t_retailtradedailyreportsummary WHERE F_ID = @iRdrID;
DELETE FROM t_commodity WHERE F_ID IN (@iRefID,@iID) ;

SELECT '-------------------- Case18���õ�Ʒ�Ķ��װ��Ʒ����Ʒ���۱�������������ɾ�� -------------------------' AS 'Case18';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������332','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,3,'1111111',2);
SET @iRefID = last_insert_id();

INSERT INTO t_retailtradedailyreportbycommodity (F_ShopID, F_Datetime, F_CommodityID, F_NO, F_TotalPurchasingAmount, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), @iRefID, 1, 1, 1, 1, now(), now());
SET @iRdrByCommodityID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�õ�Ʒ�Ķ��װ��Ʒ����Ʒ���۱�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';

DELETE FROM t_retailtradedailyreportbycommodity WHERE F_ID = @iRdrByCommodityID;
DELETE FROM t_commodity WHERE F_ID IN (@iRefID,@iID) ;

SELECT '-------------------- Case19����Ʒ�Ƕ��װ��Ʒ����δɾ����Ʒ�ĸ���λ -------------------------' AS 'Case19';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'�������233','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434864',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'�������332','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434874',
3,'15','2018-04-14','20',@iID,3,'1111111',2);

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����ɾ����Ʒ�ĸ���λ', '���Գɹ�', '����ʧ��') AS 'Case19 Testing Result';
DELETE FROM t_commodity WHERE F_ID IN (last_insert_id(),@iID);

SELECT '-------------------- Case20������Ʒ�������Ʒ������Ʒ,����ɾ�� -------------------------' AS 'Case20';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com117��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID= LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@commID1, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = 'Ҫɾ������Ʒ�������Ʒ��һ���֣�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case20 Testing Result';
SELECT @sErrorMsg;

DELETE FROM t_subcommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- Case21������Ʒ����ɾ���������Ʒ������Ʒ,����ɾ�� -------------------------' AS 'Case21';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'������com117��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID= LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com118��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������com119��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- ���A��Ʒ��Ʒ������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- ���B��Ʒ��Ʒ������

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
-- INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@commID1, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case21 Testing Result';
SELECT @sErrorMsg;

DELETE FROM t_subcommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2) ;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- Case22����Ʒ��ָ����������������û�б�ɾ������ô����Ʒ�����Ա�ɾ�� -------------------------' AS 'Case22';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iCommodityID = last_insert_id();
-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case22_1 Testing Result';
-- 
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�д�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case22_2 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case23����Ʒ��ָ���������������Ǵ����Ѿ���ɾ��(״̬��Ϊ1)����Ʒ����������������ô����Ʒ���ǲ����Ա�ɾ�� -------------------------' AS 'Case23';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iCommodityID = last_insert_id();
-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case23_1 Testing Result';
-- 
UPDATE t_promotion SET F_Status = 1 WHERE F_ID = @iPromotionID;
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�д�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case23_2 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_PromotionID = @iPromotionID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '-------------------- Case24����Ʒ��ɹ���Ʒ�����������Ǹòɹ���Ʒ������ɹ�������ɾ���������Ʒ���Ա�ɾ�� -------------------------' AS 'Case23';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID1 = last_insert_ID();
-- 
SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID1, @fPriceSuggestion);
-- 
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE24_1 Testing Result';
-- 
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '����Ʒ�вɹ�������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case24_2 Testing Result';
-- 
-- ɾ���ɹ��������޸�״̬Ϊ4
UPDATE t_purchasingorder SET F_Status = 4 WHERE F_ID = @iPurchasingOrderID;
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case24_3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case25����Ʒ��ȫ����������������û�б�ɾ������ô����ƷҲ���Ա�ɾ�� -------------------------' AS 'Case25';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iCommodityID = last_insert_id();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case25_2 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case25����Ʒ��ȫ������������������ɾ������ô����ƷҲ���Ա�ɾ�� -------------------------' AS 'Case25';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������333','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',0);
SET @iCommodityID = last_insert_id();
-- ɾ������
UPDATE t_promotion SET F_Status = 1 WHERE F_ID = @iPromotionID;
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iCommodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case25_2 Testing Result';
-- 
DELETE FROM t_promotionScope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case26����Ʒ�Ĳɹ��˻���Ʒ�м�¼ ��Ʒ����ɾ�� -------------------------' AS 'Case26';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'������˹������14','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO nbr.t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID, F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (1, @ibID, @iID, '������˹������14', 200, '��', 1);
SET @rcscID = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ʒ�ڲɹ��˻���������', '���Գɹ�', '����ʧ��') AS 'Case26_2 Testing Result';
SELECT @sErrorMsg;
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @rcscID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;


-- 
SELECT '-------------------- Case27����Ʒ���Ż�ȯ��Χ����������ɾ��(���ܸ��Ż�ȯ�Ƿ����á�����������) -------------------------' AS 'Case27';
-- 
-- �½���ͨ��Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ż�ȯ��ͨ��Ʒ', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- �����Ż�ȯ��Χ
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @commodityID, '�Ż�ȯ��ͨ��Ʒ');
SET @couponscopeID = last_insert_id();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@commodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ʒ���Ż�ȯ��Χ����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case27 Testing Result';
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case27����Ʒ���Ż�ȯȫ���������ܱ�ɾ�� -------------------------' AS 'Case27';
-- 
-- �½���ͨ��Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�Ż�ȯ��ͨ��Ʒ', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckCommodityDependency(@commodityID, @sErrorMsg) INTO @sErrorMsg;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case27 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;