SELECT '++++++++++++++++++ Test_SP_Provider_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 商品表中已有商品的供应商。删除不了，错误代码为7 -------------------------' AS 'Case1';
INSERT INTO t_provider ( F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商007', 1, 'china', 'kkk', '13726498500');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (1, @iID);

SET @iProviderID = @iID;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '供应商已经存在商品，不能删除', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_providercommodity WHERE F_ProviderID = @iID AND F_CommodityID = 1;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case2: 商品表中没有这个供应商的商品。可以直接删除，错误代码为0 -------------------------' AS 'Case2';
SET @sName = '华夏供应商1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_provider ( F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  (@sName, 1, 'china', 'kkk', '13121564654654');

SET @iID = LAST_INSERT_ID();

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT 1 FROM t_Provider WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:供应商已被采购订单引用。删除不了，错误代码为7 -------------------------' AS 'Case3';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商770', 1, 'china', 'kkk', '13532644011');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark)
VALUES (1, 1, @iID, '默认供应商', NULL, '红红火火恍恍惚惚');

SET @iProviderID = @iID;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '供应商已被采购订单引用，不能删除', '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ProviderID = @iProviderID;
DELETE FROM t_provider WHERE F_ID = @iProviderID;

SELECT '-------------------- Case4:供应商已被退货单引用。删除不了，错误代码为7 -------------------------' AS 'Case4';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商777', 1, 'china', 'kkk', '13532644012');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_ShopID)
VALUES (1, @iID, 1, 2);

SET @iProviderID = @iID;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '供应商已被退货单引用，不能删除', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ProviderID = @iProviderID;
DELETE FROM t_provider WHERE F_ID = @iProviderID;

SELECT '-------------------- Case5:删除一个不存在的供应商 -------------------------' AS 'Case5';
SET @iProviderID = -999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- Case6:供应商已被入库单引用。删除不了，错误代码为7 -------------------------' AS 'Case5';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商777', 1, 'china', 'kkk', '13532644012');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_PurchasingOrderID)
VALUES (0, @iID, 1, 3, NULL, 1);

SET @iProviderID = @iID;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '供应商已被入库单引用，不能删除', '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_warehousing WHERE F_ProviderID = @iProviderID;
DELETE FROM t_provider WHERE F_ID = @iProviderID;

SELECT '-------------------- Case7:供应商已被已删除采购订单引用。删除成功 -------------------------' AS 'Case7';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('供应商770', 1, 'china', 'kkk', '13532644011');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark)
VALUES (0, 1, @iID, '默认供应商', NULL, '红红火火恍恍惚惚');
SET @purchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Delete(@iErrorCode, @sErrorMsg, @purchasingOrderID);

SET @iProviderID = @iID;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Delete(@iErrorCode, @sErrorMsg, @iProviderID);

SELECT 1 FROM t_Provider WHERE F_ID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case7 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @purchasingOrderID;
DELETE FROM t_provider WHERE F_ID = @iProviderID;