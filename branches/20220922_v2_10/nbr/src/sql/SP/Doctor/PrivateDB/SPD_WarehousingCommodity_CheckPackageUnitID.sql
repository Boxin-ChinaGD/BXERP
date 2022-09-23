DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckPackageUnitID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckPackageUnitID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iWarehousingCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingCommodityID FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������ⵥ��Ʒ���Ƿ��ж�Ӧ��PackageUnitID
			IF EXISTS (SELECT 1 FROM t_packageunit WHERE F_ID IN (SELECT F_PackageUnitID FROM t_warehousingcommodity WHERE F_ID = iWarehousingCommodityID)) THEN
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			-- ���û�У���ô����Ϊ���ݲ�����
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('��ⵥ��Ʒ', iWarehousingCommodityID, 'û�ж�Ӧ�İ�װ��λ');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;