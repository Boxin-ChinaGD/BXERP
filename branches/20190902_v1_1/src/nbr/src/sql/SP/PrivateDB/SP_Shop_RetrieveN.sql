DROP PROCEDURE IF EXISTS `SP_Shop_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_RetrieveN`(   -- ...目前没有专门的门店管理。所以目前返回全部
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
	IN iDistrictID INT,
 	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE string1 VARCHAR(20); -- 区域名称
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
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
	   		F_UpdateDatetime,
	   		(SELECT F_Name FROM t_shopdistrict WHERE F_ID = sh.F_DistrictID LIMIT 1) as districtName
	   	FROM t_shop sh
	   	WHERE 1 = 1
	   	AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END)
		ORDER BY F_ID DESC 
		LIMIT recordIndex, iPageSize;
	
		-- 前端需要找出对应的公司和业务经理
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
		FROM nbr_bx.t_company WHERE F_ID IN (SELECT F_CompanyID FROM t_shop);
	
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
		FROM nbr_bx.t_bxstaff WHERE F_ID IN (SELECT F_BXStaffID FROM t_shop);
		
		SELECT count(1) into iTotalRecord FROM t_shop
		WHERE 1 = 1
	   	AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;