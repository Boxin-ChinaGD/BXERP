-- ״̬�����������ڼ��һ��ҵ���߼�iDomainID�У�һ��������״̬iStausIDFrom���״̬iStausIDTo��ҵ�����Ƿ���������1������������0��������
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

