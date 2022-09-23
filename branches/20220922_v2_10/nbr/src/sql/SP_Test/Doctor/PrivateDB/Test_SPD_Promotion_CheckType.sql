	SELECT '++++++++++++++++++ SPD_Promotion_CheckType.sql ++++++++++++++++++++';
	
	SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
	-- 
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	-- 
	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
	
	
	
	
	SELECT '-------------------- Case2:�������Ͳ����������͸���������-------------------------' AS 'Case2';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 2,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 20, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '�������Ͳ����������͸���������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case3:����ʱ��������ֵС��0-------------------------' AS 'Case3';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', -1, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵС��0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
	SELECT '-------------------- Case4:����ʱ��������ֵ����0-------------------------' AS 'Case4';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 0, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case5:����ʱ��������ֵ����10000-------------------------' AS 'Case5';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10001, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵ����10000') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case6:����ʱ�������ۿ�С��0-------------------------' AS 'Case6';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, -1, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ�������ۿ�С��0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case7:����ʱ�������ۿ۵���0-------------------------' AS 'Case7';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, 0, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ�������ۿ۵���0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
	SELECT '-------------------- Case8:����ʱ�������ۿ۴���1------------------------' AS 'Case8';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, 10, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ�������ۿ۴���1') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;


    SELECT '-------------------- Case10:����ʱ��������ֵС��0------------------------' AS 'Case10';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', -1, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵС��0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
    SELECT '-------------------- Case11:����ʱ��������ֵ����0------------------------' AS 'Case11';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 0, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵ����0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
    SELECT '-------------------- Case12:����ʱ��������ֵ����10000------------------------' AS 'Case12';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10001, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵ����10000') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
    SELECT '-------------------- Case13:����ʱ��������ֵС���������------------------------' AS 'Case13';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, 150, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ��������ֵС���������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case14:����ʱ���������С��0------------------------' AS 'Case14';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, -1, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ���������С��0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case15:����ʱ������������0------------------------' AS 'Case15';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', 'ָ����Ʒ1�������20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, 0, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('����', @iID, '����ʱ������������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	

	
	