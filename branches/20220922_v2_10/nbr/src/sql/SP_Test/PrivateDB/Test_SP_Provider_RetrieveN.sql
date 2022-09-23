SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����sName��Ϊ������ѯ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT @iTotalRecord;

SELECT '-------------------- Case2: ����iDistrictID��Ϊ������ѯ -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = 1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case3: ����sAddress��Ϊ������ѯ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '����';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT @iTotalRecord;

SELECT '-------------------- Case4: ����sContactName��Ϊ������ѯ -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = 'Tom';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case5: ����sMobile��Ϊ������ѯ -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='13129355444';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case6: �����κ�ֵ����� -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
SELECT @iTotalRecord;