DROP PROCEDURE IF EXISTS `SPD_InventoryCommodtiy_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodtiy_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iInventorysheetID INT;
	DECLARE iCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_CommodityID AS iCommodityID,F_InventorysheetID AS iInventorysheetID FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iCommodityID,iInventorysheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- �����ƷID�Ƿ�����Ʒ���Ѿ����ڵ���Ʒ(�����̵㵥����Ʒ���ܱ�ɾ��)
			-- ��������̵���Ʒ�����Ʒֻ������ͨ��Ʒ���̵㵥
			IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 0 AND F_Status <> 2) THEN
				-- ����Ƿ�����ͬ����Ʒ��ͬһ���̵㵥��
				IF (SELECT count(1) FROM t_inventorycommodity WHERE F_CommodityID = iCommodityID AND F_InventorysheetID = iInventorysheetID) >= 2 THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('�̵㵥��Ʒ', iInventoryCommodityID ,'��������ͬ����Ʒ��ͬһ���̵㵥');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type <> 0 AND F_Status <> 2) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥��Ʒ', iInventoryCommodityID ,'��Ӧ����Ʒ����Ʒ���Ͳ��ǵ�Ʒ');
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥��Ʒ', iInventoryCommodityID ,'��Ӧ����Ʒ������');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;