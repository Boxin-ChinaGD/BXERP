	SELECT '++++++++++++++++++ SPD_Promotion_CheckType.sql ++++++++++++++++++++';
	
	SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
	-- 
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	-- 
	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
	
	
	
	
	SELECT '-------------------- Case2:促销类型不是满折类型跟满减类型-------------------------' AS 'Case2';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 2,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 20, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '促销类型不是满折类型跟满减类型') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case3:满折时，满减阀值小于0-------------------------' AS 'Case3';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', -1, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减阀值小于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
	SELECT '-------------------- Case4:满折时，满减阀值等于0-------------------------' AS 'Case4';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 0, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减阀值等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case5:满折时，满减阀值大于10000-------------------------' AS 'Case5';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10001, 5, NULL, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减阀值大于10000') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case6:满折时，满减折扣小于0-------------------------' AS 'Case6';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, -1, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减折扣小于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
	SELECT '-------------------- Case7:满折时，满减折扣等于0-------------------------' AS 'Case7';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, 0, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减折扣等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
	SELECT '-------------------- Case8:满折时，满减折扣大于1------------------------' AS 'Case8';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 1,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10000, 5, 10, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满折时，满减折扣大于1') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;


    SELECT '-------------------- Case10:满减时，满减阀值小于0------------------------' AS 'Case10';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', -1, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减阀值小于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case10 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	
    SELECT '-------------------- Case11:满减时，满减阀值等于0------------------------' AS 'Case11';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 0, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减阀值等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case11 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
    SELECT '-------------------- Case12:满减时，满减阀值大于10000------------------------' AS 'Case12';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 10001, 5, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减阀值大于10000') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case12 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
		
    SELECT '-------------------- Case13:满减时，满减阀值小于满减金额------------------------' AS 'Case13';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, 150, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减阀值小于满减金额') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case13 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case14:满减时，满减金额小于0------------------------' AS 'Case14';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, -1, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减金额小于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case14 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	
	
	SELECT '-------------------- Case15:满减时，满减金额等于0------------------------' AS 'Case15';
	
	INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
	VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0,'2018-12-23 11:32:09' , '2019-12-22 11:32:09', 100, 0, 8, 1, 4, '2018-12-22 11:32:09', '2019-07-22 11:32:09');
	
	SET @iID = LAST_INSERT_ID();
	
	
	SET @iErrorCode = 0;
	SET @sErrorMsg = '';
	-- 
	CALL SPD_Promotion_CheckType(@iErrorCode, @sErrorMsg);
	SELECT @iErrorCode, @sErrorMsg;
	--  
	SELECT IF(@sErrorMsg = CONCAT('促销', @iID, '满减时，满减金额等于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case15 Testing Result';
	DELETE FROM t_promotion WHERE F_ID = @iID;
	

	
	