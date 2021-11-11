
package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseSQLiteBO;
import wpos.helper.Constants;
import wpos.utils.DatetimeUtil;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_SmallSheetFrame")
public class SmallSheetFrame extends BaseModel {

    public static final SmallSheetFrameField field = new SmallSheetFrameField();

    @Transient
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    @Column(name = "F_Logo")
    protected String logo;

    @Column(name = "F_CountOfBlankLineAtBottom")
    protected int countOfBlankLineAtBottom;

    @Column(name = "F_DelimiterToRepeat")
    protected String delimiterToRepeat;

    @Transient
    protected List<String[]> listCommodity;//用的是零售单商品表的商品

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

    @Column(name = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Column(name = "F_SyncType")
    protected String syncType;

    @Column(name = "F_SlaveCreated")
    protected int SlaveCreated;

    @Transient
    protected boolean ignoreSyncTypeInComparision;

    public boolean isIgnoreSyncTypeInComparision() {
        return ignoreSyncTypeInComparision;
    }

    public void setIgnoreSyncTypeInComparision(boolean ignoreSyncTypeInComparision) {
        this.ignoreSyncTypeInComparision = ignoreSyncTypeInComparision;
    }

    public static final String HTTP_REQ_URL_Create = "smallSheetFrame/createEx.bx";
    public static final String HTTP_REQ_URL_Update = "smallSheetFrame/updateEx.bx";
    public static final String HTTP_REQ_URL_RetrieveN = "smallSheetFrame/retrieveNEx.bx";
    public static final String HTTP_REQ_URL_RetrieveNC = "smallSheetFrame/retrieveNEx.bx?pageIndex=";
    public static final String HTTP_REQ_PageSize = "&pageSize=";
    public static final String HTTP_REQ_URL_Delete = "smallSheetFrame/deleteEx.bx?ID=";
    public static final String smallSheetfeedbackURL_FRONT = "smallSheetFrame/feedbackEx.bx?sID="; //...
    public static final String smallSheetfeedbackURL_BEHIND = "&errorCode= EC_NoError";

    public static final int SmallSheetLogoVolume = 163840; //应与DB中t_bxConfigGeneral SmallSheetLogoVolume字段的值相同
    public static final String FIELD_ERROR_logo = "Logo图片太大!";
    public static final int MIN_LENGTH_CountOfBlankLineAtBottom = 0;
    public static final int MAX_LENGTH_CountOfBlankLineAtBottom = 5;
    public static final String FIELD_ERROR_countOfBlankLineAtBottom = "小票底部空行数的范围[" + MIN_LENGTH_CountOfBlankLineAtBottom + ", " + MAX_LENGTH_CountOfBlankLineAtBottom + "]";
    public static final String FIELD_ERROR_delimiterToRepeat = "小票分隔符不能为null";
    public static final String FIELD_ERROR_primaryTableIDNoEqualSubTableID = "小票格式的主表的ID和从表的FrameID不一致";
    public static final int NO_SmallSheetTextItem = 20; // 小票格式从表总数

    public SmallSheetFrame(Integer ID, String logo, List<SmallSheetText> textList, List<String[]> listComm) {
        this.ID = ID;
        this.logo = logo;
        this.listSlave1 = textList;
        this.listCommodity = listComm;
    }

    public SmallSheetFrame() {

    }

    //@Generated(hash = 721534406)
//    public SmallSheetFrame(Long ID, String logo, int countOfBlankLineAtBottom, String delimiterToRepeat, Date syncDatetime, Date createDatetime, Date updateDatetime, String syncType) {
//        this.ID = ID;
//        this.logo = logo;
//        this.countOfBlankLineAtBottom = countOfBlankLineAtBottom;
//        this.delimiterToRepeat = delimiterToRepeat;
//        this.syncDatetime = syncDatetime;
//        this.createDatetime = createDatetime;
//        this.updateDatetime = updateDatetime;
//        this.syncType = syncType;
//    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String[]> getListCommodity() {
        return listCommodity;
    }

    public void setListCommodity(List<String[]> listCommodity) {
        this.listCommodity = listCommodity;
    }

    public int getCountOfBlankLineAtBottom() {
        return countOfBlankLineAtBottom;
    }

    public void setCountOfBlankLineAtBottom(int countOfBlankLineAtBottom) {
        this.countOfBlankLineAtBottom = countOfBlankLineAtBottom;
    }

    public String getDelimiterToRepeat() {
        return delimiterToRepeat;
    }

    public void setDelimiterToRepeat(String delimiterToRepeat) {
        this.delimiterToRepeat = delimiterToRepeat;
    }

    @Override
    public BaseModel doParse1(JSONObject object) {
        System.out.println("正在执行 SmallSheetFrame.doParse1，object=" + (object == null ? null : object.toString()));

        try {
            ID = Integer.valueOf(object.getString(field.getFIELD_NAME_ID()));
            logo = object.getString(field.getFIELD_NAME_logo());
            countOfBlankLineAtBottom = object.getInteger(field.getFIELD_NAME_countOfBlankLineAtBottom());
            delimiterToRepeat = object.getString(field.getFIELD_NAME_delimiterToRepeat());
            syncSequence = object.getInteger(field.getFIELD_NAME_syncSequence()); //...
            //
            String tmpSyncDatetime = object.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmpSyncDatetime)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmpSyncDatetime);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmpSyncDatetime);
                }
            }
            //
            String tmpCreateDatetime = object.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }
            //
            String tmpUpdateDatetime = object.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }
            //
            syncType = object.getString(field.getFIELD_NAME_syncType());
            return this;
        } catch (Exception e) {
            System.out.println("Parse smallsheet doParse1()异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 SmallSheetFrame.parse1，s=" + s);

        try {
            JSONObject joSSF = JSONObject.parseObject(s);
            SmallSheetFrame ssf = (SmallSheetFrame) doParse1(joSSF);
            if (ssf == null) {
                return null;
            }
            JSONArray textArr = joSSF.getJSONArray(field.getFIELD_NAME_listSlave1());//
            if (textArr != null) {
                SmallSheetText sst = new SmallSheetText();
                List<SmallSheetText> listText = (List<SmallSheetText>) sst.parseN(textArr);
                if (listText == null) {
                    return null;
                }
                ssf.setListSlave1(listText);  //非常关键！！
            }

            return ssf;
        } catch (JSONException e) {
            System.out.println("Parse smallsheet异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 SmallSheetFrame.parseN，s=" + s);

        List<BaseModel> smallSheetFrameList = new ArrayList<BaseModel>();
        try {
            JSONArray jaSSF = JSONArray.parseArray(s);
            for (int i = 0; i < jaSSF.size(); i++) {
                JSONObject jsonObject = jaSSF.getJSONObject(i);
                SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
                if (smallSheetFrame.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                smallSheetFrameList.add(smallSheetFrame);
            }
        } catch (Exception e) {
            System.out.println(" SmallSheetFrame.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
        }
        return smallSheetFrameList;
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行 SmallSheetFrame.parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> smallSheetFrameList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
                if (smallSheetFrame.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                smallSheetFrameList.add(smallSheetFrame);
            }
            return smallSheetFrameList;
        } catch (Exception e) {
            System.out.println(" SmallSheetFrame.parseN 出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }


    public String toString() {
        return "SmallSheetFrame{" +
                "ID=" + ID +
                ", logo='logo在log里占了1000多行所以不打印出来" + '\'' +
                ", countOfBlankLineAtBottom'" + countOfBlankLineAtBottom + '\'' +
                ", delimiterToRepeat'" + delimiterToRepeat + '\'' +
//                ", listSlave1=" + listSlave1 +
//                ", listCommodity=" + listCommodity +
                '}';
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        SmallSheetFrame ssfIn = (SmallSheetFrame) arg0;
        if ((ignoreIDInComparision == true ? true : (ssfIn.getID().equals(this.getID()) && printComparator(field.getFIELD_NAME_ID()))) //
                && ssfIn.getLogo().equals(this.getLogo()) && printComparator(field.getFIELD_NAME_logo())
                && ssfIn.getCountOfBlankLineAtBottom() == this.getCountOfBlankLineAtBottom() && printComparator(field.getFIELD_NAME_countOfBlankLineAtBottom())
                && ssfIn.getDelimiterToRepeat().equals(this.getDelimiterToRepeat()) && printComparator(field.getFIELD_NAME_delimiterToRepeat())
                && DatetimeUtil.compareDate(ssfIn.getCreateDatetime(), this.getCreateDatetime()) && printComparator(field.getFIELD_NAME_createDatetime())
        ) {
            List<SmallSheetText> list = (List<SmallSheetText>) ssfIn.getListSlave1();

            //由于服务器返回的List是根据Id倒序的，所以需要判断首尾两个Text的id大小，如果是倒序的，先排成升序再进行TextList的比较
            int textListSize = this.getListSlave1().size();
            List<SmallSheetText> textList = new ArrayList<SmallSheetText>();
            if (Double.valueOf(((SmallSheetText) this.getListSlave1().get(0)).getID()).compareTo(Double.valueOf(((SmallSheetText) this.getListSlave1().get(textListSize - 1)).getID())) > 0) {
                for (int i = 0; i < textListSize; i++) {
                    textList.add(((SmallSheetText) this.getListSlave1().get(textListSize - i - 1)));
                }
            } else {
                textList = (List<SmallSheetText>) this.getListSlave1();
            }


            if (list != null && this.getListSlave1() != null) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIgnoreIDInComparision(true);
                    list.get(i).setIgnoreFrameIdInComparision(true);    //....先排序，再比较
                    if (list.get(i).compareTo(textList.get(i)) != 0) {
                        System.out.println("New text list=  ID:" + list.get(i).getID() + "   content:" + list.get(i).getContent() + "   size:" + list.get(i).getSize() + "    bold:" + list.get(i).getBold() + "    gravity:" + list.get(i).getGravity() + "     frameid:" + list.get(i).getFrameId());
                        System.out.println("Old text list=   ID:" + ((SmallSheetText) this.getListSlave1().get(i)).getID() + "   content:" + ((SmallSheetText) this.getListSlave1().get(i)).getContent() + "   size:" + ((SmallSheetText) this.getListSlave1().get(i)).getSize() + "    bold:" + ((SmallSheetText) this.getListSlave1().get(i)).getBold() + "    gravity:" + ((SmallSheetText) this.getListSlave1().get(i)).getGravity() + "     frameid:" + ((SmallSheetText) this.getListSlave1().get(i)).getFrameId());
                        return -1;
                    }
                }
            } else {
                System.out.println("没有比较两个SmallSheetFrame的List<SmallSheetText>");
            }

            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        SmallSheetFrame obj = new SmallSheetFrame();
        obj.setID(this.getID());
        obj.setLogo(new String(this.getLogo()));
        obj.setCountOfBlankLineAtBottom(this.countOfBlankLineAtBottom);
        obj.setDelimiterToRepeat(this.delimiterToRepeat);
        obj.setReturnObject(this.getReturnObject());
        obj.setCreateDatetime(this.createDatetime);
        obj.setSlaveCreated(SlaveCreated);
        if (syncDatetime != null) {
            obj.setSyncDatetime((Date) this.getSyncDatetime().clone());
        }
        List<SmallSheetText> textList = new ArrayList<SmallSheetText>();
        if (this.getListSlave1() != null) {
            for (SmallSheetText text : (List<SmallSheetText>) this.getListSlave1()) {
                SmallSheetText smallSheetText = (SmallSheetText) text.clone();
                textList.add(smallSheetText);
            }
        }
        obj.setListSlave1(textList);

        return obj;
    }

    @Override
    public Date getSyncDatetime() {
        return syncDatetime;
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

    public int getSlaveCreated() {
        return this.SlaveCreated;
    }

    public void setSlaveCreated(int SlaveCreated) {
        this.SlaveCreated = SlaveCreated;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (logo != null) {
            if (logo.length() > SmallSheetLogoVolume) {
                return FIELD_ERROR_logo;
            }
        }
        if (printCheckField(field.getFIELD_NAME_countOfBlankLineAtBottom(), FIELD_ERROR_countOfBlankLineAtBottom, sbError) && !(FieldFormat.checkCountOfBlankLineAtBottom(countOfBlankLineAtBottom))) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_delimiterToRepeat(), FIELD_ERROR_delimiterToRepeat, sbError) && delimiterToRepeat == null) {
            return sbError.toString();
        }

        if (listSlave1 != null) {
            for (int i = 0; i < listSlave1.size(); i++) {
                SmallSheetText sheetText = (SmallSheetText) listSlave1.get(i);
                String err = sheetText.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
                if (!"".equals(err)) {
                    return err;
                }
            }
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        if (logo != null) {
            if (logo.length() > SmallSheetLogoVolume) {
                return FIELD_ERROR_logo;
            }
        }
        if (printCheckField(field.getFIELD_NAME_countOfBlankLineAtBottom(), FIELD_ERROR_countOfBlankLineAtBottom, sbError) && !(FieldFormat.checkCountOfBlankLineAtBottom(countOfBlankLineAtBottom))) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_delimiterToRepeat(), FIELD_ERROR_delimiterToRepeat, sbError) && delimiterToRepeat == null) {
            return sbError.toString();
        }

        if (listSlave1 != null || listSlave1.size() != 0) {
            for (int i = 0; i < listSlave1.size(); i++) {
                SmallSheetText sheetText = (SmallSheetText) listSlave1.get(i);
                if (sheetText.getFrameId().intValue() != ID) {
                    return FIELD_ERROR_primaryTableIDNoEqualSubTableID;//FIELD_ERROR_primaryTableIDNoEqualSubTableID=“小票格式的主表的ID和从表的FrameID不一致”
                }
                String err = sheetText.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
                if (!"".equals(err)) {
                    return err;
                }
            }
        }
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (!StringUtils.isEmpty(sql)) {
            int total = StringUtils.contain(sql, "%s");
            if (this.printCheckField(field.getFIELD_NAME_sql(), FIELD_ERROR_SQL, sbError) && conditions != null && total != conditions.length) {
                return sbError.toString();
            }
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public void fillNonDBFieldValue(int caseID, BaseModel bm) {
        SmallSheetFrame smallSheetFrame = (SmallSheetFrame) bm;
        this.setListSlave1(smallSheetFrame.getListSlave1());
    }
}
