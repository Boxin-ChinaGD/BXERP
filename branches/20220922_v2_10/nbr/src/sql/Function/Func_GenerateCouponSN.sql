DROP FUNCTION IF EXISTS `Func_GenerateCouponSN`;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_GenerateCouponSN`(
	iCounts INT
) RETURNS VARCHAR(20) CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
	DECLARE sTemp VARCHAR(20);
	DECLARE sTempCounts INTEGER;
	
	SET sTemp =  ROUND(ROUND(RAND(),iCounts)*(POW(10,iCounts)));
	
	IF(CHAR_LENGTH(sTemp) < iCounts) THEN
		SET sTempCounts = iCounts - CHAR_LENGTH(sTemp);
		SET sTemp =CONCAT(sTemp, RIGHT(CONCAT(POW(10,sTempCounts), ''),sTempCounts));
	END IF;
	
	IF (CHAR_LENGTH(sTemp) > iCounts) THEN
		SET sTemp = RIGHT(sTemp,iCounts);
	END IF;
	
	return sTemp;
END;