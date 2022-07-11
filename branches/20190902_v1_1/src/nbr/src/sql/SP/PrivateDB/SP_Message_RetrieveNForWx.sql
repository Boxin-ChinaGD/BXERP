DROP PROCEDURE IF EXISTS `SP_Message_RetrieveNForWx`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Message_RetrieveNForWx` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN istatus INT,
	IN iCompanyID INT
	)
BEGIN
	DECLARE INVALID_ID INT;
	DECLARE INVALID_STATUS INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';
		SELECT F_Value INTO INVALID_STATUS FROM t_nbrconstant WHERE F_Key = 'INVALID_STATUS';
		
		SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime 
		FROM t_message 
		WHERE 1 = 1 
		AND (CASE iCompanyID WHEN INVALID_ID THEN 1=1 ELSE 	F_CompanyID = iCompanyID END) 
		AND (CASE istatus WHEN INVALID_STATUS THEN 1=1 ELSE F_Status = istatus END);
		
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END
