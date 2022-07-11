SELECT '++++++++++++++++++Test_SP_Brand_Retrieve1.sql+++++++++++++++++++++++';

INSERT INTO T_Brand(F_Name)
VALUES('益力99');

SET @iID = last_insert_id();
SET @sErrorMsg = '';

CALL SP_Brand_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM T_Brand WHERE F_ID = @iID;