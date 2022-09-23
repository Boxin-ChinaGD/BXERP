-- ɾ������ʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ��������
-- ����errorMsg��������ɾ������
DROP FUNCTION IF EXISTS Func_CheckPromotionDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPromotionDependency`(
	iPromotionID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_RetailTradePromotingFlow WHERE F_PromotionID = iPromotionID) THEN
		SET sErrorMsg := '�ô����Ѿ����ɹ�������Ϣ������ɾ��';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;