SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Retrieve1.sql ++++++++++++++++++++';

INSERT INTO T_ReturnCommoditySheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (5, 5,2);

SET @iID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID,F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, 1, 2,'�ɿڿ�', 20, '��', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';


DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;