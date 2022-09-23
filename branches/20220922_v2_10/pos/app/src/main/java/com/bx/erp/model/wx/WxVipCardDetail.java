package com.bx.erp.model.wx;

import com.bx.erp.model.BaseModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONException;
import org.json.JSONObject;


@Entity
public class WxVipCardDetail extends BaseModel {
    public static final WxVipCardDetailField field = new WxVipCardDetailField();

    /**
     * unionid 字段检验
     */
    private static final int Zero = 0;
    private static final int MAX_LENGTH_Unionid = 100;
    public static final String FIELD_ERROR_unionid = "unionid的长度为[" + Zero + ", " + MAX_LENGTH_Unionid + "]";

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;


    @NotNull
    @Property(nameInDb = "F_WxVipCardID")
    private int wxVipCardID;


    @Property(nameInDb = "F_Code")
    private String code;


    @NotNull
    @Property(nameInDb = "F_CardID")
    private String card_id;


    @NotNull
    @Property(nameInDb = "F_BackgroundPicUrl")
    private String background_pic_url;


    @Property(nameInDb = "F_OldUserCardCode")
    private String oldUserCardCode;


    @Property(nameInDb = "F_IsGiveByFriend")
    private int isGiveByFriend;


    @Property(nameInDb = "F_FriendUserName")
    private String friendUserName;


    @Property(nameInDb = "F_OuterStr")
    private String outerStr;


    @Property(nameInDb = "F_IsRestoreMemberCard")
    private int isRestoreMemberCard;


    @Property(nameInDb = "F_UnionId")
    private String unionId;


    @Property(nameInDb = "F_MsgType")
    private String msgType;


    @Property(nameInDb = "F_Event")
    private String event;


    @Property(nameInDb = "F_ToUserName")
    private String toUserName;


    @Generated(hash = 2102963712)
    public WxVipCardDetail(Long ID, int wxVipCardID, String code, @NotNull String card_id, @NotNull String background_pic_url, String oldUserCardCode,
                           int isGiveByFriend, String friendUserName, String outerStr, int isRestoreMemberCard, String unionId, String msgType, String event,
                           String toUserName) {
        this.ID = ID;
        this.wxVipCardID = wxVipCardID;
        this.code = code;
        this.card_id = card_id;
        this.background_pic_url = background_pic_url;
        this.oldUserCardCode = oldUserCardCode;
        this.isGiveByFriend = isGiveByFriend;
        this.friendUserName = friendUserName;
        this.outerStr = outerStr;
        this.isRestoreMemberCard = isRestoreMemberCard;
        this.unionId = unionId;
        this.msgType = msgType;
        this.event = event;
        this.toUserName = toUserName;
    }

    @Generated(hash = 1836628350)
    public WxVipCardDetail() {
    }


    public String getFIELD_NAME_toUserName() {
        return "toUserName";
    }

    public int getWxVipCardID() {
        return wxVipCardID;
    }

    public void setWxVipCardID(int wxVipCardID) {
        this.wxVipCardID = wxVipCardID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getBackground_pic_url() {
        return background_pic_url;
    }

    public void setBackground_pic_url(String background_pic_url) {
        this.background_pic_url = background_pic_url;
    }

    public String getOldUserCardCode() {
        return oldUserCardCode;
    }

    public void setOldUserCardCode(String oldUserCardCode) {
        this.oldUserCardCode = oldUserCardCode;
    }

    public int getIsGiveByFriend() {
        return isGiveByFriend;
    }

    public void setIsGiveByFriend(int isGiveByFriend) {
        this.isGiveByFriend = isGiveByFriend;
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public String getOuterStr() {
        return outerStr;
    }

    public void setOuterStr(String outerStr) {
        this.outerStr = outerStr;
    }

    public int getIsRestoreMemberCard() {
        return isRestoreMemberCard;
    }

    public void setIsRestoreMemberCard(int isRestoreMemberCard) {
        this.isRestoreMemberCard = isRestoreMemberCard;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    @Override
    public String toString() {
        return "WxVipCardDetail{" +
                "ID=" + ID +
                ", wxVipCardID=" + wxVipCardID +
                ", code='" + code + '\'' +
                ", card_id='" + card_id + '\'' +
                ", background_pic_url='" + background_pic_url + '\'' +
                ", oldUserCardCode='" + oldUserCardCode + '\'' +
                ", isGiveByFriend=" + isGiveByFriend +
                ", friendUserName='" + friendUserName + '\'' +
                ", outerStr='" + outerStr + '\'' +
                ", isRestoreMemberCard=" + isRestoreMemberCard +
                ", unionId='" + unionId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", event='" + event + '\'' +
                ", toUserName='" + toUserName + '\'' +
                '}';
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {

        try {
            ID = jo.getLong(field.getFIELD_NAME_ID());
            wxVipCardID = jo.getInt(field.getFIELD_NAME_wxVipCardID());
            code = jo.getString(field.getFIELD_NAME_code());
            card_id = jo.getString(field.getFIELD_NAME_card_id());
            background_pic_url = jo.getString(field.getFIELD_NAME_background_pic_url());
            oldUserCardCode = jo.getString(field.getFIELD_NAME_oldUserCardCode());
            isGiveByFriend = jo.getInt(field.getFIELD_NAME_isGiveByFriend());
            friendUserName = jo.getString(field.getFIELD_NAME_friendUserName());
            outerStr = jo.getString(field.getFIELD_NAME_outerStr());
            isRestoreMemberCard = jo.getInt(field.getFIELD_NAME_isRestoreMemberCard());
            unionId = jo.getString(field.getFIELD_NAME_unionId());
            msgType = jo.getString(field.getFIELD_NAME_msgType());
            event = jo.getString(field.getFIELD_NAME_event());
            toUserName = jo.getString(getFIELD_NAME_toUserName());
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        WxVipCardDetail wxVipCardDetail = (WxVipCardDetail) arg0;
        if ((ignoreIDInComparision == true ? true : (wxVipCardDetail.getID() == ID && printComparator(field.getFIELD_NAME_ID())))//
                && wxVipCardDetail.getWxVipCardID() == wxVipCardID && printComparator(field.getFIELD_NAME_wxVipCardID())//
                && wxVipCardDetail.getCode().equals(code) && printComparator(field.getFIELD_NAME_code())//
                && wxVipCardDetail.getCard_id().equals(card_id) && printComparator(field.getFIELD_NAME_card_id())//
                && wxVipCardDetail.getBackground_pic_url().equals(background_pic_url) && printComparator(field.getFIELD_NAME_background_pic_url())//
                && wxVipCardDetail.getOldUserCardCode().equals(oldUserCardCode) && printComparator(field.getFIELD_NAME_oldUserCardCode())//
                && wxVipCardDetail.getIsGiveByFriend() == isGiveByFriend && printComparator(field.getFIELD_NAME_isGiveByFriend())//
                && wxVipCardDetail.getFriendUserName().equals(friendUserName) && printComparator(field.getFIELD_NAME_friendUserName())//
                && wxVipCardDetail.getOuterStr().equals(outerStr) && printComparator(field.getFIELD_NAME_outerStr())//
                && wxVipCardDetail.getIsRestoreMemberCard() == isRestoreMemberCard && printComparator(field.getFIELD_NAME_isRestoreMemberCard())//
                && wxVipCardDetail.getUnionId().equals(unionId) && printComparator(field.getFIELD_NAME_unionId())//
                && wxVipCardDetail.getMsgType().equals(msgType) && printComparator(field.getFIELD_NAME_msgType())//
                && wxVipCardDetail.getEvent().equals(event) && printComparator(field.getFIELD_NAME_event())//
                && wxVipCardDetail.getToUserName().equals(toUserName) && printComparator(field.getFIELD_NAME_toUserName())//
                ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        WxVipCardDetail wxVipCardDetail = new WxVipCardDetail();
        wxVipCardDetail.setWxVipCardID(wxVipCardID);
        wxVipCardDetail.setCode(code);
        wxVipCardDetail.setCard_id(card_id);
        wxVipCardDetail.setBackground_pic_url(background_pic_url);
        wxVipCardDetail.setOldUserCardCode(oldUserCardCode);
        wxVipCardDetail.setIsGiveByFriend(isGiveByFriend);
        wxVipCardDetail.setFriendUserName(friendUserName);
        wxVipCardDetail.setOuterStr(outerStr);
        wxVipCardDetail.setIsRestoreMemberCard(isRestoreMemberCard);
        wxVipCardDetail.setUnionId(unionId);
        wxVipCardDetail.setMsgType(msgType);
        wxVipCardDetail.setEvent(event);
        wxVipCardDetail.setToUserName(toUserName);

        return wxVipCardDetail;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

}
