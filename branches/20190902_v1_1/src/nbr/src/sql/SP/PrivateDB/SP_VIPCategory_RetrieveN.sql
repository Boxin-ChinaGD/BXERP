DROP PROCEDURE IF EXISTS `SP_VIPCategory_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIPCategory_RetrieveN`(
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
		
		SELECT 
			F_ID, 
			F_Name 
		FROM t_vip_category
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) INTO iTotalRecord 
		FROM t_vip_category;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;