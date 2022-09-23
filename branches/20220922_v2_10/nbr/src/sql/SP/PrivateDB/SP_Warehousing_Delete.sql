DROP PROCEDURE IF EXISTS `SP_Warehousing_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Delete`(
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
	
		IF NOT EXISTS(SELECT 1 FROM t_warehousing WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '��ⵥ������,ɾ��ʧ��';
	    ELSEIF EXISTS(SELECT 1 FROM t_warehousing WHERE F_ID = iID AND F_Status = 1) THEN
	    	SET iErrorCode := 7;
	    	SET sErrorMsg := '��ⵥ����ˣ��޷�ɾ��';
		ELSE
			DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = iID;
			DELETE FROM t_warehousing WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END