DROP PROCEDURE IF EXISTS `SP_Shop_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_RetrieveN_CheckUniqueField`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iFieldToCheckUnique INT,
	IN sUniqueField VARCHAR(32)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iFieldToCheckUnique = 1 THEN
			SELECT 1 FROM t_shop WHERE F_Name = sUniqueField  AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	
			IF found_rows() = 1 THEN 
		    	SET iErrorCode := 1;
		   		SET sErrorMsg := '该门店名称已存在';
	   		ELSE 
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		END IF;
	   	END IF;
	
	COMMIT;
END;