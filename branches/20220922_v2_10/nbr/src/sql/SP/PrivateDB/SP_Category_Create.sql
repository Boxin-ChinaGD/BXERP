DROP PROCEDURE IF EXISTS `SP_Category_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName VARCHAR(10),
	IN iParentID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT F_Name FROM t_category WHERE F_Name = sName) THEN
		    SELECT F_ID, F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime FROM t_category WHERE F_Name = sName;
			SET iErrorCode := 1;
			SET sErrorMsg := '�Ѿ�������ͬ���Ƶ���Ʒ���';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_categoryparent WHERE F_ID = iParentID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ʒ���಻����';
		ELSE
			INSERT INTO t_category (F_Name, F_ParentID) VALUES (sName, iParentID);
			
			SELECT F_ID, F_Name, F_ParentID,F_CreateDatetime,F_UpdateDatetime FROM t_category WHERE F_ID = last_insert_id();
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END