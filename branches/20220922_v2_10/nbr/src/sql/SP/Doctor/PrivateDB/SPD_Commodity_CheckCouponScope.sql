DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckCouponScope`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckCouponScope`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType, F_Status AS iStatus FROM t_commodity /*WHERE F_Status <> 2*/);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ֻ�е�Ʒ�ſ��Բ����Ż�ȯ��Χ
			IF EXISTS (SELECT 1 FROM t_couponscope WHERE F_CommodityID = iCommodityID) AND iType <> 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID ,'���ǵ�Ʒ�����ܲ����Ż�ȯ��Χ');
			-- �����Ż�ȯ��Χ����ͨ��Ʒ��������ɾ��״̬(���ܱ�ɾ��)
			ELSEIF EXISTS (SELECT 1 FROM t_couponscope WHERE F_CommodityID = iCommodityID) AND iType = 0 AND iStatus = 2 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID ,'�������Ż�ȯ��Χ, ��������ɾ��״̬(���ܱ�ɾ��)');
			-- 
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;