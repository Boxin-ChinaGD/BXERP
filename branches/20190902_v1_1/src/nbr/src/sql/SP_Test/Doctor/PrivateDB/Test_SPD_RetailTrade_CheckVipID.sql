SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckVipID.sql ++++++++++++++++++++';

-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckVipID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 

-- 该测试存在外键关系
-- 零售单的VipID不存在vip表中
-- SELECT '-------------------- Case2:零售单的VipID不存在vip表中 -------------------------' AS 'Case2';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- 插入一条零售单数据，测试完会删除
-- INSERT INTO t_retailtrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
-- VALUES (-1,now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
-- SET @iRetailTradeID = LAST_INSERT_ID();
-- 
-- CALL SPD_RetailTrade_CheckVipID(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode;
-- SELECT @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的VipID不存在vip表中') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 

-- 零售单的VipID为NULL
SELECT '-------------------- Case3:零售单的VipID为NULL -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (NULL,now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckVipID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT 1 FROM t_retailtrade WHERE F_VipID IS NULL; -- 验证插入的数据是否存在
SELECT IF(found_rows() = 1 AND @sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 