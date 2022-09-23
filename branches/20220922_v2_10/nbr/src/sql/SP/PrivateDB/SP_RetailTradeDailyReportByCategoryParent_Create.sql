DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByCategoryParent_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByCategoryParent_Create`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64), 
   	IN iShopID INT,	
   	IN dSaleDatetime DATETIME,		 -- ��������
   	IN deleteOldData INT            -- �����ڲ��ԣ�����ʱ�����ֵ�ж��Ƿ���Ҫɾ��ǰһ������ Ϊ1ʱɾ����
)
BEGIN
	DECLARE isSameDatetime INT;
	DECLARE returnAmount DECIMAL(20, 6);             -- �˻���
	DECLARE retailAmount DECIMAL(20, 6); 		 	 -- ���۶�
	DECLARE categoryparentID INT;                    -- ��Ʒ����ID
	DECLARE totalNO INT; 
	DECLARE done INT DEFAULT  false;
	DECLARE tempCategoryparentID DECIMAL(20, 6); 	 -- ��ʱ����ID�������α�ı���
	DECLARE tempAmount DECIMAL(20, 6) DEFAULT 0;  	 -- ��ʱ�ܶ�����α�ı���
	   	
	-- ��ѯ����һ�����еķ���ID
	DECLARE list3 CURSOR FOR(
	   	SELECT distinct F_ID FROM t_categoryparent WHERE F_ID IN (SELECT F_ParentID FROM t_category WHERE F_ID IN (
	   		SELECT F_CategoryID FROM t_commodity WHERE F_ID IN (
	   			SELECT F_CommodityID FROM t_retailtradecommodity rtc WHERE F_TradeID IN (
	   					SELECT F_ID FROM t_retailtrade rt WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 AND F_ShopID = iShopID
	   				)
	   			)
	   		)
	   	)
   	);
   	
   	-- ��ѯ����һ�����н��й����۵ķ��࣬�����������������ܶ�
   	DECLARE list2 CURSOR FOR(
   		SELECT cp.F_ID AS categoryparentID, sum(rtc.F_PriceReturn * rtc.F_NO) AS amount
		FROM t_retailtrade rt,t_retailtradecommodity rtc,t_commodity cd,t_category c,t_categoryparent cp
		WHERE rt.F_ID = rtc.F_TradeID 
	 		AND cd.F_ID = rtc.F_CommodityID 
	 		AND c.F_ID = cd.F_CategoryID
	 		AND cp.F_ID = c.F_ParentID
	 		AND	datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
	 		AND rt.F_SourceID = -1
	 		AND rt.F_ShopID = iShopID
		 	GROUP BY cp.F_ID
   	);
   	
   	-- ��ѯ����һ�����н��й��˻��ķ��࣬�������������˻��ܶ�
   	DECLARE list1 CURSOR FOR(
   		SELECT cp.F_ID AS categoryparentID, sum(rtc.F_PriceReturn * rtc.F_NO) AS amount
		FROM t_retailtrade rt,t_retailtradecommodity rtc,t_commodity cd,t_category c,t_categoryparent cp
		WHERE rt.F_ID = rtc.F_TradeID 
	 		AND cd.F_ID = rtc.F_CommodityID 
	 		AND c.F_ID = cd.F_CategoryID
	 		AND cp.F_ID = c.F_ParentID
	 		AND	datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
	 		AND rt.F_SourceID <> -1
	 		AND rt.F_ShopID = iShopID
		 	GROUP BY cp.F_ID
   	);
	             
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;                               
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
		
		-- �����ڲ��ԣ���֤����ͨ��
		IF deleteOldData = 1 THEN 
		   DELETE FROM t_retailtradedailyreportbycategoryparent WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		END IF;	
			
		-- �������۱��������۵�����
		SELECT NO INTO totalNO 
		FROM 
			(
				SELECT count(F_ID) AS NO
				FROM  t_retailtrade 
				WHERE datediff(F_SaleDatetime,dSaleDatetime) = 0  -- datediff(date1,date2) ��������date֮����������
				AND F_ShopID = iShopID
			) AS tmp1;	
				
		-- ��ѯ���ݿ��������ݣ��Ƿ��������ʱ��
		SELECT NO INTO isSameDatetime 
		FROM 
			(
				SELECT count(F_Datetime) AS NO
				FROM  t_retailtradedailyreportbycategoryparent 
				WHERE datediff(F_Datetime,dSaleDatetime) = 0  -- datediff(date1,date2) ��������date֮����������
				AND F_ShopID = iShopID
			) AS tmp2;
		
		IF totalNO > 0 THEN
			IF isSameDatetime <> 0 THEN 
				SET iErrorCode := 7;
				SET sErrorMsg := '���ܲ������ݿ���������';
			ELSE
			
				OPEN list3;-- �洢�˵������з�����α�
					read_loop: LOOP
				FETCH list3 INTO categoryparentID;
				IF done THEN
					LEAVE read_loop;
				END IF;
					SET retailAmount := 0;
					SET returnAmount := 0;
	   				
					OPEN list2; -- �洢�˵������н��й����۵ķ���ID,�����Ӧ�������ܶ�
						read_loop2: LOOP
					FETCH list2 INTO tempCategoryparentID, tempAmount;
					IF done THEN
				   		LEAVE read_loop2;
			   		END IF;
			   			IF tempCategoryparentID = categoryparentID THEN
			   				SET retailAmount := tempAmount;
			   				LEAVE read_loop2;
			   			END IF;
			   			
		   			END LOOP read_loop2;
	   				CLOSE list2;
	   				SET done = false;
	   				
	   				OPEN list1;-- �洢�˵������н��й��˻��ķ���ID,�����Ӧ���˻��ܶ�
	   					read_loop1: LOOP
	   				FETCH list1 INTO tempCategoryparentID, tempAmount;
	   				IF done THEN
	   					LEAVE read_loop1;
	   				END IF;
	   				
	   					IF tempCategoryparentID = categoryparentID THEN
	   						SET returnAmount := tempAmount;
	   						LEAVE read_loop1;
	   					END IF;
	   					
	   				END LOOP read_loop1;
	   				CLOSE list1;
	   				SET done := false;
							 	
					INSERT INTO t_retailtradedailyreportbycategoryparent (
						 F_ShopID,
						 F_Datetime, 
						 F_CategoryParentID, 
						 F_TotalAmount)
					VALUES (
						 iShopID,
					     DATE_FORMAT(dSaleDatetime,'%Y-%m-%d'), 
					     categoryparentID, 
					     retailAmount - returnAmount
					     );
					     
					END LOOP read_loop;
		   			CLOSE list3;	 
		   			 
					SELECT F_ID, 
						F_ShopID,
						F_Datetime, 
						F_CategoryParentID, 
						F_TotalAmount, 
						F_CreateDatetime, 
						F_UpdateDatetime
			        FROM t_retailtradedailyreportbycategoryparent
					WHERE  F_Datetime=DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
					 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				 
			END IF; 
		  	
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '�������۱���Ϊ0';
	  	END IF;
  	
  	COMMIT; 	
END;
		