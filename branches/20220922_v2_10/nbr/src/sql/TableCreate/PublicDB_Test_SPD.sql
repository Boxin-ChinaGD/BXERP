use nbr_bx;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'nbr_bx Running SPD Test...';

delimiter $$ 

-- ��鹫��DB nbr_bx��Test_SPD��Ŀ���ޱ��
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'nbr_bx' AND ROUTINE_NAME LIKE 'SPD\_%')$$
SELECT IF(@var = 0,'nbr_bx��Test_SPD��Ŀһ�£����ɹ���',concat('��鲻�ɹ���nbr_bx��Test_SPD��ĿӦ����', @var, '�����������������ЩTest_SPD����')) AS TestSPDNO$$


delimiter ;