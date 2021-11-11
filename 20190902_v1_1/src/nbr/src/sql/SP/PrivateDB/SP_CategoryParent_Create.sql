DROP PROCEDURE IF EXISTS `SP_CategoryParent_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategoryParent_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
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
	
		IF EXISTS(SELECT 1 FROM T_CategoryParent WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '大类名称已存在';
		ELSE
			INSERT INTO T_CategoryParent (F_Name) VALUES (sName);
			
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM T_CategoryParent WHERE F_ID = last_insert_id();
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END