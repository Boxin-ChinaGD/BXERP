SET @InValue = -1;
SET @InValueString = '$';
SET @InNO = -100000000;
SET @iShopID = 2;

-- CASE1:�޸���Ʒ������ʷ
select Func_CreateCommodityHistory(51, '��Ʒ����', @InValueString, @InValueString, @InValue, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��Ʒ����') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��Ʒ����';


-- CASE2:�޸���������ʷ
select Func_CreateCommodityHistory(51, @InValueString, '11111111', @InValueString, @InValue, @InValue, -100000000, @InNO, 3, '3333333333333', @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '������') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '������';


-- CASE3:�޸���Ʒ�����ʷ
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, '��', @InValue, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���';


-- CASE4:�޸İ�װ��λ��ʷ
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, 5, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��װ��λ') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��װ��λ';


-- CASE5:�޸���Ʒ�����ʷ
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, 5, -100000000, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��Ʒ���') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '��Ʒ���';


-- CASE6:�޸����ۼ���ʷ
SET @OldValue = 999.9991111111111;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�') = 1 AND @OldValue = 1000.00, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�';


-- CASE7:�޸Ŀ����ʷ
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, -100000000, 999, 1, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���') = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���';


-- CASE8:�޸����ۼ���ʷ2
SET @OldValue = 123.456789;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�') = 1 AND @OldValue = 123.46, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�';


-- CASE8:�޸����ۼ���ʷ3
SET @OldValue = 654.321;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�') = 1 AND @OldValue = 654.32, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�';


-- CASE8:�޸����ۼ���ʷ4
SET @OldValue = 100;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�') = 1 AND @OldValue = 100.00, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�';


-- CASE8:�޸����ۼ���ʷ5
SET @OldValue = 100.00000000000;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�') = 1 AND @OldValue = 100.00, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '���ۼ�';