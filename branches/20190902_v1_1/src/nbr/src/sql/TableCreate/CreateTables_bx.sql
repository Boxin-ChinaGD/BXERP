
USE nbr_bx;

DROP TABLE IF EXISTS T_BXStaff; 	   
CREATE TABLE T_BXStaff		      				   -- 博昕内部员工表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Mobile VARCHAR(11) NOT NULL,				   -- 手机号码
	F_pwdEncrypted VARCHAR(0) NULL DEFAULT NULL,				   -- 公钥加密后的用户密码
	F_Salt VARCHAR(32) NOT NULL,			   -- 密码
	F_RoleID INT NOT NULL,						   -- 角色ID
	F_DepartmentID INT NOT NULL,				   -- 用户组ID
	F_Name VARCHAR(12) NOT NULL,   				   -- 姓名
	F_Sex int NOT NULL, 				   -- 性别
	F_ICID VARCHAR(20) NOT NULL,				   -- 身份证号码

	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_Company; 	   
CREATE TABLE T_Company		       			   -- 博昕客户信息		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(32) NOT NULL,				   -- 公司名称
	F_SN VARCHAR(8) NOT NULL,					   -- 公司编号
	F_BusinessLicenseSN VARCHAR(30) NOT NULL,	   -- 营业执照号	
	F_BusinessLicensePicture VARCHAR(128) NULL, 	   -- 营业执照图片
	F_BossName VARCHAR(12) NOT NULL,			   -- 老板名字
	F_BossPhone VARCHAR(32) NOT NULL, 			   -- 老板手机
	F_BossPassword VARCHAR(16) NOT NULL, 		   -- 老板登录密码
	F_BossWechat VARCHAR(20) NOT NULL,			   -- 老板微信号
	F_DBName Varchar(20) NOT NULL, 				   -- 此公司的数据库的名称。此名称可用于针对该公司的DB的jdbc连接
	F_Key VARCHAR(32) NOT NULL,		 			   -- 钥匙
	F_DBUserName VARCHAR(20) NOT NULL,		 	   -- DB用户名
	F_DBUserPassword VARCHAR(16) NOT NULL,		   -- DB密码
	F_Status INT NOT NULL,                         -- 0=正常营业的Open，1=停业的Closed
	F_Submchid VARCHAR(10) NULL,				   -- 子商户号(用于零售的收钱退款)
	F_BrandName VARCHAR(20) NOT NULL,			   -- 商家招牌,不同公司可以相同，可以修改
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	F_ExpireDatetime DATETIME NOT NULL DEFAULT now(), 	 -- 失效时间
	F_ShowVipSystemTip INT NOT NULL DEFAULT 1, 	 -- 是否显示会员系统的提示给商家老板看
	F_Logo VARCHAR(128) NULL,
	PRIMARY KEY (F_ID),
	UNIQUE (F_DBName),
	UNIQUE (F_BusinessLicenseSN),
	UNIQUE (F_BusinessLicensePicture),
	UNIQUE (F_Logo),
	UNIQUE (F_Name),
	UNIQUE (F_Key)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BXConfigGeneral; 	   			-- 普通配置表
CREATE TABLE T_BXConfigGeneral
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(60) NOT NULL,					-- 名称 
	F_Value VARCHAR(128) NOT NULL,					-- 值
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CurrentRelease;
CREATE TABLE T_CurrentRelease					  -- 记录Release的一些版本号
	(
	F_ID INT NOT NULL AUTO_INCREMENT,
	F_WARReleaseNO VARCHAR (20) NOT NULL,		  -- 当前DB对应的WAR版本
	F_DBReleaseNO VARCHAR (20) NOT NULL,		  -- 当前DB的版本号
	F_ApkReleaseNO VARCHAR (20) NOT NULL,		  -- 当前DB对应的Apk版本
 	F_Jira VARCHAR (128) NOT NULL,				  -- 表明这次Release要解决的是哪些JIRA
	F_Remark VARCHAR (128) NOT NULL,
	F_CreateDatetime DATETIME NOT NULL,
	F_UpdateDatetime DATETIME NOT NULL,

	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';