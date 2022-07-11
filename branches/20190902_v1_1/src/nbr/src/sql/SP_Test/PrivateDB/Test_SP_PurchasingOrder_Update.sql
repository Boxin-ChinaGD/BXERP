SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 状态为未审核(F_Status=0) -------------------------' AS 'Case1';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 2;
SET @sRemark = '';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sRemark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 

SELECT '-------------------- Case2: 状态为其他(F_Status=1，2，3，4) -------------------------' AS 'Case2';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单已审核，不允许修改', '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case3: 更新状态为0的 传入的iID不存在  -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';
SET @iID = -9999;

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '采购订单不存在', '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: 更新状态为0的 传入的sReMark超过数据库限制的长度 -------------------------' AS 'Case4';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF( @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case5: 更新状态为0的 传入的iProviderID不存在  d-------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = -999;
SET @sReMark = '好好好';
SET @iID = 2;

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '供应商不存在，请重新选择供应商', '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 审核没有采购商品的采购订单(F_Status = 0) -------------------------' AS 'Case6';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE6 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case7: 审核没有采购商品的采购订单(F_Status = 1) -------------------------' AS 'Case7';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE7 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case8: 审核没有采购商品的采购订单(F_Status = 2) -------------------------' AS 'Case8';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 2, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE8 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case9: 审核没有采购商品的采购订单(F_Status = 3) -------------------------' AS 'Case9';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 3, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE9 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case10: 审核没有采购商品的采购订单(F_Status = 4) -------------------------' AS 'Case10';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 4, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @sReMark = '好好好';

CALL SP_PurchasingOrder_Update(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iProviderID, @sReMark);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID AND F_ProviderID = @iProviderID AND F_Remark = @sReMark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE10 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;