use nbr_bx;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'nbr_bx Running SPD Test...';

delimiter $$ 

-- 检查公有DB nbr_bx的Test_SPD数目有无变更
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'nbr_bx' AND ROUTINE_NAME LIKE 'SPD\_%')$$
SELECT IF(@var = 0,'nbr_bx的Test_SPD数目一致，检查成功！',concat('检查不成功！nbr_bx的Test_SPD数目应该是', @var, '！请检查最近新增了哪些Test_SPD！！')) AS TestSPDNO$$


delimiter ;