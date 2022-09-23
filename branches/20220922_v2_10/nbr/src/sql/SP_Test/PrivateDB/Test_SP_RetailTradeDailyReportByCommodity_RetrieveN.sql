SELECT '-------------------- Case1: ����Ʒ���۽������� -------------------------' AS 'Case1';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 0; -- ���
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ����Ʒ���۽���������� -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 1;    -- ����
SET @iOrderBy = 0; -- ���
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ������������������ -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 1; -- ����
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: ��ë����������-------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 2; -- ë��
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';


SELECT '-------------------- Case5: �����ƽ���ģ����ѯ������������ -------------------------' AS 'Case5';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = 2;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "��";
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';


SELECT '-------------------- Case6: ����������о�ȷ��ѯ���������������� -------------------------' AS 'Case';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';


SELECT '-------------------- Case7: �����Ž��в�ѯ��������ë���������� -------------------------' AS 'Case7';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';



SELECT '-------------------- Case8: bIgnoreZeroNO=1����0������ -------------------------' AS 'Case8';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';


SELECT '-------------------- Case9: ���������в�ѯ -------------------------' AS 'Case9';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: ��ѯһ��ʱ������� -------------------------' AS 'Case10';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';


SELECT '-------------------- Case11: ��ѯ�����ڵ�ʱ�� -------------------------' AS 'Case11';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';

SELECT '-------------------- CASE12��������Ʒ���Ʋ�ѯ(string1����_�������ַ�����ģ������)�����Ʒ�������-------------------------' AS 'Case12';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'sdjhcjs_666','�ϵ»�495946','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_NO,F_BarcodeID, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (1,@iID,'�츣��ţ����BBB',3,200,254,200, 200, 200);

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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0  , '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iID;
DELETE FROM t_retailtradecommodity WHERE F_CommodityName = '�츣��ţ����BBB';
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case13: ����һ�������ڵ��ŵ�ID -------------------------' AS 'Case13';
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iShopID = -1;
SET @dtStart ='2018/02/11 00:00:00';
SET @dtEnd = "2018/02/11 23:59:59";
SET @string1 = "";
SET @isASC = 0;    -- ����
SET @iOrderBy = 0; -- ���
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE13 Testing Result';