DROP PROCEDURE IF EXISTS `SP_Vip_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Vip_RetrieveN_CheckUniqueField`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN fieldToCheckUnique INT,
	IN queryKeyword VARCHAR(32)
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
		
		IF fieldToCheckUnique = 1 THEN
			SELECT 1 FROM t_vip WHERE F_Mobile = queryKeyword AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
			SET @name := '该手机号已存在';
			
	   	ELSEIF fieldToCheckUnique = 2 THEN
	   	  	SELECT 1 FROM t_vip WHERE F_ICID = queryKeyword AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '该身份证号已存在';
	   		
	   	ELSEIF fieldToCheckUnique = 5 THEN
	   		SELECT 1 FROM t_vip WHERE F_Email = queryKeyword AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '该邮箱已存在';
	   		
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