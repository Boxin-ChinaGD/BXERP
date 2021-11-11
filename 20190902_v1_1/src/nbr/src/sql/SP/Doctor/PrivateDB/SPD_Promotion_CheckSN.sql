DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckSN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckSN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sSN VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_SN AS sSN FROM t_promotion);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sSN;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 促销编号必须于CX或者cx开头
			IF LEFT(sSN,2) <> 'CX' THEN -- 大小写都满足
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销编号', iID, '格式不正确');
				-- 促销编号长度大于等于14位
			ELSEIF LENGTH(sSN) < 14 THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销编号', iID, '的长度不能小于14位字符');		
			END IF;
			   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;