DROP PROCEDURE IF EXISTS `SP_CategoryParent_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategoryParent_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(10)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM T_CategoryParent WHERE F_Name = sName AND F_ID <> iID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '已有重复的名字';
		ELSE		
			UPDATE T_CategoryParent SET F_Name = sName,F_UpdateDatetime = now() WHERE F_ID = iID;	
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM T_CategoryParent WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;