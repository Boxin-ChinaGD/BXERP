SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Approve.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 当状态为未审核时（F_Status=0） -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 


SELECT '-------------------- Case2: 当状态为其他时（F_Status=1） -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 5, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '可比克薯片', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该采购单已审核，请勿重复操作', '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 

SELECT '-------------------- Case3: 当输入的采购订单表的@iPurchasingOrderID不存在的时候  返回错误码7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;
SET @iApproverID = 3;
CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '采购订单不存在', '测试成功', '测试失败') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: 当输入的审核人@iApproverID不存在的时候  无法调用（主外键关系限制） 返回错误码7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = -1;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '当前帐户不允许审核', '测试成功', '测试失败') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case5: 审核没有采购商品的采购订单 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该采购订单没有采购商品', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 

SELECT '-------------------- Case6: 当输入的审核人@iApproverID为离职员工ID的时候  无法调用（主外键关系限制） 返回错误码7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 5; -- 离职员工ID

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '当前帐户不允许审核', '测试成功', '测试失败') AS 'CASE6 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;