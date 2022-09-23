DROP PROCEDURE IF EXISTS `SP_PromotionShopScope_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PromotionShopScope_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPromotionID INT,
   	IN iShopID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SET iErrorCode := 3;
	SET sErrorMsg := '���ݿ����';
	
	IF NOT EXISTS (SELECT 1 FROM T_Promotion WHERE F_ID = iPromotionID AND F_Status = 0) THEN 
	   SET iErrorCode := 7;
	   SET sErrorMsg := '���������ڣ����ܴ���������Χ'; -- ...
	ELSEIF NOT EXISTS (SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN 
	   SET iErrorCode := 7;
	   SET sErrorMsg := '����ʹ�ò����ڵ�shopID���д���'; -- ...
	ELSEIF EXISTS (SELECT 1 FROM t_promotionshopscope WHERE F_PromotionID = iPromotionID AND F_ShopID = iShopID) THEN
		SET iErrorCode := 1;
		SET sErrorMsg := 'һ�����������ظ����ͬһ���ŵ�';
	ELSE 
		INSERT INTO t_promotionShopScope (
			F_PromotionID, 
			F_ShopID,
			F_ShopName
		    )
		VALUES (	
			iPromotionID, 
			iShopID,
			(SELECT F_Name FROM t_shop WHERE F_ID = iShopID)
		    );
		       
	    SELECT 
		    F_ID, 
		    F_PromotionID, 
		    F_ShopID,
			F_ShopName
		FROM t_promotionshopscope WHERE F_ID = LAST_INSERT_ID();
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	END IF;
END;