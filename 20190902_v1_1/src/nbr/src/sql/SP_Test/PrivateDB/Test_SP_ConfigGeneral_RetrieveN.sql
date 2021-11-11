SELECT '++++++++++++++++++ Test_SP_ConfigGeneral_RetrieveN.sql ++++++++++++++++++++';

-- 不要乱来！
--	INSERT INTO t_configgeneral (F_Name, F_Value)
--	VALUES ("aaaaa", "100000000");
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;

CALL SP_ConfigGeneral_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- DELETE FROM t_configgeneral WHERE F_ID = LAST_INSERT_ID();-- 不要乱来！