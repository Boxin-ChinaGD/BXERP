SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case6.sql+++++++++++++++++++++++';

SELECT '-----------------Case6:���˱��˵ĵ������˲����ϰ�(Ա��9����8�ŵĵ���8�Ŵ�ʱ�����ϰ�) ------------------' AS 'Case6';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '���罣', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-23 10:01:33', '2019-12-23 10:07:37', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '���ǽ�', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-23 10:01:33', '2019-12-23 10:07:37', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
--
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '���ƽ�', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-23 10:01:33', '2019-12-23 10:07:37', NULL, NULL, NULL, NULL);
SET @iCommodityID3 = last_insert_id();
-- ������ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK190003010001', 1, 1, 4, 4, '2000-01-01 13:30:00', NULL, '2000-01-01 13:30:00');
SET @iWarehousingID = last_insert_id();
-- ������ⵥ��Ʒ
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID1, 100, 1, '���罣', 13, 1.7, 100, '2017-10-06 01:01:01', 36, '2020-10-06 01:01:01', '2019-12-23 10:01:35', '2019-12-23 10:01:35', 100);
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID2, 100, 1, '���ǽ�', 13, 1.4, 100, '2017-10-06 01:01:01', 36, '2020-10-06 01:01:01', '2019-12-23 10:01:35', '2019-12-23 10:01:35', 100);
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID3, 100, 1, '���ƽ�', 13, 1.2, 100, '2017-10-06 01:01:01', 36, '2020-10-06 01:01:01', '2019-12-23 10:01:35', '2019-12-23 10:01:35', 100);
-- ���۵�
SET @rt1CommotidyNO1_A = 6;
SET @rt1CommotidyPrice1_A = 18.320000;
SET @rt1CommotidyNO2_A = 8;
SET @rt1CommotidyPrice2_A = 61.590000;
SET @rt1CommotidyNO3_A = 15;
SET @rt1CommotidyPrice3_A = 34.490000;
SET @rt1AmuontCash_A = 1119.990000; -- 
SET @rt1AmuontWeChat_A = 0.000000;
SET @rt1AmuontAliPay_A = 0.000000;
SET @rt1TotalAmount_A = @rt1CommotidyNO1_A * @rt1CommotidyPrice1_A + @rt1CommotidyNO2_A * @rt1CommotidyPrice2_A + @rt1CommotidyNO3_A * @rt1CommotidyPrice3_A;
-- 
SET @rt2CommotidyNO1_A = 9;
SET @rt2CommotidyPrice1_A = 18.320000;
SET @rt2CommotidyNO2_A = 7;
SET @rt2CommotidyPrice2_A = 61.590000;
SET @rt2CommotidyNO3_A = 2;
SET @rt2CommotidyPrice3_A = 34.490000;
SET @rt2AmuontCash_A = 0.00000;
SET @rt2AmuontWeChat_A = 664.990000; -- 
SET @rt2AmuontAliPay_A = 0.000000;
SET @rt2TotalAmount_A = @rt2CommotidyNO1_A * @rt2CommotidyPrice1_A + @rt2CommotidyNO2_A * @rt2CommotidyPrice2_A + @rt2CommotidyNO3_A * @rt2CommotidyPrice3_A;
-- �˻���(�����۵�1)
SET @rrt1CommotidyNO_B = 6;
SET @rrt1CommotidyPrice_B = 34.490000;
SET @rrt1AmuontCash_B = 206.940000;
SET @rrt1AmuontWeChat_B = 0.000000;
SET @rrt1AmuontAliPay_B = 0.000000;
SET @rrt1TotalAmount_B = @rrt1CommotidyNO_B * @rrt1CommotidyPrice_B;
-- ���۵���Ʒ��Դ��
SET @commodityID216 = @iCommodityID1;
SET @commodityID217 = @iCommodityID2;
SET @commodityID218 = @iCommodityID3;
SET @rtcs1NO_A = 6;
SET @rtcs1WarehousingID_A = @iWarehousingID;
SET @rtcs2NO_A = 8;
SET @rtcs2WarehousingID_A = @iWarehousingID;
SET @rtcs3NO_A= 9;
SET @rtcs3WarehousingID_A = @iWarehousingID;
SET @rtcs4NO_A = 9;
SET @rtcs4WarehousingID_A = @iWarehousingID;
SET @rtcs5NO_A = 7;
SET @rtcs5WarehousingID_A = @iWarehousingID;
SET @rtcs6NO_A = 2;
SET @rtcs6WarehousingID_A = @iWarehousingID;
-- ��Ʒȥ���
SET @destinationCommodityID218 = @iCommodityID3;
SET @rtcd1NO_B = 6;
SET @rtcd1WarehousingID_B = @iWarehousingID;
-- ���۵���������
SET @rggWorkTimeStart_A='2130-01-01 00:00:00';
SET @rggWorkTimeEnd_A='2130-01-01 4:00:00';
SET @rggTotalAmount_A = @rt1TotalAmount_A + @rt2TotalAmount_A; 
SET @rggTotalAmountCash_A = @rt1AmuontCash_A + @rt2AmuontCash_A; 
SET @rggTotalAmountWeChat_A = @rt1AmuontWeChat_A + @rt2AmuontWeChat_A;
SET @rggTotalAmountAliPay_A = @rt1AmuontAliPay_A + @rt2AmuontAliPay_A;
SET @rggNO_A = 8; 
-- 
SET @rggWorkTimeStart_B='2130-01-01 04:00:00';
SET @rggWorkTimeEnd_B='2130-01-01 23:59:59';
SET @rggTotalAmount_B = 0.000000; 
SET @rggTotalAmountCash_B = 0.000000;
SET @rggTotalAmountWeChat_B = 0.000000;
SET @rggTotalAmountAliPay_B = 0.000000;
SET @rggNO_B = 0;
-- ����Ա
SET @iStaffIDA = 8;
SET @iStaffIDB = 9;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffIDA, 5, @rggWorkTimeStart_A, @rggWorkTimeEnd_A,@rggNO_A, @rggTotalAmount_A, 500, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A,0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_A = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffIDB, 5, @rggWorkTimeStart_B, @rggWorkTimeEnd_B,@rggNO_B, @rggTotalAmount_B, 500, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_B = last_insert_id();

SET @saleDatetime = '2130-01-01 17:42:31';
SET @syncDatetime = '2130-01-01 17:42:31';
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2130010100000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffIDA,1,'0',1,'��',-1,@syncDatetime,@rt1TotalAmount_A,@rt1AmuontCash_A,@rt1AmuontWeChat_A,@rt1AmuontAliPay_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1_A = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2130010100000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffIDB,1,'0',1,'��',@rtID1_A,@syncDatetime,@rrt1TotalAmount_B,@rrt1AmuontCash_B,@rrt1AmuontWeChat_B,@rrt1AmuontAliPay_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2130010100000500010002',11070,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffIDA,4,'0',1,'��',-1,@syncDatetime,@rt2TotalAmount_A,@rt2AmuontCash_A,@rt2AmuontAliPay_A,@rt2AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID2_A = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_A,@iCommodityID1,'���罣',245,@rt1CommotidyNO1_A,18.320000,6,@rt1CommotidyPrice1_A,NULL,18.320000);
SET @rtCommID1_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_A,@iCommodityID2,'���ǽ�',246,@rt1CommotidyNO2_A,61.590000,8,@rt1CommotidyPrice2_A,NULL,61.590000);
SET @rtCommID2_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_A,@iCommodityID3,'���ƽ�',247,@rt1CommotidyNO3_A,34.490000,9,@rt1CommotidyPrice3_A,NULL,34.490000);
SET @rtCommID3_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_B,@iCommodityID3,'���ƽ�',247,@rrt1CommotidyNO_B,34.490000,0,@rrt1CommotidyPrice_B,NULL,34.490000);
SET @rrtCommID_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID2_A,@iCommodityID1,'���罣',245,@rt2CommotidyNO1_A,18.320000,9,@rt2CommotidyPrice1_A,NULL,18.320000);
SET @rtCommID4_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID2_A,@iCommodityID2,'���ǽ�',246,@rt2CommotidyNO2_A,61.590000,0,@rt2CommotidyPrice2_A,NULL,61.590000);
SET @rtCommID5_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID2_A,@iCommodityID3,'���ƽ�',247,@rt2CommotidyNO3_A,34.490000,2,@rt2CommotidyPrice3_A,NULL,34.490000);
SET @rtCommID6_A = last_insert_id();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1_A,@commodityID216,@rtcs1NO_A,@rtcs1WarehousingID_A);
SET @rtCommSourceID1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2_A,@commodityID217,@rtcs2NO_A,@rtcs2WarehousingID_A);
SET @rtCommSourceID2_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3_A,@commodityID218,@rtcs3NO_A,@rtcs3WarehousingID_A);
SET @rtCommSourceID3_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID4_A,@commodityID216,@rtcs4NO_A,@rtcs4WarehousingID_A);
SET @rtCommSourceID4_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID5_A,@commodityID217,@rtcs5NO_A,@rtcs5WarehousingID_A);
SET @rtCommSourceID5_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID6_A,@commodityID218,@rtcs6NO_A,@rtcs6WarehousingID_A);
SET @rtCommSourceID6_A = last_insert_id();

-- �����˻���Ʒȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID_B, @destinationCommodityID218, @rtcd1NO_B, @rtcd1WarehousingID_B);
SET @rtcd_B = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2130-01-01 0:00:00';
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- ����ҵ����������ݽ����֤

-- �����ܽ��
SET @saleTotalAmount_A = (@rt1CommotidyNO1_A * @rt1CommotidyPrice1_A + @rt1CommotidyNO2_A * @rt1CommotidyPrice2_A + @rt1CommotidyNO3_A * @rt1CommotidyPrice3_A)
						+ (@rt2CommotidyNO1_A * @rt2CommotidyPrice1_A + @rt2CommotidyNO2_A * @rt2CommotidyPrice2_A + @rt2CommotidyNO3_A * @rt2CommotidyPrice3_A);
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_A = (@rtcs1NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs1WarehousingID_A AND F_CommodityID = @commodityID216))
					 + (@rtcs2NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID_A AND F_CommodityID = @commodityID217))
					 + (@rtcs3NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs3WarehousingID_A AND F_CommodityID = @commodityID218))
					 + (@rtcs4NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs4WarehousingID_A AND F_CommodityID = @commodityID216))
					 + (@rtcs5NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs5WarehousingID_A AND F_CommodityID = @commodityID217))
					 + (@rtcs6NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs6WarehousingID_A AND F_CommodityID = @commodityID218));
-- ������Ʒ��ë��
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- �˻��ܽ��
SET @returnTotalAmount_A = 0.000000;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_A = 0.000000;
-- �˻���Ʒ��ë��
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification1=0;
-- ���е�Ա8ҵ����������ݽ����֤
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffIDA 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_A
AND F_GrossMargin = @grossMargin_A
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffIDA
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'���Գɹ�','����ʧ��') AS 'Test Case6 Result';


-- �����ܽ��
SET @saleTotalAmount_B = 0.000000;
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_B = 0.000000;
-- ������Ʒ��ë��
SET @saleTotalMargin_B = @saleTotalAmount_B - @saleTotalCost_B;
-- �˻��ܽ��
SET @returnTotalAmount_B = (@rrt1CommotidyNO_B * @rrt1CommotidyPrice_B);
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_B = (@rtcd1NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID_B AND F_CommodityID = @destinationCommodityID218));
-- �˻���Ʒ��ë��
SET @returnTotalMargin_B = @returnTotalAmount_B - @returnTotalCost_B;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount_B = @saleTotalAmount_B - @returnTotalAmount_B;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin_B = @saleTotalMargin_B - @returnTotalMargin_B;

SET @ResultVerification2=0;
-- ���е�Ա9ҵ����������ݽ����֤
SELECT 1 INTO @ResultVerification2
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffIDB 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_B
AND F_GrossMargin = @grossMargin_B
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffIDB
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification2 = 1,'���Գɹ�','����ʧ��') AS 'Test Case6 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID2_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID3_A;
-- DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rrtCommSourceID_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID4_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID5_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID6_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID3_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID4_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID5_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID6_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2_A;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID1,@iCommodityID2,@iCommodityID3);