SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 输入供应商名称模糊搜索供应商 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '华';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 输入联系人模糊搜索供应商 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'k';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: 输入大于四位的供应商电话模糊搜索供应商 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '1312';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: 没有输入模糊搜索供应商 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';