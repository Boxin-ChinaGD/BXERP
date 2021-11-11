-- 删除促销时，应该检查的依赖。
-- 返回值：
-- ''，可以删除促销。
-- 其它errorMsg，不可以删除促销
DROP FUNCTION IF EXISTS Func_CheckPromotionDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPromotionDependency`(
	iPromotionID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_RetailTradePromotingFlow WHERE F_PromotionID = iPromotionID) THEN
		SET sErrorMsg := '该促销已经生成过交易信息，不能删除';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;