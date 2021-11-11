DROP PROCEDURE IF EXISTS `SP_CategorySyncCache_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategorySyncCache_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iSyncData_ID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF (SELECT COUNT(1) FROM t_pos WHERE F_Status = 0) = (SELECT COUNT(1) FROM t_categorysynccachedispatcher WHERE F_SyncCacheID
	   		IN (SELECT F_ID FROM t_categorysynccache WHERE F_SyncData_ID = iSyncData_ID)) THEN
	   		
			DELETE FROM t_categorysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_categorysynccache WHERE F_SyncData_ID = iSyncData_ID);
			DELETE FROM t_categorysynccache WHERE F_SyncData_ID =  iSyncData_ID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := 'POS机还没全部同步';
		END IF;
		
	COMMIT;
	
END;