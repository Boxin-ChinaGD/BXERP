DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckVipID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckVipID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE iVipID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_VipID AS iVipID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iVipID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有的零售单的VipID除了为空，必须存在vip表中
			-- 如果不是，那么则认为数据不健康
			IF iVipID IS NULL THEN -- VipID为空
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN -- VipID不为空并且存在vip表
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE -- VipID不为空并且不存在vip表
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单的VipID不存在vip表中');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;