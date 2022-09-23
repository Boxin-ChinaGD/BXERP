package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;

//import org.greenrobot.greendao.annotation.Transient;
//import org.json.JSONArray;
//import org.json.JSONObject;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONException;
//import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BaseModel implements Serializable, Comparable<BaseModel>, Cloneable {
    private static final long serialVersionUID = 543423142354234l;

    public static final String FIELD_NAME_syncDatetime = "F_SyncDatetime";
    public static final BaseModelField field = new BaseModelField();

    public static final String FIELD_ERROR_SQL = "本地条件查询时,sql中的通配符数目和准备的conditions[]数据数目不一致";

    protected boolean ignoreIDInComparision;

    public void setIgnoreIDInComparision(boolean ignoreIDInComparision) {
        this.ignoreIDInComparision = ignoreIDInComparision;
    }

    protected boolean ignoreSlaveListInComparision;

    public void setIgnoreSlaveListInComparision(boolean ignoreSlaveListInComparision) {
        this.ignoreSlaveListInComparision = ignoreSlaveListInComparision;
    }

    public boolean isIgnoreSlaveListInComparision() {
        return ignoreSlaveListInComparision;
    }

    public static final double TOLERANCE = 0.000001d;

//    protected Long ID;

    public Integer getID() {
        throw new RuntimeException("Not yet implemented!");
//        return ID;
    }

    public void setID(Integer ID) {
        throw new RuntimeException("Not yet implemented!");
//        this.ID = ID;
    }

    /** POS CUD对象，首先插入SQLite，然后上传到服务器，最后还有其它处理。在上传到服务器这一步失败后，下次还需要再次上传。<br />
     * 下次上传需要知道call哪个syncAction，本字段可以作为判断依据。<br />
     * 本字段只在POS端有用，在nbr端无意义 */
//    protected String syncType;

    public String getSyncType() {
        throw new RuntimeException("Not yet implemented!");
    }

    public void setSyncType(String syncType) {
        throw new RuntimeException("Not yet implemented!");
    }

    public Date getSyncDatetime() {
        throw new RuntimeException("Not yet implemented!");
    }

    public void setSyncDatetime(Date syncDatetime) {
        throw new RuntimeException("Not yet implemented!");
    }
    /**
     * 代表经常搜索的关键字
     */
    protected String queryKeyword;

    /**
     * 代表是否返回对象
     */
    protected int returnObject;

    /**
     * 检查字段是否唯一
     */
    protected int fieldToCheckUnique;

    public String getQueryKeyword() {
        return queryKeyword;
    }

    public void setQueryKeyword(String queryKeyword) {
        this.queryKeyword = queryKeyword;
    }

    public int getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(int returnObject) {
        this.returnObject = returnObject;
    }

    public int getFieldToCheckUnique() {
        return fieldToCheckUnique;
    }

    public void setFieldToCheckUnique(int fieldToCheckUnique) {
        this.fieldToCheckUnique = fieldToCheckUnique;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    protected int int1;

//    public int getInt2() {
//        return int2;
//    }
//
//    public void setInt2(int int2) {
//        this.int2 = int2;
//    }
//
//    protected int int2;
//
//    public String getFIELD_NAME_int2() {
//        return "int2";
//    }
//
//    protected String FIELD_NAME_int2;

//    public int getInt3() {
//        return int3;
//    }
//
//    public void setInt3(int int3) {
//        this.int3 = int3;
//    }
//
//    protected int int3;

//    public String getFIELD_NAME_int3() {
//        return "int3";
//    }
//
//    protected String FIELD_NAME_int3;

    protected int syncSequence; // POS收到该对象后，知道以什么顺序同步这个对象

    public int getSyncSequence() {
        return syncSequence;
    }

    public void setSyncSequence(int syncSequence) {
        this.syncSequence = syncSequence;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    protected int returnSalt;

    public int getReturnSalt() {
        return returnSalt;
    }

    public void setReturnSalt(int returnSalt) {
        this.returnSalt = returnSalt;
    }

    /** 代表要搜索的员工的ID */
    protected int operatorStaffID;

    public int getOperatorStaffID() {
        return operatorStaffID;
    }

    public void setOperatorStaffID(int operatorStaffID) {
        this.operatorStaffID = operatorStaffID;
    }

    protected boolean printComparator(String fieldName) {
        System.out.println("	Field Equal: " + fieldName);
        return true;
    }

    protected boolean printCheckField(String fieldNameInCode, String suggestion, StringBuilder err) {
        System.out.println("￥￥￥准备检查以下字段的格式: " + fieldNameInCode);
        err.setLength(0);
        err.append(suggestion);

        return true;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("尚未实现clone()方法！");
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        throw new RuntimeException("尚未实现compareTo()方法！");
    }

    public List<?> parseN(String s) {
        throw new RuntimeException("尚未实现parseN()方法！");
    }

    public List<?> parseN(JSONArray jsonArray) {
        throw new RuntimeException("尚未实现parseN()方法！");
    }

    public BaseModel parse1(String s) {
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public BaseModel parse1(JSONObject jo) {
//        try {
//            return doParse1(jo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public BaseModel parse1(JSONObject jo) {
        try {
            return doParse1(jo);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    protected BaseModel doParse1(JSONObject jo) {
//        throw new RuntimeException("尚未实现doParse1(JSONObject jo)方法！");
//    }

    protected BaseModel doParse1(JSONObject jo) {
        throw new RuntimeException("尚未实现doParse1(JSONObject jo)方法！");
    }

    public List<?> parseNC(String s) {
        throw new RuntimeException("尚未实现parseNC()方法！");
    }

    public List<?> parseNC(JSONArray jsonArray) {
        throw new RuntimeException("尚未实现parseNC()方法！");
    }

    public BaseModel parse1C(String s) {
        throw new RuntimeException("尚未实现parse1C()方法！");
    }

    public BaseModel doParse1C(JSONObject jo) {
        throw new RuntimeException("尚未实现doParse1C()方法！");
    }

    public String toJson(BaseModel bm) {
//        return JSONObject.fromObject(bm).toString();
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(bm);
        return jsonObject.toJSONString();
    }

    public static final String JSON_ERROR_KEY = "ERROR";
    public static final String JSON_OBJECT_KEY = "object";
    public static final String JSON_OBJECT_KEY2 = "object2";
    public static final String JSON_OBJECTLIST_KEY = "objectList";

    public static final String KEY_HTMLTable_Parameter_msg = "msg";
    public static final String KEY_HTMLTable_Parameter_TotalRecord = "count";

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getConditions() {
        return conditions;
    }

    public void setConditions(String[] conditions) {
        this.conditions = conditions;
    }

    /**
     * 多条件查询时的sql
     */
    protected String sql;
    /**
     * 多条件查询时的sql的条件
     */
    protected String[] conditions;

    public String pageIndex;
    public String pageSize;

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 从表信息。如果将来有第二个从表，则可以在定义listSlave2
     */
    protected List<?> listSlave1;

    public List<?> getListSlave1() {
        return listSlave1;
    }

    public void setListSlave1(List<?> listSlave1) {
        this.listSlave1 = listSlave1;
    }

    protected List<?> listSlave2;

    public List<?> getListSlave2() {
        return listSlave2;
    }

    public void setListSlave2(List<?> listSlave2) {
        this.listSlave2 = listSlave2;
    }

    protected List<?> listSlave3;

    public List<?> getListSlave3() {
        return listSlave3;
    }

    public void setListSlave3(List<?> listSlave3) {
        this.listSlave3 = listSlave3;
    }

    public String checkCreate(int iUseCaseID) {
        throw new RuntimeException("该函数尚未实现！");
    }

    public String checkUpdate(int iUseCaseID) {
        throw new RuntimeException("该函数尚未实现！");
    }

    public String checkRetrieve1(int iUseCaseID) {
        throw new RuntimeException("该函数尚未实现！");
    }

    public String checkDelete(int iUseCaseID) {
        throw new RuntimeException("该函数尚未实现！");
    }

    public String checkRetrieveN(int iUseCaseID) {

        if (StringUtils.isNotEmpty(pageIndex) && StringUtils.isNotEmpty(pageSize)) {// 1.本地RN不传pageIndex\pageSize 2.httpBO需要
            StringBuilder sbError = new StringBuilder();
            if (this.printCheckField(field.getFIELD_NAME_iPageIndex(), FieldFormat.FIELD_ERROR_Paging, sbError) && this.printCheckField(field.getFIELD_NAME_iPageSize(), FieldFormat.FIELD_ERROR_Paging, sbError)//
                    && !FieldFormat.checkPaging(Integer.valueOf(pageIndex), Integer.valueOf(pageSize)))//
            {
                return sbError.toString();
            }
        }
        return doCheckRetrieveN(iUseCaseID);
    }

    protected String doCheckRetrieveN(int iUseCaseID) {
        throw new RuntimeException("尚未实现doCheckRetrieveN(int iUseCaseID)！");
    }


    protected String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean compareSQLiteRowID(Integer id, Integer id1) {
        if ((id == null ? "0" : id).equals(id1 == null ? "0" : id1)) {
            return true;
        }
        return false;
    }

    public EnumSubUseCaseID getSubUseCaseID() {
        return subUseCaseID;
    }

    public void setSubUseCaseID(EnumSubUseCaseID subUseCaseID) {
        this.subUseCaseID = subUseCaseID;
    }

    protected EnumSubUseCaseID subUseCaseID;

    /**
     * RetrieveN的时候，conditions[]里面的参数个数和类型不确定。实现BaseModel.checkRetrieveN()时，不能确定如何检查参数的合法性。
     * 通过预先设定BaseModel.subUseCaseID，确定参数的顺序和类型，来实现BaseModel.checkRetrieveN()
     */
    public enum EnumSubUseCaseID {
        ESUC_Long("参数1是Long。总共1个参数", 0),
        ESUC_Long_String("参数1是Long，参数2是String。总共2个参数", 1),
        ESUC_String("参数1是String。总共1个参数", 2),
        ESUC_String_Long("参数1是String, 参数2是Long。总共2个参数", 3),
        ESUC_Int("参数1是Int。总共1个参数", 4),
        ESUC_Ignore("忽略查询条件", 5),
        ESUC_String_String("参数1，2都是String", 6);

        private String name;
        private int index;

        private EnumSubUseCaseID(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumSubUseCaseID c : EnumSubUseCaseID.values()) {
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

    ;

    public enum EnumBoolean {
        EB_NO("NO", 0), //
        EB_Yes("Yes", 1);

        private String name;
        private int index;

        private EnumBoolean(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumBoolean c : EnumBoolean.values()) {
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
    /**
     * 本接口在APOS中没有，只有WPOS有，原因是Presenter的实现不同，GreenDAO和JPA插入DB时，JPA需要额外地R1一个对象出来，导致非DB字段的值丢失从而需要重新赋值。
     * 把rt里面的非DB字段的值赋给当前Model的字段
     */
    public void fillNonDBFieldValue(int caseID, BaseModel bm) {
        throw new RuntimeException("尚未实现的方法！");
    }
}
