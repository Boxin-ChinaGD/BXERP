-- ɾ����ƷiCommodityID������װ��Ʒ��Ӧ�������룬ͬʱ�����������ͬ��
-- ���ﲻ��Ҫ���������Ʒ������1�������룬��Ϊ����ɾ��������Ʒ����ɾ����Ʒ�������벻һ����������һ����Ч����Ʒ��������������1��������
DROP PROCEDURE IF EXISTS `SP_Barcodes_DeleteBySimpleCommodityID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_DeleteBySimpleCommodityID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT, -- ��ƷID����ҳ��ֻ��ʾ��Ʒ��ID���û�ɾ��
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		-- 1, �õ����д�ɾ�����������ID�ļ���X.�����ظ�ʹ��.
	--		(SELECT F_ID FROM t_barcodes bar WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_RefCommodityID = iCommodityID)
	--			AND (SELECT count(F_Barcode) FROM t_barcodes WHERE F_Barcode = bar.F_Barcode) < 2); -- ����������ܱ�������Ʒ��ʹ��,�����޳�
	--		)
	--		UNION
	--		(SELECT F_ID FROM t_barcodes bar WHERE F_CommodityID = iCommodityID 
	--		AND (SELECT count(F_Barcode) FROM t_barcodes WHERE F_Barcode = bar.F_Barcode) < 2); -- ����������ܱ�������Ʒ��ʹ��,�����޳�
	--		)
		
		-- 2, ɾ��X��Ӧ��ͬ��
		-- �ж��Ƿ��ǵ�Ʒ ���򷵻ش�����7
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
		
			-- 3, ����X��Ӧ��D�͵�ͬ��
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
		
		--	SIGNAL SQLSTATE '45001' SET MYSQL_ERRNO=2000,MESSAGE_TEXT='���ⴥ���쳣';
			-- 4, ������Ʒ������ʷ	
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
				'',  -- ��������Ϊ�գ���Ϊ��ɾ��������
				'$', 
				-1, 
				-1, 
				-100000000, 
				-100000000, 
				iStaffID,
				oldBarcode,
				''
			) INTO iFuncReturnCode; -- 0
		
		
			-- 5, ɾ�������롣
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
			) AND F_ID NOT IN (SELECT F_BarcodeID FROM t_returncommoditysheetcommodity rcsc)-- t_returncommoditysheetcommodity���������
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_inventorycommodity ic) -- t_inventorycommodity���������
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_retailtradecommodity rc)-- t_retailtradecommodity���������
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_warehousingcommodity wc)-- t_warehousingcommodity���������
			AND F_ID NOT IN (SELECT F_BarcodeID FROM t_purchasingordercommodity poc) -- t_purchasingordercommodity���������
			AND F_ID NOT IN (SELECT tmp2.F_ID FROM (SELECT F_ID FROM t_barcodes WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_promotionscope)) AS tmp2); -- t_promotionscope������
			-- ������Χ����
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
		    SET iErrorCode := 7;
		    SET sErrorMsg := '��Ʒ������,ɾ�����Ӧ��������ʧ��';
		END IF ;
	COMMIT;
END;