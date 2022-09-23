
DROP PROCEDURE IF EXISTS `SP_PackageUnit_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PackageUnit_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(8)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	   	IF  EXISTS (SELECT 1 FROM t_packageunit WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '��������ظ��İ�װ��λ';
		ELSE		
			INSERT INTO t_packageunit (F_Name) VALUES (sName);
			
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_packageunit WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END