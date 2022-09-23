DROP PROCEDURE IF EXISTS `SP_Category_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_RetrieveN`(
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	  	SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
	   	SELECT F_ID, F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime
		FROM t_category
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
			
		SELECT count(1) into iTotalRecord
		FROM t_category
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;