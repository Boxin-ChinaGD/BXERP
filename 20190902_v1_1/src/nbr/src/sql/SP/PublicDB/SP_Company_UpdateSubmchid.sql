DROP PROCEDURE IF EXISTS `SP_Company_UpdateSubmchid`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_UpdateSubmchid`(
	OUT iErrorCode INT,   	
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sSubmchid VARCHAR(10)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		IF NOT EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_ID = iID AND F_Status = 0) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '修改的公司不存在';
		ELSEIF EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid <> '' AND F_Submchid = sSubmchid AND F_Status = 0 AND F_ID <> iID) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := '其它公司已存在这个子商户号';
		ELSE     	
			UPDATE nbr_bx.t_company
			SET
		       F_Submchid = sSubmchid,
		       F_UpdateDatetime = now()
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