DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByStaff_Create`;
-- ͳ��Staff������ҵ��
-- Staff������ҵ��= �����ܶ� - �˻��� 
-- �˻��ۼ���: ��ͳ���˻���ʱ����Ҫ�鿴Դ�������۵����Ĳ����˴�ʱ�����ϰࡣ�����ϰ࣬����Դ���Ĳ�����ͷ�ϣ�û���ϰ࣬�����˻����Ĳ�����ͷ�ϡ�
-- ������
-- 1.��A,B���ϰ࣬A����һ�٣�B�˻�A�����۵�50����ʱA�ϰ࣬�˻��Ľ������Aͷ��,A��ҵ����50 
-- 2.��Aû���ϰ࣬B�ϰ࣬B��A��ҵ���˿�50Ԫ����ʱAδ�ϰ࣬�˻��Ľ������Bͷ��,B��ҵ����-50
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByStaff_Create`(
	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),	 
   	IN iShopID INT,
   	IN dSaleDatetime DATETIME,		-- ��������
   	IN iDeleteOldData INT
)
BEGIN 
	DECLARE iTotalNO INT;   						 	-- �����ܱ���
	DECLARE dTotalGross DECIMAL(20, 6); 			 	-- ������ë��
	DECLARE dTotalAmountWithReturned DECIMAL(20, 6);  	-- δ�����˻��������ܶ�
	DECLARE dTotalAmountWithoutReturned DECIMAL(20, 6); -- �ѳ��˻����������ܶ�
	DECLARE iStaffID INT; 		 						-- Ա��ID
	DECLARE dTotalPurchasingPrice DECIMAL(20, 6); 		-- iStaffID������Ʒ���ܽ�����
	DECLARE amountReturnMine DECIMAL(20, 6);			-- ������iStaffID�ĵ���iStaffID���ϰ�(����������iStaffID�ĵ���iStaffID���Լ��ĵ�)
	DECLARE amountReturnOthers DECIMAL(20, 6);			-- iStaffID�˱��˵ĵ�������û�ϰࡣA��C�ģ�������B��C��
	
	DECLARE dReturnTotalPrice DECIMAL(20, 6);		-- �˻��ܳɱ�
	DECLARE dSaleGross DECIMAL(20, 6);				-- ����ë��
	DECLARE dReturnGross DECIMAL(20, 6);			-- �˻�ë��
 
	DECLARE done INT DEFAULT false;
	
	-- �������������ó������ϰ��Ա��
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;

   	START TRANSACTION;
   		-- �����ڲ��ԣ���֤����ͨ��
		IF iDeleteOldData = 1 THEN 
		   DELETE FROM T_RetailTradeDailyReportByStaff WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		END IF;
   	
		-- 	���۵�Ա��ҵ����
		OPEN listStaff;  
			read_loop: LOOP	  
		   		FETCH listStaff INTO iStaffID;   
				IF done THEN  
					   LEAVE read_loop;   
				END IF;
		
				-- ����iStaffIDĳ�죨dSaleDatetime�������۱���
				SELECT ifnull(sum(F_Amount), 0.000000), count(1) INTO dTotalAmountWithReturned, iTotalNO
				FROM t_retailtrade 
				WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 
				AND F_SourceID = -1 -- -1������Ƿ��˻������۵���>0����������˻������۵���
				AND F_ShopID = iShopID
				AND F_StaffID = iStaffID;
				
				-- ҵ����Ҫ����iStaffIDͷ�ϵ��˻���
				-- 1.������iStaffID�ĵ���iStaffID���ϰ�(����������iStaffID�ĵ���iStaffID���Լ��ĵ�)
				SELECT ifnull(sum(rtt.F_Amount), 0.000000) INTO amountReturnMine
				FROM t_retailtrade rtt -- rtt�Ǳ�����dSaleDatetime�˵ĵ�
				WHERE rtt.F_SourceID IN (
					SELECT F_ID FROM t_retailtrade WHERE F_StaffID = iStaffID -- ������ҵĵ��˵�
				)
				AND rtt.F_ShopID = iShopID
				AND datediff(rtt.F_SaleDatetime, dSaleDatetime) = 0
				AND EXISTS ( -- �����ϰ�
					SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
					WHERE rt.F_StaffID = rg.F_StaffID
						AND rt.F_ID = rtt.F_SourceID
						AND rt.F_ShopID = iShopID
						AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd
					    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0   -- iStaffID�������ϰ�
				);
 
				-- 2.iStaffID�˱��˵ĵ�������û�ϰࡣA��C�ģ�������B��C��
			    SELECT ifnull(sum(F_Amount), 0.000000) INTO amountReturnOthers
			    FROM t_retailtrade rtt
			    WHERE rtt.F_StaffID = iStaffID -- �����˵�
				    AND rtt.F_SourceID IN (
					    SELECT F_ID FROM t_retailtrade WHERE F_StaffID <> iStaffID
					)
					AND rtt.F_ShopID = iShopID
					AND datediff(rtt.F_SaleDatetime, dSaleDatetime) = 0 -- ���ҽ����˵�
					-- Դ���Ĳ�����û�ϰ�
					AND NOT EXISTS (
						SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
						WHERE rt.F_StaffID = rg.F_StaffID
							AND rt.F_ID = rtt.F_SourceID
							AND rt.F_ShopID = iShopID
							AND rt.F_StaffID <> iStaffID 
							AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd -- �˻���ʱ�䣬�ڱ����ϰ������֮��
						    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0 -- dSaleDatetime�������ϰ�
					);

				-- ���������ܶ�
				SET dTotalAmountWithoutReturned = dTotalAmountWithReturned - amountReturnMine - amountReturnOthers;
			
				-- ����iStaffID�������۵���Ʒ���ܳɱ�
				SELECT ifnull(sum(ifnull(src.F_NO, 0) * 
					ifnull((SELECT wshc.F_Price FROM t_warehousingcommodity wshc WHERE wshc.F_WarehousingID = src.F_WarehousingID AND wshc.F_CommodityID = src.F_ReducingCommodityID), 0.000000)), 0.000000) INTO dTotalPurchasingPrice
				FROM t_retailtradecommoditysource AS src
				WHERE src.F_RetailTradeCommodityID IN (
						SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
							SELECT rt.F_ID FROM t_retailtrade rt WHERE rt.F_StaffID = iStaffID AND rt.F_ShopID = iShopID AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 AND rt.F_SourceID = -1
						)
				);
				
				-- ������iStaffID�Ļ����ܳɱ�
				SELECT ifnull(sum(ifnull(dest.F_NO, 0) * 
					ifnull((SELECT wshc.F_Price FROM t_warehousingcommodity wshc WHERE wshc.F_WarehousingID = dest.F_WarehousingID AND wshc.F_CommodityID = dest.F_IncreasingCommodityID), 0.000000)), 0.000000) INTO dReturnTotalPrice
				FROM t_returnretailtradecommoditydestination AS dest  -- �����˻�����Ʒ���������ȥ�����
				WHERE dest.F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID IN (
							SELECT F_ID FROM t_retailtrade WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 AND F_ShopID = iShopID AND F_SourceID > 0 -- �˻��͵����۵�������dSaleDatetime�˵�
						)
				)
				AND (
						(
							dest.F_RetailTradeCommodityID IN (
								SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
									SELECT rtt.F_ID 			   -- �˻���
									FROM t_retailtrade rtt -- rtt�Ǳ�����dSaleDatetime�˵ĵ�
									WHERE rtt.F_SourceID IN (
										SELECT F_ID FROM t_retailtrade WHERE F_StaffID = iStaffID -- ������ҵĵ��˵�
									)
									AND rtt.F_ShopID = iShopID
									AND EXISTS ( -- �����ϰ�
										SELECT 1 FROM t_retailtrade rt, t_retailtradeaggregation rg
										WHERE rt.F_StaffID = rg.F_StaffID
											AND rt.F_ID = rtt.F_SourceID
											AND rt.F_ShopID = iShopID
											AND rtt.F_SaleDatetime BETWEEN rg.F_WorkTimeStart AND rg.F_WorkTimeEnd
										    AND datediff(rg.F_WorkTimeStart, dSaleDatetime) = 0   -- iStaffID�������ϰ�
									)
								 )	
							)
							
						)
						OR 
						(
							dest.F_RetailTradeCommodityID IN (
								-- 2.iStaffID�˱��˵ĵ�������û�ϰ�
								SELECT rtc.F_ID FROM t_retailtradecommodity rtc WHERE rtc.F_TradeID IN (
									SELECT rt.F_ID FROM t_retailtrade rt -- rt��iStaffID��dSaleDatetime���˻���
									WHERE rt.F_StaffID = iStaffID  -- �����˵�
										AND rt.F_ShopID = iShopID
										AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 -- ����dSaleDatetime�˵�
										AND rt.F_SourceID IN (
											SELECT rtOthers.F_ID FROM t_retailtrade rtOthers 
											WHERE rtOthers.F_StaffID <> iStaffID
												AND rtOthers.F_ShopID = iShopID
												AND NOT EXISTS (
											 		SELECT 1 FROM t_retailtradeaggregation agg WHERE agg.F_StaffID = rtOthers.F_StaffID
														AND datediff(agg.F_WorkTimeStart, dSaleDatetime) = 0 -- �������ϰ�
														AND rt.F_SaleDatetime BETWEEN agg.F_WorkTimeStart AND agg.F_WorkTimeEnd -- �˻���ʱ���ڱ��˵��ϰࡢ�°�ʱ���м�
														
												)
										)
								)
							)
						)
				);
				
				-- ����ë�� = �����ܶ� - �����ܳɱ�
				SET dSaleGross = dTotalAmountWithReturned - dTotalPurchasingPrice;
				
				-- �˻�ë�� = �˻��ܶ� - �˻��ܳɱ�   
				SET dReturnGross = (amountReturnMine + amountReturnOthers) - dReturnTotalPrice;	
				
				-- ë�� = ����ë�� - �˻�ë��  
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