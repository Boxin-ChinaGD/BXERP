DROP PROCEDURE IF EXISTS `SP_WarehousingCommodity_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WarehousingCommodity_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iWarehousingID INT,
	IN iCommodityID INT,
	IN iPrice Decimal(20,6),
	IN iNO INT,
	IN iAmount Decimal(20,6)
 	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS( SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID ) THEN
			SET iErrorCode := 2;
		 	SET sErrorMsg := '不能更新一个CommodityID不存在的WarehousingCommodity';
		ELSEIF NOT EXISTS( SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iWarehousingID ) THEN
			SET iErrorCode := 2;
		 	SET sErrorMsg := '不能更新一个WarehousingID不存在的WarehousingCommodity';
		 ELSE
		 	UPDATE t_warehousingcommodity
			SET F_NO = iNO,
		   		F_Price = iPrice,
		   		F_Amount = iAmount,
		   		F_UpdateDatetime = now(),
		  		F_NOSalable = iNO
			WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iWarehousingID;
			
			SELECT 
				F_ID, 
				F_WarehousingID, 
				F_CommodityID, 
				F_NO, 
				F_PackageUnitID, 
				F_CommodityName,
				F_BarcodeID,
				F_Price, 
				F_Amount, 
				F_ProductionDatetime, 
				F_ShelfLife, 
				F_ExpireDatetime,
				F_CreateDatetime,
				F_UpdateDatetime,
				F_NOSalable
			FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iWarehousingID;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		 	
		 END IF;
	
	COMMIT;
END;