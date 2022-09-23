-- 第一个参数，一般的都是model的简写，如零售单 = LS; 采购订单 = CG
-- 第二个参数这则是拿到model的上个SN
DROP FUNCTION IF EXISTS `Func_GenerateSN`;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_GenerateSN`(
	sModelName VARCHAR(2),
	sSN VARCHAR(20)
) RETURNS VARCHAR(20) CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
	-- 零售单的SN比较特殊
-- 	IF sModelName <> 'LS' THEN
		-- 新建公司时，SN码是NULL，这里考虑到这种情况
 		IF sSN IS NULL THEN
 		     SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), '0000');
 		END IF;
 		-- 判断是否是同一天，如果是同一天，则在流水号后面加1.不同一天则说明这是今天的第一张单
		IF ((date_format(now(),'%Y%m%d')) = substring(sSN, 3, 8)) = 0 THEN 
			SET @number := 1;
   		ELSE 
			SET @number := (CAST(RIGHT(sSN, 4) AS UNSIGNED INTEGER) + 1);
		END IF;
		-- 
		SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), lpad(@number, 4, 0));
--	ELSE
--	-- 零售单SN码后4位需要是随机的，同一天不能重复
--		SET @number := CEILING(RAND() * 9000 + 1000);
--   		SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), @number);
--		-- 
--		WHILE (SELECT 1 FROM t_retailtrade WHERE F_SN = sSN) = 1 do
--		   	SET @number := CEILING(RAND() * 9000 + 1000);
--			SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), @number);
--		END WHILE;
--	END IF;
 	RETURN sSN;
END;