DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByStaff_Create`;
-- 统计Staff的销售业绩
-- Staff的销售业绩= 销售总额 - 退货价 
-- 退货价计算: 当统计退货价时，需要查看源单（零售单）的操作人此时有无上班。当在上班，则算源单的操作人头上；没有上班，则算退货单的操作人头上。
-- 举例：
-- 1.当A,B都上班，A销售一百，B退货A的零售单50，此时A上班，退货的金额算在A头上,A的业绩是50 
-- 2.当A没有上班，B上班，B对A的业绩退款50元，此时A未上班，退货的金额算在B头上,B的业绩是-50
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByStaff_Create`(
	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),	 
   	IN iShopID INT,
   	IN dSaleDatetime DATETIME,		-- 销售日期
   	IN iDeleteOldData INT
)
BEGIN 
	DECLARE iTotalNO INT;   						 	-- 销售总笔数
	DECLARE dTotalGross DECIMAL(20, 6); 			 	-- 销售总毛利
	DECLARE dTotalAmountWithReturned DECIMAL(20, 6);  	-- 未除掉退货的销售总额
	DECLARE dTotalAmountWithoutReturned DECIMAL(20, 6); -- 已除退货金额的销售总额
	DECLARE iStaffID INT; 		 						-- 员工ID
	DECLARE dTotalPurchasingPrice DECIMAL(20, 6); 		-- iStaffID销售商品的总进货价
	DECLARE amountReturnMine DECIMAL(20, 6);			-- 别人退iStaffID的单，iStaffID在上班(包含别人退iStaffID的单，iStaffID退自己的单)
	DECLARE amountReturnOthers DECIMAL(20, 6);			-- iStaffID退别人的单，别人没上班。A退C的，不包括B退C的
	
	DECLARE dReturnTotalPrice DECIMAL(20, 6);		-- 退货总成本
	DECLARE dSaleGross DECIMAL(20, 6);				-- 销售毛利
	DECLARE dReturnGross DECIMAL(20, 6);			-- 退货毛利
 
	DECLARE done INT DEFAULT false;
	
	-- 从收银汇总中拿出当天上班的员工
	DECLARE listStaff CURSOR FOR (
			SELECT 
				F_StaffID AS iStaffID
			FROM t_retailtradeaggregation 
			WHERE datediff(F_WorkTimeStart, dSaleDatetime) = 0 
			GROUP BY F_StaffID
	); 
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true; 	
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;

   	START TRANSACTION;
   		-- 仅用于测试，保证测试通过
		IF iDeleteOldData = 1 THEN 
		   DELETE FROM T_RetailTradeDailyReportByStaff WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		END IF;
   	
		-- 	零售单员工业绩表
		OPEN listStaff;  
			read_loop: LOOP	  
		   		FETCH listStaff INTO iStaffID;   
				IF done THEN  
					   LEAVE read_loop;   
				END IF;
		
				-- 计算iStaffID某天（dSaleDatetime）的销售笔数
				SELECT ifnull(sum(F_Amount), 0.000000), count(1) INTO dTotalAmountWithReturned, iTotalNO
				FROM t_retailtrade 
				WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 
				AND F_SourceID = -1 -- -1代表的是非退货型零售单。>0，代表的是退货型零售单。
				AND F_ShopID = iShopID
				AND F_StaffID = iStaffID;
				
				-- 业绩需要算在iStaffID头上的退货单
				-- 1.别人退iStaffID的单，iStaffID在上班(包含别人退iStaffID的单，iStaffID退自己的单)
				SELECT ifnull(sum(rtt.F_Amount), 0.000000) INTO amountReturnMine
				FROM t_retailtrade rtt -- rtt是别人在dSaleDatetime退的单
				WHERE rtt.F_SourceID IN (
					SELECT F_ID FROM t_retailtrade WHERE F_StaffID = iStaffID -- 是针对我的单退的
				)
				AND rtt.F_ShopID = iShopID
				AND datediff(rtt.F_SaleDatetime, dSaleDatetime) = 0
				AND EXISTS ( -- 我在上班
					SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
					WHERE rt.F_StaffID = rg.F_StaffID
						AND rt.F_ID = rtt.F_SourceID
						AND rt.F_ShopID = iShopID
						AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd
					    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0   -- iStaffID今天有上班
				);
 
				-- 2.iStaffID退别人的单，别人没上班。A退C的，不包括B退C的
			    SELECT ifnull(sum(F_Amount), 0.000000) INTO amountReturnOthers
			    FROM t_retailtrade rtt
			    WHERE rtt.F_StaffID = iStaffID -- 是我退的
				    AND rtt.F_SourceID IN (
					    SELECT F_ID FROM t_retailtrade WHERE F_StaffID <> iStaffID
					)
					AND rtt.F_ShopID = iShopID
					AND datediff(rtt.F_SaleDatetime, dSaleDatetime) = 0 -- 是我今天退的
					-- 源单的操作人没上班
					AND NOT EXISTS (
						SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
						WHERE rt.F_StaffID = rg.F_StaffID
							AND rt.F_ID = rtt.F_SourceID
							AND rt.F_ShopID = iShopID
							AND rt.F_StaffID <> iStaffID 
							AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd -- 退货的时间，在别人上班的区间之内
						    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0 -- dSaleDatetime别人有上班
					);

				-- 计算销售总额
				SET dTotalAmountWithoutReturned = dTotalAmountWithReturned - amountReturnMine - amountReturnOthers;
			
				-- 计算iStaffID当天销售的商品的总成本
				SELECT ifnull(sum(ifnull(src.F_NO, 0) * 
					ifnull((SELECT wshc.F_Price FROM t_warehousingcommodity wshc WHERE wshc.F_WarehousingID = src.F_WarehousingID AND wshc.F_CommodityID = src.F_ReducingCommodityID), 0.000000)), 0.000000) INTO dTotalPurchasingPrice
				FROM t_retailtradecommoditysource AS src
				WHERE src.F_RetailTradeCommodityID IN (
						SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
							SELECT rt.F_ID FROM t_retailtrade rt WHERE rt.F_StaffID = iStaffID AND rt.F_ShopID = iShopID AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 AND rt.F_SourceID = -1
						)
				);
				
				-- 计算退iStaffID的货的总成本
				SELECT ifnull(sum(ifnull(dest.F_NO, 0) * 
					ifnull((SELECT wshc.F_Price FROM t_warehousingcommodity wshc WHERE wshc.F_WarehousingID = dest.F_WarehousingID AND wshc.F_CommodityID = dest.F_IncreasingCommodityID), 0.000000)), 0.000000) INTO dReturnTotalPrice
				FROM t_returnretailtradecommoditydestination AS dest  -- 所有退货的商品，都会插在去向表中
				WHERE dest.F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID IN (
							SELECT F_ID FROM t_retailtrade WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 AND F_ShopID = iShopID AND F_SourceID > 0 -- 退货型的零售单必须是dSaleDatetime退的
						)
				)
				AND (
						(
							dest.F_RetailTradeCommodityID IN (
								SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
									SELECT rtt.F_ID 			   -- 退货单
									FROM t_retailtrade rtt -- rtt是别人在dSaleDatetime退的单
									WHERE rtt.F_SourceID IN (
										SELECT F_ID FROM t_retailtrade WHERE F_StaffID = iStaffID -- 是针对我的单退的
									)
									AND rtt.F_ShopID = iShopID
									AND EXISTS ( -- 我在上班
										SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
										WHERE rt.F_StaffID = rg.F_StaffID
											AND rt.F_ID = rtt.F_SourceID
											AND rt.F_ShopID = iShopID
											AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd
										    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0   -- iStaffID今天有上班
									)
								 )	
							)
							
						)
						OR 
						(
							dest.F_RetailTradeCommodityID IN (
								-- 2.iStaffID退别人的单，别人没上班
								SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
									SELECT rt.F_ID FROM t_retailtrade rt -- rt是iStaffID在dSaleDatetime的退货单
									WHERE rt.F_StaffID = iStaffID  -- 是我退的
										AND rt.F_ShopID = iShopID
										AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 -- 是我dSaleDatetime退的
										AND rt.F_SourceID IN (
											SELECT rtOthers.F_ID FROM t_retailtrade rtOthers 
											WHERE rtOthers.F_StaffID <> iStaffID
												AND rtOthers.F_ShopID = iShopID
												AND NOT EXISTS (
											 		SELECT 1 FROM t_retailtradeaggregation agg WHERE agg.F_StaffID = rtOthers.F_StaffID
														AND datediff(agg.F_WorkTimeStart, dSaleDatetime) = 0 -- 别人在上班
														AND rt.F_SaleDatetime BETWEEN agg.F_WorkTimeStart AND agg.F_WorkTimeEnd -- 退货的时间在别人的上班、下班时间中间
														
												)
										)
								)
							)
						)
				);
				
				-- 销售毛利 = 销售总额 - 销售总成本
				SET dSaleGross = dTotalAmountWithReturned - dTotalPurchasingPrice;
				
				-- 退货毛利 = 退货总额 - 退货总成本   
				SET dReturnGross = (amountReturnMine + amountReturnOthers) - dReturnTotalPrice;	
				
				-- 毛利 = 销售毛利 - 退货毛利  
				SET dTotalGross = dSaleGross - dReturnGross;
			
				INSERT INTO T_RetailTradeDailyReportByStaff(
					F_Datetime,
					F_ShopID,
					F_StaffID,
					F_NO,
					F_TotalAmount,
					F_GrossMargin
				) VALUES (
					dSaleDatetime,
					iShopID,
					iStaffID,
					iTotalNO,
					dTotalAmountWithoutReturned,
					dTotalGross		
				);	   							   
		   	END LOOP read_loop;
		CLOSE listStaff;
			
		SELECT 
			F_ID,
			F_Datetime,
			F_StaffID,
			F_NO,
			F_TotalAmount,
			F_GrossMargin,
			F_CreateDatetime,
			F_UpdateDatetime
		FROM T_RetailTradeDailyReportByStaff
		WHERE F_Datetime = dSaleDatetime AND F_ShopID = iShopID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
   COMMIT;
END; 