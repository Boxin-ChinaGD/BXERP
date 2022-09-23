DROP PROCEDURE IF EXISTS `SP_ShopDistrict_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ShopDistrict_RetrieveN`(
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT F_ID, F_Name FROM t_shopdistrict
	
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord FROM t_shopdistrict;
	
		SET iErrorCode := 0;
	   	SET sErrorMsg := '';   

	COMMIT;		
END;