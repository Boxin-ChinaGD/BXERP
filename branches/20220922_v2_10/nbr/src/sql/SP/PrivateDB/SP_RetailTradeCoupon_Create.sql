DROP PROCEDURE IF EXISTS `SP_RetailTradeCoupon_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCoupon_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradeID INT,
	IN iCouponCodeID INT
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
		
		IF NOT EXISTS (SELECT 1 FROM t_retailtrade WHERE F_ID = iRetailTradeID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '���۵�������';
	  	ELSEIF NOT EXISTS (SELECT 1 FROM t_couponcode WHERE F_ID = iCouponCodeID) THEN
	  		SET iErrorCode := 2;
	  		SET sErrorMsg := '�����Ż�ȯ������';
	  	ELSEIF NOT EXISTS (SELECT 1 FROM t_couponcode tcc, t_retailtrade rt WHERE tcc.F_ID = iCouponCodeID and rt.F_ID = iRetailTradeID AND tcc.F_VipID = rt.F_VipID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '���Ż�ȯӵ���ߺͽ������۵��Ļ�Ա��һ��';
	  	ELSE 
			INSERT INTO t_retailtradecoupon (F_RetailTradeID, F_CouponCodeID, F_SyncDatetime) VALUES (iRetailTradeID, iCouponCodeID, now());
		
			SELECT F_ID, F_RetailTradeID, F_CouponCodeID, F_SyncDatetime FROM t_retailtradecoupon WHERE F_ID = LAST_INSERT_ID();
		END IF;
	COMMIT;
END;