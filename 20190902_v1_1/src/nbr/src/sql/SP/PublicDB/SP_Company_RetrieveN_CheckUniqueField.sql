DROP PROCEDURE IF EXISTS `SP_Company_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_RetrieveN_CheckUniqueField`(
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
	
		SET @name := '';
		
		IF iFieldToCheckUnique = 1 THEN
			SELECT 1 FROM t_company WHERE F_Name = sUniqueField AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
			SET @name := '该公司名称已存在';
			
	   	ELSEIF iFieldToCheckUnique = 2 THEN
	   	  	SELECT 1 FROM t_company WHERE F_BusinessLicenseSN = sUniqueField AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '该营业执照号已存在';
-- 该字段已不需要检查   
--	    ELSEIF iInt1 = 3 THEN
--	    	SELECT 1 FROM t_company WHERE F_BusinessLicensePicture = sString1 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
--	  		SET @name := '该营业执照图片已存在';
--	   	
--	   	ELSEIF iInt1 = 4 THEN
--	    	SELECT 1 FROM t_company WHERE F_Key = sString1 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
--	  		SET @name := '该钥匙已存在';
	   	   
	   	ELSEIF iFieldToCheckUnique = 5 THEN
	     	SELECT 1 FROM t_company WHERE F_DBName = sUniqueField AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	  		SET @name := '该DB名称已存在';
			
		ELSEIF iFieldToCheckUnique = 6 THEN
	     	SELECT 1 FROM t_company WHERE F_Submchid = sUniqueField AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	  		SET @name := '该子商户号已存在';
	   	 
	   	END IF;
	   	
	   	IF found_rows() = 1 THEN
	   		SET iErrorCode := 1;
	   	 	SET sErrorMsg := @name;
	   	ELSE
	   		SET iErrorCode := 0;
	   		SET sErrorMsg := '';
	   	END IF;     	
   	
   	COMMIT;
END;