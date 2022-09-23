DROP PROCEDURE IF EXISTS `SP_Company_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStatus INT,
	IN sSN VARCHAR(8),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;

	  	SET iPageIndex = iPageIndex -1;
	  	SET recordIndex = iPageIndex * iPageSize;
	  
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
			FROM nbr_bx.t_company
		    WHERE  F_Status = iStatus
		    AND (CASE sSN WHEN '' THEN 1=1 ELSE F_SN = sSN END)
		    ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) into iTotalRecord
			FROM nbr_bx.t_company
			WHERE  F_Status = iStatus
			AND (CASE sSN WHEN '' THEN 1=1 ELSE F_SN = sSN END);
			
		    SET iErrorCode := 0;
		    SET sErrorMsg := '';
	  
		
	COMMIT;
END;