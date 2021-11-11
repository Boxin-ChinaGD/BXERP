DROP PROCEDURE IF EXISTS `SP_VipSource_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipSource_Retrieve1`(
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
	
	   	SELECT F_ID, F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3
		FROM t_vipsource WHERE F_ID = iID;

	COMMIT;
END;
	