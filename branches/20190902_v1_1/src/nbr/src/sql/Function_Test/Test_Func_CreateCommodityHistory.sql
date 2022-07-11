SET @InValue = -1;
SET @InValueString = '$';
SET @InNO = -100000000;
SET @iShopID = 2;

-- CASE1:修改商品名称历史
select Func_CreateCommodityHistory(51, '商品名称', @InValueString, @InValueString, @InValue, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '商品名称') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '商品名称';


-- CASE2:修改条形码历史
select Func_CreateCommodityHistory(51, @InValueString, '11111111', @InValueString, @InValue, @InValue, -100000000, @InNO, 3, '3333333333333', @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '条形码') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '条形码';


-- CASE3:修改商品规格历史
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, '个', @InValue, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '规格') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '规格';


-- CASE4:修改包装单位历史
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, 5, @InValue, -100000000, @InNO, 3, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '包装单位') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '包装单位';


-- CASE5:修改商品类别历史
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, 5, -100000000, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '商品类别') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '商品类别';


-- CASE6:修改零售价历史
SET @OldValue = 999.9991111111111;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价') = 1 AND @OldValue = 1000.00, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价';


-- CASE7:修改库存历史
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, -100000000, 999, 1, @InValueString, @iShopID); -- 0
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '库存') = 1, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '库存';


-- CASE8:修改零售价历史2
SET @OldValue = 123.456789;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价') = 1 AND @OldValue = 123.46, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价';


-- CASE8:修改零售价历史3
SET @OldValue = 654.321;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价') = 1 AND @OldValue = 654.32, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价';


-- CASE8:修改零售价历史4
SET @OldValue = 100;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价') = 1 AND @OldValue = 100.00, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价';


-- CASE8:修改零售价历史5
SET @OldValue = 100.00000000000;
select Func_CreateCommodityHistory(51, @InValueString, @InValueString, @InValueString, @InValue, @InValue, @OldValue, @InNO, 1, @InValueString, @iShopID); -- 0
SELECT F_OldValue INTO @OldValue FROM t_commodityhistory WHERE F_CommodityID = 51;
SELECT IF((SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价') = 1 AND @OldValue = 100.00, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = 51 AND F_FieldName= '零售价';