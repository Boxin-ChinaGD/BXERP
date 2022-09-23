DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckWarehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckWarehousing`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE iNOStart INT;
	DECLARE iPurchasingPriceStart FLOAT;
	DECLARE iNOSalable INT;
	DECLARE iPrice FLOAT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType, F_NOStart AS iNOStart, F_PurchasingPriceStart AS iPurchasingPriceStart FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType, iNOStart, iPurchasingPriceStart;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- �ڳ���Ʒ���ڳ��������ڳ��ɹ��������һ����ⵥ�Ŀ��������ͽ��������
			IF iType = 0 THEN
				IF iNOStart > 0 AND iPurchasingPriceStart > 0 THEN
					SELECT F_NOSalable INTO iNOSalable FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID ORDER BY F_ID ASC LIMIT 0,1;
					SELECT F_Price INTO iPrice FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID ORDER BY F_ID ASC LIMIT 0,1;
					IF iNOSalable = iNOStart AND iPrice = iPurchasingPriceStart THEN
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSEIF iNOSalable != iNOStart THEN
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '���ڳ������Ͷ�Ӧ��ⵥ�Ŀ������������');
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '���ڳ��ɹ��ۺͶ�Ӧ��ⵥ�Ľ����۲����');
					END IF;
				ELSEIF iNOStart = -1 AND iPurchasingPriceStart = -1 THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '���ڳ��������ڳ��ɹ��۲���ȷ���ڳ��������ڳ��ɹ���Ӧ��ͬʱΪ-1����ͬʱΪ����0����');
				END IF;
			-- �����Ʒ��Ӧ����ⵥ��Ʒ���������Ʒ���ǵ�Ʒ�����ܴ��ڶ�Ӧ����ⵥ��Ʒ��
			ELSE 
				IF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '���ǵ�Ʒ���������');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;