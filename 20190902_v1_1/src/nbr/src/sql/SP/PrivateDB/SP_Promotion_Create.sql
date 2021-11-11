DROP PROCEDURE IF EXISTS `SP_Promotion_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Promotion_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN name VARCHAR(32),
   	IN status INT,
   	IN type INT,
   	IN datetimestart DATETIME,
   	IN datetimeend DATETIME,
   	IN excecutionthreshold DECIMAL(20, 6),
   	IN excecutionamount DECIMAL(20, 6),
   	IN excecutiondiscount DECIMAL(20, 6),
   	IN scope INT,
   	IN shopScope INT,
   	IN staffID INT
	)
BEGIN
	DECLARE sSN VARCHAR(20);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SELECT F_SN INTO sSN FROM t_promotion ORDER BY F_ID DESC LIMIT 1;
		SELECT Func_GenerateSN('CX', sSN) INTO sSN;
	
	   INSERT INTO t_promotion (
	   		F_SN,
			F_Name, 
			F_Status, 
			F_Type, 
			F_DatetimeStart, 
			F_DatetimeEnd, 
			F_ExcecutionThreshold, 
			F_ExcecutionAmount, 
			F_ExcecutionDiscount, 
			F_Scope, 
			F_ShopScope,
			F_Staff, 
			F_CreateDatetime, 
			F_UpdateDatetime
		) VALUES (
			sSN,
		    name, 
		    status, 
		    type, 
		    datetimestart, 
		    datetimeend, 
		    excecutionthreshold, 
		    IF (type = 0, excecutionamount, NULL), 
		    IF (type = 1, excecutiondiscount, NULL),
		    scope,
		    shopScope,
		    staffID, 
		    now(), 
		    now()
		);
   
	    SELECT 
		    F_ID,
		    F_SN,
		    F_Name, 
		    F_Status, 
		    F_Type, 
		    F_DatetimeStart, 
		    F_DatetimeEnd, 
		    F_ExcecutionThreshold, 
		    F_ExcecutionAmount, 
		    F_ExcecutionDiscount, 
		    F_Scope, 
		    F_ShopScope,
		    F_Staff, 
		    F_CreateDatetime,
		    F_UpdateDatetime
	    FROM t_promotion
	    WHERE F_ID = last_insert_id();
	    
	    SET iErrorCode := 0;
	    SET sErrorMsg := '';
   
	COMMIT;
END;