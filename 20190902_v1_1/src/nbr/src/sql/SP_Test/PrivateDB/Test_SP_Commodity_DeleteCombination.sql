SELECT '++++++++++++++++++ Test_SP_Commodity_DeleteCombination.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:传入单品 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'奥特曼com1号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:该商品是组合商品 正常删除-------------------------' AS 'Case2';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com117号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com118号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com119号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', '袋', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- Case3:传入组合商品ID，组合商品零售有记录  -------------------------' AS 'Case3';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com123号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',1);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'奥特曼com123号', @ibID, 100, 120, 100, 5, 100, NULL);
SET @irtcID = last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case4:传入多包装的Id -------------------------' AS 'Case4';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com126号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',2);
SET @iID=last_insert_id();
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Testing Case4 Result';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case5:重复删除组合商品 -------------------------' AS 'Case5';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'奥特曼com117号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com118号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com119号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该商品已删除，不能重复删除', '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- case6:删除不存在的商品 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, -1, 3);
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case6 Testing Result';

-- 组合商品不能参与促销
--	SELECT '-------------------- Case7:商品有促销依赖，促销没有被删除，那么该商品不可以被删除------------------------' AS 'Case7';
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
--	VALUES (0,'奥特曼com117号','方便面','克',3,'袋',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,1,'1111111', 0,1);
--	SET @iID = last_insert_id();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'奥特曼com118号','方便面','克',3,'袋',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,2,'1111111', 0,0);
--	SET @commID1 = LAST_INSERT_ID();
--	-- 
--	INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
--	F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
--	F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
--	VALUES (0,'奥特曼com119号','方便面','克',3,'袋',3,3,'SP',1,
--	8,50,130,11,1,1,'url=116843434834',
--	3,'15','2018-04-14','20',0,2,'1111111', 0,0);
--	SET @commID2 = LAST_INSERT_ID();
--	-- 
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码
--	-- 
--	INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
--	INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
--	
--	-- 
--	CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iID);
--	SET @promotionScopeID = last_insert_id();
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7_1 Testing Result';
--	
--	SET @sErrorMsg = '';
--	SET @iStaffID = 3;
--	-- 
--	CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
--	SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该商品有促销依赖，不能删除', '测试成功', '测试失败') AS 'Case7_2 Testing Result';
--	-- 
--	SELECT 1 FROM t_commodityhistory 
--	WHERE F_StaffID = @iStaffID 
--	AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
--	AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
--	AND F_NewValue = "" 
--	AND F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7_3 Testing Result';
--	-- 
--	SELECT 1 FROM t_commodityhistory 
--	WHERE F_StaffID = @iStaffID 
--	AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
--	AND F_OldValue LIKE CONCAT('%', '袋', '%')
--	AND F_NewValue = "" 
--	AND F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7_4 Testing Result';
--	-- 
--	SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
--	SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7_5 Testing Result';
--	-- 
--	DELETE FROM t_promotionscope WHERE F_ID = @promotionScopeID;
--	DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
--	DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2);
--	DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
--	DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
--	DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2);


SELECT '-------------------- Case8:商品有促销依赖，但是促销已经被删除，商品又无其他依赖，那么该商品可以被删除------------------------' AS 'Case8';
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
VALUES (0,'奥特曼com117号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com118号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼com119号','方便面','克',3,'袋',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987ccc9ab');
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);

-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case8_1 Testing Result';

SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteCombination(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case8_2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', '987ccc9ab', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE8_3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', '袋', '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE8_4 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE8_5 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_subcommodity WHERE F_SubCommodityID IN (@commID1,@commID2) ;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2);
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2);