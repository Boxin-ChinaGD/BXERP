DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckSmallSheetID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckSmallSheetID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE iSmallSheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_SmallSheetID AS iSmallSheetID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iSmallSheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有的零售单的SmallSheetID都不能为空，都必须存在SmallSheet表中
			-- 上面两项任意一项不通过，那么则认为数据不健康
			IF iSmallSheetID IS NULL THEN -- SmallSheetID为空
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单的SmallSheetID为NULL');
			-- 服务器对小票格式是有数量限制的，假如一开始有10张小票格式，
			-- 后面用户添加第11张小票格式并使用第11张小票格式的话只能删除一开始的1到10张小票格式，
			-- 如果T_RetailTrade的F_SmallSheetID字段有T_SmallSheetFrame的外键约束的话，
			-- 用户是删除不了小票格式的，导致用户添加不了新的小票格式并使用不了新的小票格式。
			-- 所以iSmallSheetID大于0即可
			ELSEIF iSmallSheetID > 0 THEN -- 正常情况
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE -- SmallSheetID不存在t_smallsheetframe表中
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单的SmallSheetID必须大于0');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;