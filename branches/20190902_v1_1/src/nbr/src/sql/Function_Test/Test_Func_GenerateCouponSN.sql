SELECT '-------------------- Case1：生成15位随机数字 -------------------------' AS 'Case2';
SET @iCounts := 15;

SELECT Func_GenerateCouponSN(@iCounts) INTO @sNewSN;
SELECT @sNewSN;
SELECT IF((SELECT LENGTH(@sNewSN) = 15), '测试成功', '测试失败') AS 'Case1 Testing Result';
