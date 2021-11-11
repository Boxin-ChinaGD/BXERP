SELECT '++++++++++++++++++ Test_SP_Commodity_DeleteMultiPackaging.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:删除单品 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:该商品是组合商品 -------------------------' AS 'Case2';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠2号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠3号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠4号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_subcommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2); 
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2);

SELECT '-------------------- Case3:多包装存在销存记录 采购单商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠5号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf11a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1, @iID, 100, '蜘蛛侠5号a', @ibID, 1, 11.1);
SET @pocID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @pocID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case4:多包装有销存记录，盘点商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case4';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠6号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf12a');
SET @ibID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal)
VALUES (2, @iID, '蜘蛛侠6号a', '克', @ibID, 1, 200);
SET @iicID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iicID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case5:多包装有销存记录，零售商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case5';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠7号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'蜘蛛侠7号', @ibID, 100, 120, 100, 5, 100, NULL);
SET @irtcID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case6:多包装有销存记录，入库商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case6';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠8号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf14a');
SET @ibID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime)
VALUES (1, @iID, 122, 1, '蜘蛛侠8号a', @ibID, 10, 1000, now(), 23, now());
SET @iwcID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iwcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case7:传入多包装的Id 单品有销存记录、多包装无销存记录 ec=0 -------------------------' AS 'Case7';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠9号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @cId=last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠10号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@cId,3,'1111111',2);
SET @iID=last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf12ab');
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Case7 Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', '987aaaf12ab', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE7 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue = (SELECT F_Name FROM t_packageunit WHERE F_ID = 3)
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE7 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@cId,@iID); 
DELETE FROM t_commodity WHERE F_ID IN (@cId,@iID); 

SELECT '-------------------- Case8:重复删除多包装商品 -------------------------' AS 'Case8';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠9号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @cId=last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'蜘蛛侠10号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@cId,3,'1111111',2);
SET @iID=last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该商品已删除，不能重复删除', '测试成功', '测试失败') AS 'Testing Case8 Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@cId);

SELECT '-------------------- Case9:删除不存在的商品 -------------------------' AS 'Case9';
SET @sErrorMsg = '';
SET @iStaffID = 3;
SET @iErrorCode = 0;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, -1, @iStaffID);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '商品不存在', '测试成功', '测试失败') AS 'Testing Case9 Result';

-- 多包装不能参与促销
--	SELECT '-------------------- Case10:多包装商品有促销依赖，促销没有被删除，那么该商品不可以被删除 -------------------------' AS 'Case10';
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @name = '满10-1';
--	SET @status = 0;
--	SET @type = 0;
--	SET @datetimestart = now();
--	SET @datetimeend = now();
--	SET @excecutionthreshold = 10;
--	SET @excecutionamount = 1;
--	SET @excecutiondiscount = 1;
--	SET @scope = 0;
--	SET @staff = 1 ;
--	INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
--	SET @iPromotionID = last_insert_id();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'蜘蛛侠5号','方便面','克',3,'箱',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,3,'1111111', 0,2);
--	SET @iCommodityID = last_insert_id();
--	-- 
--	SET @sBarcode = 'DDEEFF1';
--	INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
--	SET @ibID = last_insert_id();
--	-- 
--	CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
--	SET @promotionScopeID = last_insert_id();
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case10_1 Testing Result';
--	SET @iStaffID = 3;
--	-- 
--	CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iCommodityID, @iStaffID);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_commodity WHERE F_ID = @iCommodityID AND F_Status = 0;
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该商品有促销依赖，不能删除', '测试成功', '测试失败') AS 'Case10_2 Testing Result';
--	-- 
--	DELETE FROM t_promotionscope WHERE F_ID = @promotionScopeID;
--	DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
--	DELETE FROM t_barcodes WHERE F_ID = @ibID;
--	DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
--	DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case11:多包装商品有促销依赖，但是促销已经被删除，商品又无其他依赖，那么多包装该商品可以被删除 -------------------------' AS 'Case11';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 1;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠5号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iCommodityID = last_insert_id();
-- 
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
SET @ibID = last_insert_id();
-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case11_1 Testing Result';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteMultiPackaging(@iErrorCode, @sErrorMsg, @iCommodityID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iCommodityID AND F_Status = 0;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case11_2 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;