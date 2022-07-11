SELECT '++++++++++++++++++ Test_SP_RetailTradeReportByCommodity_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 按商品销售金额降序排列 -------------------------' AS 'Case1';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 0; -- 金额
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 按商品销售金额升序排列 -------------------------' AS 'Case2';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- 升序
SET @iOrderBy = 0; -- 金额
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 按销售数量降序排列 -------------------------' AS 'Case3';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 1; -- 数量
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: 按销售数量升序排列 -------------------------' AS 'Case4';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- 升序
SET @iOrderBy = 1; -- 数量
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';


SELECT '-------------------- Case5: 按毛利率降序排列 -------------------------' AS 'Case5';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 2; -- 毛利率
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 按毛利率升序排列 -------------------------' AS 'Case6';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- 降序
SET @iOrderBy = 2; -- 毛利率
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';


SELECT '-------------------- Case7: 按名称进行模糊查询并按金额降序排列 -------------------------' AS 'Case7';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "可";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: 按名称进行模糊查询并按金额升序排列 -------------------------' AS 'Case8';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "可";
SET @isASC = 1;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: 按条形码进行精确查询并按数量降序排列 -------------------------' AS 'Case9';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "3548293894545";
SET @isASC = 0;
SET @iOrderBy = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';


SELECT '-------------------- Case10: 按单号进行查询并按销售毛利降序排列 -------------------------' AS 'Case10';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = '2020/12/21 23:59:59';
SET @string1 = "7";
SET @isASC = 0;
SET @iOrderBy = 3;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';



SELECT '-------------------- Case11: bIgnoreZeroNO=1忽略0进货量 -------------------------' AS 'Case11';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 1;
SET @iCategory = -1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE11 Testing Result';


SELECT '-------------------- Case12: 按照类别进行查询 -------------------------' AS 'Case12';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = 1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE12 Testing Result';

SELECT '-------------------- Case13: 根据商品名称查询(string1包含_的特殊字符进行模糊搜索)查出商品表的名称-------------------------' AS 'Case13';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'sdjhcjs_888'/*F_Name*/,'肯德基495946rewtert','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2011080601010100011234', 1, 2, 'url=ashasoadigmnalskd', '2017-08-06', 2, 4, '0', 1, '........', -1, '2019-11-29 13:13:29', 150000, 0, 0, 150000, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iRetailtradeID = LAST_INSERT_ID();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_NO,F_BarcodeID, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailtradeID,@iID,'徐福记牛轧糖CCC',5,200,254,200, 200, 200);

SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "_";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = 1;
SET @iShopID = 2;

CALL SP_RetailTradeReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory,
	@iShopID
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0  , '测试成功', '测试失败') AS 'CASE13 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailtradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailtradeID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;