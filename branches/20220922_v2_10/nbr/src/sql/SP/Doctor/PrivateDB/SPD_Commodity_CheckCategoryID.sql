DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckCategoryID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckCategoryID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iStatus INT;
	DECLARE iCategoryID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Status AS iStatus FROM t_commodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- �����������Ʒ��CategoryID��ɾ��ǰӦ��������Ӧ��CategoryID,ɾ����Ӧ��Ϊnull
			IF iStatus <> 2 THEN
				-- 
				IF EXISTS(SELECT 1 FROM t_category WHERE F_ID IN (SELECT F_CategoryID FROM t_commodity WHERE F_ID = iCommodityID)) THEN 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID ,'û�ж�Ӧ�����ID');
				END IF;
			ELSE	
			   SELECT F_CategoryID INTO iCategoryID FROM t_commodity WHERE F_ID = iCommodityID;
			   
			   IF iCategoryID IS NULL THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ɾ����Ʒ', iCommodityID ,'û����ȷ���Ķ�Ӧ�����ID����Ӧ����null');
				END IF;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;