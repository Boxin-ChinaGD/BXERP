-- ɾ��������Ʒ������ ͬʱ�����������ͬ��
DROP PROCEDURE IF EXISTS `SP_Barcodes_DeleteByServiceCommodityID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_DeleteByServiceCommodityID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT, -- ������ƷID��
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- 1, ɾ��X��Ӧ��ͬ��
		-- �ж��Ƿ��Ƿ�����Ʒ ���򷵻ش�����7
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
		
			-- 2, ����X��Ӧ��D�͵�ͬ��
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
		
			-- 3, ������Ʒ������ʷ	
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
		
			-- 4, ɾ�������롣
			DELETE FROM t_barcodes WHERE F_ID IN
			(
				SELECT F_ID FROM
				(
				    SELECT F_ID FROM t_barcodes bar 
				    WHERE F_CommodityID = iCommodityID
				) AS tmp
			) AND F_ID NOT IN (SELECT F_BarcodeID FROM t_retailtradecommodity rc); -- t_retailtradecommodity���������,������Ʒ����Ҫ���ǲɹ�,���,�˻�,�̵�
			--  �ɹ�����⡢�̵㡢�������ɹ��˻��Ķ�����ͨ��Ʒ�����Է�����Ʒ����Ҫ�����Щ����		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
		    SET iErrorCode := 7;
		    SET sErrorMsg := '���Ƿ���������Ʒ,ɾ������������Ʒ��������ʧ��';
		END IF ;
		
	COMMIT;
END;