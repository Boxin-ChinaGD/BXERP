use staticdb;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'staticdb Running SPD Test...';

delimiter $$ 

SELECT 'Static_SP_TEST 1'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckBarcode.sql
SELECT 'Static_SP_TEST 2'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckName.sql
SELECT 'Static_SP_TEST 3'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckPriceRetail.sql
SELECT 'Static_SP_TEST 4'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckShelfLife.sql
SELECT 'Static_SP_TEST 5'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckSpecification.sql
SELECT 'Static_SP_TEST 6'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/SP_Test/Doctor/StaticDB/Test_SPD_RefCommodityHub_CheckType.sql


-- 检查公有DB staticdb的Test_SPD数目有无变更
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'staticdb' AND ROUTINE_NAME LIKE 'SPD\_%')$$
SELECT IF(@var = 6,'staticdb的Test_SPD数目一致，检查成功！',concat('检查不成功！staticdb的Test_SPD数目应该是', @var, '！请检查最近新增了哪些Test_SPD！！')) AS TestSPDNO$$


delimiter ;