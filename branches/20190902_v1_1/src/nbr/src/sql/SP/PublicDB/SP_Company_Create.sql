DROP PROCEDURE IF EXISTS `SP_Company_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_Create`(
	OUT iErrorCode INT,   	
	OUT sErrorMsg VARCHAR(64),
	IN sBusinessLicenseSN VARCHAR (30), 
	IN sBusinessLicensePicture VARCHAR (128),
	IN sBossPhone VARCHAR (32),
	IN sBossPassword VARCHAR(16),
	IN sBossWechat VARCHAR (20),
	IN sDBName VARCHAR (20),
	IN sKey VARCHAR (32),
	IN sName VARCHAR(32),
	IN sBossName VARCHAR(12),
	IN sDBUserName VARCHAR(20),
	IN sDBUserPassword VARCHAR(16),
	IN sSubmchid VARCHAR(10),
	IN sBrandName VARCHAR(20),
	IN sLogo VARCHAR(128)
)
BEGIN
	DECLARE SN INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_DBName = sDBName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司DBName重复';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_BusinessLicenseSN = sBusinessLicenseSN) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已经存在相同的营业执照';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_BusinessLicensePicture = sBusinessLicensePicture AND F_BusinessLicensePicture IS NOT NULL AND F_BusinessLicensePicture <> '') THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司营业执照图片已存在，请联系管理员';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司名称已存在';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_key = sKey) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已存在这个key';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid <> '' AND F_Submchid = sSubmchid AND F_Status = 0) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已存在这个子商户号';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Logo = sLogo AND F_Logo IS NOT NULL AND F_Logo <> '') THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该公司Logo已存在，请联系管理员';
	    ELSE
	    	-- 如果不存在任何公司时，SN按默认668866进行创建
			SET SN = (SELECT Max(F_SN) FROM nbr_bx.t_company); -- 最初没有私有DB
			SET SN = IF(SN IS NULL, 668866, SN + 1);
			
			INSERT INTO nbr_bx.t_company (
				F_SN,
				F_BusinessLicenseSN,
				F_BusinessLicensePicture,
				F_BossPhone,
				F_BossPassword,
				F_BossWechat,
				F_DBName,
				F_Key,
				F_Status,
				F_Name,
		       	F_BossName,
		       	F_DBUserName,
		       	F_DBUserPassword,
		       	F_Submchid,
		       	F_BrandName,
		       	F_Logo
				)
			VALUES (
				SN,  -- varchar(8)
				sBusinessLicenseSN,
				IF(sBusinessLicensePicture = '', NULL, sBusinessLicensePicture),
				sBossPhone,
				sBossPassword,
				sBossWechat,
				sDBName,
				sKey,
				0,
				sName,
				sBossName,
				sDBUserName,
				sDBUserPassword,
				sSubmchid,
				sBrandName,
				IF(sLogo = '', NULL, sLogo)
			);
			
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
			WHERE F_ID = last_insert_id();
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF; 
	
	COMMIT;
END;