DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_Approve`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE done INT DEFAULT FALSE;					-- ����������־����
	DECLARE iCommodityID INT;
	DECLARE iShopID INT;
	DECLARE returnNO INT;							-- �˻�����
	DECLARE iRefCommodityID INT;
	DECLARE iRefCommodityMultiple INT;
	DECLARE iFuncWarehousingForReturnCode INT;
	DECLARE iFuncReturnCode INT;
	DECLARE oldNO INT;								-- �˻�ǰ�������
	DECLARE iStaffID INT;
	DECLARE currentWarehousingID INT;				-- ��ֵ���ID
	DECLARE iStatus INT;
	
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID,
		F_NO AS returnNO
	FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID); -- Ҫ�α������
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
	    SET sErrorMsg := '���ݿ����';
	    ROLLBACK;
    END;
	
	START TRANSACTION;
		SELECT F_Status INTO iStatus FROM t_returncommoditysheet WHERE F_ID = iID;
		SELECT F_ShopID INTO iShopID FROM t_inventorysheet WHERE F_ID = iID;
		IF NOT EXISTS(SELECT 1 FROM t_returncommoditysheet WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���޸òɹ��˻���';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���˻���û���˻���Ʒ';
		ELSEIF Func_ValidateStateChange(5, iStatus, 1) = 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���˻�������ˣ������ظ�����';
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
			
			-- 1��������Ʒ��档2����ⵥ�������������������Ӧ���١�3����Ʒ�޸���ʷ��
			OPEN list;
			approve_loop: LOOP
				FETCH list INTO iCommodityID, returnNO;
				IF done THEN
			  		LEAVE approve_loop;
				END IF;
				SELECT iCommodityID;
				-- ���ʱ��Ʒ����F_CommodityNameӦΪ������Ʒ������
				UPDATE t_returncommoditysheetcommodity SET F_CommodityName = (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID) WHERE F_ReturnCommoditySheetID = iID AND F_CommodityID = iCommodityID;
				
				SELECT F_StaffID INTO iStaffID FROM t_returncommoditysheet WHERE F_ID = iID;
				
				-- �˻������Ӧ����, ������Ϊ����
				IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 0) THEN
					-- �˻�ǰ�������
					SELECT F_NO INTO oldNO FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					
					-- ��Ʒֱ�������еļ�ȥ�˻��Ŀ��
					UPDATE t_commodityshopinfo
					SET 
						F_NO = F_NO - returnNO
					WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					UPDATE t_commodity 
					SET 
						F_UpdateDatetime = now()
					WHERE F_ID = iCommodityID ;
					
					-- ��ⵥ����������Ӧ����
					SELECT F_CurrentWarehousingID INTO currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					SELECT Func_DeleteWarehousingForReturnCommoditySheet(iCommodityID, returnNO, currentWarehousingID, iShopID) INTO iFuncWarehousingForReturnCode;
					
					-- ������Ʒ�޸���ʷ��
					SELECT Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
				
				ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 2) THEN
					-- ��ƷID
					SELECT F_RefCommodityID INTO iRefCommodityID FROM t_commodity WHERE F_ID = iCommodityID;
					
					-- �˻�ǰ�������
					SELECT F_NO INTO oldNO FROM t_commodityshopinfo WHERE F_CommodityID = iRefCommodityID AND F_ShopID = iShopID;
					
					-- ���װ��Ʒ����
					SELECT F_RefCommodityMultiple INTO iRefCommodityMultiple FROM t_commodity WHERE F_ID = iCommodityID;
					
					-- ���װ��Ʒ�ҵ���Ʒ��ID�Ŀ���ȥ���װ��Ʒ�ı�������Ҫ�˻�������
					UPDATE t_commodityshopinfo 
					SET 
						F_NO = F_NO - returnNO*iRefCommodityMultiple
					WHERE F_CommodityID = iRefCommodityID AND F_ShopID = iShopID;
					UPDATE t_commodity 
					SET 
						F_UpdateDatetime = now()
					WHERE F_ID = iRefCommodityID;					
					
					-- ��ⵥ����������Ӧ����
					SELECT F_CurrentWarehousingID INTO currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID;
					SELECT Func_DeleteWarehousingForReturnCommoditySheet(iCommodityID, returnNO*iRefCommodityMultiple, currentWarehousingID, iShopID) INTO iFuncWarehousingForReturnCode;
					
					-- ������Ʒ�޸���ʷ��
					select Func_CreateCommodityHistory(iRefCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
				END IF;
				
		   		END LOOP approve_loop;
		   	CLOSE list;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
	   	END IF;
	
	COMMIT;
END;