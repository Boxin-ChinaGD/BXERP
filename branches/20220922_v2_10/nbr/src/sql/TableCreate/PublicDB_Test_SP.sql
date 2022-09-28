use nbr_bx;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'nbr_bx Running SP Test...';

delimiter $$ 

SELECT 'Public_SP_TEST 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_BXConfigGeneral_Retrieve1.sql
SELECT 'Public_SP_TEST 2'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_BXConfigGeneral_RetrieveN.sql
SELECT 'Public_SP_TEST 3'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_BXConfigGeneral_Update.sql
SELECT 'Public_SP_TEST 4'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_BxStaff_Retrieve1.sql
SELECT 'Public_SP_TEST 5'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_BxStaff_RetrieveN.sql
SELECT 'Public_SP_TEST 6'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_Create.sql
SELECT 'Public_SP_TEST 7'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_Delete.sql
SELECT 'Public_SP_TEST 8'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_MatchVip.sql
SELECT 'Public_SP_TEST 9'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_Retrieve1.sql
SELECT 'Public_SP_TEST 10'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_RetrieveN.sql
SELECT 'Public_SP_TEST 11'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_RetrieveNByVipMobile.sql
SELECT 'Public_SP_TEST 12'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_RetrieveN_CheckUniqueField.sql
SELECT 'Public_SP_TEST 13'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_Update.sql
SELECT 'Public_SP_TEST 14'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_UpdateSubmchid.sql
SELECT 'Public_SP_TEST 15'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP_Test/PublicDB/Test_SP_Company_UpdateVipSystemTip.sql



-- ��鹫��DB nbr_bx��Test_SP��Ŀ���ޱ��
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'nbr_bx' AND ROUTINE_NAME LIKE 'SP\_%')$$
SELECT IF(@var = 15,'nbr_bx��Test_SP��Ŀһ�£����ɹ���',concat('��鲻�ɹ���nbr_bx��Test_SP��ĿӦ����', @var, '�����������������ЩTest_SP����')) AS TestSPNO$$

delimiter ;

select 'nbr_bx Running SP Test Stopped';