DROP PROCEDURE IF EXISTS `SP_Brand_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Brand_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
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
	
	   	IF  EXISTS (SELECT 1 FROM t_brand WHERE F_Name = sName) THEN
	   	    SELECT F_ID, F_Name, F_CreateDatetime, F_UpdateDatetime FROM t_brand WHERE F_Name = sName;
			SET iErrorCode := 1;
			SET sErrorMsg := '品牌已存在';
		ELSE		
			INSERT INTO t_brand (F_Name,F_CreateDatetime) VALUES (sName,now());
			
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_brand WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END