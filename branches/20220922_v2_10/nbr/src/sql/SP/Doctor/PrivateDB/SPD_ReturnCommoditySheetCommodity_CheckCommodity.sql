DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheetCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheetCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iReturnCommoditySheetCommodityID INT;
	DECLARE iNO INT;
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetCommodityID, F_NO AS iNO FROM t_returncommoditysheetcommodity );
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			SELECT F_CommodityID INTO iCommodityID FROM t_returncommoditysheetcommodity WHERE F_ID = iReturnCommoditySheetCommodityID;
			-- ��������˻�����Ʒ�е���Ʒ��������Ʒ����
			-- ��������ڣ�����Ϊ���ݲ�����
			IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
				-- ��������˻�����Ʒ�е���Ʒ����Ϊ���װ��Ʒ�������Ʒ����ɾ����Ʒ�ͷ�����Ʒ
				-- ���Ϊ��ɾ������Ʒ����ô����Ϊ���ݲ�����
				SELECT F_Type, F_Status INTO iType, iStatus FROM t_commodity WHERE F_ID = iCommodityID;
				IF (iType = 0 AND iStatus IN (0, 1)) THEN
					-- ��������˻�����Ʒ���е���Ʒ����������ȫ������0
					-- ���������0������Ϊ���ݲ�����
					IF iNO > 0 THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := concat('�˻�����Ʒ', iReturnCommoditySheetCommodityID, '����Ʒ�����������0');
					END IF;
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := concat('�˻�����Ʒ', iReturnCommoditySheetCommodityID, 'ֻ����δɾ������ͨ��Ʒ');
				END IF;
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('�˻�����Ʒ', iReturnCommoditySheetCommodityID, '����������Ʒ����');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;