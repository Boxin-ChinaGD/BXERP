DROP PROCEDURE IF EXISTS `SP_POSSyncCache_POSUpload`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POSSyncCache_POSUpload`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iSyncData_ID INT,
   	IN iSyncSequence INT,
   	IN cSyncType CHAR(1),
   	IN iPOSID INT -- 可能为-1，表明是网页端在创建同步块
	)
BEGIN
  	DECLARE iMasterID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	    SELECT F_ID INTO iMasterID FROM t_possynccache WHERE F_SyncData_ID = iSyncData_ID AND F_SyncType = 'U';
		IF (iMasterID IS NOT NULL) THEN
		   
			DELETE FROM t_possynccachedispatcher WHERE F_SyncCacheID = iMasterID ;
			DELETE FROM t_possynccache WHERE F_ID = iMasterID;	
			
		END IF;
		
		INSERT INTO t_possynccache (F_SyncData_ID, F_SyncSequence, F_SyncType) VALUES (iSyncData_ID, iSyncSequence, cSyncType);
		SELECT F_ID, F_SyncData_ID, F_SyncSequence, F_SyncType,F_CreateDatetime FROM t_possynccache WHERE F_ID = LAST_INSERT_ID();
		SET @last_ID = LAST_INSERT_ID();
		
		IF iPOSID > 0 THEN
			INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID) VALUES (LAST_INSERT_ID(), iPOSID);
			SELECT F_ID, F_SyncCacheID, F_POS_ID,F_CreateDatetime FROM t_possynccachedispatcher WHERE F_ID = LAST_INSERT_ID();
		END IF;
		
	   	IF iSyncData_ID <> iPOSID THEN
			-- 被CUD的POS,也应当告诉其他POS自己已经同步
			INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID) VALUES (@last_ID, iSyncData_ID);
	   		SELECT F_ID, F_SyncCacheID, F_POS_ID,F_CreateDatetime FROM t_possynccachedispatcher WHERE F_ID = LAST_INSERT_ID();
	  	END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;