SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: Õý³£´´½¨ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'purchasingPlanTable CASE 1';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);

SELECT 1 FROM t_PurchasingOrder WHERE 1=1
	AND F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 1';

SELECT '-------------------- Case2: iStaffID ²»´æÔÚ£¬·µ»Ø´íÎóÂë3 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '¹þ¹þ¹þ';
SET @iStaffID = 999;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: iProviderID ²»´æÔÚ£¬·µ»Ø´íÎóÂë7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '¹þ¹þ¹þ';
SET @iStaffID = 1;
SET @iProviderID = 999;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: @sRemark²É¹º×Ü½á×Ö¶Î³¬¹ýÊý¾Ý¿â×Ö·ûÏÞÖÆ,³¬¹ý³¤¶ÈÏÞÖÆ(Êý¾Ý¿â»á´æÈëÓë×Ö¶Î³¤¶ÈÏàµÈµÄ×Ö·û) -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID;
SELECT IF(@iErrorCode = 0, '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_Remark = '¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ¹þ';

SELECT '-------------------- Case5: iStaffIDÎªÀëÖ°Ô±¹¤£¬·µ»Ø´íÎóÂë7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '¹þ¹þ¹þ';
SET @iStaffID = 5;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg, @iShopID, @iStaffID, @iProviderID, @sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg='µ±Ç°ÕÊ»§²»ÔÊÐí´´½¨²É¹ºµ¥', '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE5 Testing Result';

SELECT '-------------------- Case5: ´´½¨²É¹ºµ¥µÄÃÅµê²»´æÔÚ£¬·µ»Ø´íÎóÂë7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = -1;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);


SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '¸ÃÃÅµê²»´æÔÚ£¬ÇëÖØÐÂÑ¡ÔñÃÅµê', '²âÊÔ³É¹¦', '²âÊÔÊ§°Ü') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 5';