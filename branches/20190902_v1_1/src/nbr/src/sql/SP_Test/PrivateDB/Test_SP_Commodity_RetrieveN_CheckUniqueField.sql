SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询一个不存在的商品名称 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '阿萨德佛该会否';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询一个已经存在的商品名称 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '可比克薯片';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该商品名称已经存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:查询一个已经删除的商品名称 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '百事青椒味薯片1';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:查询一个已经存在的商品名称,但传入的iID是等于已存在的商品名称的商品ID -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iInt1 = 1;
SET @sString1 = '可比克薯片';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';


SELECT '-------------------- Case5:查询一个已经存在的商品名称,传入的iID不等于已存在的商品名称的商品ID -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 1;
SET @sString1 = '可比克薯片';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该商品名称已经存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';


SELECT '------- Case6:查询一个已经存在的商品名称,传入的iID是等于已存在的商品名称的商品ID,但是该商品已经删除了（状态为2）-------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 49;
SET @iInt1 = 1;
SET @sString1 = '百事青椒味薯片1';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';