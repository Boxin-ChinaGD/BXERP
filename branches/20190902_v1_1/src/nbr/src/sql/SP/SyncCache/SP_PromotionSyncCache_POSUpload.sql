DROP PROCEDURE IF EXISTS `SP_PromotionSyncCache_POSUpload`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PromotionSyncCache_POSUpload`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iSyncData_ID INT,
   	IN iSyncSequence INT,
   	IN cSyncType CHAR(1),
   	IN iPOSID INT
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
	
		SELECT F_ID INTO iMasterID FROM t_promotionsynccache WHERE F_SyncData_ID = iSyncData_ID AND F_SyncType = 'U';
		
		IF(iMasterID IS NOT NULL) THEN
			
			DELETE FROM t_promotionsynccachedispatcher WHERE F_SyncCacheID = iMasterID ;
			DELETE FROM t_promotionsynccache WHERE F_ID = iMasterID;	
		END IF;
	 
		INSERT INTO t_promotionsynccache (F_SyncData_ID, F_SyncSequence, F_SyncType) VALUES (iSyncData_ID, iSyncSequence, cSyncType);
		SELECT F_ID, F_SyncData_ID, F_SyncSequence, F_SyncType,F_CreateDatetime FROM t_promotionsynccache WHERE F_ID = LAST_INSERT_ID();
		
		IF iPOSID > 0 THEN	-- 网页端创建商品时，iPOSID <= 0
			INSERT INTO t_promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID) VALUES (LAST_INSERT_ID(), iPOSID);
			SELECT F_ID, F_SyncCacheID, F_POS_ID,F_CreateDatetime FROM t_promotionsynccachedispatcher WHERE F_ID = LAST_INSERT_ID();
		END IF ;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;