DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommodity_CheckBarcodeID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommodity_CheckBarcodeID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iBarcodeID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_BarcodeID AS iBarcodeID FROM t_retailtradecommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iBarcodeID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е����۵���Ʒ���barcodesID,�������barcodes����
			-- ��������ڣ�����Ϊ���ݲ�����
			IF NOT EXISTS(SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��BarcodeIDû�ж�Ӧ��Barcode');
			ELSE
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;