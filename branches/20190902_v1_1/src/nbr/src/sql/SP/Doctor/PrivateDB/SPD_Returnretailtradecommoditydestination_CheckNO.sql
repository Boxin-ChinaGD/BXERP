DROP PROCEDURE IF EXISTS `SPD_Returnretailtradecommoditydestination_CheckNO`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Returnretailtradecommoditydestination_CheckNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRetailTradeCommodityID INT;
	DECLARE iIncreasingCommodityID INT;
	DECLARE iNO INT;
	DECLARE iID INT;
	DECLARE iType INT;
	DECLARE iMultiple INT;
	DECLARE sGroup_concatID VARCHAR(20); 
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_RetailTradeCommodityID AS iRetailTradeCommodityID, F_IncreasingCommodityID AS iIncreasingCommodityID FROM t_returnretailtradecommoditydestination);
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
			FETCH list INTO iID, iRetailTradeCommodityID, iIncreasingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;	  
            
		    -- 检查退货单商品是否存在商品表中
		    IF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iIncreasingCommodityID) THEN
		        SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的退货单商品来源没有对应的商品');
			--  检查退货去向表商品类型不是0和3的商品
		    ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_Type IN (0,3) AND F_ID = iIncreasingCommodityID) THEN
		        SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表的商品ID对应的商品类型只能是0和3的商品');
			-- 检查退货去向表没有对应的退货单商品
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) THEN
			    SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表没有对应的退货单商品');    
			-- 检查退货单商品有没有对应的退货单
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表没有对应的退货单'); 
			-- 每一张退货去向表的F_NO必须小于或等于对应零售来源F_NO
            ELSEIF (SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_IncreasingCommodityID = iIncreasingCommodityID) 
            		>	   
			       (
			       	SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iIncreasingCommodityID AND F_RetailTradeCommodityID IN 
				       (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = 
				       		(
				       		SELECT F_SourceID FROM t_retailtrade WHERE F_ID = 
				       			(SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)
				       		)
				       	)
				   ) THEN 
			  
			    SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '退货去向表的F_NO必须小于或等于零售来源F_NO');
            ELSE 
		        -- 获取零售单商品的类型(普通商品、组合商品、多包装商品、服务商品)
		    	-- 如果iType的值并不是0，1，2、3中的值，那么就认为数据不健康
		    	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
		    	-- 拼接去向表id
		        SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID; 
		        
				IF iType = 0 THEN			 
				    IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = 
				    	(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
				    ) THEN 
					   	SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '去向表,普通商品F_NO与对应的退货商品F_NO不相等');
				    END IF;
				ELSEIF iType = 1 THEN 
				    -- 获取倍数
				    SELECT F_SubCommodityNO INTO iMultiple FROM t_subcommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) 
				    	AND F_SubCommodityID = iIncreasingCommodityID;
					-- 检查去向表组合商品F_NO与对应的退货商品F_NO*iMultiple是否相等	
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_IncreasingCommodityID = iIncreasingCommodityID)
					) THEN 			 			 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '的退货去向表的F_NO跟相应的退货单商品的F_NO不相等');
					END IF;
				ELSEIF iType = 2 THEN 
					-- 获取倍数
				    SELECT sum(F_RefCommodityMultiple) INTO iMultiple FROM t_commodity WHERE F_ID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
				   	-- 检查去向表多包装商品F_NO与对应的退货商品F_NO*iMultiple是否相等
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '的退货去向的F_NO跟相应的退货单商品的F_NO不相等');
				    END IF;
				ELSEIF iType = 3 THEN 
					-- 检查去向表服务型商品F_NO与对应的退货商品F_NO是否相等
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN 			   
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', iID, '的退货去向的F_NO跟相应的退货单商品的F_NO不相等');
					END IF;       
                    
	           END IF ;
		   
		     END IF ;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;