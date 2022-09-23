SELECT '++++++++++++++++++ Test_SP_Inventory_UpdateCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �����޸��̵㵥��Ʒ -------------------------' AS 'Case1';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (1, 30, '��Ƭ', '��', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case2: �̵㵥���ύ ���ش�����7 -------------------------' AS 'Case2';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (6, 30, '��Ƭ', '��', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case3: �̵㵥����� ���ش�����7 -------------------------' AS 'Case3';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (8, 30, '��Ƭ', '��', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = 300;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;

SELECT '-------------------- Case4: �޸ĵ�ʵ����������0 ���ش�����7 -------------------------' AS 'Case4';

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (1, 30, '��Ƭ', '��', 1, 1, 100, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iNOReal = -1;					   			  		    			   
CALL SP_Inventory_UpdateCommodity(@iErrorCode, @sErrorMsg, @iID, @iNOReal);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @iID AND F_NOReal = @iNOReal;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID;