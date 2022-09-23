DROP PROCEDURE IF EXISTS `SP_Commodity_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_RetrieveN_CheckUniqueField`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iInt1 INT,
	IN sString1 VARCHAR(32)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iInt1 = 1 THEN
			SELECT 1 FROM t_commodity WHERE F_Name = sString1 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END)  AND F_Status <> 2;
		
			IF found_rows() = 1 THEN 
		    	SET iErrorCode := 1;
		   		SET sErrorMsg := '该商品名称已经存在';
	   		ELSE 
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		END IF;
	   	END IF;
	 
	COMMIT;
END;