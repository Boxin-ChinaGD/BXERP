use staticdb;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'staticdb Running SP Test...';

delimiter $$ 

SELECT 'StaticDB_SP_TEST 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/StaticDB/Test_SP_RefCommodityHub_RetrieveN.sql

-- ��鹫��DB staticdb��Test_SP��Ŀ���ޱ��
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'staticdb' AND ROUTINE_NAME LIKE 'SP\_%')$$
SELECT IF(@var = 1,'staticdb��Test_SP��Ŀһ�£����ɹ���',concat('��鲻�ɹ���staticdb��Test_SP��ĿӦ����', @var, '�����������������ЩTest_SP����')) AS TestSPNO$$

delimiter ;

select 'staticdb Running SP Test Stopped';