-- 检查是否需要添加商品历史
drop function IF EXISTS Func_CreateCommodityHistory;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CreateCommodityHistory`(
	iCommodityID INT,
	sName VARCHAR(32),
	sNewBarcode VARCHAR(64),
	sSpecification VARCHAR (8),
	iPackageUnitID INT,
	iCategoryID INT,
	fPriceRetail Decimal(20,2),
	iNo INT,
	iStaffID INT,
	sOldBarcode VARCHAR(128),
	iShopID INT
) RETURNS INT(11)
BEGIN
	DECLARE newName VARCHAR(32);
	DECLARE newBarcode VARCHAR(64);
	DECLARE newSpecification VARCHAR (8);
	DECLARE newPackageUnitID INT;
	DECLARE newCategoryID INT;
	DECLARE newPriceRetail Decimal(20,2);
	DECLARE newNo INT;
	
	SELECT 
		F_Name, 
		F_Specification, 
		F_PackageUnitID, 
		F_CategoryID
--		F_PriceRetail, 
--		F_NO
	INTO 
		newName, 
		newSpecification,
		newPackageUnitID, 
		newCategoryID
--		newPriceRetail, 
--		newNo
	FROM t_commodity WHERE F_ID = iCommodityID;
	
	
--  	SELECT group_concat(F_Barcode) INTO newBarcode FROM t_barcodes WHERE F_CommodityID = iCommodityID;
--	SELECT IF(newBarcode IS NULL, '', newBarcode) INTO newBarcode; 
	
	IF sName != '$' THEN
	  	IF sName != newName THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '商品名称', sName, newName, iStaffID, 0, now(), 1);
 	  	END IF;
   	END IF;
   	
	IF sSpecification != '$' THEN 
		IF sSpecification != newSpecification THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '规格', sSpecification, newSpecification, iStaffID, 0, now(), 1);
 		END IF;
 	END IF;   
 		 	
	 IF iPackageUnitID != -1 THEN  
		IF iPackageUnitID <> newPackageUnitID OR newPackageUnitID IS NULL THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
	 		VALUES (iCommodityID, '包装单位', IF(iPackageUnitID = 0,'',(SELECT F_Name FROM t_packageunit WHERE F_ID = iPackageUnitID)), IF(newPackageUnitID IS NULL,'',(SELECT F_Name FROM t_packageunit WHERE F_ID = newPackageUnitID)), iStaffID, 0, now(), 1);
 		END IF;
	 END IF; 
	 	
 	IF iCategoryID != -1 THEN
		IF iCategoryID != newCategoryID THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '商品类别', (SELECT F_Name FROM t_category WHERE F_ID = iCategoryID), (SELECT F_Name FROM t_category WHERE F_ID = newCategoryID), iStaffID, 0, now(), 1);
 		END IF;
 	END IF; 
 	
 	IF fPriceRetail != -100000000 THEN 
 		SELECT F_PriceRetail INTO newPriceRetail FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		IF fPriceRetail != newPriceRetail THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '零售价', fPriceRetail, newPriceRetail, iStaffID, 0, now(), iShopID);
 		END IF;
 	END IF; 
 	
 	IF iNo != -100000000 THEN
 		SELECT F_NO INTO newNo FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		IF iNo != newNo THEN
			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '库存', iNo, newNo, iStaffID, 0, now(), iShopID);
 		END IF;
 	END IF; 
 	
 	IF sNewBarcode != '$' THEN 
-- 	   	IF !EXISTS(SELECT 1 FROM t_barcodes WHERE F_CommodityID = 2 AND F_Barcode = sNewBarcode) THEN
 			INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime, F_ShopID)
 			VALUES (iCommodityID, '条形码', sOldBarcode, sNewBarcode, iStaffID, 0, now(), 1);
--		END IF;
	END IF;

	
 	RETURN(0);
END;