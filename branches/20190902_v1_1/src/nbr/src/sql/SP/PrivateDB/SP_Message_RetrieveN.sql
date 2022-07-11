DROP PROCEDURE IF EXISTS `SP_Message_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Message_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	   	SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		-- 0:Î´¶Á 1£ºÒÑ¶Á
		SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime FROM t_message 
		WHERE F_IsRead = 0 ORDER BY F_ID DESC LIMIT recordIndex, iPageSize ;
		
		SELECT count(1) INTO iTotalRecord FROM t_message 
		WHERE F_IsRead = 0 LIMIT recordIndex, iPageSize;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END