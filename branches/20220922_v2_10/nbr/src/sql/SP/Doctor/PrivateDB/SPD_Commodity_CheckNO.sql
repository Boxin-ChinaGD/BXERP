DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckNO`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(65)
)
BEGIN
	DECLARE iID INT;
	DECLARE iNO INT;  
	DECLARE iType INT; 
	DECLARE done INT DEFAULT FALSE;
  
	DECLARE list CURSOR FOR (SELECT F_ID AS iID,F_NO AS iNO,F_Type AS iType FROM t_commodity);
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
			FETCH list INTO iID,iNO,iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_Status = 2 AND F_NO != 0) THEN
			    SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('IDΪ',iID, '��ɾ������Ʒ���ֻ��Ϊ0');  
			ELSEIF iType = 1 OR iType = 2 OR iType = 3  THEN
			   -- �����װ,���,��������Ʒ���
			   IF iNO != 0 THEN
			       SET done = TRUE;
				   SET iErrorCode = 7;
				   SET sErrorMsg = CONCAT('IDΪ',iID, '���װ,���,��������Ʒ����Ʒ���ֻ��Ϊ0');
			   END IF ;
			ELSEIF iType = 0 THEN
			   -- ��ͨ��Ʒ   
		       IF NOT EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
		           -- ���δ�����Ʒ���
		           IF IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) -
		              IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource  WHERE F_ReducingCommodityID = iID),0) != iNO THEN
			              SET done = TRUE;
				          SET iErrorCode = 7;
				          SET sErrorMsg = CONCAT('IDΪ',iID, '����Ʒ,��δ�����Ʒ,��Ʒ��� = ȥ���F_NO - ��Դ��F_NO');
		           END IF ;
		       -- ������ƷID�����ⵥ��Ʒ��δ��˵����� - ������ƷID�����ⵥ��Ʒ������ ���������0 ��������Ʒ��ȫ����δ��˵�          
			   ELSEIF (SELECT count(F_ID) FROM t_warehousingcommodity WHERE F_WarehousingID IN (SELECT F_ID FROM t_warehousing WHERE F_Status = 0) AND F_CommodityID = iID) - 
			          (SELECT count(F_ID) FROM t_warehousingcommodity WHERE F_CommodityID = iID) = 0 THEN   
			       -- �����ⵥ��Ʒȫ��δ��˵���Ʒ���   
			       IF IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) -
		              IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource  WHERE F_ReducingCommodityID = iID),0) != iNO THEN
			              SET done = TRUE;
				          SET iErrorCode = 7;
				          SET sErrorMsg = CONCAT('IDΪ',iID, '����Ʒ,��ⵥ��Ʒȫ��δ���,��Ʒ��� = ȥ���F_NO - ��Դ��F_NO');
		           END IF ;   
		       -- �����ⵥ����˵���Ʒ���(�������ƷҲ����ⵥδ��˵���Ʒ,�����Ʒ���Ҳһ��)
		       ELSEIF (SELECT IFNULL(SUM(F_NO),0) FROM t_warehousingcommodity WHERE F_CommodityID = iID AND F_WarehousingID IN (SELECT F_ID FROM t_warehousing WHERE F_Status = 1)) 
		       		- 
		       		IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iID),0) 
		       		+
		            IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) 
		            != iNO 
		       THEN
	              SET done = TRUE;
		          SET iErrorCode = 7;
		          SET sErrorMsg = CONCAT('IDΪ',iID, '����Ʒ,��ⵥ������˺�δ��˵���Ʒ,��Ʒ��� = ��ⵥ�������ƷF_NO + ȥ���F_NO - ��Դ��F_NO');
		       END IF;
			ELSE 
		       SET done = TRUE;
			   SET iErrorCode = 7;
			   SET sErrorMsg = CONCAT('IDΪ',iID, '����Ʒ���Ͳ���ȷ');
		    END IF ;
		END LOOP read_loop;
		CLOSE list;
	COMMIT;
END;