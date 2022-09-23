DROP PROCEDURE IF EXISTS `SP_Brand_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Brand_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(20)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_brand WHERE F_Name = sName AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '品牌名称重复';
		ELSE		
			UPDATE t_brand SET F_Name = sName,F_UpdateDatetime=now() WHERE F_ID = iID;	
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_brand WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;