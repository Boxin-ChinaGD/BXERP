DROP PROCEDURE IF EXISTS `SP_Category_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_Delete`(
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
	
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_CategoryID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '商品表中已有商品的类别';
		ELSE		
			DELETE FROM t_category WHERE F_ID = iID;
			DELETE FROM t_categorysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_categorysynccache WHERE F_SyncData_ID = iID);
			DELETE FROM t_categorysynccache WHERE F_SyncData_ID =  iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;