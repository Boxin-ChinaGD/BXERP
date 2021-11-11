SELECT '++++++++++++++++++ Test_SP_Category_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询一个不存在的商品小类 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheck = 1;
SET @sString1 = '阿萨德佛该会否';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询一个已经存在的商品小类 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '饮料';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '该商品小类已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:查询一个已经存在的商品小类,但传入的ID与已存在的商品小类的ID相同 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iFieldToCheck = 1;
SET @sString1 = '饮料';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';



SELECT '-------------------- Case4:查询一个已经存在的商品小类,但传入的ID与已存在的商品小类的ID不相同 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iFieldToCheck = 1;
SET @sString1 = '饮料';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '该商品小类已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';