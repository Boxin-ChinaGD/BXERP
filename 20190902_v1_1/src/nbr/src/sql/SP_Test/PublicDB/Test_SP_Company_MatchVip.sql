SELECT '++++++++++++++++++ Test_SP_Company_MatchVip.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询nbr存在的会员，返回错误0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @sMobile = '13545678110';
SET @OpenID = '1586421134797';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;


CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 , '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:创建一个公司, 新建一个会员信息，根据该会员查询 -------------------------' AS 'Case2';

SET @iSourceCode = 0;
SET @sMobile = '13129355441';
SET @OpenID = '25548962145633';

-- 
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- 会员编号
	F_CardID INT NOT NULL,							   -- 外键 关联T_VipCard的ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS机的SN码
	F_Sex INT NOT NULL DEFAULT 1,				   -- 性别
	F_Logo VARCHAR(128) NULL,					   -- 头像?图片?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Email VARCHAR(30) NULL,	            	   -- 邮箱
	F_ConsumeTimes INT NOT NULL,				   -- 总消费次数
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- 总消费金额
	F_District VARCHAR(30) NOT NULL,			   -- 区域
	F_Category INT NOT NULL,					   -- 类别
	F_Birthday DATETIME NULL,			 		   -- 生日
	F_Bonus INT NOT NULL,			   			   -- 当前积分
	F_LastConsumeDatetime DATETIME NULL,		   -- 上次消费日期时间
	F_Remark VARCHAR(50) NULL,			  		   -- 备注		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- 创建表时，注释要两个-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','Ada','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10',@sMobile);

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (1, @iSourceCode, '', '', @OpenID);

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SET @sMobile = '13129350000';
SET @OpenID = '25548962145633';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case2 Testing Result';


SET @sMobile = '13129355441';
SET @OpenID = '25548962140000';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case2 Testing Result';

-- 删除新建的数据库
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case3:创建一个公司, 创建一个nbr拥有的会员 -------------------------' AS 'Case3';
-- 
SET @iSourceCode = 0;
SET @sMobile = '13545678110';
SET @OpenID = '1586421134797';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- 会员编号
	F_CardID INT NOT NULL,							   -- 外键 关联T_VipCard的ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS机的SN码
	F_Sex INT NOT NULL DEFAULT 1,				   -- 性别
	F_Logo VARCHAR(128) NULL,					   -- 头像?图片?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Email VARCHAR(30) NULL,	            	   -- 邮箱
	F_ConsumeTimes INT NOT NULL,				   -- 总消费次数
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- 总消费金额
	F_District VARCHAR(30) NOT NULL,			   -- 区域
	F_Category INT NOT NULL,					   -- 类别
	F_Birthday DATETIME NULL,			 		   -- 生日
	F_Bonus INT NOT NULL,			   			   -- 当前积分
	F_LastConsumeDatetime DATETIME NULL,		   -- 上次消费日期时间
	F_Remark VARCHAR(50) NULL,			  		   -- 备注		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- 创建表时，注释要两个-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','Ada','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10',@sMobile);

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (1, @iSourceCode, '', '', @OpenID);

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @sMobile = '13545678110';
SET @OpenID = '1586421134797';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 删除新建的数据库
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case4:所有公司都没有查询会员信息，返回错误码2 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @sMobile = '12312312312';
SET @OpenID = '123123123123123';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '未匹配到该会员信息', '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case5:该会员仅在一家公司注册并且是通过nbr注册的。返回错误码0并且更新了会员的OpenID,UnionID,Name,Sex -------------------------' AS 'Case5';
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL,							   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr创建的会员是无法获取到openID和UnionID的
SET @iVipSourceID = last_insert_id();

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @OpenID = '123123123123123';
SET @sUnionID = '1234567899';
SET @sName = 'VIP9999';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 检查openID,UnionID是否已经修改
SET @updateAfterOpenID = '';
SET @updateAfterUnionID = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID, @updateAfterUnionID FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID;
SELECT IF(@updateAfterOpenID = @OpenID AND @updateAfterUnionID = @sUnionID, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 检查Name,Sex是否已经修改
SET @updateAfterName = '';
SET @updateAfterSex = '';
SELECT F_Name, F_Sex INTO @updateAfterName, @updateAfterSex FROM nbr_2020.t_vip WHERE F_ID = @iVipID LIMIT 1;
SELECT IF(@updateAfterName = @sName AND @updateAfterSex = @iSex, '测试成功', '测试失败') AS 'Case5 Testing Result';

-- 删除新建的数据库
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case6:该会员在俩家公司进行注册;公司A的会员是通过nbr注册的,公司B的会员是通过小程序注册的。返回错误码0并且更新了会员的OpenID,UnionID,Name,Sex -------------------------' AS 'Case6';
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL,							   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyA = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr创建的会员是无法获取到openID和UnionID的
SET @iVipSourceID_CompanyA = last_insert_id();

-- 创建公司B
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2021;
-- 创建数据库
CREATE DATABASE nbr_2021
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2021;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL,							   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyB = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', 'a123456789', '123456789'); -- 小程序创建的会员是能获取到openID和UnionID的
SET @iVipSourceID_CompanyB = last_insert_id();

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iIDA = LAST_INSERT_ID();
--
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777778'/*F_BusinessLicenseSN*/, '/p/1813.jpg'/*F_BusinessLicensePicture*/, '老板2号', '13123615883', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789999'/*F_Key*/, 'nbr_2021', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iIDB = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @OpenID = '123123123123123';
SET @sUnionID = '1234567899';
SET @sName = 'VIP0001234';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 检查公司A的openID和UnionID是否已经修改
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = @sUnionID, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 检查公司A的name和Sex是否已经修改
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID_CompanyA;
SELECT IF(@updateAfterName_CompanyA = @sName AND @updateAfterSex_CompanyA = @iSex, '测试成功', '测试失败') AS 'Case6 Testing Result';

-- 检查是否没改变公司B的openID和UnionID
SET @updateAfterOpenID_CompanyB = '';
SET @updateAfterUnionID_CompanyB = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyB, @updateAfterUnionID_CompanyB FROM nbr_2021.t_vipsource WHERE F_ID = @iVipSourceID_CompanyB;
SELECT IF(@updateAfterOpenID_CompanyB = '123456789' AND @updateAfterUnionID_CompanyB = 'a123456789', '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 检查是否没改变公司B的Name和Sex
SET @updateAfterName_CompanyB = '';
SET @updateAfterSex_CompanyB = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyB, @updateAfterSex_CompanyB FROM nbr_2021.t_vip WHERE F_ID = @iVipID_CompanyB;
SELECT IF(@updateAfterName_CompanyB = 'VIP000001' AND @updateAfterSex_CompanyB = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

-- 删除新建的数据库
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-------------------- Case7:该会员在俩家公司进行注册;两家公司都是通过nbr注册的。返回错误码0并且更新了会员的OpenID,UnionID -------------------------' AS 'Case7';
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL,							   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyA = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr创建的会员是无法获取到openID和UnionID的
SET @iVipSourceID_CompanyA = last_insert_id();

-- 创建公司B
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2021;
-- 创建数据库
CREATE DATABASE nbr_2021
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2021;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL, 						   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyB = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- 小程序创建的会员是能获取到openID和UnionID的
SET @iVipSourceID_CompanyB = last_insert_id();

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iIDA = LAST_INSERT_ID();
--
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777778'/*F_BusinessLicenseSN*/, '/p/1813.jpg'/*F_BusinessLicensePicture*/, '老板2号', '13123615883', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789999'/*F_Key*/, 'nbr_2021', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iIDB = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @OpenID = '123123123123123';
SET @sUnionID = '1234567899';
SET @sName = 'test';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 检查公司A的openID和UnionID是否已经修改
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = @sUnionID, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 检查公司A的Name和Sex是否已经修改
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID_CompanyA;
SELECT IF(@updateAfterName_CompanyA = @sName AND @updateAfterSex_CompanyA = @iSex, '测试成功', '测试失败') AS 'Case7 Testing Result';

-- 检查公司B的openID和UnionID是否已经更改
SET @updateAfterOpenID_CompanyB = '';
SET @updateAfterUnionID_CompanyB = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyB, @updateAfterUnionID_CompanyB FROM nbr_2021.t_vipsource WHERE F_ID = @iVipSourceID_CompanyB;
SELECT IF(@updateAfterOpenID_CompanyB = @OpenID AND @updateAfterUnionID_CompanyB = @sUnionID, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 检查公司B的Name和Sex是否已经修改
SET @updateAfterName_CompanyB = '';
SET @updateAfterSex_CompanyB = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyB, @updateAfterSex_CompanyB FROM nbr_2021.t_vip WHERE F_ID = @iVipID_CompanyB;
SELECT IF(@updateAfterName_CompanyB = @sName AND @updateAfterSex_CompanyB = @iSex, '测试成功', '测试失败') AS 'Case7 Testing Result';

-- 删除新建的数据库
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-------------------- Case8:该会员并不是首次登陆,所以并不会传递UnionID,并不会更新会员来源表中的OpenID和UnionID -------------------------' AS 'Case8';
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2020;
-- 创建数据库
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2020;
-- 创建会员表
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Status int NOT NULL,						   -- 状态
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_Name VARCHAR(12) NOT NULL,				   -- 姓名
	F_Sex INT NOT NULL,  						   -- 性别
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 创建会员来源表
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- 插入数据
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', '123123123123123'); -- nbr创建的会员是无法获取到openID和UnionID的
SET @iVipSourceID_CompanyA = last_insert_id();

USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iIDA = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSourceCode = 0;
SET @OpenID = '123123123123123';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 检查会员的UnionID和OpenID是否没改变
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = '', '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 检查会员的Name和Sex是否没改变
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID;
SELECT IF(@updateAfterName_CompanyA = 'VIP000001' AND @updateAfterSex_CompanyA = 1, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iIDA;