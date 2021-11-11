DROP PROCEDURE IF EXISTS `SP_BrandSyncCacheDispatcher_RetriveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BrandSyncCacheDispatcher_RetriveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	OUT iTotalRecord INT

	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_SyncCacheID, F_POS_ID FROM T_BrandSyncCacheDispatcher;
		SET iTotalRecord := 1; /*... TODO ???*/
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;