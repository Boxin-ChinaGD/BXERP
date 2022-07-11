DROP PROCEDURE IF EXISTS `SP_Warehousing_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iProviderID INT,
	IN iwarehouseID INT,
	IN istaffID INT,
	IN ipurchasingOrderID INT,
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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';  
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
	
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
		FROM t_warehousing
		WHERE 1 = 1
		AND (CASE iID WHEN INVALID_ID THEN 1=1 ELSE F_ID = iID END)
		AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE F_ProviderID = iProviderID END)
		AND (CASE iwarehouseID WHEN INVALID_ID THEN 1=1 ELSE F_WarehouseID = iwarehouseID END)
		AND (CASE istaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = istaffID END)
		-- 入库单可以不引用采购订单。
		AND (CASE ipurchasingOrderID WHEN INVALID_ID THEN 1=1 ELSE F_purchasingOrderID = ipurchasingOrderID END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
			
	    SELECT count(1) into iTotalRecord
	    FROM t_warehousing
		WHERE 1 = 1
		AND (CASE iID WHEN INVALID_ID THEN 1=1 ELSE F_ID = iID END)
		AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE F_ProviderID = iProviderID END)
		AND (CASE iwarehouseID WHEN INVALID_ID THEN 1=1 ELSE F_WarehouseID = iwarehouseID END)
		AND (CASE istaffID WHEN INVALID_ID THEN 1=1 ELSE F_StaffID = istaffID END)
		AND (CASE ipurchasingOrderID WHEN INVALID_ID THEN 1=1 ELSE F_purchasingOrderID = ipurchasingOrderID END);
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;