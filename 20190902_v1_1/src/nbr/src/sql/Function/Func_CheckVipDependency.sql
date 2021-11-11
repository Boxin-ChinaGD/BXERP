-- 删除会员时，应该检查的依赖。
-- 返回值：
-- ''，可以删除会员。
-- 其它errorMsg，不可以删除会员
DROP FUNCTION IF EXISTS Func_CheckVipDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckVipDependency`(
	iVipID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_RetailTrade WHERE F_VipID = iVipID) THEN
		SET sErrorMsg := '该会员有零售单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_VIPPointHistory WHERE F_VIP_ID = iVipID) THEN
		SET sErrorMsg := '该会员有会员积分依赖，不能删除';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;