
DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE type INT;
	DECLARE excecutionThreshold DECIMAL(20,6); -- ������ֵ
	DECLARE excecutionDiscount DECIMAL(20,6); -- �����ۿ�
	DECLARE excecutionAmount DECIMAL(20,6); -- �������
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID,F_Type AS type,F_ExcecutionThreshold AS excecutionThreshold,F_ExcecutionDiscount AS excecutionDiscount,F_ExcecutionAmount AS excecutionAmount FROM t_promotion);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,type,excecutionThreshold,excecutionDiscount,excecutionAmount;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			IF type = 1 THEN
			    -- ����ʱ��������ֵС��0
			    IF excecutionThreshold < 0 THEN 
		          SET done := TRUE;
		          SET iErrorCode := 7; 
		          SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵС��0');
		          
		        -- ����ʱ��������ֵ����0  
		        ElSEIF excecutionThreshold = 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵ����0');
			    -- ����ʱ��������ֵ����10000
			    ELSEIF excecutionThreshold > 10000 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵ����10000'); 
			     -- ����ʱ�������ۿ�С��0   
			    ELSEIF excecutionDiscount < 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('����', iID, '����ʱ�������ۿ�С��0');  
			    -- ����ʱ�������ۿ۵���0 
		    	ELSEIF excecutionDiscount = 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('����', iID, '����ʱ�������ۿ۵���0'); 
			    -- ����ʱ�������ۿ۴���1
		    	ELSEIF excecutionDiscount > 1 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('����', iID, '����ʱ�������ۿ۴���1'); 
			    END IF;
			ELSEIF type = 0 THEN 
			   -- ����ʱ��������ֵС��0
			   IF excecutionThreshold < 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵС��0'); 
			   -- ����ʱ��������ֵ����0
			   ELSEIF excecutionThreshold = 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵ����0');
			   -- ����ʱ��������ֵ����10000
			   ELSEIF excecutionThreshold > 10000 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
		         SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵ����10000');
		       -- ����ʱ��������ֵС���������
		       ELSEIF excecutionThreshold < excecutionAmount THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('����', iID, '����ʱ��������ֵС���������');
			   -- ����ʱ���������С��0 
		       ELSEIF excecutionAmount < 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('����', iID, '����ʱ���������С��0');  
			    -- ����ʱ������������0 
		       ELSEIF excecutionAmount = 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('����', iID, '����ʱ������������0');  
	           END IF;   
			ELSE 
			    SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '�������Ͳ����������͸���������');
			END IF;
		 					   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;