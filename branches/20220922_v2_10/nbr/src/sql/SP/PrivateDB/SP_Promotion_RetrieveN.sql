DROP PROCEDURE IF EXISTS `SP_Promotion_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Promotion_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iSubStatusOfStatus0 INT, -- 0 ��������״̬Ϊ0�Ĵ���� 10 ��������״̬Ϊ0��δ��ʼ�Ĵ���� 11 ��������״̬Ϊ0�ҽ����еĴ���� 12 ��������״̬Ϊ0���ѽ����Ĵ���� 13 ��ѯ�����л��н�Ҫ���е�
	IN iStatus INT,-- -1 �������еĴ����  1 ������ɾ���Ĵ����  0 ����f_status=0 
	IN queryKeyword VARCHAR(32), -- ����F_Name��F_SN�ֶ�ģ����ѯ
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN 
    DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		-- ����������Ĳ�����0������
		IF iSubStatusOfStatus0 NOT IN (0,10,11,12,13) THEN
			 SET iSubStatusOfStatus0 := 0;
--			 SET sErrorMsg := '����Ĳ�������';
		END IF;
		
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
			F_UpdateDatetime,
			F_RetailTradeNO,
			F_StaffName
		FROM 
		(
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
				F_UpdateDatetime,
				(select count(1) from t_retailtradepromotingflow WHERE t_retailtradepromotingflow.F_PromotionID = t_promotion.F_ID) AS F_RetailTradeNO,
				(SELECT F_Name FROM t_staff WHERE t_promotion.F_Staff = t_staff.F_ID) AS F_StaffName
			FROM t_promotion 
			WHERE 
			(CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END) AND	                
			  (CASE queryKeyword WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',queryKeyword,'%') END)
			 UNION 
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
				F_UpdateDatetime,
				(select count(1) from t_retailtradepromotingflow WHERE t_retailtradepromotingflow.F_PromotionID = t_promotion.F_ID) AS F_RetailTradeNO,
				(SELECT F_Name FROM t_staff WHERE t_promotion.F_Staff = t_staff.F_ID) AS F_StaffName
			FROM t_promotion 
			WHERE 
			(CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END) AND length(queryKeyword) > 9 AND F_SN LIKE CONCAT('%',queryKeyword,'%')
		) AS TMP 
		WHERE 		                            
			  (CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END)
			    
		ORDER BY F_ID DESC 
		LIMIT recordIndex, iPageSize;	
		
		SELECT count(1) into iTotalRecord 
		FROM 
		(
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
				F_UpdateDatetime,
				(select count(1) from t_retailtradepromotingflow WHERE t_retailtradepromotingflow.F_PromotionID = t_promotion.F_ID) AS F_RetailTradeNO,
				(SELECT F_Name FROM t_staff WHERE t_promotion.F_Staff = t_staff.F_ID) AS F_StaffName
			FROM t_promotion 
			WHERE 
			(CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END) AND	                
			  (CASE queryKeyword WHEN '' THEN 1=1 ELSE F_Name LIKE CONCAT('%',queryKeyword,'%') END)
			 UNION 
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
				F_UpdateDatetime,
				(select count(1) from t_retailtradepromotingflow WHERE t_retailtradepromotingflow.F_PromotionID = t_promotion.F_ID) AS F_RetailTradeNO,
				(SELECT F_Name FROM t_staff WHERE t_promotion.F_Staff = t_staff.F_ID) AS F_StaffName
			FROM t_promotion 
			WHERE 
			(CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END) AND length(queryKeyword) > 9 AND F_SN LIKE CONCAT('%',queryKeyword,'%')
		) AS TMP 
		WHERE 		                            
			  (CASE iStatus 
				 WHEN -1 THEN 1=1 
	             WHEN 1  THEN F_Status = 1 
	             WHEN 0  THEN (CASE iSubStatusOfStatus0 
	            				WHEN 0  THEN F_Status = 0 
	                         	WHEN 10 THEN F_Status = 0 AND F_DatetimeStart > now()
	                            WHEN 11 THEN F_Status = 0 AND F_DatetimeStart <= now() AND F_DatetimeEnd >= now()
	                            WHEN 12 THEN F_Status = 0 AND F_DatetimeEnd < now() 
	                            WHEN 13 THEN F_Status = 0 AND F_DatetimeEnd >= now()
	                          ELSE 1=1 END)
			   ELSE 1=1 END); 
	   
	   SET iErrorCode := 0;
	   SET sErrorMsg := '';
	   
    COMMIT;
END;