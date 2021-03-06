-- 删除服务商品条形码 同时更新条形码的同存
DROP PROCEDURE IF EXISTS `SP_Barcodes_DeleteByServiceCommodityID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_DeleteByServiceCommodityID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT, -- 服务商品ID。
	IN iStaffID INT
)
BEGIN

	DECLARE barcodeID INT;
	DECLARE oldBarcode VARCHAR(128);
	DECLARE syncSequence INT;
	DECLARE iFuncReturnCode INT;
	DECLARE done INT DEFAULT FALSE;
	DECLARE listBarcodesID CURSOR FOR (
		SELECT F_ID FROM
		(
		    SELECT F_ID FROM t_barcodes bar 
		    WHERE F_CommodityID = iCommodityID 
		) AS tmp
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- 1, 删除X对应的同存
		-- 判断是否是服务商品 否则返回错误码7
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_TYPE = 3) THEN
			DELETE FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID IN 
			(
				SELECT F_ID FROM t_barcodessynccache WHERE F_SyncData_ID IN
				(
					SELECT F_ID FROM
					(
					    SELECT F_ID FROM t_barcodes bar 
					    WHERE F_CommodityID = iCommodityID 
					) AS tmp
				)
			);
		
			DELETE FROM t_barcodessynccache WHERE F_SyncData_ID IN
			(
				SELECT F_ID FROM
				(
					SELECT F_ID FROM t_barcodes bar 
				    WHERE F_CommodityID = iCommodityID
				) AS tmp
			);
		
			-- 2, 创建X对应的D型的同存
			SELECT max(F_SyncSequence) INTO syncSequence FROM t_barcodessynccache;
			IF syncSequence IS NULL 
				THEN SET syncSequence = 0;
			END IF ;
			
			OPEN listBarcodesID;
					read_loop: LOOP
				   		FETCH listBarcodesID INTO barcodeID;
				   		IF done THEN
				  			LEAVE read_loop;
				   		END IF;
				   			
						SET syncSequence := syncSequence + 1; 
						
						INSERT INTO t_barcodessynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
						VALUES (barcodeID, 'D', syncSequence, now());
		   	   		END LOOP read_loop;
		   	 CLOSE listBarcodesID;	
		
			-- 3, 生成商品操作历史	
			select group_concat(F_Barcode) INTO oldBarcode from 
			(
				SELECT DISTINCT F_Barcode FROM t_barcodes WHERE F_ID in
				(
					SELECT F_ID FROM
					(
					    SELECT F_ID FROM t_barcodes bar 
						WHERE F_CommodityID = iCommodityID
					) AS tmp1
				)
			) AS tmp2; 
			
			SELECT IF(oldBarcode IS NULL, '', oldBarcode) INTO oldBarcode; 
			
			select Func_CreateCommodityHistory(
				iCommodityID, 
				'$',
				'',  -- 新条形码为空，因为是删除条形码
				'$', 
				-1, 
				-1, 
				-100000000, 
				-100000000, 
				iStaffID,
				oldBarcode,
				''
			) INTO iFuncReturnCode; -- 0
		
			-- 4, 删除条形码。
			DELETE FROM t_barcodes WHERE F_ID IN
			(
				SELECT F_ID FROM
				(
				    SELECT F_ID FROM t_barcodes bar 
				    WHERE F_CommodityID = iCommodityID
				) AS tmp
			) AND F_ID NOT IN (SELECT F_BarcodeID FROM t_retailtradecommodity rc); -- t_retailtradecommodity有外键依赖,服务商品不需要考虑采购,入库,退货,盘点
			--  采购、入库、盘点、促销、采购退货的都是普通商品，所以服务商品不需要检查这些依赖		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
		    SET iErrorCode := 7;
		    SET sErrorMsg := '不是服务类型商品,删除服务类型商品的条形码失败';
		END IF ;
		
	COMMIT;
END;