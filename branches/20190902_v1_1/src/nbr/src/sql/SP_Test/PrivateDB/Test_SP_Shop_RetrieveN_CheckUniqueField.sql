SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: 创建门店时，传入一个不存在的门店名称，检查名称是否已存在------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '部卖小昕博';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';



SELECT '--------------------- case2: 创建门店时，传入一个已存在的门店名称，检查名称是否已存在------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '博昕小卖部';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该门店名称已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case2 Testing Result';



SELECT '--------------------- case3: 更新门店时，传入一个已存在的门店名称，检查名称是否已存在------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '博昕小卖部';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@iErrorCode = 0,'测试成功','测试失败') AS 'Case3 Testing Result';


SELECT '--------------------- case4: 更新门店时，传入一个已存在的门店名称，检查名称是否已存在------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '博昕小卖部';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该门店名称已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case4 Testing Result';