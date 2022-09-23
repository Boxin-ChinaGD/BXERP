
-- CASE1:用Status查询
SELECT '-------------------- Case1: 用Status查询 -------------------------' AS 'Case1';
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

SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- CASE2:用Status和IDInPOS查询
SELECT '-------------------- CASE2:用Status和IDInPOS查询 -------------------------' AS 'Case2';
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

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- CASE3:用Status和District查询
SELECT '-------------------- CASE3:用Status和District查询 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sDistrict = "广州";
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
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- CASE4:用Status和Category查询
SELECT '-------------------- CASE4:用Status和Category查询 -------------------------' AS 'Case3';
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
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';


	