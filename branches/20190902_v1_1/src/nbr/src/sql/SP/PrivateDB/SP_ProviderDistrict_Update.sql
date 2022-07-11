DROP PROCEDURE IF EXISTS `SP_ProviderDistrict_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ProviderDistrict_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(20)
	)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;  	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_providerdistrict WHERE F_ID = iID) THEN
			SET iErrorCode := 4;
			SET sErrorMsg := '不能修改一个不存在的ID';
		ELSEIF EXISTS (SELECT 1 FROM t_providerdistrict WHERE F_Name = sName AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能修改为已有名称';
		ELSE		
			UPDATE t_providerdistrict SET F_Name = sName,F_UpdateDatetime = now() WHERE F_ID = iID;
			
			SELECT F_ID,
			F_Name,
			F_CreateDatetime,
			F_UpdateDatetime
			FROM t_providerdistrict WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;