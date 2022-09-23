DROP PROCEDURE IF EXISTS `SP_Provider_RetrieveN_CheckUniqueField`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_RetrieveN_CheckUniqueField`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN fieldToCheckUnique INT,
	IN sString1 VARCHAR(32)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET @name := '';
		
		IF fieldToCheckUnique = 1 THEN
			SELECT 1 FROM t_provider WHERE F_Name = sString1 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
			SET @name := '�ù�Ӧ�������Ѵ���';
	  
	   	ELSEIF fieldToCheckUnique = 2 THEN
	   	  	SELECT 1 FROM t_provider WHERE F_Mobile = sString1 AND (CASE iID WHEN 0 THEN 1=1 ELSE F_ID <> iID END);
	   		SET @name := '����ϵ�˵绰�Ѵ���';
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