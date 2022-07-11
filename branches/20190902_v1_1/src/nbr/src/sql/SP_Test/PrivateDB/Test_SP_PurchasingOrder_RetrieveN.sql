SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 查询状态为0的采购单 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 0;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 查询状态为1的采购单 -------------------------' AS 'Case2';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 查询状态为2的采购单 -------------------------' AS 'Case3';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 2;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: 查询状态为3的采购单 -------------------------' AS 'Case4';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 3;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: 查询状态不为4的采购单,传入-1 -------------------------' AS 'Case5';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 查询状态为4的采购单，返回错误码7 -------------------------' AS 'Case6';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 4;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: 查询未定义的采购订单状态，返回错误码7 -------------------------' AS 'Case7';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 999;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: 根据string1传的值进行模糊查询（根据商品名称） -------------------------' AS 'Case8';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '可乐';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: 根据string1传的值进行模糊查询（根据供应商名称） -------------------------' AS 'Case9';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '默认供应商';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: 根据string1传的值进行模糊查询（等于最小长度的采购订单单号,等于10位） -------------------------' AS 'Case10';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';

SELECT '-------------------- Case11: 根据string1传的值进行模糊查询（等于最小长度的采购订单单号匹配或者商品名称匹配或者供应商名称匹配的可以查询到,等于10位） -------------------------' AS 'Case11';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE11 Testing Result';

SELECT '-------------------- Case12: 根据string1传的值进行模糊查询（无匹配项） -------------------------' AS 'Case12';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '-999999999';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE12 Testing Result';

SELECT '-------------------- Case13: 根据时间段进行查询（一天） -------------------------' AS 'Case13';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/06 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE13 Testing Result';

SELECT '-------------------- Case14: 根据时间段进行查询（一星期） -------------------------' AS 'Case14';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE14 Testing Result';

SELECT '-------------------- Case15: 根据时间段进行查询（无记录时间段） -------------------------' AS 'Case15';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='1998/12/06 00:00:00';
SET @dtDate2 = '1999/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE15 Testing Result';

SELECT '-------------------- Case16: 根据时间段进行查询（有开始时间,无结束时间） -------------------------' AS 'Case16';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/07 00:00:00';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE16 Testing Result';

SELECT '-------------------- Case17: 根据时间段进行查询（有结束时间,无开始时间） -------------------------' AS 'Case17';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '2016/12/07 00:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE17 Testing Result';

SELECT '-------------------- Case18: 根据操作人的ID（iStaffID=1）查询采购订单 -------------------------' AS 'Case18';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = 6;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE18 Testing Result';

SELECT '-------------------- Case19: 根据操作人的ID（iStaffID=-999999,ID不存在，返回0条采购订单）查询采购订单  -------------------------' AS 'Case19';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -999999;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE19 Testing Result';

SELECT '-------------------- Case20: 全条件查询  -------------------------' AS 'Case20';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @string1 = '供应商';
SET @iStaffID = 1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE20 Testing Result';

SELECT '-------------------- Case21: 空条件查询  -------------------------' AS 'Case21';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE21 Testing Result';

SELECT '-------------------- Case22: 根据string1传的值进行模糊查询（小于最小长度的采购订单单号,小于10位） -------------------------' AS 'Case22';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG2019060';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE22 Testing Result';

SELECT '-------------------- Case23: 根据string1传的值进行模糊查询（大于最大长度的采购订单单号,大于20位） -------------------------' AS 'Case23';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604123451234512345';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE23 Testing Result';

SELECT '-------------------- Case24: 根据string1传的值进行模糊查询（等于最大长度的采购订单单号,等于20位） -------------------------' AS 'Case24';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG201906041234512345';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE24 Testing Result';

SELECT '-------------------- Case25:传入string1包含_的特殊字符进行模糊搜索  -------------------------' AS 'Case25';

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1,5,200,'钢铁侠2_3号',1,1,11.1);
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '_';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE25 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_CommodityName = '钢铁侠2_3号';


SELECT '-------------------- Case26: 根据门店ID查询采购单 -------------------------' AS 'Case26';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE26 Testing Result';