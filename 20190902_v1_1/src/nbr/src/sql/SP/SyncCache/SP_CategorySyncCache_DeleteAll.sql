DROP PROCEDURE IF EXISTS `SP_CategorySyncCache_DeleteAll`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategorySyncCache_DeleteAll`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		DELETE FROM t_categorysynccachedispatcher;
		DELETE FROM t_categorysynccache;
		   	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;