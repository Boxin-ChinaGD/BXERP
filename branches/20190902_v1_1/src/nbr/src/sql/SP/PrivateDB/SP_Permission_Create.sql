DROP PROCEDURE IF EXISTS `SP_Permission_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Permission_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sSP VARCHAR(80),
	IN sName VARCHAR(20),
	IN sDomain VARCHAR(16),
	IN sRemark VARCHAR(32)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	   	IF  EXISTS (SELECT 1 FROM t_Permission WHERE F_Name = sName) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '不能添加重复的权限';
		ELSE		
			INSERT INTO t_Permission (F_SP, F_Name, F_Domain, F_Remark) VALUES (sSP, sName, sDomain, sRemark);
		   
			SELECT F_ID, F_SP, F_Name, F_Domain, F_Remark,F_CreateDatetime FROM t_Permission WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END