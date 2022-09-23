SELECT '++++++++++++++++++ Test_SP_Report_Warehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��ID�������� -------------------------' AS 'Case1';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ��ID�������� -------------------------' AS 'Case2';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: �������������б� -------------------------' AS 'Case3';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: �������������� -------------------------' AS 'Case4';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: ���ܶ������ -------------------------' AS 'Case5';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: �����ܶ��������б��� -------------------------' AS 'Case6';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: ����������������ID�������� -------------------------' AS 'Case7';
SET @sErrorMsg = '';
SET @dtStart ='1970-01-01 00:00:00';
SET @dtEnd = now();
SET @string1 = "����";
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: ������������������ID�������� -------------------------' AS 'Case8';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: bIgnoreZeroNO=1����0��������Ŀǰ11Ϊ0������ -------------------------' AS 'Case9';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: ���������в�ѯ -------------------------' AS 'Case10';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';