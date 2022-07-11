DROP PROCEDURE IF EXISTS `SP_CommodityHistory_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CommodityHistory_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 Varchar(64),
	IN iCommodityID INT,
	IN sFieldName Varchar(20),
	IN iStaffID INT,
	IN iShopID INT,
	IN dtStart DATETIME,
	IN dtEnd DATETIME,
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
	  	
	  	SET iPageIndex = iPageIndex -1;
	  	
	  	SET recordIndex = iPageIndex * iPageSize;
	
	
		IF (string1 != '') THEN 
			SELECT
		  		F_ID, 
		  		F_CommodityID, 
		  		F_FieldName, 
			  	F_OldValue, 
			  	F_NewValue, 
			  	F_StaffID, 
			  	F_BySystem, 
			  	F_ShopID,
			  	(SELECT F_Name FROM t_shop WHERE F_ID = F_ShopID) AS F_ShopName,
			  	F_Datetime
			FROM t_commodityhistory c
			WHERE 1=1
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_Datetime >= dtStart END)
	  			AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_Datetime <= dtEnd END)
				AND (c.F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Name LIKE CONCAT('%', replace(string1, '_', '\_') , '%')) 
					OR (length(string1) > 6 AND c.F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_Barcode LIKE CONCAT('%', replace(string1, '_', '\_') , '%'))))   
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
				AND (CASE sFieldName WHEN '' THEN 1=1 ELSE F_FieldName = sFieldName END)
				AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END)		
			ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;	
			  	
		ELSE 
		  	SELECT
			  	F_ID, 
			  	F_CommodityID, 
			  	F_FieldName, 
			  	F_OldValue, 
			  	F_NewValue, 
			  	F_StaffID, 
			  	F_BySystem, 
			  	F_ShopID,
			  	(SELECT F_Name FROM t_shop WHERE F_ID = F_ShopID) AS F_ShopName,
			  	F_Datetime
			FROM t_commodityhistory c
			WHERE 1=1
			AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_Datetime >= dtStart END)
	  		AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_Datetime <= dtEnd END)
			AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
			AND (CASE sFieldName WHEN '' THEN 1=1 ELSE F_FieldName = sFieldName END)
			AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END)
			ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;
		
		END IF;
		
		IF (string1 != '') THEN 
			SELECT count(1) into iTotalRecord
			FROM t_commodityhistory c
			WHERE 1=1
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_Datetime >= dtStart END)
	  			AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_Datetime <= dtEnd END)
				AND (c.F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Name LIKE CONCAT('%', replace(string1, '_', '\_') , '%')) 
					OR (length(string1) > 6 AND c.F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_Barcode LIKE CONCAT('%', replace(string1, '_', '\_') , '%')))))   
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
				AND (CASE sFieldName WHEN '' THEN 1=1 ELSE F_FieldName = sFieldName END)
				AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END);
		ELSE 	
			SELECT count(1) into iTotalRecord
			FROM t_commodityhistory t
		 	WHERE 1=1
			AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_Datetime >= dtStart END)
	  		AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_Datetime <= dtEnd END)
			AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
			AND (CASE sFieldName WHEN '' THEN 1=1 ELSE F_FieldName = sFieldName END)
			AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END);
		END IF;
		 		
		SET iErrorCode := 0;  
		SET sErrorMsg := '';	
		
	COMMIT;
END;