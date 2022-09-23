DROP PROCEDURE IF EXISTS `SP_Warehousing_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN iProviderID INT,
	IN iWarehouseID INT,
	IN iStaffID INT,
	IN iPurchasingOrderID INT
 	)
BEGIN
	DECLARE sSN VARCHAR(20);
    DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	 	
	  	SELECT F_Status INTO iStatus FROM t_purchasingorder WHERE F_ID = iPurchasingOrderID;
	  	
	  	IF NOT EXISTS(SELECT 1 FROM t_warehouse WHERE F_ID = iWarehouseID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '����ʹ�ò����ڵĲֿ���д���';
	  	ELSEIF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '����ʹ�ò����ڵ�ҵ��Ա���д���';
	  	ELSEIF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
	  		SET sErrorMsg := '�ù�Ӧ�̲����ڣ�������ѡ��Ӧ��';
	  	ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
	  		SET sErrorMsg := '���ŵ겻���ڣ�������ѡ���ŵ�';
		ELSEIF iPurchasingOrderID > 0 THEN
			-- �ڲɹ�����ȫ������״̬�£�������������֮
			IF iStatus > 0 AND iStatus < 3 THEN
				SELECT F_SN INTO sSN FROM T_Warehousing ORDER BY F_ID DESC LIMIT 1;
				SELECT Func_GenerateSN('RK', sSN) INTO sSN;
				
				INSERT INTO T_Warehousing (F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
				VALUES (iShopID, sSN, 0, iProviderID, iWarehouseID, iStaffID, now(), iPurchasingOrderID);
				
				SELECT F_ID, F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime
				FROM t_warehousing WHERE F_ID = last_insert_id();
					
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSEIF iStatus = 0 THEN
				SET iErrorCode := 7;
				SET sErrorMsg := '�ɹ�����״̬Ϊδ��˵�';
			ELSE 
				SET iErrorCode := 7;
				SET sErrorMsg := '�ɹ�������ȫ�����';
	   		END IF;
	   	ELSE
	   		SELECT F_SN INTO sSN FROM T_Warehousing ORDER BY F_ID DESC LIMIT 1;
			SELECT Func_GenerateSN('RK', sSN) INTO sSN;
			
	   		INSERT INTO T_Warehousing (F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
	   		VALUES (iShopID, sSN, 0, iProviderID, iWarehouseID, iStaffID, now());
	   		
	   		SELECT F_ID, F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime
			FROM t_warehousing WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;