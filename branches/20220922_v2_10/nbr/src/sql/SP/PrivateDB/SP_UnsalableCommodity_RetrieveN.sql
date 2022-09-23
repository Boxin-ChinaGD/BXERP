DROP PROCEDURE IF EXISTS `SP_UnsalableCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_UnsalableCommodity_RetrieveN`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),	
   	IN dtStartDate DATETIME,		 -- ��ʼ����
   	IN dtEndDate DATETIME,           -- ��������
   	IN iMessageIsRead INT,               -- 1��0��һ����Ϣ���Ա�Ϊ�Ѷ���δ��
   	IN sMessageParameter VARCHAR(255),      -- JSON��ʽ���ַ�������������Ĳ������Ա���д������������Ϣģ�壨����Ϣ�������ñ�����
   	IN iMessageCategoryID INT,		 -- ��Ϣ����ID
   	IN iCompanyID INT,				 -- ��˾ID
   	IN iMessageSenderID INT,         -- �����û�ID,0��ʾϵͳ���ͣ�����Ϊ�û����ͣ�ֵΪ�û�ID
   	IN iMessageReceiverID INT        -- �����û�ID,��ʵ�û�ID��������Ⱥ������ҪΪÿһ�˲���һ����Ϣ
)


BEGIN
	DECLARE done INT DEFAULT  false;
	DECLARE commodityID INT; 
	DECLARE messageID INT DEFAULT 0;
	-- ����dStartDate��dEndDate��ѯ�����е�������Ʒ,�����α�list��
	DECLARE list CURSOR FOR(
		SELECT F_ID 
		FROM t_commodity 
		WHERE F_Type = 0 		-- ��Ʒ
			AND F_Status != 2 	-- ������ɾ��״̬
--			AND F_NO > 0 		-- ������0
			AND F_ID IN (SELECT F_CommodityID FROM t_commodityshopinfo WHERE F_NO > 0)
--			AND F_PriceRetail > 0 -- ���ۼ۴���0
			AND F_ID IN (SELECT F_CommodityID FROM t_commodityshopinfo WHERE F_PriceRetail > 0)
			AND F_ID NOT IN 	-- ��dStartDate��dEndDate֮��û�н������۹�
			(SELECT cd.F_ID
			 FROM t_retailtrade rt,t_retailtradecommodity rtc, t_commodity cd 
			 WHERE F_SaleDatetime >= dtStartDate 
			 	AND F_SaleDatetime <= dtEndDate
			 	AND F_SourceID = -1 
			 	AND rtc.F_TradeID = rt.F_ID 
			 	AND cd.F_ID = rtc.F_CommodityID 
			 	GROUP BY cd.F_ID) 
   	);
   	
   	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	    
		-- ������Ϣ
		IF NOT EXISTS (SELECT 1 FROM t_messagecategory WHERE F_ID = iMessageCategoryID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�����ڸ���Ϣ���';
		ELSEIF NOT EXISTS (SELECT 1 FROM nbr_bx.t_company WHERE F_ID = iCompanyID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�����ڸù�˾';
		ELSE 
			-- ɾ���ɵ�������
			DELETE FROM t_messageitem WHERE F_MessageCategoryID = iMessageCategoryID;
			-- 
		   	INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID, F_CompanyID)
			VALUES (iMessageCategoryID, iMessageIsRead, sMessageParameter, now(), iMessageSenderID, iMessageReceiverID, iCompanyID);
			
			SET messageID = last_insert_id();
			-- ������Ϣ��
			OPEN list;
				read_loop: LOOP
			FETCH list INTO commodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
		
				INSERT INTO t_messageitem(F_MessageID, F_MessageCategoryID, F_CommodityID) VALUES (messageID, iMessageCategoryID, commodityID);
						  		
			END LOOP read_loop;
			CLOSE list;
			
			SELECT 
				F_ID, 
				F_Status, 
				F_Name, 
				F_ShortName, 
				F_Specification, 
				F_PackageUnitID, 
				F_PurchasingUnit, 
				F_BrandID, 
				F_CategoryID, 
				F_MnemonicCode, 
				F_PricingType, 
	--			F_IsServiceType, 
	--			F_PricePurchase, 
--				F_LatestPricePurchase, 
--				F_PriceRetail, 
				F_PriceVIP, 
				F_PriceWholesale, 
	--			F_RatioGrossMargin, 
				F_CanChangePrice, 
				F_RuleOfPoint, 
				F_Picture, 
				F_ShelfLife, 
				F_ReturnDays, 
				F_CreateDate, 
				F_PurchaseFlag, 
				F_RefCommodityID, 
				F_RefCommodityMultiple, 
 --				F_IsGift, 
				F_Tag, 
--				F_NO, 
--				F_NOAccumulated, 
				F_Type, 
--				F_NOStart, 
--				F_PurchasingPriceStart, 
				F_StartValueRemark, 
				F_CreateDatetime, 
				F_UpdateDatetime, 
				F_PropertyValue1, 
				F_PropertyValue2, 
				F_PropertyValue3, 
				F_PropertyValue4,
--				F_CurrentWarehousingID,
				(SELECT F_Name FROM t_brand WHERE F_ID = t.F_BrandID) AS brandName,
				(SELECT F_Name FROM t_category WHERE F_ID = t.F_CategoryID) AS categoryName
			FROM T_Commodity t WHERE F_ID IN
				(SELECT F_CommodityID FROM t_messageitem WHERE F_MessageCategoryID = iMessageCategoryID);
			
			SELECT 
				F_ID,
				F_CategoryID,
				F_CompanyID,
				F_IsRead,
				F_Status,
				F_Parameter,
				F_CreateDatetime,
				F_SenderID,
				F_ReceiverID,
				F_UpdateDatetime
			FROM t_message WHERE F_ID = messageID;
			
			SET iErrorCode = 0;
			SET sErrorMsg = '';
		END IF;
	COMMIT;
END;