DROP PROCEDURE IF EXISTS `SP_Shop_Update`;
CREATE DEFINER='root'@'localhost' PROCEDURE `SP_Shop_Update`(
	OUT iErrorCode INT,  
	OUT sErrorMsg VARCHAR(64), 
	IN iID INT,
	IN sName VARCHAR(20),
	IN sAddress VARCHAR(30),
	IN iDistrictID INT,
	IN fLongitude Decimal(20,6),
	IN fLatitude Decimal(20,6),
--	IN sKey VARCHAR(32), 不可以更新key
	IN sRemark VARCHAR(30)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3; 
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT 1 FROM t_shop WHERE F_Name = sName AND F_ID <> iID) THEN
			SET sErrorMsg := '门店名称重复';
			SET iErrorCode := 1;
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shopdistrict WHERE F_ID = iDistrictID) THEN 		
			SET iErrorCode := 7;
			SET sErrorMsg := '不能修改成不存在的门店区域';
		ELSEIF EXISTS(SELECT 1 FROM t_shop WHERE F_Status = 3 AND F_ID = iID) THEN 		
			SET iErrorCode := 7;
			SET sErrorMsg := '不能修改已冻结的门店';
		ELSE
	 		UPDATE t_shop SET 
	 			F_Name = sName, 
	 			F_Address = sAddress,
	 			F_DistrictID = IF(iDistrictID = 0, NULL, iDistrictID),
	 			F_Longitude = fLongitude,
				F_Latitude = fLatitude,
--				F_Key = sKey,
				F_Remark = sRemark,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
	 	  
			SELECT 
				F_ID, 
				F_Name, 
				F_CompanyID, 
				F_BXStaffID, 
				F_Address, 
				F_DistrictID,
				F_Status, 
				F_Longitude, 
				F_Latitude, 
				F_Key, 
				F_Remark, 
				F_CreateDatetime, 
				F_UpdateDatetime 
			FROM t_shop WHERE F_ID = iID;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END; 