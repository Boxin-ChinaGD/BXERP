DROP PROCEDURE IF EXISTS `SP_POS_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SELECT Func_CheckPosDependency(iID, sErrorMsg) INTO iCheckDependency;
		SELECT F_Status INTO iStatus FROM t_pos WHERE F_ID = iID;
		
		IF iCheckDependency <> '' THEN
			SET iErrorCode := 7;
			SET sErrorMsg := iCheckDependency;
		ELSEIF Func_ValidateStateChange(8, iStatus, 1) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该POS机已删除，不能重复删除';
		ELSE 
			UPDATE t_pos SET F_Status = 1, F_UpdateDatetime = now() WHERE F_ID = iID;
		   	SET iErrorCode := 0;	
		   	SET sErrorMsg := '';
		 END IF;
	
	COMMIT;
END;