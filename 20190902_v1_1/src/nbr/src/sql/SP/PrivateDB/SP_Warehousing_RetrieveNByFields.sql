DROP PROCEDURE IF EXISTS `SP_Warehousing_RetrieveNByFields`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_RetrieveNByFields`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 VARCHAR(32),
	IN iShopID INT,
	IN iStaffID INT,
	IN iStatus INT,
	IN iProviderID INT,
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
			
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;

		IF (string1 != '') THEN 
		
		    SELECT 
			    F_ID,
			    F_ShopID,
			    F_SN,
			    F_Status, 
			    F_ProviderID,
			    F_WarehouseID, 
			    F_StaffID, 
			    F_ApproverID, 
			    F_CreateDatetime, 
			    F_PurchasingOrderID, 
			    F_UpdateDatetime
			FROM 
			(
				SELECT 
				    F_ID,
				    F_ShopID, 
				    F_SN,
				    F_Status, 
				    F_ProviderID,
				    F_WarehouseID, 
				    F_StaffID, 
				    F_ApproverID, 
				    F_CreateDatetime, 
				    F_PurchasingOrderID, 
				    F_UpdateDatetime
				FROM t_warehousing ws 
				WHERE length(string1) > 9 AND ws.F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%') 
				UNION
				SELECT 
				    F_ID, 
				    F_ShopID,
				    F_SN,
				    F_Status,
				    F_ProviderID, 
				    F_WarehouseID, 
				    F_StaffID, 
				    F_ApproverID, 
				    F_CreateDatetime, 
				    F_PurchasingOrderID, 
				    F_UpdateDatetime
				FROM t_warehousing ws 
				WHERE ws.F_ID IN(
				SELECT wc.F_WarehousingID FROM t_warehousingcommodity wc 
				WHERE ws.F_ID = wc.F_WarehousingID
				AND wc.F_CommodityName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
				) 
				UNION
				SELECT 
				    F_ID, 
				    F_ShopID,
				    F_SN,
				    F_Status,
				    F_ProviderID, 
				    F_WarehouseID, 
				    F_StaffID, 
				    F_ApproverID, 
				    F_CreateDatetime, 
				    F_PurchasingOrderID, 
				    F_UpdateDatetime
				FROM t_warehousing ws
				WHERE ws.F_ProviderID IN(
				SELECT F_ID FROM t_provider p
				WHERE p.F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
				)
				UNION 
				SELECT 
				    F_ID, 
				    F_ShopID,
				    F_SN,
				    F_Status,
				    F_ProviderID, 
				    F_WarehouseID, 
				    F_StaffID, 
				    F_ApproverID, 
				    F_CreateDatetime, 
				    F_PurchasingOrderID, 
				    F_UpdateDatetime
				FROM t_warehousing ws
				WHERE ws.F_PurchasingOrderID IN(
				SELECT F_ID FROM t_purchasingorder po
				WHERE (CASE (length(string1) >= 10 AND length(string1) <= 20) WHEN 1 THEN po.F_SN LIKE concat('%',replace(string1, '_', '\_'),'%') END)
				) 
			)AS TMP
			WHERE 1=1
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ShopID = iShopID END)
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_StaffID = iStaffID END)
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE TMP.F_Status = iStatus END)
			   	AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ProviderID = iProviderID END)
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtStart END)
	 			AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtEnd END)
	  			
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
		ELSE 
			SELECT 
			    F_ID, 
			    F_ShopID,
			    F_SN,
			    F_Status, 
			    F_ProviderID,
			    F_WarehouseID, 
			    F_StaffID, 
			    F_ApproverID, 
			    F_CreateDatetime, 
			    F_PurchasingOrderID, 
			    F_UpdateDatetime
			FROM t_warehousing ws 
		   	WHERE 1=1
		   		AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE ws.F_ShopID = iShopID END)
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE ws.F_StaffID = iStaffID END)
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE ws.F_Status = iStatus END)
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE ws.F_ProviderID = iProviderID END)
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtStart END)
	 	 		AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtEnd END)
	  			
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
	
		END IF;
	
		IF (string1 != '') THEN 
	
			SELECT COUNT(1) INTO iTotalRecord
			FROM 
			(
				SELECT F_ID FROM t_warehousing ws 
				WHERE length(string1) > 9 AND ws.F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%') 
				UNION
				SELECT F_ID FROM t_warehousing ws 
				WHERE ws.F_ID IN(
				SELECT wc.F_WarehousingID FROM t_warehousingcommodity wc 
				WHERE ws.F_ID = wc.F_WarehousingID
				AND wc.F_CommodityName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
			) 
			UNION
			SELECT F_ID 
			FROM t_warehousing ws
			WHERE ws.F_ProviderID IN(
				SELECT F_ID FROM t_provider p
				WHERE p.F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
			)
			UNION 
			SELECT F_ID 
			FROM t_warehousing ws
			WHERE ws.F_PurchasingOrderID IN(
				SELECT F_ID FROM t_purchasingorder po
				WHERE (CASE (length(string1) >= 10 AND length(string1) <= 20) WHEN 1 THEN po.F_SN LIKE concat('%',replace(string1, '_', '\_'),'%') END)
			)
			)AS TMP
			WHERE 1=1
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_ShopID = iShopID) END)
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_StaffID = iStaffID) END)
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_Status = iStatus ) END)
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_ProviderID = iProviderID) END)
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_CreateDatetime >= dtStart ) END)
				AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_CreateDatetime <= dtEnd ) END);
		
		ELSE 
			SELECT COUNT(1) INTO iTotalRecord
			FROM t_warehousing ws
			WHERE 1=1
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_ShopID = iShopID) END)
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_StaffID = iStaffID) END)
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_Status = iStatus) END)
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_ProviderID = iProviderID) END)
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_CreateDatetime >= dtStart ) END)
				AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE ws.F_ID IN (SELECT F_ID FROM t_warehousing ws WHERE F_CreateDatetime <= dtEnd ) END);
		END IF ;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;