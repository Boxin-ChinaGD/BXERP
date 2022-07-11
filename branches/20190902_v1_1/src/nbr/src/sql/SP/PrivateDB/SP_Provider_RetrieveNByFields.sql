DROP PROCEDURE IF EXISTS `SP_Provider_RetrieveNByFields`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_RetrieveNByFields`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 VARCHAR(32),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	  	SET iPageIndex = iPageIndex -1;
	  	
	  	SET recordIndex = iPageIndex * iPageSize;
	
		SELECT 
			F_ID, 
			F_Name, 
			F_DistrictID, 
			F_Address, 
			F_ContactName, 
			F_Mobile 
		FROM t_provider p
		WHERE F_Name LIKE CONCAT('%', string1, '%')
			OR F_ContactName LIKE CONCAT('%', string1, '%')
			OR length(string1) > 3 AND F_Mobile LIKE CONCAT('%', string1, '%')
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_provider p
		WHERE F_Name LIKE CONCAT('%', string1, '%')
			OR F_ContactName LIKE CONCAT('%', string1, '%')
			OR length(string1) > 3 AND F_Mobile LIKE CONCAT('%', string1, '%');
	
		SET iErrorCode=0;
		SET sErrorMsg := '';
	
	COMMIT;
END;