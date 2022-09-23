SELECT '++++++++++++++++++ Test_SP_Staff_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- case 1：根据id查询出门店客户 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- case2：根据电话号码查询出门店客户 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '15854320895';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- case3：根据状态码，查询出所有在职的门店客户 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '';
SET @iInvolvedResigned = 0;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- case4：根据状态码，电话号码，id，查询出在职的门店客户 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 0;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- case5：根据电话号码，id，查询出门店客户 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- case6：根据状态码，id，查询出在职的门店客户 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '-------------------- case7：根据电话号码，状态码，查询出在职门店客户 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '--------------------- case8：没有条件，全部使用默认参数 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';

SELECT '--------------------- case9：使用不存在的ID进行查询 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';

SELECT '--------------------- case10：使用不存在的电话号码进行查询 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '333333333333333333';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';