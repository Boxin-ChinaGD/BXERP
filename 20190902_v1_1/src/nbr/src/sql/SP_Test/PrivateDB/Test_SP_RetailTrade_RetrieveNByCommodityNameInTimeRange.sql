SELECT '++++++++++++++++++ Test_SP_RetailTrade_RetrieveNByCommodityNameInTimeRange.sql ++++++++++++++++++++';


SELECT '------------- Case1:正常查询，根据商品关键字查询零售单，只有普通商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case1';
SET @dtStartDate = now();

-- 
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
-- 零售单主表1
-- 零售单商品1
SET @retailTradeCommodity1NO = 12;
SET @retailTradeCommodity1Price = 22;
-- 
-- 销售数量
SET @sumCommNO = @retailTradeCommodity1NO;
-- 销售总额
SET @sumAmount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;
-- 销售成本
SET @totalpurchasingPrice = @retailTradeCommodity1NO * @warehousingCommPrice;
-- 销售毛利
SET @totalGross = @sumAmount - @totalpurchasingPrice;
-- 

-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '贝贝普通商品', '贝贝普通商品', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO/*F_NO*/, 1, '贝贝普通商品', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '贝贝普通商品', @barcodeID, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
set @retailTradeCommodityID = last_insert_id();
-- 零售来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID, @retailTradeCommodity1NO, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "贝贝普通商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType, @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;



SELECT IF(@iTotalRecord = 1, '测试成功', '测试失败') AS 'CASE1.1 Testing Result';
SELECT IF(@dRetailAmount = @sumAmount, '测试成功', '测试失败') AS 'CASE1.2 Testing Result';
SELECT IF(@iTotalCommNO = @sumCommNO, '测试成功', '测试失败') AS 'CASE1.3 Testing Result';
SELECT IF(@dTotalGross = @totalGross, '测试成功', '测试失败') AS 'CASE1.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '------------- Case2:不存在的商品关键字，根据商品关键字查询零售单，只有普通商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case2';
SET @sumCommNO = 10;
SET @sumAmount = 200;
SET @totalGross = 89.0;
SET @dtStartDate = now();
-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '贝贝普通商品', '贝贝普通商品', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, 50, 1, '贝贝普通商品', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '贝贝普通商品', @barcodeID, @sumCommNO/*F_NO*/, 20, 10, 20, 10, 10);
set @retailTradeCommodityID = last_insert_id();
-- 零售来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID, 10, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "1贝贝普通商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;



SELECT IF(@iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE2.1 Testing Result';
SELECT IF(@dRetailAmount = 0, '测试成功', '测试失败') AS 'CASE2.2 Testing Result';
SELECT IF(@iTotalCommNO = 0, '测试成功', '测试失败') AS 'CASE2.3 Testing Result';
SELECT IF(@dTotalGross = 0, '测试成功', '测试失败') AS 'CASE2.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '------------- Case3:正常查询，根据商品关键字查询零售单，只有多包装商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case3';
SET @dtStartDate = now();
-- 
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
-- 参照商品倍数
SET @refCommodityMultiple = 8;
-- 零售单主表1
-- 零售单商品1
SET @retailTradeCommodity1NO = 12;
SET @retailTradeCommodity1Price = 22;
-- 
-- 销售数量
SET @sumCommNO = @retailTradeCommodity1NO;
-- 销售总额
SET @sumAmount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;
-- 销售成本
SET @totalpurchasingPrice = @retailTradeCommodity1NO * @warehousingCommPrice * @refCommodityMultiple;
-- 销售毛利
SET @totalGross = @sumAmount - @totalpurchasingPrice;
-- 

-- 创建商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '贝贝普通商品', '贝贝普通商品', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '晶晶多包装', '晶晶多包装', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894546', now(), now());
SET @barcodeID2 = last_insert_id();

-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO/*F_NO*/, 1, '贝贝普通商品', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID2/**/, '晶晶多包装', @barcodeID2/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
set @retailTradeCommodityID = last_insert_id();
-- 零售来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity1NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "晶晶多包装";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;



SELECT IF(@iTotalRecord = 1, '测试成功', '测试失败') AS 'CASE3.1 Testing Result';
SELECT IF(@dRetailAmount = @sumAmount, '测试成功', '测试失败') AS 'CASE3.2 Testing Result';
SELECT IF(@iTotalCommNO = @sumCommNO, '测试成功', '测试失败') AS 'CASE3.3 Testing Result';
SELECT IF(@dTotalGross = @totalGross, '测试成功', '测试失败') AS 'CASE3.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case4:正常查询，根据商品关键字查询零售单，只有组合商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case4';
SET @dtStartDate = now();
-- 
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
-- 零售单主表1
-- 零售单商品1
SET @retailTradeCommodity1NO = 12;
SET @retailTradeCommodity1Price = 22;
-- 
-- 销售数量
SET @sumCommNO = @retailTradeCommodity1NO;
-- 销售总额
SET @sumAmount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;
-- 销售成本
SET @totalpurchasingPrice = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
-- 销售毛利
SET @totalGross = @sumAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '贝贝普通商品2', '贝贝普通商品2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '贝贝普通商品2', '贝贝普通商品2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '欢欢组合', '薯片', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '贝贝普通商品', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '贝贝普通商品2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '欢欢组合', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 零售来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "欢欢组合";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;



SELECT IF(@iTotalRecord = 1, '测试成功', '测试失败') AS 'CASE4.1 Testing Result';
SELECT IF(@dRetailAmount = @sumAmount, '测试成功', '测试失败') AS 'CASE4.2 Testing Result';
SELECT IF(@iTotalCommNO = @sumCommNO, '测试成功', '测试失败') AS 'CASE4.3 Testing Result';
SELECT IF(@dTotalGross = @totalGross, '测试成功', '测试失败') AS 'CASE4.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '------------- Case5:正常查询，根据商品关键字查询零售单，只有服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case5';
--	SET @sumCommNO = 10;
--	SET @sumAmount = 200;
--	SET @totalGross = 200;
SET @dtStartDate = now();

-- 零售单主表1
-- 零售单商品1
SET @retailTradeCommodity1NO = 12;
SET @retailTradeCommodity1Price = 22;
-- 
-- 销售数量
SET @sumCommNO = @retailTradeCommodity1NO;
-- 销售总额
SET @sumAmount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;
-- 销售成本
SET @totalpurchasingPrice = 0;
-- 销售毛利
SET @totalGross = @sumAmount - @totalpurchasingPrice;
-- 

-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '迎迎7642', '薯片', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '迎迎7642', @barcodeID, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
set @retailTradeCommodityID = last_insert_id();
-- 零售来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID, @retailTradeCommodity1NO, NULL);
SET @retailTradeCommoditySourceID  = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "迎迎7642";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;



SELECT IF(@iTotalRecord = 1, '测试成功', '测试失败') AS 'CASE5.1 Testing Result';
SELECT IF(@dRetailAmount = @sumAmount, '测试成功', '测试失败') AS 'CASE5.2 Testing Result';
SELECT IF(@iTotalCommNO = @sumCommNO, '测试成功', '测试失败') AS 'CASE5.3 Testing Result';
SELECT IF(@dTotalGross = @totalGross, '测试成功', '测试失败') AS 'CASE5.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case6:正常查询，根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case6';

SET @dtStartDate = now();

-- 
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @retailTradeCommodity1Price = 20;
SET @RETAILTRADECOMMODITY1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;
-- 
-- 销售数量
SET @totalRetailTradeCommodityNO = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
-- 销售总额
SET @totalRetailTradeAmount = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
-- 销售成本
SET @totalpurchasingPrice = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 销售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, 5/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售商品
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();

-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 1, '测试成功', '测试失败') AS 'CASE6.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE6.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE6.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE6.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '------------- Case7: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case7';
SET @dtStartDate = now();

-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 2, '测试成功', '测试失败') AS 'CASE7.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE7.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE7.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE7.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;






SELECT '------------- Case8: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利,传入正确的零售单操作人 -------------' AS 'Case7';
SET @dtStartDate = now();

-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 3;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 2, '测试成功', '测试失败') AS 'CASE8.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE8.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE8.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE8.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case9: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 传入不存在的操作人 -------------' AS 'Case9';
SET @sumCommNO = 80;
SET @sumAmount = 800;
SET @totalGross = -842.0;
SET @dtStartDate = now();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
SET @subCommodityNO1 = 3;
SET @subCommodityNO2 = 2;
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodityNO1, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodityNO2, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, 5/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, 50, 1, '多商品单1', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, 50, 1, '多商品单2', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = -1;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE9.1 Testing Result';
SELECT IF(@dRetailAmount = 0, '测试成功', '测试失败') AS 'CASE9.2 Testing Result';
SELECT IF(@iTotalCommNO = 0, '测试成功', '测试失败') AS 'CASE9.3 Testing Result';
SELECT IF(@dTotalGross = 0, '测试成功', '测试失败') AS 'CASE9.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case10: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ，传入正确的零售单支付方式-------------' AS 'Case10';
SET @dtStartDate = now();

-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1,
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 1, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 1, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 1;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 2, '测试成功', '测试失败') AS 'CASE10.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE10.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE10.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE10.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;





SELECT '------------- Case11: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ，传入不存在的零售单支付方式-------------' AS 'Case11';
SET @sumCommNO = 80;
SET @sumAmount = 800;
SET @totalGross = -842.0;
SET @dtStartDate = now();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
SET @subCommodityNO1 = 3;
SET @subCommodityNO2 = 2;
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodityNO1, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodityNO2, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, 5/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, 50, 1, '多商品单1', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, 50, 1, '多商品单2', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 1, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 1, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 3;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE11.1 Testing Result';
SELECT IF(@dRetailAmount = 0, '测试成功', '测试失败') AS 'CASE11.2 Testing Result';
SELECT IF(@iTotalCommNO = 0, '测试成功', '测试失败') AS 'CASE11.3 Testing Result';
SELECT IF(@dTotalGross = 0, '测试成功', '测试失败') AS 'CASE11.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;





SELECT '------------- Case12: (2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利,传入不存在的时间段 -------------' AS 'Case12';
SET @sumCommNO = 80;
SET @sumAmount = 800;
SET @totalGross = -842.0;
SET @dtStartDate = '3019/1/15 17:45:31';
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
SET @subCommodityNO1 = 3;
SET @subCommodityNO2 = 2;
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodityNO1, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodityNO2, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, 5/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, 50, 1, '多商品单1', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, 50, 1, '多商品单2', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = '3019/1/15 18:45:31';
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE12.1 Testing Result';
SELECT IF(@dRetailAmount = 0, '测试成功', '测试失败') AS 'CASE12.2 Testing Result';
SELECT IF(@iTotalCommNO = 0, '测试成功', '测试失败') AS 'CASE12.3 Testing Result';
SELECT IF(@dTotalGross = 0, '测试成功', '测试失败') AS 'CASE12.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case13: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 -------------' AS 'Case13';
SET @dtStartDate = now();

-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 

-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();


-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 3, '测试成功', '测试失败') AS 'CASE13.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE13.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE13.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE13.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;












SELECT '------------- Case14: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 不存在的商品关键字 -------------' AS 'Case14';
SET @sumCommNO = 80;
SET @sumAmount = 800;
SET @totalGross = -842.0;
SET @dtStartDate = now();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
SET @subCommodityNO1 = 3;
SET @subCommodityNO2 = 2;
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodityNO1, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodityNO2, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, 5/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, 50, 1, '多商品单1', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, 50, 1, '多商品单2', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, 10, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, 10/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, 30/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, 20/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID5, 10, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();


-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "龙行龘龘";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE14.1 Testing Result';
SELECT IF(@dRetailAmount = 0, '测试成功', '测试失败') AS 'CASE14.2 Testing Result';
SELECT IF(@iTotalCommNO = 0, '测试成功', '测试失败') AS 'CASE14.3 Testing Result';
SELECT IF(@dTotalGross = 0, '测试成功', '测试失败') AS 'CASE14.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;






/*（ps：零售单售卖商品A10件、40元，售卖商品B20件、60元，那么搜索关键字商品A，结果应该是销售数量为10、销售总额为40元）*/
SELECT '------------- Case15: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ,有的零售商品符合传入的关键字，有的不符合关键字 -------------' AS 'Case15';
SET @dtStartDate = now();

-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 
-- 不匹配关键字的商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '不匹配', '不匹配', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID6 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID6, '3548293894545', now(), now());
SET @barcodeID6 = last_insert_id();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 入库商品
-- 入库单主表
INSERT INTO nbr.t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID6, 50, 1, '不匹配', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID3 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID5 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID5, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID6 = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID25 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID25, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID26 = last_insert_id();

-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 3, '测试成功', '测试失败') AS 'CASE15.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE15.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE15.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE15.4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID6;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID26;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID35;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID3;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '------------- Case16: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ,有的零售商品符合传入的关键字，有的不符合关键字，跳库，创建入库单1不审核、创建入库单2并审核 -------------' AS 'Case16';
SET @dtStartDate = now();
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 
-- 不匹配关键字的商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '不匹配', '不匹配', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID6 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID6, '3548293894545', now(), now());
SET @barcodeID6 = last_insert_id();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 
-- 跳库
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 'RK200003010044', 1, 1, 4, NULL, now(), NULL, now());
SET @warehousingID2 = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID, 50, 1, '多商品单1', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID21 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID2, 50, 1, '多商品单2', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID22 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID6, 50, 1, '不匹配', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID23 = last_insert_id();
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID6, 50, 1, '不匹配', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID3 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID5 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID5, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID6 = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID25 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID25, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID26 = last_insert_id();

-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 3, '测试成功', '测试失败') AS 'CASE16.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE16.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE16.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE16.4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID6;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID26;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID35;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID21;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID22;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID23;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '------------- Case17: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ,有的零售商品符合传入的关键字，有的不符合关键字,零售商品价格有小数 -------------' AS 'Case17';
SET @dtStartDate = now();
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20.11;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20.12;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20.13;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20.14;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20.15;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20.16;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20.17;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20.18;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 
-- 不匹配关键字的商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '不匹配', '不匹配', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID6 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID6, '3548293894545', now(), now());
SET @barcodeID6 = last_insert_id();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 
-- 跳库
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 'RK200003010044', 1, 1, 4, NULL, now(), NULL, now());
SET @warehousingID2 = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID, 50, 1, '多商品单1', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID21 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID2, 50, 1, '多商品单2', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID22 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID6, 50, 1, '不匹配', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID23 = last_insert_id();
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID6, 50, 1, '不匹配', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID3 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID5 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID5, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID6 = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID25 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID25, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID26 = last_insert_id();

-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @sumAmount/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 3, '测试成功', '测试失败') AS 'CASE17.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE17.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE17.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE17.4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID6;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID26;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID35;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID21;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID22;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID23;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;






SELECT '------------- Case18: (2个零售单 , 1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ,有的零售商品符合传入的关键字，有的不符合关键字，跨库，单品零售来源来自两张不同的入库单，入库价格不一样 -------------' AS 'Case18';
SET @dtStartDate = now();
SET @warehousingCommPrice2 = 22.2;
SET @warehousingCommNO2 = 1;
-- 入库单主表1
-- 入库单从表1
SET @warehousingCommNO = 8;
SET @warehousingCommPrice = 22.2;
SET @warehousingComm2NO = 9;
SET @warehousingComm2Price = 33.3;
-- 组合商品对应的子商品数量
SET @subCommodity1NO = 3;
SET @subCommodity2NO = 2;
SET @refCommodityMultiple = 5;
-- 零售单主表2
-- 零售单商品1
-- 服务
SET @retailTradeCommodity24NO = 10;
SET @retailTradeCommodity24Price = 20.11;
SET @purchasingPrice24 = 0;
SET @retailTrade24Amount = @retailTradeCommodity24NO * @retailTradeCommodity24Price;
-- 多包装，参照商品1
SET @retailTradeCommodity23NO = 10;
SET @retailTradeCommodity23Price = 20.12;
SET @purchasingPrice23 = @retailTradeCommodity23NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade23Amount = @retailTradeCommodity23NO * @retailTradeCommodity23Price;
-- 单品
SET @retailTradeCommodity22NO = 10;
SET @retailTradeCommodity22Price = 20.13;
SET @purchasingPrice22 = @retailTradeCommodity22NO * @warehousingCommPrice;
SET @retailTrade22Amount = @retailTradeCommodity22NO * @retailTradeCommodity22Price;
-- 组合
SET @retailTradeCommodity21NO = 10;
SET @retailTradeCommodity21Price = 20.14;
SET @purchasingPrice21 = @retailTradeCommodity21NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity21NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade21Amount = @retailTradeCommodity21NO * @retailTradeCommodity21Price;
SET @totalRetailTradeCommodityNO2 = @retailTradeCommodity21NO + @retailTradeCommodity22NO + @retailTradeCommodity23NO + @retailTradeCommodity24NO;
SET @totalRetailTradeAmount2 = @retailTrade21Amount + @retailTrade22Amount + @retailTrade23Amount + @retailTrade24Amount;
SET @totalpurchasingPrice2= @purchasingPrice21 + @purchasingPrice22 + @purchasingPrice23 + @purchasingPrice24;
-- 
-- 零售单主表1
-- 零售单商品1
-- 服务商品
SET @retailTradeCommodity4NO = 10;
SET @retailTradeCommodity4Price = 20.15;
SET @purchasingPrice4 = 0;
SET @retailTrade4Amount = @retailTradeCommodity4NO * @retailTradeCommodity4Price;
-- 多包装，参照商品1
SET @refCommodityMultiple = 5;
SET @retailTradeCommodity3NO = 10;
SET @retailTradeCommodity3Price = 20.16;
SET @purchasingPrice3 = @retailTradeCommodity3NO * @warehousingCommPrice * @refCommodityMultiple;
SET @retailTrade3Amount = @retailTradeCommodity3NO * @retailTradeCommodity3Price;
-- 单品1
SET @retailTradeCommodity2NO = 10;
SET @retailTradeCommodity2Price = 20.17;
SET @purchasingPrice2 = @retailTradeCommodity2NO * @warehousingCommPrice;
SET @retailTrade2Amount = @retailTradeCommodity2NO * @retailTradeCommodity2Price;
-- 组合
SET @retailTradeCommodity1Price = 20.18;
SET @retailTradeCommodity1NO = 10;
SET @purchasingPrice1 = @retailTradeCommodity1NO * @warehousingCommPrice * @subCommodity1NO + @retailTradeCommodity1NO * @warehousingComm2Price * @subCommodity2NO;
SET @retailTrade1Amount = @retailTradeCommodity1NO * @retailTradeCommodity1Price;

SET @totalRetailTradeCommodityNO1 = @retailTradeCommodity1NO + @retailTradeCommodity2NO + @retailTradeCommodity3NO + @retailTradeCommodity4NO;
SET @totalRetailTradeAmount1 = @retailTrade1Amount + @retailTrade2Amount + @retailTrade3Amount + @retailTrade4Amount;
SET @totalpurchasingPrice1 = @purchasingPrice1 + @purchasingPrice2 + @purchasingPrice3 + @purchasingPrice4;
-- 零售数量
SET @totalRetailTradeCommodityNO = @totalRetailTradeCommodityNO1 + @totalRetailTradeCommodityNO2;
-- 零售总额
SET @totalRetailTradeAmount = @totalRetailTradeAmount1 + @totalRetailTradeAmount2;
-- 成本
SET @totalpurchasingPrice = @totalpurchasingPrice1 + @totalpurchasingPrice2  + @warehousingCommPrice2 * @warehousingCommNO2 -  @warehousingCommPrice * @warehousingCommNO2;
-- 零售毛利
SET @totalRetailTradeGross = @totalRetailTradeAmount - @totalpurchasingPrice;
-- 
-- 不匹配关键字的商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '不匹配', '不匹配', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID6 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID6, '3548293894545', now(), now());
SET @barcodeID6 = last_insert_id();
-- 子商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单1', '多商品单1', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID, '3548293894545', now(), now());
SET @barcodeID = last_insert_id();
-- 子商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品单2', '多商品单2', 'ml', 7, '包', 1, 8, 'FWSP2', 1, 
199.8, 199.5, 0, 1, NULL, 365, 3, '01/01/2000 09:00:00 上午', 1, 0, 0, '182', 0, NULL, '01/01/2000 09:00:00 上午', '01/01/2000 09:00:00 上午', NULL, NULL, NULL, NULL);
SET @commodityID2 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID2, '3548293894545', now(), now());
SET @barcodeID2 = last_insert_id();
-- 组合商品
INSERT INTO nbr.t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品组', '多商品组', '克', 1, '箱', 1, 1, 'SP', 1, 
0.780178, 0.147884, 0, NULL, '', NULL, 8, '01/10/2020 04:45:48 下午', NULL, 0, 0, '111', 1, NULL, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID3 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, '3548293894545', now(), now());
SET @barcodeID3 = last_insert_id();
-- 插入从表
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID, @subCommodity1NO, 3, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID = last_insert_id();
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID3, @commodityID2, @subCommodity2NO, 2, '01/10/2020 04:45:48 下午', '01/10/2020 04:45:48 下午');
SET @subCommodityID2 = last_insert_id();
-- 
-- 创建多包装商品，倍数为5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品多', '多商品多', 'ml', 6, NULL, 1, 3, 'FWSP1', 1, 
124.8, 124.5, 0, 1, NULL, 365, 3, now(), 10, @commodityID, @refCommodityMultiple/*F_RefCommodityMultiple*/, '178', 2, NULL, now(), now(), NULL, NULL, NULL, NULL);
SET @commodityID4 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID4, '3548293894546', now(), now());
SET @barcodeID4 = last_insert_id();
-- 
-- 创建服务商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多商品服', '多商品服', '克', 1, '', 1, 1, 'SP', 1, 
0.958926, 0.320467, 0, 1, '', 0, 6, '01/10/2020 05:29:07 下午', 0, 0, 0, '111', 3, NULL, '01/10/2020 05:29:07 下午', '01/10/2020 05:29:07 下午', '自定义属性1', '自定义属性2', '自定义属性3', '自定义属性4');
SET @commodityID5 = last_insert_id();
-- 条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commodityID5, '3548293894545', now(), now());
SET @barcodeID5 = last_insert_id();
-- 
-- 跨库，入库单1不够卖，继续售卖下一个入库单2，两个入库单价格不一致
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010045', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID2 = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commodityID, 1/*F_NO*/, 1, '多商品单1', 211, 22.2/*F_Price*/, 300, now(), 365, now(), now(), now(), 1);
SET @warehousingCommodityID21 = last_insert_id();
-- 入库单主表
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK200003010044', 1, 1, 4, 4, now(), NULL, now());
SET @warehousingID = last_insert_id();
-- 入库单从表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID, @warehousingCommNO, 1, '多商品单1', 211, @warehousingCommPrice/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID2, @warehousingComm2NO, 1, '多商品单2', 211, @warehousingComm2Price/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID2 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehousingID, @commodityID6, 50, 1, '不匹配', 211, 11.1/*F_Price*/, 300, now(), 365, now(), now(), now(), 0);
SET @warehousingCommodityID3 = last_insert_id();
-- 零售单1
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130060', 12704, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount1/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity2NO/*F_NO*/, 20, 10, @retailTradeCommodity2Price, 10, 10);
SET @retailTradeCommodityID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity1NO/*F_NO*/, 20, 10, @retailTradeCommodity1Price, 10, 10);
SET @retailTradeCommodityID2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity3NO/*F_NO*/, 20, 10, @retailTradeCommodity3Price, 10, 10);
SET @retailTradeCommodityID3 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity4NO/*F_NO*/, 20, 10, @retailTradeCommodity4Price, 10, 10);
SET @retailTradeCommodityID4 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID5 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @retailTradeCommodity2NO - @warehousingCommNO2/**/, @warehousingID);
SET @retailTradeCommoditySourceID  = last_insert_id();
-- 来源入库单2
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commodityID/**/, @warehousingCommNO2/**/, @warehousingID2);
SET @retailTradeCommoditySourceID_fromWs2  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID/**/, @retailTradeCommodity1NO * @subCommodity1NO /**/, @warehousingID);
SET @retailTradeCommoditySourceID2  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID2, @commodityID2/**/, @retailTradeCommodity1NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID3  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID3, @commodityID/**/, @retailTradeCommodity3NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID4 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID4, @commodityID, @retailTradeCommodity4NO, NULL);
SET @retailTradeCommoditySourceID5  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID5, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID6 = last_insert_id();
-- 
-- 零售单2
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130061', 12705, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', -1, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID2 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID/**/, '多商品单1', @barcodeID/**/, @retailTradeCommodity22NO/*F_NO*/, 20, 10, @retailTradeCommodity22Price, 10, 10);
SET @retailTradeCommodityID21 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID3/**/, '多商品组', @barcodeID3/**/, @retailTradeCommodity21NO/*F_NO*/, 20, 10, @retailTradeCommodity21Price, 10, 10);
SET @retailTradeCommodityID22 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID4/**/, '多商品多', @barcodeID4/**/, @retailTradeCommodity23NO/*F_NO*/, 20, 10, @retailTradeCommodity23Price, 10, 10);
SET @retailTradeCommodityID23 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID5/**/, '多商品服', @barcodeID5/**/, @retailTradeCommodity24NO/*F_NO*/, 20, 10, @retailTradeCommodity24Price, 10, 10);
SET @retailTradeCommodityID24 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID2, @commodityID6/**/, '不匹配', @barcodeID6/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID25 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID21, @commodityID/**/, @retailTradeCommodity22NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID21  = last_insert_id();
-- 组合商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID/**/, @retailTradeCommodity21NO * @subCommodity1NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID22  = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID22, @commodityID2/**/, @retailTradeCommodity21NO * @subCommodity2NO/**/, @warehousingID);
SET @retailTradeCommoditySourceID23  = last_insert_id();
-- 多包装商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID23, @commodityID/**/, @retailTradeCommodity23NO * @refCommodityMultiple/**/, @warehousingID);
SET @retailTradeCommoditySourceID24 = last_insert_id();
-- 服务商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID24, @commodityID, @retailTradeCommodity24NO, NULL);
SET @retailTradeCommoditySourceID25  = last_insert_id();
-- 不匹配商品
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID25, @commodityID6/**/, 50/**/, @warehousingID);
SET @retailTradeCommoditySourceID26 = last_insert_id();

-- 退货单
-- 零售单主表
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS201909130062', 12706, 1, 'url=ashasoadigmnalskd', now(), 3, 4, '0', 1, '…', @retailTradeID2/*F_SourceID*/, now(), @totalRetailTradeAmount2/*F_Amount*/, 200, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID3 = last_insert_id();
-- 零售单从表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID/**/, '多商品单1', @barcodeID/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID31 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID3/**/, '多商品组', @barcodeID3/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID32 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID4/**/, '多商品多', @barcodeID4/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID33 = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID3, @commodityID5/**/, '多商品服', @barcodeID5/**/, 10/*F_NO*/, 20, 10, 20, 10, 10);
SET @retailTradeCommodityID34 = last_insert_id();
-- 零售来源
-- 普通商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID31, @commodityID/**/, 10/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination1  = last_insert_id();
-- 组合商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID/**/, 30/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination2  = last_insert_id();
-- 
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID32, @commodityID2/**/, 20/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination3  = last_insert_id();
-- 多包装商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID33, @commodityID/**/, 50/**/, @warehousingID);
SET @returnRetailTradeCommoditydestination4 = last_insert_id();
-- 服务商品
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID34, @commodityID, 10, NULL);
SET @returnRetailTradeCommoditydestination5  = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @commodityName = "多商品";
SET @dtEndDate = now();
SET @dRetailAmount = 0;
SET @iTotalCommNO = 0;
SET @dTotalGross = 0;
SET @iStaffID = 0;
SET @iPaymentType = 0;
-- 
CALL SP_RetailTrade_RetrieveNByCommodityNameInTimeRange(@iErrorCode, @sErrorMsg, @commodityName, @iStaffID, @iPaymentType,  @dtStartDate, @dtEndDate, @iPageIndex, @iPageSize, @iTotalRecord, @dRetailAmount, @iTotalCommNO, @dTotalGross);
SELECT @iTotalRecord;
SELECT @dRetailAmount;
SELECT @iTotalCommNO;
SELECT @dTotalGross;
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT @dtStartDate;
SELECT @dtEndDate;


SELECT IF(@iTotalRecord = 3, '测试成功', '测试失败') AS 'CASE18.1 Testing Result';
SELECT IF(@dRetailAmount = @totalRetailTradeAmount, '测试成功', '测试失败') AS 'CASE18.2 Testing Result';
SELECT IF(@iTotalCommNO = @totalRetailTradeCommodityNO, '测试成功', '测试失败') AS 'CASE18.3 Testing Result';
SELECT IF(@dTotalGross = @totalRetailTradeGross, '测试成功', '测试失败') AS 'CASE18.4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID6;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID21;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID22;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID23;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID24;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID25;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID26;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailTradeCommoditySourceID_fromWs2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination3;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination4;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnRetailTradeCommoditydestination5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID5;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID21;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID22;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID23;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID24;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID25;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID31;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID32;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID33;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID34;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID35;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID21;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID;
DELETE FROM t_subcommodity WHERE F_ID = @subCommodityID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID3;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID4;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID5;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID6;
DELETE FROM t_commodity WHERE F_ID = @commodityID5;
DELETE FROM t_commodity WHERE F_ID = @commodityID4;
DELETE FROM t_commodity WHERE F_ID = @commodityID3;
DELETE FROM t_commodity WHERE F_ID = @commodityID2;
DELETE FROM t_commodity WHERE F_ID = @commodityID;