DROP PROCEDURE IF EXISTS `SP_Warehousing_Retrieve1OrderID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Retrieve1OrderID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPurchasingOrderID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID, 
			F_ShopID,
			F_SN,
			F_Status,
			F_ProviderID,
			F_WarehouseID, 
			F_StaffID, 
			F_CreateDatetime, 
			F_PurchasingOrderID
		FROM t_warehousing
		WHERE 1 = 1
		-- 入库单可以不引用采购订单。
		AND (CASE ipurchasingOrderID WHEN -1 THEN 1=1 ELSE F_purchasingOrderID = iPurchasingOrderID END)
		GROUP BY F_PurchasingOrderID;
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;