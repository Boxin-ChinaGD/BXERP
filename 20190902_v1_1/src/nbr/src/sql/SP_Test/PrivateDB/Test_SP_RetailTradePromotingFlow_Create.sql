SELECT '----------------------------------Test_SP_RetailTradePromotingFlow_Create.sql----------------------------------------';


INSERT INTO t_retailtradepromoting (F_TradeID) VALUES (1);
SET @ireID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPromotionID = 1;
CALL SP_RetailTradePromotingFlow_Create(@iErrorCode, @sErrorMsg, @ireID ,@iPromotionID, '测试数据');
SET @iID = LAST_INSERT_ID();
SELECT @iErrorCode;
SELECT 1 FROM t_retailtradepromotingflow WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT @sErrorMsg;
DELETE FROM t_retailtradepromotingflow WHERE F_ID = @iID;
DELETE FROM t_retailtradepromoting WHERE F_ID = @ireID;

SELECT '-------------------------CASE2:用不存在promotionID创建-------------------------' AS 'case2';
SET @ireID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPromotionID = -999;
CALL SP_RetailTradePromotingFlow_Create(@iErrorCode, @sErrorMsg, @ireID ,@iPromotionID, '测试数据');


SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';


SELECT '-----------------------CASE3:用不存在RetailTradePromotingID创建-------------------------' AS 'case3';
SET @ireID = -99;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @iPromotionID = 1;
CALL SP_RetailTradePromotingFlow_Create(@iErrorCode, @sErrorMsg, @ireID ,@iPromotionID, '测试数据');

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-----------------------CASE4:promotionID为空进行创建-------------------------' AS 'case4';
INSERT INTO t_retailtradepromoting (F_TradeID) VALUES (1);
SET @ireID = last_insert_id();
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @iPromotionID = NULL;
-- 
CALL SP_RetailTradePromotingFlow_Create(@iErrorCode, @sErrorMsg, @ireID ,@iPromotionID, '测试数据');
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_retailtradepromotingflow WHERE F_ID = last_insert_id();
DELETE FROM t_retailtradepromoting WHERE F_ID = @ireID;