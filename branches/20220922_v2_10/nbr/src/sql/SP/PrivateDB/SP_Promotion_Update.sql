DROP PROCEDURE IF EXISTS `SP_Promotion_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Promotion_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
    IN name VARCHAR(32),
   	IN status INT,
   	IN type INT,
   	IN datetimestart DATETIME,
   	IN datetimeend DATETIME,
   	IN excecutionthreshold DECIMAL(20, 6),
   	IN excecutionamount DECIMAL(20, 6),
   	IN excecutiondiscount DECIMAL(20, 6),
   	IN scope INT,
   	IN staffID INT
	)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;  	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = staffID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的staffID修改';
		ELSE
			-- 先删除PromotionScope的外键引用
	    	DELETE FROM t_promotionscope WHERE F_PromotionID = iID;
			-- 更新活动信息
			UPDATE t_promotion
		    SET F_Name = name,
				F_Status = status,
				F_Type = type,
				F_DatetimeStart = datetimestart,
				F_DatetimeEnd = datetimeend,
				F_ExcecutionThreshold = excecutionthreshold,
				F_ExcecutionAmount = excecutionamount,
				F_ExcecutionDiscount = excecutiondiscount,
				F_Scope = scope,
				F_Staff = staffID,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
			
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
				F_Staff, 
				F_CreateDatetime, 
				F_UpdateDatetime
		    FROM t_promotion
		    WHERE F_ID = iID;
		    SET iErrorCode := 0;
		    SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;