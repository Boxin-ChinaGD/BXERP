DROP PROCEDURE IF EXISTS `SP_VIPCategory_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIPCategory_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_vip WHERE F_Category = iID ) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '会员表中已有该会员的类别,不能删除';
		ELSE	
			DELETE FROM t_vip_category WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;