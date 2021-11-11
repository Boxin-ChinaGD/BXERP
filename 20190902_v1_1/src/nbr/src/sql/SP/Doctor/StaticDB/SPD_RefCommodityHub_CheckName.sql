DROP PROCEDURE IF EXISTS `SPD_RefCommodityHub_CheckName`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RefCommodityHub_CheckName`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sName VARCHAR(32);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Name AS sName FROM staticdb.t_refcommodityhub);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sName;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查商品名称的长度是否满足(0~32]
			IF sName IS NULL OR length(sName) = 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的参考商品的商品名称不能为空');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;