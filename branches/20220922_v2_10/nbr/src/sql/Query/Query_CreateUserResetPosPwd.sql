
create user'userResetPosPwd'@'%' identified by'psb666666';
-- DEV & SIT & UAT
grant SELECT, UPDATE on nbr.t_pos to 'userResetPosPwd'@'%';  
-- SIT & UAT
grant SELECT, UPDATE on nbr_myj.t_pos to 'userResetPosPwd'@'%';
grant SELECT, UPDATE on nbr_xsd.t_pos to 'userResetPosPwd'@'%';
grant SELECT, UPDATE on nbr_frxs.t_pos to 'userResetPosPwd'@'%';
-- SIT
grant SELECT, UPDATE on test_name.t_pos to 'userResetPosPwd'@'%';
flush privileges;


/*
drop USER userResetPosPwd;

SELECT * FROM mysql.user;
*/

-- �鿴ϵͳ�û�
--	select Host,User,Password from mysql.user;

-- ����һ��Զ���û� 
--	create user test1 identified by '123456'; 

-- ����Ȩ�� 
-- grant all privileges on *.* to 'test'@'%'identified by '123456' with grant option; 

-- ˢ��mysql�û�Ȩ��
-- flush privileges ; 

-- �޸�ָ���û����� 
-- update mysql.user set password=password('������') where User="test" and Host="localhost"; 