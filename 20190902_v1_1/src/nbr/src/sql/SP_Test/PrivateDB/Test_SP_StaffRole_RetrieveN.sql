SELECT '++++++++++++++++++Test_SP_StaffRole_RetrieveN.sql+++++++++++++++++++++++';

-- 角色
SELECT '-----------------Case1: 查询所有角色(在职和离职)------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case2: 查询所有在职的角色------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;


SELECT '-----------------Case3: 查询所有离职的角色------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 收银员
SELECT '-----------------Case4: 查询收银员(在职和离职)------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case5: 查询在职的收银员------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case6: 查询离职的收银员------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 经理
SELECT '-----------------Case7: 查询经理(在职和离职)------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case8: 查询在职的经理------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case9: 查询离职的经理------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 2;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 副店长
SELECT '-----------------Case10: 查询副店长(在职和离职)------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case11: 查询在职的副店长------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case12: 查询离职的副店长------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 3;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 店长
SELECT '-----------------Case13: 查询店长(在职和离职)------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case14: 查询在职的店长------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case15: 查询离职的店长------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 4;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 业务经理
SELECT '-----------------Case16: 查询业务经理(在职和离职)------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case17: 查询在职的业务经理------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case18: 查询离职的业务经理------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 5;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

-- 博昕售前
SELECT '-----------------Case19_1: 老板查询博昕售前(在职和离职)------------------' AS 'Case19_1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case19_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case19_2: 老板查询博昕售前(在职和离职)------------------' AS 'Case19_2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = -1;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '测试成功', '测试失败') AS 'Case19_2 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case20_1: 查询在职的博昕售前------------------' AS 'Case20_1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case20_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case20_2: 查询在职的博昕售前------------------' AS 'Case20_2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 0;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '测试成功', '测试失败') AS 'Case20_2 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case21_1: 查询离职的博昕售前------------------' AS 'Case21_1';
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0  AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case21_1 Testing Result';
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

SELECT '-----------------Case21_2: 查询离职的博昕售前------------------' AS 'Case21_2';
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 6;
SET @iStatus = 1;
SET @iOperator = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0  AND @iTotalRecord = 1, '测试成功', '测试失败') AS 'Case21_2 Testing Result';
--
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

-- 不存在的值
SELECT '-----------------Case22: 使用不存在的值(负数)查询角色------------------' AS 'Case22';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -9999999999;
SET @iStatus = -9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case23: 使用不存在的角色ID(负数)查询角色------------------' AS 'Case23';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -9999999999;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case24: 使用不存在的状态(负数)查询角色------------------' AS 'Case24';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 1;
SET @iStatus = -9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;


SELECT '-----------------Case25: 使用不存在的值(正数)查询角色------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 9999999999;
SET @iStatus = 9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case26: 使用不存在的角色ID(正数)查询角色------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = 9999999999;
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT '-----------------Case27: 使用不存在的状态(正数)查询角色------------------' AS 'Case27';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iRoleID = -1;
SET @iStatus = 9999999999;
SET @iOperator = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_StaffRole_RetrieveN(@iErrorCode, @sErrorMsg, @iRoleID, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @iTotalRecord;
SELECT @sErrorMsg;