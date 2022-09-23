-- --------------------------------------------------------------------------------------------------------- ״̬���� begin ----------------------------------------
CREATE TABLE T_SMDomain
	(
	F_ID INT NOT NULL AUTO_INCREMENT,
	F_DomainID INT NOT NULL,
	F_Name VARCHAR(16) NOT NULL, 
	F_Description VARCHAR(64) NOT NULL,
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_DomainID (F_DomainID),
	UNIQUE KEY F_Name (F_Name)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

CREATE TABLE T_SMNode
	(
	F_ID INT NOT NULL AUTO_INCREMENT,
	F_Status INT NOT NULL,
	F_DomainID INT NOT NULL,
	F_Name VARCHAR(16)  NOT NULL, 
	F_Description VARCHAR(64) NOT NULL,
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_DomainID) REFERENCES T_SMDomain(F_DomainID),
	UNIQUE KEY F_NodeTuple (F_Status, F_DomainID, F_Name)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';


CREATE TABLE T_SMForward
	(
	F_ID INT NOT NULL AUTO_INCREMENT,
	F_DomainID INT NOT NULL,
	F_CurrentNodeID INT NOT NULL, 
	F_NextNodeID INT NOT NULL,
	F_Description VARCHAR(64) NOT NULL,
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CurrentNodeID) REFERENCES T_SMNode(F_Status),
	FOREIGN KEY (F_NextNodeID) REFERENCES T_SMNode(F_Status),
	UNIQUE KEY F_ForwardTuple (F_DomainID, F_CurrentNodeID, F_NextNodeID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- --------------------------------------------------------------------------------------------------------- ״̬���� end ----------------------------------------

CREATE TABLE t_tmp
	(
	F_Log VARCHAR (255) NULL,
	F_Num1 INT NULL,
	F_Num2 INT NULL,
	F_Num3 INT NULL,
	F_Num4 INT NULL,
	F_Num5 INT NULL
	);
-- ��������

DROP TABLE IF EXISTS t_errorcode;

CREATE TABLE t_errorcode
	(
	F_ID               INT auto_increment,
	F_ErrorCode        INT NOT NULL,
	F_ErrorDescription VARCHAR (30) NOT NULL,
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_ErrorCode (F_ErrorCode)
	);
	
-- ��Ӧ��

DROP TABLE IF EXISTS T_ProviderDistrict;
CREATE TABLE T_ProviderDistrict					  -- ��Ӧ�������
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,		          -- ��������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Provider;
CREATE TABLE T_Provider							  -- ��Ӧ��
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (32) NOT NULL,			      -- ����
	F_DistrictID INT NULL,					 		  -- ����ID
	F_Address VARCHAR (50) NULL,			  			  -- ��ϵ��ַ
	F_ContactName VARCHAR (20) NULL,		  			  -- ��ϵ������
	F_Mobile VARCHAR (24) NULL,   			 		  -- ��ϵ�˵绰
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_DistrictID) REFERENCES T_ProviderDistrict(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- ��Ʒ

DROP TABLE IF EXISTS T_Brand;
CREATE TABLE T_Brand					 		  -- Ʒ�Ʊ�
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,	     	  -- Ʒ������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_CategoryParent; 	   	 	   -- ��Ʒ���Ĵ���
CREATE TABLE T_CategoryParent						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (10) NOT NULL,	  			   -- �������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Category; 	   	 		   -- ��Ʒ���
CREATE TABLE T_Category						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (10) NOT NULL,	  			   -- �������
	F_ParentID INT NOT NULL,					   -- �������ӦT_CategoryParent��F_ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ParentID) REFERENCES T_CategoryParent(F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_PackageUnit; 	   	 	   -- ��װ��λ
CREATE TABLE T_PackageUnit						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (8) NOT NULL,	  			   -- ��װ��λ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��

	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Department; 	   
CREATE TABLE T_Department		          		   -- �û�������ű�		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_DepartmentName VARCHAR(20) NOT NULL,  	   -- �û�������
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_ShopDistrict;
CREATE TABLE T_ShopDistrict					  -- �ŵ������
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,		          -- ��������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Shop; 	   
CREATE TABLE T_Shop		       					   -- �ŵ���Ϣ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(20) NOT NULL,				   -- �ŵ�����
	F_CompanyID	INT NOT NULL,				       -- ��꿿ͻ�ID 
	F_BXStaffID INT NOT NULL,					   -- ���ҵ��ԱID
	F_Address VARCHAR(30) NOT NULL,				   -- ��ַ
	F_DistrictID INT NULL,					 	   -- ����ID
	F_Status INT NOT NULL,				           -- ״̬
	F_Longitude Decimal(20,6) NOT NULL,	   		   -- ����
	F_Latitude Decimal(20,6) NOT NULL,			   -- γ��
	F_Key VARCHAR(32) NOT NULL,	  				   -- Կ��
	F_Remark VARCHAR(30) NULL,					   -- ��ע
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID), -- ,
	FOREIGN KEY (F_CompanyID) REFERENCES nbr_bx.T_Company(F_ID),
	FOREIGN KEY (F_BXStaffID) REFERENCES nbr_bx.T_BXStaff(F_ID),
	FOREIGN KEY (F_DistrictID) REFERENCES T_ShopDistrict(F_ID)
--	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POS; 	   
CREATE TABLE T_POS		      					   -- POS����Ϣ		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_POS_SN VARCHAR(32) NOT NULL,				   -- SN       ...
	F_ShopID INT NOT NULL,						   -- �ŵ�ID
	F_pwdEncrypted VARCHAR(0) NULL,				   -- ��Կ���ܺ���û�����
	F_Salt VARCHAR(32) NOT NULL,			   	   -- ���κ��MD5ֵ
	F_PasswordInPOS VARCHAR(16) NULL,	  		   -- ��POS���ϵ�����
	F_Status INT NOT NULL ,                        -- POS��״̬ 0������ 1������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID)
--	UNIQUE KEY F_POS_SN (F_POS_SN)  statusΪ1��F_POS_SN�������´���һ������ʹ�õ�pos��
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Staff; 	   
CREATE TABLE T_Staff		           			   -- �ŵ��û�		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(12) NOT NULL,				   -- ��Ա����
	F_Phone VARCHAR(32) NOT NULL,				   -- �ֻ�����
	F_ICID VARCHAR(20) NULL,				   -- ���֤����
	F_WeChat VARCHAR(20) NULL,				   -- ΢�ź�
	F_OpenID VARCHAR(100)  NULL,			   -- ΢���û���Ψһ��ʶ��
	F_Unionid VARCHAR(100) NULL,				   -- ֻ�н����ںŰ󶨵�΢�ſ���ƽ̨�ʺź󣬲Ż���ָ��ֶ�,ͬһ�û���unionid��Ψһ�ġ�
	F_pwdEncrypted VARCHAR(0) NULL,				   -- ��Կ���ܺ���û�����
	F_Salt VARCHAR(32) NOT NULL,			   	   -- ���κ��MD5ֵ
	F_PasswordExpireDate DATETIME NULL,	       -- ������Ч��
	F_IsFirstTimeLogin INT NOT NULL DEFAULT 1,     -- �״ε�¼�ɹ���
	F_ShopID INT NOT NULL,						   -- �ŵ�ID
	F_DepartmentID INT NOT NULL,				   -- �����Ĳ��š�Ĭ��Ϊ1
	F_Status INT NOT NULL,						   -- 0����ְ��1����ְ��
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID), 
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID), 
	FOREIGN KEY (F_DepartmentID) REFERENCES T_Department(F_ID)
--	FOREIGN KEY (F_IDInPOS) REFERENCES T_POS(F_ID) -- ,
--	UNIQUE KEY F_ICID (F_ICID),
--	UNIQUE KEY F_WeChat (F_WeChat)
--	UNIQUE KEY F_Phone (F_Phone)     statusΪ1��phone�������´���һ������ʹ�õ�staff��
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Warehouse; 	   
CREATE TABLE T_Warehouse		      	  		   -- �ֿ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
--	F_CompanyID INT NULL,				       -- ��꿿ͻ�ID
	F_Name VARCHAR(32) NOT NULL,				   -- ����	
	F_Address VARCHAR(32) NULL,			   	   -- ��ַ
	F_Status INT NOT NULL DEFAULT 0,	   		   -- ״̬ 0������ 1��ɾ��
	F_StaffID INT NULL,						   -- ��ϵ��
	F_Phone VARCHAR(32) NULL,				   -- �绰
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
--	FOREIGN KEY (F_CompanyID) REFERENCES T_BXStaff(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_PurchasingOrder; 	   
CREATE TABLE T_PurchasingOrder		               -- �ɹ�������		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ShopID INT NULL,							   -- �ŵ�ID
	F_SN VARCHAR(20) NOT NULL,					   -- ����
	F_Status INT NOT NULL DEFAULT 0,			   -- ״̬
	F_StaffID INT NOT NULL,  					   -- ������
	F_ProviderID INT NULL,					   -- ��Ӧ��ID
	F_ProviderName VARCHAR(32) NOT NULL,		   -- ��Ӧ������
	F_ApproverID INT NULL,					       -- �����
	F_Remark VARCHAR (128) NOT NULL,			   -- �ɹ��ܽ�
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),	 -- ��������ʱ��
	F_ApproveDatetime DATETIME NULL,		  	   -- �������ʱ��
	F_EndDatetime DATETIME NULL,			       -- ��ֹ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ProviderID) REFERENCES T_Provider(F_ID),
	FOREIGN KEY (F_ApproverID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Warehousing; 	   	  
CREATE TABLE T_Warehousing					  		 -- ��ⵥ��
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ShopID INT NULL,							   -- �ŵ�ID
	F_Status INT NOT NULL,						   -- 0:δ���,1,���
	F_SN VARCHAR(20) NOT NULL,					   -- ����
	F_ProviderID INT NOT NULL,					   -- ��Ӧ��ID
	F_WarehouseID INT NOT NULL ,		  		   -- �ջ��ֿ�	 					   
	F_StaffID INT NOT NULL ,			 		   -- ҵ��Ա 
	F_ApproverID INT NULL,					       -- �����
	F_CreateDatetime DATETIME NOT NULL,			   -- �������
	F_PurchasingOrderID	INT NULL ,  		   -- ��Ӧ�Ĳɹ�����ID		
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ProviderID) REFERENCES T_Provider(F_ID),
	FOREIGN KEY (F_WarehouseID) REFERENCES T_Warehouse(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ApproverID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_PurchasingOrderID) REFERENCES T_PurchasingOrder(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Commodity;
CREATE TABLE T_Commodity						  -- ��Ʒ��Ϣ
	(
	F_ID   INT NOT NULL AUTO_INCREMENT,    		  -- ��ƷID
	F_Status INT NOT NULL DEFAULT 0, 			  -- ��Ʒ״̬
	F_Name VARCHAR (32) NOT NULL,		          -- ��Ʒ����
	F_ShortName VARCHAR (32),			   		  -- ��Ʒ���
	F_Specification VARCHAR (8) NOT NULL, 		  -- ���
	F_PackageUnitID INT,    	 		  		  -- ��װ��λID
	F_PurchasingUnit VARCHAR (16) NULL, 		  -- �ɹ���λ
	F_BrandID INT,     		 					  -- ��ƷƷ��ID
	F_CategoryID INT,		   	 			      -- ���ID
	F_MnemonicCode VARCHAR (32) NOT NULL,  		  -- ������
	F_PricingType INT NOT NULL,   				  -- �Ƽ۷�ʽ	
	-- F_IsServiceType INT NOT NULL,  		 		  -- �Ƿ����������Ʒ(�ѷ���)
	-- F_PricePurchase Decimal(20,6) NOT NULL,		  -- ƽ��������(�ѷ���)
--	F_LatestPricePurchase Decimal(20,6) NOT NULL, -- ���½�����
--	F_PriceRetail Decimal(20,6) NOT NULL,		  -- ���ۼ�
	F_PriceVIP Decimal(20,6) NOT NULL,			  -- ��Ա��
	F_PriceWholesale Decimal(20,6) NOT NULL,  	  -- ������	
	-- F_RatioGrossMargin Decimal(20,6) NOT NULL,	  -- ë����(�ѷ���)
	F_CanChangePrice INT NOT NULL, 		          -- ǰ̨�Ƿ��ܸļ�
	F_RuleOfPoint INT NULL,   		  		 	  -- ���ֹ��� --
	F_Picture VARCHAR (128) NULL,	   		 	  -- ��ƷͼƬ
	F_ShelfLife INT NULL,     		 	   		  -- ������ --
	F_ReturnDays INT NOT NULL,	   		  		  -- �˻�����
	F_CreateDate DATETIME NOT NULL,		   		  -- ��������
	F_PurchaseFlag INT NULL,		        	  -- �ɹ���ֵ --
	F_RefCommodityID INT NOT NULL DEFAULT 0, 	  -- ������ƷID
	F_RefCommodityMultiple INT NOT NULL DEFAULT 0, -- ������Ʒ����
	-- F_IsGift INT NOT NULL,	 					  -- �Ƿ�������Ʒ(�ѷ���)
	F_Tag VARCHAR (32) NOT NULL,				  -- TAG
--	F_NO INT NOT NULL,							  -- ϵͳ�����������̵��ʵ��������ͬ
	-- F_NOAccumulated INT NOT NULL,				  -- ÿ����Ʒ��ⶼ�Ὣ��Ʒ�����ۼӵ�����ֶΡ�(�ѷ���)
	F_Type INT NOT NULL,						  -- ��Ʒ����
--	F_NOStart INT NOT NULL DEFAULT -1,			  -- �ڳ�����
--	F_PurchasingPriceStart Decimal(20,6) NOT NULL DEFAULT -1, 		  -- �ڳ��ɹ���
	F_StartValueRemark Varchar(50) NULL, 		  -- �ڳ�ֵ��ע
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- �޸�ʱ��
	F_PropertyValue1 VARCHAR(50) NULL,            -- �Զ�������1
	F_PropertyValue2 VARCHAR(50) NULL,            -- �Զ�������2
	F_PropertyValue3 VARCHAR(50) NULL,            -- �Զ�������3
	F_PropertyValue4 VARCHAR(50) NULL,            -- �Զ�������4
--	F_CurrentWarehousingID INT NULL,			  -- ��ֵ���ID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryID) REFERENCES T_Category(F_ID),
	FOREIGN KEY (F_BrandID) REFERENCES T_Brand(F_ID),
--	FOREIGN KEY (F_CurrentWarehousingID) REFERENCES T_Warehousing(F_ID),
	FOREIGN KEY (F_PackageUnitID) REFERENCES T_PackageUnit(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_CommodityShopInfo;
CREATE TABLE T_CommodityShopInfo
	(
		F_ID INT NOT NULL AUTO_INCREMENT,
		F_CommodityID INT NOT NULL,
		F_ShopID INT NOT NULL,
		F_LatestPricePurchase Decimal(20,6) NOT NULL, -- ���½�����
		F_PriceRetail Decimal(20,6) NOT NULL,		  -- ���ۼ�
		F_NO INT NOT NULL,
		F_NOStart INT NOT NULL DEFAULT -1,			  -- �ڳ�����
		F_PurchasingPriceStart Decimal(20,6) NOT NULL DEFAULT -1, 		  -- �ڳ��ɹ���
		F_CurrentWarehousingID INT NULL,			  -- ��ֵ���ID
		PRIMARY KEY (F_ID),
		FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID),
		FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
		FOREIGN KEY (F_CurrentWarehousingID) REFERENCES T_Warehousing(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_ProviderCommodity; 	 	   		  
CREATE TABLE T_ProviderCommodity						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			    -- ID
	F_CommodityID INT NOT NULL,		  			    -- ��ƷID
	F_ProviderID INT NOT NULL,			  			-- ��Ӧ��ID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_ProviderID ) REFERENCES T_Provider(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_SubCommodity; 	 	   		  
CREATE TABLE T_SubCommodity						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			    -- ID
	F_CommodityID INT NOT NULL,		  			    -- ��ƷID
	F_SubCommodityID INT NOT NULL,			  		-- �����ƷID
	F_SubCommodityNO INT NOT NULL,					-- ����Ʒ������
	F_Price DECIMAL(20, 6) NOT NULL,				-- ����Ʒ�ĵ���
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_SubCommodityID ) REFERENCES T_Commodity(F_ID),
	UNIQUE KEY F_CombinationCommodity(F_CommodityID, F_SubCommodityID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Barcodes; 	 	   		   -- ��ƷһƷ����
CREATE TABLE T_Barcodes						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CommodityID INT NOT NULL,		  			   -- ��ƷID��֧��һƷ����
	F_Barcode VARCHAR (64) NOT NULL,			   -- ���롣֧��һ���Ʒ
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),    -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_CommodityBarcode(F_CommodityID, F_Barcode),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommodityProperty;		   -- ��Ʒ���Ա�
CREATE TABLE T_CommodityProperty
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ��Ʒ����ID
	F_Name1 VARCHAR(16) NULL,			 		   -- ��Ʒ������1
	F_Name2 VARCHAR(16) NULL,			 		   -- ��Ʒ������2
	F_Name3 VARCHAR(16) NULL,			 		   -- ��Ʒ������3
	F_Name4 VARCHAR(16) NULL,			 		   -- ��Ʒ������4
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Permission; 	   
CREATE TABLE T_Permission		          		   -- ����Ȩ�ޱ�		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SP VARCHAR(80) NOT NULL,					   -- ��Ӧ������SP
	F_Name VARCHAR(20) NOT NULL,  	   			   -- ��������
	F_Domain VARCHAR(16) NOT NULL ,				       -- ��������
	F_Remark VARCHAR(32) NOT NULL,				   -- ������ע
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Role; 	   
CREATE TABLE T_Role		          		  		   -- ��ɫ		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(20) NOT NULL,			   -- ��ɫ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Role_Permission; 	   
CREATE TABLE T_Role_Permission		          	   -- ��ɫ�����м��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_RoleID INT NOT NULL,			  			   -- ��ɫID
	F_PermissionID INT NOT NULL,				   -- ����ID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RoleID ) REFERENCES T_Role(F_ID),
	FOREIGN KEY (F_PermissionID ) REFERENCES T_Permission(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_StaffRole; 	   
CREATE TABLE T_StaffRole		          		   -- �û���ɫ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_StaffID INT NOT NULL,						   -- �û�ID
	F_RoleID INT NOT NULL,						   -- ��ɫID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_RoleID ) REFERENCES T_Role(F_ID),
	UNIQUE KEY F_StaffID(F_StaffID)	 -- ... һ��Ա������ֻ��һ����ɫ
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_WarehousingCommodity; 	   
CREATE TABLE T_WarehousingCommodity				   	-- ��ⵥ��Ʒ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   	-- ID
	F_WarehousingID INT NOT NULL,			  	   	-- ��ⵥID
	F_CommodityID INT NOT NULL,					  	-- ��ƷID	
	F_NO INT NOT NULL,							   	-- �ջ�����
	F_PackageUnitID INT NOT NULL,					-- ��װ��λID
	F_CommodityName VARCHAR(32) NOT NULL,			-- ��Ʒ����
	F_BarcodeID INT NOT NULL,						-- ������ID
	F_Price Decimal(20,6) NOT NULL,					-- ����
	F_Amount Decimal(20,6) NOT NULL,				-- ���
	F_ProductionDatetime DATETIME NOT NULL,		   	-- ��������
	F_ShelfLife INT NOT NULL,			          	-- ������
	F_ExpireDatetime DATETIME NOT NULL,			   	-- ʧЧ��
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	F_NOSalable INT NOT NULL, 						-- ��������
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES T_Warehousing(F_ID),
	FOREIGN KEY (F_PackageUnitID) REFERENCES T_PackageUnit(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID) REFERENCES T_Barcodes(F_ID),
	UNIQUE KEY F_WarehousingCommodityID (F_CommodityID, F_WarehousingID)
	
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- �ɹ�����

DROP TABLE IF EXISTS T_PurchasingOrderCommodity; 	   
CREATE TABLE T_PurchasingOrderCommodity		       -- �ɹ�������Ʒ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_PurchasingOrderID INT NOT NULL,			   -- ��Ʒ������ID
	F_CommodityID INT NOT NULL,					   -- ��ƷID
	F_CommodityNO INT NOT NULL DEFAULT 1,		   -- ��Ʒ����
	F_CommodityName VARCHAR(32) NOT NULL,		   -- ��Ʒ����
	F_BarcodeID INT NOT NULL,					   -- ������ID
	F_PackageUnitID INT NOT NULL,				   -- ��װ��λID
	F_PriceSuggestion Decimal(20,6) NOT NULL,	   -- ����ɹ�����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PurchasingOrderID ) REFERENCES T_PurchasingOrder(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES t_barcodes(F_ID),
	FOREIGN KEY (F_PackageUnitID ) REFERENCES t_packageunit(F_ID),
	UNIQUE KEY UniquePurchasingOrderCommodity (F_PurchasingOrderID, F_CommodityID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- �ֹ��˻���

DROP TABLE IF EXISTS T_ReturnCommoditySheet; 	   
CREATE TABLE T_ReturnCommoditySheet		           -- �ֹ��˻���
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(20) NOT NULL,					   -- ����
	F_StaffID INT NOT NULL,						   -- ������ID
	F_ProviderID INT NOT NULL,					   -- ��Ӧ��ID
	F_Status INT NOT NULL DEFAULT 0,			   -- ���״̬ 0=δ��� 1=���
	F_ShopID INT NOT NULL,
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),		-- �˻�����
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),		-- �޸�����
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ProviderID) REFERENCES T_Provider(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- �ֹ��˻�����Ʒ��

DROP TABLE IF EXISTS T_ReturnCommoditySheetCommodity; 	   
CREATE TABLE T_ReturnCommoditySheetCommodity		           -- �ֹ��˻�����Ʒ��
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ReturnCommoditySheetID INT NOT NULL,		   -- �˻���ID
	F_BarcodeID INT NOT NULL,					   -- ������ID
	F_CommodityID INT NOT NULL,					   -- ��ƷID
	F_CommodityName VARCHAR(32) NOT NULL,		   -- ��Ʒ����
	F_NO INT NOT NULL,							   -- ����
	F_Specification VARCHAR (8) NOT NULL,	 	   -- ���
	F_PurchasingPrice Decimal(20,6) NOT NULL,	   -- �ɹ���
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ReturnCommoditySheetID) REFERENCES T_ReturnCommoditySheet(F_ID),
	FOREIGN KEY (F_BarcodeID) REFERENCES T_Barcodes(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_Commodity(F_ID),
	UNIQUE KEY UniqueReturnCommoditySheetCommodity (F_ReturnCommoditySheetID, F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- �̵㵥

DROP TABLE IF EXISTS T_InventorySheet; 	   
CREATE TABLE T_InventorySheet		               -- �̵㵥	
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(20) NOT NULL,					   -- ����
	F_ShopID INT NOT NULL,		  	           	   -- �ŵ�ID
	F_WarehouseID INT NOT NULL,		  	           -- �̵�ֿ�
	F_Scope INT NOT NULL,						   -- �̵㷶Χ
	F_Status INT NOT NULL,				  		   -- ״̬  
	F_StaffID INT NOT NULL,  			           -- ������
	F_ApproverID INT NULL,					       -- �����
	F_CreateDatetime DATETIME NOT NULL,			   -- ��������ʱ��
	F_ApproveDatetime DATETIME NULL,			   -- �������ʱ��
	F_Remark VARCHAR (128) NOT NULL,			   -- �̵��ܽ�
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_WarehouseID) REFERENCES T_Warehouse(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID),
	FOREIGN KEY (F_ApproverID) REFERENCES T_Staff(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_InventoryCommodity; 	   
CREATE TABLE T_InventoryCommodity		           -- �̵���Ʒ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_InventorySheetID INT NOT NULL,			   -- �̵㵥��
	F_CommodityID INT NOT NULL,					   -- ��ƷID
	F_CommodityName VARCHAR (32) NOT NULL,		   -- ��ƷID
	F_Specification VARCHAR (8) NOT NULL,		   -- ��Ʒ���
	F_BarcodeID INT NOT NULL,					   -- ������ID
	F_PackageUnitID INT NOT NULL,				   -- ��װ��λID
	F_NOReal INT NULL,						  	   -- ʵ����Ʒ����
	F_NOSystem INT NULL,					       -- ϵͳ�������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_InventorySheetID ) REFERENCES T_InventorySheet(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES t_barcodes(F_ID),
	FOREIGN KEY (F_PackageUnitID ) REFERENCES t_packageunit(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- ��Ա

DROP TABLE IF EXISTS T_VIP_Category; 	   
CREATE TABLE T_VIP_Category		      			   -- ��Ա�����		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(30) NOT NULL,				   -- �������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VipCard; 	   
CREATE TABLE T_VipCard		      			 	   -- һ�ֻ�Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Title VARCHAR(9) NOT NULL,				   -- ����
	F_BackgroundColor VARCHAR(23) NOT NULL ,  	   -- ��Ա������ͼ
	F_ClearBonusDay INT NULL,  					   -- ������������
	F_ClearBonusDatetime DATETIME NULL,  		   -- ������������
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),  -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- ��Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- ��Ա���
	F_CardID INT NOT NULL,						   -- ��� ����T_VipCard��ID
	F_Mobile VARCHAR(11) NULL,					   -- �ֻ�����
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS����SN��
	F_Sex INT NOT NULL DEFAULT 1,				   -- �Ա�
	F_Logo VARCHAR(128) NULL,					   -- ͷ��?ͼƬ?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(32) NOT NULL,				   -- ����
	F_Email VARCHAR(30) NULL,	            	   -- ����
	F_ConsumeTimes INT NOT NULL,				   -- �����Ѵ���
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- �����ѽ��
	F_District VARCHAR(30) NOT NULL,			   -- ����
	F_Category INT NOT NULL,					   -- ���
	F_Birthday DATETIME NULL,			 		   -- ����
	F_Bonus INT NOT NULL,			   			   -- ��ǰ����
	F_LastConsumeDatetime DATETIME NULL,		   -- �ϴ���������ʱ��
	F_Remark VARCHAR(50) NULL,			  		   -- ��ע		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_Category) REFERENCES T_VIP_Category(F_ID),
	FOREIGN KEY (F_CardID) REFERENCES T_VipCard(F_ID),
	UNIQUE KEY F_Mobile (F_Mobile),
	UNIQUE KEY F_ICID (F_ICID),
	UNIQUE KEY F_Email (F_Email),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VipCardCode; 	   
CREATE TABLE T_VipCardCode		      			   -- һ�Ż�Ա��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_VipCardID INT NOT NULL ,  		   		   -- ��Ա��ID
	F_SN VARCHAR(16) NOT NULL,  	 	  		   -- ��Ա����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),  -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	FOREIGN KEY (F_VipCardID) REFERENCES t_vipcard(F_ID),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- ��Ա��Դ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- ��ԱID
	F_SourceCode INT NOT NULL ,  		   		   -- �������� 0=WX, 1=ALIPAY, 2=MEITUAN
	F_ID1 VARCHAR(50) NOT NULL,  	 	  		   -- openID
	F_ID2 VARCHAR(50) NOT NULL,  	 	  		   -- unionID
	F_ID3 VARCHAR(50) NOT NULL,  	 	  		   -- MiniProgramOpenID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	UNIQUE KEY F_VipSource (F_VipID, F_SourceCode)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTrade;	   
CREATE TABLE T_RetailTrade		                   -- ���۵�		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NULL,							   -- ��ԱID
	F_SN VARCHAR(26) NOT NULL,					   -- ������ˮ��
	F_LocalSN INT NOT NULL,				           -- ���������ţ�����̨�������ϵ�Ωһ����
	F_POS_ID INT NOT NULL,						   -- ������ID
	F_Logo VARCHAR(128) NOT NULL,				   -- �Զ���logo
	F_SaleDatetime DATETIME NOT NULL,			   -- ����ʱ��
	F_StaffID INT NOT NULL,						   -- ����ԱID
	F_PaymentType INT NOT NULL,					   -- ֧����ʽ
	F_PaymentAccount VARCHAR(20) NOT NULL,		   -- �ʺ�
	F_Status INT NOT NULL,						   -- ״̬   0����/1����/2ɾ��
	F_Remark VARCHAR(20) NOT NULL,				   -- ��ע
	F_SourceID INT DEFAULT -1,		   			   -- Դ����ID
	F_SyncDatetime DATETIME NOT NULL,        	  	   -- ͬ��ʱ��
	F_Amount Decimal(20,6) NOT NULL,               -- ���۵��ܽ��
	F_AmountPaidIn Decimal(20,6) NOT NULL,         -- ���۵�ʵ�ս��
	F_AmountChange Decimal(20,6) NOT NULL,         -- ���۵�������
	F_AmountCash Decimal(20,6) NULL, 			   -- �ֽ�֧����Ŀ
	F_AmountAlipay Decimal(20,6) NULL,			   -- ֧����֧����Ŀ
	F_AmountWeChat Decimal(20,6) NULL,			   -- ΢��֧����Ŀ
	F_Amount1 Decimal(20,6) NULL,				   -- ����֧������Ŀ1
	F_Amount2 Decimal(20,6) NULL,				   -- ����֧������Ŀ2
	F_Amount3 Decimal(20,6) NULL,				   -- ����֧������Ŀ3
	F_Amount4 Decimal(20,6) NULL,				   -- ����֧������Ŀ4
	F_Amount5 Decimal(20,6) NULL,				   -- ����֧������Ŀ5
	F_SmallSheetID INT NOT NULL,			   	   -- СƱID
	F_AliPayOrderSN VARCHAR(32) NULL,			   -- ֧����������
	F_WxOrderSN VARCHAR(32) NULL,				   -- ΢�Ŷ�����
	F_WxTradeNO VARCHAR(32) NULL,				   -- ΢�Ž��׵���
	F_WxRefundNO VARCHAR(32) NULL,				   -- ΢���˿��
	F_WxRefundDesc VARCHAR(80) NULL,			   -- ΢���˿�����
	F_WxRefundSubMchID VARCHAR(32) NULL,           -- ΢���˿����̻���
	F_CouponAmount Decimal(20,6) NOT NULL DEFAULT '0.000000d',   -- �Ż�ȯ�ֿ۽��
	F_ConsumerOpenID VARCHAR(100) NULL,            -- ��ʶ˭��WX֧���˱����۵�
	F_ShopID INT NOT NULL,            -- �ŵ��ID -- TODO not null
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY RetailTradeIDInPOS (F_LocalSN, F_POS_ID, F_SaleDatetime), -- Ωһ��ʶһ�����۵�
	FOREIGN KEY (F_POS_ID) REFERENCES T_POS(F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_Vip(F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID) -- TODO inserttable�Ƚ϶��������ݣ���ע��
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
	
DROP TABLE IF EXISTS T_RetailTradeCommodity; 	   
CREATE TABLE T_RetailTradeCommodity		           -- ���۵���Ʒ��
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_TradeID INT NOT NULL, 					   -- ����ID
	F_CommodityID INT NULL ,		 		 	   -- ��ƷID
	F_CommodityName VARCHAR(32) NOT NULL,		   -- ��Ʒ����
	F_BarcodeID INT NULL,						   -- ������ID
	F_NO INT NOT NULL,			   				   -- ����
	F_PriceOriginal Decimal(20,6) NULL,			   -- ԭ��
    -- F_Discount Decimal(20,6) NOT NULL,			   -- �ۿ�(�ѷ���)
	-- F_IsGift INT NOT NULL DEFAULT 0,			   -- �Ƿ�����(�ѷ���)
	F_NOCanReturn INT NOT NULL,   				   -- ���˻�����
	F_PriceReturn Decimal(20,6) NULL,			   -- �˻���
	F_PriceSpecialOffer Decimal(20,6) NULL,		   -- �ؼ�
	F_PriceVIPOriginal Decimal(20,6) NULL,		   -- ��Ʒԭ��Ա��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_TradeID ) REFERENCES T_RetailTrade(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES T_Barcodes(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	UNIQUE KEY UniqueCommodity (F_TradeID, F_CommodityID) -- Ωһ��ʶһ�����۵���Ʒ��
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeCommoditySource; 	   
CREATE TABLE T_RetailTradeCommoditySource		        -- ���۵���Ʒ��Դ��
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  		-- ID
	F_RetailTradeCommodityID INT NOT NULL, 				-- ���۵���ƷID
	F_ReducingCommodityID INT NOT NULL,					-- ʵ�ʱ����ٿ�����ƷID���������������Ʒ�е�����Ʒ��Ҳ�����Ƕ��װ��Ʒ��Ӧ�ĵ�Ʒ
	F_NO INT NOT NULL ,		 		 	   					-- ���۵���Ʒ����
	F_WarehousingID INT NULL,							-- ���ID(PET-1281�޸�Ϊ��null)
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeCommodityID) REFERENCES T_RetailTradeCommodity(F_ID),
	FOREIGN KEY (F_ReducingCommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES t_warehousing(F_ID),
	UNIQUE KEY UniqueWarehousing (F_RetailTradeCommodityID, F_WarehousingID, F_ReducingCommodityID) -- Ωһ��ʶN��ͬ������Ʒ����Դ����⣩
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_ReturnRetailTradeCommodityDestination; 	   
CREATE TABLE T_ReturnRetailTradeCommodityDestination		-- ���۵���Ʒ�˻�ȥ���
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  			-- ID
	F_RetailTradeCommodityID INT NOT NULL, 					-- ���۵���ƷID���˻����ӱ�ID��
	F_IncreasingCommodityID INT NOT NULL,					-- ʵ�ʱ����ӿ�����ƷID���������������Ʒ�е�����Ʒ��Ҳ�����Ƕ��װ��Ʒ��Ӧ�ĵ�Ʒ��Ҳ�����Ƿ�������Ʒ
	F_NO INT NOT NULL,		 		 	   					-- �������<=��Ӧ�����۵���Ʒ���и���Ʒ����������������F_NO����Ʒ�������Ĵ���⣬����֪�����Ľ�����-��Ϊ������б�����F_NO����Ʒ�Ľ������Ƕ���
	F_WarehousingID INT NULL,								-- ���ID�� ����ƷΪ�ڳ���Ʒʱ���Ҳ�����ⵥ����ʱ����Ϊ0.000000d
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeCommodityID) REFERENCES T_RetailTradeCommodity(F_ID),
	FOREIGN KEY (F_IncreasingCommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES t_warehousing(F_ID),
	UNIQUE KEY UniqueReturnRetailTradeCommodityDestination (F_RetailTradeCommodityID, F_WarehousingID, F_IncreasingCommodityID)  -- Ωһ��ʶN��ͬ������Ʒ����Դ����⣩
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VIPPointHistory; 	   
CREATE TABLE T_VIPPointHistory		      		   -- ��Ա������ʷ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VIP_ID INT NOT NULL,						   -- ��ԱID
	F_PointChanged INT NOT NULL,		   		   -- ��������ֵ
	F_RetailTradeID INT NOT NULL,	   	   		   -- ��Ӧ�����۵�ID
	F_CreateDatetime DATETIME NOT NULL,			   -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VIP_ID) REFERENCES T_VIP(F_ID),
	FOREIGN KEY (F_RetailTradeID) REFERENCES t_RetailTrade(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeAggregation; 	   
CREATE TABLE T_RetailTradeAggregation		       -- ����������������		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_StaffID INT NOT NULL,					       -- �ŵ���ԱID
	F_PosID INT NOT NULL,                          -- POS��ID
	F_WorkTimeStart DATETIME NOT NULL,			   -- �ϰ�ʱ��
	F_WorkTimeEnd DATETIME NULL,				   -- �°�ʱ��
	F_TradeNO INT NOT NULL,					       -- ���׵���
	F_Amount Decimal(20,6) NOT NULL,			   	   -- Ӫҵ��
	F_ReserveAmount Decimal(20,6) NOT NULL,	       -- ׼����
	F_CashAmount Decimal(20,6) NULL,		   	   -- �ֽ�����
	F_WechatAmount Decimal(20,6) NULL,		       -- ΢������
	F_AlipayAmount Decimal(20,6) NULL,		   	   -- ֧��������
	F_Amount1 Decimal(20,6) NULL,			  	   -- ����֧������Ŀ1
	F_Amount2 Decimal(20,6) NULL,			  	   -- ����֧������Ŀ2
	F_Amount3 Decimal(20,6) NULL,			       -- ����֧������Ŀ3
	F_Amount4 Decimal(20,6) NULL,			  	   -- ����֧������Ŀ4
	F_Amount5 Decimal(20,6) NULL,			       -- ����֧������Ŀ5 
	F_UploadDateTime DATETIME NOT NULL,			   -- �ϴ�����������ʱ��

	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_PosID) REFERENCES t_pos(F_ID),
	UNIQUE KEY StaffWorkTiome (F_StaffID, F_PosID, F_WorkTimeStart)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- ��Ϣ

DROP TABLE IF EXISTS T_MessageCategory; 	   
CREATE TABLE T_MessageCategory		          	       -- ��Ϣ����		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(32) NOT NULL,				   -- �������

	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Message; 	   
CREATE TABLE T_Message		          	           -- ��Ϣ��		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CategoryID INT NOT NULL,				 	   -- ���
	F_CompanyID INT NOT NULL,                      -- ��ʶ�����Ϣ�����ļҹ�˾
	F_IsRead INT NOT NULL DEFAULT 0,	 		   -- �Ѷ�
	F_Status INT NOT NULL DEFAULT 0,               -- 0δ����, 1�ѷ���,���͵����ںŸ��ϰ�������
	F_Parameter VARCHAR(255) NOT NULL,			   -- ����
	F_CreateDatetime DATETIME NOT NULL,				   -- ��������
	F_SenderID INT NOT NULL DEFAULT 0,  		   -- �����û�ID
	F_ReceiverID INT NOT NULL, 					   -- �����û�ID
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryID) REFERENCES T_MessageCategory(F_ID),
	FOREIGN KEY (F_CompanyID) REFERENCES nbr_bx.T_Company(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_MessageHandlerSetting; 	   
CREATE TABLE T_MessageHandlerSetting		       -- ��Ϣ�������ñ�		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CategoryID INT NOT NULL,		   			   -- ���ID
	F_Template VARCHAR(128) NOT NULL,	 		   -- ���ģ��
	F_Link VARCHAR(255) NOT NULL,				   -- ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryID) REFERENCES T_MessageCategory(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Promotion; 					-- ������
CREATE TABLE T_Promotion
	(	
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SN VARCHAR(20) NOT NULL,					    -- ����
    F_Name VARCHAR(32) NOT NULL,                    -- �����                      
    F_Status INT,                                   -- ״̬ 0=��Ч��1=�Ѿ�ɾ������Ч��
    F_Type INT,                                     -- ����� 0=������1=����
    F_DatetimeStart DATETIME NOT NULL,              -- ��ʼ����
    F_DatetimeEnd DATETIME NOT NULL,                -- ��������
    F_ExcecutionThreshold DECIMAL(20, 6) NOT NULL,  -- ������ֵ
    F_ExcecutionAmount DECIMAL(20, 6) NULL,         -- ������� F_TypeΪ0ʱ��Ч
    F_ExcecutionDiscount DECIMAL(20, 6) NULL,       -- �����ۿ� F_TypeΪ1ʱ��Ч 0<F_ExcecutionDiscount<10 ��һλС��
    F_Scope INT NOT NULL,                           -- �������Ʒ�ķ�Χ 0=ȫ����Ʒ��1=������Ʒ����Щ��Ʒ��¼��T_PromotionScope�У� 
    F_ShopScope INT NOT NULL,                       -- ������ŵ�ķ�Χ 0=ȫ���ŵ꣬1=�����ŵ꣨��Щ�ŵ��¼��T_PromotionShopScope�У� 
    F_Staff INT NOT NULL,                           -- ������ ���
    F_CreateDatetime DATETIME,                      -- ����ʱ��
    F_UpdateDatetime DATETIME,                      -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_Staff ) REFERENCES T_Staff(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionScope; 					-- ������Χ��
CREATE TABLE T_PromotionScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_PromotionID INT,                              -- ����ID
	F_CommodityID INT DEFAULT NULL,                 -- ��ƷID
	F_CommodityName VARCHAR (32) NOT NULL,		    -- ��Ʒ����
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PromotionID ) REFERENCES T_Promotion(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionShopScope; 					-- �����ŵ귶Χ��
CREATE TABLE T_PromotionShopScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_PromotionID INT,                              -- ����ID
	F_ShopID INT NOT NULL,                 			-- �ŵ�ID
	F_ShopName VARCHAR (32) NOT NULL,		 	    -- �ŵ�����
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PromotionID ) REFERENCES T_Promotion(F_ID),
	FOREIGN KEY (F_ShopID ) REFERENCES t_shop(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionSyncCache; 	 		 -- ����ͬ�������
CREATE TABLE T_PromotionSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			     -- ID
	F_SyncData_ID INT NULL,						     -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 		     -- ��������
	F_SyncSequence	INT NOT NULL,					 -- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),-- ����ʱ��
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionSyncCacheDispatcher; -- ����ͬ��������ȱ�
CREATE TABLE T_PromotionSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				 -- ID
	F_SyncCacheID INT NOT NULL,						 -- ͬ������ID
	F_POS_ID INT NOT NULL,					  		 -- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),-- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_PromotionSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradePromoting; 		-- ���۵�������
CREATE TABLE T_RetailTradePromoting
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_TradeID INT NOT NULL,							-- ���۵�ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY (F_TradeID),
	FOREIGN KEY (F_TradeID ) REFERENCES t_retailtrade(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradePromotingFlow; 		-- ���۵�������ӱ�
CREATE TABLE T_RetailTradePromotingFlow
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			
	F_RetailTradePromotingID INT NOT NULL,
	F_PromotionID INT NULL,
	F_ProcessFlow VARCHAR(2048) NOT NULL,			-- �������������������п��ܲ�����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),	
	FOREIGN KEY (F_RetailTradePromotingID) REFERENCES T_RetailTradePromoting(F_ID),
	FOREIGN KEY (F_PromotionID) REFERENCES t_promotion(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradePromotingSyncCache; 	 			-- ���۴�����ͬ�������
CREATE TABLE T_RetailTradePromotingSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	-- FOREIGN KEY (F_SyncData_ID ) REFERENCES t_RetailTradePromoting(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradePromotingSyncCacheDispatcher; 		-- ���۴�����ͬ��������ȱ�
CREATE TABLE T_RetailTradePromotingSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_RetailTradePromotingSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommoditySyncCache; 	 			-- ��Ʒͬ�������
CREATE TABLE T_CommoditySyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncData_ID ) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommoditySyncCacheDispatcher; 		-- ��Ʒͬ��������ȱ�
CREATE TABLE T_CommoditySyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_CommoditySyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BrandSyncCache; 	 			-- Ʒ��ͬ�������
CREATE TABLE T_BrandSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	-- FOREIGN KEY (F_SyncData_ID ) REFERENCES t_brand(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BrandSyncCacheDispatcher; 		-- Ʒ��ͬ��������ȱ�
CREATE TABLE T_BrandSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_BrandSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_ConfigGeneral; 	   			-- ��ͨ���ñ�
CREATE TABLE T_ConfigGeneral
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(60) NOT NULL,					-- ���� -- �����մ��౨�����ֳ���Ϊ55 ���ֶγ��ȸ���Ϊ60
	F_Value VARCHAR(128) NOT NULL,					-- ֵ
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_ConfigCacheSize; 	   		-- �������������ñ�
CREATE TABLE T_ConfigCacheSize
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(50) NOT NULL,					-- ����
	F_Value VARCHAR(20) NOT NULL,					-- ֵ
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY (F_Name)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_SmallSheetFrame; 	   		-- СƱ��ӡ
CREATE TABLE T_SmallSheetFrame
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Logo MEDIUMTEXT NULL,			   					-- �Զ���Logo
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	F_CountOfBlankLineAtBottom INT NOT NULL,			 -- �ײ�������
	F_DelimiterToRepeat varchar(1) not null default '-',  -- �ָ���
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_SmallSheetText; 	   		-- СƱ���ݸ�ʽ
CREATE TABLE T_SmallSheetText
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Content VARCHAR(100) NOT NULL,				-- ����
	F_Size Decimal(20,6) NOT NULL,				 	-- �����С
	F_Bold INT NOT NULL,				   			-- ����Ӵ�
	F_Gravity INT NOT NULL,							-- ����λ��
	F_FrameID INT NOT NULL,							-- СƱID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_FrameID) REFERENCES T_SmallSheetFrame(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POSSyncCache; 	 	-- POSͬ�������
CREATE TABLE T_POSSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,					  		-- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncData_ID) REFERENCES T_POS(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POSSyncCacheDispatcher; 	-- POSͬ��������ȱ�
CREATE TABLE T_POSSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_POSSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_POS(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CategorySyncCache; 	 		-- ���ͬ�������
CREATE TABLE T_CategorySyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CategorySyncCacheDispatcher; -- ���ͬ��������ȱ�
CREATE TABLE T_CategorySyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_CategorySyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BarcodesSyncCache; 	 	   -- ������ͬ�������
CREATE TABLE T_BarcodesSyncCache						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- ���ݵ�ID������ΪD��ͬ���飬�ʿ�NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- ��������
	F_SyncSequence	INT NOT NULL,					-- ���ڿͻ����յ�N��ͬ�����֪������ʲô˳��ȥ������Щͬ����
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';		

DROP TABLE IF EXISTS T_BarcodesSyncCacheDispatcher; -- ������ͬ��������ȱ�
CREATE TABLE T_BarcodesSyncCacheDispatcher
	(
F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- ͬ������ID
	F_POS_ID INT NOT NULL,					  		-- POS��ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_BarcodesSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommodityHistory;   			-- ��Ʒ�޸���ʷ
CREATE TABLE T_CommodityHistory
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_CommodityID INT NOT NULL, 					-- ��ƷID
	F_FieldName Varchar(20) NOT NULL,				-- �ֶ�����
	F_OldValue Varchar(128) NOT NULL,				-- �ֶξ�ֵ
	F_NewValue Varchar(64) NOT NULL,				-- �ֶ���ֵ
	F_StaffID INT NOT NULL,								-- �޸���
	F_BySystem INT DEFAULT 0,						-- �Ƿ�ϵͳ�޸�
	F_ShopID INT NULL,
	F_Datetime DATETIME NOT NULL,					-- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_NbrConstant;   			-- ��Ŀ������
CREATE TABLE T_NbrConstant
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Scope INT NOT NULL, 							-- 0=ȫ�֣�1=��Ʒ������2=�ɹ�������3=�̵㣬4=��⣬������������
	F_Key Varchar(50) NOT NULL,						-- ��������
	F_Value Varchar(50) NOT NULL,					-- ����ֵ
	F_Remark Varchar(128) NOT NULL,					-- ��ע
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeDailyReportByCategoryParent;
CREATE TABLE T_RetailTradeDailyReportByCategoryParent					-- ��Ʒ�����ձ���
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  						  -- ID
	F_ShopID INT NOT NULL,													  -- �ŵ�ID
 	F_Datetime DATETIME NOT NULL,					  						  -- ͳ�����ڣ�Ωһ��ϼ�������+��Ʒ����ID��
 	F_CategoryParentID INT NOT NULL,				   						  -- ��Ʒ����ID
 	F_TotalAmount DECIMAL(20, 6) NOT NULL,			    					  -- �����ܶ�
 	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryParentID) REFERENCES t_categoryparent(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_CategoryParentID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeDailyReportSummary;			     -- �����ձ����ܱ�
CREATE TABLE T_RetailTradeDailyReportSummary
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_ShopID INT NOT NULL,													  -- �ŵ�ID
	F_Datetime DATETIME NOT NULL,											  -- ͳ������
	F_TotalNO INT NOT NULL,                				  					  -- ���۱���
	F_PricePurchase DECIMAL(20,6) NOT NULL,									  -- �ܽ�����
	F_TotalAmount DECIMAL(20, 6) NOT NULL,	 		  		 				  -- ���۶�
	F_AverageAmountOfCustomer  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,	  -- �͵���
	F_TotalGross  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,    				  -- ����ë��
	F_RatioGrossMargin DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,			  -- ����ë����
	F_TopSaleCommodityID INT NULL, 			   						          -- ���۶���ߵ���Ʒ�����첻������Ļ�������ΪNULL
	F_TopSaleCommodityNO INT NOT NULL DEFAULT 0,				   			  -- ���۶���ߵ���Ʒ������
	F_TopSaleCommodityAmount DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,		  -- ���۶���ߵ���Ʒ�����۶�
	F_TopPurchaseCustomerName VARCHAR(30) NOT NULL DEFAULT '',				  -- �������ߵĿͻ�
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- �޸�ʱ��

	PRIMARY KEY (F_ID) ,
	FOREIGN KEY (F_TopSaleCommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeMonthlyReportSummary;			           -- �����±����ܱ�
CREATE TABLE T_RetailTradeMonthlyReportSummary
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_ShopID INT NOT NULL,													  -- �ŵ�ID
	F_Datetime DATETIME NOT NULL,											  -- ͳ���·�
	F_TotalAmount DECIMAL(20, 6) NOT NULL,	 		  		 				  -- ���۶�
	F_TotalGross  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,    				  -- ����ë��
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- �޸�ʱ��

	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradeDailyReportByStaff;
CREATE TABLE T_RetailTradeDailyReportByStaff	-- Staff���۱���
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  -- ID
	F_ShopID INT NOT NULL,							  -- �ŵ�ID
	F_Datetime DATETIME NOT NULL,					  -- ����
	F_StaffID INT NOT NULL,							  -- Ա��ID
	F_NO INT NOT NULL,								  -- �����ܱ���
	F_TotalAmount Decimal(20, 6) NOT NULL,			  -- �����ܶ�
	F_GrossMargin Decimal(20, 6) NOT NULL,			  -- ������ë��
	F_CreateDatetime Datetime NOT NULL DEFAULT now(),			  -- ����ʱ��
	F_UpdateDatetime Datetime NOT NULL DEFAULT now(),			  -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_StaffID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradeDailyReportByCommodity;
CREATE TABLE T_RetailTradeDailyReportByCommodity	-- ��Ʒ���۱���
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  -- ID
	F_ShopID INT NOT NULL,							  -- �ŵ�ID
	F_Datetime DATETIME NOT NULL,					  -- ����
	F_CommodityID INT NOT NULL,						  -- ��ƷID(���)
	F_NO INT NOT NULL,								  -- ��������
	F_TotalPurchasingAmount DECIMAL(20, 6) NOT NULL,  -- �ܽ�����
	F_TotalAmount DECIMAL(20, 6) NOT NULL,			  -- �����ܶ�
	F_GrossMargin DECIMAL(20, 6) NOT NULL,			  -- ����ë��
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(), -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(), -- �޸�ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_CommodityID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP VIEW IF EXISTS V_Staff_Permission;   			-- �û�, ��ɫ, Ȩ����ͼ
CREATE VIEW V_Staff_Permission
AS
SELECT s.F_ID AS StaffID, s.F_Name AS StaffName,r.F_ID AS RoleID, r.F_Name AS RoleName, p.F_SP, p.F_Name PermissionName, p.F_Remark FROM t_staff s
LEFT JOIN t_staffrole sr ON sr.F_StaffID = s.F_ID
JOIN t_role r ON sr.F_RoleID = r.F_ID
JOIN t_role_permission ap ON ap.F_RoleID = r.F_ID
JOIN t_permission p ON p.F_ID = ap.F_PermissionID
WHERE s.F_Status = 0;


DROP TABLE IF EXISTS T_WXUser;			     -- WX�û���Ϣ
CREATE TABLE T_WXUser
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_Subscribe VARCHAR(5) NOT NULL,										  -- ֵΪ0ʱ��������û�û�й�ע�ù��ں�,Ϊ1ʱ�ѹ�ע
	F_OpenId VARCHAR(50) NOT NULL,                				  			  -- �û���Ψһ��ʶ
	F_NickName VARCHAR(100) NULL,	 		  		 				  	      -- �û��ǳ�
	F_Sex  VARCHAR(5) NULL,	  					                              -- ֵΪ1ʱ�����ԣ�ֵΪ2ʱ��Ů�ԣ�ֵΪ0ʱ��δ֪
	F_Language VARCHAR(10) NULL,											  -- ����
	F_City VARCHAR(20) NULL,			  			                          -- ����
	F_Province  VARCHAR(10) NULL,    				  						  -- ʡ��
	F_Country VARCHAR(100) NULL,			   						          -- ����
	F_HeadImgUrl VARCHAR(200) NULL,				   			 		          -- �û�ͷ��
	F_SubscribeTime VARCHAR(30) NULL,							              -- �û���עʱ�䣬Ϊʱ���
	F_Unionid VARCHAR(30) NULL,								  	  			  -- ֻ�н����ںŰ󶨵�΢�ſ���ƽ̨�ʺź󣬲Ż���ָ��ֶΡ�
	F_Remark VARCHAR(100) NULL,												  -- �Թ�ע�û��ı�ע
	F_GroupId VARCHAR(100) NULL,											  -- �û����ڵķ���ID
	F_TagIdList VARCHAR(100) NULL,											  -- �û������ϵı�ǩID�б�
	F_SubscribeScene VARCHAR(100) NULL,										  -- �����û���ע��������Դ
	F_QrScene VARCHAR(30) NULL,												  -- ��ά��ɨ�볡��
	F_QrSceneStr VARCHAR(30) NULL,										      -- ��ά��ɨ�볡������
	-- F_Privilege VARCHAR(100) NULL,				  			 			  -- ��Ȩ��Ϣ��json ���飬��΢���ֿ��û�Ϊ��chinaunicom��
   
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_MessageItem;	-- ��Ϣ������
CREATE TABLE T_MessageItem
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_MessageID INT NOT NULL,										 		  -- �����������T_Message.F_ID
	F_MessageCategoryID INT NOT NULL,                				  		  -- �����������T_MessageCategory.F_ID
	F_CommodityID INT NOT  NULL,	 		  		 				  	      -- �������������Ʒ��˵����T_Commodity.F_ID,����ƷID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- ����ʱ��
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- �޸�ʱ��
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_MessageID) REFERENCES t_message(F_ID),
	FOREIGN KEY (F_MessageCategoryID) REFERENCES t_messagecategory(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_BonusRule;			     -- ���ֹ����
CREATE TABLE T_BonusRule
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipCardID INT NOT NULL,										 		  -- �����ָ��T_VipCardID.F_ID
	F_AmountUnit INT NOT NULL,                				  				  -- ���ѽ��Է�Ϊ��λ
	F_IncreaseBonus INT NOT NULL,	 		  		 				  	      -- ���ӵĻ���
	F_MaxIncreaseBonus INT NOT NULL,				  						  -- ���οɻ�ȡ�Ļ�������
	F_InitIncreaseBonus INT NOT NULL DEFAULT 0,					  			  -- ��ʼ���û���
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_BonusConsumeHistory;		 -- ������ʷ��
CREATE TABLE T_BonusConsumeHistory
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipID INT NOT NULL,										 		  	  -- �����ָ��T_Vip.F_ID
	F_StaffID INT NULL,                				  				  	  	  -- ˭���˻���
	F_Bonus INT NULL,	 		  		 				  	      		 	  -- ����ȫ��ֵ
	F_AddedBonus INT NULL,				  		 						  	  -- ���ֱ䶯ֵ
	F_Remark VARCHAR(48) NULL,					  			   		 	 	  -- �Զ���������ļ�¼
	F_CreateDatetime DATETIME NOT NULL,
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES t_vip(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_Coupon;					 -- һ���Ż�ȯ��
CREATE TABLE T_Coupon
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_Status INT NOT NULL,										 		  	  -- ״̬
	F_Type INT NOT NULL,                				  				  -- �Ż�ȯ����
	F_Bonus INT NOT NULL,	 		  		 				  	      		  -- ��Ҫ���ٻ��ֶһ�
	F_LeastAmount Decimal(20, 6) NOT NULL DEFAULT 0,				  		  -- ���ý��
	F_ReduceAmount Decimal(20, 6) NOT NULL,					  				  -- ������
	F_Discount Decimal(20, 6) NOT NULL,					  				 	  -- ���۶��
	F_Title VARCHAR(9) NOT NULL,					  						  -- ����
	F_Color VARCHAR(16) NOT NULL,					  			  			  -- ��ɫ
	F_Description VARCHAR(1024) NOT NULL,					  				  -- ʹ��˵��
	F_PersonalLimit INT NOT NULL,					  				 		  -- ÿ�˿���ȯ����������
	F_WeekDayAvailable INT NULL,					  				  					  -- ����ʹ������
	F_BeginTime VARCHAR(8) NOT NULL,					  					  -- ������ʼʱ��
	F_EndTime VARCHAR(8) NOT NULL,					  					 	  -- �������ʱ��
	F_BeginDateTime DATETIME NOT NULL,					  					  -- ��������ʱ��
	F_EndDateTime DATETIME NOT NULL,					  					  -- ��������ʱ��
	F_Quantity INT NOT NULL,					  							  -- �������
	F_RemainingQuantity INT NOT NULL,					  					  -- ��ǰʣ��������
    F_Scope INT NOT NULL,					  								  -- �������Ʒ�ķ�Χ
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_CouponCode;		 -- һ���Ż�ȯ��
CREATE TABLE T_CouponCode
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipID INT NOT NULL,										 		  	  -- �����ָ��T_Vip.F_ID
	F_CouponID INT NOT NULL,                				  				  -- �����ָ��T_Coupon.F_ID
	F_Status INT NOT NULL,	 		  		 				  	      		  -- ״̬
	F_SN VARCHAR(15) NOT NULL,				  		 						  -- CouponCode
	F_CreateDatetime DATETIME NOT NULL,					  				 	  -- ����ʱ��
	F_UsedDatetime DATETIME NULL,					  			 		 	  -- ����ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES t_vip(F_ID),
	FOREIGN KEY (F_CouponID) REFERENCES t_coupon(F_ID),
	UNIQUE KEY F_SN (F_SN)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_CouponScope;		 -- �Ż�ȯ���÷�Χ��
CREATE TABLE T_CouponScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_CouponID INT NOT NULL,                				  				  -- �����ָ��T_Coupon.F_ID
	F_CommodityID INT NOT NULL,	 		  		 				  	      	  -- ��ƷID
	F_CommodityName VARCHAR(32) NOT NULL,				  		 			  -- ��Ʒ����
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CouponID) REFERENCES t_coupon(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradeCoupon;			-- ���۵�ʹ�õ��Ż�ȯ
CREATE TABLE T_RetailTradeCoupon
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ����ID
	F_RetailTradeID INT NOT NULL,					-- ���۵����
	F_CouponCodeID INT NOT NULL,					-- ȯ���
	F_SyncDatetime DATETIME NOT NULL,				-- ͬ��ʱ��
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeID) REFERENCES t_retailtrade(F_ID),
 	FOREIGN KEY (F_CouponCodeID) REFERENCES t_couponcode(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';