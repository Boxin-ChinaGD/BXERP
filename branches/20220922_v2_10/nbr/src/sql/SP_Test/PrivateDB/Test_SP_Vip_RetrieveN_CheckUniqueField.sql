SELECT '++++++++++++++++++ Test_SP_Vip_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: 查询一个不存在的会员的手机号码------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '33144496272';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';


SELECT '--------------------- case2: 查询一个已存在的会员的手机号码------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '13545678110';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '该手机号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case2 Testing Result';



SELECT '--------------------- case3: 查询一个不存在的会员的身份证号------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '540883198412111666';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case4 Testing Result';


SELECT '--------------------- case4: 查询一个已存在的会员的身份证号------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '320803199707016031';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '该身份证号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case4 Testing Result';




SELECT '--------------------- case9: 查询一个不存在的会员的邮箱------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '623456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case9 Testing Result';


SELECT '--------------------- case10: 查询一个已存在的会员的邮箱------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '123456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '该邮箱已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case10 Testing Result';

-- account字段已删除
--	SELECT '--------------------- case11: 查询一个不存在的会员的登录账号------------------------------' AS 'case11';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '623456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case9 Testing Result';
--	
--	
--	SELECT '--------------------- case12: 查询一个已存在的会员的登录账号------------------------------' AS 'case12';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '123456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '该登录账号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case12 Testing Result';



SELECT '--------------------- case13: 查询一个已存在的会员的手机号码,传入的ID与已存在的会员手机号码的ID相同------------------------------' AS 'case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '13545678110';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case13 Testing Result';



SELECT '--------------------- case14: 查询一个已存在的会员的身份证号，但传入的ID与已存在的会员的身份证号的ID相同------------------------------' AS 'case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '123456';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case14 Testing Result';





SELECT '--------------------- case17: 查询一个已存在的会员的邮箱，但传入的ID与已存在的会员有邮箱的ID相同------------------------------' AS 'case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '123456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case17 Testing Result';


-- account字段已删除
--	SELECT '--------------------- case18: 查询一个已存在的会员的登录账号，但传入的ID与已存在的会员登录账号登录------------------------------' AS 'case18';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 1;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '123456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case18 Testing Result';