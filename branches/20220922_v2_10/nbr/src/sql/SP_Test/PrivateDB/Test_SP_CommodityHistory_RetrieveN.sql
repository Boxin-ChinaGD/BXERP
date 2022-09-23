SELECT '++++++++++++++++++ Test_SP_CommodityHistory_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 查询具体时间段的修改历史 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 30000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;
 
CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1, @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord); 
SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 查询某个商品的修改历史 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1, @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: 查询某个员工的修改历史 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = 2;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1, @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: 查询某个字段的修改历史 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = -1;
SET @sFieldName = '零售价';
SET @iStaffID = -1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- Case5: 多个条件查询修改历史 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = 1;
SET @sFieldName = '零售价';
SET @iStaffID = 1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- Case6: 所有条件进行查询修改历史 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '薯片';
SET @iCommodityID = 1;
SET @sFieldName = '零售价';
SET @iStaffID = 1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '-------------------- case7:根据商品名称进行名称查询 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '可乐';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case7 Testing Result';


SELECT '-------------------- case8:根据商品条形码进行名称查询 -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '3548293';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 5000000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case8 Testing Result';


SELECT '-------------------- case9:根据输入特殊字符"_"商品名称模糊查询商品修改历史 -------------------------' AS 'Case9';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cj_s666','特辣辣条','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '钢铁侠_2号';		          
SET @sShortName = '饮料2';     
SET @sSpecification = '毫升';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';  
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '_';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = '';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = -1;

CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1,  @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0 , '测试成功', '测试失败') AS 'case9 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = LAST_INSERT_ID();
DELETE FROM t_commodity WHERE F_ID = LAST_INSERT_ID();



SELECT '-------------------- Case10: 查询某个门店的修改历史 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 30000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = 2;
 
CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1, @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord); 
SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';


SELECT '-------------------- Case10: 查询某个门店的修改历史（门店不存在） -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iCommodityID = -1;
SET @sFieldName = '';
SET @iStaffID = -1;
SET @dtStart = now() - 30000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iShopID = 999999;
 
CALL SP_CommodityHistory_RetrieveN(@iErrorCode, @sErrorMsg, @string1, @iCommodityID, @sFieldName, @iStaffID, @iShopID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord); 
SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';