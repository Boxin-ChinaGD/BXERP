DROP PROCEDURE IF EXISTS `SP_Company_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	    SELECT 
			F_ID, 
			F_Name, 
			F_SN, 
			F_BusinessLicenseSN, 
			F_BusinessLicensePicture, 
			F_BossName, 
			F_BossPhone, 
			F_BossPassword,
			F_BossWechat, 
			F_DBName, 
			F_Key, 
			F_DBUserName, 
			F_DBUserPassword, 
			F_Status, 
			F_Submchid,
			F_BrandName,
			F_CreateDatetime, 
			F_UpdateDatetime, 
			F_ExpireDatetime,
			F_ShowVipSystemTip,
			F_Logo
	    FROM nbr_bx.t_company WHERE F_ID = iID AND F_Status = 0;
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;