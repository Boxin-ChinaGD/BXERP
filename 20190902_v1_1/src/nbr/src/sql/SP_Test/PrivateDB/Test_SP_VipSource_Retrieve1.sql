SELECT '++++++++++++++++++ Test_SP_VipSource_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常查询 -------------------------' AS 'Case1';
INSERT INTO nbr.t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (2, 0, '1111111111111111', '222222222222222222', '33333333333333333');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipSource_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_vipsource WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vipsource WHERE F_ID = @iID;