DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	-- ... 0��δ��ˡ�1������ˡ�2��������⡢3��ȫ����⡢4����ɾ��	
	DECLARE status INT;
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
  		SELECT Func_CheckPurchasingOrderDependency(iID, sErrorMsg) INTO iCheckDependency;
   		
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '����ɾ�������ڵĲɹ���';
		ELSEIF iCheckDependency <> '' THEN
			SET iErrorCode := 7;
			SET sErrorMsg := iCheckDependency; 
		ELSE 
			SELECT F_Status INTO status FROM t_purchasingorder WHERE F_ID = iID;
			IF Func_ValidateStateChange(2, status, 4) = 1 THEN
				UPDATE t_purchasingorder SET F_Status = 4, F_ProviderID = NULL WHERE F_ID = iID;
				-- �ɹ�������Ϊ��ɾ��״̬����ô���òɹ������ɹ�����Ʒ���Ա�ɾ����ɾ����Ʒ��Ҫɾ�����е������룬����������ɹ�������Ʒ�����Լ��������������Ҫɾ���ɹ�������Ʒ
			    DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID;
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET iErrorCode := 7;
				SET sErrorMsg := '�òɹ���������ˣ�������ɾ��';
			END IF;
		END IF;
	
	COMMIT;
END;