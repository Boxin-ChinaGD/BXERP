DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN iStatus INT,
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
	DECLARE INVALID_STATUS INT;
	DECLARE INVALID_ID INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_STATUS FROM t_nbrconstant WHERE F_Key = 'INVALID_STATUS';
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';   
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		-- 0：未审核、1：已审核、2：部分入库、3：全部入库、4：已删除
		IF iStatus IN (INVALID_STATUS, 0, 1, 2, 3) THEN 
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
				FROM t_purchasingorder 
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
					AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
		  			AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
		  			AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
		  			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
		   			AND F_ID IN (SELECT F_PurchasingOrderID FROM t_purchasingordercommodity WHERE F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%'))
		   		-- 
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
				FROM t_purchasingorder
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
					AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
		  			AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
		  			AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
		  			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
		  			AND F_ProviderName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
		  		-- 
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
				FROM t_purchasingorder
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
					AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
		  			AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
		  			AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = iStaffID END)
		  			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
					AND length(string1) > 9 AND F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%')
	   		) AS TMP
			WHERE F_Status <> 4 
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
		   			
			SELECT count(1) into iTotalRecord
			FROM 
			(
				SELECT F_ID FROM t_purchasingorder po
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
					AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
				  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)	
				   	AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE po.F_StaffID = iStaffID END)
				   	AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
					AND F_ID IN(SELECT F_PurchasingOrderID FROM t_purchasingordercommodity WHERE F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%'))
				-- 
				UNION
				SELECT F_ID FROM t_purchasingorder po 
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
			    	AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
			  		AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
			    	AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE po.F_StaffID = iStaffID END)
			    	AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
					AND po.F_ProviderName LIKE CONCAT('%',replace(string1, '_', '\_'), '%')
			   	-- 
				UNION
				SELECT F_ID FROM t_purchasingorder po 
				WHERE (CASE iStatus WHEN INVALID_STATUS THEN F_Status != 4  ELSE F_Status = iStatus  END)
				    AND (CASE ifnull(dtDate1, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime >= dtDate1 END)
				  	AND (CASE ifnull(dtDate2, 0) WHEN 0 THEN 1=1 ELSE F_CreateDatetime <= dtDate2 END)
				    AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE po.F_StaffID = iStaffID END)
				    AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
					AND length(string1) > 9 AND po.F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%') 
			) AS TMP;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
   
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := '不能查询未定义状态或已删除的采购单';
		END IF;
		
	COMMIT;
END;