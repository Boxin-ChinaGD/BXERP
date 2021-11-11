SELECT '++++++++++++++++++ Test_SP_PromotionShopScope_RetrieveN.sql ++++++++++++++++++++';
SELECT '---------------------------Case 1 ------------------------------------' AS 'Case1';
SET @name = '¬˙10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPromotionID = @iID;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

INSERT INTO t_promotionShopscope(F_PromotionID, F_ShopID) VALUES(@iPromotionID, 2);
SET @SSiD = LAST_INSERT_ID();
CALL SP_PromotionShopScope_RetrieveN(@iErrorCode, @sErrorMsg, @iPromotionID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_PromotionID = @iPromotionID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';
DELETE FROM t_promotionShopScope WHERE F_ID = @SSiD;
DELETE FROM t_promotion WHERE F_ID =@iID; 