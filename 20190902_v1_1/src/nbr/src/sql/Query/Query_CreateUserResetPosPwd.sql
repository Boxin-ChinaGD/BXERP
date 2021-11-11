
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

-- 查看系统用户
--	select Host,User,Password from mysql.user;

-- 创建一个远程用户 
--	create user test1 identified by '123456'; 

-- 分配权限 
-- grant all privileges on *.* to 'test'@'%'identified by '123456' with grant option; 

-- 刷新mysql用户权限
-- flush privileges ; 

-- 修改指定用户密码 
-- update mysql.user set password=password('新密码') where User="test" and Host="localhost"; 