SELECT '++++++++++++++++++ Test_SP_WarehousingCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 添加正常状态下的普通商品到入库单商品 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'辣条','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3年','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iNO = 300;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

SELECT @iErrorCode;
SELECT @sErrorMsg;

DELETE FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- case2:创建一个commodityID不存在的入库商品 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 999999999;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@sErrorMsg = '不能添加一个不存在的商品' AND @iErrorCode = 2, '测试成功', '测试失败') AS 'case2 Testing Result';

SELECT '-------------------- case3:创建一个warehousing中不存在的WarehousingID入库商品-------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 999999999;
SET @iCommodityID = 2;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);

SELECT IF(@sErrorMsg = '该入库单不存在' AND @iErrorCode = 2, '测试成功', '测试失败') AS 'case3 Testing Result';

SELECT '-------------------- case4:创建一个iBarcodeID不存在的入库商品-------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 6;
SET @iNO = 300;
SET @iBarcodeID = 999999999;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '入库单商品的条形码不存在', '测试成功', '测试失败') AS 'case4 Testing Result';

SELECT '-------------------- case5:用组合商品创建有一个入库单商品-------------------------' AS 'Case5';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '薯愿香辣味6670', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111',1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能添加非单品的商品进入库单', '测试成功', '测试失败') AS 'case5 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '薯愿香辣味6670';

SELECT '-------------------- case6:用已删除的组合商品创建有一个入库单商品-------------------------' AS 'Case6';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '薯愿香辣味6671', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '不能添加已删除的商品进入库单', '测试成功', '测试失败') AS 'case6 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '薯愿香辣味6671';

SELECT '-------------------- case7:用已删除的商品创建有一个入库单商品-------------------------' AS 'Case7';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '薯愿香辣味6672', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '不能添加已删除的商品进入库单', '测试成功', '测试失败') AS 'case7 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '薯愿香辣味6672';

SELECT '-------------------- case8:用已删除的多包装商品创建有一个入库单商品-------------------------' AS 'Case8';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '薯愿香辣味6673', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '不能添加已删除的商品进入库单', '测试成功', '测试失败') AS 'case8 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '薯愿香辣味6673';

SELECT '-------------------- case9:用多包装商品创建有一个入库单商品-------------------------' AS 'Case9';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '薯愿香辣味6675', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;
	
CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能添加非单品的商品进入库单' , '测试成功', '测试失败') AS 'case9 Testing Result';

DELETE FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_WarehousingID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- case10:创建一张入库单商品中有两个相同的商品-------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 2;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);


SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'case10 Testing Result';


SELECT '-------------------- Case11: 条形码的ID与商品的ID不对应 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'辣条','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3年','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeIDs = last_insert_ID();

SET @iBarcodeID = 1;

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iNO = 300;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '条形码ID与商品实际条形码ID不对应', '测试成功', '测试失败') AS 'case11 Testing Result';

SELECT @iErrorCode;
SELECT @sErrorMsg;

DELETE FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeIDs;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;