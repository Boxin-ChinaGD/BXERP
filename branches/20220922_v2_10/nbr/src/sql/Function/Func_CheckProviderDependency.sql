-- ɾ����Ӧ��ʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ����Ӧ�̡�
-- ����errorMsg��������ɾ����Ӧ��
DROP FUNCTION IF EXISTS Func_CheckProviderDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckProviderDependency`(
	iProviderID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM t_providercommodity WHERE F_ProviderID = iProviderID) THEN
		SET sErrorMsg := '��Ӧ���Ѿ�������Ʒ������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_Warehousing WHERE F_ProviderID = iProviderID) THEN
		SET sErrorMsg := '��Ӧ���ѱ���ⵥ���ã�����ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_ReturnCommoditySheet WHERE F_ProviderID = iProviderID) THEN 
		SET sErrorMsg := '��Ӧ���ѱ��˻������ã�����ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_PurchasingOrder WHERE F_ProviderID = iProviderID ) THEN 
		SET sErrorMsg := '��Ӧ���ѱ��ɹ��������ã�����ɾ��';	
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;