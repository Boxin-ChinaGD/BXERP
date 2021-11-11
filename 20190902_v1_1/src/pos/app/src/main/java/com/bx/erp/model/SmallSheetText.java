
package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.StringUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WPNA on 2018/8/31.
 * 用于设置打印的内容、字体大小、字体粗细、内容的位置
 */
@Entity(nameInDb = "smallSheetText")
public class SmallSheetText extends BaseModel {
    private static final int Zero = 0;
    private static final int MAX_LENGTH_Content = 100;
    public static final String FIELD_ERROR_content = "小票内容长度为(" + Zero + ", " + MAX_LENGTH_Content + "]";
    public static final String FIELD_ERROR_frameID = "小票格式ID必须大于" + Zero;
    public static final String FIELD_ERROR_size = "字体大小必须大于" + Zero;
    public static final String FIELD_ERROR_bold = "字体加粗只能是" + Zero + "或" + 1;
    public static final String FIELD_ERROR_gravity = "内容位置必须大于" + Zero;

    public final static String show_Header = "show_header";
    public final static String document_Number = "document_number";
    public final static String date = "date";
    public final static String total_Money = "total_money";
    public final static String payment_Method = "payment_method";
    public final static String discount = "discount";
    public final static String payable = "payable";
    public final static String payment_Method1 = "payment_method1";
//    public final static String payment_Method2 = "payment_method2";
    public final static String payment_Method3 = "payment_method3";
    public final static String show_Footer1 = "show_footer1";
    public final static String show_Footer2 = "show_footer2";
    public final static String show_Footer3 = "show_footer3";
    public final static String show_Footer4 = "show_footer4";
    public final static String show_Footer5 = "show_footer5";
    public final static String show_Footer6 = "show_footer6";
    public final static String show_Footer7 = "show_footer7";
    public final static String show_Footer8 = "show_footer8";
    public final static String show_Footer9 = "show_footer9";
    public final static String show_Footer10 = "show_footer10";
    public final static String show_Ticket_Bottom = "show_ticket_bottom";

    public static final SmallSheetTextField field = new SmallSheetTextField();

    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_Content")
    protected String content;

    @Property(nameInDb = "F_Size")
    protected float size;

    @Property(nameInDb = "F_Bold")
    protected int bold;

    @Property(nameInDb = "F_Gravity")
    protected int gravity;

    @Property(nameInDb = "F_FrameID")
    protected Long frameId;

    @Transient
    protected boolean ignoreFrameIdInComparision;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    public static final String HTTP_REQ_PARAMETER_Create = "smallSheetTextList";

    public static final String HTTP_REQ_PARAMETER_Update = "smallSheetTextList";

    @Generated(hash = 195973140)
    public SmallSheetText(Long ID, String content, float size, int bold, int gravity, Long frameId, Date syncDatetime, String syncType) {
        this.ID = ID;
        this.content = content;
        this.size = size;
        this.bold = bold;
        this.gravity = gravity;
        this.frameId = frameId;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
    }

    public SmallSheetText() {

    }

    public void setIgnoreFrameIdInComparision(boolean ignoreFrameIdInComparision) {
        this.ignoreFrameIdInComparision = ignoreFrameIdInComparision;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getBold() {
        return bold;
    }

    public void setBold(int bold) {
        this.bold = bold;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public Long getFrameId() {
        return frameId;
    }

    public void setFrameId(Long frameId) {
        this.frameId = frameId;
    }

    @Override
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行 SmallSheetText.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            ID = Long.valueOf(object.getString(field.getFIELD_NAME_ID()));
            frameId = Long.valueOf(object.getString(field.getFIELD_NAME_frameId()));
            bold = object.getInt(field.getFIELD_NAME_bold());
            content = object.getString(field.getFIELD_NAME_content());
            gravity = object.getInt(field.getFIELD_NAME_gravity());
            size = object.getInt(field.getFIELD_NAME_size());
            //
            String tmp = object.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }
            //
            syncType = object.getString(field.getFIELD_NAME_syncType());
            return this;
        } catch (Exception e) {
            System.out.println("SmallSheetText.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 SmallSheetText.parse1，s=" + s);

        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("SmallSheetText.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 SmallSheetText.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> smallSheetTextList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                SmallSheetText smallSheetText = new SmallSheetText();
                smallSheetText.doParse1(jsonObject2);
                smallSheetTextList.add(smallSheetText);
            }
        } catch (Exception e) {
            System.out.println("SmallSheetText.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return smallSheetTextList;
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);
        return json;
    }


    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        SmallSheetText sst = (SmallSheetText) arg0;
        if ((ignoreIDInComparision ? true : (sst.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID())))
                && (ignoreFrameIdInComparision ? true : sst.getFrameId().equals(this.getFrameId()) && printComparator(field.getFIELD_NAME_frameId()))
                && sst.getContent().equals(this.getContent()) && printComparator(field.getFIELD_NAME_content())
                && sst.getSize() == this.getSize() && printComparator(field.getFIELD_NAME_size())
                && sst.getBold() == this.getBold() && printComparator(field.getFIELD_NAME_bold())
                && sst.getGravity() == this.getGravity() && printComparator(field.getFIELD_NAME_gravity())
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        SmallSheetText obj = new SmallSheetText();
        obj.setID(this.getID());
        obj.setContent(new String(this.getContent()));
        obj.setSize(this.getSize());
        obj.setBold(this.getBold());
        obj.setGravity(this.getGravity());
        obj.setFrameId(this.getFrameId());
        if (syncDatetime != null) {
            obj.setSyncDatetime(syncDatetime);
        }

        return obj;
    }

    @Override
    public String toString() {
        return "SmallSheetText{" +
                "ID=" + ID +
                ", content='" + content + '\'' +
                ", size=" + size +
                ", bold=" + bold +
                ", gravity=" + gravity +
                ", frameId=" + frameId +
                '}';
    }

    @Override
    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField( field.getFIELD_NAME_content(), FIELD_ERROR_content, sbError) && content == null || content.length() > MAX_LENGTH_Content) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_size(), FIELD_ERROR_size, sbError) && !(size > Zero)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_bold(), FIELD_ERROR_bold, sbError) && !(bold == Zero || bold == 1)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_gravity(), FIELD_ERROR_gravity, sbError) && !(gravity > Zero)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField( field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_content(), FIELD_ERROR_content, sbError) && content == null || content.length() > MAX_LENGTH_Content) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_size(), FIELD_ERROR_size, sbError) && !(size > Zero)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_bold(), FIELD_ERROR_bold, sbError) && !(bold == Zero || bold == 1)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_gravity(), FIELD_ERROR_gravity, sbError) && !(gravity > Zero)) {
            return sbError.toString();
        }

        if (printCheckField( field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField( field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if(printCheckField( field.getFIELD_NAME_frameId(), FIELD_ERROR_frameID, sbError) && !FieldFormat.checkID(frameId)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        return "";
    }

    public static int TextIndex = 0;
    public enum EnumSmallSheetTextIndex {
        Show_Header(show_Header, TextIndex ++),
        Document_Number(document_Number, TextIndex ++),
        Date(date, TextIndex ++),
        Total_Money(total_Money, TextIndex ++),
        Payment_Method(payment_Method, TextIndex ++),
        Discount(discount, TextIndex ++),
        Payable(payable, TextIndex ++),
        Payment_Method1(payment_Method1, TextIndex ++),
//        Payment_Method2(payment_Method2, TextIndex ++),
        Payment_Method3(payment_Method3, TextIndex ++),
        Show_Footer1(show_Footer1, TextIndex ++),
        Show_Footer2(show_Footer2, TextIndex ++),
        Show_Footer3(show_Footer3, TextIndex ++),
        Show_Footer4(show_Footer4, TextIndex ++),
        Show_Footer5(show_Footer5, TextIndex ++),
        Show_Footer6(show_Footer6, TextIndex ++),
        Show_Footer7(show_Footer7, TextIndex ++),
        Show_Footer8(show_Footer8, TextIndex ++),
        Show_Footer9(show_Footer9, TextIndex ++),
        Show_Footer10(show_Footer10, TextIndex ++),
        Show_Ticket_Bottom(show_Ticket_Bottom,  TextIndex ++);

        private String name;
        private int index;

        private EnumSmallSheetTextIndex(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static int getIndex(String name){
            for (EnumSmallSheetTextIndex esst : values()){
                if(esst.getName().equals(name)){
                    return esst.getIndex();
                }
            }
            return 0;
        }
        public static String getName(int index) {
            for (EnumSmallSheetTextIndex c : EnumSmallSheetTextIndex.values()) {
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
