SELECT '-------------------- Case1������15λ������� -------------------------' AS 'Case2';
SET @iCounts := 15;

SELECT Func_GenerateCouponSN(@iCounts) INTO @sNewSN;
SELECT @sNewSN;
SELECT IF((SELECT LENGTH(@sNewSN) = 15), '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
