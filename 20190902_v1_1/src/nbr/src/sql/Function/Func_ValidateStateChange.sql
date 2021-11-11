-- 状态机函数，用于检查一个业务逻辑iDomainID中，一个对象由状态iStausIDFrom变成状态iStausIDTo在业务上是否允许。返回1代表允许，返回0代表不允许。
drop function IF EXISTS Func_ValidateStateChange;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_ValidateStateChange`(
	iDomainID INT, 
	iStausIDFrom INT,
	iStausIDTo INT
	) RETURNS int(11)
BEGIN
	IF EXISTS (
		SELECT 1 FROM t_smforward WHERE F_CurrentNodeID = iStausIDFrom
		AND F_NextNodeID = iStausIDTo AND F_DomainID = iDomainID
						)
	THEN
		RETURN(1);
	ELSE
		RETURN(0);
	END IF;
END;

