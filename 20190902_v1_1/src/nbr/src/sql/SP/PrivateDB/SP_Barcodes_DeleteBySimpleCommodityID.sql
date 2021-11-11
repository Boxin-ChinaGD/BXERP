-- 删除商品iCommodityID及其多包装商品对应的条形码，同时更新条形码的同存
-- 这里不需要限制这个商品至少有1个条形码，因为它是删除整个商品。和删除商品的条形码不一样。后者是一个有效的商品，它必须至少有1个条形码
DROP PROCEDURE IF EXISTS `SP_Barcodes_DeleteBySimpleCommodityID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_DeleteBySimpleCommodityID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT, -- 单品ID。网页上只显示单品的ID给用户删除
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
			(
				SELECT F_ID FROM t_barcodes bar 
				WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
			)
			UNION
			(
				SELECT F_ID FROM t_barcodes bar 
				WHERE F_CommodityID = iCommodityID 
			)
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
		-- 1, 拿到所有待删除的条形码的ID的集合X.下面重复使用.
	--		(SELECT F_ID FROM t_barcodes bar WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
	--			AND (SELECT count(F_Barcode) FROM t_barcodes WHERE F_Barcode = bar.F_Barcode) < 2); -- 此条形码可能被其它商品所使用,必须剔除
	--		)
	--		UNION
	--		(SELECT F_ID FROM t_barcodes bar WHERE F_CommodityID = iCommodityID 
	--		AND (SELECT count(F_Barcode) FROM t_barcodes WHERE F_Barcode = bar.F_Barcode) < 2); -- 此条形码可能被其它商品所使用,必须剔除
	--		)
		
		-- 2, 删除X对应的同存
		-- 判断是否是单品 否则返回错误码7
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_TYPE = 0) THEN
			DELETE FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID IN 
			(
				SELECT F_ID FROM t_barcodessynccache WHERE F_SyncData_ID IN
				(
					SELECT F_ID FROM
					(
						(
							SELECT F_ID FROM t_barcodes bar 
							WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
						)
						UNION
						(
							SELECT F_ID FROM t_barcodes bar 
							WHERE F_CommodityID = iCommodityID 
						)
					) AS tmp
				)
			);
		
			DELETE FROM t_barcodessynccache WHERE F_SyncData_ID IN
			(
				SELECT F_ID FROM
				(
					(
						SELECT F_ID FROM t_barcodes bar 
						WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
					)
					UNION
					(
						SELECT F_ID FROM t_barcodes bar 
						WHERE F_CommodityID = iCommodityID 
					)
				) AS tmp
			);
		
			-- 3, 创建X对应的D型的同存
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
		
		--	SIGNAL SQLSTATE '45001' SET MYSQL_ERRNO=2000,MESSAGE_TEXT='故意触发异常';
			-- 4, 生成商品操作历史	
			select group_concat(F_Barcode) INTO oldBarcode from 
			(
				SELECT DISTINCT F_Barcode FROM t_barcodes WHERE F_ID in
				(
					SELECT F_ID FROM
					(
						(
							SELECT F_ID FROM t_barcodes bar 
							WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
						)
						UNION
						(
							SELECT F_ID FROM t_barcodes bar 
							WHERE F_CommodityID = iCommodityID
						)
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
		
		
			-- 5, 删除条形码。
			DELETE FROM t_barcodes WHERE F_ID IN
			(
				SELECT F_ID FROM
				(
					(
						SELECT F_ID FROM t_barcodes bar 
						WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
					)
					UNION
					(
						SELECT F_ID FROM t_barcodes bar 
						WHERE F_CommodityID = iCommodityID
					)
				) AS tmp
			) AND F_ID NOT IN (SELECT F_BarcodeID FROM t_returncommoditysheetcommodity rcsc)-- t_returncommoditysheetcommodity有外键依赖
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_inventorycommodity ic) -- t_inventorycommodity有外键依赖
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_retailtradecommodity rc)-- t_retailtradecommodity有外键依赖
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_warehousingcommodity wc)-- t_warehousingcommodity有外键依赖
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_purchasingordercommodity poc) -- t_purchasingordercommodity有外键依赖
			AND F_ID NOT IN (SELECT tmp2.F_ID FROM (SELECT F_ID FROM t_barcodes WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_promotionscope)) AS tmp2); -- t_promotionscope有依赖
			-- 促销范围依赖
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
		    SET iErrorCode := 7;
		    SET sErrorMsg := '单品不存在,删除相对应的条形码失败';
		END IF ;
	COMMIT;
END;