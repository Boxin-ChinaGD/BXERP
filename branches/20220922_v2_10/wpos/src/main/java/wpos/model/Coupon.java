package wpos.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.helper.Constants;
import wpos.utils.GeneralUtil;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "T_Coupon")
public class Coupon extends BaseModel {
    public static final CouponField field = new CouponField();

//    @Transient
    public static final String HTTP_Coupon_RetrieveNC = "coupon/retrieveNEx.bx?pageIndex=";
//    @Transient
    public static final String HTTP_Coupon_RETRIEVENC_PageSize = "&pageSize=";

    @Column(name = "F_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    @Column(name = "F_Status")
    protected int status;

    @Column(name = "F_Type")
    protected int type;

    @Column(name = "F_Bonus")
    protected int bonus;

    @Column(name = "F_LeastAmount")
    protected double leastAmount;

    @Column(name = "F_ReduceAmount")
    protected double reduceAmount;

    @Column(name = "F_Discount")
    protected double discount;

    @Column(name = "F_Title")
    protected String title;

    @Column(name = "F_Color")
    protected String color;

    @Column(name = "F_Description")
    protected String description;

    @Column(name = "F_PersonalLimit")
    protected int personalLimit;

    @Column(name = "F_WeekDayAvailable")
    protected int weekDayAvailable;

    @Column(name = "F_BeginTime")
    protected String beginTime;

    @Column(name = "F_EndTime")
    protected String endTime;

    @Column(name = "F_BeginDateTime")
    protected Date beginDateTime;

    @Column(name = "F_EndDateTime")
    protected Date endDateTime;

    @Column(name = "F_Quantity")
    protected int quantity;

    @Column(name = "F_RemainingQuantity")
    protected int remainingQuantity;

    @Column(name = "F_Scope")
    protected int scope;

//    @Generated(hash = 181398860)
//    public Coupon(Long ID, int status, int type, int bonus, double leastAmount, double reduceAmount, double discount, @NotNull String title,
//            @NotNull String color, @NotNull String description, int personalLimit, int weekDayAvailable, @NotNull String beginTime,
//            @NotNull String endTime, @NotNull Date beginDateTime, @NotNull Date endDateTime, int quantity, int remainingQuantity, int scope) {
//        this.ID = ID;
//        this.status = status;
//        this.type = type;
//        this.bonus = bonus;
//        this.leastAmount = leastAmount;
//        this.reduceAmount = reduceAmount;
//        this.discount = discount;
//        this.title = title;
//        this.color = color;
//        this.description = description;
//        this.personalLimit = personalLimit;
//        this.weekDayAvailable = weekDayAvailable;
//        this.beginTime = beginTime;
//        this.endTime = endTime;
//        this.beginDateTime = beginDateTime;
//        this.endDateTime = endDateTime;
//        this.quantity = quantity;
//        this.remainingQuantity = remainingQuantity;
//        this.scope = scope;
//    }

//    @Generated(hash = 75265961)
//    public Coupon() {
//    }

    @Override
    public String toString() {
        return "Coupon{" +
                "ID=" + ID +
                ", status=" + status +
                ", type=" + type +
                ", bonus=" + bonus +
                ", leastAmount=" + leastAmount +
                ", reduceAmount=" + reduceAmount +
                ", discount=" + discount +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", personalLimit=" + personalLimit +
                ", weekDayAvailable=" + weekDayAvailable +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", beginDateTime=" + beginDateTime +
                ", endDateTime=" + endDateTime +
                ", quantity=" + quantity +
                ", remainingQuantity=" + remainingQuantity +
                ", scope=" + scope +
                '}';
    }

    @Override
    public Integer getID() {
        return this.ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public double getLeastAmount() {
        return leastAmount;
    }

    public void setLeastAmount(double leastAmount) {
        this.leastAmount = leastAmount;
    }

    public double getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(double reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPersonalLimit() {
        return personalLimit;
    }

    public void setPersonalLimit(int personalLimit) {
        this.personalLimit = personalLimit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getWeekDayAvailable() {
        return weekDayAvailable;
    }

    public void setWeekDayAvailable(int weekDayAvailable) {
        this.weekDayAvailable = weekDayAvailable;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Coupon coupon = new Coupon();
        coupon.setID(ID);
        coupon.setStatus(status);
        coupon.setBonus(bonus);
        coupon.setLeastAmount(leastAmount);
        coupon.setReduceAmount(reduceAmount);
        coupon.setDiscount(discount);
        coupon.setTitle(title);
        coupon.setColor(color);
        coupon.setDescription(description);
        coupon.setPersonalLimit(personalLimit);
        coupon.setType(type);
        coupon.setBeginTime(beginTime);
        coupon.setEndTime(endTime);
        coupon.setBeginDateTime(beginDateTime);
        coupon.setEndDateTime(endDateTime);
        coupon.setQuantity(quantity);
        coupon.setRemainingQuantity(remainingQuantity);
        coupon.setScope(scope);
        coupon.setWeekDayAvailable(weekDayAvailable);

        return coupon;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {

            return -1;
        }
        Coupon coupon = (Coupon) arg0;
        if ((ignoreIDInComparision == true ? true : coupon.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
                && coupon.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
                && coupon.getBonus() == bonus && printComparator(field.getFIELD_NAME_bonus()) //
                && coupon.getWeekDayAvailable() == weekDayAvailable && printComparator(field.getFIELD_NAME_weekDayAvailable()) //
                && Math.abs(GeneralUtil.sub(coupon.getLeastAmount(), leastAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_leastAmount()) //
                && Math.abs(GeneralUtil.sub(coupon.getReduceAmount(), reduceAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_reduceAmount()) //
                && Math.abs(GeneralUtil.sub(coupon.getDiscount(), discount)) < TOLERANCE && printComparator(field.getFIELD_NAME_discount()) //
                && coupon.getTitle().equals(title) && printComparator(field.getFIELD_NAME_title()) //
                && coupon.getColor().equals(color) && printComparator(field.getFIELD_NAME_color()) //
                && coupon.getDescription().equals(description) && printComparator(field.getFIELD_NAME_description()) //
                && coupon.getPersonalLimit() == personalLimit && printComparator(field.getFIELD_NAME_personalLimit()) //
                && coupon.getType() == type && printComparator(field.getFIELD_NAME_type()) //
                && coupon.getBeginTime().equals(beginTime) && printComparator(field.getFIELD_NAME_beginTime()) //
                && coupon.getEndTime().equals(endTime) && printComparator(field.getFIELD_NAME_endTime()) //
                && coupon.getQuantity() == quantity && printComparator(field.getFIELD_NAME_quantity()) //
                && coupon.getRemainingQuantity() == remainingQuantity && printComparator(field.getFIELD_NAME_remainingQuantity()) //
                && coupon.getScope() == scope && printComparator(field.getFIELD_NAME_scope()) //
                ) {
            return 0;
        }

        return -1;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Coupon.parse1，s=" + s);

        try {
            JSONObject jo =JSONObject.parseObject(s);
            Coupon c = (Coupon) doParse1(jo);
            if (c == null) {
                return null;
            }
            JSONArray csArr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
            if (csArr != null) {
                CouponScope cs = new CouponScope();
                List<CouponScope> listCS = (List<CouponScope>) cs.parseN(csArr);
                if (listCS == null) {
                    return null;
                }
                c.setListSlave1(listCS);  //非常关键！！
            }

            return c;
        } catch (JSONException e) {
            System.out.println("PromotionScope.parse1()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public BaseModel doParse1(JSONObject jo) {
        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            status = jo.getInteger(field.getFIELD_NAME_status());
            bonus = jo.getInteger(field.getFIELD_NAME_bonus());
            leastAmount = jo.getDouble(field.getFIELD_NAME_leastAmount());
            reduceAmount = jo.getDouble(field.getFIELD_NAME_reduceAmount());
            discount = jo.getDouble(field.getFIELD_NAME_discount());
            title = jo.getString(field.getFIELD_NAME_title());
            color = jo.getString(field.getFIELD_NAME_color());
            description = jo.getString(field.getFIELD_NAME_description());
            personalLimit = jo.getInteger(field.getFIELD_NAME_personalLimit());
            weekDayAvailable = jo.getInteger(field.getFIELD_NAME_weekDayAvailable());
            type = jo.getInteger(field.getFIELD_NAME_type());
            beginTime = jo.getString(field.getFIELD_NAME_beginTime());
            endTime = jo.getString(field.getFIELD_NAME_endTime());
            //
            String tmp = jo.getString(field.getFIELD_NAME_beginDateTime());
            if (!StringUtils.isEmpty(tmp)) {
                beginDateTime = Constants.getSimpleDateFormat2().parse(tmp);
                if (beginDateTime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_beginDateTime() + "=" + tmp);
                }
            }
            //
            tmp = jo.getString(field.getFIELD_NAME_endDateTime());
            if (!StringUtils.isEmpty(tmp)) {
                endDateTime = Constants.getSimpleDateFormat2().parse(tmp);
                if (endDateTime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_endDateTime() + "=" + tmp);
                }
            }
            //
            quantity = jo.getInteger(field.getFIELD_NAME_quantity());
            remainingQuantity = jo.getInteger(field.getFIELD_NAME_remainingQuantity());
            scope = jo.getInteger(field.getFIELD_NAME_scope());

            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<?> parseN(JSONArray jsonArray) {
        List<BaseModel> list = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Coupon c = new Coupon();
                if (c.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                list.add(c);
            }
            return list;
        } catch (Exception e) {
            System.out.println("Coupon.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        List<BaseModel> couponList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = JSONArray.parseArray(s);
            for (int i = 0; i < jsonArray1.size(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Coupon coupon = new Coupon();
                coupon.doParse1(jsonObject1);
                couponList.add(coupon);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return couponList;
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        return "";
    }

    public enum EnumCouponStatus {
        ECS_Normal("Normal", 0), //
        ECS_Expired("Expired", 1);

        private String name;
        private int index;

        private EnumCouponStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCouponStatus c : EnumCouponStatus.values()) {
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

    public enum EnumCouponCardType {
        ECCT_CASH("Cash", 0), //
        ECCT_DISCOUNT("Discount", 1);

        private String name;
        private int index;

        private EnumCouponCardType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumCouponCardType c : EnumCouponCardType.values()) {
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
}
