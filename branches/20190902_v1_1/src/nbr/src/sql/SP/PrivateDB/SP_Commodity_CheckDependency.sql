DROP PROCEDURE IF EXISTS `SP_Commodity_CheckDependency`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_CheckDependency`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN 
	DECLARE NO INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
--		SELECT F_NO INTO NO FROM t_commodity WHERE F_ID = iID;
	
		IF EXISTS(SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_NO > 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '商品数量不为0不能删除';
	  	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID = iID) THEN-- ... 采购计划表
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '商品在RetailtradeCommodity有依赖';
	  	ELSEIF  EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '商品在PurchasingOrderCommodity有依赖';
	  	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '商品在InventoryCommodity有依赖';
	  	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7; -- ...这里最好返回7
	  		SET sErrorMsg := '商品在WarehousingCommodity有依赖';
	  	ELSE
	  		SET iErrorCode := 0;
	  		SET sErrorMsg := '';
	  	END IF;		
	  	
	  	-- ... 还有一种情况，就是库存>0或库存<0时，依赖仍然存在。返回7。
 	COMMIT;
END;