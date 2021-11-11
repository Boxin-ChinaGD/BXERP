DROP PROCEDURE IF EXISTS `SP_Warehouse_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(32),
	IN sAddress VARCHAR(32),
	IN iStatus INT,
	IN iStaffID INT,
	IN sPhone VARCHAR(32)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF  EXISTS (SELECT 1 FROM t_warehouse WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能添加已有同样名字的仓库';
		ELSEIF EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID AND F_Status <> 1) THEN 
			INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
			VALUES (sName, sAddress, iStatus, iStaffID, sPhone);
			
			SELECT 
				F_ID, 
				F_Name, 
				F_Address, 
				F_Status, 
				F_StaffID, 
				F_Phone, 
				F_CreateDatetime,
				F_UpdateDatetime 
			FROM t_warehouse WHERE F_ID = last_insert_id();
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
			SET iErrorCode := 2;
			SET sErrorMsg := '不能使用不存在的联系人进行创建';
		END IF;
		
	COMMIT;
END;