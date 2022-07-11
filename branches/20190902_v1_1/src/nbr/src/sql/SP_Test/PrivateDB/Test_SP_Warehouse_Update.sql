SELECT '++++++++++++++++++ Test_SP_Warehouse_Update.sql ++++++++++++++++++++';

INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ( '仓库922', '植物园', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='仓库998'; 
SET @sAddress = '植物园';
SET @iStaffID = 2;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case2:修改已有的名字
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('仓库9992', '植物园', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='默认仓库'; 
SET @sAddress = '植物园';
SET @iStaffID = 2;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'case2 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case3：修改的StaffID为不存在的.
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ( '仓库9991', '植物园', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='仓库99922'; 
SET @sAddress = '植物园';
SET @iStaffID = -1;
SET @sPhone = '22222';

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'case3 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;

-- case4:地址，电话为空
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('仓库9995', '植物园', 0, 1, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();
SET @sName ='仓库99963'; 
SET @sAddress = NULL;
SET @iStaffID = 3;
SET @sPhone = NULL;

CALL SP_Warehouse_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress,@iStaffID, @sPhone);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case4 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;