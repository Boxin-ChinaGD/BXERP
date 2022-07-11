DROP PROCEDURE IF EXISTS `SP_BrandSyncCacheDispatcher_UpdatePOSStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BrandSyncCacheDispatcher_UpdatePOSStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iSyncCacheID INT,
   	IN iPOS_ID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT 1 FROM t_brandsynccachedispatcher WHERE F_SyncCacheID = iSyncCacheID AND F_POS_ID = iPOS_ID) THEN
			SELECT 
				F_ID, 
				F_SyncCacheID, 
				F_POS_ID 
			FROM t_brandsynccachedispatcher WHERE F_SyncCacheID = iSyncCacheID AND F_POS_ID = iPOS_ID;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
			INSERT INTO t_brandsynccachedispatcher (F_SyncCacheID, F_POS_ID) VALUES (iSyncCacheID, iPOS_ID);
			
			SELECT F_ID, F_SyncCacheID, F_POS_ID,F_CreateDatetime FROM t_brandsynccachedispatcher WHERE F_ID = LAST_INSERT_ID();
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;