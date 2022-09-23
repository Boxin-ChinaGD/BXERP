SELECT '++++++++++++++++++ Test_SP_Warehouse_Create.sql ++++++++++++++++++++';

-- case1:正确的添加仓库
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='仓库10';
SET @sAddress = '植物园';
SET @iStatus = 0;
SET @iStaffID = 1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result case1';

DELETE FROM t_warehouse WHERE F_ID = LAST_INSERT_ID();

-- case2:添加已有同样名字的仓库
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='默认仓库'; 
SET @sAddress = '植物园';
SET @iStatus = 0;
SET @iStaffID = 1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result case2';

-- case3：联系人为不存在的
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='仓库12'; 
SET @sAddress = '植物园';
SET @iStatus = 0;
SET @iStaffID = -1;
SET @sPhone = '123456';

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case3 Testing Result case3';

-- case4:电话，地址为空
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName ='仓库case5'; 
SET @sAddress = NULL;
SET @iStatus = 0;
SET @iStaffID = 3;
SET @sPhone = NULL;

CALL SP_Warehouse_Create(@iErrorCode, @sErrorMsg, @sName, @sAddress, @iStatus, @iStaffID, @sPhone);

SELECT @sErrorMsg;
DELETE FROM t_warehouse WHERE F_ID = LAST_INSERT_ID();

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5Testing Result case5';

