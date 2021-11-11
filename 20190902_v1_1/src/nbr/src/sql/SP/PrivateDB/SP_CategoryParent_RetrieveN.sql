DROP PROCEDURE IF EXISTS `SP_CategoryParent_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategoryParent_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(10),
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
		
		SELECT F_ID, F_Name
		FROM t_categoryparent
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_categoryparent
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;