DROP PROCEDURE IF EXISTS `SP_Company_MatchVip`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Company_MatchVip`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iSourceCode INT,
	IN sMobile VARCHAR(11),
	IN sOpenID VARCHAR(50),
	IN sUnionID VARCHAR(50),
	IN sName VARCHAR(32),
	IN iSex INT
)
BEGIN
    DECLARE createTmpTable_sql varchar(500);     -- 需要执行的SQL语句   
    DECLARE updateVip_sql varchar(500);     
	DECLARE ID INT;		   
	DECLARE dbName Varchar(20);			  	 
	DECLARE done INT DEFAULT false;
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
	
		SET iErrorCode := 2;
		SET sErrorMsg := '未匹配到该会员信息';
		
		-- 遍历公司的时候，如果公司有该会员，则向该表插入数据
		DROP TEMPORARY TABLE IF EXISTS tmp_table_matchVip;
		DROP TEMPORARY TABLE IF EXISTS tmp_table_hasVip;
		
		OPEN list;
			read_loop1: LOOP
			FETCH list INTO ID, dbName;
				IF done THEN
					
					LEAVE read_loop1;
				END IF;
				-- 
				IF dbName <> 'nbr_bx' THEN
					DROP TEMPORARY TABLE IF EXISTS tmp_table_hasVip;
					-- 
				   	SET createTmpTable_sql = concat('create temporary table tmp_table_hasVip (select 1 from ', dbName,'.t_vip where F_Mobile = ''', sMobile, ''')');  
							    SET @createTmpTable_sql = createTmpTable_sql;
								prepare statement from @createTmpTable_sql;
								EXECUTE statement;
								deallocate prepare statement;
					-- 
					IF EXISTS(SELECT 1 FROM tmp_table_hasVip) THEN
						-- 当sUnionID和sOpenID都不为空串的时候，说明是首次使用小程序登录，需要更新其openid和unionid
						IF sUnionID <> '' AND sOpenID <> '' THEN
							-- 修改会员信息
							SET updateVip_sql = concat('update ',dbName,'.t_vip v SET v.F_Name = ''', sName, ''', v.F_Sex = ', iSex,' WHERE v.F_Mobile = ''', sMobile,''' AND '''' = (SELECT vs.F_ID3 FROM ', dbName,'.t_vipsource vs WHERE  vs.F_VipID = v.F_ID limit 1) limit 1');
							SET @updateVip_sql = updateVip_sql;
							prepare statement from @updateVip_sql;
							EXECUTE statement;
							deallocate prepare statement;
							-- 如果此会员中是在nbr上注册,则更新会员的openID(一个微信号,他在不同的公司注册,他的openID都是一样的)
							SET updateVip_sql = concat('update ',dbName,'.t_vipsource vs SET vs.F_ID2 = ''', sUnionID, ''', vs.F_ID3 = ''', sOpenID,''' WHERE vs.F_ID3 = '''' AND vs.F_VipID = (SELECT v.F_ID FROM ', dbName,'.t_vip v WHERE v.F_Mobile = ''', sMobile,''' limit 1)');
							SET @updateVip_sql = updateVip_sql;
							prepare statement from @updateVip_sql;
							EXECUTE statement;
							deallocate prepare statement;
						END IF;
		     		END IF;
		     		DROP TEMPORARY TABLE IF EXISTS tmp_table_hasVip; 
		     		-- 
				END IF;
				-- 
			END LOOP read_loop1;
			SET done := false;
		CLOSE list;
		
		OPEN list;
			--  
			read_loop1: LOOP
			FETCH list INTO ID, dbName;
				IF done THEN
					LEAVE read_loop1;
				END IF;
			-- 
				IF dbName <> 'nbr_bx' THEN
					DROP TEMPORARY TABLE IF EXISTS tmp_table_hasVip;
					-- 
					SET createTmpTable_sql = concat('create temporary table tmp_table_hasVip (select 1 from ', dbName,'.t_vip where F_Mobile = ''', sMobile, ''')');  
						    SET @createTmpTable_sql = createTmpTable_sql;
							prepare statement from @createTmpTable_sql;
							EXECUTE statement;
							deallocate prepare statement;
					-- 
					IF EXISTS(SELECT 1 FROM tmp_table_hasVip) THEN
						SET createTmpTable_sql = concat('create temporary table tmp_table_matchVip (select 1 from ',dbName,'.t_vipsource', ' where F_SourceCode = ', iSourceCode, 
					   	' AND F_ID3 = ''', sOpenID, ''' AND F_VipID = (SELECT F_ID FROM ', dbName, '.T_Vip WHERE F_Mobile = ''', sMobile, ''' limit 1) limit 1)');  
					    SET @createTmpTable_sql = createTmpTable_sql;
						prepare statement from @createTmpTable_sql;
						EXECUTE statement;      -- 执行SQL语句    		
			     		deallocate prepare statement;     -- 释放掉预处理段
						-- 
						IF EXISTS(SELECT 1 FROM tmp_table_matchVip) THEN -- 如果该公司有这个VIP, 插入临时公司表
 							SET iErrorCode := 0;
							SET sErrorMsg := '';
							-- 
							SET createTmpTable_sql = concat('select * from ',dbName,'.t_vip where F_Mobile = ''', sMobile, '''');  
						    SET @createTmpTable_sql = createTmpTable_sql;
							prepare statement from @createTmpTable_sql;  
							EXECUTE statement;  
							-- 
							SET createTmpTable_sql = concat('select * from ',dbName,'.t_vipSource where F_ID3 = ''', sOpenID, '''');  
						    SET @createTmpTable_sql = createTmpTable_sql;
							prepare statement from @createTmpTable_sql;   
							EXECUTE statement;		
							-- 
				     		deallocate prepare statement;
							
							DROP TEMPORARY TABLE tmp_table_matchVip;
							LEAVE read_loop1;
			     		END IF;
			     		DROP TEMPORARY TABLE IF EXISTS tmp_table_matchVip;
			     		-- 
					END IF;
					-- 
					DROP TEMPORARY TABLE IF EXISTS tmp_table_hasVip;
				END IF;
			-- 
			END LOOP read_loop1;
		CLOSE list;
		
	COMMIT;
END;