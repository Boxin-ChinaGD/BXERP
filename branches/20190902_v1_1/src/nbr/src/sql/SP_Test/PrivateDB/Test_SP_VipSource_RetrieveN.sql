SELECT '++++++++++++++++++ Test_SP_VipSource_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';
INSERT INTO nbr.t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (2, 0, '1111111111111111', '222222222222222222', '33333333333333333');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_VipSource_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vipsource WHERE F_ID = @iID;