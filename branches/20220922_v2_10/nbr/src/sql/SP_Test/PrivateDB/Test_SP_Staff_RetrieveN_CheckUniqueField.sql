SELECT '++++++++++++++++++ Test_SP_staff_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: 查询一个不存在的员工的手机号码------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '33144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';


SELECT '--------------------- case2: 查询一个已存在的员工的手机号码------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该手机号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case2 Testing Result';


SELECT '--------------------- case3: 查询一个已存在的离职员工的手机号码------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '13196721886';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case3 Testing Result';


SELECT '--------------------- case4: 查询一个不存在的员工的身份证号------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '540883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case4 Testing Result';


SELECT '--------------------- case5: 查询一个已存在的员工的身份证号------------------------------' AS 'case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该身份证号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case5 Testing Result';


SELECT '--------------------- case6: 查询一个已存在的离职员工的身份证号------------------------------' AS 'case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '341522198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case6 Testing Result';


SELECT '--------------------- case7: 查询一个不存在的员工的微信号------------------------------' AS 'case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'fffff2f';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case7 Testing Result';


SELECT '--------------------- case8: 查询一个已存在的员工的微信号------------------------------' AS 'case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该微信号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case8 Testing Result';


SELECT '--------------------- case9: 查询一个已存在的离职员工的微信号------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'd2sasb4';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case9 Testing Result';


SELECT '--------------------- case10: 查询一个已存在的员工的手机号码,但传入的ID与已存在的员工的手机号码对应的员工ID相同------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case10 Testing Result';


SELECT '--------------------- case11: 查询一个已存在的员工的身份证号,但传入的ID与已存在的员工的身份证号对应的员工ID相同------------------------------' AS 'case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case11 Testing Result';


SELECT '--------------------- case12: 查询一个已存在的员工的微信号,但传入的ID与已存在的员工的微信号对应的员工ID相同------------------------------' AS 'case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case12 Testing Result';


SELECT '--------------------- case13: 查询一个已存在的员工的微信号,传入的ID与已存在的员工的微信号对应的员工ID不相同------------------------------' AS 'case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该微信号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case13 Testing Result';


SELECT '--------------------- case14: 查询一个已存在的员工的身份证号,传入的ID与已存在的员工的身份证号对应的员工ID不相同------------------------------' AS 'case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该身份证号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case14 Testing Result';


SELECT '--------------------- case15: 查询一个已存在的员工的手机号码,传入的ID与已存在的员工的手机号码对应的员工ID不相同------------------------------' AS 'case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '该手机号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case15 Testing Result';