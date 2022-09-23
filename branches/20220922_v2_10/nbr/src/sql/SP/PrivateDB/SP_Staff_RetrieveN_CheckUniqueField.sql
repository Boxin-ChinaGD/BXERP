DROP PROCEDURE IF EXISTS `SP_Staff_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_RetrieveN_CheckUniqueField`(
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
	
		SET @name := '';
		
		IF iInt1 = 1 THEN
			SELECT 1 FROM t_staff WHERE F_Phone = sString1 AND F_Status = 0 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '该手机号已存在';
	   	ELSEIF iInt1 = 2 THEN
	   	  	SELECT 1 FROM t_staff WHERE F_ICID = sString1 AND F_Status = 0 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '该身份证号已存在';
	   	ELSEIF iInt1 = 3 THEN
	   	  	SELECT 1 FROM t_staff WHERE F_WeChat = sString1 AND F_Status = 0 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   	  	SET @name := '该微信号已存在';
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