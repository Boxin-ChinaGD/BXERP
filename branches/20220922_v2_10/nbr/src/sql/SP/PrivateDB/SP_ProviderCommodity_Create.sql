DROP PROCEDURE IF EXISTS `SP_ProviderCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ProviderCommodity_Create`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
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
	
		IF EXISTS (SELECT F_ID FROM t_commodity WHERE F_ID = iCommodityID AND F_Status = 2) THEN -- ��ɾ����Ʒ������Ӧ����Ʒ������7	
			SET iErrorCode := 7;
			SET sErrorMsg := '���������ɾ����Ʒ����Ӧ����Ʒ����';
		ELSEIF EXISTS (SELECT F_ID FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 1) THEN -- �����Ʒ������Ӧ����Ʒ������7	
			SET iErrorCode := 7;
			SET sErrorMsg := '������������Ʒ����Ӧ����Ʒ����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) OR NOT EXISTS (SELECT 1 FROM t_provider WHERE F_ID = iProviderID)  THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '������Ӳ����ڵĹ�Ӧ�̵���Ӧ����Ʒ����';
		ELSEIF EXISTS (SELECT 1 FROM t_providercommodity WHERE F_CommodityID = iCommodityID AND F_ProviderID = iProviderID) THEN -- �����ͬ�����ش��󣬵��ɴ�������
			SELECT 
				F_ID, 
				F_CommodityID, 
				F_ProviderID
			FROM t_providercommodity
			WHERE F_CommodityID = iCommodityID
			AND F_ProviderID = iProviderID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';  
		ELSE
			INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
			VALUES (iCommodityID, iProviderID);
			
			SELECT 
				F_ID, 
				F_CommodityID, 
				F_ProviderID
			FROM t_providercommodity
			WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;

	COMMIT;
END;