DROP PROCEDURE IF EXISTS `SP_Permission_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Permission_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(20),
	IN sDomain VARCHAR(16),
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
		
		SELECT F_ID, F_SP, F_Name, F_Domain, F_Remark
		FROM t_Permission
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		AND (CASE sDomain WHEN '' THEN 1=1 ELSE F_Domain LIKE CONCAT('%',sDomain,'%') END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_Permission
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		AND (CASE sDomain WHEN '' THEN 1=1 ELSE F_Domain LIKE CONCAT('%',sDomain,'%') END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;