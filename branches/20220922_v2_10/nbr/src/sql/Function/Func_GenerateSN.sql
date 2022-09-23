-- ��һ��������һ��Ķ���model�ļ�д�������۵� = LS; �ɹ����� = CG
-- �ڶ��������������õ�model���ϸ�SN
DROP FUNCTION IF EXISTS `Func_GenerateSN`;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_GenerateSN`(
	sModelName VARCHAR(2),
	sSN VARCHAR(20)
) RETURNS VARCHAR(20) CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
	-- ���۵���SN�Ƚ�����
-- 	IF sModelName <> 'LS' THEN
		-- �½���˾ʱ��SN����NULL�����￼�ǵ��������
 		IF sSN IS NULL THEN
 		     SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), '0000');
 		END IF;
 		-- �ж��Ƿ���ͬһ�죬�����ͬһ�죬������ˮ�ź����1.��ͬһ����˵�����ǽ���ĵ�һ�ŵ�
		IF ((date_format(now(),'%Y%m%d')) = substring(sSN, 3, 8)) = 0 THEN 
			SET @number := 1;
   		ELSE 
			SET @number := (CAST(RIGHT(sSN, 4) AS UNSIGNED INTEGER) + 1);
		END IF;
		-- 
		SET sSN := CONCAT(sModelName, date_format(now(),'%Y%m%d'), lpad(@number, 4, 0));
--	ELSE
--	-- ���۵�SN���4λ��Ҫ������ģ�ͬһ�첻���ظ�
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