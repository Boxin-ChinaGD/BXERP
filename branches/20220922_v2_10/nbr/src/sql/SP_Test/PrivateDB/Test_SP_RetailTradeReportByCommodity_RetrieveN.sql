SELECT '++++++++++++++++++ Test_SP_RetailTradeReportByCommodity_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����Ʒ���۽������� -------------------------' AS 'Case1';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 0; -- ���
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ����Ʒ���۽���������� -------------------------' AS 'Case2';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- ����
SET @iOrderBy = 0; -- ���
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ������������������ -------------------------' AS 'Case3';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 1; -- ����
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: ������������������ -------------------------' AS 'Case4';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- ����
SET @iOrderBy = 1; -- ����
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';


SELECT '-------------------- Case5: ��ë���ʽ������� -------------------------' AS 'Case5';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 2; -- ë����
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: ��ë������������ -------------------------' AS 'Case6';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- ����
SET @iOrderBy = 2; -- ë����
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';


SELECT '-------------------- Case7: �����ƽ���ģ����ѯ������������ -------------------------' AS 'Case7';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "��";
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: �����ƽ���ģ����ѯ��������������� -------------------------' AS 'Case8';
SET @iTotalRecord = 0;
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2020/12/21 23:59:59";
SET @string1 = "��";
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: ����������о�ȷ��ѯ���������������� -------------------------' AS 'Case9';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';


SELECT '-------------------- Case10: �����Ž��в�ѯ��������ë���������� -------------------------' AS 'Case10';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';



SELECT '-------------------- Case11: bIgnoreZeroNO=1����0������ -------------------------' AS 'Case11';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';


SELECT '-------------------- Case12: ���������в�ѯ -------------------------' AS 'Case12';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';

SELECT '-------------------- Case13: ������Ʒ���Ʋ�ѯ(string1����_�������ַ�����ģ������)�����Ʒ�������-------------------------' AS 'Case13';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0/*F_Status*/,'sdjhcjs_888'/*F_Name*/,'�ϵ»�495946rewtert','��',1,'��',3,1,'SP',1,
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
VALUES (@iRetailtradeID,@iID,'�츣��ţ����CCC',5,200,254,200, 200, 200);

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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0  , '���Գɹ�', '����ʧ��') AS 'CASE13 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iRetailtradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailtradeID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;