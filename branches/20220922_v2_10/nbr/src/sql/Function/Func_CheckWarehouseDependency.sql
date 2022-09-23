-- ɾ���ֿ�ʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ���ֿ⡣
-- ����errorMsg��������ɾ���ֿ�
DROP FUNCTION IF EXISTS Func_CheckWarehouseDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckWarehouseDependency`(
	iWarehouseID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_Warehousing WHERE F_WarehouseID = iWarehouseID) THEN
		SET sErrorMsg := '�òֿ��ѱ���ⵥʹ�ã�����ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_InventorySheet WHERE F_WarehouseID = iWarehouseID) THEN
		SET sErrorMsg := '�òֿ��ѱ��̵㵥ʹ�ã�����ɾ��';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;