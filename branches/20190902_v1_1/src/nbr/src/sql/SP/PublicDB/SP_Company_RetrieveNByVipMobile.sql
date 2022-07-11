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
    DECLARE createTmpTable_sql varchar(500);     -- 需要执行的SQL语句          
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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
			-- 遍历公司的时候，如果公司有该会员，则向该表插入数据
			DROP TEMPORARY TABLE IF EXISTS tmp_table;
			
			-- 建一个与t_company一样的临时表
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
				    SET @createTmpTable_sql = createTmpTable_sql;   -- 将连成成的字符串赋值给一个变量
					prepare statement from @createTmpTable_sql;  -- 预处理需要执行的动态SQL，其中statement是一个变量   
					EXECUTE statement;      -- 执行SQL语句    		
		     		deallocate prepare statement;     -- 释放掉预处理段  
					-- 
					IF EXISTS(SELECT 1 FROM tmp_table) THEN -- 如果该公司有这个VIP, 插入临时公司表
	 						INSERT INTO tmp_t_company (F_ID) VALUES (ID);
		     		END IF;
		     		DROP TEMPORARY TABLE tmp_table;       -- 删除临时表 
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
		DROP TEMPORARY TABLE tmp_t_company;       -- 删除临时表 
		-- 
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		-- 
	COMMIT;
END;