DROP PROCEDURE IF EXISTS `SP_MessageHandlerSetting_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MessageHandlerSetting_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iCategoryID INT,
	IN sTemplate VARCHAR(128),
	IN sLink VARCHAR(255)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_messagehandlersetting SET 
		F_CategoryID = iCategoryID,
		F_Template = sTemplate,
		F_Link = sLink,
		F_UpdateDatetime = now()
		WHERE F_ID = iID;
		
		SELECT F_ID, F_CategoryID, F_Template, F_Link,F_CreateDatetime,F_UpdateDatetime FROM t_messagehandlersetting WHERE F_ID = iID;
	
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END