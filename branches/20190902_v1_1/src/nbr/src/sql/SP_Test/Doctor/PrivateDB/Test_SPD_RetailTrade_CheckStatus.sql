SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckStatus.sql ++++++++++++++++++++';

-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 

-- 零售单的状态不是0、1、2
SELECT '-------------------- Case2:零售单的状态不是0、1、2 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,-1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的状态只能为0、1、2') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 
