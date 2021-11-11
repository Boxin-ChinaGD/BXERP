DROP PROCEDURE IF EXISTS `SP_Company_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_Delete`(
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
	
		UPDATE nbr_bx.t_company
	    SET 
		    F_Status = 1
		WHERE F_ID = iID;
	 	
	    SET iErrorCode := 0;
	    SET sErrorMsg := '';
	    
	COMMIT;
END;