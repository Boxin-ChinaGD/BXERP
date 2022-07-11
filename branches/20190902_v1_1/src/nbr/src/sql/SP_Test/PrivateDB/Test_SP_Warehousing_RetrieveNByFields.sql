SELECT '++++++++++++++++++ Test_SP_Warehousing_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++ CASE1;等于最小长度的入库单单号(等于10位)模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '++++++++++++++++++ CASE2;根据商品名称模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'A可乐18';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE3;根据供应商名称模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '默认';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE4;在限定状态下根据供应商名称模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '默认';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE5;在限定经办人ID下根据供应商名称模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '默认';
SET @iShopID = -1;
SET @sStaffID = 3;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE6;在限定供应商ID下根据最小长度的入库单单号(10位)模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 2; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE7;在限定创建时间段下根据供应商名称查模糊询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '安徽';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/10/8 1:01:01';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE8;根据状态的进行搜索（不传入string1） ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE9;根据经办人ID的进行搜索（不传入string1) ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = 3;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE10;根据供应商ID的进行搜索（不传入string1） ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 10; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE11;根据传入创建时间段的进行搜索（不传入string1） ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2019/1/8 1:01:00';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '++++++++++++++++++ CASE12:传值为空则查询全部 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT '++++++++++++++++++ CASE13;小于最小长度的入库单单号(小于10位)模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK2019060';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';


SELECT '++++++++++++++++++ CASE14;大于最大长度的入库单单号(大于20位)模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605123451234512345';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';

SELECT '++++++++++++++++++ CASE15;等于最大长度的入库单单号(等于20位)模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK201906051234512345';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case15 Testing Result';


SELECT '++++++++++++++++++ CASE16;输入10位以下的采购订单模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG2019';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case16 Testing Result';

SELECT '++++++++++++++++++ CASE17;输入20位以上的采购订单模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG20190604000700000000';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'Case18 Testing Result';

SELECT '++++++++++++++++++ CASE18;输入10-20位的采购订单模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG201906040007';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case18 Testing Result';



SELECT '-------------------- Case19:传入string1包含_（）的特殊字符进行模糊搜索 -------------------------' AS 'Case19';
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (5, 4, 20, 1, '天地一号_()就看看', 6, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '_(';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE19 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_CommodityName = '天地一号_()就看看';

-- 
SELECT '++++++++++++++++++ CASE20;根据门店ID模糊查询入库单 ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = 2;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case20 Testing Result';