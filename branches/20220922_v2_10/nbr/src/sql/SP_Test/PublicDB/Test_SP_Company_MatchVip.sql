SELECT '++++++++++++++++++ Test_SP_Company_MatchVip.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯnbr���ڵĻ�Ա�����ش���0 -------------------------' AS 'Case1';

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
SELECT IF(@iErrorCode = 0 , '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:����һ����˾, �½�һ����Ա��Ϣ�����ݸû�Ա��ѯ -------------------------' AS 'Case2';

SET @iSourceCode = 0;
SET @sMobile = '13129355441';
SET @OpenID = '25548962145633';

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
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','Ada','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10',@sMobile);

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (1, @iSourceCode, '', '', @OpenID);

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SET @sMobile = '13129350000';
SET @OpenID = '25548962145633';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SET @sMobile = '13129355441';
SET @OpenID = '25548962140000';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
-- 
CALL SP_Company_MatchVip(@iErrorCode, @sErrorMsg, @iSourceCode, @sMobile, @OpenID, @sUnionID, @sName, @iSex);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case3:����һ����˾, ����һ��nbrӵ�еĻ�Ա -------------------------' AS 'Case3';
-- 
SET @iSourceCode = 0;
SET @sMobile = '13545678110';
SET @OpenID = '1586421134797';
SET @sUnionID = '';
SET @sName = '';
SET @iSex = 0;
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
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Status,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000001',1,'320803199707016031','Ada','123456@bx.vip',5,99.8,'����',1,0,
'2017-08-06',0,'2017-08-08 23:59:10',@sMobile);

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (1, @iSourceCode, '', '', @OpenID);

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case4:���й�˾��û�в�ѯ��Ա��Ϣ�����ش�����2 -------------------------' AS 'Case4';
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
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = 'δƥ�䵽�û�Ա��Ϣ', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case5:�û�Ա����һ�ҹ�˾ע�Ტ����ͨ��nbrע��ġ����ش�����0���Ҹ����˻�Ա��OpenID,UnionID,Name,Sex -------------------------' AS 'Case5';
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL,							   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr�����Ļ�Ա���޷���ȡ��openID��UnionID��
SET @iVipSourceID = last_insert_id();

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- ���openID,UnionID�Ƿ��Ѿ��޸�
SET @updateAfterOpenID = '';
SET @updateAfterUnionID = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID, @updateAfterUnionID FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID;
SELECT IF(@updateAfterOpenID = @OpenID AND @updateAfterUnionID = @sUnionID, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- ���Name,Sex�Ƿ��Ѿ��޸�
SET @updateAfterName = '';
SET @updateAfterSex = '';
SELECT F_Name, F_Sex INTO @updateAfterName, @updateAfterSex FROM nbr_2020.t_vip WHERE F_ID = @iVipID LIMIT 1;
SELECT IF(@updateAfterName = @sName AND @updateAfterSex = @iSex, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-------------------- Case6:�û�Ա�����ҹ�˾����ע��;��˾A�Ļ�Ա��ͨ��nbrע���,��˾B�Ļ�Ա��ͨ��С����ע��ġ����ش�����0���Ҹ����˻�Ա��OpenID,UnionID,Name,Sex -------------------------' AS 'Case6';
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL,							   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyA = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr�����Ļ�Ա���޷���ȡ��openID��UnionID��
SET @iVipSourceID_CompanyA = last_insert_id();

-- ������˾B
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL,							   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyB = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', 'a123456789', '123456789'); -- С���򴴽��Ļ�Ա���ܻ�ȡ��openID��UnionID��
SET @iVipSourceID_CompanyB = last_insert_id();

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
SET @iIDA = LAST_INSERT_ID();
--
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777778'/*F_BusinessLicenseSN*/, '/p/1813.jpg'/*F_BusinessLicensePicture*/, '�ϰ�2��', '13123615883', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789999'/*F_Key*/, 'nbr_2021', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- ��鹫˾A��openID��UnionID�Ƿ��Ѿ��޸�
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = @sUnionID, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- ��鹫˾A��name��Sex�Ƿ��Ѿ��޸�
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID_CompanyA;
SELECT IF(@updateAfterName_CompanyA = @sName AND @updateAfterSex_CompanyA = @iSex, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- ����Ƿ�û�ı乫˾B��openID��UnionID
SET @updateAfterOpenID_CompanyB = '';
SET @updateAfterUnionID_CompanyB = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyB, @updateAfterUnionID_CompanyB FROM nbr_2021.t_vipsource WHERE F_ID = @iVipSourceID_CompanyB;
SELECT IF(@updateAfterOpenID_CompanyB = '123456789' AND @updateAfterUnionID_CompanyB = 'a123456789', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- ����Ƿ�û�ı乫˾B��Name��Sex
SET @updateAfterName_CompanyB = '';
SET @updateAfterSex_CompanyB = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyB, @updateAfterSex_CompanyB FROM nbr_2021.t_vip WHERE F_ID = @iVipID_CompanyB;
SELECT IF(@updateAfterName_CompanyB = 'VIP000001' AND @updateAfterSex_CompanyB = 1, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-------------------- Case7:�û�Ա�����ҹ�˾����ע��;���ҹ�˾����ͨ��nbrע��ġ����ش�����0���Ҹ����˻�Ա��OpenID,UnionID -------------------------' AS 'Case7';
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL,							   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyA = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- nbr�����Ļ�Ա���޷���ȡ��openID��UnionID��
SET @iVipSourceID_CompanyA = last_insert_id();

-- ������˾B
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL, 						   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID_CompanyB = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', ''); -- С���򴴽��Ļ�Ա���ܻ�ȡ��openID��UnionID��
SET @iVipSourceID_CompanyB = last_insert_id();

-- 
USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
SET @iIDA = LAST_INSERT_ID();
--
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2021'/*F_Name*/, '668870', '87777777777778'/*F_BusinessLicenseSN*/, '/p/1813.jpg'/*F_BusinessLicensePicture*/, '�ϰ�2��', '13123615883', '000000', 'a13123615881', 'nbr_2021'/*F_DBName*/, '22345678901234567890123456789999'/*F_Key*/, 'nbr_2021', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- ��鹫˾A��openID��UnionID�Ƿ��Ѿ��޸�
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = @sUnionID, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- ��鹫˾A��Name��Sex�Ƿ��Ѿ��޸�
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID_CompanyA;
SELECT IF(@updateAfterName_CompanyA = @sName AND @updateAfterSex_CompanyA = @iSex, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

-- ��鹫˾B��openID��UnionID�Ƿ��Ѿ�����
SET @updateAfterOpenID_CompanyB = '';
SET @updateAfterUnionID_CompanyB = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyB, @updateAfterUnionID_CompanyB FROM nbr_2021.t_vipsource WHERE F_ID = @iVipSourceID_CompanyB;
SELECT IF(@updateAfterOpenID_CompanyB = @OpenID AND @updateAfterUnionID_CompanyB = @sUnionID, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- ��鹫˾B��Name��Sex�Ƿ��Ѿ��޸�
SET @updateAfterName_CompanyB = '';
SET @updateAfterSex_CompanyB = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyB, @updateAfterSex_CompanyB FROM nbr_2021.t_vip WHERE F_ID = @iVipID_CompanyB;
SELECT IF(@updateAfterName_CompanyB = @sName AND @updateAfterSex_CompanyB = @iSex, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

-- ɾ���½������ݿ�
DROP DATABASE nbr_2020;
DROP DATABASE nbr_2021;
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-------------------- Case8:�û�Ա�������״ε�½,���Բ����ᴫ��UnionID,��������»�Ա��Դ���е�OpenID��UnionID -------------------------' AS 'Case8';
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
	F_Status int NOT NULL,						   -- ״̬
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_Name VARCHAR(12) NOT NULL,				   -- ����
	F_Sex INT NOT NULL,  						   -- �Ա�
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Mobile (F_Mobile)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ������Ա��Դ��
DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(100) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(100) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(100) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
-- ��������
SET @sMobile = '12312312312';
DELETE FROM T_VIP;
INSERT INTO T_VIP (F_Name, F_Mobile, F_Sex) VALUES ('VIP000001',@sMobile, 1);
SET @iVipID = last_insert_id();

DELETE FROM t_vipsource;
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (last_insert_id(), @iSourceCode, '', '', '123123123123123'); -- nbr�����Ļ�Ա���޷���ȡ��openID��UnionID��
SET @iVipSourceID_CompanyA = last_insert_id();

USE nbr_bx;
-- ���½��Ĺ�˾����nbr_bx��˾��
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('nbr_2020'/*F_Name*/, '668869', '87777777777777'/*F_BusinessLicenseSN*/, '/p/183.jpg'/*F_BusinessLicensePicture*/, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_2020'/*F_DBName*/, '22345678901234567890123456789099'/*F_Key*/, 'nbr_2020', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- ����Ա��UnionID��OpenID�Ƿ�û�ı�
SET @updateAfterOpenID_CompanyA = '';
SET @updateAfterUnionID_CompanyA = '';
SELECT F_ID3, F_ID2 INTO @updateAfterOpenID_CompanyA, @updateAfterUnionID_CompanyA FROM nbr_2020.t_vipsource WHERE F_ID = @iVipSourceID_CompanyA;
SELECT IF(@updateAfterOpenID_CompanyA = @OpenID AND @updateAfterUnionID_CompanyA = '', '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- ����Ա��Name��Sex�Ƿ�û�ı�
SET @updateAfterName_CompanyA = '';
SET @updateAfterSex_CompanyA = '';
SELECT F_Name, F_Sex INTO @updateAfterName_CompanyA, @updateAfterSex_CompanyA FROM nbr_2020.t_vip WHERE F_ID = @iVipID;
SELECT IF(@updateAfterName_CompanyA = 'VIP000001' AND @updateAfterSex_CompanyA = 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
DROP DATABASE nbr_2020;
DELETE FROM t_company WHERE F_ID = @iIDA;