DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_RetrieveNByFields`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_RetrieveNByFields`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 VARCHAR(32),
	IN iStaffID INT,
	IN dtDate1 DATETIME,		
	IN dtDate2 DATETIME,
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
	
	  	SET iPageIndex = iPageIndex -1;
	  	
	  	SET recordIndex = iPageIndex * iPageSize;
	
		SELECT 	
			F_ID, 
			F_ShopID,
			F_SN,
			F_Status,
			F_StaffID,
			F_ProviderID,
			F_ProviderName,
			F_ApproverID,
			F_Remark, 
			F_CreateDatetime, 
			F_ApproveDatetime, 
			F_EndDatetime 
		FROM
		(
		SELECT 
			F_ID, 
			F_ShopID,
			F_SN,
			F_Status,
			F_StaffID,
			F_ProviderID,
			F_ProviderName, 
			F_ApproverID,
			F_Remark,
			F_CreateDatetime, 
			F_ApproveDatetime, 
			F_EndDatetime
		FROM t_purchasingorder po
		WHERE F_Status <> 4 
		AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	  	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ID IN (
		SELECT poc.F_PurchasingOrderID FROM t_purchasingordercommodity poc
		WHERE poc.F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
		)
		UNION
		SELECT 
			F_ID, 
			F_ShopID,
			F_SN,
			F_Status,
			F_StaffID,
			F_ProviderID,
			F_ProviderName,
			F_ApproverID,
			F_Remark, 
			F_CreateDatetime, 
			F_ApproveDatetime, 
			F_EndDatetime
		FROM t_purchasingorder po
		WHERE F_Status <> 4 
	   	AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	  	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ProviderName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
		UNION
		SELECT 
			F_ID, 
			F_ShopID,
			F_SN,
			F_Status,
			F_StaffID,
			F_ProviderID,
			F_ProviderName,
			F_ApproverID,
			F_Remark, 
			F_CreateDatetime, 
			F_ApproveDatetime, 
			F_EndDatetime
		FROM t_purchasingorder po
		WHERE F_Status <> 4 
	   	AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	  	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ID = string1
		) AS TMP
		WHERE F_Status <> 4 
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		SELECT count(1) into iTotalRecord
		FROM 
		(
		SELECT F_ID FROM t_purchasingorder po
		WHERE F_Status <> 4 
		AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	   	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ID 
			IN(SELECT poc.F_PurchasingOrderID FROM t_purchasingordercommodity poc WHERE poc.F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
		)
		UNION
		SELECT F_ID FROM t_purchasingorder po 
		WHERE F_Status <> 4 
	    AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	    AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ProviderName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
		UNION
		SELECT F_ID FROM t_purchasingorder po 
		WHERE F_Status <> 4 
	    AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
	  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
	    AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE po.F_StaffID = iStaffID END)
		AND po.F_ID = string1
		) AS TMP;
	
		SET iErrorCode=0;
		SET sErrorMsg := '';
	
	COMMIT;
END;