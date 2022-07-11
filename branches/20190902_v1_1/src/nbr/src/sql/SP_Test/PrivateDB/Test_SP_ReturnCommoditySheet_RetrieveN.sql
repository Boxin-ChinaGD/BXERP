SELECT '++++++++++++++++++ Test_SP_returnCommoditySheet_RetrieveN.sql ++++++++++++++++++++';
SELECT '-------------------- CASE1;等于最小长度的退货单单号模糊查询入库单(等于10位)，期望的@iTotalRecord >= 1 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- CASE2;根据商品部分名称模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '润泽';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- CASE3;根据经办人ID模糊查询入库单，期望的@iTotalRecord >= 1 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 6;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- CASE4;根据退货单状态模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord>=1, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- CASE5;根据供应商ID模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- CASE6;根据创建时间模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '-------------------- CASE7;根据等于最小长度的退货单号和退货单状态模糊查询入库单(等于10位)，期望的@iTotalRecord>=1 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '-------------------- CASE8;根据等于最小长度的退货单号和经办人模糊查询入库单(等于10位)，期望的@iTotalRecord>=1 -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = 4;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case8 Testing Result';

SELECT '-------------------- CASE9;根据等于最小长度的退货单单号和供应商ID模糊查询入库单(等于10位)，期望的@iTotalRecord>=1 -------------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case9 Testing Result';

SELECT '-------------------- CASE10;根据等于最小长度的退货单号和创建时间模糊查询入库单(等于10位)，期望的@iTotalRecord>=1 -------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case10 Testing Result';

SELECT '-------------------- CASE11;根据退货单状态和退货单经办人模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 4;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '-------------------- CASE12;根据退货单状态和供应商模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT '-------------------- CASE13;根据退货单状态和创建时间模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case13 Testing Result';

SELECT '-------------------- CASE14;根据退货单状态和商品部分名称模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '润泽';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case14 Testing Result';

SELECT '-------------------- CASE15;根据退货经办人和供应商模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case15';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case15 Testing Result';

SELECT '-------------------- CASE16;根据退货经办人和创建时间模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case16';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case16 Testing Result';

SELECT '-------------------- CASE17;根据退货经办人和商品部分名称模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case17';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '雪晶灵';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case17 Testing Result';

SELECT '-------------------- CASE18;根据供应商和创建时间模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case18';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case18 Testing Result';

SELECT '-------------------- CASE19;根据供应商和商品部分名称模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case19';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '雪晶灵';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case19 Testing Result';

SELECT '-------------------- CASE20;根据创建时间和商品部分名称模糊查询入库单，期望的@iTotalRecord>=1 -------------------------' AS 'Case20';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '润泽';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case20 Testing Result';

SELECT '-------------------- CASE21;根据只有结束时间没有开始时间模糊查询入库单，期望的@iTotalRecord = 退货单的总数 -------------------------' AS 'Case21';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case21 Testing Result';


SELECT '-------------------- CASE22;根据只有开始时间没有结束时间模糊查询入库单，期望的@iTotalRecord = 退货单的总数 -------------------------' AS 'Case22';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case22 Testing Result';

SELECT '-------------------- CASE23;根据空的条件模糊查询入库单，期望的@iTotalRecord = 退货单的总数 -------------------------' AS 'Case23';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case23 Testing Result';

SELECT '-------------------- CASE24;根据全部条件(string1为退货单ID)模糊查询入库单，期望的@iTotalRecord = 退货单的总数 -------------------------' AS 'Case24';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = 2;
SET @iStatus = 1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/6 0:00:00';
SET @dtEnd = '2018/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case24 Testing Result';

SELECT '-------------------- CASE25;根据全部条件(string1为商品名称)模糊查询入库单，期望的@iTotalRecord = 退货单的总数 -------------------------' AS 'Case25';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '雪晶灵肌密面霜';
SET @sStaffID = 2;
SET @iStatus = 1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/6 0:00:00';
SET @dtEnd = '2018/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '测试成功', '测试失败') AS 'Case25 Testing Result';

SELECT '-------------------- CASE26;根据小于最小长度的退货单单号模糊查询入库单(小于10位)，期望的@iTotalRecord=0 -------------------------' AS 'Case26';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH2019060';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case26 Testing Result';

SELECT '-------------------- CASE27;根据大于最小长度的退货单单号模糊查询入库单(大于20位)，期望的@iTotalRecord=0 -------------------------' AS 'Case27';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605123451234512345';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case27 Testing Result';

SELECT '-------------------- CASE28:根据商品名称查询(未审核的)查出商品表的名称 -------------------------' AS 'Case28';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'维他柠檬', 10, 100, '箱', 8);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '可比克';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'Case28 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- CASE29:根据商品名称查询(已审核的)查出退货单商品的商品名称 -------------------------' AS 'Case28';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_ShopID)
VALUES (3, 3, 1,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'维他柠檬', 10, 100, '箱', 8);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '维他柠檬';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'Case29 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- CASE30;根据等于最小长度的退货单单号模糊查询入库单(等于20位)，期望的@iTotalRecord=0 -------------------------' AS 'Case30';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH201906051234512345';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case30 Testing Result';

SELECT '-------------------- CASE31：根据商品名称查询(已审核的，string1包含_的特殊字符进行模糊搜索)查出退货单商品的商品名称 -------------------------' AS 'Case31';

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID,F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (1, 1, 2,'退货单薯片_12（3', 20, '箱', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '_12（3';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE31 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityName = '退货单薯片_12（3';

SELECT '-------------------- CASE32：根据商品名称查询(未审核的，string1包含_的特殊字符进行模糊搜索)查出商品表的名称-------------------------' AS 'Case32';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cjs_666','特辣辣条','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID,F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (3, 1,@iID ,'退货可口快乐73468', 20, '箱', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '_';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE32 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityName = '退货可口快乐73468';
DELETE FROM t_commodity WHERE F_Name = 'cjs_666';

SELECT '-------------------- CASE33;根据门店ID进行查询，期望的@iTotalRecord >= 1 -------------------------' AS 'Case33';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = 2;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'Case33 Testing Result';