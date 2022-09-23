
USE nbr_bx;

DROP TABLE IF EXISTS T_BXStaff; 	   
CREATE TABLE T_BXStaff		      				   -- ����ڲ�Ա����		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Mobile VARCHAR(11) NOT NULL,				   -- �ֻ�����
	F_pwdEncrypted VARCHAR(0) NULL DEFAULT NULL,				   -- ��Կ���ܺ���û�����
	F_Salt VARCHAR(32) NOT NULL,			   -- ����
	F_RoleID INT NOT NULL,						   -- ��ɫID
	F_DepartmentID INT NOT NULL,				   -- �û���ID
	F_Name VARCHAR(12) NOT NULL,   				   -- ����
	F_Sex int NOT NULL, 				   -- �Ա�
	F_ICID VARCHAR(20) NOT NULL,				   -- ���֤����

	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_Company; 	   
CREATE TABLE T_Company		       			   -- ��꿿ͻ���Ϣ		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(32) NOT NULL,				   -- ��˾����
	F_SN VARCHAR(8) NOT NULL,					   -- ��˾���
	F_BusinessLicenseSN VARCHAR(30) NOT NULL,	   -- Ӫҵִ�պ�	
	F_BusinessLicensePicture VARCHAR(128) NULL, 	   -- Ӫҵִ��ͼƬ
	F_BossName VARCHAR(12) NOT NULL,			   -- �ϰ�����
	F_BossPhone VARCHAR(32) NOT NULL, 			   -- �ϰ��ֻ�
	F_BossPassword VARCHAR(16) NOT NULL, 		   -- �ϰ��¼����
	F_BossWechat VARCHAR(20) NOT NULL,			   -- �ϰ�΢�ź�
	F_DBName Varchar(20) NOT NULL, 				   -- �˹�˾�����ݿ�����ơ������ƿ�������Ըù�˾��DB��jdbc����
	F_Key VARCHAR(32) NOT NULL,		 			   -- Կ��
	F_DBUserName VARCHAR(20) NOT NULL,		 	   -- DB�û���
	F_DBUserPassword VARCHAR(16) NOT NULL,		   -- DB����
	F_Status INT NOT NULL,                         -- 0=����Ӫҵ��Open��1=ͣҵ��Closed
	F_Submchid VARCHAR(10) NULL,				   -- ���̻���(�������۵���Ǯ�˿�)
	F_BrandName VARCHAR(20) NOT NULL,			   -- �̼�����,��ͬ��˾������ͬ�������޸�
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	F_ExpireDatetime DATETIME NOT NULL DEFAULT now(), 	 -- ʧЧʱ��
	F_ShowVipSystemTip INT NOT NULL DEFAULT 1, 	 -- �Ƿ���ʾ��Աϵͳ����ʾ���̼��ϰ忴
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

DROP TABLE IF EXISTS T_BXConfigGeneral; 	   			-- ��ͨ���ñ�
CREATE TABLE T_BXConfigGeneral
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(60) NOT NULL,					-- ���� 
	F_Value VARCHAR(128) NOT NULL,					-- ֵ
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CurrentRelease;
CREATE TABLE T_CurrentRelease					  -- ��¼Release��һЩ�汾��
	(
	F_ID INT NOT NULL AUTO_INCREMENT,
	F_WARReleaseNO VARCHAR (20) NOT NULL,		  -- ��ǰDB��Ӧ��WAR�汾
	F_DBReleaseNO VARCHAR (20) NOT NULL,		  -- ��ǰDB�İ汾��
	F_ApkReleaseNO VARCHAR (20) NOT NULL,		  -- ��ǰDB��Ӧ��Apk�汾
 	F_Jira VARCHAR (128) NOT NULL,				  -- �������ReleaseҪ���������ЩJIRA
	F_Remark VARCHAR (128) NOT NULL,
	F_CreateDatetime DATETIME NOT NULL,
	F_UpdateDatetime DATETIME NOT NULL,

	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';