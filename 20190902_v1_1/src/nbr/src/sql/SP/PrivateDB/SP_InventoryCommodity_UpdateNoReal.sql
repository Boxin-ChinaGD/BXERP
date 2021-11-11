DROP PROCEDURE IF EXISTS `SP_InventoryCommodity_UpdateNoReal`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_InventoryCommodity_UpdateNoReal`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iNoReal INT,
	IN iNOSystem INT
	)
BEGIN
	-- 该sp只处理盘点单商品的实盘数量
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_inventorycommodity SET F_NOReal = iNoReal, F_UpdateDatetime = now(), F_NOSystem = iNOSystem WHERE F_ID = iID;
	  
		SELECT 
			F_ID, 
			F_InventorySheetID, 
			F_CommodityID, 
			F_CommodityName, 
			F_Specification, 
			F_BarcodeID, 
			F_PackageUnitID, 
			F_NOReal, 
			F_NOSystem, 
			F_CreateDatetime, 
			F_UpdateDatetime
		FROM t_inventorycommodity WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;