
-- CASE1:��Status��ѯ
SELECT '-------------------- Case1: ��Status��ѯ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sDistrict = "";
SET @iCategory = -1;
SET @sMobile = "";
SET @sICID = "";
SET @sEmail = "";
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_VIP_RetrieveN(@iErrorCode, @sErrorMsg, @sDistrict, @iCategory, @sMobile, @sICID, @sEmail, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- CASE2:��Status��IDInPOS��ѯ
SELECT '-------------------- CASE2:��Status��IDInPOS��ѯ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iIDInPOS = 1;
SET @sDistrict = "";
SET @iCategory = -1;
SET @sMobile = "";
SET @sICID = "";
SET @sEmail = "";
SET @sAccount = "";
SET @iPageIndex = 1;
SET @iPageSize = 10;
	

CALL SP_VIP_RetrieveN(@iErrorCode, @sErrorMsg, @sDistrict, @iCategory, @sMobile, @sICID, @sEmail, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP 
	WHERE F_District = @sDistrict
	AND F_Category = @iCategory
	AND F_Name = @sName
	AND F_Mobile = @sMobile
	AND F_ICID = @sICID
	AND F_Email = @sEmail;

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- CASE3:��Status��District��ѯ
SELECT '-------------------- CASE3:��Status��District��ѯ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sDistrict = "����";
SET @iCategory = -1;
SET @sMobile = "";
SET @sICID = "";
SET @sEmail = "";
SET @iPageIndex = 1;
SET @iPageSize = 10;
	

CALL SP_VIP_RetrieveN(@iErrorCode, @sErrorMsg, @sDistrict, @iCategory, @sMobile, @sICID, @sEmail, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP 
	WHERE F_District = @sDistrict
	AND F_Category = @iCategory
	AND F_Name = @sName
	AND F_Mobile = @sMobile
	AND F_ICID = @sICID
	AND F_Email = @sEmail;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- CASE4:��Status��Category��ѯ
SELECT '-------------------- CASE4:��Status��Category��ѯ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sDistrict = "";
SET @iCategory = 2;
SET @sMobile = "";
SET @sICID = "";
SET @sEmail = "";
SET @iPageIndex = 1;
SET @iPageSize = 10;
	
CALL SP_VIP_RetrieveN(@iErrorCode, @sErrorMsg, @sDistrict, @iCategory, @sMobile, @sICID, @sEmail, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP 
	WHERE F_District = @sDistrict
	AND F_Category = @iCategory
	AND F_Name = @sName
	AND F_Mobile = @sMobile
	AND F_ICID = @sICID
	AND F_Email = @sEmail;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';


	