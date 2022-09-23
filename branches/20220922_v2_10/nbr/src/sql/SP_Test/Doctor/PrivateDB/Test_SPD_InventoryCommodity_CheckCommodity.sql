SELECT '++++++++++++++++++ Test_SPD_InventoryCommodity_CheckCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:�̵㵥��Ʒ��ͬ����Ʒ��ͬһ���̵㵥 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 1, '��Ƭ', 1, 1, 1, 0, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 1, '��Ƭ', 1, 1, 1, 0, 0);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'��������ͬ����Ʒ��ͬһ���̵㵥') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID + 1;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

SELECT '-------------------- Case3:�̵㵥��Ʒ��Ӧ����Ʒ����Ʒ���Ͳ��ǵ�Ʒ -------------------------' AS 'Case3';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 45, '��Ը����ζ��Ƭ', 1, 49, 1, 200, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'��Ӧ����Ʒ����Ʒ���Ͳ��ǵ�Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

--	������
--	SELECT '-------------------- Case4:�̵㵥��Ʒ��Ӧ����Ʒ������ -------------------------' AS 'Case4';
--	INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
--	VALUES (1,200,4,4,'2017-12-06','...........................zz');
--	SET @iInventorysheetID = LAST_INSERT_ID();
--	-- 
--	INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
--	VALUES (@iInventorysheetID, -1, '��Ը����ζ��Ƭ', 1, 49, 1, 200, 0);
--	SET @iInventorysheetCommodityID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'��Ӧ����Ʒ������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
--	-- 
--	DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
--	DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

SELECT '-------------------- Case5:�̵㵥��Ʒ��Ӧ����Ʒ����Ʒ�����Ƿ�����Ʒ���ǵ�Ʒ -------------------------' AS 'Case5';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'˳��','���','��',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'˳����',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, @iCommodityID, '˳��', 1, 49, 1, 200, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'��Ӧ����Ʒ����Ʒ���Ͳ��ǵ�Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;