DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckReturnCommoditySheet`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckReturnCommoditySheet`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��Ʒ����Ϊ�����Ʒ���ܱ��˻�
			-- ��Ʒ����Ϊ������Ʒ���ܱ��˻�
			IF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID) AND iType = 1 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�������Ʒ�������˻�');
			ELSEIF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID) AND iType = 3 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�Ƿ�����Ʒ�������˻�');
			ELSE 
			-- ��Ʒ����û���˻����������Ʒ�ͷ�����Ʒ�����˻�,�������͵���Ʒ���˻�
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;