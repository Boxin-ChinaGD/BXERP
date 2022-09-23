-- ɾ��Posʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ��Pos��
-- ����errorMsg��������ɾ��POS
DROP FUNCTION IF EXISTS Func_CheckPosDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPosDependency`(
	iPosID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
--	IF EXISTS (SELECT 1 FROM t_staff WHERE F_IDInPOS = iPosID AND F_Status = 0) THEN
--		SET sErrorMsg := '��POS���ѱ�Ա��ʹ�ã�����ɾ��';
	IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS�������۵�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeAggregation WHERE F_PosID = iPosID) THEN
		SET sErrorMsg := '��POS����������������������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_PromotionSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS���д���ͬ��������ȱ�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_CommoditySyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS������Ʒͬ��������ȱ�����������ɾ��';	   
	ELSEIF EXISTS(SELECT 1 FROM T_BrandSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS����Ʒ��ͬ��������ȱ�����������ɾ��';
   	ELSEIF EXISTS(SELECT 1 FROM T_CategorySyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS�������ͬ��������ȱ�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM T_BarcodesSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '��POS����������ͬ��������ȱ�����������ɾ��'; 						
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;