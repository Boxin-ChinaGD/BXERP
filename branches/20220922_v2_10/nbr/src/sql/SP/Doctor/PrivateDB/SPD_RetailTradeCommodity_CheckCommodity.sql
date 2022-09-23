DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iNO INT;
	DECLARE iCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_CommodityID AS iCommodityID, F_NO AS iNO FROM t_retailtradecommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
		
		-- ����������۵���Ʒ�����Ʒ�����Ƿ����0
		-- ������ǣ���ô����Ϊ���ݲ�����
		IF iNO <=0 THEN 
			SET done := TRUE;
			SET iErrorCode := 7;
			SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ�Ŀ��������0');
		-- ����������۵���Ʒ�����Ʒ�ǲ�����Ʒ���е���Ʒ
		-- ������ǣ���ô����Ϊ���ݲ�����
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN 
			SET done := TRUE;
			SET iErrorCode := 7;
			SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Ӧ����Ʒ������Ʒ���е���Ʒ');
		ELSE
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;