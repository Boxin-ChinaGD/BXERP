DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckPurchasingOrder`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckPurchasingOrder`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE sPurchasingUnit VARCHAR(16);
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_PurchasingUnit AS sPurchasingUnit, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, sPurchasingUnit, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������Ʒ�������Ʒ�Ͷ��װ��Ʒ���ܱ��ɹ�
			IF sPurchasingUnit IS NOT NULL AND iType <> 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT(IF(iType = 1, '�����Ʒ', IF(iType = 2, '���װ��Ʒ', '������Ʒ')), iCommodityID, '�����вɹ���λ');
			END IF;
			
			IF done = FALSE THEN -- ����������ֹ��ʶΪtrue����ô������������Ĵ���
			
				-- �����Ʒ�Ĳɹ���λΪ�գ���ô����Ʒ���ܴ��ڲɹ�����
				IF sPurchasingUnit IS NULL THEN 
					IF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID) THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('��Ʒ', iCommodityID ,'���ܱ��ɹ���������');
					END IF;
				ELSE
					IF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID)  THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('��Ʒ', iCommodityID ,'��Ҫ�ж�Ӧ�Ĳɹ�����');
					END IF;
				END IF;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;