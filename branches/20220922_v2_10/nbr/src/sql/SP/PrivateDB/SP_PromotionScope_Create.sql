DROP PROCEDURE IF EXISTS `SP_PromotionScope_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PromotionScope_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPromotionID INT,
   	IN iCommodityID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET iErrorCode := 3;
	SET sErrorMsg := '���ݿ����';
	
	IF NOT EXISTS (SELECT 1 FROM T_Promotion WHERE F_ID = iPromotionID AND F_Status = 0) THEN 
	   SET iErrorCode := 7;
	   SET sErrorMsg := '���������ڣ����ܴ���������Χ'; -- ...
	ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN 
	   SET iErrorCode := 7;
	   SET sErrorMsg := '����ʹ�ò����ڵ�CommodityID���д���'; -- ...
	ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type <> 0) THEN 
		SET iErrorCode := 7;
		SET sErrorMsg := '����ͨ��Ʒ���ܲ�������';
	ELSEIF EXISTS (SELECT 1 FROM t_promotionscope WHERE F_PromotionID = iPromotionID AND F_CommodityID = iCommodityID) THEN
		SET iErrorCode := 1;
		SET sErrorMsg := 'һ�������������ͬ�����͵���Ʒ';
	ELSE 
		INSERT INTO t_promotionScope (
			F_PromotionID, 
			F_CommodityID,
			F_CommodityName
		    )
		VALUES (	
			iPromotionID, 
			iCommodityID,
			(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID)
		    );
		       
	    SELECT 
		    F_ID, 
		    F_PromotionID, 
		    F_CommodityID,
			F_CommodityName
		FROM t_promotionScope WHERE F_ID = LAST_INSERT_ID();
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	END IF;
END;