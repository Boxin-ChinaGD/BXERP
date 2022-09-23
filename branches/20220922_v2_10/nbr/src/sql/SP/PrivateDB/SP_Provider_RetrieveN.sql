DROP PROCEDURE IF EXISTS `SP_Provider_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_RetrieveN`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
 	IN sName VARCHAR(32),
 	IN iDistrictID INT,
 	IN sAddress VARCHAR(50),
 	IN sContactName VARCHAR(20),
 	IN sMobile VARCHAR(11),
 	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT 
			F_ID, 
			F_Name, 
			F_DistrictID, 
			F_Address, 
			F_ContactName, 
			F_Mobile 
		FROM t_provider p
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END)
		AND (CASE sAddress WHEN '' THEN 1=1 ELSE F_Address LIKE CONCAT('%',sAddress,'%') END)
		AND (CASE sContactName WHEN '' THEN 1=1 ELSE F_ContactName LIKE CONCAT('%',sContactName,'%') END)
		AND (CASE sMobile WHEN '' THEN 1=1 ELSE F_Mobile LIKE CONCAT('%',sMobile,'%') END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	        
		SELECT count(1) into iTotalRecord FROM t_provider
		WHERE 1 = 1
		AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
		AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END)
		AND (CASE sAddress WHEN '' THEN 1=1 ELSE F_Address LIKE CONCAT('%',sAddress,'%') END)
		AND (CASE sContactName WHEN '' THEN 1=1 ELSE F_ContactName LIKE CONCAT('%',sContactName,'%') END)
		AND (CASE sMobile WHEN '' THEN 1=1 ELSE F_Mobile LIKE CONCAT('%',sMobile,'%') END);
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';   
	
	COMMIT;	
END;