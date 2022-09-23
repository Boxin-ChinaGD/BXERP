DROP PROCEDURE IF EXISTS `SP_Inventory_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_Approve`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iApproverID INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE icID INT;
	DECLARE iCommodityID INT;
	DECLARE imessageID INT DEFAULT 0;	-- ���ɵ���ϢID
	DECLARE iNOReal INT;
	DECLARE iNOSystem INT;
	DECLARE done INT DEFAULT FALSE;					-- ����������־����
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID, 
		F_NOReal AS iNOReal, 
		F_NOSystem AS iNOSystem
	FROM t_inventorycommodity WHERE F_InventorySheetID = iID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- ���ࣺ0=��¼�롢1=���ύ��2=����ˡ�3=��ɾ��
		SELECT F_Status INTO iStatus FROM t_inventorysheet WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_inventorysheet WHERE F_ID = iID) THEN 
			SET iErrorCode = 7;
			SET sErrorMsg := '�̵㵥������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iID) THEN 
			SET iErrorCode = 7;
			SET sErrorMsg := '���̵㵥û���̵���Ʒ';
		ELSEIF iStatus = 1 AND (SELECT Func_ValidateStateChange(1, 1, 2) = 1) THEN
			UPDATE t_inventorysheet SET 
			F_Status = 2, 
			F_ApproveDatetime = now(),
			F_UpdateDatetime = now(),
			F_ApproverID = iApproverID
			WHERE F_ID = iID;
			
			
			OPEN list;
			read_loop: LOOP
				FETCH list INTO iCommodityID,iNOReal,iNOSystem;
				IF done THEN
			  		LEAVE read_loop;
				END IF;
				
				-- ���ͨ��ʱ�޸�F_CommodityNameΪ��ǰ��Ʒ������
				UPDATE t_inventorycommodity SET F_CommodityName = (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID) WHERE F_InventorySheetID = iID AND F_CommodityID = iCommodityID;
					
				IF iNOReal <> iNOSystem THEN
				  	SELECT F_CompanyID INTO icID FROM t_shop WHERE F_ID = 1;
					INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_ReceiverID, F_CompanyID)
			 		VALUES (5, 0,'[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"�̵���챨��\"}]', NOW(), 1, icID);
			 	   	SET imessageID := last_insert_id();
				END IF;
					
	   		END LOOP read_loop;
		
	   		CLOSE list;
			
			
			SELECT 
				F_ID,
				F_ShopID, 
				F_SN,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID,
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark,
				F_UpdateDatetime,
			    imessageID AS messageID
			FROM t_inventorysheet
			WHERE F_ID = iID;
				
				-- �ӱ���Ϣ
				
		    SELECT 
			  	F_ID,
			  	F_InventorySheetID,
			    F_CommodityID,
			    F_CommodityName,
			    F_Specification,
			    F_Specification,
			    F_BarcodeID,
			    F_PackageUnitID,
			    F_NOReal,
			    F_NOSystem,
			    F_CreateDatetime,
			    F_UpdateDatetime
			FROM t_inventorycommodity
			WHERE F_InventorySheetID = iID;
			
		  	IF imessageID > 0 THEN
		 		SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime FROM t_message WHERE F_ID = imessageID;
			END IF;
			
	
			SET iErrorCode = 0;
			SET sErrorMsg := '';
		ELSE
			SET iErrorCode = 7; -- ��������ҵ�߼�
			SET sErrorMsg := '��������ҵ�߼�';
		END IF;	
	
	COMMIT;
END;