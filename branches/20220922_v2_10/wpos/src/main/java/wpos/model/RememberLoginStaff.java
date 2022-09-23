package wpos.model;


import wpos.utils.FieldFormat;

import javax.persistence.*;

@Entity
@Table(name = "T_RememberLoginStaff")
public class RememberLoginStaff extends BaseAuthenticationModel {

    public static final String PHONE_ERROR = "手机号格式错误";

    @Column(name = "F_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "F_Phone")
    private String phone;

    @Column(name = "F_Password")
    private String password;

    @Column(name = "F_Remembered")
    private boolean remembered;

//    @Generated(hash = 2017122232)
    public RememberLoginStaff(String phone, String password, boolean remembered) {
        this.phone = phone;
        this.password = password;
        this.remembered = remembered;
    }

//    @Generated(hash = 162649159)
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

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public boolean isRemembered() {
        return remembered;
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
