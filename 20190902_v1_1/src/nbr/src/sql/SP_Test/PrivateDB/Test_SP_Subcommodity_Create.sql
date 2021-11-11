SELECT '++++++++++++++++++ Test_SP_Subcommodity_Create ++++++++++++++++++++';

SELECT '-------------------- Case1:添加不重复商品，错误码为0 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 33;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2:添加相同的商品到同一组合商品中，错误码为1 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 49;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case3:将普通商品当做组合商品插入子商品，错误码为7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 25;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4:添加不存在的商品，错误码为7 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 999999999;
SET @iSubCommodityID = 11;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5:添加不存在的子商品，错误码为7 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 2;
SET @iSubCommodityID = 999999999;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6:添加多包装商品为子商品，错误码为7 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 51;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7:添加组合商品为子商品，错误码为7 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 49;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8:子商品价格为负数，错误码为7 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 2;
SET @iSubCommodityNO = 1;
SET @iPrice = -1;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9:将多包装商品当做组合商品插入子商品，错误码为7 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 51;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '只有组合商品能插入子商品', '测试成功', '测试失败') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10:将服务商品当做组合商品插入子商品，错误码为7 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 166;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '只有组合商品能插入子商品', '测试成功', '测试失败') AS 'CASE10 Testing Result';

SELECT '-------------------- Case11:组合商品添加服务商品为子商品 -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 166;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能组合除了单品外的其他类型商品', '测试成功', '测试失败') AS 'CASE11 Testing Result';