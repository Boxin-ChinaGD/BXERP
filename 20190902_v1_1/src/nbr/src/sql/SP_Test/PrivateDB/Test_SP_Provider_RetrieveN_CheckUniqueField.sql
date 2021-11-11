SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询一个不存在的供应商名称 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '阿萨德佛该会否';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询一个已经存在的供应商名称 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '华南供应商';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '该供应商名称已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:查询一个已经存在的供应商名称,但传入的ID与已存在的供应商名称的供应商ID相同 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '华南供应商';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:查询一个不存在的联系人电话 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '93129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- Case5:查询一个已经存在的联系人电话 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '该联系人电话已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';


SELECT '-------------------- Case6:查询一个已经存在的联系人电话,但传入的ID与已存在的联系人电话的供应商ID相同 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';



SELECT '-------------------- Case7:查询一个已经存在的供应商名称,传入的ID与已存在的供应商名称的供应商ID不相同 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '华南供应商';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '该供应商名称已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case7 Testing Result';


SELECT '-------------------- Case8:查询一个已经存在的联系人电话,传入的ID与已存在的联系人电话的供应商ID不相同 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '该联系人电话已存在' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case8 Testing Result'