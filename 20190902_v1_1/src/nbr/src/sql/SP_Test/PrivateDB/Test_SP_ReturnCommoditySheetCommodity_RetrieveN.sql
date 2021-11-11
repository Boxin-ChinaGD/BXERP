SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheetCommodity_RetrieveN.sql ++++++++++++++++++++';	

SELECT '-------------------- Case1:查询一张退货单的退货单商品表的所有数据,此时未审核，商品名称从商品表拿 -------------------------' AS 'Case1';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 6,'润泽保湿补水喷雾', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 7,'润泽保湿原液', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 8,'润泽保湿泉能乳', 10, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID,9,'润泽精华乳霜', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'可比克薯片', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 2,'可口可乐', 10, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 3,'百事可乐', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 4,'不二家棒棒糖', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 5,'康师傅牛肉面', 10, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 10,'润泽玻尿酸面膜', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 11,'雪晶灵肌密水', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 12,'雪晶灵肌密精华原液', 10, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 13,'雪晶灵肌密乳液', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 14,'雪晶灵肌密面霜', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 15,'肌密焕彩面膜', 10, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 16,'维他奶低糖原味330ml', 8, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 17,'维他柠檬茶300', 9, 100, '箱', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 18,'维他柠檬', 10, 100, '箱', 8);

SET @iErrorCode = 0; 
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 100000000;
SET @iTotalRecord = 0;

CALL SP_ReturnCommoditySheetCommodity_RetrieveN (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 18 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;