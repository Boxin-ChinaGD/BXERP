SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckProviderDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:供应商已经存在商品，不能删除-------------------------' AS 'Case1';
INSERT INTO t_provider ( F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商007', 1, 'china', 'kkk', '13726498500');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (1, @iID);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '供应商已经存在商品，不能删除', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_providercommodity WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case2:供应商已被退货单引用，不能删除-------------------------' AS 'Case2';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商777', 1, 'china', 'kkk', '13532644012');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_ShopID)
VALUES (1, @iID, 1,2);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '供应商已被退货单引用，不能删除', '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case3:供应商已被采购订单引用，不能删除-------------------------' AS 'Case3';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商770', 1, 'china', 'kkk', '13532644011');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark)
VALUES (1, 1, @iID, '默认供应商', NULL, '红红火火恍恍惚惚');

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '供应商已被采购订单引用，不能删除', '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case4:供应商已被入库引用，不能删除-------------------------' AS 'Case4';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商774', 1, 'china', 'kkk', '13532644016');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_PurchasingOrderID)
VALUES (0, @iID, 1, 3, NULL, 1);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '供应商已被入库单引用，不能删除', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_warehousing WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case5：供应商没有被引用-------------------------' AS 'Case5';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商776', 1, 'china', 'kkk', '13532644016');
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_provider WHERE F_ID = @iID;