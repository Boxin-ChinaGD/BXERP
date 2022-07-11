DROP PROCEDURE IF EXISTS `SP_Message_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Message_Retrieve1` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime FROM t_message WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END