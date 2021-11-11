DROP PROCEDURE IF EXISTS `SP_VipCardCode_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCardCode_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iVipID INT,
	IN iVipCardID INT,
	IN iCompanySN VARCHAR(6)
)
BEGIN
	DECLARE sSN VARCHAR(16);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
        
        IF NOT EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '该会员不存在';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_vipcard WHERE F_ID = iVipCardID) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '该会员卡不存在';
		ELSE
			read_loop: LOOP -- 循环获取到唯一的SN码为止
		    	SET sSN = CONCAT(iCompanySN, Func_GenerateCouponSN(10));
		    	
	            IF NOT EXISTS(SELECT 1 FROM t_vipcardcode WHERE F_SN = sSN) THEN
	                LEAVE read_loop;
	            end IF;
        	END LOOP read_loop;
			
			INSERT INTO t_vipcardcode (F_VipID, F_VipCardID, F_SN, F_CreateDatetime)
			VALUES (iVipID, iVipCardID, sSN, now());
			
			SELECT F_ID, F_VipID, F_VipCardID, F_SN, F_CreateDatetime
			FROM t_vipcardcode WHERE F_ID = LAST_INSERT_ID();
		END IF;

	COMMIT;
END;