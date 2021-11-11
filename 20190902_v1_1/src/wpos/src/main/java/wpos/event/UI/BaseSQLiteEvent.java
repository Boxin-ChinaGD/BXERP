package wpos.event.UI;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.BaseEvent;
@Component("baseSQLiteEvent")
@Scope("prototype")
public class BaseSQLiteEvent extends BaseEvent {
    private static int INDEX = 0;

    public BaseSQLiteEvent(){

    }

    public enum EnumSQLiteEventType {
        ESET_Commodity_CreateAsync("ESET_Commodity_CreateAsync", INDEX++), //
        ESET_Commodity_CreateNAsync("ESET_Commodity_CreateNAsync", INDEX++), //
        ESET_Commodity_UpdateAsync("ESET_Commodity_UpdateAsync", INDEX++), //
        ESET_Commodity_Retrieve1Async("ESET_Commodity_Retrieve1Async", INDEX++), //
        ESET_Commodity_RetrieveNAsync("ESET_Commodity_RetrieveNAsync", INDEX++),
        ESET_Commodity_RefreshByServerDataAsync_Done("ESET_Commodity_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Commodity_RefreshByServerDataAsyncC_Done("ESET_Commodity_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_Commodity_UpdateNAsync("ESET_Commodity_UpdateNAsync", INDEX++),
        ESET_RetailTrade_CreateAsync("ESET_RetailTrade_CreateAsync", INDEX++),
        ESET_RetailTrade_CreateSync("ESET_RetailTrade_CreateSync", INDEX++),
        ESET_RetailTrade_UpdateAsync("ESET_RetailTrade_UpdateAsync", INDEX++),
        ESET_RetailTrade_Retrieve1Async("ESET_RetailTrade_Retrieve1Async", INDEX++),
        ESET_RetailTrade_RetrieveNAsync("ESET_RetailTrade_RetrieveNAsync", INDEX++),
        ESET_RetailTrade_RetrieveNSync("ESET_RetailTrade_RetrieveNSync", INDEX++),
        /**
         * 这里的RN是为了上传残余的零售单的
         */
        ESET_RetailTrade_RetrieveNAsyncForUpload("ESET_RetailTrade_RetrieveNAsyncForUpload", INDEX++),
        ESET_RetailTrade_DeleteAsync("ESET_RetailTrade_DeleteAsync", INDEX++),
        ESET_RetailTrade_CreateNAsync("ESET_RetailTrade_CreateNAsync", INDEX++),
        ESET_RetailTradeCommodity_DeleteAsync("ESET_RetailTradeCommodity_DeleteAsync", INDEX++),
        ESET_RetailTradeCommodity_CreateAsync("ESET_RetailTradeCommodity_CreateAsync", INDEX++),
        ESET_RetailTradeCommodity_Retrieve1Async("ESET_RetailTradeCommodity_Retrieve1Async", INDEX++),
        ESET_SmallSheetFrame_CreateAsync("ESET_SmallSheetFrame_CreateAsync", INDEX++), //
        ESET_SmallSheetFrame_CreateNAsync("ESET_SmallSheetFrame_CreateNAsync", INDEX++), //
        ESET_SmallSheetFrame_UpdateAsync("ESET_SmallSheetFrame_UpdateAsync", INDEX++), //
        ESET_SmallSheetFrame_DeleteAsync("ESET_SmallSheetFrame_DeleteAsync", INDEX++),
        ESET_SmallSheetFrame_Retrieve1Async("ESET_SmallSheetFrame_Retrieve1Async", INDEX++), //
        ESET_SmallSheetFrame_RetrieveNAsync("ESET_SmallSheetFrame_RetrieveNAsync", INDEX++),
        ESET_SmallSheetText_CreateAsync("ESET_SmallSheetText_CreateAsync", INDEX++), //
        ESET_SmallSheetText_CreateNAsync("ESET_SmallSheetText_CreateNAsync", INDEX++), //
        ESET_SmallSheetText_UpdateAsync("ESET_SmallSheetText_UpdateAsync", INDEX++), //
        ESET_SmallSheetText_Retrieve1Async("ESET_SmallSheetText_Retrieve1Async", INDEX++), //
        ESET_SmallSheetText_RetrieveNAsync("ESET_SmallSheetText_RetrieveNAsync", INDEX++),
        /**
         * 创建小票格式主从表完成。下一步是触发网络请求
         */
        ESET_SmallSheet_CreateMasterSlaveAsync_Done("ESET_SmallSheet_CreateMasterSlaveAsync_Done", INDEX++),
        /**
         * 修改小票格式主从表,设置SyncDatetime和SyncType。下一步是触发网络请求
         */
        ESET_SmallSheet_UpdateMasterSlaveAsync("ESET_SmallSheet_UpdateMasterSlaveAsync", INDEX++),
        /**
         * 本地SQLite已经成功同步了服务器返回的同步数据块(C型)
         */
        ESET_SmallSheet_CreateReplacerAsync_Done("ESET_SmallSheet_CreateReplacerAsync_Done", INDEX++),
        /**
         * 本地SQLite已经全部成功同步了服务器返回的N个同步数据块
         */
        ESET_SmallSheet_CreateReplacerNAsync_Done("ESET_SmallSheet_CreateReplacerNAsync_Done", INDEX++),
        /**
         * 修改小票格式主从表,设置SyncDatetime和SyncType。下一步是触发网络请求
         */
        ESET_SmallSheet_UpdateMasterSlaveAsync_Done("ESET_SmallSheet_UpdateMasterSlaveAsync_Done", INDEX++),
        /**
         * 修改了小票格式，也上传到服务器，通过服务器返回的SyncDatetime设置本地SQLite的SyncDatetime
         */
        ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done("ESET_SmallSheet_UpdateMasterSlaveByServerDataAsync_Done", INDEX++),
        /**
         * 主表和从表都已经设置SyncDatetime和SyncType成功，触发网络同步请求
         */
        ESET_SmallSheet_DeleteMasterSlaveAsync_Done("ESET_SmallSheet_DeleteMasterSlaveAsync_Done", INDEX++),
        /**
         * 在删除本地的小票格式之前需要发送请求先删除服务器的，确保服务器的删除成功才能删除本地的，
         * 但是在之前需要在本地设置好（SyncType，SyncDatetime），那些小票格式是删除的，这样的话在服务器删除失败的时候，可以重新删除
         */
        ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done("ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done", INDEX++),
        /**
         * 主从表的数据都已经根据服务器返回的数据(CUD)同步成功
         */
        ESET_SmallSheet_RefreshByServerDataAsync_Done("ESET_SmallSheet_RefreshByServerDataAsync_Done", INDEX++),
        /**
         * call普通Action，将服务器返回的所有对象同步到本地
         */
        ESET_SmallSheet_RefreshByServerDataAsyncC_Done("ESET_SmallSheet_RefreshByServerDataAsyncC_Done", INDEX++),
        /**
         * 创建零售单主从表完成。下一步是触发网络请求
         */
        ESET_RetailTrade_CreateMasterSlaveAsync_Done("ESET_RetailTrade_CreateMasterSlaveAsync_Done", INDEX++),
        /**
         * 本地SQLite已经成功同步了服务器返回的同步数据块(C型)
         */
        ESET_RetailTrade_CreateReplacerAsync_Done("ESET_RetailTrade_CreateReplacerAsync_Done", INDEX++),
        /**
         * 本地SQLite已经成功同步了所有服务器返回的同步数据块(C型)
         */
        ESET_RetailTrade_CreateNReplacerAsync_Done("ESET_RetailTrade_CreateNReplacerAsync_Done", INDEX++),
        /**
         * 本地SQLite已经全部成功同步了服务器返回的N个同步数据块
         */
        ESET_RetailTrade_CreateReplacerNAsync_Done("ESET_RetailTrade_CreateReplacerNAsync_Done", INDEX++),
        /**
         * 主表和从表都已经删除成功，触发网络同步请求
         */
        ESET_RetailTrade_DeleteMAsterSlaveAsync_Done("ESET_RetailTrade_DeleteMasterSlaveAsync_Done", INDEX++),
        /**
         * 主从表的数据都已经根据服务器返回的数据(CUD)同步成功
         */
        ESET_RetailTrade_RefreshByServerDataAsync_Done("ESET_RetailTrade_RefreshByServerDataAsync_Done", INDEX++),
        /**
         * 主从表的数据都全部创建在本地SQLite中。TODO 将来要重命名，因为它已经不涉及到网络操作
         */
        ESET_RetailTrade_RefreshMasterSlaveAsyncSQLite_Done("ESET_RetailTrade_RefreshMasterSlaveAsyncSQLite_Done", INDEX++),
        ESET_RetailTrade_updateNAsync("ESET_RetailTrade_updateNAsync", INDEX++),
        ESET_RetailTradeCommodity_RetrieveNAsync("ESET_RetailTradeCommodity_RetrieveNAsync", INDEX++),
        ESET_RetailTradeCommodity_CreateNAsync("ESET_RetailTradeCommodity_CreateNAsync", INDEX++),
        ESET_Barcodes_CreateAsync("ESET_Barcodes_CreateAsync", INDEX++),
        ESET_Barcodes_UpdateAsync("ESET_Barcodes_UpdateAsync", INDEX++),
        ESET_Barcodes_RetrieveNAsync("ESET_Barcodes_RetrieveNAsync", INDEX++),
        ESET_Barcodes_DeleteAsync("ESET_Barcodes_DeleteAsync", INDEX++),
        ESET_Barcodes_Retrieve1Async("ESET_Barcodes_Retrieve1Async", INDEX++),
        ESET_Barcodes_CreateNAsync("ESET_Barcodes_CreateNAsync", INDEX++),
        ESET_Vip_CreateAsync("ESET_Vip_CreateAsync", INDEX++),
        ESET_Vip_UpdateAsync("ESET_Vip_UpdateAsync", INDEX++),
        ESET_Vip_RetrieveNAsync("ESET_Vip_RetrieveNAsync", INDEX++),
        ESET_Vip_DeleteAsync("ESET_Vip_DeleteAsync", INDEX++),
        ESET_Vip_Retrieve1Async("ESET_Vip_Retrieve1Async", INDEX++),
        ESET_Vip_CreateNAsync("ESET_Vip_CreateNAsync", INDEX++),
        ESET_Vip_CreateReplacerAsync_Done("ESET_Vip_CreateReplacerAsync_Done", INDEX++),
        ESET_Vip_UpdateByServerDataAsync_Done("ESET_Vip_UpdateByServerDataAsync_Done", INDEX++),
        ESET_Vip_UpdateByServerDataNAsync_Done("ESET_Vip_UpdateByServerDataNAsync_Done", INDEX++),
        ESET_Vip_RefreshByServerDataAsync_Done("ESET_Vip_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Vip_BeforeDeleteAsync_Done("ESET_Vip_BeforeDeleteAsync_Done", INDEX++),
        ESET_Pos_CreateAsync("ESET_Pos_CreateAsync", INDEX++),
        ESET_Pos_UpdateAsync("ESET_Pos_UpdateAsync", INDEX++),
        ESET_Pos_RetrieveNAsync("ESET_Pos_RetrieveNAsync", INDEX++),
        ESET_Pos_DeleteAsync("ESET_Pos_DeleteAsync", INDEX++),
        ESET_Pos_Retrieve1Async("ESET_Pos_Retrieve1Async", INDEX++),
        ESET_Pos_CreateNAsync("ESET_Pos_CreateNAsync", INDEX++),
        ESET_Pos_CreateReplacerAsync("ESET_Pos_CreateReplacerAsync", INDEX++),
        ESET_Pos_RefreshByServerDataAsync_Done("ESET_Pos_RefreshByServerDataAsync_Done", INDEX++),
        ESET_RetailTradeAggregation_CreateAsync("ESET_RetailTradeAggregation_CreateAsync", INDEX++),
        ESET_RetailTradeAggregation_RetrieveNAsync("ESET_RetailTradeAggregation_RetrieveNAsync", INDEX++),
        ESET_RetailTradeAggregation_DeleteAsync("ESET_RetailTradeAggregation_DeleteAsync", INDEX++),
        ESET_RetailTradeAggregation_UpdateAsync("ESET_RetailTradeAggregation_UpdateAsync", INDEX++),
        //        ESET_RetailTradeAggregation_CreateReplacerAsync_Done("ESET_RetailTradeAggregation_CreateReplacerAsync_Done", INDEX++), //现在服务器返回的收银汇总不再插入SQLite，所以不需要此EventType。用ESET_RetailTradeAggregation_CreateAsync_Done取代
        /**
         * 收银汇总上传到服务器成功后，会删除本地SQLite的收银汇总。本Enum代表删除本地SQLite的收银汇总
         */
        ESET_RetailTradeAggregation_CreateAsync_HttpDone("ESET_RetailTradeAggregation_CreateAsync_HttpDone", INDEX++),
        ESET_Staff_RefreshByServerDataAsync_Done("ESET_Staff_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Brand_RefreshByServerDataAsync_Done("ESET_Brand_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Brand_RefreshByServerDataAsyncC_Done("ESET_Brand_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_Brand_CreateAsync("ESET_Brand_CreateAsync", INDEX++),
        ESET_Brand_DeleteAsync("ESET_Brand_DeleteAsync", INDEX++),
        ESET_Brand_UpdateAsync("ESET_Brand_UpdateAsync", INDEX++),
        ESET_Brand_CreateNAsync("ESET_Brand_CreateNAsync", INDEX++),
        ESET_CommodityCategory_RefreshByServerDataAsync_Done("ESET_CommodityCategory_RefreshByServerDataAsync_Done", INDEX++),
        ESET_CommodityCategory_RefreshByServerDataAsyncC_Done("ESET_CommodityCategory_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_CommodityCategory_CreateAsync("ESET_CommodityCategory_CreateAsync", INDEX++),
        ESET_CommodityCategory_DeleteAsync("ESET_CommodityCategory_DeleteAsync", INDEX++),
        ESET_CommodityCategory_UpdateAsync("ESET_CommodityCategory_UpdateAsync", INDEX++),
        ESET_CommodityCategory_CreateNAsync("ESET_CommodityCategory_CreateNAsync", INDEX++),
        ESET_CommodityCategory_RetrieveNAsync("ESET_CommodityCategory_RetrieveNAsync", INDEX++),
        ESET_Barcodes_RefreshByServerDataAsync_Done("ESET_Barcodes_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Barcodes_RefreshByServerDataAsyncC_Done("ESET_Barcodes_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_PackageUnit_RefreshByServerDataAsync_Done("ESET_PackageUnit_RefreshByServerDataAsync_Done", INDEX++),
        ESET_ConfigCacheSize_RefreshByServerDataAsync_Done("ESET_ConfigCacheSize_RefreshByServerDataAsync_Done", INDEX++),
        ESET_ConfigCacheSize_RefreshByServerDataAsyncC_Done("ESET_ConfigCacheSize_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_VipCategory_RefreshByServerDataAsync_Done("ESET_VipCategory_RefreshByServerDataAsync_Done", INDEX++),
        ESET_VipCategory_RefreshByServerDataAsyncC_Done("ESET_VipCategory_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_VipCategory_CreateAsync("ESET_VipCategory_CreateAsync", INDEX++),
        ESET_VipCategory_DeleteAsync("ESET_VipCategory_DeleteAsync", INDEX++),
        ESET_VipCategory_UpdateAsync("ESET_VipCategory_UpdateAsync", INDEX++),
        ESET_VipCategory_CreateNAsync("ESET_VipCategory_CreateNAsync", INDEX++),
        ESET_VipCategory_RetrieveNAsync("ESET_VipCategory_RetrieveNAsync", INDEX++),
        ESET_ConfigGeneral_UpdateAsync("ESET_ConfigGeneral_UpdateAsync", INDEX++),
        ESET_ConfigGeneral_UpdateByServerDataAsync("ESET_ConfigGeneral_UpdateByServerDataAsync", INDEX++),
        ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done("ESET_ConfigGeneral_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done("ESET_BXConfigGeneral_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_Promotion_RefreshByServerDataAsync_Done("ESET_Promotion_RefreshByServerDataAsync_Done", INDEX++),
        ESET_Promotion_CreateNAsync("ESET_Promotion_CreateNAsync", INDEX++),
        ESET_Promotion_CreateAsync("ESET_Promotion_CreateAsync", INDEX++),
        ESET_Promotion_DeleteAsync("ESET_Promotion_DeleteAsync", INDEX++),
        ESET_Promotion_UpdateAsync("ESET_Promotion_UpdateAsync", INDEX++),
        ESET_Promotion_RetrieveNAsync("ESET_Promotion_RetrieveNAsync", INDEX++),
        ESET_Promotion_RefreshByServerDataAsyncC_Done("ESET_Promotion_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_PromotionScope_CreateAsync("ESET_PromotionScope_CreateAsync", INDEX++),
        ESET_PromotionScope_DeleteAsync("ESET_PromotionScope_DeleteAsync", INDEX++),
        ESET_PromotionScope_Retrieve1Async("ESET_PromotionScope_Retrieve1Async", INDEX++),
        ESET_PromotionScope_CreateNAsync("ESET_PromotionScope_CreateNAsync", INDEX++),
        ESET_PromotionScope_RetrieveNAsync("ESET_PromotionScope_RetrieveNAsync", INDEX++),
        ESET_RetailTradePromoting_CreateReplacerAsync_Done("ESET_RetailTradePromoting_CreateReplacerAsync_Done", INDEX++),
        ESET_RetailTradePromotingFlow_CreateReplacerAsync_Done("ESET_RetailTradePromotingFlow_CreateReplacerAsync_Done", INDEX++),
        /**
         * 创建零售单计算过程主从表完成。下一步是触发网络请求
         */
        ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done("ESET_RetailTradePromoting_CreateMasterSlaveAsync_Done", INDEX++),
        /**
         * 主从表的数据都全部创建在本地SQLite中
         */
        ESET_RetailTradePromoting_RefreshMasterSlaveAsyncSQLite_Done("ESET_RetailTradePromoting_RefreshMasterSlaveAsyncSQLite_Done", INDEX++),
        /**
         * 异步根据TradeID查找本地RetailTradePromoting
         */
        ESET_RetailTradePromoting_Retrieve1ByTradeID("ESET_RetailTradePromoting_Retrieve1ByTradeID", INDEX++),
        /**
         * 异步根据ID查找本地RetailTradePromoting
         */
        ESET_RetailTradePromoting_RetrieveNByIDs("ESET_RetailTradePromoting_RetrieveNByIDs", INDEX++),
        ESET_RetailTradePromoting_CreateReplacerNAsync_Done("ESET_RetailTradePromoting_CreateReplacerNAsync_Done", INDEX++),
        ESET_RetailTradePromoting_RetrieveNAsync("ESET_RetailTradePromoting_RetrieveNAsync", INDEX++),
        ESET_RetailTradePromoting_UpdateNAsync("ESET_RetailTradePromoting_UpdateNAsync", INDEX++),
        ESET_Company_RetrieveNAsync("ESET_Company_RetrieveNAsync", INDEX++),
        ESET_Company_CreateAsync("ESET_Company_CreateAsync", INDEX++),
        ESET_RetailTrade_RefreshByServerDataAsyncC_Done("ESET_RetailTrade_RefreshByServerDataAsyncC_Done", INDEX++),
        ESET_Staff_RetrieveNAsync("ESET_Staff_RetrieveNAsync", INDEX++),
        ESET_Staff_RetrieveResigned("ESET_Staff_RetrieveResigned", INDEX++),
        /**
         * 向NBR服务器下载优惠券
         */
        ESET_Coupon_RefreshByServerDataAsyncC("ESET_Coupon_RefreshByServerDataAsyncC", INDEX++);

        private String name;
        private int index;

        private EnumSQLiteEventType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumSQLiteEventType c : EnumSQLiteEventType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }
    }

    public EnumSQLiteEventType getEventTypeSQLite() {
        return eventTypeSQLite;
    }

    public void setEventTypeSQLite(EnumSQLiteEventType eventTypeSQLite) {
        this.eventTypeSQLite = eventTypeSQLite;
    }

    protected EnumSQLiteEventType eventTypeSQLite;

}
