SELECT '++++++++++++++++++Test_SP_Category_Retrieve1.sql+++++++++++++++++++++++';

INSERT INTO T_Category(F_Name, F_ParentID)
VALUES('益力', 1);

SET @iID = last_insert_id();
SET @sErrorMsg = '';

CALL SP_Category_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM T_Category WHERE F_ID = @iID;