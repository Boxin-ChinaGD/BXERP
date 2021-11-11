package com.bx.erp.model;

import com.bx.erp.utils.FieldFormat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "RememberLoginStaff")
public class RememberLoginStaff extends BaseAuthenticationModel {

    public static final String PHONE_ERROR = "手机号格式错误";

    @Property(nameInDb = "F_Phone")
    private String phone;

    @Property(nameInDb = "F_Password")
    private String password;

    @Property(nameInDb = "F_Remembered")
    private boolean remembered;

    @Generated(hash = 2017122232)
    public RememberLoginStaff(String phone, String password, boolean remembered) {
        this.phone = phone;
        this.password = password;
        this.remembered = remembered;
    }

    @Generated(hash = 162649159)
    public RememberLoginStaff() {
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (!FieldFormat.checkPhone(phone)){
            sbError.append(PHONE_ERROR);
            return sbError.toString();
        }
        return "";
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRemembered() {
        return this.remembered;
    }

    public void setRemembered(boolean remembered) {
        this.remembered = remembered;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        RememberLoginStaff obj = new RememberLoginStaff();
        obj.setRemembered(this.remembered);
        obj.setPhone(new String(this.phone));
        obj.setPassword(new String(this.password));
        return obj;
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

}
