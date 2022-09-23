-- ɾ����Աʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ����Ա��
-- ����errorMsg��������ɾ����Ա
DROP FUNCTION IF EXISTS Func_CheckVipDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckVipDependency`(
	iVipID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_RetailTrade WHERE F_VipID = iVipID) THEN
		SET sErrorMsg := '�û�Ա�����۵�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_VIPPointHistory WHERE F_VIP_ID = iVipID) THEN
		SET sErrorMsg := '�û�Ա�л�Ա��������������ɾ��';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;