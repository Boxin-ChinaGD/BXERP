DROP PROCEDURE IF EXISTS `SP_BonusRule_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusRule_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN bForceDelete INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
   		
   		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		IF bForceDelete = 1 THEN
			DELETE FROM t_bonusrule WHERE F_ID = iID;
		ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_Bonus > 1) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '�û��ֹ�������ʹ�ã�����ɾ��';
		ELSE 
			DELETE FROM t_bonusrule WHERE F_ID = iID;
		END IF;
	
	COMMIT;
END;