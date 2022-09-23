SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByCategoryParent_RetrieveN.sql+++++++++++++++++++++++';

SELECT '---------------------------------Case1: ��ѯһ�� ------------------------------' AS 'Case1';
SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/16 00:00:00';
SET @dtEnd = '2019/01/16 23:59:59';

CALL SP_RetailTradeDailyReportByCategoryParent_RetrieveN(@iErrorCode,@sErrorMsg,@iShopID,@dtStart,@dtEnd);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime BETWEEN @dtStart AND @dtEnd GROUP BY F_CategoryParentID;
SELECT IF (found_rows()>0 AND @iErrorCode = 0, '���Գɹ�','����ʧ��') AS 'Test Case1 Result';

SELECT '---------------------------------Case2: ��ѯһ���� ------------------------------' AS 'Case2';
SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/01/31 23:59:59';

CALL SP_RetailTradeDailyReportByCategoryParent_RetrieveN(@iErrorCode,@sErrorMsg,@iShopID,@dtStart,@dtEnd);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime BETWEEN @dtStart AND @dtEnd GROUP BY F_CategoryParentID;
SELECT IF (found_rows()>0 AND @iErrorCode = 0, '���Գɹ�','����ʧ��') AS 'Test Case2 Result';

SELECT '------------------------------Case3: ��ѯĳ���ض�ʱ��� --------------------------' AS 'Case3';
SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/16 00:00:00';
SET @dtEnd = '2019/01/17 23:59:59';

CALL SP_RetailTradeDailyReportByCategoryParent_RetrieveN(@iErrorCode,@sErrorMsg,@iShopID,@dtStart,@dtEnd);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime BETWEEN @dtStart AND @dtEnd GROUP BY F_CategoryParentID;
SELECT IF (found_rows()>0 AND @iErrorCode = 0, '���Գɹ�','����ʧ��') AS 'Test Case3 Result';

SELECT '------------------------------Case4: ��ѯ�����ڵ�ʱ��� --------------------------' AS 'Case4';
SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2029/01/13 00:00:00';
SET @dtEnd = '2029/01/15 23:59:59';

CALL SP_RetailTradeDailyReportByCategoryParent_RetrieveN(@iErrorCode,@sErrorMsg,@iShopID,@dtStart,@dtEnd);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime BETWEEN @dtStart AND @dtEnd GROUP BY F_CategoryParentID;
SELECT IF (found_rows()=0 AND @iErrorCode = 0, '���Գɹ�','����ʧ��') AS 'Test Case4 Result';

SELECT '------------------------------Case5: ��ѯ�����ڵ��ŵ�ID --------------------------' AS 'Case5';
SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/01/31 23:59:59';

CALL SP_RetailTradeDailyReportByCategoryParent_RetrieveN(@iErrorCode,@sErrorMsg,@iShopID,@dtStart,@dtEnd);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime BETWEEN @dtStart AND @dtEnd AND F_ShopID = @iShopID GROUP BY F_CategoryParentID ;
SELECT IF (found_rows()=0 AND @iErrorCode = 0, '���Գɹ�','����ʧ��') AS 'Test Case5 Result';