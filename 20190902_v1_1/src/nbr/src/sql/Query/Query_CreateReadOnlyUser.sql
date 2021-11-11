CREATE USER 'userReadOnly'@'%' identified BY 'psb666666';
grant select,show view on *.* to 'userReadOnly'@'%';
flush privileges;

show grants;

