DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckSalableNO`;
/* 
1��δ��˵���ⵥ�������ƷA����������A�Ŀ���������
2����ƷA���������-A����������+A���˻�����=A�Ŀ���������ǰ����A����ⵥID=���۵���Ʒ��Դ������ID=�˻�ȥ�������ID�� 
*/
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckSalableNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iCommodityID INT;
	DECLARE iWarehousingID INT;  
	DECLARE iNO INT;  
	DECLARE iNOSalable INT;   
	DECLARE iStatus INT;   
	DECLARE sGroup_concatID VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_CommodityID AS iCommodityID,F_WarehousingID AS iWarehousingID,F_NO AS iNO,F_NOSalable AS iNOSalable FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
					
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iCommodityID,iWarehousingID,iNO,iNOSalable;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������Ʒ����ⵥid�ҵ���ⵥ���״̬
			SELECT F_Status INTO iStatus FROM t_warehousing WHERE F_ID = iWarehousingID; 
		   
			IF iStatus = 0 THEN
			    -- �����ⵥδ��������Ʒ��������
			    IF iNO - iNOSalable != 0 THEN
			        SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('IDΪ',iID, 'δ��������Ʒ������������ȷ');
			    END IF;
			ELSEIF iStatus = 1 THEN
			    -- ƴ�������Ʒid
		     	SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID AND  F_WarehousingID IN  (SELECT F_ID FROM t_warehousing WHERE F_Status = 1);   
			    -- �����ⵥ����������Ʒ��������
			    IF iNO - IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource WHERE F_WarehousingID = iWarehousingID AND F_ReducingCommodityID = iCommodityID),0)
			           + IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_WarehousingID = iWarehousingID AND F_IncreasingCommodityID = iCommodityID),0)
			           != iNOSalable THEN			       
			        SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('IDΪ',sGroup_concatID, '����������Ʒ������������ȷ');
			    END IF ;    
			ELSE 
			    SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('IDΪ',iID, '�����Ʒ��Ӧ����ⵥ��״ֻ̬����0��1');
			END IF ;	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;