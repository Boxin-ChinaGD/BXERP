SELECT '++++++++++++++++++ Test_SP_ProviderCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 添加不重复供应商到商品中，错误码为0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iProviderID = 11;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_providercommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: 添加相同的供应商到同一组合商品中，错误码为1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iProviderID = 1;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: 添加组合商品到供应商商品表中，错误码为7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @iErrorCode;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: 添加已删除商品到供应商商品表中，错误码为7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 50;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @iErrorCode;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- Case5: 添加不存在的商品到供应商商品表中，错误码为7 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -99;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- Case6: 添加不存在的供应商到供应商商品表中，错误码为7 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 20;
SET @iProviderID = -99;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';