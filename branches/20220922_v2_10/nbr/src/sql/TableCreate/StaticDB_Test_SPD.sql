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


-- ��鹫��DB staticdb��Test_SPD��Ŀ���ޱ��
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'staticdb' AND ROUTINE_NAME LIKE 'SPD\_%')$$
SELECT IF(@var = 6,'staticdb��Test_SPD��Ŀһ�£����ɹ���',concat('��鲻�ɹ���staticdb��Test_SPD��ĿӦ����', @var, '�����������������ЩTest_SPD����')) AS TestSPDNO$$


delimiter ;