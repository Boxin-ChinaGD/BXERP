
-- ��˾Ӫҵִ�յ�Ŀ¼ �ĸ�·����Ҫͬ����pom.xml��baseAction��CompanyBusinessLicensePictureDir
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyBusinessLicensePictureDir', 'D:/nbr/pic/common_db/license/');	
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyBusinessLicensePictureVolumeMax', '102400');	-- ��˾Ӫҵִ�յĴ�С����
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityNOStart', '-1'); -- ��Ʒ������Ĭ���ڳ�ֵ�������ڳ�û�����������������û�и���Ʒ
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityPurchasingPriceStart', '-1.000000'); -- ��Ʒ�ɹ��۵�Ĭ���ڳ�ֵ�������ڳ�û�й��ɹ������������û�и���Ʒ
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CommodityLogoDir', 'D:/nbr/pic/private_db/');	-- ��ƷͼƬ��Ŀ¼�����沿�ִ���T_Commdity.F_Picture�У�ʾ����nbr1/1.jpg
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyAPICertDir', 'D:/nbr/apicert/');	-- API֤��ĵ�Ŀ¼������ʾ����nbr1.p12
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyAPICertVolumeMax', '10240');	-- API֤��Ĵ�С����
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_SmallSheetNumber', '10');  -- СƱ��ʽ���������
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_SmallSheetLogoVolume', '163840'); -- СƱ��ʽͼƬ�Ĵ�С���ֵ
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestCountIn1Day', '60000'); -- һ������������������������������ֵ��log4j�ᷢ�����ʼ�
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestCountIn1Min', '600'); -- һ��������������������������������ֵ��log4j�ᷢ�����ʼ�
-- ����������Ĵ����������ֵ��log4j�ᷢ�����ʼ��������������������ÿ�γ���һ���������󣬻��ظ��������ʼ���������ҵ���߼����Ա������ľ����ʼ���
-- ���磬MaxRequestCountIn1Min����Ϊ600������Ϊ600X0.1=60����1�����ڵķ��ʴ�������600ʱ���ᷢ�;����ʼ�������660ʱ���ٷ�һ�Ρ�����720ʱ���ٷ�һ�Ρ�
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxRequestIncrementPercent', '0.1'); 
-- ��˾Logo��Ŀ¼ �ĸ�·����Ҫͬ����pom.xml��baseAction��CompanyLogoDir
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyLogoDir', 'D:/nbr/pic/common_db/logo/');
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('CompanyLogoVolumeMax', '102400'); -- ��˾logo�Ĵ�С����
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MaxLoginCountIn1Day', '50'); -- һ���û�һ������Ե�¼������������������ᱻ��Ϊ�Ǻڿ��ڱ������룬�ᷢ�����ʼ�����
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('MAX_IMPORT_FILE_SIZE', '5242880'); -- �ļ��ϴ����ƴ�С 5 * 1024 * 1024
INSERT INTO t_bxconfiggeneral (F_Name, F_Value) VALUES ('EXTRA_DISK_SPACE_SIZE', '104807600'); -- ���̿ռ�ʣ���С 100 * 1024 * 1024
