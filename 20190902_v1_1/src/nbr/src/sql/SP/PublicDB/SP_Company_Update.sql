DROP PROCEDURE IF EXISTS `SP_Company_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_Update`(
	OUT iErrorCode INT,   	
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sBusinessLicenseSN VARCHAR (30), 
	IN sBusinessLicensePicture VARCHAR (128),
	IN sBossPhone VARCHAR (32),
	IN sBossWechat VARCHAR (20),
	IN sKey VARCHAR (32),
	IN sName VARCHAR(32),
	IN sBossName VARCHAR(12),
	IN sBrandName VARCHAR(20),
	IN sLogo VARBINARY(128)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;

		IF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Name = sName AND F_ID <> iID)  THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '公司名称重复';
	   	ELSEIF sBusinessLicenseSN <> '' AND EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_BusinessLicenseSN = sBusinessLicenseSN AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已经存在相同的营业执照';
	   	ELSEIF sBusinessLicensePicture <> '' AND EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_BusinessLicensePicture = sBusinessLicensePicture AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司营业执照图片已存在，请联系管理员';
	   	ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Key = sKey AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已存在这个key';
		ELSEIF sLogo <> '' AND EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Logo = sLogo AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司Logo图片已存在，请联系管理员';
		ELSE 
		   UPDATE nbr_bx.t_company
	       SET
		       F_BusinessLicenseSN = sBusinessLicenseSN,
		       F_BusinessLicensePicture = IF(sBusinessLicensePicture = '', NULL, sBusinessLicensePicture),
               F_BossPhone = sBossPhone,
	           F_BossWechat = sBossWechat,
--          在UI界面 update两个公司，key 传不过来 ，都为null 重复
--		       F_Key = sKey,
		       F_Name = sName,
		       F_BossName = sBossName,
		       F_BrandName = sBrandName,
		       F_UpdateDatetime = now(),
		       F_Logo = IF(sLogo = '', NULL, sLogo)
	       WHERE F_ID = iID;
	       
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
		   WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF; 
	
	COMMIT;
END;