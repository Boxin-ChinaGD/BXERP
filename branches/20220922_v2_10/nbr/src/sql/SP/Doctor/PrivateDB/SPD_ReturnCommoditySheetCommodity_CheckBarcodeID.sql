DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheetCommodity_CheckBarcodeID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheetCommodity_CheckBarcodeID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iReturnCommoditySheetCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetCommodityID FROM t_returncommoditysheetcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е��˻�����Ʒ���barcodesID,�������barcodes����
			IF EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID IN (SELECT F_BarcodeID FROM t_returncommoditysheetcommodity WHERE F_ID = iReturnCommoditySheetCommodityID)) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			-- ��������ڣ�����Ϊ���ݲ�����
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('�˻�����Ʒ', iReturnCommoditySheetCommodityID, '��������ID������');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;