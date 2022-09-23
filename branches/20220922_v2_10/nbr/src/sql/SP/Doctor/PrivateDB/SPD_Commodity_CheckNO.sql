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
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
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
				SET sErrorMsg = CONCAT('ID为',iID, '已删除的商品库存只能为0');  
			ELSEIF iType = 1 OR iType = 2 OR iType = 3  THEN
			   -- 检查多包装,组合,服务型商品库存
			   IF iNO != 0 THEN
			       SET done = TRUE;
				   SET iErrorCode = 7;
				   SET sErrorMsg = CONCAT('ID为',iID, '多包装,组合,服务型商品的商品库存只能为0');
			   END IF ;
			ELSEIF iType = 0 THEN
			   -- 普通商品   
		       IF NOT EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
		           -- 检查未入库商品库存
		           IF IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) -
		              IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource  WHERE F_ReducingCommodityID = iID),0) != iNO THEN
			              SET done = TRUE;
				          SET iErrorCode = 7;
				          SET sErrorMsg = CONCAT('ID为',iID, '的商品,是未入库商品,商品库存 = 去向表F_NO - 来源表F_NO');
		           END IF ;
		       -- 根据商品ID查出入库单商品是未审核的条数 - 根据商品ID查出入库单商品的条数 如果不等于0 这个入库商品就全不是未审核的          
			   ELSEIF (SELECT count(F_ID) FROM t_warehousingcommodity WHERE F_WarehousingID IN (SELECT F_ID FROM t_warehousing WHERE F_Status = 0) AND F_CommodityID = iID) - 
			          (SELECT count(F_ID) FROM t_warehousingcommodity WHERE F_CommodityID = iID) = 0 THEN   
			       -- 检查入库单商品全是未审核的商品库存   
			       IF IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) -
		              IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource  WHERE F_ReducingCommodityID = iID),0) != iNO THEN
			              SET done = TRUE;
				          SET iErrorCode = 7;
				          SET sErrorMsg = CONCAT('ID为',iID, '的商品,入库单商品全是未审核,商品库存 = 去向表F_NO - 来源表F_NO');
		           END IF ;   
		       -- 检查入库单已审核的商品库存(如果此商品也有入库单未审核的商品,检查商品库存也一样)
		       ELSEIF (SELECT IFNULL(SUM(F_NO),0) FROM t_warehousingcommodity WHERE F_CommodityID = iID AND F_WarehousingID IN (SELECT F_ID FROM t_warehousing WHERE F_Status = 1)) 
		       		- 
		       		IFNULL((SELECT SUM(F_NO) FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iID),0) 
		       		+
		            IFNULL((SELECT SUM(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iID),0) 
		            != iNO 
		       THEN
	              SET done = TRUE;
		          SET iErrorCode = 7;
		          SET sErrorMsg = CONCAT('ID为',iID, '的商品,入库单有已审核和未审核的商品,商品库存 = 入库单已审核商品F_NO + 去向表F_NO - 来源表F_NO');
		       END IF;
			ELSE 
		       SET done = TRUE;
			   SET iErrorCode = 7;
			   SET sErrorMsg = CONCAT('ID为',iID, '的商品类型不正确');
		    END IF ;
		END LOOP read_loop;
		CLOSE list;
	COMMIT;
END;