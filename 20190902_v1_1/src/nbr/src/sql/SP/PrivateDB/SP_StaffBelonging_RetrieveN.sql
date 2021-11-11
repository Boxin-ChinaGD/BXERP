-- 该SP是直接获取所有的openID信息到缓存，所以不需要分页
DROP PROCEDURE IF EXISTS `SP_StaffBelonging_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_StaffBelonging_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		
		SELECT F_ID, F_OpenID, database() AS 'dbName' FROM t_staff;

	  	SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;