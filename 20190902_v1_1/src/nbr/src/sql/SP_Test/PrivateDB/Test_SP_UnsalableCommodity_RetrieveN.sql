SELECT '++++++++++++++++++ Test_SP_UnsalableCommodity_RetrieveN.sql ++++++++++++++++++++';


SELECT '-------------------- Case1:  查询出在dStartDate、dEndDate之间没有进行零售过的商品(即滞销商品) -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SET @totalRecord = 0;
SELECT count(*) INTO @totalRecord FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0 AND @totalRecord > 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case2:  传入错误的消息分类ID -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 1000000;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '不存在该消息类别' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case3:  传入错误的公司ID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1000000;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '不存在该公司' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;


SELECT '-------------------- Case4:  新建一个商品,如果商品的零售时间在dStartDate、dEndDate之间,则该商品不是滞销商品 -------------------------' AS 'Case4';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 29 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case4_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '-------------------- Case5:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,则该商品是滞销商品 -------------------------' AS 'Case5';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2, 8, 12, 800, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() > 0, '测试成功', '测试失败') AS 'Case5_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case6:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是退货时间是在这两个日期之间，则该商品还是滞销商品 -------------------------' AS 'Case6';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2, 8, 12, 800, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();


-- 新建退货单B，B对零售A退货
SET @dSaleDatetimeB = DATE_SUB(now(), INTERVAL 29 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test223483218000', 3, 5, 'url=ashasoadigmnalskd', @dSaleDatetimeB, 2, 4, '0', 1, '........', @retailTradeID, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, @commodityID, '我是滞销商品啊', 2, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() > 0, '测试成功', '测试失败') AS 'Case6_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case7:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品的库存为0，则该商品不是滞销商品 -------------------------' AS 'Case7';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case7_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '-------------------- Case8:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是组合商品，则该商品不是滞销商品 -------------------------' AS 'Case8';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 1, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case8_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case9:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是多包装商品，则该商品不是滞销商品 -------------------------' AS 'Case9';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 2, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case9_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case9_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '-------------------- Case10:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是服务商品，则该商品不是滞销商品 -------------------------' AS 'Case10';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 3, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case10_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case10_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case11:  新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品为已删除状态，则该商品不是滞销商品 -------------------------' AS 'Case11';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (2, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case11_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

SELECT '-------------------- Case12:  新建一个商品,该商品零售价为0，商品的零售时间不在dStartDate、dEndDate之间，该商品不是滞销商品 -------------------------' AS 'Case11';

-- 新建一个商品

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (2, '我不是滞销商品啊', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '04/14/2018 01:00:01 上午', 20, 0, 0, '1111111', 0, NULL, '08/19/2019 05:50:26 下午', '08/19/2019 05:50:26 下午', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();

-- 新建一个零售单retailTradeA
SET @dSaleDatetime = DATE_SUB(now(), INTERVAL 31 DAY);
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'Test123483218000', 2, 4, 'url=ashasoadigmnalskd', @dSaleDatetime, 2, 4, '0', 1, '........', -1, @dSaleDatetime, 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建零售单A的零售商品retailTradeACommodity
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, @commodityID, '可口可乐Test8', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dStartDate = DATE_SUB(now(), INTERVAL 30 DAY);
SET @dEndDate = now();
SET @iMessageIsRead = 0;
SET @sMessageParameter = '[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]';
SET @iMessageCategoryID = 8;
SET @iCompanyID = 1;
SET @iMessageSenderID = 0;
SET @iMessageReceiverID = 1;
 
CALL SP_UnsalableCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @dStartDate, @dEndDate, @iMessageIsRead, @sMessageParameter, @iMessageCategoryID, @iCompanyID, @iMessageSenderID, @iMessageReceiverID);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11_1 Testing Result';

SELECT 1 FROM t_messageitem WHERE F_CommodityID = @commodityID;

SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'Case11_2 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_messageitem WHERE F_MessageCategoryID = @iMessageCategoryID;
DELETE FROM t_message WHERE F_CategoryID = @iMessageCategoryID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;