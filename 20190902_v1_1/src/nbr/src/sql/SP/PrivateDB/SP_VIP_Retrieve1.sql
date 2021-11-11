DROP PROCEDURE IF EXISTS `SP_VIP_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_Retrieve1` (
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
	
		SELECT 
			F_ID, 
			F_SN, 
			F_CardID,
			F_Mobile, 
			F_LocalPosSN, 
			F_Sex, 
			F_Logo, 
			F_ICID, 
			F_Name, 
			F_Email, 
			F_ConsumeTimes, 
			F_ConsumeAmount, 
			F_District, 
			F_Category, 
			F_Birthday, 
			F_Bonus, 
			F_LastConsumeDatetime
		FROM t_vip WHERE F_ID = iID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;