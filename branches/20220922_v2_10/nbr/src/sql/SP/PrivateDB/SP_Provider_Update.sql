DROP PROCEDURE IF EXISTS `SP_Provider_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(32),
	IN iDistrictID INT,
	IN sAddress VARCHAR(50),
	IN sContactName VARCHAR(20),
	IN sMobile VARCHAR(24)
	)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;  	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_provider WHERE F_Name = sName AND F_ID <> iID) 
			OR EXISTS (SELECT 1 FROM t_provider WHERE F_Mobile = sMobile AND F_ID <> iID AND (sMobile IS NOT NULL) AND F_Mobile <> '') THEN
			
			SET iErrorCode := 1;
			SET sErrorMsg := '不能修改成重复的供应商名称';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_providerdistrict WHERE F_ID = iDistrictID) AND iDistrictID <> 0 THEN 
			
			SET iErrorCode := 7;
			SET sErrorMsg := '不能修改成不存在的供应商区域';
		ELSE  
			UPDATE t_provider 
			SET 
				F_Name = sName,
				F_DistrictID =  IF(iDistrictID = 0, NULL, iDistrictID),
				F_Address = sAddress,
		 		F_ContactName = sContactName,
		   		F_Mobile = sMobile,
		   		F_UpdateDatetime = now()
	   		WHERE F_ID = iID;	
			
			
			SELECT 
				F_ID,
				F_Name,
				F_DistrictID,
				F_Address,
				F_ContactName,
				F_Mobile,
				F_CreateDatetime,
				F_UpdateDatetime FROM t_provider WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;