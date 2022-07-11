-- 该SP不用于Action，只用于测试
DROP PROCEDURE IF EXISTS `SP_VipSource_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipSource_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
   		
   		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		DELETE FROM t_vipsource WHERE F_ID = iID;
	
	COMMIT;
END;