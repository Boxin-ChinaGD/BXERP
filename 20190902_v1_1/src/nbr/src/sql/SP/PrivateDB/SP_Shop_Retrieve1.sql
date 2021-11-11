DROP PROCEDURE IF EXISTS `SP_Shop_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN
	DECLARE iCompanyID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_Name, F_CompanyID, F_BXStaffID, F_Address, F_DistrictID, F_Status, F_Longitude, F_Latitude, F_Key, F_Remark, F_CreateDatetime, F_UpdateDatetime FROM t_shop WHERE F_ID = iID;
		
		-- 前端需要传值回去，所以增加了一个返回的结果集
		SELECT 
			F_ID, 
			F_SN, 
			F_BossPassword, 
			F_Name, 
			F_BossName,
			F_BusinessLicenseSN, 
			F_BusinessLicensePicture, 
			F_BossPhone, 
			F_BossWechat, 
			F_DBName, 
			F_Key, 
			F_Status, 
			F_Submchid,
			F_CreateDatetime, 
			F_UpdateDatetime, 
			F_ExpireDatetime,
			F_ShowVipSystemTip,
			F_DBUserName,
			F_DBUserPassword,
			F_Logo,
			F_BrandName
		FROM nbr_bx.t_company WHERE F_ID IN (SELECT F_CompanyID FROM t_shop WHERE F_ID = iID);
	
		SELECT 
			F_ID, 
			F_Mobile, 
			F_pwdEncrypted, 
			F_Salt, 
			F_RoleID, 
			F_DepartmentID, 
			F_Name, 
			F_Sex, 
			F_ICID
		FROM nbr_bx.t_bxstaff WHERE F_ID IN (SELECT F_BXStaffID FROM t_shop WHERE F_ID = iID);
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;