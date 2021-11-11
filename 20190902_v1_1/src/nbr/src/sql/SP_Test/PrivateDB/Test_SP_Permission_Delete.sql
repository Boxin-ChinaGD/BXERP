SELECT '++++++++++++++++++ Test_SP_Permission_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 删除已有角色在使用权限，错误码为7 -------------------------' AS 'Case1';

SET @iID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @bForceDelete = 0;

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 删除没有角色在使用的权限 -------------------------' AS 'Case2';

SET @sName = '修改1';
SET @sRemark = '修改会员';
SET @bForceDelete = 0;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Permission (F_Name, F_Remark)
VALUES (@sName, @sRemark);

SET @iID = LAST_INSERT_ID();

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: 当bForceDelete=1的时候可以强制删除 -------------------------' AS 'Case3';

INSERT INTO T_Permission (F_SP, F_Name, F_Domain, F_Remark) VALUES ('SP_Barcodes','添加条形码',"条形码","");

SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @bForceDelete = 1;

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';