DROP PROCEDURE IF EXISTS `SP_Warehousing_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Approve` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
--	IN bToApproveStartValue INT,	-- 期初值，0代表正常商品，1代表期初商品
	IN iApproverID INT
	)
BEGIN
	DECLARE iFuncReturnCode INT;
	DECLARE iCommodityID INT;
	DECLARE iShopID INT;
	DECLARE icompanyID INT; -- 所属公司
	DECLARE imessageID INT DEFAULT 0;	-- 生成的消息ID
	DECLARE priceSuggestion Decimal(20,6);
	DECLARE iNO INT;
	DECLARE iPrice Decimal(20,6);
--	DECLARE iStaffID INT;
	DECLARE status INT;
	DECLARE done INT DEFAULT FALSE;					-- 创建结束标志变量
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID, 
		F_NO AS iNO, 
		F_Price AS iPrice
	FROM t_warehousingcommodity WHERE F_WarehousingID = iID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	-- 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SET sErrorMsg := '';
		
		SELECT F_Status, F_ShopID INTO status, iShopID FROM t_warehousing WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_warehousing WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '审核的入库单id不存在';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该入库单没有入库商品';
		ELSEIF Func_ValidateStateChange(4, status, 1) <> 1 THEN	
			SET iErrorCode := 7;
			SET sErrorMsg := '审核的入库单已审核';
		ELSE
			UPDATE t_warehousing SET F_Status = 1, F_UpdateDatetime = now(), F_ApproverID = iApproverID WHERE F_ID = iID;
			
		 	OPEN list;
			read_loop: LOOP
				FETCH list INTO iCommodityID,iNO,iPrice;
				IF done THEN
			  		LEAVE read_loop;
				END IF;
				
				-- 审核通过时修改F_CommodityName为当前商品的名称
				UPDATE t_warehousingcommodity SET F_CommodityName = (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID) WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iID;	
					
				CALL SP_Commodity_UpdateWarehousing(iErrorCode, sErrorMsg, iCommodityID, iNO, iPrice, iApproverID, iShopID); -- 审核人为商品历史修改人
				
		   		SET priceSuggestion = (SELECT F_PriceSuggestion FROM t_purchasingordercommodity 
	   	   					   	   		WHERE F_PurchasingOrderID = (SELECT F_PurchasingOrderID FROM t_warehousing WHERE F_ID = iID) 
	   	  					     	    AND F_CommodityID = iCommodityID);
	   	  					      
	   	   		IF priceSuggestion IS NOT NULL AND priceSuggestion <> iPrice THEN
	   	   
				   	SELECT F_CompanyID INTO icompanyID FROM t_shop WHERE F_ID = 1;
			   			INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_ReceiverID, F_CompanyID)
						VALUES (3, 0, '[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"入库价格与采购订单上的价格不符\"}]', now(), 1, icompanyID);
						SET imessageID := last_insert_id();
	   	   		END IF;	
				
	   		END LOOP read_loop;
	   		
	   		CLOSE list;
	   		SET iErrorCode := 0;
		END IF; 
		
		SELECT 
			F_ID, 
			F_ShopID, 
			F_SN,
			F_Status,
			F_ProviderID,
			F_WarehouseID, 
			F_ApproverID,
			F_StaffID, 
			F_CreateDatetime, 
			F_PurchasingOrderID,
			F_UpdateDatetime,
			(SELECT F_ID FROM t_message WHERE F_ID = imessageID) AS messageID,
			(SELECT F_SN FROM t_purchasingorder WHERE F_ID = ws.F_PurchasingOrderID) AS purchasingOrderSN -- 入库单关联的采购订单的单号
		FROM t_warehousing ws WHERE F_ID = iID;
		 
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
		FROM t_warehousingcommodity WHERE F_WarehousingID = iID;
		
		IF imessageID > 0 THEN
			SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime FROM t_message WHERE F_ID = imessageID;
		END IF;
		
	 COMMIT;
END;