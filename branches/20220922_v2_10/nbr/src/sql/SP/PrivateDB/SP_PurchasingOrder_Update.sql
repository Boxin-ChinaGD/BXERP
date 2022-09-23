DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iProviderID INT,
	IN sRemark VARCHAR(128)
	)
BEGIN
	-- 0��δ��ˡ�1������ˡ�2��������⡢3��ȫ����⡢4����ɾ��
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Status INTO iStatus FROM T_PurchasingOrder WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '�ɹ�����������';	
		ELSEIF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '�òɹ�����û�вɹ���Ʒ';	
		ELSEIF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ӧ�̲����ڣ�������ѡ��Ӧ��';
		ELSEIF iStatus = 0 THEN 
			Update T_PurchasingOrder SET
				F_UpdateDatetime = now(),
				F_ProviderID = iProviderID,
				F_ProviderName = (SELECT F_Name FROM t_provider WHERE F_ID = iProviderID),
				F_Remark = sRemark
				WHERE F_ID = iID;
			
			SELECT 
				F_ID, 
				F_ShopID,
				F_SN,
				F_Status, 
				F_StaffID, 
				F_ProviderID, 
				F_ProviderName, 
				F_ApproverID,
				F_Remark, 
				F_CreateDatetime, 
				F_UpdateDatetime 
			FROM t_purchasingorder -- ...
			WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '�òɹ���������ˣ��������޸�';
		END IF;
	
	COMMIT;
END;