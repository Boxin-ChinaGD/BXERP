DROP PROCEDURE IF EXISTS `SP_RetailTradeAggregation_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeAggregation_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStaffID INT,
	IN iPosID INT,
	IN dtWorkTimeStart DATETIME,
	IN dtWorkTimeEnd DATETIME,
	IN iTradeNO INT,
	IN fAmount Decimal(20,6),
	IN fReserveAmount Decimal(20,6),
	IN fCashAmount Decimal(20,6),
	IN fWechatAmount Decimal(20,6),
	IN fAlipayAmount Decimal(20,6),
	IN fAmount1 Decimal(20,6),
	IN fAmount2 Decimal(20,6),
	IN fAmount3 Decimal(20,6),
	IN fAmount4 Decimal(20,6),
	IN fAmount5 Decimal(20,6)
	)
BEGIN
	DECLARE icompanyID INT; 		 	-- ������˾	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT 1 FROM t_retailtradeaggregation WHERE F_StaffID = iStaffID AND F_PosID = iPosID AND F_WorkTimeStart = dtWorkTimeStart) THEN
			SELECT
				F_ID, F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, 
				F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime
			FROM t_retailtradeaggregation WHERE F_StaffID = iStaffID AND F_PosID = iPosID AND F_WorkTimeStart = dtWorkTimeStart;
			
			SET iErrorCode := 1; 
			SET sErrorMsg := 'Ψһ��StartWorkTime�ظ�ʱ������ʧ��';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID)  THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ�StaffID���д���'; 
		ELSEIF NOT EXISTS(SELECT 1 FROM t_pos WHERE F_ID = iPosID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ�PosID���д���'; 
		ELSE
			INSERT INTO t_retailtradeaggregation (
				F_StaffID, 
				F_PosID,
				F_WorkTimeStart, 
				F_WorkTimeEnd,
				F_TradeNO, 
				F_Amount, 
				F_ReserveAmount, 
				F_CashAmount, 
				F_WechatAmount,
				F_AlipayAmount, 
				F_Amount1, 
				F_Amount2, 
				F_Amount3, 
				F_Amount4, 
				F_Amount5, 
				F_UploadDateTime)
			VALUES (
				iStaffID,
				iPosID,
				dtWorkTimeStart, 
				dtWorkTimeEnd,
				iTradeNO, 
				fAmount, 
				fReserveAmount, 
				fCashAmount, 
				fWechatAmount, 
				fAlipayAmount, 
				fAmount1, 
				fAmount2, 
				fAmount3, 
				fAmount4, 
				fAmount5, 
				NOW());
		
			SELECT
				F_ID,
				F_StaffID, 
				F_PosID,
				F_WorkTimeStart, 
				F_WorkTimeEnd, 
				F_TradeNO, 
				F_Amount, 
				F_ReserveAmount, 
				F_CashAmount, 
				F_WechatAmount, 
				F_AlipayAmount, 
				F_Amount1, 
				F_Amount2, 
				F_Amount3, 
				F_Amount4, 
				F_Amount5, 
				F_UploadDateTime
			FROM t_retailtradeaggregation WHERE F_ID = LAST_INSERT_ID();
			SELECT F_CompanyID INTO icompanyID FROM t_shop WHERE F_ID = 1;
			INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_ReceiverID, F_CompanyID)
			VALUES (7, 0, '[{\"Link1\": \": \"www.xxxx.com\"}, \"}, {\"Link1_Tag\": \"����\"}]', now(), 1, icompanyID);
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;