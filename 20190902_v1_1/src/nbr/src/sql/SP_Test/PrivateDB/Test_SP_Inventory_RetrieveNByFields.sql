SELECT '++++++++++++++++++ Test_SP_Inventory_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 根据商品的名称模糊搜素盘点商品表 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '薯片';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 没有输入任何值搜索盘点商品表 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 查询被删除商品的采购订单 -------------------------' AS 'Case3';

INSERT INTO t_inventorysheet (F_ShopID, F_WarehouseID, F_Scope, F_Status, F_StaffID, F_ApproverID, F_CreateDatetime, F_ApproveDatetime, F_Remark, F_UpdateDatetime)
VALUES (2, 1, 100, 0, 1, 1, now(), now(), "111", now());

SET @iID = LAST_INSERT_ID();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iID, 1, 1, 1, 0, 0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '百事青椒味薯片1';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iID;
DELETE FROM t_inventorysheet WHERE F_ID = @iID;

SELECT '-------------------- Case4: 查询已经被删除的采购订单 -------------------------' AS 'Case4';

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( 10, 1, 1, 1, 0, 0);

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '润泽玻尿酸面膜';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iID;

SELECT '-------------------- Case5: 根据等于最小长度的盘点单单号(等于10位)模糊搜素盘点商品表 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD20190605';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 根据小于最小长度的盘点单单号(小于10位)模糊搜素盘点商品表 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD2019060';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: 根据大于最大长度的盘点单单号(大于20位)模糊搜素盘点商品表 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD20190605123451234512345';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: 根据等于最大长度的盘点单单号(等于20位)模糊搜素盘点商品表 -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD201906051234512345';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';


SELECT '-------------------- Case9:传入string1包含_的特殊字符进行模糊搜索 -------------------------' AS 'Case9';
INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (8, 1, '薯片_)三号', '克', 1, 1, -1, -1, now());
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '_)';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_CommodityName = '薯片_)三号';
