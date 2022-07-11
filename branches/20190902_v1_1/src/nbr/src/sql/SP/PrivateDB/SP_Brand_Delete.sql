DROP PROCEDURE IF EXISTS `SP_Brand_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Brand_Delete`(
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
	
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_BrandID = iID ) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '品牌表中已有商品的品牌';
		ELSE		
			DELETE FROM t_brand WHERE F_ID = iID;
			DELETE FROM t_brandsynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_brandsynccache WHERE F_SyncData_ID = iID);
			DELETE FROM t_brandsynccache WHERE F_SyncData_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;