DROP PROCEDURE IF EXISTS `SP_Warehouse_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(32),
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
		
		SELECT F_ID, F_Name, F_Address, F_Status, F_StaffID, F_Phone, F_CreateDatetime, F_UpdateDatetime
		FROM t_warehouse 
		WHERE F_Status != 1 
	--		AND (CASE iBX_CustomerID WHEN 0 THEN 1 = 1 ELSE F_BX_CustomerID = iBX_CustomerID END)
			AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END)
			ORDER BY F_ID DESC 
			LIMIT recordIndex,iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_warehouse 
		WHERE F_Status != 1 
	--		AND (CASE iBX_CustomerID WHEN 0 THEN 1 = 1 ELSE F_BX_CustomerID = iBX_CustomerID END)
			AND (CASE sName WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',sName,'%') END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;