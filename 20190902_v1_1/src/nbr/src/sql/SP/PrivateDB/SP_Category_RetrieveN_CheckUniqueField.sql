DROP PROCEDURE IF EXISTS `SP_Category_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_RetrieveN_CheckUniqueField`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iFieldToCheck INT,
	IN uniqueField VARCHAR(32)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iFieldToCheck = 1 THEN
			SELECT 1 FROM t_category WHERE F_Name = uniqueField AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	
			IF found_rows() = 1 THEN 
		    	SET iErrorCode := 1;
		   		SET sErrorMsg := '该商品小类已存在';
	   		ELSE 
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		END IF;
	   	END IF;
   	
   	COMMIT;
END;