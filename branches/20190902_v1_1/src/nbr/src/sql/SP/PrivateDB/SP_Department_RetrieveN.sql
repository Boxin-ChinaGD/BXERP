DROP PROCEDURE IF EXISTS `SP_Department_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Department_RetrieveN`( 
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
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
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT F_ID, F_DepartmentName
		FROM T_Department ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;
	
		SELECT found_rows() INTO iTotalRecord;
		
		SELECT iTotalRecord;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;