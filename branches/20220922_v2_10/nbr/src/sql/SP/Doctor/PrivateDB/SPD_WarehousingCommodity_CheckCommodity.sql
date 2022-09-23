DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iCommodityID INT;
	DECLARE iWarehousingCommodityID INT;
	DECLARE iNO INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingCommodityID, F_CommodityID AS iCommodityID, F_NO AS iNO FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingCommodityID, iCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������ⵥ��Ʒ�����Ʒ�ǲ�����Ʒ���е���Ʒ
			IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
				-- 
				-- �������������������ⵥ��Ʒ�����ƷID�Ƿ�Ʒ��������ǵ�Ʒ����ô����Ϊ���ݲ�����
				IF (SELECT F_Type FROM t_commodity WHERE F_ID = iCommodityID) = 0 THEN
			   	    -- 
			   		-- �������������������ⵥ��Ʒ�����������Ƿ����0
			   		IF iNO > 0 THEN
						SET iErrorCode = 0;
						SET sErrorMsg = '';
					-- ������ǣ���ô����Ϊ���ݲ�����
					ELSE
						SET iErrorCode = 7;
						SET sErrorMsg = CONCAT('��ⵥ��Ʒ', iWarehousingCommodityID, '�������С��1');
						SET done = TRUE;
					END IF;
			   		
				ELSE
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('��ⵥ��Ʒ', iWarehousingCommodityID, '���ǵ�Ʒ');
					SET done = TRUE;
				END IF;
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('��ⵥ��Ʒ', iWarehousingCommodityID, '������Ʒ���е���Ʒ');
			END IF;
			
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;