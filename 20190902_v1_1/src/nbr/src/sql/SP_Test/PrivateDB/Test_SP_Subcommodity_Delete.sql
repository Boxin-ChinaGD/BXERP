SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 49;
SET @iSubCommodityID = 11;
SET @iPrice = 11;

INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_Price)
VALUES (@iCommodityID, @iSubCommodityID, @iPrice);

CALL SP_Subcommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';