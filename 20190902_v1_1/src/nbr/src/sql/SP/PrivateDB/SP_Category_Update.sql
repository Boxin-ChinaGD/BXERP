DROP PROCEDURE IF EXISTS `SP_Category_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Category_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(10),
	IN iParentID INT
	)
BEGIN
	DECLARE beforeID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ParentID INTO beforeID FROM t_category WHERE F_ID = iID;
		
		IF iParentID = beforeID THEN
			IF EXISTS (SELECT 1 FROM t_category WHERE F_Name = sName AND F_ID <> iID) THEN
				SET iErrorCode := 1;
				SET sErrorMsg := '类别名称重复';
	   		ELSE		
		   		UPDATE t_category SET F_Name = sName,F_UpdateDatetime = now() WHERE F_ID = iID;	
		   		SELECT F_ID, F_Name, F_ParentID,F_CreateDatetime,F_UpdateDatetime FROM t_category WHERE F_ID = iID;
				SET iErrorCode := 0;
				SET sErrorMsg := '';
	   		END IF;
		ELSE
			IF EXISTS (SELECT 1 FROM t_categoryparent WHERE F_ID = iParentID) then
				UPDATE t_category SET F_Name = sName, F_ParentID = iParentID,F_UpdateDatetime = now() WHERE F_ID = iID;	
		   		SELECT F_ID, F_Name, F_ParentID,F_CreateDatetime,F_UpdateDatetime FROM t_category WHERE F_ID = iID;
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET iErrorCode := 7;
				SET sErrorMsg := '该商品大类不存在';			
			END IF;
		END IF;
	
	COMMIT;
END;