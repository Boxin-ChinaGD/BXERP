DROP PROCEDURE IF EXISTS `SP_Category_RetrieveNByParent`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_RetrieveNByParent`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_Name, F_ParentID FROM t_category WHERE F_ParentID = iID;
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;