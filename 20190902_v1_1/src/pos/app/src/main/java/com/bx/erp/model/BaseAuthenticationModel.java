package com.bx.erp.model;

public class BaseAuthenticationModel extends BaseModel {
    private static final long serialVersionUID = 7176260027700578354L;

    /**
     * 用户根据什么字段登录，则这里就返回什么字段的值
     *
     * @return
     */
    public String getKey() {
        throw new RuntimeException("Not yet implemented!");
    }

    protected String companySN;

    public String getCompanySN() {
        return companySN;
    }

    public void setCompanySN(String companySN) {
        this.companySN = companySN;
    }
}