DROP PROCEDURE IF EXISTS `SP_Inventory_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN iStatus INT,
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
		
		-- iStatus：0=待录入、1=已提交、2=已审核
		
		IF iStatus = 0 OR iStatus = 1 OR iStatus = 2 THEN
	   		SELECT 
				F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
	   		FROM t_inventorysheet
			WHERE F_Status = iStatus 
			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
	
			SELECT count(1) INTO iTotalRecord 
			FROM t_inventorysheet 
			WHERE F_Status = iStatus; 
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
			
		ELSEIF iStatus = INVALID_STATUS THEN
	   		SELECT 
				F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
	   		FROM t_inventorysheet
			WHERE F_Status != 3 
			AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE F_ShopID = iShopID END)
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) INTO iTotalRecord 
			FROM t_inventorysheet 
			WHERE F_Status != 3;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;