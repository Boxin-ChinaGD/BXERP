DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_Approve`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE done INT DEFAULT FALSE;					-- 创建结束标志变量
	DECLARE iCommodityID INT;
	DECLARE iShopID INT;
	DECLARE returnNO INT;							-- 退货数量
	DECLARE iRefCommodityID INT;
	DECLARE iRefCommodityMultiple INT;
	DECLARE iFuncWarehousingForReturnCode INT;
	DECLARE iFuncReturnCode INT;
	DECLARE oldNO INT;								-- 退货前库存数量
	DECLARE iStaffID INT;
	DECLARE currentWarehousingID INT;				-- 当值入库ID
	DECLARE iStatus INT;
	
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID,
		F_NO AS returnNO
	FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID); -- 要游标的数据
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
	    SET sErrorMsg := '数据库错误';
	    ROLLBACK;
    END;
	
	START TRANSACTION;
		SELECT F_Status INTO iStatus FROM t_returncommoditysheet WHERE F_ID = iID;
		SELECT F_ShopID INTO iShopID FROM t_inventorysheet WHERE F_ID = iID;
		IF NOT EXISTS(SELECT 1 FROM t_returncommoditysheet WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '查无该采购退货单';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该退货单没有退货商品';
		ELSEIF Func_ValidateStateChange(5, iStatus, 1) = 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该退货单已审核，请勿重复操作';
		ELSE
			UPDATE t_returncommoditysheet 
			SET 
				F_Status = 1,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
			
			SELECT F_ID, F_SN, F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime, F_ShopID, 
				(SELECT group_concat(F_CommodityID) FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) AS commodityIDs
			FROM t_returncommoditysheet
			WHERE F_ID = iID;
			
			-- 1、处理商品库存。2、入库单可售数量和入库数量相应减少。3、商品修改历史表
			OPEN list;
			approve_loop: LOOP
				FETCH list INTO iCommodityID, returnNO;
				IF done THEN
			  		LEAVE approve_loop;
				END IF;
				SELECT iCommodityID;
				-- 审核时商品名称F_CommodityName应为现在商品的名称
				UPDATE t_returncommoditysheetcommodity SET F_CommodityName = (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID) WHERE F_ReturnCommoditySheetID = iID AND F_CommodityID = iCommodityID;
				
				SELECT F_StaffID INTO iStaffID FROM t_returncommoditysheet WHERE F_ID = iID;
				
				-- 退货库存相应减少, 库存可以为负数
				IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 0) THEN
					-- 退货前库存数量
					SELECT F_NO INTO oldNO FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					
					-- 单品直接以现有的减去退货的库存
					UPDATE t_commodityshopinfo
					SET 
						F_NO = F_NO - returnNO
					WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					UPDATE t_commodity 
					SET 
						F_UpdateDatetime = now()
					WHERE F_ID = iCommodityID ;
					
					-- 入库单可售数量相应减少
					SELECT F_CurrentWarehousingID INTO currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					SELECT Func_DeleteWarehousingForReturnCommoditySheet(iCommodityID, returnNO, currentWarehousingID, iShopID) INTO iFuncWarehousingForReturnCode;
					
					-- 插入商品修改历史表
					SELECT Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
				
				ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 2) THEN
					-- 单品ID
					SELECT F_RefCommodityID INTO iRefCommodityID FROM t_commodity WHERE F_ID = iCommodityID;
					
					-- 退货前库存数量
					SELECT F_NO INTO oldNO FROM t_commodityshopinfo WHERE F_CommodityID = iRefCommodityID AND F_ShopID = iShopID;
					
					-- 多包装商品倍数
					SELECT F_RefCommodityMultiple INTO iRefCommodityMultiple FROM t_commodity WHERE F_ID = iCommodityID;
					
					-- 多包装商品找到单品的ID的库存减去多包装商品的倍数乘以要退货的数量
					UPDATE t_commodityshopinfo 
					SET 
						F_NO = F_NO - returnNO*iRefCommodityMultiple
					WHERE F_CommodityID = iRefCommodityID AND F_ShopID = iShopID;
					UPDATE t_commodity 
					SET 
						F_UpdateDatetime = now()
					WHERE F_ID = iRefCommodityID;					
					
					-- 入库单可售数量相应减少
					SELECT F_CurrentWarehousingID INTO currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID;
					SELECT Func_DeleteWarehousingForReturnCommoditySheet(iCommodityID, returnNO*iRefCommodityMultiple, currentWarehousingID, iShopID) INTO iFuncWarehousingForReturnCode;
					
					-- 插入商品修改历史表
					select Func_CreateCommodityHistory(iRefCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
				END IF;
				
		   		END LOOP approve_loop;
		   	CLOSE list;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
	   	END IF;
	
	COMMIT;
END;