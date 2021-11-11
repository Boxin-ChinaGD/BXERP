SELECT '++++++++++++++++++ Test_SP_Inventory_UpdateCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常修改盘点单商品 -------------------------' AS 'Case1';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (1, 30, '薯片', '克', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case2: 盘点单已提交 返回错误码7 -------------------------' AS 'Case2';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (6, 30, '薯片', '克', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case3: 盘点单已审核 返回错误码7 -------------------------' AS 'Case3';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (8, 30, '薯片', '克', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case4: 修改的实盘数量少于0 返回错误码7 -------------------------' AS 'Case4';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (1, 30, '薯片', '克', 1, 1, 100, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = -1;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;