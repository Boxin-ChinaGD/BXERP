SELECT '++++++++++++++++++ Test_SPD_InventoryCommodity_CheckInventorySheetID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodity_CheckInventorySheetID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

--	������
--	SELECT '-------------------- Case2:�̵㵥��Ӧ���̵㵥ID����ȷ -------------------------' AS 'Case2';
--	INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
--	VALUES (-1, 1, '��Ƭ', 1, 1, 1, 0, 0);
--	SET @iInventoryCommodityID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_InventoryCommodity_CheckInventorySheetID(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'��Ӧ���̵㵥ID����ȷ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--	-- 
--	DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventorysheetCommodityID;