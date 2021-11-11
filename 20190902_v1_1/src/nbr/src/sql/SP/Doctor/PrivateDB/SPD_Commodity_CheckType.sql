DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有商品的类型，商品类型只能为0,1,2,3
			IF iType = 0 OR iType = 1 OR iType = 3 THEN
			-- 商品类型为1，存在参照商品ID和参照商品倍数都要为0
			-- 商品类型为0，存在参照商品ID和参照商品倍数都要为0
			-- 商品类型为3，存在参照商品ID和参照商品倍数都要为0
				IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityID = 0 AND F_RefCommodityMultiple = 0) THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityID = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '的参照商品ID不正确，单品和组合商品的参照商品ID为0');
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityMultiple = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '的参照商品倍数不正确，单品和组合商品的参照商品倍数为0');
				END IF;
			-- 商品类型为2，存在参照商品ID是单品和参照商品倍数大于1
			ELSEIF iType = 2 THEN 
				IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = iCommodityID) AND F_Type = 0) 
				AND EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityMultiple > 1 AND F_ID = iCommodityID) THEN 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = iCommodityID) AND F_Type = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '的参照商品ID不正确，多包装商品的参照商品ID为存在的单品');
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityMultiple > 1 AND F_ID = iCommodityID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '的参照商品倍数不正确，多包装商品的参照商品倍数大于1');
				END IF;
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('商品', iCommodityID, '的商品类型Type不正确，它只能为0，1，2，3'); 
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;