DROP PROCEDURE IF EXISTS `SP_RetailTradeReportByCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeReportByCommodity_RetrieveN`(
   	OUT iErrorCode INT,   	-- ������
   	OUT sErrorMsg VARCHAR(64),
    OUT iTotalRecord INT, 	-- �ܼ�¼��
   	IN dtStart DATE,		-- �������ڵĿ�ʼʱ��	
	IN dtEnd DATETIME,		-- �������ڵĽ���ʱ��
	IN string1 VARCHAR(32),	-- ����ģ����ѯ��״̬
	IN isASC INT,		   	-- ����	
	IN iOrderBy INT,		-- ����״̬��������   0��1������2ë��
	IN iPageIndex INT,		-- ҳ��
	IN iPageSize INT,		-- ҳ����
	IN bIgnoreZeroNO INT,	-- �Ų�Ϊ0    1����ʾ��������Ϊ0����Ʒ�� 0����ʾ
	IN iCategoryID INT,		-- ��Ʒ����ID   -1�����ѯ���е���Ʒ����
	IN iShopID INT
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
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';  -- -1
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
	
		-- �����Ĺ�ϵΪ��һ�����۵���Ӧ������۵���Ʒ��  һ�����۵���Ʒ���Ӧһ����Ʒ��һ��������
		SELECT F_ID, F_Name, F_Specification, F_PackageUnitName, F_Barcode, F_NO, F_Amouunt, F_TradeID, F_AveragePrice, F_GrossMargin, F_SaleDatetime  
		FROM(
			SELECT
				comm.F_ID AS F_ID,
				comm.F_Name AS F_Name,
				comm.F_Specification AS F_Specification,  					-- ���
				(SELECT F_Name FROM t_packageunit pu WHERE pu.F_ID IN ( SELECT comm.F_PackageUnitID FROM t_commodity WHERE comm.F_PackageUnitID = pu.F_ID )) AS F_PackageUnitName, -- ��װ��λ����
				(SELECT group_concat(F_Barcode) FROM t_barcodes bc WHERE bc.F_ID = rtc.F_BarcodeID) AS F_Barcode, -- ������
				rtc.F_NO AS F_NO,
			    commShopInfo.F_PriceRetail * rtc.F_NO AS F_Amouunt,					-- ���
			    rtc.F_TradeID AS F_TradeID,                 				-- ���۵���Ʒ���е�����id(���۵�id)
			    (commShopInfo.F_PriceRetail * rtc.F_NO) / rtc.F_NO AS F_AveragePrice, -- ���۾���
			    rtc.F_NO / (commShopInfo.F_PriceRetail * rtc.F_NO) AS F_GrossMargin,  -- ����ë��   -- ...  (��δ�о����� ��ʱ����)
				rt.F_SaleDatetime AS F_SaleDatetime
			FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt, t_commodityshopinfo AS commShopInfo
			WHERE rtc.F_CommodityID = comm.F_ID AND rtc.F_TradeID = rt.F_ID AND commShopInfo.F_CommodityID = comm.F_ID AND commShopInfo.F_ShopID = iShopID
				  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- ������id���в�ѯ
		)AS tmp
		WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_TradeID = string1) 
			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- ����Ϊ0��������Ʒ
		 	  AND F_SaleDatetime BETWEEN dtStart AND dtEnd
		ORDER BY
			  -- ����Ʒ �����ݽ�������
			  -- iOrderByΪ(0,1,2)ʱ�����۽�����������ë�����������isASCΪ1ʱ������������
	    	  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END DESC,
			  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END ASC
		LIMIT recordIndex, iPageSize;
	
	 
		-- ��������۱����������
		SELECT count(1) INTO iTotalRecord
		FROM(
			SELECT
				comm.F_ID AS F_ID,
				comm.F_Name AS F_Name,
				comm.F_Specification AS F_Specification,  					-- ���
				(SELECT F_Name FROM t_packageunit pu WHERE pu.F_ID IN ( SELECT comm.F_PackageUnitID FROM t_commodity WHERE comm.F_PackageUnitID = pu.F_ID )) AS F_PackageUnitName, -- ��װ��λ����
				(SELECT group_concat(F_Barcode) FROM t_barcodes bc WHERE bc.F_ID = rtc.F_BarcodeID) AS F_Barcode, -- ������
				rtc.F_NO AS F_NO,
			    commShopInfo.F_PriceRetail * rtc.F_NO AS F_Amouunt,					-- ���
			    rtc.F_TradeID AS F_TradeID,                 				-- ���۵���Ʒ���е�����id(���۵�id)
			    (commShopInfo.F_PriceRetail * rtc.F_NO) / rtc.F_NO AS F_AveragePrice, -- ���۾���
			    rtc.F_NO / (commShopInfo.F_PriceRetail * rtc.F_NO) AS F_GrossMargin,  -- ����ë��   -- ...  (��δ�о����� ��ʱ����)
				rt.F_SaleDatetime AS F_SaleDatetime
			FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt, t_commodityshopinfo AS commShopInfo
			WHERE rtc.F_CommodityID = comm.F_ID AND rtc.F_TradeID = rt.F_ID  AND commShopInfo.F_CommodityID = comm.F_ID AND commShopInfo.F_ShopID = iShopID
				  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- ������id���в�ѯ
		)AS tmp
		WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_TradeID = string1) 
			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- ����Ϊ0��������Ʒ
		 	  AND F_SaleDatetime BETWEEN dtStart AND dtEnd
		ORDER BY
			  -- ����Ʒ �����ݽ�������
			  -- iOrderByΪ(0,1,2)ʱ�����۽�����������ë�����������isASCΪ1ʱ������������
	    	  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END DESC,
			  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END ASC;
	   
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;