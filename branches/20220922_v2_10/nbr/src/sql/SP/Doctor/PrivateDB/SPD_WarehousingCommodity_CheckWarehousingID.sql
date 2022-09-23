DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckWarehousingID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckWarehousingID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iCommodityID INT;
	DECLARE iWarehousingID INT;
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID,F_CommodityID AS iCommodityID,F_WarehousingID AS iWarehousingID FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode = 0;
		SET sErrorMsg = '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iCommodityID,iWarehousingID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单商品表存在的入库单ID是否为存在的入库单,如果不是，那么则认为数据不健康
			IF NOT EXISTS (SELECT 1 FROM t_warehousing WHERE F_ID = iWarehousingID) THEN 
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('ID为', iID, '的入库单商品对应入库单不存在');				
			-- 检查退货去向商品对应的入库单商品，如果还是未审核,入库ID只能为null
			ELSEIF EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iCommodityID AND F_WarehousingID =
			       (SELECT F_ID FROM t_warehousing WHERE F_ID = iWarehousingID AND F_Status = 0)) THEN
                SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('ID为', iID, '入库单商品对应的退货去向表入库ID不为NULL');				
			-- 检查零售来源商品对应的入库单商品，如果还是未审核,入库ID只能为null
			ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iCommodityID AND F_WarehousingID =
			       (SELECT F_ID FROM t_warehousing WHERE F_ID = iWarehousingID AND F_Status = 0)) THEN
                SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('ID为', iID, '入库单商品对应的零售来源表入库ID不为NULL');
			END IF;
		END LOOP read_loop;
		CLOSE list;	
	COMMIT;
END;