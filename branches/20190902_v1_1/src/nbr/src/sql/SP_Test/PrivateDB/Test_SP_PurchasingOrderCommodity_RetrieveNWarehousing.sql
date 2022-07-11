SELECT '++++++++++++++++++ Test_SP_PurchasingOrderCommodity_RetrieveNWarehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: iWarehousing为0时查询未已入库的  结果为百事可乐剩余100,不二家棒棒糖剩余200 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 0;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: iWarehousing为1时查询已入库的 一共有3个入库单，但只有审核过才算真正入库，所以总共是两个入库单 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 1;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3: iWarehousing为-99时,错误码=7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = -99;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能查询到其他状态的入库单', '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: iWarehousing为1时,采购订单ID为不存在的ID -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 1;
SET @iPurchasingOrderID = -99;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);

SELECT IF(@iTotalRecord = '' AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case4 Testing Result';