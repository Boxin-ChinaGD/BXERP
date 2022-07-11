SELECT '++++++++++++++++++ Test_SP_Message_RetrieveNForWx.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:根据status查询 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = 0;
SET @iCompanyID = -1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:根据companyID查询 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = -1;
SET @iCompanyID = 1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:查询全部 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = -1;
SET @iCompanyID = -1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:根据companyID和Status查询 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = 1;
SET @iCompanyID = 1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result'
