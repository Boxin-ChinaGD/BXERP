DROP PROCEDURE IF EXISTS `SP_WxUser_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WxUser_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN oppenId VARCHAR(30)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_wxuser WHERE F_OpenId = oppenId) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '传入一个不存在的oppenid,删除失败';
		ELSEIF (oppenId IS NULL) OR (oppenId='') THEN
		   	SET iErrorCode := 7;
		   	SET sErrorMsg := '传入一个空的oppenid,删除失败';
		ELSE
		
		    DELETE FROM t_wxuser WHERE F_OpenId = oppenId;
		    SET iErrorCode := 0;
		    SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;
		