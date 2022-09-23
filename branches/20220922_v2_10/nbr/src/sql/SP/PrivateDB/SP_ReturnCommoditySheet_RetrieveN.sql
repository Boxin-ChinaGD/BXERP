DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN string1 VARCHAR(32),
	IN iStaffID INT,
	IN iStatus INT,
	IN iProviderID INT,
	IN dtStart DATETIME,
	IN dtEnd DATETIME,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
			
		SET iPageIndex = iPageIndex - 1; 
		
		SET recordIndex = iPageIndex * iPageSize; 
		
		IF (string1 != '') THEN 
		
			SELECT  
				F_ID,
				F_SN,
				F_StaffID,
				F_ProviderId,
				F_Status,
				F_CreateDatetime,
				F_UpdateDatetime,
				F_ShopID
			FROM 
			( 
				SELECT  -- �����˻���ID��ѯ
					rcs.F_ID,
					F_SN,
					rcs.F_StaffID,
					rcs.F_ProviderId,
					rcs.F_Status,
					rcs.F_CreateDatetime,
					rcs.F_UpdateDatetime,
					rcs.F_ShopID
				FROM t_returncommoditysheet rcs 
				WHERE length(string1) > 9 AND rcs.F_SN LIKE CONCAT('%',replace(string1, '_', '\_') ,'%')  
				UNION
				SELECT -- ������Ʒ���Ʋ�ѯ(δ���)
					rcs.F_ID,
					F_SN,
					rcs.F_StaffID,
					rcs.F_ProviderId,
					rcs.F_Status,
					rcs.F_CreateDatetime,
					rcs.F_UpdateDatetime,
					rcs.F_ShopID
				FROM t_returncommoditysheet rcs
				WHERE F_ID IN(
					SELECT rcsc.F_ReturnCommoditySheetID FROM t_returncommoditysheetcommodity rcsc
					WHERE rcsc.F_CommodityID IN( 
						SELECT c.F_ID FROM t_commodity c
						WHERE c.F_Name LIKE CONCAT('%',replace(string1, '_', '\_'),'%')
					) 
					AND rcs.F_Status = 0
				)
				UNION
				SELECT -- ������Ʒ���Ʋ�ѯ(�����)
					rcs.F_ID,
					F_SN,
					rcs.F_StaffID,
					rcs.F_ProviderId,
					rcs.F_Status,
					rcs.F_CreateDatetime,
					rcs.F_UpdateDatetime,
					rcs.F_ShopID
				FROM t_returncommoditysheet rcs
				WHERE F_ID IN(
					SELECT rcsc.F_ReturnCommoditySheetID FROM t_returncommoditysheetcommodity rcsc
					WHERE rcsc.F_CommodityName LIKE CONCAT('%',replace(string1, '_', '\_'),'%')
				) 
				AND rcs.F_Status = 1
			)AS TMP
			WHERE 1=1 
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ShopID = iShopID END) -- �����ŵ�ID��ѯ
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_StaffID = iStaffID END) -- ���ݾ�����ID��ѯ
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE TMP.F_Status = iStatus END) -- �����˻���״̬��ѯ
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ProviderID = iProviderID END) -- ���ݹ�Ӧ��ID��ѯ
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE TMP.F_CreateDatetime >= dtStart END) -- ����ʱ���ѯ
				AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE TMP.F_CreateDatetime <= dtEnd END) -- ����ʱ���ѯ
				
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
		ELSE
			SELECT 
				rcs.F_ID,
				rcs.F_SN,
				rcs.F_StaffID,
				rcs.F_ProviderID,
				rcs.F_Status,
				rcs.F_CreateDatetime,
				rcs.F_UpdateDatetime,
				rcs.F_ShopID
			FROM t_returncommoditysheet rcs 
			WHERE 1=1 
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ShopID = iShopID END) -- �����ŵ�ID��ѯ
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_StaffID = iStaffID END) -- ���ݾ�����ID��ѯ
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE rcs.F_Status = iStatus END) -- �����˻���״̬��ѯ
			   	AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ProviderID = iProviderID END) -- ���ݹ�Ӧ��ID��ѯ
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE rcs.F_CreateDatetime >= dtStart END) -- ����ʱ���ѯ
	 			AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE rcs.F_CreateDatetime <= dtEnd END) -- ����ʱ���ѯ
	 			
	 		ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;
		
		END IF;
		
		IF (string1 !='') THEN
		
			SELECT COUNT(1) INTO iTotalRecord 
			FROM 
			( 
				SELECT rcs.F_ID FROM t_returncommoditysheet rcs 
				WHERE length(string1) > 9 AND rcs.F_SN LIKE CONCAT('%', replace(string1, '_', '\_') ,'%')  
				UNION
				SELECT rcs.F_ID FROM t_returncommoditysheet rcs 
				WHERE F_ID IN(
					SELECT rcsc.F_ReturnCommoditySheetID FROM t_returncommoditysheetcommodity rcsc
					WHERE rcsc.F_CommodityID IN( 
						SELECT c.F_ID FROM t_commodity c
						WHERE c.F_Name LIKE CONCAT('%',replace(string1, '_', '\_'),'%')
					) 
					AND rcs.F_Status = 0
				)
				UNION
				SELECT rcs.F_ID FROM t_returncommoditysheet rcs 
				WHERE F_ID IN(
					SELECT rcsc.F_ReturnCommoditySheetID FROM t_returncommoditysheetcommodity rcsc
					WHERE rcsc.F_CommodityName LIKE CONCAT('%',replace(string1, '_', '\_'),'%')
				) 
					AND rcs.F_Status = 1
			)AS TMP
			WHERE 1=1
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_ShopID = iShopID) END) 
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_StaffID = iStaffID) END) 
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_Status = iStatus) END)
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_ProviderID = iProviderID) END)
				AND (CASE ifnull(dtstart, 0) WHEN 0 THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_CreateDatetime >= dtStart) END)
				AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE TMP.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_CreateDatetime <= dtEnd) END);
				
		ELSE
			SELECT COUNT(1) INTO iTotalRecord 
			FROM t_returncommoditysheet rcs 
			WHERE 1=1 
				AND (CASE iShopID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_ShopID = iShopID) END) 
				AND (CASE iStaffID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_StaffID = iStaffID) END) 
				AND (CASE iStatus WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_Status = iStatus) END) 
				AND (CASE iProviderID WHEN INVALID_ID THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_ProviderID = iProviderID) END) 
				AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_CreateDatetime >= dtStart) END) 
				AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE rcs.F_ID IN (SELECT F_ID FROM t_returncommoditysheet WHERE F_CreateDatetime <= dtEnd) END);
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;