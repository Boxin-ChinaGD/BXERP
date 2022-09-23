SELECT '++++++++++++++++++ Test_SP_Company_RetrieveNByVip.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ���� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';




SELECT '-------------------- Case2:����һ����˾, �ù�˾���ֻ���Ϊ13545678110��VIP�������Ƿ��ظù�˾ -------------------------' AS 'Case2';


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '13545678110';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_Company_RetrieveNByVipMobile(@iErrorCode, @sErrorMsg, @sMobile, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;

-- 
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2020;
-- �������ݿ�
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2020;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','��ȫ��','1239999@bx.vip',11,110.8,'����',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 and @iTotalRecord = @iOldTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;




SELECT '-------------------- Case3:����һ����˾, �ù�˾û���ֻ���Ϊ13545678110��VIP��������û�з��ظù�˾ -------------------------' AS 'Case3';
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
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2020;
-- �������ݿ�
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2020;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','��ȫ��','1239999@bx.vip',11,110.8,'����',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;





SELECT '-------------------- Case4:����3����˾, ��˾�����ֻ���Ϊ13545678110��VIP����@iPageIndex = 1��@iPageSize = 2������������ȷ��ҳ,  �����������ݣ�iTotalRecordӦ����4(����nbr) -------------------------' AS 'Case4';
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
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2020;
-- �������ݿ�
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2020;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','��ȫ��','1239999@bx.vip',11,110.8,'����',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777770'/*F_BusinessLicenseSN*/, '/p/180.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789090'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
SET @iID1 = LAST_INSERT_ID();


-- 
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2021;
-- �������ݿ�
CREATE DATABASE nbr_2021
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2021;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','��ȫ��','1239999@bx.vip',11,110.8,'����',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777771'/*F_BusinessLicenseSN*/, '/p/181.jpg'/*F_BusinessLicensePicture*/, '/p/11.jpg'/*F_Logo*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789091'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
SET @iID2 = LAST_INSERT_ID();


-- 
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2022;
-- �������ݿ�
CREATE DATABASE nbr_2022
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2022;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10','13545678110');
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000002',1,'320803200107016031','��ȫ��','1239999@bx.vip',11,110.8,'����',2,0,
'2017-11-06',0,'2017-09-08 23:59:10','13545678111');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2022'/*F_Name*/, '668871', '87777777777772'/*F_BusinessLicenseSN*/, '/p/182.jpg'/*F_BusinessLicensePicture*/, '/p/1231.jpg'/*F_Logo*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2022'/*F_DBName*/, '22345678901234567890123456789092'/*F_Key*/, 'nbr_2022', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 3, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DROP DATABASE nbr_2022;
-- ɾ�������ı�����
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;
DELETE FROM t_company WHERE F_ID = @iID3;


SELECT '-------------------- Case5: ����������˾,������˾�����ֻ���Ϊ15200702314�Ļ�Ա�������мҹ�˾�Ѿ���ͣҵ״̬������ʱ,����1�� -------------------------' AS 'Case5';
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2020;
-- �������ݿ�
CREATE DATABASE nbr_2020
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2020;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,'2017-08-06',0,'2017-08-08 23:59:10','15200702314');

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777770'/*F_BusinessLicenseSN*/, '/p/180.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789090'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
SET @iID1 = LAST_INSERT_ID();

-- 
-- ɾ�����ݿ�
DROP DATABASE IF EXISTS nbr_2021;
-- �������ݿ�
CREATE DATABASE nbr_2021
CHARACTER   SET=utf8
COLLATE=utf8_unicode_ci;
-- ʹ���½������ݿ�
USE nbr_2021;
-- ������Ա��
DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,							   -- ��� ����T_VipCard��ID
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	-- FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID), -- ������ʱ��ע��Ҫ����-- -- 
--	-- FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','giggs','123456@bx.vip',5,99.8,'����',1,0,'2017-08-06',0,'2017-08-08 23:59:10','15200702314');
-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777771'/*F_BusinessLicenseSN*/, '/p/181.jpg'/*F_BusinessLicensePicture*/, '/p/11.jpg'/*F_Logo*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789091'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 1 /*F_Status*/, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 1, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
-- ɾ�������ı�����
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;
