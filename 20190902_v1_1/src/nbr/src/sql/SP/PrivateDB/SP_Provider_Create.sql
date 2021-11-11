DROP PROCEDURE IF EXISTS `SP_Provider_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_Create`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
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
	
		IF EXISTS (SELECT 1 FROM t_provider WHERE F_Name = sName) OR EXISTS (SELECT 1 FROM t_provider WHERE F_Mobile = sMobile AND (sMobile IS NOT NULL) AND F_Mobile <> '') THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能新建重复的供应商名称和电话';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_providerdistrict WHERE F_ID = iDistrictID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能新建不存在的供应商区域';
		ELSE	
			INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
			VALUES (
		   		sName, 
		   		IF(iDistrictID = 0, NULL, iDistrictID), 
		   		sAddress, 
		   		sContactName, 
		   		sMobile);
			
			SELECT F_ID, F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile,F_CreateDatetime,F_UpdateDatetime 
			FROM t_provider WHERE F_ID = last_insert_id();
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
    COMMIT;
END;