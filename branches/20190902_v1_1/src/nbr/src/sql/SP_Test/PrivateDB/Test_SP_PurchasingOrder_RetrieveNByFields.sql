SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 根据商品名称查询商品 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '可乐';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

SELECT '-------------------- Case2: 根据供应商名称查询商品 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '默认供应商';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case2 Testing Result';

SELECT '-------------------- Case3: 根据商品名称和供应商名称查询商品 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '某某';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case3 Testing Result';

SELECT '-------------------- Case4: 根据时间段查询采购订单，查询一天 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = "2016/12/06 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case4 Testing Result';

SELECT '-------------------- Case5: 根据时间段查询采购订单，查询一个星期 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = "2016/12/12 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case5 Testing Result';

SELECT '-------------------- Case6: 根据时间段查询采购订单，查询一个没有信息的时间段 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @string2 = '';
SET @dtDate1 ='2000/12/06 00:00:00';
SET @dtDate2 = "2010/12/12 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case6 Testing Result';

SELECT '-------------------- Case7: 根据操作人的ID（iStaffID=1）查询采购订单 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 2;
SET @dtDate1 =NULL;
SET @dtDate2 = NULL;
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case7 Testing Result';

SELECT '-------------------- Case8: 根据操作人的ID（iStaffID=-999999,ID不存在，返回0条采购订单）查询采购订单 -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = -999999;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case8 Testing Result';

SELECT '-------------------- Case9: 根据时间段进行查询采购订单.查询2019/3/13 10:35:47以后的采购订单 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 = '2019/3/13 10:35:47';
SET @dtDate2 = NULL;
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case9 Testing Result';

SELECT '-------------------- Case10: 根据时间段进行查询采购订单.查询2016/12/6 0:00:00之前的采购订单 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 = NULL;
SET @dtDate2 = '2016/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case10 Testing Result';

SELECT '-------------------- Case11: 根据不存在的采购订单ID进行查询 -------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '-99999999';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case11 Testing Result';

SELECT '-------------------- Case12: 根据string1(采购订单ID匹配或者商品名称匹配或者供应商名称匹配的可以查询到)进行查询 -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '1';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case12 Testing Result';

SELECT '-------------------- Case13: 根据空条件进行查询 -------------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case13 Testing Result';

SELECT '-------------------- Case14: 根据全条件进行查询 -------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '5';
SET @iStaffID = 5;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'case14 Testing Result';

SELECT '-------------------- Case15:传入string1包含_的特殊字符进行模糊搜索 -------------------------' AS 'Case15';
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1,5,200,'钢铁侠2_3号',1,1,11.1);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '_';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE15 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_CommodityName = '钢铁侠2_3号';