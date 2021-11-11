DROP PROCEDURE IF EXISTS `SP_Shop_RetrieveNByFields`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_RetrieveNByFields`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 VARCHAR(32),
	IN iDistrictID INT,
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
	   		F_CompanyID, 
	   		F_BXStaffID, 
	   		F_Address, 
	   		F_DistrictID,
	   		F_Status, 
	   		F_Longitude, 
	   		F_Latitude, 
	   		F_Key, 
	   		F_Remark,
	   		F_CreateDatetime,
	   		F_UpdateDatetime,
	   		(SELECT F_Name FROM t_shopdistrict WHERE F_ID = sh.F_DistrictID LIMIT 1) as districtName
	   	FROM t_shop sh
	    WHERE F_Name LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
	   	AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END)
		ORDER BY F_ID DESC 
		LIMIT recordIndex, iPageSize;
	
		
		SELECT count(1) into iTotalRecord FROM t_shop
		WHERE F_Name LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
	   	AND (CASE iDistrictID WHEN INVALID_ID THEN 1=1 ELSE F_DistrictID = iDistrictID END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;