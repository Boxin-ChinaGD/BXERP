DROP PROCEDURE IF EXISTS `SP_ProviderDistrict_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ProviderDistrict_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_provider WHERE F_DistrictID = iID ) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ɾ�����й�Ӧ��ʹ�õ�����';
		ELSE		
			DELETE FROM t_providerdistrict WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;