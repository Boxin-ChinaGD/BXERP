DROP PROCEDURE IF EXISTS `SP_VipCardCode_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCardCode_Retrieve1`(
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
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	   	SELECT F_ID, F_VipID, F_VipCardID, F_SN, F_CreateDatetime
		FROM t_vipcardcode WHERE F_ID = iID;

	COMMIT;
END;
	