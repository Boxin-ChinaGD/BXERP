DROP PROCEDURE IF EXISTS `SP_WarehousingCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WarehousingCommodity_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iWarehousingID INT,
	IN iCommodityID INT,
	IN iNO INT,
	IN iBarcodeID INT,
	IN iPrice Decimal(20,6),
	IN iAmount Decimal(20,6),
	IN iShelfLife INT
)
BEGIN
	DECLARE iType INT;
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Type, F_Status INTO iType, iStatus FROM t_commodity WHERE F_ID = iCommodityID;	
		-- 入库商品要满足的条件：1、商品类型必须是单品或多包装商品。2、商品状态不能是被删除的商品。
		IF EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iWarehousingID AND F_CommodityID = iCommodityID) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := '一个入库单下不能添加重复商品';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN 
		   	SET iErrorCode = 2;
			SET sErrorMsg := '入库单商品的条形码不存在';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_warehousing WHERE F_ID = iWarehousingID) THEN
			SET iErrorCode = 2;
			SET sErrorMsg := '该入库单不存在';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode = 2;
			SET sErrorMsg := '不能添加一个不存在的商品';
		ELSEIF iStatus = 2 THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '不能添加已删除的商品进入库单';
		ELSEIF iType <> 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能添加非单品的商品进入库单';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_CommodityID = iCommodityID AND F_ID = iBarcodeID) THEN
		   		SET iErrorCode := 7;
		   		SET sErrorMsg := '条形码ID与商品实际条形码ID不对应';
		ELSE   
		   	INSERT INTO t_warehousingcommodity (
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
				F_NOSalable
			) VALUES (
				iWarehousingID, 
				iCommodityID, 
				iNO, 
				(SELECT F_PackageUnitID FROM t_commodity WHERE F_ID = iCommodityID),
				(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID),
				iBarcodeID, 
				iPrice, 
				iAmount, 
				now(),
				iShelfLife, 
				now(),
				iNO
			);
	
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
		FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
		
		   SET iErrorCode := 0;
		   SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;