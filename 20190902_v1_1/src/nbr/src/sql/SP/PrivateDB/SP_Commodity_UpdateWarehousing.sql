DROP PROCEDURE IF EXISTS `SP_Commodity_UpdateWarehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_UpdateWarehousing` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iNO INT,
	IN iPrice Decimal(20,6),
	IN iStaffID INT,
	IN iShopID INT 
	)
BEGIN
	DECLARE iNum INT; 		-- 当前累计数量
--	DECLARE iCost Decimal(20,6);	-- 当前的平均进货价  
	DECLARE iCommodityMultiple INT; -- 商品倍数
	DECLARE iRefID INT; -- 参照商品ID
	DECLARE iFuncReturnCode INT; 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		
		
		SELECT 
	--		F_NOAccumulated, 
	--		F_PricePurchase, 
			F_RefCommodityMultiple, 
			F_RefCommodityID 
		INTO 
	--		iNum, 
	--		iCost, 
			iCommodityMultiple, 
			iRefID 
		FROM t_commodity WHERE F_ID = iCommodityID;
		
	--	SET @total = iNum + iNO * cm;											-- 审核成功后商品的累计总数
	--	SET @imean = (iPrice * iNO * cm + iCost * iNum)/@total; 					-- 审核成功后商品的平均进货价
		
   		UPDATE t_commodityshopinfo SET 
--			F_PricePurchase = @imean,
			F_LatestPricePurchase = iPrice, 
			-- F_NOAccumulated = @total,
			F_NO = F_NO + iNO	
   		WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
   		
   		UPDATE t_commodity SET F_UpdateDatetime = now()	WHERE F_ID = iCommodityID;
   		
   		-- 插入商品修改历史表
		select Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID) - iNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;