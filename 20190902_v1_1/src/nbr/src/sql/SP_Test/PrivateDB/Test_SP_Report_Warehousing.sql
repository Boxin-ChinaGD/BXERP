SELECT '++++++++++++++++++ Test_SP_Report_Warehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 按ID降序排列 -------------------------' AS 'Case1';
SET @sErrorMsg = '';
SET @dtStart ='1970/01/01 00:00:00';
SET @dtEnd = "2018/12/21 23:59:59";
SET @string1 = "";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 按ID升序排列 -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 1;
SET @string1 = "";
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 按数量降序排列报 -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @iOrderBy = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @string1 = "";
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: 按数量升序排列 -------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @iOrderBy = 1;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 1;
SET @string1 = "";
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: 按总额降序排列 -------------------------' AS 'Case5';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @iOrderBy = 2;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @string1 = "";
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: 按照总额升序排列报表 -------------------------' AS 'Case6';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @iOrderBy = 2;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 1;
SET @string1 = "";
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: 根据名称搜索并按ID降序排列 -------------------------' AS 'Case7';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "可乐";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: 根据条形码搜索并按ID降序排列 -------------------------' AS 'Case8';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "123456789";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @bIgnoreZeroNO = 0;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: bIgnoreZeroNO=1忽略0进货量，目前11为0进货量 -------------------------' AS 'Case9';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @bIgnoreZeroNO = 1;
SET @iCategory = -1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: 按照类别进行查询 -------------------------' AS 'Case10';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "";
SET @iOrderBy = 0;
SET @iPageIndex = 1;
SET @iPageSize = 100;
SET @iTotalRecord = 0;
SET @isASC = 0;
SET @bIgnoreZeroNO = 0;
SET @iCategory = 1;

CALL SP_Report_Warehousing
	(@iErrorCode, 
	@sErrorMsg,
	@dtStart, 
	@dtEnd, 
	@iOrderBy,
	@isASC,
	@string1,
	@bIgnoreZeroNO,
	@iCategory,
	@iPageIndex, 
	@iPageSize, 
	@iTotalRecord
	);
	
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';