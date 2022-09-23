SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_UpdateStatus.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 修改状态为部分入库(1 --> 2) -------------------------' AS 'Case1';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 修改状态为全部入库(2 --> 3) -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 3;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';


SELECT '-------------------- Case3: 修改状态为全部入库(3 --> 3) -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 3;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: 修改全部入库状态的订单为部分入库(这是不允许的 所以返回错误码7)(3 --> 2) -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能修改全部入库状态的订单为部分入库', '测试成功', '测试失败') AS 'CASE4 Testing Result';

-- DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
-- DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case5: 输入一个不存在的iStatus(-1) -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SELECT @iPurchasingOrderID;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能修改全部入库状态的订单为部分入库', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case6: 输入一个不存在的iID(-1) -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;
SET @iID=-11;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '采购订单不存在', '测试成功', '测试失败') AS 'CASE6 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @iID;


--	SELECT '-------------------- Case6: 考虑采购订单为NULL(即iID=0)的情况，返回一个新的错误码2 -------------------------' AS 'Case6';
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0; 
--	SET @iStatus = 2;
--	
--	CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);
--	
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
--	SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: 修改没有采购商品的采购订单 -------------------------' AS 'Case6';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE6 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @iID;