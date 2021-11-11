DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckSalableNO`;
/* 
1、未审核的入库单的入库商品A的数量等于A的可售数量。
2、商品A的入库数量-A的零售数量+A的退货数量=A的可售数量，前提是A的入库单ID=零售单商品来源表的入库ID=退货去向表的入库ID。 
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
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
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
			
			-- 根据入库商品的入库单id找到入库单审核状态
			SELECT F_Status INTO iStatus FROM t_warehousing WHERE F_ID = iWarehousingID; 
		   
			IF iStatus = 0 THEN
			    -- 检查入库单未审核入库商品可售数量
			    IF iNO - iNOSalable != 0 THEN
			        SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('ID为',iID, '未审核入库商品可售数量不正确');
			    END IF;
			ELSEIF iStatus = 1 THEN
			    -- 拼接入库商品id
		     	SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID AND  F_WarehousingID IN  (SELECT F_ID FROM t_warehousing WHERE F_Status = 1);   
			    -- 检查入库单已审核入库商品可售数量
			    IF iNO - IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource WHERE F_WarehousingID = iWarehousingID AND F_ReducingCommodityID = iCommodityID),0)
			           + IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_WarehousingID = iWarehousingID AND F_IncreasingCommodityID = iCommodityID),0)
			           != iNOSalable THEN			       
			        SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('ID为',sGroup_concatID, '已审核入库商品可售数量不正确');
			    END IF ;    
			ELSE 
			    SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('ID为',iID, '入库商品对应的入库单的状态只能是0和1');
			END IF ;	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;