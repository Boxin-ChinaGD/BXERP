--	SELECT '-------------------- Case1：给零售单生成一个单号 -------------------------' AS 'Case1';
--	SET @sSN := 'LS201906050001';
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_POS_SN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (1,@sSN,-1,1,'url=ashasoadigmnalskd','2017-08-06',2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2);
--	SET @id = LAST_INSERT_ID();
--	-- 
--	SELECT Func_GenerateSN('LS', @sSN) INTO @sNewSN;
--	SELECT IF(@sNewSN <> @sSN, '测试成功', '测试失败') AS 'Case1 Testing Result';
--	-- 
--	DELETE FROM t_retailtrade WHERE F_ID = @id;

SELECT '-------------------- Case2：采购订单第一天首次创建 -------------------------' AS 'Case2';
SET @sSN := 'xxx';
INSERT INTO T_PurchasingOrder (F_Status,F_SN,F_CreateDatetime,F_ApproveDatetime,F_EndDatetime,F_StaffID,F_ProviderID,F_ProviderName,F_Remark,F_ApproverID)
VALUES (1,@sSN,'2016-12-06 1:01:01',now(),'2017-10-17 1:01:01',2,1,'默认供应商','红红火火恍恍惚惚',NULL);
SET @id = LAST_INSERT_ID();
-- 
SELECT Func_GenerateSN('CG', @sSN) INTO @sNewSN;
SELECT @sNewSN;
SELECT IF(@sNewSN <> @sSN, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3：采购订单第一天第二次创建 -------------------------' AS 'Case3';
SET @sSN := @sNewSN;
SELECT Func_GenerateSN('CG', @sSN) INTO @sNewSN;
SELECT @sNewSN;
SELECT IF(@sNewSN = (CONCAT('CG', date_format(now(),'%Y%m%d'), lpad((CAST(RIGHT(@sSN, 4) AS UNSIGNED INTEGER) + 1), 4, 0))), '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case4：第二天生成的SN码并不会再前几天的基础上递增 -------------------------' AS 'Case4';
SET @sSN := CONCAT('CG', date_format(DATE_SUB(curdate(),INTERVAL 1 DAY),'%Y%m%d'), '0001');
SELECT Func_GenerateSN('CG', @sSN) INTO @sNewSN;
SELECT @sNewSN;
SELECT IF(@sNewSN <> CONCAT(LEFT(@sSN,10), '','0002'), '测试成功', '测试失败') AS 'Case4 Testing Result';