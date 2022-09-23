-- --------------------------------------------------------------------------------------------------------- 状态机表 begin ----------------------------------------
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

-- --------------------------------------------------------------------------------------------------------- 状态机表 end ----------------------------------------

CREATE TABLE t_tmp
	(
	F_Log VARCHAR (255) NULL,
	F_Num1 INT NULL,
	F_Num2 INT NULL,
	F_Num3 INT NULL,
	F_Num4 INT NULL,
	F_Num5 INT NULL
	);
-- 错误代码表

DROP TABLE IF EXISTS t_errorcode;

CREATE TABLE t_errorcode
	(
	F_ID               INT auto_increment,
	F_ErrorCode        INT NOT NULL,
	F_ErrorDescription VARCHAR (30) NOT NULL,
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_ErrorCode (F_ErrorCode)
	);
	
-- 供应商

DROP TABLE IF EXISTS T_ProviderDistrict;
CREATE TABLE T_ProviderDistrict					  -- 供应商区域表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,		          -- 区域名字
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Provider;
CREATE TABLE T_Provider							  -- 供应商
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (32) NOT NULL,			      -- 名称
	F_DistrictID INT NULL,					 		  -- 区域ID
	F_Address VARCHAR (50) NULL,			  			  -- 联系地址
	F_ContactName VARCHAR (20) NULL,		  			  -- 联系人姓名
	F_Mobile VARCHAR (24) NULL,   			 		  -- 联系人电话
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_DistrictID) REFERENCES T_ProviderDistrict(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 商品

DROP TABLE IF EXISTS T_Brand;
CREATE TABLE T_Brand					 		  -- 品牌表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,	     	  -- 品牌名称
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- 修改时间
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_CategoryParent; 	   	 	   -- 商品类别的大类
CREATE TABLE T_CategoryParent						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (10) NOT NULL,	  			   -- 类别名称
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- 修改时间
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Category; 	   	 		   -- 商品类别
CREATE TABLE T_Category						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (10) NOT NULL,	  			   -- 类别名称
	F_ParentID INT NOT NULL,					   -- 外键。对应T_CategoryParent的F_ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ParentID) REFERENCES T_CategoryParent(F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_PackageUnit; 	   	 	   -- 包装单位
CREATE TABLE T_PackageUnit						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR (8) NOT NULL,	  			   -- 包装单位名称
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间

	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Department; 	   
CREATE TABLE T_Department		          		   -- 用户组表（部门表）		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_DepartmentName VARCHAR(20) NOT NULL,  	   -- 用户组名称
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_ShopDistrict;
CREATE TABLE T_ShopDistrict					  -- 门店区域表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  -- ID
	F_Name VARCHAR (20) NOT NULL,		          -- 区域名字
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Shop; 	   
CREATE TABLE T_Shop		       					   -- 门店信息表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(20) NOT NULL,				   -- 门店名称
	F_CompanyID	INT NOT NULL,				       -- 博昕客户ID 
	F_BXStaffID INT NOT NULL,					   -- 博昕业务员ID
	F_Address VARCHAR(30) NOT NULL,				   -- 地址
	F_DistrictID INT NULL,					 	   -- 区域ID
	F_Status INT NOT NULL,				           -- 状态
	F_Longitude Decimal(20,6) NOT NULL,	   		   -- 经度
	F_Latitude Decimal(20,6) NOT NULL,			   -- 纬度
	F_Key VARCHAR(32) NOT NULL,	  				   -- 钥匙
	F_Remark VARCHAR(30) NULL,					   -- 备注
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID), -- ,
	FOREIGN KEY (F_CompanyID) REFERENCES nbr_bx.T_Company(F_ID),
	FOREIGN KEY (F_BXStaffID) REFERENCES nbr_bx.T_BXStaff(F_ID),
	FOREIGN KEY (F_DistrictID) REFERENCES T_ShopDistrict(F_ID)
--	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POS; 	   
CREATE TABLE T_POS		      					   -- POS机信息		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_POS_SN VARCHAR(32) NOT NULL,				   -- SN       ...
	F_ShopID INT NOT NULL,						   -- 门店ID
	F_pwdEncrypted VARCHAR(0) NULL,				   -- 公钥加密后的用户密码
	F_Salt VARCHAR(32) NOT NULL,			   	   -- 加盐后的MD5值
	F_PasswordInPOS VARCHAR(16) NULL,	  		   -- 在POS机上的密码
	F_Status INT NOT NULL ,                        -- POS机状态 0可以用 1不可用
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID)
--	UNIQUE KEY F_POS_SN (F_POS_SN)  status为1的F_POS_SN可以重新创建一个正常使用的pos。
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Staff; 	   
CREATE TABLE T_Staff		           			   -- 门店用户		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(12) NOT NULL,				   -- 店员名称
	F_Phone VARCHAR(32) NOT NULL,				   -- 手机号码
	F_ICID VARCHAR(20) NULL,				   -- 身份证号码
	F_WeChat VARCHAR(20) NULL,				   -- 微信号
	F_OpenID VARCHAR(100)  NULL,			   -- 微信用户的唯一标识。
	F_Unionid VARCHAR(100) NULL,				   -- 只有将公众号绑定到微信开放平台帐号后，才会出现该字段,同一用户的unionid是唯一的。
	F_pwdEncrypted VARCHAR(0) NULL,				   -- 公钥加密后的用户密码
	F_Salt VARCHAR(32) NOT NULL,			   	   -- 加盐后的MD5值
	F_PasswordExpireDate DATETIME NULL,	       -- 密码有效期
	F_IsFirstTimeLogin INT NOT NULL DEFAULT 1,     -- 首次登录成功？
	F_ShopID INT NOT NULL,						   -- 门店ID
	F_DepartmentID INT NOT NULL,				   -- 所属的部门。默认为1
	F_Status INT NOT NULL,						   -- 0，在职。1，离职。
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID), 
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID), 
	FOREIGN KEY (F_DepartmentID) REFERENCES T_Department(F_ID)
--	FOREIGN KEY (F_IDInPOS) REFERENCES T_POS(F_ID) -- ,
--	UNIQUE KEY F_ICID (F_ICID),
--	UNIQUE KEY F_WeChat (F_WeChat)
--	UNIQUE KEY F_Phone (F_Phone)     status为1的phone可以重新创建一个正常使用的staff。
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Warehouse; 	   
CREATE TABLE T_Warehouse		      	  		   -- 仓库表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
--	F_CompanyID INT NULL,				       -- 博昕客户ID
	F_Name VARCHAR(32) NOT NULL,				   -- 名称	
	F_Address VARCHAR(32) NULL,			   	   -- 地址
	F_Status INT NOT NULL DEFAULT 0,	   		   -- 状态 0：正常 1：删除
	F_StaffID INT NULL,						   -- 联系人
	F_Phone VARCHAR(32) NULL,				   -- 电话
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
--	FOREIGN KEY (F_CompanyID) REFERENCES T_BXStaff(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	UNIQUE KEY F_Name (F_Name)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_PurchasingOrder; 	   
CREATE TABLE T_PurchasingOrder		               -- 采购订单表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ShopID INT NULL,							   -- 门店ID
	F_SN VARCHAR(20) NOT NULL,					   -- 单号
	F_Status INT NOT NULL DEFAULT 0,			   -- 状态
	F_StaffID INT NOT NULL,  					   -- 创建人
	F_ProviderID INT NULL,					   -- 供应商ID
	F_ProviderName VARCHAR(32) NOT NULL,		   -- 供应商名称
	F_ApproverID INT NULL,					       -- 审核人
	F_Remark VARCHAR (128) NOT NULL,			   -- 采购总结
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 创建日期时间
	F_ApproveDatetime DATETIME NULL,		  	   -- 审核日期时间
	F_EndDatetime DATETIME NULL,			       -- 终止日期时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ProviderID) REFERENCES T_Provider(F_ID),
	FOREIGN KEY (F_ApproverID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Warehousing; 	   	  
CREATE TABLE T_Warehousing					  		 -- 入库单表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ShopID INT NULL,							   -- 门店ID
	F_Status INT NOT NULL,						   -- 0:未审核,1,审核
	F_SN VARCHAR(20) NOT NULL,					   -- 单号
	F_ProviderID INT NOT NULL,					   -- 供应商ID
	F_WarehouseID INT NOT NULL ,		  		   -- 收货仓库	 					   
	F_StaffID INT NOT NULL ,			 		   -- 业务员 
	F_ApproverID INT NULL,					       -- 审核人
	F_CreateDatetime DATETIME NOT NULL,			   -- 入库日期
	F_PurchasingOrderID	INT NULL ,  		   -- 对应的采购订单ID		
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
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
CREATE TABLE T_Commodity						  -- 商品信息
	(
	F_ID   INT NOT NULL AUTO_INCREMENT,    		  -- 商品ID
	F_Status INT NOT NULL DEFAULT 0, 			  -- 商品状态
	F_Name VARCHAR (32) NOT NULL,		          -- 商品名称
	F_ShortName VARCHAR (32),			   		  -- 商品简称
	F_Specification VARCHAR (8) NOT NULL, 		  -- 规格
	F_PackageUnitID INT,    	 		  		  -- 包装单位ID
	F_PurchasingUnit VARCHAR (16) NULL, 		  -- 采购单位
	F_BrandID INT,     		 					  -- 商品品牌ID
	F_CategoryID INT,		   	 			      -- 类别ID
	F_MnemonicCode VARCHAR (32) NOT NULL,  		  -- 助记码
	F_PricingType INT NOT NULL,   				  -- 计价方式	
	-- F_IsServiceType INT NOT NULL,  		 		  -- 是否服务器类商品(已废弃)
	-- F_PricePurchase Decimal(20,6) NOT NULL,		  -- 平均进货价(已废弃)
--	F_LatestPricePurchase Decimal(20,6) NOT NULL, -- 最新进货价
--	F_PriceRetail Decimal(20,6) NOT NULL,		  -- 零售价
	F_PriceVIP Decimal(20,6) NOT NULL,			  -- 会员价
	F_PriceWholesale Decimal(20,6) NOT NULL,  	  -- 批发价	
	-- F_RatioGrossMargin Decimal(20,6) NOT NULL,	  -- 毛利率(已废弃)
	F_CanChangePrice INT NOT NULL, 		          -- 前台是否能改价
	F_RuleOfPoint INT NULL,   		  		 	  -- 积分规则 --
	F_Picture VARCHAR (128) NULL,	   		 	  -- 商品图片
	F_ShelfLife INT NULL,     		 	   		  -- 保质期 --
	F_ReturnDays INT NOT NULL,	   		  		  -- 退货天数
	F_CreateDate DATETIME NOT NULL,		   		  -- 创建日期
	F_PurchaseFlag INT NULL,		        	  -- 采购阀值 --
	F_RefCommodityID INT NOT NULL DEFAULT 0, 	  -- 参照商品ID
	F_RefCommodityMultiple INT NOT NULL DEFAULT 0, -- 参照商品倍数
	-- F_IsGift INT NOT NULL,	 					  -- 是否赠送商品(已废弃)
	F_Tag VARCHAR (32) NOT NULL,				  -- TAG
--	F_NO INT NOT NULL,							  -- 系统的数量，和盘点的实盘数量不同
	-- F_NOAccumulated INT NOT NULL,				  -- 每次商品入库都会将商品数量累加到这个字段。(已废弃)
	F_Type INT NOT NULL,						  -- 商品类型
--	F_NOStart INT NOT NULL DEFAULT -1,			  -- 期初数量
--	F_PurchasingPriceStart Decimal(20,6) NOT NULL DEFAULT -1, 		  -- 期初采购价
	F_StartValueRemark Varchar(50) NULL, 		  -- 期初值备注
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),         -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	      -- 修改时间
	F_PropertyValue1 VARCHAR(50) NULL,            -- 自定义属性1
	F_PropertyValue2 VARCHAR(50) NULL,            -- 自定义属性2
	F_PropertyValue3 VARCHAR(50) NULL,            -- 自定义属性3
	F_PropertyValue4 VARCHAR(50) NULL,            -- 自定义属性4
--	F_CurrentWarehousingID INT NULL,			  -- 当值入库ID
	
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
		F_LatestPricePurchase Decimal(20,6) NOT NULL, -- 最新进货价
		F_PriceRetail Decimal(20,6) NOT NULL,		  -- 零售价
		F_NO INT NOT NULL,
		F_NOStart INT NOT NULL DEFAULT -1,			  -- 期初数量
		F_PurchasingPriceStart Decimal(20,6) NOT NULL DEFAULT -1, 		  -- 期初采购价
		F_CurrentWarehousingID INT NULL,			  -- 当值入库ID
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
	F_CommodityID INT NOT NULL,		  			    -- 商品ID
	F_ProviderID INT NOT NULL,			  			-- 供应商ID
	
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
	F_CommodityID INT NOT NULL,		  			    -- 商品ID
	F_SubCommodityID INT NOT NULL,			  		-- 组合商品ID
	F_SubCommodityNO INT NOT NULL,					-- 子商品的数量
	F_Price DECIMAL(20, 6) NOT NULL,				-- 子商品的单价
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_SubCommodityID ) REFERENCES T_Commodity(F_ID),
	UNIQUE KEY F_CombinationCommodity(F_CommodityID, F_SubCommodityID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Barcodes; 	 	   		   -- 商品一品多码
CREATE TABLE T_Barcodes						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CommodityID INT NOT NULL,		  			   -- 商品ID。支持一品多码
	F_Barcode VARCHAR (64) NOT NULL,			   -- 条码。支持一码多品
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),    -- 修改时间
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY F_CommodityBarcode(F_CommodityID, F_Barcode),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommodityProperty;		   -- 商品属性表
CREATE TABLE T_CommodityProperty
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- 商品属性ID
	F_Name1 VARCHAR(16) NULL,			 		   -- 商品属性名1
	F_Name2 VARCHAR(16) NULL,			 		   -- 商品属性名2
	F_Name3 VARCHAR(16) NULL,			 		   -- 商品属性名3
	F_Name4 VARCHAR(16) NULL,			 		   -- 商品属性名4
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Permission; 	   
CREATE TABLE T_Permission		          		   -- 操作权限表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SP VARCHAR(80) NOT NULL,					   -- 对应操作的SP
	F_Name VARCHAR(20) NOT NULL,  	   			   -- 操作名称
	F_Domain VARCHAR(16) NOT NULL ,				       -- 领域名称
	F_Remark VARCHAR(32) NOT NULL,				   -- 操作备注
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Role; 	   
CREATE TABLE T_Role		          		  		   -- 角色		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(20) NOT NULL,			   -- 角色名称
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_Role_Permission; 	   
CREATE TABLE T_Role_Permission		          	   -- 角色操作中间表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_RoleID INT NOT NULL,			  			   -- 角色ID
	F_PermissionID INT NOT NULL,				   -- 操作ID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RoleID ) REFERENCES T_Role(F_ID),
	FOREIGN KEY (F_PermissionID ) REFERENCES T_Permission(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_StaffRole; 	   
CREATE TABLE T_StaffRole		          		   -- 用户角色表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_StaffID INT NOT NULL,						   -- 用户ID
	F_RoleID INT NOT NULL,						   -- 角色ID
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_RoleID ) REFERENCES T_Role(F_ID),
	UNIQUE KEY F_StaffID(F_StaffID)	 -- ... 一个员工有且只有一个角色
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_WarehousingCommodity; 	   
CREATE TABLE T_WarehousingCommodity				   	-- 入库单商品表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   	-- ID
	F_WarehousingID INT NOT NULL,			  	   	-- 入库单ID
	F_CommodityID INT NOT NULL,					  	-- 商品ID	
	F_NO INT NOT NULL,							   	-- 收货数量
	F_PackageUnitID INT NOT NULL,					-- 包装单位ID
	F_CommodityName VARCHAR(32) NOT NULL,			-- 商品名称
	F_BarcodeID INT NOT NULL,						-- 条形码ID
	F_Price Decimal(20,6) NOT NULL,					-- 单价
	F_Amount Decimal(20,6) NOT NULL,				-- 金额
	F_ProductionDatetime DATETIME NOT NULL,		   	-- 生产日期
	F_ShelfLife INT NOT NULL,			          	-- 保质期
	F_ExpireDatetime DATETIME NOT NULL,			   	-- 失效期
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	F_NOSalable INT NOT NULL, 						-- 可售数量
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES T_Warehousing(F_ID),
	FOREIGN KEY (F_PackageUnitID) REFERENCES T_PackageUnit(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID) REFERENCES T_Barcodes(F_ID),
	UNIQUE KEY F_WarehousingCommodityID (F_CommodityID, F_WarehousingID)
	
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 采购订单

DROP TABLE IF EXISTS T_PurchasingOrderCommodity; 	   
CREATE TABLE T_PurchasingOrderCommodity		       -- 采购订单商品表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_PurchasingOrderID INT NOT NULL,			   -- 商品订单表ID
	F_CommodityID INT NOT NULL,					   -- 商品ID
	F_CommodityNO INT NOT NULL DEFAULT 1,		   -- 商品数量
	F_CommodityName VARCHAR(32) NOT NULL,		   -- 商品名称
	F_BarcodeID INT NOT NULL,					   -- 条形码ID
	F_PackageUnitID INT NOT NULL,				   -- 包装单位ID
	F_PriceSuggestion Decimal(20,6) NOT NULL,	   -- 建议采购单价
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PurchasingOrderID ) REFERENCES T_PurchasingOrder(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES t_barcodes(F_ID),
	FOREIGN KEY (F_PackageUnitID ) REFERENCES t_packageunit(F_ID),
	UNIQUE KEY UniquePurchasingOrderCommodity (F_PurchasingOrderID, F_CommodityID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 仓管退货单

DROP TABLE IF EXISTS T_ReturnCommoditySheet; 	   
CREATE TABLE T_ReturnCommoditySheet		           -- 仓管退货单
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(20) NOT NULL,					   -- 单号
	F_StaffID INT NOT NULL,						   -- 操作人ID
	F_ProviderID INT NOT NULL,					   -- 供应商ID
	F_Status INT NOT NULL DEFAULT 0,			   -- 审核状态 0=未审核 1=审核
	F_ShopID INT NOT NULL,
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),		-- 退货日期
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),		-- 修改日期
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ProviderID) REFERENCES T_Provider(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 仓管退货单商品表

DROP TABLE IF EXISTS T_ReturnCommoditySheetCommodity; 	   
CREATE TABLE T_ReturnCommoditySheetCommodity		           -- 仓管退货单商品表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_ReturnCommoditySheetID INT NOT NULL,		   -- 退货单ID
	F_BarcodeID INT NOT NULL,					   -- 条形码ID
	F_CommodityID INT NOT NULL,					   -- 商品ID
	F_CommodityName VARCHAR(32) NOT NULL,		   -- 商品名称
	F_NO INT NOT NULL,							   -- 数量
	F_Specification VARCHAR (8) NOT NULL,	 	   -- 规格
	F_PurchasingPrice Decimal(20,6) NOT NULL,	   -- 采购价
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ReturnCommoditySheetID) REFERENCES T_ReturnCommoditySheet(F_ID),
	FOREIGN KEY (F_BarcodeID) REFERENCES T_Barcodes(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_Commodity(F_ID),
	UNIQUE KEY UniqueReturnCommoditySheetCommodity (F_ReturnCommoditySheetID, F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 盘点单

DROP TABLE IF EXISTS T_InventorySheet; 	   
CREATE TABLE T_InventorySheet		               -- 盘点单	
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(20) NOT NULL,					   -- 单号
	F_ShopID INT NOT NULL,		  	           	   -- 门店ID
	F_WarehouseID INT NOT NULL,		  	           -- 盘点仓库
	F_Scope INT NOT NULL,						   -- 盘点范围
	F_Status INT NOT NULL,				  		   -- 状态  
	F_StaffID INT NOT NULL,  			           -- 创建者
	F_ApproverID INT NULL,					       -- 审核人
	F_CreateDatetime DATETIME NOT NULL,			   -- 创建日期时间
	F_ApproveDatetime DATETIME NULL,			   -- 审核日期时间
	F_Remark VARCHAR (128) NOT NULL,			   -- 盘点总结
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_WarehouseID) REFERENCES T_Warehouse(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES T_Shop(F_ID),
	FOREIGN KEY (F_ApproverID) REFERENCES T_Staff(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	



DROP TABLE IF EXISTS T_InventoryCommodity; 	   
CREATE TABLE T_InventoryCommodity		           -- 盘点商品表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_InventorySheetID INT NOT NULL,			   -- 盘点单号
	F_CommodityID INT NOT NULL,					   -- 商品ID
	F_CommodityName VARCHAR (32) NOT NULL,		   -- 商品ID
	F_Specification VARCHAR (8) NOT NULL,		   -- 商品规格
	F_BarcodeID INT NOT NULL,					   -- 条形码ID
	F_PackageUnitID INT NOT NULL,				   -- 包装单位ID
	F_NOReal INT NULL,						  	   -- 实盘商品数量
	F_NOSystem INT NULL,					       -- 系统库存数量
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_InventorySheetID ) REFERENCES T_InventorySheet(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES t_barcodes(F_ID),
	FOREIGN KEY (F_PackageUnitID ) REFERENCES t_packageunit(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 会员

DROP TABLE IF EXISTS T_VIP_Category; 	   
CREATE TABLE T_VIP_Category		      			   -- 会员表类别		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(30) NOT NULL,				   -- 类别名称
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VipCard; 	   
CREATE TABLE T_VipCard		      			 	   -- 一种会员卡		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Title VARCHAR(9) NOT NULL,				   -- 名称
	F_BackgroundColor VARCHAR(23) NOT NULL ,  	   -- 会员卡背景图
	F_ClearBonusDay INT NULL,  					   -- 积分清零天数
	F_ClearBonusDatetime DATETIME NULL,  		   -- 积分清零日期
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),  -- 开卡时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Vip;
CREATE TABLE T_Vip		      					   -- 会员表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_SN VARCHAR(9) NOT NULL, 					   -- 会员编号
	F_CardID INT NOT NULL,						   -- 外键 关联T_VipCard的ID
	F_Mobile VARCHAR(11) NULL,					   -- 手机号码
	F_LocalPosSN VARCHAR(32) NULL,				   -- POS机的SN码
	F_Sex INT NOT NULL DEFAULT 1,				   -- 性别
	F_Logo VARCHAR(128) NULL,					   -- 头像?图片?
	F_ICID VARCHAR(30) NULL,			   		   -- ICID
	F_Name VARCHAR(32) NOT NULL,				   -- 姓名
	F_Email VARCHAR(30) NULL,	            	   -- 邮箱
	F_ConsumeTimes INT NOT NULL,				   -- 总消费次数
	F_ConsumeAmount Decimal(20,6) NOT NULL,	  	   -- 总消费金额
	F_District VARCHAR(30) NOT NULL,			   -- 区域
	F_Category INT NOT NULL,					   -- 类别
	F_Birthday DATETIME NULL,			 		   -- 生日
	F_Bonus INT NOT NULL,			   			   -- 当前积分
	F_LastConsumeDatetime DATETIME NULL,		   -- 上次消费日期时间
	F_Remark VARCHAR(50) NULL,			  		   -- 备注		
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
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
CREATE TABLE T_VipCardCode		      			   -- 一张会员卡		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_VipCardID INT NOT NULL ,  		   		   -- 会员卡ID
	F_SN VARCHAR(16) NOT NULL,  	 	  		   -- 会员卡号
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),  -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_VIP(F_ID),
	FOREIGN KEY (F_VipCardID) REFERENCES t_vipcard(F_ID),
	UNIQUE KEY F_SN (F_SN)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VipSource; 	   
CREATE TABLE T_VipSource		      			   -- 会员来源表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NOT NULL,				  		   -- 会员ID
	F_SourceCode INT NOT NULL ,  		   		   -- 来自哪里 0=WX, 1=ALIPAY, 2=MEITUAN
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
CREATE TABLE T_RetailTrade		                   -- 零售单		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VipID INT NULL,							   -- 会员ID
	F_SN VARCHAR(26) NOT NULL,					   -- 单据流水号
	F_LocalSN INT NOT NULL,				           -- 收银机单号，在这台收银机上的惟一单号
	F_POS_ID INT NOT NULL,						   -- 收银机ID
	F_Logo VARCHAR(128) NOT NULL,				   -- 自定义logo
	F_SaleDatetime DATETIME NOT NULL,			   -- 消费时间
	F_StaffID INT NOT NULL,						   -- 收银员ID
	F_PaymentType INT NOT NULL,					   -- 支付方式
	F_PaymentAccount VARCHAR(20) NOT NULL,		   -- 帐号
	F_Status INT NOT NULL,						   -- 状态   0挂起/1正常/2删除
	F_Remark VARCHAR(20) NOT NULL,				   -- 备注
	F_SourceID INT DEFAULT -1,		   			   -- 源开单ID
	F_SyncDatetime DATETIME NOT NULL,        	  	   -- 同步时间
	F_Amount Decimal(20,6) NOT NULL,               -- 零售单总金额
	F_AmountPaidIn Decimal(20,6) NOT NULL,         -- 零售单实收金额
	F_AmountChange Decimal(20,6) NOT NULL,         -- 零售单找零金额
	F_AmountCash Decimal(20,6) NULL, 			   -- 现金支付数目
	F_AmountAlipay Decimal(20,6) NULL,			   -- 支付宝支付数目
	F_AmountWeChat Decimal(20,6) NULL,			   -- 微信支付数目
	F_Amount1 Decimal(20,6) NULL,				   -- 其它支付的数目1
	F_Amount2 Decimal(20,6) NULL,				   -- 其它支付的数目2
	F_Amount3 Decimal(20,6) NULL,				   -- 其它支付的数目3
	F_Amount4 Decimal(20,6) NULL,				   -- 其它支付的数目4
	F_Amount5 Decimal(20,6) NULL,				   -- 其它支付的数目5
	F_SmallSheetID INT NOT NULL,			   	   -- 小票ID
	F_AliPayOrderSN VARCHAR(32) NULL,			   -- 支付宝订单号
	F_WxOrderSN VARCHAR(32) NULL,				   -- 微信订单号
	F_WxTradeNO VARCHAR(32) NULL,				   -- 微信交易单号
	F_WxRefundNO VARCHAR(32) NULL,				   -- 微信退款单号
	F_WxRefundDesc VARCHAR(80) NULL,			   -- 微信退款描述
	F_WxRefundSubMchID VARCHAR(32) NULL,           -- 微信退款子商户号
	F_CouponAmount Decimal(20,6) NOT NULL DEFAULT '0.000000d',   -- 优惠券抵扣金额
	F_ConsumerOpenID VARCHAR(100) NULL,            -- 标识谁用WX支付了本零售单
	F_ShopID INT NOT NULL,            -- 门店的ID -- TODO not null
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY RetailTradeIDInPOS (F_LocalSN, F_POS_ID, F_SaleDatetime), -- 惟一标识一张零售单
	FOREIGN KEY (F_POS_ID) REFERENCES T_POS(F_ID),
	FOREIGN KEY (F_VipID) REFERENCES T_Vip(F_ID),
	FOREIGN KEY (F_StaffID ) REFERENCES T_Staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID) -- TODO inserttable比较多零售数据，先注释
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	
	
DROP TABLE IF EXISTS T_RetailTradeCommodity; 	   
CREATE TABLE T_RetailTradeCommodity		           -- 零售单商品表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_TradeID INT NOT NULL, 					   -- 主表ID
	F_CommodityID INT NULL ,		 		 	   -- 商品ID
	F_CommodityName VARCHAR(32) NOT NULL,		   -- 商品名称
	F_BarcodeID INT NULL,						   -- 条形码ID
	F_NO INT NOT NULL,			   				   -- 数量
	F_PriceOriginal Decimal(20,6) NULL,			   -- 原价
    -- F_Discount Decimal(20,6) NOT NULL,			   -- 折扣(已废弃)
	-- F_IsGift INT NOT NULL DEFAULT 0,			   -- 是否赠送(已废弃)
	F_NOCanReturn INT NOT NULL,   				   -- 可退货数量
	F_PriceReturn Decimal(20,6) NULL,			   -- 退货价
	F_PriceSpecialOffer Decimal(20,6) NULL,		   -- 特价
	F_PriceVIPOriginal Decimal(20,6) NULL,		   -- 商品原会员价
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_TradeID ) REFERENCES T_RetailTrade(F_ID),
	FOREIGN KEY (F_BarcodeID ) REFERENCES T_Barcodes(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES T_Commodity(F_ID),
	UNIQUE KEY UniqueCommodity (F_TradeID, F_CommodityID) -- 惟一标识一张零售单商品表
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeCommoditySource; 	   
CREATE TABLE T_RetailTradeCommoditySource		        -- 零售单商品来源表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  		-- ID
	F_RetailTradeCommodityID INT NOT NULL, 				-- 零售单商品ID
	F_ReducingCommodityID INT NOT NULL,					-- 实际被减少库存的商品ID。它可能是组合商品中的子商品，也可能是多包装商品对应的单品
	F_NO INT NOT NULL ,		 		 	   					-- 零售单商品数量
	F_WarehousingID INT NULL,							-- 入库ID(PET-1281修改为可null)
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeCommodityID) REFERENCES T_RetailTradeCommodity(F_ID),
	FOREIGN KEY (F_ReducingCommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES t_warehousing(F_ID),
	UNIQUE KEY UniqueWarehousing (F_RetailTradeCommodityID, F_WarehousingID, F_ReducingCommodityID) -- 惟一标识N个同样的商品的来源（入库）
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_ReturnRetailTradeCommodityDestination; 	   
CREATE TABLE T_ReturnRetailTradeCommodityDestination		-- 零售单商品退货去向表
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			  			-- ID
	F_RetailTradeCommodityID INT NOT NULL, 					-- 零售单商品ID（退货单从表ID）
	F_IncreasingCommodityID INT NOT NULL,					-- 实际被增加库存的商品ID。它可能是组合商品中的子商品，也可能是多包装商品对应的单品，也可能是服务型商品
	F_NO INT NOT NULL,		 		 	   					-- 这个数量<=对应的零售单商品表中该商品的数量。它表明这F_NO个商品，属于哪次入库，进而知道它的进货价-因为入库中有保存这F_NO个商品的进货价是多少
	F_WarehousingID INT NULL,								-- 入库ID， 当商品为期初商品时，找不到入库单，此时入库价为0.000000d
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeCommodityID) REFERENCES T_RetailTradeCommodity(F_ID),
	FOREIGN KEY (F_IncreasingCommodityID) REFERENCES T_Commodity(F_ID),
	FOREIGN KEY (F_WarehousingID) REFERENCES t_warehousing(F_ID),
	UNIQUE KEY UniqueReturnRetailTradeCommodityDestination (F_RetailTradeCommodityID, F_WarehousingID, F_IncreasingCommodityID)  -- 惟一标识N个同样的商品的来源（入库）
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_VIPPointHistory; 	   
CREATE TABLE T_VIPPointHistory		      		   -- 会员积分历史表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_VIP_ID INT NOT NULL,						   -- 会员ID
	F_PointChanged INT NOT NULL,		   		   -- 增减积分值
	F_RetailTradeID INT NOT NULL,	   	   		   -- 对应的零售单ID
	F_CreateDatetime DATETIME NOT NULL,			   -- 日期时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VIP_ID) REFERENCES T_VIP(F_ID),
	FOREIGN KEY (F_RetailTradeID) REFERENCES t_RetailTrade(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeAggregation; 	   
CREATE TABLE T_RetailTradeAggregation		       -- 服务器的收银汇总		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_StaffID INT NOT NULL,					       -- 门店人员ID
	F_PosID INT NOT NULL,                          -- POS机ID
	F_WorkTimeStart DATETIME NOT NULL,			   -- 上班时间
	F_WorkTimeEnd DATETIME NULL,				   -- 下班时间
	F_TradeNO INT NOT NULL,					       -- 交易单数
	F_Amount Decimal(20,6) NOT NULL,			   	   -- 营业额
	F_ReserveAmount Decimal(20,6) NOT NULL,	       -- 准备金
	F_CashAmount Decimal(20,6) NULL,		   	   -- 现金收入
	F_WechatAmount Decimal(20,6) NULL,		       -- 微信收入
	F_AlipayAmount Decimal(20,6) NULL,		   	   -- 支付宝收入
	F_Amount1 Decimal(20,6) NULL,			  	   -- 其它支付的数目1
	F_Amount2 Decimal(20,6) NULL,			  	   -- 其它支付的数目2
	F_Amount3 Decimal(20,6) NULL,			       -- 其它支付的数目3
	F_Amount4 Decimal(20,6) NULL,			  	   -- 其它支付的数目4
	F_Amount5 Decimal(20,6) NULL,			       -- 其它支付的数目5 
	F_UploadDateTime DATETIME NOT NULL,			   -- 上传到服务器的时间

	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_PosID) REFERENCES t_pos(F_ID),
	UNIQUE KEY StaffWorkTiome (F_StaffID, F_PosID, F_WorkTimeStart)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

-- 消息

DROP TABLE IF EXISTS T_MessageCategory; 	   
CREATE TABLE T_MessageCategory		          	       -- 消息类别表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_Name VARCHAR(32) NOT NULL,				   -- 类别名称

	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_Message; 	   
CREATE TABLE T_Message		          	           -- 消息表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CategoryID INT NOT NULL,				 	   -- 类别
	F_CompanyID INT NOT NULL,                      -- 标识这家消息属于哪家公司
	F_IsRead INT NOT NULL DEFAULT 0,	 		   -- 已读
	F_Status INT NOT NULL DEFAULT 0,               -- 0未发送, 1已发送,发送到公众号给老板们审阅
	F_Parameter VARCHAR(255) NOT NULL,			   -- 参数
	F_CreateDatetime DATETIME NOT NULL,				   -- 生成日期
	F_SenderID INT NOT NULL DEFAULT 0,  		   -- 发送用户ID
	F_ReceiverID INT NOT NULL, 					   -- 接收用户ID
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryID) REFERENCES T_MessageCategory(F_ID),
	FOREIGN KEY (F_CompanyID) REFERENCES nbr_bx.T_Company(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_MessageHandlerSetting; 	   
CREATE TABLE T_MessageHandlerSetting		       -- 消息处理配置表		
    (
	F_ID INT NOT NULL AUTO_INCREMENT,			   -- ID
	F_CategoryID INT NOT NULL,		   			   -- 类别ID
	F_Template VARCHAR(128) NOT NULL,	 		   -- 类别模版
	F_Link VARCHAR(255) NOT NULL,				   -- 链接
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryID) REFERENCES T_MessageCategory(F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_Promotion; 					-- 促销表
CREATE TABLE T_Promotion
	(	
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SN VARCHAR(20) NOT NULL,					    -- 单号
    F_Name VARCHAR(32) NOT NULL,                    -- 活动名称                      
    F_Status INT,                                   -- 状态 0=有效，1=已经删除（无效）
    F_Type INT,                                     -- 活动类型 0=满减。1=满折
    F_DatetimeStart DATETIME NOT NULL,              -- 开始日期
    F_DatetimeEnd DATETIME NOT NULL,                -- 结束日期
    F_ExcecutionThreshold DECIMAL(20, 6) NOT NULL,  -- 满减阀值
    F_ExcecutionAmount DECIMAL(20, 6) NULL,         -- 满减金额 F_Type为0时有效
    F_ExcecutionDiscount DECIMAL(20, 6) NULL,       -- 满减折扣 F_Type为1时有效 0<F_ExcecutionDiscount<10 的一位小数
    F_Scope INT NOT NULL,                           -- 参与的商品的范围 0=全部商品，1=部分商品（这些商品记录在T_PromotionScope中） 
    F_ShopScope INT NOT NULL,                       -- 参与的门店的范围 0=全部门店，1=部分门店（这些门店记录在T_PromotionShopScope中） 
    F_Staff INT NOT NULL,                           -- 创建者 外键
    F_CreateDatetime DATETIME,                      -- 创建时间
    F_UpdateDatetime DATETIME,                      -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_Staff ) REFERENCES T_Staff(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionScope; 					-- 促销范围表
CREATE TABLE T_PromotionScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_PromotionID INT,                              -- 促销ID
	F_CommodityID INT DEFAULT NULL,                 -- 商品ID
	F_CommodityName VARCHAR (32) NOT NULL,		    -- 商品名称
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PromotionID ) REFERENCES T_Promotion(F_ID),
	FOREIGN KEY (F_CommodityID ) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionShopScope; 					-- 促销门店范围表
CREATE TABLE T_PromotionShopScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_PromotionID INT,                              -- 促销ID
	F_ShopID INT NOT NULL,                 			-- 门店ID
	F_ShopName VARCHAR (32) NOT NULL,		 	    -- 门店名称
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_PromotionID ) REFERENCES T_Promotion(F_ID),
	FOREIGN KEY (F_ShopID ) REFERENCES t_shop(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionSyncCache; 	 		 -- 促销同步缓存表
CREATE TABLE T_PromotionSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			     -- ID
	F_SyncData_ID INT NULL,						     -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 		     -- 数据类型
	F_SyncSequence	INT NOT NULL,					 -- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),-- 创建时间
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_PromotionSyncCacheDispatcher; -- 促销同步缓存调度表
CREATE TABLE T_PromotionSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				 -- ID
	F_SyncCacheID INT NOT NULL,						 -- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		 -- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),-- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_PromotionSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradePromoting; 		-- 零售单促销表
CREATE TABLE T_RetailTradePromoting
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_TradeID INT NOT NULL,							-- 零售单ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY (F_TradeID),
	FOREIGN KEY (F_TradeID ) REFERENCES t_retailtrade(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradePromotingFlow; 		-- 零售单促销表从表
CREATE TABLE T_RetailTradePromotingFlow
	(
	F_ID INT NOT NULL AUTO_INCREMENT,			
	F_RetailTradePromotingID INT NOT NULL,
	F_PromotionID INT NULL,
	F_ProcessFlow VARCHAR(2048) NOT NULL,			-- 促销计算描述。将来有可能不够长
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),	
	FOREIGN KEY (F_RetailTradePromotingID) REFERENCES T_RetailTradePromoting(F_ID),
	FOREIGN KEY (F_PromotionID) REFERENCES t_promotion(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradePromotingSyncCache; 	 			-- 零售促销单同步缓存表
CREATE TABLE T_RetailTradePromotingSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID)
	-- FOREIGN KEY (F_SyncData_ID ) REFERENCES t_RetailTradePromoting(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradePromotingSyncCacheDispatcher; 		-- 零售促销单同步缓存调度表
CREATE TABLE T_RetailTradePromotingSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_RetailTradePromotingSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommoditySyncCache; 	 			-- 商品同步缓存表
CREATE TABLE T_CommoditySyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncData_ID ) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommoditySyncCacheDispatcher; 		-- 商品同步缓存调度表
CREATE TABLE T_CommoditySyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_CommoditySyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BrandSyncCache; 	 			-- 品牌同步缓存表
CREATE TABLE T_BrandSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID)
	-- FOREIGN KEY (F_SyncData_ID ) REFERENCES t_brand(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BrandSyncCacheDispatcher; 		-- 品牌同步缓存调度表
CREATE TABLE T_BrandSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_BrandSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_ConfigGeneral; 	   			-- 普通配置表
CREATE TABLE T_ConfigGeneral
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(60) NOT NULL,					-- 名称 -- 由于日大类报表名字长度为55 把字段长度更改为60
	F_Value VARCHAR(128) NOT NULL,					-- 值
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_ConfigCacheSize; 	   		-- 缓存对象个数配置表
CREATE TABLE T_ConfigCacheSize
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Name VARCHAR(50) NOT NULL,					-- 名称
	F_Value VARCHAR(20) NOT NULL,					-- 值
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	UNIQUE KEY (F_Name)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_SmallSheetFrame; 	   		-- 小票打印
CREATE TABLE T_SmallSheetFrame
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Logo MEDIUMTEXT NULL,			   					-- 自定义Logo
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	F_CountOfBlankLineAtBottom INT NOT NULL,			 -- 底部空行数
	F_DelimiterToRepeat varchar(1) not null default '-',  -- 分隔线
	PRIMARY KEY (F_ID)	
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_SmallSheetText; 	   		-- 小票内容格式
CREATE TABLE T_SmallSheetText
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Content VARCHAR(100) NOT NULL,				-- 内容
	F_Size Decimal(20,6) NOT NULL,				 	-- 字体大小
	F_Bold INT NOT NULL,				   			-- 字体加粗
	F_Gravity INT NOT NULL,							-- 内容位置
	F_FrameID INT NOT NULL,							-- 小票ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),	 -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_FrameID) REFERENCES T_SmallSheetFrame(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POSSyncCache; 	 	-- POS同步缓存表
CREATE TABLE T_POSSyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,					  		-- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncData_ID) REFERENCES T_POS(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_POSSyncCacheDispatcher; 	-- POS同步缓存调度表
CREATE TABLE T_POSSyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_POSSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_POS(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CategorySyncCache; 	 		-- 类别同步缓存表
CREATE TABLE T_CategorySyncCache
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CategorySyncCacheDispatcher; -- 类别同步缓存调度表
CREATE TABLE T_CategorySyncCacheDispatcher
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_CategorySyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_BarcodesSyncCache; 	 	   -- 条形码同步缓存表
CREATE TABLE T_BarcodesSyncCache						
    (
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncData_ID INT NULL,						    -- 数据的ID。可能为D型同步块，故可NULL
	F_SyncType CHAR(1) NOT NULL,		 			-- 数据类型
	F_SyncSequence	INT NOT NULL,					-- 用于客户端收到N条同步块后，知道按照什么顺序去处理这些同步块
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID)
	)	
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';		

DROP TABLE IF EXISTS T_BarcodesSyncCacheDispatcher; -- 条形码同步缓存调度表
CREATE TABLE T_BarcodesSyncCacheDispatcher
	(
F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_SyncCacheID INT NOT NULL,						-- 同步缓存ID
	F_POS_ID INT NOT NULL,					  		-- POS机ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),    -- 创建时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_SyncCacheID ) REFERENCES T_BarcodesSyncCache(F_ID),
	FOREIGN KEY (F_POS_ID ) REFERENCES t_pos(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_CommodityHistory;   			-- 商品修改历史
CREATE TABLE T_CommodityHistory
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_CommodityID INT NOT NULL, 					-- 商品ID
	F_FieldName Varchar(20) NOT NULL,				-- 字段名称
	F_OldValue Varchar(128) NOT NULL,				-- 字段旧值
	F_NewValue Varchar(64) NOT NULL,				-- 字段新值
	F_StaffID INT NOT NULL,								-- 修改人
	F_BySystem INT DEFAULT 0,						-- 是否系统修改
	F_ShopID INT NULL,
	F_Datetime DATETIME NOT NULL,					-- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_NbrConstant;   			-- 项目常量表
CREATE TABLE T_NbrConstant
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- ID
	F_Scope INT NOT NULL, 							-- 0=全局，1=商品档案，2=采购订单，3=盘点，4=入库，…其它待定义
	F_Key Varchar(50) NOT NULL,						-- 常量名称
	F_Value Varchar(50) NOT NULL,					-- 常量值
	F_Remark Varchar(128) NOT NULL,					-- 备注
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeDailyReportByCategoryParent;
CREATE TABLE T_RetailTradeDailyReportByCategoryParent					-- 商品大类日报表
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  						  -- ID
	F_ShopID INT NOT NULL,													  -- 门店ID
 	F_Datetime DATETIME NOT NULL,					  						  -- 统计日期（惟一组合键：日期+商品大类ID）
 	F_CategoryParentID INT NOT NULL,				   						  -- 商品大类ID
 	F_TotalAmount DECIMAL(20, 6) NOT NULL,			    					  -- 销售总额
 	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CategoryParentID) REFERENCES t_categoryparent(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_CategoryParentID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeDailyReportSummary;			     -- 销售日报汇总表
CREATE TABLE T_RetailTradeDailyReportSummary
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_ShopID INT NOT NULL,													  -- 门店ID
	F_Datetime DATETIME NOT NULL,											  -- 统计日期
	F_TotalNO INT NOT NULL,                				  					  -- 销售笔数
	F_PricePurchase DECIMAL(20,6) NOT NULL,									  -- 总进货价
	F_TotalAmount DECIMAL(20, 6) NOT NULL,	 		  		 				  -- 销售额
	F_AverageAmountOfCustomer  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,	  -- 客单价
	F_TotalGross  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,    				  -- 销售毛利
	F_RatioGrossMargin DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,			  -- 销售毛利率
	F_TopSaleCommodityID INT NULL, 			   						          -- 销售额最高的商品。当天不做生意的话，此项为NULL
	F_TopSaleCommodityNO INT NOT NULL DEFAULT 0,				   			  -- 销售额最高的商品的数量
	F_TopSaleCommodityAmount DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,		  -- 销售额最高的商品的销售额
	F_TopPurchaseCustomerName VARCHAR(30) NOT NULL DEFAULT '',				  -- 购买额最高的客户
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 修改时间

	PRIMARY KEY (F_ID) ,
	FOREIGN KEY (F_TopSaleCommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP TABLE IF EXISTS T_RetailTradeMonthlyReportSummary;			           -- 销售月报汇总表
CREATE TABLE T_RetailTradeMonthlyReportSummary
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_ShopID INT NOT NULL,													  -- 门店ID
	F_Datetime DATETIME NOT NULL,											  -- 统计月份
	F_TotalAmount DECIMAL(20, 6) NOT NULL,	 		  		 				  -- 销售额
	F_TotalGross  DECIMAL(20, 6) NOT NULL DEFAULT 0.000000,    				  -- 销售毛利
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 修改时间

	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradeDailyReportByStaff;
CREATE TABLE T_RetailTradeDailyReportByStaff	-- Staff零售报表
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  -- ID
	F_ShopID INT NOT NULL,							  -- 门店ID
	F_Datetime DATETIME NOT NULL,					  -- 日期
	F_StaffID INT NOT NULL,							  -- 员工ID
	F_NO INT NOT NULL,								  -- 销售总笔数
	F_TotalAmount Decimal(20, 6) NOT NULL,			  -- 销售总额
	F_GrossMargin Decimal(20, 6) NOT NULL,			  -- 销售总毛利
	F_CreateDatetime Datetime NOT NULL DEFAULT now(),			  -- 创建时间
	F_UpdateDatetime Datetime NOT NULL DEFAULT now(),			  -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_StaffID) REFERENCES t_staff(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_StaffID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_RetailTradeDailyReportByCommodity;
CREATE TABLE T_RetailTradeDailyReportByCommodity	-- 商品零售报表
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				  -- ID
	F_ShopID INT NOT NULL,							  -- 门店ID
	F_Datetime DATETIME NOT NULL,					  -- 日期
	F_CommodityID INT NOT NULL,						  -- 商品ID(外键)
	F_NO INT NOT NULL,								  -- 销售数量
	F_TotalPurchasingAmount DECIMAL(20, 6) NOT NULL,  -- 总进货价
	F_TotalAmount DECIMAL(20, 6) NOT NULL,			  -- 销售总额
	F_GrossMargin DECIMAL(20, 6) NOT NULL,			  -- 销售毛利
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(), -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(), -- 修改时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID),
	FOREIGN KEY (F_ShopID) REFERENCES t_shop(F_ID),
	UNIQUE KEY F_Datetime (F_Datetime, F_CommodityID, F_ShopID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	

DROP VIEW IF EXISTS V_Staff_Permission;   			-- 用户, 角色, 权限视图
CREATE VIEW V_Staff_Permission
AS
SELECT s.F_ID AS StaffID, s.F_Name AS StaffName,r.F_ID AS RoleID, r.F_Name AS RoleName, p.F_SP, p.F_Name PermissionName, p.F_Remark FROM t_staff s
LEFT JOIN t_staffrole sr ON sr.F_StaffID = s.F_ID
JOIN t_role r ON sr.F_RoleID = r.F_ID
JOIN t_role_permission ap ON ap.F_RoleID = r.F_ID
JOIN t_permission p ON p.F_ID = ap.F_PermissionID
WHERE s.F_Status = 0;


DROP TABLE IF EXISTS T_WXUser;			     -- WX用户信息
CREATE TABLE T_WXUser
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_Subscribe VARCHAR(5) NOT NULL,										  -- 值为0时，代表此用户没有关注该公众号,为1时已关注
	F_OpenId VARCHAR(50) NOT NULL,                				  			  -- 用户的唯一标识
	F_NickName VARCHAR(100) NULL,	 		  		 				  	      -- 用户昵称
	F_Sex  VARCHAR(5) NULL,	  					                              -- 值为1时是男性，值为2时是女性，值为0时是未知
	F_Language VARCHAR(10) NULL,											  -- 语言
	F_City VARCHAR(20) NULL,			  			                          -- 城市
	F_Province  VARCHAR(10) NULL,    				  						  -- 省份
	F_Country VARCHAR(100) NULL,			   						          -- 国家
	F_HeadImgUrl VARCHAR(200) NULL,				   			 		          -- 用户头像
	F_SubscribeTime VARCHAR(30) NULL,							              -- 用户关注时间，为时间戳
	F_Unionid VARCHAR(30) NULL,								  	  			  -- 只有将公众号绑定到微信开放平台帐号后，才会出现该字段。
	F_Remark VARCHAR(100) NULL,												  -- 对关注用户的备注
	F_GroupId VARCHAR(100) NULL,											  -- 用户所在的分组ID
	F_TagIdList VARCHAR(100) NULL,											  -- 用户被打上的标签ID列表
	F_SubscribeScene VARCHAR(100) NULL,										  -- 返回用户关注的渠道来源
	F_QrScene VARCHAR(30) NULL,												  -- 二维码扫码场景
	F_QrSceneStr VARCHAR(30) NULL,										      -- 二维码扫码场景描述
	-- F_Privilege VARCHAR(100) NULL,				  			 			  -- 特权信息，json 数组，如微信沃卡用户为（chinaunicom）
   
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_MessageItem;	-- 消息数据项
CREATE TABLE T_MessageItem
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_MessageID INT NOT NULL,										 		  -- 外键，索引到T_Message.F_ID
	F_MessageCategoryID INT NOT NULL,                				  		  -- 外键，索引到T_MessageCategory.F_ID
	F_CommodityID INT NOT  NULL,	 		  		 				  	      -- 外键，对滞销商品来说，是T_Commodity.F_ID,即商品ID
	F_CreateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 创建时间
	F_UpdateDatetime DATETIME NOT NULL DEFAULT now(),						  -- 修改时间
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_MessageID) REFERENCES t_message(F_ID),
	FOREIGN KEY (F_MessageCategoryID) REFERENCES t_messagecategory(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';	


DROP TABLE IF EXISTS T_BonusRule;			     -- 积分规则表
CREATE TABLE T_BonusRule
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipCardID INT NOT NULL,										 		  -- 外键，指向T_VipCardID.F_ID
	F_AmountUnit INT NOT NULL,                				  				  -- 消费金额。以分为单位
	F_IncreaseBonus INT NOT NULL,	 		  		 				  	      -- 增加的积分
	F_MaxIncreaseBonus INT NOT NULL,				  						  -- 单次可获取的积分上限
	F_InitIncreaseBonus INT NOT NULL DEFAULT 0,					  			  -- 初始设置积分
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_BonusConsumeHistory;		 -- 积分历史表
CREATE TABLE T_BonusConsumeHistory
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipID INT NOT NULL,										 		  	  -- 外键，指向T_Vip.F_ID
	F_StaffID INT NULL,                				  				  	  	  -- 谁动了积分
	F_Bonus INT NULL,	 		  		 				  	      		 	  -- 积分全量值
	F_AddedBonus INT NULL,				  		 						  	  -- 积分变动值
	F_Remark VARCHAR(48) NULL,					  			   		 	 	  -- 自定义积分消耗记录
	F_CreateDatetime DATETIME NOT NULL,
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES t_vip(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_Coupon;					 -- 一种优惠券表
CREATE TABLE T_Coupon
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_Status INT NOT NULL,										 		  	  -- 状态
	F_Type INT NOT NULL,                				  				  -- 优惠券类型
	F_Bonus INT NOT NULL,	 		  		 				  	      		  -- 需要多少积分兑换
	F_LeastAmount Decimal(20, 6) NOT NULL DEFAULT 0,				  		  -- 起用金额
	F_ReduceAmount Decimal(20, 6) NOT NULL,					  				  -- 减免金额
	F_Discount Decimal(20, 6) NOT NULL,					  				 	  -- 打折额度
	F_Title VARCHAR(9) NOT NULL,					  						  -- 名称
	F_Color VARCHAR(16) NOT NULL,					  			  			  -- 颜色
	F_Description VARCHAR(1024) NOT NULL,					  				  -- 使用说明
	F_PersonalLimit INT NOT NULL,					  				 		  -- 每人可领券的数量限制
	F_WeekDayAvailable INT NULL,					  				  					  -- 限制使用星期
	F_BeginTime VARCHAR(8) NOT NULL,					  					  -- 当天起始时间
	F_EndTime VARCHAR(8) NOT NULL,					  					 	  -- 当天结束时间
	F_BeginDateTime DATETIME NOT NULL,					  					  -- 起用日期时间
	F_EndDateTime DATETIME NOT NULL,					  					  -- 结束日期时间
	F_Quantity INT NOT NULL,					  							  -- 库存数量
	F_RemainingQuantity INT NOT NULL,					  					  -- 当前剩余库存数量
    F_Scope INT NOT NULL,					  								  -- 参与的商品的范围
	
	PRIMARY KEY (F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_CouponCode;		 -- 一张优惠券表
CREATE TABLE T_CouponCode
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_VipID INT NOT NULL,										 		  	  -- 外键，指向T_Vip.F_ID
	F_CouponID INT NOT NULL,                				  				  -- 外键，指向T_Coupon.F_ID
	F_Status INT NOT NULL,	 		  		 				  	      		  -- 状态
	F_SN VARCHAR(15) NOT NULL,				  		 						  -- CouponCode
	F_CreateDatetime DATETIME NOT NULL,					  				 	  -- 创建时间
	F_UsedDatetime DATETIME NULL,					  			 		 	  -- 核销时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_VipID) REFERENCES t_vip(F_ID),
	FOREIGN KEY (F_CouponID) REFERENCES t_coupon(F_ID),
	UNIQUE KEY F_SN (F_SN)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_CouponScope;		 -- 优惠券作用范围表
CREATE TABLE T_CouponScope
	(
	F_ID INT NOT NULL AUTO_INCREMENT,                   					  -- ID
	F_CouponID INT NOT NULL,                				  				  -- 外键，指向T_Coupon.F_ID
	F_CommodityID INT NOT NULL,	 		  		 				  	      	  -- 商品ID
	F_CommodityName VARCHAR(32) NOT NULL,				  		 			  -- 商品名称
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_CouponID) REFERENCES t_coupon(F_ID),
	FOREIGN KEY (F_CommodityID) REFERENCES t_commodity(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';

DROP TABLE IF EXISTS T_RetailTradeCoupon;			-- 零售单使用的优惠券
CREATE TABLE T_RetailTradeCoupon
	(
	F_ID INT NOT NULL AUTO_INCREMENT,				-- 主键ID
	F_RetailTradeID INT NOT NULL,					-- 零售单外键
	F_CouponCodeID INT NOT NULL,					-- 券外键
	F_SyncDatetime DATETIME NOT NULL,				-- 同步时间
	
	PRIMARY KEY (F_ID),
	FOREIGN KEY (F_RetailTradeID) REFERENCES t_retailtrade(F_ID),
 	FOREIGN KEY (F_CouponCodeID) REFERENCES t_couponcode(F_ID)
	)
ENGINE=InnoDB
DEFAULT CHARACTER SET='utf8' COLLATE='utf8_unicode_ci';