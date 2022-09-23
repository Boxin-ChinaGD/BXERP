SELECT '++++++++++++++++++ Test_SP_Staff_RetrieveN.sql ++++++++++++++++++++';

SELECT '--------------------- case1:iStatus传0的时候只可以查到在职的员工-------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

SELECT '--------------------- case2:iStatus传0的时候用name值查询，可以查到在职的相应员工 -------------------------' AS 'Case2';
INSERT INTO t_staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('店员6号','189154954','4483198412115667','e12asf','7DBCB7F471CB4C224C6B862BA2BE04','2019-01-01','1',5,1,0,now(),now());

SET @iStatus = 0;
SET @iOperator = 0;
SET @sErrorMsg = '';
SET @string3 = '6号';
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case2 Testing Result';

DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '--------------------- case3:iStatus传-1的时候可以查到所有的员工 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case3 Testing Result';

SELECT '--------------------- case4:iStatus传-1的时候用name值查询，可以查到所有的相应员工 -------------------------' AS 'Case4';
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('店员7号','189154954','4483198412115667','e12asf','7DBCB7F471CB4C224C6B862BA2BE04','2019-01-01','1',5,1,0,now(),now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '7号';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '--------------------- case5:iStatus传递1的时候只可以查询到离职的员工 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT '--------------------- case6：iStatus传1的时候用name值查询，可以查到所有的离职员工 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '4号';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT '--------------------- case7：iStatus传-1的时候用不存在的name值查询 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '9999号';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '--------------------- case8:iStatus传0的时候用phone值查询，可以查到在职的相应员工 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '13144496272';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';


SELECT '--------------------- case9:iStatus传2的时候用phone值查询，可以查到所有相应员工 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '13144496272';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

SELECT '--------------------- case10:用不存在的phone值查询 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '131111';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case10 Testing Result';

SELECT '--------------------- case11:iStatus传-1，iOperator为0的时候，可以查到除售前账号外的所有员工-------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = -1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- 为了和下面结果验证对比(没想到更好的比较方法)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- 用来和@iTotalRecord作对比，比@iTotalRecord多1条
SELECT count(1) INTO @iTotalRecord2 FROM t_staff;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '测试成功', '测试失败') AS 'case11 Testing Result';

SELECT '--------------------- case12:售前为未删除时，iStatus传0，iOperator为0的时候，可以查到除售前账号外的所有员工-------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- 为了和下面结果验证对比(没想到更好的比较方法)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- 用来和@iTotalRecord作对比，比@iTotalRecord多1条
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '测试成功', '测试失败') AS 'case12 Testing Result';

SELECT '--------------------- case13:售前为删除时，iStatus传1，iOperator为0的时候，可以查到除售前外账号的所有员工-------------------------' AS 'Case13';
-- 
UPDATE t_staff SET F_Status = 1 WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 1;
SET @iOperator = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- 为了和下面结果验证对比(没想到更好的比较方法)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- 用来和@iTotalRecord作对比，比@iTotalRecord多1条
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord + 1, '测试成功', '测试失败') AS 'case13 Testing Result';
-- 
UPDATE t_staff SET F_Status = 0 WHERE F_ID = 1;

SELECT '--------------------- case14:售前为未删除时，iStatus传0，iOperator为1的时候，可以查到所有员工-------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string3 = '';
SET @iStatus = 0;
SET @iOperator = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100000000; -- 为了和下面结果验证对比(没想到更好的比较方法)
SET @iTotalRecord = 0;
CALL SP_Staff_RetrieveN(@iErrorCode, @sErrorMsg, @string3, @iStatus, @iOperator, @iPageIndex, @iPageSize, @iTotalRecord);
-- 
SELECT @iTotalRecord;
SELECT @sErrorMsg;
-- 
SET @iTotalRecord2 = 0; -- 用来和@iTotalRecord作对比，比@iTotalRecord多1条
SELECT count(1) INTO @iTotalRecord2 FROM t_staff WHERE F_Status = @iStatus;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord2 = @iTotalRecord, '测试成功', '测试失败') AS 'case14 Testing Result';