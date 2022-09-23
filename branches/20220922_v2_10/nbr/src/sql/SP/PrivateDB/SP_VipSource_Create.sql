DROP PROCEDURE IF EXISTS `SP_VipSource_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipSource_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iVipID INT,
	IN iSourceCode INT,
	IN sID1 VARCHAR(50),
	IN sID2 VARCHAR(50),
	IN sID3 VARCHAR(50)
)
BEGIN
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
		ELSEIF EXISTS(SELECT 1 FROM t_vipsource WHERE F_VipID = iVipID AND F_SourceCode = iSourceCode) THEN
			IF EXISTS(SELECT 1 FROM t_vipsource WHERE F_VipID = iVipID AND F_SourceCode = iSourceCOde AND F_ID1 = sID1 AND F_ID2 = sID2 AND F_ID3 = sID3) THEN 
				SELECT F_ID, F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3
		   		FROM t_vipsource WHERE F_VipID = iVipID AND F_SourceCode = iSourceCode;
		   	ELSE -- 先在网页创建(此时ID123都是空串)，再在小程序请求后创建
		   		UPDATE t_vipsource SET F_ID1 = sID1, F_ID2 = sID2, F_ID3 = sID3 WHERE F_VipID = iVipID;
		   		-- 
		   		SELECT F_ID, F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3
				FROM t_vipsource WHERE F_VipID = iVipID AND F_SourceCode = iSourceCode AND F_ID1 = sID1 AND F_ID2 = sID2 AND F_ID3 = sID3;
			END IF;
		ELSE
			INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
			VALUES (iVipID, iSourceCode, sID1, sID2, sID3);
			
			SELECT F_ID, F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3
			FROM t_vipsource WHERE F_ID = LAST_INSERT_ID();
		END IF;

	COMMIT;
END;