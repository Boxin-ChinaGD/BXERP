DROP PROCEDURE IF EXISTS `SP_Commodity_RetrieveInventory`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_RetrieveInventory`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iShopID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM T_Commodity WHERE F_ID = iID AND F_Status = 2) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '已经删除的商品';
		ELSE
--			SELECT F_NO FROM T_Commodity WHERE F_ID = iID AND (F_Status = 0 OR F_Status = 1);
			SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_ShopID = iShopID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;