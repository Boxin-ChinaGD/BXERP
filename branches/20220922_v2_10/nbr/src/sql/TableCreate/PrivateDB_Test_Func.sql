use nbr;

delimiter $$

SELECT 'Test PrivateDB Functions'$$
SELECT 'FUNC_TEST 1'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckCommodityDependency.sql
SELECT 'FUNC_TEST 2'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckPosDependency.sql
SELECT 'FUNC_TEST 3'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckPromotionDependency.sql
SELECT 'FUNC_TEST 4'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckProviderDependency.sql
SELECT 'FUNC_TEST 5'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckPurchasingOrderDependency.sql
SELECT 'FUNC_TEST 6'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckStaffDependency.sql
SELECT 'FUNC_TEST 7'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckVipDependency.sql
SELECT 'FUNC_TEST 8'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CheckWarehouseDependency.sql
SELECT 'FUNC_TEST 9'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CreateCommodityHistory.sql
SELECT 'FUNC_TEST 10'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_CreateRetailTradeCommoditySource.sql
SELECT 'FUNC_TEST 11'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_DeleteRetailTradeCommoditySource.sql
SELECT 'FUNC_TEST 12'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_DeleteWarehousingForReturnCommoditySheet.sql
SELECT 'FUNC_TEST 13'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_GenerateCouponSN.sql
SELECT 'FUNC_TEST 14'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_GenerateSN.sql
SELECT 'FUNC_TEST 15'$$
source D:/BXERP/branches/20190902_v1_1/src/nbr/src/sql/Function_Test/Test_Func_ValidateStateChange.sql


-- ���˽��DB nbr��Test_Function��Ŀ���ޱ��
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'nbr' AND ROUTINE_NAME LIKE 'Func\_%')$$
SELECT IF(@var = 15,'nbr��Test_Function��Ŀһ�£����ɹ���',concat('��鲻�ɹ���nbr��Test_Function��ĿӦ����', @var, '�����������������ЩTest_Function����')) AS TestFuncNO$$

delimiter ;