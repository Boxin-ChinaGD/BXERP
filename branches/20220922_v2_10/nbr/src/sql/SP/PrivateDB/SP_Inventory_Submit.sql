DROP PROCEDURE IF EXISTS `SP_Inventory_Submit`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_Submit`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE iShopID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode = 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Status, F_ShopID INTO iStatus, iShopID FROM t_inventorysheet WHERE F_ID = iID;
		
		-- ���ࣺ0=��¼�롢1=���ύ��2=����ˡ�3=��ɾ��
		
		IF NOT EXISTS(SELECT 1 FROM t_inventorysheet WHERE F_ID = iID) THEN 
			SET iErrorCode = 7;
			SET sErrorMsg := '�̵㵥������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iID) THEN 
			SET iErrorCode = 7;
			SET sErrorMsg := '���̵㵥û���̵���Ʒ';
		ELSEIF iStatus = 0 AND (SELECT Func_ValidateStateChange(1, 0, 1) = 1) THEN
		
			UPDATE t_inventorysheet SET F_Status = 1, F_UpdateDatetime = now() WHERE F_ID = iID;   
				
			UPDATE t_inventorycommodity inventComm SET 
			F_NOSystem = (SELECT F_NO FROM t_commodityshopinfo commShopInfo WHERE commShopInfo.F_CommodityID = inventComm.F_CommodityID AND commShopInfo.F_ShopID = iShopID),
			F_UpdateDatetime = now()
			WHERE F_InventorySheetID = iID;    
			
			SELECT F_ID, F_SN,F_ShopID, F_WarehouseID, F_Scope, F_Status, F_StaffID, F_CreateDatetime, F_ApproveDatetime, F_Remark,F_UpdateDatetime FROM t_inventorysheet 
			WHERE F_ID = iID;
	
			SET iErrorCode = 0;
			SET sErrorMsg := '';
		ELSE
			SET iErrorCode = 7;
			SET sErrorMsg := '�̵㵥״̬��Ϊ0�����ܽ����ύ';
		END IF;	
		
	COMMIT;
END;