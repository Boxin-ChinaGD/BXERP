DROP PROCEDURE IF EXISTS `SP_StateMachine_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_StateMachine_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
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
			td.F_DomainID AS F_DomainID, 
			td.F_Name AS F_DomainName, 
			tn.F_Status AS F_Status, 
			tn.F_Name AS F_StatusName, 
			tn.F_Description AS F_StatusDescription,
			tf.F_CurrentNodeID AS F_StatusFrom, 
			tf.F_NextNodeID AS F_StatusTo, 
			tf.F_Description AS F_ForwardDescription
		FROM t_smdomain td, t_smnode tn, t_smforward tf 
		WHERE td.F_DomainID = tn.F_DomainID = tf.F_DomainID
		ORDER BY F_ID DESC;
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;