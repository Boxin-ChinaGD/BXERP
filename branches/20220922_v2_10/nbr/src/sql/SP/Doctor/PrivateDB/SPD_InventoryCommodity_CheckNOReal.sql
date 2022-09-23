DROP PROCEDURE IF EXISTS `SPD_InventoryCommodtiy_CheckNOReal`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodtiy_CheckNOReal`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iNOReal INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_NOReal AS iNOReal FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iNOReal;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ����̵㵥��Ʒ��ʵ�������Ƿ���ڵ���0
			IF iNOReal >= 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥��Ʒ', iInventoryCommodityID ,'ʵ����������ȷ����Ҫ���ڵ���0');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;