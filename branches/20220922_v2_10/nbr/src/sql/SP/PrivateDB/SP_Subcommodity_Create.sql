DROP PROCEDURE IF EXISTS `SP_Subcommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Subcommodity_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iSubCommodityID INT,
	IN iSubCommodityNO INT,
	IN iPrice DECIMAL(20, 6)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- 在创建组合商品的页面中，不能出现除了单品外的其他类型商品
		
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_Status <> 2 AND F_ID = iCommodityID AND F_Type <> 1) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '只有组合商品能插入子商品';
		ELSEIF iPrice < 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '子商品价格不能为负数';
	   	ELSEIF EXISTS (SELECT 1 FROM t_subcommodity WHERE F_CommodityID = iCommodityID AND F_SubCommodityID = iSubCommodityID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能添加相同的商品到同一组合商品中';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) OR NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iSubCommodityID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '不能添加不存在的商品';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iSubCommodityID AND F_Type = 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能组合除了单品外的其他类型商品';
		ELSE
			INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price) VALUES (iCommodityID, iSubCommodityID, iSubCommodityNO, iPrice);
			
			SELECT 
				F_ID, 
				F_CommodityID, 
				F_SubCommodityID,
				F_SubCommodityNO,
				F_Price,
				F_CreateDatetime,
				F_UpdateDatetime 
			FROM t_subcommodity 
			WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;