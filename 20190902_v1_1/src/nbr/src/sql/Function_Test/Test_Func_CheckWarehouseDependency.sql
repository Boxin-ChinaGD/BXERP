SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckWarehouseDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:没有依赖，可以删除-------------------------' AS 'Case1';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('仓库994', '植物园', 0);
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID;

SELECT '-------------------- Case2:该仓库已被入库单使用，不能删除-------------------------' AS 'Case2';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('仓库994', '植物园', 0);
SET @iID = LAST_INSERT_ID();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (0, 3, @iID, 5, now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '该仓库已被入库单使用，不能删除', '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM t_warehouse WHERE F_ID = @iID;


SELECT '-------------------- Case3:该仓库已被盘点单使用，不能删除-------------------------' AS 'Case3';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('仓库994', '植物园', 0);
SET @iID = LAST_INSERT_ID();

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,@iID,200,0,3,'2017-12-06','...........................zz');
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '该仓库已被盘点单使用，不能删除', '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iID2;
DELETE FROM t_warehouse WHERE F_ID = @iID;