package com.bx.erp.model;

public class BaseAuthenticationModelField extends BaseModelField {
    protected String FIELD_NAME_pwdEncrypted;

    public String getFIELD_NAME_pwdEncrypted() {
        return "pwdEncrypted";
    }

    protected String FIELD_NAME_salt;

    public String getFIELD_NAME_salt() {
        return "salt";
    }

    protected String FIELD_NAME_passwordInPOS;

    public String getFIELD_NAME_passwordInPOS() {
        return "passwordInPOS";
    }

    public String FIELD_NAME_companySN;

    public String getFIELD_NAME_companySN() {
        return "companySN";
    }
}
