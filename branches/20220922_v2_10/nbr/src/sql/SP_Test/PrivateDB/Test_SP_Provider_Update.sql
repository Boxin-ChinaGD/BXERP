SELECT '++++++++++++++++++ Test_SP_Provider_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �޸ĳ��ظ��Ĺ�Ӧ�����ƣ�������Ϊ1 -------------------------' AS 'Case1';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��è';		          
SET @sDistrictID = 2;     
SET @sAddress = '�����������';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s46565';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case2: �޸ĳ��ظ��Ĺ�Ӧ�̵绰���룬������Ϊ1 -------------------------' AS 'Case2';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���Ĺ�Ӧ��121';		          
SET @sDistrictID = 2;     
SET @sAddress = '�����������';   
SET @sContactName = 'kkk';     
SET @sMobile='13129355441';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;


SELECT '-------------------- Case3: �޸ĳɲ��ظ��Ĺ�Ӧ�����ƣ�������Ϊ0 -------------------------' AS 'Case3';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���Ĺ�Ӧ��121';		          
SET @sDistrictID = 2;     
SET @sAddress = '�����������';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s465652';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case4: ��Ӧ��IDΪ0��ʱ������޸ģ�������Ϊ0 -------------------------' AS 'Case4';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��99',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���Ĺ�Ӧ��111';		          
SET @sDistrictID = 1;     
SET @sAddress = '�����������';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s46565';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case5: ��Ӧ�̵绰����ΪNULL��ʱ������޸ģ�������Ϊ0 -------------------------' AS 'Case5';
INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��1',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���Ĺ�Ӧ��122';		          
SET @sDistrictID = 1;     
SET @sAddress = '';   
SET @sContactName = '';     
SET @sMobile = '';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case6:ֻ����绰����͹�Ӧ�����ƽ����޸ģ�������Ϊ0 -------------------------' AS 'Case6';
INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('������Ӧ��1',1,'�������������ʮ����ѧ','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���Ĺ�Ӧ��123';		          
SET @sDistrictID = 1;     
SET @sAddress = '';   
SET @sContactName = '';     
SET @sMobile = '1111111111';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;