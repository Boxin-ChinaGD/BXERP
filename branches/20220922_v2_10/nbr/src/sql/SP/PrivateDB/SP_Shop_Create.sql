DROP PROCEDURE IF EXISTS `SP_Shop_Create`;
CREATE DEFINER='root'@'localhost' PROCEDURE `SP_Shop_Create`(
	OUT iErrorCode INT,  
	OUT sErrorMsg VARCHAR(32),  
	IN sName VARCHAR(20),
	IN iCompanyID INT,
	IN sAddress VARCHAR(30),
	IN iDistrictID INT,
	IN sStatus INT,
	IN fLongitude Decimal(20,6),
	IN fLatitude Decimal(20,6),
	IN sKey VARCHAR(32),
	IN sRemark VARCHAR(30),
	IN iBxStaffID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3; 
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM nbr_bx.t_company WHERE F_ID = iCompanyID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���޸ù�˾';
		ELSEIF NOT EXISTS(SELECT 1 FROM nbr_bx.t_bxstaff WHERE F_ID = iBxStaffID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���޸�ҵ����';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shopdistrict WHERE F_ID = iDistrictID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���޸��ŵ�����';
		ELSEIF EXISTS(SELECT 1 FROM t_shop WHERE F_Name = sName AND F_CompanyID = iCompanyID)THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '���ŵ������Ѵ���';
		ELSE
	 		INSERT INTO t_shop (
	 			F_Name, 
	 			F_CompanyID,
	 			F_Address, 
	 			F_DistrictID,
	 			F_Status, 
	 			F_Longitude, 
	 			F_Latitude, 
	 			F_Key,
	 			F_Remark,
	 			F_BxStaffID
	 		) 
	 		VALUES 
	 		(
	 			sName, 		
	 			iCompanyID,
	 			sAddress, 
	 			iDistrictID,
	 			sStatus, 
	 			fLongitude, 
	 			fLatitude, 
	 			sKey,
	 			sRemark,
	 			iBxStaffID
	 		);
	 
	 		SELECT 
	 			F_ID, 
	 			F_Name, 
	 			F_CompanyID, 
	 			F_BXStaffID,  
	 			F_Address,
	 			F_DistrictID, 
	 			F_Status, 
	 			F_Longitude, 
	 			F_Latitude, 
	 			F_Key, 
	 			F_Remark, 
	 			F_CreateDatetime, 
	 			F_UpdateDatetime 
	 		FROM t_shop WHERE F_ID = LAST_INSERT_ID();
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END; 