DROP PROCEDURE IF EXISTS `SP_BarcodesSyncCacheDispatcher_RetriveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BarcodesSyncCacheDispatcher_RetriveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	OUT iTotalRecord INT

	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_SyncCacheID, F_POS_ID FROM t_barcodessynccachedispatcher;
		SET iTotalRecord := 1; /*... TODO ???*/
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;