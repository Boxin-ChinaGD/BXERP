DROP PROCEDURE IF EXISTS `SP_BrandSyncCache_DeleteAll`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BrandSyncCache_DeleteAll`(
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
   		
		DELETE FROM t_brandsynccachedispatcher;
		DELETE FROM t_brandsynccache;
	   	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;