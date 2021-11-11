DROP PROCEDURE IF EXISTS `SP_Warehouse_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_Delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE status INT;
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
	   	SET sErrorMsg := '数据库错误';
	   	ROLLBACK;
   	END;
   	
   	START TRANSACTION;
   		SELECT Func_CheckWarehouseDependency(iID, sErrorMsg) INTO iCheckDependency;
   		
   		IF iCheckDependency <> '' THEN
			SET iErrorCode := 7;
			SET sErrorMsg := iCheckDependency;
				
		ELSE 
			SELECT F_Status INTO status FROM t_warehouse WHERE F_ID = iID;
			IF Func_ValidateStateChange(6, status, 1) = 1 THEN
		   		UPDATE t_warehouse SET F_Status = 1 WHERE F_ID = iID;
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET iErrorCode := 7;
				SET sErrorMsg := '仓库已删除,不能重复删除，不能删除';
			END IF;
		END IF;
	COMMIT;
END