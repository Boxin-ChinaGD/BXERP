SELECT '++++++++++++++++++ Test_SP_Company_RetrieveNByVip.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';




SELECT '-------------------- Case2:创建一个公司, 该公司有手机号为13545678110的VIP，期望是返回该公司 -------------------------' AS 'Case2';


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;

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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','张全蛋','1239999@bx.vip',11,110.8,'深圳',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 and @iTotalRecord = @iOldTotalRecord + 1, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 删除新建的数据库
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;




SELECT '-------------------- Case3:创建一个公司, 该公司没有手机号为13545678110的VIP，期望是没有返回该公司 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','张全蛋','1239999@bx.vip',11,110.8,'深圳',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 删除新建的数据库
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;





SELECT '-------------------- Case4:创建3个公司, 公司都有手机号为13545678110的VIP，传@iPageIndex = 1，@iPageSize = 2，期望是能正确分页,  返回两条数据，iTotalRecord应等于4(包含nbr) -------------------------' AS 'Case4';
-- 
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','张全蛋','1239999@bx.vip',11,110.8,'深圳',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777770'/*F_BusinessLicenseSN*/, '/p/180.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789090'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID1 = LAST_INSERT_ID();


-- 
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','张全蛋','1239999@bx.vip',11,110.8,'深圳',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777771'/*F_BusinessLicenseSN*/, '/p/181.jpg'/*F_BusinessLicensePicture*/, '/p/11.jpg'/*F_Logo*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789091'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID2 = LAST_INSERT_ID();


-- 
-- 删除数据库
DROP DATABASE IF EXISTS nbr_2022;
-- 创建数据库
CREATE DATABASE nbr_2022
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- 使用新建的数据库
USE nbr_2022;
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','张全蛋','1239999@bx.vip',11,110.8,'深圳',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2022'/*F_Name*/, '668871', '87777777777772'/*F_BusinessLicenseSN*/, '/p/182.jpg'/*F_BusinessLicensePicture*/, '/p/1231.jpg'/*F_Logo*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2022'/*F_DBName*/, '22345678901234567890123456789092'/*F_Key*/, 'nbr_2022', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID3 = LAST_INSERT_ID();

-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 2;
SET @iTotalRecord = 0;
-- 
CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 3, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 删除新建的数据库
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DROP DATABASE nbr_2022;
-- 删除新增的表数据
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;
DELETE FROM t_company WHERE F_ID = @iID3;


SELECT '-------------------- Case5: 创建俩个公司,俩个公司都有手机号为15200702314的会员。但是有家公司已经是停业状态。查找时,返回1家 -------------------------' AS 'Case5';
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,'2017-08-06',0,'2017-08-08 23:59:10','15200702314');

-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777770'/*F_BusinessLicenseSN*/, '/p/180.jpg'/*F_BusinessLicensePicture*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789090'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID1 = LAST_INSERT_ID();

-- 
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
-- 插入数据
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'广州',1,0,'2017-08-06',0,'2017-08-08 23:59:10','15200702314');
-- 
USE nbr_bx;
-- 将新建的公司插入nbr_bx公司表
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777771'/*F_BusinessLicenseSN*/, '/p/181.jpg'/*F_BusinessLicensePicture*/, '/p/11.jpg'/*F_Logo*/, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789091'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 1 /*F_Status*/, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID2 = LAST_INSERT_ID();

-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '15200702314';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 删除新建的数据库
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
-- 删除新增的表数据
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;
