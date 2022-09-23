-- ֻ�ڲ�����ʹ��
DROP PROCEDURE IF EXISTS `SP_VIP_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_Delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN	
	DECLARE iStatus INT;
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		DELETE FROM T_BonusConsumeHistory WHERE F_VipID = iID;
		DELETE FROM t_vip WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';

	COMMIT;
END;