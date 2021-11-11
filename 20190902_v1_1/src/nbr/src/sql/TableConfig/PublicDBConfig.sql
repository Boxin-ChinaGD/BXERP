
-- 公司营业执照的目录 改该路径需要同步改pom.xml和baseAction的CompanyBusinessLicensePictureDir
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyBusinessLicensePictureDir', 'D:/nbr/pic/common_db/license/');	
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyBusinessLicensePictureVolumeMax', '102400');	-- 公司营业执照的大小限制
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityNOStart', '-1'); -- 商品数量的默认期初值，代表期初没有数量，即店里最初没有该商品
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityPurchasingPriceStart', '-1.000000'); -- 商品采购价的默认期初值，代表期初没有过采购，即店里最初没有该商品
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityLogoDir', 'D:/nbr/pic/private_db/');	-- 商品图片的目录。后面部分存在T_Commdity.F_Picture中，示例：nbr1/1.jpg
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyAPICertDir', 'D:/nbr/apicert/');	-- API证书的的目录。命名示例：nbr1.p12
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyAPICertVolumeMax', '10240');	-- API证书的大小限制
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_SmallSheetNumber', '10');  -- 小票格式的最大数量
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_SmallSheetLogoVolume', '163840'); -- 小票格式图片的大小最大值
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestCountIn1Day', '60000'); -- 一天中请求服务器的最大次数。超过这个值，log4j会发警报邮件
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestCountIn1Min', '600'); -- 一分钟内请求服务器的最大次数。超过这个值，log4j会发警报邮件
-- 请求服务器的次数超过最大值后，log4j会发警报邮件。请求次数继续增长，每次超过一定的增量后，会重复发警报邮件。设计这个业务逻辑可以避免过多的警报邮件。
-- 比如，MaxRequestCountIn1Min设置为600，增量为600X0.1=60。当1分钟内的访问次数超过600时，会发送警报邮件。超过660时，再发一次。超过720时，再发一次。
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestIncrementPercent', '0.1'); 
-- 公司Logo的目录 改该路径需要同步改pom.xml和baseAction的CompanyLogoDir
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyLogoDir', 'D:/nbr/pic/common_db/logo/');
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyLogoVolumeMax', '102400'); -- 公司logo的大小限制
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxLoginCountIn1Day', '50'); -- 一个用户一天最大尝试登录次数。超过这个次数会被认为是黑客在爆破密码，会发报警邮件出来
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_IMPORT_FILE_SIZE', '5242880'); -- 文件上传限制大小 5 * 1024 * 1024
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('EXTRA_DISK_SPACE_SIZE', '104807600'); -- 磁盘空间剩余大小 100 * 1024 * 1024
