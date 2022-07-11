-- ��SP������Action,ֻ���ڲ���
DROP PROCEDURE IF EXISTS `SP_CouponScope_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponScope_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
   		
   		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		DELETE FROM t_couponscope WHERE F_ID = iID;
	
	COMMIT;
END;