DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckWarehousingID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckWarehousingID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iCommodityID INT;
	DECLARE iWarehousingID INT;
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID,F_CommodityID AS iCommodityID,F_WarehousingID AS iWarehousingID FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode = 0;
		SET sErrorMsg = '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iCommodityID,iWarehousingID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������ⵥ��Ʒ����ڵ���ⵥID�Ƿ�Ϊ���ڵ���ⵥ,������ǣ���ô����Ϊ���ݲ�����
			IF NOT EXISTS (SELECT 1 FROM t_warehousing WHERE F_ID = iWarehousingID) THEN 
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('IDΪ', iID, '����ⵥ��Ʒ��Ӧ��ⵥ������');				
			-- ����˻�ȥ����Ʒ��Ӧ����ⵥ��Ʒ���������δ���,���IDֻ��Ϊnull
			ELSEIF EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iCommodityID AND F_WarehousingID =
			       (SELECT F_ID FROM t_warehousing WHERE F_ID = iWarehousingID AND F_Status = 0)) THEN
                SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('IDΪ', iID, '��ⵥ��Ʒ��Ӧ���˻�ȥ������ID��ΪNULL');				
			-- ���������Դ��Ʒ��Ӧ����ⵥ��Ʒ���������δ���,���IDֻ��Ϊnull
			ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iCommodityID AND F_WarehousingID =
			       (SELECT F_ID FROM t_warehousing WHERE F_ID = iWarehousingID AND F_Status = 0)) THEN
                SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('IDΪ', iID, '��ⵥ��Ʒ��Ӧ��������Դ�����ID��ΪNULL');
			END IF;
		END LOOP read_loop;
		CLOSE list;	
	COMMIT;
END;