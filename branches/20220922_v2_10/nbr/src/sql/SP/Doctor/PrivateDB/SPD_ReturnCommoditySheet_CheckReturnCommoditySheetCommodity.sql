DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ����˻����Ƿ�����˻���Ʒ
			-- ��������ڣ�����Ϊ���ݲ�����
			IF EXISTS (SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('�˻���:', iID, 'û���˻���Ʒ');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;