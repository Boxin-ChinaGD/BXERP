DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_UpdateStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_UpdateStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStatus INT,
	IN iID INT
)
BEGIN
	DECLARE iCurrentStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	   	SELECT F_Status INTO iCurrentStatus FROM T_PurchasingOrder WHERE F_ID = iID;
	
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '�ɹ�����������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '�òɹ�����û�вɹ���Ʒ';
	   	ELSEIF Func_ValidateStateChange(2, iCurrentStatus, iStatus) = 1 THEN 
			UPDATE t_purchasingorder SET F_Status = iStatus,F_UpdateDatetime = now() WHERE F_ID = iID;
				
			SELECT 
				F_ID, 
				F_ShopID,
				F_SN,
			  	F_Status, 
			  	F_StaffID, 
			   	F_Remark,
			   	F_ProviderID,
				F_ProviderName,
				F_ApproverID,
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_EndDatetime, 
				F_UpdateDatetime
			FROM t_purchasingorder WHERE F_ID = iID;
				
			SET iErrorCode := 0;
			SET sErrorMsg := '';
	   	ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '�����޸�ȫ�����״̬�Ķ���Ϊ�������';
		END IF;
	COMMIT;
END;