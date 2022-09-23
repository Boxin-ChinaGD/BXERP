DROP PROCEDURE IF EXISTS `SP_Warehousing_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Approve` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
--	IN bToApproveStartValue INT,	-- �ڳ�ֵ��0����������Ʒ��1�����ڳ���Ʒ
	IN iApproverID INT
	)
BEGIN
	DECLARE iFuncReturnCode INT;
	DECLARE iCommodityID INT;
	DECLARE iShopID INT;
	DECLARE icompanyID INT; -- ������˾
	DECLARE imessageID INT DEFAULT 0;	-- ���ɵ���ϢID
	DECLARE priceSuggestion Decimal(20,6);
	DECLARE iNO INT;
	DECLARE iPrice Decimal(20,6);
--	DECLARE iStaffID INT;
	DECLARE status INT;
	DECLARE done INT DEFAULT FALSE;					-- ����������־����
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID, 
		F_NO AS iNO, 
		F_Price AS iPrice
	FROM t_warehousingcommodity WHERE F_WarehousingID = iID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	-- 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SET sErrorMsg := '';
		
		SELECT F_Status, F_ShopID INTO status, iShopID FROM t_warehousing WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_warehousing WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��˵���ⵥid������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ⵥû�������Ʒ';
		ELSEIF Func_ValidateStateChange(4, status, 1) <> 1 THEN	
			SET iErrorCode := 7;
			SET sErrorMsg := '��˵���ⵥ�����';
		ELSE
			UPDATE t_warehousing SET F_Status = 1, F_UpdateDatetime = now(), F_ApproverID = iApproverID WHERE F_ID = iID;
			
		 	OPEN list;
			read_loop: LOOP
				FETCH list INTO iCommodityID,iNO,iPrice;
				IF done THEN
			  		LEAVE read_loop;
				END IF;
				
				-- ���ͨ��ʱ�޸�F_CommodityNameΪ��ǰ��Ʒ������
				UPDATE t_warehousingcommodity SET F_CommodityName = (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID) WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iID;	
					
				CALL SP_Commodity_UpdateWarehousing(iErrorCode, sErrorMsg, iCommodityID, iNO, iPrice, iApproverID, iShopID); -- �����Ϊ��Ʒ��ʷ�޸���
				
		   		SET priceSuggestion = (SELECT F_PriceSuggestion FROM t_purchasingordercommodity 
	   	   					   	   		WHERE F_PurchasingOrderID = (SELECT F_PurchasingOrderID FROM t_warehousing WHERE F_ID = iID) 
	   	  					     	    AND F_CommodityID = iCommodityID);
	   	  					      
	   	   		IF priceSuggestion IS NOT NULL AND priceSuggestion <> iPrice THEN
	   	   
				   	SELECT F_CompanyID INTO icompanyID FROM t_shop WHERE F_ID = 1;
			   			INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_ReceiverID, F_CompanyID)
						VALUES (3, 0, '[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"���۸���ɹ������ϵļ۸񲻷�\"}]', now(), 1, icompanyID);
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
			(SELECT F_SN FROM t_purchasingorder WHERE F_ID = ws.F_PurchasingOrderID) AS purchasingOrderSN -- ��ⵥ�����Ĳɹ������ĵ���
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