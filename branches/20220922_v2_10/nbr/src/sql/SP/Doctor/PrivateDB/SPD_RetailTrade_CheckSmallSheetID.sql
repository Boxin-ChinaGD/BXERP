DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckSmallSheetID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckSmallSheetID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iSmallSheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_SmallSheetID AS iSmallSheetID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iSmallSheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е����۵���SmallSheetID������Ϊ�գ����������SmallSheet����
			-- ������������һ�ͨ������ô����Ϊ���ݲ�����
			IF iSmallSheetID IS NULL THEN -- SmallSheetIDΪ��
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���SmallSheetIDΪNULL');
			-- ��������СƱ��ʽ�����������Ƶģ�����һ��ʼ��10��СƱ��ʽ��
			-- �����û���ӵ�11��СƱ��ʽ��ʹ�õ�11��СƱ��ʽ�Ļ�ֻ��ɾ��һ��ʼ��1��10��СƱ��ʽ��
			-- ���T_RetailTrade��F_SmallSheetID�ֶ���T_SmallSheetFrame�����Լ���Ļ���
			-- �û���ɾ������СƱ��ʽ�ģ������û���Ӳ����µ�СƱ��ʽ��ʹ�ò����µ�СƱ��ʽ��
			-- ����iSmallSheetID����0����
			ELSEIF iSmallSheetID > 0 THEN -- �������
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE -- SmallSheetID������t_smallsheetframe����
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���SmallSheetID�������0');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;