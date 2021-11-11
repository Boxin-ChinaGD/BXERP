const EnumErrorCode = {
	EC_NoError : 'EC_NoError',//正确
	EC_Duplicated : 'EC_Duplicated',//重复
	EC_NoSuchData : 'EC_NoSuchData',
	EC_OtherError : 'EC_OtherError',//其他错误
	EC_Hack : 'EC_Hack',//黑客行为
	EC_NotImplemented : 'EC_NotImplemented',
	EC_ObjectNotEqual : 'EC_ObjectNotEqual',
	EC_BusinessLogicNotDefined : 'EC_BusinessLogicNotDefined',//业务逻辑
	EC_WrongFormatForInputField : 'EC_WrongFormatForInputField'//输入格式
};
const ERROR_Msg_Retrieve1Commodity1 = "查看商品详情失败";
const ERROR_Msg_Retrieve1Commodity_toDeleteAllProvider = "商品至少需要有一个供应商<br/>不支持删除全部的供应商";