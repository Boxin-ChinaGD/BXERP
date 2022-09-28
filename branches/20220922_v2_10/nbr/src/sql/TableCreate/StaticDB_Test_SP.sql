use staticdb;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'staticdb Running SP Test...';

delimiter $$ 

SELECT 'StaticDB_SP_TEST 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/StaticDB/Test_SP_RefCommodityHub_RetrieveN.sql

-- 检查公有DB staticdb的Test_SP数目有无变更
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'staticdb' AND ROUTINE_NAME LIKE 'SP\_%')$$
SELECT IF(@var = 1,'staticdb的Test_SP数目一致，检查成功！',concat('检查不成功！staticdb的Test_SP数目应该是', @var, '！请检查最近新增了哪些Test_SP！！')) AS TestSPNO$$

delimiter ;

select 'staticdb Running SP Test Stopped';