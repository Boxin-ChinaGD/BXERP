SELECT '++++++++++++++++++ Test_SP_StaffBelonging_RetrieveN.sql ++++++++++++++++++++';

SELECT '--------------------- case1:查询所有openID归属公司-------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_StaffBelonging_RetrieveN(@iErrorCode, @sErrorMsg);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';