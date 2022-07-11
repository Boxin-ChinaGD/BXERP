DROP PROCEDURE IF EXISTS `SP_POS_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sPOS_SN VARCHAR(32),
	IN iShopID INT,
	IN iStatus INT ,
	IN iReturnSalt INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE INVALID_STATUS INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		SELECT F_Value INTO INVALID_STATUS FROM t_nbrconstant WHERE F_Key = 'INVALID_STATUS'; 
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT 
			F_ID, 
			F_POS_SN, 
			F_ShopID, 
			F_pwdEncrypted, 
			IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt, 
			F_PasswordInPOS, 
			F_Status 
		FROM t_pos WHERE 1 = 1
		AND (CASE sPOS_SN WHEN '' THEN 1 = 1 ELSE F_POS_SN LIKE CONCAT('%',sPOS_SN,'%') END)
		AND (CASE iShopID WHEN INVALID_ID THEN 1 = 1 ELSE F_ShopID = iShopID END)
		AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE F_Status = iStatus END)
		ORDER BY F_ID DESC;
			
		SELECT count(1) into iTotalRecord FROM t_pos 
		WHERE 1 = 1
		AND (CASE sPOS_SN WHEN '' THEN 1 = 1 ELSE F_POS_SN LIKE CONCAT('%',sPOS_SN,'%') END)
		AND (CASE iShopID WHEN INVALID_ID THEN 1 = 1 ELSE F_ShopID = iShopID END)
		AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE F_Status = iStatus END);
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;