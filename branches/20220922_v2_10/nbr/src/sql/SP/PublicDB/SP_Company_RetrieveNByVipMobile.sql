DROP PROCEDURE IF EXISTS `SP_Company_RetrieveNByVipMobile`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_RetrieveNByVipMobile`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sMobile VARCHAR(11),
	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
)
BEGIN
    DECLARE createTmpTable_sql varchar(500);     -- ��Ҫִ�е�SQL���          
	DECLARE recordIndex INT;
	DECLARE ID INT;		   
	DECLARE dbName Varchar(20);			  	 
	DECLARE done INT DEFAULT  false;
	DECLARE list CURSOR FOR(
			SELECT 
			   	F_ID,  
				F_DBName
			FROM nbr_bx.t_company
			WHERE  F_Status = 0
   	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
			-- ������˾��ʱ�������˾�иû�Ա������ñ��������
			DROP TEMPORARY TABLE IF EXISTS tmp_table;
			
			-- ��һ����t_companyһ������ʱ��
			DROP TEMPORARY TABLE IF EXISTS tmp_t_company;
			CREATE TEMPORARY TABLE tmp_t_company (
				F_ID INT NOT NULL  
			);
			-- 
			OPEN list;-- 
				read_loop1: LOOP
				FETCH list INTO ID, dbName;
				IF done THEN
					LEAVE read_loop1;
				END IF;
				-- 
				IF dbName <> 'nbr_bx' THEN
					-- 
					SET createTmpTable_sql = concat('create temporary table tmp_table(select F_Mobile from ',dbName,'.t_vip', ' where F_Mobile = ''', sMobile, ''')');  
				    SET @createTmpTable_sql = createTmpTable_sql;   -- �����ɳɵ��ַ�����ֵ��һ������
					prepare statement from @createTmpTable_sql;  -- Ԥ������Ҫִ�еĶ�̬SQL������statement��һ������   
					EXECUTE statement;      -- ִ��SQL���    		
		     		deallocate prepare statement;     -- �ͷŵ�Ԥ�����  
					-- 
					IF EXISTS(SELECT 1 FROM tmp_table) THEN -- ����ù�˾�����VIP, ������ʱ��˾��
	 						INSERT INTO tmp_t_company (F_ID) VALUES (ID);
		     		END IF;
		     		DROP TEMPORARY TABLE tmp_table;       -- ɾ����ʱ�� 
		     		-- 
				END IF;
				-- 
				END LOOP read_loop1;
			CLOSE list;
		-- 
		SELECT 
		   	F_ID, 
			F_Name, 
			F_SN, 
			F_BusinessLicenseSN, 
			F_BusinessLicensePicture, 
			F_BossName, 
			F_BossPhone, 
			F_BossPassword,
			F_BossWechat, 
			F_DBName, 
			F_Key, 
			F_DBUserName, 
			F_DBUserPassword, 
			F_Status, 
			F_Submchid,
			F_BrandName,
			F_CreateDatetime, 
			F_UpdateDatetime, 
			F_ExpireDatetime,
			F_Logo
		FROM nbr_bx.t_company
		WHERE F_ID IN (SELECT F_ID FROM tmp_t_company)
		ORDER BY F_ID DESC 
		LIMIT recordIndex, iPageSize;
		-- 
		SELECT count(1) into iTotalRecord 
		FROM nbr_bx.t_company
		WHERE F_ID IN (SELECT F_ID FROM tmp_t_company);
		-- 
		DROP TEMPORARY TABLE tmp_t_company;       -- ɾ����ʱ�� 
		-- 
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		-- 
	COMMIT;
END;