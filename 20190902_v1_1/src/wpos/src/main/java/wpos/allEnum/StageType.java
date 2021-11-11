package wpos.allEnum;

public enum StageType {
    LOGIN_STAGE("loginStage"),
    PRESALE_STAGE("preSaleStage"),
    SYNCDATA_STAGE("syncDataStage"),
    RETAILTRADEAGGREGATION_STAGE("retailTradeAggregationStage"),
    FRAGMENT_STAGE("fragmentStage");


    private String stageName;

    StageType(String stageName){
        this.stageName = stageName;
    }

}
