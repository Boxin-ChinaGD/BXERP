DROP PROCEDURE IF EXISTS `SP_PackageUnit_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PackageUnit_Delete`(
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
	
		IF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�ð�װ��λ�ѱ��ɹ�������Ʒʹ�ã�����ɾ��';
		ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�ð�װ��λ�ѱ���ⵥ��Ʒʹ�ã�����ɾ��';
		ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�ð�װ��λ�ѱ��̵㵥��Ʒʹ�ã�����ɾ��';
		ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_PackageUnitID = iID ) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�ð�װ��λ�ѱ���Ʒʹ�ã�����ɾ��';
		ELSE
			DELETE FROM t_packageunit WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
			
		END IF;
	
	COMMIT;
END;