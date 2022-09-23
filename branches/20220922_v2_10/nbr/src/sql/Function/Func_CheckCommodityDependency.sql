-- ������Ʒ�Ƿ������棨���������ȼ�¼��
-- ����ֵ��
-- 1��������������ɾ������Ʒ��
-- 0��������������ɾ������Ʒ��
drop function IF EXISTS Func_CheckCommodityDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckCommodityDependency`(
	iID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	DECLARE NO INT;
	DECLARE iType INT;
	
	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iID; 
 --	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iID;
	
--	IF NO <> 0 
	IF EXISTS(SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_NO > 0) THEN
	    SET sErrorMsg := '����Ʒ���п�棬����ɾ��';
    ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iID) THEN
		SET sErrorMsg := '����Ʒ����Ʒ��Դ������������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_Status <> 4 AND F_ID IN(SELECT F_PurchasingOrderID FROM t_purchasingordercommodity WHERE F_CommodityID = iID)) THEN
		SET sErrorMsg := '����Ʒ�вɹ�������������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '����Ʒ���̵㵥����������ɾ��';	
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '����Ʒ�����۵�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '����Ʒ����ⵥ����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_promotionscope WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '����Ʒ�д�������������ɾ��';
--	���ڲ�������Ʒ��ʷ������
--	ELSEIF EXISTS(SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = iID) THEN
--   		SET sErrorMsg := '����Ʒ����Ʒ��ʷ����������ɾ��';
   	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_TopSaleCommodityID = iID) THEN
   		SET sErrorMsg := '����Ʒ�������ձ����ܱ�����������ɾ��';
   	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = iID) THEN
   		SET sErrorMsg := '����Ʒ����Ʒ���۱�������������ɾ��';
--  ���װ��Ʒ�����Ա��ɹ���ֻ�ܲɹ���Ʒ
--	ELSEIF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN	
--		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ�вɹ�������������ɾ��';
-- ֻ���̵��Ԥ��̭״̬��Ԥ��̭����ͨ��Ʒ
--	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ���̵㵥����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ�����۵�����������ɾ��';
--  ֻ������Ԥ��̭״̬����ͨ��Ʒ
--	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ����ⵥ����������ɾ��';
-- ���װ����������
-- 	ELSEIF EXISTS(SELECT 1 FROM t_promotionscope WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ�д�������������ɾ��';
--	���ڲ�������Ʒ��ʷ������
--	ELSEIF EXISTS(SELECT 1 FROM t_commodityhistory WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ����Ʒ��ʷ����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_TopSaleCommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ�������ձ����ܱ�����������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '�õ�Ʒ�Ķ��װ��Ʒ����Ʒ���۱�������������ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityID = iID AND F_Status <> 2) THEN
		SET sErrorMsg := '����ɾ����Ʒ�ĸ���λ';
	ELSEIF EXISTS(SELECT 1 FROM t_subcommodity ts WHERE ts.F_SubCommodityID = iID AND EXISTS (SELECT 1 FROM t_commodity c WHERE c.F_ID = ts.F_CommodityID AND c.F_Status <> 2)) THEN
		SET sErrorMsg := 'Ҫɾ������Ʒ�������Ʒ��һ���֣�����ɾ��';
	ELSEIF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iID) THEN
	  	SET sErrorMsg := '��Ʒ�ڲɹ��˻���������';
	ELSEIF EXISTS (SELECT 1 FROM t_couponscope WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '��Ʒ���Ż�ȯ��Χ����������ɾ��';
	ELSE 
		SET sErrorMsg := '';
	END IF;		
	RETURN sErrorMsg;	
END;