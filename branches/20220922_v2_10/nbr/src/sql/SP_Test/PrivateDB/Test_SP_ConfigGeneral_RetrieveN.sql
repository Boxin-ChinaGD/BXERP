SELECT '++++++++++++++++++ Test_SP_ConfigGeneral_RetrieveN.sql ++++++++++++++++++++';

-- ��Ҫ������
--	INSERT INTO t_configgeneral (F_Name, F_Value)
--	VALUES ("aaaaa", "100000000");
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;

CALL SP_ConfigGeneral_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- DELETE FROM t_configgeneral WHERE F_ID = LAST_INSERT_ID();-- ��Ҫ������