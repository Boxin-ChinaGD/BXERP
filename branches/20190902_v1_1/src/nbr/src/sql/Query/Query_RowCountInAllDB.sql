-- ��Query��Ŀ�ģ�
-- ��TEST SP֮ǰ����ͳ������DB�����б����������������ļ�1.txt�С�
-- ��TEST SP֮������ͳ������DB�����б����������������ļ�2.txt�С�
-- ��svn diff�Ĺ��ܱȽ�1.txt��2.txt�Ĳ��졣������ڲ��죬����TEST SP��DB���������ݣ�����û��ɾ������ɽϴ��������Ⱦ��
USE nbr_bx;
SELECT count(1) AS 't_bxconfiggeneral' FROM nbr_bx.t_bxconfiggeneral;
SELECT count(1) AS 't_bxstaff' FROM nbr_bx.t_bxstaff;
SELECT count(1) AS 't_company' FROM nbr_bx.t_company;
SELECT count(1) AS 't_refcommodityhub' FROM nbr_bx.t_refcommodityhub;

USE nbr;
SELECT count(1) AS 't_barcodes' FROM t_barcodes;
SELECT count(1) AS 't_barcodessynccache' FROM t_barcodessynccache;
SELECT count(1) AS 't_barcodessynccachedispatcher' FROM t_barcodessynccachedispatcher; 
SELECT count(1) AS 't_brand' FROM t_brand;
SELECT count(1) AS 't_brandsynccache' FROM t_brandsynccache; 
SELECT count(1) AS 't_brandsynccachedispatcher' FROM t_brandsynccachedispatcher;
SELECT count(1) AS 't_category' FROM t_category;
SELECT count(1) AS 't_categoryparent' FROM t_categoryparent;
SELECT count(1) AS 't_categorysynccache' FROM t_categorysynccache;
SELECT count(1) AS 't_categorysynccachedispatcher' FROM t_categorysynccachedispatcher;
SELECT count(1) AS 't_commodity' FROM t_commodity;
SELECT count(1) AS 't_commodityhistory' FROM t_commodityhistory;
SELECT count(1) AS 't_commodityproperty' FROM t_commodityproperty;
SELECT count(1) AS 't_commoditysynccache' FROM t_commoditysynccache;
SELECT count(1) AS 't_commoditysynccachedispatcher' FROM t_commoditysynccachedispatcher;
SELECT count(1) AS 't_configcachesize' FROM t_configcachesize;
SELECT count(1) AS 't_configgeneral' FROM t_configgeneral;
SELECT count(1) AS 't_configgeneralsynccache' FROM t_configgeneralsynccache;
SELECT count(1) AS 't_configgeneralsynccachedispatcher' FROM t_configgeneralsynccachedispatcher;
SELECT count(1) AS 't_department' FROM t_department;
SELECT count(1) AS 't_errorcode' FROM t_errorcode;
SELECT count(1) AS 't_inventorycommodity' FROM t_inventorycommodity;
SELECT count(1) AS 't_inventorysheet' FROM t_inventorysheet;
SELECT count(1) AS 't_message' FROM t_message;
SELECT count(1) AS 't_messagecategory' FROM t_messagecategory;
SELECT count(1) AS 't_messagehandlersetting' FROM t_messagehandlersetting;
SELECT count(1) AS 't_messageitem' FROM t_messageitem;
SELECT count(1) AS 't_nbrconstant' FROM t_nbrconstant;
SELECT count(1) AS 't_packageunit' FROM t_packageunit;
SELECT count(1) AS 't_permission' FROM t_permission;
SELECT count(1) AS 't_pos' FROM t_pos;
SELECT count(1) AS 't_possynccache' FROM t_possynccache;
SELECT count(1) AS 't_possynccachedispatcher' FROM t_possynccachedispatcher;
SELECT count(1) AS 't_promotion' FROM t_promotion;
SELECT count(1) AS 't_promotionscope' FROM t_promotionscope;
SELECT count(1) AS 't_promotionsynccache' FROM t_promotionsynccache;
SELECT count(1) AS 't_promotionsynccachedispatcher' FROM t_promotionsynccachedispatcher;
SELECT count(1) AS 't_provider' FROM t_provider;
SELECT count(1) AS 't_providercommodity' FROM t_providercommodity;
SELECT count(1) AS 't_providerdistrict' FROM t_providerdistrict;
SELECT count(1) AS 't_purchasingorder' FROM t_purchasingorder;
SELECT count(1) AS 't_purchasingordercommodity' FROM t_purchasingordercommodity;
SELECT count(1) AS 't_retailtrade' FROM t_retailtrade;
SELECT count(1) AS 't_retailtradeaggregation' FROM t_retailtradeaggregation;
SELECT count(1) AS 't_retailtradecommodity' FROM t_retailtradecommodity;
SELECT count(1) AS 't_retailtradecommoditysource' FROM t_retailtradecommoditysource;
SELECT count(1) AS 't_retailtradedailyreportbycategoryparent' FROM t_retailtradedailyreportbycategoryparent;
SELECT count(1) AS 't_retailtradedailyreportbycommodity' FROM t_retailtradedailyreportbycommodity;
SELECT count(1) AS 't_retailtradedailyreportbystaff' FROM t_retailtradedailyreportbystaff;
SELECT count(1) AS 't_retailtradedailyreportsummary' FROM t_retailtradedailyreportsummary;
SELECT count(1) AS 't_retailtrademonthlyreportsummary' FROM t_retailtrademonthlyreportsummary;
SELECT count(1) AS 't_retailtradepromoting' FROM t_retailtradepromoting;
SELECT count(1) AS 't_retailtradepromotingflow' FROM t_retailtradepromotingflow;
SELECT count(1) AS 't_retailtradepromotingsynccache' FROM t_retailtradepromotingsynccache;
SELECT count(1) AS 't_retailtradepromotingsynccachedispatcher' FROM t_retailtradepromotingsynccachedispatcher;
SELECT count(1) AS 't_returncommoditysheet' FROM t_returncommoditysheet;
SELECT count(1) AS 't_returncommoditysheetcommodity' FROM t_returncommoditysheetcommodity;
SELECT count(1) AS 't_returnretailtradecommoditydestination' FROM t_returnretailtradecommoditydestination;
SELECT count(1) AS 't_role' FROM t_role;
SELECT count(1) AS 't_role_permission' FROM t_role_permission;
SELECT count(1) AS 't_shop' FROM t_shop;
SELECT count(1) AS 't_shopdistrict' FROM t_shopdistrict;
SELECT count(1) AS 't_smallsheetframe' FROM t_smallsheetframe;
SELECT count(1) AS 't_smallsheetsynccache' FROM t_smallsheetsynccache;
SELECT count(1) AS 't_smallsheetsynccachedispatcher' FROM t_smallsheetsynccachedispatcher;
SELECT count(1) AS 't_smallsheettext' FROM t_smallsheettext;
SELECT count(1) AS 't_smdomain' FROM t_smdomain;
SELECT count(1) AS 't_smforward' FROM t_smforward;
SELECT count(1) AS 't_smnode' FROM t_smnode;
SELECT count(1) AS 't_staff' FROM t_staff;
SELECT count(1) AS 't_staffrole' FROM t_staffrole;
SELECT count(1) AS 't_subcommodity' FROM t_subcommodity;
SELECT count(1) AS 't_tmp' FROM t_tmp;
SELECT count(1) AS 't_vip' FROM t_vip;
SELECT count(1) AS 't_vip_category' FROM t_vip_category;
SELECT count(1) AS 't_vipcategorysynccache' FROM t_vipcategorysynccache;
SELECT count(1) AS 't_vipcategorysynccachedispatcher' FROM t_vipcategorysynccachedispatcher;
SELECT count(1) AS 't_vippointhistory' FROM t_vippointhistory;
SELECT count(1) AS 't_vipsynccache' FROM t_vipsynccache;
SELECT count(1) AS 't_vipsynccachedispatcher' FROM t_vipsynccachedispatcher;
SELECT count(1) AS 't_warehouse' FROM t_warehouse;
SELECT count(1) AS 't_warehousing' FROM t_warehousing;
SELECT count(1) AS 't_warehousingcommodity' FROM t_warehousingcommodity;
SELECT count(1) AS 't_wxuser' FROM t_wxuser;

-- ���˽��DB nbr�ı�����ͼ��Ŀ���ޱ��
SET @nbr_table_no = (SELECT COUNT(1) FROM information_schema.TABLES  WHERE table_schema = 'nbr');
SELECT IF(@nbr_table_no = 85,'nbr������ͼ��Ŀһ��',concat('nbr�ı�����ͼ��ĿӦ����', @nbr_table_no, '�����������������Щ������ͼ����')) AS TableNO;

-- ��鹫��DB nbr_bx�ı�����ͼ��Ŀ���ޱ��
SET @nbr_table_no = (SELECT COUNT(1) FROM information_schema.TABLES  WHERE table_schema = 'nbr_bx');
SELECT IF(@nbr_table_no = 4,'nbr_bx������ͼ��Ŀһ��',concat('nbr_bx�ı�����ͼ��ĿӦ����', @nbr_table_no, '�����������������Щ������ͼ����')) AS TableNO;