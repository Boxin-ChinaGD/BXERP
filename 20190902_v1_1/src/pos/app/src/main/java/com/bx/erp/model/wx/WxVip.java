package com.bx.erp.model.wx;

import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.utils.DatetimeUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class WxVip extends BaseModel {
    public static final WxVipField field = new WxVipField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;


    @NotNull
    @Property(nameInDb = "F_VipID")
    private int vipID;


    @NotNull
    @Property(nameInDb = "F_Nickname")
    private String nickName;


    @NotNull
    @Property(nameInDb = "F_Bonus")
    private int bonus;


    @NotNull
    @Property(nameInDb = "F_Balance")
    private double balance;


    @NotNull
    @Property(nameInDb = "F_Sex")
    private int sex;


    @NotNull
    @Property(nameInDb = "F_UserInfo")
    private String user_info;


    @NotNull
    @Property(nameInDb = "F_CustomFieldList")
    private String custom_field_list;


    @NotNull
    @Property(nameInDb = "F_Name")
    private String name;


    @NotNull
    @Property(nameInDb = "F_Value")
    private String value;


    @NotNull
    @Property(nameInDb = "F_UserCardStatus")
    private String user_card_status;


    @NotNull
    @Property(nameInDb = "F_LastUsedDateTime")
    private Date lastUsedDateTime;


    @Transient
    private int isIncreaseBonus;


    @Transient
    private int amountOnAddBonus;


    @Transient
    private int bonusOnMinusAmount;

    public String getFIELD_NAME_bonusOnMinusAmount() {
        return "bonusOnMinusAmount";
    }

    @Property(nameInDb = "F_PublicAccountOpenID")
    private String publicAccountOpenID;

    @Property(nameInDb = "F_MiniProgramOpenID")
    private String miniProgramOpenID;

    @Property(nameInDb = "F_UnionID")
    private String unionID;

    @Generated(hash = 1395820642)
    public WxVip() {
    }

    @Generated(hash = 1134964178)
    public WxVip(Long ID, int vipID, @NotNull String nickName, int bonus, double balance, int sex, @NotNull String user_info,
                 @NotNull String custom_field_list, @NotNull String name, @NotNull String value, @NotNull String user_card_status,
                 @NotNull Date lastUsedDateTime, String publicAccountOpenID, String miniProgramOpenID, String unionID) {
        this.ID = ID;
        this.vipID = vipID;
        this.nickName = nickName;
        this.bonus = bonus;
        this.balance = balance;
        this.sex = sex;
        this.user_info = user_info;
        this.custom_field_list = custom_field_list;
        this.name = name;
        this.value = value;
        this.user_card_status = user_card_status;
        this.lastUsedDateTime = lastUsedDateTime;
        this.publicAccountOpenID = publicAccountOpenID;
        this.miniProgramOpenID = miniProgramOpenID;
        this.unionID = unionID;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getVipID() {
        return this.vipID;
    }

    public void setVipID(int vipID) {
        this.vipID = vipID;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_info() {
        return this.user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getCustom_field_list() {
        return this.custom_field_list;
    }

    public void setCustom_field_list(String custom_field_list) {
        this.custom_field_list = custom_field_list;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUser_card_status() {
        return this.user_card_status;
    }

    public void setUser_card_status(String user_card_status) {
        this.user_card_status = user_card_status;
    }

    public Date getLastUsedDateTime() {
        return this.lastUsedDateTime;
    }

    public void setLastUsedDateTime(Date lastUsedDateTime) {
        this.lastUsedDateTime = lastUsedDateTime;
    }

    public int getIsIncreaseBonus() {
        return isIncreaseBonus;
    }

    public void setIsIncreaseBonus(int isIncreaseBonus) {
        this.isIncreaseBonus = isIncreaseBonus;
    }

    public int getAmountOnAddBonus() {
        return amountOnAddBonus;
    }

    public void setAmountOnAddBonus(int amountOnAddBonus) {
        this.amountOnAddBonus = amountOnAddBonus;
    }

    public int getBonusOnMinusAmount() {
        return bonusOnMinusAmount;
    }

    public void setBonusOnMinusAmount(int bonusOnMinusAmount) {
        this.bonusOnMinusAmount = bonusOnMinusAmount;
    }

    @Override
    public String toString() {
        return "WxVip{" +
                "ID=" + ID +
                ", vipID=" + vipID +
                ", nickName='" + nickName + '\'' +
                ", bonus=" + bonus +
                ", balance=" + balance +
                ", sex=" + sex +
                ", user_info='" + user_info + '\'' +
                ", custom_field_list='" + custom_field_list + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", user_card_status='" + user_card_status + '\'' +
                ", lastUsedDateTime=" + lastUsedDateTime +
                ", isIncreaseBonus=" + isIncreaseBonus +
                ", amountOnAddBonus=" + amountOnAddBonus +
                ", bonusOnMinusAmount=" + bonusOnMinusAmount +
                ", publicAccountOpenID='" + publicAccountOpenID + '\'' +
                ", miniProgramOpenID='" + miniProgramOpenID + '\'' +
                ", unionID='" + unionID + '\'' +
                '}';
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 WxVip.doParse1，object=" + (jo == null ? null : jo.toString()));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default2);

            ID = jo.getLong(field.getFIELD_NAME_ID());
            vipID = jo.getInt(field.getFIELD_NAME_vipID());
            bonus = jo.getInt(field.getFIELD_NAME_bonus());
            balance = jo.getDouble(field.getFIELD_NAME_balance());
            sex = jo.getInt(field.getFIELD_NAME_sex());
            user_info = jo.getString(field.getFIELD_NAME_user_info());
            custom_field_list = jo.getString(field.getFIELD_NAME_custom_field_list());
            name = jo.getString(field.getFIELD_NAME_name());
            value = jo.getString(field.getFIELD_NAME_value());
            user_card_status = jo.getString(field.getFIELD_NAME_user_card_status());
            publicAccountOpenID = jo.getString(field.getFIELD_NAME_publicAccountOpenID());
            miniProgramOpenID = jo.getString(field.getFIELD_NAME_miniProgramOpenID());
            unionID = jo.getString(field.getFIELD_NAME_unionID());
            String tmp = jo.getString(field.getFIELD_NAME_lastUsedDateTime());
            if (!"".equals(tmp)) {
                lastUsedDateTime = sdf.parse(tmp);
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行 WxVip.parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> wxVipList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                WxVip wxVip = new WxVip();
                if (wxVip.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                wxVipList.add(wxVip);
            }
            return wxVipList;
        } catch (Exception e) {
            System.out.println("WxVip.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxVip wxVip = (WxVip) arg0;
        if ((ignoreIDInComparision == true ? true : (wxVip.getID() == ID && printComparator(field.getFIELD_NAME_ID())))//
                && wxVip.getVipID() == vipID && printComparator(field.getFIELD_NAME_vipID())//
                && wxVip.getNickName().equals(nickName) && printComparator(field.getFIELD_NAME_nickName())//
                && wxVip.getBonus() == bonus && printComparator(field.getFIELD_NAME_bonus())//
                && (wxVip.getBalance() - balance) < TOLERANCE && printComparator(field.getFIELD_NAME_balance())//
                && wxVip.getSex() == sex && printComparator(field.getFIELD_NAME_sex())//
                && wxVip.getUser_info().equals(user_info) && printComparator(field.getFIELD_NAME_user_info())//
                && wxVip.getCustom_field_list().equals(custom_field_list) && printComparator(field.getFIELD_NAME_custom_field_list())//
                && wxVip.getName().equals(name) && printComparator(field.getFIELD_NAME_name())//
                && wxVip.getValue().equals(value) && printComparator(field.getFIELD_NAME_value())//
                && wxVip.getUser_card_status().equals(user_card_status) && printComparator(field.getFIELD_NAME_user_card_status())//
                && DatetimeUtil.compareDate(wxVip.getLastUsedDateTime(), lastUsedDateTime) && printComparator(field.getFIELD_NAME_lastUsedDateTime())//
                && wxVip.getPublicAccountOpenID().equals(publicAccountOpenID) && printComparator(field.getFIELD_NAME_publicAccountOpenID())//
                && wxVip.getMiniProgramOpenID().equals(miniProgramOpenID) && printComparator(field.getFIELD_NAME_miniProgramOpenID())//
                && wxVip.getUnionID().equals(unionID) && printComparator(field.getFIELD_NAME_unionID())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        WxVip wxVip = new WxVip();
        wxVip.setID(ID);
        wxVip.setVipID(vipID);
        wxVip.setNickName(nickName);
        wxVip.setBonus(bonus);
        wxVip.setBalance(balance);
        wxVip.setSex(sex);
        wxVip.setUser_info(user_info);
        wxVip.setCustom_field_list(custom_field_list);
        wxVip.setName(name);
        wxVip.setValue(value);
        wxVip.setUser_card_status(user_card_status);
        wxVip.setLastUsedDateTime((lastUsedDateTime == null ? null : (Date) lastUsedDateTime.clone()));
        wxVip.setIsIncreaseBonus(isIncreaseBonus);
        wxVip.setAmountOnAddBonus(amountOnAddBonus);
        wxVip.setBonusOnMinusAmount(bonusOnMinusAmount);
        wxVip.setPublicAccountOpenID(publicAccountOpenID);
        wxVip.setMiniProgramOpenID(miniProgramOpenID);
        wxVip.setUnionID(unionID);
        return wxVip;
    }

    public String getPublicAccountOpenID() {
        return this.publicAccountOpenID;
    }

    public void setPublicAccountOpenID(String publicAccountOpenID) {
        this.publicAccountOpenID = publicAccountOpenID;
    }

    public String getMiniProgramOpenID() {
        return this.miniProgramOpenID;
    }

    public void setMiniProgramOpenID(String miniProgramOpenID) {
        this.miniProgramOpenID = miniProgramOpenID;
    }

    public String getUnionID() {
        return this.unionID;
    }

    public void setUnionID(String unionID) {
        this.unionID = unionID;
    }
}
