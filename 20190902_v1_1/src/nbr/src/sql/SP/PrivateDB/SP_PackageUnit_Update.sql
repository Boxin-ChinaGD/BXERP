DROP PROCEDURE IF EXISTS `SP_PackageUnit_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PackageUnit_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(8)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_PackageUnit WHERE F_Name = sName AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '包装单位重复,不能进行修改';
		ELSE		
			UPDATE t_PackageUnit SET F_Name = sName,F_UpdateDatetime = now() WHERE F_ID = iID;	 
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_PackageUnit WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;	
END;