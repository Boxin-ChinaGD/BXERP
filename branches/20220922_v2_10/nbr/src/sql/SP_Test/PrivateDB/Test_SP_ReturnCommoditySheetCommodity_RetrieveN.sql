SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheetCommodity_RetrieveN.sql ++++++++++++++++++++';	

SELECT '-------------------- Case1:��ѯһ���˻������˻�����Ʒ�����������,��ʱδ��ˣ���Ʒ���ƴ���Ʒ���� -------------------------' AS 'Case1';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 6,'����ʪ��ˮ����', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 7,'����ʪԭҺ', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 8,'����ʪȪ����', 10, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID,9,'���󾫻���˪', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'�ɱȿ���Ƭ', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 2,'�ɿڿ���', 10, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 3,'���¿���', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 4,'�����Ұ�����', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 5,'��ʦ��ţ����', 10, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 10,'����������Ĥ', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 11,'ѩ���鼡��ˮ', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 12,'ѩ���鼡�ܾ���ԭҺ', 10, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 13,'ѩ���鼡����Һ', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 14,'ѩ���鼡����˪', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 15,'���ܻ�����Ĥ', 10, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 16,'ά���̵���ԭζ330ml', 8, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 17,'ά�����ʲ�300', 9, 100, '��', 8);

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 18,'ά������', 10, 100, '��', 8);

SET @iErrorCode = 0; 
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 100000000;
SET @iTotalRecord = 0;

CALL SP_ReturnCommoditySheetCommodity_RetrieveN (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 18 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;