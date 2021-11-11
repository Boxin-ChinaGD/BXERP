SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheetCommodity_Delete.sql ++++++++++++++++++++';	

SELECT '-------------------- Case1:删除一张退货单的退货单商品表的一条数据 -------------------------' AS 'Case1';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();
-- 浮点类型不插入默认为0，用不到F_PurchasingPrice，所以此Tset就不插入了
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 5,'康师傅牛肉面', 7, 100, '箱');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 5;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ID = last_insert_id();

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case2:删除一张退货单的退货单商品表的所有数据 -------------------------' AS 'Case2';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 6,'润泽保湿补水喷雾', 8, 100, '箱');

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 7,'润泽保湿原液', 9, 100, '箱');

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 8,'润泽保湿泉能乳', 10, 100, '箱');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case2 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case3:删除一张已审核的退货单对应的退货单商品表数据 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iReturnCommoditySheetID = 2;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'case3 Testing Result';

SELECT '-------------------- Case4:删除一张不存在的退货单对应的退货单商品表数据 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iReturnCommoditySheetID = -2;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'case4 Testing Result';