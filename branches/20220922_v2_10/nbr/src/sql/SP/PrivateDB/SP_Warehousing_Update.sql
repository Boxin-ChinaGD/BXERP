DROP PROCEDURE IF EXISTS `SP_Warehousing_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iWarehouseID INT,
	IN iProviderID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_warehousing WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�����޸ĳɲ����ڵ���ⵥ';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ⵥû�������Ʒ';
		ELSEIF (SELECT F_Status FROM t_warehousing WHERE F_ID = iID) <> 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := 'ֻ���޸�״̬Ϊδ��˵���ⵥ';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
		    SET sErrorMsg := '�����޸ĳɲ����ڵĹ�Ӧ��';	
		ELSEIF EXISTS(SELECT 1 FROM t_warehouse WHERE F_ID = iWarehouseID) THEN
		  	UPDATE t_warehousing SET 
			F_WarehouseID = iWarehouseID, 
			F_ProviderID = iProviderID,
			F_UpdateDatetime = now()
			WHERE F_ID = iID;
			
			SELECT F_ID, F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime
			FROM t_warehousing WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
		   SET iErrorCode := 7;
		   SET sErrorMsg := '�����޸�һ���ֿⲻ���ڵ���ⵥ';
		END IF;
		
	COMMIT;
END;