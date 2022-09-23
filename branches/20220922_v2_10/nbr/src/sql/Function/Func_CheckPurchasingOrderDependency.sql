-- ɾ���ɹ���ʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ���ɹ�����
-- ����errorMsg��������ɾ���ɹ���
DROP FUNCTION IF EXISTS Func_CheckPurchasingOrderDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPurchasingOrderDependency`(
	iPurchasingOrderID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_Warehousing WHERE F_PurchasingOrderID = iPurchasingOrderID) THEN
		SET sErrorMsg := '�òɹ�������⣬������ɾ��';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;