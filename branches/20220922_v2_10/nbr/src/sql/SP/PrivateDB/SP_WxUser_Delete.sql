DROP PROCEDURE IF EXISTS `SP_WxUser_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WxUser_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN oppenId VARCHAR(30)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_wxuser WHERE F_OpenId = oppenId) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '����һ�������ڵ�oppenid,ɾ��ʧ��';
		ELSEIF (oppenId IS NULL) OR (oppenId='') THEN
		   	SET iErrorCode := 7;
		   	SET sErrorMsg := '����һ���յ�oppenid,ɾ��ʧ��';
		ELSE
		
		    DELETE FROM t_wxuser WHERE F_OpenId = oppenId;
		    SET iErrorCode := 0;
		    SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;
		