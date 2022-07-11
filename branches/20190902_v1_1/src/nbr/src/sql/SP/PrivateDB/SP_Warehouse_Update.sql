DROP PROCEDURE IF EXISTS `SP_Warehouse_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
--	IN iBX_CustomerID INT,
	IN sName VARCHAR(32),
	IN sAddress VARCHAR(32),
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
	
		IF  EXISTS (SELECT 1 FROM t_warehouse WHERE F_Name = sName AND F_ID <> iID) THEN	
			SET iErrorCode := 1;
			SET sErrorMsg := '采购单名称不能修改为已有的名字';
		ELSEIF EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID AND F_Status <> 1) THEN 
			UPDATE t_warehouse SET 
				F_Name = sName,
				F_Address = sAddress,
				F_StaffID = iStaffID,
				F_Phone = sPhone,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
	
			SELECT 
				F_ID, 
				F_Name, 
				F_Address, 
				F_Status, 
				F_StaffID, 
				F_Phone, 
				F_CreateDatetime, 
				F_UpdateDatetime 
			FROM t_warehouse WHERE F_ID = iID;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
		   	SET iErrorCode := 2;
		   	SET sErrorMsg := '不能修改的StaffID为不存在的盘点单';
		END IF;
	
	COMMIT;
END