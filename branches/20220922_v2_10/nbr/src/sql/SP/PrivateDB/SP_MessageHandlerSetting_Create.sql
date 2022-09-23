DROP PROCEDURE IF EXISTS `SP_MessageHandlerSetting_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MessageHandlerSetting_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCategoryID INT,
	IN sTemplate VARCHAR(128),
	IN sLink VARCHAR(255)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS (SELECT 1 FROM T_MessageCategory WHERE F_ID = iCategoryID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�����ڸ���Ϣ���';
		ELSE 
			INSERT INTO t_messagehandlersetting (F_CategoryID, F_Template, F_Link) VALUES (iCategoryID, sTemplate, sLink);
			
			SELECT 
				F_ID, 
				F_CategoryID, 
				F_Template, 
				F_Link,
				F_CreateDatetime,
				F_UpdateDatetime 
			FROM t_messagehandlersetting WHERE F_ID = LAST_INSERT_ID();
		
			SET iErrorCode := 0;
		END IF;
		SET sErrorMsg := '';
		
	COMMIT;
END