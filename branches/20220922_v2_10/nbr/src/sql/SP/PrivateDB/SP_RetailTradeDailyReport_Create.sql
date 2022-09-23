
DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReport_Create`;
-- ë���� > 0ʱ�������ɱ����������ɱ������磬������������Ʒ��ȫ�������ˣ���һ����û�б������ɵģ���Ϊë����Ϊ0���������ȫ�ˣ�Ҳ��������ģ���ô��ë���Ǹ��������۶�
-- Ҳ�Ǹ���������������ë����Ҳ����������Ȼ�����ɱ���
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReport_Create`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),
   	IN iShopID INT, 				-- �ŵ�ID	
   	IN dSaleDatetime DATETIME,		-- ��������
   	IN deleteOldData INT
)
BEGIN

	DECLARE totalNO INT;   						 	-- ���۱���
	DECLARE topSaleCommodityName VARCHAR(32);    	-- ���۶���ߵ���Ʒ
	DECLARE topSaleCommodityNO INT;			   		-- ���۶���ߵ���Ʒ������
	DECLARE topSaleCommodityID INT;              	-- ���۶���ߵ���Ʒ����ƷID
	DECLARE totalAmount DECIMAL(20, 6); 		 	-- �������۶�
	DECLARE retailAmount DECIMAL(20, 6); 		 	-- �����ܶ�
	DECLARE returnAmount DECIMAL(20, 6); 		 	-- �˻��ܶ�
	DECLARE pricePurchase DECIMAL(20, 6); 			-- һ��������Ʒ���ܽ�����
	DECLARE returnPricePurchase DECIMAL(20, 6); 	-- һ���˻���Ʒ���ܽ�����
	DECLARE totalGross DECIMAL(20, 6); 			 	-- ����ë��
	DECLARE ratioGrossMargin DECIMAL(20, 6); 	 	-- ����ë����
	DECLARE topPurchaseCustomerName VARCHAR(30); 	-- �������ߵĿͻ�
	DECLARE averageAmountOfCustomer DECIMAL(20, 6); -- �͵���
	DECLARE topSaleCommodityAmount DECIMAL(30,6);  	-- ����������۶�
	
	DECLARE type INT; 								-- ��Ʒ����
	DECLARE commID INT;								-- ��ƷID
	DECLARE commNO INT;   							-- ÿ����Ʒ����������,����ָʵ����������������Ҫ��ȥ�˻�����Ʒ����
	DECLARE commodityAmount DECIMAL(20, 6); 	    -- ÿ����Ʒ�����۶�,��Ҫ��ȥ�˻���Ʒ�����۶��,�õ����������۶�
	DECLARE commodityPricePurchase DECIMAL(20, 6); 	-- ÿ����Ʒ�Ľ�����,��Ҫ��ȥ�˻���Ʒ�Ľ����ۺ�,�õ������Ľ�����
	DECLARE commGross DECIMAL(20, 6); 				-- ÿ����Ʒ������ë��
	DECLARE done INT DEFAULT  false; 
	
	DECLARE dtSaleDate DATE;				-- �Դ����ʱ����и�ʽ��
	
	DECLARE list CURSOR FOR (
		-- ��ѯÿ����Ʒ�ĵ�����������������۽������ܼۣ�ë��
		-- ���㵥Ʒ
		SELECT * FROM 
		(
			(
				SELECT F_CommodityID, sum(rtcsNO) AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtcs.F_NO AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rt.F_ShopID = iShopID
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 0 -- ��Ʒ
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			)
			UNION
			-- �������
			(
			   SELECT F_CommodityID, no AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtc.F_NOCanReturn, rtcs.F_ReducingCommodityID
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID  
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(rtcsNO) AS no, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount,
						rtc.F_NO AS rtcsNO	-- ����Ҫ��ѯ����Ʒʵ���۳�������������Ҫ��F_NO��������F_NOCanReturn
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 1 -- ���
						AND rt.F_SourceID = -1 
						AND rt.F_ShopID = iShopID
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			) 
			UNION
			-- ������װ
			(
			   SELECT F_CommodityID, no AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtc.F_NOCanReturn AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID  
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount, sum(rtcsNO) AS no
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount,
						rtc.F_NO AS rtcsNO
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rt.F_SourceID = -1 
				   		AND rt.F_ShopID = iShopID
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 2 -- ���װ
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			) 
			UNION 
			-- ���������Ʒ
			(
				SELECT F_CommodityID, sum(rtcsNO) AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtcs.F_NO AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 3 -- ������Ʒ
						AND rt.F_ShopID = iShopID
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			)
		) AS TMP
	
	); 
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true; 
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		-- �����ڲ��ԣ���֤����ͨ��
		IF deleteOldData = 1 THEN 
		   DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		   DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		END IF;	
	
		-- �������۱��������۵�����
		SELECT NO INTO totalNO 
		FROM 
		(
			SELECT count(F_ID) AS NO
			FROM  t_retailtrade 
			WHERE datediff(F_SaleDatetime,dSaleDatetime) = 0  -- datediff(date1,date2) ��������date֮����������
			AND F_SourceID = -1
			AND F_ShopID = iShopID
		) AS tmp1;

		-- �������ڲ�ѯ�����������������Ʒ��ID����Ʒ���ơ���������
	   	SELECT ifnull(Amount, 0.000000), F_CommodityID, F_Name, NO INTO topSaleCommodityAmount, topSaleCommodityID, topSaleCommodityName, topSaleCommodityNO  FROM  
		(
			SELECT 
			 rtc.F_CommodityID, rtc.F_PriceReturn * rtc.F_NOCanReturn , c.F_Name, sum(rtc.F_NOCanReturn) AS NO, rt.F_SaleDatetime, sum(rtc.F_PriceReturn * rtc.F_NOCanReturn) AS Amount
			FROM T_RetailTradeCommodity AS rtc, t_retailtrade AS rt, t_commodity c
			WHERE rtc.F_TradeID = rt.F_ID 
			AND rt.F_SourceID = -1
			AND rt.F_ShopID = iShopID
			AND rtc.F_CommodityID = c.F_ID 
			AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtc.F_CommodityID
			ORDER BY NO DESC, rtc.F_CommodityID DESC LIMIT 1
		) AS tmp2;
		
		 -- ͳ�������ܶ�
		 SELECT IF(Amount IS NULL, 0.000000, Amount) INTO retailAmount
		 FROM 
		 (
		 	SELECT sum(F_Amount) AS Amount
		 	FROM t_retailtrade
		 	WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0
		 	AND F_SourceID = -1
			AND F_ShopID = iShopID
		 ) AS tmp4;	
		 
		 -- ͳ���˻��ܶ�
		 SELECT IF(Amount IS NULL, 0.000000, Amount) INTO returnAmount
		 FROM 
		 (
		 	SELECT sum(F_Amount) AS Amount
		 	FROM t_retailtrade
		 	WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0
		 	AND F_SourceID <> -1
			AND F_ShopID = iShopID
		 	
		 ) AS tmp4;	
	
		-- ��������ܼ�,����Ʒδ��⣬������Ϊ0,������ = �ܽ����� - �˻�������
		SELECT IF(pricePurchaseSum IS NULL, 0.000000, pricePurchaseSum) INTO pricePurchase
		FROM (
		SELECT sum(totalPricePurchase) AS pricePurchaseSum FROM (	 
		   	SELECT rtc.F_CommodityID, rtcs.F_NO, wc.F_Price, rtcs.F_NO * wc.F_Price AS totalpricePurchase 
			FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
			WHERE rt.F_ID = rtc.F_TradeID AND 
					rt.F_ShopID = iShopID AND 
					(SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = rtc.F_CommodityID) <> 3 AND -- ������Ʒû�н�����
					rtc.F_ID = rtcs.F_RetailTradeCommodityID AND 
					w.F_ID = rtcs.F_WarehousingID  AND 
					w.F_ID = wc.F_WarehousingID AND
				    w.F_ShopID = iShopID AND
					wc.F_CommodityID = rtcs.F_ReducingCommodityID AND  
					datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtcs.F_ID
			) AS tmp5
		) AS tmp6;	 
		
		-- �����˻������ܼ�,����Ʒδ��⣬������Ϊ0
		SELECT IF(pricePurchaseSum IS NULL, 0.000000, pricePurchaseSum) INTO returnPricePurchase
		FROM (
		SELECT sum(totalPricePurchase) AS pricePurchaseSum FROM (	 
		   	SELECT rtc.F_CommodityID, rtcd.F_NO, wc.F_Price, rtcd.F_NO * wc.F_Price AS totalpricePurchase 
			FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
			WHERE rt.F_ID = rtc.F_TradeID AND 
					rt.F_ShopID = iShopID AND 
					(SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = rtc.F_CommodityID) <> 3 AND -- ������Ʒû�н�����
					rtc.F_ID = rtcd.F_RetailTradeCommodityID AND 
					w.F_ID = rtcd.F_WarehousingID  AND 
					w.F_ID = wc.F_WarehousingID AND
					w.F_ShopID = iShopID AND 
					wc.F_CommodityID = rtcd.F_IncreasingCommodityID AND  
					datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtcd.F_ID
			) AS tmp5
		) AS tmp6;	
		
		-- case1:��������۳���
		-- case2:�������۳����˻�,�۳��ܽ����۴����˻��ܽ�����
		-- case3:�������۳����˻�,�۳��ܽ�����С���˻��ܽ�����
		-- case4:�������۳����˻�,�۳��ܽ����۵����˻��ܽ�����
		-- case5:����ֻ���˻�
		-- ����case���м����ܽ����۷�ʽ��Ϊ  �۳��ܽ����ۼ�ȥ�˻��ܽ�����
		-- ���㵱�������Ľ����ܼ�,������ļ۸�Ϊ����ʱ,��ʱ�Ͳ���˵���ܽ�����
		SET pricePurchase = pricePurchase - returnPricePurchase;
		-- ...���㹺�����ߵĿͻ�(δ�о���������ʱ����)
		
		 -- �������۶�  �������۶� = �����ܶ� - �˻��ܶ�
	    SET totalAmount = retailAmount - returnAmount;
	    
	    -- ��������ë��  ë�� = ���۶� - �����ܼ�
	    SET totalGross = totalAmount - pricePurchase;
	  	
	    -- ��������ë����  ë���� = ë�� / ���۶�
	    SET ratioGrossMargin = IF(totalAmount = 0, 0, totalGross / totalAmount);
	    
	    -- ����͵���   �͵��� = ���۶� / ���۱���	
		SET averageAmountOfCustomer = IF(totalNO = 0, 0, totalAmount / totalNO);
		
	    -- SELECT totalNO, totalAmount, averageAmountOfCustomer, totalGross, ratioGrossMargin, pricePurchase, topSaleCommodityID, topSaleCommodityName, topSaleCommodityAmount, topSaleCommodityNO;
	
		-- ����ʱ���ʽ�������뵽���ݿⲻ��Ҫʱ����
		SET dtSaleDate = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d');
		
	 	 IF ratioGrossMargin <> 0 THEN 
			-- �������ڲ�ѯ����������۶����Ʒ����
			SELECT topSaleCommodityName;

			-- ���۵��ձ����ܱ�
			INSERT INTO t_retailtradedailyreportsummary (
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO,
				F_TopSaleCommodityAmount)
			VALUES (
				iShopID,
				dtSaleDate, 
				totalNO, 
				pricePurchase, 
				totalAmount, 
				averageAmountOfCustomer, 
				totalGross, 
				ratioGrossMargin, 
				topSaleCommodityID, 
				(CASE WHEN topSaleCommodityID IS NULL THEN 0 ELSE topSaleCommodityNO END),
				(CASE WHEN topSaleCommodityID IS NULL THEN 0 ELSE topSaleCommodityAmount END)
				);

			SELECT F_ID, 
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO, 
				F_TopSaleCommodityAmount, 
				F_TopPurchaseCustomerName, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportsummary
			WHERE F_ID = last_insert_id() 
			AND F_ShopID = iShopID;
			
			-- 	���۵��ձ���Ʒ��
			OPEN  list;  
		    	read_loop: LOOP	  
		    FETCH list INTO commID, commNO, commodityAmount;   
		    IF done THEN  
		        LEAVE read_loop;   
		    END IF;
		    	SELECT F_Type INTO type FROM t_commodity WHERE F_ID = commID;
		    	-- ���¼�����������Ժϲ�Ϊһ����� ��ֻ��Ҫ����Դ����������������ܼ�
		    	IF type = 2 THEN
		    		-- ��Ʒ�����ܼ� = �ܽ����� - �˻���Ʒ�����ܼ�(��������ѯ��䲻�ܺϲ�����Ϊ�п���û���˻���Ϣ���ͻᵼ�����ռ�����Ϊ0)
		    		SELECT ifnull(sum(totalpricePurchase), 0.000000) INTO commodityPricePurchase
						FROM
						(
							SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
							FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
							WHERE rt.F_ID = rtc.F_TradeID 
							AND rt.F_SourceID = -1
							AND rt.F_ShopID = iShopID 
							AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
							AND w.F_ID = rtcs.F_WarehousingID  
							AND w.F_ID = wc.F_WarehousingID 
							AND rtc.F_CommodityID = commID 
							AND wc.F_CommodityID = rtcs.F_ReducingCommodityID
							AND w.F_ShopID = iShopID
							AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
						) AS tmp7;
						
					SELECT ifnull(sum(returnTotalpricePurchase), 0.000000) INTO returnPricePurchase
						FROM	
						(
							SELECT rtcd.F_NO * wc.F_Price AS returnTotalpricePurchase -- ����ʹ��ifnullû�ã���Ϊ������ǿգ�����null��Ҳ��������ֵ
							FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
							WHERE rt.F_ID = rtc.F_TradeID 
							AND rt.F_SourceID > 0
							AND rt.F_ShopID = iShopID
							AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
							AND w.F_ID = rtcd.F_WarehousingID  
							AND w.F_ID = wc.F_WarehousingID 
							AND w.F_ShopID = iShopID
							AND rtc.F_CommodityID = commID
							AND wc.F_CommodityID = rtcd.F_IncreasingCommodityID
							AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
						)AS tmp8;		   
				ELSEIF type = 1 THEN
					SELECT ifnull(sum(totalpricePurchase),0.000000) INTO commodityPricePurchase
					FROM
					( 
						SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND rt.F_SourceID = -1
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
						AND rtc.F_CommodityID = commID
						AND w.F_ID = rtcs.F_WarehousingID
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND wc.F_CommodityID = rtcs.F_ReducingCommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
					) AS tmp7;

					SELECT ifnull(sum(totalpricePurchase),0.000000) INTO returnPricePurchase 
					FROM
					( 
						SELECT rtcd.F_NO * wc.F_Price AS totalpricePurchase	-- ����ʹ��ifnullû�ã���Ϊ������ǿգ�����null��Ҳ��������ֵ
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND rt.F_SourceID > 0
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
						AND rtc.F_CommodityID = commID
						AND w.F_ID = rtcd.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND wc.F_CommodityID = rtcd.F_IncreasingCommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
					)AS tmp8; 				
				ELSE
			    	SELECT ifnull(sum(totalpricePurchase), 0.000000) INTO commodityPricePurchase
					FROM
					(
						SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND (SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = commID) <> 3
						AND rt.F_SourceID = -1
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
						AND w.F_ID = rtcs.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND rtc.F_CommodityID = commID
						AND rtc.F_CommodityID = wc.F_CommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
					) AS tmp7;
					
					SELECT ifnull(sum(returnTotalpricePurchase), 0.000000) INTO returnPricePurchase
					FROM
					(
						SELECT rtcd.F_NO * wc.F_Price AS returnTotalpricePurchase	-- ����ʹ��ifnullû�ã���Ϊ������ǿգ�����null��Ҳ��������ֵ
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND (SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = commID) <> 3
						AND rt.F_SourceID > 0
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
						AND w.F_ID = rtcd.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND rtc.F_CommodityID = commID
						AND rtc.F_CommodityID = wc.F_CommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
					)AS tmp8;
				END IF;
				
				SET commodityPricePurchase = commodityPricePurchase - returnPricePurchase;	
				
				INSERT INTO t_retailtradedailyreportbycommodity (
					F_ShopID,
					F_Datetime, 
					F_CommodityID, 
					F_NO, 
					F_TotalPurchasingAmount, 
					F_TotalAmount, 
					F_GrossMargin)
				VALUES (
					iShopID,
					dtSaleDate, 
					commID, 
					commNO, 
					commodityPricePurchase, 
					commodityAmount, 
					commodityAmount - commodityPricePurchase
					);
			END LOOP read_loop;
			 
	
	   		CLOSE list;
		
			SELECT 
				F_ID, 
				F_ShopID,
				F_Datetime, 
				F_CommodityID, 
				F_NO, 
				F_TotalPurchasingAmount,
				F_TotalAmount, 
				F_GrossMargin, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportbycommodity
			WHERE F_Datetime = dtSaleDate 
			AND F_ShopID = iShopID;
		 ELSE
		 	SELECT topSaleCommodityName;
		  	-- ���۵��ձ����ܱ�
			INSERT INTO t_retailtradedailyreportsummary (
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO,
				F_TopSaleCommodityAmount)
			VALUES (
				iShopID,
				dtSaleDate, 
				0, 
				0, 
				0, 
				0, 
				0, 
				0, 
				NULL, 
				0,
				0
				);

			SELECT F_ID, 
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO, 
				F_TopSaleCommodityAmount, 
				F_TopPurchaseCustomerName, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportsummary
			WHERE F_ID = last_insert_id() 
			AND F_ShopID = iShopID; 
		END IF; 	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;