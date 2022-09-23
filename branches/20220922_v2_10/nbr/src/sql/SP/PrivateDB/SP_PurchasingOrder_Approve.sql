DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Approve`(
	OUT iErrorCode INT, 
	OUT sErrorMsg VARCHAR(64), 
	IN iID INT,
	IN iApproverID INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE iProviderID INT;
	DECLARE iCommodityID INT;
	DECLARE pocID INT;	
	DECLARE done INT DEFAULT FALSE;
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID, 
		F_ID AS pocID
	FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- 0��δ��ˡ�1������ˡ�2��������⡢3��ȫ����⡢4����ɾ��
		SELECT F_Status, F_ProviderID INTO iStatus, iProviderID FROM T_PurchasingOrder WHERE F_ID = iID;
	
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;	
			SET sErrorMsg := '�ɹ�����������';	
		ELSEIF NOT EXISTS(SELECT F_Name FROM t_staff WHERE F_ID = iApproverID AND F_Status = 0) THEN	-- ���Ա��ID���Ƿ񲻴��ڻ��Ƿ�Ϊ��ְԱ��
			SET iErrorCode := 7;	
			SET sErrorMsg := '��ǰ�ʻ����������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN
			SET iErrorCode := 7;	
			SET sErrorMsg := '�òɹ�����û�вɹ���Ʒ';
		ELSE
			IF Func_ValidateStateChange(2, iStatus, 1) = 1 THEN
				Update T_PurchasingOrder SET 
					F_Status = 1,
					F_ProviderName = (SELECT F_Name FROM t_provider WHERE F_ID = iProviderID),
					F_ApproveDatetime = now(),
					F_UpdateDatetime = now(),
					F_ApproverID = iApproverID
				WHERE F_ID = iID;
					
				SELECT F_ID, F_ShopID, F_SN, F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime
				FROM T_PurchasingOrder
				WHERE F_ID = iID;
				
				OPEN list;
				read_loop: LOOP
					FETCH list INTO iCommodityID,pocID;
					IF done THEN
				  		LEAVE read_loop;
					END IF;
					
					UPDATE t_purchasingordercommodity SET
						F_CommodityName =  (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID)
					WHERE F_ID = pocID;
						
		   		END LOOP read_loop;
			
		   		CLOSE list;
						
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET iErrorCode := 7; -- ��������ҵ�߼�
				SET sErrorMsg := '�òɹ�������ˣ������ظ�����';
			END IF;
		END IF;
		
	COMMIT;
END;