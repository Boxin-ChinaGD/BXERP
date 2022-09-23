DROP PROCEDURE IF EXISTS `SP_ShopDistrict_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ShopDistrict_Create`(
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
	
		IF EXISTS (SELECT 1 FROM t_shopdistrict WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能重复添加供应商区域';
		ELSE
			INSERT INTO t_shopdistrict (F_Name) VALUES (sName);
			
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_shopdistrict WHERE F_ID = LAST_INSERT_ID();
		   
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
   	COMMIT;
END;