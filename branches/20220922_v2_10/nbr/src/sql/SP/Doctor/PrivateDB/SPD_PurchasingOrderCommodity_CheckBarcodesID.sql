DROP PROCEDURE IF EXISTS `SPD_PurchasingOrderCommodity_CheckBarcodesID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrderCommodity_CheckBarcodesID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingOrderCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iPurchasingOrderCommodityID FROM t_purchasingordercommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
		   
			FETCH list INTO iPurchasingOrderCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			-- ������еĲɹ�������Ʒ��barcodesID,�������barcodes����
			-- ��������ڣ�����Ϊ���ݲ�����
			SELECT 1 FROM t_barcodes WHERE F_ID IN (SELECT F_BarcodeID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF found_rows() > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, 'û�ж�Ӧ��������ID');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;