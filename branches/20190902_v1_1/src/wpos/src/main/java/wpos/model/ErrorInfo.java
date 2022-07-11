package wpos.model;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.utils.StringUtils;

public class ErrorInfo extends BaseModel {
    private static final long serialVersionUID = 2033798065231436579L;
    public static final ErrorInfoField field = new ErrorInfoField();

    protected EnumErrorCode errorCode;

    public ErrorInfo() {
        errorCode = EnumErrorCode.EC_OtherError;
    }

    public EnumErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(EnumErrorCode eec) {
        this.errorCode = eec;
    }

    public enum EnumErrorCode {
        EC_NoError("No error", 0), //
        EC_Duplicated("Duplicated", 1), //
        EC_NoSuchData("No such data", 2), //
        EC_OtherError("Other error", 3), //
        EC_Hack("Hack", 4), //
        EC_NotImplemented("Not implemented", 5), //
        EC_ObjectNotEqual("Object not equal", 6), //
        EC_BusinessLogicNotDefined("Business logic not defined", 7), //
        EC_WrongFormatForInputField("Wrong format for input field", 8), //
        EC_Timeout("Time out", 9), //
        EC_NoPermission("No permission", 10), //
        EC_OtherError2("Other error 2", 11),//
        EC_PartSuccess("Part success", 12),
        EC_Redirect("Redirect", 13),
        EC_DuplicatedSession("Duplicated session", 14), //你已经在其它地方登录
        EC_InvalidSession("Invalid session", 15), // 代表会话无效，一般是因为已经断网
        EC_SessionTimeout("Session time out", 16), // 会话过期。每个Stage都有一个Timer，循环检测当前会话是否过期，如果过期，会自动返回登录页面
        EC_WechatServerError("Wechat Server Error", 17); // 微信服务器错误

        private String name;
        private int index;

        private EnumErrorCode(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumErrorCode c : EnumErrorCode.values()) {
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

        public void setIndex(int index) {
            this.index = index;
        }
    }

    @Override
    public String toString() {
        return "错误码=" + errorCode + "\t错误信息=" + errorMessage;
    }

//    public String getFIELD_NAME_errorCode() {
//        return "errorCode";
//    }
//
//    public String getFIELD_NAME_errorMessage() {
//        return "errorMessage";
//    }

    // 重写parse1 doParse1方法
    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行ErrorInfo的parse1，s=" + s);
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("ErrorInfo.parse1()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 ErrorInfo.doParse1，jo=" + (jo == null ? null : jo.toString()));
        try {
            String errorMsg = jo.getString(field.getFIELD_NAME_ErrorMessage());
            if (!StringUtils.isEmpty(errorMsg) && "null".equals(errorMsg)){
                errorMessage = errorMsg;
            }

            errorCode = ErrorInfo.EnumErrorCode.valueOf(jo.getString(field.getFIELD_NAME_ErrorCode()));
            return this;
        } catch (Exception e) {
            System.out.println("ErrorInfo.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
