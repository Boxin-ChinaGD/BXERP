DROP PROCEDURE IF EXISTS `SP_Message_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Message_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCategoryID INT,
	IN iIsRead INT,
	IN sParameter VARCHAR(255),
	IN iSenderID INT,
	IN iReceiverID INT,
	IN iCompanyID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS (SELECT 1 FROM t_messagecategory WHERE F_ID = iCategoryID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不存在该消息类别';
		ELSEIF NOT EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_ID = iCompanyID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不存在该公司';
		ELSE 
			-- 删除旧的数据项
			DELETE FROM t_messageitem WHERE F_MessageCategoryID = iCategoryID;
			-- 
		   	INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_CompanyID)
			VALUES (iCategoryID, iIsRead, sParameter, now(), iSenderID, iReceiverID, iCompanyID);
			
			SELECT F_ID, F_CategoryID, F_CompanyID, F_IsRead, F_Status, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_UpdateDatetime FROM t_message WHERE F_ID = LAST_INSERT_ID();
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END