SELECT '-------------------- Case1: 按商品销售金额降序排列 -------------------------' AS 'Case1';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 0; -- 金额
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 按商品销售金额升序排列 -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- 升序
SET @iOrderBy = 0; -- 金额
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 按销售数量降序排列 -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 1; -- 数量
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode,
	@sErrorMsg, 
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: 按毛利降序排列-------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 2; -- 毛利
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';


SELECT '-------------------- Case5: 按名称进行模糊查询并按金额降序排列 -------------------------' AS 'Case5';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "可";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';


SELECT '-------------------- Case6: 按条形码进行精确查询并按数量降序排列 -------------------------' AS 'Case';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "3548293894545";
SET @isASC = 0;
SET @iOrderBy = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';


SELECT '-------------------- Case7: 按单号进行查询并按销售毛利降序排列 -------------------------' AS 'Case7';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "1";
SET @isASC = 0;
SET @iOrderBy = 3;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';



SELECT '-------------------- Case8: bIgnoreZeroNO=1忽略0进货量 -------------------------' AS 'Case8';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 1;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';


SELECT '-------------------- Case9: 按照类别进行查询 -------------------------' AS 'Case9';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/03/20 23:59:59";
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = 1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: 查询一段时间的数据 -------------------------' AS 'Case10';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/03/20 23:59:59";
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';


SELECT '-------------------- Case11: 查询不存在的时间 -------------------------' AS 'Case11';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2025/02/11 00:00:00';
SET @dtEnd = "2025/02/14 23:59:59";
SET @string1 = "";
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE11 Testing Result';

SELECT '-------------------- CASE12：根据商品名称查询(string1包含_的特殊字符进行模糊搜索)查出商品表的名称-------------------------' AS 'Case12';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'sdjhcjs_666','肯德基495946','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_NO,F_BarcodeID, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (1,@iID,'徐福记牛轧糖BBB',3,200,254,200, 200, 200);

INSERT INTO t_retailtradedailyreportbycommodity (F_ShopID, F_Datetime,F_CommodityID,F_NO,F_TotalPurchasingAmount,F_TotalAmount,F_GrossMargin)
VALUES (2, now(),@iID,60,400,300,300);



SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2000/02/11 00:00:00';
SET @dtEnd = now();
SET @string1 = '_';	
SET @isASC = 0;
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0  , '测试成功', '测试失败') AS 'CASE12 Testing Result';
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iID;
DELETE FROM t_retailtradecommodity WHERE F_CommodityName = '徐福记牛轧糖BBB';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case13: 搜索一个不存在的门店ID -------------------------' AS 'Case13';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = -1;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- 降序
SET @iOrderBy = 0; -- 金额
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_RetailTradeDailyReportByCommodity_RetrieveN
(
	@iErrorCode, 
	@sErrorMsg,
	@iTotalRecord,
	@iShopID,
	@dtStart, 
	@dtEnd,
	@string1,
	@isASC,
	@iOrderBy,
	@iPageIndex, 
	@iPageSize,
	@bIgnoreZeroNO,
	@iCategory
);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE13 Testing Result';